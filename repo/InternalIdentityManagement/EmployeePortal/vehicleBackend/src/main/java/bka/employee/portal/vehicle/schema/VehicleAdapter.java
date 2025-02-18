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

    File        :   VehiculeAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    VehiculeAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2020-06-30  SBernet     First release version
*/
package bka.employee.portal.vehicle.schema;

import java.math.BigInteger;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

import oracle.jbo.Row;
import oracle.jbo.domain.DBSequence;

////////////////////////////////////////////////////////////////////////////////
// class VehiculeAdapter
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by <code>Vehicule</code> customization.
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
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class VehicleAdapter extends StaticModelAdapterBean{

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String ID             = "id";
  public static final String CARPLATENUMBER = "carPlateNumber";
  public static final String USRKEY         = "usrKey";
  public static final String BRANDKEY       = "brandKey";
  public static final String COLORKEY       = "colorKey";
  public static final String TYPEKEY        = "typeKey";
  public static final String ROWVERSION     = "rowVersion";
  public static final String CREATEDBY      = "createdBy";
  public static final String CREATEDON      = "createdOn";
  public static final String UPDATEDBY      = "updatedBy";
  public static final String UPDATEDON      = "updatedOn";
  public static final String DEL            = "del";
  public static final String MOD            = "mod";
  
  @SuppressWarnings("compatibility:-6449697903801128019")
  private static final long  serialVersionUID = -3716454596750996247L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private DBSequence id;
  @ModelAttr
  private String     carPlateNumber;
  @ModelAttr
  private BigInteger usrKey;
  @ModelAttr
  private String     brandKey;
  @ModelAttr
  private String     colorKey;
  @ModelAttr
  private String     typeKey;
  @ModelAttr
  private String     rowVersion;
  @ModelAttr
  private String     createdBy;
  @ModelAttr
  private String     createdOn;
  @ModelAttr
  private String     updatedBy;
  @ModelAttr
  private String     updatedOn;

  /**
   ** Constructs an empty <code>VehiculeAdapter</code> values
   ** object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */

  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  public VehicleAdapter() {
    // ensure inheritance
    super();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ApplicationInstanceAdapter</code> values object
   ** which use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   */
  public VehicleAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////


  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAppInstanceKey
  /**
   ** Sets the value of the id property.
   **
   ** @param  id               the value of the id property.
   **                          Allowed object is {@link DBSequence}.
   */
  public void setId(DBSequence id) {
    this.id = id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getId
  /**
   ** Returns the value of the id property.
   **
   ** @return                    the value of the id property.
   **                            Possible object is {@link DBSequence}.
   */
  public DBSequence getId() {
    return id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCarPlateNumber
  /**
   ** Sets the value of the carPlateNumber property.
   **
   ** @param  carPlateNumber     the value of the carPlateNumber property.
   **                            Possible object is {@link String}.
   */
  public void setCarPlateNumber(String carPlateNumber) {
    this.carPlateNumber = carPlateNumber;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCarPlateNumber
  /**
   ** Returns the value of the carPlateNumber property.
   **
   ** @return                    the value of the carPlateNumber property.
   **                            Possible object is {@link String}.
   */
  public String getCarPlateNumber() {
    return carPlateNumber;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setUsrKey
  /**
   ** Sets the value of the usrKey property.
   **
   ** @param  usrKey             the value of the usrKey property.
   **                            Possible object is {@link BigInteger}.
   */
  public void setUsrKey(BigInteger usrKey) {
    this.usrKey = usrKey;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getUsrKey
  /**
   ** Returns the value of the usrKey property.
   **
   ** @return                    the value of the usrKey property.
   **                            Possible object is {@link BigInteger}.
   */
  public BigInteger getUsrKey() {
    return usrKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setBrandKey
  /**
   ** Sets the value of the brandKey property.
   **
   ** @param  brandKey           the value of the brandKey property.
   **                            Possible object is {@link String}.
   */
  public void setBrandKey(String brandKey) {
    this.brandKey = brandKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getBrandKey
  /**
   ** Returns the value of the brandKey property.
   **
   ** @return                    the value of the brandKey property.
   **                            Possible object is {@link String}.
   */
  public String getBrandKey() {
    return brandKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setColorKey
  /**
   ** Sets the value of the colorKey property.
   **
   ** @param  colorKey           the value of the colorKey property.
   **                            Possible object is {@link String}.
   */
  public void setColorKey(String colorKey) {
    this.colorKey = colorKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getColorKey
  /**
   ** Returns the value of the colorKey property.
   **
   ** @return                    the value of the colorKey property.
   **                            Possible object is {@link String}.
   */
  public String getColorKey() {
    return colorKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setTypeKey
  /**
   ** Sets the value of the typeKey property.
   **
   ** @param  typeKey            the value of the typeKey property.
   **                            Possible object is {@link String}.
   */
  public void setTypeKey(String typeKey) {
    this.typeKey = typeKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getTypeKey
  /**
   ** Returns the value of the typeKey property.
   **
   ** @return                    the value of the typeKey property.
   **                            Possible object is {@link String}.
   */
  public String getTypeKey() {
    return typeKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRowVersion
  /**
   ** Sets the value of the rowVersion property.
   **
   ** @param  rowVersion         the value of the rowVersion property.
   **                            Possible object is {@link String}.
   */
  public void setRowVersion(String rowVersion) {
    this.rowVersion = rowVersion;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRowVersion
  /**
   ** Returns the value of the rowVersion property.
   **
   ** @return                    the value of the rowVersion property.
   **                            Possible object is {@link String}.
   */
  public String getRowVersion() {
    return rowVersion;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCreatedBy
  /**
   ** Sets the value of the createdBy property.
   **
   ** @param  createdBy          the value of the createdBy property.
   **                            Possible object is {@link String}.
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCreatedBy
  /**
   ** Returns the value of the createdBy property.
   **
   ** @return                    the value of the createdBy property.
   **                            Possible object is {@link String}.
   */
  public String getCreatedBy() {
    return createdBy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCreatedOn
  /**
   ** Sets the value of the createdOn property.
   **
   ** @param  createdOn          the value of the createdOn property.
   **                            Possible object is {@link String}.
   */
  public void setCreatedOn(String createdOn) {
    this.createdOn = createdOn;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCreatedOn
  /**
   ** Returns the value of the createdOn property.
   **
   ** @return                    the value of the createdOn property.
   **                            Possible object is {@link String}.
   */
  public String getCreatedOn() {
    return createdOn;
  }

  // Methode:   setUpdatedBy
  /**
   ** Sets the value of the updatedBy property.
   **
   ** @param  updatedBy          the value of the updatedBy property.
   **                            Possible object is {@link String}.
   */
  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  //////////////////////////////////////////////////////////////////////////////								 
  // Methode:   getUpdatedBy
  /**
   ** Returns the value of the updatedBy property.
   **
   ** @return                    the value of the updatedBy property.
   **                            Possible object is {@link String}.
   */
  public String getUpdatedBy() {
    return updatedBy;
  }

  // Methode:   setUpdatedOn
  /**
   ** Sets the value of the updatedOn property.
   **
   ** @param  updatedOn          the value of the updatedOn property.
   **                            Possible object is {@link String}.
   */
  public void setUpdatedOn(String updatedOn) {
    this.updatedOn = updatedOn;
  }

  //////////////////////////////////////////////////////////////////////////////								 
  // Methode:   getUpdatedBy
  /**
   ** Returns the value of the updatedOn property.
   **
   ** @return                    the value of the updatedOn property.
   **                            Possible object is {@link String}.
   */
  public String getUpdatedOn() {
    return updatedOn;
  }
}