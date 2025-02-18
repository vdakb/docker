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

    File        :   Group.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Group.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-03-06  SBERNET     First release version
*/
package oracle.iam.identity.icf.connector.jira.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import oracle.iam.identity.icf.schema.Attribute;
import oracle.iam.identity.icf.schema.Mutability;
import oracle.iam.identity.icf.schema.Schema;

////////////////////////////////////////////////////////////////////////////////
// class Group
// ~~~~~ ~~~~~
/**
 ** <code>Group</code>s apply to applications as a whole, not individual
 ** projects (for example, whether users can see the other users in the
 ** application).
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** Only JIRA administrators can change the membership of JIRA groups.
 ** <p>
 ** The difference between JIRA groups and JIRA project Groups seems to confuse
 ** many JIRA administrators.
 ** <br>
 ** JIRA originally just had users and groups of users, and no project Groups.
 ** <code>Group</code>s were pretty powerful?wherever you could do something
 ** with a user, you could generally use a <code>Group</code> instead.
 ** <br>
 ** For instance, if you wanted to allow a specific user <i>john.doe</i> to
 ** change the <i>Reporter</i> field in a project?s issues, you could:
 ** <ol>
 **   <li>Create a new permission scheme with a description something like
 **       <i>john.doe can change Reporter</i>.
 **   <li>Add the <i>john.doe</i> user to the appropriate Modify Reporter
 **       permission entry in the new permission scheme.
 **   <li>Change the appropriate JIRA project to use the new permission scheme.
 ** </ol>
 ** You could also do the same thing with a group:
 ** <ol>
 **   <li>Define a new JIRA group named <i>Can Modify Reporters</i>.
 **   <li>Add the user <i>jane.doe</i> to the new group.
 **   <li>Create a new permission scheme with a description something like
 **       <i>Added an extra group of users that can change Reporter</i>.
 **   <li>Add the <b>group</b> (instead of the user) to the appropriate
 **       <i>Modify Reporter</i> permission entry in the new permission scheme.
 **   <li>Just as before, change the appropriate JIRA project to use the new
 **       permission scheme.
 ** </ol>
 ** Both of these approaches now allow <i>john.doe</i> or <i>jane.doe</i>
 ** respectivly to change the Reporter field. So far so good, but there are two
 ** main problems with using JIRA <code>Group</code>s like this:
 ** <ul>
 **   <li>scaling
 **   <li>updating
 ** </ul>
 ** <b>Scaling</b>
 ** <br>
 ** If you want <i>john.doe</i> to be able to edit the Reporter field in some
 ** projects, and also allow a different user, <i>jane.doe</i>, to do the same
 ** thing in other projects, then you have to create two permission schemes, one
 ** for each user being granted this permission. If you then decide that they
 ** are both allowed to edit the Reporter in some shared projects, then you
 ** need a third permission scheme. With lots of users, this leads to an
 ** explosion in the number of permission schemes (and any other JIRA scheme
 ** that supports <code>Group</code>s).
 ** <p>
 ** Keeping track of the difference between each of these permission schemes is
 ** tedious and error-prone, even with the scheme comparison tools
 ** (Administration --&gt; Scheme Tools), which are themselves deprecated since
 ** JIRA 6.4.
 ** <p>
 ** <b>Updating</b>
 ** As time passes, users will likely need to be part of different JIRA
 ** <code>Group</code>s. Only JIRA administrators can change the membership of
 ** JIRA <code>Group</code>s. However project leads are allowed to make changes
 ** to project Groups, and project leads usually know which project Groups a user
 ** should currently be part of. Using project Groups means fewer tasks for
 ** JIRA administrators.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema
public class Group extends Adressable<Group>{

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  @JsonProperty("name")
  @Attribute(value=Attribute.IDENTIFIER, mutability=Mutability.GENERATED)
  private String             id;

  @Attribute
  @JsonProperty
  private String             html;

  @JsonProperty
  @Attribute(multiValueClass=Label.class)
  private List<Label>        labels;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Group</code> REST Resource that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Group() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Sets the identifier of the <code>Group</code>.
   **
   ** @param  value              the identifier of the <code>Group</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Group</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Group</code>.
   */
  public final Group id(final String value) {
    this.id = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the identifier of the <code>Group</code>.
   **
   ** @return                    the identifier of the <code>Group</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   html
  /**
   ** Sets the name of the <code>Group</code>.
   **
   ** @param  value              the name of the <code>Group</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Group</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Group</code>.
   */
  public final Group name(final String value) {
    this.html = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the html of the <code>Group</code>.
   **
   ** @return                    the html of the <code>Group</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String html() {
    return this.html;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   html
  /**
   ** Sets list of <code>Label</code>of the <code>Group</code>.
   **
   ** @param  value              list of <code>Label</code> of the
   **                            <code>Group</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Group</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Group</code>.
   */
  public final Group labels(final List<Label> value) {
    this.labels = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the list of <code>Label</code> of the <code>Group</code>.
   **
   ** @return                    the list of <code>Label</code> of the <code>Group</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final List<Label> label() {
    return this.labels;
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
    int result = this.id != null ? this.id.hashCode() : 0;
    result = 31 * result + (this.id        != null ? this.id.hashCode()     : 0);
    result = 31 * result + (this.html      != null ? this.html.hashCode()   : 0);
    result = 31 * result + (this.labels    != null ? this.labels.hashCode() : 0);
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
    builder.append("{\"id\":\"").append(this.id).append("\"");
    builder.append(",\"html\":\"").append(this.html).append("\"");
    builder.append(",\"labels\":\"").append(this.labels).append("\"");
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
   ** Two <code>Role</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Role</code>s may be different even though they contain the same set
   ** of names with the same values, but in a different order.
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

    final Group that = (Group)other;
    if (this.id != null ? !this.id.equals(that.id) : that.id != null)
      return false;

    if (this.html != null ? !this.html.equals(that.html) : that.html != null)
      return false;

    return !(this.labels != null ? !this.labels.equals(that.labels) : that.labels != null);
  }
}
