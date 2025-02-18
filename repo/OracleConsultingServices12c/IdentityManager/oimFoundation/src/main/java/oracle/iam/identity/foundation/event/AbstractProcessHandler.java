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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Provisioning Facilities

    File        :   AbstractProcessHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractProcessHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.event;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import java.io.Serializable;

import oracle.iam.platform.context.ContextAware;

import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.platform.kernel.vo.BulkOrchestration;
import oracle.iam.platform.kernel.vo.AbstractGenericOrchestration;

import oracle.iam.platform.entitymgr.vo.Entity;

import oracle.iam.platform.entitymgr.EntityManager;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractProcessHandler
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractProcessHandler</code> provide the basic implementation of
 ** common tasks a process handler needs.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 1.0.0.0
 */
abstract class AbstractProcessHandler extends AbstractEventHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the plugin point of EventHandlers to lookup the metadata */
  protected static final String EVENT  = "oracle.iam.platform.kernel.spi.EventHandler";

  /** the operational code of a Create Event */
	protected static final String CREATE = "CREATE";

  /** the operational code of a Modify Event */
	protected static final String MODIFY = "MODIFY";

  /** the operational code of a Delete Event */
	protected static final String DELETE = "DELETE";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractProcessHandler</code> which use the specified
   ** category for logging purpose.
   **
   ** @param  loggerCategory     the category for the Logger.
   */
  protected AbstractProcessHandler(final String loggerCategory) {
    // ensure inheritance
    super(loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   storeEventData
	/**
	 ** Stores the given attribute name and value in the
   ** <code>InterEventData</code> data structure of the orchestration.
   ** <p>
   ** InterEventData is used for communication of data between event handlers
   **
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestartion parameters or operation.
	 ** @param  attributeName      the name of the attribute to store in the
   **                            process data mapping.
	 ** @param  attributeValue     the value for <code>attributeName</code> to
   **                            store in the orchestration process mapping.
	 */
	public static void storeEventData(final AbstractGenericOrchestration orchestration, final String attributeName, final String attributeValue) {
		HashMap<String, Serializable> eventData = orchestration.getInterEventData();
		if (eventData == null) {
			eventData = new HashMap<String, Serializable>();
			orchestration.setInterEventData(eventData);
		}
		eventData.put(attributeName, attributeValue);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchEventData
	/**
	 ** Retrieves the value of the given attribute <code>attributeName</code> from
   ** the <code>InterEventData</code> data structure of the orchestration.
   **
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestartion parameters or operation.
	 ** @param  attributeName      the name of the attribute to obtain from the
   **                            orchestration process data mapping.
   **
   ** @return                    the value for <code>attributeName</code>
   **                            obtained from the orchestration process
   **                            mapping.
	 */
	public static String fetchEventData(final AbstractGenericOrchestration orchestration, final String attributeName) {
		final HashMap<String, Serializable> interEventData = orchestration.getInterEventData();
    return (interEventData == null) ? null : interEventData.get(attributeName).toString();
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchOrchestrationValue
	/**
   ** Retrieves the value of the given attribute <code>attributeName</code> for
   ** the entity being created/modified.
   ** <p>
   ** It checks if a value has been provided as part of the operation being
   ** performed for <code>attributeName</code> (i.e. in
   ** <code>orchestration</code>). If not, then it retrieves the value of
   ** <code>attributeName</code> from the data store.
   **
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestartion parameters or operation.
	 ** @param  attributeName      the name of the attribute to obtain from the
   **                            orchestration data mapping.
   **
   ** @return                    the value for <code>attributeName</code>
   **                            obtained from the orchestration data mapping.
   **
   ** @throws Exception          if the entity for <code>type</code> is not
   **                            or if <code>attributeName</code> is not defined
   **                            for the entity type <code>type</code>.
   */
  public static String fetchOrchestrationValue(final Orchestration orchestration, final String attributeName)
    throws Exception {

    String attributeValue = fetchOrchestrationData(orchestration, attributeName);
		if (attributeValue == null && !CREATE.equals(orchestration.getOperation()))
			attributeValue = fetchDataStoreValue(orchestration.getTarget().getType(), orchestration.getTarget().getEntityId(), attributeName);

		return attributeValue;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchOrchestrationValue
	/**
	 ** Retrieves the value of the given attribute <code>attributeName</code> for
   ** all entities in the batch.
   ** <p>
   ** It checks if a value has been provided as part of the operation being
   ** performed for <code>attributeName</code> (i.e. in
   ** <code>orchestration</code>). If not, then it retrieves the value of
   ** <code>attributeName</code> from the data store for all entities in the
   ** batch.
   ** <p>
   ** <b>NOTE</b>:
   ** <br>
   ** If value of <code>attributeName</code> has been provided as part of the
   ** operation being performed then it applies to all users in the batch.
   **
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestartion parameters or operation.
	 ** @param  attributeName      the name of the attribute to obtain from the
   **                            orchestration data mapping.
   **
   ** @return                    the value for <code>attributeName</code>
   **                            obtained from the orchestration data mapping.
   **
   ** @throws Exception          if the entity for <code>type</code> is not
   **                            or if <code>attributeName</code> is not defined
   **                            for the entity type <code>type</code>.
	 */
	public static Map<String, String> fetchOrchestrationValue(final BulkOrchestration orchestration, final String attributeName)
		throws Exception {

		final HashMap<String, String> attributeValues = new HashMap<String, String>();
		final String[]                entityIDs       = orchestration.getTarget().getAllEntityId();

		String attributeValue = fetchOrchestrationData(orchestration, attributeName);
		if (attributeValue != null) {
			for (int i = 0; i < entityIDs.length; i++)
				attributeValues.put(entityIDs[i], attributeValue);
		}
    else if (!CREATE.equals(orchestration.getOperation())){
			for (int i = 0; i < entityIDs.length; i++) {
				attributeValue = fetchDataStoreValue(orchestration.getTarget().getType(), entityIDs[i], attributeName);
				attributeValues.put(entityIDs[i], attributeValue);
			}
		}

		return attributeValues;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchOrchestrationData
	/**
	 ** Retrieves the value of the given attribute <code>attributeName</code> from
   ** the orchestration parameters.
   **
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestartion parameters or operation.
	 ** @param  attributeName      the name of the attribute to obtain from the
   **                            orchestration data mapping.
   **
   ** @return                    the value for <code>attributeName</code>
   **                            obtained from the orchestration data mapping.
	 */
	public static String fetchOrchestrationData(final AbstractGenericOrchestration orchestration, final String attributeName) {
    return fetchOrchestrationData(orchestration.getParameters(), attributeName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchOrchestrationData
  /**
   ** Retrieves the value of the given attribute <code>attributeName</code> from
   ** the orchestration data mapping.
   ** <br>
   ** {@link ContextAware} object is obtained when the actor is a regular user.
   ** <br>
   ** If the actor is an administrator, the exact value of the attribute is
   ** obtained.
   **
   ** @param  parameters         the orchestration data mapping of a proccess.
	 ** @param  attributeName      the name of the attribute to obtain from the
   **                            orchestration data mapping.
   **
   ** @return                    the value for <code>attributeName</code>
   **                            obtained from the orchestration data mapping.
   */
  public static String fetchOrchestrationData(final Map<String, Serializable> parameters, final String attributeName) {
    Object attributeValue = parameters.get(attributeName);
    if (attributeValue != null)
      attributeValue = (attributeValue instanceof ContextAware) ? ((ContextAware)attributeValue).getObjectValue() : attributeValue;

    return (attributeValue == null) ? null : attributeValue.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchDataStoreValue
	/**
   ** Retrieves the value of the given attribute <code>attributeName</code> from
   ** the data store for the given entity.
   **
   ** @param  type               the type of the entity.
   ** @param  entityID           the identifier of an entity in the namespace of
   **                            <code>type</code>
	 ** @param  attributeName      the name of the attribute to obtain from the
   **                            entity.
   **
   ** @return                    the value for <code>attributeName</code>
   **                            obtained from the entity.
   **
   ** @throws Exception          if the entity for <code>type</code> is not
   **                            or if <code>attributeName</code> is not defined
   **                            for the entity type <code>type</code>.
   */
  public static String fetchDataStoreValue(final String type, final String entityID, final String attributeName)
    throws Exception {

    final Set<String> returning = new HashSet<String>();
		returning.add(attributeName);
		final Entity details        = service(EntityManager.class).findEntity(type, entityID, returning);
		final Object attributeValue = details.getAttribute(attributeName);

		return attributeValue != null ? attributeValue.toString() : null;
	}
}