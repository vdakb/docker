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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   Provisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Provisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.provisioning;

import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLFormat;
import oracle.hst.foundation.xml.XMLOutputNode;
import oracle.hst.foundation.xml.XMLProcessor;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.offline.Entity;
import oracle.iam.identity.foundation.offline.EntityListener;

import oracle.iam.identity.utility.file.XMLEntityFactory;

import oracle.iam.identity.ots.service.Controller;

import oracle.iam.identity.ots.service.catalog.HarvesterDescriptor;

////////////////////////////////////////////////////////////////////////////////
// abstract class Provisioning
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>Provisioning</code> implements the base functionality of a service
 ** end point for the Oracle Identity Manager Adapter Factory which handles data
 ** delivered to an Offline Target System.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
abstract class Provisioning<E extends Entity> extends    Controller
                                              implements EntityListener<E> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on a scheduled task to specify the
   ** fullqualified name of the Java Class (<code>EntityFactory</code>) governs
   ** the process of serializing XML data into newly created Java content
   ** trees, optionally validating the XML data as it is marshalled.
   */
  protected static final String MARSHALLER      = "Marshaller Implementation";

  /** the category of the logging facility to use */
  private static final String   LOGGER_CATEGORY = "OCS.OTS.PROVISIONING";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the format specification */
  private final XMLFormat       format          = new XMLFormat(XMLEntityFactory.PROLOG);

  /** the root element of the XML file to produce */
  private XMLOutputNode         root            = null;

  /** the listener to marshal XML file */
  private EntityListener<E>     listener        = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Provisioning</code> task adapter that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Provisioning() {
    // ensure inheritance
    super(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   listener
  /**
   ** Returns the {@link EntityListener} this task is using to marshal the
   ** enties to the XML file.
   **
   ** @return                    the {@link EntityListener} this task is using
   **                            to marshal the enties to the XML file.
   */
  protected final EntityListener<E> listener() {
    return this.listener;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   adapter
  /**
   ** Returns the {@link XMLOutputNode} this task is using to provision the
   ** enties to the XML file.
   **
   ** @return                    the {@link XMLOutputNode} this task is using to
   **                            provision the enties to the XML file.
   */
  protected final XMLOutputNode root() {
    return this.root;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nullValue (EntityListener)
  /**
   ** Returns the value which represents a <code>null</code> for an attribute
   ** element.
   ** <p>
   ** Such specification is required to distinct between empty attribute
   ** elements which are not passed through and overriding an already existing
   ** metadata to make it <code>null</code>.
   **
   ** @return                    the value which represents a <code>null</code> for
   **                            an attribute element.
   */
  @Override
  public String nullValue() {
    return ((HarvesterDescriptor)this.descriptor).nullValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** This method is invoked just before the thread operation will be executed.
   **
   ** @throws TaskException      the exception thrown if any goes wrong
   */
  @Override
  protected void initialize()
    throws TaskException {

    // ensure inheritance
    // this will produce the trace of the configured task parameter and create
    // the abstract file paths to the data and error directories
    super.initialize();

    final String className = stringValue(MARSHALLER);
    if (className.equalsIgnoreCase("this"))
      this.listener = this;
    else {
      try {
        // a little bit reflection
        final Class<?> clazz = Class.forName(className);
        this.listener = (EntityListener<E>)clazz.newInstance();
      }
      catch (ClassNotFoundException e) {
        throw TaskException.classNotFound(className);
      }
      catch (InstantiationException e) {
        throw TaskException.classNotCreated(className);
      }
      catch (IllegalAccessException e) {
        throw TaskException.classNoAccess(className);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterExecution (overridden)
  /**
   ** The call back method just invoked after reconciliation finished.
   ** <br>
   ** Close all resources requested before reconciliation takes place.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void afterExecution()
    throws TaskException {

    try {
      if (this.root != null)
        this.root.commit();
    }
    catch (XMLException e) {
      throw new TaskException(e);
    }

    // ensure inheritance
    super.afterExecution();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRoot
  /**
   ** This is used to create the root element within the XML document.
   ** <p>
   ** The created <code>XMLOutputNode</code> root object can be used to add
   ** attributes to the root element as well as other elements.
   **
   ** @param  name               the name of the root element to create.
   ** @param  schemaLocation     the location of the XML schema the node to
   **                            marshal belongs to.
   ** @param  identifier         the identifier of the root element to create.
   **
   ** @return                    the {@link XMLOutputNode} reday for further
   **                            processing.
   **
   ** @throws TaskException      if an error occurs during the marshalling.
   */
  protected XMLOutputNode createRoot(final String name, final String schemaLocation, final String identifier)
    throws TaskException {

    try {
      this.root = XMLProcessor.marshal(this, dataFile(), this.format).element(name);
      this.root.attribute(XMLEntityFactory.ATTRIBUTE_ID,    identifier);
      this.root.attribute(XMLProcessor.ATTRIBUTE_XMLNS,     XMLEntityFactory.NAMESPACE);
      this.root.attribute(XMLProcessor.ATTRIBUTE_XMLNS_XSI, XMLEntityFactory.SCHEMA);
      this.root.attribute(XMLProcessor.ATTRIBUTE_SCHEMA,    schemaLocation);
    }
    catch (XMLException e) {
      throw new TaskException(e);
    }
    return this.root;
  }
}