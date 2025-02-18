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

    Copyright 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   EFBS Connector

    File        :   EFBSSCIMConnector.java

    Compiler    :   JDK 1.8

    Author      :   richard.x.rutter@oracle.com

    Purpose     :   This custom connector implementation wraps the standard
                    Oracle Generic SCIM connector in order to fix the following
                    issues when intergating with the Rola EFBS SCIM service:

                    1. The SCIM specification defines the status attribute
                    'active' as optional. The Rola service defines this as
                    mandatory on create.
                    2. THe Rola SCIM service defines a csutom schema extension
                    called 'EFBSUSer'. The Standard SCIM connector does not
                    process this connector correctly, so requires a custom
                    schema parser to be created and invoked by the connector
                    class (this class) when initilizing its configuration.

                    Note the Rola SCIM service also does not correctly handle
                    PATCH replace operations for extension schemas. Some code
                    has been included in this class which could be used to
                    support replacement of the PATCH replace operations with
                    PUT operations in the event that the Rola Service cannot
                    be fixed. This code is commented out and is not fully
                    completed and tested.

                    Note also that the Rola SCIM service does not
                    support PATCH remove of multi-valued attributes (child tables)
                    and substitutes PATCH add operations with PATCH replace.
                    While this is not ideal it can be tolerated in our case
                    if child table delete operations are implemented as no-ops
                    and if only one child table entry is permitted.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2019  rirutter    First release version
*/
package bka.identityconnectors.genericscim;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Formatter;

import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

import java.util.ArrayList;

import org.identityconnectors.common.logging.Log;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;

import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.exceptions.ConfigurationException;

import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.ConnectorClass;

import org.identityconnectors.restcommon.utils.RESTCommonConstants;

import org.identityconnectors.restcommon.parser.spi.ParserPlugin;

import org.identityconnectors.genericscim.GenericSCIMConnector;
import org.identityconnectors.genericscim.GenericSCIMConnection;
import org.identityconnectors.genericscim.GenericSCIMConfiguration;

import org.identityconnectors.genericscim.utils.GenericSCIMUtil;
import org.identityconnectors.genericscim.utils.GenericSCIMConstants;

////////////////////////////////////////////////////////////////////////////////
// class EFBSSCIMConnector
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>EFBSSCIMConnector</code> wraps the <code>GenericSCIMConnector</code>
 ** providing a new implementation of the ICF SPI <code>create</code> method which
 ** adds the 'active' status attribute and also email and phone number entries.
 **
 ** Note a large section of commented-out code is included for support in handling
 ** modification of update should that be necessary. At the time of writing, update
 ** is not handled correctly by the Rola EFBS service which fails to handle PATCH
 ** / replace for non-core schema attributes. If the Rola solution cannot be modified
 ** then a custom solution might be required in which cause the ICF SPI update
 ** implementation could be changed update from submitting PATCH / replace requests
 ** to PUT requests. The commented-out code sections are the starting point for
 ** implementation of such a solution. Refer to comments alongside the code for details.
 ** It is acknowledged that commented-out code would normally best be handled via the SCS
 ** solution (svn), so the commented sections should ideally be removed once they are
 ** checked-in.
 **
 ** @author  richard.x.rutter@oracle.com
 ** @version 1.0.0.0
 */
@ConnectorClass(configurationClass = GenericSCIMConfiguration.class, displayNameKey = "display_GenericSCIMConnector")
public class EFBSSCIMConnector extends GenericSCIMConnector {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Log log = Log.getLog(GenericSCIMConnector.class);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  Map<String, String>                      parserConfigParamsMap;
  Map<String, String>                      reconSortByAttrs;
  Map<String, List<String>>                dateAttributes        = new HashMap<>(3);
  Map<String, Map<String, String>>         extAttrsAndSchemas    = new HashMap<>(3);
  Map<String, String>                      customPayloadMap      = new HashMap<>(3);
  Map<String, String>                      relURLsMap            = new HashMap<>(3);
  Map<String, String>                      httpOperationTypesMap = new HashMap<>(3);
  Map<String, String>                      coreSchemas           = new HashMap<>(3);

