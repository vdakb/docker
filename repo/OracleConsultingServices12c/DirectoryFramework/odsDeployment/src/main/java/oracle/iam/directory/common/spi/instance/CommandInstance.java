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

    File        :   CommandInstance.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CommandInstance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.common.spi.instance;

import java.util.Map;
import java.util.List;
import java.util.EnumMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import oracle.hst.deployment.spi.AbstractInstance;

import org.apache.tools.ant.BuildException;

import oracle.iam.directory.common.FeatureError;
import oracle.iam.directory.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class CommandInstance
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>CommandInstance</code> represents an command wrapper used by
 ** <code>ConfigHandler.Server</code> to configure a directory.
 ** <p>
 ** Subclasses of this classes providing the data model an implementation of
 ** <code>ConfigurationHandler</code> needs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CommandInstance extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////

  // the connection Input/Output options
  final Map<Option, String>          option   = new EnumMap<Option, String>(Option.class);
  final Map<String, CommandProperty> property = new LinkedHashMap<String, CommandProperty>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // enum Option
  // ~~~~ ~~~~~~
  /**
   ** The <code>ConfigurationHandler</code> allows to interact.
   ** These input/output options are used in interaction with the directory
   ** server.
   */
  public enum Option {

    /**
     ** Run in quiet mode.
     ** <br>
     ** No output will be generated unless a significant error occurs during the
     ** process.
     */
    quiet("quiet"),
    /**
     ** Run in verbose mode, displaying diagnostics on standard output.
     */
    verbose("verbose"),
    /**
     ** Unless all configuration parameters and this option are specified the
     ** <code>ConfigurationHandler</code> runs in interactive mode.
     ** <p>
     ** <b>Note</b>: This option is configured by default because console
     ** input is not permitted at runtime using ANT.
     */
    noprompt("no-prompt"),
    /**
     ** Indicate that the command will not use a properties file to get the
     ** default command-line options.
     ** <p>
     ** <b>Note</b>: This option is configured by default because its embeded
     ** in the scrpits itself.
     */
    noPropertyFile("noPropertiesFile"),
    /**
     ** Specify the path to the properties file that contains the default
     ** command-line options.
     ** <p>
     ** <b>Note</b>: This option must not be used.
     */
    propertyFilePath("propertiesFilePath"),
    /**
     ** Run in <em>script friendly</em> mode.
     ** <br>
     ** Display the output in a format that can be easily parsed by a script.
     */
    scriptFriendly("script-friendly"),
    /**
     ** Display the equivalent non-interactive option in the standard output
     ** when this command is run in interactive mode.
     ** <p>
     ** <b>Note</b>: This option will be ignored.
     */
    displayCommand("displayCommand"),
    /**
     ** Specifies the path to a file that contains a set of commands to be
     ** executed.
     ** <br>
     ** This option supports line splitting, backslash ('\'), quotes (") escaped
     ** quotes (\") inside a quoted string, and hash for comments ('#').
     */
    batchFile("batchFile"),
    /**
     ** Specify the full path to the file, where the equivalent non-interactive
     ** commands will be written when this command is run in interactive mode.
     ** <p>
     ** <b>Note</b>: This option will be ignored.
     */
    commandFilePath("commandFilePath")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Option</code>
     **
     ** @param  id               the <code>ConfigurationHandler</code> accepts
     **                          an option in either its short form (for
     **                          example, <code>-h hostname</code>) or its long
     **                          form equivalent (for example,
     **                          <code>--hostname hostname</code>).
     **                          <br>
     **                          Per design only the long form equivalent will
     **                          be accepted.
     **                          Allowed object is {@link String}.
     */
    Option(final String id) {
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Returns the value of the id property.
     **
     ** @return                  the value of the id property.
     **                          Possible object is {@link String}.
     */
    public String id() {
      return this.id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper input/output option from the given
     ** string value.
     **
     ** @param  id                 the string value the input/output option
     **                            should be returned for.
     **
     ** @return                    input/output option.
     **                            Possible object is <code>Option</code>.
     */
    public static Option from(final String id) {
      for (Option cursor : Option.values()) {
        if (cursor.id.equals(id))
          return cursor;
      }
      throw new IllegalArgumentException(id);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: argument
    /**
     ** Returns the string representation of input/output option.
     **
     ** @return                  the string representation of input/output option.
     **                          Nothing else the id prepended with two dashes.
     */
    public String argument() {
      return String.format("--%s", this.id);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CommandInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public CommandInstance() {
    // ensure inheritance
    super();

    // initialize instance
    this.option.put(Option.noprompt,       null);
    this.option.put(Option.noPropertyFile, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   option
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>option</code>.
   **
   ** @param  option             the option identifier to add.
   ** @param  value              the value to bind at option.
   */
  public void option(final String option, final String value) {
    this.option.put(Option.from(option), value);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   optionArguments
  /**
   ** Returns the value of the input/output option property as a list of command
   ** line arguments.
   **
   ** @return                   the value of the input/output option property as
   **                           a list of command line arguments.
   */
  public final List<String> optionArguments() {
    final List<String> arguments = new ArrayList<String>();
    for (Map.Entry<Option, String> cursor : this.option.entrySet()) {
      final Option option = cursor.getKey();
      switch (option) {
        case quiet            :
        case scriptFriendly   :
        case noprompt         : 
        case displayCommand   : 
        case noPropertyFile   : arguments.add(option.argument());
                                break;
        case batchFile        : 
        case commandFilePath  : 
        case propertyFilePath : arguments.add(option.argument());
                                arguments.add(cursor.getValue());
                                break;
      
        
      }
    }
    return arguments;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   parameterArguments
  /**
   ** Returns the value of the parameter property as a list of command
   ** line arguments.
   **
   ** @return                   the value of the parameter property as a list of
   **                           command line arguments.
   */
  public final List<String> parameterArguments() {
    final List<String> arguments = new ArrayList<String>();
    for (Map.Entry<String, Object> cursor : parameter().entrySet()) {
      arguments.add(String.format("--%s", cursor.getKey()));
      arguments.add(cursor.getValue().toString());
    }
    for (Map.Entry<String, CommandProperty> cursor : this.property.entrySet()) {
      arguments.addAll(cursor.getValue().parameterArguments());
    }
    return arguments;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the specified property to the properties that has to be applied.
   **
   ** @param  object             the {@link SearchInstance} to add.
   */
  public void add(final CommandProperty object) {
    if (this.property.containsKey(object.name()))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.COMMAND_PROPERTY_ONLYONCE, object.name()));

    // add the instance to the object to handle
    this.property.put(object.name(), object);
  }

}