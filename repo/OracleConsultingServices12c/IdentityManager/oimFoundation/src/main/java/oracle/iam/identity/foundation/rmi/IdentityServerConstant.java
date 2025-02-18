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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   IdentityServerConstant.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    IdentityServerConstant.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.rmi;

////////////////////////////////////////////////////////////////////////////////
// interface IdentityServerConstant
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
public interface IdentityServerConstant {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Default value of the separator sto specify multiple value for a
   ** configuration tag name.
   */
  static final String MULTIVALUE_SEPARATOR_DEFAULT = "|";

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

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the character that separates multiple values for the same entry
   ** tag name.
   */
  static final String MULTIVALUE_SEPARATOR         = "multi-value-separator";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify if the entitlement loaded from a Tivoli Server needs to be
   ** prefixed with the internal system identifier and/or the name of the
   ** <code>IT Resource</code>.
   */
  static final String ENTITLEMENT_PREFIX_REQUIRED  = "entitlement-prefix-required";
}