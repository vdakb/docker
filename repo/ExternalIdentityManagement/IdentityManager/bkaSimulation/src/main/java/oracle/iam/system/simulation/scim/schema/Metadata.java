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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM Interface

    File        :   Metadata.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Metadata.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.schema;

import java.util.Calendar;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.system.simulation.scim.annotation.Attribute;
import oracle.iam.system.simulation.scim.annotation.Definition;

////////////////////////////////////////////////////////////////////////////////
// class Metadata
// ~~~~~ ~~~~~~~~
/**
 ** Stores metadata about a SCIM object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Metadata {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  public static final String PREFIX = "metadata";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("resourceType")
  @Attribute(description="The resource Type", mutability=Definition.Mutability.READ_ONLY, returned=Definition.Returned.ALWAYS)
  private String   resourceType;

  @JsonProperty("created")
  @Attribute(description="Date and time the resource was created", mutability=Definition.Mutability.READ_ONLY, returned=Definition.Returned.ALWAYS)
  private Calendar created;

  @JsonProperty("lastModified")
  @Attribute(description="Date and time the resource was last modified", mutability=Definition.Mutability.READ_ONLY, returned=Definition.Returned.ALWAYS)
  private Calendar modified;

  @JsonProperty("location")
  @Attribute(description="The location (URI) of the resource", mutability=Definition.Mutability.READ_ONLY, returned=Definition.Returned.ALWAYS)
  private URI     location;

  @JsonProperty("version")
  @Attribute(description="The version of the resource", mutability=Definition.Mutability.READ_ONLY, returned=Definition.Returned.ALWAYS)
  private String  version;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Metadata</code> SCIM Resource that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Metadata() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceType
  /**
   ** Sets the resourceType of the SCIM object.
   **
   ** @param  value              the resourceType of the SCIM object.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Metadata</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Metadata</code>.
   */
  public final Metadata resourceType(final String value) {
    this.resourceType = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceType
  /**
   ** Returns the resourceType of the SCIM object.
   **
   ** @return                    the resourceType of the SCIM object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String resourceType() {
    return this.resourceType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   created
  /**
   ** Sets the timestamp of when the SCIM object was created.
   **
   ** @param  value              the date and time the SCIM object was created.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    the <code>Metadata</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Metadata</code>.
   */
  public final Metadata created(final Calendar value) {
    this.created = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   created
  /**
   ** Returns the timestamp of when the SCIM object was created.
   **
   ** @return                    the date and time the SCIM object was created.
   **                            <br>
   **                            Possible object is {@link Calendar}.
   */
  public final Calendar created() {
    return this.created;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modified
  /**
   ** Sets the timestamp of when the SCIM object was last modified.
   **
   ** @param  value              the date and time the SCIM object was last
   **                            modified.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    the <code>Metadata</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Metadata</code>.
   */
  public final Metadata modified(final Calendar value) {
    this.modified = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modified
  /**
   ** Returns the timestamp of when the SCIM object was last modified.
   **
   ** @return                    the date and time the SCIM object was last
   **                            modified.
   **                            <br>
   **                            Possible object is {@link Calendar}.
   */
  public final Calendar modified() {
    return this.modified;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Sets the location URI of the SCIM object.
   **
   ** @param  value              the location URI of the SCIM object.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the <code>Metadata</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Metadata</code>.
   */
  public final Metadata location(final URI value) {
    this.location = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Returns the location URI of the SCIM object.
   **
   ** @return                    the location URI of the SCIM object.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  public final URI location() {
    return this.location;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   version
  /**
   ** Sets the version of the SCIM object.
   **
   ** @param  value              the version of the SCIM object.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Metadata</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Metadata</code>.
   */
  public final Metadata version(final String value) {
    this.version = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   version
  /**
   ** Returns the version of the SCIM object.
   **
   ** @return                    the version of the SCIM object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String version() {
    return this.version;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
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
   */
  @Override
  public int hashCode() {
    int result = this.resourceType != null ? this.resourceType.hashCode()    : 0;
    result = 31 * result + (this.created  != null ? this.created.hashCode()  : 0);
    result = 31 * result + (this.modified != null ? this.modified.hashCode() : 0);
    result = 31 * result + (this.location != null ? this.location.hashCode() : 0);
    result = 31 * result + (this.version  != null ? this.version.hashCode()  : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Metadata</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Metadata</code>s may be different even though they contain the same
   ** set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final Metadata that = (Metadata)other;
    if (this.resourceType != null ? !this.resourceType.equals(that.resourceType) : that.resourceType != null)
      return false;

    if (this.created != null ? !this.created.equals(that.created) : that.created != null)
      return false;

    if (this.modified != null ? !this.modified.equals(that.modified) : that.modified != null)
      return false;

    if (this.location != null ? !this.location.equals(that.location) : that.location != null)
      return false;

    return !(this.version != null ? !this.version.equals(that.version) : that.version != null);
  }
}