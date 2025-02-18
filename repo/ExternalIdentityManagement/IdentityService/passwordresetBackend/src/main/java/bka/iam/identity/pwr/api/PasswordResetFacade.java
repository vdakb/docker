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

    File        :   PasswordResetFacade.java

    Compiler    :   Oracle JDeveloper 12c

    Purpose     :   This file implements the class
                    PasswordResetFacade.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
*/

package bka.iam.identity.pwr.api;

import bka.iam.identity.pwr.error.ServiceError;
import bka.iam.identity.pwr.error.ServiceException;
import bka.iam.identity.pwr.error.ValidationException;
import bka.iam.identity.pwr.model.PasswordReset;
import bka.iam.identity.pwr.utility.ResetUtility;
import oracle.hst.platform.core.logging.Logger;
import oracle.hst.platform.core.utility.CollectionUtility;
import oracle.hst.platform.core.utility.StringUtility;
import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.SearchKeyNotUniqueException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.exception.UserManagerException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.passwordmgmt.api.PasswordMgmtService;
import oracle.iam.passwordmgmt.vo.PasswordPolicyDescription;
import oracle.iam.passwordmgmt.vo.ValidationResult;
import oracle.iam.platform.OIMInternalClient;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * The PasswordResetFacade provides functionalities to perform the password reset operation.
 */
@Stateless(name = "passwordResetFacade")
public class PasswordResetFacade implements Serializable {

  private static final String     category         = PasswordResetFacade.class.getName();
  private static final Logger     logger           = Logger.create(category);
  /** The official serial version ID which says cryptically which version we're compatible with */
  private static final long       serialVersionUID = -4662771941841816768L;
  /** database query for searching entries */
  private static final String     PWR_LOOKUP_QUERY = "SELECT * FROM pwr_requests WHERE request_id=?";
  /** database query for deleting entries */
  private static final String     PWR_DELETE_QUERY = "DELETE FROM pwr_requests WHERE ID=?";
  /** to establish the connection to OIM system */
  private final OIMInternalClient platform         = new OIMInternalClient();
  @Resource(name = "jdbc/idsDS")
  private DataSource dataSource;

