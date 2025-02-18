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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   XMLStyle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    XMLStyle.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.74  2018-05-15  DSteding    First release version
*/

package oracle.jdeveloper.workspace.iam.xml;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// interface XMLStyle
// ~~~~~~~~~ ~~~~~~~~
/**
 ** The <code>XMLStyle</code> interface is used to represent an XML style that
 ** can be applied to a serialized object.
 ** <p>
 ** A style can be used to modify the element and attribute names for the
 ** generated document.
 ** <p>
 ** <code>XMLStyle</code>s can be used to generate hyphenated or camel case XML.
 ** <pre>
 **   &lt;example-element&gt;
 **     &lt;child-element example-attribute='example'&gt;
 **       &lt;inner-element&gt;example&lt;/inner-element&gt;
 **     &lt;/child-element&gt;
 **   &lt;/example-element&gt;
 ** </pre>
 ** Above the hyphenated XML elements and attributes can be generated from a
 ** style implementation. <code>XMLStyle</code>s enable the same objects to be
 ** serialized in different ways, generating different styles of XML without
 ** having to modify the class schema for that object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public interface XMLStyle extends Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class NULL
  // ~~~~~ ~~~~
  /**
   ** The <code>NULL</code> style object is used to represent a style that does
   ** not modify the tokens passed in to it. This is used if there is no style
   ** specified or if there is no need to convert the XML elements an attributes
   ** to a particular style. This is also the most performant style as it does
   ** not require cache lookups.
   */
  public static class NULL implements XMLStyle {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with

    @SuppressWarnings("compatibility:7506488472294923578")
    private static final long serialVersionUID = 8386928786996190835L;

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: element (XMLStyle)
    /**
     ** This is used to generate the XML element representation of the specified
     ** name.
     ** <p>
     ** Element names should ensure to keep the uniqueness of the name such that
     ** two different names will be styled in to two different strings.
     **
     ** @param  name             this is the element name that is to be styled.
     **
     ** @return                  the styled name of the XML element.
     */
    @Override
    public final String element(final String name) {
      return name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: attribute (XMLStyle)
    /**
     ** This is used to generate the XML attribute representation of the
     ** specified name.
     ** <p>
     ** Attribute names should ensure to keep the uniqueness of the name such
     ** that two different names will be styled in to two different strings.
     **
     ** @param  name             this is the attribute name that is to be
     **                          styled.
     **
     ** @return                  the styled name of the XML attribute.
     */
    @Override
    public final String attribute(final String name) {
      return name;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element
  /**
   ** This is used to generate the XML element representation of the specified
   ** name.
   ** <p>
   ** Element names should ensure to keep the uniqueness of the name such that
   ** two different names will be styled in to two different strings.
   **
   ** @param  name               this is the element name that is to be styled.
   **
   ** @return                    the styled name of the XML element.
   */
  String element(final String name);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** This is used to generate the XML attribute representation of the specified
   ** name.
   ** <p>
   ** Attribute names should ensure to keep the uniqueness of the name such that
   ** two different names will be styled in to two different strings.
   **
   ** @param  name               this is the attribute name that is to be
   **                            styled.
   **
   ** @return                    the styled name of the XML attribute.
   */
  String attribute(final String name);
}