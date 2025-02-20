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

    File        :   EFBSSCIMSchema.java

    Compiler    :   JDK 1.8

    Author      :   richard.x.rutter@oracle.com

    Purpose     :   Provides a custom implementation of the
                    <code>GenericSCIMSchema</code> class from the GenericSCIM
                    Connector for use by the EFBS SCIM connector.
                    This is required because of a simple problem with the
                    standard implementation whereby it can't handle
                    extension schema 'EFBSUsers' properly because the schema
                    name does not follow a hard-coded pattern.
                    See <code>buildSchema</code> method.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2019  rirutter    First release version
*/

package bka.identityconnectors.genericscim;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;

import org.identityconnectors.common.logging.Log;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.SchemaBuilder;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.AttributeInfoBuilder;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder;

import org.identityconnectors.framework.spi.operations.SearchOp;
import org.identityconnectors.framework.spi.operations.SPIOperation;

import org.identityconnectors.restcommon.parser.spi.ParserPlugin;

import org.identityconnectors.genericscim.GenericSCIMSchema;
import org.identityconnectors.genericscim.GenericSCIMConnection;
import org.identityconnectors.genericscim.GenericSCIMConfiguration;

import org.identityconnectors.genericscim.utils.GenericSCIMConstants;

////////////////////////////////////////////////////////////////////////////////
// class EFBSSCIMSchema
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>EFBSSCIMSChema</code> replaces the <code>GenericSCIMSchema</code>
 ** implementation to support the EFBSUsers schema extension. The standard class
 ** includes use of a simple naming pattern which results in the EFBSUsers
 ** schema from being incorrectly classified which in turn results in failure to
 ** create and update attributes from this schema correctly.
 **
 ** @author  richard.x.rutter@oracle.com
 ** @version 1.0.0.0
 */
