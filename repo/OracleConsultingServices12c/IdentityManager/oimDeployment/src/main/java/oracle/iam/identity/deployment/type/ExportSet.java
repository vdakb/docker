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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   ExportSet.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ExportSet.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.type;

import java.util.Collection;
import java.util.ArrayList;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

import com.thortech.xl.vo.ddm.RootObject;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ExportSet
// ~~~~~ ~~~~~~~~~
/**
 ** <code>ExportSet</code> is a group of {@link Category} that will be exported
 ** to the same file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ExportSet extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private File                   file;
  private String                 description;
  private Collection<RootObject> category = new ArrayList<RootObject>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ExportSet</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ExportSet() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another
   ** <code>ExportSet</code> instance.
   ** <p>
   ** You must not set another attribute or nest elements inside this element
   ** if you make it a reference.
   **
   ** @param  reference          the id of this instance.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  public void setRefid(final Reference reference)
    throws BuildException {

    if (!StringUtility.isEmpty(this.description) || this.file != null || this.category.size() > 0)
      throw tooManyAttributes();

    // ensure inheritance
    super.setRefid(reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setExportFile
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>exportFile</code>.
   **
   ** @param  file             the {@link File} where the export has to be
   **                          written to.
   */
  public void setExportFile(final File file) {
    this.file = file;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   exportFile
  /**
   ** Returns the {@link File} where the export of this set has to be written
   ** to.
   **
   ** @return                    the {@link File} where the export of this set
   **                            has to be written to.
   */
  public final File exportFile() {
    return this.file;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>description</code>.
   **
   ** @param  description      the description in the created deployment.
   */
  public void setDescription(final String description) {
    this.description = description;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description in the created deployment.
   **
   ** @return                    the description in the created deployment.
   */
  public final String description() {
    return this.description;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   category
  /**
   ** Returns the category in the created deployment.
   **
   ** @return                    the category in the created deployment.
   */
  public final Collection<RootObject> category() {
    return this.category;
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
   ** <code>null</code> and is a <code>ExportSet</code> object that
   ** represents the same <code>file</code> as this object.
   **
   ** @param other               the object to compare this
   **                            <code>ExportSet</code> against.
   **
   ** @return                   <code>true</code> if the
   **                           <code>ExportSet</code>s are
   **                           equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof ExportSet))
      return false;

    return this.file.equals(((ExportSet)other).file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addCategory
  /**
   ** Call by the ANT deployment to inject the argument for adding a category.
   **
   ** @param  category           the cache category to add.
   **
   ** @throws BuildException     if this instance is referencing an already
   **                            added {@link Category}.
   */
  public void addCategory(final Category category)
    throws BuildException {

    addObject(new RootObject(category.getValue(), category.name()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredCategory
  /**
   ** Call by the ANT deployment to inject the argument for adding a category.
   **
   ** @param  category           the cache category to add.
   **
   ** @throws BuildException     if this instance is referencing an already
   **                            added {@link Category}.
   */
  public void addConfiguredCategory(final Category category)
    throws BuildException {

    addCategory(category);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addObject
  /**
   ** Called to inject the argument for adding an {@link RootObject}.
   **
   ** @param  object             the {@link RootObject} to add.
   **
   ** @throws BuildException     if this instance is referencing an already
   **                            added {@link Category}.
   */
  public void addObject(final RootObject object)
    throws BuildException {

    if (isReference())
      throw noChildrenAllowed();

    this.category.add(object);
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
      throw new BuildException(FeatureResourceBundle.string(FeatureError.EXPORT_FILE_MANDATORY));

    if ((this.file != null) && (this.file.isDirectory()))
      throw new BuildException(FeatureResourceBundle.string(FeatureError.EXPORT_FILE_ISDIRECTORY));

    if (StringUtility.isEmpty(this.description))
      throw new BuildException(FeatureResourceBundle.string(FeatureError.EXPORT_FILE_DESCRIPTION));
  }
}