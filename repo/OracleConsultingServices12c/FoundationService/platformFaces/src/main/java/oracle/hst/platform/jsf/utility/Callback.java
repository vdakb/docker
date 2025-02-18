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
    Subsystem   :   Java Server Faces Feature

    File        :   Callback.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Callback.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jsf.utility;

import java.io.IOException;
import java.io.Serializable;
import java.io.OutputStream;

////////////////////////////////////////////////////////////////////////////////
// interface Callback
// ~~~~~~~~~ ~~~~~~~~
/**
 ** Collection of callback interfaces.
 ** <br>
 ** Useful in visitor and strategy patterns.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Callback {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface Output
  // ~~~~~~~~~ ~~~~~~
  /**
   ** An output stream callback.
	 */
  @FunctionalInterface
	public interface Output {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: invoke
    /**
     ** This method should be invoked by the method where you're passing this
     ** callback instance to.
     **
     ** @param  output           the callback output stream to write to.
     **                          <br>
     **                          Allowed object is {@link OutputStream}.
     **
     ** @throws IOException      if given output stream cannot be written.
		 */
		void invoke(final OutputStream output)
      throws IOException;
	}

  //////////////////////////////////////////////////////////////////////////////
  // interface Void
  // ~~~~~~~~~ ~~~~
  /**
   ** A void callback.
   */
  @FunctionalInterface
  public interface Void {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: invoke
    /**
     ** This method should be invoked by the method where you're passing this
     ** callback instance to.
     */
    void invoke();
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface VoidSerializable
  // ~~~~~~~~~ ~~~~~~~~~~~~~~~~
  /**
   ** A serializable void callback.
	 */
	@FunctionalInterface
	public interface VoidSerializable extends Serializable {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: invoke
    /**
     ** This method should be invoked by the method where you're passing this
     ** callback instance to.
     */
    void invoke();
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Returning
  // ~~~~~~~~~ ~~~~~~~~~
  /**
   ** UA callback which returns a value.
   **
   ** @param  <R>                the expected return type.
   **                            <br>
   **                            Allowed object is <code>R</code>.
	 */
	@FunctionalInterface
	public interface Returning<R> {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: invoke
    /**
     ** This method should be invoked by the method where you're passing this
     ** callback instance to.
     **
     ** @return                  the callback result.
     */
    R invoke();
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface ReturningSerializable
  // ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~
  /**
   ** A serializable callback which returns a value.
   **
   ** @param  <R>                the expected return type.
   **                            <br>
   **                            Allowed object is <code>R</code>.
	 */
	@FunctionalInterface
	public interface ReturningSerializable<R> extends Serializable {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: invoke
    /**
     ** This method should be invoked by the method where you're passing this
     ** callback instance to.
     **
     ** @return                  the callback result.
     **                          <br>
     **                          Possible object is <code>R</code>.
     */
    R invoke();
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Parameterized
  // ~~~~~~~~~ ~~~~~~~~~~~~~
  /**
   ** A callback which takes a parameter.
   **
	 ** @param  <P>                the expected parameter type.
   **                            <br>
   **                            Allowed object is <code>&lt;P&lt;</code>.
	 */
	@FunctionalInterface
	public interface Parameterized<P> {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: invoke
    /**
     ** This method should be invoked by the method where you're passing this
     ** callback instance to.
     **
     ** @param  value            the parameter to pass to the callback.
     **                          <br>
     **                          Allowed objec is <code>P</code>.
     */
    void invoke(final P value);
	}

  //////////////////////////////////////////////////////////////////////////////
  // interface ParameterizedSerializable
  // ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
  /**
   ** A serializable callback which takes a parameter.
   **
	 ** @param  <P>                the expected parameter type.
   **                            <br>
   **                            Allowed object is <code>&lt;P&lt;</code>.
	 */
	@FunctionalInterface
	public interface ParameterizedSerializable<P> extends Serializable {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: invoke
    /**
     ** This method should be invoked by the method where you're passing this
     ** callback instance to.
     **
     ** @param  value            the parameter to pass to the callback.
     **                          <br>
     **                          Allowed objec is <code>P</code>.
     */
    void invoke(final P value);
	}

  //////////////////////////////////////////////////////////////////////////////
  // interface ParameterizedReturning
  // ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
  /**
   ** A callback which takes a parameter and returns a value.
   **
   ** @param  <R>                the expected return type.
   **                            <br>
   **                            Allowed object is <code>R</code>.
	 ** @param  <P>                the expected parameter type.
   **                            <br>
   **                            Allowed object is <code>&lt;P&lt;</code>.
	 */
	@FunctionalInterface
	public interface ParameterizedReturning<R, P> {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: invoke
    /**
     ** This method should be invoked by the method where you're passing this
     ** callback instance to.
     **
     ** @param  value            the parameter to pass to the callback.
     **                          <br>
     **                          Allowed objec is <code>P</code>.
     **
     ** @return                  the callback result.
     **                          <br>
     **                          Possible object is <code>R</code>.
     */
    R invoke(final P value);
	}

  //////////////////////////////////////////////////////////////////////////////
  // interface ParameterizedReturning
  // ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
  /**
   ** A callback which takes a parameter and returns a value.
   **
   ** @param  <R>                the expected return type.
   **                            <br>
   **                            Allowed object is <code>R</code>.
	 ** @param  <P>                the expected parameter type.
   **                            <br>
   **                            Allowed object is <code>&lt;P&lt;</code>.
	 */
	@FunctionalInterface
	public interface ParameterizedReturningSerializable<R, P> extends Serializable {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: invoke
    /**
     ** This method should be invoked by the method where you're passing this
     ** callback instance to.
     **
     ** @param  value            the parameter to pass to the callback.
     **                          <br>
     **                          Allowed objec is <code>P</code>.
     **
     ** @return                  the callback result.
     **                          <br>
     **                          Possible object is <code>R</code>.
     */
    R invoke(final P value);
	}
}