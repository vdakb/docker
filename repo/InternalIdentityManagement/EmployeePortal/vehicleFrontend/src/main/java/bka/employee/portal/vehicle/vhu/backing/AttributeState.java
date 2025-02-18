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

    Copyright © 2016. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Employee Self Service Portal
    Subsystem   :   Vehicle Administration

    File        :   AttributeState.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    AttributeState.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2020-05-28  SBernet     First release version
*/
package bka.employee.portal.vehicle.vhu.backing;

import bka.employee.portal.train.state.AbstractStep;
import bka.employee.portal.vehicle.model.view.BrandVORowImpl;
import bka.employee.portal.vehicle.schema.VehicleAdapter;
import bka.employee.portal.vehicle.vhu.state.Train;

import java.io.Serializable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;

import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.hst.foundation.faces.ADF;
import oracle.hst.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class AttributeState
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to manage attibute
 ** values of <code>Vehicle Brand Manufactor</code>s.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AttributeState extends AbstractStep {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String            DETAIL_ITERATOR  = "VehicleIterator";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5808856838884424235")
  private static final long            serialVersionUID = 88749107106101882L;
  
  private RichInputText                brandKey;
  private RichInputText                brandNameComponent;
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AttributeState</code> backing bean that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AttributeState() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setBrandNameComponent
  /**
   ** Sets the UI brandNameComponent which renders the input component
   ** belonging to the property name.
   **
   ** @param  brandNameComponent the UI brandNameComponent which renders the
   **                            input component belonging to the property name.
   **                            Allowed object is {@link RichInputText}.
   */
  public void setBrandNameComponent(RichInputText brandNameComponent) {
    this.brandNameComponent = brandNameComponent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getBrandNameComponent
  /**
   ** Returns the UI brandNameComponent which renders the endpoint name input
   ** component in the attribute region.
   **
   ** @return                    the UI brandNameComponent which renders the
   **                            endpoint name input component in the attribute
   **                            region.
   **                            Possible object is {@link RichInputText}.
   */
  public RichInputText getBrandNameComponent() {
    return brandNameComponent;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setPropertyName
  /**
   ** Sets the UI brandKey which renders the input component belonging to the
   ** property name.   
   **
   ** @param  brandKey           the UI brandKey which renders the input
   **                            component belonging to the property name.
   **                            Allowed object is {@link RichInputText}.
   */
  public void setBrandKey(RichInputText brandKey) {
    this.brandKey = brandKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getBrandKey
  /**
   ** Returns the UI brandKey which renders the endpoint name input component
   ** in the attribute region.
   **
   ** @return                    the UI brandKey which renders the endpoint
   **                            name input component in the attribute region.
   **                            Possible object is {@link RichInputText}.
   */
  public RichInputText getBrandKey() {
    return brandKey;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityDistinguisher
  /**
   ** Factory method to creat a unique entity distinguisher to provide the
   ** possibility to identify the taskflow which raised the contextual event
   ** to pick a organization by the event handler.
   ** <br>
   ** The event handler itself is registered for every region.
   ** <p>
   ** The string of the created identifier consists of
   ** <ol>
   **   <li>the hardcoded prefix <code>app</code>
   **   <li>the instance identifier of the current taskflow obtained from the
   **        page flow scope.
   ** </ol>
   **
   ** @return                    the created unique event name.
   */
  public static String entityDistinguisher() {
    return String.format("assign_brand_to_vehicle:%s", ADF.pageFlowScopeStringValue("carID"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pickListener
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** taskflow to select vehicle brand to be added.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   */
  public void pickListener(final @SuppressWarnings("unused") ActionEvent event) {
    final Map<String, Object> parameter = new HashMap<String, Object>();
    
    if (getBrandNameComponent() != null) {
      getBrandNameComponent().setSubmittedValue(null);
    }
    // it's required to distinct the taskflow of the picker to give the event
    // handler the ability to bypass taskflows which didn't raise the event due
    // to that the event handler is registered for every region and there can be
    // more than one taskflow on the same page
    parameter.put(EVENT_DISTINGUISHER, entityDistinguisher());
    parameter.put(PARAMETER_MODE,                         MODE_ASSIGN);
    raiseTaskFlowLaunchEvent("assign_brand_to_vehicle", "/WEB-INF/bka/employee/portal/vehicle/vmb/flow/picker-tf.xml", "Brand Picker", "/images/add_ena.png", null, null, true, parameter);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methode:   assign
  /**
   ** Adds the given targets to the collection of scopes to be allowed.
   **
   ** @param  selection          the collection of scopes to be added to the
   **                            scopes to be allowed.
   */
  public void assign(final List<Serializable> selection) {
    if (!CollectionUtility.empty(selection)) {
      final Train               train  = (Train)getTrain();
      final Map<String, Object> modify = train.scopeModify();
	  if (selection != null && selection.size() == 1) {
        final BrandVORowImpl  selectedBrand = (BrandVORowImpl)selection.get(0);
        modify.put(VehicleAdapter.BRANDKEY , selectedBrand.getId());
        train.markDirty();
        refresh();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Refreshs the publication region of the <code>Vehicule</code>.
   */
  private void refresh() {
    final Train train = (Train)getTrain();
    final Map<String, Object> pending = train.scopeModify();
    final Map<String, Object> parameter = new HashMap<String, Object>();
    
    parameter.put("identifier", ADF.pageFlowScopeStringValue("carId"));
    parameter.put("pending",    pending);
    ADF.executeOperation("requeryAttribute", parameter);
  }
}
