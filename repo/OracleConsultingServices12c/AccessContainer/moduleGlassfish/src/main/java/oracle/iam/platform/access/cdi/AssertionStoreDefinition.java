/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Authentication

    File        :   AssertionStoreDefinition.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the annotation
                    AssertionStoreDefinition.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-03-11  DSteding    First release version
*/

package oracle.iam.platform.access.cdi;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

////////////////////////////////////////////////////////////////////////////////
// annotation AssertionStoreDefinition
// ~~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Used to define an {@link AssertionStore} along the
 ** {@link AssertionMechanism} with an annotation.
 ** <br>
 ** Supports connecting to the database validation service.
 ** <br>
 ** You must provide a JDBC Datasource for this service
 ** <br>
 ** The configurable options are:
 ** <ol>
 **   <li>igs.verify.realm       - the name of realm that will be sent via the
 **                                <code>WWW-Authenticate</code> header.
 **                                <br>
 **                                Required property.
 **   <li>igs.verify.issuer      - the issuer identifier to enable clients to
 **                                validate the iss parameter effectively.
 **                                <br>
 **                                Required property.
 **   <li>igs.oauth.tenant       - the tenant identifier issued when the
 **                                application
 **                                was registered.
 **                                <br>
 **                                Optional property.
 **   <li>igs.oauth.client       - the public identifier for application.
 **                                <br>
 **                                Optional property.
 **   <li>igs.oauth.secret       - the secret known only to the application and
 **                                the authorization server.
 **                                <br>It is essential the application’s own
 **                                password.
 **                                <br>
 **                                Optional property.
 **   <li>igs.oauth.infoEndpoint - the URL for the OAuth2 Authentication
 **                                Provider to provide authentication.
 **                                <br>
 **                                Optional property.
 **   <li>igs.signing.key        - the PEM encoded certificate of the signing
 **                                authority.
 **                                <br>
 **                                Optional property.
 **   <li>igs.signing.url        - the location of the <code>PublicKey</code> of
 **                                the signing authority.
 **                                <br>
 **                                Optional property.
 **   <li>igs.jndi.datasource    - the JNDI name of the JDBC DataSource used for
 **                                authentication and authotization purpose.
 **                                <br>
 **                                Required property.
 **   <li>igs.query.principal    - the query used to authenticate users based on
 **                                specific key types.
 **                                <br>
 **                                Required property.
 **   <li>igs.query.permission   - the query used to authorize users based on
 **                                specific key types.
 **                                <br>
 **                                Required property.
 ** </ol>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Target(value=ElementType.TYPE)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface AssertionStoreDefinition {
}