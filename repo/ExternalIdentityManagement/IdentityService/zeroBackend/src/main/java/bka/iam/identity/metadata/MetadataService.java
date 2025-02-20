package bka.iam.identity.metadata;


import bka.iam.identity.zero.api.AppSchemaFacade;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import java.io.Reader;

import oracle.hst.platform.core.logging.Logger;

import oracle.mds.config.MDSConfig;
import oracle.mds.config.PConfig;
import oracle.mds.core.IsolationLevel;
import oracle.mds.core.MDSInstance;
import oracle.mds.core.MDSSession;
import oracle.mds.core.SessionOptions;
import oracle.mds.naming.DocumentName;
import oracle.mds.persistence.MetadataStore;
import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.PManager;
import oracle.mds.persistence.stores.db.DBMetadataStore;

import org.xml.sax.InputSource;

public class MetadataService {
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  private static final String                     category = MetadataService.class.getName();
  private static final Logger                     logger   = Logger.create(category);
  
  public static final String                      NAME     = "MetadataService";

  //////////////////////////////////////////////////////////////////////////////
  // non-static final attributes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////
  public MetadataService() {
    super();
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
  
  public static InputStream documentStream(final InputSource source)
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
      throw new MetadataException("Unhandled exception", e);
    }
    finally {
      close(source.getCharacterStream());
      close(stream);
    }
    return new ByteArrayInputStream(bos.toByteArray());
  }
  
  public static MDSSession getMdsSession(String metadataRepoName, String jdbcDataSource, String partition) {
    final String method = "retrieveFile";
    logger.entering(category, method);
    
    MDSSession session = null;
    try {
      MDSConfig config = null;
      // create a metadata store that leverage a database
      final MetadataStore store = new DBMetadataStore(jdbcDataSource, false, true, partition);
      config = new MDSConfig(null, new PConfig(store), null);

      MDSInstance instance = MDSInstance.getOrCreateInstance(metadataRepoName, config);
      logger.debug(category, method, "MDS OIM instance: " + instance);

      final SessionOptions option = new SessionOptions(IsolationLevel.READ_COMMITTED, null, null);
      // create a session to the Metadata Store using the session options and
      // without any specific state handlers
      session = instance.createSession(option, null);
    } catch (Exception e) {
      logger.throwing(category, method, e);
    }
    return session;
  }
  
  public static PDocument retrieveFile(MDSSession session, String filePath) {
    final String method = "retrieveFile";
    logger.entering(category, method);
    
    PDocument document = null;
    try {
      PManager manager = session.getPersistenceManager();
      document = manager.getDocument(session.getPContext(), DocumentName.create(filePath));
    } catch (Exception e) {
      logger.throwing(category, method, e);
    }
    logger.debug(category, method, "Returning document: " + document);
    return document;
  }
}
