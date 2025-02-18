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
    Subsystem   :   Identity Governance Connector

    File        :   Translator.java
    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Translator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-21  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.oig.schema;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.io.Serializable;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationalAttributeInfos;

import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.OrFilter;
import org.identityconnectors.framework.common.objects.filter.AndFilter;
import org.identityconnectors.framework.common.objects.filter.NotFilter;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;
import org.identityconnectors.framework.common.objects.filter.ContainsFilter;
import org.identityconnectors.framework.common.objects.filter.EndsWithFilter;
import org.identityconnectors.framework.common.objects.filter.LessThanFilter;
import org.identityconnectors.framework.common.objects.filter.StartsWithFilter;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;
import org.identityconnectors.framework.common.objects.filter.GreaterThanFilter;
import org.identityconnectors.framework.common.objects.filter.LessThanOrEqualFilter;
import org.identityconnectors.framework.common.objects.filter.GreaterThanOrEqualFilter;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;

import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;

import oracle.iam.identity.icf.foundation.utility.SchemaUtility;

import oracle.iam.identity.icf.jes.ServerError;
import oracle.iam.identity.icf.jes.ServerException;

import oracle.iam.identity.icf.resource.ServerBundle;

////////////////////////////////////////////////////////////////////////////////
// class Translator
// ~~~~~ ~~~~~~~~~~
/**
 ** OIG implementation to translate ICF Filter in the Identity Governance 
 ** SearchCriteria syntax.
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
public class Translator implements FilterTranslator<SearchCriteria> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ObjectClass type;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Switcher
  // ~~~~ ~~~~~~~~
  /**
   ** Constraint type that stores the class names as field.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Any {@link Filter} that represents an <code>AttributeFilter</code>
   ** <b>must</b> be enlisted here to avoid NPE's in the translation algorithm.
   */
  public static enum Switcher {
      EQ(EqualsFilter.class)
    , GE(GreaterThanOrEqualFilter.class)
    , GT(GreaterThanFilter.class)
    , LE(LessThanOrEqualFilter.class)
    , LT(LessThanFilter.class)
    , EW(EndsWithFilter.class)
    , SW(StartsWithFilter.class)
    , CO(ContainsFilter.class)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    private static final Map<String, Switcher> classEnum = new HashMap<String, Switcher>();

    ////////////////////////////////////////////////////////////////////////////
    // static init bloack
    ////////////////////////////////////////////////////////////////////////////

    static {
      for (Switcher sw : values()) {
        classEnum.put(sw.className, sw);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String className;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Switcher</code> with a constraint value.
     **
     ** @param  value            the class type of the object.
     **                          <br>
     **                          Allowed object is {@link Class}.
     */
    Switcher(Class<?> clazz) {
      this.className = clazz.getName();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Switcher</code> constraint from
     ** the given string value.
     **
     ** @param  className        the string value the type constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Switcher</code> constraint.
     **                          <br>
     **                          Possible object is <code>Switcher</code>.
     */
    public static Switcher from(final String className) {
      return classEnum.get(className);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a OIG filter <code>Translator</code> for the specified
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
   ** @param  filter              the {@link Filter} to translate.
   **
   ** @return                     the list of queries to be performed.
   **                             The list <code>size()</code> may be one of the
   **                             following:
   **                             <ol>
   **                               <li>0 - This signifies <b>fetch everything</b>.
   **                                   This may occur if your filter was
   **                                   <code>null</code> or one of your
   **                                   <code>create*</code> methods returned
   **                                   <code>null</code>.
   **                               <li>1 - List contains a single query that
   **                                   will return the results from the filter.
   **                                   Note that the results may be a
   **                                   <b>superset</b> of those specified by
   **                                   the filter in the case that one of your
   **                                   <code>create*</code> methods returned
   **                                   <code>null</code>. However it is
   **                                   undesirable from a performance
   **                                   standpoint.
   **                               <li>&lt;1 - List contains multiple queries
   **                                   that must be performed in order to meet
   **                                   the filter that was passed in.
   **                                   Note that this only occurs if your
   **                                   {@link #createOR} method can return
   **                                   <code>null</code>. If this happens, it
   **                                   is the responsibility of the
   **                                   implementor to perform each query and
   **                                   combine the results. In order to
   **                                   eliminate duplicates, the
   **                                   implementation must keep an in-memory
   **                                   <code>HashSet</code> of those UID that
   **                                   have been visited thus far. This will
   **                                   not scale well if your result sets are
   **                                   large. Therefore it is
   **                                   <b>recommended</b> that if at all
   **                                   possible you implement
   **                                   {@link #createOR}.
   **                             </ol>
   **
   ** @throws RuntimeException    if filter syntax becomes invalid.
   */
  @Override
  public final List<SearchCriteria> translate(Filter filter) {
    if (filter == null)
      return new ArrayList<SearchCriteria>();

    try {
      // this must come first
      filter = simplify(normalize(filter));
      // might have simplified it to the everything filter
      return (filter == null) ? new ArrayList<SearchCriteria>() : translateInternal(filter);
    }
    catch (ServerException e) {
      throw new RuntimeException(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a ICF to OIG filter translator.
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
  // Method:   createAND
  /**
   ** Factory method to create a OIG <code>AND</code> filter expression.
   **
   ** @param  lhs                the left hand side OIG <code>AND</code> filter
   **                            expression.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link SearchCriteria}.
   ** @param  rhs                the right hand side OIG <code>AND</code> filter
   **                            expression.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link SearchCriteria}.
   **
   ** @return                    the OIG <code>AND</code> expression.
   **                            A return value of <code>null</code> means a
   **                            native AND query cannot be created for the
   **                            given expressions. In this case,
   **                            {@link #translate} may return multiple queries,
   **                            each of which must be run and results combined.
   **                            <br>
   **                            Possible object is {@link SearchCriteria}.
   */
  protected SearchCriteria createAND(final SearchCriteria lhs, final SearchCriteria rhs) {
    return new SearchCriteria(lhs, rhs, SearchCriteria.Operator.AND);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOR (ServiceFilterTranslator)
  /**
   ** Factory method to create a OIG <code>OR</code> filter expression.
   **
   ** @param  lhs                the left hand side OIG <code>OR</code> filter
   **                            expression.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link SearchCriteria}.
   ** @param  rhs                the right hand side OIG <code>OR</code> filter
   **                            expression.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link SearchCriteria}.
   **
   ** @return                    the OIG <code>OR</code> expression.
   **                            A return value of <code>null</code> means a
   **                            native OR query cannot be created for the
   **                            given expressions. In this case,
   **                            {@link #translate} may return multiple queries,
   **                            each of which must be run and results combined.
   **                            <br>
   **                            Possible object is {@link SearchCriteria}.
   */
  protected SearchCriteria createOR(final SearchCriteria lhs, final SearchCriteria rhs) {
    return new SearchCriteria(lhs, rhs, SearchCriteria.Operator.OR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEQ
  /**
   ** Factory method to create a OIG <code>EQUAL</code> filter expression.
   **
   ** @param  filter             the ICF <code>equal</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EqualsFilter}.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            EQUALS.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the OIG <code>EQUAL</code> filter
   **                            expression.
   **                            A return value of <code>null</code> means a
   **                            native EQUALS query cannot be created for
   **                            the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   **                            <br>
   **                            Possible object is {@link SearchCriteria}.
   */
  protected SearchCriteria createEQ(final EqualsFilter filter, final boolean not) {
    return not
         ? createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), String.class), SearchCriteria.Operator.NOT_EQUAL)
         : createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), String.class), SearchCriteria.Operator.EQUAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGT
  /**
   ** Factory method to create a OIG <code>GREATER-THAN</code> filter
   ** expression.
   **
   ** @param  filter             the ICF <code>greater than</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link GreaterThanFilter}.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            GREATER-THAN.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the GREATER-THAN expression.
   **                            A return value of <code>null</code> means a
   **                            native GREATER-THAN query cannot be created for
   **                            the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   **                            <br>
   **                            Possible object is {@link SearchCriteria}.
   */
  protected SearchCriteria createGT(final GreaterThanFilter filter, final boolean not) {
    return not
         // FIXME: How to negate the filter criteria
         ? createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), Number.class), SearchCriteria.Operator.GREATER_THAN)
         : createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), Number.class), SearchCriteria.Operator.GREATER_THAN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGE
  /**
   ** Factory method to create a OIG <code>GREATER-THAN-EQUAL</code> filter
   ** expression.
   **
   ** @param  filter             the ICF <code>greater than or equal</code>
   **                            filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is
   **                            {@link GreaterThanOrEqualFilter}.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            GREATER-THAN-EQUAL.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the GREATER-THAN-EQUAL expression.
   **                            A return value of <code>null</code> means a
   **                            native GREATER-THAN-EQUAL query cannot be
   **                            created for the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   **                            <br>
   **                            Possible object is {@link SearchCriteria}.
   */
  protected SearchCriteria createGE(final GreaterThanOrEqualFilter filter, final boolean not) {
    return not
         // FIXME: How to negate the filter criteria
         ? createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), Number.class), SearchCriteria.Operator.GREATER_EQUAL)
         : createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), Number.class), SearchCriteria.Operator.GREATER_EQUAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLT
  /**
   ** Factory method to create a OIG <code>LESS-THAN</code> filter
   ** expression.
   **
   ** @param  filter             the ICF <code>less than</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link LessThanFilter}.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            LESS-THAN.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LESS-THAN expression.
   **                            A return value of <code>null</code> means a
   **                            native LESS-THAN query cannot be created for
   **                            the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   **                            <br>
   **                            Possible object is {@link SearchCriteria}.
   */
  protected SearchCriteria createLT(final LessThanFilter filter, final boolean not) {
    return not
         // FIXME: How to negate the filter criteria
         ? createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), Number.class), SearchCriteria.Operator.LESS_THAN)
         : createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), Number.class), SearchCriteria.Operator.LESS_THAN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLE
  /**
   ** Factory method to create a OIG <code>LESS-THAN-EQUAL</code> filter
   ** expression.
   **
   ** @param  filter             the ICF <code>less than or equal</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is
   **                            {@link LessThanOrEqualFilter}.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            LESS-THAN-EQUAL.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the LESS-THAN-EQUAL expression.
   **                            A return value of <code>null</code> means a
   **                            native LESS-THAN-EQUAL query cannot be created
   **                            for the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   **                            <br>
   **                            Possible object is {@link SearchCriteria}.
   */
  protected SearchCriteria createLE(final LessThanOrEqualFilter filter, final boolean not) {
    return not
         // FIXME: How to negate the filter criteria
         ? createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), Number.class), SearchCriteria.Operator.LESS_EQUAL)
         : createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), Number.class), SearchCriteria.Operator.LESS_EQUAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSW
  /**
   ** Factory method to create a OIG <code>STARTS-WITH</code> filter
   ** expression.
   **
   ** @param  filter             the ICF <code>starts with</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link StartsWithFilter}.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            STARTS-WITH.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the STARTS-WITH expression.
   **                            A return value of <code>null</code> means a
   **                            native STARTS-WITH query cannot be created for
   **                            the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   **                            <br>
   **                            Possible object is {@link SearchCriteria}.
   */
  protected SearchCriteria createSW(final StartsWithFilter filter, final boolean not) {
    return not
         ? createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), String.class), SearchCriteria.Operator.DOES_NOT_BEGIN_WITH)
         : createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), String.class), SearchCriteria.Operator.BEGINS_WITH);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEW
  /**
   ** Factory method to create a OIG <code>ENDS-WITH</code> filter
   ** expression.
   **
   ** @param  filter             the ICF <code>ends with</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EndsWithFilter}.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            ENDS-WITH.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the ENDS-WITH expression.
   **                            A return value of <code>null</code> means a
   **                            native ENDS-WITH query cannot be created for
   **                            the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   **                            <br>
   **                            Possible object is {@link SearchCriteria}.
   */
  protected SearchCriteria createEW(final EndsWithFilter filter, final boolean not) {
    return not
         ? createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), String.class), SearchCriteria.Operator.DOES_NOT_END_WITH)
         : createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), String.class), SearchCriteria.Operator.ENDS_WITH);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCO
  /**
   ** Factory method to create a OIG <code>CONTAINS</code> filter
   ** expression.
   **
   ** @param  filter             the ICF <code>contains</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ContainsFilter}.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            CONTAINS.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the CONTAINS expression.
   **                            A return value of <code>null</code> means a
   **                            native CONTAINS query cannot be created for the
   **                            given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   **                            <br>
   **                            Possible object is {@link SearchCriteria}.
   */
  protected SearchCriteria createCO(final ContainsFilter filter, final boolean not) {
    return not
         ? createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), String.class), SearchCriteria.Operator.DOES_NOT_CONTAIN)
         : createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), String.class), SearchCriteria.Operator.CONTAINS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFilter
  /**
   **
   ** @param  <T>                the type of the attribute value.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code> extending
   **                            {@link Serializable}.
   ** @param  name               the name of the attribute to apply the filter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the filter value to apply.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  operator           the {@link SearchCriteria} operator to be
   **                            applied by the filter.
   **                            <br>
   **                            Allowed object is
   **                            {@link SearchCriteria.Operator}.
   **
   ** @return                    the {@link SearchCriteria} populated with the
   **                            specified properties.
   **                            <br>
   **                            Possible object is {@link SearchCriteria}.
   */
  protected <T extends Serializable> SearchCriteria createFilter(final String name, final T value, final SearchCriteria.Operator operator) {
    if (Uid.NAME.equals(name)) {
      if (this.type.equals(ObjectClass.ACCOUNT))
        return new SearchCriteria(UserManagerConstants.AttributeName.USER_KEY.getId(), value, operator);
      else if (this.type.equals(ObjectClass.GROUP))
        return new SearchCriteria( RoleManagerConstants.ROLE_KEY, value, operator);
      else if (this.type.equals(ServiceClass.TENANT))
        return new SearchCriteria( OrganizationManagerConstants.AttributeName.ID_FIELD.getId(), value, operator);
    }
    else if (Name.NAME.equals(name)) {
      if (this.type.equals(ObjectClass.ACCOUNT))
        return new SearchCriteria(UserManagerConstants.AttributeName.USER_LOGIN.getId(), value, operator);
      else if (this.type.equals(ObjectClass.GROUP))
        return new SearchCriteria(RoleManagerConstants.ROLE_NAME, value, operator);
      else if (this.type.equals(ServiceClass.TENANT))
        return new SearchCriteria( OrganizationManagerConstants.AttributeName.ORG_NAME.getId(), value, operator);
    }
    else if (OperationalAttributeInfos.ENABLE.equals(name)) {
      if (this.type.equals(ObjectClass.ACCOUNT))
        return new SearchCriteria(UserManagerConstants.AttributeName.STATUS.getId(), value, operator);
      else if (this.type.equals(ServiceClass.TENANT))
        return new SearchCriteria( OrganizationManagerConstants.AttributeName.ORG_STATUS.getId(), value, operator);
    }
    else if (OperationalAttributeInfos.PASSWORD.equals(name)) {
      if (this.type.equals(ObjectClass.ACCOUNT))
        return new SearchCriteria(UserManagerConstants.AttributeName.PASSWORD.getId(), value, operator);
    }
    return new SearchCriteria(name, value, operator);
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
   ** @throws ServerException     if filter syntax becomes invalid.
   */
  private Filter simplify(final Filter filter)
    throws ServerException {

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
        final List<SearchCriteria> lex = translateInternal(lhs);
        final List<SearchCriteria> rex = translateInternal(rhs);
        if (lex.isEmpty()) {
          // this can happen only when one of the create* methods is
          // inconsistent from one invocation to the next (simplifiedLeft should
          // have been null in the previous 'if' above).
          throw ServerException.invalidFilter(ServerBundle.string(ServerError.FILTER_METHOD_INCONSISTENT, lex));
        }
        if (rex.isEmpty()) {
          // this can happen only when one of the create* methods is
          // inconsistent from one invocation to the next (simplifiedRight
          // should have been null in the previous 'if' above).
          throw ServerException.invalidFilter(ServerBundle.string(ServerError.FILTER_METHOD_INCONSISTENT, rex));
        }

        // simulate ANDing each pair(left,right).
        // if all of them return null (i.e., "everything"), then the request
        // cannot be filtered.
        boolean possibleAnd = false;
        for (SearchCriteria l : lex) {
          for (SearchCriteria r : rex) {
            SearchCriteria test = createAND(l, r);
            if (test != null) {
              possibleAnd = true;
              break;
            }
          }
          if (possibleAnd) {
            break;
          }
        }

        // if no AND filtering is possible, return whichever of left or right
        // contains the fewest expressions.
        if (!possibleAnd) {
          if (lex.size() <= rex.size()) {
            return lhs;
          }
          else {
            return rhs;
          }
        }

        // since AND filtering is possible for at least one expression, let's
        // distribute.
        if (lex.size() > 1) {
          // the left can contain more than one expression only if the left-hand
          // side is an unimplemented OR.
          // distribute our AND to the left.
          final OrFilter left      = (OrFilter)lhs;
          final OrFilter newFilter = new OrFilter(new AndFilter(left.getLeft(), rhs), new AndFilter(left.getRight(), rhs));
          return simplify(newFilter);
        }
        else if (rex.size() > 1) {
          // the right can contain more than one expression only if the
          // right-hand side is an unimplemented OR.
          // distribute our AND to the right.
          final OrFilter right     = (OrFilter)rhs;
          final OrFilter newFilter = new OrFilter(new AndFilter(lhs, right.getLeft()), new AndFilter(lhs, right.getRight()));
          return simplify(newFilter);
        }
        else {
          // each side contains exactly one expression and the translator does
          // implement AND (anyAndsPossible must be true for them to have hit
          // this branch).
          assert possibleAnd;
          return new AndFilter(lhs, rhs);
        }
      }
    }
    else if (filter instanceof OrFilter) {
      final OrFilter of = (OrFilter)filter;
      final Filter   lhs = simplify(of.getLeft());
      final Filter   rhs = simplify(of.getRight());
      // if either left or right reduces to "everything", then simplify the OR
      // to "everything".
      if (lhs == null || rhs == null) {
        return null;
      }
      // otherwise
      return new OrFilter(lhs, rhs);
    }
    else {
      // otherwise, it's a NOT(LEAF) or a LEAF.
      // simulate creating it.
      SearchCriteria exp = createLeafExpression(filter);
      if (exp == null) {
        // if the expression cannot be implemented,
        // return the "everything" filter.
        return null;
      }
      else {
        // otherwise, return the filter.
        return filter;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   negate
  /**
   ** Given a filter, create a filter representing its negative. This is used
   ** by normalize.
   */
  private Filter negate(final Filter filter) {
    if (filter instanceof AndFilter) {
      AndFilter af = (AndFilter)filter;
      return new OrFilter(negate(af.getLeft()), negate(af.getRight()));
    }
    else if (filter instanceof OrFilter) {
      OrFilter of = (OrFilter)filter;
      return new AndFilter(negate(of.getLeft()), negate(of.getRight()));
    }
    else if (filter instanceof NotFilter) {
      NotFilter nf = (NotFilter)filter;
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
   ** @param  filter              a {@link Filter} (normalized, simplified, and
   **                             distibuted).
   **                             <br>
   **                             Allowed object is {@link Filter}.
   **
   ** @return                     a collection of expressions or empty
   **                             collection for everything.
   **                             <br>
   **                             Possible object is {@link List} where each
   **                             element is of type {@link SearchCriteria}.
   **
   ** @throws ServerException     if filter syntax becomes invalid.
   */
  private List<SearchCriteria> translateInternal(final Filter filter)
    throws ServerException {

    if (filter instanceof AndFilter) {
      final SearchCriteria       result = translateAnd((AndFilter)filter);
      final List<SearchCriteria> rv     = new ArrayList<SearchCriteria>();
      if (result != null) {
        rv.add(result);
      }
      return rv;
    }
    else if (filter instanceof OrFilter) {
      return translateOr((OrFilter)filter);
    }
    else {
      // otherwise it's either a leaf or a NOT (leaf)
      SearchCriteria       leaf = createLeafExpression(filter);
      List<SearchCriteria> expr = new ArrayList<SearchCriteria>();
      if (expr != null) {
        expr.add(leaf);
      }
      return expr;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   translateAnd
  /**
   ** Translates the {@link And} filter into a list of expressions.
   ** <p>
   ** The filter must have already been transformed using
   ** {@link #normalize(Filter)} followed by a {@link #simplify(Filter)}.
   **
   ** @param  filter              an {@link And} filter (normalized, simplified,
   **                             and distibuted).
   **                             <br>
   **                             Allowed object is {@link AndFilter}.
   **
   ** @return                     a collection of expressions or empty
   **                             collection for everything.
   **                             <br>
   **                             Possible object is {@link List} where each
   **                             element is of type {@link SearchCriteria}.
   **
   ** @throws ServerException     if filter syntax becomes invalid.
   */
  private SearchCriteria translateAnd(final AndFilter filter)
    throws ServerException {

    final List<SearchCriteria> lhs = translateInternal(filter.getLeft());
    final List<SearchCriteria> rhs = translateInternal(filter.getRight());
    if (lhs.size() != 1)
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed).
      throw ServerException.invalidFilter(ServerBundle.string(ServerError.FILTER_METHOD_INCONSISTENT, lhs));

    if (rhs.size() != 1) {
      // this can happen only if one of the create* methods
      // is inconsistent from one invocation to the next
      // (at this point we've already been simplified and
      // distributed).
      throw ServerException.invalidFilter(ServerBundle.string(ServerError.FILTER_METHOD_INCONSISTENT, rhs));
    }
    SearchCriteria rv = createAND(lhs.get(0), rhs.get(0));
    if (rv == null) {
      // this could happen only if we're inconsistent since the simplify logic
      // already should have removed any expression that cannot be filtered
      throw ServerException.invalidFilter(ServerBundle.string(ServerError.FILTER_EXPRESSION_INCONSISTENT, "createAND"));
    }
    return rv;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   translateOr
  /**
   ** Translates the {@link Or} filter into a list of expressions.
   ** <p>
   ** The filter must have already been transformed using
   ** {@link #normalize(Filter)} followed by a {@link #simplify(Filter)}.
   **
   ** @param  filter              an {@link Or} filter (normalized, simplified,
   **                             and distibuted).
   **                             <br>
   **                             Allowed object is @link OrFilter}.
   **
   ** @return                     a collection of expressions or empty
   **                             collection for everything.
   **                             <br>
   **                             Possible object is {@link List} where each
   **                             element is of type {@link SearchCriteria}.
   **
   ** @throws ServerException     if filter syntax becomes invalid.
   */
  private List<SearchCriteria> translateOr(final OrFilter filter)
    throws ServerException {

    final List<SearchCriteria> lhs = translateInternal(filter.getLeft());
    final List<SearchCriteria> rhs = translateInternal(filter.getRight());
    if (lhs.isEmpty())
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed).
      throw ServerException.invalidFilter(ServerBundle.string(ServerError.FILTER_METHOD_INCONSISTENT, lhs));

    if (rhs.isEmpty())
      // this can happen only if one of the create* methods is inconsistent from
      // one invocation to the next (at this point we've already been simplified
      // and distributed).
      throw ServerException.invalidFilter(ServerBundle.string(ServerError.FILTER_METHOD_INCONSISTENT, rhs));

    if (lhs.size() == 1 && rhs.size() == 1) {
      // If each side contains exactly one expression,
      // try to create a combined expression.
      SearchCriteria val = createOR(lhs.get(0), rhs.get(0));
      if (val != null) {
        List<SearchCriteria> rv = new ArrayList<SearchCriteria>();
        rv.add(val);
        return rv;
      }
      // otherwise, fall through
    }

    // return a list of queries from the left and from the right
    List<SearchCriteria> rv = new ArrayList<SearchCriteria>(lhs.size() + rhs.size());
    rv.addAll(lhs);
    rv.addAll(rhs);
    return rv;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLeafExpression
  /**
   ** Creates an expression for a LEAF or a NOT(leaf)
   **
   ** @param  filter             a filter (normalized, simplified, and
   **                            distibuted).
   **                            <br>
   **                            Must be either a leaf or a NOT(leaf).
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    the translated filter expression.
   **                            <br>
   **                            Possible object is {@link SearchCriteria}.
   */
  private SearchCriteria createLeafExpression(final Filter filter) {
    return (filter instanceof NotFilter)
      ? createLeafExpression(((NotFilter)filter).getFilter(), true)
      : createLeafExpression(filter, false)
      ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLeafExpression
  /**
   ** Creates a Leaf expression
   **
   ** @param  filter             a filter (normalized, simplified, and
   **                            distibuted).
   **                            <br>
   **                            Must be either a leaf or a NOT(leaf).
   **                            <br>
   **                            Allowed object is {@link Filter}.
   ** @param  not                <code>true</code> if the filter have to
   **                            negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** 
   **
   ** @return                    the translated filter expression or
   **                            <code>null</code> (for everything).
   **                            <br>
   **                            Possible object is {@link SearchCriteria}.
   */
  private SearchCriteria createLeafExpression(final Filter filter, boolean not) {
    switch (Switcher.from(filter.getClass().getName())) {
      case EQ : return createEQ((EqualsFilter)filter, not);
      case GT : return createGT((GreaterThanFilter)filter, not);
      case GE : return createGE((GreaterThanOrEqualFilter)filter, not);
      case LT : return createLT((LessThanFilter)filter, not);
      case LE : return createLE((LessThanOrEqualFilter)filter, not);
      case SW : return createSW((StartsWithFilter)filter, not);
      case EW : return createEW((EndsWithFilter)filter, not);
      case CO : return createCO((ContainsFilter)filter, not);
      // unrecognized expression - nothing we can do
      default : return null;
    }
  }
}