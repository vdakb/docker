/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Unique Identifier Administration 

    File        :   DialogModel.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DialogModel.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.uid.model;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

////////////////////////////////////////////////////////////////////////////////
// abstract class DialogModel
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~
/**
 ** An arbitrary dialog framework property.
 ** <br>
 ** Convenient implementation of Map.Entry.
 ** <table summary="">
 **   <thead>
 **     <tr>
 **       <th>Name</th>
 **       <th>Default</th>
 **       <th>Type</th>
 **       <th>Description</th>
 **     </tr>
 **   </thead>
 **   <tbody> 
 **     <tr>
 **       <td>modal</td>
 **       <td>false</td>
 **       <td>Boolean</td>
 **       <td>Controls modality of the dialog.</td>
 **     </tr>
 **     <tr>
 **       <td>resizable</td>
 **       <td>true</td>
 **       <td>Boolean</td>
 **       <td>When enabled, makes dialog resizable.</td>
 **     </tr>
 **     <tr>
 **       <td>draggable</td>
 **       <td>true</td>
 **       <td>Boolean</td>
 **       <td>When enabled, makes dialog draggable.</td>
 **     </tr>
 **     <tr>
 **       <td>width</td>
 **       <td>auto</td>
 **       <td>String</td>
 **       <td>Width of the dialog.</td>
 **     </tr>
 **     <tr>
 **       <td>height</td>
 **       <td>auto</td>
 **       <td>String</td>
 **       <td>Height of the dialog.</td>
 **     </tr>
 **     <tr>
 **       <td>contentWidth</td>
 **       <td>640</td>
 **       <td>String</td>
 **       <td>Width of the dialog content.</td>
 **     </tr>
 **     <tr>
 **       <td>contentHeight</td>
 **       <td>640</td>
 **       <td>String</td>
 **       <td>Height of the dialog content.</td>
 **     </tr>
 **     <tr>
 **       <td>closable</td>
 **       <td>true</td>
 **       <td>Boolean</td>
 **       <td>Whether the dialog can be closed or not.</td>
 **     </tr>
 **     <tr>
 **       <td>includeViewParams</td>
 **       <td>false</td>
 **       <td>Boolean</td>
 **       <td>When enabled, includes the view parameters.</td>
 **     </tr>
 **     <tr>
 **       <td>headerElement</td>
 **       <td>null</td>
 **       <td>String</td>
 **       <td>Client id of the element to display inside header.</td>
 **     </tr>
 **     <tr>
 **       <td>minimizable</td>
 **       <td>false</td>
 **       <td>Boolean</td>
 **       <td>Makes dialog minimizable.</td>
 **     </tr>
 **     <tr>
 **       <td>maximizable</td>
 **       <td>false</td>
 **       <td>Boolean</td>
 **       <td>Makes dialog maximizable.</td>
 **     </tr>
 **     <tr>
 **       <td>closeOnEscape</td>
 **       <td>false</td>
 **       <td>Boolean</td>
 **       <td>Whether the dialog can be closed with escape key.</td>
 **     </tr>
 **     <tr>
 **       <td>minWidth</td>
 **       <td>150</td>
 **       <td>Integer</td>
 **       <td>Minimum width of a resizable dialog.</td>
 **     </tr>
 **     <tr>
 **       <td>minHeight</td>
 **       <td>0</td>
 **       <td>Integer</td>
 **       <td>Minimum height of a resizable dialog.</td>
 **     </tr>
 **     <tr>
 **       <td>appendTo</td>
 **       <td>null</td>
 **       <td>String</td>
 **       <td>Appends the dialog to the element defined by the given search expression.</td>
 **     </tr>
 **     <tr>
 **       <td>dynamic</td>
 **       <td>false</td>
 **       <td>Boolean</td>
 **       <td>Enables lazy loading of the content with ajax.</td>
 **     </tr>
 **     <tr>
 **       <td>showEffect</td>
 **       <td>null</td>
 **       <td>String</td>
 **       <td>Effect to use when showing the dialog.</td>
 **     </tr>
 **     <tr>
 **       <td>hideEffect</td>
 **       <td>null</td>
 **       <td>String</td>
 **       <td>Effect to use when hiding the dialog.</td>
 **     </tr>
 **     <tr>
 **       <td>position</td>
 **       <td>null</td>
 **       <td>String</td>
 **       <td>Defines where the dialog should be displayed.</td>
 **     </tr>
 **     <tr>
 **       <td>fitViewport</td>
 **       <td>false</td>
 **       <td>Boolean</td>
 **       <td>In responsive mode, dialog adjusts itself based on screen width.</td>
 **     </tr>
 **     <tr>
 **       <td>responsive</td>
 **       <td>false</td>
 **       <td>Boolean</td>
 **       <td>In responsive mode, dialog adjusts itself based on screen width.</td>
 **     </tr>
 **     <tr>
 **       <td>onShow</td>
 **       <td>null</td>
 **       <td>String</td>
 **       <td>Client side callback to execute when dialog is displayed.</td>
 **     </tr>
 **     <tr>
 **       <td>onHide</td>
 **       <td>null</td>
 **       <td>String</td>
 **       <td>Client side callback to execute when dialog is hidden.</td>
 **     </tr>
 **   </tbody>
 ** </table>
 */
