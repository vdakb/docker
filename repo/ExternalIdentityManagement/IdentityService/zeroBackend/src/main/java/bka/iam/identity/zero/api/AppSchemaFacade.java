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

    Copyright © 2023. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   ZeRo Backend

    File        :   AppSchemaFacade.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com

    Purpose     :   This file implements the class
                    AppSchemaFacade.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-17-03  AFarkas     First release version
*/

package bka.iam.identity.zero.api;


import bka.iam.identity.helper.Helper;
import bka.iam.identity.zero.model.SchemaAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.security.auth.login.LoginException;

import javax.sql.DataSource;

import Thor.API.tcResultSet;

import Thor.API.Operations.tcLookupOperationsIntf;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcInvalidLookupException;

import oracle.hst.platform.core.logging.Logger;

import oracle.iam.platform.OIMInternalClient;

import oracle.iam.provisioning.vo.FormInfo;
import oracle.iam.provisioning.vo.FormField;
import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.iam.provisioning.api.ApplicationInstanceService;

import oracle.iam.provisioning.exception.GenericAppInstanceServiceException;
import oracle.iam.provisioning.exception.ApplicationInstanceNotFoundException;

////////////////////////////////////////////////////////////////////////////////
// class AppSchemaFacade
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The session facade to manage
 ** {@link bka.iam.identity.zero.model.SchemaAttribute} entity.
 **
 ** @author adrien.farkas@oracle.com
 ** @version 1.0.0.0
 ** @since 1.0.0.0
 */
@Stateless(name = AppSchemaFacade.NAME)
public class AppSchemaFacade {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  public static final String  NAME         = "AppSchemaFacade";
  
  // Establish some reasonable defaults
  // Don't forger that SQL SELECT command indexes from 0!
  public static final int     START        = 1;
  public static final int     ITEMS        = 25;


  private static final String CLASS        = AppSchemaFacade.class.getName();
  private static final Logger LOGGER       = Logger.create(CLASS);
  
  //////////////////////////////////////////////////////////////////////////////
  // non-static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  private DataSource          operationsDS = null;

