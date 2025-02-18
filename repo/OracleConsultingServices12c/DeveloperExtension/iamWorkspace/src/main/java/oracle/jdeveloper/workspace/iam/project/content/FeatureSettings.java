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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facilities

    File        :   FeatureSettings.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    FeatureSettings.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.63  2015-02-14  DSteding    content root path added
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Content Set's shipped to standard
                                               PathConfiguration container
*/

package oracle.jdeveloper.workspace.iam.project.content;

import java.io.IOException;

import java.net.URL;

import oracle.javatools.data.HashStructure;
import oracle.javatools.data.PropertyStorage;

import oracle.ide.net.URLPath;

import oracle.ide.model.Project;
import oracle.ide.model.ContentSet;

import oracle.ide.marshal.xml.HashStructureIO;

import oracle.jdeveloper.workspace.iam.model.DataStructureAdapter;

////////////////////////////////////////////////////////////////////////////////
// abstract class FeatureSettings
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
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
 ** can be stored in <code>FeatureSettings</code> are limited to the
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
 **   <li>ListStructure
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.60.63
 */
public abstract class FeatureSettings extends DataStructureAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** key to use for storage of the data structure settings */
  static final String PATH = "url-path";

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** The specified {@link HashStructure} must not be <code>null</code>.
   **
   ** @param  hash               the {@link HashStructure} this adapter wrappes.
   **
   ** @throws IllegalArgumentException if the specified {@link HashStructure} is
   **                                  <code>null</code>.
   */
  protected FeatureSettings(final HashStructure hash) {
    // ensure inheritance
    super(hash);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors and Mutators
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link HashStructure} managed by this adapter.
   **
   ** @param  storage            the {@link PropertyStorage} to obtain the
   **                            content set settings from.
   ** @param  content            the name of the {@link ContentSet} to the
   **                            {@link HashStructure} return for.
   **
   ** @return                    the {@link HashStructure} managed by this
   **                            adapter.
   */
  protected static HashStructure instance(final PropertyStorage storage, final String content) {
    HashStructure value = null;
    if ((storage instanceof Project)) {
      final Project project = (Project)storage;
      if (project.isOpen()) {
        value = findOrCreate(project, content);
      }
      else {
        final URL             url = project.getURL();
        final HashStructureIO jpr = new HashStructureIO("http://xmlns.oracle.com/ide/project", "jpr:project");
        try {
          final HashStructure hash = (HashStructure)jpr.load(url);
          return hash.getHashStructure(content);
        }
        catch (IOException e) {
          value = null;
        }
      }
      if (value == null) {
        value = findOrCreate(storage, content);
      }
    }
    else {
      value = findOrCreate(storage, content);
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Returns the {@link URLPath} contained in the {@link HashStructure} managed
   ** by this adapter.
   **
   ** @return                    the {@link URLPath} contained in the
   **                            {@link HashStructure} managed by this adapter.
   */
  public URLPath path() {
    return contentSet().getAllRootDirs();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contentSet
  /**
   ** Returns the {@link ContentSet} contained in the {@link HashStructure}
   ** managed by this adapter.
   **
   ** @return                    the {@link ContentSet} contained in the
   **                            {@link HashStructure} managed by this adapter.
   */
  public final ContentSet contentSet() {
    return new ContentSet(this._hash);
  }
}