public abstract class DialogModel {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // class Option
  // ~~–~~ ~~~~~~
  /**
   ** Defines the options that may be used for dynamically created dialogs.
   */
  public static class Option {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the collection corresponding to this value pair builder */
    private final Map<String, Object> model = new HashMap<String, Object>();

    ////////////////////////////////////////////////////////////////////////////
    // enum Property
    // ~~~~ ~~~~~~~~
    /**
     ** Defines the set of possible properties that may be used for dialogs
     */
    public enum Property {
        /** Controls modality of the dialog. */
        MODAL("modal")
        /** Enables lazy loading of the content with ajax. */
      , DYNAMIC("dynamic")
        /** Whether the dialog can be closed or not. */
      , CLOSABLE("closable")
        /** Whether the dialog can be closed with escape key. */
      , CLOSABLEONESCAPE("closeOnEscape")
        /** When enabled, makes dialog resizable. */
      , RESIZABLE("resizable")
        /** When enabled, makes dialog draggable. */
      , DRAGGABLE("draggable")
        /** In responsive mode, dialog adjusts itself based on screen width. */
      , RESPONSIVE("responsive")
        /** In responsive mode, dialog adjusts itself based on screen width. */
      , FITVIEWPORT("fitViewport")
        /** Makes dialog dialog minimizable. */
      , MINIMIZABLE("minimizable")
        /** Makes dialog maximizable. */
      , MAXIMIZABLE("maximizable")
        /** When enabled, includes the view parameters. */
      , INCLUDEVIEWPARM("includeViewParams")
        /** Width of the dialog. */
      , WIDTH("width")
        /** Height of the dialog. */
      , HEIGHT("height")
        /** Minimum width of a resizable dialog. */
      , MINWIDTH("minWidth")
        /** Minimum height of a resizable dialog. */
      , MINHEIGHT("minHeight")
        /** Width of the dialog content. */
      , CONTENTWIDTH("contentWidth")
        /** Height of the dialog content. */
      , CONTENTHEIGHT("contentHeight")
        /** Defines where the dialog should be displayed. */
      , POSITION("position")
        /** Appends the dialog to the element defined by the given search expression. */
      , APPENDTO("appendTo")
        /** Client id of the element to display inside header. */
      , HEADERELEMENT("headerElement")
        /** Effect to use when showing the dialog. */
      , SHOWEFFECT("showEffect")
        /** Effect to use when hiding the dialog. */
      , HIDEEFFECT("hideEffect")
        /** Client side callback to execute when dialog is displayed. */
      , ONSHOW("onShow")
        /** Client side callback to execute when dialog is hidden. */
      , ONHIDE("onHide")
      ;

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      /** the value of the property. */
      private final String id;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructor for <code>Property</code> with a constraint value.
       **
       ** @param  id             the value of the property.
       **                        <br>
       **                        Allowed object is {@link String}.
       */
      Property(final String id) {
        this.id = id;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Option</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    private Option() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: modal
    /**
     ** Controls modality of the dialog.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#MODAL}.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                    the <code>Builder</code> to allow method
     **                            chaining.
     **                            <br>
     **                            Possible object is <code>Builder</code>.
     */
    public Option modal(final boolean value) {
      this.model.put(Property.MODAL.id, Boolean.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: dynamic
    /**
     ** Enables lazy loading of the content with ajax.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#DYNAMIC}.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option dynamic(final boolean value) {
      this.model.put(Property.DYNAMIC.id, Boolean.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: closable
    /**
     ** Whether the dialog can be closed or not.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#CLOSABLE}.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option closable(final boolean value) {
      this.model.put(Property.CLOSABLE.id, Boolean.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: closeOnEscape
    /**
     ** Whether the dialog can be closed with escape key.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#CLOSABLEONESCAPE}.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option closeOnEscape(final boolean value) {
      this.model.put(Property.CLOSABLEONESCAPE.id, Boolean.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: resizable
    /**
     ** When enabled, makes dialog resizable.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#RESIZABLE}.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option resizable(final boolean value) {
      this.model.put(Property.RESIZABLE.id, Boolean.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: draggable
    /**
     ** When enabled, makes dialog draggable.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#DRAGGABLE}.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option draggable(final boolean value) {
      this.model.put(Property.DRAGGABLE.id, Boolean.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: responsive
    /**
     ** In responsive mode, dialog adjusts itself based on screen width.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#RESPONSIVE}.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option responsive(final boolean value) {
      this.model.put(Property.RESPONSIVE.id, Boolean.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: minimizable
    /**
     ** Makes dialog minimizable.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#MINIMIZABLE}.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option minimizable(final boolean value) {
      this.model.put(Property.MINIMIZABLE.id, Boolean.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: maxmizable
    /**
     ** Makes dialog maxmizable.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#MAXIMIZABLE}.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option maxmizable(final boolean value) {
      this.model.put(Property.MAXIMIZABLE.id, Boolean.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: includeViewParams
    /**
     ** When enabled, includes the view parameters.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#INCLUDEVIEWPARM}.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option includeViewParam(final boolean value) {
      this.model.put(Property.INCLUDEVIEWPARM.id, Boolean.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: width
    /**
     ** Set the width of the dialog.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#WIDTH}.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option width(final int value) {
      this.model.put(Property.WIDTH.id, Integer.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: height
    /**
     ** Set the height of the dialog.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#HEIGHT}.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option height(final int value) {
      this.model.put(Property.HEIGHT.id, Integer.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: minWidth
    /**
     ** Set the minimum width of a resizable dialog.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#MINWIDTH}.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option minWidth(final int value) {
      this.model.put(Property.MINWIDTH.id, Integer.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: minHeight
    /**
     ** Set the minimum height of a resizable dialog.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#MINHEIGHT}.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option minHeight(final int value) {
      this.model.put(Property.MINHEIGHT.id, Integer.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: contentWidth
    /**
     ** Set the width of the dialog content.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#CONTENTWIDTH}.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option contentWidth(final int value) {
      this.model.put(Property.CONTENTWIDTH.id, Integer.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: contentHeight
    /**
     ** Set the height of the dialog content.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#CONTENTHEIGHT}.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option contentHeight(final int value) {
      this.model.put(Property.CONTENTHEIGHT.id, Integer.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: fitViewport
    /**
     ** In responsive mode, dialog adjusts itself based on screen width.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#FITVIEWPORT}.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option fitViewport(final boolean value) {
      this.model.put(Property.FITVIEWPORT.id, Boolean.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: position
    /**
     ** Defines where the dialog should be displayed.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#POSITION}.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option position(final int value) {
      this.model.put(Property.POSITION.id, Integer.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: appendTo
    /**
     ** Appends the dialog to the element defined by the given search
     ** expression.
     ** <br>
     ** Defaults to the body.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#APPENDTO}.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option appendTo(final int value) {
      this.model.put(Property.APPENDTO.id, Integer.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: headerElement
    /**
     ** set the client id of the element to display inside header.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#HEADERELEMENT}.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option headerElement(final int value) {
      this.model.put(Property.HEADERELEMENT.id, Integer.valueOf(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: showEffect
    /**
     ** Set effect to use when showing the dialog.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#SHOWEFFECT}.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option showEffect(final String value) {
      this.model.put(Property.SHOWEFFECT.id, value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: hideEffect
    /**
     ** Set effect to use when hiding the dialog.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#HIDEEFFECT}.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option hideEffect(final String value) {
      this.model.put(Property.HIDEEFFECT.id, value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: onShow
    /**
     ** Set the client side callback to execute when dialog is displayed.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#ONSHOW}.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option onShow(final String value) {
      this.model.put(Property.ONSHOW.id, value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: onHide
    /**
     ** Set the client side callback to execute when dialog is hidden.
     **
     ** @param  value            the value to set for the property
     **                          {@link Property#ONHIDE}.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Option</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option onHide(final String value) {
      this.model.put(Property.ONHIDE.id, value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////
    
    public Map<String, Object> build() {
      return this.model;
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  // class Parameter
  // ~~–~~ ~~~~~~~~~
  /**
   ** Defines the parameters that may be used for dynamically created dialogs.
   */
  public static class Parameter {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the collection corresponding to this value pair builder */
    private final Map<String, List<String>> model = new HashMap<String, List<String>>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Parameter</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    private Parameter() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////
    
    public Map<String, List<String>> build() {
      return this.model;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DialogModel</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DialogModel() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by funtionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   option
  public static Option option() {
    return new Option();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parameter
  public static Parameter parameter() {
    return new Parameter();
  }
}
