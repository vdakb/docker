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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Adapter Shared Library
    Subsystem   :   Common Shared Adapter

    File        :   BulkAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    BulkAdapter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.adapter.spi;

import java.util.Map;

import Thor.API.Operations.tcFormInstanceOperationsIntf;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.CollectionUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.GenericAdapter;
import oracle.iam.identity.foundation.AttributeMapping;

////////////////////////////////////////////////////////////////////////////////
// class BulkAdapter
// ~~~~~ ~~~~~~~~~~~
/**
 ** Oracle Identity Manager provides capability to copy the data from user form
 ** to the process forms of the resource objects. This is accomplished by
 ** defining a mapping between the USR table column and the process task that
 ** should be invoked in <code>Lookup.USR_PROCESS_TRIGGERS</code> lookup.
 ** <p>
 ** In example adding a mapping for:
 ** <pre>
 **   <code>
 **   Code Key        Decode 
 **   -------------   ----------------------  
 **   USR_MOBILE      Change Mobile
 **   </code>
 ** </pre>
 ** will cause the <code>Change Mobile</code> process task to be executed for
 ** each provisioning process the task is defined when the <code>Mobile</code>
 ** (USR_MOBILE) field for the OIM User is updated. The
 ** <code>Change Mobile</code> process task needs to update the resource objects
 ** process form with a new value which will trigger the process task that
 ** actually updates the target system with the new value. Normally, if there
 ** are not that many fields defined to be synchronized between the User form
 ** and the resource objects, this works fine and will not cause performance
 ** problems or load peaks in the target system. However if there are a lot of
 ** fields that needs to be synchronized and there are a lot of changes made for
 ** the OIM users on daily basis the customers might see some performance issues
 ** as each update is made in a single modify operation to the target system.
 ** <p>
 ** Oracle Identity Manager version 11.1.1.5 introduced a new feature called
 ** <b>Bulk Attribute Propagation</b> where the multiple updates on User form
 ** could be moved to process form in a single operation and the thus the
 ** multiple update/modify calls to the target resource can be avoided.
 ** <p>
 ** The adapter expects three variables:
 ** <pre>
 **   <code>
 **   Name                  Type       Map To
 **   --------------------  ---------- ------------------------
 **   processInstance       String     Resolve at runtime
 **   attributeMapping      String     Resolve at runtime
 **   data                  Map        Resolve at runtime
 **   </code>
 ** </pre>
 ** To use this feature you need to:
 ** <ol>
 **   <li>Create a lookup with name a name of your choice to hold a mapping
 **       between USR table columns and the resources UDF columns.
 **       <br>
 **       The adapter code uses this lookup to map the change on User form to
 **       the UDF column.
 **   <li>Add a conditional process task to the provision process of each
 **       resource object the bulk updates should be propagated to. In example:
 **       <pre>
 **       Process task name: Bulk Update Process Form
 **       Mapped to:         Adapter provided by this implementation
 **       Adapter Variables mapped as:
 **         processInstance : Process Data    -&gt; Process Instance
 **         attributeMapping: Literal         -&gt; Lookup Definition Name
 **         data            : User Definition -&gt; Bulk Changes
 **       </pre>
 **   <li>Make sure the <b>&lt;UD table name&gt; Updated</b> process task exists
 **       for the resource and it's able to propagate all changes to the target
 **       system.
 **       <br>
 **       OIM will automatically search for this process task and execute it if
 **       there are multiple updates made to the process form data at the same
 **       time.
 **       <br>
 **       For new ICF based connectors this process task automatically exists.
 **   <li>Map the "BULK" keyword to the process task name that should be
 **       executed on bulk update of User form. In example:
 **       <pre>
 **         Code Key       Decode
 **         -------------  ----------------------
 **         BULK           Bulk Update Process Form
 **       </pre>
 ** </ol>
 ** After creating the components, when multiple User attributes are changed for
 ** some user OIM will check the <code>Lookup.USR_PROCESS_TRIGGERS</code> lookup
 ** for the process task name mapped to "BULK" keyword and executed the process
 ** task for each provisioning process the process task exists. The adapter will
 ** update the process form which will trigger the
 ** <b>&lt;UDF name&gt; Updated</b> task propagating all the changes to the
 ** target system in one modify/update call.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class BulkAdapter extends GenericAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>BulkAdapter</code> task adpater that allows
   ** use as  a JavaBean.
   **
   ** @param  provider           the session provider connection
   */
  public BulkAdapter(final tcDataProvider provider) {
    // ensure inheritance
    super(provider);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>BulkAdapter</code> task adpater that
   ** allows use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   ** @param  loggerCategory     the category of the logging facility under
   **                            which the log messages will be spooled to the
   **                            output devices.
   */
  public BulkAdapter(final tcDataProvider provider, final String loggerCategory) {
    // ensure inheritance
    super(provider, loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateProcess
  /**
   ** Oracle Identity Manager provides capability to copy the data from user
   ** form to the process forms of the resource objects. This is accomplished by
   ** defining a mapping between the USR table column and the process task that
   ** should be invoked in <code>Lookup.USR_PROCESS_TRIGGERS</code> lookup.
   ** <p>
   ** In example adding a mapping for:
   ** <pre>
   **   <code>
   **   Code Key        Decode 
   **   -------------   ----------------------  
   **   USR_MOBILE      Change Mobile
   **   </code>
   ** </pre>
   ** will cause the <code>Change Mobile</code> process task to be executed for
   ** each provisioning process the task is defined when the <code>Mobile</code>
   ** (USR_MOBILE) field for the OIM User is updated. The
   ** <code>Change Mobile</code> process task needs to update the resource
   ** objects process form with a new value which will trigger the process task
   ** that actually updates the target system with the new value. Normally, if
   ** there are not that many fields defined to be synchronized between the User
   ** form and the resource objects, this works fine and will not cause
   ** performance problems or load peaks in the target system. However if there
   ** are a lot of fields that needs to be synchronized and there are a lot of
   ** changes made for the OIM users on daily basis the customers might see some
   ** performance issues as each update is made in a single modify operation to
   ** the target system.
   ** <p>
   ** Oracle Identity Manager version 11.1.1.5 introduced a new feature called
   ** <b>Bulk Attribute Propagation</b> where the multiple updates on User form
   ** could be moved to process form in a single operation and the thus the
   ** multiple update/modify calls to the target resource can be avoided.
   ** <p>
   ** The adapter expects three variables:
   ** <pre>
   **   <code>
   **   Name                  Type       Map To
   **   --------------------  ---------- ------------------------
   **   processInstance       String     Resolve at runtime
   **   attributeMapping      String     Resolve at runtime
   **   data                  Map        Resolve at runtime
   **   </code>
   ** </pre>
   ** To use this feature you need to:
   ** <ol>
   **   <li>Create a lookup with name a name of your choice to hold a mapping
   **       between USR table columns and the resources UDF columns.
   **       <br>
   **       The adapter code uses this lookup to map the change on User form
   **       to the UD table column.
   **   <li>Add a conditional process task to the provision process of each
   **       resource object the bulk updates should be propagated to. In
   **       example:
   **       <pre>
   **       Process task name: Bulk Update Process Form
   **       Mapped to:         Adapter provided by this implementation
   **       Adapter Variables mapped as:
   **         processInstance : Process Data    -&gt; Process Instance
   **         attributeMapping: Literal         -&gt; Lookup Definition Name
   **         data            : User Definition -&gt; Bulk Changes
   **       </pre>
   **   <li>Make sure the <b>&lt;UD table name&gt; Updated</b> process task
   **       exists for the resource and it's able to propagate all changes to
   **       the target system.
   **       <br>
   **       OIM will automatically search for this process task and execute it
   **       if there are multiple updates made to the process form data at the
   **       same time.
   **       <br>
   **       For new ICF based connectors this process task automatically exists.
   **   <li>Map the "BULK" keyword to the process task name that should be
   **       executed on bulk update of User form. In example:
   **       <pre>
   **         Code Key       Decode
   **         -------------  ----------------------
   **         BULK           Bulk Update Process Form
   **       </pre>
   ** </ol>
   ** After creating the components, when multiple User attributes are changed
   ** for some user OIM will check the <code>Lookup.USR_PROCESS_TRIGGERS</code>
   ** lookup for the process task name mapped to "BULK" keyword and executed the
   ** process task for each provisioning process the process task exists. The
   ** custom adapter (created here) will update the process form which will
   ** trigger the <b>&lt;UDF name&gt; Updated</b> task propagating all the
   ** changes to the target system in one modify/update call.
   **
   ** @param  processInstance    the <code>Process Instance</code> providing the
   **                            data of an account model.
   ** @param  attributeMapping   the name of the <code>Lookup Definition</code>
   **                            specifying the Mapping of user form fields and
   **                            their transformation that part of a
   **                            provisioning task.
   ** @param  identityData       the changed values of an identity entity like
   **                            user, organization or role etc.
   **
   ** @return                    an appropriate response code.
   **
   ** @throws TaskException      if the Lookup Definition is not defined in the
   **                            Oracle Identity Manager meta entries or one or
   **                            more attributes are missing on the
   **                            Lookup Definition.
   */
  public String updateProcess(final Long processInstance, final String attributeMapping, final Map<String, Object> identityData)
    throws TaskException {

    final String method = "updateProcess";
    trace(method, SystemMessage.METHOD_ENTRY);

    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    // the Lookup containing the mappings between USR table and the UD table.
    // for flexibility this will be provided as adapter variable
    final AttributeMapping lookup = new AttributeMapping(this, attributeMapping);
    debug(method, String.format("Bulk data containing the identity changes:\n%s", StringUtility.formatCollection(identityData)));
    if (!CollectionUtility.empty(lookup)) {
      // loop through the USR table changes and add the values to the process
      // form data
      final Map<String, Object> processData = lookup.filterByEncoded(identityData);
      debug(method, String.format("Bulk data containing the process changes:\n%s", StringUtility.formatCollection(processData)));
      // obtain instances of tcFormInstanceOperationsIntf API's
      tcFormInstanceOperationsIntf formInstanceFacade = formInstanceFacade();
      try {
        // update the process form with the new data
        formInstanceFacade.setProcessFormData(processInstance.longValue(), processData);
      }
      catch (Exception e) {
        responseCode = FAILURE;
      }
      finally {
        formInstanceFacade.close();
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }
}