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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Google Drupal Connector

    File        :   AccountProvisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com based on work of dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AccountProvisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.gws.service.provisioning.drupal;

import java.util.Map;

import org.identityconnectors.framework.common.objects.ObjectClass;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.object.Pair;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class AccountProvisioning
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AccountProvisioning</code> acts as the service end point for
 ** Identity Manager to provision account properties to a Service Provider.
 ** <br>
 ** This wrapper class has methods for account operations like create account,
 ** modify account, delete account etc.
 **
 ** @author  adrien.farkas@oracle.com based on work of dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccountProvisioning extends Service {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////



  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccountProvisioning</code> task adapter.
   **
   ** @param  provider           the session provider connection.
   **                            <br>
   **                            Allowed object is {@link tcDataProvider}.
   ** @param  processInstance    the Process Instance Key providing the data for
   **                            the provisioning tasks.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  processTask        the Process Task Key providing the data for
   **                            the provisioning tasks.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public AccountProvisioning(final tcDataProvider provider, final Long processInstance, final Integer processTask) {
    // ensure inheritance
    super(provider, processInstance, processTask);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Create an <code>Account</code> in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String create(final Long resourceInstance) {
    return super.createEntry(ObjectClass.ACCOUNT, resourceInstance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rename
  /**
   ** Renames an <code>Account</code> in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  origin             the value of the unique name attribute before
   **                            the event happend.
   **                            <br>
   **                            This is used to evaluate the correct identifier
   **                            passed as the UID to the connector.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  attributeFilter    the name of the attribute to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String rename(final Long resourceInstance, final String origin, final String attributeFilter) {
    this.origin = origin;
    return modify(resourceInstance, attributeFilter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an <code>Account</code> entirely in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  attribute          the name(s) and the values of the attribute(s)
   **                            to modify.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element consist as a {@link String} for the key
   **                            and an {@link Object} as its value.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String modify(final Long resourceInstance, final Map<String, Object> attribute) {
    // this hack is required due to Apigee is expecting the complete set of
    // attributes on update; unfortunately the bulk update in Idetity Manager
    // populates the given map with the update attributes only hence we need to
    // merge the missing values
    this.data.entrySet().stream().filter(
      (entry) -> (attribute.get(entry.getKey()) == null)).forEachOrdered((entry) -> {
        attribute.put(entry.getKey(), entry.getValue());
      }
    );
    return super.modifyAttribute(ObjectClass.ACCOUNT, resourceInstance, attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies one or more attribute values of an <code>Account</code> in the
   ** Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  attributeFilter    the name of the attribute to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String modify(final Long resourceInstance, final String attributeFilter) {
    return super.modifyAttribute(ObjectClass.ACCOUNT, resourceInstance, attributeFilter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   activate
  /**
   ** Activates an deactivated <code>Account</code> in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String activate(final Long resourceInstance) {
    return super.activateEntry(ObjectClass.ACCOUNT, resourceInstance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deactivate
  /**
   ** Deactivates an activated <code>Account</code> in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String deactivate(final Long resourceInstance) {
    return super.deactivateEntry(ObjectClass.ACCOUNT, resourceInstance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Delete an <code>Account</code> in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String delete(final Long resourceInstance) {
    return super.deleteEntry(ObjectClass.ACCOUNT, resourceInstance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assign
  /**
   ** Assign an <code>Account</code> to an entity in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String assign(final Long resourceInstance) {
    return assignPermission(ObjectClass.ACCOUNT, resourceInstance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revoke
  /**
   ** Revoke an <code>Account</code> from an entity in the Service Provider.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String revoke(final Long resourceInstance) {
    return revokePermission(ObjectClass.ACCOUNT, resourceInstance);
  }
}