  private OIMInternalClient   client       = new OIMInternalClient(new Hashtable<String, String>());

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2063532653579549662")
  private static final long  serialVersionUID = 6164211711658242785L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AppSchemaFacade</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AppSchemaFacade() {
    // ensure inheritance
    super();
    final String method = "AppSchemaFacade";
    LOGGER.entering(CLASS, method);

    try {
      this.operationsDS = (DataSource)new InitialContext().lookup("jdbc/operationsDB");
    }
    catch (NamingException e) {
      LOGGER.throwing(CLASS, method, e);
    }
    LOGGER.exiting(CLASS, method);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Request to retrieve a certain {@link SchemaAttribute} by its <code>name</code>.
   **
   ** @param name                      the name of the {@link SchemaAttribute} to lookup.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **
   ** @return                          the {@link SchemaAttribute} mapped at <code>name</code>.
   **                                  <br>
   **                                  Possible object is {@link SchemaAttribute}.
   **
   ** @throws IllegalArgumentException if any error occurred.
   **/
  public List<SchemaAttribute> lookup(final String name)
    throws IllegalArgumentException {

    final String method = "lookup";
    LOGGER.entering(CLASS, method, "name=", name);

    List<SchemaAttribute> result = new ArrayList<>();

    if (!Helper.getIgnoredApplications(client).contains(name)) {
      ApplicationInstance appInstance = null;
      try {
        //      client = new OIMInternalClient(new Hashtable<String, String>());
        this.client.loginAsOIMInternal();
        ApplicationInstanceService appInstService = client.getService(ApplicationInstanceService.class);
        appInstance = appInstService.findApplicationInstanceByName(name);
        LOGGER.debug(CLASS, method, "Retrieved ApplicationInstance: " + appInstance);
        FormInfo formInfo = appInstance.getAccountForm();
        LOGGER.debug(CLASS, method, "Retrieved FormInfo: " + formInfo);
        for (FormField field : formInfo.getFormFields()) {
          LOGGER.debug(CLASS, method, "Processing Field: " + field);
          SchemaAttribute attr = buildAttribute(field);
          LOGGER.debug(CLASS, method, "Processed attribute: " + attr);
          result.add(attr);
        }

        List<FormInfo> childForms = appInstance.getChildForms();
        for (FormInfo childForm : childForms) {
          LOGGER.debug(CLASS, method, "  Retrieved Child FormInfo: " + formInfo);
          LOGGER.debug(CLASS, method, "  Child form name: " + childForm.getName());
          LOGGER.debug(CLASS, method, "  Child form key: " + childForm.getFormKey());

          SchemaAttribute       attr = new SchemaAttribute().name(childForm.getName());
          List<SchemaAttribute> innerAttrList = new ArrayList<>();
          for (FormField childFormField : childForm.getFormFields()) {
            SchemaAttribute innerAttr = buildAttribute(childFormField);
            LOGGER.debug(CLASS, method, "  Processing child attribute: " + innerAttr);
            innerAttrList.add(innerAttr);
          }
          attr.attrRef(innerAttrList);
          result.add(attr);
        }
      }
      catch (LoginException e) {
        final String message = "Login exception caught";
        LOGGER.error(CLASS, method, message);
        LOGGER.throwing(CLASS, method, e);
        throw new IllegalArgumentException(message, e);
      }
      catch (GenericAppInstanceServiceException e) {
        final String message = "Generic Application Instance service exception caught";
        LOGGER.error(CLASS, method, message);
        LOGGER.throwing(CLASS, method, e);
        throw new IllegalArgumentException(message, e);
      }
      catch (ApplicationInstanceNotFoundException e) {
        // Dummy catch, if the exception occurs let the processing finish and return empty result
        ;
      }
    }
    else {
      LOGGER.trace(CLASS, method, "Ignoring app " + name + " as per Lookup configuration");
    }
    LOGGER.exiting(CLASS, method);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAttribute
  /**
   ** Factory method to translate a {@link FormField} to a {@link SchemaAttribute}.
   **
   ** @param  field              the {@link FormField} field to be translated.
   **                            <br>
   **                            Allowed object is {@link FormField}.
   **
   ** @return                    the resulting {@link SchemaAttribute}.
   **                            <br>
   **                            Possible object is {@link SchemaAttribute}.
   */
  private SchemaAttribute buildAttribute(FormField field) {
    final String method = "buildAttribute";
    LOGGER.entering(CLASS, method);

    LOGGER.debug(CLASS, method, "Field: " + field.getName());
    LOGGER.debug(CLASS, method, "  label: " + field.getLabel());
    LOGGER.debug(CLASS, method, "  type: " + field.getType());
    LOGGER.debug(CLASS, method, "  variant type: " + field.getVariantType());
    LOGGER.debug(CLASS, method, "  length: " + field.getLength());
    SchemaAttribute attr = new SchemaAttribute().
                                                 name(field.getName()).
                                                 label(field.getLabel()).
                                                 type(field.getType()).
                                                 variantType(field.getVariantType()).
                                                 length(field.getLength()).
                                                 entitlement(false);
    Map<String, Object> props = field.getProperties();
    for (String setKey : props.keySet()) {
      LOGGER.debug(CLASS, method, "  property: " + setKey + ", value: " + props.get(setKey));
      if ("Required".equals(setKey)) {
        attr.required(Boolean.valueOf((String)props.get(setKey)));
      }
      else if ("Lookup Code".equals(setKey) && "LookupField".equals(field.getType())) {
        attr.lookupName((String)props.get(setKey));
      }
    }

    if ("LookupField".equals(field.getType()) && props.containsKey("Lookup Code")) {
      if (props.containsKey("Entitlement") && "true".equals(props.get("Entitlement"))) {
        LOGGER.debug(CLASS, method, "  Attribute is an entitlement, not fetching lookup values");
        attr.entitlement(true);
      }
      else {
        String lookupName = (String)props.get("Lookup Code");
        LOGGER.debug(CLASS, method, "  Fetching lookup: " + lookupName);
        try {
          tcLookupOperationsIntf    lookupService = client.getService(tcLookupOperationsIntf.class);
          tcResultSet               values = lookupService.getLookupValues(lookupName);
          List<Map<String, String>> lookupValues = new ArrayList<>();
          int                       j = 0;
          while (j < values.getRowCount()) {
            values.goToRow(j++);
            Map<String, String> lookupValue = new HashMap<>();
            lookupValue.put("key", values.getStringValue("Lookup Definition.Lookup Code Information.Code Key"));
            lookupValue.put("decode", values.getStringValue("Lookup Definition.Lookup Code Information.Decode"));
            lookupValues.add(lookupValue);
          }
          attr.lookupValues(lookupValues);
        }
        catch (tcAPIException | tcInvalidLookupException | tcColumnNotFoundException e) {
          LOGGER.throwing(CLASS, method, e);
        }
      }
    }
    return attr;
  }
}