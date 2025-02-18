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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   ObjectFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ObjectFactory.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/
package oracle.iam.access.common.spi.schema;

import java.util.List;

import javax.xml.bind.annotation.XmlRegistry;

////////////////////////////////////////////////////////////////////////////////
// class ObjectFactory
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This object contains factory methods for each Java content interface and
 ** Java element interface generated in the oracle.iam.access.common.spi.schema
 ** package.
 ** <p>
 ** An ObjectFactory allows you to programatically construct new instances of
 ** the Java representation for XML content. The Java representation of XML
 ** content can consist of schema derived interfaces and classes representing
 ** the binding of schema type definitions, element declarations and model
 ** groups. Factory methods for each of these are provided in this class.
 ** <b>Note</b>:
 ** The underlying XSD does not define a namespace hence there isn't any
 ** package-info in this package.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
@XmlRegistry
public class ObjectFactory {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ObjectFactory</code> that can be used to create new
   ** instances of schema derived classes for package:
   ** <code>oracle.iam.access.common.spi.schema</code>
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public ObjectFactory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServer
  /**
   ** Create an instance of {@link Server}.
   **
   ** @return                    an instance of {@link Server}.
   **                            <br>
   **                            Possible object is {@link Server}.
   */
  public Server createServer() {
    return new Server();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createHostPortVariations
  /**
   ** Create an instance of {@link HostPortVariations}.
   **
   ** @return                    an instance of {@link HostPortVariations}.
   **                            <br>
   **                            Possible object is {@link HostPortVariations}.
   */
  public HostPortVariations createHostPortVariations() {
    return new HostPortVariations();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createHostPortVariationsList
  /**
   ** Create an instance of {@link HostPortVariationsList}.
   **
   ** @return                    an instance of {@link HostPortVariationsList}.
   **                            <br>
   **                            Possible object is
   **                            {@link HostPortVariationsList}.
   */
  public HostPortVariationsList createHostPortVariationsList() {
    return new HostPortVariationsList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPrimaryServerList
  /**
   ** Create an instance of {@link PrimaryServerList}.
   **
   ** @return                    an instance of {@link PrimaryServerList}.
   **                            <br>
   **                            Possible object is {@link PrimaryServerList}.
   */
  public PrimaryServerList createPrimaryServerList() {
    return new PrimaryServerList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createUriList
  /**
   ** Create an instance of {@link UriList}.
   **
   ** @return                    an instance of {@link UriList}.
   **                            <br>
   **                            Possible object is {@link UriList}.
   */
  public UriList createUriList() {
    return new UriList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createUriList
  /**
   ** Create an instance of {@link UriList} with the specified
   ** properties.
   **
   ** @param  uriResource        the intial collection of URI's.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link UriResource}s.
   **
   ** @return                    an instance of {@link UriList}.
   **                            <br>
   **                            Possible object is {@link UriList}.
   */
  public UriList createUriList(final List<UriResource> uriResource) {
    return new UriList(uriResource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDeletedUriList
  /**
   ** Create an instance of {@link DeletedUriList}.
   **
   ** @return                    an instance of {@link DeletedUriList}.
   **                            <br>
   **                            Possible object is {@link DeletedUriList}.
   */
  public DeletedUriList createDeletedUriList() {
    return new DeletedUriList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSecondaryServerList
  /**
   ** Create an instance of {@link SecondaryServerList}.
   **
   ** @return                    an instance of {@link SecondaryServerList}.
   **                            <br>
   **                            Possible object is {@link SecondaryServerList}.
   */
  public SecondaryServerList createSecondaryServerList() {
    return new SecondaryServerList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLogInUrls
  /**
   ** Create an instance of {@link LogInUrls}.
   **
   ** @return                    an instance of {@link LogInUrls}.
   **                            <br>
   **                            Possible object is {@link LogInUrls}.
   */
  public LogInUrls createLogInUrls() {
    return new LogInUrls();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLogOutUrls
  /**
   ** Create an instance of {@link LogOutUrls}.
   **
   ** @return                    an instance of {@link LogOutUrls}.
   **                            <br>
   **                            Possible object is {@link LogOutUrls}.
   */
  public LogOutUrls createLogOutUrls() {
    return new LogOutUrls();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNotEnforcedUrls
  /**
   ** Create an instance of {@link NotEnforcedUrls}.
   **
   ** @return                    an instance of {@link NotEnforcedUrls}.
   **                            <br>
   **                            Possible object is {@link NotEnforcedUrls}.
   */
  public NotEnforcedUrls createNotEnforcedUrls() {
    return new NotEnforcedUrls();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPublicResourcesList
  /**
   ** Create an instance of {@link PublicResourcesList}.
   **
   ** @return                    an instance of {@link PublicResourcesList}.
   **                            <br>
   **                            Possible object is {@link PublicResourcesList}.
   */
  public PublicResourcesList createPublicResourcesList() {
    return new PublicResourcesList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createExcludedResourcesList
  /**
   ** Create an instance of {@link ExcludedResourcesList}.
   **
   ** @return                    an instance of {@link ExcludedResourcesList}.
   **                            <br>
   **                            Possible object is {@link ExcludedResourcesList}.
   */
  public ExcludedResourcesList createExcludedResourcesList() {
    return new ExcludedResourcesList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProtectedResourcesList
  /**
   ** Create an instance of {@link ProtectedResourcesList}.
   **
   ** @return                    an instance of {@link ProtectedResourcesList}.
   **                            <br>
   **                            Possible object is {@link ProtectedResourcesList}.
   */
  public ProtectedResourcesList createProtectedResourcesList() {
    return new ProtectedResourcesList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createUserDefinedParam
  /**
   ** Create an instance of {@link UserDefinedParam}.
   **
   ** @return                    an instance of {@link UserDefinedParam}.
   **                            <br>
   **                            Possible object is {@link UserDefinedParam}.
   */
  public UserDefinedParam createUserDefinedParam() {
    return new UserDefinedParam();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createUserDefinedParameters
  /**
   ** Create an instance of {@link UserDefinedParameters}.
   **
   ** @return                    an instance of {@link UserDefinedParameters}.
   **                            <br>
   **                            Possible object is {@link UserDefinedParameters}.
   */
  public UserDefinedParameters createUserDefinedParameters() {
    return new UserDefinedParameters();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createUriResource
  /**
   ** Create an instance of {@link UriResource}.
   **
   ** @return                    an instance of {@link UriResource}.
   **                            <br>
   **                            Possible object is {@link UriResource}.
   */
  public UriResource createUriResource() {
    return new UriResource();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createUriResource
  /**
   ** Create an instance of {@link UriResource} with the specified
   ** properties.
   **
   ** @param  uri                the uri od the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  description        the description od the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  queryString        the query string od the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link UriResource}.
   **                            <br>
   **                            Possible object is {@link UriResource}.
   */
  public UriResource createUriResource(final String uri, final String description, final String queryString) {
    return new UriResource(uri, description, queryString);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRregAuthenticationPolicy
  /**
   ** Create an instance of {@link RregAuthenticationPolicy}.
   **
   ** @return                    an instance of
   **                            {@link RregAuthenticationPolicy}.
   */
  public RregAuthenticationPolicy createRregAuthenticationPolicy() {
    return new RregAuthenticationPolicy();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRregAuthenticationPolicies
  /**
   ** Create an instance of {@link RregAuthenticationPolicies}.
   **
   ** @return                    an instance of
   **                            {@link RregAuthenticationPolicies}.
   */
  public RregAuthenticationPolicies createRregAuthenticationPolicies() {
    return new RregAuthenticationPolicies();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRregAuthorizationPolicy
  /**
   ** Create an instance of {@link RregAuthorizationPolicy}.
   **
   ** @return                    an instance of
   **                            {@link RregAuthorizationPolicy}.
   */
  public RregAuthorizationPolicy createRregAuthorizationPolicy() {
    return new RregAuthorizationPolicy();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRregAuthorizationPolicies
  /**
   ** Create an instance of {@link RregAuthorizationPolicies}.
   **
   ** @return                    an instance of
   **                            {@link RregAuthorizationPolicies}.
   */
  public RregAuthorizationPolicies createRregAuthorizationPolicies() {
    return new RregAuthorizationPolicies();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRregApplicationDomain
  /**
   ** Create an instance of {@link RregApplicationDomain}.
   **
   ** @return                    an instance of
   **                            {@link RregAuthorizationPolicies}.
   */
  public RregApplicationDomain createRregApplicationDomain() {
    return new RregApplicationDomain();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIpValidationExceptions
  /**
   ** Create an instance of {@link IpValidationExceptions}.
   **
   ** @return                    an instance of {@link IpValidationExceptions}.
   */
  public IpValidationExceptions createIpValidationExceptions() {
    return new IpValidationExceptions();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRule
  /**
   ** Create an instance of {@link Rule}.
   **
   ** @return                    an instance of {@link Rule}.
   */
  public Rule createRule() {
    return new Rule();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRules
  /**
   ** Create an instance of {@link Rules}.
   **
   ** @return                    an instance of {@link Rules}.
   */
  public Rules createRules() {
    return new Rules();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIdentity
  /**
   ** Create an instance of {@link Identity}.
   **
   ** @return                    an instance of {@link Identity}.
   */
  public Identity createIdentity() {
    return new Identity();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIp4Range
  /**
   ** Create an instance of {@link Ip4Range}.
   **
   ** @return                    an instance of {@link Ip4Range}.
   */
  public Ip4Range createIp4Range() {
    return new Ip4Range();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIp4RangeList
  /**
   ** Create an instance of {@link Ip4RangeList}.
   **
   ** @return                    an instance of {@link Ip4RangeList}.
   */
  public Ip4RangeList createIp4RangeList() {
    return new Ip4RangeList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDayOfWeek
  /**
   ** Create an instance of {@link DayOfWeek}.
   **
   ** @return                    an instance of {@link DayOfWeek}.
   */
  public DayOfWeek createDayOfWeek() {
    return new DayOfWeek();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTemporal
  /**
   ** Create an instance of {@link Temporal}.
   **
   ** @return                    an instance of {@link Temporal}.
   */
  public Temporal createTemporal() {
    return new Temporal();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAttributeMapping
  /**
   ** Create an instance of {@link AttributeMapping}.
   **
   ** @return                    an instance of {@link AttributeMapping}.
   */
  public AttributeMapping createAttributeMapping() {
    return new AttributeMapping();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProfileAttributeMapping
  /**
   ** Create an instance of {@link ProfileAttributeMapping}.
   **
   ** @return                    an instance of {@link ProfileAttributeMapping}.
   */
  public ProfileAttributeMapping createProfileAttributeMapping() {
    return new ProfileAttributeMapping();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSessionAttributeMapping
  /**
   ** Create an instance of {@link SessionAttributeMapping}.
   **
   ** @return                    an instance of {@link SessionAttributeMapping}.
   */
  public SessionAttributeMapping createSessionAttributeMapping() {
    return new SessionAttributeMapping();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createResponseAttributeMapping
  /**
   ** Create an instance of {@link ResponseAttributeMapping}.
   **
   ** @return                    an instance of {@link ResponseAttributeMapping}.
   */
  public ResponseAttributeMapping createResponseAttributeMapping() {
    return new ResponseAttributeMapping();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createListValue
  /**
   ** Create an instance of {@link ListValue}.
   **
   ** @return                    an instance of {@link ListValue}.
   */
  public ListValue createListValue() {
    return new ListValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createListValue
  /**
   ** Create an instance of {@link ListValue} with the specified
   ** properties.
   **
   ** @param  value              the intial collection of values.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link String}s.
   **
   ** @return                    an instance of {@link ListValue}.
   **                            <br>
   **                            Possible object is {@link ListValue}.
   */
  public ListValue createListValue(final List<String> value) {
    return new ListValue(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAttrMappingValue
  /**
   ** Create an instance of {@link AttrMappingValue}.
   **
   ** @return                    an instance of {@link AttrMappingValue}.
   */
  public AttrMappingValue createAttrMappingValue() {
    return new AttrMappingValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAttrMappingValue
  /**
   ** Create an instance of {@link AttrMappingValue} with the specified
   ** properties.
   **
   ** @param  mapping            the intial collection of attribute mappings.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link AttributeMapping}s.
   **
   ** @return                    an instance of {@link AttrMappingValue}.
   **                            <br>
   **                            Possible object is {@link AttrMappingValue}.
   */
  public AttrMappingValue createAttrMappingValue(final List<AttributeMapping> mapping) {
    return new AttrMappingValue(mapping);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProperty
  /**
   ** Create an instance of {@link Property}.
   **
   ** @return                    an instance of {@link Property}.
   */
  public Property createProperty() {
    return new Property();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createMiscellaneousProperty
  /**
   ** Create an instance of {@link MiscellaneousProperty}.
   **
   ** @return                    an instance of {@link MiscellaneousProperty}.
   */
  public MiscellaneousProperty createMiscellaneousProperty() {
    return new MiscellaneousProperty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createMiscellaneousProperties
  /**
   ** Create an instance of {@link MiscellaneousProperties}.
   **
   ** @return                    an instance of {@link MiscellaneousProperties}.
   */
  public MiscellaneousProperties createMiscellaneousProperties() {
    return new MiscellaneousProperties();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAttributeCondition
  /**
   ** Create an instance of {@link AttributeCondition}.
   **
   ** @return                    an instance of {@link AttributeCondition}.
   */
  public AttributeCondition createAttributeCondition() {
    return new AttributeCondition();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createConditionNameList
  /**
   ** Create an instance of {@link ConditionNameList}.
   **
   ** @return                    an instance of {@link ConditionNameList}.
   */
  public ConditionNameList createConditionNameList() {
    return new ConditionNameList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCombinerMode
  /**
   ** Create an instance of {@link CombinerMode}.
   **
   ** @return                    an instance of {@link CombinerMode}.
   */
  public CombinerMode createCombinerMode() {
    return new CombinerMode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createConditionCombiner
  /**
   ** Create an instance of {@link ConditionCombiner}.
   **
   ** @return                    an instance of {@link ConditionCombiner}.
   */
  public ConditionCombiner createConditionCombiner() {
    return new ConditionCombiner();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCondition
  /**
   ** Create an instance of {@link Condition}.
   **
   ** @return                    an instance of {@link Condition}.
   */
  public Condition createCondition() {
    return new Condition();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createConditionsList
  /**
   ** Create an instance of {@link ConditionsList}.
   **
   ** @return                    an instance of {@link ConditionsList}.
   */
  public ConditionsList createConditionsList() {
    return new ConditionsList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSuccessResponse
  /**
   ** Create an instance of {@link SuccessResponse}.
   **
   ** @return                    an instance of {@link SuccessResponse}.
   */
  public SuccessResponse createSuccessResponse() {
    return new SuccessResponse();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSuccessResponseList
  /**
   ** Create an instance of {@link SuccessResponseList}.
   **
   ** @return                    an instance of {@link SuccessResponseList}.
   */
  public SuccessResponseList createSuccessResponseList() {
    return new SuccessResponseList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createBaseRequest
  /**
   ** Create an instance of {@link BaseRequest}.
   **
   ** @return                    an instance of {@link BaseRequest}.
   */
  public BaseRequest createBaseRequest() {
    return new BaseRequest();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createBaseRequest
  /**
   ** Create an instance of {@link BaseRequest} with the specified properties.
   **
   ** @param  mode               the operational mode to apply.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverAddress      the initial serverAddress of the request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  username           the initial username of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the initial password of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  agentName          the initial agentName of the agent.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link BaseRequest}.
   **                            <br>
   **                            Possible object is {@link BaseRequest}.
   */
  public BaseRequest createBaseRequest(final String mode, final String serverAddress, final String username, final String password, final String agentName) {
    return new BaseRequest(mode, serverAddress, username, password, agentName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createBaseRequest
  /**
   ** Create an instance of {@link BaseRequest} with the specified properties.
   **
   ** @param  type               the agent type  to apply at the request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  mode               the operational mode to apply.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverAddress      the initial serverAddress of the request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  username           the initial username of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the initial password of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  agentName          the initial agentName of the agent.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link BaseRequest}.
   **                            <br>
   **                            Possible object is {@link BaseRequest}.
   */
  public BaseRequest createBaseRequest(final String type, final String mode, final String serverAddress, final String username, final String password, final String agentName) {
    return new BaseRequest(type, mode, serverAddress, username, password, agentName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createBaseResponse
  /**
   ** Create an instance of {@link BaseResponse}.
   **
   ** @return                    an instance of {@link BaseResponse}.
   **                            <br>
   **                            Possible object is {@link BaseResponse}.
   */
  public BaseResponse createBaseResponse() {
    return new BaseResponse();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAgent10gCreate
  /**
   ** Create an instance of {@link Agent10gCreate}.
   **
   ** @return                    an instance of {@link Agent10gCreate}.
   **                            <br>
   **                            Possible object is {@link Agent10gCreate}.
   */
  public Agent10gCreate createAgent10gCreate() {
    return new Agent10gCreate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAgent10gCreate
  /**
   ** Create an instance of {@link Agent10gCreate} with the specified
   ** properties.
   **
   ** @param  mode               the operational mode to apply.
   ** @param  serverAddress      the initial serverAddress of the request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  username           the initial username of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the initial password of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  agentName          the initial agentName of the agent.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link Agent10gCreate}.
   **                            <br>
   **                            Possible object is {@link Agent10gCreate}.
   */
  public Agent10gCreate createAgent10gCreate(final String mode, final String serverAddress, final String username, final String password, final String agentName) {
    return new Agent10gCreate(mode, serverAddress, username, password, agentName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAgent10gUpdate
  /**
   ** Update an instance of {@link Agent10gUpdate}.
   **
   ** @return                    an instance of {@link Agent10gUpdate}.
   **                            <br>
   **                            Possible object is {@link Agent10gUpdate}.
   */
  public Agent10gUpdate createAgent10gUpdate() {
    return new Agent10gUpdate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAgent10gUpdate
  /**
   ** Update an instance of {@link Agent10gUpdate} with the specified
   ** properties.
   **
   ** @param  mode               the operational mode to apply.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverAddress      the initial serverAddress of the request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  username           the initial username of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the initial password of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  agentName          the initial agentName of the agent.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link Agent10gUpdate}.
   **                            <br>
   **                            Possible object is {@link Agent10gUpdate}.
   */
  public Agent10gUpdate createAgent10gUpdate(final String mode, final String serverAddress, final String username, final String password, final String agentName) {
    return new Agent10gUpdate(mode, serverAddress, username, password, agentName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAgent10gResponse
  /**
   ** Create an instance of {@link Agent10gResponse}.
   **
   ** @return                    an instance of {@link Agent10gResponse}.
   */
  public Agent10gResponse createAgent10gResponse() {
    return new Agent10gResponse();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAgent11gCreate
  /**
   ** Create an instance of {@link Agent11gCreate}.
   **
   ** @return                    an instance of {@link Agent11gCreate}.
   */
  public Agent11gCreate createAgent11gCreate() {
    return new Agent11gCreate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAgent11gRequest
  /**
   ** Create an instance of {@link Agent11gCreate} with the specified
   ** properties.
   **
   ** @param  mode               the operational mode to apply.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverAddress      the initial serverAddress of the request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  username           the initial username of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the initial password of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  agentName          the initial agentName of the agent.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link Agent11gCreate}.
   **                            <br>
   **                            Possible object is {@link Agent11gCreate}.
   */
  public Agent11gCreate createAgent11gRequest(final String mode, final String serverAddress, final String username, final String password, final String agentName) {
    return createAgent11gCreate(mode, serverAddress, username, password, agentName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAgent11gCreate
  /**
   ** Create an instance of {@link Agent11gCreate} with the specified
   ** properties.
   **
   ** @param  mode               the operational mode to apply.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverAddress      the initial serverAddress of the request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  username           the initial username of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the initial password of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  agentName          the initial agentName of the agent.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link Agent11gCreate}.
   **                            <br>
   **                            Possible object is {@link Agent11gCreate}.
   */
  public Agent11gCreate createAgent11gCreate(final String mode, final String serverAddress, final String username, final String password, final String agentName) {
    return new Agent11gCreate(mode, serverAddress, username, password, agentName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAgent11gUpdate
  /**
   ** Update an instance of {@link Agent11gUpdate}.
   **
   ** @return                    an instance of {@link Agent11gUpdate}.
   */
  public Agent11gUpdate createAgent11gUpdate() {
    return new Agent11gUpdate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAgent11gUpdate
  /**
   ** Update an instance of {@link Agent11gUpdate} with the specified
   ** properties.
   **
   ** @param  mode               the operational mode to apply.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverAddress      the initial serverAddress of the request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  username           the initial username of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the initial password of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  agentName          the initial agentName of the agent.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link Agent11gUpdate}.
   **                            <br>
   **                            Possible object is {@link Agent11gUpdate}.
   */
  public Agent11gUpdate createAgent11gUpdate(final String mode, final String serverAddress, final String username, final String password, final String agentName) {
    return new Agent11gUpdate(mode, serverAddress, username, password, agentName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAgent11gResponse
  /**
   ** Create an instance of {@link Agent11gResponse}.
   **
   ** @return                    an instance of {@link Agent11gResponse}.
   */
  public Agent11gResponse createAgent11gResponse() {
    return new Agent11gResponse();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrclSSOCreate
  /**
   ** Create an instance of {@link OrclSSOCreate}.
   **
   ** @return                    an instance of {@link OrclSSOCreate}.
   */
  public OrclSSOCreate createOrclSSOCreate() {
    return new OrclSSOCreate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrclSSOCreate
  /**
   ** Create an instance of {@link OrclSSOCreate} with the specified
   ** properties.
   **
   ** @param  mode               the operational mode to apply.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverAddress      the initial serverAddress of the request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  username           the initial username of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the initial password of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  agentName          the initial agentName of the agent.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link OrclSSOCreate}.
   **                            <br>
   **                            Possible object is {@link OrclSSOCreate}.
   */
  public OrclSSOCreate createOrclSSOCreate(final String mode, final String serverAddress, final String username, final String password, final String agentName) {
    return new OrclSSOCreate(mode, serverAddress, username, password, agentName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrclSSOUpdate
  /**
   ** Create an instance of {@link OrclSSOUpdate}.
   **
   ** @return                    an instance of {@link OrclSSOUpdate}.
   **                            <br>
   **                            Possible object is {@link OrclSSOUpdate}.
   */
  public OrclSSOUpdate createOrclSSOUpdate() {
    return new OrclSSOUpdate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrclSSOUpdate
  /**
   ** Create an instance of {@link OrclSSOUpdate} with the specified
   ** properties.
   **
   ** @param  mode               the operational mode to apply.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverAddress      the initial serverAddress of the request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  username           the initial username of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the initial password of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  agentName          the initial agentName of the agent.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link OrclSSOUpdate}.
   **                            <br>
   **                            Possible object is {@link OrclSSOUpdate}.
   */
  public OrclSSOUpdate createOrclSSOUpdate(final String mode, final String serverAddress, final String username, final String password, final String agentName) {
    return new OrclSSOUpdate(mode, serverAddress, username, password, agentName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrclSSOResponse
  /**
   ** Create an instance of {@link OrclSSOResponse}.
   **
   ** @return                    an instance of {@link OrclSSOResponse}.
   */
  public OrclSSOResponse createOrclSSOResponse() {
    return new OrclSSOResponse();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOpenSSOCreate
  /**
   ** Create an instance of {@link OpenSSOCreate}.
   **
   ** @return                    an instance of {@link OpenSSOCreate}.
   */
  public OpenSSOCreate createOpenSSOCreate() {
    return new OpenSSOCreate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOpenSSOCreate
  /**
   ** Create an instance of {@link OpenSSOCreate} with the specified
   ** properties.
   **
   ** @param  mode               the operational mode to apply.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverAddress      the initial serverAddress of the request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  username           the initial username of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the initial password of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  agentName          the initial agentName of the agent.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link OpenSSOCreate}.
   **                            <br>
   **                            Possible object is {@link OpenSSOCreate}.
   */
  public OpenSSOCreate createOpenSSOCreate(final String mode, final String serverAddress, final String username, final String password, final String agentName) {
    return new OpenSSOCreate(mode, serverAddress, username, password, agentName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOpenSSOUpdate
  /**
   ** Update an instance of {@link OpenSSOUpdate}.
   **
   ** @return                    an instance of {@link OpenSSOUpdate}.
   **                            <br>
   **                            Possible object is {@link OpenSSOUpdate}.
   */
  public OpenSSOUpdate createOpenSSOUpdate() {
    return new OpenSSOUpdate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOpenSSOUpdate
  /**
   ** Update an instance of {@link OpenSSOUpdate} with the specified
   ** properties.
   **
   ** @param  mode               the operational mode to apply.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverAddress      the initial serverAddress of the request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  username           the initial username of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the initial password of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  agentName          the initial agentName of the agent.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link OpenSSOUpdate}.
   **                            <br>
   **                            Possible object is {@link OpenSSOUpdate}.
   */
  public OpenSSOUpdate createOpenSSOUpdate(final String mode, final String serverAddress, final String username, final String password, final String agentName) {
    return new OpenSSOUpdate(mode, serverAddress, username, password, agentName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOpenSSOResponse
  /**
   ** Create an instance of {@link OpenSSOResponse}.
   **
   ** @return                    an instance of {@link OpenSSOResponse}.
   **                            <br>
   **                            Possible object is {@link OpenSSOResponse}.
   */
  public OpenSSOResponse createOpenSSOResponse() {
    return new OpenSSOResponse();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPolicyRequest
  /**
   ** Create an instance of {@link PolicyRequest}.
   **
   ** @return                    an instance of {@link PolicyRequest}.
   **                            <br>
   **                            Possible object is {@link PolicyRequest}.
   */
  public PolicyRequest createPolicyRequest() {
    return new PolicyRequest();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPolicyRequest
  /**
   ** Create an instance of {@link PolicyRequest} with the specified
   ** properties.
   **
   ** @param  mode               the operational mode to apply.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverAddress      the initial serverAddress of the request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  username           the initial username of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the initial password of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  hostIdentifier     the initial hostIdentifier of the policy.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  applicationDomain  the initial applicationDomain of the agent.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link PolicyRequest}.
   **                            <br>
   **                            Possible object is {@link PolicyRequest}.
   */
  public PolicyRequest createPolicyRequest(final String mode, final String serverAddress, final String username, final String password, final String hostIdentifier, final String applicationDomain) {
    return new PolicyRequest(mode, serverAddress, username, password, hostIdentifier, applicationDomain);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPolicyResponse
  /**
   ** Create an instance of {@link PolicyResponse}.
   **
   ** @return                    an instance of {@link PolicyResponse}.
   **                            <br>
   **                            Possible object is {@link PolicyResponse}.
   */
  public PolicyResponse createPolicyResponse() {
    return new PolicyResponse();
  }
}