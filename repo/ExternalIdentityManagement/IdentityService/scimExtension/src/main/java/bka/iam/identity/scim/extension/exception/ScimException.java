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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   ScimException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the ScimException class.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-20-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.exception;

import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.AttributeValue;
import bka.iam.identity.scim.extension.model.MultiValueSimpleAttribute;
import bka.iam.identity.scim.extension.model.SingularSimpleAttribute;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.HTTPContext.StatusCode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.Response;
////////////////////////////////////////////////////////////////////////////////
// class ScimException
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The ScimException class represents SCIM-specific exceptions containing:
 ** HTTP status code, detail and type.
 ** 
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ScimException extends Exception implements Iterable<Attribute>{

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with

  @SuppressWarnings("compatibility:-847924797156734904")
  private static final long serialVersionUID = -1383710381876042188L;
  
  // Default SCIM error schema.
  public static final String SCHEMA   = "urn:ietf:params:scim:api:messages:2.0:Error";
  
  // Key for SCIM schema attributes.
  public static final String SCHEMAS  = "schemas";
  
  // Key for HTTP status attributes.
  public static final String STATUS   = "status";
  
  // Key for exception detail attributes.
  public static final String DETAIL   = "detail";
  
  // Key for exception scim type attributes.
  public static final String SCIMTYPE = "scimType";
  
  public enum ScimType {
    INVALID_FILTER("invalidFilter"),
    TOOMANY("tooMany"),
    UNIQUENESS("uniqueness"),
    MUTABILITY("mutability"),
    INVALID_SYNTAX("invalidSyntax"),
    INVALID_PATH("invalidPath"),
    NO_TARGET("noTarget"),
    INVALID_VALUE("invalidValue"),
    INVALID_VERS("invalidVers"),
    SENSITIVE("sensitive");
    
    private final String message;
    
    ScimType(String message) {
      this.message = message;
    }
    
    public String getMessage() {
      return toString();
    }
    
    public String toString() {
      return this.message;
    }
    
    public static ScimType getStatus(final String message) {
      for (ScimType type : values()) {
        if (type.message.equals(message))
          return type; 
      }
      return null;
    }
  } 
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  // HTTP status code associated with the exception
  private HTTPContext.StatusCode httpCode;

  // A list that holds all the attributes of the resource.
  private List<Attribute> attributes = new LinkedList<Attribute>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new <code>ScimException</code> by copying another
   ** <code>ScimException</code>.
   **
   ** @param  exception    The exception to copy.
   **                      Allowed object is {@link ScimException}.
   */
  public ScimException(final ScimException exception) {
    // ensure inheritance
    super();
    
    this.httpCode = exception.httpCode;
    this.attributes = new LinkedList<>(exception.attributes);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ScimException</code> from a http code and detail.
   **
   ** @param  httpCode     The HTTP status code.
   **                      Allowed object is {@link StatusCode}.
   ** @param  detail       The detailed message for the exception.
   **                      Allowed object is {@link String}.
   */
  public ScimException(final HTTPContext.StatusCode httpCode, final String detail) {
    // ensure inheritance
    super(detail);
    
    this.httpCode = httpCode;
    
    this.attributes.add(new MultiValueSimpleAttribute(SCHEMAS, new AttributeValue[] {new AttributeValue(SCHEMA)}));
    this.attributes.add(new SingularSimpleAttribute(DETAIL, new AttributeValue(detail)));
    this.attributes.add(new SingularSimpleAttribute(STATUS, new AttributeValue(String.valueOf(httpCode.getStatusCode()))));
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ScimException</code> from a http code, scimType and
   ** detail. As defined by the RFC76, this method must be use only if the
   ** ScimException raise an Bad Request (400).
   **
   ** @param  httpCode     The HTTP status code.
   **                      Allowed object is {@link StatusCode}.
   ** @param  scimType     The HTTP status code.
   **                      Allowed object is {@link StatusCode}.
   ** @param  detail       The detailed message for the exception.
   **                      Allowed object is {@link String}.
   */
  public ScimException(final HTTPContext.StatusCode httpCode, final ScimType scimType, final String detail) {
    // ensure inheritance
    super(detail);
    
    this.httpCode = httpCode;
    
    this.attributes.add(new MultiValueSimpleAttribute(SCHEMAS, new AttributeValue[] {new AttributeValue(SCHEMA)}));
    this.attributes.add(new SingularSimpleAttribute(DETAIL, new AttributeValue(detail)));
    this.attributes.add(new SingularSimpleAttribute(STATUS, new AttributeValue(String.valueOf(httpCode.getStatusCode()))));
    this.attributes.add(new SingularSimpleAttribute(SCIMTYPE, new AttributeValue(scimType.toString())));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ScimException</code> from a http code the causing
   ** exception.
   **
   ** @param  httpCode             the http code responce.
   ** @param  causing              the causing exception.
   */
  public ScimException(final StatusCode httpCode, final Throwable causing) {
    // ensure inheritance
    super(causing);
    
    this.httpCode = httpCode;
    
    final Attribute schema = new MultiValueSimpleAttribute(SCHEMAS, new AttributeValue[] {new AttributeValue(SCHEMA)});
    final String message = (causing.getCause() == null? causing.getLocalizedMessage():causing.getCause().getLocalizedMessage());
    final Attribute detail = new SingularSimpleAttribute(DETAIL, new AttributeValue((message != null?message:"null")));
    final Attribute status = new SingularSimpleAttribute(STATUS, new AttributeValue(httpCode.getStatusCode()));
    
    this.attributes.add(schema);
    this.attributes.add(detail);
    this.attributes.add(status);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ScimException</code> from a response.
   **
   ** @param  response             the {@link Response} containing the SCIM 
   **                              exception.
   **                              
   ** @throws IOException          if a low-level I/O problem (unexpected
   **                              end-of-input) occurs.
   */
  public ScimException(final Response response)
    throws IOException {
    // ensure inheritance
    super();
    
    this.httpCode = StatusCode.getStatus(response.getStatus());

    final JsonNode node =  new ObjectMapper().readTree(response.readEntity(String.class));
    
    this.attributes.add(new MultiValueSimpleAttribute(SCHEMAS, new AttributeValue[] {new AttributeValue(SCHEMA)}));
    this.attributes.add(new SingularSimpleAttribute(DETAIL, new AttributeValue(node.get(DETAIL).asText())));
    this.attributes.add(new SingularSimpleAttribute(STATUS, new AttributeValue(node.get(STATUS).asText())));
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Accessor method
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getHttpCode
  /**
   ** Returns the detail of this SCIM exception.
   **
   ** @return                      The HTTP status code.
   **                              Possible object is {@link StatusCode}.
   */
  public StatusCode getHttpCode() {
    return httpCode;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDetails
  /**
   ** Returns the detail of this SCIM exception.
   **
   ** @return                      the detail of the SCIM exception.
   */
  private String getAttribute(final String key) {
    for (Attribute attribute : attributes) {
      if (attribute.getName().equals(key)) {
        return attribute.getValue().getStringValue();
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDetails
  /**
   ** Returns the detail of this SCIM exception.
   **
   ** @return                      the detail of the SCIM exception.
   */
  public String getSchemas() {
    return getAttribute(SCHEMAS);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDetails
  /**
   ** Returns the detail message of this SCIM exception.
   **
   ** @return                      The detailed message.
   **                              Possible object is {@link String}.
   */
  public String getDetails() {
    return getAttribute(DETAIL);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getType
  /**
   ** Returns the type of this SCIM exception.
   **
   ** @return                      The type of the exception.
   **                              Possible object is {@link String}.
   */
  public String getScimType() {
    return getAttribute(SCIMTYPE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Implemented abstract method
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator
  /**
   ** Returns an iterator over ths SCIM exception attributes.
   **
   ** @return                      an {@link Iterator}.
   */
  @Override
  public Iterator<Attribute> iterator() {
    return attributes.iterator();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString
  /**
   ** Provides a JSON-like string representation of the exception.
   ** 
   ** @return                       A string representation of the SCIM error.
   **                               Possible object is {@link String}.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("{");
    final Iterator<Attribute> attributes = this.iterator();
    while (attributes.hasNext()) {
      Attribute attribute = attributes.next();
      builder.append(attribute);
      
      if (attributes.hasNext()) {
        builder.append(",");
      }
    }
    builder.append("}");
    
    return builder.toString();
  }
}