public class EFBSSCIMSchema {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Log log = Log.getLog(GenericSCIMSchema.class);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EFBSSCIMSchema</code>.
   ** <br>
   ** Zero argument constructor required by the connector framework.
   ** <br>
   ** Default Constructor
   */
  public EFBSSCIMSchema() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildSchema
  /**
   ** Builds schema meta-data from configuration and by obtaining meta-data from
   ** target environment. Can't override this method because static, so this
   ** requires a new class.
   **
   ** @param connection          to target system
   ** @param baseURI             for obtaining schemas from target system
   ** @param config              configuration meta-data
   ** @param oimResourceAttributes  the attribute mapping got from OIM
   ** @param parser              for parsing schema obtained from target system
   ** @param parserConfigParamsMap parser configuration
   ** @param resourceEndpoints   target system endpoint meta-data
   ** @param coreSchemas         method constructs this parsed from the target system schema
   ** @param extAttrsAndSchemas  method constructs this parsed from the target system schema
   ** @param attrToOClassMapping attributes for object classes, specifically 'User' for EFBS SCIM
   **
   ** @return                    the {@link Schema}
   */
  public static Schema buildSchema(GenericSCIMConnection connection, String baseURI, GenericSCIMConfiguration config, Map<String, Map<String, String>> oimResourceAttributes, ParserPlugin parser, Map<String, String> parserConfigParamsMap, Map<String, String> resourceEndpoints, Map<String, String> coreSchemas, Map<String, Map<String, String>> extAttrsAndSchemas, Map<String, String> attrToOClassMapping) {
    log.ok("METHOD_ENTERED", new Object[0]);
    log.info("Building Schema", new Object[0]);
    SchemaBuilder schemaBuilder = new SchemaBuilder(EFBSSCIMConnector.class);
    List<Map<String, Object>> resources = new ArrayList<>();
    int batch_size = config.getDefaultBatchSize();
    int index = 1;
    do {
      String url_part = (new StringBuilder()).append("&startIndex=".replace('&', '?')).append(index).append("&count=").append(batch_size).toString();
      resources.addAll(parser.parseResponse(GenericSCIMSchema.getschema(connection, (new StringBuilder()).append(baseURI).append("/Schemas").append(url_part).toString(), config), parserConfigParamsMap));
      index += batch_size;
    }
    while (resources.size() % batch_size == 0);
    List<ObjectClassInfoBuilder> ocBldrList = new ArrayList<>();
    Map<String, Set<AttributeInfo>> ocAttrDetails = new HashMap<>();
    for (Iterator<Map<String, Object>> i$ = resources.iterator(); i$.hasNext();) {
      Map<String, Object> resource = i$.next();
      Set<AttributeInfo> attributeInfos = new HashSet<>();
      String schema = (String)resource.get("id");
      schema = schema.substring(schema.lastIndexOf(":") + 1);
      // RR add EFBSUsers schema name to handle Rola EFBS SCIM implementation
      // with EFBSUsers extension schema
      if (schema.endsWith("User"))
        schema = ObjectClass.ACCOUNT_NAME;
      else if (schema.endsWith("Group"))
        schema = ObjectClass.GROUP_NAME;

      if (resource.containsKey("endpoint"))
        resourceEndpoints.put(schema, (String) resource.get("endpoint"));

      List<Map<String, Object>> attrList = (List<Map<String, Object>>)resource.get("attributes");
      if (attrList != null) {
        for (Iterator<Map<String, Object>> cursor = attrList.iterator(); cursor.hasNext();) {
          Map<String, Object> attr = cursor.next();
          String name = (String) attr.get("name");
          if (((Boolean) attr.get("multiValued")).booleanValue() && ((attr.get("type")).equals("complex") || attr.containsKey("subAttributes"))) {
            name = (new StringBuilder()).append(schema).append(".").append(name).toString();
            formSchemaMaps((String)resource.get("id"), schema, name, coreSchemas, extAttrsAndSchemas);
            handleEmbObjSchema(attr, attributeInfos, schemaBuilder, name, attrToOClassMapping);
          }
          else if ((attr.get("type")).equals("complex")) {
            handleComplexAttrSchema(attr, attributeInfos, name, (String) resource.get("id"), schema, coreSchemas, extAttrsAndSchemas);
          }
          else {
            Map<String, String> namedVals = null;
            if (oimResourceAttributes.containsKey(schema))
              namedVals = oimResourceAttributes.get(schema);
            formSchemaMaps((String)resource.get("id"), schema, name, coreSchemas, extAttrsAndSchemas);
            handleSimpleAttrSchema(attr, attributeInfos, name, namedVals);
          }
        }
      }
      if (ocAttrDetails.containsKey(schema)) {
        Set<AttributeInfo> attrInf = ocAttrDetails.get(schema);
        attrInf.addAll(attributeInfos);
      }
      else {
        ocAttrDetails.put(schema, attributeInfos);
      }
    }

    for (Map.Entry<String, Set<AttributeInfo>> cursor : ocAttrDetails.entrySet() ) {
      final ObjectClassInfoBuilder builder = new ObjectClassInfoBuilder();
      builder.setType(cursor.getKey());
      builder.addAllAttributeInfo(cursor.getValue());
      schemaBuilder.defineObjectClass(builder.build());
      ocBldrList.add(builder);
    }

    schemaBuilder.clearSupportedObjectClassesByOperation();
    Class<? extends SPIOperation>[] arr = GenericSCIMConstants.supportedOps;
    int len = arr.length;
    for (int i = 0; i < len; i++) {
      final Class<? extends SPIOperation> op = arr[i];
      for (ObjectClassInfoBuilder cursor : ocBldrList)
        schemaBuilder.addSupportedObjectClass(op, cursor.build());
    }

    final List<ObjectClassInfoBuilder> ocbList = addLookupObjectClasses(config, schemaBuilder);
    for (ObjectClassInfoBuilder cursor : ocbList)
      schemaBuilder.addSupportedObjectClass(SearchOp.class, cursor.build());

    log.ok("CoreSchemas::{0}", coreSchemas.toString());
    log.ok("ExtendedSchemas::{0}", extAttrsAndSchemas);
    log.info("Schema building complete", new Object[0]);
    log.ok("METHOD_EXITING", new Object[0]);
    return schemaBuilder.build();
  }

