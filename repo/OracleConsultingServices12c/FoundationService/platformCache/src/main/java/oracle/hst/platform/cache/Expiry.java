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

    File        :   Expiry.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Expiry.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.cache;

////////////////////////////////////////////////////////////////////////////////
// interface Expiry
// ~~~~~~~~~ ~~~~~~
/**
 ** Calculates when arbitrary entries expire.
 ** <br>
 ** A single expiration time is retained so that the lifetime of an entry may be
 ** extended or reduced by subsequent evaluations.
 **
 ** @param  <K>                  the most general key type this
 **                              <code>Expiry</code> will be able to compute.
 **                              This is normally {@link Object}.
 ** @param  <V>                  the most general value type this
 **                              <code>Expiry</code> will be able to compute.
 **                              This is normally {@link Object}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 **
 */
public interface Expiry<K extends Object, V extends Object> {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterCreate
  /**
   ** Specifies that the entry should be automatically removed from the cache
   ** once the duration has elapsed after the entry's creation. To indicate no
   ** expiration an entry may be given an excessively long period, such as
   ** {@link Long#MAX_VALUE}.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The <code>currentTime</code> is supplied by the configured {@link Ticker}
   ** and by default does not relate to system or wall-clock time. When
   ** calculating the duration based on a time stamp, the current time should be
   ** obtained independently.
   **
   ** @param  key                the key represented by this entry.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  value              the value represented by this entry.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  currentTime        the current time, in nanoseconds.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    the length of time before the entry expires,
   **                            in nanoseconds.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  long afterCreate(final K key, final V value, final long currentTime);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterUpdate
  /**
   ** Specifies that the entry should be automatically removed from the cache
   ** once the duration has elapsed after the replacement of its value. To
   ** indicate no expiration an entry may be given an excessively long period,
   ** such as {@link Long#MAX_VALUE}. The <code>duration</code> may be returned
   ** to not modify the expiration time.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The <code>currentTime</code> is supplied by the configured {@link Ticker}
   ** and by default does not relate to system or wall-clock time. When
   ** calculating the duration based on a time stamp, the current time should be
   ** obtained independently.
   **
   ** @param  key                the key represented by this entry.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  value              the value represented by this entry.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  duration           the duration, in nanoseconds.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    the length of time before the entry expires,
   **                            in nanoseconds.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  long afterUpdate(final K key, final V value, final long duration);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterFetch
  /**
   ** Specifies that the entry should be automatically removed from the cache
   ** once the duration has elapsed after its last fetch. To indicate no
   ** expiration an entry may be given an excessively long period, such as
   ** {@link Long#MAX_VALUE}. The <code>duration</code> may be returned to not
   ** modify the expiration time.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The <code>currentTime</code> is supplied by the configured {@link Ticker}
   ** and by default does not relate to system or wall-clock time. When
   ** calculating the duration based on a time stamp, the current time should be
   ** obtained independently.
   **
   ** @param  key                the key represented by this entry.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  value              the value represented by this entry.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  currentTime        the current time, in nanoseconds.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  duration           the duration, in nanoseconds.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    the length of time before the entry expires,
   **                            in nanoseconds.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  long afterFetch(final K key, final V value, final long currentTime, final long duration);
}