  private Map<String, String>              resourceEndpoints = new HashMap<>();
  private Map<String, Map<String, String>> namedAttributes;
  private ParserPlugin                     parser;
  private GenericSCIMConfiguration         config;
  private GenericSCIMConnection            connection;
  private String                           baseURI;
  private Schema                           schema;
  private Map<String, String>              attrToOClassMapping;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EFBSSCIMConnector</code> connector wrapping the
   ** standard <code>GenericSCIMConnector</code>.
   ** <br>
   ** Zero argument constructor required by the connector framework.
   ** <br>
   ** Default Constructor
   */
  public EFBSSCIMConnector() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   init (Connector)
  /**
   ** Initialize the connector with its configuration.
   ** <p>
   ** For instance in a JDBC Connector this would include the database URL,
   ** password, and user.
   ** <p>
   ** Makes a copy of the {@link Configuration} and constructs meta-data for use
   ** by overridden methods. Need to make this local copy because the standard
   ** connector keeps this meta-data private so this would not be available to
   ** overridden methods.
   **
   ** @param  configuration      the instance of the {@link Configuration}
   **                            object implemented by the
   **                            <code>Connector</code> developer and populated
   **                            with information in order to initialize the
   **                            <code>Connector</code>.
   */
  @Override
  public void init(final Configuration configuration) {
    // init config for wrapped connector to enable it to support operations not
    // overridden on this class
    super.init(configuration);

    // local config copy for create op
    this.config = (GenericSCIMConfiguration)configuration;
    log.ok("Initializing the Connector config.getDateFormat()={0}", new Object[] {this.config.getDateFormat()});
    log.error("Content-Type={0}", new Object[] {this.config.getContentType()});
    log.error("Accept={0}",       new Object[] {this.config.getAcceptType()});
    this.config.validate();
    this.connection = new GenericSCIMConnection(this.config);
    this.baseURI    = (new StringBuilder()).append(this.config.isSslEnabled() ? "https://" : "http://").append(this.config.getHost()).append(this.config.getPort() == 0 ? "" : (new StringBuilder()).append(":").append(String.valueOf(this.config.getPort())).toString()).append(this.config.getBaseURI()).toString();
    this.parser     = RESTClientHandler.getParserInstance(this.config.getCustomParserClassName());
    initConfigMaps();
    // invoke custom EFBSSCIMSchema class to ensure EFBSUser extension schema is loaded properly
    this.schema = EFBSSCIMSchema.buildSchema(this.connection, this.baseURI, this.config, this.namedAttributes, this.parser, this.parserConfigParamsMap, this.resourceEndpoints, this.coreSchemas, this.extAttrsAndSchemas, this.attrToOClassMapping);
    log.ok("Connector initialization completed, coreSchemas={0}, extAttrsAndSchemas={1} attrToOClassMapping={2}", new Object[]{this.coreSchemas, this.extAttrsAndSchemas, this.attrToOClassMapping});
    log.info("Connector initialization completed, schema={0}", new Object[] {this.schema});
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getConfiguration (overridden)
  /**
   ** Return the configuration that was passed to {@link #init(Configuration)}.
   **
   ** @return                    the configuration that was passed to
   **                            {@link #init(Configuration)}.
   */
  @Override
  public Configuration getConfiguration() {
    return this.config;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispose
  /**
   ** Dispose of local copy of connection and invoke dispose for wrapped
   ** connector class.
   */
  public void dispose() {
    log.info("Disposing connection to target");
    if (this.connection != null)
      this.connection.disposeConnection();
    // ensure inheritance
    super.dispose();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema (overridden)
  /**
   ** Describes the types of objects this Connector supports.
   ** <p>
   ** This method is considered an operation since determining supported objects
   ** may require configuration information and allows this determination to be
   ** dynamic.
   ** <p>
   ** The special [@link Uid} attribute should never appear in the schema, as it
   ** is not a real attribute of an object, rather a reference to it.
   ** <br>
   ** If the resource object-class has a writable unique id attribute that is
   ** different than its <code>Name</code>, then the schema should contain a
   ** resource-specific attribute that represents this unique id.
   ** <br>
   ** For example, a Unix account object might contain unix <code>uid</code>.
   **
   ** @return                    basic schema supported by this Connector
   **                            correctly incorporating EFBSUser as extension
   **                            schema.
   **                            Possible object {@link Schema}.
   */
  @Override
  public Schema schema() {
    return this.schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create (overridden)
  /**
   ** Taking the attributes given (which always includes the
   ** {@link ObjectClass}) and create an object and its {@link Uid}.
   ** <br>
   ** The Uid must returned so that the caller can refer to the created object.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** There will never be a {@link Uid} passed in with the attribute set for
   ** this method. If the resource supports some sort of mutable {@link Uid},
   ** you should create your own resource-specific attribute for it, such as
   ** <code>unix_uid</code>.
   ** <p>
   ** Implements custom create user request against the Rola EFBS target system.
   ** Note this is a standard ICF connector SPI create operation.
   ** The Generic SCIM standard connector needs to be customised for the
   ** following reasons:
   ** <ol>
   **  <li>Status attribute 'active' is not set in the standard connector
   **      implementation but the Rola EFBS SCIM service defines this as
   **      mandatory on create contrary to the SCIM specification.
   **  <li>Multi-valued attributes (email and phone numbers) are implemented as
   **      mandatory by the Rola EFBS SCIM service so values must be provided on
   **      create. The standard Generic SCIM connector however is implemented to
   **      create the user account without child table entries and then to add
   **      those following the create i.e. as two separate operations.
   **      <br>
   **      The Rola service rejects this.
   ** </ol>
   **
   ** @param  objectClass        the class of object for which to find the most
   **                            recent synchronization event (if any).
   **                            Must not be <code>null</code>.
   **                            Allowed object {@link ObjectClass}.
   ** @param  attribute          includes all the attributes necessary to create
   **                            the resource object including the
   **                            {@link ObjectClass} attribute and
   **                            <code>Name</code> attribute.
   **                            Allowed object {@link Set} where each element
   **                            is of type {@link Attribute}.
   ** @param  operation          the options that affect the way this operation
   **                            is run. If the caller passes <code>null</code>,
   **                            the framework will convert this into an empty
   **                            set of options, so an implementation need not
   **                            guard against this being <code>null</code>.
   **                            Allowed object {@link OperationOptions}.
   **
   ** @return                    the unique id for the object that is created.
   **                            For instance in LDAP this would be the 'dn',
   **                            for a database this would be the primary key,
   **                            and for 'ActiveDirectory' this would be the
   **                            GUID.
   **                            Possible object {@link Uid}.
   */
  @Override
  public Uid create(final ObjectClass objectClass, Set<Attribute> attribute, final OperationOptions operation) {
    log.info("In create() oclass={0}, oclass.getDisplayNameKey={1}, oclass.getObjectClassValue={2} attrSet={3}", new Object[] {objectClass, objectClass.getDisplayNameKey(), objectClass.getObjectClassValue(), attribute});
    if (attribute.isEmpty()) {
      log.error("Attribute set passed is empty.");
      throw new ConnectorException(config.getMessage("ex.emptyattrset", "Attribute set passed is empty."));
    }

    this.namedAttributes = GenericSCIMUtil.getNamedAttributeMap(config);
    GenericSCIMUtil.formDateAttrMap(dateAttributes, config);
    if (this.resourceEndpoints.containsKey(objectClass.getObjectClassValue())) {
      attribute = GenericSCIMUtil.convertSpclParentAttrToEmbdObj(attribute, objectClass.getObjectClassValue());
      attribute = GenericSCIMUtil.handleAttrValues(attribute, this.namedAttributes.get(objectClass.getObjectClassValue()), this.dateAttributes.get(objectClass.getObjectClassValue()), this.config);
      Map<String, Object> payload    = new HashMap<>();
      Set<Attribute>      addAttrSet = new HashSet<>();
      GenericSCIMUtil.formPayloadMapBelowV3(objectClass, attribute, this.namedAttributes.get(objectClass.getObjectClassValue()), payload, addAttrSet, this.schema, this.config, this.coreSchemas.get(objectClass.getObjectClassValue()), this.extAttrsAndSchemas.get(objectClass.getObjectClassValue()), this.attrToOClassMapping);
      log.ok("Payload origin -> {0}", new Object[] {RESTClientHandler.handlePasswordInLogs(this.parser, payload, this.namedAttributes.get(objectClass.getObjectClassValue()).get(OperationalAttributes.PASSWORD_NAME), this.parserConfigParamsMap)});
      // Add phone number. note this is a multi-valued attribute
      // for SCIM even though user in EFBS will only have one.
      // Note value is taken off the parent process form which
      // is mapped to the user profile.
      Object                    field = payload.remove("phone");
      List<Map<String, Object>> array = new ArrayList<Map<String, Object>>();
      Map<String, Object>       value = new HashMap<>();
      value.put(GenericSCIMConstants.PayLoadTags.VALUE.toString(), field == null ? "" : field);
      array.add(value);
      payload.put("phoneNumbers", array);

      // Add email. note this is a multi-valued attribute
      // for SCIM even though user in EFBS will only have one.
      // Note value is taken off the parent process form which
      // is mapped to the user profile.
      field = payload.remove("email");
      array = new ArrayList<Map<String, Object>>();
      value = new HashMap<>();
      value.put(GenericSCIMConstants.PayLoadTags.VALUE.toString(), field == null ? "" : field);
      array.add(value);
      payload.put("emails", array);

      // set the account status to active on create
      payload.put("active", Boolean.TRUE);
      log.ok("Payload target -> {0}", new Object[] {RESTClientHandler.handlePasswordInLogs(this.parser, payload, this.namedAttributes.get(objectClass.getObjectClassValue()).get(OperationalAttributes.PASSWORD_NAME), this.parserConfigParamsMap)});

      List<Map<String, Object>> respList = this.parser.parseResponse(
        RESTClientHandler.executeRequest(
          this.connection.getConnection()
        , (new StringBuilder()).append(this.baseURI).append((String)this.resourceEndpoints.get(objectClass.getObjectClassValue())).toString()
        , RESTCommonConstants.HTTPOperationType.POST
        , this.parser.parseRequest(payload, this.parserConfigParamsMap)
        )
      , this.parserConfigParamsMap
      );
      payload = respList.get(0);
      Uid uid = new Uid((String)payload.get("id"));
      if (addAttrSet.size() != 0) {
        addAttributeValues(objectClass, uid, addAttrSet, operation);
        log.error("Create Operation is not supported for object class {0}. Supported object classes are {1}", new Object[] {objectClass.getObjectClassValue(), resourceEndpoints.keySet()});
      }
      return uid;
    }
    else {
      throw new UnsupportedOperationException(config.getMessage("ex.unsupportedoperation", (new StringBuilder()).append("Create Operation is not supported for object class ").append(objectClass.getObjectClassValue()).append(". Supported object classes are ").append(resourceEndpoints.keySet()).toString(), new Object[] { "Create", objectClass.getObjectClassValue(), resourceEndpoints.keySet() }));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update (overridden)
  /**
   ** Update the object specified by the {@link ObjectClass} and [@link Uid},
   ** replacing the current values of each attribute with the values provided.
   ** <p>
   ** For each input attribute, replace all of the current values of that
   ** attribute in the target object with the values of that attribute.
   ** <p>
   ** If the target object does not currently contain an attribute that the
   ** input set contains, then add this attribute (along with the provided
   ** values) to the target object.
   ** <p>
   ** If the value of an attribute in the input set is <code>null</code>, then
   ** do one of the following, depending on which is most appropriate for the
   ** target:
   ** <ul>
   **   <li>If possible, <em>remove</em> that attribute from the target object
   **        entirely.
   **   <li>Otherwise, <em>replace all of the current values</em> of that
   **       attribute in the target object with a single value of
   **       <code>null</code>.
   ** </ul>
   **
   ** @param  objectClass        type of object to modify.
   **                            Must not be <code>null</code>.
   **                            Allowed object {@link ObjectClass}.
   ** @param  uid                the unique id that specifies the object to
   **                            modify.
   **                            Must not be <code>null</code>.
   **                            Allowed object {@link Uid}.
   ** @param  replace            the {@link Set} of new {@link Attribute}s the
   **                            values in this set represent the new, merged
   **                            values to be applied to the object. This set
   **                            may also include operational attributes.
   **                            Must not be <code>null</code>.
   **                            Allowed object {@link Set} where each element
   **                            is of type {@link Attribute}.
   ** @param  option             the options that affect the way this operation
   **                            is run. If the caller passes <code>null</code>,
   **                            the framework will convert this into an empty
   **                            set of options, so an implementation need not
   **                            guard against this being <code>null</code>.
   **                            Allowed object {@link OperationOptions}.
   **
   ** @return                    the unique id of the modified object in case
   **                            the update changes the formation of the unique
   **                            identifier.
   **                            Possible object {@link Uid}.
   */
  @Override
  public Uid update(final ObjectClass objectClass, final Uid uid, Set<Attribute> replace, final OperationOptions option) {
    if (replace.isEmpty()) {
      log.error("Attribute set passed is empty.");
      throw new ConnectorException(config.getMessage("ex.emptyattrset", "Attribute set passed is empty."));
    }

    if (this.resourceEndpoints.containsKey(objectClass.getObjectClassValue())) {
      final Map<String, Object> payload    = new HashMap<>();
      final Set<Attribute>      addAttrSet = new HashSet<>();
      replace = GenericSCIMUtil.handleAttrValues(replace, this.namedAttributes.get(objectClass.getObjectClassValue()), this.dateAttributes.get(objectClass.getObjectClassValue()), this.config);
      replace = GenericSCIMUtil.convertSpclParentAttrToEmbdObj(replace, objectClass.getObjectClassValue());
      replace = GenericSCIMUtil.handleSpecialAttribute(this.customPayloadMap, this.relURLsMap, this.httpOperationTypesMap, new StringBuilder().append(this.baseURI).append((String)this.resourceEndpoints.get(objectClass.getObjectClassValue())).toString(), replace, uid, objectClass, GenericSCIMConstants.ConnectorOpTags.UPDATEOP.toString(), this.parser, this.connection.getConnection(), this.parserConfigParamsMap, this.baseURI, this.namedAttributes.get(objectClass.getObjectClassValue()));

      if (this.config.getScimVersion() <= 3) {
        GenericSCIMUtil.formPayloadMapBelowV3(objectClass, replace, this.namedAttributes.get(objectClass.getObjectClassValue()), payload, addAttrSet, this.schema, this.config, this.coreSchemas.get(objectClass.getObjectClassValue()), this.extAttrsAndSchemas.get(objectClass.getObjectClassValue()), this.attrToOClassMapping);
      }
      else {
        GenericSCIMUtil.formPayloadMapAboveV3(objectClass, replace, addAttrSet, payload, this.namedAttributes.get(objectClass.getObjectClassValue()), this.extAttrsAndSchemas.get(objectClass.getObjectClassValue()));
      }
      Object patch = payload.get("Operations");
      if (patch != null) {
        // tag Operations point to a list
        // due to single operations per attribute we have only ones a time hence
        // we can go directly to the map at index zero
        final Map<Object, Object> op = (Map<Object, Object>)((List)patch).get(0);
        // check the path for phone number
        // note this becomes a multi-valued attribute for SCIM even though
        if ("phone".equals(op.get(GenericSCIMConstants.PayLoadTags.PATH.toString()))) {
          // change the path appropriate.
          op.put(GenericSCIMConstants.PayLoadTags.PATH.toString(), "phoneNumbers");
          // Add phone. note this is a multi-valued attribute
          // for SCIM even though user in EFBS will only have one.
          // Note value is taken off the parent process form which
          // is mapped to the user profile.
          Object                    field = op.remove(GenericSCIMConstants.PayLoadTags.VALUE.toString());
          List<Map<String, Object>> array = new ArrayList<Map<String, Object>>();
          Map<String, Object>       value = new HashMap<>();
          value.put(GenericSCIMConstants.PayLoadTags.VALUE.toString(), field == null ? "" : field);
          array.add(value);
          op.put(GenericSCIMConstants.PayLoadTags.VALUE.toString(), array);
        }
        // check the path for email address
        // note this becomes a multi-valued attribute for SCIM even though
        if ("email".equals(op.get(GenericSCIMConstants.PayLoadTags.PATH.toString()))) {
          // change the path appropriate.
          // note this is a multi-valued attribute for SCIM even though
          op.put(GenericSCIMConstants.PayLoadTags.PATH.toString(),  "emails");
          // Add email. note this is a multi-valued attribute
          // for SCIM even though user in EFBS will only have one.
          // Note value is taken off the parent process form which
          // is mapped to the user profile.
          Object                    field = op.remove(GenericSCIMConstants.PayLoadTags.VALUE.toString());
          List<Map<String, Object>> array = new ArrayList<Map<String, Object>>();
          Map<String, Object>       value = new HashMap<>();
          value.put(GenericSCIMConstants.PayLoadTags.VALUE.toString(), field == null ? "" : field);
          array.add(value);
          op.put(GenericSCIMConstants.PayLoadTags.VALUE.toString(), array);
        }
      }
      if (((this.config.getScimVersion() <= 3) && (payload.keySet().size() > 1)) || ((this.config.getScimVersion() > 3) && (((List)payload.get("Operations")).size() > 0))) {
        log.ok("Payload target -> {0}", RESTClientHandler.handlePasswordInLogs(this.parser, payload, this.namedAttributes.get(objectClass.getObjectClassValue()).get(OperationalAttributes.PASSWORD_NAME), this.parserConfigParamsMap));
        RESTClientHandler.executeRequest(
          this.connection.getConnection()
        , new StringBuilder().append(this.baseURI).append((String)this.resourceEndpoints.get(objectClass.getObjectClassValue())).append("/").append(uid.getUidValue()).toString()
        , RESTCommonConstants.HTTPOperationType.PATCH
        , this.parser.parseRequest(payload, this.parserConfigParamsMap)
        );
      }

      if (addAttrSet.size() != 0) {
        addAttributeValues(objectClass, uid, addAttrSet, option);
      }
      return uid;
    }
    log.error("Update Operation is not supported for object class {0}. Supported object classes are {1}.", new Object[] { objectClass.getObjectClassValue(), resourceEndpoints.keySet() });
    throw new UnsupportedOperationException(config.getMessage("ex.unsupportedoperation", new StringBuilder().append("Update Operation is not supported for object class ").append(objectClass.getObjectClassValue()).append(". Supported object classes are ").append(resourceEndpoints.keySet()).toString(), new Object[] { "Update", objectClass.getObjectClassValue(), resourceEndpoints.keySet() }));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   executeQuery
  /**
   ** Implementation of the executeQuery method.
   ** <br>
   ** The method evaluates if generic methods can be applied to the query. If
   ** not the methods implemented for core schema processing are applied. This
   ** method is used to execute any query define via the Filter "query"
   ** parameter.
   **
   ** @throws IllegalArgumentException if the provided object class is not
   **                                  supported.
   ** @throws ConnectorException       if the handler attribute is null.
   */
  @Override
  public void executeQuery(final ObjectClass oclass, final String query, final ResultsHandler handler, final OperationOptions options) {
    log.info("Starting EFBS Reconciliation", new Object[0]);
    String[] attrToGet = options.getAttributesToGet();
    if (this.resourceEndpoints.containsKey(oclass.getObjectClassValue())) {
      if (this.namedAttributes.get(oclass.getObjectClassValue()) == null) {
        log.error("Missing special attributes for object class {0}. Please provide the values appropriately.", new Object[] { oclass.getObjectClassValue() });
        throw new ConfigurationException(config.getMessage("ex.missingSpecialAttribute", new StringBuilder().append("Missing special attributes for object class ").append(oclass.getObjectClassValue()).append("Please provide the values appropriately.").toString(), new Object[] { oclass.getObjectClassValue() }));
      }

      String url = new StringBuilder().append(this.baseURI).append((String)this.resourceEndpoints.get(oclass.getObjectClassValue())).append(GenericSCIMUtil.formAttributeQuery(GenericSCIMUtil.handleOperationalAttributes(attrToGet, this.namedAttributes.get(oclass.getObjectClassValue()), this.config, this.schema, this.extAttrsAndSchemas.get(oclass.getObjectClassValue()), oclass))).toString();
      int batch_size = this.config.getDefaultBatchSize();
      if ((options.getOptions().containsKey("Batch Size")) && (!(options.getOptions().get("Batch Size")).equals("")) && (Integer.parseInt((String) options.getOptions().get("Batch Size")) > 0)) {
        batch_size = Integer.parseInt((String) options.getOptions().get("Batch Size"));
      }

      String oimOrgName = null;
      if (options.getOptions().containsKey("OIM Organization Name")) {
        oimOrgName = (String) options.getOptions().get("OIM Organization Name");
      }

      int index = 1;
      try {
        if (query != null) {
          url = new StringBuilder().append(url).append("&filter=").append(URLEncoder.encode(query, "UTF-8")).toString();
        }
      }
      catch (UnsupportedEncodingException e) {
        log.error("Failed while URL Encoding. {0}", new Object[] { e });
        throw new UnsupportedOperationException(new StringBuilder().append(config.getMessage("ex.unsupportedEncoding", "Failed while URL Encoding.", new Object[0])).append(" ").append(e.getMessage()).toString(), e);
      }

      String reconSortByAttr = this.reconSortByAttrs.get(oclass.getObjectClassValue());
      if (reconSortByAttr != null) {
        reconSortByAttr = GenericSCIMUtil.handleOperationalAttributes(reconSortByAttr, this.namedAttributes.get(oclass.getObjectClassValue()));
        reconSortByAttr = new StringBuilder().append("&sortBy=").append(reconSortByAttr).toString();
      }
      else {
        reconSortByAttr = "";
      }

      List<Map<String, Object>> respList = null;
      final Map<String, Map<String, List<Map<String, Object>>>> childToParentMap = GenericSCIMUtil.getChildToParentMap(oclass.getObjectClassValue(), attrToGet);
      do {
        final String url_part = new StringBuilder().append("&startIndex=").append(index).append("&count=").append(batch_size).append(reconSortByAttr).toString();
        respList = this.parser.parseResponse(RESTClientHandler.executeRequest(connection.getConnection(), new StringBuilder().append(url).append(url_part).toString(), RESTCommonConstants.HTTPOperationType.GET, null), this.parserConfigParamsMap);
        if (respList.size() == 0) {
          log.info(config.getMessage("ex.noRecToFetch", "No Records available to be fetched from Target."));
          break;
        }
        for (Map<String, Object> resultObj : respList) {
          log.ok("ConnectorResponse::{0}", new Object[] {formatCollection(resultObj)});
          try {
            final ConnectorObjectBuilder builder = GenericSCIMUtil.makeConnectorObjectBuilder(oclass, resultObj, config, this.namedAttributes.get(oclass.getObjectClassValue()), attrToGet, this.extAttrsAndSchemas.get(oclass.getObjectClassValue()), childToParentMap, this.dateAttributes.get(oclass.getObjectClassValue()));
            if (oimOrgName != null) {
              builder.addAttribute(new Attribute[] { AttributeBuilder.build("OIM Organization Name", new Object[] { oimOrgName }) });
            }
            builder.setObjectClass(oclass);
            final ConnectorObject object = builder.build();
            log.ok("ConnectorObject::{0}", object.toString());
            if (!handler.handle(object))
              break;
          }
          catch (Exception e) {
            log.error("Failed while processing the following details : {0} \n Error : {1}. Moving on to the next information.", new Object[] { resultObj.toString(), e.toString() });
          }
        }
        index += batch_size;
      }
      while (respList.size() == batch_size);
      log.info("Reconcilation completed");
    }
    else {
      log.error("Search Operation is not supported for object class {0}. Supported object classes are {1}", new Object[] { oclass.getObjectClassValue(), resourceEndpoints.keySet() });
      throw new UnsupportedOperationException(config.getMessage("ex.unsupportedoperation", new StringBuilder().append("Search Operation is not supported for object class ").append(oclass.getObjectClassValue()).append(". Supported object classes are ").append(resourceEndpoints.keySet()).toString(), new Object[] { "Search", oclass.getObjectClassValue(), resourceEndpoints.keySet() }));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatCollection
  /**
   ** Formats a {@link Map} as an output for debugging purpose.
   **
   ** @param  mapping            the {@link Map} to format for debugging output.
   **
   ** @return                    the formatted string representation
   */
  public static String formatCollection(final Map<String, Object> mapping) {
    final StringBuilder buffer = new StringBuilder();
    for (String name : mapping.keySet()) {
      final Object value = mapping.get(name);
      formatValuePair(buffer, name, (value == null) ? "<null>" : value.toString());
      buffer.append('\n');
    }
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatValuePair
  /**
   ** Formats a tagged valued pair as an output for debugging purpose.
   **
   ** @param  buffer             the {@link StringBuilder} to receive.
   ** @param  key                the key of the tagged value pair.
   ** @param  value              the value of the tagged value pair.
   */
  public static void formatValuePair(final StringBuilder buffer, final String key, final String value) {
    final Locale    locale    = Locale.getDefault();
    final String    format    = String.format(locale, "%%-%ds = %%s", 35);
    final Formatter formatter = new Formatter(buffer, locale);
    if (key.toUpperCase().contains("PASSWORD"))
      formatter.format(format, key, "********");
    else
      formatter.format(format, key, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initConfigMaps
  /**
   ** Sets up local copies of meta-data for use in overriden methods.
   */
  private void initConfigMaps() {
    this.parserConfigParamsMap = GenericSCIMUtil.formParserConfigParamsMap(this.config);
    if(this.config.getScimVersion() > 1) {
      this.resourceEndpoints = GenericSCIMUtil.getResourceEndpoints(this.config, this.connection, this.baseURI, this.parser, this.parserConfigParamsMap);
    }
    this.namedAttributes = GenericSCIMUtil.getNamedAttributeMap(config);
    this.attrToOClassMapping = GenericSCIMUtil.getAttrToOClassMapping(config);
    log.ok("initConfigMaps(), resourceEndpoints=" + this.resourceEndpoints+", namedAttributes=" + this.namedAttributes+", attrToOClassMapping=" + this.attrToOClassMapping);
    this.customPayloadMap = GenericSCIMUtil.convertConfigArrayToMap("customPayload", this.config.getCustomPayload(), config);
    log.ok("initConfigMaps(), resourceEndpoints=" + this.resourceEndpoints+", namedAttributes=" + this.namedAttributes+", attrToOClassMapping=" + this.attrToOClassMapping+", customPayloadMap=" + this.customPayloadMap);
    this.relURLsMap = GenericSCIMUtil.convertConfigArrayToMap("relURLs", this.config.getRelURLs(), config);
    this.httpOperationTypesMap = GenericSCIMUtil.convertConfigArrayToMap("httpOperationTypes", this.config.getHttpOperationTypes(), this.config);
    this.reconSortByAttrs = GenericSCIMUtil.convertConfigArrayToMap("reconSortByAttrs", this.config.getReconSortByAttrs(), this.config);
    GenericSCIMUtil.formDateAttrMap(this.dateAttributes, this.config);
  }
}