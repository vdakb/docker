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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Red Hat Keycloak Connector

    File        :   Translator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Translator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.schema;

import java.util.List;
import java.util.Collections;

import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationalAttributeInfos;

import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.OrFilter;
import org.identityconnectors.framework.common.objects.filter.AndFilter;
import org.identityconnectors.framework.common.objects.filter.NotFilter;
import org.identityconnectors.framework.common.objects.filter.AttributeFilter;
import org.identityconnectors.framework.common.objects.filter.CompositeFilter;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;

import oracle.iam.identity.icf.foundation.utility.SchemaUtility;

import oracle.iam.identity.icf.rest.ServiceError;
import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.resource.ServiceBundle;

////////////////////////////////////////////////////////////////////////////////
// final class Translator
// ~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** OIG implementation to translate ICF Filter in the Keycloak Server search
 ** criteria syntax.
 ** <p>
 ** A search filter may contain operators (such as 'contains' or 'in') or may
 ** contain logical operators (such as 'AND', 'OR' or 'NOT') that a connector
 ** cannot implement using the native API of the target system or application. A
 ** connector developer should subclass <code>Translator</code> in order to
 ** declare which filter operations the connector does support. This allows
 ** the <code>Translator</code> instance to analyze a specified search filter
 ** and reduce the filter to its most efficient form. The default (and
 ** worst-case) behavior is to return a <code>null</code> expression, which
 ** means that the connector should return "everything" (that is, should return
 ** all values for every requested attribute) and rely on the common code in the
 ** framework to perform filtering. This "fallback" behavior is good (in that it
 ** ensures consistency of search behavior across connector implementations) but
 ** it is obviously better for performance and scalability if each connector
 ** performs as much filtering as the native API of the target can support.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Translator implements FilterTranslator<String> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ObjectClass type;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a Keycloak filter <code>Translator</code> for the specified
   ** {@link ObjectClass} <code>type</code>.
   **
   ** @param  type               the {@link ObjectClass} to translate the filter
   **                            createria for.
   **                            <br>
   **                            Allowed object is {@link ObjectClass};
   */
  private Translator(final ObjectClass type) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.type = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of impelmented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   translate
  /**
   ** Main method to be called to translate a filter.
   **
   ** @param  filter             the {@link Filter} to translate.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    the list of queries to be performed.
   **                            The list <code>size()</code> may be one of the
   **                            following:
   **                            <ol>
   **                              <li>0 - This signifies <b>fetch everything</b>.
   **                                  This may occur if your filter was
   **                                  <code>null</code> or one of your
   **                                  <code>create*</code> methods returned
   **                                  <code>null</code>.
   **                              <li>1 - List contains a single query that
   **                                  will return the results from the filter.
   **                                  Note that the results may be a
   **                                  <b>superset</b> of those specified by
   **                                  the filter in the case that one of your
   **                                  <code>expression</code> methods returned
   **                                  <code>null</code>. However it is
   **                                  undesirable from a performance
   **                                  standpoint.
   **                              <li>&lt;1 - List contains multiple queries
   **                                  that must be performed in order to meet
   **                                  the filter that was passed in.
   **                                  Note that this only occurs if your
   **                                  {@link #expression(CompositeFilter)}
   **                                  method can return <code>null</code>. If
   **                                  this happens, it is the responsibility
   **                                  of the implementor to perform each query
   **                                  and combine the results. In order to
   **                                  eliminate duplicates, the
   **                                  implementation must keep an in-memory
   **                                  <code>HashSet</code> of those UID that
   **                                  have been visited thus far. This will
   **                                  not scale well if your result sets are
   **                                  large. Therefore it is
   **                                  <b>recommended</b> that if at all
   **                                  possible you implement
   **                                  {@link #expression(CompositeFilter)}.
   **                            </ol>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @throws RuntimeException    if filter syntax becomes invalid.
   */
  @Override
  public final List<String> translate(Filter filter) {
    // prevent bogus input 
    if (filter == null)
      return Collections.emptyList();

    try {
      // this must come first
      filter = simplify(normalize(filter));
      // might have simplified it to the everything filter
      return (filter == null) ? Collections.emptyList() : Collections.singletonList(translateInternal(filter));
    }
    catch (Exception e) {
      throw new RuntimeException(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a ICF to Keycloak filter translator.
   **
   ** @param  type               the {@link ObjectClass} to translate the filter
   **                            createria for.
   **                            <br>
   **                            Allowed object is {@link ObjectClass};
   **
   ** @return                    the filter translator applicable.
   **                            <br>
   **                            Possible object is <code>Translator</code>.
   */
  public static Translator build(final ObjectClass type) {
    return new Translator(type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   criteria
  /**
   ** Factory method to create a Keycloak serach filter criteria.
   **
   ** @param  name               the name of the attribute to apply the filter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the filter value to apply.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link String} populated with the
   **                            specified properties.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static  String criteria(final String name, final String value) {
    return name.concat(":").concat(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalize
  /**
   ** Pushes Not's so that they are just before the leaves of the tree
   */
  private Filter normalize(final Filter filter) {
    if (filter instanceof AndFilter) {
      AndFilter af = (AndFilter)filter;
      return new AndFilter(normalize(af.getLeft()), normalize(af.getRight()));
    }
    else if (filter instanceof OrFilter) {
      OrFilter of = (OrFilter)filter;
      return new OrFilter(normalize(of.getLeft()), normalize(of.getRight()));
    }
    else if (filter instanceof NotFilter) {
      NotFilter nf = (NotFilter)filter;
      return negate(normalize(nf.getFilter()));
    }
    else {
      return filter;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   simplify
  /**
   ** Simultaneously prunes those portions of the filter than cannot be
   ** implemented and distributes Ands over Ors where needed if the resource
   ** does not implement Or.
   **
   ** @param  filter              {@link Not}s must already be normalized.
   **
   ** @return                     a simplified filter or <code>null</code> to
   **                             represent the "everything" filter.
   **
   ** @throws ServiceException     if filter syntax becomes invalid.
   */
  private Filter simplify(final Filter filter)
    throws ServiceException {

    if (filter instanceof AndFilter) {
      final AndFilter af  = (AndFilter)filter;
      final Filter    lhs = simplify(af.getLeft());
      final Filter    rhs = simplify(af.getRight());
      if (lhs == null) {
        // left is "everything" - just return the right
        return rhs;
      }
      else if (rhs == null) {
        // right is "everything" - just return the left
        return lhs;
      }
      else {
        // simulate translation of the left and right
        // to see where we end up
        final String lex = translateInternal(lhs);
        final String rex = translateInternal(rhs);
        if (lex.isEmpty())
          // this can happen only when one of the create* methods is
          // inconsistent from one invocation to the next (simplifiedLeft should
          // have been null in the previous 'if' above).
          throw ServiceException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_METHOD_INCONSISTENT, lex));

        if (rex.isEmpty())
          // this can happen only when one of the create* methods is
          // inconsistent from one invocation to the next (simplifiedRight
          // should have been null in the previous 'if' above).
          throw ServiceException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_METHOD_INCONSISTENT, rex));

        return new AndFilter(lhs, rhs);
      }
    }
    else if (filter instanceof OrFilter) {
      final OrFilter of  = (OrFilter)filter;
      final Filter   lhs = simplify(of.getLeft());
      final Filter   rhs = simplify(of.getRight());
      // if either left or right reduces to "everything", then simplify the OR
      // to "everything"
      return (lhs == null || rhs == null)  ? null : new OrFilter(lhs, rhs);
    }
    else {
      // otherwise, it's a NOT(LEAF) or a LEAF by simulate creating it
      final String exp = expression((AttributeFilter)filter);
      return (exp == null || exp.isEmpty()) ? null : filter;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   negate
  /**
   ** Given a filter, create a filter representing its negative. This is used
   ** by normalize.
   **
   ** @param  filter             a {@link Filter} (normalized and simplified).
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    the {@link Filter} negated.
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  private Filter negate(final Filter filter) {
    if (filter instanceof AndFilter) {
      final AndFilter af = (AndFilter)filter;
      return new OrFilter(negate(af.getLeft()), negate(af.getRight()));
    }
    else if (filter instanceof OrFilter) {
      final OrFilter of = (OrFilter)filter;
      return new AndFilter(negate(of.getLeft()), negate(of.getRight()));
    }
    else if (filter instanceof NotFilter) {
      final NotFilter nf = (NotFilter)filter;
      return nf.getFilter();
    }
    else {
      return new NotFilter(filter);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   translateInternal
  /**
   ** Translates the filter into a list of expressions.
   ** <p>
   ** The filter must have already been transformed using
   ** {@link #normalize(Filter)} followed by a {@link #simplify(Filter)}.
   **
   ** @param  filter             a {@link Filter} (normalized and simplified).
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    a collection of expressions or empty
   **                            collection for everything.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @throws ServiceException    if filter syntax becomes invalid.
   */
  private String translateInternal(final Filter filter)
    throws ServiceException {

    if (filter instanceof AndFilter) {
      return expression((AndFilter)filter);
    }
    else if (filter instanceof OrFilter) {
      return expression((OrFilter)filter);
    }
    else {
      // otherwise it's a leaf but never a NOT (leaf)
      return expression((AttributeFilter)filter);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expression
  /**
   ** Translates the {@link CompositeFilter} filter into a string expression.
   ** <p>
   ** The filter must have already been transformed using
   ** {@link #normalize(Filter)} followed by a {@link #simplify(Filter)}.
   **
   ** @param  filter             an {@link And} filter (normalized, simplified,
   **                            and distibuted).
   **                            <br>
   **                            Allowed object is {@link CompositeFilter}.
   **
   ** @return                    a collection of expressions or empty
   **                            collection for everything.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type{@link String}.
   **
   ** @throws ServiceException    if filter syntax becomes invalid.
   */
  private String expression(final CompositeFilter filter)
    throws ServiceException {

    final String lhs = translateInternal(filter.getLeft());
    final String rhs = translateInternal(filter.getRight());
    if (lhs.isEmpty())
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed).
      throw ServiceException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_METHOD_INCONSISTENT, lhs));

    if (rhs.isEmpty()) {
      // this can happen only if one of the create* methods
      // is inconsistent from one invocation to the next
      // (at this point we've already been simplified and
      // distributed).
      throw ServiceException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_METHOD_INCONSISTENT, rhs));
    }
    return lhs + " " +rhs;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expression
  /**
   ** Factory method to create a filter leaf expression
   **
   ** @param  filter             a filter (normalized, simplified, and
   **                            distibuted).
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    the translated filter expression or
   **                            <code>null</code> (for everything).
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  private String expression(final AttributeFilter filter) {
    return expression(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), String.class));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expression
  /**
   ** Factory method to create a Keycloak serach filter expression.
   **
   ** @param  name               the name of the attribute to apply the filter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the filter value to apply.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the {@link String} populated with the
   **                            specified properties.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private String expression(final String name, final String value) {
    if (Name.NAME.equals(name)) {
      if (this.type.equals(ObjectClass.ACCOUNT))
        return criteria(Service.USERNAME, value);
      else if (this.type.equals(ObjectClass.GROUP))
        return criteria(Service.NAME, value);
      else if (this.type.equals(Keycloak.ROLE.clazz))
        return criteria(Service.NAME, value);
    }
    else if (OperationalAttributeInfos.ENABLE.equals(name)) {
      if (this.type.equals(ObjectClass.ACCOUNT))
        return criteria(Service.STATUS, value);
    }
    return criteria(name, value);
  }
}