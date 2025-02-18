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

    File        :   AbstractConverter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractConverter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    0.0.0.1     2013-05-31  DSteding    First release version
*/

package oracle.hst.foundation.faces.converter;

import java.util.Collection;
import java.util.Collections;

import javax.faces.convert.Converter;
import javax.faces.context.FacesContext;

import javax.faces.component.UIComponent;

import org.apache.myfaces.trinidad.convert.ClientConverter;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractConverter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Basic implementation of a {@link Converter} interface describing a Java
 ** class that can perform Object-to-String and String-to-Object conversions
 ** between model data objects and a String representation of those objects that
 ** is suitable for rendering.
 ** <p>
 ** {@link Converter} implementations must have a zero-arguments public
 ** constructor. In addition, if the {@link Converter} class wishes to have
 ** configuration property values saved and restored with the component tree,
 ** the implementation must also implement StateHolder.
 ** <p>
 ** Starting with version 1.2 of the specification, an exception to the above
 ** zero-arguments constructor requirement has been introduced. If a converter
 ** has a single argument constructor that takes a Class instance and the Class
 ** of the data to be converted is known at converter instantiation time, this
 ** constructor must be used to instantiate the converter instead of the
 ** zero-argument version. This enables the per-class conversion of Java
 ** enumerated types.
 ** <p>
 ** If any {@link Converter} implementation requires a java.util.Locale to
 ** perform its job, it must obtain that Locale from the UIViewRoot of the
 ** current FacesContext, unless the Converter maintains its own Locale as part
 ** of its state.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class AbstractConverter implements Converter
                                 ,          ClientConverter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractConverter</code> converter that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractConverter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////


  //////////////////////////////////////////////////////////////////////////////
  // Method:   getClientLibrarySource (Converter)
  @Override
  public String getClientLibrarySource(final FacesContext context) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getClientLibrarySource (ClientConverter)
  @Override
  public Collection<String> getClientImportNames() {
    return Collections.emptySet();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getClientScript (ClientConverter)
  @Override
  public String getClientScript(final FacesContext context, final UIComponent component) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getClientConversion (ClientConverter)
  @Override
  public String getClientConversion(final FacesContext context, final UIComponent component) {
    return null;
  }
}