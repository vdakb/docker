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

    Copyright © 2013. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Analytics Extension Library
    Subsystem   :   Offline Target Connector

    File        :   ObjectFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ObjectFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2013-08-23  DSteding    First release version
*/

package oracle.iam.analytics.harvester.request.role;

import javax.xml.bind.annotation.XmlRegistry;

import oracle.iam.analytics.harvester.domain.role.Policy;
import oracle.iam.analytics.harvester.domain.role.Context;

////////////////////////////////////////////////////////////////////////////////
// class ObjectFactory
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This object contains factory methods for each Java content interface and
 ** Java element interface generated in the oracle.iam.analytics.harvester.domain
 ** package.
 ** <p>
 ** An ObjectFactory allows you to programatically construct new instances of
 ** the Java representation for XML content. The Java representation of XML
 ** content can consist of schema derived interfaces and classes representing
 ** the binding of schema type definitions, element declarations and model
 ** groups. Factory methods for each of these are provided in this class.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
@XmlRegistry
public class ObjectFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public final static String NAMESPACE = "http://service.api.oia.iam.ots/role";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ObjectFactory</code> that can be used to create new
   ** instances of schema derived classes for package:
   ** <code>oracle.iam.identity.ots.domain</code>
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public ObjectFactory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createExists
  /**
   ** Create an instance of {@link Exists}.
   **
   ** @return                    an instance of {@link Exists}.
   */
  public Exists createExists() {
    return new Exists();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createExport
  /**
   ** Create an instance of {@link Exists} with the specified identifier
   ** <code>id</code>.
   **
   ** @param  id                 the identifier of the {@link Exists}.
   **
   ** @return                    an instance of {@link Exists}.
   */
  public Exists createExists(final String id) {
    return new Exists(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createExistsResponse
  /**
   ** Create an instance of {@link ExistsResponse}.
   **
   ** @return                    an instance of {@link ExistsResponse}.
   */
  public ExistsResponse createExistsResponse() {
    return new ExistsResponse();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createExport
  /**
   ** Create an instance of {@link Export}.
   **
   ** @return                    an instance of {@link Export}.
   */
  public Export createExport() {
    return new Export();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createExport
  /**
   ** Create an instance of {@link Export} with the specified identifier
   ** <code>id</code>.
   **
   ** @param  id                 the identifier of the {@link Export}.
   **
   ** @return                    an instance of {@link Export}.
   */
  public Export createExport(final String id) {
    return new Export(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCreate
  /**
   ** Create an instance of {@link Create}.
   **
   ** @return                    an instance of {@link Create}.
   */
  public Create createCreate() {
    return new Create();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCreate
  /**
   ** Create an instance of {@link Create}.
   **
   ** @param  context            the transactional {@link Context} determining
   **                            the behavior of the servive.
   ** @param  policy             the {@link Policy} of this request.
   **
   ** @return                    an instance of {@link Create}.
   */
  public Create createCreate(final Context context, final Policy policy) {
    return new Create(context, policy);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCreateResponse
  /**
   ** Create an instance of {@link CreateResponse}.
   **
   ** @return                    an instance of {@link CreateResponse}.
   */
  public CreateResponse createCreateResponse() {
    return new CreateResponse();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createModify
  /**
   ** Create an instance of {@link Modify}
   **
   ** @return                    an instance of {@link Modify}.
   */
  public Modify createModify() {
    return new Modify();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createModify
  /**
   ** Create an instance of {@link Modify}.
   **
   ** @param  context            the transactional {@link Context} determining
   **                            the behavior of the servive.
   ** @param  policy             the {@link Policy} of this request.
   **
   ** @return                    an instance of {@link Modify}.
   */
  public Modify createModify(final Context context, final Policy policy) {
    return new Modify(context, policy);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createModifyResponse
  /**
   ** Create an instance of {@link ModifyResponse}.
   **
   ** @return                    an instance of {@link ModifyResponse}.
   */
  public ModifyResponse createModifyResponse() {
    return new ModifyResponse();
  }
}