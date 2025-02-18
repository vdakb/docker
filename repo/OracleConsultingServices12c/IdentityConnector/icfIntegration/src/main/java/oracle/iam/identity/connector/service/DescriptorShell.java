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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Connector Bundle Framework

    File        :   DescriptorShell.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DescriptorShell.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.service;

import java.util.Map;

import groovy.lang.GString;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

////////////////////////////////////////////////////////////////////////////////
// abstract class DescriptorShell
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>DescriptorShell</code> represents a groovy shell capable of running
 ** arbitrary groovy scripts
 ** <p>
 ** There is only one existing instance of the class in a JVM; it is implemented
 ** as singleton.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class DescriptorShell {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static DescriptorShell singleton = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Shell
  // ~~~~~ ~~~~~
  /**
   ** <code>Shell</code> defines the properties of an descriptor attribute
   */
  public static class Shell extends DescriptorShell {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Shell</code> with the specified target and
     ** source name.
     ** <br>
     ** The type of the attribute is per default <code>String.class</code>.
     */
    private Shell() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: execute (overridden)
    /**
     ** Evaluates the specified script <code>template</code> against the given
     ** mapping as the binding and returns the result.
     **
     ** @param  script           the script template to evaluate.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  binding          the variable bindings of a script which can be
     **                          altered from outside the script object or
     **                          created outside of a script and passed into it.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element is of type {@link String} as the key
     **                          and {@link Object} as the value.
     **
     ** @return                  the evaluated result.
     **                          <br>
     **                          Possible object is {@link Object}.
     */
    @Override
    protected Object execute(final String script, final Map<String, Object> binding) {
      final GroovyShell delegate = new GroovyShell(new Binding(binding));
      final Object      result   = delegate.evaluate(script);
      return (result instanceof GString) ? result.toString() : result;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor (protected)
  /**
   ** Default constructor
   ** <br>
   ** Access modifier private prevents other classes using
   ** "new DescriptorShell()"
   */
  private DescriptorShell() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the singleton instance of this {@link DescriptorShell},
   **
   ** @return                    the singleton instance
   **                            <br>
   **                            Possible object is
   **                            <code>DescriptorShell</code>.
   */
  public static synchronized DescriptorShell instance() {
    if (singleton == null) {
      singleton = build();
    }

    return singleton;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a template.
   **
   ** @return                    an instance of <code>DescriptorShell</code>
   **                            with the specified properties.
   **                            <br>
   **                            Possible object is
   **                            <code>DescriptorShell</code>.
   */
  protected static DescriptorShell build() {
    return new Shell();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Evaluates the specified script <code>template</code> against the given
   ** mapping as the binding and returns the result.
   **
   ** @param  script             the script template to evaluate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  binding            the variable bindings of a script which can be
   **                            altered from outside the script object or
   **                            created outside of a script and passed into it.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link Object} as the value.
   **
   ** @return                    the evaluated result.
   **                            <br>
   **                            Possible object is {@link Object}.
   */
  protected abstract Object execute(final String script, final Map<String, Object> binding);
}