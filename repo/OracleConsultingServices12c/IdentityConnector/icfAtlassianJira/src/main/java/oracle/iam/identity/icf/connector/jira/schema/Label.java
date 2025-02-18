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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Atlassian Jira Server Connector

    File        :   Label.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Label.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.jira.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.schema.Resource;
import oracle.iam.identity.icf.schema.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class Label
// ~~~~~ ~~~~~
/**
 **
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Label implements Resource<Label> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Attribute
  @JsonProperty
  private String type;

  @Attribute
  @JsonProperty
  private String text;

  @Attribute
  @JsonProperty
  private String title;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Label</code> REST Resource that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Label() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets the type of the <code>Label</code>.
   **
   ** @param  value              the type of the <code>Label</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Label</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Label</code>.
   */
  public final Label type(final String value) {
    this.type = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of the <code>Label</code>.
   **
   ** @return                    the type of the <code>Label</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   text
  /**
   ** Sets the text of the <code>Label</code>.
   **
   ** @param  value              the text of the <code>Label</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Label</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Label</code>.
   */
  public final Label text(final String value) {
    this.text = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   text
  /**
   ** Returns the text of the <code>Label</code>.
   **
   ** @return                    the text of the <code>Label</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String text() {
    return this.text;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   title
  /**
   ** Sets the title of the <code>Label</code>.
   **
   ** @param  value              the title of the <code>Label</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Label</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Label</code>.
   */
  public final Label title(final String value) {
    this.title = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   title
  /**
   ** Returns the title of the <code>Label</code>.
   **
   ** @return                    the title of the <code>Label</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String title() {
    return this.title;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (Entity)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    int result = this.type != null ? this.type.hashCode() : 0;
    result = 31 * result + (this.text  != null ? this.text.hashCode()  : 0);
    result = 31 * result + (this.title != null ? this.title.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (Entity)
  /**
   ** Returns the string representation for this instance in its minimal form.
   **
   ** @return                    the string representation for this instance in
   **                            its minimal form.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("{\"type\":\"").append(this.type).append("\"");
    builder.append(",\"text\":\"").append(this.text).append("\"");
    builder.append(",\"title\":\"").append(this.title).append("\"");
    builder.append("}");
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Label</code>s are considered equal if and only if they represent
   ** the same properties. As a consequence, two given <code>Label</code>s may
   ** be different even though they contain the same set of names with the same
   ** values, but in a different
   ** order.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final Label that = (Label)other;
    if (this.type != null ? !this.type.equals(that.type) : that.type != null)
      return false;

    if (this.text != null ? !this.text.equals(that.text) : that.text != null)
      return false;

    return !(this.title != null ? !this.title.equals(that.title) : that.title != null);
  }
}