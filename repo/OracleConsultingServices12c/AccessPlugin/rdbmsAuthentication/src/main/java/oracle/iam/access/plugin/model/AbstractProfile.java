/*
    ORACLE Deutschland B.V. & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Plugin
    Subsystem   :   OpenIdConnect Discovery

    File        :   AbstractProfile.java

    Compiler    :   Java Development Kit 8

    Author      :   nitin.popli@oracle.com

    Purpose     :   This file implements the class
                    AbstractProfile.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-11-11  npopli      First release version
*/

package oracle.iam.access.plugin.model;

import java.util.Iterator;

import java.io.Serializable;

import java.security.Principal;

import oracle.security.idm.Identity;
import oracle.security.idm.Property;
import oracle.security.idm.PropertySet;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractProfile
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** This class represents a basic identity in the identity repository..
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.1
 */
public abstract class AbstractProfile<T extends AbstractProfile, I extends Comparable>  implements Identity
                                                                                        ,          Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7612927283667809370")
  private static final long serialVersionUID = 858603846540788486L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final PropertySet property = new PropertySet();

  private Boolean             active;
  private String              primary;
  private String              unique;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AbstractProfile</code> database resource that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Do not remove this constructor; its required for serialization!
   */
  protected AbstractProfile() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a minimal <code>AbstractProfile</code> database resource.
   **
   ** @param  property           this name of the primary identifier to map
   **                            <code>primaryIdentifier</code> in the embedded
   **                            property set.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the primary identifier of an
   **                            <code>AbstractProfile</code> database resource.
   **                            <br>
   **                            Allowed object is <code>I</code>.
   */
  protected AbstractProfile(final String property, final I identifier) {
    // ensure inheritance
    super();

    // initialize instance sttributes
    this.primary = property;
    primaryIdentifier(identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a minimal <code>AbstractProfile</code> database resource.
   **
   ** @param  property           this name of the unique identifier to map
   **                            <code>uniqueIdentifier</code> in the embedded
   **                            property set.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the unique identifier of an
   **                            <code>AbstractProfile</code> database resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected AbstractProfile(final String property, final String identifier) {
    // ensure inheritance
    super();

    // initialize instance sttributes
    this.unique = property;
    uniqueIdentifier(identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primaryIdentifier
  /**
   ** Sets the <code>id</code> property of the <code>AbstractProfile</code>.
   **
   ** @param  value              the primary identifier property of the
   **                            <code>AbstractProfile</code> to set.
   **                            <br>
   **                            Allowed object is <code>I</code>.
   **
   ** @return                    this instance of
   **                            <code>AbstractProfile</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>AbstractProfile</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public final T primaryIdentifier(final I value) {
    this.property.put(property(this.primary, value));
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uniqueIdentifier
  /**
   ** Sets the unique identifier of the <code>AbstractProfile</code>.
   **
   ** @param  value              the unique identifier property of the
   **                            <code>AbstractProfile</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    this instance of
   **                            <code>AbstractProfile</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>AbstractProfile</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public final T uniqueIdentifier(final String value) {
    this.property.put(property(this.unique, value));
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   active
  /**
   ** Sets the <code>active</code> property of the <code>AbstractProfile</code>
   ** entity.
   **
   ** @param  value              the <code>active</code> property of the
   **                            <code>AbstractProfile</code> entity to set.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    this instance of
   **                            <code>AbstractProfile</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>AbstractProfile</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public final T active(final Boolean value) {
    this.active = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   active
  /**
   ** Returns the <code>active</code> property of the
   ** <code>AbstractProfile</code> entity.
   **
   ** @return                    the <code>active</code> property of the
   **                            <code>AbstractProfile</code> entity.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean active() {
    return this.active;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  /**
   ** Returns a <code>boolean</code> from the embedded property set of this
   ** <code>AbstractProfile</code>.
   ** <p>
   ** Returns <code>false</code> if no value is mapped at <code>name</code> in
   ** the embedded property set.
   **
   ** @param  name               the name for the desired boolean value.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the boolean for the given <code>name</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean booleanValue(final String name) {
    return booleanValue(name, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  /**
   ** Returns a <code>boolean</code> from the embedded property set of this
   ** <code>AbstractProfile</code>.
   ** <p>
   ** Returns <code>defaultValue</code> if no value is mapped at
   ** <code>name</code> in the embedded property set.
   **
   ** @param  name               the name for the desired boolean value.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  defaultValue       the value that should be returned if no value
   **                            is mapped at <code>name</code> in the embedded
   **                            property set.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the boolean for the given <code>name</code> or
   **                            the default.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean booleanValue(final String name, final boolean defaultValue) {
    final Object result = propertyValue(name);
    return result == null ? defaultValue : Boolean.valueOf(result.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerValue
  /**
   ** Returns a <code>int</code> from the embedded property set of this
   ** <code>AbstractProfile</code>.
   ** <p>
   ** Returns {@link Integer#MIN_VALUE} if no value is mapped at
   ** <code>name</code> in the embedded property set.
   **
   ** @param  name               the name for the desired integer value.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the integer for the given <code>name</code>.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int integerValue(final String name) {
    return integerValue(name, Integer.MIN_VALUE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerValue
  /**
   ** Returns a <code>int</code> from the embedded property set of this
   ** <code>AbstractProfile</code>.
   ** <p>
   ** Returns <code>defaultValue</code> if no value is mapped at
   ** <code>name</code> in the embedded property set.
   **
   ** @param  name               the name for the desired integer value.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  defaultValue       the value that should be returned if no value
   **                            is mapped at <code>name</code> in the embedded
   **                            property set.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the integer for the given <code>name</code> or
   **                            the default.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int integerValue(final String name, final int defaultValue) {
    final Object result = propertyValue(name);
    return result == null ? defaultValue : Integer.valueOf(result.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longValue
  /**
   ** Returns a <code>long</code> from the embedded property set of this
   ** <code>AbstractProfile</code>.
   ** <p>
   ** Returns {@link Long#MIN_VALUE} if no value is mapped at <code>name</code>
   ** in the embedded property set.
   **
   ** @param  name               the name for the desired long value.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the long for the given <code>name</code>.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  public long longValue(final String name) {
    return longValue(name, Long.MIN_VALUE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longValue
  /**
   ** Returns a <code>long</code> from the embedded property set of this
   ** <code>AbstractProfile</code>.
   ** <p>
   ** Returns <code>defaultValue</code> if no value is mapped at
   ** <code>name</code> in the embedded property set.
   **
   ** @param  name               the name for the desired long value.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  defaultValue       the value that should be returned if no value
   **                            is mapped at <code>name</code> in the embedded
   **                            property set.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    the long for the given <code>name</code> or
   **                            the default.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  public long longValue(final String name, final long defaultValue) {
    final Object result = propertyValue(name);
    return result == null ? defaultValue : Long.valueOf(result.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
  /**
   ** Returns a {@link String} from the embedded property set of this
   ** <code>AbstractProfile</code>.
   ** <p>
   ** Returns <code>null</code> if no value is mapped at <code>name</code> in
   ** the embedded property set.
   **
   ** @param  name               the name for the desired {@link String} value.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the string for the given <code>name</code> or
   **                            the <code>null</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String stringValue(final String name) {
    return stringValue(name, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
  /**
   ** Returns a {@link String} from the embedded property set of this
   ** <code>AbstractProfile</code>.
   ** <p>
   ** Returns <code>defaultValue</code> if no value is mapped at
   ** <code>name</code> in the embedded property set.
   **
   **
   ** @param  name               the name for the desired {@link String} value.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  defaultValue       the value that should be returned if no value
   **                            is mapped at <code>name</code> in the embedded
   **                            property set.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the string for the given <code>name</code> or
   **                            the default.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String stringValue(final String name, final String defaultValue) {
    final Object result = propertyValue(name);
    return result == null ? defaultValue : result.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProperty (overridden)
  /**
   ** Returns a property mapped by the specified <code>name</code>.
   **
   ** @param  name               the name of the property to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Property} mapped at
   **                            <code>name</code> in the embedded property set
   **                            or <code>null</code> if there is no
   **                            {@link Property} mapped at name or the value
   **                            type is not compatible with the
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Property}.
   */
  public final Property getProperty(final String name) {
    return property(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProperties (overridden)
  /**
   ** Returns a property mapped by the specified <code>name</code>.
   **
   ** @param  name               the name of the property to lookup.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    the {@link Property} mapped at
   **                            <code>name</code> in the embedded property set
   **                            or <code>null</code> if there is no
   **                            {@link Property} mapped at name or the value
   **                            type is not compatible with the
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Property}.
   */
  public final PropertySet getProperties(final String[] name) {
    final PropertySet result = new PropertySet();
    final Iterator    i      = this.property.getAll();
    while (i.hasNext()) {
      final Property cursor = (Property)i.next();
      final String   tagged = cursor.getName();
      for (String match : name) {
        if (match.equals(tagged)) {
          result.put(cursor);
          break;
        }
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyValue
  /**
   ** Returns a property value mapped by the specified <code>name</code>.
   **
   ** @param  name               the name of the property to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Property} mapped at
   **                            <code>name</code> in the embedded property set
   **                            or <code>null</code> if there is no
   **                            {@link Property} mapped at name or the value
   **                            type is not compatible with the
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Object}.
   */
  protected final Object propertyValue(final String name) {
    final Object o = property(name);
    return o != null ? null : o;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Returns a property mapped by the specified <code>name</code>.
   **
   ** @param  name               the name of the property to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Property} mapped at
   **                            <code>name</code> in the embedded property set
   **                            or <code>null</code> if there is no
   **                            {@link Property} mapped at name or the value
   **                            type is not compatible with the
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Property}.
   */
  protected final Property property(final String name) {
    return this.property.get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Factory method to create a new {@link Property}.
   **
   ** @param  name               the name of the property to create.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value of the property to associate with
   **                            <code>name</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the {@link Property} created.
   **                            <br>
   **                            Possible object is {@link Property}.
   */
  protected final Property property(final String name, final Object value) {
    return new Property(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrincipal (Identity)
  @Override
  public final Principal getPrincipal() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getGUID (Identity)
  /**
   ** Returns the global unique identifier of the <code>AbstractProfile</code>.
   ** <br>
   ** A GUID uniquely represents a Identity in a given store. It always remains
   ** the same irrespective of any changes to the Identity.
   **
   ** @return                    the global unique identifier of the
   **                            <code>AbstractProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getGUID() {
    return stringValue(this.primary);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName (overridden)
  /**
   ** Sets the name identifier of the <code>AbstractProfile</code>.
   ** <p>
   ** Some repositories allow for multiple identities to have the same name.
   ** However such identities will have different "UniqueName"s.
   **
   ** @param  value              the name identifier of the
   **                            <code>AbstractProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
//  @Override
  public final void setName(final String value) {
    property(this.unique, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getName (Identity)
  /**
   ** Returns the name identifier of the <code>AbstractProfile</code>.
   ** <p>
   ** Some repositories allow for multiple identities to have the same name.
   ** However such identities will have different "UniqueName"s.
   **
   ** @return                    the name identifier of the
   **                            <code>AbstractProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getName() {
    return stringValue(this.unique);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUniqueName (Identity)
  /**
   ** Returns the unique name of the <code>AbstractProfile</code>.
   ** <p>
   ** It is the name with which Identity is uniquely represented in the
   ** underlying store. Depending upon the underlying store unique name might be
   ** subject to change if Identity information is changed.
   **
   ** @return                    the unique name of the
   **                            <code>AbstractProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getUniqueName() {
    return stringValue(this.unique);
  }
}