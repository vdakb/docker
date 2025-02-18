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

    System      :   Presistence Foundation Shared Library
    Subsystem   :   Generic Persistence Interface

    File        :   Transactional.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the annotation
                    Transactional.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa.injection;

import java.lang.annotation.Target;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import javax.enterprise.inject.Any;

import javax.enterprise.util.Nonbinding;

import javax.interceptor.InterceptorBinding;

////////////////////////////////////////////////////////////////////////////////
// annotation Transactional
// ~~~~~~~~~~ ~~~~~~~~~~~~~
/**
 ** Marks method as working in transaction.
 ** <p>
 ** If it isn't possible to use EJB's, this interceptor adds transaction support
 ** to methods or a class.
 ** <br>
 ** The optional qualifier can be used to specify different entity managers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Transactional {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   qualifier
  /**
   ** Optional qualifier/s which allow/s to start only specific transactions
   ** instead of one for the injected {@link javax.persistence.EntityManager}s.
   ** <br>
   ** Default-value is {@link Any} which means any injected
   ** {@link javax.persistence.EntityManager}s should be detected automatically
   ** and transactions for all injected {@link javax.persistence.EntityManager}s
   ** will be started. Or the {@link javax.enterprise.inject.Default}
   ** {@link javax.persistence.EntityManager} will be used, if no qualifier and
   ** no {@link javax.persistence.EntityManager} was found (in the annotated
   ** class).
   ** <p>
   ** This qualifier can also be used for integrating other frameworks, as the
   ** usage of {@link javax.persistence.EntityManager}s with qualifiers in a
   ** called method (of a different bean) which isn't {@link Transactional}
   ** itself.
   **
   ** @return                    the target persistence-unit identifier.
   **                            <br>
   **                            Possible object is array of {@link Class} for
   **                            type {@link Annotation}.
   */
  @Nonbinding
  Class<? extends Annotation>[] qualifier() default Any.class;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readOnly
  /**
   ** Only evaluated on the first/outermost transactional bean/method in the
   ** chain
   **
   ** @return                    <code>true</code> to trigger #rollback for the
   **                            current transaction(s), <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Nonbinding
  boolean readOnly() default false;
}