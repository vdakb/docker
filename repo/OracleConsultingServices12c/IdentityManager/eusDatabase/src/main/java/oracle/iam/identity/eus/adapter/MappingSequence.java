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

    Copyright © 2011. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Enterprise User Security

    File        :   MappingSequence.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    MappingSequence.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2011-03-01  DSteding    First release version
*/

package oracle.iam.identity.eus.adapter;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.hst.foundation.SystemMessage;

////////////////////////////////////////////////////////////////////////////////
// class MappingSequence
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Adpapter dedicated to operate on the Oracle Identity Manager Enterprise
 ** User Secutity Forms.
 ** <p>
 ** This adapter populates a mappinh sequence number that ensures uniqueness of
 ** a schema mapping entry.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class MappingSequence extends AbstractServiceProvider {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>MappingSequence</code> task adpater that
   ** allows use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   */
  public MappingSequence(final tcDataProvider provider) {
    // ensure inheritance
    super(provider);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generates a Global UID.
   **
   ** @param  formName           the name of the process form within the mapping
   **                            name has to ensure uniquness.
   ** @param  formField          the name of the field in the process form that
   **                            provides and stores the mapping name.
   ** @param  prefixName         the string to prefix the name to generate.
   **
   ** @return                    the passed String with the replaced characters
   */
  public String generate(final String formName, final String formField, final String prefixName) {
    return generate(formName, formField, prefixName, "%1$s%2$d");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generates a Global UID the meets the requirements of the specified format.
   **
   ** @param  formName           the name of the process form within the mapping
   **                            name has to ensure uniquness.
   ** @param  formField          the name of the field in the process form that
   **                            provides and stores the mapping name.
   ** @param  prefixName         the string to prefix the name to generate.
   ** @param  paddingFormat      the format string indicate how the arguments
   **                            should be processed and where they should be
   **                            inserted in the text.
   **
   ** @return                    the passed String with the replaced characters
   */
  public String generate(final String formName, final String formField, final String prefixName, final String paddingFormat) {
    final String method = "generate";
    trace(method, SystemMessage.METHOD_ENTRY);

    long id    = 0L;
    // the result is formatted as a decimal integer
    String guid = String.format(paddingFormat, prefixName, id);
    trace(method, SystemMessage.METHOD_EXIT);
    return guid;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formates the passed identifier to the required format.
   **
   ** @param  identifier         the identifier to convert.
   **                            <b>Note:</b>
   **                            Must be a decimal value.
   ** @paran  format             the format string indicate how the arguments
   **                            should be processed and where they should be
   **                            inserted in the text.
   **
   ** @return                    the passed String with the replaced characters
   */
  private String format(long identifier, final String format) {
    // the result is formatted as a decimal integer
    return String.format(format, identifier);
  }
}