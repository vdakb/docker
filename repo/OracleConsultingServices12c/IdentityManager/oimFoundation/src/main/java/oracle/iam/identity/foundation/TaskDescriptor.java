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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   TaskDescriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TaskDescriptor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-03-11  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import java.util.Set;
import java.util.LinkedHashSet;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class TaskDescriptor
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>TaskDescriptor</code> is intend to use where inbound attributes of
 ** an Oracle Identity Manager Object (core or user defined) are mapped to the
 ** outbound provisioning or reconciliation target.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class TaskDescriptor extends AbstractLookup {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String MULTI_VALUE_TYPE       = "multi-valued";

  /**
   ** Attribute tag which must be defined on this Metadata Descriptor to hold
   ** the name of the attribute that is the human readable identifier of an
   ** attribute set.
   */
  public static final String IDENTIFIER             = "identifier";

  /**
   ** Attribute tag which must be defined on this Metadata Descriptor to hold
   ** the name of the attribute that is the hierarchally identifier of an
   ** attribute set.
   */
  public static final String HIERARCHY             = "hierarchy";

  /**
   ** Attribute tag which must be defined on this Metadata Descriptor to hold
   ** the flag to indicate that the descriptor specifies the target or source
   ** names as the native field names instead of field labels.
   */
  public static final String TARGETNAME_NATIVELY    = "natively";

  /**
   ** Attribute tag which must be defined on this Metadata Descriptor to specify
   ** that the attribute transformation should be applied.
   */
  public static final String TRANSFORMATION_ENABLED = "transformation";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Entitlement
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** The <code>Entitlement</code> is intend to use where inbound attributes of
   ** an Oracle Identity Manager Entitlement Object (core or user defined) are
   ** mapped to the outbound provisioning or reconciliation target.
   */
  public static class Entitlement extends TaskDescriptor {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** the system identifier belonging to the target system to map it with the
     ** values of the Oracle Identity Manager.
     */
    private final String systemID;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Entitlement</code> which is associated with
     ** the specified logging provider <code>loggable</code> for the specified
     ** system identifier.
     ** <p>
     ** The instance created through this constructor has to populated manually
     ** and does not belongs to an Oracle Identity Manager Object.
     **
     ** @param  loggable         the {@link Loggable} which has instantiated
     **                          this <code>Metadata Descriptor</code>
     **                          configuration wrapper.
     ** @param  systemID         the identifier of the source/target that
     **                          belongs to the outbound system.
     **
     ** @throws TaskException    if <code>systemID</code> is <code>null</code>
     **                          or empty.
     */
    public Entitlement(final Loggable loggable, final String systemID)
      throws TaskException {

      // ensure inheritance
      super(loggable);

      if (StringUtility.isEmpty(systemID))
        throw TaskException.argumentIsNull("systemID");

      // initialize instance
      this.systemID = systemID;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Entitlement</code> which is associated with
     ** the specified logging provider <code>loggable</code> for the specified
     ** system identifier.
     **
     **
     ** @param  loggable         the {@link Loggable} which has instantiated
     **                          this <code>Metadata Descriptor</code>
     **                          configuration wrapper.
     ** @param  systemID         the identifier of the source/target that
     **                          belongs to the outbound system.
     ** @param  mapping          the {@link AttributeMapping} of varaiables
     **                          provided by this reference descriptor.
     ** @param  constant         the {@link AttributeMapping} of constants
     **                          provided by this reference descriptor.
     ** @param  transformation   the {@link AttributeTransformation} of this
     **                          reference descriptor.
     **
     ** @throws TaskException    if <code>systemID</code> is <code>null</code>
     **                          or empty.
     */
    public Entitlement(final Loggable loggable, final String systemID, final AttributeMapping mapping, final AttributeMapping constant, final AttributeTransformation transformation)
      throws TaskException {

      // ensure inheritance
      super(loggable, mapping, constant, transformation);

      if (StringUtility.isEmpty(systemID))
        throw TaskException.argumentIsNull("systemID");

      // initialize instance
      this.systemID = systemID;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: systemID
    /**
     ** Returns the name of the source/target for this <code>Entitlement</code>
     ** that belongs to the outbound system.
     **
     ** @return                  the identifier of the source/target that
     **                          belongs to the outbound system..
     */
    public final String systemID() {
      return this.systemID;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the mapping of attributes */
  private final AttributeMapping        mapping;

  /** the constant of attributes */
  private final AttributeMapping        constant;

  /** the mapping of attribute lookup transformation */
  private final AttributeTransformation transformation;

  private       Set<String>             returning;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TaskDescriptor</code> which is associated
   ** with the specified logging provider <code>loggable</code>.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Metadata Descriptor</code>
   **                            configuration wrapper.
   */
  public TaskDescriptor(final Loggable loggable) {
    // ensure inheritance
    this(loggable, new AttributeMapping(loggable), new AttributeMapping(loggable), new AttributeTransformation(loggable));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TaskDescriptor</code> which is associated
   ** with the specified logging provider <code>loggable</code>.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Metadata Descriptor</code>
   **                            configuration wrapper.
   ** @param  mapping            the {@link AttributeMapping} of varaiables
   **                            provided by this reference descriptor.
   ** @param  constant           the {@link AttributeMapping} of constants
   **                            provided by this reference descriptor.
   ** @param  transformation     the {@link AttributeTransformation} of this
   **                            reference descriptor.
   */
  public TaskDescriptor(final Loggable loggable, final AttributeMapping mapping, final AttributeMapping constant, final AttributeTransformation transformation) {
    // ensure inheritance
    super(loggable);

    // initialize instance attributes
    this.mapping        = mapping;
    this.constant       = constant;
    this.transformation = transformation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifier
  /**
   ** Sets the attribute that is the human readable identifier of an
   ** attribute set.
   **
   ** @param  identifier         the attribute that is the human readable
   **                            identifier of an attribute set.
   */
  public final void identifier(final String identifier) {
    this.put(IDENTIFIER, identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifier
  /**
   ** Returns the attribute that is the human readable identifier of an
   ** attribute set.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #IDENTIFIER}.
   **
   ** @return                    the attribute that is the human readable
   **                            identifier of an attribute set.
   */
  public final String identifier() {
    return stringValue(IDENTIFIER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hierarchy
  /**
   ** Sets the attribute that is the hierarchy field of an attribute set.
   **
   ** @param  hierarchy          the attribute that is the hierarchy field of an
   **                            attribute set
   */
  public final void hierarchy(final String hierarchy) {
    this.put(HIERARCHY, hierarchy);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hierarchy
  /**
   ** Returns the attribute that is the hierarchy field of an attribute set.
   ** attribute set.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #HIERARCHY}.
   **
   ** @return                    the attribute that is the attribute that is the
   **                            hierarchy field of an attribute set.
   */
  public final String hierarchy() {
    return stringValue(HIERARCHY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformationEnabled
  /**
   ** Whether if a transformation should be done after the values are fetched
   ** from the process form.
   **
   ** @param  transformationEnabled <code>true</code> if a transformation should
   **                               be done after the values are fetched from
   **                               the process form.
   */
  public final void transformationEnabled(final boolean transformationEnabled) {
    this.put(TRANSFORMATION_ENABLED, transformationEnabled ? SystemConstant.TRUE : SystemConstant.FALSE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformationEnabled
  /**
   ** Returns <code>true</code> if a transformation should be done after the
   ** values are fetched from the process form.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #TRANSFORMATION_ENABLED}.
   **
   ** @return                    <code>true</code> if a transformation should be
   **                            done after the values are fetched from the
   **                            process form.
   */
  public final boolean transformationEnabled() {
    return booleanValue(TRANSFORMATION_ENABLED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   natively
  /**
   ** Determines if the descriptor specifies the source or target names as the
   ** native field names instead of field labels.
   ** <p>
   ** If the source or target names are affected is determined by the direction
   ** this descriptor is used.
   **
   ** @param  natively             <code>true</code> if a transformation should
   **                               be done after the values are fetched from
   **                               the process form.
   */
  public final void natively(final boolean natively) {
    this.put(TARGETNAME_NATIVELY, natively ? SystemConstant.TRUE : SystemConstant.FALSE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   natively
  /**
   ** Returns <code>true</code> if the descriptor specifies the source or target
   ** names are the native field names instead of field labels.
   ** <p>
   ** If the source or target names are affected is determined by the direction
   ** this descriptor is used.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #TARGETNAME_NATIVELY}.
   **
   ** @return                    <code>true</code> if the descriptor specifies
   **                            the source or target names as the native field
   **                            names instead of filed labels.
   */
  public final boolean natively() {
    return booleanValue(TARGETNAME_NATIVELY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   constantMapping
  /**
   ** Returns or creates the {@link AttributeMapping} used by this reference
   ** descriptor.
   **
   ** @return                    the {@link AttributeMapping} of this reference
   **                            descriptor.
   */
  public final AttributeMapping constantMapping() {
    return this.constant;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeMapping
  /**
   ** Returns or creates the {@link AttributeMapping} used by this reference
   ** descriptor.
   **
   ** @return                    the {@link AttributeMapping} of this reference
   **                            descriptor.
   */
  public final AttributeMapping attributeMapping() {
    return this.mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformationMapping
  /**
   ** Returns the attribute transformation of this reference descriptor.
   **
   ** @return                    {@link AttributeTransformation} used by this
   **                            reference descriptor.
   */
  public final AttributeTransformation transformationMapping() {
    return this.transformation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   returningAttributes
  /**
   ** Returns the {@link Set} of attribute names that will be passed to a Target
   ** System search operation to specify which attributes the Service Provider
   ** should return for an account.
   **
   ** @return                   the array of attribute names that will be
   **                           passed to a Target System search operation to
   **                           specify which attributes the Service Provider
   **                           should return.
   */
  public final Set<String> returningAttributes() {
    // Lazy initialization of the attribute names to return for an account to
    // reconcile
    if (this.returning != null)
      return this.returning;

    // transform the defined mapping to an array
    final AbstractAttribute[] encoded = this.mapping.attributes();

    // create a new array with is big enough to hold all requested attributes
    // of an entry
    this.returning = new LinkedHashSet<String>(encoded.length);

    // add all defined attribute id's to the array
    for (int i = 0; i < encoded.length; i++)
      this.returning.add(encoded[i].id());

    return this.returning;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character "\n" (line feed).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                    the string representation of this instance.
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(TaskBundle.format(TaskMessage.TASK_DESCRIPTOR, StringUtility.formatCollection(this)));
    builder.append(TaskBundle.format(TaskMessage.ATTRIBUT_MAPPING, this.mapping.toString()));
    if (transformationEnabled())
      builder.append(TaskBundle.format(TaskMessage.ATTRIBUT_TRANSFORMATION, this.transformation.toString()));
    return builder.toString();
  }
}