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

    File        :   Ticker.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Ticker.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.cache;

////////////////////////////////////////////////////////////////////////////////
// interface Ticker
// ~~~~~~~~~ ~~~~~~
/**
 ** A time source that returns a time value representing the number of
 ** nanoseconds elapsed since some fixed but arbitrary point in time.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Ticker {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Default
  // ~~~~ ~~~~~~~
  /**
   ** A {@link Ticker} that reads the current time using
   ** {@link System#nanoTime}.
   */
  enum Default implements Ticker {

    INSTANCE;

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: read (Ticker)
    /**
     ** Returns the number of nanoseconds elapsed since this ticker's fixed
     ** point of reference.
     **
     ** @return                  the number of nanoseconds elapsed since this
     **                          ticker's fixed point of reference
     **                          <br>
     **                          Possible object is <code>long</code>.
     */
    @Override
    public long read() {
      return System.nanoTime();
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // enum Zero
  // ~~~~ ~~~~
  /**
   ** A {@link Ticker} that always returns <code>0</code>.
   */
  enum Zero implements Ticker {

    INSTANCE;

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: read (Ticker)
    /**
     ** Returns the number of nanoseconds elapsed since this ticker's fixed
     ** point of reference.
     **
     ** @return                  the number of nanoseconds elapsed since this
     **                          ticker's fixed point of reference
     **                          <br>
     **                          Possible object is <code>long</code>.
     */
    @Override
    public long read() {
      return 0L;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read
  /**
   ** Returns the number of nanoseconds elapsed since this ticker's fixed point
   ** of reference.
   **
   ** @return                    the number of nanoseconds elapsed since this
   **                            ticker's fixed point of reference
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  long read();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultTicker
  /**
   ** Returns a ticker that reads the current time using
   ** {@link System#nanoTime}.
   **
   ** @return                    a ticker that reads the current time using
   **                            {@link System#nanoTime}.
   **                            <br>
   **                            Possible object is {@link Ticker}.
   */
  static Ticker defaultTicker() {
    return Default.INSTANCE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   zeroTicker
  /**
   ** Returns a ticker that always returns <code>0</code>.
   **
   ** @return                    a ticker that always returns <code>0</code>.
   **                            <br>
   **                            Possible object is {@link Ticker}.
   */
  static Ticker zeroTicker() {
    return Zero.INSTANCE;
  }
}