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

    File        :   NotMutable.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the annotation
                    NotMutable.


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
// annotation NotMutable
// ~~~~~~~~~~ ~~~~~~~~~~
/**
 ** This annotation type is used to indicate that instances of the associated
 ** class may not be altered after they have been created.  Note that this may
 ** or may not indicate strict immutability, as some classes marked with this
 ** annotation type may have their internal state altered in some way that is
 ** not externally visible.
 ** <br>
 ** In addition, the following caveats must be observed for classes containing
 ** this annotation type, and for all other classes in the framework:
 ** <ul>
 **   <li>If an array is provided as an argument to a constructor or a method,
 **       then that array must not be referenced or altered by the caller at any
 **       time after that point unless it is clearly noted that it is acceptable
 **       to do so.
 **   <li>If an array is returned by a method, then the contents of that array
 **       must not be altered unless it is clearly noted that it is acceptable
 **       to do so.
 ** </ul>
 ** It will only be used for classes which are primarily used as data structures
 ** and will not be included in classes whose primary purpose is something other
 ** than as a data type.
 ** <br>
 ** It will also not be used for interfaces, abstract classes, or enums.
 ** <p>
 ** This annotation type will appear in the generated Javadoc documentation for
 ** classes and interfaces that include it.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 **
 ** @see     Mutable
 */
@Documented()
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotMutable {
}