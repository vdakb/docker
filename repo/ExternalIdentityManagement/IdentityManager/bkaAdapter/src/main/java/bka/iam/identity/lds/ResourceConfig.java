package bka.iam.identity.lds;

import java.util.Map;
import java.util.HashMap;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcITResourceNotFoundException;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;

import oracle.iam.platform.Platform;

import oracle.hst.foundation.logging.Logger;

import oracle.iam.identity.foundation.naming.ITResource;

public class ResourceConfig {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final        String ITR_ATTR_CONFIG_LOOKUP       = "Configuration Lookup";
  private static final        String ITR_ATTR_CONFIG_BASECONTEXTS = "baseContexts";

  private static final Logger LOGGER = Logger.create(ResourceConfig.class.getName());

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   service
  /**
   ** Returns an instance of a Business Facade by invoking the method platform
   ** service resolver to return the appropriate instance of the desired
   ** Business Facade.
   ** <br>
   ** The utility factory keeps track of created Business Facade and on
   ** execution of close() will free all aquired resources of the created
   ** Business Facade.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @param  <T>                the java class type of the  Business Facade.
   ** @param  serviceClass       the class of the Business Facade to create.
   **                            Typically it will be of the sort:
   **                            <code>Thor.API.tcNameUtilityIntf.class</code>.
   **
   ** @return                    the Business Facade.
   **                            It needs not be cast to the requested Business
   **                            Facade.
   */
  public static final <T> T service(final Class<T> serviceClass) {
    return Platform.getService(serviceClass);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   participantRealm
  /**
   ** Obtains the value of from the configuration <code>Lookup Definition</code>
   ** mapped for {@link #ITR_ATTR_CONFIG_BASECONTEXTS}.
   **
   ** @param  resourceInstance   the system identifier of the Identity Manager
   **                            <code>IT Resource</code> where this wrapper
   **                            belongs to.
   ** 
   ** @return                    a value obtained from the configuration
   **                            <code>Lookup Definition</code> mapped at given
   **                            <code>name</code> or <code>null</code> if value
   **                            isn't mapped at <code>name</code>.
   **
   **
   ** @throws tcAPIException                if data set errors occur.
   ** @throws tcColumnNotFoundException     if if the desired value isn't mapped
   **                                       at given <code>name</code> in the
   **                                       configuration lookup definition.
   ** @throws tcITResourceNotFoundException in case of the
   **                                       <code>IT Resource</code> is not
   **                                       defined in the Identity Manager meta
   **                                       data entries with the system
   **                                       identifier passed in
   */
  public static final String participantRealm(final long resourceInstance)
    throws tcAPIException
     ,     tcITResourceNotFoundException
     ,     tcColumnNotFoundException {

    return resourceValue(resourceInstance, ITR_ATTR_CONFIG_BASECONTEXTS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configurationValue
  /**
   ** Obtains the value from the configuration <code>Lookup Definition</code>
   ** mapped at given <code>name</code>.
   **
   ** @param  resourceInstance   the system identifier of the Identity Manager
   **                            <code>IT Resource</code> where this wrapper
   **                            belongs to.
   ** @param  name               the name of the value to lookup from the main
   **                            configuration lookup definition.
   ** 
   ** @return                    a value obtained from the configuration
   **                            <code>Lookup Definition</code> mapped at given
   **                            <code>name</code> or <code>null</code> if value
   **                            isn't mapped at <code>name</code>.
   **
   ** @throws tcAPIException                if data set errors occur.
   ** @throws tcColumnNotFoundException     if if the desired value isn't mapped
   **                                       at given <code>name</code> in the
   **                                       configuration lookup definition.
   ** @throws tcITResourceNotFoundException in case of the
   **                                       <code>IT Resource</code> is not
   **                                       defined in the Identity Manager meta
   **                                       data entries with the system
   **                                       identifier passed in
   */
  public static final String configurationValue(final long resourceInstance, final String name)
    throws tcAPIException
     ,     tcITResourceNotFoundException
     ,     tcColumnNotFoundException {

    final String                 lookupDefinition = mainConfiguration(resourceInstance);
    final tcLookupOperationsIntf lookupService    = service(tcLookupOperationsIntf.class);
    LOGGER.debug("Retrieving attr[" + name + "] value from [" + lookupDefinition + "]...");
    String value = lookupService.getDecodedValueForEncodedValue(lookupDefinition, name);
    LOGGER.trace("Found value[" + value + "] from attr[" + name + "], lookup[" + lookupDefinition + "]...");
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mainConfiguration
  /**
   ** Obtains the name of the <code>Lookup Definition</code> configured from the
   ** <code>IT Resource</code> parameters.
   **
   ** @param  resourceInstance   the system identifier of the Identity Manager
   **                            <code>IT Resource</code> where this wrapper
   **                            belongs to.
   ** 
   ** @return                    the name of the <code>Lookup Definition</code>
   **                            configured at the <code>IT Resource</code>
   **                            parameters.
   **
   ** @throws tcAPIException                if data set errors occur.
   ** @throws tcColumnNotFoundException     if if the desired value isn't mapped
   **                                       at given <code>name</code> in the
   **                                       <code>IT Resource</code> parameters.
   ** @throws tcITResourceNotFoundException in case of the
   **                                       <code>IT Resource</code> is not
   **                                       defined in the Identity Manager meta
   **                                       data entries with the system
   **                                       identifier passed in
   */
  public static final String mainConfiguration(final long resourceInstance)
    throws tcAPIException
     ,     tcITResourceNotFoundException
     ,     tcColumnNotFoundException {

    LOGGER.debug("Retrieving cache lookup table name via ITResurceKey: " + resourceInstance);
    String mainConfiguration = resourceValue(resourceInstance, ITR_ATTR_CONFIG_LOOKUP);
    LOGGER.trace("Found mainConfigLookupName[" + mainConfiguration + "]");
    return mainConfiguration;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceValue
  /**
   ** Obtains the value of an <code>IT Resource</code> parameters mapped at
   ** given <code>name</code>.
   **
   ** @param  resourceInstance   the system identifier of the Identity Manager
   **                            <code>IT Resource</code> where this wrapper
   **                            belongs to.
   ** @param  name               the name of the value to lookup from the 
   **                            <code>IT Resource</code> parameters.
   ** 
   ** @return                    the value mapped at <code>name</code> in the
   **                            <code>IT Resource</code> parameters.
   **
   ** @throws tcAPIException                if data set errors occur.
   ** @throws tcColumnNotFoundException     if if the desired value isn't mapped
   **                                       at given <code>name</code> in the
   **                                       <code>IT Resource</code> parameters.
   ** @throws tcITResourceNotFoundException in case of the
   **                                       <code>IT Resource</code> is not
   **                                       defined in the Identity Manager meta
   **                                       data entries with the system
   **                                       identifier passed in
   */
  private static final String resourceValue(final long resourceInstance, final String name)
    throws tcAPIException
     ,     tcITResourceNotFoundException
     ,     tcColumnNotFoundException {

    return populateAttributes(resourceInstance).get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateAttributes
  /**
   ** Obtains the <code>IT Resource</code> definition from Identity Manager.
   **
   ** @param  resourceInstance   the system identifier of the Identity Manager
   **                            <code>IT Resource</code> where this wrapper
   **                            belongs to.
   **
   ** @throws tcITResourceNotFoundException in case of the
   **                                       <code>IT Resource</code> is not
   **                                       defined in the Identity Manager meta
   **                                       data entries with the system
   **                                       identifier passed in
   */
  private static final Map<String, String> populateAttributes(final long resourceInstance)
    throws tcAPIException
     ,     tcITResourceNotFoundException
     ,     tcColumnNotFoundException {

    tcITResourceInstanceOperationsIntf itResourceService = service(tcITResourceInstanceOperationsIntf.class);

    Map<String, String> attrs = new HashMap<String, String>();
    // obtain declared attributes
    final tcResultSet parameter = itResourceService.getITResourceInstanceParameters(resourceInstance);
    int size = parameter.getRowCount();
    for (int i = 0; i < size; i++) {
      parameter.goToRow(i);
      attrs.put(parameter.getStringValue(ITResource.PARAMETER_NAME), parameter.getStringValue(ITResource.PARAMETER_VALUE));
    }
    return attrs;
  }
}