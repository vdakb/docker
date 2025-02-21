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

    File        :   EntitlementVORowImpl.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EntitlementVORowImpl.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.model.view;

import oracle.jbo.server.ViewRowImpl;

import oracle.iam.identity.sysauthz.model.entity.EntitlementEOImpl;

////////////////////////////////////////////////////////////////////////////////
// class EntitlementVORowImpl
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** ---------------------------------------------------------------------
 ** --- File generated by Oracle ADF Business Components Design Time.
 ** --- Tue Mar 07 07:51:54 CET 2017
 ** --- Custom code may be added to this class.
 ** --- Warning: Do not modify method signatures of generated methods.
 ** ---------------------------------------------------------------------
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class EntitlementVORowImpl extends ViewRowImpl {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final int ENTITLEMENTKEY   = AttributesEnum.entitlementKey.ordinal();
  public static final int ENTITYTYPE       = AttributesEnum.entityType.ordinal();
  public static final int OBJECTSKEY       = AttributesEnum.objectsKey.ordinal();
  public static final int OBJECTSNAME      = AttributesEnum.objectsName.ordinal();
  public static final int ENDPOINTKEY      = AttributesEnum.endpointKey.ordinal();
  public static final int ENDPOINTNAME     = AttributesEnum.endpointName.ordinal();
  public static final int ENTITLEMENTCODE  = AttributesEnum.entitlementCode.ordinal();
  public static final int ENTITLEMENTVALUE = AttributesEnum.entitlementValue.ordinal();
  public static final int DISPLAYNAME      = AttributesEnum.displayName.ordinal();
  public static final int DESCRIPTION      = AttributesEnum.description.ordinal();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class AttributesEnum
  /**
   ** AttributesEnum: generated enum for identifying attributes and accessors.
   ** Do not modify.
   */
  @SuppressWarnings({ "oracle.jbo.attribute-enumeration-classes-rule", "oracle.jbo.attribute-order-rule" })
  private static enum AttributesEnum {
     entitlementKey
   , entityType
   , objectsKey
   , objectsName
   , endpointKey
   , endpointName
   , entitlementCode
   , entitlementValue
   , displayName
   , description
   ;
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EntitlementVORowImpl</code> view object that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EntitlementVORowImpl() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEntitlementEO
  /**
   ** Returns the Entitlement entity object.
   **
   ** @return                    the getEntitlementEO
   */
  public EntitlementEOImpl getEntitlementEO() {
    return (EntitlementEOImpl)getEntity(0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setIdentifier
  /**
   ** Sets the attribute value for <code>ENTITLEMENTKEY</code> using the alias
   ** name <code>entitlementKey</code>.
   **
   ** @param  value              the attribute value for
   **                            <code>ENTITLEMENTKEY</code> using the alias
   **                            name <code>entitlementKey</code>.
   **                            Allowed object is {@link Long}.
   */
  public void setIdentifier(final Long value) {
    setAttributeInternal(ENTITLEMENTKEY, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getIdentifier
  /**
   ** Returns the attribute value for <code>ENTITLEMENTKEY</code> using the
   ** alias name <code>entitlementKey</code>.
   **
   ** @return                    the attribute value for
   **                            <code>ENTITLEMENTKEY</code> using the alias
   **                            name <code>entitlementKey</code>.
   **                            Possible object is {@link Long}.
   */
  public Long getIdentifier() {
    return (Long)getAttributeInternal(ENTITLEMENTKEY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEntityType
  /**
   ** Sets the attribute value for <code>ENTITYTYPE</code> using the alias
   ** name <code>entityType</code>.
   **
   ** @param  value              the attribute value for
   **                            <code>ENTITYTYPE</code> using the alias
   **                            name <code>entityType</code>.
   **                            Allowed display is {@link String}.
   */
  public void setEntityType(final String value) {
    setAttributeInternal(ENTITYTYPE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEntityType
  /**
   ** Returns the attribute value for <code>ENTITYTYPE</code> using the
   ** alias name <code>entityType</code>.
   **
   ** @return                    the attribute value for
   **                            <code>ENTITYTYPE</code> using the alias
   **                            name <code>entityType</code>.
   **                            Possible display is {@link String}.
   */
  public String getEntityType() {
    return (String)getAttributeInternal(ENTITYTYPE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setObjectsKey
  /**
   ** Sets the attribute value for <code>OBJECTSKEY</code> using the alias name
   ** <code>objectsKey</code>.
   **
   ** @param  value              the attribute value for <code>OBJECTSKEY</code>
   **                            using the alias name <code>objectsKey</code>.
   **                            Allowed object is {@link String}.
   */
  public void setObjectsKey(final String value) {
    setAttributeInternal(OBJECTSKEY, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getObjectsKey
  /**
   ** Returns the attribute value for <code>OBJECTSKEY</code> using the alias
   ** name <code>objectsKey</code>.
   **
   ** @return                    the attribute value for <code>OBJECTSKEY</code>
   **                            using the alias name <code>objectsKey</code>.
   **                            Possible object is {@link String}.
   */
  public String getObjectsKey() {
    return (String)getAttributeInternal(OBJECTSKEY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setObjectsName
  /**
   ** Sets the attribute value for <code>OBJECTSNAME</code> using the alias
   ** name <code>objectsName</code>.
   **
   ** @param  value              the attribute value for
   **                            <code>OBJECTSNAME</code> using the alias
   **                            name <code>objectsName</code>.
   **                            Allowed display is {@link String}.
   */
  public void setObjectsName(final String value) {
    setAttributeInternal(OBJECTSNAME, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getObjectsName
  /**
   ** Returns the attribute value for <code>OBJECTSNAME</code> using the
   ** alias name <code>objectsName</code>.
   **
   ** @return                    the attribute value for
   **                            <code>OBJECTSNAME</code> using the alias
   **                            name <code>objectsName</code>.
   **                            Possible display is {@link String}.
   */
  public String getObjectsName() {
    return (String)getAttributeInternal(OBJECTSNAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEndpointKey
  /**
   ** Sets the attribute value for <code>ENDPOINTKEY</code> using the alias name
   ** <code>endpointKey</code>.
   **
   ** @param  value              the attribute value for <code>ENDPOINTKEY</code>
   **                            using the alias name <code>endpointKey</code>.
   **                            Allowed endpoint is {@link String}.
   */
  public void setEndpointKey(final String value) {
    setAttributeInternal(ENDPOINTKEY, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEndpointKey
  /**
   ** Returns the attribute value for <code>ENDPOINTKEY</code> using the alias
   ** name <code>endpointKey</code>.
   **
   ** @return                    the attribute value for <code>ENDPOINTKEY</code>
   **                            using the alias name <code>endpointKey</code>.
   **                            Possible endpoint is {@link String}.
   */
  public String getEndpointKey() {
    return (String)getAttributeInternal(ENDPOINTKEY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEndpointName
  /**
   ** Sets the attribute value for <code>ENDPOINTNAME</code> using the alias
   ** name <code>endpointName</code>.
   **
   ** @param  value              the attribute value for
   **                            <code>ENDPOINTNAME</code> using the alias
   **                            name <code>endpointName</code>.
   **                            Allowed display is {@link String}.
   */
  public void setEndpointName(final String value) {
    setAttributeInternal(ENDPOINTNAME, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEndpointName
  /**
   ** Returns the attribute value for <code>ENDPOINTNAME</code> using the
   ** alias name <code>endpointName</code>.
   **
   ** @return                    the attribute value for
   **                            <code>ENDPOINTNAME</code> using the alias
   **                            name <code>endpointName</code>.
   **                            Possible display is {@link String}.
   */
  public String getEndpointName() {
    return (String)getAttributeInternal(ENDPOINTNAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEntitlementCode
  /**
   ** Sets the attribute value for <code>ENTITLEMENTCODE</code> using the alias
   ** name <code>entitlementCode</code>.
   **
   ** @param  value              the attribute value for
   **                            <code>ENTITLEMENTCODE</code> using the alias
   **                            name <code>entitlementCode</code>.
   **                            Allowed display is {@link String}.
   */
  public void setEntitlementCode(final String value) {
    setAttributeInternal(ENTITLEMENTCODE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEntitlementCode
  /**
   ** Returns the attribute value for <code>ENTITLEMENTCODE</code> using the
   ** alias name <code>entitlementCode</code>.
   **
   ** @return                    the attribute value for
   **                            <code>ENTITLEMENTCODE</code> using the alias
   **                            name <code>entitlementCode</code>.
   **                            Possible display is {@link String}.
   */
  public String getEntitlementCode() {
    return (String)getAttributeInternal(ENTITLEMENTCODE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEntitlementValue
  /**
   ** Sets the attribute value for <code>ENTITLEMENTVALUE</code> using the alias
   ** name <code>entitlementValue</code>.
   **
   ** @param  value              the attribute value for
   **                            <code>ENTITLEMENTVALUE</code> using the alias
   **                            name <code>entitlementValue</code>.
   **                            Allowed display is {@link String}.
   */
  public void setEntitlementValue(final String value) {
    setAttributeInternal(ENTITLEMENTVALUE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEntitlementValue
  /**
   ** Returns the attribute value for <code>ENTITLEMENTVALUE</code> using the
   ** alias name <code>entitlementValue</code>.
   **
   ** @return                    the attribute value for
   **                            <code>ENTITLEMENTVALUE</code> using the alias
   **                            name <code>entitlementValue</code>.
   **                            Possible display is {@link String}.
   */
  public String getEntitlementValue() {
    return (String)getAttributeInternal(ENTITLEMENTVALUE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setDisplayName
  /**
   ** Sets the attribute value for <code>DISPLAYNAME</code> using the alias name
   ** <code>displayName</code>.
   **
   ** @param  value              the attribute value for <code>DISPLAYNAME</code>
   **                            using the alias name <code>displayName</code>.
   **                            Allowed display is {@link String}.
   */
  public void setDisplayName(final String value) {
    setAttributeInternal(DISPLAYNAME, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getDisplayName
  /**
   ** Returns the attribute value for <code>DISPLAYNAME</code> using the alias
   ** name <code>displayName</code>.
   **
   ** @return                    the attribute value for <code>DISPLAYNAME</code>
   **                            using the alias name <code>displayName</code>.
   **                            Possible display is {@link String}.
   */
  public String getDisplayName() {
    return (String)getAttributeInternal(DISPLAYNAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setDescription
  /**
   ** Sets the attribute value for <code>DESCRIPTION</code> using the alias name
   ** <code>description</code>.
   **
   ** @param  value              the attribute value for <code>DESCRIPTION</code>
   **                            using the alias name <code>description</code>.
   **                            Allowed display is {@link String}.
   */
  public void setDescription(final String value) {
    setAttributeInternal(DESCRIPTION, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getDescription
  /**
   ** Returns the attribute value for <code>DESCRIPTION</code> using the alias
   ** name <code>description</code>.
   **
   ** @return                    the attribute value for <code>DESCRIPTION</code>
   **                            using the alias name <code>description</code>.
   **                            Possible display is {@link String}.
   */
  public String getDescription() {
    return (String)getAttributeInternal(DESCRIPTION);
  }
}