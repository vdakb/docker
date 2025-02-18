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

    Copyright © 2018. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facilities

    File        :   TemplateApplicationWizard.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    TemplateApplicationWizard.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.74  2018-05-15  DSteding    First release version
*/

package oracle.jdeveloper.workspace.iam.wizard;

import java.util.Map;

import oracle.ide.model.Workspace;

import oracle.ide.extension.rules.RuleFunction;
import oracle.ide.extension.rules.RuleFunctionParameter;
import oracle.ide.extension.rules.RuleEvaluationContext;
import oracle.ide.extension.rules.RuleEvaluationException;

////////////////////////////////////////////////////////////////////////////////
// class TemplateApplicationRule
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Implementation of the "Oracle Identity and Access Management Application"
 ** {@link RuleFunction}.
 ** <p>
 ** The rule evaluates if the current workspace contains a parameter
 ** {@link #PROPERTY_NAME_KEY} and the value for that parameter is equal to the
 ** value specified by parameter {@link #PROPERTY_VALUE_KEY}
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   12.2.1.3.42.60.74
 */
public class TemplateApplicationRule extends RuleFunction {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String PROPERTY_NAME_KEY  = "property-name";
  private static final String PROPERTY_VALUE_KEY = "property-value";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TemplateApplicationRule</code> that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TemplateApplicationRule() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   evaluate (RuleFunction)
  /**
   ** Handle the events that this controller is asked to handle
   **
   ** @param  context            the evaluation context
   ** @param  parameters         the collection of parameters.
   **
   ** @return                    <code>true</code> if the workspace contains a
   **                            parameter {@link #PROPERTY_NAME_KEY} and the
   **                            value for that parameter is equal to the
   **                            parameter value {@link #PROPERTY_VALUE_KEY};
   **                            otherwise <code>false</code>.
   */
  @Override
  public boolean evaluate(final RuleEvaluationContext context, final Map<String, RuleFunctionParameter> parameters)
    throws RuleEvaluationException {

    final RuleFunctionParameter parameterName  = getRequiredParameterOrThrow(parameters, PROPERTY_NAME_KEY);
    final RuleFunctionParameter parameterValue = getRequiredParameterOrThrow(parameters, PROPERTY_VALUE_KEY);
    final Workspace             workspace      = context.getIdeContext().getWorkspace();
    if (workspace != null) {
      final String key   = parameterName.getValue();
      final String value = parameterValue.getValue();
      return value.equals(workspace.getProperty(key));
    }
    return false;
  }
}
