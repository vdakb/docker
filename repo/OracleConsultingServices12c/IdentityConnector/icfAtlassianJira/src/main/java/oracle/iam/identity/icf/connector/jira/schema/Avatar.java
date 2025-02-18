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

    File        :   Avatar.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Avatar.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-22-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.jira.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.schema.Resource;
import oracle.iam.identity.icf.schema.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class Avatar
// ~~~~~ ~~~~~~
/**
 ** A avatar is used as the icon for a account profile to illustrate comments on
 ** an issue and account Hover Profile.
 ** <p>
 ** JIRA comes pre-packaged with its own set of user avatars, which appear in
 ** the first few rows of a dialog box.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Avatar implements Resource<Avatar> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The URL pointing to the small avatar image */
  @JsonIgnore
  public static final String SM   = "16x16";

  /** The URL pointing to the medium small avatar image*/
  @JsonIgnore
  public static final String MS   = "24x24";

  /** The URL pointing to the medium avatar image*/
  @JsonIgnore
  public static final String MD   = "32x32";

  /** The URL pointing to the large avatar image*/
  @JsonIgnore
  public static final String LG   = "48x48";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Attribute
  @JsonProperty(SM)
  private String             sm;

  @Attribute
  @JsonProperty(MS)
  private String             ms;

  @Attribute
  @JsonProperty(MD)
  private String             md;

  @Attribute
  @JsonProperty(LG)
  private String             lg;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Avatar</code> REST Resource that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Avatar() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sm
  /**
   ** Sets the URL pointing to the small avatar image of the
   ** <code>Account</code>.
   **
   ** @param  value              the URL pointing to the small avatar image of
   **                            the <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Avatar sm(final String value) {
    this.sm = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sm
  /**
   ** Returns the URL pointing to the small avatar image of the
   ** <code>Account</code>.
   **
   ** @return                    the URL pointing to the small avatar image of
   **                            the <code>Account</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String sm() {
    return this.sm;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ms
  /**
   ** Sets the URL pointing to the medium small avatar image of the
   ** <code>Account</code>.
   **
   ** @param  value              the URL pointing to the medium small avatar
   **                            image of the <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Avatar ms(final String value) {
    this.ms = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ms
  /**
   ** Returns the URL pointing to the medium small avatar image of the
   ** <code>Account</code>.
   **
   ** @return                    the URL pointing to the medium small avatar
   **                            image of the <code>Account</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String ms() {
    return this.ms;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   md
  /**
   ** Sets the URL pointing to the medium avatar image of the
   ** <code>Account</code>.
   **
   ** @param  value              the URL pointing to the medium avatar image of
   **                            the <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Avatar md(final String value) {
    this.md = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   md
  /**
   ** Returns the URL pointing to the medium avatar image of the
   ** <code>Account</code>.
   **
   ** @return                    the URL pointing to the medium avatar image of
   **                            the <code>Account</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String md() {
    return this.md;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lg
  /**
   ** Sets the URL pointing to the large avatar image of the
   ** <code>Account</code>.
   **
   ** @param  value              the URL pointing to the large avatar image of
   **                            the <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Avatar lg(final String value) {
    this.lg = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lg
  /**
   ** Returns the URL pointing to the large avatar image of the
   ** <code>Account</code>.
   **
   ** @return                    the URL pointing to the large avatar image of
   **                            the <code>Account</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String lg() {
    return this.lg;
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
    int result = this.sm != null ? this.sm.hashCode() : 0;
    result = 31 * result + (this.ms != null ? this.ms.hashCode() : 0);
    result = 31 * result + (this.md != null ? this.md.hashCode() : 0);
    result = 31 * result + (this.lg != null ? this.lg.hashCode() : 0);
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
    builder.append("{\"").append(SM).append(":\"").append(this.sm).append("\"");
    builder.append(",\"").append(MS).append(":\"").append(this.ms).append("\"");
    builder.append(",\"").append(MD).append(":\"").append(this.md).append("\"");
    builder.append(",\"").append(LG).append(":\"").append(this.lg).append("\"");
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
   ** Two <code>Avatar</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Avatar</code>s may be different even though they contain the same
   ** set of names with the same values, but in a different
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

    final Avatar that = (Avatar)other;
    if (this.sm != null ? !this.sm.equals(that.sm) : that.sm != null)
      return false;

    if (this.ms != null ? !this.ms.equals(that.ms) : that.ms != null)
      return false;

    if (this.md != null ? !this.md.equals(that.md) : that.md != null)
      return false;

    return !(this.lg != null ? !this.lg.equals(that.lg) : that.lg != null);
  }
}