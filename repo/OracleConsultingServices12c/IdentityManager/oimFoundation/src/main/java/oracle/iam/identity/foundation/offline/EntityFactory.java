/*
    Oracle Consulting Services

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
    NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
    LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR
    ITS DERIVATIVES.

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Offline Resource Management

    File        :   EntityFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    EntityFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-02-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.offline;

import java.io.File;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// interface EntityFactory
// ~~~~~~~~~ ~~~~~~~~~~~~~
/**
** The factory creating the notification events processed by an
** {@link EntityListener}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface EntityFactory<E extends Entity> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String  PROLOG           = "<?xml version=\"1.0\" encoding=\"%s\"?>";
  static final String  NAMESPACE        = "http://www.oracle.com/schema/oim/offline";

  static final String ELEMENT_ATTRIBUTE = "attribute";

  static final String ATTRIBUTE_ID      = "id";
  static final String ATTRIBUTE_VALUE   = "value";
  static final String ATTRIBUTE_ACTION  = "action";
  static final String ATTRIBUTE_RISK    = "risk";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Unique
  // ~~~~ ~~~~~~
  /**
   ** Java class for unique.
   ** <p>
   ** The following schema fragment specifies the expected content contained
   ** within this class.
   ** <pre>
   ** &lt;simpleType name="unique"&gt;
   **   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
   **     &lt;enumeration value="ignore"/&gt;
   **     &lt;enumeration value="strong"/&gt;
   **     &lt;enumeration value="keepFirst"/&gt;
   **     &lt;enumeration value="keepLast"/&gt;
   **   &lt;/restriction&gt;
   ** &lt;/simpleType&gt;
   ** </pre>
   */
  public static enum Unique {

      IGNORE("ignore")
    , STRONG("strong")
    , FIRST("keepFirst")
    , LAST("keepLast")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:1735212126056643904")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String      value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a empty <code>Unique</code> that allows use as a JavaBean.
     **
     ** @param  value              the value.
     */
    Unique(final String value) {
      // initialize instance attributes
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the value property.
     **
     ** @return                    possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: fromValue
    /**
     ** Factory method to create a proper severity from the given string value.
     **
     ** @param  value            the string value the severity should be
     **                          returned for.
     **
     ** @return                  the severity.
     */
    public static Unique fromValue(final String value) {
      for (Unique cursor : Unique.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uniqueName
  /**
   ** Set the mode to enforce uniqueness.
   **
   ** @param  mode               the mode of {@link Unique} to set.
   */
  void uniqueName(final Unique mode);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uniqueName
  /**
   ** Return the mode to enforce uniqueness.
   **
   ** @return                    the mode of {@link Unique} used.
   */
  Unique uniqueName();

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate
  /**
   ** Factory method to create an {@link Entity} items from an {@link File}.
   ** <p>
   ** The {@link Entity} will be validate accordingly to the state of
   ** <code>validate</code>. If <code>validate</code> is set to
   ** <code>true</code> the entire XML {@link File} is validated before the
   ** real parsing process is started. If this step should be skipped
   ** <code>false</code> has to e passed as the value for <code>validate</code>.
   **
   ** @param  listener           the {@link EntityListener} that will process a
   **                            articular batch of entities populated from the
   **                            {@link File}.
   ** @param  file               the input source for the {@link Entity} items.
   **                            to reconcile by unmarshalling the {@link File}.
   ** @param  validate           <code>true</code> if the provided {@link File}
   **                            has to be validated.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  void populate(final EntityListener<E> listener, final File file, final boolean validate)
    throws TaskException;
}