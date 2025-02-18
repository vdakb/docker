package oracle.iam.identity.ots.service;

import java.util.Set;
import java.util.List;

import java.io.Writer;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import oracle.mds.core.MDSSession;

import oracle.mds.naming.DocumentName;
import oracle.mds.naming.ReferenceException;

import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.PManager;

import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskMessage;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.reconciliation.Descriptor;

import oracle.iam.identity.ots.service.reconciliation.DescriptorFactory;

public class XMLExport extends Controller {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute that sets application instance type we wnat to export
   */
  //protected static final String APPLICATION_INSTANCE_TYPE              = "Application Instance Type";
  protected static final String APPLICATION_INSTANCE_DISCONNECTED_TYPE   = "Disconnected Type Resource";
  protected static final String APPLICATION_INSTANCE_DOBBASED_TYPE       = "Application Type Resource";
  protected static final String VALID_ACCOUNTS                           = "Valid Accounts Only";

  /**
   ** Attribute tag which must be defined on a scheduled task to specify if
   ** entitlements needes to be handled unmarshalling from or mashalling to an
   ** XML file.
   */
   protected static final String PROCESS_ENTITELEMENT = "Process Entitlements";
  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute that holds the value of the last run */
    //TaskAttribute.build(TIMESTAMP,            TaskAttribute.MANDATORY)
    /** Application Instance Name */
    TaskAttribute.build(APPLICATION_INSTANCE, TaskAttribute.MANDATORY),
    /** Application Instance Type: * | Disconnected | DOBBased */
    //TaskAttribute(APPLICATION_INSTANCE_TYPE, TaskAttribute.MANDATORY),
    /** Application Instance Type: * | Disconnected | DOBBased */
    TaskAttribute.build(APPLICATION_INSTANCE_DISCONNECTED_TYPE, TaskAttribute.MANDATORY),
    /** Application Instance Type: * | Disconnected | DOBBased */
    TaskAttribute.build(APPLICATION_INSTANCE_DOBBASED_TYPE, TaskAttribute.MANDATORY),
    /** Application Instance Type: * | Disconnected | DOBBased */
    TaskAttribute.build(VALID_ACCOUNTS, TaskAttribute.MANDATORY),
    /** Entitlements should be exported? */
    TaskAttribute.build(PROCESS_ENTITELEMENT, TaskAttribute.MANDATORY),
    /** the filename of the raw data  */
    TaskAttribute.build(DATAFILE, TaskAttribute.MANDATORY),
    /** the location from where the raw files will be loaded */
    TaskAttribute.build(DATA_FOLDER, TaskAttribute.MANDATORY),
    /** the file encoding to use */
    TaskAttribute.build(FILE_ENCODING, TaskAttribute.MANDATORY)
  };

  /**
   ** Attribute tag which must be defined on a scheduled task to specify if the
   ** entire file has to be validate against the XML schema before it will be
   ** unmarshalled.
   */
  protected static final String VALIDATE_SCHEMA = "Validate Import";

  /** the category of the logging facility to use */
  private static final String LOGGER_CATEGORY = "OCS.ARC.HARVESTER";

  protected static boolean mappingDefLoaded = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor

  /**
   ** Constructs an empty <code>IdentityRoleExporter</code> scheduled job that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public XMLExport() {
    // ensure inheritance
    super(LOGGER_CATEGORY);
  }


  protected TaskAttribute[] attributes() {
    return new TaskAttribute[0];
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)

  /**
   ** This method is invoked just before the thread operation will be executed.
   **
   ** @throws TaskException      the exception thrown if any goes wrong
   */
  @Override
  protected void initialize() throws TaskException {


    parameter(TIMESTAMP, "0");
    parameter(ERROR_FOLDER, stringValue(DATA_FOLDER));
    //parameter(RECONCILE_DESCRIPTOR,"/metadata/fsag-features-configuration/testexport/account-export.xml");
    parameter(RECONCILE_DESCRIPTOR,"");
    super.initialize();

    final String method = "initialize";
    final String path   = stringValue(RECONCILE_DESCRIPTOR);

    if (path != null && path != SystemConstant.EMPTY){
      mappingDefLoaded = true;
      try {
        debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_FETCH, path));
        final MDSSession session = createSession();
        final PManager   manager  = session.getPersistenceManager();
        final PDocument  document = manager.getDocument(session.getPContext(), DocumentName.create(path));
        if (document == null){
          throw new TaskException(TaskError.METADATA_OBJECT_NOTFOUND, path);
        }
        if(mappingDefLoaded){
          debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_UNMARSHAL, path));
          this.descriptor = new Descriptor(this);
          DescriptorFactory.configure(this.descriptor, document);
          // produce the logging output only if the logging level is enabled for
          if (this.logger != null && this.logger.debugLevel())
            debug(method, this.descriptor.toString());
        }
      }
      catch (ReferenceException e) {
        throw new TaskException(e);
      }

      if (this.descriptor.attributeMapping().isEmpty())
        throw new TaskException(TaskError.ATTRIBUTE_MAPPING_EMPTY);
    }
  }

  public final Set<String> returningAttributes() {
    return this.descriptor.returningAttributes();
  }


  protected static void useBufferedFileOutPutStream(List<String> content,
                                                    String filePath,
                                                    String fileEncoding) throws TaskException {
    Writer writer = null;
    try {
      writer =
          new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath),
                                                    fileEncoding));
      for (String line : content) {
        line += System.getProperty("line.separator");
        writer.write(line);
      }
    }
    catch (IOException e) {
      throw new TaskException(e);

    }
    finally {
      if (writer != null) {
        try {
          writer.close();
        }
        catch (Exception e) {
          throw new TaskException(e);
        }
      }
    }
  }

  public String appInstanceNameFilter(){
    String str = stringValue(APPLICATION_INSTANCE);

    if(str.equals("*") || str.equals(null) || str.equals(""))
      str = "%";

    return (str);
  }

