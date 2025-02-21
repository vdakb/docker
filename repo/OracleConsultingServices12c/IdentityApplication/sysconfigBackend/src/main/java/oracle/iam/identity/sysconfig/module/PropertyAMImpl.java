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

    File        :   PropertyAMImpl.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PropertyAMImpl.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysconfig.module;

import oracle.jbo.Row;

import oracle.jbo.server.ApplicationModuleImpl;

import oracle.iam.ui.platform.model.filter.Criterion;

import oracle.iam.identity.sysconfig.module.common.PropertyAM;

import oracle.iam.identity.sysconfig.model.view.PropertyVOImpl;

import oracle.iam.identity.sysconfig.schema.PropertyAdapter;
import oracle.iam.identity.sysconfig.schema.PropertyDataProvider;

////////////////////////////////////////////////////////////////////////////////
// class PropertyAMImpl
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The local {@link ApplicationModuleImpl} to coordinate certain task
 ** belonging to <code>Properties</code>.
 ** <p>
 ** ---------------------------------------------------------------------
 ** --- File generated by Oracle ADF Business Components Design Time.
 ** --- Mon Mar 06 19:50:30 CET 2017
 ** --- Custom code may be added to this class.
 ** --- Warning: Do not modify method signatures of generated methods.
 ** ---------------------------------------------------------------------
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class PropertyAMImpl extends    ApplicationModuleImpl
                            implements PropertyAM {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PropertyAMImpl</code> application module that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PropertyAMImpl() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   detailView
  /**
   ** Container's getter for {@link PropertyVOImpl}.
   **
   ** @return                    the implementation of view object
   **                            <code>PropertyVO</code>.
   */
  private PropertyVOImpl detailView() {
    return (PropertyVOImpl)findViewObject("PropertyDetailVO");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchByIdentifier (PropertyAM)
  /**
   ** Finds a certain <code>Property Definition</code> by executing a query
   ** against the persistence layer which leverage the primary key of the entity
   ** object.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>Property Definition</code> to fetch from
   **                            the persistence layer.
   **                            Allowed object is {@link String}.
   */
  @Override
  public void fetchByIdentifier(final Long identifier) {
    final PropertyVOImpl view = detailView();
    view.applyFilter(new Criterion(PropertyAdapter.PK, identifier));
    view.executeQuery();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchByName (PropertyAM)
  /**
   ** Finds a certain <code>Property Definition</code> by executing a query
   ** against the persistence layer which leverage the unique key of the entity
   ** object.
   **
   ** @param  name               the unique identifier of the
   **                            <code>Property Definition</code> to fetch from
   **                            the persistence layer.
   **                            Allowed object is {@link String}.
   */
  @Override
  public void fetchByName(final String name) {
    final PropertyVOImpl view = detailView();
    view.applyFilter(new Criterion(PropertyAdapter.NAME, name));
    view.executeQuery();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDetail (PropertyAM)
  /**
   ** Initialize the model to create a new <code>Property Definition</code>.
   */
  @Override
  public void createDetail() {
    final PropertyVOImpl  detail = detailView();
    final PropertyAdapter bean   = new PropertyAdapter();
    final Row             row    = detail.createRow();
    // call toRow to initialize, row handles lookups as well.
    bean.toRow(row, true);
    // initialize the primary key of the property to create with the proper
    // value to ensure the the iterators and dependend entities find the correct
    // binding value to initialize themself
    row.setAttribute(PropertyAdapter.PK,            "-1");
    row.setAttribute(PropertyAdapter.DATALEVEL,     2);
    row.setAttribute(PropertyAdapter.SYSTEM,        "1");
    row.setAttribute(PropertyAdapter.LOGINREQUIRED, "0");
    row.setAttribute(PropertyAdapter.RUNON,         "S");
    // setting the proper row state to avoid the row is considered as candidate
    // for pending changes in ADF, until user types value by himself.
    row.setNewRowState(Row.STATUS_INITIALIZED);
    detail.insertRow(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDetail (PropertyAM)
  /**
   ** Creates a new <code>Property Definition</code>.
   **
   ** @param  mab                the {@link PropertyAdapter} model adapter bean,
   **                            with fields set to create.
   */
  @Override
  public void createDetail(final PropertyAdapter mab) {
    final PropertyVOImpl       detail   = detailView();
    final PropertyDataProvider provider = (PropertyDataProvider)detail.getDataProvider();
    provider.create(mab);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyDetail (PropertyAM)
  /**
   ** Modifies an existing <code>Property Definition</code>.
   **
   ** @param  mab                the {@link PropertyAdapter} model adapter bean,
   **                            with fields set to create.
   */
  @Override
  public void modifyDetail(final PropertyAdapter mab) {
    final PropertyVOImpl       detail   = detailView();
    final PropertyDataProvider provider = (PropertyDataProvider)detail.getDataProvider();
    provider.update(mab);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchDetail (PropertyAM)
  /**
   ** Initialize the model to maintain an existing
   ** <code>Property Definition</code>.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>Property Definition</code> to fetch from
   **                            the persistence layer.
   **                            Allowed object is {@link Long}.
   */
  @Override
  public void fetchDetail(final Long identifier) {
    fetchByIdentifier(identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshAttribute (PropertyAM)
  /**
   ** Refresh the <code>Property Value</code>s belonging to a certain
   ** <code>Property Definition</code>.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>Property Definition</code> to refresh.
   **                            Allowed object is {@link Long}.
   */
  @Override
  public void refreshAttribute(final Long identifier) {
    final PropertyVOImpl view = detailView();
    view.reset();
    view.executeQuery();
  }
}