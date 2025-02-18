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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Configuration Extension
    Subsystem   :   Common Shared Utility

    File        :   MethodValidator.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    MethodValidator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.config;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

////////////////////////////////////////////////////////////////////////////////
// class MethodValidator
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** A validator for {@link ValidationMethod}-annotated methods.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class MethodValidator implements ConstraintValidator<ValidationMethod, Boolean> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>MethodValidator</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public MethodValidator() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (ConstraintValidator)
  /**
   ** Initializes the validator in preparation for
   ** isValid(Object, ConstraintValidatorContext) calls.
   ** <br>
   ** The constraint annotation for a given constraint declaration is passed.
   ** <p>
   ** This method is guaranteed to be called before any use of this instance for
   ** validation.
   **
   ** @param  annotation         the annotation instance for a given constraint
   **                            declaration.
   */
  public final void initialize(final ValidationMethod annotaion) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isValid (ConstraintValidator)
  /**
   ** Implements the validation logic.
   ** <br>
   ** The state of value must <b>not</b> be altered.
   ** <p>
   ** This method can be accessed concurrently, thread-safety must be ensured by
   ** the implementation.
   **
   ** @param  value              the value to validate.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   ** @param  context            the context in which the constraint is
   **                            evaluated.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConstraintValidatorContext}.
   **
   ** @return                    <code>false</code> if value does not pass the
   **                            constraint; otherwise <code>true</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean isValid(final Boolean value, final ConstraintValidatorContext context) {
    return (value == null) || value;
  }
}