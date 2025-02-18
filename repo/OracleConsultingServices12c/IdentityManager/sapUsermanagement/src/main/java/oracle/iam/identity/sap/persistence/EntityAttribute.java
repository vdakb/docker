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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   SAP/R3 Usermanagement Connector

    File        :   EntityAttribute.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    EntityAttribute.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-06-19  DSteding    First release version
*/

package oracle.iam.identity.sap.persistence;

////////////////////////////////////////////////////////////////////////////////
// class EntityAttribute
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Represents the SAP User Management attribute mapping configuration and
 ** contains the value objec attributes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
/**
 */
public class EntityAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** BAPI Structure Name e.g. ADDRESS */
  private String segment;

  /** BAPI StructureX Name e.g. ADDRESSX */
  private String segmentX;

  /** the identifier attribute used for querying the custom table e.g. BNAME */
  private String identifier;

  /** Field Name of the field in the BAPI e.g. TEL1_NUMBR */
  /** the attribute name used by the outbound operations e.g. TEL1_NUMBR */
  private String outboundName;

  /** the extended attribute name used by the outbound operations e.g. TEL1_NUMBR */
  private String outboundNameX;

  /** the attribute name used by the inbound operations  */
  private String inboundName;

  /** the value of the attribute e.g. 9812523562 */
  private String value;

  /** the type of the attribute e.g. TEXT */
  private String type;

  /** BAPI Funtion Name  e.g. BAPI_USER_GETDETAILS */
  private String function;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EntityAttribute</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EntityAttribute() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   segment
  /**
   ** Sets the value of the segment property.
   **
   ** @param  value              the value of the segment property to set.
   **                            Allowed object is {@link String}.
   */
  public void segment(final String value) {
    this.segment = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   segment
  /**
   ** Returns the value of the segment property.
   **
   ** @return                    the value of the segment property.
   **                            Possible object is {@link String}.
   */
  public String segment() {
    return this.segment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   segmentX
  /**
   ** Sets the value of the segmentX property.
   **
   ** @param  value              the value of the segmentX property to set.
   **                            Allowed object is {@link String}.
   */
  public void segmentX(final String value) {
    this.segmentX = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   segmentX
  /**
   ** Returns the value of the segmentX property.
   **
   ** @return                    the value of the segmentX property.
   **                            Possible object is {@link String}.
   */
  public String segmentX() {
    return this.segmentX;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   outboundName
  /**
   ** Sets the value of the outboundName property.
   **
   ** @param  value              the value of the outboundName property to set.
   **                            Allowed object is {@link String}.
   */
  public void outboundName(final String value) {
    this.outboundName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   outboundName
  /**
   ** Returns the value of the outboundName property.
   **
   ** @return                    the value of the outboundName property.
   **                            Possible object is {@link String}.
   */
  public String outboundName() {
    return this.outboundName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   outboundNameX
  /**
   ** Sets the value of the outboundNameX property.
   **
   ** @param  value              the value of the outboundNameX property to set.
   **                            Allowed object is {@link String}.
   */
  public void outboundNameX(final String value) {
    this.outboundNameX = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   outboundNameX
  /**
   ** Returns the value of the outboundNameX property.
   **
   ** @return                    the value of the outboundNameX property.
   **                            Possible object is {@link String}.
   */
  public String outboundNameX() {
    return this.outboundNameX;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   function
  /**
   ** Sets the value of the function property.
   **
   ** @param  value              the value of the function property to set.
   **                            Allowed object is {@link String}.
   */
  public void function(final String value) {
    this.function = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   function
  /**
   ** Returns the value of the function property.
   **
   ** @return                    the value of the function property.
   **                            Possible object is {@link String}.
   */
  public String function() {
    return this.function;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifier
  /**
   ** Sets the value of the identifier property.
   **
   ** @param  value              the value of the identifier property to set.
   **                            Allowed object is {@link String}.
   */
  public void identifier(final String value) {
    this.identifier = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifier
  /**
   ** Returns the value of the identifier property.
   **
   ** @return                    the value of the identifier property.
   **                            Possible object is {@link String}.
   */
  public String identifier() {
    return this.identifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inboundName
  /**
   ** Sets the value of the inboundName property.
   **
   ** @param  value              the value of the inboundName property to set.
   **                            Allowed object is {@link String}.
   */
  public void inboundName(final String value) {
    this.inboundName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inboundName
  /**
   ** Returns the value of the inboundName property.
   **
   ** @return                    the value of the inboundName property.
   **                            Possible object is {@link String}.
   */
  public String inboundName() {
    return this.inboundName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Sets the value of the fieldValue property.
   **
   ** @param  value              the value of the fieldValue property to set.
   **                            Allowed object is {@link String}.
   */
  public void value(final String value) {
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of the fieldValue property.
   **
   ** @return                    the value of the fieldValue property.
   **                            Possible object is {@link String}.
   */
  public String value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets the value of the type property.
   **
   ** @param  value              the value of the type property to set.
   **                            Allowed object is {@link String}.
   */
  public void type(final String value) {
    this.type = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the value of the type property.
   **
   ** @return                    the value of the type property.
   **                            Possible object is {@link String}.
   */
  public String type() {
    return this.type;
  }
}