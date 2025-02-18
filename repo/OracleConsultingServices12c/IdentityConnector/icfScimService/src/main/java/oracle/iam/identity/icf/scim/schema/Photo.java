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
    Subsystem   :   Generic SCIM Library

    File        :   Photo.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Photo.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.schema;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.scim.annotation.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class Photo
// ~~~~~ ~~~~~
/**
 ** Stores URL of a photo of the User.
 ** <p>
 ** The value <b>should</b> be a canonicalized URL, and <b>must</b> point to an
 ** image file (e.g. a GIF, JPEG, or PNG image file) rather than to a web page
 ** containing an image.
 ** <p>
 ** Service Providers <b>may</b> return the same image at different sizes,
 ** though it is recognized that no standard for describing images of various
 ** sizes currently exists. Note that this attribute <b>should not</b> be used
 ** to send down arbitrary photos taken by this User, but specifically profile
 ** photos of the User suitable for display when describing the User.
 ** <p>
 ** Instead of the standard Canonical Values for type, this attribute defines
 ** the following Canonical Values to represent popular photo sizes:
 ** <ul>
 **   <li>photo
 **   <li>thumbnail
 ** </uL>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Photo {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  @JsonIgnore(true)
  public static final String PREFIX    = "photos";

  /** The canonical value of a photo used as a picture */
  @JsonIgnore(true)
  public static final String PHOTO     = "photo";

  /** The canonical value of a photo used as a thumbnail */
  @JsonIgnore(true)
  public static final String THUMBNAIL = "thumbnail";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("primary")
  @Attribute(description="A Boolean value indicating the 'primary' or preferred attribute value for this attribute, e.g., the preferred messenger or primary messenger. The primary attribute value 'true' MUST appear no more than once.")
  private Boolean primary;

  @JsonProperty("type")
  @Attribute(description="A label indicating the attribute's function; e.g., 'photo' or 'thumbnail'.", canonical={PHOTO, THUMBNAIL})
  private String  type;

  @JsonProperty("value")
  @Attribute(description="URI of a photo of the User.", reference={ "external" })
  private URI     value;

  @JsonProperty("display")
  @Attribute(description="A human readable name, primarily used for display purposes.")
  private String  display;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Photo</code> SCIM Resource that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Photo() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Whether the 'primary' or preferred attribute value for this attribute.
   **
   ** @param  value              the 'primary' or preferred attribute value for
   **                            this attribute.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>Photo</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Photo</code>.
   */
  public final Photo primary(final Boolean value) {
    this.primary = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Whether the 'primary' or preferred attribute value for this attribute.
   **
   ** @return                    the 'primary' or preferred attribute value for
   **                            this attribute.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean primary() {
    return this.primary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets the label indicating the attribute's function.
   **
   ** @param  value              the label indicating the attribute's function.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Photo</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Photo</code>.
   */
  public final Photo type(final String value) {
    this.type = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the label indicating the attribute's function.
   **
   ** @return                    the label indicating the attribute's function.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Sets the photo location URI for the user.
   **
   ** @param  value              the photo location URI for the user.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the <code>Photo</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Photo</code>.
   */
  public final Photo value(final URI value) {
    this.value = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the photo location URI for the user.
   **
   ** @return                    the photo location URI for the user.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  public final URI value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   display
  /**
   ** Sets the display name, primarily used for display purposes.
   **
   ** @param  value              the display name, primarily used for display
   **                            purposes.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Photo</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Photo</code>.
   */
  public final Photo display(final String value) {
    this.display = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   display
  /**
   ** Returns the display name, primarily used for display purposes.
   **
   ** @return                    the display name, primarily used for display
   **                            purposes.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String display() {
    return this.display;
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
    int result = this.value != null ? this.value.hashCode() : 0;
    result = 31 * result + (this.display != null ? this.display.hashCode() : 0);
    result = 31 * result + (this.type    != null ? this.type.hashCode()    : 0);
    result = 31 * result + (this.primary != null ? this.primary.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Photo</code>s are considered equal if and only if they represent
   ** the same properties. As a consequence, two given <code>Photo</code>s may
   ** be different even though they contain the same set of names with the same
   ** values, but in a different order.
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

    final Photo that = (Photo)other;
    if (this.value != null ? !this.value.equals(that.value) : that.value != null)
      return false;

    if (this.display != null ? !this.display.equals(that.display) : that.display != null)
      return false;

    if (this.type != null ? !this.type.equals(that.type) : that.type != null)
      return false;

    return !(this.primary != null ? !this.primary.equals(that.primary) : that.primary != null);
  }
}