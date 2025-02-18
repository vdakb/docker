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

    File        :   IAMPreference.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IAMPreference.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15  2012-02-06  DSteding    Change setter/getter from name
                                               to file.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.model;

import oracle.javatools.data.HashStructure;
import oracle.javatools.data.PropertyStorage;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.workspace.iam.Manifest;

////////////////////////////////////////////////////////////////////////////////
// class IAMPreference
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The model to support the configuration wizard in creating the preferences
 ** stored in the file system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class IAMPreference extends BuildPropertyAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** A passphrase is a sequence of words or other text used to control access
   ** to a computer system, program or data. A passphrase is similar to a
   ** password in usage, but is generally longer for added security.
   ** <p>
   ** Passphrases are often used to control both access to, and operation of,
   ** cryptographic programs and systems.
   ** <br>
   ** Passphrases are particularly applicable to systems that use the passphrase
   ** as an encryption key.
   ** <p>
   ** The origin of the term is by analogy with password.
   */
  public static final String PASSPHRASE   = "passphrase";

  private static final String ANT_FILE         = "iam-12c-preferences";
  private static final String ANT_TEMPLATE     = Manifest.TEMPLATE_PACKAGE + "." + ANT_FILE;
  private static final String ANT_DEFAULT_FILE = ANT_FILE + "." + Manifest.CONFIG_FILE_TYPE;

  private static final String MVN_FILE         = "iam-preferences";
  private static final String MVN_TEMPLATE     = Manifest.TEMPLATE_PACKAGE + "." + MVN_FILE;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IAMPreference</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new IAMPreference()" and enforces use of the public factory method
   ** below.
   **
   ** @param  structure          the data provider to initialize the instance.
   */
  private IAMPreference(final HashStructure structure) {
    // ensure inheritance
    super(structure);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   file (overridden)
  /**
   ** Returns the file name tag used by the component configuration to store the
   ** bulld file in the local filesystem.
   **
   ** @return                    the file name tag used by the component
   **                            configuration to store the bulld file in the
   **                            local filesystem.
   */
  @Override
  public String file() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(FILE, ANT_DEFAULT_FILE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   project (overridden)
  /**
   ** Returns the project name tag used by the component configuration.
   **
   ** @return                    the project name tag used by the component
   **                            configuration.
   */
  @Override
  public String project() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(PROJECT, Manifest.string(Manifest.IAM_PREFERENCE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passphrase
  /**
   ** Called to inject the argument for instance attribute
   ** <code>passphrase</code>.
   **
   ** @param  passphrase         the pass phrase to decrypt passwords.
   */
  public void passphrase(final String passphrase) {
    this._hash.putString(PASSPHRASE, passphrase);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passphrase
  /**
   ** Returns the pass phrase to decrypt passwords.
   **
   ** @return                    the pass phrase to decrypt passwords.
   */
  public String passphrase() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(PASSPHRASE, "changeit");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   template (BuildPropertyAdapter)
  /**
   ** Returns the template of this descriptor the generation is based on.
   **
   ** @return                    the template of this descriptor the generation
   **                            is based on.
   */
  @Override
  public final String template() {
    return ANT_TEMPLATE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns an instance of this {@link BuildPropertyAdapter}.
   ** <p>
   ** This method tries to find an existing instance of a
   ** {@link BuildPropertyAdapter} using {@link Manifest#IAM_PREFERENCE} in the
   ** design time objects of the specified {@link TraversableContext}.
   ** <p>
   ** If none can be found, this method will create a new {@link HashStructure},
   ** first attempting to wire that into the specified name in the
   ** {@link TraversableContext} or, failing that, leave the new
   ** {@link HashStructure} disconnected.
   ** <p>
   ** This method is guaranteed to return a <code>non-null</code>.
   ** Factory method takes a {@link PropertyStorage} (instead of
   ** {@link HashStructure} directly). This decouples the origin of the
   ** {@link HashStructure} and allows the future possibility of resolving
   ** preferences through multiple layers of {@link HashStructure}.
   ** <p>
   ** Classes/methods that currently implement/return PropertyStorage:
   ** <ul>
   **   <li>oracle.ide.config.Preferences
   **   <li>oracle.ide.model.Project
   **   <li>oracle.ide.model.Workspace
   **   <li>oracle.ide.panels.TraversableContext.getPropertyStorage()
   ** </ul>
   **
   ** @param  context            the data provider to initialize the instance.
   **
   ** @return                    an instance of this
   **                            {@link BuildPropertyAdapter} bound at the
   **                            given context.
   */
  public static IAMPreference instance(final TraversableContext context) {
    IAMPreference instance = (IAMPreference)context.getDesignTimeObject(Manifest.IAM_PREFERENCE);
    if (instance == null) {
      instance = new IAMPreference(HashStructure.newInstance());
      context.putDesignTimeObject(Manifest.IAM_PREFERENCE, instance);
    }
    return instance;
  }
}