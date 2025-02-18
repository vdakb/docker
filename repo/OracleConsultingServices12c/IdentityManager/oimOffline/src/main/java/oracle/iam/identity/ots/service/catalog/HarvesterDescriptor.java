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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   HarvesterDescriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    HarvesterDescriptor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.catalog;

import java.util.Map;
import java.util.HashMap;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.reconciliation.Descriptor;

////////////////////////////////////////////////////////////////////////////////
// class HarvesterDescriptor
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>HarvesterDescriptor</code> is intend to use where exported or
 ** imported attributes of an Oracle Identity Manager Catalog (core or user
 ** defined) are mapped to the export or import stream target.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public class HarvesterDescriptor extends Descriptor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the character that separates multiple values for the same entry
   ** tag name.
   */
  static final String MULTIVALUE_SEPARATOR                = "multiValueSeparator";

  /**
   ** Attribute tag which must be defined on this Metadata Descriptor to hold
   ** the name of the attribute that is the risk attribute name of an attribute
   ** set.
   */
  public static final String RISK_LEVEL                   = "riskLevel";

  /**
   ** Attribute tag which must be defined on this Metadata Descriptor to hold
   ** the name of the attribute that is the delete status of an attribute set.
   */
  public static final String DELETE_STATUS                = "deleteStatus";

  /**
   ** Attribute tag which must be defined on this Metadata Descriptor to hold
   ** the name of the attribute that is the request status of an attribute set.
   */
  public static final String REQUEST_STATUS               = "requestStatus";

  /**
   ** Attribute tag which might be defined on this Metadata Descriptor to
   ** specify if warnings can be ignored.
   */
  public static final String IGNORE_WARNINGS              = "ignoreWarnings";

  /**
   ** Attribute tag which must be defined on this Metadata Descriptor to
   ** specify if the action to ensure unique names.
   */
  public static final String UNIQUE_NAME                  = "uniqueName";

  /**
   ** Attribute tag which must be defined on this Metadata Descriptor to
   ** specify how the telationship between policies and their entitlements has
   ** to be build.
   */
  public static final String RELATIONSHIP                 = "relationShip";

  /**
   ** Attribute tag which must be defined on this Metadata Descriptor to
   ** specify if the action to ensure unique entitlement sets for policies.
   */
  public static final String EXACT_MATCH                  = "exactMatch";

  /**
   ** Attribute tag which must be defined on this Metadata Descriptor to
   ** specify if the format of a date value.
   */
  public static final String DATE_FORMAT                  = "dateFormat";

  /**
   ** Attribute tag which might be defined on this Metadata Descriptor to
   ** specify if the format of a timestamp value.
   */
  public static final String TIMESTAMP_FORMAT             = "timestampFormat";

  /**
   ** Attribute tag which might be defined on this Metadata Descriptor to
   ** specify if the <code>null</code> value.
   */
  public static final String NULL_VALUE                   = "nullValue";

  /**
   ** Default value of the separator to specify multiple value for a
   ** configuration tag name.
   */
  public static final String DEFAULT_MULTIVALUE_SEPARATOR = "|";

  /** the default pattern for date values. */
  public static final String DEFAULT_DATE_FORMAT          = "yyyy-MM-dd";

  /** the default pattern for date values. */
  public static final String DEFAULT_TIMESTAMP_FORMAT     = "yyyy-MM-dd hh:mm:ss";

  /** the default value for null values. */
  public static final String DEFAULT_NULL_VALUE           = "null";

  /** the default pattern of the encoded value of an entitlement. */
  public static final String DEFAULT_PATTERN_ENCODED      = "%d~%s";

  /** the default pattern of the decoded value of an entitlement. */
  public static final String DEFAULT_PATTERN_DECODED      = "%s~%s";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the data direction of the harvester operation */
  private final boolean               inbound;

  private final Map<String, String[]> type          = new HashMap<String, String[]>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>HarvesterDescriptor</code> which is associated
   ** with the specified logging provider <code>loggable</code>.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Metadata Descriptor</code>
   **                            configuration wrapper.
   ** @param  inbound            <code>true</code> if this
   **                            <code>Harvester</code> is importing catalog
   **                            data; otherwise <code>false</code>.
   */
  public HarvesterDescriptor(final Loggable loggable, final boolean inbound) {
    // ensure inheritance
    super(loggable);

    // initialze instance attribtes
    this.inbound = inbound;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inbound
  /**
   ** Returns <code>true</code> if this {@link Descriptor} is used to import
   ** catalog data; otherwise <code>false</code>.
   **
   ** @return                    <code>true</code> if this {@link Descriptor} is
   **                            used to import catalog data; otherwise
   **                            <code>false</code>.
   */
  public final boolean inbound() {
    return this.inbound;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   outbound
  /**
   ** Returns <code>true</code> if this {@link Descriptor} is used to export
   ** catalog data; otherwise <code>false</code>.
   **
   ** @return                    <code>true</code> if this {@link Descriptor} is
   **                            used to export catalog data; otherwise
   **                            <code>false</code>.
   */
  public final boolean outbound() {
    return !this.inbound;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multiValueSeparator
  /**
   ** Sets separator character for Strings that provides more than one value.
   **
   ** @param  separator          separator sign for Strings that provides more
   **                            than one value.
   */
  public final void multiValueSeparator(final String separator) {
    this.put(MULTIVALUE_SEPARATOR, separator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multiValueSeparator
  /**
   ** Returns separator character for Strings that provides more than one value.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #MULTIVALUE_SEPARATOR}.
   ** <p>
   ** If {@link #MULTIVALUE_SEPARATOR} is not mapped in the underlying
   ** {@link Descriptor} this method returns
   ** {@link #DEFAULT_MULTIVALUE_SEPARATOR}.
   **
   ** @return                    separator sign for Strings that provides more
   **                            than one value.
   */
  public final String multiValueSeparator() {
    return stringValue(MULTIVALUE_SEPARATOR, DEFAULT_MULTIVALUE_SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   riskLevel
  /**
   ** Sets the attribute that is the risk level field of an attribute set.
   **
   ** @param  riskLevel          the attribute that is the risk level field of
   **                            an attribute set
   */
  public final void riskLevel(final String riskLevel) {
    this.put(RISK_LEVEL, riskLevel);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   riskLevel
  /**
   ** Returns the attribute that is the risk level field of an attribute set.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #RISK_LEVEL}.
   **
   ** @return                    the attribute that is the attribute that is the
   **                            risk level field of an attribute set.
   */
  public final String riskLevel() {
    return stringValue(RISK_LEVEL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteStatus
  /**
   ** Sets the attribute that is the delete status field of an attribute set.
   **
   ** @param  deleteStatus       the attribute that is the delete status field
   **                            of an attribute set.
   */
  public final void deleteStatus(final String deleteStatus) {
    this.put(DELETE_STATUS, deleteStatus);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteStatus
  /**
   ** Returns the attribute that is the delete status field of an attribute set.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #DELETE_STATUS}.
   **
   ** @return                    the attribute that is the attribute that is the
   **                            delete status of an attribute set.
   */
  public final String deleteStatus() {
    return stringValue(DELETE_STATUS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestStatus
  /**
   ** Sets the attribute that is the delete status field of an attribute set.
   **
   ** @param  requestStatus      the attribute that is the request status field
   **                            of an attribute set.
   */
  public final void requestStatus(final String requestStatus) {
    this.put(REQUEST_STATUS, requestStatus);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestStatus
  /**
   ** Returns the attribute that is the delete status field of an attribute set.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #REQUEST_STATUS}.
   **
   ** @return                    the attribute that is the attribute that is the
   **                            delete status of an attribute set.
   */
  public final String requestStatus() {
    return stringValue(REQUEST_STATUS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dateFormat
  /**
   ** Sets the attribute that is the format of date values.
   **
   ** @param  format             the attribute that is the format of date
   **                            values.
   */
  public final void dateFormat(final String format) {
    this.put(DATE_FORMAT, format);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dateFormat
  /**
   ** Returns the attribute that is the format of date values.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #DATE_FORMAT}.
   ** <br>
   ** If {@link #DATE_FORMAT} is not mapped in the underlying {@link Descriptor}
   ** this method returns {@link #DEFAULT_DATE_FORMAT}.
   **
   ** @return                    the attribute that is the format of date
   **                            values.
   */
  public final String dateFormat() {
    return stringValue(DATE_FORMAT, DEFAULT_DATE_FORMAT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timestampFormat
  /**
   ** Sets the attribute that is the format of timestamp values.
   **
   ** @param  format             the attribute that is the format of timestamp
   **                            values.
   */
  public final void timestampFormat(final String format) {
    this.put(TIMESTAMP_FORMAT, format);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timestampFormat
  /**
   ** Returns the attribute that is the format of timestamp values.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #TIMESTAMP_FORMAT}.
   ** <br>
   ** If {@link #TIMESTAMP_FORMAT} is not mapped in the underlying
   ** {@link Descriptor} this method returns {@link #DEFAULT_TIMESTAMP_FORMAT}.
   **
   ** @return                    the attribute that is the format of timestamp
   **                            values.
   */
  public final String timestampFormat() {
    return stringValue(TIMESTAMP_FORMAT, DEFAULT_TIMESTAMP_FORMAT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nullValue
  /**
   ** Sets the attribute value for <code>null</code>-values.
   **
   ** @param  value              the attribute value configured to detect
   **                            <code>null</code>-values.
   */
  public final void nullValue(final String value) {
    this.put(NULL_VALUE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nullValue
  /**
   ** Returns the attribute value configured to detect <code>null</code>-values.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #NULL_VALUE}.
   ** <br>
   ** If {@link #NULL_VALUE} is not mapped in the underlying
   ** {@link Descriptor} this method returns {@link #DEFAULT_NULL_VALUE}.
   **
   ** @return                    the attribute value configured to detect
   **                            <code>null</code>-values.
   */
  public final String nullValue() {
    return stringValue(NULL_VALUE, DEFAULT_NULL_VALUE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets a <code>String</code> object in the binding mapping of this wrapper.
   **
   ** @param  identifier         the name of an <code>IT Resource Type</code>
   **                            for the binding name string to set.
   ** @param  attributeName      the value associated with the specified
   **                            typeIdentifier.
   ** @param  encodedPattern     the pattern to apply at the encoded value.
   ** @param  decodedPattern     the pattern to apply at the decoded value.
   */
  public final void type(final String identifier, final String attributeName, final String encodedPattern, final String decodedPattern) {
    final String[] config = {
      attributeName
    , StringUtility.isEmpty(encodedPattern) ? DEFAULT_PATTERN_ENCODED : encodedPattern
    , StringUtility.isEmpty(decodedPattern) ? DEFAULT_PATTERN_DECODED : decodedPattern
    };
    this.type.put(identifier, config );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the mapping with names of <code>IT Resource Types</code> to one of
   ** their attributes.
   **
   ** @return                    the mapping with names of
   **                            <code>IT Resource Types</code> to one of their
   **                            attributes.
   */
  public Map<String, String[]> type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ignoreWarning
  /**
   ** Sets the attribute value that defines if warnings can be irgnored.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #IGNORE_WARNINGS}.
   **
   ** @param  ignoreWarning      the attribute value that defines if warnings
   **                            can be irgnored.
   */
  public final void ignoreWarning(final String ignoreWarning) {
    this.put(IGNORE_WARNINGS, ignoreWarning);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ignoreWarning
  /**
   ** Returns the attribute value that defines if warnings can be irgnored.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #IGNORE_WARNINGS}.
   **
   ** @return                    the attribute value that defines if warnings
   **                            can be irgnored.
   */
  public final boolean ignoreWarning() {
    return booleanValue(IGNORE_WARNINGS, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uniqueName
  /**
   ** Sets the encoded action string defining how to ensure unique names.
   **
   ** @param  encodedAction      the encoded action string defining how to
   **                            ensure unique names.
   */
  public final void uniqueName(final String encodedAction) {
    put(UNIQUE_NAME, encodedAction);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uniqueName
  /**
   ** Returns the encoded action string defining how to ensure unique names.
   **
   ** @return                    the encoded action string defining how to
   **                            ensure unique names.
   */
  public String uniqueName() {
    return stringValue(UNIQUE_NAME, "keepFirst");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   relationShip
  /**
   ** Sets the encoded action string defining how to set the telationship
   ** between policies and their entitlements.
   **
   ** @param  encodedAction      the encoded action string defining how to
   **                            set the telationship between policies and their
   **                            entitlements.
   */
  public final void relationShip(final String encodedAction) {
    put(RELATIONSHIP, encodedAction);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   relationShip
  /**
   ** Returns the encoded action string defining how to set the telationship
   ** between policies and their entitlements.
   **
   ** @return                    the encoded action string defining how to
   **                            set the telationship between policies and their
   **                            entitlements.
   */
  public String relationShip() {
    return stringValue(RELATIONSHIP, "many");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exactMatch
  /**
   ** Sets the encoded action string defining how to ensure entitlement match in
   ** policies.
   **
   ** @param  encodedAction      the encoded action string defining how to
   **                            ensure entitlement match in policies.
   */
  public final void exactMatch(final String encodedAction) {
    put(EXACT_MATCH, encodedAction);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exactMatch
  /**
   ** Returns the encoded action string defining how to ensure entitlement match
   ** in policies.
   **
   ** @return                    the mapping wich defined the action to perform
   **                            to ensure unique entitlement match in policies.
   */
  public String exactMatch() {
    return stringValue(EXACT_MATCH, "none");
  }
}