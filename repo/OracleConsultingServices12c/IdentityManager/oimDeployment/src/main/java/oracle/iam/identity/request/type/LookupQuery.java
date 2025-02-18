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

    File        :   LookupQuery.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LookupQuery.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.request.type;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

////////////////////////////////////////////////////////////////////////////////
// class LookupQuery
// ~~~~~ ~~~~~~~~~~~
/**
 ** <code>LookupQuery</code> is a container representing the values of a
 ** value restriction based on a Lookup Query on an attribute in a Request
 ** DataSet.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="lookup-query"&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;attribute name="lookup-query"  use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **       &lt;attribute name="display-field" use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **       &lt;attribute name="save-field"    use="required" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="lookup-query")
public class LookupQuery {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlAttribute(name="lookup-query",  required=true)
  protected String lookupQuery;
  @XmlAttribute(name="display-field", required=true)
  protected String displayField;
  @XmlAttribute(name="save-field",    required=true)
  protected String saveField;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LookupQuery</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public LookupQuery() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setLookupQuery
  /**
   ** Sets the query string to execute to obtain the values from the Oracle
   ** Identity Manager Repository.
   **
   ** @param  lookupQuery        the query string to execute to obtain the
   **                            values from the Identity Manager Repository.
   */
  public void setLookupQuery(final String lookupQuery) {
    this.lookupQuery = lookupQuery;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getLookupQuery
  /**
   ** Returns the query string to execute to obtain the values from the Oracle
   ** Identity Manager Repository.
   **
   ** @return                    the query string to execute to obtain the
   **                            values from the Identity Manager Repository.
   */
  public final String getLookupQuery() {
    return this.lookupQuery;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setDisplayField
  /**
   ** Sets the name of the field to display as a result of the query.
   **
   ** @param  displayField       the name of the field to display as a result of
   **                            the query.
   */
  public void setDisplayField(final String displayField) {
    this.displayField = displayField;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getDisplayField
  /**
   ** Returns the name of the field to display as a result of the query.
   **
   ** @return                    the name of the field to display as a result of
   **                            the query.
   */
  public final String getDisplayField() {
    return this.displayField;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setSaveField
  /**
   ** Sets the name of the field thats value has to be saved on the attribute
   ** he lookup query is assigned to.
   **
   ** @param  saveField          the name of the field thats value has to be
   **                            saved on the attribute the lookup query is
   **                            assigned to.
   */
  public void setSaveField(final String saveField) {
    this.saveField = saveField;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   getSaveField
  /**
   ** Returns the name of the field thats value has to be saved on the attribute
   ** he lookup query is assigned to.
   **
   ** @return                    the name of the field thats value has to be
   **                            saved on the attribute the lookup query is
   **                            assigned to.
   */
  public final String getSaveField() {
    return this.saveField;
  }
}