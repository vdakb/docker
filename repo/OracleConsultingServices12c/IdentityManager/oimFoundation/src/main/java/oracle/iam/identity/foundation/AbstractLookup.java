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
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AbstractLookup.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractLookup.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcInvalidLookupException;

import Thor.API.Operations.tcLookupOperationsIntf;

import oracle.iam.platform.Platform;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.naming.LookupValue;

////////////////////////////////////////////////////////////////////////////////
// class AbstractLookup
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>AbstractLookup</code> implements the base functionality
 ** of an Oracle Identity Manager Lookup Definition.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class AbstractLookup extends AbstractObject {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AbstractLookup</code> which is associated
   ** with the specified logging provider <code>loggable</code>.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Metadata Descriptor</code> wrapper.
   **                            Allowed object {@link Loggable}.
   */
  public AbstractLookup(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractLookup</code> which is associated
   ** with the specified logging provider <code>loggable</code> and belongs to
   ** the <code>Lookup Definition</code> specified by <code>instanceName</code>.
   ** <p>
   ** The Lookup Definition will be populated from the repository of Oracle
   ** Identity Manager and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Metadata Descriptor</code> wrapper.
   **                            Allowed object {@link Loggable}.
   ** @param  instanceName       the name of the <code>Metadata Descriptor</code>
   **                            instance where this wrapper belongs to.
   **                            Allowed object {@link String}.
   **
   ** @throws TaskException      if the <code>Metadata Descriptor</code> is not
   **                            defined in  Oracle Identity Manager metadata
   **                            entries or one or more attributes are missing
   **                            on the <code>Metadata Descriptor</code>.
   */
  public AbstractLookup(final Loggable loggable, final String instanceName)
    throws TaskException {

    // ensure inheritance
    super(loggable, instanceName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Sets a <code>AbstractLookup</code> object in the attribute mapping of this
   ** wrapper.
   **
   ** @param  key                the key for the attribute string to set.
   **                            Allowed object {@link String}.
   ** @param  value              the <code>AbstractLookup</code> to associate
   **                            with the specified key.
   **                            Allowed object {@link AbstractLookup}.
   */
  public void attribute(final String key, final AbstractLookup value) {
    attribute(this.attribute, key, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractLoggable)
  /**
   ** Returns the array with names which should be populated from the
   ** lookup definition of Oracle Identity Manager.
   ** <br>
   ** This object cannot have any pre declared attribute. We are population each
   ** encoded field defined on the Lookup Instance and creating an appropriate
   ** attribute.
   **
   ** @return                    the array with names which should be populated.
   */
  @Override
  public final AbstractAttribute[] attributes() {

    final String method = "attributes";
    trace(method, SystemMessage.METHOD_ENTRY);

    int                 j     = 0;
    AbstractAttribute[] entry = new AbstractAttribute[this.attribute.size()];
    for (String name : this.attribute.keySet())
      entry[j++] = LookupAttribute.build(name);

    trace(method, SystemMessage.METHOD_EXIT);
    return entry;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateAttributes (AbstractObject)
  /**
   ** Obtains the Lookup Definition definition from Oracle Identity Manager.
   **
   ** @param  instanceName       the name of the Oracle Identity Manager
   **                            Lookup Definition where this wrapper belongs
   **                            to.
   **                            Allowed object {@link String}.
   **
   ** @throws TaskException      in case of
   **                            <ul>
   **                              <li>the Lookup Definition is not defined in
   **                                  the Oracle Identity Manager meta data
   **                                  entries with the name passed to the
   **                                  constructor of this wrapper
   **                              <li>more than one instance was found for the
   **                                  name; seems to that data corruption has
   **                                  been occurred;
   **                              <li>a generell error has occurred.
   **                            </ul>
   **                            or a generell error has occurred.
   */
  @Override
  public void populateAttributes(final String instanceName)
    throws TaskException {

    final tcLookupOperationsIntf facade = Platform.getService(tcLookupOperationsIntf.class);
    try {
      populateAttributes(facade, instanceName);
    }
    finally {
      facade.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateInstance (AbstractObject)
  /**
   ** Obtains the Lookup Definition from Oracle Identity Manager.
   **
   ** @param  instanceName       the name of the Oracle Identity Manager
   **                            Lookup Definition where this wrapper belongs
   **                            to.
   **                            Allowed object {@link String}.
   **
   ** @throws TaskException      in case of
   **                            <ul>
   **                              <li>the Lookup Definition is not defined in
   **                                  the Oracle Identity Manager meta data
   **                                  entries with the name passed to the
   **                                  constructor of this wrapper
   **                              <li>more than one instance was found for the
   **                                  name; seems to that data corruption has
   **                                  been occurred;
   **                            </ul> or a generell error has occurred.
   */
  @Override
  protected final void populateInstance(final String instanceName)
    throws TaskException {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodedValue
  /**
   ** Search in this <code>Lookup Definition</code> based on the specified
   ** <code>decodedValue</code> and returns the encoded value.
   **
   ** @param  decodedValue       the value based on which we are searching.
   **                            Allowed object {@link String}.
   **
   ** @return                    the encoded value that match the passed
   **                            <code>decodedVAlue</code> or <code>null</code>
   **                            if no matching value exists in this
   **                            <code>Lookup Definition</code>.
   */
  public final String encodedValue(final String decodedValue) {
    for (String encodedValue : this.keySet()) {
      final String value = (String)get(encodedValue);
      if (value != null && value.equals(decodedValue))
        return encodedValue;
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate
  /**
   ** Obtains the Lookup Definition definition from Oracle Identity Manager.
   **
   ** @param  instanceName       the name of the Oracle Identity Manager
   **                            Lookup Definition where this wrapper belongs
   **                            to.
   **                            Allowed object {@link String}.
   **
   ** @throws TaskException      in case of
   **                            <ul>
   **                              <li>the Lookup Definition is not defined in
   **                                  the Oracle Identity Manager meta data
   **                                  entries with the name passed to the
   **                                  constructor of this wrapper
   **                              <li>more than one instance was found for the
   **                                  name; seems to that data corruption has
   **                                  been occurred;
   **                              <li>a generell error has occurred.
   **                            </ul> or a generell error has occurred.
   */
  private void populateAttributes(final tcLookupOperationsIntf facade, final String instanceName)
    throws TaskException {

    final String method = "populateAttributes";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      // gets the lookup values of the instance name passed to constructor
      tcResultSet  mapping = facade.getLookupValues(instanceName);
      // clear the value holder
      clear();
      // iterate about both collections and get the value of each field for the
      // appropriate attribute name
      for (int i = 0; i < mapping.getRowCount(); i++) {
        mapping.goToRow(i);
        put(fetchStringValue(mapping, LookupValue.ENCODED), fetchStringValue(mapping, LookupValue.DECODED));
      }
    }
    // in case the specified Lookup Definition does not exists
    catch (tcInvalidLookupException e) {
      throw new TaskException(TaskError.LOOKUP_NOT_FOUND, instanceName);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}