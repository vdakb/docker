/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2024. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Grafana Connector

    File        :   RoleMember.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    RoleMember.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.grafana.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import oracle.iam.identity.icf.schema.Attribute;
import oracle.iam.identity.icf.schema.Mutability;
import oracle.iam.identity.icf.schema.Resource;
import oracle.iam.identity.icf.schema.Returned;

////////////////////////////////////////////////////////////////////////////////
// final class RoleMember
// ~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** The base REST role member entity representation.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@JsonIgnoreProperties({Role.VERSION, Role.DISPLAY_NAME, Role.DESCRIPTION})
public class RoleMember implements Resource<RoleMember> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The global system identifier of the <code>Role</code> resource generated
   ** by the Service Provider at time of creation.
   */
  @JsonProperty(Role.UID)
  @Attribute(value=Attribute.IDENTIFIER, mutability=Mutability.IMMUTABLE, returned=Returned.ALWAYS)
  protected String  id;
  /**
   ** The unique identifier for the <code>Role</code> typically used as a label
   ** for the <code>Role</code> by Service Provider.
   */
  @JsonProperty(Role.NAME)
  @Attribute(value=Attribute.UNIQUE, mutability=Mutability.IMMUTABLE, returned=Returned.ALWAYS)
  protected String  name;
  /**
   ** Indicate whether the <code>Role</code> is global.
   */
  @JsonProperty(Role.GLOBAL)
  @Attribute(required=true, mutability=Mutability.IMMUTABLE, returned=Returned.ALWAYS)
  protected Boolean global;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>RoleMember</code> REST representation that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RoleMember() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Sets the identifier of the <code>RoleMember</code>.
   **
   ** @param  value              the identifier of the <code>RoleMember</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>RoleMember</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>RoleMember</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public final RoleMember id(final String value) {
    this.id = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the identifier of the <code>RoleMember</code>.
   **
   ** @return                    the identifier of the <code>RoleMember</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   global
  /**
   ** Whether the <code>RoleMember</code> is global.
   **
   ** @param  value              <code>true</code> if the
   **                            <code>RoleMember</code> is global.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>RoleMember</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>RoleMember</code>.
   */
  public final RoleMember global(final Boolean value) {
    this.global = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   global
  /**
   ** Whether the <code>RoleMember</code> is global.
   **
   ** @return                    <code>true</code> if the
   **                            <code>RoleMember</code> is global.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean global() {
    return this.global;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (Resource)
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
  public final int hashCode() {
    return Objects.hash(
      this.id
    , this.name
    , this.global
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (Resource)
  /**
   ** Returns the string representation for this instance in its minimal form.
   **
   ** @return                    the string representation for this instance in
   **                            its minimal form.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String toString() {
    return this.id == null ? "<null>" : this.id;
  }
}