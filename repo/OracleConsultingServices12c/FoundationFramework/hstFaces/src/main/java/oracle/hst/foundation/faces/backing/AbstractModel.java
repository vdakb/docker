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

    Copyright © 2013. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Consulting Services Foundation Shared Library
    Subsystem   :   Java Server Faces Foundation

    File        :   AbstractModel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractModel.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    0.0.0.1     2013-05-31  DSteding    First release version
*/

package oracle.hst.foundation.faces.backing;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractModel {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private BeanInfo beanInfo;

  /*
   * Generic getter used by common LOV model classes.
   */
  public final Object get(String attrName) {
    final BeanInfo beanInfo = beanInfo();
    for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
      if (descriptor.getName().equals(attrName)) {
        try {
          return descriptor.getReadMethod().invoke(this);
        }
        catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
        catch (InvocationTargetException e) {
          throw new RuntimeException(e);
        }
      }
    }
    throw new IllegalArgumentException("Getter not found for " + attrName);
  }

  private BeanInfo beanInfo() {
    if (this.beanInfo == null) {
      try {
        this.beanInfo = Introspector.getBeanInfo(this.getClass());
      }
      catch (IntrospectionException e) {
        throw new RuntimeException(e);
      }
    }
    return this.beanInfo;
  }
}