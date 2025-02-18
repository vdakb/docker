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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   MetadataService.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    MetadataService.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
    12.2.1.3.42.60.99  2021-08-27  DSteding    Conection is opened per default
                                               as writable
*/

package oracle.jdeveloper.connection.iam.service;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;

import java.io.Reader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.Closeable;

import java.net.URL;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import oracle.mds.core.MDSSession;
import oracle.mds.core.MDSInstance;
import oracle.mds.core.MetadataObject;
import oracle.mds.core.IsolationLevel;
import oracle.mds.core.SessionOptions;
import oracle.mds.core.ValidationException;
import oracle.mds.core.MetadataExistsException;
import oracle.mds.core.MetadataNotFoundException;
import oracle.mds.core.ConcurrentMOChangeException;

import oracle.mds.config.PConfig;
import oracle.mds.config.MDSConfig;
import oracle.mds.config.CustConfig;
import oracle.mds.config.NamespaceConfig;
import oracle.mds.config.DocumentCacheConfig;
import oracle.mds.config.CustClassListMapping;
import oracle.mds.config.MDSConfigurationException;

import oracle.mds.naming.Namespace;
import oracle.mds.naming.PackageName;
import oracle.mds.naming.DocumentName;
import oracle.mds.naming.ResourceName;
import oracle.mds.naming.ReferenceException;
import oracle.mds.naming.InvalidReferenceException;
import oracle.mds.naming.InvalidReferenceTypeException;

import oracle.mds.query.QueryResult;
import oracle.mds.query.QueryFactory;
import oracle.mds.query.ResourceQuery;
import oracle.mds.query.NameCondition;
import oracle.mds.query.CustLayerFilter;
import oracle.mds.query.ConditionFactory;

import oracle.mds.cust.CustClassList;
import oracle.mds.cust.CustomizationClass;

import oracle.mds.exception.MDSException;
import oracle.mds.exception.MDSRuntimeException;
import oracle.mds.exception.InvalidNamespaceException;
import oracle.mds.exception.UnsupportedUpdateException;

import oracle.mds.transfer.CustOption;
import oracle.mds.transfer.MDSTransfer;
import oracle.mds.transfer.TransOption;
import oracle.mds.transfer.TransferUnit;
import oracle.mds.transfer.TransferOption;
import oracle.mds.transfer.TransferOptions;
import oracle.mds.transfer.TransferUnitList;
import oracle.mds.transfer.ExtendedMetadataOption;

import oracle.mds.versioning.Version;
import oracle.mds.versioning.VersionedNamespaceConfig;

import oracle.mds.versioning.persistence.PManagerVersionSupport;

import oracle.mds.persistence.PManager;
import oracle.mds.persistence.PContext;
import oracle.mds.persistence.PPackage;
import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.PResource;
import oracle.mds.persistence.PTransaction;
import oracle.mds.persistence.MetadataStore;
import oracle.mds.persistence.MDSIOException;
import oracle.mds.persistence.ConcurrentDocChangeException;

import oracle.mds.persistence.stores.db.DBMetadataStore;
import oracle.mds.persistence.stores.db.DBMetadataRepository;

import oracle.jdeveloper.connection.iam.Bundle;

import oracle.jdeveloper.connection.iam.model.MetadataServer;

