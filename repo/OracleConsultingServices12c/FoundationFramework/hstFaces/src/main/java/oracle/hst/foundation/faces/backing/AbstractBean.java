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

    File        :   AbstractBean.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractBean.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    0.0.0.1     2013-05-31  DSteding    First release version
*/

package oracle.hst.foundation.faces.backing;

import java.lang.reflect.Field;

import java.util.Set;
import java.util.Map;
import java.util.LinkedHashMap;

import oracle.hst.foundation.faces.annotation.Attribute;
import oracle.hst.foundation.faces.annotation.DisplayName;
import oracle.hst.foundation.faces.annotation.Identifier;

public class AbstractBean {

  private AbstractAttribute identifier;
  private AbstractAttribute displayName;
  private Map<String, AbstractAttribute> attributeInfos;

  AbstractBean(final AbstractAttribute identifier, final AbstractAttribute displayName, final Map<String, AbstractAttribute> attributeInfos) {
    this.identifier = identifier;
    this.displayName = displayName;
    this.attributeInfos = attributeInfos;
  }

  public AbstractAttribute getIdentifier() {
    return this.identifier;
  }

  public AbstractAttribute getDisplayName() {
    return this.displayName;
  }

  public AbstractAttribute getAbstractAttribute(String attributeName) {
    return attributeInfos.get(attributeName);
  }

  public Set<String> getAttributeNames() {
    return attributeInfos.keySet();
  }

  public static AbstractBean create(Class<? extends AbstractModel> clazz) {
    AbstractAttribute idAbstractAttribute = null;
    AbstractAttribute displayNameAbstractAttribute = null;
    Map<String, AbstractAttribute> AbstractAttributes = new LinkedHashMap<String, AbstractAttribute>();
    for (Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(Attribute.class)) {
        String name = field.getName();
        String label = field.getAnnotation(Attribute.class).label();
        if (label == null) {
          label = name;
        }
        Class<?> type = field.getType();
        boolean queryable = field.getAnnotation(Attribute.class).queryable();
        AbstractAttribute attrInfo = new AbstractAttribute(name, label, type, queryable);
        if (field.isAnnotationPresent(Identifier.class)) {
          if (idAbstractAttribute != null) {
            throw new IllegalArgumentException("Two Id attributes defined for bean " + clazz);
          }
          idAbstractAttribute = attrInfo;
        }
        if (field.isAnnotationPresent(DisplayName.class)) {
          if (displayNameAbstractAttribute != null) {
            throw new IllegalArgumentException("Two DisplayName attributes defined for bean " + clazz);
          }
          displayNameAbstractAttribute = attrInfo;
        }
        AbstractAttributes.put(name, attrInfo);
      }
    }
    if (idAbstractAttribute == null) {
      throw new IllegalArgumentException("No Id attribute specified for bean " + clazz);
    }
    if (displayNameAbstractAttribute == null) {
      throw new IllegalArgumentException("No DisplayName attribute specified for bean " + clazz);
    }
    return new AbstractBean(idAbstractAttribute, displayNameAbstractAttribute, AbstractAttributes);
  }
}