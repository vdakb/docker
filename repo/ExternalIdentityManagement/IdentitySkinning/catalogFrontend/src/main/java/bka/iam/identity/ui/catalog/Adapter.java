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

    Copyright 2019 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Common Shared Plugin

    File        :   UserView.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class
                    UserView.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      22.08.2022  BSylvert    First release version
*/
package bka.iam.identity.ui.catalog;

import java.io.Serializable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.el.MethodExpression;

import javax.faces.event.ActionEvent;

import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.exception.OrganizationManagerException;
import oracle.iam.identity.frontend.AbstractManagedBean;
import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.ui.catalog.view.backing.CartUtils;
import oracle.iam.ui.platform.model.common.OIMClientFactory;
import oracle.iam.ui.platform.utils.FacesUtils;

import oracle.jbo.ApplicationModule;
import oracle.jbo.ViewObject;

////////////////////////////////////////////////////////////////////////////////
// class UserView
// ~~~~~ ~~~~~~~~
/**
 ** Declares methods to submit catalog button from a bulk of Request
 ** Template.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Adapter extends AbstractManagedBean 
                         implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  @SuppressWarnings("compatibility:-1519345272281576891")
  private static final long            serialVersionUID = -4935943529068275837L;

  private static final String          CREATE_USER_REQUEST         = "create_user";

  private static final String          UNIQUEIDENTIFIER_ATTRIBUTE  = "uniqueIdentifier__c";
  
  private static final String          TENANT_UID_ATTRIBUTE        = "tenant_uid";
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructor
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>UserView</code> state bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Adapter() {
    super();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   submit
  /**
   ** The action listener to be called when the submit button is clicked.
   ** This event can be trigger on multiple request on the catalog. Here we 
   ** catch only events concerning user create operation for generating a
   ** new UID for the user. Then the original submit method is called.
   **
   ** @para  event               the {@link ActionEvent} object that
   **                            characterize the action to perform
   **                            <br>
   **                            Allowed object is {@link ActionEvent}
   */
  public void submit(ActionEvent event) {
    final String requestType = CartUtils.getRequestType();
    Object userUID = null;
    //Catch only user creation request
    if (requestType.equals(CREATE_USER_REQUEST)) {
      final String usrOrg    = fetchUserOrgKey();
      final String orgTenant = organizationTenant(usrOrg, false);
      // Generate new UID
      final Map param = new HashMap();
      param.put("tenant", orgTenant);
      userUID = ADF.executeAction("requestUniqueIdentifier", param);
    }
  
    if (FacesUtils.executeOperationBindingFromActionListenerSucceeded(userUID)) {
      addAttributeOnUserVO(UNIQUEIDENTIFIER_ATTRIBUTE, userUID);
      final MethodExpression originalEvent = FacesUtils.getMethodExpressionFromEL("#{backingBeanScope.cartReqBean.submitActionListener}",
                                                                                null,
                                                                                new Class[] {ActionEvent.class});
      originalEvent.invoke(FacesUtils.getELContext(), new Object[] {event});
    }
    
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchUserOrgKey
  /**
   ** Retrieves the user organization key from the cart.
   ** <br>
   **
   ** @return                   the user organization key from the cart.
   */
  private String fetchUserOrgKey() {
    final Object orgKey = FacesUtils.executeOperationBindingFromActionListener("getUserOrgKey");
    return orgKey != null ? orgKey.toString() : null;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   organization
  /**
   ** Looks up an already existing Organization entity.
   **
   ** @param  identifier         the identifier of the organization entity to be
   **                            looked up.
   **                            <br>
   **                            Allowed object is {@link String}
   ** @param  returning          the {@link Set} of attributes of the
   **                            Organization to be returned.
   **                            <br>
   **                            Allowed object is {@link String}
   ** @param  name               <code>true</code>, if <code>identifier</code>
   **                            contains organization name and
   **                            <code>false</code> if <code>identifier</code>
   **                            is organization key.
   **
   ** @return                    a name of an organization entity matching the
   **                            given <code>identifier</code>.
   */
  private Organization organization(final String identifier, final Set<String> returning, final boolean name) {
    final OrganizationManager service = OIMClientFactory.getOrganizationManager();
    try {
      return service.getDetails(identifier, returning, name);
    }
    catch (OrganizationManagerException e) {
      System.err.println(e.getLocalizedMessage());
      return null;
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationTenant
  /**
   ** Looks up an already existing Organization entity and returns the tenant
   ** value for this organization.
   **
   ** @param  identifier         the identifier of the organization entity to
   **                            be looked up.
   **                            <br>
   **                            Allowed object is {@link String}
   ** @param  name               <code>true</code>, if <code>identifier</code>
   **                            contains organization name and
   **                            <code>false</code> if <code>identifier</code>
   **                            is organization key.
   **                            <br>
   **                            Allowed object is {@link String}
   **
   ** @return                    the tenant value of an organization entity
   **                            matching the given <code>identifier</code>.
   */
  private String organizationTenant(final String identifier, final boolean name) {
    final Set<String> returning = new HashSet<String>();
    returning.add(TENANT_UID_ATTRIBUTE);
    final Organization organization = organization(identifier, returning, name);
    return organization == null ? null : (String)organization.getAttribute(TENANT_UID_ATTRIBUTE);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAttributeOnUserVO
  /**
   ** Insert the provided attribute in the User view object.
   **
   ** @param  attribute          the name of the attibute to insert.
   **                            <br>
   **                            Allowed object is {@link String}
   ** @param  value              The value of the attribute to insert.
   **                            <br>
   **                            Allowed object is {@link String}
   */
  private void addAttributeOnUserVO(final String attribute, final Object value){
    ApplicationModule am = ADF.applicationModule("CatalogAMDataControl");
    if (am != null) {
      ViewObject vo = am.findViewObject("userVO");
      if (vo != null)
        vo.getCurrentRow().setAttribute(attribute, value);
    }
  }
}
