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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   DirectoryReference.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryReference.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.ldap;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.TaskDescriptor;
import oracle.iam.identity.foundation.AttributeMapping;
import oracle.iam.identity.foundation.AttributeTransformation;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryReference
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The descriptor to handle entity references
 */
public class DirectoryReference extends TaskDescriptor {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String  objectClassName     = null;
  private boolean entryLinkStandard   = true;
  private boolean entryLinkPrefixed   = true;
  private String  entryLinkAttribute  = null;
  private String  entrySearchBase     = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryReference</code> which is associated
   ** the specified logging provider <code>loggable</code> for debugging
   ** purpose.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} this
   **                            <code>Metadata Descriptor</code> wrapper
   **                            belongs to.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   */
  private DirectoryReference(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryReference</code> which is associated the
   ** specified logging provider <code>loggable</code> for debugging purpose.
   **
   ** @param  loggable           the {@link Loggable} this
   **                            <code>Metadata Descriptor</code> wrapper
   **                            belongs to.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  mapping            the {@link AttributeMapping} of varaiables
   **                            provided by this reference descriptor.
   **                            <br>
   **                            Allowed object is {@link AttributeMapping}.
   ** @param  constant           the {@link AttributeMapping} of constants
   **                            provided by this reference descriptor.
   **                            <br>
   **                            Allowed object is {@link AttributeMapping}.
   ** @param  transformation     the {@link AttributeTransformation} of this
   **                            reference descriptor.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeTransformation}.
   */
  public DirectoryReference(final Loggable loggable, final AttributeMapping mapping, final AttributeMapping constant, final AttributeTransformation transformation) {
    // ensure inheritance
    super(loggable, mapping, constant, transformation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutators
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClassName
  /**
   ** Sets the name of the object class name defined for a LDAP server.
   **
   ** @param objectClassName     the name of the generic object class.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void objectClassName(final String objectClassName) {
    this.objectClassName = objectClassName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClassName
  /**
   ** Returns the name of the object class name defined for a LDAP server.
   **
   ** @return                    the name of the generic object class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String objectClassName() {
    return this.objectClassName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryLinkAttribute
  /**
   ** Sets the name of the entry link attribute defined for a LDAP server.
   **
   ** @param  entryLinkAttribute the name of the generic object link attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void entryLinkAttribute(final String entryLinkAttribute) {
    this.entryLinkAttribute = entryLinkAttribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryLinkAttribute
  /**
   ** Returns the name of the entry link attribute defined for a LDAP server.
   **
   ** @return                    the name of the generic object link attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String entryLinkAttribute() {
    return this.entryLinkAttribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryLinkStandard
  /**
   ** Sets to <code>true</code> if the entry link attribute defined for a LDAP
   ** server use the distinguished name as the attribute value.
   **
   ** @param  entryLinkStandard  <code>true</code> if the entry link attribute
   **                            defined for a LDAP server use the distinguished
   **                            name as the attribute value; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public final void entryLinkStandard(final boolean entryLinkStandard) {
    this.entryLinkStandard = entryLinkStandard;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryLinkStandard
  /**
   ** Returns <code>true</code> if the entry link attribute defined for a LDAP
   ** server use the distinguished name as the attribute value.
   **
   ** @return                    <code>true</code> if the entry link attribute
   **                            defined for a LDAP server use the distinguished
   **                            name as the attribute value; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean entryLinkStandard() {
    return this.entryLinkStandard;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryLinkPrefixed
  /**
   ** Sets to <code>true</code> if the entry link attribute value defined for in
   ** the LDAP server needs to be handled as an entiltelement which requires to
   ** prefix the value accordingly with key of the <code>IT Resource</code> and
   ** its name.
   **
   ** @param  entryLinkPrefixed  <code>true</code> if the entry link attribute
   **                            value defined for in the LDAP server needs to
   **                            be handled as an entiltelement which requires
   **                            to prefix the value accordingly with the key of
   **                            the <code>IT Resource Key</code> and its name;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public final void entryLinkPrefixed(final boolean entryLinkPrefixed) {
    this.entryLinkPrefixed = entryLinkPrefixed;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryLinkPrefixed
  /**
   ** Returns <code>true</code> if the entry link attribute value defined for in
   ** the LDAP server needs to be handled as an entiltelement which requires to
   ** prefix the value accordingly with key of the <code>IT Resource</code> and
   ** its name.
   **
   ** @return                    <code>true</code> if the entry link attribute
   **                            value defined for in the LDAP server needs to
   **                            be handled as an entiltelement which requires
   **                            to prefix the value accordingly with the key of
   **                            the <code>IT Resource Key</code> and its name;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean entryLinkPrefixed() {
    return this.entryLinkPrefixed;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entrySearchBase
  /**
   ** Sets the search base of the object class defined for a LDAP server.
   **
   ** @param  entrySearchBase    the search base of the object entry.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void entrySearchBase(final String entrySearchBase) {
    this.entrySearchBase = entrySearchBase;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entrySearchBase
  /**
   ** Returns the search base of the object entry defined for a LDAP server.
   **
   ** @return                    the search base of the object entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String entrySearchBase() {
    return this.entrySearchBase;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>DirectoryReference</code> which is
   ** associated the specified logging provider <code>loggable</code> for
   ** debugging purpose.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} this
   **                            <code>Metadata Descriptor</code> wrapper
   **                            belongs to.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   **
   ** @return                    an empty <code>DirectoryReference</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryFeature</code>.
   */
  public static DirectoryReference build(final Loggable loggable) {
    return new DirectoryReference(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DirectoryReference</code> which is
   ** associated the specified logging provider <code>loggable</code> for
   ** debugging purpose.
   **
   ** @param  loggable           the {@link Loggable} this
   **                            <code>Metadata Descriptor</code> wrapper
   **                            belongs to.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  mapping            the {@link AttributeMapping} of varaiables
   **                            provided by this reference descriptor.
   **                            <br>
   **                            Allowed object is {@link AttributeMapping}.
   ** @param  constant           the {@link AttributeMapping} of constants
   **                            provided by this reference descriptor.
   **                            <br>
   **                            Allowed object is {@link AttributeMapping}.
   ** @param  transformation     the {@link AttributeTransformation} of this
   **                            reference descriptor.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeTransformation}.
   **
   ** @return                    a <code>IT Resource</code> reference instance
   **                            wrapped as <code>DirectoryReference</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryReference</code>.
   */
  public static DirectoryReference build(final Loggable loggable, final AttributeMapping mapping, final AttributeMapping constant, final AttributeTransformation transformation) {
    return new DirectoryReference(loggable, mapping, constant, transformation);
  }
}