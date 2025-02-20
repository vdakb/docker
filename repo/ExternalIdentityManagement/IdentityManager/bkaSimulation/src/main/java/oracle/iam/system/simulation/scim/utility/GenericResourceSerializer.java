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

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   GenericResourceSerializer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    GenericResourceSerializer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.utility;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import oracle.iam.system.simulation.scim.schema.Support;
import oracle.iam.system.simulation.scim.schema.GenericResource;

////////////////////////////////////////////////////////////////////////////////
// abstract class GenericResourceSerializer
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Serializes SCIM 2 generic resource values to {@link GenericResource}
 ** objects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class GenericResourceSerializer extends JsonSerializer<GenericResource>{

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>GenericResourceSerializer</code> SCIM generic resource
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public GenericResourceSerializer() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serialize (JsonSerializer)
  /**
   ** Method that can be called to ask implementation to serialize values of
   ** type this serializer handles.
   **
   ** @param  value              the {@link Integer} value to serialize
   **                            Must not be <code>null</code>.
   ** @param  generator          the {@link JsonGenerator} used to output
   **                            resulting Json content.
   ** @param  serializers        the {@link SerializerProvider} that can be used
   **                            to get serializers for serializing Objects
   **                            value contains, if any.
   **
   ** @throws IOException        if the parser has an issue to get the
   **                            token text.
   */
  @Override
  public void serialize(final GenericResource value, final JsonGenerator generator, final SerializerProvider serializers)
    throws IOException
    ,      JsonProcessingException {

    Support.objectWriter().writeValue(generator, value.objectNode());
  }
}