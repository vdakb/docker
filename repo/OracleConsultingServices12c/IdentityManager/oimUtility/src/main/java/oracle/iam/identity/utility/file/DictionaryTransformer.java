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
    Subsystem   :   Common Shared Utility Facilities

    File        :   DictionaryTransformer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DictionaryTransformer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.HashMap;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.Transformer;
import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class DictionaryTransformer
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>DictionaryTransformer</code> translate a String to another
 ** based on the translation dictionary passed to the constructor
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class DictionaryTransformer<K, V> extends    HashMap<K, V>
                                         implements Transformer<V> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3215952051832279000")
  private static final long serialVersionUID = -6839961653897387555L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the default value this transformer returns as the translation if the
   ** desired origin is not found in the dictionary.
   */
  private String defaultValue;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DictionaryTransformer</code> attribute
   ** transformer that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   ** <p>
   ** The default initial capacity (16) and the default load factor (0.75).
   */
  public DictionaryTransformer() {
    // ensure inharitance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DictionaryTransformer</code> attribute
   ** transformer with the specified initial capacity and the default load
   ** factor (0.75).
   **
   ** @param  initialCapacity    the initial capacity.
   **
   ** @throws IllegalArgumentException if the initial capacity is negative.
   */
  public DictionaryTransformer(final int initialCapacity) {

    // ensure inharitance
    super(initialCapacity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DictionaryTransformer</code> attribute
   ** transformer with the specified initial capacity and load factor.
   **
   ** @param  initialCapacity    the initial capacity.
   ** @param  loadFactor         the load factor.
   **
   ** @throws IllegalArgumentException if the initial capacity is negative or
   **                                  the load factor is nonpositive.
   */
  public DictionaryTransformer(final int initialCapacity, final float loadFactor) {

    // ensure inharitance
    super(initialCapacity, loadFactor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>DictionaryTransformer</code> attribute
   ** transformer with the same mappings as the specified
   ** <code>DictionaryTransformer</code>.
   ** <p>
   ** The <code>DictionaryTransformer</code> is created with default load
   ** factor (0.75) and an initial capacity sufficient to hold the mappings in
   ** the specified <code>DictionaryTransformer</code>.
   **
   ** @param  transformer        the <code>DictionaryTransformer</code>
   **                            whose translation mappings are to be placed in
   **                            this transformer.
   **
   ** @throws  NullPointerException if the specified
   **                               <code>DictionaryTransformer</code> is null.
   */
  public DictionaryTransformer(final DictionaryTransformer<K, V> transformer) {
    // ensure inheritance
    super(transformer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors and Mutators
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDefault
  /**
   ** Sets the default value this transformer returns as the translation if the
   ** desired origin is not found in the dictionary.
   **
   ** @param  defaultValue       the default value to set for this transformer
   **                            that will be returned as the translation if the
   **                            desired origin is not found in the dictionary.
   */
  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDefault
  /**
   ** Returns the default value this transformer returns as the translation if
   ** the desired origin is not found in the dictionary.
   **
   ** @return                    the default value this transformer returns as
   **                            the translation if the desired origin is not
   **                            found in the dictionary.
   */
  public String getDefaultValue() {
    return this.defaultValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform (Transformer)
  /**
   ** Returns the specified <code>origin</code> as an appropriate
   ** translation.
   ** <br>
   ** If the specified <code>origin</code> is not contained in the dictionary of
   ** this transformer the defaultValue will be returned.
   **
   ** @param  origin             the <code>Object</code> to transform.
   **
   ** @return                    the translation of the specified
   **                            <code>origin</code>.
   **
   ** @throws ClassCastException if the provided <code>Object</code> is
   **                            not castable to a <code>String</code>.
   */
  @SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
  public Object transform(final Object origin) {
    return this.transform(origin, defaultValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform (Transformer)
  /**
   ** Returns the specified <code>origin</code> as an appropriate
   ** translation.
   **
   ** @param  origin             the <code>Object</code> to translate.
   ** @param  defaultValue       the <code>String</code> to return if the
   **                            origin is not mapped.
   **
   ** @return                    the translation of the specified
   **                            <code>origin</code>.
   **
   ** @throws ClassCastException if the provided <code>Object</code> is
   **                            not castable to a <code>String</code>.
   */
  public Object transform(final Object origin, final String defaultValue) {
    String value = (String)this.get(origin);
    if (StringUtility.isEmpty(value))
      value = defaultValue;
    if (StringUtility.isEmpty(value))
      value = SystemConstant.EMPTY;
    return value;
  }
}