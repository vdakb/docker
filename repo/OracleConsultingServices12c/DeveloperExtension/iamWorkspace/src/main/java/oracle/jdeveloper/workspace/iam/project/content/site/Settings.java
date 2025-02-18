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
    Subsystem   :   Identity and Access Management Facilities

    File        :   Settings.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    Settings.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.83  2019-02-14  DSteding    First release version
    12.2.1.3.42.60.84  2019-06-10  DSteding    Class made public to be able to
                                               configure the ContentSet in the
                                               initial project setup
    12.2.1.3.42.60.94  2021-03-09  DSteding    Content Set's shipped to standard
                                               PathConfiguration container
*/

package oracle.jdeveloper.workspace.iam.project.content.site;

import oracle.javatools.data.HashStructure;
import oracle.javatools.data.ListStructure;
import oracle.javatools.data.PropertyStorage;

import oracle.ide.model.Project;
import oracle.ide.model.ContentLevel;
import oracle.ide.model.ProjectContent;

import oracle.jdeveloper.model.PathsConfiguration;

import oracle.jdeveloper.workspace.iam.project.content.FeatureSettings;

////////////////////////////////////////////////////////////////////////////////
// class Settings
// ~~~~~ ~~~~~~~~
/**
 ** Object to encapsulate content settings in a project's configuration.
 ** <br>
 ** This object is added to the common data of a project when it is first given
 ** an provider.
 ** <br>
 ** Provides access to content set information such as the root content path
 ** under which the objects' .xml files are stored.
 ** <br>
 ** This class use a hash data structure intended to be used as the generic
 ** storage for project metadata that can be marshalled to and from a persistent
 ** form without depending on custom marshalling code. The types of data that
 ** can be stored in <code>Settings</code> are limited to the
 ** following:
 ** <ul>
 **   <li>String
 **   <li>boolean
 **   <li>int
 **   <li>long
 **   <li>float
 **   <li>double
 **   <li>{@link java.net.URL}
 **   <li>HashStructure
 **   <li>{@link ListStructure}
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   12.2.1.3.42.60.83
 */
public class Settings extends FeatureSettings {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** key to use for storage of the data structure settings */
  static final String DATA = PathsConfiguration.DATA_KEY + "/siteContentSet";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** The specified <code>HashStructure</code> must not be <code>null</code>.
   **
   ** @throws IllegalArgumentException if the specified
   **                                  <code>HashStructure</code> is
   **                                  <code>null</code>.
   */
  Settings(final HashStructure hash) {
    // ensure inheritance
    super(hash);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Invoked by the addin initializer after the instance of the addin is
   ** instantiated to register the content set provider.
   */
  public static void register() {
    // register our content set provider property with Project
    ProjectContent.registerContentSetProvider(new Provider(DATA));
    // add our content filter
    ContentLevel.addContentLevelFilter(new Filter(DATA));
    // attach our project listener to the node factory
    Project.addProjectChangeListener(DATA, Observer.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the <code>Settings</code> this content set settings are configured
   ** for.
   **
   ** @param  storage            the {@link PropertyStorage} to obtain the
   **                            content set settings from.
   **
   ** @return                    the <code>Settings</code> this content set
   **                            settings are configured for.
   */
  public static Settings instance(final Project storage) {
    return new Settings(instance(storage, DATA));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the <code>Settings</code> this content set settings are configured
   ** for.
   **
   ** @param  storage            the {@link PropertyStorage} to obtain the
   **                            content set settings from.
   **
   ** @return                    the <code>Settings</code> this content set
   **                            settings are configured for.
   */
  public static Settings instance(final PropertyStorage storage) {
    return new Settings(instance(storage, DATA));
  }
}