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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Authorization Management

    File        :   ReportAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ReportAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.schema;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class ReportAdapter
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Data Access Object used by <code>Report</code> customization.
 ** <p>
 ** Define an instance variable for each VO attribute, with the data type that
 ** corresponds to the VO attribute type. The name of the instance variable must
 ** match the name of the VO attribute, and the case must match.
 ** <p>
 ** When the instance of this class is passed to the methods in the
 ** corresponding <code>Application Module</code> class, the
 ** <code>Application Module</code> can call the getter methods to retrieve the
 ** values from the AdapterBean and pass them to the OIM APIs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class ReportAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PATH             = "path";
  public static final String NAME             = "name";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-9110643309626639645")
  private static final long serialVersionUID = 8162938753000504970L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private String path;
  @ModelAttr
  private String name;
  @ModelAttr
  private String description;
  @ModelAttr
  private String owner;
  @ModelAttr
  private String download;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ReportAdapter</code> value object that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ReportAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ReportAdapter</code> value object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   **                            <br>
   **                            Allowed object is {@link Row}.
   */
  public ReportAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ReportAdapter</code> value object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   **                            <br>
   **                            Allowed object is {@link Row}.
   ** @param  deferChildren      <code>true</code> if the child rows are lazy
   **                            populated at access time; <code>false</code> if
   **                            its reuired to populate them immediatly.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public ReportAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ReportAdapter</code> value object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  path               the value of the path property.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  name               the value of the name property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public ReportAdapter(final String path, final String name) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.name = name;
    this.path = path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setPath
  /**
   ** Sets the value of the path property.
   **
   ** @param  value              the value of the path property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setPath(final String value) {
    this.path = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getPath
  /**
   ** Returns the value of the path property.
   **
   ** @return                    the value of the path property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getPath() {
    return this.path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setName
  /**
   ** Sets the value of the name property.
   **
   ** @param  value              the value of the name property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setName(final String value) {
    this.name = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getName
  /**
   ** Returns the value of the name property.
   **
   ** @return                    the value of the name property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getName() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setDescription
  /**
   ** Sets the value of the description property.
   **
   ** @param  value              the value of the description property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setDescription(final String value) {
    this.description = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getDescription
  /**
   ** Returns the value of the description property.
   **
   ** @return                    the value of the description property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getDescription() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setOwner
  /**
   ** Sets the value of the owner property.
   **
   ** @param  value              the value of the owner property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setOwner(final String value) {
    this.owner = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getOwner
  /**
   ** Returns the value of the owner property.
   **
   ** @return                    the value of the owner property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getOwner() {
    return this.owner;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setDownload
  /**
   ** Sets the value of the download property.
   **
   ** @param  value              the value of the download property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setDownload(final String value) {
    this.download = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getDownload
  /**
   ** Returns the value of the download property.
   **
   ** @return                    the value of the download property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getDownload() {
    return this.download;
  }
}