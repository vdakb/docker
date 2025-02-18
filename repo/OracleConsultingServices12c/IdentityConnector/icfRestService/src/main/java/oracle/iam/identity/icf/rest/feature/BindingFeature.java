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
    Subsystem   :   Generic REST Library

    File        :   BindingFeature.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    BindingFeature.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest.feature;

import java.lang.reflect.Type;

import java.lang.annotation.Annotation;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

///////////////////////////////////////////////////////////////////////////////
// class BindingFeature
// ~~~~~ ~~~~~~~~~~~~~~
public class BindingFeature extends JacksonJaxbJsonProvider {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>BindingFeature</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public BindingFeature() {
    // ensure inheritance
    this(MapperFactory.instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>BindingFeature</code> to customize the
   ** default Jersey runtime binding strategy with the specified Jackson
   ** {@link ObjectMapper}.
   **
   ** @param  mapper             the runtime binding strategy to register.
   **                            <br>
   **                            Allowed object is {@link ObjectMapper}.
   */
  public BindingFeature(final ObjectMapper mapper) {
    // ensure inheritance
    super();

    // propagate configuration
    setMapper(mapper);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isReadable (overridden)
  /**
   ** Method that JAX-RS container calls to try to check whether values of given
   ** type (and media type) can be deserialized by this provider.
   ** <br>
   ** Implementation will first check that expected media type is a JSON type
   ** (via call to hasMatchingMediaType(javax.ws.rs.core.MediaType)); then
   ** verify that type is not one of "untouchable" types (types we will never
   ** automatically handle), and finally that there is a deserializer for type
   ** (if checkCanDeserialize(boolean) has been called with true argument --
   ** otherwise assumption is there will be a handler).
   **
   ** @param  clazz              the Java class object used to determine the
   **                            deserialization capabilities
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>?</code>.
   ** @param  type               the implementing {@link Type} to determine the
   **                            deserialization capabilities.
   **                            <br>
   **                            Allowed object is {@link Type}.
   ** @param  annotation         the {@link Annotation} for {@link Class}
   **                            <code>clazz</code> to determine the
   **                            deserialization capabilities.
   **                            <br>
   **                            Allowed object is array of {@link Annotation}.
   ** @param  media              the {@link MediaType} received to determine the
   **                            deserialization capabilities.
   **                            <br>
   **                            Allowed object is {@link MediaType}.
   **
   ** @return                    <code>true</code> if deserialization is
   **                            applicable.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  @Override
  public boolean isReadable(final Class<?> clazz, final Type type, final Annotation[] annotation, final MediaType media) {
    return providable(clazz) && super.isReadable(clazz, type, annotation, media);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isWriteable (overridden)
  /**
   ** Method that JAX-RS container calls to try to check whether given value (of
   ** specified type) can be serialized by this provider.
   ** <br>
   ** Implementation will first check that expected media type is expected one
   ** (by call to {@link #hasMatchingMediaType}); then verify that type is not
   ** one of "untouchable" types (types we will never automatically handle), and
   ** finally that there is a serializer for type (if {@link #checkCanSerialize}
   ** has been called with true argument -- otherwise assumption is there will
   ** be a handler)
   **
   ** @param  clazz              the Java class object used to determine the
   **                            serialization capabilities
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>?</code>.
   ** @param  type               the implementing {@link Type} to determine the
   **                            serialization capabilities.
   **                            <br>
   **                            Allowed object is {@link Type}.
   ** @param  annotation         the {@link Annotation} for {@link Class}
   **                            <code>clazz</code> to determine the
   **                            serialization capabilities.
   **                            <br>
   **                            Allowed object is array of {@link Annotation}.
   ** @param  media              the {@link MediaType} received to determine the
   **                            serialization capabilities.
   **                            <br>
   **                            Allowed object is {@link MediaType}.
   **
   ** @return                    <code>true</code> if serialization is
   **                            applicable.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  @Override
  public boolean isWriteable(final Class<?> clazz, final Type type, final Annotation[] annotation, final MediaType media) {
    return providable(clazz) && super.isWriteable(clazz, type, annotation, media);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providable (overridden)
  /**
   ** Determines if the provided call is annotate as ingnorable.
   **
   ** @param  clazz              the Java class object used to determine the
   **                            type to verify.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>?</code>.
   */
  private boolean providable(final Class<?> clazz) {
    final JsonIgnoreType ignore = clazz.getAnnotation(JsonIgnoreType.class);
    return (ignore == null) || !ignore.value();
  }
}