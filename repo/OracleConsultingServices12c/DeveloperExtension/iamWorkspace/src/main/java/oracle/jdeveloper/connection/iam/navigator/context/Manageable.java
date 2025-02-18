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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   Manageable.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    Manageable.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator.context;

import java.util.List;

import java.io.Serializable;

//////////////////////////////////////////////////////////////////////////////
// interface Manageable
// ~~~~~~~~~ ~~~~~~~~~~
/**
 ** Tagging interface for a manageable object that can be exposed in a
 ** Navigator.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public interface Manageable extends Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface Composite
  // ~~~~~~~~~ ~~~~~~~~~
  /**
   ** Tagging interface
   */
  public interface Composite extends Manageable {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getManageables
    /**
     ** Returns the collection of {@link Manageable} attached to this composite.
     **
     ** @return                  the collection of {@link Manageable} attached
     **                          to this composite.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link Manageable}.
     */
    List<Manageable> getManageables();
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Refreshable
  // ~~~~~~~~~ ~~~~~~~~~~~
  /**
   ** Tagging interface for a refreshable manageable object that can be exposed
   ** in a Navigator.
   */
  public interface Refreshable extends Manageable {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: refresh
    /**
     ** Performs all action to refresh an element.
     */
    void refresh();
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Viewable
  // ~~~~~~~~~ ~~~~~~~~
  /**
   ** Tagging interface for a viewable manageable object that can be exposed
   ** in a Navigator.
   */
  public interface Viewable extends Manageable {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: view
    /**
     ** Performs all action to view an element.
     */
    void view();
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Removeable
  // ~~~~~~~~~ ~~~~~~~~~~
  /**
   ** Tagging interface for a removeable manageable object that can be exposed
   ** in a Navigator.
   ** <br>
   ** <b>Note</b>:
   ** The method delete (which is the primary intention) is block
   ** by Node implementation due to its declared final:
   */
  public interface Removeable extends Manageable {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: remove
    /**
     ** Performs all action to remove an element.
     */
    void remove();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // interface Modifiable
  // ~~~~~~~~~ ~~~~~~~~~~
  /**
   ** Tagging interface for a modifiable manageable object that can be exposed
   ** in a Navigator.
   */
  public interface Modifiable extends Manageable {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: modify
    /**
     ** Performs all action to modify an element.
     **
     ** @return                  <code>true</code> if the modify activity
     **                          succeeded; <code>false</code> if the modify
     **                          activity was canceled.
     **                          <br>
     **                          Possible object is <code>bolean</code>.
     */
    boolean modify();
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Searchable
  // ~~~~~~~~~ ~~~~~~~~~~
  /**
   ** Tagging interface for a searchable manageable object that can be exposed
   ** in a Navigator.
   */
  public interface Searchable extends Manageable {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: search
    /**
     ** Performs all action to search for elements.
     */
    void search();
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Exportable
  // ~~~~~~~~~ ~~~~~~~~~~
  /**
   ** Tagging interface for an manageable object that can be exported from a
   ** Navigator.
   */
  public interface Exportable extends Manageable {
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Importable
  // ~~~~~~~~~ ~~~~~~~~~~
  /**
   ** Tagging interface for an manageable object that can be imported into a
   ** Navigator.
   */
  public interface Importable extends Manageable {
  }
}