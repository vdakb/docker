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

    System      :   Foundation Shared Library
    Subsystem   :   Deployment Utilities

    File        :   CommandLine.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CommandLine.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

import oracle.hst.foundation.SystemError;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.resource.SystemBundle;

////////////////////////////////////////////////////////////////////////////////
//class CommandLine
//~~~~~ ~~~~~~~~~~~
/**
 ** The <code>CommandLine</code> class supplies utility methods for parsing
 ** command line arguments passed to programs.
 ** <p>
 ** A command line option can be defined to take an argument. If an option has
 ** an argument, the value of that argument is stored in an instance variable
 ** called <code>optionArgument</code>, which can be accessed using the
 ** <code>optionArgument()</code> method. If an option that requires an
 ** argument is found, but there is no argument present, then an error message
 ** is printed. Normally {@link #nextOption()} returns a question mark ('?') in
 ** this situation, but that can be changed as described below.
 ** <p>
 ** If an invalid option is encountered, an error message is printed to the
 ** standard error and {@link #nextOption()} returns a question mark ('?'). The
 ** value of the invalid option encountered is stored in the instance variable
 ** {@link #unknown} which can be retrieved using the {@link #unknown()} method.
 ** To suppress the printing of error messages for this or any other error, set
 ** the value of the {@link #showError} instance variable to <code>false</code>
 ** using the {@link #suppressErrors()} method.
 ** <p>
 ** Between calls to {@link #nextOption()}, the instance variable
 ** <code>index</code> is used to keep track of where the object is in the
 ** parsing process. After all options have been returned,
 ** <code>index</code> is the index in args of the first non-option
 ** argument. This variable can be accessed with the <code>index</code> method.
 ** <p>
 ** Note that this object expects command line options to be passed in the
 ** traditional Unix manner. That is, proceeded by a '-' character.  Multiple
 ** options can follow the '-'.  For example "-abc" is equivalent to "-a -b -c".
 ** If an option takes a required argument, the value of the argument can
 ** immediately follow the option character or be present in the next argv
 ** element. For example, "-cfoo" and "-c foo" both represent an option
 ** character of 'c' with an argument of "foo" assuming c takes a required
 ** argument. If an option takes an argument that is not required, then any
 ** argument must immediately follow the option character in the same argv
 ** element. For example, if c takes a non-required argument, then "-cfoo"
 ** represents option character 'c' with an argument of "foo" while "-c foo"
 ** represents the option character 'c' with no argument, and a first
 ** non-option argv element of "foo".
 ** <p>
 ** The user can stop nextOption() from scanning any further into a command line
 ** by using the special argument "--" by itself. For example: "-a -- -d" would
 ** return an option character of 'a', then return -1. The "--" is discarded and
 ** "-d" is pointed to by optionIndex as the first * non-option argv element.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CommandLine {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Constant value used for the "has_arg" constructor argument.
   ** <br>
   ** This value indicates that the option takes no argument.
   */
  public static final int             ARGUMENT_NO       = 0;

  /**
   ** Constant value used for the "has_arg" constructor argument.
   ** <br>
   ** This value indicates that the option takes an argument that is required.
   */
  public static final int             ARGUMENT_REQUIRED = 1;

  /**
   ** Constant value used for the "has_arg" constructor argument.
   ** <br>
   ** This value indicates that the option takes an argument that is optional.
   */
  public static final int             ARGUMENT_OPTIONAL = 2;

  /**
   ** Describe how to deal with options that follow non-option ARGS-elements.
   ** <br>
   ** <code>PERMUTE</code> is the default.
   ** <br>
   ** We permute the contents of ARGS as we scan, so that eventually all the
   ** non-options are at the end.  This allows options to be given in any order,
   ** even with programs that were not written to expect this.
   */
  protected static final int          PERMUTE           = 1;

  /**
   ** Describe how to deal with options that follow non-option ARGS-elements.
   ** <br>
   ** <code>REQUIRE_ORDER</code> means don't recognize them as options; stop
   ** option processing when the first non-option is seen. This is what Unix
   ** does.
   ** <br>
   ** This mode of operation is selected by either setting the property
   ** qms.strict, or using `+' as the first character of the list of option
   ** characters.
   */
  protected static final int          REQUIRE_ORDER     = 2;
   /**
    ** Describe how to deal with options that follow non-option ARGS-elements.
    ** <br>
    ** <code>RETURN_IN_ORDER</code> is an option available to programs that were
    ** written to expect options and other ARGS-elements in any order and that
    ** care about the ordering of the two. We describe each non-option
    ** ARGS-element as if it were the argument of an option with character code
    ** 1. Using `-' as the first character of the list of option characters
    ** selects this mode of operation.
    */
  protected static final int          RETURN_IN_ORDER   = 3;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Prevents output of error message for unrecognized options if
   ** <code>true</code>.
   */
  protected boolean      showError       = true;

  /** The flag determines whether or not we operate in strict POSIX compliance */
  protected boolean      posix;

  /** The flag determines whether or not we are parsing only long args */
  protected boolean      noSwitches;

  /** Determines whether we permute arguments or not */
  protected int          ordering;

  /** The index of the first non-option in args[] */
  protected int          nonOptionFirst  = 1;

  /** The index of the last non-option in args[] */
  protected int          nonOptionLast   = 1;

  /**
   ** If an unknown option is encountered, <code>getOption()</code> returns a
   ** question mark ('?') and store the index of the invalid option in the
   ** command line arguments here.
   */
  protected int          unknown         = SystemConstant.QUESTION;

  /**
   ** Name to print as the program name in error messages.
   ** <br>
   ** This is a hack since Java does not place the program name in argv[0]
   */
  protected String       program;

  /**
   ** Index of the next element to be scanned in the comand line argument array
   ** NOTE:
   ** If <code>nextOption()</code> returns -1, this is the index of the first of
   ** the non-option elements that the caller should itself scan.
   */
  protected int          index           = 0;

  /**
   ** A flag which communicates whether or not <code>checkOption()</code> did
   ** all necessary processing for the current option.
   */
  protected boolean      optionHandled;

  /** Stores the index into the options array of the option found. */
  protected int          optionIndex;

  /**
   ** The next char to be scanned in the option-element in which the last option
   ** character we returned was found.
   ** This allows us to pick up the scan where we left off.
   ** If this is zero, or a null string, it means resume the scan by advancing
   ** to the next ARGS-element.
   */
  protected String       cursor;

  /**
   ** This is the string describing the valid command line switches.
   */
  protected String       optionShort;

  /**
   ** Stores the current parsed argument for an commond line option.
   */
  protected String       optionArgument;

  /**
   ** Flag to tell <code>option()</code> to immediately return <code>-1</code>
   ** the next time it is called.
   */
  private boolean        eop               = false;

  /** Saved argument list passed to the program */
  private final String[] argument;

  /** This is an array of option objects which describe the valid long options. */
  private final Option[] option;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Option
  // ~~~~~ ~~~~~~
  /**
   ** The <code>Option</code> represents the definition of a command line
   ** argument specified by a long name.
   */
  public static class Option {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    //////////////////////////////////////////////////////////////////////////////

    /** The name of the long option. */
    protected String        name;

    /**
     ** Indicates whether the option has no argument, a required argument, or an
     ** optional argument.
     */
    protected int           acceptArgument;

    /**
     ** The value to store in "flag" if flag is not null, otherwise the
     ** equivalent short option character for this long option.
     */
    protected int           value;

    /**
     ** If this variable is not <code>null</code>, then the value stored in
     ** "value" is stored here when this option is encountered. If this is
     ** <code>null</code>, the value stored in "value" is treated as the name of
     ** an equivalent short option.
     */
    protected StringBuilder flag;

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    //
    public Option(final String name, final int acceptArgument, final StringBuilder flag, final int value)
      throws IllegalArgumentException {

      // Validate has_arg
      if ((acceptArgument != ARGUMENT_NO) && (acceptArgument != ARGUMENT_REQUIRED) && (acceptArgument != ARGUMENT_OPTIONAL))
        throw new IllegalArgumentException(SystemBundle.format(SystemError.CMDLINE_INVALIDVALUE, new Integer(acceptArgument).toString()));

      // Store off values
      this.name           = name;
      this.acceptArgument = acceptArgument;
      this.flag           = flag;
      this.value          = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   name
    /**
     ** Returns the name of this <code>Option</code> as a String.
     **
     ** @return                   the name of the option.
     */
    public final String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   acceptArgument
    /**
     ** Returns the value set for the 'acceptArgument' field for this option.
     **
     ** @return                   the value of 'acceptArgument'
     */
    public final int acceptArgument() {
      return this.acceptArgument;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   flag
    /**
     ** Returns the value of the 'flag' field for this option.
     **
     ** @return                   the value of 'flag'
     */
    public final StringBuilder flag() {
      return this.flag;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   value
    /**
     ** Returns the value of the 'val' field for this option.
     **
     ** @return                   the value of 'val'
     */
    public final int value() {
      return this.value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a <code>CommandLine</code>.
   ** <br>
   ** <b>NOTE:</b>
   ** This Constructor handles only short named options aka commend line
   ** switches.
   **
   ** @param  program            the name to display as the program name when
   **                            output errors.
   ** @param  argument           the string array passed as the command line to
   **                            the program.
   ** @param  switches           a string containing a description of the valid
   **                            arguments for the program.
   */
  public CommandLine(final String program, final String[] argument, final String switches) {
    this(program, argument, switches, null, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a <code>CommandLine</code>.
   **
   ** @param  program            the name to display as the program name when
   **                            output errors.
   ** @param  argument           the string array passed as the command line to
   **                            the program.
   ** @param  switches           a string containing a description of the valid
   **                            arguments for the program.
   ** @param  option             an array of <code>CommandLine.Option</code>
   **                            objects that describes the valid option
   **                            arguments for the program.
   */
  public CommandLine(final String program, final String[] argument, final String switches, final Option[] option) {
    this(program, argument, switches, option, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a <code>CommandLine</code>.
   **
   ** @param  program            the name to display as the program name when
   **                            output errors.
   ** @param  argument           the string array passed as the command line to
   **                            the program.
   ** @param  switches           a string containing a description of the valid
   **                            arguments for the program.
   ** @param  option             an array of <code>CommandLine.Option</code>
   **                            objects that describes the valid option
   **                            arguments for the program.
   ** @param  noSwitches         <code>true</code> if only long options allowed
   **                            for the noSwitches; otherwise
   **                            <code>false</code>.
   */
  public CommandLine(final String program, final String[] argument, final String switches, final Option[] option, final boolean noSwitches) {
    this.program    = program;
    this.argument   = argument;
    this.option     = option;
    this.noSwitches = noSwitches;

    // Determine how to handle the ordering of options and non-options
    this.optionShort = (switches.length() == 0) ? " " : switches;
    if (this.optionShort.charAt(0) == '-') {
      this.ordering = RETURN_IN_ORDER;
      if (this.optionShort.length() > 1)
        this.optionShort = this.optionShort.substring(1);
    }
    else if (this.optionShort.charAt(0) == '+') {
      this.ordering = REQUIRE_ORDER;
      if (this.optionShort.length() > 1)
        this.optionShort = this.optionShort.substring(1);
    }
    else if (this.posix)
      this.ordering = REQUIRE_ORDER;
    else
      this.ordering = PERMUTE; // The normal default case
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   suppressErrors
  /**
   ** Sets the value of <code>showError</code>.
   ** <br>
   ** Normaly a error message is printed if an invalid command line option is
   ** detected. Inoking this method the output of this error messages can be
   ** suppressed.
   */
  public void suppressErrors() {
    this.showError = false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   index
  /**
   ** Sets the value of <code>index</code>.
   ** <br>
   ** Normally this will never be necessary, but <code>optionIndex</code> has
   ** private access so we must give subclasses a chance to manipulate the
   ** value.
   **
   ** @param  index                the index of the next element to be scanned.
   */
  protected void index(int index) {
    this.index = index;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   index
  /**
   ** Returns the value of <code>index</code>.
   ** <br>
   ** <code>index</code> is the index of the next element to be scanned in
   ** <code>args</code> passed to constructors.
   ** <br>
   ** If <code>option()</code> returns <code>-1</code> this is the index of
   ** the first of the non-option elements that the caller should itself scan.
   ** Otherwise <code>optionIndex</code> can used to obtain how much arguments
   ** ared scanned.
   **
   ** @return                    the index of the next element to be scanned.
   */
  public int index() {
    return this.index;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   optionIndex
  /**
   ** Returns the index of an option find by {@link #nextOption()}.
   ** <p>
   ** Furthermore, if `ordering' is {@link #RETURN_IN_ORDER}, each non-option
   ** ARGS-element is returned here.
   ** <p>
   ** No set method is provided because setting this variable has no effect.
   **
   ** @return                    the index of an option find by
   **                            {@link #nextOption()}.
   */
  public int optionIndex() {
    return this.optionIndex;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   optionArgument
  /**
   ** Returns the argument of an option find by {@link #nextOption()}.
   ** <p>
   ** Furthermore, if `ordering' is {@link #RETURN_IN_ORDER}, each non-option
   ** ARGS-element is returned here.
   ** <p>
   ** No set method is provided because setting this variable has no effect.
   **
   ** @return                    the argument of an option find by
   **                            {@link #nextOption()}.
   */
  public String optionArgument() {
    return this.optionArgument;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unknown
  /**
   ** If <code>getOption()</code> encounters an invalid option, it stores the
   ** value of that option in <code>unknown</code> which can be retrieved with
   ** this method.
   ** <br>
   ** There is no corresponding set method because setting this variable has no
   ** effect.
   **
   ** @return                    the index of the unknown option if there is
   **                            any.
   */
  public int unknown() {
    return this.unknown;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nextOption
  /**
   ** This method returns a char that is the current option that has been parsed
   ** from the command line.
   ** <br>
   ** If the option takes an argument, then the attribute
   ** <code>optionArgument</code> is set which is the string representing of the
   ** value for the argument. This value can be retrieved using the
   ** {@link #optionArgument()} method. If an invalid option is found, an
   ** error message is printed and a question mark ('?') is returned. The name
   ** of the invalid option character can be retrieved by using
   ** {@link #unknown()} method. When there are no more options to be scanned,
   ** this method returns <code>-1</code>. The index of first non-option element
   ** in <code>args</code> passed to constructor can be retrieved with the
   ** {@link #optionIndex()} method.
   **
   ** @return Various things as described above
   */
  public int nextOption() {
    this.optionArgument = null;

    if (this.eop == true)
      return -1;

    if (StringUtility.isEmpty(this.cursor)) {
      // if we have just processed some options following some non-options,
      //  exchange them so that the options come first.
      if (this.nonOptionLast > this.index)
        this.nonOptionLast = this.index;
      if (this.nonOptionFirst > this.index)
        this.nonOptionFirst = this.index;

      if (this.ordering == PERMUTE) {
        // If we have just processed some options following some non-options,
        // exchange them so that the options come first.
        if ((this.nonOptionFirst != this.nonOptionLast) && (this.nonOptionLast != this.index))
          exchange(this.argument);
        else if (this.nonOptionLast != this.index)
          this.nonOptionFirst = this.index;

        // Skip any additional non-options and extend the range of non-options
        // previously skipped.
        while ((this.index < this.argument.length)
            && (this.argument[this.index].equals(SystemConstant.EMPTY)
              || (this.argument[this.index].charAt(0) != '-')
              || this.argument[this.index].equals("-")))
          this.index++;

        this.nonOptionLast = this.index;
      }

      // The special ARGS-element `--' means premature end of options.
      // Skip it like a null option, then exchange with previous non-options as
      // if it were an option, then skip everything else like a non-option.
      if ((this.index != this.argument.length) && this.argument[this.index].equals("--")) {
        this.index++;

        if ((this.nonOptionFirst != this.nonOptionLast) && (this.nonOptionLast != this.index))
          exchange(this.argument);
        else if (this.nonOptionFirst == this.nonOptionLast)
          this.nonOptionFirst = index;

        this.nonOptionLast = this.argument.length;
        this.index         = this.argument.length;
      }

      // If we have done all the ARGV-elements, stop the scan and back over any
      // non-options that we skipped and permuted.
      if (this.index == this.argument.length) {
        // Set the next-arg-index to point at the non-options that we previously
        // skipped, so the caller will digest them.
        if (this.nonOptionFirst != this.nonOptionLast)
          this.index = this.nonOptionFirst;

        return -1;
      }

      // If we have come to a non-option and did not permute it, either stop the
      // scan or describe it to the caller and pass it by.
      if (this.argument[index].equals(SystemConstant.EMPTY) || (this.argument[index].charAt(0) != '-') || this.argument[index].equals("-")) {
        if (this.ordering == REQUIRE_ORDER)
          return -1;

        this.optionArgument = this.argument[index++];
        return 1;
      }

      // We have found another option-ARGV-element.
      // Skip the initial punctuation.
      if (this.argument[index].startsWith("--"))
        this.cursor = this.argument[index].substring(2);
      else
        this.cursor = this.argument[index].substring(1);
    }

    // Decode the current option-ARGV-element.
    // Check whether the ARGV-element is a long option.
    // If long_only and the ARGV-element has the form "-f", where f is a valid
    // short option, don't consider it an abbreviated form of a long option that
    // starts with f. Otherwise there would be no way to give the -f short option.
    // On the other hand, if there's a long option "fubar" and the ARGV-element
    // is "-fu", do consider that an abbreviation of the long option, just like
    // "--fu", and not "-f" with arg "u".
    // This distinction seems to be the most useful approach.
    if ((this.option != null) && (this.argument[index].startsWith("--") || (noSwitches && ((this.argument[index].length() > 2) || (this.optionShort.indexOf(this.argument[index].charAt(1)) == -1))))) {
      int c = checkOption();

      if (this.optionHandled)
        return c;

      // Can't find it as a long option.  If this is not getopt_long_only, or
      // the option starts with '--' or is not a valid short option, then it's
      // an error; otherwise interpret it as a short option.
      if (!this.noSwitches || this.argument[index].startsWith("--") || (this.optionShort.indexOf(this.cursor.charAt(0)) == -1)) {
        if (this.showError) {
          if (this.argument[index].startsWith("--")) {
            Object[] arguments = { this.program, this.cursor };
            System.err.println(SystemBundle.format(SystemError.CMDLINE_UNREGCOGNIZED1, arguments));
          }
          else {
            Object[] arguments = { this.program, Character.toString(this.argument[index].charAt(0)), this.cursor };
            System.err.println(SystemBundle.format(SystemError.CMDLINE_UNREGCOGNIZED2, arguments));
          }
        }

        this.cursor  =  SystemConstant.EMPTY;
        this.unknown = 0;
        this.index++;
        return SystemConstant.QUESTION;
      }
    } // if ((this.option != null) ...

    // Look at and handle the next short option-character
    int c = this.cursor.charAt(0); // do we need to check for empty str?
    if (this.cursor.length() > 1)
      this.cursor = this.cursor.substring(1);
    else
      this.cursor = SystemConstant.EMPTY;

    String temp = null;
    if (this.optionShort.indexOf(c) != -1)
      temp = this.optionShort.substring(this.optionShort.indexOf(c));

    if (this.cursor.equals(SystemConstant.EMPTY))
      this.index++;

    if ((temp == null) || (c == SystemConstant.COLON)) {
      if (this.showError) {
        Object[] arguments = { this.program, Character.toString((char)c)};
        if (this.posix)
          System.err.println(SystemBundle.format(SystemError.CMDLINE_ILLEGAL, arguments));
        else
          System.err.println(SystemBundle.format(SystemError.CMDLINE_INVALID, arguments));
      }

      this.unknown = c;
      return SystemConstant.QUESTION;
    }

    // Convenience. Treat POSIX -W foo same as long option --foo
    if ((temp.charAt(0) == 'W') && (temp.length() > 1) && (temp.charAt(1) == SystemConstant.SEMICOLON)) {
      if (!this.cursor.equals(SystemConstant.EMPTY))
        this.optionArgument = this.cursor;
      // No further chars in this argv element and no more argv elements
      else if (this.index == this.argument.length) {
        if (this.showError) {
          Object[] arguments = { this.program, Character.toString((char)c) };
          System.err.println(SystemBundle.format(SystemError.CMDLINE_REQUIRED2, arguments));
        }

        this.unknown = c;
        return (this.optionShort.charAt(0) == SystemConstant.COLON) ? SystemConstant.COLON : SystemConstant.QUESTION;
      }
      else {
        // We already incremented index once;
        // increment it again when taking next ARGS-elt as argument.
        this.cursor         = this.argument[index];
        this.optionArgument = this.argument[index];
      }

      c = checkOption();
      if (this.optionHandled)
        return c;
      else {
        // Let the application handle it
        this.cursor = null;
        this.index++;
        return 'W';
      }
    }

    if ((temp.length() > 1) && (temp.charAt(1) == SystemConstant.COLON)) {
      if ((temp.length() > 2) && (temp.charAt(2) == SystemConstant.COLON)) {
        // this is an option that accepts an argument optionally
        if (!this.cursor.equals(SystemConstant.EMPTY)) {
          this.optionArgument = this.cursor;
          this.index++;
        }
        else
          this.optionArgument = null;

        this.cursor = null;
      }
      else {
        if (!this.cursor.equals(SystemConstant.EMPTY)) {
          this.optionArgument = this.cursor;
          this.index++;
        }
        else if (this.index == this.argument.length) {
          if (this.showError) {
            Object[] arguments = { this.program, Character.toString((char)c) };
            System.err.println(SystemBundle.format(SystemError.CMDLINE_REQUIRED2, arguments));
          }

          this.unknown = c;
          return (this.optionShort.charAt(0) == SystemConstant.COLON) ? SystemConstant.COLON : SystemConstant.QUESTION;
        }
        else {
          this.optionArgument = this.argument[index++];
          // Ok, here's an obscure Posix case.  If we have o:, and we get -o
          // -- foo, then we're supposed to skip the --, end parsing of options,
          // and make foo an operand to -o. Only do this in Posix mode.
          if ((this.posix) && this.optionArgument.equals("--")) {
            // if end of argv, error out
            if (this.index == this.argument.length) {
              if (this.showError) {
                Object[] arguments = { this.program, Character.toString((char)c) };
                System.err.println(SystemBundle.format(SystemError.CMDLINE_REQUIRED2, arguments));
              }

              this.unknown = c;
              return (this.optionShort.charAt(0) == SystemConstant.COLON) ? SystemConstant.COLON : SystemConstant.QUESTION;
            }
            // Set new optarg and set to end
            // Don't permute as we do on -- up above since we know we aren't in
            // permute mode because of Posix.
            this.optionArgument = this.argument[index++];
            this.nonOptionFirst = index;
            this.nonOptionLast  = this.argument.length;
            this.eop            = true;
          }
        }
        this.cursor = null;
      }
    }
    return c;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exchange
  /**
   ** Exchange the shorter segment with the far end of the longer segment.
   ** <br>
   ** That puts the shorter segment into the right place. It leaves the longer
   ** segment in the right place overall, but it consists of two parts that need
   ** to be swapped next.
   ** <br>
   ** This method is used by <code>getOption()</code> for argument permutation.
   */
  private void exchange(String[] args) {
    int bottom = this.nonOptionFirst;
    int middle = this.nonOptionLast;
    int top    = this.index;

    String tmp;
    while (top > middle && middle > bottom) {
      if (top - middle > middle - bottom) {
        // Bottom segment is the short one.
        int len = middle - bottom;
        // Swap it with the top part of the top segment.
        for (int i = 0; i < len; i++) {
          tmp                               = args[bottom + i];
          args[bottom + i]                  = args[top - (middle - bottom) + i];
          args[top - (middle - bottom) + i] = tmp;
        }
        // Exclude the moved bottom segment from further swapping.
        top -= len;
      }
      else {
        // Top segment is the short one.
        int len = top - middle;
        // Swap it with the bottom part of the bottom segment.
        for (int i = 0; i < len; i++) {
          tmp              = args[bottom + i];
          args[bottom + i] = args[middle + i];
          args[middle + i] = tmp;
        }
        // Exclude the moved top segment from further swapping.
        bottom += len;
      }
    }
    // Update records for the slots the non-options now occupy.
    this.nonOptionFirst += (this.index - this.nonOptionLast);
    this.nonOptionLast   = this.index;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   checkOption
  /**
   ** Check to see if an option is a valid long option.
   ** <br>
   ** Called by getopt().
   ** Put in a separate method because this needs to be done twice.
   ** (The C getopt authors just copy-pasted the code!).
   **
   ** @return                    Various things depending on circumstances
   */
  private int checkOption() {
    boolean ambigious  = false;
    boolean exact      = false;
    Option  found      = null;

    this.optionHandled = true;
    this.optionIndex   = -1;

    int argumentStart = this.cursor.indexOf("=");
    if (argumentStart == -1)
      argumentStart = this.cursor.length();

    // Test all options for either exact match or abbreviated matches
    for (int i = 0; i < this.option.length; i++) {
      if (this.option[i].name().startsWith(this.cursor.substring(0, argumentStart))) {
        if (this.option[i].name().equals(this.cursor.substring(0, argumentStart))) {
          // exact match found
          found            = this.option[i];
          this.optionIndex = i;
          exact            = true;
          break;
        }
        else if (found == null) {
          // first nonexact match found
          found            = this.option[i];
          this.optionIndex = i;
        }
        else {
          // Second or later nonexact match found
          ambigious = true;
        }
      }
    }

    // Print out an error if the option specified was ambiguous
    if (ambigious && !exact) {
      if (this.showError)
        System.err.println(SystemBundle.format(SystemError.CMDLINE_AMBIGIUOS, this.argument[index]));

      this.cursor  = SystemConstant.EMPTY;
      this.unknown = 0;
      this.index++;
      return SystemConstant.QUESTION;
    }

    if (found != null) {
      this.index++;
      if (argumentStart != this.cursor.length()) {
        if (found.acceptArgument != ARGUMENT_NO) {
          if (this.cursor.substring(argumentStart).length() > 1)
            this.optionArgument = this.cursor.substring(argumentStart + 1);
          else
            this.optionArgument = SystemConstant.EMPTY;
        }
        else {
          if (this.showError) {
            // -- option
            if (this.argument[index - 1].startsWith("--"))
              System.err.println(SystemBundle.format(SystemError.CMDLINE_ARGUMENT1, found.name));
            // +option or -option
            else {
              Object[] arguments = { Character.toString(this.argument[index - 1].charAt(0)), found.name };
              System.err.println(SystemBundle.format(SystemError.CMDLINE_ARGUMENT2, arguments));
            }
          }
          this.cursor  = SystemConstant.EMPTY;
          this.unknown = found.value;
          return SystemConstant.QUESTION;
        }
      }
      else if (found.acceptArgument == ARGUMENT_REQUIRED) {
        if (this.index < this.argument.length)
          this.optionArgument = this.argument[++this.index];
        else {
          if (this.showError)
            System.err.println(SystemBundle.format(SystemError.CMDLINE_REQUIRED1, this.argument[index - 1]));

          this.cursor  = SystemConstant.EMPTY;
          this.unknown = found.value;
          if (this.optionShort.charAt(0) == SystemConstant.COLON)
            return(SystemConstant.COLON);
          else
            return(SystemConstant.QUESTION);
        }
      }
      this.cursor = SystemConstant.EMPTY;
      if (found.flag != null) {
        found.flag.setLength(0);
        found.flag.append(found.value);
        return 0;
      }
      return(found.value);
    }
    optionHandled = false;
    return 0;
  }
}