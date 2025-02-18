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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   DataSetInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    DataSetInstance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.Set;

import java.io.Serializable;

import oracle.hst.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class DataSetInstance
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>DataSetInstance</code> encapsulate a Identity Manager
 ** <code>Request DataSet</code> which belongs to a <code>Resource Object</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DataSetInstance {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final Set<String> INGORE = CollectionUtility.unmodifiableSet(
    "SoDCheckStatus"
  , "SoDCheckTrackingID"
  , "SoDCheckViolation"
  , "SoDCheckEntitlementViolation"
  , "SoDCheckTimestamp"
  , "Service Account"
  );

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Attribute
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Attribute</code> represents a particular attribute of a
   ** <code>Request DataSet</code> in  Identity Manager that is used to create
   ** forms.
   */
  public static class Attribute {

    String       name;
    String       description;
    String       displayName;
    int          length;
    String       lookupCode;
    Serializable defaultValue;
    String       refAttrName;

    boolean      mandatory;
    boolean      searchable;
    boolean      encrypted;
    boolean      bulkUpdatable;

    boolean      keyField;
    boolean      persistent;
    boolean      createOnly;
    boolean      readOnly;
    boolean      itresource;
    Type         type;
    boolean      entitlement;

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    public static enum Type {
      TEXT("java.lang.String",            "Text",    "String"),
      LONG("oracle.jbo.domain.Number",    "Number",   "Integer"),
      INTEGER("oracle.jbo.domain.Number", "Number",   "Integer"),
      DATE("oracle.jbo.domain.Date",      "Date",     "Date"),
      BOOLEAN("java.lang.Boolean",        "Checkbox", "Boolean"),
      CHECKBOX("java.lang.Boolean",       "Checkbox", "Boolean");

      @SuppressWarnings("compatibility:-2621218373002928527")
      private static final long serialVersionUID = 1L;

      private String clazz;
      private String modelType;
      private String dataSetType;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>Type</code> that allows use as a JavaBean.
       **
       ** @param  id             the identifier of the resource type.
       */
      Type(final String clazz, final String modelType, final String dataSetType) {
        this.clazz = clazz;
        this.modelType = modelType;
        this.dataSetType = dataSetType;
      }

      //////////////////////////////////////////////////////////////////////////
      // Accessor methods
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: id
      /**
       ** Returns the value of the clazz property.
       **
       ** @return                the value of the clazz property.
       */
      public String clazz() {
        return this.clazz;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: modelType
      /**
       ** Returns the value of the modelType property.
       **
       ** @return                the value of the modelType property.
       */
      public String modelType() {
        return this.modelType;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: dataSetType
      /**
       ** Returns the value of the dataSetType property.
       **
       ** @return                the value of the dataSetType property.
       */
      public String dataSetType() {
        return this.dataSetType;
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods grouped by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: from
      /**
       ** Factory method to create a proper type from the given string value.
       **
       ** @param  value          the string value the type should be returned
       **                        for.
       **
       ** @return                the type property.
       */
      public static Type from(final String value) {

        if ("Date".equals(value) || "Timestamp".equals(value)) {
          return DATE;
        }
        else if ("long".equals(value)) {
          return INTEGER;
        }
        else if ("int".equals(value)) {
          return INTEGER;
        }
        else if ("boolean".equals(value)) {
          return BOOLEAN;
        }
        return TEXT;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Attribute</code> with the spcified name and type.
     **
     ** @param  name             the name of the attribute.
     ** @param  type             the {@link Type} of the attribute.
     */
    public Attribute(final String name, final Type type) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.name = name;
      this.type = type;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DataSetInstance</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DataSetInstance() {
    // ensure inheritance
    super();
  }
}