  /*
   * Copied from <code>GenericSCIMSchema</code>
   */
  private static void formSchemaMaps(String schemaName, String ocName, String fieldName, Map<String, String> coreSchemas, Map<String, Map<String, String>> extAttrsAndSchemas) {
    if (schemaName.contains("core")) {
      if (!coreSchemas.containsKey(ocName))
        coreSchemas.put(ocName, schemaName);
    }
    else {
      if (extAttrsAndSchemas.containsKey(ocName)) {
        extAttrsAndSchemas.get(ocName).put(fieldName, schemaName);
      }
      else {
        final Map<String, String> attrSchemaMap = new HashMap<>();
        attrSchemaMap.put(fieldName, schemaName);
        extAttrsAndSchemas.put(ocName, attrSchemaMap);
      }
    }
  }

  /*
   * Copied from <code>GenericSCIMSchema</code>
   */
  private static List<ObjectClassInfoBuilder> addLookupObjectClasses(final GenericSCIMConfiguration config, final SchemaBuilder schemaBuilder) {
    log.ok("METHOD_ENTERED", new Object[0]);
    List<ObjectClassInfoBuilder> ocBldrList = new ArrayList<>();
    if (config.getLookupObjectClasses() != null) {
      String arr$[] = config.getLookupObjectClasses();
      int len$ = arr$.length;
      for (int i = 0; i < len$; i++) {
        final String                 name    = arr$[i];
        final ObjectClassInfoBuilder builder = new ObjectClassInfoBuilder();
        builder.setType(name);
        schemaBuilder.defineObjectClass(builder.build());
        ocBldrList.add(builder);
      }
    }
    log.ok("METHOD_EXITING", new Object[0]);
    return ocBldrList;
  }
  /*
   * Copied from <code>GenericSCIMSchema</code>
   */
  private static void handleEmbObjSchema(Map<String, Object> attr, Set<AttributeInfo> attributeInfos, final SchemaBuilder schema, final String name, Map<String, String> attrToOClassMapping) {
    log.ok("METHOD_ENTERED", new Object[0]);
    List<Map<String, Object>> subAttrList = (List<Map<String, Object>>)attr.get("subAttributes");
    final ObjectClassInfoBuilder builder = new ObjectClassInfoBuilder();
    builder.setEmbedded(true);
    builder.setType(name);
    builder.addAllAttributeInfo(getEmbeddedObjAttributeInfo(subAttrList, name, attrToOClassMapping));
    final ObjectClassInfo classInfo = builder.build();
    attributeInfos.add(addEmbObj(name));
    schema.defineObjectClass(classInfo);
    log.ok("METHOD_EXITING", new Object[0]);
  }
  /*
   * Copied from <code>GenericSCIMSchema</code>
   */
  private static Set<AttributeInfo> getEmbeddedObjAttributeInfo(List<Map<String, Object>> subAttrList, String ocName, Map<String, String> attrToOClassMapping) {
    log.ok("METHOD_ENTERED", new Object[0]);
    final Set<AttributeInfo> complex = new HashSet<>();
    if (attrToOClassMapping.containsKey(ocName)) {
      AttributeInfoBuilder infoBldr = new AttributeInfoBuilder();
      infoBldr.setName(GenericSCIMConstants.PayLoadTags.VALUE.toString());
      infoBldr.setRequired(true);
      infoBldr.setType(java.lang.String.class);
      complex.add(infoBldr.build());
    }
    else {
      AttributeInfoBuilder infoBldr;
      for (Iterator i$ = subAttrList.iterator(); i$.hasNext(); complex.add(infoBldr.build())) {
        Map subAttr = (Map) i$.next();
        infoBldr = new AttributeInfoBuilder();
        infoBldr.setName((String) subAttr.get("name"));
        if (subAttr.containsKey("required"))
          infoBldr.setRequired(((Boolean) subAttr.get("required")).booleanValue());
        else
          infoBldr.setRequired(false);
        infoBldr.setType(getAttributeType((String) subAttr.get("type")));
      }
    }
    log.ok("METHOD_EXITING", new Object[0]);
    return complex;
  }
  /*
   * Copied from <code>GenericSCIMSchema</code>
   */
  private static void handleComplexAttrSchema(Map attr, Set<AttributeInfo> attributeInfos, String name, String schemaName, String ocName, Map<String, String> coreSchemas, Map<String, Map<String, String>> extAttrsAndSchemas) {
    log.ok("METHOD_ENTERED", new Object[0]);
    List subAttrList = (List) attr.get("subAttributes");
    AttributeInfoBuilder attrInfoBldr;
    for (Iterator i$ = subAttrList.iterator(); i$.hasNext(); attributeInfos.add(attrInfoBldr.build())) {
      Map subAttr = (Map) i$.next();
      attrInfoBldr = new AttributeInfoBuilder();
      String subName = (new StringBuilder()).append(name).append(".").append((String) subAttr.get("name")).toString();
      formSchemaMaps(schemaName, ocName, subName, coreSchemas, extAttrsAndSchemas);
      attrInfoBldr.setName(subName);
      attrInfoBldr.setRequired(((Boolean) subAttr.get("required")).booleanValue());
      attrInfoBldr.setType(getAttributeType((String) subAttr.get("type")));
    }
    log.ok("METHOD_EXITING", new Object[0]);
  }
  /*
   * Copied from <code>GenericSCIMSchema</code>
   */
  private static void handleSimpleAttrSchema(Map<String, Object> attr, Set<AttributeInfo> attributeInfos, String name, Map<String, String> namedVals) {
    log.ok("METHOD_ENTERED", new Object[0]);
    AttributeInfoBuilder attrInfoBldr = new AttributeInfoBuilder();
    boolean isNotNamedVar = true;
    String attrName = null;
    if (namedVals != null) {
      if (namedVals.containsKey(Uid.NAME) && (namedVals.get(Uid.NAME)).equals(name))
        return;
      if (namedVals.containsKey(Name.NAME) && (namedVals.get(Name.NAME)).equals(name)) {
        attrName = Name.NAME;
        attrInfoBldr.setRequired(true);
        isNotNamedVar = false;
      }
      if (namedVals.containsKey(OperationalAttributes.ENABLE_NAME) && (namedVals.get(OperationalAttributes.ENABLE_NAME)).equals(name)) {
        attrName = OperationalAttributes.ENABLE_NAME;
        attrInfoBldr.setRequired(false);
        isNotNamedVar = false;
      }
    }
    if (isNotNamedVar) {
      attrName = name;
      if (attr.containsKey("required"))
        attrInfoBldr.setRequired(((Boolean) attr.get("required")).booleanValue());
    }
    attrInfoBldr.setName(attrName);
    attrInfoBldr.setMultiValued(((Boolean) attr.get("multiValued")).booleanValue());
    attrInfoBldr.setType(getAttributeType((String) attr.get("type")));
    attributeInfos.add(attrInfoBldr.build());
    log.ok("METHOD_EXITING", new Object[0]);
  }
  /*
   * Copied from <code>GenericSCIMSchema</code>
   */
  private static AttributeInfo addEmbObj(String complexAttrName) {
    log.ok("METHOD_ENTERED", new Object[0]);
    AttributeInfoBuilder attrInfoBld = new AttributeInfoBuilder();
    attrInfoBld.setName(complexAttrName);
    attrInfoBld.setMultiValued(true);
    attrInfoBld.setObjectClassName(complexAttrName);
    attrInfoBld.setType(EmbeddedObject.class);
    log.ok("METHOD_EXITING", new Object[0]);
    return attrInfoBld.build();
  }
  /*
   * Copied from <code>GenericSCIMSchema</code>
   */
  private static Class getAttributeType(String type) {
    if (type.equalsIgnoreCase("boolean"))
      return java.lang.Boolean.class;
    else if (type.equalsIgnoreCase("decimal") || type.equalsIgnoreCase("integer"))
      return java.lang.Integer.class;
    else if (type.equalsIgnoreCase("dateTime"))
      return java.lang.Long.class;
    else
      return java.lang.String.class;
  }
}