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

    File        :   FederationPartnerType.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FederationPartnerType.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import oracle.hst.deployment.ServiceAction;

////////////////////////////////////////////////////////////////////////////////
// enum FederationPartnerType
// ~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>FederationPartnerType</code> defines specific parameter type
 ** declarations regarding <code>Federation Partner</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum FederationPartnerType implements ServiceAction {
    OAM("agent-webgate",                        "Agent", "WebGate")
  , OSSO("agent-osso",                          "Agent", "OSSO")
  , OPENSSO("agent-opensso",                    "Agent", "OpenSSO")

   /**
    ** Oracle Identity Federation enables business partners to achieve a
    ** federated environment by providing the mechanism with which companies can
    ** form a federation and securely share services and data across their
    ** respective security domains.
    */
  , FEDSP("federation-service-provider",        "Federation", "ServiceProvider")
  , FEDIDP("federation-identity-provider",      "Federation", "IdentityProvider")

   /**
    ** Oracle Secure Token Service (STS) facilitates secure communications
    ** between web services providers and consumers.
    ** <p>
    ** In STS terminology, a web service provider is a
    ** <code>Relying Party</code>, while a web service consumer is a
    ** <code>Requester</code>. It works as a central hub where web services
    ** consumers come and get security tokens that are further propagated to web
    ** services providers. In this way, it frees clients from all the necessary
    ** infrastructure to generate tokens required for identity propagation.
    ** Whenever the client needs a token, it requests one from STS.
    */
  , STSISSUER("tokenservice-issuing-authority", "STS", "IssuingAuthority")
  , STSRELYING("tokenservice-relying-party",    "STS", "RelyingParty")
  , STSREQUESTER("tokenservice-requester",      "STS", "Requester")
  ;


  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String   COMPONENT        = "DeployedComponent";

  /**
   ** operation executed to determine the status of
   ** <code>Federation Partner</code>s.
   */
  static final String   STATUS           = "retrieveMapKeySet";

  /**
   ** Java method signature passed to the MBean Server to find the appropriate
   ** MBean interface to report a <code>Federation Partner</code> status.
   */
  static final String[] SIGNATURE_STATUS = {
    String.class.getName() //  0: location
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final String          id;
  final String          flavor;
  final String          path;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FederationPartnerType</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  FederationPartnerType(final String id, final String flavor, final String path) {
    this.id     = id;
    this.flavor = flavor;
    this.path   = path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   flavor
  /**
   ** Returns the flavor of the property.
   **
   ** @return                    the flavor of the property.
   */
  public String flavor() {
    return this.flavor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Returns the path of the property.
   **
   ** @return                    the path of the property.
   */
  public String path() {
    return this.path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Evaluates the location to the configuration item from the top root element
   ** until the <code>item</code>.
   **
   ** @return                    path to the configuration item from the top
   **                            .root element
   */
  public String location() {
    return String.format("/%s/%s/%s/Instance", COMPONENT, this.flavor, this.path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id (FeatureProperty)
  /**
   ** Returns the id of the property.
   **
   ** @return                    the id of the property.
   */
  @Override
  public String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a proper federation partner type from the given
   ** string value.
   **
   ** @param  value              the string value the access server service
   **                            should be returned for.
   **
   ** @return                    the federation partner type.
   */
  public static FederationPartnerType from(final String value) {
    for (FederationPartnerType cursor : FederationPartnerType.values()) {
      if (cursor.id.equals(value))
        return cursor;
    }
    throw new IllegalArgumentException(value);
  }
}