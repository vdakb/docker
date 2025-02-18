/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Unique Identifier Administration

    File        :   Navigator.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Navigator.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.uid.state;

import javax.inject.Named;

import javax.faces.view.ViewScoped;

import oracle.hst.platform.jsf.state.ManagedBean;

////////////////////////////////////////////////////////////////////////////////
// class Navigator
// ~~~~~ ~~~~~~~~~
/**
 ** <code>Navigator</code> is the pluggablity mechanism for allowing
 ** implementations of or applications using the Jakarta Server Faces
 ** specification to provide their own handling of the activities in the Render
 ** Response and Restore View phases of the request processing lifecycle.
 ** <br>
 ** This allows for implementations to support different response generation
 ** technologies, as well as alternative strategies for saving and restoring the
 ** state of each view.
 ** <p>
 ** The implementation of is <b>thread-safe</b>.
 ** <p>
 ** Java Server Faces (JSF) is a very useful server-side rendering framework to
 ** create HTML UI in Java projects. It's part of the Java EE specifications.
 ** One of the drawbacks of Java Server Faces is the generated URLs for our
 ** pages, they follow as pattern the structure of the folders in the project.
 ** <p>
 ** From a UIX perspective, having URL's like
 ** <code>/pages/user/user-list.xhml</code> looks ugly, it's better to have an
 ** URL like <code>/users</code>, <code>/user/new</code>. Also, from a security
 ** perspective, we are exposing our folder's structure to other people, and
 ** they can use that information to find a vulnerability.
 ** <p>
 ** That's why there are some third party libraries for JSF like pretty-faces,
 ** that allows to use friendly and nice URL's in our JSF application. However,
 ** they usually offer a bunch of functionalities (patterns, mapping path
 ** parameter, and more) that are not necessary in some of the applications.
 ** Therefore, we have created a very simple rewrite URL for JSF and avoid
 ** overload the application with things that not needed.
 ** <br>
 ** There is only one existing instance of the class in a JVM; it is implemented
 ** as singleton.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Named
@ViewScoped
public class Navigator extends ManagedBean<Navigator> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2702143320449202680")
  private static final long serialVersionUID = 3955645169532878544L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String            page;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Navigator</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Navigator() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   active
  /**
   ** Returns the style class to apply on a sidebar menu item.
   **
   ** @param  value              the page value to verify if its active,
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the style class to apply on a sidebar menu
   **                            item.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String active(final String value) {
    return this.page == null ? null : this.page.equals(value) ? "active" : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dashboard
  /**
   ** Set the <code>page</code> property of the <code>Handler</code> to
   ** <code>dashboard</code>.
   */
  public void dashboard() {
    this.page = "dashboard";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifier
  /**
   ** Set the <code>page</code> property of the <code>Handler</code> to
   ** <code>identifier</code>.
   */
  public void identifier() {
    this.page = "identifier";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   country
  /**
   ** Set the <code>page</code> property of the <code>Handler</code> to
   ** <code>country</code>.
   */
  public void country() {
    this.page = "cnt";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   state
  /**
   ** Set the <code>page</code> property of the <code>Handler</code> to
   ** <code>state</code>.
   */
  public void state() {
    this.page = "sta";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tenant
  /**
   ** Set the <code>page</code> property of the <code>Handler</code> to
   ** <code>tenant</code>.
   */
  public void tenant() {
    this.page = "tnt";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   participant
  /**
   ** Set the <code>page</code> property of the <code>Handler</code> to
   ** <code>participant</code>.
   */
  public void participant() {
    this.page = "pts";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   participantType
  /**
   ** Set the <code>page</code> property of the <code>Handler</code> to
   ** <code>participantType</code>.
   */
  public void participantType() {
    this.page = "ptt";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Set the <code>page</code> property of the <code>Handler</code> to
   ** <code>type</code>.
   */
  public void type() {
    this.page = "typ";
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   user
  /**
   ** Set the <code>page</code> property of the <code>Handler</code> to
   ** <code>user</code>.
   */
  public void user() {
    this.page = "usr";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Set the <code>page</code> property of the <code>Handler</code> to
   ** <code>role</code>.
   */
  public void role() {
    this.page = "rol";
  }
}