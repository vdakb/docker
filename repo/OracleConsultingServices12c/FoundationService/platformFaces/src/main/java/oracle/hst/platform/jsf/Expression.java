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
    Subsystem   :   Java Server Faces Feature

    File        :   Expression.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Expression.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jsf;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.lang.annotation.Annotation;

import javax.el.ELContext;
import javax.el.ValueReference;
import javax.el.ValueExpression;
import javax.el.ExpressionFactory;
import javax.el.PropertyNotFoundException;

import javax.faces.component.UIComponent;

import javax.faces.view.facelets.FaceletContext;

////////////////////////////////////////////////////////////////////////////////
// abstract class Expression
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** Collection of helper methods dealing with the JSF Expression language.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Expression {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Expression</code>.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Expression()" and enforces use of the public method below.
   */
  private Expression() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   annotation
  /**
   ** Determines which annotations are given to an object displayed by a JSF
   ** component.
   **
   ** @param  component          the {@link UIComponent} with a
   **                            {@link ValueExpression} declared on it.
   **                            <br>
   **                            Allowed object is {@link UIComponent}.
   **
   ** @return                    <code>null</code> if there are no annotations,
   **                            or if they cannot be accessed.
   **                            <br>
   **                            Possible object is array of {@link Annotation}.
   */
  public static Annotation[] annotation(final UIComponent component) {
    final ValueExpression expression = component.getValueExpression("value");
    if (expression != null && expression.getExpressionString() != null && expression.getExpressionString().length()>0) {
      return annotation(expression);
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   annotation
  /**
   ** Determines which annotations are given to an object described by an EL
   ** expression.
   **
   ** @param  expression         the EL expression of the JSF bean attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>null</code> if there are no annotations,
   **                            or if they cannot be accessed.
   **                            <br>
   **                            Possible object is array of {@link Annotation}.
   */
  public static Annotation[] annotation(final ValueExpression expression) {
    final ELContext context = Faces.expressionContext();
    try {
      ValueReference valueReference = expression.getValueReference(context);
      Object         base;
      if (null == valueReference) {
        base = evalMojarra(context, expression);
      }
      else {
        base = valueReference.getBase();
      }
      if (null == base) {
        return null;
      }
      Field field = field(base, expression.getExpressionString());
      if (field != null ) {
        return field.getAnnotations();
      }
      Method getter = getter(base, expression.getExpressionString());
      if (getter != null) {
        return getter.getAnnotations();
      }
    }
    catch (PropertyNotFoundException e) {
      // this happens if a bean is null.
      // that's a legal state, so suffice it to return no annotation.
      return null;
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Parses and returns an expression into a {@link String}.
   ** <p>
   ** This performs syntactic validation of the expression. If in doing so it
   ** detects errors, it raise an ELException.
   **
   ** @param  expression         the expression to parse.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the string value evaluated from the given
   **                            <code>expression</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String string(final String expression) {
    return eval(expression, String.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   object
  /**
   ** Parses and returns an expression into a {@link Object}.
   ** <p>
   ** This performs syntactic validation of the expression. If in doing so it
   ** detects errors, it raise an ELException.
   **
   ** @param  expression         the expression to parse.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the string value evaluated from the given
   **                            <code>expression</code>.
   **                            <br>
   **                            Possible object is {@link Object}.
   */
  public static Object object(final String expression) {
    return eval(expression, Object.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eval
  /**
   ** Evaluates EL expression and returns value.
   **
   ** @param  <T>                the expected type of the expression.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  expression         the expression to parse.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clazz              the type the result of the expression will be
   **                            coerced to after evaluation.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   **
   ** @return                    the value of the {@link ValueExpression}.
   */
  public static <T> T eval(final String expression, final Class<T> clazz) {
    return (T)valueExpression(expression, clazz).getValue(Faces.expressionContext());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueExpression
  /**
   ** Parses and returns an expression into a {@link ValueExpression} for later
   ** evaluation. Use this method for expressions that refer to values.
   ** <p>
   ** This performs syntactic validation of the expression. If in doing so it
   ** detects errors, it raise an ELException.
   **
   ** @param  expression         the expression to parse.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clazz              the type the result of the expression will be
   **                            coerced to after evaluation.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   **
   ** @return                    the {@link ValueExpression}.
   **                            <br>
   **                            Possible object is {@link ValueExpression}.
   */
  public static ValueExpression valueExpression(final String expression, final Class<?> clazz) {
    final ExpressionFactory factory = Faces.expressionFactory();
    final ELContext         context = Faces.expressionContext();
    return factory.createValueExpression(context, expression,clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   evalMojarra
  private static Object evalMojarra(final ELContext context, final ValueExpression expression) {
    String exp = expression.getExpressionString();
    int endOfBaseName   = exp.lastIndexOf('.');
    int mapDelimiterPos = exp.lastIndexOf('[');
    if (mapDelimiterPos >= 0) {
      int mapEndDelimiterPos = exp.lastIndexOf(']');
      if (endOfBaseName < mapEndDelimiterPos) {
        // treat the [...] as field
        endOfBaseName = mapDelimiterPos;
      }
    }

    if (endOfBaseName < 0) {
//      LOGGER.log(Level.WARNING, "There's no getter to access: #{" + p_expression + "}");
      return null;
    }
    String basename = exp.substring(2, endOfBaseName);
    Object result   = object("#{" + basename + "}");
    if (result != null) {
      return result;
    }

    int start = 0;
    int dot  = basename.indexOf('.', start);
    int arr  = basename.indexOf('[', start);
    if (arr >= 0 && arr < dot) {
      dot = arr;
    }
    if (dot < 0) {
      dot = basename.length();
    }
    final String         variable = basename.substring(start, dot);
    final FaceletContext facelet  = (FaceletContext)Faces.context().getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
    Object resolvedBase = facelet.getAttribute(variable);
    if (resolvedBase != null) {
      if (endOfBaseName == dot + 2) {
        result = resolvedBase;
      } else {
        basename = basename.substring(dot + 1, endOfBaseName - 2);
        result = context.getELResolver().getValue(context, resolvedBase, basename);
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   field
  private static Field field(final Object container, final String expression) {
    // prevent bogus input
    if (container == null)
      return null;

    if (expression.startsWith("#{") && expression.endsWith("}")) {
      // the following code covers these use cases:
      // #{someBean.someField}
      // #{someBean.someMap['someField']}
      // #{someBean.someMap[someBean.fieldName]}
      // #{someBean.someMap[someBean.fieldName].someAttribute}
      int delimiterPos    = expression.lastIndexOf('.');
      int mapDelimiterPos = expression.lastIndexOf('[');
      if (mapDelimiterPos >= 0) {
        int mapEndDelimiterPos = expression.lastIndexOf(']');
        if (delimiterPos < mapEndDelimiterPos) {
          delimiterPos = mapDelimiterPos; // treat the [...] as field
        }
      }
      if (delimiterPos < 0) {
        return null;
      }
      String                  name  = expression.substring(delimiterPos + 1, expression.length() - 1);
      Class<? extends Object> clazz = container.getClass();
      while (clazz != null) {
        Field declaredField;
        try {
          declaredField = clazz.getDeclaredField(name);
          return declaredField;
        }
        catch (NoSuchFieldException e) {
          // let's try with the super class
          clazz = clazz.getSuperclass();
        }
        catch (SecurityException e) {
          return null;
        }
      }
    }
    return null;
  }

  private static Method getter(final Object container, final String expression) {
    // prevent bogus input
    if (container == null)
      return null;

    if (expression.startsWith("#{") && expression.endsWith("}")) {
      // the following code covers these use cases:
      // #{someBean.someField}
      // #{someBean.someMap['someField']}
      // #{someBean.someMap[someBean.fieldName]}
      // #{someBean.someMap[someBean.fieldName].someAttribute}
      int delimiterPos    = expression.lastIndexOf('.');
      int mapDelimiterPos = expression.lastIndexOf('[');
      if (mapDelimiterPos >= 0) {
        int mapEndDelimiterPos = expression.lastIndexOf(']');
        if (delimiterPos < mapEndDelimiterPos) {
          // the last part of the expression is a map access, so there's no getter
          return null;
        }
      }
      if (delimiterPos < 0) {
        return null;
      }
      String name   = expression.substring(delimiterPos + 1, expression.length() - 1);
      String getter = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
      Method method = method(container, getter);
      if (method == null) {
        getter = "is" + name.substring(0, 1).toUpperCase() + name.substring(1);
        method = method(container, getter);
      }
      return method;
    }
    return null;
  }

  private static Method method(final Object container, final String name) {
    Class<? extends Object> c = container.getClass();
    Method                  declared;
    try {
      declared = c.getMethod(name);
      return declared;
    }
    catch (NoSuchMethodException e) {
      // let's try with the super class
      c = c.getSuperclass();
    }
    catch (SecurityException e) {
      ;
    }
    return null;
  }
}