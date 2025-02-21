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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Employee Self Service Portal
    Subsystem   :   Vehicle Backend Model Component

    File        :   VehicleAMImpl.java

    Compiler    :   Oracle JDeveloper 12g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    VehicleAMImpl.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2020-02-23  DSteding    First release version
*/

package bka.employee.portal.vehicle.module;

import bka.employee.portal.vehicle.model.view.VehicleVOImpl;
import bka.employee.portal.vehicle.model.view.VehicleVORowImpl;
import bka.employee.portal.vehicle.module.common.VehicleAM;

import java.util.Map;

import oracle.iam.ui.platform.model.common.OIMClientFactory;

import oracle.jbo.Row;
import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewCriteriaRow;
import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.TransactionEvent;

////////////////////////////////////////////////////////////////////////////////
// class VehicleAMImpl
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The local {@link ApplicationModuleImpl} to coordinate certain task belonging
 ** to Employee Portal Vehicle Management.
 ** <p>
 ** ---------------------------------------------------------------------
 ** --- File generated by Oracle ADF Business Components Design Time.
 ** ---    Fri Feb 23 12:08:29 CET 2018
 ** --- Custom code may be added to this class.
 ** --- Warning: Do not modify method signatures of generated methods.
 ** ---------------------------------------------------------------------
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class VehicleAMImpl extends    ApplicationModuleImpl
                           implements VehicleAM {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>VehicleAMImpl</code> application module that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argumment constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public VehicleAMImpl() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   detailVehicleView
  /**
   ** Returns the VO VehicleVOImpl which renders the endpoint name input
   ** component in the attribute region.
   **
   ** @return                    the UI brandNameComponent which renders the
   **                            endpoint name input component in the attribute
   **                            region.
   **                            Possible object is {@link VehicleVOImpl}.
   */
  private VehicleVOImpl detailVehicleView() {
    return (VehicleVOImpl)findViewObject("VehicleDetailVO");
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchVehicleByIdentifier (AdministrationAM)
  /**
   ** Finds a certain  <code>Vehicle</code> by executing a
   ** query against the persistence layer which leverage the primary key of the
   ** entity object.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>Vehicle</code> to fetch
   **                            from the persistence layer.
   **                            Allowed object is {@link String}.
   */
  @Override
  public void fetchVehicleByIdentifier(final String identifier) {
    final VehicleVOImpl view = detailVehicleView(); 
    view.removeViewCriteria("VehicleDetailsSearchViewCriteria");
    final ViewCriteria vc = view.createViewCriteria();
    vc.setName("VehicleDetailsSearchViewCriteria");
    final ViewCriteriaRow vcr = vc.createViewCriteriaRow();
    vcr.setAttribute("id", identifier);
    vcr.setOperator("id", "=");
    vc.add(vcr);
    view.applyViewCriteria(vc);
    view.executeQuery();
  }
  
    
  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshVehiculeAttribute
  /**
   ** Refresh the attributes belonging to a certain
   ** <code>Vehicle</code>.
   ** 
   */
  @Override
  public void refreshVehiculeAttribute() {
    final VehicleVOImpl view = detailVehicleView();
    view.reset();
    view.executeQuery();
  }

  //////////////////////////////////////////////////////////////////////////////
  //  Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getVehicleSearchVO
  /**
   ** Container's getter for <code>VehicleVOImpl</code>.
   **
   ** @return                    the implementation of view object
   **                            <code>VehicleVOImpl</code>.
   */
  public VehicleVOImpl getVehicleSearchVO() {
    return (VehicleVOImpl)findViewObject("VehicleSearchVO");
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   getVehicleDetailVO
  /**
   ** Container's getter for <code>VehicleVOImpl</code>.
   **
   ** @return                    the implementation of view object
   **                            <code>VehicleVOImpl</code>.
   */
  public VehicleVOImpl getVehicleDetailVO() {
    return (VehicleVOImpl)findViewObject("VehicleDetailVO");
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////
 
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDetail (VehicleAM)
  /**
   ** Initialize the model to create a new <code>Vehicule</code>.
   */
  @Override
  public void createDetail() {
    final VehicleVOImpl  detail = detailVehicleView();
    final Row        row    = detail.createRow();
    
    String loggedUserKey = OIMClientFactory.getLoginUserKey();
    if (loggedUserKey != null) {
      row.setAttribute(VehicleVORowImpl.USRKEY, loggedUserKey);
    }
    // setting the proper row state to avoid the row is considered as candidate
    // for pending changes in ADF, until user types value by himself.
    row.setNewRowState(Row.STATUS_INITIALIZED);
    detail.insertRow(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requeryPublication (ApplicationInstanceAM)
  /**
   ** Initialize the <code>Attribute</code>s belonging to a certain
   ** <code>Vehicule</code> to take in account pending changes on
   ** attribute.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>Vehicule</code> to
   **                            initialize.
   **                            Allowed object is {@link String}.
   ** @param  pending            the collection of pending changes belonging to
   **                            modified attibute.
   */
  @Override
    public void requeryAttribute(String identifier, Map<String, Object> pending) {
    final VehicleVOImpl view = detailVehicleView();
    Row row = view.getCurrentRow();
    if (row != null) {
      for (Map.Entry<String, Object> attribute : pending.entrySet()) {
        row.setAttribute(attribute.getKey(), attribute.getValue());
      }
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented inherit class
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeCommit (VehicleAM)
  /**
   ** Polls transaction listeners before a commit operation.
   */
  @Override
  public void beforeCommit(TransactionEvent transactionEvent) {
    super.beforeCommit(transactionEvent);
  }
}
