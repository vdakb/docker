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

    System      :   Foundation Service Extension
    Subsystem   :   Common Shared Utility

    File        :   Converter.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Converter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.entity;

import java.util.List;
import java.util.Collection;

import java.util.stream.Collectors;

import java.util.function.Function;

////////////////////////////////////////////////////////////////////////////////
// interface Converter
// ~~~~~~~~~ ~~~~~~~~~
/**
 ** An interface that converts entity representations into their corresponding
 ** target entitiy representation and vice versa.
 ** <p>
 ** The purpose of the Converter pattern is to provide a generic, common way of
 ** bidirectional conversion between corresponding types, allowing a clean
 ** implementation in which the types do not need to be aware of each other.
 ** Moreover, the Converter pattern introduces bidirectional collection mapping,
 ** reducing a boilerplate code to minimum.
 ** <p>
 ** In real world applications it is often the case that database layer consists
 ** of entities that need to be mapped into DTO's for use on the business logic
 ** layer. Similar mapping is done for potentially huge amount of classes and we
 ** need a generic way to achieve this.
 ** <p>
 ** Converter pattern makes it easy to map instances of one class into instances
 ** of another class.
 **
 ** @param  <T>                  the type of the source entity representations.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request payload
 **                              extending this class (requests can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 ** @param  <R>                  the type of the target entity representations.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request payload
 **                              extending this class (requests can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **                              <br>
 **                              Allowed object is <code>&lt;R&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Converter<T, R> {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // abstract class Type
  // ~~~~~~~~ ~~~~~ ~~~~
  /**
   ** The core implementation of a converter for the specified types.
   **
   ** @param  <T>                the type of the source entity representations.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of the request payload
   **                            extending this class (requests can return their
   **                            own specific type instead of type defined by
   **                            this class only).
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  <R>                the type of the target entity representations.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of the request payload
   **                            extending this class (requests can return their
   **                            own specific type instead of type defined by
   **                            this class only).
   **                            <br>
   **                            Allowed object is <code>R</code>.
   */
  static abstract class Type<T, R> implements Converter<T, R> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Function<R, T> inbound;
    private final Function<T, R> outbound;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Type</code> converter with the {@link Function}s to
     ** convert source entities to target entities and vice versa.
     **
     ** @param  outbound         the {@link Function} to convert a source entity
     **                          representation to its target entity
     **                          representation.
     **                          <br>
     **                          Allowed object is {@link Function} for type
     **                          <code>T</code> as the source and <code>R</code>
     **                          as the result.
     ** @param  inbound          the {@link Function} to convert target entity
     **                          representation to its source entity
     **                          representation.
     **                          <br>
     **                          Allowed object is {@link Function} for type
     **                          <code>R</code> as the source and <code>T</code>
     **                          as the result.
     */
    protected Type(final Function<T, R> outbound, final Function<R, T> inbound) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.inbound  = inbound;
      this.outbound = outbound;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: outbound
    /**
     ** Converts the specified source entity representation to its
     ** correspondending target entity representation.
     **
     ** @param  source           the source entity representation to convert.
     **                          <br>
     **                          Allowed object is <code>T</code>.
     **
     ** @return                  the converted target entity representation.
     **                          <br>
     **                          Possible object is <code>R</code>.
     */
    public final R outbound(final T source) {
      return this.outbound.apply(source);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: inbound
    /**
     ** Converts the specified target entity representation to its
     ** correspondending source entity representation.
     **
     ** @param  target           the terget entity representation to convert.
     **                          <br>
     **                          Allowed object is <code>R</code>.
     **
     ** @return                  the converted target entity representation.
     **                          <br>
     **                          Possible object is <code>T</code>.
     */
    public final T inbound(final R target) {
      return this.inbound.apply(target);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   outbound
  /**
   ** Converts the specified source entity representation to its
   ** correspondending target entity representation.
   **
   ** @param  source             the source entity representation to convert.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the converted target entity representation.
   **                            <br>
   **                            Possible object is <code>R</code>.
   */
  R outbound(final T source);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inbound
  /**
   ** Converts the specified target entity representation to its
   ** correspondending source entity representation.
   **
   ** @param  target             the terget entity representation to convert.
   **                            <br>
   **                            Allowed object is <code>R</code>.
   **
   ** @return                    the converted target entity representation.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  T inbound(final R target);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   outbound
  /**
   ** Factory method to create a {@link Collection} of target entity
   ** representations and populating their values from the {@link List} of
   ** source entity representations.
   **
   ** @param  source             the {@link List} of source entity
   **                            representations providing the data to convert.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    the {@link Collection} of target entity
   **                            representations populated from the {@link List}
   **                            source entity representations.
   **                            <br>
   **                            Possilble object is {@link Collection} where
   **                            each element is of type <code>R</code>.
   */
  default Collection<R> outbound(final List<T> source) {
    return source == null ? null : source.stream().map(this::outbound).collect(Collectors.toList());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inbound
  /**
   ** Factory method to create a {@link Collection} of source entity
   ** representations and populating their values from the {@link List} of
   ** target entity representations.
   **
   ** @param  target             the {@link List} of target entity
   **                            representations providing the data to convert.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>R</code>.
   **
   ** @return                    the {@link Collection} of source resource
   **                            representations populated from the {@link List}
   **                            target entity representations.
   **                            <br>
   **                            Possilble object is {@link Collection} where
   **                            each element is of type <code>R</code>.
   */
  default Collection<T> inbound (final List<R> target) {
    return target == null ? null : target.stream().map(this::inbound).collect(Collectors.toList());
  }
}