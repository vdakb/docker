package oracle.jdeveloper.connection.oim.model;


////////////////////////////////////////////////////////////////////////////////
// interface IdentityServiceConstant
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
public interface IdentityServerConstant {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Default value of the URL encoding.
   */
  static final String URL_ENCODING_DEFAULT         = "UTF-8";

  /** the system property name to manage JAAS login configurations */
  static final String JAVA_SECURITY_CONFIG         = "java.security.auth.login.config";

  /** the type name of a WebLogic Managed Server */
  static final String SERVER_TYPE_WEBLOGIC         = "weblogic";

  /** the type name of a WebSphere Server */
  static final String SERVER_TYPE_WEBSPHERE        = "websphere";

   /** the name of the system property to identify a WebLogic Managed Server */
  static final String SYSTEM_PROPERTY_WEBLOGIC     = "weblogic.Name";

  /** the name of the system property to identify a WebSphere Server */
  static final String SYSTEM_PROPERTY_WEBSPHERE    = "was.install.root";

  /** the system property name to manage connection timeout WebLogic Server */
  static final String TIMEOUT_WEBLOGIC_CONNECT     = "weblogic.ConnectTimeout";

  /** the protocol each WebLogic Managed Server is using */
  static final String PROTOCOL_WEBLOGIC_DEFAULT    = "t3";

  /** the protocol each WebLogic Managed Server is using over SSL */
  static final String PROTOCOL_WEBLOGIC_SECURE     = "t3s";

  /** the protocol each WebLogic Managed Server is using */
  static final String PROTOCOL_WEBSPHERE_DEFAULT   = "corbaname:iiop";

  /** the protocol each WebLogic Managed Server is using over SSL */
  static final String PROTOCOL_WEBSPHERE_SECURE    = "corbaname:iiops";

  /** the value to set to identify a WebLogic Managed Server */
  static final String SYSTEM_PROPERTY_value        = "system-property";
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the URL encoding.
   */
  static final String URL_ENCODING                 = "url-encoding";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the Initial Context factory.
   */
  static final String INITIAL_CONTEXT_FACTORY      = "context-factory";
}