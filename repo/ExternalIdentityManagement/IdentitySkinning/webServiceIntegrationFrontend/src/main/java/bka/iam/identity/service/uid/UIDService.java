package bka.iam.identity.service.uid;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcITResourceNotFoundException;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.tcResultSet;

import bka.iam.identity.service.ServiceError;
import bka.iam.identity.service.ServiceException;
import bka.iam.identity.service.rest.ServiceResource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.hst.foundation.SystemConsole;
import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.logging.AbstractLoggable;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.utils.Constants;
import oracle.iam.ui.platform.model.common.OIMClientFactory;

public class UIDService extends AbstractLoggable {
  
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The console used for logging purpose */
  public final static SystemConsole console = new SystemConsole("bka.iam.ui.service.uid");
  
  /** The name of the IT Resource that holds informations of UID service */
  private static final String ENDPOINT_DEFAULT              = "UID.Endpoint";
  
  /** The regex that identify a tenant */
  private static String TENANT_PATTERN = "[TP]-[0-9]{1,3}-[0-9]{1,2}-[0-9]{2,11}-[0-9]{3}";
  
  /** special caracter that separate the UID */
  private static final String UID_SEPARATOR                 = "-";
  
  /** number of fieds in the Unique Identifier */
  private static final int    UID_SIZE                      = 6;
  
  /** number of fieds in the tenant */
  private static final int    TENANT_SIZE                   = 5;
  
  /** the tenant index of the <code>participantType</code> */
  private static final int    TENANT_PARTICIPANT_TYPE_INDEX = 0;
  
  /** the tenant index of the <code>country</code> */
  private static final int    TENANT_COUNTRY_INDEX          = 1;
  
  /** the index of the <code>state</code> */
  private static final int    TENANT_STATE_INDEX            = 2;
  
  /** the tenant index of the <code>participant</code> */
  private static final int    TENANT_PARTICIPANT_INDEX      = 3;
  
  /** the tenant index of the <code>type</code> */
  private static final int    TENANT_TYPE_INDEX             = 4;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the abstraction layer to describe the connection to the target system */
  private Context context;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs the class <code>UIDService</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** 
   ** @throws ServiceException   in the event of misconfiguration or if
   **                            initialization fails.
   */
  public UIDService()
    throws ServiceException {
    // ensure inheritance
    super(console);
    
    initialize();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionallity
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** Initialize the configuration.
   **
   ** @throws ServiceException   in the event of misconfiguration or if
   **                            initialization fails.
   */
  private void initialize()
    throws ServiceException {
    final String method = "initialize";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    final Map<String, String> itResData = new HashMap<String, String>();
    
    try {
      final tcITResourceInstanceOperationsIntf   itResourceInstOp = OIMClientFactory.getITResourceInstanceOperations();
      final Map<String, String>                  param = new HashMap<String, String>();
      param.put(Constants.IT_RESOURCE_NAME, ENDPOINT_DEFAULT);
      tcResultSet  results = itResourceInstOp.findITResourceInstances(param);

      if (results.getRowCount() != 1)
        throw new ServiceException(ServiceError.ITRESOURCENOTFOUND, ENDPOINT_DEFAULT);
      
      results.goToRow(0);
      final long itResouceKey = results.getLongValue(Constants.IT_RESOURCE_KEY);
      results = itResourceInstOp.getITResourceInstanceParameters(itResouceKey);
      for (int i = 0; i < results.getRowCount(); i++) {
        results.goToRow(i);
        itResData.put(results.getStringValue(Constants.IT_RESOURCE_TYPE_PARAMETER_NAME), results.getStringValue(Constants.IT_RESOURCE_TYPE_PARAMETER_VALUE));
      }

      this.context = Context.build(this, new ServiceResource(this, itResData));
    }
    catch (TaskException e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    catch (tcAPIException e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    catch (tcColumnNotFoundException e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    catch (tcITResourceNotFoundException e) {
      throw new ServiceException(ServiceError.ITRESOURCENOTFOUND, ENDPOINT_DEFAULT);
    }
    catch (IllegalArgumentException e) {
      throw new ServiceException(ServiceError.ITRESOURCE_MISCONFIGURED, ENDPOINT_DEFAULT);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   request
  /**
   ** Build and executes the request to generate unique identifier at UID
   ** Service for the specified tenant <code>tenant</code>.
   **
   ** @param tenant               the organization tenant to create the unique
   **                             identifier for.
   **                             <br>
   **                             Allowed object is {@link String}.
   **
   ** @return                     the successfully created uid.
   **                             <br>
   **                             Possible object is {@link String}.
   **
   ** @throws UIDException        if the provided tenant is not correct
   **                             
   ** @throws ServiceException    if the REST service provider responded with an
   **                             error.
   */
  public String request(final String tenant)
    throws ServiceException, UIDException {
    // prevent bogus state
    if (tenant == null)
      throw new UIDException(UIDError.TENANT_MISSING);

    if (!tenant.matches(TENANT_PATTERN))
      throw new UIDException(UIDError.TENANT_FIELD_MISSING, tenant);
    
    final List<String> tenantFields = Arrays.asList(tenant.split(UID_SEPARATOR));
    return this.context.requestUniqueIdentifier(tenantFields.get(TENANT_PARTICIPANT_TYPE_INDEX),
                                                tenantFields.get(TENANT_COUNTRY_INDEX),
                                                tenantFields.get(TENANT_STATE_INDEX),
                                                tenantFields.get(TENANT_PARTICIPANT_INDEX),
                                                tenantFields.get(TENANT_TYPE_INDEX));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Build and executes the register unique identifier at UID Service with
   ** the specified <code>uid</code>.
   **
   ** @param uid                  the value of the uid tenant to register
   **                             <br>
   **                             Allowed object is {@link String}.
   **
   ** @return                     the successfully registered uid.
   **                             <br>
   **                             Possible object is {@link String}.
   **
   ** @throws UIDException        if the provided uid is not correct
   **                             
   ** @throws ServiceException    if the REST service provider responded with an
   **                             error.
   */
  public String register(final String uid)
    throws ServiceException, UIDException {
    if (uid == null) {
      throw new UIDException(UIDError.UID_MISSING);
    }
    if (uid.split(UID_SEPARATOR).length != UID_SIZE) {
      throw new UIDException(UIDError.UID_FIELD_MISSING, uid);
    }
    return this.context.registerUniqueIdentifier(uid);
  }
}
