/*
    Oracle Deutschland BV & Co. KG

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Password Reset Administration

    File        :   PasswordForgotFacade.java

    Compiler    :   Oracle JDeveloper 12c

    Purpose     :   This file implements the class
                    PasswordForgotFacade.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
*/

package bka.iam.identity.pwr.api;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Operations.tcLookupOperationsIntf;
import bka.iam.identity.pwr.error.ServiceError;
import bka.iam.identity.pwr.error.ServiceException;
import bka.iam.identity.pwr.model.PasswordReset;
import bka.iam.identity.pwr.utility.ResetUtility;
import oracle.hst.platform.core.logging.Logger;
import oracle.hst.platform.core.utility.CollectionUtility;
import oracle.hst.platform.core.utility.StringUtility;
import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.SearchKeyNotUniqueException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.notification.api.NotificationService;
import oracle.iam.notification.exception.*;
import oracle.iam.notification.vo.NotificationEvent;
import oracle.iam.platform.OIMInternalClient;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * The PasswordForgotFacade provides functionalities to perform the email sending operation.
 */
@Stateless(name = "passwordForgotFacade")
public class PasswordForgotFacade implements Serializable {

  private static final String     category         = PasswordForgotFacade.class.getName();
  private static final Logger     logger           = Logger.create(category);
  /** The official serial version ID which says cryptically which version we're compatible with */
  private static final long       serialVersionUID = 5205896688192403090L;
  /** Name of the template deployed in OIM to be sent */
  private static final String     TEMPLATE         = "bka-external-password-reset";
  /** Keyword of a lookup configuration deployed on OIM containing the callback url for pw reset */
  private static final String     CALLBACK_URL     = "OIM.PasswordReset.Callback";
  /** Key of a lookup value deployed on OIM for default callback configuration. */
  private static final String     DEFAULT_HOST     = "default";
  /** database query for saving entries */
  private static final String     PWR_SAVE_QUERY   = "INSERT INTO pwr_requests (id,request_id,email,created) VALUES (pwr_req_seq.nextval,?,?,?)";
  /** to establish the connection to OIM system */
  private final OIMInternalClient platform         = new OIMInternalClient();

  @Resource(name = "jdbc/idsDS")
  private DataSource dataSource;

