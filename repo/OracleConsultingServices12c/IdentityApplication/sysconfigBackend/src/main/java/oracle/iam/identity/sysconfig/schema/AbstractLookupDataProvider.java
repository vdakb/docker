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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Configuration Management

    File        :   AbstractDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysconfig.schema;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;

import Thor.API.Operations.tcLookupOperationsIntf;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcInvalidValueException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Exceptions.tcInvalidAttributeException;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.iam.identity.foundation.naming.LookupValue;

import oracle.iam.identity.foundation.persistence.DatabaseFilter;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractLookupDataProvider
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by Lookup Administration customization.
 ** <p>
 ** Define an instance variable for each VO attribute, with the data type that
 ** corresponds to the VO attribute type. The name of the instance variable must
 ** match the name of the VO attribute, and the case must match.
 ** <p>
 ** When the instance of this class is passed to the methods in the
 ** corresponding <code>Application Module</code> class, the
 ** <code>Application Module</code> can call the getter methods to retrieve the
 ** values from the AdapterBean and pass them to the OIM APIs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public abstract class AbstractLookupDataProvider extends AbstractDataProvider<LookupValueAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String              LKU_ALIAS        = "lku";
  static final String              LKU_KEY          = "lku_key";
  static final String              LKU_NAME         = "lku_type_string_key";
  static final String              LKU_TYPE         = "lku_type";
  static final String              LKU_GROUP        = "lku_group";
  static final String              LKU_FIELD        = "lku_field";
  static final String              LKU_REQUIRED     = "lku_required";
  static final String              LKU_MEANING      = "lku_meaning";

  static final String              LKV_ALIAS        = "lkv";
  static final String              LKV_KEY          = "lkv_key";
  static final String              LKV_ENCODED      = "lkv_encoded";
  static final String              LKV_DECODED      = "lkv_decoded";
  static final String              LKV_DISABLED     = "lkv_disabled";

  static final Map<String, String> FILTER           = new HashMap<String, String>();

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6248789559715093941")
  private static final long        serialVersionUID = -8304086788331581665L;

  static {
    synchronized(FILTER) {
      FILTER.put(LKU_KEY,      "lku." + LKU_KEY);
      FILTER.put(LKU_NAME,     "lku." + LKU_NAME);
      FILTER.put(LKV_KEY,      "lkv." + LKV_KEY);
      FILTER.put(LKV_ENCODED,  "lkv." + LKV_ENCODED);
      FILTER.put(LKV_DECODED,  "lkv." + LKV_DECODED);
      FILTER.put(LKV_DISABLED, "lkv." + LKV_DISABLED);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final String    lookup;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // class LookupValueComparator
  // ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
  public class LookupValueComparator implements Comparator<LookupValueAdapter> {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>LookupValueComparator</code> values object
     ** that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public LookupValueComparator() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: compare
    /**
     ** Compares its two {@link LookupValueAdapter}s for order.
     ** <p>
     ** Returns a negative integer, zero, or a positive integer as the first
     ** argument is less than, equal to, or greater than the second.
     ** <p>
     ** In the foregoing description, the notation
     ** <code>sgn(</code><i>expression</i><code>)</code> designates the
     ** mathematical <i>signum</i> function, which is defined to return one of
     ** <code>-1</code>, <code>0</code>, or <code>1</code> according to whether
     ** the value of <i>expression</i> is negative, zero or positive.
     ** <p>
     ** The implementaion ensures that
     ** <code>sgn(compare(x, y)) == -sgn(compare(y, x))</code> for all
     ** <code>x</code> and <code>y</code>.
     ** <p>
     ** The implementaion ensures that the relation is transitive:
     ** <code>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</code>
     ** implies <code>compare(x, z)&gt;0</code>.
     ** <p>
     ** Finally, implementaion ensures that <code>compare(x, y)==0</code>
     ** implies that <code>sgn(compare(x, z))==sgn(compare(y, z))</code> for all
     ** <code>z</code>.
     **
     ** @param  lkv1             the first {@link LookupValueAdapter} to be
     **                          compared.
     ** @param  lkv2             the second {@link LookupValueAdapter} to be
     **                          compared.
     **
     ** @return                  a negative integer, zero, or a positive integer
     **                          as the first {@link LookupValueAdapter} is
     **                          less than, equal to, or greater than the
     **                          second.
     **
     ** @throws NullPointerException if an argument is <code>null</code>.
     */
    public int compare(final LookupValueAdapter lkv1, final LookupValueAdapter lkv2) {
      return lkv1.getEncoded().toLowerCase().compareTo(lkv2.getEncoded().toLowerCase());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AbstractLookupDataProvider</code> data access
   ** object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractLookupDataProvider() {
    // ensure inheritance
    this(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractLookupDataProvider</code> instance which
   ** belongs to specified name of a <code>Lookup Definition</code>.
   **
   ** @param  lookup             the name of a <code>Lookup Definition</code>.
   */
  protected AbstractLookupDataProvider(final String lookup) {
    // ensure inheritance
    super();

    // initialize instance
    this.lookup = lookup;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   exists (DataProvider)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** to check if the B-Object exists
   **
   ** @param  mab                the model adapter bean, with key fields set.
   */
  @Override
  public boolean exists(final LookupValueAdapter mab) {
    // intentionally left blank
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   lookup (DataProvider)
  /**
   ** Return a specific sysconfig object identified by the name.
   ** <p>
   ** The ModelAdapterBean argument implementation class must correspond to the
   ** DataProvider implementation. In other words, this method implemented in
   ** AttributeDataProvider expects a AttributeAdapterBean as an argument.
   ** <br>
   ** If the DataProvider and ModelAdapterBean types do not match, and exception
   ** will be thrown.
   ** <p>
   ** The ModelAdapterBean must has its "key" fields set. All other fields are
   ** ignored. In most cases, the field field is simply the name.
   **
   ** @param  mab                the model adapter bean, with key fields set.
   **
   ** @return                    object identified by the given name.
   */
  @Override
  public LookupValueAdapter lookup(final LookupValueAdapter mab) {
    // intentionally left blank
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   create (DataProvider)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** B-Object creation.
   **
   ** @param  mab                the model adapter bean, with key fields set.
   */
  @Override
  public void create(final LookupValueAdapter mab) {
    // prevent bogus instance state
    // normally it cannot happens but to be safe we need to verify if our own
    // code is correct
    if (this.lookup == null)
      throw new OIMRuntimeException(resourceBackendValue("lku.missing"));

    final tcLookupOperationsIntf facade = service(tcLookupOperationsIntf.class);
    try {
      facade.addLookupValue(this.lookup, mab.getEncoded(), mab.getDecoded(), "en", "US");
    }
    catch (tcInvalidLookupException e) {
      throw new OIMRuntimeException(resourceBackendValue("lku.invalid", this.lookup));
    }
    catch (tcInvalidValueException e) {
      throw new OIMRuntimeException(resourceBackendValue("lkv.invalid", this.lookup, mab.getEncoded()));
    }
    catch (tcAPIException e) {
      throw new OIMRuntimeException(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   update (DataProvider)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** B-Object update.
   **
   ** @param  mab                the model adapter bean, with key fields set.
   */
  @Override
  public void update(final LookupValueAdapter mab) {
    // prevent bogus instance state
    // normally it cannot happens but to be safe we need to verify if our own
    // code is correct
    if (this.lookup == null)
      throw new OIMRuntimeException(resourceBackendValue("lku.missing"));

    // we know the value in the passed bean already, means we have to update
    final Map<String, String> data = new HashMap<String, String>();
    data.put(LookupValue.DECODED,  mab.getDecoded());
    final tcLookupOperationsIntf facade = service(tcLookupOperationsIntf.class);
    try {
      facade.updateLookupValue(this.lookup, mab.getEncoded(), data);
    }
    catch (tcInvalidLookupException e) {
      throw new OIMRuntimeException(resourceBackendValue("lku.invalid", this.lookup));
    }
    catch (tcInvalidValueException e) {
      throw new OIMRuntimeException(resourceBackendValue("lkv.invalid", this.lookup, mab.getEncoded()));
    }
    catch (tcInvalidAttributeException e) {
      throw new OIMRuntimeException(resourceBackendValue("lkv.invalid", this.lookup, mab.getEncoded()));
    }
    catch (tcAPIException e) {
      throw new OIMRuntimeException(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   delete (DataProvider)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** B-Object delete.
   **
   ** @param  mab                the model adapter bean, with key fields set.
   */
  @Override
  public void delete(final LookupValueAdapter mab) {
    // prevent bogus instance state
    // normally it cannot happens but to be safe we need to verify if our own
    // code is correct
    if (this.lookup == null)
      throw new OIMRuntimeException(resourceBackendValue("lku.missing"));

    final tcLookupOperationsIntf facade = service(tcLookupOperationsIntf.class);
    try {
      facade.removeLookupValue(this.lookup, mab.getEncoded());
    }
    catch (tcInvalidLookupException e) {
      throw new OIMRuntimeException(resourceBackendValue("lku.invalid", this.lookup));
    }
    catch (tcInvalidValueException e) {
      throw new OIMRuntimeException(resourceBackendValue("lkv.invalid", this.lookup, mab.getEncoded()));
    }
    catch (tcAPIException e) {
      throw new OIMRuntimeException(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   lookupFilter
  /**
   ** Returns the extended search criteria to be applied on the search to
   ** perform.
   **
   ** @param  searchCriteria     the OIM search criteria to investigate.
   **
   ** @return                    the name of the lookup definition found in the
   **                            specified {@link SearchCriteria}.
   **
   ** @throws OIMRuntimeException if the criteria with the name
   **                             <code>lookupName</code> isn't part of the
   **                             specified {@link SearchCriteria} or no
   **                             argument is defined for the criteria.
   */
  public static Map<String, String> lookupFilter(final SearchCriteria searchCriteria) {
    final Map<String, String> filter = new HashMap<String, String>();
    if (searchCriteria == null) {
      filter.put(LookupValue.ENCODED, ConstantsDefinition.WILDCARD);
    }
    else {
      // obtain encoded value filter from searchCriteria
      SearchCriteria criteria = findCriteria(searchCriteria, LookupValueAdapter.ENCODED);
      if (criteria != null)
        filter.put(LookupValue.ENCODED, queryString(criteria));

      // obtain decoded value filter from searchCriteria
      criteria = findCriteria(searchCriteria, LookupValueAdapter.DECODED);
      if (criteria != null)
        filter.put(LookupValue.DECODED, queryString(criteria));
    }
    return filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enableLookupValue
  /**
   ** Enables the specified lookup values in Identity Manager.
   ** <p>
   ** The operation use for this purpose the {@link Set} of identifiers
   ** (aka primary keys).
   **
   ** @param  identifier         the {@link Set} of identifiers belonging to
   **                            lookup values to be enabled.
   **
   ** @return                    <code>true</code> if all specified values are
   **                            enabled; otherwise <code>false</code>.
   */
  public Boolean enableLookupValue(final Set<String> identifier) {
    // prevent bogus instance state
    // normally it cannot happens but to be safe we need to verify if our own
    // code is correct
    if (this.lookup == null)
      throw new OIMRuntimeException(resourceBackendValue("lku.missing"));

    // we know the value in the passed bean already, means we have to update
    final Map<String, String> data = new HashMap<String, String>();
    data.put(LookupValue.DISABLED, LookupValue.STATUS_ENABLED);

    final tcLookupOperationsIntf facade = service(tcLookupOperationsIntf.class);
    for (String cursor : identifier) {
      try {
        facade.updateLookupValue(this.lookup, cursor, data);
      }
      catch (tcInvalidLookupException e) {
        throw new OIMRuntimeException(resourceBackendValue("lku.invalid", this.lookup));
      }
      catch (tcInvalidValueException e) {
        throw new OIMRuntimeException(resourceBackendValue("lkv.invalid", this.lookup, cursor));
      }
      catch (tcInvalidAttributeException e) {
        throw new OIMRuntimeException(resourceBackendValue("lkv.invalid", this.lookup, cursor));
      }
      catch (tcAPIException e) {
        throw new OIMRuntimeException(e.getLocalizedMessage());
      }
    }
    return Boolean.TRUE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enableLookupValueBulk
  /**
   ** Enables the specified lookup values in Identity Manager.
   ** <p>
   ** The operation use for this purpose the {@link Set} of identifiers
   ** (aka primary keys).
   **
   ** @param  identifier         the {@link Set} of identifiers belonging to
   **                            lookup values to be enabled.
   **
   ** @return                    <code>true</code> if all specified values are
   **                            enabled; otherwise <code>false</code>.
   */
  public Boolean enableLookupValueBulk(final Set<String> identifier) {
    // prevent bogus instance state
    // normally it cannot happens but to be safe we need to verify if our own
    // code is correct
    if (this.lookup == null)
      throw new OIMRuntimeException(resourceBackendValue("lku.missing"));

    // we know the value in the passed bean already, means we have to update
    final Map<String, String> data = new HashMap<String, String>();
    data.put(LookupValue.DISABLED, LookupValue.STATUS_ENABLED);

    final tcLookupOperationsIntf facade = service(tcLookupOperationsIntf.class);
    for (String cursor : identifier) {
      try {
        facade.updateLookupValue(this.lookup, cursor, data);
      }
      catch (tcInvalidLookupException e) {
        throw new OIMRuntimeException(resourceBackendValue("lku.invalid", this.lookup));
      }
      catch (tcInvalidValueException e) {
        throw new OIMRuntimeException(resourceBackendValue("lkv.invalid", this.lookup, cursor));
      }
      catch (tcInvalidAttributeException e) {
        throw new OIMRuntimeException(resourceBackendValue("lkv.invalid", this.lookup, cursor));
      }
      catch (tcAPIException e) {
        throw new OIMRuntimeException(e.getLocalizedMessage());
      }
    }
    return Boolean.TRUE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disableLookupValue
  /**
   ** Disables the specified lookup values in Identity Manager.
   ** <p>
   ** The operation use for this purpose the {@link Set} of identifiers
   ** (aka primary keys).
   **
   ** @param  identifier         the {@link Set} of identifiers belonging to
   **                            lookup values to be disabled.
   **
   ** @return                    <code>true</code> if all specified values are
   **                            deleted; otherwise <code>false</code>.
   */
  public Boolean disableLookupValue(final Set<String> identifier) {
    // prevent bogus instance state
    // normally it cannot happens but to be safe we need to verify if our own
    // code is correct
    if (this.lookup == null)
      throw new OIMRuntimeException(resourceBackendValue("lku.missing"));

    // we know the value in the passed bean already, means we have to update
    final Map<String, String> data = new HashMap<String, String>();
    data.put(LookupValue.DISABLED, LookupValue.STATUS_DISABLED);

    final tcLookupOperationsIntf facade = service(tcLookupOperationsIntf.class);
    for (String cursor : identifier) {
      try {
        facade.updateLookupValue(this.lookup, cursor, data);
      }
      catch (tcInvalidLookupException e) {
        throw new OIMRuntimeException(resourceBackendValue("lku.invalid", this.lookup));
      }
      catch (tcInvalidValueException e) {
        throw new OIMRuntimeException(resourceBackendValue("lkv.invalid", this.lookup, cursor));
      }
      catch (tcInvalidAttributeException e) {
        throw new OIMRuntimeException(resourceBackendValue("lkv.invalid", this.lookup, cursor));
      }
      catch (tcAPIException e) {
        throw new OIMRuntimeException(e.getLocalizedMessage());
      }
    }
    return Boolean.TRUE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disableLookupValueBulk
  /**
   ** Disables the specified lookup values in Identity Manager.
   ** <p>
   ** The operation use for this purpose the {@link Set} of identifiers
   ** (aka primary keys).
   **
   ** @param  identifier         the {@link Set} of identifiers belonging to
   **                            lookup values to be disabled.
   **
   ** @return                    <code>true</code> if all specified values are
   **                            disabled; otherwise <code>false</code>.
   */
  public Boolean disableLookupValueBulk(final Set<String> identifier) {
    // prevent bogus instance state
    // normally it cannot happens but to be safe we need to verify if our own
    // code is correct
    if (this.lookup == null)
      throw new OIMRuntimeException(resourceBackendValue("lku.missing"));

    // we know the value in the passed bean already, means we have to update
    final Map<String, String> data = new HashMap<String, String>();
    data.put(LookupValue.DISABLED, LookupValue.STATUS_DISABLED);

    final tcLookupOperationsIntf facade = service(tcLookupOperationsIntf.class);
    for (String cursor : identifier) {
      try {
        facade.updateLookupValue(this.lookup, cursor, data);
      }
      catch (tcInvalidLookupException e) {
        throw new OIMRuntimeException(resourceBackendValue("lku.invalid", this.lookup));
      }
      catch (tcInvalidValueException e) {
        throw new OIMRuntimeException(resourceBackendValue("lkv.invalid", this.lookup, cursor));
      }
      catch (tcInvalidAttributeException e) {
        throw new OIMRuntimeException(resourceBackendValue("lkv.invalid", this.lookup, cursor));
      }
      catch (tcAPIException e) {
        throw new OIMRuntimeException(e.getLocalizedMessage());
      }
    }
    return Boolean.TRUE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   deleteLookupValue
  /**
   ** Implement this method in the data provider to provide an implementation
   ** B-Object delete.
   **
   ** @param  identifier         the {@link Set} of identifiers belonging to
   **                            entity <code>Lookup Definition</code> to
   **                            delete.
   **
   ** @return                    <code>true</code> if all specified values are
   **                            deleted; otherwise <code>false</code>.
   */
  public Boolean deleteLookupValue(final Set<String> identifier) {
    // prevent bogus instance state
    // normally it cannot happens but to be safe we need to verify if our own
    // code is correct
    if (this.lookup == null)
      throw new OIMRuntimeException(resourceBackendValue("lku.missing"));

    final tcLookupOperationsIntf facade = service(tcLookupOperationsIntf.class);
    try {
      facade.removeBulkLookupValues(this.lookup, identifier);
    }
    catch (tcInvalidLookupException e) {
      throw new OIMRuntimeException(resourceBackendValue("lku.invalid", this.lookup));
    }
    catch (tcAPIException e) {
      throw new OIMRuntimeException(e.getLocalizedMessage());
    }

    return Boolean.TRUE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   extractCriteria
  /**
   ** Converts the attribute names contained in the passed
   ** {@link SearchCriteria} and obtained from the ADF Entity/View Definitions
   ** to the names declared in Identity Manager for entity Lookup.
   **
   ** @param criteria            the search criteria mapped to the ADF
   **                            Entity/View Definitions.
   **
   ** @return                    the {@link DatabaseFilter} constructed from the
   **                            provided {@link SearchCriteria}.
   */
  protected DatabaseFilter extractCriteria(final SearchCriteria criteria) {
    DatabaseFilter filter = null;
    final Object lhs = criteria.getFirstArgument();
    if (lhs instanceof SearchCriteria) {
      final DatabaseFilter partial = extractCriteria((SearchCriteria)lhs);
      if (partial != null) {
        final SearchCriteria.Operator operator = criteria.getOperator();
        filter = (filter == null) ? partial : DatabaseFilter.build(filter, partial, operator == SearchCriteria.Operator.OR ? DatabaseFilter.Operator.OR : DatabaseFilter.Operator.AND);
      }
    }

    final Object rhs = criteria.getSecondArgument();
    if (rhs instanceof SearchCriteria) {
      final DatabaseFilter partial = extractCriteria((SearchCriteria)rhs);
      if (partial != null) {
        final SearchCriteria.Operator operator = criteria.getOperator();
        filter = (filter == null) ? partial : DatabaseFilter.build(filter, partial, operator == SearchCriteria.Operator.OR ? DatabaseFilter.Operator.OR : DatabaseFilter.Operator.AND);
      }
    }

    if (!((lhs instanceof SearchCriteria) && (rhs instanceof SearchCriteria))) {
      DatabaseFilter partial = null;
      final SearchCriteria.Operator op = criteria.getOperator();
      if (LookupAdapter.PK.equalsIgnoreCase((String)lhs) || LookupAdapter.FK.equalsIgnoreCase((String)lhs)) {
        partial = DatabaseFilter.build(FILTER.get(LKU_KEY), rhs, DatabaseFilter.Operator.EQUAL);
      }
      else if (LookupAdapter.NAME.equalsIgnoreCase((String)lhs) || LookupValueAdapter.NAME.equalsIgnoreCase((String)lhs)) {
        partial = DatabaseFilter.build(FILTER.get(LKU_NAME), rhs, DatabaseFilter.Operator.EQUAL);
      }
      else if (LookupValueAdapter.VALUE_KEY.equalsIgnoreCase((String)lhs)) {
        partial = DatabaseFilter.build(FILTER.get(LKV_KEY), rhs, DatabaseFilter.Operator.EQUAL);
      }
      else if (LookupValueAdapter.ENCODED.equalsIgnoreCase((String)lhs)) {
        partial = filterString(op, LKV_ENCODED, rhs);
      }
      else if (LookupValueAdapter.DECODED.equalsIgnoreCase((String)lhs)) {
        partial = filterString(op, LKV_DECODED, rhs);
      }
      else if (LookupValueAdapter.DISABLED.equalsIgnoreCase((String)lhs)) {
        partial = filterString(op, LKV_DISABLED, rhs);
      }
      else if (LookupValueAdapter.DISABLED.equalsIgnoreCase((String)lhs)) {
        partial = filterBoolean(op, LKV_DISABLED, rhs);
      }
      filter = (filter == null) ? partial : DatabaseFilter.build(filter, partial, DatabaseFilter.Operator.AND);
    }
    return filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   filterString
  /**
   ** Factory method to create a {@link DatabaseFilter}.
   **
   ** @param  operator           the filter operator.
   ** @param  column             the name of the column the created filter will
   **                            take in account.
   ** @param  value              the value used to filter on.
   */
  private DatabaseFilter filterString(final SearchCriteria.Operator operator, final String column, final Object value) {
    return AbstractDataProvider.createFilter(operator, FILTER.get(column), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   filterBoolean
  /**
   ** Factory method to create a {@link DatabaseFilter}.
   **
   ** @param  operator           the filter operator.
   ** @param  column             the name of the column the created filter will
   **                            take in account.
   ** @param  value              the value used to filter on.
   */
  private DatabaseFilter filterBoolean(final SearchCriteria.Operator operator, final String column, final Object value) {
    return AbstractDataProvider.booleanFilter(operator, FILTER.get(column), value);
  }
}