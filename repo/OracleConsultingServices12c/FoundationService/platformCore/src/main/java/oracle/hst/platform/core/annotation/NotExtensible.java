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

    System      :   Foundation Service Extension
    Subsystem   :   Common Shared Utility

    File        :   NotExtensible.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the annotation
                    NotExtensible.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.core.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

////////////////////////////////////////////////////////////////////////////////
// annotation NotExtensible
// ~~~~~~~~~~ ~~~~~~~~~~~~~
/**
 ** This annotation type is used to indicate that a non-final class or interface
 ** is NOT intended to be extended or implemented by third-party code.
 ** <br>
 ** In order to be completely safe, third-party code should only extend or
 ** implement code marked with the <code>@Extensible</code> annotation type, but
 ** the <code>@NotExtensible</code> annotation type can serve as a reminder for
 ** classes or interfaces that are not intended to be extended or implemented by
 ** third-party code.
 ** <p>
 ** This annotation type will appear in the generated Javadoc documentation for
 ** classes and interfaces that include it.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 **
 ** @see     Extensible
 */
@Documented()
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotExtensible {
}