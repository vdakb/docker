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

    File        :   ObjectFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ObjectFactory.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.request.type;

import javax.xml.bind.annotation.XmlRegistry;

////////////////////////////////////////////////////////////////////////////////
// class ObjectFactory
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This object contains factory methods for each Java content interface and
 ** Java element interface generated in the
 ** oracle.iam.identity.common.type.request package.
 ** <p>
 ** An ObjectFactory allows you to programatically construct new instances of
 ** the Java representation for XML content. The Java representation of XML
 ** content can consist of schema derived interfaces and classes representing
 ** the binding of schema type definitions, element declarations and model
 ** groups.
 ** <p>
 ** Factory methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ObjectFactory</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ObjectFactory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   createAttribute
  /**
   ** Factory method to create an {@link Attribute} binding.
   **
   ** @return                    the created {@link Attribute} binding.
   */
  public Attribute createAttribute() {
    return new Attribute();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   createAttributeReference
  /**
   ** Factory method to create an {@link AttributeReference} binding.
   **
   ** @return                    the created {@link AttributeReference} binding.
   */
  public AttributeReference createAttributeReference() {
    return new AttributeReference();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   createLookupValues
  /**
   ** Factory method to create an {@link LookupValues} binding.
   **
   ** @return                    the created {@link LookupValues} binding.
   */
  public LookupValues createLookupValues() {
    return new LookupValues();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   createLookupQuery
  /**
   ** Factory method to create an {@link LookupQuery} binding.
   **
   ** @return                    the created {@link LookupQuery} binding.
   */
  public LookupQuery createLookupQuery() {
    return new LookupQuery();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   createPrePopAdapter
  /**
   ** Factory method to create an {@link PrePopAdapter} binding.
   **
   ** @return                    the created {@link PrePopAdapter} binding.
   */
  public PrePopAdapter createPrePopAdapter() {
    return new PrePopAdapter();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   createDataSetValidator
  /**
   ** Factory method to create an {@link DataSetValidator} binding.
   **
   ** @return                    the created {@link DataSetValidator} binding.
   */
  public DataSetValidator createDataSetValidator() {
    return new DataSetValidator();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   createRequestDataSet
  /**
   ** Factory method to create an {@link RequestDataSet} binding.
   **
   ** @return                    the created {@link RequestDataSet} binding.
   */
  public RequestDataSet createRequestDataSet() {
    return new RequestDataSet();
  }
}