  /**
   * Lookups for a password reset request in the database by the given request id what is come from an url parameter.
   *
   * @param pwrId the identifier of the request
   * @return      a {@link PasswordReset} instance read from the database
   * @throws ServiceException in case of any unexpected behaviour
   */
  public PasswordReset lookup(final String pwrId) throws ServiceException {
    final String method = "lookup";
    logger.entering(category, method);

    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(PWR_LOOKUP_QUERY);
      statement.setString(1, pwrId);
      logger.debug(category, method, String.format("SQL: %s [%s]", PWR_LOOKUP_QUERY, pwrId));
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        PasswordReset passwordReset = new PasswordReset();
        passwordReset.id(resultSet.getLong("ID"));
        passwordReset.email(resultSet.getString("EMAIL"));
        passwordReset.created(resultSet.getObject("CREATED", LocalDateTime.class));
        passwordReset.requestId(pwrId);
        logger.debug(category, method, String.format("Request was found: %s", passwordReset));
        return passwordReset;
      } else {
        logger.warn(category, method, String.format("No request was found by ID: %s", pwrId));
        ServiceException exception = new ServiceException(ServiceError.NO_RESET_REQUEST);
        logger.throwing(category, method, exception);
        throw exception;
      }
    } catch (SQLException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.DATABASE_ERROR, e);
      logger.throwing(category, method, exception);
      throw exception;
    } finally {
      logger.exiting(category, method);
    }
  }

  /**
   * Returns the password policy rules for the actual user in a printable format.
   * @param email e-mail address which identifies the user.
   * @param locale the locale which has to be used to localize the rules.
   * @return the password policy roles for the user.
   * @throws ServiceException     in case of any unexpected behaviour
   */
  public String policyRules(final String email, final Locale locale) throws ServiceException {
    final String method = "policyRules";
    logger.entering(category, method);

    try {
      platform.loginAsAdmin();

      final UserManager               userFacade                = platform.getService(UserManager.class);
      final User                      user                      = userFacade.getDetails(UserManagerConstants.AttributeName.EMAIL.getName(), email, CollectionUtility.set(UserManagerConstants.AttributeName.USER_LOGIN.getName()));
      final PasswordMgmtService       passwordFacade            = platform.getService(PasswordMgmtService.class);
      final PasswordPolicyDescription passwordPolicyDescription = passwordFacade.getApplicablePasswordPolicyDescription(user.getLogin(), locale);

      if (passwordPolicyDescription == null) {
        return null;
      }
      return ResetUtility.prettyPrint(passwordPolicyDescription);
    } catch (LoginException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.LOGIN_FAILED, e);
      logger.throwing(category, method, exception);
      throw exception;
    } catch (UserLookupException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.USER_LOOKUP_ERROR, e);
      logger.throwing(category, method, exception);
      throw exception;
    } catch (NoSuchUserException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.NO_SUCH_USER, e, UserManagerConstants.AttributeName.EMAIL.getName(), email);
      logger.throwing(category, method, exception);
      throw exception;
    } catch (SearchKeyNotUniqueException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.EMAIL_NOT_UNIQUE, e);
      logger.throwing(category, method, exception);
      throw exception;
    } finally {
      platform.logout();
      logger.exiting(category, method);
    }
  }

  /**
   * Performs validations such as: the login and the email belong to the same user, then invokes the password change operation.
   *
   * @param userLogin the login name attribute of the user
   * @param email     the email address attribute of the user
   * @param password  the password to be set on the user
   * @throws ValidationException  in case the password violates any policy rules
   * @throws ServiceException     in case of any unexpected behaviour
   */
  public void reset(final String userLogin, final String email, final String password) throws ValidationException, ServiceException {
    final String method = "reset";
    logger.entering(category, method);

    try {
      logger.debug(category, method, "Logging in to OIM");
      platform.loginAsAdmin();

      final UserManager userFacade = platform.getService(UserManager.class);
      final User        user       = userFacade.getDetails(userLogin, CollectionUtility.set(UserManagerConstants.AttributeName.EMAIL.getName()), true);

      if (!StringUtility.equalIgnoreCase(user.getEmail(), email)) {
        logger.warn(category, method,"Email address: " + email + "doesn't belong to the user: " + userLogin);
        ServiceException exception = new ServiceException(ServiceError.LOGIN_EMAIL_MISMATCH, userLogin, email);
        logger.throwing(category, method, exception);
        throw exception;
      }

      validatePassword(userLogin, password);
      userFacade.changePassword(userLogin, password.toCharArray(), true, null,false, false);
    } catch (LoginException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.LOGIN_FAILED, e);
      logger.throwing(category, method, exception);
      throw exception;
    } catch (UserLookupException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.USER_LOOKUP_ERROR, e);
      logger.throwing(category, method, exception);
      throw exception;
    } catch (NoSuchUserException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.NO_SUCH_USER, e, UserManagerConstants.AttributeName.USER_LOGIN.getName(), userLogin);
      logger.throwing(category, method, exception);
      throw exception;
    } catch (UserManagerException e) {
      logger.error(e);
      ServiceException exception = new ServiceException(ServiceError.USER_FACADE_ERROR, e);
      logger.throwing(category, method, exception);
      throw exception;
    } finally {
      logger.debug(category, method, "Logging out from OIM");
      platform.logout();
      logger.exiting(category, method);
    }
  }

  /**
   * Validates the password against a password policy defined in OIM and assigned to the user.
   * In case of a violation happens, composes an error message what can be shown on the UI.
   *
   * @param userLogin the login name attribute of the user to be validated
   * @param password  the password to be validated against the policies
   * @throws ValidationException if the password violates any rules
   */
  private void validatePassword(final String userLogin, final String password) throws ValidationException {
    final String method = "validatePassword";
    logger.entering(category, method);

    try {
      final PasswordMgmtService passwordFacade = platform.getService(PasswordMgmtService.class);
      final ValidationResult    result         = passwordFacade.validatePasswordAgainstPolicy(password.toCharArray(), userLogin, null);

      if (!result.isPasswordValid()) {
        final String validationResult = ResetUtility.prettyPrint(result);

        logger.debug(category, method, "The password has violated the policy rules.");
        ValidationException exception = new ValidationException(validationResult);
        logger.throwing(category, method, exception);
        throw exception;
      }
    } finally {
      logger.exiting(category, method);
    }
  }

  /**
   * Deletes a password reset request from the database.
   * @param id the primary key of the entry to be deleted
   */
  public void delete(final Long id) {
    final String method = "delete";
    logger.entering(category, method);

    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(PWR_DELETE_QUERY);
      statement.setLong(1, id);
      logger.debug(category, method, String.format("SQL: %s [%d]", PWR_DELETE_QUERY, id));
      int result = statement.executeUpdate();
      logger.debug(category, method, String.format("%d record was deleted.", result));
    } catch (SQLException e) {
      logger.error(e);
      //process doesn't have to be interrupted or no message has to be shown to the user,
      // the database will be cleaned up by the scheduler
    } finally {
      logger.exiting(category, method);
    }
  }
}
