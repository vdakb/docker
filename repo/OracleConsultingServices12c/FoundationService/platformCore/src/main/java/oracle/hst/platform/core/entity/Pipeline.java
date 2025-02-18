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

    File        :   Pipeline.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Pipeline.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.entity;

////////////////////////////////////////////////////////////////////////////////
// final class Pipeline
// ~~~~~ ~~~~~ ~~~~~~~~
/**
 ** An interface that allows processing of data in a series of stages by giving
 ** in an initial input and passing the processed output to be used by the next
 ** stages.
 ** <p>
 ** The Pipeline pattern uses ordered stages to process a sequence of input
 ** values. Each implemented task is represented by a stage of the pipeline.
 ** You can think of pipelines as similar to assembly lines in a factory, where
 ** each item in the assembly line is constructed in stages. The partially
 ** assembled item is passed from one assembly stage to another. The outputs of
 ** the assembly line occur in the same order as that of the inputs.
 **
 ** @param  <I>                  the type of the input for the first stage
 **                              filter.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request payload
 **                              extending this class (requests can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **                              <br>
 **                              Allowed object is <code>&lt;I&gt;</code>.
 ** @param  <O>                  the type of the the final stage filter's
 **                              output type.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request payload
 **                              extending this class (requests can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **                              <br>
 **                              Allowed object is <code>&lt;O&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Pipeline<I, O> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Filter<I, O> current;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // interface Filter
  // ~~~~~~~~~ ~~~~~~
  /**
   ** The contract to all stage handlers to accept a certain type of input and
   ** return a processed output.
   **
   ** @param  <I>                the type of the input for the stage filter.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of the request payload
   **                            extending this class (requests can return their
   **                            own specific type instead of type defined by
   **                            this class only).
   **                            <br>
   **                            Allowed object is <code>&lt;I&gt;</code>.
   ** @param  <O>                the processed output type of the stage filter.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of the request payload
   **                            extending this class (requests can return their
   **                            own specific type instead of type defined by
   **                            this class only).
   **                            <br>
   **                            Allowed object is <code>&lt;O&gt;</code>.
   **
   */
  public static interface Filter<I, O> {

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply
    /**
     ** Execute current filter by the input.
     ** <br>
     ** Execute the next filter by current filter's output.
     **
     ** @param  input            the input value for the current filter.
     **                          <br>
     **                          Allowed object is <code>I</code>.
     **
     ** @return                  the next filter's output.
     */
    O apply(final I input);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Pipeline</code> with the {@link Filter} to be applied.
   **
   ** @param  current            the {@link Filter} to be applied.
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            <code>I</code> as the input and <code>O</code>
   **                            as the result.
   */
  public Pipeline(final Filter<I, O> current) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.current = current;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   then
  /**
   ** Create new pipeline by piping current and next filters.
   **
   ** @param  <K>                the processed output type of the stage filter.
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  filter             the next filter to apply.
   **                            <br>
   **                            Allowed object is <code>Filter</code>.
   **
   ** @return                    the <code>Pipeline</code> created.
   **                            <br>
   **                            Possible object is <code>Pipeline</code>.
   */
  public final <K> Pipeline<I, K> then(final Filter<O, K> filter) {
    return new Pipeline<>(input -> filter.apply(apply(input)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply
  /**
   ** Execute current filter by the input.
   **
   ** @param  input              the input value for the current filter.
   **                            <br>
   **                            Allowed object is <code>I</code>.
   **
   ** @return                    the result of the applied filter.
   **                            <br>
   **                            Possible object is <code>O</code>.
   */
  public final O apply(final I input) {
    return this.current.apply(input);
  }
}