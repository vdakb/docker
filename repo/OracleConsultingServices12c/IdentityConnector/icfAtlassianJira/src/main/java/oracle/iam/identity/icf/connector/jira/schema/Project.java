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

    File        :   Project.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Project.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.jira.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import oracle.iam.identity.icf.schema.Schema;
import oracle.iam.identity.icf.schema.Attribute;
import oracle.iam.identity.icf.schema.Mutability;

import oracle.iam.identity.icf.connector.jira.Marshaller;

////////////////////////////////////////////////////////////////////////////////
// class Project
// ~~~~~ ~~~~~~~
/**
 **
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Marshaller.PROJECT_NAME)
public class Project extends Adressable<Project> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Attribute
  @JsonProperty
  private String             id;

  @JsonProperty
  @Attribute(value=Attribute.IDENTIFIER, mutability=Mutability.GENERATED)
  private String             key;

  @JsonProperty
  @Attribute(Attribute.UNIQUE)
  private String             name;

  @Attribute
  @JsonProperty
  private String             description;

  @JsonProperty("projectTypeKey")
  @Attribute
  private String             type;

  @Attribute
  @JsonProperty
  private Boolean            archived;

  @JsonProperty("avatarUrls")
  @Attribute
  private Avatar             avatar;

  @Attribute
  @JsonProperty("lead")
  private User              administrator;

  @JsonProperty(required=false)
  @Attribute
  private String              expand;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  public static class Role extends Adressable<Role> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////
    @JsonProperty
    @Attribute
    private String             name;

    @JsonProperty
    @Attribute(value=Attribute.IDENTIFIER)
    private Integer             id;

    @JsonProperty
    @Attribute
    private String             description;

    @JsonProperty
    @Attribute(multiValueClass = Actor.class)
    private List<Actor>        actors;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Role</code> REST Resource that allows use as
     ** a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Role() {
      // ensure inheritance
      super();
    }



    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Sets the identifier of the <code>Role</code>.
     **
     ** @param  value            the identifier of the <code>Role</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Role</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>Role</code>.
     */
    public final Role id(final Integer value) {
      this.id = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Returns the identifier of the <code>Role</code>.
     **
     ** @return                    the identifier of the <code>Role</code>.
     **                            <br>
     **                            Possible object is {@link String}.
     */
    public final Integer id() {
      return this.id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Sets the name of the <code>Role</code>.
     **
     ** @param  value            the name of the <code>Role</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Role</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>Role</code>.
     */
    public final Role name(final String value) {
      this.name = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Returns the name of the <code>Role</code>.
     **
     ** @return                  the name of the <code>Role</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: description
    /**
     ** Sets the human readable description of the <code>Role</code>.
     **
     ** @param  value            the human readable description of the
     **                          <code>Role</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Role</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>Role</code>.
     */
    public final Role description(final String value) {
      this.description = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: description
    /**
     ** Returns the human readable description of the <code>Role</code>.
     **
     ** @return                  the human readable description of the
     **                          <code>Role</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String description() {
      return this.description;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: actors
    /**
     ** Sets the {@link List} of {@link Actor}s that the <code>Role</code>
     ** belongs
     ** to.
     **
     ** @param  value            the {@link List} of {@link Actor}s that the
     **                          <code>Role</code> belongs to.
     **                          <br>
     **                          Allowed object is {@link List} of
     **                          {@link Actor}.
     **
     ** @return                  the <code>Role</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>Role</code>.
     */
    public final Role actors(final List<Actor> value) {
      this.actors = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: actors
    /**
     ** Returns the {@link List} of {@link Actor}s that the <code>Role</code>
     ** belongs to.
     ** <p>
     ** This accessor method returns a reference to the live list, not a
     ** snapshot. Therefore any modification you make to the returned list will
     ** be present inside the JSON object.
     **
     ** @return                  the {@link List} of {@link Actor}s that the
     **                          <code>Role</code> belongs to.
     **                          <br>
     **                          Possible object is {@link List} of
     **                          {@link Actor}.
     */
    public final List<Actor> actors() {
      return this.actors;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (Entity)
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
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int hashCode() {
      int result = this.id != null ? this.id.hashCode() : 0;
      result = 31 * result + (this.self != null        ? this.self.hashCode()        : 0);
      result = 31 * result + (this.name != null        ? this.name.hashCode()        : 0);
      result = 31 * result + (this.description != null ? this.description.hashCode() : 0);
      result = 31 * result + (this.actors != null      ? this.actors.hashCode()      : 0);
      return result;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>Role</code>s are considered equal if and only if they
     ** represent the same properties. As a consequence, two given
     ** <code>Role</code>s may be different even though they contain the same
     ** set of names with the same values, but in a different
     ** order.
     **
     ** @param  other            the reference object with which to compare.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the object argument; <code>false</code>
     **                          otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean equals(final Object other) {
      if (this == other)
        return true;

      if (other == null || getClass() != other.getClass())
        return false;

      final Role that = (Role)other;
      if (this.id != null ? !this.id.equals(that.id) : that.id != null)
        return false;

      if (this.name != null ? !this.name.equals(that.name) : that.name != null)
        return false;

      if (this.self != null ? !this.self.equals(that.self) : that.self != null)
        return false;

      if (this.description != null ? !this.description.equals(that.description) : that.description != null)
        return false;

      return !(this.actors != null ? !this.actors.equals(that.actors) : that.actors != null);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns the string representation for this instance in its minimal form.
     **
     ** @return                  the string representation for this instance in
     **                          its minimal form.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String toString() {
      final StringBuilder builder = new StringBuilder();
      builder.append("{\"id\":\"").append(this.id).append("\"");
      builder.append(",\"self\":\"").append(this.self).append("\"");
      builder.append(",\"name\":\"").append(this.name).append("\"");
      builder.append(",\"description\":\"").append(this.description).append("\"");
      builder.append(",\"actors\":[").append(this.actors).append("]");
      builder.append("}");
      return builder.toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Project</code> REST Resource that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Project() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Sets the identifier of the <code>Project</code>.
   **
   ** @param  value              the identifier of the <code>Project</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Project</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Project</code>.
   */
  public final Project id(final String value) {
    this.id = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the identifier of the <code>Project</code>.
   **
   ** @return                    the identifier of the <code>Project</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   key
  /**
   ** Sets the key of the <code>Project</code>.
   **
   ** @param  value              the key of the <code>Project</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Project</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Project</code>.
   */
  public final Project key(final String value) {
    this.key = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   key
  /**
   ** Returns the key of the <code>Project</code>.
   **
   ** @return                    the key of the <code>Project</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String key() {
    return this.key;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the name of the <code>Project</code>.
   **
   ** @param  value              the name of the <code>Project</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Project</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Project</code>.
   */
  public final Project name(final String value) {
    this.name = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the <code>Project</code>.
   **
   ** @return                    the name of the <code>Project</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Sets the user count description of the <code>Project</code>.
   **
   ** @param  value              the user count description of the
   **                            <code>Project</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Project</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Project</code>.
   */
  public final Project description(final String value) {
    this.description = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the user count description of the <code>Project</code>.
   **
   ** @return                    the user count description of the
   **                            <code>Project</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String description() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets type of the <code>Project</code>.
   **
   ** @param  value              the type of the <code>Project</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Project</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Project</code>.
   */
  public final Project type(final String value) {
    this.type = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns type of the <code>Project</code>.
   **
   ** @return                    the type of the <code>Project</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   archived
  /**
   ** Whether the <code>Project</code> is archived.
   **
   ** @param  value              the state of the <code>Project</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>Project</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Project</code>.
   */
  public final Project archived(final Boolean value) {
    this.archived = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   archived
  /**
   ** Returns whether the <code>Project</code> is archived.
   **
   ** @return                    whether the <code>Project</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean archived() {
    return this.archived;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   avatar
  /**
   ** Sets the {@link Avatar}s that the <code>Project</code> is associated with.
   **
   ** @param  value              the {@link Avatar}s that the <code>Project</code>
   **                            is associated with.
   **                            <br>
   **                            Allowed object is {@link Avatar}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final Project avatar(final Avatar value) {
    this.avatar = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   avatar
  /**
   ** Returns the {@link Avatar}s that the <code>Project</code> is associated
   ** with.
   **
   ** @return                    the {@link Avatar}s that the <code>Project</code>
   **                            is associated with.
   **                            <br>
   **                            Possible object is {@link Avatar}.
   */
  public final Avatar avatar() {
    return this.avatar;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   administrator
  /**
   ** Returns the parameters to expands for <code>Project</code>
   **
   ** @return                    parameters to expands for <code>Project</code>.
   **                            <br>
   **                            Possible object is {@link User}.
   */
  public final User administrator() {
    return this.administrator;
  }

    //////////////////////////////////////////////////////////////////////////////
  // Method:   administrator
  /**
   ** Sets the administraotr ({@link User}) that the <code>Project</code> is associated with.
   **
   ** @param  value              the {@link Avatar}s that the <code>Project</code>
   **                            is associated with.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final Project administrator(final User value) {
    this.administrator = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expand
  /**
   ** Returns the expands value for <code>Project</code>
   **
   ** @return                    parameters to expands for <code>Project</code>.
   **                            <br>
   **                            Possible object is {@link User}.
   */
  public final String expand() {
    return this.expand;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expand
  /**
   ** Sets the expand value the <code>Project</code> is associated with.
   **
   ** @param  value              the expand value that the <code>Project</code>
   **                            is associated with.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Project</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final Project expand(final String value) {
    this.expand = value;
    return this;
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
    result = 31 * result + (this.key         != null   ? this.key.hashCode()           : 0);
    result = 31 * result + (this.name        != null   ? this.name.hashCode()          : 0);
    result = 31 * result + (this.self        != null   ? this.self.hashCode()          : 0);
    result = 31 * result + (this.description != null   ? this.description.hashCode()   : 0);
    result = 31 * result + (this.type        != null   ? this.type.hashCode()          : 0);
    result = 31 * result + (this.archived    != null   ? this.archived.hashCode()      : 0);
    result = 31 * result + (this.avatar != null        ? this.avatar.hashCode()        : 0);
    result = 31 * result + (this.administrator != null ? this.administrator.hashCode() : 0);
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
    builder.append(",\"id\":\"").append(this.id).append("\"");
    builder.append(",\"key\":\"").append(this.key).append("\"");
    builder.append(",\"name\":\"").append(this.name).append("\"");
    builder.append(",\"self\":\"").append(this.self).append("\"");
    builder.append(",\"description\":\"").append(this.description).append("\"");
    builder.append(",\"type\":\"").append(this.type).append("\"");
    builder.append(",\"archived\":").append(this.archived).append("");
    builder.append(",\"avatar\":[").append(this.avatar).append("]");
    builder.append(",\"administrator\":{").append(this.administrator).append("}");
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
   ** Two <code>Project</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Project</code>s may be different even though they contain the same
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

    final Project that = (Project)other;
    if (this.id != null ? !this.id.equals(that.id) : that.id != null)
      return false;

    if (this.name != null ? !this.name.equals(that.name) : that.name != null)
      return false;

    if (this.self != null ? !this.self.equals(that.self) : that.self != null)
      return false;

    return !(this.description != null ? !this.description.equals(that.description) : that.description != null);
  }
}