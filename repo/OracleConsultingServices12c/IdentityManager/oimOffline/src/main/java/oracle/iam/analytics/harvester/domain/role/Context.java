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

    Copyright © 2013. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   Policy.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Policy.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2013-08-23  DSteding    First release version
*/

package oracle.iam.analytics.harvester.domain.role;

import javax.xml.namespace.QName;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class Context
// ~~~~~ ~~~~~~~
/**
 ** Java class for context complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="context"&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;attribute name="uniqueName"     use="required" type="{http://service.api.oia.iam.ots/role}unique"/&gt;
 **       &lt;attribute name="relationShip"   use="required" type="{http://service.api.oia.iam.ots/role}relationship"/&gt;
 **       &lt;attribute name="exactMatch"     use="required" type="{http://service.api.oia.iam.ots/role}match"/&gt;
 **       &lt;attribute name="validateOnly"   use="required" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 **       &lt;attribute name="ignoreWarnings" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
@XmlType(name=Context.LOCAL)
@XmlAccessorType(XmlAccessType.FIELD)
public class Context {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "context";
  public static final QName  PORT  = new QName(ObjectFactory.NAMESPACE, LOCAL);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlAttribute(required=true)
  protected Unique           uniqueName;
  @XmlAttribute(required=true)
  protected RelationShip     relationShip;
  @XmlAttribute(required=true)
  protected Match            exactMatch;
  @XmlAttribute(required=true)
  protected boolean          validateOnly;
  @XmlAttribute(required=true)
  protected boolean          ignoreWarnings;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Context</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public Context() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>Context</code> with the specified id.
   **
   ** @param  uniqueName         the unique name strategy.
   ** @param  relationShip       the policy build strategy.
   ** @param  exactMatch         the exact match strategy.
   ** @param  validateOnly       <code>true</code> if the data should only be
   **                            validated by the request; otherwise
   **                            <code>false</code>.
   ** @param  ignoreWarnings     <code>true</code> if any warning detected by
   **                            the validation of the request should be
   **                            ignored; otherwise <code>false</code>.
   */
  public Context(final String uniqueName, final String relationShip, final String exactMatch, final boolean validateOnly, final boolean ignoreWarnings) {
    // ensure inheritance
    this(Unique.fromValue(uniqueName), RelationShip.fromValue(relationShip), Match.fromValue(exactMatch), validateOnly, ignoreWarnings);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>Context</code> with the specified id.
   **
   ** @param  uniqueName         the unique name strategy.
   ** @param  relationShip       the policy build strategy.
   ** @param  exactMatch         the exact match strategy.
   ** @param  validateOnly       <code>true</code> if the data should only be
   **                            validated by the request; otherwise
   **                            <code>false</code>.
   ** @param  ignoreWarnings     <code>true</code> if any warning detected by
   **                            the validation of the request should be
   **                            ignored; otherwise <code>false</code>.
   */
  public Context(final Unique uniqueName, final RelationShip relationShip, final Match exactMatch, final boolean validateOnly, final boolean ignoreWarnings) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.uniqueName     = uniqueName;
    this.relationShip   = relationShip;
    this.exactMatch     = exactMatch;
    this.validateOnly   = validateOnly;
    this.ignoreWarnings = ignoreWarnings;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUniqueName
  /**
   ** Sets the value of the uniqueName property.
   **
   ** @param  value            allowed object is {@link String}.
   */
  public void setUniqueName(final String value) {
    setUniqueName(Unique.fromValue(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUniqueName
  /**
   ** Sets the value of the uniqueName property.
   **
   ** @param  value            allowed object is {@link Unique}.
   */
  public void setUniqueName(final Unique value) {
    this.uniqueName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUniqueName
  /**
   ** Returns the value of the uniqueName property.
   **
   ** @return                  possible object is {@link Unique}.
   */
  public Unique getUniqueName() {
    return this.uniqueName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRelationShip
  /**
   ** Sets the value of the relationShip property.
   **
   ** @param  value            allowed object is {@link String}.
   */
  public void setRelationShip(final String value) {
    setRelationShip(RelationShip.fromValue(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRelationShip
  /**
   ** Sets the value of the relationShip property.
   **
   ** @param  value            allowed object is {@link RelationShip}.
   */
  public void setRelationShip(final RelationShip value) {
    this.relationShip = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRelationShip
  /**
   ** Returns the value of the relationShip property.
   **
   ** @return                  possible object is {@link RelationShip}.
   */
  public RelationShip getRelationShip() {
    return this.relationShip;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setExactMatch
  /**
   ** Sets the value of the exactMatch property.
   **
   ** @param  value            allowed object is {@link String}.
   */
  public void setExactMatch(final String value) {
    setExactMatch(Match.fromValue(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setExactMatch
  /**
   ** Sets the value of the exactMatch property.
   **
   ** @param  value            allowed object is {@link Match}.
   */
  public void setExactMatch(final Match value) {
    this.exactMatch = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getExactMatch
  /**
   ** Returns the value of the exactMatch property.
   **
   ** @return                  possible object is {@link Match}.
   */
  public Match getExactMatch() {
    return this.exactMatch;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setValidateOnly
  /**
   ** Sets the value of the validateOnly property.
   **
   ** @param  value            allowed object is <code>boolean</code>.
   */
  public void setValidateOnly(final boolean value) {
    this.validateOnly = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isValidateOnly
  /**
   ** Returns the value of the validateOnly property.
   **
   ** @return                  possible object is <code>boolean</code>.
   */
  public boolean isValidateOnly() {
    return this.validateOnly;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setIgnoreWarnings
  /**
   ** Sets the value of the ignoreWarnings property.
   **
   ** @param  value            allowed object is <code>boolean</code>.
   */
  public void setIgnoreWarnings(final boolean value) {
    this.ignoreWarnings = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isIgnoreWarnings
  /**
   ** Returns the value of the ignoreWarnings property.
   **
   ** @return                  possible object is <code>boolean</code>.
   */
  public boolean isIgnoreWarnings() {
    return this.ignoreWarnings;
  }
}