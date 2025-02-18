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
    Subsystem   :   System Authorization Management

    File        :   ApplicationInstanceDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ApplicationInstanceDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.schema;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import oracle.iam.platform.authopss.exception.AccessDeniedException;

import oracle.iam.platform.authopss.vo.EntityPublication;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.platform.entitymgr.spi.entity.Searchable;

import oracle.iam.platformservice.api.EntityPublicationService;

import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.iam.provisioning.api.ApplicationInstanceService;

import oracle.iam.provisioning.exception.GenericAppInstanceServiceException;
import oracle.iam.provisioning.exception.ApplicationInstanceNotFoundException;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.iam.ui.platform.model.common.PagedArrayList;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

////////////////////////////////////////////////////////////////////////////////
// class ApplicationInstanceDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by System Authorization Management customization.
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
public class ApplicationInstanceDataProvider extends AbstractDataProvider<ApplicationInstanceAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6999451510958426419")
  private static final long serialVersionUID = 157576168550364862L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ApplicationInstanceDataProvider</code> data
   ** access object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ApplicationInstanceDataProvider() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   search (DataProvider)
  /**
   ** Return a list of backend objects based on the given filter criteria.
   **
   ** @param  searchCriteria     the OIM search criteria to submit.
   **                            <br>
   **                            Allowed object is {@link SearchCriteria}.
   ** @param  requested          the {@link Set} of attributes to be returned in
   **                            search results.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  control            contains pagination, sort attribute, and sort
   **                            direction.
   **                            <br>
   **                            Allowed object is {@link HashMap} where each
   **                            element is of type [@link String} for the key
   **                            and {@link Object} for the value.
   **
   ** @return                    a list of backend objects.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type
   **                            {@link ApplicationInstanceAdapter}.
   */
  @Override
  public List<ApplicationInstanceAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    // default search is based on name
    SearchCriteria criteria = searchCriteria == null ? null : convertCriteria(searchCriteria);
    if (criteria == null) {
      criteria = new SearchCriteria(ApplicationInstance.APPINST_NAME, "*", SearchCriteria.Operator.EQUAL);
    }
    // default search is sorted on name and ascending
    final HashMap<String, Object> parameter = convertParameter(control);
    if (!parameter.containsKey(ApplicationInstance.SORTEDBY)) {
      parameter.put(ApplicationInstance.SORTEDBY,  ApplicationInstance.APPINST_NAME);
      parameter.put(ApplicationInstance.SORTORDER, Searchable.SortOrder.ASCENDING);
    }

    // perform the search
    final ApplicationInstanceService service  = service(ApplicationInstanceService.class);
    try {
      return paginated(parameter, service.findApplicationInstance(criteria, parameter), ConstantsDefinition.UNKNOWN_ROW_COUNT);
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   update (overridden)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** B-Object update.
   **
   ** @param  mab                the model adapter bean, with key fields set.
   */
  @Override
  public void update(final ApplicationInstanceAdapter mab) {
    final ApplicationInstanceService service = service(ApplicationInstanceService.class);
    try {
      ApplicationInstance instance = mab.getInstance();
      if (instance.getApplicationInstanceKey() == 0) {
        instance = service.findApplicationInstanceByName(mab.getName());
      }
      instance = service.updateApplicationInstance(instance);
    }
    catch (ApplicationInstanceNotFoundException e) {
      throw new OIMRuntimeException(e);
    }
    catch (AccessDeniedException e) {
      throw new OIMRuntimeException(e);
    }
    catch (GenericAppInstanceServiceException e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   delete (overridden)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** B-Object delete.
   **
   ** @param  mab                the model adapter bean, with key fields set.
   */
  @Override
  public void delete(final ApplicationInstanceAdapter mab) {
    final long                       key     = mab.getAppInstanceKey();
    final ApplicationInstanceService service = service(ApplicationInstanceService.class);
    try {
      service.deleteApplicationInstance(key);
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   lookup (overridden)
  /**
   ** Return a specific backend object identified by the name.
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
  public ApplicationInstanceAdapter lookup(final ApplicationInstanceAdapter mab) {
    // perform the search
    final ApplicationInstanceService service = service(ApplicationInstanceService.class);
    try {
      mab.setInstance(service.findApplicationInstanceByKey(mab.getAppInstanceKey()));
      return mab;
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignPublication
  /**
   ** Assigns the passed <code>Entity Publication</code>s to an
   ** <code>Application Instance</code>.
   **
   ** @param  publication        the {@link List} of {@link EntityPublication}
   **                            to persist.
   ** @param  entitlement        <code>true</code> if the publications should
   **                            also assigned to every entitlement which
   **                            belongs to the
   **                            <code>Application Instance</code>.
   **
   ** @return                    <code>true</code> if the
   **                            {@link EntityPublication}s are assigned;
   **                            otherwise <code>false</code>.
   */
  public List<EntityPublication> assignPublication(final List<EntityPublication> publication, final boolean entitlement) {
    final EntityPublicationService service = service(EntityPublicationService.class);
    try {
      return service.addEntityPublications(publication, entitlement);
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updatePublication
  /**
   ** Performing an update on the passed <code>Entity Publication</code>s.
   **
   ** @param  publication        the data providing the changes.
   **
   ** @return                    <code>true</code> if the
   **                            {@link EntityPublication}s are updated;
   **                            otherwise <code>false</code>.
   */
  public Boolean updatePublication(final List<EntityPublication> publication) {
    final EntityPublicationService service = service(EntityPublicationService.class);
    try {
      return service.updateEntityPublications(publication);
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokePublication
  /**
   ** Revokes the passed <code>Entity Publication</code>s from an
   ** <code>Application Instance</code>.
   **
   ** @param  publication        the data providing the changes.
   **
   ** @return                    <code>true</code> if the
   **                            {@link EntityPublication}s are revoked;
   **                            otherwise <code>false</code>.
   */
  public Boolean revokePublication(final List<EntityPublication> publication) {
    final EntityPublicationService service = service(EntityPublicationService.class);
    try {
      return service.removeEntityPublications(publication);
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   convertCriteria
  /**
   ** Converts the attribute names contained in the passed
   ** {@link SearchCriteria} and obtained from the ADF Entity/View Definitions
   ** to the names declared in Identity Manager for entity Application Instance.
   **
   ** @param criteria            the search criteria mapped to the ADF
   **                            Entity/View Definitions.
   **
   ** @return                    the search criteria mapped to the OIM
   **                            declarations.
   */
  private SearchCriteria convertCriteria(final SearchCriteria criteria) {
    Object lhs = criteria.getFirstArgument();
    if (lhs instanceof SearchCriteria) {
      lhs = convertCriteria((SearchCriteria)lhs);
    }
    else if (lhs instanceof String) {
      lhs = convertAttributeName((String)lhs);
    }

    Object rhs = criteria.getSecondArgument();
    if (rhs instanceof SearchCriteria) {
      rhs = convertCriteria((SearchCriteria)rhs);
    }
    return new SearchCriteria(lhs, rhs, criteria.getOperator());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   convertParameter
  /**
   ** Converts the attribute named defined by the ADF view controller to the
   ** natural names exposed by the OIM kernel API.
   **
   ** @param  parameter          the mapping of control parameter names defined
   **                            by the ADF view controller.
   **
   ** @return                    the attribute name exposed by the OIM kernel
   **                            API if any.
   */
  private HashMap<String, Object> convertParameter(final HashMap<String, Object> parameter) {
    // prevent bogus input
    if (parameter == null || parameter.size() == 0)
      return parameter;

    if (parameter.containsKey(ApplicationInstance.SORTEDBY)) {
      String value = parameter.get(ApplicationInstance.SORTEDBY).toString();
      value = this.convertAttributeName(value);
      parameter.put(ApplicationInstance.SORTEDBY, value);
    }
    return parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   convertAttributeName
  /**
   ** Converts the attribute name defined in the ADF view query criteria to the
   ** natural names exposed by the OIM kernel API.
   **
   ** @param  outboundName       the attribute name defined in the ADF view
   **                            query criteria.
   **
   ** @return                    the attribute name exposed by the OIM kernel
   **                            API if any.
   */
  private String convertAttributeName(final String outboundName) {
    // prevent bogus input
    String inboundName = outboundName;
    if (inboundName == null)
      return null;

    inboundName = inboundName.trim();
    if (inboundName.equalsIgnoreCase(ApplicationInstanceAdapter.DISPLAY_NAME)) {
      inboundName = ApplicationInstance.DISPLAY_NAME;
    }
    else if (inboundName.equalsIgnoreCase(ApplicationInstanceAdapter.NAME)) {
      inboundName = ApplicationInstance.APPINST_NAME;
    }
    else if (inboundName.equalsIgnoreCase(ApplicationInstanceAdapter.OBJECTSNAME)) {
      inboundName = ApplicationInstance.OBJ_NAME;
    }
    else if (inboundName.equalsIgnoreCase(ApplicationInstanceAdapter.ENDPOINTNAME)) {
      inboundName = ApplicationInstance.ITRES_NAME;
    }
    else if (inboundName.equalsIgnoreCase(ApplicationInstanceAdapter.DESCRIPTION)) {
      inboundName = ApplicationInstance.DESCRIPTION;
    }
    return inboundName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   paginate
  /**
   ** Factory method to create a paginated result set from the given
   ** results list.
   ** <b>
   ** Each element in the returned list is an
   ** {@link ApplicationInstanceAdapter}.
   **
   ** @param  control            contains pagination, sort attribute, and sort
   **                            direction.
   **                            Allowed object is {@link HashMap}.
   ** @param  results            the {@link List} provider to obtain the
   **                            attribute values from.
   **                            Allowed object is {@link List}.
   ** @param  totalRowCount      the size of the entire result set
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a paginated result set from the {@link List}.
   */
  private List<ApplicationInstanceAdapter> paginated(final Map<String, Object> parameter, List<ApplicationInstance> results, final long totalRow) {
    final List<ApplicationInstanceAdapter> resultSet = new ArrayList<ApplicationInstanceAdapter>();
    if (results == null)
      return resultSet;

    for (ApplicationInstance cursor : results) {
      final ApplicationInstanceAdapter mab = new ApplicationInstanceAdapter();
      mab.setInstance(cursor);
      resultSet.add(mab);
    }

    int rangeStart = 0;
    if (parameter != null && parameter.get(ConstantsDefinition.SEARCH_STARTROW) != null) {
      rangeStart = (Integer)parameter.get(ConstantsDefinition.SEARCH_STARTROW);
    }
    return new PagedArrayList<ApplicationInstanceAdapter>(resultSet, rangeStart, totalRow);
  }
}