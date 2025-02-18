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

    File        :   Translator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    Translator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.grafana.schema;

import java.util.List;
import java.util.Collections;

import org.identityconnectors.framework.common.objects.ObjectClass;

import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.CompositeFilter;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;

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
 ** @author  dieter.steding@icloud.com
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
   **                                  {@link #translate(Filter)} method can
   **                                  return <code>null</code>. If this
   **                                  happens, it is the responsibility of the
   **                                  implementor to perform each query and
   **                                  combine the results. In order to
   **                                  eliminate duplicates, the implementation
   **                                  must keep an in-memory
   **                                  <code>HashSet</code> of those UID that
   **                                  have been visited thus far. This will
   **                                  not scale well if your result sets are
   **                                  large. Therefore it is
   **                                  <b>recommended</b> that if at all
   **                                  possible you implement
   **                                  {@link #translate(Filter)}.
   **                            </ol>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @throws RuntimeException    if filter syntax becomes invalid.
   */
  @Override
  public final List<String> translate(final Filter filter) {
    // prevent bogus input 
    if (filter == null)
      return Collections.emptyList();

    // TODO: implemente
    return Collections.emptyList();
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
   ** Factory method to create a Grafana serach filter criteria.
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
}