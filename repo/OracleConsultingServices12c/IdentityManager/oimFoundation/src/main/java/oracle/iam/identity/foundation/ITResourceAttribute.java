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

    File        :   ITResourceAttribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ITResourceAttribute.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

////////////////////////////////////////////////////////////////////////////////
// class ITResourceAttribute
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>ITResourceAttribute</code> implements the base functionality
 ** for describing the attributes of an IT Resource.
 ** <br>
 ** An IT Resource may have attriutes or not. Some of them are mandatory for
 ** the functionality of a scheduled task.
 ** <p>
 ** This class extends the {@link AbstractAttribute} to achive type safetivity.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class ITResourceAttribute extends AbstractAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>ITResourceAttribute</code> which has a name and is
   ** optional for a specific task.
   **
   ** @param  id                 the identifier of the attribute.
   **                            Allowed object {@link String}.
   */
  private ITResourceAttribute(final String id) {
    // ensure inheritance
    super(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>ITResourceAttribute</code> which has a name and the
   ** default value which this attribute provides in case it is not defined.
   ** <br>
   ** The attribute will be an optional attribute.
   **
   ** @param  id                 the identifier of the attribute.
   **                            Allowed object {@link String}.
   ** @param  defaultValue       the default value for this attribute.
   **                            Allowed object {@link String}.
   */
  private ITResourceAttribute(final String id, final String defaultValue) {
    // ensure inheritance
    super(id, defaultValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>ITResourceAttribute</code> which has a name and is
   ** optional/mandatory according to the passed parameter.
   **
   ** @param  id                 the identifier of the attribute.
   **                            Allowed object {@link String}.
   ** @param  mandatory          <code>true</code> the attribute must be
   **                            declared for the task; otherwise
   **                            <code>false</code>.
   */
  private ITResourceAttribute(final String id, final boolean mandatory) {
    // ensure inheritance
    super(id, mandatory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>ITResourceAttribute</code> which has a
   ** name and the default value which this attribute provides in case it is not
   ** defined.
   ** <br>
   ** The attribute will be an optional attribute.
   **
   ** @param  id                 the identifier of the attribute.
   **                            Allowed object {@link String}.
   ** @param  defaultValue       the default value for this attribute.
   **                            Allowed object <code>int</code>.
   **
   ** @return                    an instance of <code>ITResourceAttribute</code>
   **                            with the value provided.
   */
  public static ITResourceAttribute build(final String id, final int defaultValue) {
    return new ITResourceAttribute(id, String.valueOf(defaultValue));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>ITResourceAttribute</code> which has a
   ** name and the default value which this attribute provides in case it is not
   ** defined.
   ** <br>
   ** The attribute will be an optional attribute.
   **
   ** @param  id                 the identifier of the attribute.
   **                            Allowed object {@link String}.
   ** @param  defaultValue       the default value for this attribute.
   **                            Allowed object <code>long</code>.
   **
   ** @return                    an instance of <code>ITResourceAttribute</code>
   **                            with the value provided.
   */
  public static ITResourceAttribute build(final String id, final long defaultValue) {
    return new ITResourceAttribute(id, String.valueOf(defaultValue));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>ITResourceAttribute</code> which has a
   ** name and the default value which this attribute provides in case it is not
   ** defined.
   ** <br>
   ** The attribute will be an optional attribute.
   **
   ** @param  id                 the identifier of the attribute.
   **                            Allowed object {@link String}.
   ** @param  defaultValue       the default value for this attribute.
   **                            Allowed object {@link String}.
   **
   ** @return                    an instance of <code>ITResourceAttribute</code>
   **                            with the value provided.
   */
  public static ITResourceAttribute build(final String id, final String defaultValue) {
    return new ITResourceAttribute(id, defaultValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>ITResourceAttribute</code> which has a
   ** name and is optional/mandatory according to the passed parameter
   ** <code>mandatory</code>.
   **
   ** @param  id                 the identifier of the attribute.
   **                            Allowed object {@link String}.
   ** @param  mandatory          <code>true</code> the attribute must be
   **                            declared for the task; otherwise
   **                            <code>false</code>.
   **
   ** @return                    an instance of <code>ITResourceAttribute</code>
   **                            with the value provided.
   */
  public static ITResourceAttribute build(final String id, final boolean mandatory) {
    return new ITResourceAttribute(id, mandatory);
  }
}