/*
    Oracle Deutschland BV & Co. KG

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information"). You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2025. All Rights reserved.

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   OIMServiceContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class OIMServiceContext.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2025-21-02  SBernet     First release version
*/
package bka.iam.identity.scim.extension.oim;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.exception.ScimMessage;
import bka.iam.identity.scim.extension.exception.resource.ScimBundle;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;
import bka.iam.identity.scim.extension.oim.Option.SortOrder;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.spi.AbstractEndpoint;

import java.lang.reflect.InvocationTargetException;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.OIMInternalClient;
import oracle.iam.platform.entitymgr.spi.entity.Searchable;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
////////////////////////////////////////////////////////////////////////////////
// class OIMScimContext
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** This class is responsible for managing OIM operations with
 ** SCIM resources such as Users, Groups, Entitlement and so on.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class OIMServiceContext extends AbstractEndpoint {
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  private   OIMClient platform;
  
  protected Option    options;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OIMServiceContext</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected OIMServiceContext() {
    // ensure inheritance
    super();
    
    this.platform = (OIMClient) new OIMInternalClient();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>OIMServiceContext</code> instance with search parameters.
   **
   ** @param count                 The number of results to return.
   ** @param startIndex            The index of the first result.
   ** @param sortOrder             The sorting order (ascending or descending).
   ** @param requiredAttributeName Set of attributes to include.
   ** @param excludedAttributeName Set of attributes to exclude.
   */
  protected OIMServiceContext(final Integer count, final Integer startIndex, final String sortBy, final SortOrder sortOrder, final Set<String> requiredAttributeName, final Set<String> excludedAttributeName) {
    // ensure inheritance
    super();
    
    this.platform = (OIMClient) new OIMInternalClient();
    this.options = new Option(count, startIndex, sortBy, sortOrder, requiredAttributeName, excludedAttributeName, getDefaultSearchCriteria());
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>OIMServiceContext</code> instance with search parameters.
   **
   ** @param requiredAttributeName Set of attributes to include.
   ** @param excludedAttributeName Set of attributes to exclude.
   */
  protected OIMServiceContext(final Set<String> requiredAttributeName, final Set<String> excludedAttributeName) {
    // ensure inheritance
    super();
    
    this.platform = (OIMClient) new OIMInternalClient();
    this.options = new Option(null, null, null, null, requiredAttributeName, excludedAttributeName, getDefaultSearchCriteria());
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Accessors methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   platform
  /**
   ** Returns the OIM Client to allow access to internal methods.
   **
   ** @return                    Returns the OIM Client to allow access to
   **                            internal methods.
   **                            <br>
   **                            Possible object is {@link OIMClient}.
   */
  public final OIMClient platform() {
    return this.platform;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserService
  /**
   ** Returns an instance of a User manager Facade by invoking the method
   ** platform service.
   **
   **
   ** @return                    Returns an instance of a User manager Facade
   **                            by invoking the method platform service.
   */
  public final UserManager getUserService() {
    return platform().getService(UserManager.class);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRoleService
  /**
   ** Returns an instance of a Role manager Facade by invoking the method
   ** platform service.
   **
   **
   ** @return                    Returns an instance of a Role manager Facade
   **                            by invoking the method platform service.
   */
  public final RoleManager getRoleService() {
    return platform().getService(RoleManager.class);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOrganizationService
  /**
   ** Returns an instance of a Organization manager Facade by invoking the
   ** method platform service.
   **
   **
   ** @return                    Returns an instance of a Organization manager
   **                            Facade by invoking the method platform service.
   */
  public final OrganizationManager getOrganizationService() {
    return platform().getService(OrganizationManager.class);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createResource
  /**
   ** Creates a new SCIM resource in OIM.
   **
   ** @param resource            The SCIM resource to create.
   ** @return                    The created resource.
   ** 
   ** @throws ScimException      If creation fails.
   */
  public final <T extends ScimResource> T createResource(final T resource) throws ScimException {
    final String method = "create";
    
    this.entering(method);
    this.watch.start(method);
    
    final T createdResource = create(resource);
    
    this.watch.stop(method);
    this.exiting(method, this.watch.summary());
    
    return createdResource;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchResource
  /**
   ** Searches for SCIM resources in OIM.
   **
   ** @param   additionalSearchCriteria Additional search criteria.
   ** 
   ** @return  List of matching         SCIM resources.
   ** 
   ** @throws  ScimException            If search fails.
   */
  public final <T extends ScimResource> List<T> searchResource(final SearchCriteria additionalSearchCriteria) throws ScimException {
    final String method = "search";
    
    this.entering(method);
    this.watch.start(method);

    
    if (additionalSearchCriteria != null) {
      
      SearchCriteria sc = this.options.getSearchCriteria();
      SearchCriteria nc = new SearchCriteria(sc, additionalSearchCriteria, SearchCriteria.Operator.AND);
      this.options.setSearchCriteria(nc);
    }
    
    final List<T> resources = search();
    
    
    this.watch.stop(method);
    this.debug(method, this.watch.toString());
    this.exiting(method, this.watch.summary());
    
    return resources;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupResource
  /**
   ** Searches for a SCIM resource in OIM using a given entity ID and
   ** discriminator.
   ** <p>
   ** This method applies the specified discriminator as a search criteria,
   ** performs the search, and returns the first matching resource. If no
   ** resource is found, an exception is thrown.
   ** </p>
   **
   ** @param   entityID            The unique identifier of the resource to look
   **                              up.
   **                              Allowed object is {@link String}.
   ** @param   discriminator       The attribute used to filter the search
   **                              results.
   **                              Allowed object is {@link String}.
   **
   ** @return                      The first matching resource.
   **                              Allowed object is {@link ScimResource}.
   **
   ** @throws  ScimException       If no resource is found or an error occurs
   **                              during the lookup.
   */
  public final <T extends ScimResource> T lookupResource(final String entityID, final String discriminator) throws ScimException {
    final String method = "search";
    
    this.entering(method);
    this.watch.start(method);
    
    if (discriminator != null) {
      
      SearchCriteria sc = this.options.getSearchCriteria();
      SearchCriteria dc = new SearchCriteria(discriminator, entityID, SearchCriteria.Operator.EQUAL);
      SearchCriteria nc = new SearchCriteria(sc, dc, SearchCriteria.Operator.AND);
      this.options.setSearchCriteria(nc);
    }
    
    System.out.println("Search with the following search criteria: " + this.options.getSearchCriteria());
    final List<T> resources = search();

    this.debug(method, "Search returns: " + resources.size());
    this.watch.stop(method);
    this.debug(method, this.watch.toString());
    this.exiting(method, this.watch.summary());
    if (resources.size() >= 1)
        return resources.get(0);
    else
        throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.USER_NOTFOUND, entityID));
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   modifyResource
  /**
   ** Modifies a SCIM resource in OIM by comparing and updating attributes.
   ** <p>
   ** This method compares the attributes of the current resource and the
   ** modified resource, updating only those that have changed. If an attribute
   ** remains the same, it is ignored. Attributes that exist in the
   ** current resource but not in the modified resource are reset to an empty
   ** value.
   ** </p>
   **
   ** @param   entityID            The unique identifier of the resource to 
   **                              modify.
   **                              Allowed object is {@link String}.
   ** @param   modifiedResource    The modified version of the resource with
   **                              new attributes.
   **                              Allowed object is {@link ScimResource}.
   ** @param   currentResource     The existing resource currently stored in OIM.
   **                              Allowed object is {@link ScimResource}.
   **
   ** @return                      The updated SCIM resource after modification.
   **                              Allowed object is {@link ScimResource}.
   **
   ** @throws  ScimException       If an error occurs during modification.
   */
  public final <T extends ScimResource> T modifyResource(final String entityID, T modifiedResource, T currentResource) throws ScimException {
    final String method = "search";
    
    this.entering(method);
    this.watch.start(method);
    try {
      T resourceBuilder = (T) currentResource.getClass().getDeclaredConstructor(ResourceDescriptor.class).newInstance(currentResource.getResourceDescriptor());
      this.debug(method, "Before  currentResource " + currentResource);
      this.debug(method, "Before  modifyResource " + modifiedResource);
      for (String schema : currentResource.getSchemaURIs())
        resourceBuilder.addSchema(schema);
      for (String attribute : getResourceAttributeList()) {
        Object currentAttribute = currentResource.getAttribute(attribute);
        Object modifiedAttribute = modifiedResource.getAttribute(attribute);
       
        System.out.println("Comparing currentResource " + currentAttribute + " and modifiedresource: " + modifiedAttribute);
      if (currentResource.getAttribute(attribute) != null && modifiedAttribute != null) {
        if (currentAttribute.equals(modifiedAttribute)) {
          System.out.println("Attribute are equals. Put null in attribute: " + attribute);
          //resource.addAttribute(attribute, new Object());
        }
        else {
          resourceBuilder.addAttribute(attribute, modifiedAttribute);
        }
      }
      else if (currentAttribute == null && modifiedAttribute != null) {
        System.out.println("Attribute in new resource is not null but currentResource yes: " + attribute);
        resourceBuilder.addAttribute(attribute, modifiedAttribute);
        // Do nothing
      }
      else if (currentAttribute != null && modifiedAttribute == null) {
        System.out.println("Attribute in currentResource is not null but new resource yes: " + attribute);
        resourceBuilder.addAttribute(attribute, null);
      }
      else if (currentAttribute == null && modifiedAttribute == null) {
        System.out.println("Attribute in currentResource is null and new resource also: " + attribute);
        // Do nothing
      }
    }
      this.debug(method, "Attribute to modify" + resourceBuilder);
      modifiedResource = replace(entityID, resourceBuilder);
    }
    catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e);
    };
    
    this.watch.stop(method);
    this.exiting(method, this.watch.summary());
    
    return modifiedResource;
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   replaceResource
  /**
   ** Replaces an existing SCIM resource in OIM.
   ** <p>
   ** This method fully replaces the existing resource with the provided resource,
   ** overwriting all attributes. Unlike {@code modifyResource}, which
   ** updates only changed attributes, this method performs a complete
   ** replacement.
   ** </p>
   **
   ** @param   entityID            The unique identifier of the resource to be
   **                              replaced.
   **                              Allowed object is {@link String}.
   ** @param   resource            The new version of the resource to replace
   **                              the existing one.
   **                              Allowed object is {@link ScimResource}.
   **
   ** @return                      The updated SCIM resource after replacement.
   **                              Allowed object is {@link ScimResource}.
   **
   ** @throws  ScimException       If an error occurs during the replacement
   **                              operation.
   */
  public final <T extends ScimResource> T replaceResource(final String entityID, T resource) throws ScimException {
    final String method = "search";
    
    this.entering(method);
    this.watch.start(method);

    resource = replace(entityID, resource);
    
    this.watch.stop(method);
    this.exiting(method, this.watch.summary());
    
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteResource
  /**
   ** Deletes a SCIM resource from OIM.
   **
   ** @param  entityID       The resource ID.
   ** 
   ** @throws ScimException  If deletion fails.
   */
  public final void deleteResource(final String entityID) throws ScimException {
    final String method = "search";
    
    this.entering(method);
    this.watch.start(method);

    delete(entityID);
    
    this.watch.stop(method);
    this.exiting(method, this.watch.formatted());;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSearchOptions
  /**
   ** Builds search options for OIM queries.
   **
   ** @return HashMap containing search options.
   */
  protected HashMap<String, Object> getSearchOptions() {
    final HashMap<String, Object> searchOption = new HashMap<>();
    final String method = "getSearchOptions";
    this.entering(method);
    
    if (this.options == null)
      return searchOption;
    
    if (this.options.getStarIndex() != null)
    searchOption.put("STARTROW", new Integer((int) this.options.getStarIndex()));
    
    if (this.options.getSortOrder() != null) {
      switch (this.options.getSortOrder()) {
        case ASCENDING:
          searchOption.put("SORTORDER", Searchable.SortOrder.ASCENDING);
          break;
        default:
          searchOption.put("SORTORDER", Searchable.SortOrder.DESCENDING);
          break;
      }
    }
    
    if (this.options.getSortBy() != null && !this.options.getSortBy().isEmpty())
      searchOption.put("SORTEDBY", this.options.getSortBy());
    if (this.options.getStarIndex() != null) {
      searchOption.put("STARTROW", new Integer(this.options.getStarIndex()));
      if (this.options.getCount() != -1L)
      searchOption.put("ENDROW", new Integer((int) (this.options.getStarIndex() + this.options.getCount() - 1L))); 
    }
    if (this.options.getSortBy() != null && this.options.getCount() == null)
        searchOption.put("ENDROW", Integer.valueOf(2147483647)); 
    
    this.exiting(method, searchOption);
    return searchOption;
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   count
  /**
   ** Retrieves the total count of SCIM resources that match the current search
   ** criteria.
   ** <p>
   ** This method performs a search operation to determine the number of
   ** matching resources and returns the total count.
   ** </p>
   **
   ** @return                      The total number of matching SCIM resources.
   **                              Allowed object is {@link Long}.
   **
   ** @throws  ScimException       If an error occurs during the search
   **                              operation.
   */
  public Long count() throws ScimException {
    final List users = search();
    
    return new Long(users.size());
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Abstract Methods
  //////////////////////////////////////////////////////////////////////////////
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a SCIM resource in OIM.
   **
   ** @param  resource           The SCIM resource to create.
   **                            Allowed object is {@link ScimResource}.
   **
   ** @return                    The created SCIM resource.
   **                            Allowed object is {@link ScimResource}.
   **
   ** @throws ScimException      If the resource creation fails.
   */
  protected abstract <T extends ScimResource> T create(T resource) throws ScimException;
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** Searches for SCIM resources in OIM.
   **
   ** @return                    A list of SCIM resources matching the criteria.
   **                            Allowed object is {@link List}.
   **
   ** @throws ScimException      If the search operation fails.
   */
  protected abstract <T extends ScimResource> List<T> search() throws ScimException;
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Replaces an existing SCIM resource in OIM.
   **
   ** @param  entityID           The unique identifier of the resource to replace.
   **                            Allowed object is {@link String}.
   ** @param  resource           The new SCIM resource to replace the existing one.
   **                            Allowed object is {@link ScimResource}.
   **
   ** @return                    The updated SCIM resource.
   **                            Allowed object is {@link ScimResource}.
   **
   ** @throws ScimException      If the replacement operation fails.
   */
  protected abstract <T extends ScimResource> T replace(final String entityID, final T resource) throws ScimException;
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes a SCIM resource from OIM.
   **
   ** @param  entityID           The unique identifier of the resource to delete.
   **                            Allowed object is {@link String}.
   **
   ** @throws ScimException      If the deletion operation fails.
   */
  protected abstract void delete(final String entityID) throws ScimException;
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   getDefaultSearchCriteria
  /**
   ** Provides the default search criteria for SCIM resource queries.
   **
   ** @return                    The default search criteria.
   **                            Allowed object is {@link SearchCriteria}.
   */
  protected abstract SearchCriteria getDefaultSearchCriteria();
  
  ////////////////////////////////////////////////////////////////////////////////
  // Method:   getResourceAttributeList
  /**
   ** Retrieves the list of attributes used in SCIM resource
   **
   ** @return                    A list of SCIM attribute names.
   **                            Allowed object is {@link List}.
   */
  protected abstract List<String> getResourceAttributeList();
}
