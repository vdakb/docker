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

    File        :   ScopedEvaluator.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ScopedEvaluator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jsf.utility;

import java.util.Map;
import java.util.HashMap;

import javax.faces.context.FacesContext;

////////////////////////////////////////////////////////////////////////////////
// final class ScopedEvaluator
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** This class helps in letting code executed within its own scope.
 ** <br>
 ** Such scope is defined by specific variables being available to EL within it.
 ** <br>
 ** The request scope is used to store the variables.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ScopedEvaluator {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  private final FacesContext        context;
  private final Map<String, Object> variable;
	private final Map<String, Object> previous= new HashMap<>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ScopedEvaluator</code> leveraging the specified
   ** {@link FacesContext}.
   **
   ** @param  context            the {@link FacesContext} involved.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   ** @param  variable           the initial values of this
   **                            <code>ScopedEvaluator</code>.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   */
  private ScopedEvaluator(final FacesContext context, final Map<String, Object> variable) {
    // ensure inheritance
    super();

    // initalize instance attributes
		this.context  = context;
		this.variable = variable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   with
	/**
	 ** Adds the given variable to this instance.
   **
   ** @param  key                the key name of the variable.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value of the variable
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the <code>ScopedEvaluator</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object <code>ScopedEvaluator</code>.
	 */
	public final ScopedEvaluator with(String key, Object value) {
		this.variable.put(key, value);
		return this;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create <code>ScopedEvaluator</code>.
   **
   ** @param  context            the {@link FacesContext} involved.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   **
   ** @return                    the <code>ScopedEvaluator</code> created for
   **                            the specified {@link FacesContext}.
   **                            <br>
   **                            Possible object is
   **                            <code>ScopedEvaluator</code>.
   */
  public static ScopedEvaluator of(final FacesContext context) {
    return new ScopedEvaluator(context, new HashMap<>());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   forEach
  /**
   ** Invokes the callback within the scope of the given variable.
   **
   ** @param  context            the {@link FacesContext} involved.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   ** @param  key                the key name of the variable.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value of the variable
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  callback           the callback to invoke after evaluation.
   **                            <br>
   **                            Allowed object is {@link Runnable}.
   */
  public static void forEach(final FacesContext context, final String key, final Object value, final Runnable callback) {
    new ScopedEvaluator(context, new HashMap<>()).with(key, value).invoke(callback::run);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   forEach
  /**
   ** Invokes the callback within the scope of the given variable.
   **
   ** @param  context            the {@link FacesContext} involved.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   ** @param  variable           the initial values of this
   **                            <code>ScopedEvaluator</code>.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   ** @param  callback           the callback to invoke after evaluation.
   **                            <br>
   **                            Allowed object is {@link Runnable}.
   */
  public static void forEach(final FacesContext context, final Map<String, Object> variable, final Runnable callback) {
    new ScopedEvaluator(context, variable).invoke(callback::run);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invokes the callback within the scope of the variables being given in the
   ** constructor.
   **
   ** @param  callback           the callback to invoke after evaluation.
   **                            <br>
   **                            Allowed object is {@link Callback.Void}.
   */
  public void invoke(Callback.Void callback) {
    try {
      backup();
      callback.invoke();
    }
    finally {
      restore();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   backup
  /**
   ** Backup request scope mapping.
   */
	private void backup() {
		this.previous.clear();
    final Map<String, Object> requestMap = this.context.getExternalContext().getRequestMap();
    for (Map.Entry<String, Object> cursor : this.variable.entrySet()) {
			final Object value = requestMap.put(cursor.getKey(), cursor.getValue());
      if (cursor.getValue() != null) {
				this.previous.put(cursor.getKey(), value);
			}
		}
	}
  //////////////////////////////////////////////////////////////////////////////
  // Method:   restore
  /**
   ** Restore request scope mapping.
   */
	private void restore() {
		try {
			Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
			for (Map.Entry<String, Object> entry : this.variable.entrySet()) {
				final Object value = this.previous.get(entry.getKey());
				if (value != null) {
					requestMap.put(entry.getKey(), value);
				} else {
					requestMap.remove(entry.getKey());
				}
			}
		}
    finally {
			this.previous.clear();
		}
	}
}
