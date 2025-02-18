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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Provisioning Management

    File        :   ResourceObjectAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ResourceObjectAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysprov.schema;

import oracle.jbo.Row;

import oracle.iam.identity.foundation.naming.ResourceObject;

////////////////////////////////////////////////////////////////////////////////
// class ResourceObjectAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by Resource Object customization.
 ** <p>
 ** Define an instance variable for each VO attribute, with the data type that
 ** corresponds to the VO attribute type. The name of the instance variable must
 ** match the name of the VO attribute, and the case must match.
 ** <p>
 ** When the instance of this class is passed to the methods in the
 ** corresponding <code>Application Module</code> class, the
 ** <code>Application Module</code> can call the getter methods to retrieve the
 ** values from the AdapterBean and pass them to the OIM APIs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class ResourceObjectAdapter extends LegacyAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PK               = "objectsKey";
  public static final String NAME             = "objectsName";
  public static final String TYPE             = "objectsType";
  public static final String ORDERFOR         = "objectsOrderFor";
  public static final String ALLOWALL         = "objectsAllowAll";
  public static final String ALLOWMULTIPLE    = "objectsAllowMultiple";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1832135150089881144")
  private static final long  serialVersionUID = 5869746067160602080L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ResourceObjectAdapter</code> values object
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private ResourceObjectAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ResourceObjectAdapter</code> values object which
   ** use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   **                            <br>
   **                            Allowed object is {@link Row}.
   */
  public ResourceObjectAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ResourceObjectAdapter</code> values object which
   ** use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   **                            <br>
   **                            Allowed object is {@link Row}.
   ** @param  deferChildren      <code>true</code> if the child rows are lazy
   **                            populated at access time; <code>false</code> if
   **                            its reuired to populate them immediatly.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public ResourceObjectAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setObjectsKey
  /**
   ** Sets the value of the objectsKey property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @param  value              the value of the objectsKey property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setObjectsKey(final String value) {
    setAttribute(ResourceObject.KEY, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getObjectsKey
  /**
   ** Returns the value of the objectsKey property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @return                    the value of the objectsKey property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getObjectsKey() {
    return (String)getAttributeValue(ResourceObject.KEY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setObjectsName
  /**
   ** Sets the value of the objectsName property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @param  value              the value of the objectsName property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setObjectsName(final String value) {
    setAttribute(ResourceObject.NAME, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getObjectsName
  /**
   ** Returns the value of the objectsName property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @return                    the value of the objectsName property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getObjectsName() {
    return (String)getAttributeValue(ResourceObject.NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setObjectsOrderFor
  /**
   ** Sets the value of the objectsOrderFor property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @param  value              the value of the objectsOrderFor property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setObjectsOrderFor(final String value) {
    setAttribute(ResourceObject.ORDER_FOR, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getObjectsOrderFor
  /**
   ** Returns the value of the objectsOrderFor property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @return                    the value of the objectsOrderFor property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getObjectsOrderFor() {
    return (String)getAttributeValue(ResourceObject.ORDER_FOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setObjectsAllowAll
  /**
   ** Sets the value of the objectsAllowAll property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @param  value              the value of the objectsAllowAll property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setObjectsAllowAll(final String value) {
    setAttribute(ResourceObject.ALLOWALL, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getObjectsAllowAll
  /**
   ** Returns the value of the objectsAllowAll property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @return                    the value of the objectsAllowAll property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getObjectsAllowAll() {
    return (String)getAttributeValue(ResourceObject.ALLOWALL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setObjectsAllowMultiple
  /**
   ** Sets the value of the objectsAllowMultiple property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @param  value              the value of the objectsAllowMultiple property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public void setObjectsAllowMultiple(final String value) {
    setAttribute(ResourceObject.ALLOWMULTIPLE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getObjectsAllowMultiple
  /**
   ** Returns the value of the objectsAllowMultiple property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @return                    the value of the objectsAllowMultiple property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getObjectsAllowMultiple() {
    return (String)getAttributeValue(ResourceObject.ALLOWMULTIPLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   build
  /**
   ** Factory method to create an empty <code>ResourceObjectAdapter</code>.
   **
   ** @return                    the model adapter bean.
   **                            <br>
   **                            Possible object is
   **                            <code>ResourceObjectAdapter</code>.
   */
  public static ResourceObjectAdapter build() {
    return new ResourceObjectAdapter();
  }
}