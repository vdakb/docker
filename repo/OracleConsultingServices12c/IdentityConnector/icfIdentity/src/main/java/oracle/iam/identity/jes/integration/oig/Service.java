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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Identity Governance Connector

    File        :   Service.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Service.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-21  DSteding    First release version
*/

package oracle.iam.identity.jes.integration.oig;

import java.util.Set;
import java.util.Map;

import oracle.hst.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// interface Service
// ~~~~~~~~~ ~~~~~~
public interface Service {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface ICF
  // ~~~~~~~~~ ~~~
  interface ICF {

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // interface Resource
    // ~~~~~~~~~ ~~~~~~~~
    public interface Resource {

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the host name for the target system Service Provider.
       */
      static final String SERVER_HOST               = "serverHost";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the listener port for the target system Service Provider.
       */
      static final String SERVER_PORT               = "serverPort";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the name of the Managed Server of the target system Service
       ** Provider.
       */
      static final String SERVER_NAME               = "serverName";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the type of the Managed Server of the target system Service
       ** Provider.
       */
      static final String SERVER_TYPE              = "serverType";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify if you plan to configure SSL to secure communication between
       ** Identity Manager and the target system.
       */
      static final String ROOT_CONTEXT              = "rootContext";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify if you plan to configure SSL to secure communication between
       ** Identity Manager and the target system.
       */
      static final String SECURE_SOCKET             = "secureSocket";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the user name of the target system platform account to be used
       ** to establish a connection.
       */
      static final String DOMAIN_PRINCIPAL          = "domainPrincipal";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the password of the target system platform account specified
       ** by the #DOMAIN_PRINCIPAL parameter.
       */
      static final String DOMAIN_PASSWORD           = "domainPassword";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the user name of the target system account to be used to
       ** establish a connection.
       */
      static final String PRINCIPAL_NAME            = "principalUsername";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the password of the target system account specified by the
       ** #PRINCIPAL_NAME parameter.
       */
      static final String PRINCIPAL_PASSWORD        = "principalPassword";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the path the login configuration of the Managed Server used to
       ** authenticate the target system account specified by the
       ** #PRINCIPAL_NAME parameter.
       */
      static final String SECURITY_CONFIG           = "loginConfig";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** hold the name of language the server is using.
       */
      static final String LOCALE_LANGUAGE           = "language";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** hold the name of language region the server is using.
       */
      static final String LOCALE_COUNTRY            = "country";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** hold the name of time zone the server is using.
       */
      static final String LOCALE_TIMEZONE           = "timeZone";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specifiy the time (in milliseconds) within which the target system is
       ** expected to respond to a connection attempt.
       */
      static final String CONNECTION_TIMEOUT        = "connectionTimeOut";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specifiy the number of consecutive attempts to be made at establishing
       ** a connection with the target system.
       */
      static final String CONNECTION_RETRY_COUNT    = "connectionRetryCount";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specifiy the interval (in milliseconds) between consecutive attempts
       ** at establishing a connection with the target system.
       */
      static final String CONNECTION_RETRY_INTERVAL = "connectionRetryInterval";
      /**
       ** Attribute tag which may be defined on a <code>IT Resource</code>
       ** to specify the timeout period the Service Provider consumer doesn't
       ** get a HTTP response.
       ** <p>
       ** If this property has not been specified, the default is to wait for the
       ** response until it is received.
       */
      static final String RESPONSE_TIMEOUT          = "responseTimeOut";
    }

    ////////////////////////////////////////////////////////////////////////////
    // interface Feature
    // ~~~~~~~~~ ~~~~~~~~
    public interface Feature {

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface OIM
  // ~~~~~~~~~ ~~~
  interface OIM {

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // interface Resource
    // ~~~~~~~~~ ~~~~~~~~
    public interface Resource {

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

    }

    ////////////////////////////////////////////////////////////////////////////
    // interface Feature
    // ~~~~~~~~~ ~~~~~~~~
    interface Feature {

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

    }
  }
  /**
   ** The attribute tags of the feature definition in the
   ** <code>Metadata Descriptor</code>.
   */
  static final Set<String> PROPERTY = CollectionUtility.unmodifiableSet(
  );
  /**
   ** The attribute mapping of the feature transfer from the
   ** <code>Metadata Descriptor</code> to
   ** <code>Identity Governance Connector</code> configuration.
   */
  static final Map<String, String> FEATURE = CollectionUtility.unmodifiableMap(
    new String[][]{
    }
  );
  /**
   ** The attribute mapping of the feature transfer from the
   ** <code>IT Resource</code> to <code>Identity Governance Connector</code>
   ** configuration.
   */
  static final Map<String, String> RESOURCE = CollectionUtility.synchronizedMap(
    new String[][]{
      {ICF.Resource.SERVER_HOST,               ServiceResource.SERVER_HOST}
    , {ICF.Resource.SERVER_PORT,               ServiceResource.SERVER_PORT}
    , {ICF.Resource.SERVER_NAME,               ServiceResource.SERVER_NAME}
    , {ICF.Resource.SERVER_TYPE,               ServiceResource.SERVER_TYPE}
    , {ICF.Resource.ROOT_CONTEXT,              ServiceResource.ROOT_CONTEXT}
    , {ICF.Resource.SECURE_SOCKET,             ServiceResource.SECURE_SOCKET}
    , {ICF.Resource.PRINCIPAL_NAME,            ServiceResource.PRINCIPAL_NAME}
    , {ICF.Resource.PRINCIPAL_PASSWORD,        ServiceResource.PRINCIPAL_PASSWORD}
    , {ICF.Resource.DOMAIN_PRINCIPAL,          ServiceResource.DOMAIN_PRINCIPAL}
    , {ICF.Resource.DOMAIN_PASSWORD,           ServiceResource.DOMAIN_PASSWORD}
    , {ICF.Resource.SECURITY_CONFIG,           ServiceResource.SECURITY_CONFIG}
    , {ICF.Resource.LOCALE_LANGUAGE,           ServiceResource.LOCALE_LANGUAGE}
    , {ICF.Resource.LOCALE_COUNTRY,            ServiceResource.LOCALE_COUNTRY}
    , {ICF.Resource.LOCALE_TIMEZONE,           ServiceResource.LOCALE_TIMEZONE}
    , {ICF.Resource.CONNECTION_TIMEOUT,        ServiceResource.CONNECTION_TIMEOUT}
    , {ICF.Resource.CONNECTION_RETRY_COUNT,    ServiceResource.CONNECTION_RETRY_COUNT}
    , {ICF.Resource.CONNECTION_RETRY_INTERVAL, ServiceResource.CONNECTION_RETRY_INTERVAL}
    , {ICF.Resource.RESPONSE_TIMEOUT,          ServiceResource.RESPONSE_TIMEOUT}
    }
  );
}