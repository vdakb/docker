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

    File        :   Preference.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Preference.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.uid.state;

import java.io.Serializable;

import javax.inject.Named;

import javax.enterprise.context.SessionScoped;

////////////////////////////////////////////////////////////////////////////////
// class Preference
// ~~~~~ ~~~~~~~~~~
/**
 ** The <code>Preference</code> bean to handle the page configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SessionScoped
@Named("preference")
@SuppressWarnings("oracle.jdeveloper.cdi.not-proxyable-bean")
public class Preference implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4510296862682174391")
  private static final long serialVersionUID = -2295863672539757333L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Boolean fixedLayout;
  private Boolean fixedNavigation;
  private Boolean collapseSideBar;

  private Integer layout;

  private String  theme;

  private Integer pageSize;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Preference</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  Preference() {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.fixedLayout     = false;
    this.fixedNavigation = true;
    this.collapseSideBar = false;
    this.layout          = 0;
    this.theme           = "default";
    this.pageSize        = 15;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLayout
  /**
   ** Set the <code>layout</code> property of the <code>Preference</code>.
   **
   ** @param  value              the <code>layout</code> property of the
   **                            <code>Preference</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public void setLayout(final Integer value) {
    this.layout = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLayout
  /**
   ** Returns the <code>layout</code> property of the <code>Preference</code>.
   **
   ** @return                    the <code>layout</code> property of the
   **                            <code>Preference</code>.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public Integer getLayout() {
    return this.layout;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFixedLayout
  /**
   ** Set the <code>fixedLayout</code> property of the
   ** <code>Preference</code>.
   **
   ** @param  value              the <code>fixedLayout</code> property of the
   **                            <code>Preference</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public void setFixedLayout(final Boolean value) {
    this.fixedLayout = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFixedLayout
  /**
   ** Returns the <code>fixedLayout</code> property of the
   ** <code>Preference</code>.
   **
   ** @return                    the <code>fixedLayout</code> property of the
   **                            <code>Preference</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getFixedLayout() {
    return this.fixedLayout;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFixedNavigation
  /**
   ** Set the <code>fixedNavigation</code> property of the
   ** <code>Preference</code>.
   **
   ** @param  value              the <code>fixedNavigation</code> property of
   **                            the <code>Preference</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public void setFixedNavigation(final Boolean value) {
    this.fixedNavigation = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFixedNavigation
  /**
   ** Returns the <code>fixedNavigation</code> property of the
   ** <code>Preference</code>.
   **
   ** @return                    the <code>fixedNavigation</code> property of
   **                            the <code>Preference</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getFixedNavigation() {
    return this.fixedNavigation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCollapseSideBar
  /**
   ** Set the <code>collapseSideBar</code> property of the
   ** <code>Preference</code>.
   **
   ** @param  value              the <code>collapseSideBar</code> property of
   **                            the <code>Preference</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public void setCollapseSideBar(final Boolean value) {
    this.collapseSideBar = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCollapseSideBar
  /**
   ** Returns the <code>collapseSideBar</code> property of the
   ** <code>Preference</code>.
   **
   ** @return                    the <code>collapseSideBar</code> property of
   **                            the <code>Preference</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getCollapseSideBar() {
    return this.collapseSideBar;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTheme
  /**
   ** Set the <code>theme</code> property of the <code>Preference</code>.
   **
   ** @param  value              the <code>theme</code> property of
   **                            the <code>Preference</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setTheme(final String value) {
    this.theme = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTheme
  /**
   ** Returns the <code>theme</code> property of the <code>Preference</code>.
   **
   ** @return                    the <code>theme</code> property of
   **                            the <code>Preference</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getTheme() {
    return this.theme;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getStyleClass
  /**
   ** Returns the <code>styleClass</code> attribute if the <code>body</code>
   ** element of the HTML DOM depending on the <code>Preference</code>
   ** properties choosen by the end user.
   **
   ** @return                    the <code>styleClass</code> attribute if the
   **                            <code>body</code> element of the HTML DOM
   **                            depending on the <code>Preference</code>
   **                            properties choosen by the end user.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getStyleClass() {
    return "container-fluid vh-100 fixed-navbar".concat(this.collapseSideBar ? " sidebar-mini" : "");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPageSize
  /**
   ** Set the <code>pageSize</code> property of the <code>Preference</code>.
   **
   ** @param  value              the <code>pageSize</code> property of the
   **                            <code>Preference</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public void setPageSize(final Integer value) {
    this.pageSize = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPageSize
  /**
   ** Returns the <code>pageSize</code> property of the <code>Preference</code>.
   **
   ** @return                    the <code>pageSize</code> property of the
   **                            <code>Preference</code>.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public Integer getPageSize() {
    return this.pageSize;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toggleSideBar
  /**
   ** Callback method invoke by the action <code>collapseSideBar</code> in the
   ** UI to collapse or expand the SideBar.
   */
  public void toggleSideBar() {
    this.collapseSideBar = !this.collapseSideBar;
  }
}