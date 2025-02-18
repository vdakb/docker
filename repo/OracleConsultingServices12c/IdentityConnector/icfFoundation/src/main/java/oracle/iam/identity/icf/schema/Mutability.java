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
    Subsystem   :   Foundation Shared Library

    File        :   Mutability.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the enum
                    Mutability.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.schema;

////////////////////////////////////////////////////////////////////////////////
// enum Mutability
// ~~~~ ~~~~~~~~~~
/**
 ** The <code>Mutability</code> implements the constraint for describing an
 ** attribute mutability in an ICF connector schema.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum Mutability {
    /**
     ** indicates whether the target-application generates or modifies
     ** value(s) for attribute during create or update operations.
     */
    GENERATED
    /**
     ** the attribute can be read and written.
     */
 ,  MUTABLE
    /**
     ** the attribute can only be read, and not written after object creation
     ** (unless it was not provided in that moment).
     ** <br>
     ** This might be used for operational attributes for example.
     */
 ,  IMMUTABLE
    /**
     ** the attribute can only be written, and not read
     ** <br>
     ** This might be used for password hashes for example.
     */
 ,  WRITEONLY
    /**
     ** the attribute can be written, and cannot be set after object creation
     ** <br>
     ** It can be set during object creation.
     */
  , CREATABLE
    /** Determines if the attribute writable during update. */
  , UPDATABLE
  ;
}