////////////////////////////////////////////////////////////////////////////////
// class MetadataService
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>MetadataService</code> implements the base functionality of an
 ** Oracle Metadata Service Connector.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class MetadataService extends EndpointService {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String                MALFORMED_PREFIX  = "java.net.MalformedURLException: ";
  static final String                CONNECTION_PREFIX = "java.net.ConnectException: ";
  static final String                SECURITY_PREFIX   = "javax.security.auth.login.LoginException: java.lang.SecurityException: ";

  static final String                CUST_NAME_ALL     = "%";
  static final String                CUST_VALUE_START  = "[";
  static final String                CUST_VALUE_END    = "]";

  static final List<CustLayerFilter> NULL              = null;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private MetadataStore store           = null;
  private MDSInstance   instance        = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>MetadataService</code> which is associated with the
   ** specified task.
   **
   ** @param  resource           the {@link MetadataServer} definition where
   **                            this service is associated with.
   **                            <br>
   **                            Allowed object is {@link MetadataServer}.
   */
  public MetadataService(final MetadataServer resource) {
    // ensure inheritance
    super(resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   established (overridden)
  /**
   ** Returns the state of the connection of this service.
   **
   ** @return                    <code>true</code> if the connection of the
   **                            related target system is established;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public final boolean established() {
    return this.instance != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partition
  /**
   ** Returns the partiotion of the Metadata Service to connect to.
   **
   ** @return                    the partiotion of the Metadata Service to
   **                            connect to.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final String partition() {
    return ((MetadataServer)this.resource).partition();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unwrap
  /**
   ** Returns the {@link MDSInstance} this context is using to connect and
   ** perform operations on the metadata server.
   **
   ** @return                    the {@link MDSInstance} this task is using to
   **                            connect and perform operations on the metadata
   **                            server.
   **                            <br>
   **                            Allowed object is {@link MDSInstance}.
   */
  public final MDSInstance unwrap() {
    return this.instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Close the source or destination of data preventing propagation of
   ** <code>Exception</code>/.
   **
   ** @param  source             the source or destination to be closed.
   **                            <br>
   **                            Allowed object is {@link Closeable}.
   */
  public static void close(final Closeable source) {
    if (source != null) {
      try {
        source.close();
      }
      catch (Throwable e) {
        e.printStackTrace();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNamespaceConfig
  /**
   ** Factory method to get a namespaceconfig array based on the root namespace
   ** ("/") and store and a labelname(if present).
   **
   ** @param  namespacePath      the {@link Namespace} to be set on the
   **                            {@link NamespaceConfig}.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  store              the {@link MetadataStore} to be set on the
   **                            {@link NamespaceConfig}.
   **                            <br>
   **                            Allowed object is {@link MetadataStore}.
   ** @param  labelName          the name of the label to be set on the
   **                            {@link NamespaceConfig}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link NamespaceConfig} array, can be used
   **                            to build PConfig.
   **                            <br>
   **                            Possible object is array of
   **                            {@link NamespaceConfig}.
   **
   ** @throws MetadataException if the  given path is not valid.
   */
  public static NamespaceConfig[] createNamespaceConfig(final String namespacePath, final MetadataStore store, final String labelName)
    throws MetadataException {

    return createNamespaceConfig(namespacePath, store, labelName, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   custLayerFilterList
  /**
   ** Factory to create a {@link List} of {@link CustLayerFilter} objects to be
   ** used in TransferOption.
   **
   ** @param  excludeAllCust     whether to exclude all customization documents.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  includeCustList    the array of layer name patterns to be
   **                            included.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   ** @param  excludeCustList    the array of layer name patterns to be
   **                            excluded.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   ** @param  mdsConfig          MDSConfig object.
   **                            <br>
   **                            Allowed object is {@link MDSConfig}.
   **
   ** @return                    the {@link List} of {@link CustLayerFilter}
   **                            objects.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link CustLayerFilter}.
   **
   ** @throws MetadataException  when error occurs.
   */
  public static List<CustLayerFilter> custLayerFilterList(final boolean excludeAllCust, final String[] includeCustList, final String[] excludeCustList, final MDSConfig mdsConfig)
    throws MetadataException {

    // If excludeCustList contains cust layer value, throw Exclude
    // Cust Layer Value Not Supported Exception
    if (excludeCustList != null) {
      for (int i = 0; i < excludeCustList.length; i++) {
        if (custLayerValues(excludeCustList[i]) != null)
          throw new MetadataException("EXCLUDE_CUST_VALUE_NOT_SUPPORTED");
      }
    }

    List<CustLayerFilter> filter = null;
    if (!excludeAllCust) {
      if (includeCustList != null && includeCustList.length != 0) {
        filter = new ArrayList<CustLayerFilter>();
        for (int i = 0; i < includeCustList.length; i++) {
          String nameAndValues = includeCustList[i];

          // Check for the include all cust option
          if (CUST_NAME_ALL.equals(nameAndValues)) {
            // In this case, we don't need to remove
            // excludeCustList from the cust class list,
            // because cust class excludsion can be
            // handled by transfer API.
            return NULL;
          }

          String custLayerName = custLayerName(nameAndValues);

          // Check if this cust name is on the exclusion list
          if (custLayerName != null && !custLayerExcluded(custLayerName, excludeCustList)) {
            List<String> custLayerValues = custLayerValues(nameAndValues);
            if (custLayerValues == null) {
              filter.add(new CustLayerFilter(custLayerName));
            }
            else {
              filter.add(new CustLayerFilter(custLayerName, custLayerValues));
            }
          }
        }
      }
      else if (mdsConfig != null) {
        CustConfig custConfig = mdsConfig.getCustConfig();
        if (custConfig != null) {
          CustClassListMapping[] mappings = custConfig.getMappings();
          filter = new ArrayList<>();
          for (int i = 0; i < mappings.length; i++) {
            if (mappings[i] != null) {
              CustClassList ccList = mappings[i].getCustClassList();
              for (int j = 0; j < ccList.size(); j++) {
                CustomizationClass cc = ccList.item(j);

                // Check if cust name is on the exclusion list
                String ccName = cc.getName();
                if (!custLayerExcluded(ccName, excludeCustList)) {
                  filter.add(new CustLayerFilter(ccName));
                }
              }
            }
          }
        }
      }
    }
    return filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transferOptions
  /**
   ** Factory method to build the {@link TransferOptions}.
   **
   ** @param  excludeExtended    whether to exclude extended metadata documents.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  excludeAllCust     whether to exclude all customization documents.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  filter             the {@link List} of {@link CustLayerFilter}
   **                            objects.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link CustLayerFilter}.
   ** @param  exclude            the CustClassList to be excluded from the
   **                            operation.
   **                            <br>
   **                            Allowed object is {@link CustClassList}.
   **
   ** @return                    the {@link TransferOptions} object.
   **                            <br>
   **                            Possible object is {@link TransferOptions}.
   */
  public static TransferOptions transferOptions(boolean excludeExtended, boolean excludeAllCust, final List<CustLayerFilter> filter, final CustClassList exclude) {
    TransOption          transOption        = null;
    TransferOptions      transferOptions    = null;
    List<TransferOption> transferOptionList = new ArrayList<TransferOption>(10);

    // Creates ExtendedMetadataOption instance.
    if (!excludeExtended) {
      transferOptionList.add(new ExtendedMetadataOption());
    }

    // Creates CustOption instance.
    if (!excludeAllCust) {
      if (filter != null) {
        // transfer specified cust list
        // excludeCustClassList has already been deducted from
        // custLayerFilterList
        if (filter.size() > 0) {
          transferOptionList.add(new CustOption(filter));
        }
      }
      else {
        // transfer all cust with exclude cust list
        transferOptionList.add(new CustOption(transOption, exclude, true, false));
      }
    }

    if (transferOptionList.size() > 0) {
      transferOptions = new TransferOptions(transferOptionList.toArray(new TransferOption[transferOptionList.size()]));
    }
    else {
      transferOptions = new TransferOptions(new TransferOption[0]);
    }

    return transferOptions;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNamespaceConfig
  /**
   ** Factory method to get a namespaceconfig array based on the root namespace
   ** ("/") and store and a labelname(if present).
   **
   ** @param  namespacePath      the {@link Namespace} to be set on the
   **                            {@link NamespaceConfig}.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  store              {@link MetadataStore} to be set on the
   **                            {@link NamespaceConfig}.
   **                            <br>
   **                            Allowed object is {@link MetadataStore}.
   ** @param  labelName          the name of the label to be set on the
   **                            {@link NamespaceConfig}.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  cacheConfig        the {@link DocumentCacheConfig} for the
   **                            document cache (PDCache), can be
   **                            <code>null</code>, in which case it will be set
   **                            to default value.
   **                            <br>
   **                            Allowed object is {@link DocumentCacheConfig}.
   **
   ** @return                    the {@link NamespaceConfig} array, can be used
   **                            to build PConfig.
   **                            <br>
   **                            Possible object is array of
   **                            {@link NamespaceConfig}.
   **
   ** @throws MetadataException if the  given path is not valid.
   */
  public static NamespaceConfig[] createNamespaceConfig(final String namespacePath, final MetadataStore store, final String labelName, final DocumentCacheConfig cacheConfig)
    throws MetadataException {

    NamespaceConfig[] nsConfArray = null;
    try {
      final Namespace ns = Namespace.create(namespacePath);
      // #(6071268) Support specifying a DocumentCacheConfig for cases
      // when PDCache needs to be disabled.
      if (labelName != null) {
        // build a namespaceConfig array to store the namespace, store, and
        // label info
        VersionedNamespaceConfig vNsConf = new VersionedNamespaceConfig(ns, store, labelName, cacheConfig);
        nsConfArray = new NamespaceConfig[] { vNsConf };
      }
      else {
        nsConfArray = new NamespaceConfig[] { new NamespaceConfig(ns, store, cacheConfig) };
      }
    }
    catch (InvalidNamespaceException e) {
      throw new MetadataException(e);
    }

    return nsConfArray;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link MetadataStore} by creating the appropriate
   ** environment from the attributes of the associated model.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The connection is a writable connection.
   **
   ** @throws MetadataException if the {@link MDSInstance} could not be created
   **                           at the first time this method is invoked.
   */
  public void connect()
    throws MetadataException {

    connect(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link MetadataStore} by creating the appropriate
   ** environment from the attributes of the associated model.
   **
   ** @param  readOnly           the mode the connection to the
   **                            {@link MetadataStore} should be opened.
   **                            If <code>true</code> the connection id opened
   **                            readonly.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the {@link MetadataStore} connection. 
   **                            <br>
   **                            Possible object is {@link MetadataStore}.
   **
   ** @throws MetadataException if the {@link MDSInstance} could not be created
   **                           at the first time this method is invoked.
   */
  public MetadataStore connect(final boolean readOnly)
    throws MetadataException {

    if (this.instance != null) {
      return this.store;
    }

   try {
      // create a metadata store that leverage a database
      this.store    = new DBMetadataStore(principalName(), principalPassword(), ((MetadataServer)this.resource).serviceURL(), (!readOnly), false, partition());
      this.instance = aquireInstance(this.resource.name(), this.store);
    }
    catch (MDSException e) {
      throw new MetadataException(e);
    }
    return this.store;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect
  /**
   ** Closes the managed application context.
   ** <br>
   ** This method releases the context's resources immediately, instead of
   ** waiting for them to be released automatically by the garbage collector.
   ** <p>
   ** This method is idempotent:  invoking it on a context that has already been
   ** closed has no effect. Invoking any other method on a closed context is not
   ** allowed, and results in undefined behaviour.
   **
   ** @throws MetadataException in case an error does occur.
   */
  public void disconnect()
    throws MetadataException {

    releaseInstance(this.resource.name());
    this.instance = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partitions
  /**
   ** List all partiontions available in an Metadata Repository.
   **
   ** @return                    the {@link List} of partitions in a Metadata
   **                            Repository.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @throws MetadataException if the request fails.
   */
  public List<String> partitions()
    throws MetadataException {

    try {
      // create a metadata store that leverage a database
      final DBMetadataRepository store = new DBMetadataRepository(principalName(), principalPassword(), ((MetadataServer)this.resource).serviceURL());
      return store.getAllPartitionNames();
    }
    catch (MDSIOException e) {
      throw new MetadataException(e);
    }
    catch (MDSException e) {
      throw new MetadataException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   packageCreate
  /**
   ** Creates a package in the MetadataStore specified for the associated
   ** {@link PManager}.
   ** <p>
   ** In multitenancy enabled environment, users are allowed to create packages
   ** that are mapped to the same tenant scope of the associated
   ** {@link PTransaction} (i.e scope of the tenantId attached to
   ** <code>PContext</code> of the {@link PTransaction}).
   ** <br>
   ** If user tries to create a pacakge that is mapped to different tenant
   ** scope, this will throw {@link MetadataException}. For example, if global
   ** user tries to create a document of tenant user scope or tenant user tries
   ** to create a document of global user scope, this will throw exception.
   ** <p>
   ** If multitenancy is disabled, no validation will happen based on the tenant
   ** scope.
   **
   ** @param  packageName        the {@link PackageName} representing the fully
   **                            qualified name for the package.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws MetadataException  if the package being created is not created
   **                            using the same store as previous mutate
   **                            operations or document name is mapped to
   **                            different tenant scope.
   */
  public void packageCreate(final PackageName packageName)
    throws MetadataException {

    if (!established())
      connect();

    if (resourceExists(packageName))
      throw new MetadataException(Bundle.format(Bundle.CONTEXT_RESOURCE_EXISTS, packageName.getAbsoluteName()));

    final MDSSession   session     = this.instance.createSession(null, null);
    final PTransaction transaction = session.getPTransaction();
    try {
      transaction.createPackage(packageName);
    }
    catch (MDSIOException e) {
      throw new MetadataException(e);
    }
    catch (UnsupportedUpdateException e) {
      throw new MetadataException(e);
    }
    catch (InvalidNamespaceException e) {
      throw new MetadataException(e);
    }
    finally {
      try {
        session.flushChanges();
      }
      catch (ValidationException e) {
        throw new MetadataException(e);
      }
      catch (ConcurrentMOChangeException e) {
        throw new MetadataException(e);
      }
      catch (MDSIOException e) {
        throw new MetadataException(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourcePurge
  /**
   **
   ** @param  olderThan          the {@link Number} of seconds which all
   **                            unlabeled documents will be purged if they are
   **                            older than. If this parameter is missing or
   **                            specified as <code>0</code>, all versions
   **                            except for the tip version will be purged. 
   */
  public void resourcePurge(final Number olderThan) {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceQuery
  /**
   ** Performs a {@link ResourceQuery} as a specialized form of a query to
   ** return the packages and documents that belongs to {@link PackageName}
   ** <code>base</code>.
   ** <br>
   ** {@link ResourceQuery} operates at the persistence level of a Metadata
   ** Service and  evaluates the query conditions against each package and
   ** document.
   ** <br>
   ** The result set returned can only contain {@link QueryResult}s of type
   ** package and document.
   **
   ** @param  base               the {@link PackageName} to query for packages
   **                            and documents.
   **                            <br>
   **                            Allowed object is {@link PackageName}.
   **
   ** @return                    the {@link ResourceQuery}.
   **                            <br>
   **                            Possible object is {@link ResourceQuery}.
   **
   ** @throws MetadataException  if the connection isn't available at the time
   **                            of invocation.
   */
  public ResourceQuery resourceQuery(final PackageName base)
    throws MetadataException {

    return resourceQuery(base, "%");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceQuery
  /**
   ** Performs a {@link ResourceQuery} as a specialized form of a query to
   ** return the packages and documents that belongs to {@link PackageName}
   ** <code>base</code>.
   ** <br>
   ** {@link ResourceQuery} operates at the persistence level of a Metadata
   ** Service and  evaluates the query conditions against each package and
   ** document.
   ** <br>
   ** The result set returned can only contain {@link QueryResult}s of type
   ** package and document.
   **
   ** @param  base               the {@link PackageName} to query for packages
   **                            and documents.
   **                            <br>
   **                            Allowed object is {@link PackageName}.
   ** @param  filter             the condition(s) to restrict the result set.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link ResourceQuery}.
   **                            <br>
   **                            Possible object is {@link ResourceQuery}.
   **
   ** @throws MetadataException  if the connection isn't available at the time
   **                            of invocation.
   */
  public ResourceQuery resourceQuery(final PackageName base, final String filter)
    throws MetadataException {

    if (!established())
      connect();

    return QueryFactory.createResourceQuery(this.instance, ConditionFactory.createNameCondition(base.getAbsoluteName(), filter));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   documentStream
  public InputStream documentStream(final DocumentName document)
    throws MetadataException {

    return documentStream(resourceDocument(document));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceDocument
  /**
   ** Returns a {@link InputSource} which could be used for reading the contents
   ** of the document. The {@link InputStream} or <code>Reader</code> from the
   ** {@link InputSource} returned must be closed by calling close() method
   ** after reading the content from the <code>Reader</code> so that any system
   ** resources acquired for reading the document are released.
   ** <p>
   ** The contents of the document could be either metadata (XML) or
   ** non-metadata (binary/text) and can be returned as either
   ** {@link InputStream} or <code>Reader</code>.
   **
   ** @param  document           the {@link DocumentName} to read.
   **                            <br>
   **                            Allowed object is {@link DocumentName}.
   **
   ** @return                    the {@link InputSource} from which the document
   **                            contents can be read.
   **                            <br>
   **                            Possible object is {@link InputSource}.
   **
   ** @throws MetadataException  if any errors occur while reading the document.
    */
  public InputSource resourceDocument(final DocumentName document)
    throws MetadataException {

    if (!established())
      connect();

    InputSource     source  = null;
    final PManager  manager = this.instance.getPersistenceManager();
    final PContext  context = manager.createPContext();
    try {
      final PDocument pd = manager.getDocument(context, document);
      source = pd.read();
    }
    catch (MDSIOException e) {
      throw new MetadataException(e);
    }
    return source;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceExport
  /**
   ** Downloads a {@link ResourceName} to the local filesystem.
   **
   ** @param  resource           the {@link ResourceName} to export.
   **                            <br>
   **                            Allowed object is {@link ResourceName}.
   ** @param  target             the {@link URL} receiving the values for the
   **                            export process.
   **                            <br>
   **                            Allowed object is {@link URL}.
   **
   ** @throws MetadataException  if an unrecoverable error occurs during the
   **                            course of the export process.
   */
  public void resourceExport(final ResourceName resource, final URL target)
    throws MetadataException {

    if (!established())
      connect();

    final String     source  = resource.getAbsoluteName();
    final MDSSession session = this.instance.createSession(null, null);
    try {
      final MetadataObject objectFile = session.getMetadataObject(resource.getAbsoluteName());
      MetadataMarshaller.write(target, objectFile.getDocument());
    }
    catch (ReferenceException e) {
      throw new MetadataException(e);
    }
    catch (MetadataNotFoundException e) {
      throw new MetadataException(Bundle.format(Bundle.CONTEXT_RESOURCE_NOT_EXISTS, source), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceImport
  /**
   ** Creates a document in the MetadataStore specified for the associated
   ** {@link PManager} with specified document name and contents from given
   ** {@link URL} <code>source</code>. The <code>source</code> can contain
   ** either metadata or non-metadta.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** <b>Any subsequent changes made to the document will be taken into account
   ** when the metadata object is saved to the configured metadata store by
   ** making a call to {@link MDSSession#flushChanges()} on the session.</b>
   **
   ** @param  resource           the {@link ResourceName} to import.
   **                            <br>
   **                            This must be a folder in the Metadata Service.
   **                            <br>
   **                            Allowed object is {@link PackageName}.
   ** @param  name               the name of the document to create.
   **                            <br>
   **                            This needs not to be the same as the filename
   **                            of the provided <code>source</code>
   **                            {@link URL}, but ususally it is.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  source             the {@link URL} providing the values for the
   **                            import process.
   **                            <br>
   **                            Allowed object is {@link URL}.
   **
   ** @throws MetadataException  if an unrecoverable error occurs during the
   **                            course of the import process.
   */
  public void resourceImport(final PackageName resource, final String name, final URL source)
    throws MetadataException {

    if (!established())
      connect();

    final MDSSession   session  = this.instance.createSession(null, null);
    final DocumentName location = documentName(resource, name);
    try {
      session.createMetadataObject(location.getAbsoluteName(), MetadataMarshaller.read(source));
    }
    catch (MetadataExistsException e) {
      int response = confirmOverride(name, resource.getAbsoluteName());
      if (response == 0) {
        documentUpdate(location, source);
      }
    }
    catch (UnsupportedUpdateException e) {
      // rollback all changes done so far
      session.cancelChanges();
      throw new MetadataException(e);
    }
    catch (ReferenceException e) {
      // rollback all changes done so far
      session.cancelChanges();
      throw new MetadataException(e);
    }
    finally {
      try {
        session.flushChanges();
      }
      catch (MDSException e) {
        throw new MetadataException(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   documentUpdate
  /**
   ** Saves content from the given {@link URL} <code>source</code> into the
   ** specified document.
   ** <p>
   ** The {@link URL} <code>source</code>can contain either metadata or
   ** non-metadata content.
   ** <p>
   ** In multitenancy enabled environment, users are allowed to save a document
   ** from the same tenant stripe of the associated {@link PTransaction} (i.e
   ** scope of the tenantId attached to <code>PContext</code> of the associated
   ** {@link PTransaction}). If the {@link PDocument} ceated here has different
   ** tenantId attached, this will throw {@link MetadataException}.
   ** <p>
   ** If multitenancy is disabled, no validation will happen based on the tenant
   ** scope.
   **
   ** @param  name               the name of the document to update.
   **                            <br>
   **                            This needs not to be the same as the filename
   **                            of the provided <code>source</code>
   **                            {@link URL}, but ususally it is.
   **                            <br>
   **                            Allowed object is {@link DocumentName}.
   ** @param  source             the {@link URL} from which the new contents
   **                            should read.
   **                            <br>
   **                            Allowed object is {@link URL}.
   **
   ** @throws MetadataException  if an unrecoverable error occurs during the
   **                            course of the import process.
   */
  public void documentUpdate(final DocumentName name, final URL source)
    throws MetadataException {

    if (!established())
      connect();

    final MDSSession   session     = this.instance.createSession(null, null);
    final PManager     manager     = this.instance.getPersistenceManager();
    final PDocument    resource    = manager.getDocument(session.getPContext(), name);
    final PTransaction transaction = session.getPTransaction();
    try {
      transaction.saveDocument(resource, true, MetadataMarshaller.read(source));
    }
    catch (UnsupportedUpdateException e) {
      // rollback all changes done so far
      session.cancelChanges();
      throw new MetadataException(e);
    }
    catch (ConcurrentDocChangeException e) {
      // rollback all changes done so far
      session.cancelChanges();
      throw new MetadataException(e);
    }
    catch (MDSIOException e) {
      // rollback all changes done so far
      session.cancelChanges();
      throw new MetadataException(e);
    }
    finally {
      try {
        session.flushChanges();
      }
      catch (MDSException e) {
        throw new MetadataException(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   documentRename
  /**
   ** Renames the MetadataObject with <code>source</code> to
   ** <code>target</code>.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** Referential integrity checks are not made. Only single document rename are
   ** handled and dependent documents are not updated.
   ** <p>
   ** In multitenancy enabled environment, both <code>source</code> and
   ** <code>target</code> should be in the same tenant scope of the associated
   ** {@link PTransaction} (i.e scope of the tenantId attached to
   ** <code>PContext</code> of the associated {@link PTransaction}).
   ** <br>
   **  If <code>source</code> or <code>target</code> is mapped to different
   **  tenant scope, this will throw {@link MetadataException}. While renaming
   **  global scope base documents clients should rename global customization
   **  documents available in tenant stripe separately with tenant user
   **  {@link PTransaction}.
   **  <p>
   **  If multitenancy is disabled, no validation will happen based on the
   **  tenant scope.
   **
   ** @param  source             the name of the document to rename.
   **                            <br>
   **                            Allowed object is {@link DocumentName}.
   ** @param  target             the new name of the document.
   **                            <br>
   **                            Allowed object is {@link DocumentName}.
   **
   ** @throws MetadataException  if an unrecoverable error occurs during the
   **                            course of the import process.
   */
  public void documentRename(final DocumentName source, final DocumentName target)
    throws MetadataException {

    if (!established())
      connect();

    final MDSSession   session     = this.instance.createSession(null, null);
    final PTransaction transaction = session.getPTransaction();
    try {
      transaction.renameDocument(source, target);
    }
    catch (UnsupportedUpdateException e) {
      // rollback all changes done so far
      session.cancelChanges();
      throw new MetadataException(e);
    }
    catch (MDSIOException e) {
      // rollback all changes done so far
      session.cancelChanges();
      throw new MetadataException(e);
    }
    finally {
      try {
        session.flushChanges();
      }
      catch (MDSException e) {
        throw new MetadataException(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   documentVersion
  /**
   ** Obtains the version information associated with a MetadataObject with
   ** <code>source</code>.
   **
   ** @param  source             the name of the document to obtain the version
   **                            information for.
   **                            <br>
   **                            Allowed object is {@link DocumentName}.
   ** @param  maxVersion         the maximum number of versions to return.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the mapping of the versions where the key of
   **                            the return {@link Map} is the version number
   **                            and the values of the correspondending element
   **                            is the date if the version.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element if of type {@link Long} for the key and
   **                            {@link Document} as the value.
   **
   ** @throws MetadataException  if an unrecoverable error occurs during the
   **                            course of the import process.
   */
  public Map<Long, Document> documentVersion(final DocumentName source, final int maxVersion)
    throws MetadataException {

    final Map<Long, Document> result = new HashMap<Long, Document>();
    final List<String>        query  = new ArrayList<String>();
    query.add(source.getAbsoluteName());

    final List<CustLayerFilter> filter = custLayerFilterList(false, null, null, this.instance.getMDSConfig());

    TransferUnitList       transferUnit    = null;

    try {
      transferUnit = TransferUnitList.create(this.instance, query, MetadataService.transferOptions(true, false, filter, null), false, true);
      if (transferUnit == null || transferUnit.size() == 0)
       return result;

      final MDSSession             session = this.instance.createSession(new SessionOptions(IsolationLevel.READ_COMMITTED, null, null, null, null), null);
      final PManager               manager = session.getPersistenceManager();
      final PContext               context = session.getPContext();
      // walk through TUL and export doc versions
      final Iterator i = transferUnit.iterator();
      while (i.hasNext()) {
        final TransferUnit unit = (TransferUnit)i.next();
        final PDocument    pDoc = manager.getDocument(context, unit.getDocumentName());
        // if the document not exists anymore also a version will not exists
        // hence return an empty result
        if (pDoc == null) {
          return result;
        }
        // if the document itself has to be part of the transfer unit put it in
        // the result set on top
        if (unit.shouldTransferTUDocument()) {
          // Exporting version info of base docs
          exportVersion(session, unit, maxVersion, result);
        }

        TransferOptions option = unit.getTransferOptions();
        if (option != null) {
          final Iterator j = option.iterator();
          while (j.hasNext()) {
              TransferOption transOpt = (TransferOption)j.next();
              if (transOpt != null) {
                final Iterator k = transOpt.getAssociatedTransferUnits(this.instance, session, unit.getDocumentName());
                while (k.hasNext()) {
                  TransferUnit assoc = (TransferUnit)k.next();
                  // Exporting version info of cust docs
                  exportVersion(session, assoc, maxVersion, result);
                }
              }
            }
          }
      }
    }
    catch (MDSConfigurationException e) {
      throw new MetadataException(e);
    }
    catch (MDSIOException e) {
      throw new MetadataException(e);
    }
    catch (MDSException e) {
      throw new MetadataException(e);
    }
    finally {
      // Clean up the resource
      if (transferUnit != null) {
        transferUnit.clear();
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceDelete
  /**
   ** Deletes the specified metadata resource from the MetadataStore.
   ** <br>
   ** The specified {@link ResourceName} <code>resource</code> can either refere
   ** to a {@link PackageName} or a {@link DocumentName}.
   **
   ** @param resource            the {@link ResourceName} to be deleted.
   **                            <br>
   **                            Allowed object is {@link ResourceName}.
   **
   ** @throws MetadataException  if the resource being deleted is not deleted
   **                            using the same store as previous mutate
   **                            operations or passed in resource has different
   **                            tenantId attached.
   */
  public void resourceDelete(final ResourceName resource)
    throws MetadataException {

    if (!established())
      connect();

    final MDSSession   session     = this.instance.createSession(null, null);
    final PManager     manager     = this.instance.getPersistenceManager();
    final PContext     context     = session.getPContext();
    final PTransaction transaction = session.getPTransaction();
    try {
      if ((resource instanceof PackageName)) {
        packageDelete((PackageName)resource, context, transaction);
      }
      else {
        transaction.deleteDocument(manager.getDocument(context, (DocumentName)resource), true);
      }
    }
    catch (MDSIOException | UnsupportedUpdateException | ConcurrentDocChangeException e) {
      throw new MetadataException(e);
    }
    finally {
      try {
        session.flushChanges();
      }
      catch (ValidationException e) {
        throw new MetadataException(e);
      }
      catch (ConcurrentMOChangeException e) {
        throw new MetadataException(e);
      }
      catch (MDSIOException e) {
        throw new MetadataException(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceExists
  /**
   ** Performs a {@link ResourceQuery} as a specialized form of a query to
   ** verify the existance of a certain {@link ResourceName}
   ** <code>resource</code>.
   ** <br>
   ** {@link ResourceQuery} operates at the persistence level of a Metadata
   ** Service and  evaluates the query conditions against each package and
   ** document.
   ** <br>
   ** The result set returned can only contain {@link QueryResult}s of type
   ** package and document.
   **
   ** @param  resource           the {@link ResourceName} to verify.
   **                            <br>
   **                            Allowed object is {@link ResourceName}.
   **
   ** @return                    <code>true</code> if the {@link ResourceName}
   **                            exists in the Metadata Service.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws MetadataException  if the connection isn't available at the time
   **                            of invocation.
   */
  protected boolean resourceExists(final ResourceName resource)
    throws MetadataException {

    if (this.instance == null)
      throw new MetadataException(Bundle.format(Bundle.CONTEXT_ERROR_CONNECTION, "MDSInstance == null"));

    final NameCondition condition = ConditionFactory.createNameCondition(new ResourceName[] { resource });
    final ResourceQuery query     = QueryFactory.createResourceQuery(this.instance, condition);
    final Iterator      documents = query.execute();
    if (documents == null) {
      return false;
    }
    while (documents.hasNext()) {
      final QueryResult result = (QueryResult)documents.next();
      if (result != null) {
        return true;
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   packageDelete
  /**
   ** Deletes a package recursively if its not empty.
   **
   ** @param  resource           the {@link PackageName} to delete.
   **                            <br>
   **                            Allowed object is {@link PackageName}.
   ** @param  context            the  persistence context required to populate
   **                            potential content belonging to the package to
   **                            delete.
   **                            <br>
   **                            Allowed object is {@link PContext}.
   ** @param  transaction        the  transaction context required to delete the
   **                            package to delete.
   **                            <br>
   **                            Allowed object is {@link PTransaction}.
   **
   ** @throws MetadataException  if the connection isn't available at the time
   **                            of invocation.
   */
  protected void packageDelete(final PackageName resource, final PContext context, final PTransaction transaction)
    throws MetadataException {

    try {
      final PManager            manager  = this.instance.getPersistenceManager();
      final Iterator<PResource> contents = manager.getResources(context, resource);
      while (contents.hasNext()) {
        final PResource item = contents.next();
        if (item.isPackage()) {
          // resoursively delete the packages conatined
          packageDelete((PackageName)item.getResourceName(), context, transaction);
        }
        else {
          transaction.deleteDocument((PDocument)item, true);
        }
      }
      // finally delete the package itself because now it should be empty
      final PPackage ppackage = manager.getPackage(context, resource);
      transaction.deletePackage(ppackage, true);
    }
    catch (MDSIOException | UnsupportedUpdateException | InvalidNamespaceException | ConcurrentDocChangeException e) {
      throw new MetadataException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   documentStream
  protected InputStream documentStream(final InputSource source)
    throws MetadataException {

    ByteArrayOutputStream bos    = null;
    InputStream           stream = source.getByteStream();
    if (stream == null) {
      final Reader reader   = source.getCharacterStream();
      final String encoding = source.getEncoding();
      stream = new MetadataStream(reader, encoding == null ? "UTF-8" : encoding);
    }

    bos   = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    try {
      int len;
      while ((len = stream.read(buffer)) > -1) {
        bos.write(buffer, 0, len);
       }
      bos.flush();
      stream.close();
      stream = null;
    }
    catch (IOException e) {
      throw new MetadataException(Bundle.string(Bundle.CONTEXT_ERROR_UNHANDLED), e);
    }
    finally {
      close(source.getCharacterStream());
      close(stream);
    }
    return new ByteArrayInputStream(bos.toByteArray());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aquireInstance
  /**
   ** Creates the instance to the metadata store.
   **
   ** @param  name               the name of the connection registered in
   **                            the connection cache.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  config             the {@link MDSConfig} used to perform the
   **                            operation.
   **                            <br>
   **                            Allowed object is {@link MDSConfig}.
   **
   ** @return                    the instance to the metadata store.
   **                            <br>
   **                            Possible object is {@link MDSInstance}.
   **
   ** @throws MetadataException if the connection instance cannot be created.
   */
  protected MDSInstance aquireInstance(final String name, final MDSConfig config)
    throws MetadataException {

    // create an instance to the metadata store leveraging the provided
    // configuration and will be accesssible by the provided name
    // if an instance already existe for the provided name it will be reused to
    // create a session
    MDSInstance instance = null;
    try {
      instance = MDSInstance.getOrCreateInstance(name, config);
      established(true);
    }
    catch (MDSConfigurationException e) {
      throw new MetadataException(e);
    }
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aquireInstance
  /**
   ** Creates the instance to the metadata store.
   **
   ** @param  name               the name of the connection registered in
   **                            the connection cache.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  store              the {@link MetadataStore} used to create the
   **                            instance for.
   **                            <br>
   **                            Allowed object is {@link MetadataStore}.
   **
   ** @return                    the instance to the metadata store.
   **                            <br>
   **                            Possible object is {@link MDSInstance}.
   **
   ** @throws MetadataException if the connection instance cannot be created.
   */
  protected MDSInstance aquireInstance(final String name, final MetadataStore store)
    throws MetadataException {

    // create an instance to the metadata store leveraging the provided
    // configuration and will be accesssible by the provided name
    // if an instance already existe for the provided name it will be reused to
    // create a session
    MDSInstance instance = null;
    try {
      // #(6071268)  Disable PDCache on MDSInstances used in transfer/delete
      // since populating the PDCache is expensive due to non-existence queries
      // and also because PDCache will be ignored after the operations.
      final NamespaceConfig[] namespace = createNamespaceConfig("/", store, null, new DocumentCacheConfig(false, 0));
      final PConfig           config    = new PConfig(namespace, null, false, 0, 0);
      instance = MDSInstance.getOrCreateInstance(name, new MDSConfig(null, config, null, null), true);
    }
    catch (MDSConfigurationException e) {
      throw new MetadataException(e);
    }
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   release
  /**
   ** Release all resources aquired for the metadata store.
   **
   ** @throws MetadataException if the connection instance cannot be closed.
   */
  public void release()
    throws MetadataException {

   if (this.instance != null) {
      releaseInstance(this.instance.getName());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   releaseInstance
  /**
   ** Release all resources aquired for the metadata store.
   **
   ** @param  name               the name of the connection registered in
   **                            the connection cache.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws MetadataException if the connection instance cannot be closed.
   */
  protected void releaseInstance(final String name)
    throws MetadataException {

    // release an instance from the metadata store
    if (MDSInstance.getInstance(name) != null) {
      MDSTransfer.release(this.instance);
      try {
        // #(19186722) Use Global in case the instance
        // is MT. This is a deprecated API. Ideally we should be
        // calling the old API with the same TenantContext with
        // which the MDSInstance was created.
        MDSInstance.releaseInstance(name);
        established(false);
      }
      catch (MDSRuntimeException e) {
        throw new MetadataException(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   documentName
  /**
   ** Creates full qualified path for a document.
   **
   ** @param  resource           the package name.
   **                            <br>
   **                            Allowed object is {@link PackageName}.
   ** @param  name               the name of the document.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    full qualified path for a document.
   **                            <br>
   **                            Possible object is {@link DocumentName}.
   **
   ** @throws MetadataException  if the resulting document name is invalid.
   */
  public DocumentName documentName(final PackageName resource, final String name)
    throws MetadataException {

    DocumentName path = null;
    try {
      path = DocumentName.create(resource, name);
    }
    catch (InvalidReferenceException e) {
      throw new MetadataException(Bundle.string(Bundle.CONTEXT_ERROR_UNHANDLED), e);
    }
    catch (InvalidReferenceTypeException e) {
      throw new MetadataException(Bundle.string(Bundle.CONTEXT_ERROR_UNHANDLED), e);
    }
    return path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   custLayerExcluded
  /**
   ** Check whether a customization name is on the exclusion list.
   **
   ** @param  custName           the cust layer name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  exclude            the cust layer exclusion list.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    <code>true</code> if the cust layer is
   **                            excluded; <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is array of <code>boolean</code>.
   */
  private static boolean custLayerExcluded(final String custName, final String[] exclude) {
    if (exclude != null && exclude.length != 0) {
      for (int i = 0; i < exclude.length; i++) {
        String custLayerName = custLayerName(exclude[i]);
        if (custName.equals(custLayerName)) {
          return true;
        }
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   custLayerName
  /**
   ** Given a String like: "custName[cv1, cv2, cv3]", return custName.
   **
   ** @param  custNameValue      a String like: "custName[cv1, cv2, cv3]"
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the custName String
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private static String custLayerName(final String custNameValue) {
    String custName       = null;
    int    custValueStart = custNameValue.indexOf(CUST_VALUE_START);
    if (custValueStart == -1) {
      custName = custNameValue;
    }
    else {
      custName = custNameValue.substring(0, custValueStart);
    }
    custName = custName.trim();
    if (custName.length() == 0) {
      return null;
    }
    return custName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   custLayerValues
  /**
   ** Given a String like: "custName[cv1, cv2, cv3]", return List of
   ** cust layer values.
   **
   ** @param  custNameValue      a String like: "custName[cv1, cv2, cv3]".
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a {@link List} of cust layer values.
   **                            <code>null</code> if no values found; never
   **                            retruns empty list.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  private static List<String> custLayerValues(final String custNameValue) {
    int custValueStart = custNameValue.indexOf(CUST_VALUE_START);
    if (custValueStart == -1) {
      return null;
    }

    String valuesStr = custNameValue.substring(custValueStart + CUST_VALUE_START.length());
    int    custValueEnd = valuesStr.indexOf(CUST_VALUE_END);
    if (custValueEnd != -1) {
      valuesStr = valuesStr.substring(0, custValueEnd);
    }
    valuesStr = valuesStr.trim();
    if (valuesStr.length() == 0) {
      return null;
    }

    final String[]     values = valuesStr.split(",");
    final List<String> result = new ArrayList<String>(values.length);
    for (int i = 0; i < values.length; i++) {
      String val = values[i].trim();
      if (val.length() > 0) {
        result.add(val);
      }
    }
    if (result.size() == 0) {
      return null;
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exportVersion
  private void exportVersion(final MDSSession session, final TransferUnit unit, int maxVersions, final Map<Long, Document> result)
    throws MetadataException {

    // create the DocumentBuilder
    final DocumentBuilder builder = MetadataMarshaller.builder();
    final PManager        manager = session.getPersistenceManager();
    final PContext        context = session.getPContext();
    final PDocument       pDoc    = manager.getDocument(context, unit.getDocumentName());
    if (pDoc == null) {
      return;
    }

    final List<Version> version = pDoc.getVersionSupport().getVersionInfo().listVersions();
    if (version == null || version.size() == 0) {
      return;
    }

    int numVersions = version.size();
    if (maxVersions > numVersions) {
      maxVersions = numVersions;
    }

    int i = 0;
    final PManagerVersionSupport support = manager.getVersionSupport();
    try {
      for (Version cursor : version) {
        // check maximum number of versions to export
        if (i >= maxVersions)
          break;

        String name          = cursor.getName();
        long   versionNumber = cursor.getVersionNumber();

        final PDocument versioned = support.getDocument(context, DocumentName.create(name), versionNumber);
        result.put(cursor.getVersionNumber(), builder.parse(documentStream(versioned.read())));
      }
    }
    catch (MDSException e) {
      throw new MetadataException(e);
    }
    catch (SAXException e) {
      throw new MetadataException(Bundle.string(Bundle.CONTEXT_ERROR_GENERAL), e);
    }
    catch (IOException e) {
      throw new MetadataException(Bundle.string(Bundle.CONTEXT_ERROR_UNHANDLED), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   confirmOverride
  /**
   ** Aks user for confirmation to override an existing file.
   **
   ** @param  path               the pathname to the file to override.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  file               the name of the file to override.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>0</code> if the file should be override.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  private int confirmOverride(final String file, final String path) {
    return JOptionPane.showConfirmDialog(
      null
      , Bundle.format(Bundle.METADATA_DOCUMENT_CONFIRM_MESSAGE, path, file)
      , Bundle.string(Bundle.METADATA_DOCUMENT_CONFIRM_TITLE)
      , JOptionPane.YES_NO_OPTION
      , JOptionPane.QUESTION_MESSAGE
    );
  }
}