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
    Subsystem   :   Connector Bundle Integration

    File        :   ServiceFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.integration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.thortech.xl.dataaccess.tcDataProvider;

////////////////////////////////////////////////////////////////////////////////
// class ServiceFactory
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** There is only one existing instance of the class in a JVM; it is implemented
 ** as singleton.
 ** <br>
 ** Implements the service locator design pattern.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServiceFactory {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor (private)
  /**
   ** Default constructor
   ** <br>
   ** Access modifier private prevents other classes using
   ** "new ServiceFactory()"
   */
  private ServiceFactory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Returns a handle to a {@link Service} available in the intergration
   ** framework.
   **
   ** @param  <T>                the expected class type of the returned
   **                            service.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  service            the Class object corresponding to the Service
   **                            Interface.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            <code>T</code>.
   ** @param  parameter          the parameter values to pass to the created
   **                            service.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    a POJO handle to the Service.
   **                            <br>
   **                            Allowed object is {@link Service} for type
   **                            <code>T</code>.
   **
   ** @throws RuntimeException   if <code>null</code> is passed for
   **                            <code>service</code> or <code>service</code> is
   **                            empty or if object could not be found.
   */
  public static final <T extends Service> T create(final Class<T> service, final Object... parameter) {
    try {
      if ((parameter == null) || (parameter.length == 0)) {
        return service.newInstance();
      }

      final Constructor<T> constructor = service.getConstructor(new Class<?>[] { tcDataProvider.class });
      return constructor.newInstance(parameter);
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    catch (InstantiationException e) {
      throw new RuntimeException(e);
    }
    catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}