  /**
   * Initializes a password reset request and performs a notification to the given e-mail address.
   *
   * @param emailAddress the recipient address to send the notification
   * @param host         hostname header value for resolving callback url
   * @return true if the notification was successfully sent, otherwise false
   * @throws ServiceException in case of any unexpected behaviour
   */
  public boolean requestPasswordReset(final String emailAddress, final String host) throws ServiceException {
    final String method = "requestPasswordReset";
    logger.entering(category, method);

    try {
      final PasswordReset passwordReset = new PasswordReset();
      passwordReset.email(emailAddress);
      passwordReset.requestId(ResetUtility.uuid());
      passwordReset.created(LocalDateTime.now());

      save(passwordReset);

      logger.debug(category, method, "Logging in to OIM");
      platform.loginAsAdmin();

      return notify(passwordReset, host);
    } catch (LoginException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.LOGIN_FAILED, e);
      logger.throwing(category, method, exception);
      throw exception;
    } finally {
      logger.debug(category, method, "Logging out from OIM");
      platform.logout();
      logger.exiting(category, method);
    }
  }

  /**
   * Initializes {@link NotificationEvent} instance and sets its parameters, then performs the event.
   * @param passwordReset a {@link PasswordReset} instance contains notification parameters
   * @param host          hostname header value for resolving callback url
   * @return true if the notification was successfully sent, otherwise false
   * @throws ServiceException in case of any unexpected behaviour
   */
  private boolean notify(final PasswordReset passwordReset, final String host) throws ServiceException {
    final String method = "notify";
    logger.entering(category, method);

    try {
      final NotificationService notificationFacade = platform.getService(NotificationService.class);

      final String recipient                       = lookupUser(passwordReset.email());
      final Map<String, Object> params             = new HashMap<>();
      params.put("url",                            ResetUtility.urlParam(url(host), passwordReset.requestId()));
      params.put("expire",                         passwordReset.created().plusMinutes(10));
      params.put("userLogin",                      recipient);

      final NotificationEvent event                = new NotificationEvent();
      event.setUserIds(new String[]{recipient});
      event.setSender(null);
      event.setTemplateName(TEMPLATE);
      event.setParams((HashMap<String, Object>) params);

      logger.debug(category, method, String.format("Sending password reset notification to %s, ID: %s", recipient, passwordReset.requestId()));
      boolean result = notificationFacade.notify(event);
      logger.debug(category, method, String.format("Password reset notification %s was successfully sent: %s", passwordReset.requestId(), result));
      return result;
    } catch (EventException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.NOTIFICATION_FAILED, e);
      logger.throwing(category, method, exception);
      throw exception;
    } catch (UnresolvedNotificationDataException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.NOTIFICATION_UNRESOLVED_DATA, e);
      logger.throwing(category, method, exception);
      throw exception;
    } catch (TemplateNotFoundException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.NOTIFICATION_TEMPLATE_NOTFOUND, e);
      logger.throwing(category, method, exception);
      throw exception;
    } catch (MultipleTemplateException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.NOTIFICATION_TEMPLATE_AMBIGUOUS, e);
      logger.throwing(category, method, exception);
      throw exception;
    } catch (NotificationResolverNotFoundException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.NOTIFICATION_RESOLVER_NOTFOUND, e);
      logger.throwing(category, method, exception);
      throw exception;
    } catch (UserDetailsNotFoundException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.NOTIFICATION_IDENTITY_NOTFOUND, e);
      logger.throwing(category, method, exception);
      throw exception;
    } catch (NotificationException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.NOTIFICATION_EXCEPTION, e);
      logger.throwing(category, method, exception);
      throw exception;
    } catch (URISyntaxException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.URI_SYNTAX, e);
      logger.throwing(category, method, exception);
      throw exception;
    } finally {
      logger.exiting(category, method);
    }
  }

  /**
   * Reads the callback url as an OIM lookup value and converts it into an {@link URL}.
   * @param host hostname header value for resolving callback url
   * @return the callback url from OIM
   * @throws ServiceException in case of any unexpected behaviour
   */
  private URL url(final String host) throws ServiceException {
    final String method = "url";
    logger.entering(category, method);

    try {
      String                       property     = null;
      final tcLookupOperationsIntf lookupFacade = platform.getService(tcLookupOperationsIntf.class);

      if (!StringUtility.empty(host)) {
        logger.debug(category, method, "Searching for lookup value: [" + CALLBACK_URL + " - " + host.toLowerCase() + "]");
        property = lookupFacade.getDecodedValueForEncodedValue(CALLBACK_URL, host.toLowerCase());
      }
      if (StringUtility.empty(property)) {
        logger.debug(category, method, "Searching for default lookup value: [" + CALLBACK_URL + " - " + DEFAULT_HOST + "]");
        property = lookupFacade.getDecodedValueForEncodedValue(CALLBACK_URL, DEFAULT_HOST);
        if (StringUtility.empty(property)) {
          logger.error(category, method, "[" + CALLBACK_URL + " - " + DEFAULT_HOST + "] Lookup value doesn't exist.");
          ServiceException exception = new ServiceException(ServiceError.NO_SUCH_PROPERTY, CALLBACK_URL);
          logger.throwing(category, method, exception);
          throw exception;
        }
      }
      return new URL(property);
    } catch (MalformedURLException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.URL_FORMAT_ERROR, e, CALLBACK_URL);
      logger.throwing(category, method, exception);
      throw exception;
    } catch (tcAPIException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.LOOKUP_OPERATION_ERROR, e, CALLBACK_URL);
      logger.throwing(category, method, exception);
      throw exception;
    } finally {
      logger.exiting(category, method);
    }
  }

  /**
   * Lookups a user by the given e-mail address.
   *
   * @param emailAddress an attribute of the user
   * @return the login name of the user
   * @throws ServiceException in case of any unexpected behaviour
   */
  private String lookupUser(final String emailAddress) throws ServiceException {
    final String method = "lookupUser";
    logger.entering(category, method);

    try {
      final UserManager userFacade = platform.getService(UserManager.class);
      final User        user       = userFacade.getDetails(UserManagerConstants.AttributeName.EMAIL.getName(), emailAddress, CollectionUtility.set(UserManagerConstants.AttributeName.USER_LOGIN.getName()));
      return user.getLogin();
    } catch (NoSuchUserException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.NO_SUCH_USER, e, UserManagerConstants.AttributeName.EMAIL.getName(), emailAddress);
      logger.throwing(category, method, exception);
      throw exception;
    } catch (UserLookupException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.USER_LOOKUP_ERROR, e);
      logger.throwing(category, method, exception);
      throw exception;
    } catch (SearchKeyNotUniqueException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.EMAIL_NOT_UNIQUE, e);
      logger.throwing(category, method, exception);
      throw exception;
    } finally {
      logger.exiting(category, method);
    }
  }

  /**
   * Persists a password reset request to the database.
   *
   * @param passwordReset a {@link PasswordReset} instance to persist
   * @throws ServiceException in case of any unexpected behaviour
   */
  private void save(final PasswordReset passwordReset) throws ServiceException {
    final String method = "save";
    logger.entering(category, method);

    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(PWR_SAVE_QUERY);
      statement.setString(1, passwordReset.requestId());
      statement.setString(2, passwordReset.email());
      statement.setObject(3, passwordReset.created());

      logger.debug(category, method, String.format("SQL: %s [%s, %s, %s]", PWR_SAVE_QUERY, passwordReset.requestId(), passwordReset.email(), passwordReset.created()));
      int affectedRows = statement.executeUpdate();
      if (affectedRows == 0) {
        ServiceException exception = new ServiceException(ServiceError.PERSIST_NOT_SUCCEEDED);
        logger.throwing(category, method, exception);
        throw exception;
      }
      logger.debug(category, method, String.format("Password reset request saved: %s", passwordReset));
    } catch (SQLException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.PERSIST_ERROR, e);
      logger.throwing(category, method, exception);
      throw exception;
    } finally {
      logger.exiting(category, method);
    }
  }
}
