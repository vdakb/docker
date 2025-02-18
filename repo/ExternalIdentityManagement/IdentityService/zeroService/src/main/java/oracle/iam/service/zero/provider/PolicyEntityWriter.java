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

    File        :   PolicyEntityWriter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com

    Purpose     :   This file implements the class
                    PolicyEntityWriter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-06-30  afarkas     First release version
*/

package oracle.iam.service.zero.provider;

import java.io.OutputStream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import oracle.iam.identity.igs.model.ApplicationEntity;
import oracle.iam.identity.igs.model.PolicyEntity;
import oracle.iam.identity.igs.model.Schema;

////////////////////////////////////////////////////////////////////////////////
// class ApplicationEntityWriter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Contract for a provider that supports the conversion of a Java type to a
 ** stream.
 ** <br>
 ** A {@link MessageBodyWriter} implementation may be annotated with
 ** {@code Produces} to restrict the media types for which it will be considered
 ** suitable.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class PolicyEntityWriter implements MessageBodyWriter<PolicyEntity> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ApplicationEntityWriter</code> allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PolicyEntityWriter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  isWriteable (MessageBodyWriter)
  /**
   ** Ascertain if the {@link MessageBodyWriter} supports a particular type.
   **
   ** @param  type               the class of instance to be written.
   **                            <br>
   **                            Allowed object is {@link Class} of type any.
   ** @param  generic            the type of instance to be written, obtained
   **                            either by reflection of a resource method
   **                            return type or via inspection of the returned
   **                            instance.
   **                            <br>
   **                            {@code GenericEntity} provides a way to specify
   **                            this information at runtime.
   **                            <br>
   **                            Allowed object is {@link Type}.
   ** @param  annotation         an array of the annotations on the declaration
   **                            of the artifact that will be initialized with
   **                            the instance to be written.
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
  public boolean isWriteable(final Class<?> type, final Type generic, final Annotation[] annotation, final MediaType mediaType) {
    return type.equals(PolicyEntity.class) && mediaType.toString().equals(MediaType.APPLICATION_JSON);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  getSize (MessageBodyWriter)
  /**
   ** Originally, the method has been called before <code>writeTo</code> to
   ** ascertain the length in bytes of the serialized form of t.
   ** <br>
   ** A non-negative return value has been used in a HTTP Content-Length header.
   ** <p>
   ** As of JAX-RS 2.0, the method has been deprecated and the value returned by
   ** the method is ignored by a JAX-RS runtime.
   ** <br>
   ** All {@link MessageBodyWriter} implementations are advised to return -1
   ** from the method. Responsibility to compute the actual Content-Length
   ** header value has been delegated to JAX-RS runtime.
   **
   ** @param  object             the entity instance to write.
   **                            <br>
   **                            Possible object is {@link ApplicationEntity}.
   ** @param  type               the class of instance to be written.
   **                            <br>
   **                            Allowed object is {@link Class} of type any.
   ** @param  generic            the type of instance to be written, obtained
   **                            either by reflection of a resource method
   **                            return type or via inspection of the returned
   **                            instance.
   **                            <br>
   **                            {@code GenericEntity} provides a way to specify
   **                            this information at runtime.
   **                            <br>
   **                            Allowed object is {@link Type}.
   ** @param  annotation         an array of the annotations on the declaration
   **                            of the artifact that will be initialized with
   **                            the instance to be written.
   **                            <br>
   **                            Allowed object is array of {@link Annotation}.
   ** @param  mediaType          the media type of the HTTP entity, if one is
   **                            not specified in the request then
   **                            application/octet-stream is used.
   **                            <br>
   **                            Allowed object is {@link MediaType}.
   **
   ** @return                    the length in bytes or <code>-1</code> if the
   **                            length cannot be determined in advance.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  @Override
  public long getSize(final PolicyEntity object, final Class<?> type, final Type generic, final Annotation[] annotation, final MediaType mediaType) {
    return -1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  writeTo (MessageBodyWriter)
  /**
   ** Write a type to an HTTP message.
   ** <br>
   ** The message header map is mutable but any changes must be made before
   ** writing to the output stream since the headers will be flushed prior to
   ** writing the message body.
   **
   ** @param  entity             the instance to write.
   **                            <br>
   **                            Possible object is {@link ApplicationEntity}.
   ** @param  type               the class of instance to be written.
   **                            <br>
   **                            Allowed object is {@link Class} of type any.
   ** @param  generic            the type of instance to be written, obtained
   **                            either by reflection of a resource method
   **                            return type or via inspection of the returned
   **                            instance.
   **                            <br>
   **                            {@code GenericEntity} provides a way to specify
   **                            this information at runtime.
   **                            <br>
   **                            Allowed object is {@link Type}.
   ** @param  annotation         an array of the annotations on the declaration
   **                            of the artifact that will be initialized with
   **                            the instance to be written.
   **                            <br>
   **                            Allowed object is array of {@link Annotation}.
   ** @param  mediaType          the media type of the HTTP entity, if one is
   **                            not specified in the request then
   **                            application/octet-stream is used.
   **                            <br>
   **                            Allowed object is {@link MediaType}.
   ** @param  stream             the {@link OutputStream} for the HTTP entity.
   **                            The implementation should not close the output
   **                            stream.
   **                            <br>
   **                            Allowed object is {@link OutputStream}.
   */
  @Override
  public void writeTo(final PolicyEntity entity, final Class<?> type, final Type generic, final Annotation[] annotation, final MediaType mediaType, final MultivaluedMap<String, Object> header, final OutputStream stream) {
    Schema.marshalPolicy(entity, stream);
  }
}