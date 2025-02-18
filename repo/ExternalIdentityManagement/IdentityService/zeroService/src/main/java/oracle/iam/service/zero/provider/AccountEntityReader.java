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

    System      :   Identity Governance Extension
    Subsystem   :   Account Provisioning Service Model

    File        :   AccountEntityReader.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AccountEntityReader.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.service.zero.provider;

import java.io.IOException;
import java.io.InputStream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import oracle.hst.platform.rest.response.ErrorResponse;

import oracle.iam.identity.igs.model.AccountEntity;
import oracle.iam.identity.igs.model.Schema;

////////////////////////////////////////////////////////////////////////////////
// class AccountEntityReader
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Contract for a provider that supports the conversion of a stream to a
 ** Java type.
 ** <br>
 ** A {@link MessageBodyReader} implementation may be annotated with
 ** {@code Consumes} to restrict the media types for which it will be considered
 ** suitable.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class AccountEntityReader implements MessageBodyReader<AccountEntity> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccountEntityReader</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccountEntityReader() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isReadable (MessageBodyReader)
  /**
   ** Ascertain if the {@link MessageBodyReader} can produce an instance of a
   ** particular type. The type parameter gives the class of the instance that
   ** should be produced, the <code>genericType</code> parameter gives the
   ** <code>java.lang.reflect.Type</code> of the instance that should be
   ** produced.
   ** <br>
   ** E.g. if the instance to be produced is <code>List&lt;String&gt;</code>,
   ** the type parameter will be <code>java.util.List</code> and the
   ** <code>genericType</code> parameter will be
   ** <code>java.lang.reflect.ParameterizedType</code>.
   **
   ** @param  type               the class of instance to be produced.
   **                            <br>
   **                            Allowed object is {@link Class} of type any.
   ** @param  generic            the type of instance to be produced. E.g. if
   **                            the message body is to be converted into a
   **                            method parameter, this will be the formal type
   **                            of the method parameter as returned by
   **                            <code>Method.getGenericParameterTypes</code>.
   **                            <br>
   **                            Allowed object is {@link Type}.
   ** @param  annotation         an array of the annotations on the declaration
   **                            of the artifact that will be initialized with
   **                            the produced instance. E.g. if the message body
   **                            is to be converted into a method parameter,
   **                            this will be the annotations on that parameter
   **                            returned by
   **                            <code>Method.getParameterAnnotations</code>.
   **                            <br>
   **                            Allowed object is array of {@link Annotation}.
   ** @param  mediaType          the media type of the HTTP entity, if one is
   **                            not specified in the request then
   **                            application/octet-stream is used.
   **                            <br>
   **                            Allowed object is {@link MediaType}.
   **
   ** @return                    <code>true</code> if the type is supported,
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean isReadable(final Class<?> type, final Type generic, final Annotation[] annotation, final MediaType mediaType){
    return type.equals(AccountEntity.class) && mediaType.toString().equals(MediaType.APPLICATION_JSON);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readFrom (MessageBodyReader)
  /**
   ** In case the entity input stream is empty, the reader is expected to either
   ** return a Java representation of a zero-length entity or throw a
   ** <code>NoContentException</code> in case no zero-length entity
   ** representation is defined for the supported Java type.
   ** <br>
   ** A <code>NoContentException</code>, if thrown by a message body reader
   ** while reading a server request entity, is automatically translated by
   ** JAX-RS server runtime into a <code>BadRequestException</code> wrapping the
   ** original <code>NoContentException</code> and rethrown for a standard
   ** processing by the registered exception mappers.
   **
   ** @param  type               the class type is to be read from the entity
   **                            stream.
   **                            <br>
   **                            Allowed object is {@link Class} of type any.
   ** @param  generic            the type of instance to be produced. E.g. if
   **                            the message body is to be converted into a
   **                            method parameter, this will be the formal type
   **                            of the method parameter as returned by
   **                            <code>Method.getGenericParameterTypes</code>.
   **                            <br>
   **                            Allowed object is {@link Type}.
   ** @param  annotation         an array of the annotations on the declaration
   **                            of the artifact that will be initialized with
   **                            the produced instance. E.g. if the message body
   **                            is to be converted into a method parameter,
   **                            this will be the annotations on that parameter
   **                            returned by
   **                            <code>Method.getParameterAnnotations</code>.
   **                            <br>
   **                            Allowed object is array of {@link Annotation}.
   ** @param  mediaType          the media type of the HTTP entity, if one is
   **                            not specified in the request then
   **                            application/octet-stream is used.
   **                            <br>
   **                            Allowed object is {@link MediaType}.
   ** @param  header             the read-only HTTP headers associated with HTTP
   **                            entity.
   **                            <br>
   **                            Allowed object is {@link MultivaluedMap} where
   **                            each element is of type {@link String} for the
   **                            key and {@link String} as the value.
   ** @param  stream             the {@link InputStream} of the HTTP entity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            The caller is responsible for ensuring that the
   **                            input stream ends when the entity has been
   **                            consumed. The implementation should not close
   **                            the input stream.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    the type that was read from the stream.
   **                            <br>
   **                            In case the entity input stream is empty, the
   **                            reader is expected to either return an instance
   **                            representing a zero-length entity or throw a
   **                            <code>NoContentException</code> in case no
   **                            zero-length entity representation is defined
   **                            for the supported Java type.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws IOException             if an IO error arises.
   **                                 <br>
   **                                 In case the entity input stream is empty
   **                                 and the reader is not able to produce a
   **                                 Java representation for a zero-length
   **                                 entity, <code>NoContentException</code> is
   **                                 expected to be thrown.
   ** @throws WebApplicationException if a specific HTTP error response needs to
   **                                 be produced.
   **                                 <br>
   **                                 <b>Note</b>:
   **                                 <br>
   **                                 Only effective if thrown prior to the
   **                                 response being committed.
   */
  @Override
  public AccountEntity readFrom(final Class<AccountEntity> type, final Type generic, final Annotation[] annotation, final MediaType mediaType, final MultivaluedMap<String,String> header, final InputStream stream)
    throws IOException
    ,      WebApplicationException {

    try {
      return Schema.unmarshalAccount(stream);
    } catch (Exception e) {
      System.out.println("AccountEntityReader#readFrom(): Exception caught: " + e.getMessage());
      ErrorResponse response = new ErrorResponse(400);
      response.description(e.getMessage()).type("invalidValue").detail("Attribute is required");
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                     .entity(response.jsonify())
                     .build()
                );
    }
  }
}