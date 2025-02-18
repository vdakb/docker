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

    Copyright © 2016. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Text Stream Facilities

    File        :   FlatFileIdentifier.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FlatFileIdentifier.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.2.0      2016-10-15  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.List;
import java.util.ArrayList;

////////////////////////////////////////////////////////////////////////////////
// final class FlatFileIdentifier
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** This is the superclass of all object identifier classes.
 ** <br>
 ** An object identifier should identify an entity. It implements a kind of
 ** primary key for an object.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public final class FlatFileIdentifier<FlatFileAttribute> extends ArrayList<FlatFileAttribute> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1846371101693609525")
  private static final long serialVersionUID = 1908452710622732053L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructor for <code>FlatFileIdentifier</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default constructor
   */
  public FlatFileIdentifier() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates an empty <code>FlatFileIdentifier</code> object with the specified
   ** initial capacity.
   **
   ** @param  initialCapacity    the capacity to initialize.
   */
  public FlatFileIdentifier(final int initialCapacity) {
    // ensure inheritance
    super(initialCapacity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new an <code>FlatFileIdentifier</code> using supplied attribute
   ** array.
   **
   ** @param  attributes         the attributes describing the identifier.
   */
  public FlatFileIdentifier(final List<FlatFileAttribute> attributes) {
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
  public List<FlatFileAttribute> attributes() {
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
    if (other instanceof FlatFileIdentifier) {
      final int size = size();
      @SuppressWarnings("unchecked")
      final FlatFileIdentifier<FlatFileAttribute> tmp = (FlatFileIdentifier<FlatFileAttribute>)other;
      for (int i = 0; i < size; i++) {
        equality = get(i).equals(tmp.get(i));
        if (!equality)
          break;
      }
    }
    return equality;
  }
}