/*
  public String appInstanceTypeFilter(){
    String str = stringValue(APPLICATION_INSTANCE_TYPE);

    if(str.equals("*") || str.equals(null) || str.equals(""))
    str = "%";

    return (str);
  }


  public String appInstanceTypeObjFilter(){
    String str = stringValue(APPLICATION_INSTANCE_TYPE);

    if(str.equals("*") || str.equals(null) || str.equals(""))
      str = "%";
    else
      str = appInstanceTypeFilter().equals("DOBBased")?"Application":"Disconnected";

    return (str);
  }
  */

  public String getAppInstanceTypeFilter(){
    final boolean isDisconnected = booleanValue(APPLICATION_INSTANCE_DISCONNECTED_TYPE,false);
    final boolean isDOBased      = booleanValue(APPLICATION_INSTANCE_DOBBASED_TYPE,false);

    if(isDisconnected && isDOBased)
      return("%");
    else if(isDisconnected && !isDOBased)
      return("Disconnected");
    else if(!isDisconnected && isDOBased)
      return("DOBBased");
    //By default returns Disconnected
    return("Disconnected");
  }


  public String getAppInstanceTypeObjFilter(){

    final boolean isDisconnected = booleanValue(APPLICATION_INSTANCE_DISCONNECTED_TYPE,false);
    final boolean isDOBased      = booleanValue(APPLICATION_INSTANCE_DOBBASED_TYPE,false);

    if(isDisconnected && isDOBased)
      return("%");
    else if(isDisconnected && !isDOBased)
      return("Disconnected");
    else if(!isDisconnected && isDOBased)
      return("Application");

    //By default returns Disconnected
    return("Disconnected");
  }



  public String appInstanceLoggingName(){
    String str = stringValue(APPLICATION_INSTANCE);

    if(str.equals("*") || str.equals(null) || str.equals(""))
      str = "All Application Instances";

    return (str);
  }


  protected void onExecution() throws TaskException {
  }
}
