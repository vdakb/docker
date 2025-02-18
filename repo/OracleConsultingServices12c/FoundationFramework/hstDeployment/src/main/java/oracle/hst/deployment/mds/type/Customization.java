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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Customization.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Customization.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.1.0.0     2014-11-29  DSteding    First release version
*/

package oracle.hst.deployment.mds.type;

import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.ResourceCollection;

import oracle.mds.config.CustConfig;
import oracle.mds.config.CustClassListMapping;
import oracle.mds.config.MDSConfigurationException;

import oracle.mds.cust.CustClassList;
import oracle.mds.cust.CustomizationClass;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class Customization
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The environment wrapper of a specific cutomization configuration in a Oracle
 ** Metadata Store.
 ** A customization configuration object holds configuration information for
 ** customizations. Specifcally it maintains a lookup table of customization
 ** class lists.
 ** <p>
 ** This class maintains an ordered list of customization class lists.
 ** These are keyed by 3 parameters:
 ** Path - the path or name of the metadata object (MO)
 ** Namespace - the XML namespace of the top level element in the MO
 ** Local Name - the XML localname of the top level element in the MO
 ** <p>
 ** At runtime, the customization subsystem will search through the list in
 ** order of registration until a matching entry is found. Note that a
 ** <code>null</code> key in the mapping is considered to match anything as is a
 ** <code>null</code> key in the lookup.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
public class Customization extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Map<String, CustClassList> match = new LinkedHashMap<String, CustClassList>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Customization</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Customization() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another
   ** <code>Customization</code> instance.
   ** <p>
   ** You must not set another attribute or nest elements inside this element
   ** if you make it a reference.
   **
   ** @param  reference          the id of this instance.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  @Override
  public void setRefid(final Reference reference)
    throws BuildException {

    Object other = reference.getReferencedObject(getProject());
    if(other instanceof Customization) {
      final Customization that = (Customization)other;
      this.match.clear();
      this.match.putAll(that.match);
      // ensure inheritance
      super.setRefid(reference);
    }
    else {
      final Object[] parameter = {reference.getRefId(), "customization", reference.getRefId(), other.getClass().getName() };
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_REFERENCE_MISMATCH, parameter), getLocation());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match
  /**
   ** Returns the registered path matches.
   **
   ** @return                  the registered path matches.
   */
  public Map<String, CustClassList> match() {
    return this.match;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: iterator (ResourceCollection)
  /**
   ** Return an {@link Iterator} over the contents of this
   ** {@link ResourceCollection}, whose elements are {@link String} instances.
   **
   ** @return                  an {@link Iterator} of Resources.
   */
  public Iterator<String> iterator() {
    return this.match.keySet().iterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  isFilesystemOnly (ResourceCollection)
  /**
   ** Indicate whether this {@link ResourceCollection} is composed entirely of
   ** Resources accessible via local filesystem conventions.
   **
   ** @return                    whether this is a filesystem-only
   **                            resource collection.
   */
  public boolean isFilesystemOnly() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  size (ResourceCollection)
  /**
   ** Return the number of contained Resources.
   **
   ** @return                    number of elements as int.
   */
  public int size() {
    return this.match.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   config
  /**
   ** Returns the customization configuration object holds configuration
   ** information for customizations.
   **
   ** @return                  the the customization configuration object holds
   **                          configuration information for customizations or
   **                          <code>null</code> if a costumozation mapping is
   **                          not configured.
   **
   ** @throws MDSConfigurationException if the configuration of the
   **                                   customization could not be created.
   */
  public CustConfig config()
    throws MDSConfigurationException {

    final List<CustClassListMapping> mapping = new ArrayList<CustClassListMapping>();
    for (String path : this.match.keySet()) {
      mapping.add(new CustClassListMapping(path, null, null, this.match.get(path)));
    }

    return mapping.size() == 0 ? null : new CustConfig(mapping.toArray(new CustClassListMapping[mapping.size()]));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredMatch
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Match}.
   **
   ** @param  namespace         the subject of maintenance.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            <code>MDSCustomizationMatch</code>
   */
  public void addConfiguredMatch(final Match namespace)
    throws BuildException {

    // check if we have this file already
    if (this.match.containsKey(namespace.path()))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.METADATA_NAMESPACE_ONLYONCE, namespace.path()));

    final List<Clazz>          classes = namespace.clazz();
    final CustomizationClass[] array   = new CustomizationClass[classes.size()];
    for (int i = 0; i < classes.size(); i++)
      array[i] = classes.get(i).delegate();

    this.match.put(namespace.path(), new CustClassList(array));
  }
}