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
    Subsystem   :   Generic REST Library

    File        :   MarshallFeature.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    MarshallFeature.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest.feature;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import com.fasterxml.jackson.jaxrs.base.JsonParseExceptionMapper;
import com.fasterxml.jackson.jaxrs.base.JsonMappingExceptionMapper;

///////////////////////////////////////////////////////////////////////////////
// class MarshallFeature
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** {@link Feature} used to register Jackson JSON providers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class MarshallFeature implements Feature {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>MarshallFeature</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public MarshallFeature() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure (Feature)
  /**
   ** A call-back method called when the feature is to be enabled in a given
   ** runtime configuration scope. The responsibility of the feature is to
   ** properly update the supplied runtime configuration context and return
   ** <code>true</code> if the feature was successfully enabled or
   ** <code>false</code> otherwise.
   ** <br>
   ** <b>Note</b>
   ** <br>
   ** Under some circumstances the feature may decide not to enable itself,
   ** which is indicated by returning <code>false</code>. In such case the
   ** configuration context does not add the feature to the collection of
   ** enabled features and a subsequent call to Configuration.isEnabled(Feature)
   ** or Configuration.isEnabled(Class) method would return <code>false</code>.
   **
   ** @param  context            the configurable context in which the feature
   **                            should be enabled.
   **
   ** @return                    <code>true</code> if the feature was
   **                            successfully enabled, <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean configure(final FeatureContext context) {
    context.register(JsonParseExceptionMapper.class);
    context.register(JsonMappingExceptionMapper.class);
    context.register(BindingFeature.class, MessageBodyReader.class, MessageBodyWriter.class);
    return true;
  }
}