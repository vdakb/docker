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
    Subsystem   :   Generic Directory Connector

    File        :   DirectoryFilterTranslator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryFilterTranslator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import javax.naming.ldap.Rdn;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;

import oracle.iam.identity.icf.foundation.object.filter.Translator;

import oracle.iam.identity.icf.foundation.utility.SchemaUtility;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryFilterTranslator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>DirectoryFilter</code> is used to instantiate a new filter criteria
 ** that will match any ConnectorObject or EmbeddedObject that satisfies all of
 ** the selection criteria that were specified using this filter.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryFilterTranslator extends Translator<DirectoryFilter> {

  //////////////////////////////////////////////////////////////////////////////
  // instane attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ObjectClass     type;
  private final DirectorySchema schema;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryFilterTranslator</code> with the specified
   ** schema mapping belonging to a specific object class.
   **
   ** @param  schema             the schema mapping to resolve and translate
   **                            object class names and their attributes.
   **                            <br>
   **                            Allowed object is {@link DirectorySchema}.
   ** @param  type               the {@link ObjectClass} this filter belongs to.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   */
  private DirectoryFilterTranslator(final DirectorySchema schema, final ObjectClass type) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.schema = schema;
    this.type   = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DirectoryFilter</code> with the
   ** specified schema mapping belonging to a specific object class.
   ** flags.
   **
   ** @param  schema             the schema mapping to resolve and translate
   **                            object class names and their attributes.
   **                            <br>
   **                            Allowed object is {@link DirectorySchema}.
   ** @param  objectClass        the {@link ObjectClass} this filter belongs to.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   **
   ** @return                    an instance of <code>DirectoryFilter</code>
   **                            with the specified properties.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public static DirectoryFilterTranslator build(final DirectorySchema schema, final ObjectClass objectClass) {
    return new DirectoryFilterTranslator(schema, objectClass);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and (overridden)
  /**
   ** Factory method to creates an <code>AND</code> filter expression composed
   ** by both provided filter expressions.
   **
   ** @param  lhs                the left hand side LDAP syntax of an attribute
   **                            filter.
   **                            <br>
   **                            Allowed object is {@link DirectoryFilter}.
   ** @param  rhs                the right hand side LDAP syntax of an attribute
   **                            filter.
   **                            <br>
   **                            Allowed object is {@link DirectoryFilter}.
   **
   ** @return                    the LDAP syntax of a <code>AND</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  @Override
  public DirectoryFilter and(final DirectoryFilter lhs, final DirectoryFilter rhs) {
    return lhs.and(rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   or (overridden)
  /**
   ** Factory method to creates an <code>OR</code> filter expression composed by
   ** both provided filter expressions.
   **
   ** @param  lhs                the left hand side LDAP syntax of an attribute
   **                            filter.
   **                            <br>
   **                            Allowed object is {@link DirectoryFilter}.
   ** @param  rhs                the right hand side LDAP syntax of an attribute
   **                            filter.
   **                            <br>
   **                            Allowed object is {@link DirectoryFilter}.
   **
   ** @return                    the LDAP syntax of a <code>OR</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  @Override
  public DirectoryFilter or(final DirectoryFilter lhs, final DirectoryFilter rhs) {
    return lhs.or(rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pr
  /**
   ** Factory method to creates a <code>PRESENT</code> filter expression.
   **
   ** @param  lhs                the ICF syntax of a attribute filter.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the LDAP syntax of a <code>PRESENT</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter pr(final Attribute lhs) {
    return pr(lhs, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pr
  /**
   ** Factory method to creates a <code>PRESENT</code> filter expression that
   ** might be negated regarding <code>not</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a <code>PRESENT</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter pr(final Attribute filter, final boolean not) {
    return pr(filter.getName(), not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pr
  /**
   ** Factory method to creates a <code>PRESENT</code> filter expression.
   **
   ** @param  filter             the JNDI syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   **
   ** @return                    the LDAP syntax of a <code>PRESENT</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter pr(final DirectoryName filter) {
    return pr(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pr
  /**
   ** Factory method to creates a <code>PRESENT</code> filter expression that
   ** might be negated regarding <code>not</code>.
   **
   ** @param  filter             the JNDI syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a <code>PRESENT</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter pr(final DirectoryName filter, final boolean not) {
    final Rdn rdn = filter.getRdn(0);
    return eq(rdn.getType(), Rdn.escapeValue(rdn.getValue()), not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pr
  /**
   ** Creates an <code>PRESENT</code> filter expression.
   **
   ** @param  lhs                the ICF syntax of a attribute filter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the LDAP syntax of a  <code>PRESENT</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter pr(final String lhs) {
    return pr(lhs, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pr
  /**
   ** Factory method to creates a <code>PRESENT</code> filter expression that
   ** might be negated regarding <code>not</code>.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a <code>PRESENT</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter pr(final String prefix, final boolean not) {
    return present(prefix, not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to creates an <code>EQUALS</code> filter expression.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the LDAP syntax of a <code>EQUALS</code> filter
   **                            expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter eq(final Attribute filter) {
    return eq(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq (overridden)
  /**
   ** Factory method to creates an <code>EQUALS</code> filter expression that
   ** might be negated regarding <code>not</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a <code>EQUALS</code> filter
   **                            expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  @Override
  public DirectoryFilter eq(final Attribute filter, final boolean not) {
    return eq(filter.getName(), SchemaUtility.singleValue(filter, String.class), not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to creates an <code>EQUALS</code> filter expression.
   **
   ** @param  filter             the JNDI syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   **
   ** @return                    the LDAP syntax of a <code>EQUALS</code> filter
   **                            expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter eq(final DirectoryName filter) {
    return eq(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to creates an <code>EQUALS</code> filter expression that
   ** might be negated regarding <code>not</code>.
   **
   ** @param  filter             the JNDI syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a <code>EQUALS</code> filter
   **                            expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter eq(final DirectoryName filter, final boolean not) {
    final Rdn rdn = filter.getRdn(0);
    return eq(rdn.getType(), Rdn.escapeValue(rdn.getValue()), not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to creates an <code>EQUALS</code> filter expression.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the LDAP syntax of a <code>EQUALS</code> filter
   **                            expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter eq(final String prefix, final String value) {
    return eq(prefix, value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to creates an <code>EQUALS</code> filter expression that
   ** might be negated regarding <code>not</code>.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a <code>EQUALS</code> filter
   **                            expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter eq(final String prefix, final String value, final boolean not) {
    return compose("=", prefix, value, not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to creates a <code>LESS-THAN</code> filter expression.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the LDAP syntax of a <code>LESS-THAN</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter lt(final Attribute filter) {
    return lt(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt (overridden)
  /**
   ** Factory method to creates a <code>LESS-THAN</code> filter expression that
   ** might be negated regarding <code>not</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a <code>LESS-THAN</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  @Override
  public DirectoryFilter lt(final Attribute filter, final boolean not) {
    return lt(filter.getName(), SchemaUtility.singleValue(filter, String.class), not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to creates a <code>LESS-THAN</code> filter expression.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the LDAP syntax of a <code>LESS-THAN</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter lt(final String prefix, final String value) {
    return lt(prefix, value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to creates a <code>LESS-THAN</code> filter expression that
   ** might be negated regarding <code>not</code>.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a <code>LESS-THAN</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter lt(final String prefix, final String value, final boolean not) {
    return compose("<", prefix, value, not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to creates a <code>LESS-THAN_OR-EQUALS</code> filter
   ** expression.
   **
   ** @param  filter             the ICF syntax of a attribute filter.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the LDAP syntax of a
   **                            <code>LESS-THAN_OR-EQUALS</code> filter
   **                            expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter le(final Attribute filter) {
    return le(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to creates a <code>LESS-THAN_OR-EQUALS</code> filter
   ** expression that might be negated regarding <code>not</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a
   **                            <code>LESS-THAN_OR-EQUALS</code> filter
   **                            expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter le(final Attribute filter, final boolean not) {
    return le(filter.getName(), SchemaUtility.singleValue(filter, String.class), not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to creates a <code>LESS-THAN_OR-EQUALS</code> filter
   ** expression.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the LDAP syntax of a
   **                            <code>LESS-THAN_OR-EQUALS</code> filter
   **                            expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter le(final String prefix, final String value) {
    return le(prefix, value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to creates a <code>LESS-THAN_OR-EQUALS</code> filter
   ** expression that might be negated regarding <code>not</code>.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a
   **                            <code>LESS-THAN_OR-EQUALS</code> filter
   **                            expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter le(final String prefix, final String value, final boolean not) {
    return compose("<=", prefix, value, not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to creates a <code>GREATER-THAN</code> filter expression.
   **
   ** @param  filter             the ICF syntax of a attribute filter.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the LDAP syntax of a <code>GREATER-THAN</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter gt(final Attribute filter) {
    return gt(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt (overridden)
  /**
   ** Factory method to creates a <code>GREATER-THAN</code> filter expression
   ** that might be negated regarding <code>not</code>.
   **
   ** @param  filter             the ICF syntax of a attribute filter.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a <code>GREATER-THAN</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  @Override
  public DirectoryFilter gt(final Attribute filter, final boolean not) {
    return gt(filter.getName(), SchemaUtility.singleValue(filter, String.class), not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to creates a <code>GREATER-THAN</code> filter expression.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the LDAP syntax of a <code>GREATER-THAN</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter gt(final String prefix, final String value) {
    return gt(prefix, value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to creates a <code>GREATER-THAN</code> filter expression
   ** that might be negated regarding <code>not</code>.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a <code>GREATER-THAN</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter gt(final String prefix, final String value, final boolean not) {
    return compose(">", prefix, value, not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to creates a <code>GREATER-THAN-OR-EQUALS</code> filter
   ** expression.
   **
   ** @param  filter             the ICF syntax of a attribute filter.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the LDAP syntax of a
   **                            <code>GREATER-THAN-OR-EQUALS</code> filter
   **                            expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter ge(final Attribute filter) {
    return ge(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge (overridden)
  /**
   ** Factory method to creates a <code>GREATER-THAN-OR-EQUALS</code> filter
   ** expression that might be negated regarding <code>not</code>.
   **
   ** @param  filter             the ICF syntax of a attribute filter.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a
   **                            <code>GREATER-THAN-OR-EQUALS</code> filter
   **                            expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  @Override
  public DirectoryFilter ge(final Attribute filter, final boolean not) {
    return ge(filter.getName(), SchemaUtility.singleValue(filter, String.class), not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to creates a <code>GREATER-THAN-OR-EQUALS</code> filter
   ** expression.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the LDAP syntax of a
   **                            <code>GREATER-THAN-OR-EQUALS</code> filter
   **                            expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter ge(final String prefix, final String value) {
    return ge(prefix, value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to creates a <code>GREATER-THAN-OR-EQUALS</code> filter
   ** expression that might be negated regarding <code>not</code>.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a
   **                            <code>GREATER-THAN-OR-EQUALS</code> filter
   **                            expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter ge(final String prefix, final String value, final boolean not) {
    return compose(">=", prefix, value, not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw
  /**
   ** Factory method to creates a <code>STARTS-WITH</code> filter expression.
   **
   ** @param  filter             the ICF syntax of a attribute filter.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the LDAP syntax of a <code>STARTS-WITH</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter sw(final Attribute filter) {
    return sw(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw (overridden)
  /**
   ** Factory method to creates a <code>STARTS-WITH</code> filter expression
   ** that might be negated regarding <code>not</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a <code>STARTS-WITH</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  @Override
  public DirectoryFilter sw(final Attribute filter, final boolean not) {
    return sw(filter.getName(), SchemaUtility.singleValue(filter, String.class), not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw
  /**
   ** Factory method to creates a <code>STARTS-WITH</code> filter expression.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the LDAP syntax of a <code>STARTS-WITH</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter sw(final String prefix, final String value) {
    return sw(prefix, value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw
  /**
   ** Factory method to creates a <code>STARTS-WITH</code> filter expression
   ** that might be negated regarding <code>not</code>.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a <code>STARTS-WITH</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter sw(final String prefix, final String value, final boolean not) {
    final String name = this.schema.attribute(this.type, prefix);
    if (name == null) {
      return null;
    }
//    if (this.context.endpoint().dis(name)) {
//      return LdapFilter.forEntryDN(filter.getValue().toString());
//    }

    final StringBuilder builder = open(not);
    builder.append(name).append('=').append(value).append('*');
    return close(builder);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew
  /**
   ** Factory method to creates a <code>ENDS-WITH</code> filter expression.
   **
   ** @param  filter             the ICF syntax of a attribute filter.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the LDAP syntax of a <code>ENDS-WITH</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter ew(final Attribute filter) {
    return ew(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew (overridden)
  /**
   ** Factory method to creates a <code>ENDS-WITH</code> filter expression that
   ** might be negated regarding <code>not</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a <code>ENDS-WITH</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  @Override
  public DirectoryFilter ew(final Attribute filter, final boolean not) {
    return ew(filter.getName(), SchemaUtility.singleValue(filter, String.class), not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew
  /**
   ** Factory method to creates a <code>ENDS-WITH</code> filter expression.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the LDAP syntax of a <code>ENDS-WITH</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter ew(final String prefix, final String value) {
    return ew(prefix, value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew
  /**
   ** Factory method to creates a <code>ENDS-WITH</code> filter expression that
   ** might be negated regarding <code>not</code>.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a <code>ENDS-WITH</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter ew(final String prefix, final String value, final boolean not) {
    final String name = this.schema.attribute(this.type, prefix);
    if (name == null) {
      return null;
    }
//    if (LdapEntry.isDNAttribute(name)) {
//      return LdapFilter.forEntryDN(filter.getValue().toString());
//    }

    final StringBuilder builder = open(not);
    builder.append(name).append('=').append('*').append(value);
    return close(builder);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co
  /**
   ** Factory method to creates a <code>CONTAINS</code> filter expression.
   **
   ** @param  filter             the ICF syntax of a attribute filter.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the LDAP syntax of a <code>CONTAINS</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter co(final Attribute filter) {
    return co(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co (overridden)
  /**
   ** Factory method to creates a <code>CONTAINS</code> filter expression that
   ** might be negated regarding <code>not</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a <code>CONTAINS</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  @Override
  public DirectoryFilter co(final Attribute filter, final boolean not) {
    return co(filter.getName(), SchemaUtility.singleValue(filter, String.class), not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co
  /**
   ** Factory method to creates a <code>CONTAINS</code> filter expression.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the LDAP syntax of a <code>CONTAINS</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter co(final String prefix, final String value) {
    return co(prefix, value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co
  /**
   ** Factory method to creates a <code>CONTAINS</code> filter expression that
   ** might be negated regarding <code>not</code>.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a <code>CONTAINS</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  public DirectoryFilter co(final String prefix, final String value, final boolean not) {
    final String name = this.schema.attribute(this.type, prefix);
    if (name == null) {
      return null;
    }
//    if (LdapEntry.isDNAttribute(name)) {
//      return LdapFilter.forEntryDN(filter.getValue().toString());
//    }

    final StringBuilder builder = open(not);
    builder.append(name).append('=').append('*').append(value).append('*');
    return close(builder);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   present
  /**
   ** This method gives the filter expression for the given filter.
   **
   ** @param  attribute          the ICF syntax of a attribute filter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a <code>PRESENT</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  private DirectoryFilter present(final String attribute, final boolean not) {
    final String prefix = this.schema.attribute(this.type, attribute);
    if (prefix == null)
      return null;

    final StringBuilder builder = open(not).append(prefix).append('=').append('*');
    return close(builder);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compose
  /**
   ** This method gives the filter expression for the given operator and filter.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LDAP syntax of a negated
   **                            <code>present</code> filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}.
   */
  private DirectoryFilter compose(final String operator, final String prefix, final String value, final boolean not) {
    final String name = this.schema.attribute(this.type, prefix);
    if (name == null) {
      return null;
    }
//    if (LdapEntry.isDNAttribute(name)) {
//      return LdapFilter.forEntryDN(filter.getValue().toString());
//    }

    final StringBuilder builder = open(not);
    compose(name, operator, value, builder);
    return close(builder);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   open
  /**
   ** Creates the opening part of a filter expression.
   **
   ** @param  not                <code>true</code> if the filter expression
   **                            negates.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the filter expression.
   **                            <br>
   **                            Possible object is {@link StringBuilder}
   */
  private StringBuilder open(final boolean not) {
    return new StringBuilder(not ? "(!(" : "(");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Creates the closing part of a filter expression.
   **
   ** @param  builder            the value holder receiving the transformed
   **                            data.
   **                            <br>
   **                            Allowed object is {@link StringBuilder}.
   **
   ** @return                    the filter expression.
   **                            <br>
   **                            Possible object is {@link DirectoryFilter}
   */
  private DirectoryFilter close(final StringBuilder builder) {
    boolean not = (builder.charAt(0) == '(') && (builder.charAt(1) == '!');
    builder.append(not ? "))" : ")");
    return DirectoryFilter.expression(builder.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compose
  /**
   ** This method gives the filter expression for the given prefix, operator and
   ** value.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  operator           the ....
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  builder            the value holder receiving the transformed
   **                            data.
   **                            <br>
   **                            Allowed object is {@link StringBuilder}.
   */
  private void compose(final String prefix, final String operator, final Object value, final StringBuilder builder) {
    builder.append(prefix);
    builder.append(operator);
    if (value.toString().matches("[0-9A-Fa-f]+")) {
      String escapedString = DirectoryEntry.escape(value.toString());
      builder.append(escapedString);
    }
    else if (!DirectoryEntry.escapeValue(value, builder)) {
      builder.append('*');
    }
  }
}