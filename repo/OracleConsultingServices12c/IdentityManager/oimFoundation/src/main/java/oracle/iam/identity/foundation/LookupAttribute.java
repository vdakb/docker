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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   LookupAttribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LookupAttribute.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

////////////////////////////////////////////////////////////////////////////////
// class LookupAttribute
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>LookupAttribute</code> implements the base functionality for
 ** describing the attributes of a Lookup Definition.
 ** <br>
 ** A Lookup Definition may have attriutes or not. Some of them are mandatory
 ** for the functionality of a scheduled task.
 ** <p>
 ** This class extends the {@link AbstractAttribute} to achive type safetivity.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class LookupAttribute extends AbstractAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LookupAttribute</code> which has a name and is optional
   ** for a specific task.
   **
   ** @param  name               the name of the attribute.
   */
  private LookupAttribute(final String name) {
    super(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LookupAttribute</code> which has a name and the
   ** default value which this attribute provides in case it is not defined.
   ** <br>
   ** The attribute will be an optional attribute.
   **
   ** @param  name               the name of the attribute.
   ** @param  defaultValue       the default value for this attribute.
   */
  private LookupAttribute(final String name, final String defaultValue) {
    super(name, defaultValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LookupAttribute</code> which has a name and is
   ** optional/mandatory according to the passed parameter.
   **
   ** @param  name               the name of the attribute.
   ** @param  mandatory          <code>true</code> the attribute must be
   **                            declared for the task; otherwise false.
   */
  private LookupAttribute(final String name, final boolean mandatory) {
    super(name, mandatory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>LookupAttribute</code> which has a name
   ** and is optional for a specific task.
   **
   ** @param  name               the name of the attribute.
   **
   ** @return                    an instance of <code>LookupAttribute</code>
   **                            with the value provided.
   */
  public static LookupAttribute build(final String name) {
    return new LookupAttribute(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>LookupAttribute</code> which has a name
   ** and the default value which this attribute provides in case it is not
   ** defined.
   ** <br>
   ** The attribute will be an optional attribute.
   **
   ** @param  name               the name of the attribute.
   ** @param  defaultValue       the default value for this attribute.
   **
   ** @return                    an instance of <code>LookupAttribute</code>
   **                            with the value provided.
   */
  public static LookupAttribute build(final String name, final String defaultValue) {
    return new LookupAttribute(name, defaultValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>LookupAttribute</code> which has a name
   ** and is optional/mandatory according to the passed parameter.
   **
   ** @param  name               the name of the attribute.
   ** @param  mandatory          <code>true</code> the attribute must be
   **                            declared for the task; otherwise false.
   **
   ** @return                    an instance of <code>LookupAttribute</code>
   **                            with the value provided.
   */
  public static LookupAttribute build(final String name, final boolean mandatory) {
    return new LookupAttribute(name, mandatory);
  }
}