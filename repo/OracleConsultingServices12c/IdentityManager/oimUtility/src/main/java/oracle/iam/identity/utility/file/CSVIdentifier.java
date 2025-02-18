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

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Flatfile Facilities

    File        :   CSVIdentifier.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CSVIdentifier.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.List;
import java.util.ArrayList;

////////////////////////////////////////////////////////////////////////////////
// final class CSVIdentifier
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** This is the superclass of all object identifier classes.
 ** <br>
 ** An object identifier should identify an entity. It implements a kind of
 ** primary key for an object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public final class CSVIdentifier<CSVAttribute> extends ArrayList<CSVAttribute> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8351334724054574830")
  private static final long serialVersionUID = 6882324603816841761L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructor for <code>CSVIdentifier</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default constructor
   */
  public CSVIdentifier() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates an empty <code>CSVIdentifier</code> object with the specified
   ** initial capacity.
   **
   ** @param  initialCapacity    the capacity to initialize.
   */
  public CSVIdentifier(final int initialCapacity) {
    // ensure inheritance
    super(initialCapacity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new an <code>CSVIdentifier</code> using supplied attribute
   ** array.
   **
   ** @param  attributes         the attributes describing the identifier.
   */
  public CSVIdentifier(final List<CSVAttribute> attributes) {
    // ensure inheritance
    super(attributes);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttributes
  /**
   ** Returns the array containing the identifier attributes
   **
   ** @return                    the array containing the identifier attributes.
   */
  public List<CSVAttribute> getAttributes() {
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  //Method:   equals (overridden)
  /**
   ** Compares to this identifier with the given identifier for equality.
   **
   ** @param  other              the identifier to compare with.
   **
   ** @return                    <code>true</code> if the identifier are equal;
   **                            <code>false</code> otherwise.
   */
  public boolean equals(final Object other) {
    boolean equality = false;
    if (other instanceof CSVIdentifier) {
      final int size = size();
      @SuppressWarnings("unchecked")
      final CSVIdentifier<CSVAttribute> tmp = (CSVIdentifier<CSVAttribute>)other;
      for (int i = 0; i < size; i++) {
        equality = get(i).equals(tmp.get(i));
        if (!equality)
          break;
      }
    }
    return equality;
  }
}