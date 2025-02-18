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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   Lookup.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Lookup.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf.rest.request;

import java.util.Map;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.WebTarget;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.request.Request;

import oracle.iam.identity.icf.connector.pcf.rest.ExceptionParser;

import oracle.iam.identity.icf.connector.pcf.rest.domain.Result;

import oracle.iam.identity.icf.connector.pcf.rest.schema.Embed;
import oracle.iam.identity.icf.connector.pcf.rest.schema.Entity;
import oracle.iam.identity.icf.connector.pcf.rest.schema.Metadata;

////////////////////////////////////////////////////////////////////////////////
// class Lookup
// ~~~~~ ~~~~~~
/**
 ** A factory for Cloud Controller lookup requests.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Lookup extends Request<Lookup>  {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new Cloud Controller lookup request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   */
  private Lookup(final WebTarget target) {
    // ensure inheritance
    super(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a generic lookup resource request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   **
   ** @return                    a JAX-RS request.
   **                            <br>
   **                            Possible object is <code>Lookup</code>.
   */
  public static Lookup build(final WebTarget target) {
    return new Lookup(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the SCIM lookup request.
   **
   ** @param  <R>                the type of resource to return.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  clazz              the Java {@link Class} type used to determine
   **                            the type to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the successfully lookedup SCIM resource.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws SystemException    if the SCIM service provider responded with an
   **                            error.
   */
  public <R extends Result> R invoke(final Class<R> clazz)
    throws SystemException {

    try {
      // this is a hack to solve the unmarshalling of an entity if the
      // connector runs in the embedded Conncetor Server deployed in Identity
      // Manager
      // The native call of readEntity fails with an empty entity without any
      // exception; Aaaargh
      // the solution for the time being is to umarshall to an generic hash map
      // and set the minimal required properties like guid and name manually
      // return response.readEntity(clazz);
      final Map<String, Object> generic = invoke();
      final Map<String, Object> meta    = (Map<String, Object>)generic.get(Metadata.PREFIX);
      final Map<String, Object> entity  = (Map<String, Object>)generic.get(Embed.PREFIX);
      final R                   result  = clazz.newInstance();
      result.guid((String)meta.get(Metadata.ID));
      result.name((String)entity.get(Entity.ID));
      return result;
    }
    catch (IllegalAccessException e) {
      throw SystemException.abort(e);
    }
    catch (InstantiationException e) {
      throw SystemException.abort(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the SCIM lookup request.
   **
   ** @return                    the successfully lookedup SCIM resource.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is a {@link String} mapped to a
   **                            {@link Object}.
   **
   ** @throws SystemException    if the SCIM service provider responded with an
   **                            error.
   */
  public Map<String, Object> invoke()
    throws SystemException {

    Response response = buildRequest().get();
    try {
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        return response.readEntity(Map.class);
      }
      else {
        throw ExceptionParser.from(response);
      }
    }
    finally {
      response.close();
    }
  }
}