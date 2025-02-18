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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Utility Facility

    File        :   BinarySearchTree.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    BinarySearchTree.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.SimpleTimeZone;

////////////////////////////////////////////////////////////////////////////////
// final class GreenwichMeanTimeZone
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** This class is a way to patch up your time and date code to allow proper
 ** handling of daylight time (aka, "summer time" or "daylight savings time") in
 ** the GMT meridian.
 ** <p>
 ** In Java 1.1.x versions only 3-letter time zone ID's were handled, but after
 ** release 1.1.6, longer ID's such as "Europe/London" could be handled by using
 ** SimpleDateFormat and specifying a custom time zone. That works but is hard
 ** to generalize, that is, internationalize, since you would have to write your
 ** own way to handle machines in the GMT meridian.
 ** <p>
 ** The data for several hundred time zones is present in releases as far back
 ** as 1.1.6, in java.util.TimeZoneData. Use code like what is in this class to
 ** access it. You will find that java.text.DateFormat (and subclasses) for
 ** releases 1.1.x do not fully and consistently produce the date strings you
 ** expect. For example, using en_IE (Ireland) you only get the formatter to
 ** print "IST" in summer time if the specified time zone is "Europe/London".
 ** That is, specifying "Europe/Dublin" doesn't get you that "IST" in the date
 ** output. On the hand you can live with the actual output for "Europe/Dublin"
 ** which is "GMT+01:00" in summer time and GMT in winter.
 ** <p>
 ** This class just overrides the three static methods in the java.util.TimeZone
 ** class. To implement the fix, make this class available and replace calls to
 ** <code>TimeZone.getTimeZone()</code> with
 ** <code>GreenwichMeanTimeZone.getTimeZone()</code>. That is, change "TimeZone"
 ** to "GMTZone". You can simply replace all references to java.util.TimeZone
 ** with references to GMTZone for all locales. This update of GMTZone also
 ** overrides the two getAvailableIDs() methods so your added zone ID's can be
 ** "user-selectable" at runtime.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class GreenwichMeanTimeZone extends SimpleTimeZone {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:9140671183883100264")
  private static final long serialVersionUID = 2602588156583294034L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  // Dummy constructor.  Works around lack of default constructor in SimpleTimeZone.
  private GreenwichMeanTimeZone() {
    super(0, "GreenwichMeanTimeZone");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeZone
  /**
   ** You can add other "more than 3-letter zone ID's" if you wish by just
   ** adding additional "else if" code to this method, and maybe renaming the
   ** class. This code is "forwards compatible" with Java 2 so the long time
   ** zone IDs supported in JDK 1.2 will be honored when users upgrade.
   ** <br>
   ** Overrides - getTimeZone() in class java.util.TimeZone
   **
   ** @param  ID                 the ID for a {@link TimeZone}, either an
   **                            abbreviation such as "PST", a full name such as
   **                            "America/Los_Angeles", or a custom ID such as
   **                            "GMT-8:00". Note that the support of
   **                            abbreviations is for JDK 1.1.x compatibility
   **                            only and full names should be used.
   **
   ** @return                    the specified {@link TimeZone}, or the GMT
   **                            zone if the given <code>ID</code> cannot be
   **                            understood.
   */
  public static synchronized TimeZone timeZone(final String ID) {
    if (ID.length() <= 3 && !ID.equals("WET"))
      return TimeZone.getTimeZone(ID);
    else if (ID.equals("Europe/London") || ID.equals("Europe/Belfast") || ID.equals("Europe/Dublin") || ID.equals("Europe/Lisbon") || ID.equals("Atlantic/Canary") || ID.equals("Atlantic/Faeroe") || ID.equals("Atlantic/Madeira") || ID.equals("WET")) {
      return new SimpleTimeZone(0, ID, Calendar.MARCH, -1, Calendar.SUNDAY, 1000 * 60 * 60 * 1, Calendar.OCTOBER, -1, Calendar.SUNDAY, 1000 * 60 * 60 * 1);
    }
    else
      return TimeZone.getTimeZone(ID);
  }
  // Programming note:  The data for long TimeZone ID's is stored in
  // java.util.TimeZoneData, an internal (package private) class which can only
  // be called by methods in the java.util.* package. TimeZoneData is just a
  // list of constructors for SimpleTimeZone (as written in JDK 1.1.6 source
  // code). There is a look-up method that finds a constructor based on the ID
  // String passed in. The method in java.util.TimeZone that returns a TimeZone
  // object is getTimeZone(), and, sure enough, in 1.1.x releases it is set up
  // to pass only 3-letter time zone ID's to the get() method in TimeZoneData.
  // Hence, I concluded we have to override getTimeZone() if we want to use long
  // ID's in one or more zones before Sun officially lets us do so in all zones.
  // This is admittedly a hack, but for the implementation of daylight "summer"
  // time in the countries that use GMT in winter, it represents a good and
  // valid fix, IMHO. This means of getting "GMT/BST", "GMT/IST", or "WET/WE%sT"
  // functionality can be implemented by anyone worldwide who is writing code
  // that will run in the UK, Ireland, Portugal, etc. The same code will work
  // "normally" everywhere else, whether the client is running 1.1.x or 1.2.
  // Of course, eventually, someone may want to remove the call to GMTZone, say
  // in 3 or 4 years when no one is using JDK 1.1.x code anymore.
  // So annotate your code accordingly. You needn't fear that TimeZoneData might
  // be changed: it isn't used here. And java.util.resources, a package of
  // locale data that is used internally to get strings for the formatter, is
  // likely only to be expanded, not trashed. And changes to that package will
  // be transparent to this class, although they might affect the output of your
  // date and time formatters.
  //
  // There is a way to implement any time zone you want, including long ID's if
  // you choose, and including any formatting symbols you choose. That is to
  // subclass SimpleTimeZone to create the new ID, and then use SimpleDateFormat
  // to replace or add to the zoneStrings[][] array maintained in
  // DateFormatSymbols. All this trouble does *not* get you a substitute
  // TimeZone class that simply adds your chosen zone and formatting symbols to
  // all the others already available. You end up instead with a "personal" time
  // zone available only if you instantiate the personal SimpleTimeZone
  // subclass.
  //
  // And a minor note on daylight savings (summer) time. The API now reflects
  // that ending time should be coded as the standard or non-daylight time, so
  // using 1 * 1000 * 60 * 60 in the constructor means we change from BST to GMT
  // at 1:00 GMT. (I had this wrong for a year.)

  //////////////////////////////////////////////////////////////////////////////
  // Method:   availableIDs
  /**
   ** Adds the additional supported time zone ID's to the list of all available
   ** ID's.
   **
   ** @return                    an array of IDs, where the time zone for that
   **                            ID has the specified GMT offset. For example,
   **                            "America/Phoenix" and "America/Denver" both
   **                            have GMT-07:00, but differ in daylight saving
   **                            behavior.
   */
  public static synchronized String[] availableIDs() {
    String[] zoneIDs = TimeZone.getAvailableIDs();
    int len = zoneIDs.length;
    String[] gmtZoneIDs = new String[len + 8];
    System.arraycopy(zoneIDs, 0, gmtZoneIDs, 0, len);
    String[] temp = {
      "Europe/London"
    , "Europe/Belfast"
    , "Europe/Dublin"
    , "Europe/Lisbon"
    , "Atlantic/Canary"
    , "Atlantic/Faeroe"
    , "Atlantic/Madeira"
    , "WET"
    };
    System.arraycopy(temp, 0, gmtZoneIDs, len, 8);
    return gmtZoneIDs;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   availableIDs
  /**
   ** Replaces the list of supported time zone ID's at zero offset from GMT.
   **
   ** @param  rawOffset          the given time zone GMT offset in milliseconds.
   **
   ** @return                    an array of IDs, where the time zone for that
   **                            ID has the specified GMT offset. For example,
   **                            "America/Phoenix" and "America/Denver" both
   **                            have GMT-07:00, but differ in daylight saving
   **                            behavior.
   */
  public static synchronized String[] availableIDs(int rawOffset) {
    if (rawOffset == 0) {
      String[] result = {
        "GMT"
      , "UTC"
      , "Europe/London"
      , "Europe/Belfast"
      , "Europe/Dublin"
      , "Europe/Lisbon"
      , "Atlantic/Canary"
      , "Atlantic/Faeroe"
      , "Atlantic/Madeira"
      , "WET"
      };
      return result;
    }
    else
      return TimeZone.getAvailableIDs(rawOffset);
  }
}
