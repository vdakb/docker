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

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities

    File        :   FileInstance.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FileInstance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.common.spi.instance;

import java.io.File;

import org.apache.tools.ant.BuildException;

import oracle.iam.directory.common.FeatureConstant;
import oracle.iam.directory.common.FeatureError;
import oracle.iam.directory.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class FileInstance
// ~~~~~ ~~~~~~~~~~~~
/**
 ** <code>FileInstance</code> represents a data instance wrapper a
 ** <code>ExportHandler</code> or <code>ImportHandler</code> use to control the
 ** file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class FileInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected File   file;
  protected String format  = FeatureConstant.FORMAT_LDIF;
  protected int    version = 2;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FileInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public FileInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FileInstance</code> with the specified properties.
   **
   ** @param  file               the {@link File} where the export goes to or
   **                            the import has to get from.
   ** @param  format             the format descriptor one of
   **                            <ul>
   **                              <li>{@link FeatureConstant#FORMAT_LDIF}
   **                              <li>{@link FeatureConstant#FORMAT_DSML}
   **                              <li>{@link FeatureConstant#FORMAT_JSON}
   **                             </ul>
   ** @param  version             the version of the file format in the created
   **                             deployment.
   */
  public FileInstance(final File file, final String format, final int version) {
    // ensure inheritance
    super();

    // initailize instance attributes
    this.file    = file;
    this.format  = format;
    this.version = version;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   file
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>file</code>.
   **
   ** @param  file             the {@link File} to set.
   */
  public void file(final File file) {
    this.file = file;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   file
  /**
   ** Returns the {@link File} where the export of this set has to be written
   ** to or the import in a set has to get from.
   **
   ** @return                    the {@link File} where the export of this set
   **                            has to be written to or the import in a set has
   **                            to get from.
   */
  public final File file() {
    return this.file;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>format</code>.
   **
   ** @param  format             the format descriptor one of
   **                            <ul>
   **                              <li>{@link FeatureConstant#FORMAT_LDIF}
   **                              <li>{@link FeatureConstant#FORMAT_DSML}
   **                              <li>{@link FeatureConstant#FORMAT_JSON}
   **                             </ul>
   */
  public void format(final String format) {
    this.format = format;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Returns the output format of the file.
   **
   ** @return                    the format descriptor one of
   **                            <ul>
   **                              <li>{@link FeatureConstant#FORMAT_LDIF}
   **                              <li>{@link FeatureConstant#FORMAT_DSML}
   **                              <li>{@link FeatureConstant#FORMAT_JSON}
   **                             </ul>
   */
  public final String format() {
    return this.format;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   version
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>version</code>.
   **
   ** @param  version      the version in the created deployment.
   */
  public void version(final String version) {
    version(Double.valueOf(version).intValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   version
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>version</code>.
   **
   ** @param  version            the version in the created deployment.
   */
  public void version(final int version) {
    this.version = version;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   version
  /**
   ** Returns the version in the created deployment.
   **
   ** @return                    the version in the created deployment.
   */
  public final int version() {
    return this.version;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   */
  @Override
  public int hashCode() {
    return this.file.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>FileInstance</code> object that
   ** represents the same {@link File} as this instance.
   **
   ** @param other             the object to compare this
   **                          <code>FileInstance</code> with.
   **
   ** @return                  <code>true</code> if the
   **                          <code>FileInstance</code>s are equal;
   **                          <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof FileInstance))
      return false;

    final FileInstance another = (FileInstance)other;
    return another.file.equals(this.file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the type to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  public void validate()
    throws BuildException {

    if (this.file == null)
      throw new BuildException(FeatureResourceBundle.string(FeatureError.IMPORT_FILE_MANDATORY));

    // we cannot allow to import a complete directory
    if (this.file.isDirectory())
      throw new BuildException(FeatureResourceBundle.string(FeatureError.IMPORT_FILE_ISDIRECTORY));

    // check if we are able to import the file
    if (!this.file.exists())
      throw new BuildException(FeatureResourceBundle.format(FeatureError.IMPORT_FILE_NOTEXISTS, this.file.getName()));

    // we need at least read permissions on the file to add
    if (!this.file.canRead())
      throw new BuildException(FeatureResourceBundle.format(FeatureError.IMPORT_FILE_NOPERMISSION, this.file.getName()));
  }
}