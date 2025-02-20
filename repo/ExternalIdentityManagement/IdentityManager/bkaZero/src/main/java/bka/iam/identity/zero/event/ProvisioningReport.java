/*
    Oracle Deutschland GmbH

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

    Copyright 2023 All Rights reserved.

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Zero Provisioning

    File        :   ProvisioningReport.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class
                    ProvisioningReport.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2023-11-09  Sbernet     First release version
*/
package bka.iam.identity.zero.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import oracle.hst.platform.core.utility.ListResourceBundle;

import oracle.iam.service.api.RequestBundle;
import oracle.iam.service.api.RequestMessage;
////////////////////////////////////////////////////////////////////////////////
// class ProvisioningTask
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The <code>ProvioningReport</code> holds <code>RequestMessage</code> for each
 ** accounts that has been created, modified or revoked.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ProvisioningReport {
  
  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Action
  // ~~~~ ~~~~~~~~~
  /**
   ** The list of action supported by <code>ProvisioningReport</code>.
   */
  public static enum Action {
    /** The encoded action values that can by applied on repport. */
    CREATE("create"),
    MODIFY("modify"),
    DELETE("delete");
    
    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The human readable state value for this <code>Action</code>. */
    public final String id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Action</code> with a single state.
     **
     ** @param  id               the human readable state value for this
     **                          <code>Action</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    Action(final String id) {
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Action</code> from the given
     ** <code>id</code> value.
     **
     ** @param  id               the string value the <code>Action</code> should
     **                          be returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Action</code> mapped at
     **                          <code>id</code>.
     **                          <br>
     **                          Possible object is <code>Action</code>.
     **
     ** @throws IllegalArgumentException if the given <code>id</code> is not
     **                                  mapped to an <code>Action</code>.
     */
    public static Action from(final String id) {
      for (Action cursor : Action.values()) {
        if (cursor.id.equals(id))
          return cursor;
      }
      throw new IllegalArgumentException(id);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  /**
   ** The map that holds the status for each account creation request.
   */
  final private Map<String, List<RequestMessage>> accountCreate;
  /**
   ** The map that holds the status for each account modification request.
   */
  final private Map<String, List<RequestMessage>> accountModify;
  /**
   ** The map that holds the status for each account revoke request.
   */
  final private Map<String, List<RequestMessage>> accountRevoke;
  
  ////////////////////////////////////////////////////////////////////////////
  // Constructors
  ////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructs an empty <code>ProvioningReport</code> instance that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
  */
  public ProvisioningReport() {
    super();
    
    this.accountCreate    = new HashMap<String, List<RequestMessage>>();
    this.accountModify   = new HashMap<String, List<RequestMessage>>();
    this.accountRevoke    = new HashMap<String, List<RequestMessage>>();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: add
  /**
   ** Insert the list of <code>RequestMessage</code> belonging the provided
   ** accountID for the create request.
   **
   **
   ** @param  action             the type of action operate on the report.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  accountID          the account identifier.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  messages           the list of <code>RequestMessage</code>.
   **                            <br>
   **                            Allowed object is {@link List}.ju
   */
  public void add(final Action action, final String accountID, final List<RequestMessage> messages) {
    switch(action) {
      case CREATE:
        accountCreate.put(accountID, messages);
        break;
      case MODIFY:
        accountModify.put(accountID, messages);
        break;
       case DELETE:
        accountRevoke.put(accountID, messages);
        break;
      default:
        break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: getStatus
  /**
   ** Return the translated  for the provided
   ** operation.
   **
   **
   ** @param  action             the type of action operate on the report.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  locale             the {@link Locale} for which a resource is
   **                            desired.
   **                            <br>
   **                            Allowed object is {@link Locale}.
   ** 
   ** @return                    the map of translated
   **                            <code>RequestMessage</code> for the provided
   **                            operation.
   */
  public Map<String, List<String>> getStatus(final Action action, final Locale locale) {
    switch(action) {
      case CREATE:
        return getFormattedAccountStatus(locale, accountCreate);
      case MODIFY:
        return getFormattedAccountStatus(locale, accountModify);
       case DELETE:
        return getFormattedAccountStatus(locale, accountRevoke);
      default:
        return null;
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getLocalResourceBundle
  /**
   ** Returns the translated status {@link Map} from the provided locale and 
   ** the map that holds the status.
   **
   ** @param  locale             the {@link Locale} for which a resource is
   **                            desired.
   ** @param  map                the map that holds the status.
   **                            <br>
   **                            Allowed object is {@link Map}.
   ** 
   ** @return                    the translated status map.
   */
  private Map<String, List<String>> getFormattedAccountStatus(final Locale locale, final Map<String, List<RequestMessage>> map) {
    final Map<String, List<String>> formatedMessage = new HashMap<String, List<String>>();
    
    for (Map.Entry<String, List<RequestMessage>> entry : map.entrySet()) {
      final String identifier    = entry.getKey();
      final List<String> msgList = new ArrayList<String>();
      for (RequestMessage message : entry.getValue()) {
        final ListResourceBundle bundle = getLocalResourceBundle(RequestBundle.class, locale);
        msgList.add(bundle.formatted(message.code(), message.parameters()));
      }
      formatedMessage.put(identifier, msgList);
    }
    
    return formatedMessage;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: isEmpty
  /**
   ** Returns <code>True</code> if the report contains no
   ** <code>RequestMessage</code>. False otherwise.
   **
   **
   ** @return                     true is report is empty. False otherwise.
   */
  public boolean isEmpty() {
    return accountCreate.isEmpty() && accountModify.isEmpty() && accountRevoke.isEmpty();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getLocalResourceBundle
  /**
   ** Return {@link ListResourceBundle} with the provided locale, and class
   ** loader.
   **
   ** @param  clazz              the class loader from which to load the
   **                            resource bundle
   ** @param  locale             the {@link Locale} for which a resource is
   **                            desired.
   ** 
   ** @return                    a resource bundle for the given class lodaer
   **                            and locale.
   */
  private ListResourceBundle getLocalResourceBundle(final Class clazz, final Locale locale) {
    return (ListResourceBundle)ResourceBundle.getBundle(clazz.getName(), locale.getDefault(), clazz.getClassLoader());
  }  
}
