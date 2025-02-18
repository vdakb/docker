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

    System      :   Presistence Foundation Shared Library
    Subsystem   :   Generic Persistence Interface

    File        :   PersistenceLifeCycle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    PersistenceLifeCycle.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa;

import java.io.Serializable;

import java.lang.annotation.Annotation;

import java.lang.reflect.Proxy;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.Collections;

import java.util.Objects;

import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.BeanManager;

import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PostPersist;

////////////////////////////////////////////////////////////////////////////////
// class PersistenceLifeCycle
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** This is by default already registered on {@link PersistenceEntity}.
 ** <br>
 ** It will fire CDI events {@link Created}, {@link Updated} and {@link Deleted}
 ** which can {@code Observes} an entity.
 ** <p>
 ** Usage example:
 ** <pre>
 **   public void onCreate(&#64;Observes &#64;Created AnEntity entity) {
 **     // ...
 **   }
 **
 **   public void onUpdate(&#64;Observes &#64;Updated AnEntity entity) {
 **     // ...
 **   }
 **
 **   public void onDelete(&#64;Observes &#64;Deleted AnEntity entity) {
 **     // ...
 **   }
 ** </pre>
 **
 ** @see Created
 ** @see Updated
 ** @see Deleted
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PersistenceLifeCycle {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

	private final Optional<BeanManager> optional;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Annotator
  // ~~~~~ ~~~~~~~~~
  /**
   ** A {@link InvocationHandler} implementation that implements the base
   ** methods required for an annotation.
   */
  private static class Annotator implements InvocationHandler {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    private static final Object[]             NOARG   = new Object[0];

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

	  private final Class<? extends Annotation> type;
	  private final Map<String, Object>         attribute;    
  
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Annotator</code> for the specified annotation
     ** <code>type</code> and optional <code>attribute</code>s.
     ** @param  type             the annotation type.
     **                          <br>
     **                          Allowed object is array of {@link Class} for
     **                          type {@link Annotation}.
   	 ** @param  attribute        the annotation attributes.
     **                          May be a partial attribute map or even an empty
     **                          map.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element is of type {@link String} for the key
     **                          and any as the value.
     */
    private Annotator(final Class<? extends Annotation> type, final Map<String, ?> attribute) {
      // ensure inheritance
      super();

      // intitialize instance attributes
      this.type      = type;
      // clone the attribute mapping
      this.attribute = new HashMap<>(attribute);
  		for (Method method : type.getDeclaredMethods()) {
	  		this.attribute.putIfAbsent(method.getName(), method.getDefaultValue());
		  }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: invoke (InvocationHandler)
    /**
     ** Processes a method invocation on a proxy instance and returns the
     ** result.
     ** <br>
     ** This method will be invoked when a method is invoked on a proxy instance
     ** that it is associated with.
     **
     ** @param  proxy            the proxy instance that the method was invoked
     **                          on.
     **                          <br>
     **                          Allowed object is {@link Object}.
     ** @param  method           the {@link Method} instance corresponding to
     **                          the interface method invoked on the proxy
     **                          instance.
     **                          <br>
     **                          The declaringclass of the {@link Method} object
     **                          will be the interface that the method was
     **                          declared in, which may be a superinterface of
     **                          the proxy interface that the proxy class
     **                          inherits the method through.
     **                          <br>
     **                          Allowed object is {@link Method}.
     ** @param  argument         an array of objects containing the values of
     **                          the arguments passed in the method invocation
     **                          on the proxy instance, or <code>null</code> if
     **                          interface method takes no arguments.
     **                          <br>
     **                          Arguments of primitive types are wrapped in
     **                          instances of the appropriate primitive wrapper
     **                          class, such as {@link java.lang.Integer} or
     **                          {@link java.lang.Boolean}.
     **                          <br>
     **                          Allowed object is array of {@link Object}.
     **
     ** @return                  the value to return from the method invocation
     **                          on the proxy instance.
     **                          <br>
     **                          If the declared return type of the interface
     **                          method is a primitive type, then the value
     **                          returned by this method must be an instance of
     **                          the corresponding primitive wrapper class;
     **                          otherwise, it must be a type assignable to the
     **                          declared return type.
     **                          <br>
     **                          If the value returned by this method is
     **                          <code>null</code> and the interface method's
     **                          return type is primitive, then a
     **                          {@link NullPointerException} will be thrown by
     **                          the method invocation on the proxy instance.
     **                          <br>
     **                          If the value returned by this method is
     **                          otherwise not compatible with the interface
     **                          method's declared return type as described
     **                          above, a {@link ClassCastException} will be
     **                          thrown by the method invocation on the proxy
     **                          instance.
     **
     ** @throws Throwable        the exception to throw from the method
     **                          invocation on the proxy instance.
     **                          <br>
     **                          The exception's type must be assignable either
     **                          to any of the exception types declared in the
     **                          <code>throws</code> clause of the interface
     **                          method or to the unchecked exception types
     **                          {@link java.lang.RuntimeException} or
     **                          {@link java.lang.Error}.
     **                          <br>
     **                          If a checked exception is thrown by this method
     **                          that is not assignable to any of the exception
     **                          types declared in the <code>throws</code>
     **                          clause of the interface method, then an
     **                          {@link UndeclaredThrowableException} containing
     **                          the exception that was thrown by this method
     **                          will be thrown by the method invocation on the
     **                          proxy instance.
     */
    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args)
      throws Throwable {

      switch (method.getName()) {
        case "annotationType" : return this.type;
        case "equals"         : return args.length > 0 && equals(proxy, args[0]);
        case "hashCode"       : return hashCode();
        case "toString"       : return toString();
        default               : return this.attribute.get(method.getName());
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals
    public boolean equals(final Object proxy, final Object other) {
      if (this.type.isInstance(other)) {
        try {
          Method[] methods = type.getDeclaredMethods();
          if (methods.length == this.attribute.size()) {
            for (Method method : methods) {
              if (!Objects.deepEquals(invoke(proxy, method, NOARG), method.invoke(other))) {
                return false;
              }
            }
            return true;
          }
        }
        catch (Throwable ignore) {
          // intentionally left blank
          ;
        }
      }
      return false;
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PersistenceLifeCycle</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PersistenceLifeCycle() {
    // ensure inheritance
    super();

    // initialize instance attributes
    BeanManager manager = null;
    try {
      // work around for CDI inject not working in JPA EntityLifeCycle
      // (as observed in OpenJPA).
      manager = CDI.current().getBeanManager();
    }
    catch (IllegalStateException ignore) {
      // can happen when actually not in CDI environment, e.g. local unit
      // test.
      manager = null;
    }
    this.optional = Optional.ofNullable(manager);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterCreate
  /**
   ** A callback method for the corresponding lifecycle event.
   ** <br>
   ** This annotation may be applied to methods of an entity class, a mapped
   ** superclass, or a callback listener class.
   **
   ** @param  <I>                the type of the identifiying value
   **                            implementation.
   **                            <br>
   **                            Allowed object is <code>&lt;I&gt;</code>.
   ** @param  entity             the entity the event occured.
   **                            <br>
   **                            Allowed object is <code>I</code>.
   */
	@PostPersist
	public <I extends Comparable<I> & Serializable> void afterCreate(final I entity) {
		notify(entity, Created.class);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterUpdate
  /**
   ** A callback method for the corresponding lifecycle event.
   ** <br>
   ** This annotation may be applied to methods of an entity class, a mapped
   ** superclass, or a callback listener class.
   **
   ** @param  <I>                the type of the identifiying value
   **                            implementation.
   **                            <br>
   **                            Allowed object is <code>&lt;I&gt;</code>.
   ** @param  entity             the entity the event occured.
   **                            <br>
   **                            Allowed object is <code>I</code>.
   */
	@PostUpdate
	public <I extends Comparable<I> & Serializable> void afterUpdate(final I entity) {
		notify(entity, Updated.class);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterDelete
  /**
   ** A callback method for the corresponding lifecycle event.
   ** <br>
   ** This annotation may be applied to methods of an entity class, a mapped
   ** superclass, or a callback listener class.
   **
   ** @param  <I>                the type of the identifiying value
   **                            implementation.
   **                            <br>
   **                            Allowed object is <code>&lt;I&gt;</code>.
   ** @param  entity             the entity the event occured.
   **                            <br>
   **                            Allowed object is <code>I</code>.
   */
	@PostRemove
	public <I extends Comparable<I> & Serializable> void afterDelete(final I entity) {
		notify(entity, Deleted.class);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notify
  /**
   ** Fire an event and notify observers.
   **
   ** @param  <I>                the type of the identifiying value
   **                            implementation.
   **                            <br>
   **                            Allowed object is <code>&lt;I&gt;</code>.
   ** @param  entity             the entity the event occured.
   **                            <br>
   **                            Allowed object is <code>I</code>.
   ** @param  type               the event qualifier.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            {@link Annotation}.
   */
	private <I extends Comparable<I> & Serializable> void notify(final I entity, final Class<? extends Annotation> type) {
		this.optional.ifPresent(cdi -> cdi.fireEvent(entity, annotate(type)));
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   annotate
	/**
	 ** Create an array of instances of the specified annotation types with
   ** default attributes.
   ** <br>
   ** Useful for varargs calls such as
   ** <code>CDI.current().select(type, createAnnotationInstances(Qualifier1.class, Qualifier2.class))</code>.
   **
   ** @param  <A>                the generic annotation type.
   **                            <br>
   **                            Allowed object is <code>&lt;A&gt;</code>.
   ** @param  type               the annotation type.
   **                            <br>
   **                            Allowed object is array of {@link Class} for
   **                            type <code>A</code>.
   **
   ** @return                    an array of instances of the specified
   **                            annotation types with default attributes.
   **                            <br>
   **                            Allowed object is array of {@link Annotation}
   **                            of type <code>A</code>.
   */
  @SafeVarargs
  @SuppressWarnings({"unchecked", "oracle.jdeveloper.java.null-array-return"})
  public static <A extends Annotation> A[] annotate(final Class<A>... type) {
    if (type == null)
      return null;

    final A[] instances = (A[])Array.newInstance(Annotation.class, type.length);
    for (int i = 0; i < type.length; i++) {
      instances[i] = annotate(type[i], Collections.<String, Object>emptyMap());
    }
    return instances;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   annotate
	/**
	 ** Create an instance of the specified annotation type with given attributes.
   **
   ** @param  <A>                the generic annotation type.
   **                            <br>
   **                            Allowed object is <code>&lt;A&gt;</code>.
   ** @param  type               the annotation type.
   **                            <br>
   **                            Allowed object is array of {@link Class} for
   **                            type <code>A</code>.
	 ** @param  attribute          the annotation attributes.
   **                            May be a partial attribute map or even an empty
   **                            map.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and any as the value.
   **
   ** @return                    an of instances of the specified annotation
   **                            type with attributes provided by
   **                            <code>attribute</code>.
   **                            <br>
   **                            Allowed object is array of {@link Annotation}
   **                            of type <code>A</code>.
   */
	public static <A extends Annotation> A annotate(final Class<A> type, final Map<String, ?> attribute) {
		InvocationHandler handler = new Annotator(type, attribute);
		return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler));
	}
}