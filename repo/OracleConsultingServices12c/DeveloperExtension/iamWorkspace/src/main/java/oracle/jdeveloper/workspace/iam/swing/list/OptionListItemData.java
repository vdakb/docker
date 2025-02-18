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

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   OptionListItemData.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    OptionListItemData.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.list;

import java.io.Serializable;

import java.text.Collator;

import javax.swing.Icon;

import oracle.jdeveloper.workspace.iam.swing.widget.TriStateButton;

////////////////////////////////////////////////////////////////////////////////
// class OptionListItemData
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Data structure for describing a list item that might have an triggerable
 ** hotspot.
 ** <p>
 ** This is used to direct the behavior of the {@link OptionListCellRenderer}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class OptionListItemData implements Comparable
                                ,          Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the flag to switch text enabled. */
  public static final int       TEXT_ENABLED       = 0x0001;

  /** the flag to switch text visible. */
  public static final int       TEXT_VISIBLE       = 0x0002;

  /** the flag to switch text selected. */
  public static final int       TEXT_SELECTED      = 0x0004;

  /** the flag to switch text focused. */
  public static final int       TEXT_FOCUSED       = 0x0008;

  /** the flag to to indicate the text part can be selected. */
  public static final int       TEXT_SELECTABLE    = 0x0010;

  /** the flag to to indicate the text part can be selected. */
  public static final int       TEXT_EDITABLE      = 0x0020;

  /** the flag to switch icon enabled. */
  public static final int       ICON_ENABLED       = 0x0100;

  /** the flag to switch icon visible. */
  public static final int       ICON_VISIBLE       = 0x0200;

  /** the flag to switch icon selected. */
  public static final int       ICON_SELECTED      = 0x0400;

  /** the flag to switch icon focused. */
  public static final int       ICON_FOCUSED       = 0x0800;

  /** the flag to switch hotspot enabled. */
  public static final int       HOTSPOT_ENABLED    = 0x0001;

  /** the flag to switch hotspot visible. */
  public static final int       HOTSPOT_VISIBLE    = 0x0002;

  /** the flag to switch hotspot selected. */
  public static final int       HOTSPOT_SELECTED   = 0x0004;

  /** the flag to switch hotspot focused. */
  public static final int       HOTSPOT_FOCUSED    = 0x0008;

  /** the flag to to indicate the hotspot part can be selected. */
  public static final int       HOTSPOT_SELECTABLE = 0x0010;

  /** the flag to switch hotspot armed. */
  public static final int       HOTSPOT_ARMED      = 0x4000;

  /** the flag to switch hotspot pressed. */
  public static final int       HOTSPOT_PRESSED    = 0x8000;

  /**
   ** the {@link Collator} class performs locale-sensitive <code>String</code>
   ** comparison.
   */
  private static final Collator COLLATOR          = Collator.getInstance();

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1732751461067292974")
  private static final long     serialVersionUID  = -5977219745955094114L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the text to display in a tree node */
  protected String               text;

  /** the text to display as the tooltip for a tree node */
  protected String               tooltip;

  /** the symbol to display in a tree node */
  protected transient Icon       icon;

  /** the adjustment of the node contents along the Y axis */
  protected int                  verticalAdjustment;

  /** the bit mask representing the state of the content */
  protected int                  contentState       = TEXT_ENABLED | ICON_ENABLED;

  /** the bit mask representing the state of the hotspot */
  protected int                  hotspotState       = HOTSPOT_ENABLED | HOTSPOT_VISIBLE | HOTSPOT_SELECTABLE;

  /** the selection state of the hotspot */
  protected TriStateButton.Model hotspotModel;

  /** the symbol to display in a tree node */
  protected transient Object     userObject;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionListItemData</code> with the specified text and
   ** no image.
   **
   ** @param  text                the text to be displayed by the node.
   */
  public OptionListItemData(final String text) {
    // ensure inheritance
    this(text, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionListItemData</code> instance with the specified
   ** text, image, and vertical adjustment.
   **
   ** @param  text                the text to be displayed by the node.
   ** @param  icon                the image to be displayed by the node.
   ** @param  verticalAdjustment  the adjustment of the node contents along the
   **                             Y axis.
   */
  public OptionListItemData(final String text, final Icon icon, final int verticalAdjustment) {
    // ensure inheritance
    this(text, icon);

    // initailiaze instance attributes
    this.text               = text;
    this.icon               = icon;
    this.verticalAdjustment = verticalAdjustment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionListItemData</code> instance with the specified
   ** text, image.
   **
   ** @param  text                the text to be displayed by the node.
   ** @param  icon                the image to be displayed by the node.
   */
  public OptionListItemData(final String text, final Icon icon) {
    // ensure inheritance
    super();

    // initailiaze instance attributes
    this.text = text;
    this.icon = icon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionListItemData</code> instance with the specified
   ** text, image, and vertical adjustment.
   **
   ** @param  text                the text to be displayed by the node.
   ** @param  icon                the image to be displayed by the node.
   ** @param  hotspotModel        the initial {@link TriStateButton.Model} of
   **                             the hotspot.
   */
  public OptionListItemData(final String text, final Icon icon, final TriStateButton.Model hotspotModel) {
    // ensure inheritance
    super();

    // initailiaze instance attributes
    this.text          = text;
    this.icon          = icon;

    // switch on the capability to display a hotspot
    this.hotspotModel  = hotspotModel;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enable
  /**
   ** Convenience method to enable that the node can interact with the user.
   **
   ** @param  enabled            <code>true</code> if either the node supplies
   **                            text or hotspot or both, <code>false</code>
   **                            otherwise.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData enable(final boolean enabled) {
    return enable(enabled, enabled);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enable
  /**
   ** Convenience method to enable that the node can interact with the user.
   **
   ** @param  enabled            <code>true</code> if either the node supplies
   **                            text, <code>false</code> otherwise.
   ** @param  selectable         <code>true</code> if either the node supplies
   **                            hotspot, <code>false</code> otherwise.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData enable(final boolean enabled, final boolean selectable) {
    if (enabled) {
      this.contentState |= TEXT_ENABLED;
      this.hotspotState |= HOTSPOT_ENABLED | HOTSPOT_VISIBLE | HOTSPOT_SELECTABLE;
    }
    else {
      this.contentState &= ~TEXT_ENABLED;
      this.hotspotState &= ~(HOTSPOT_ENABLED | HOTSPOT_VISIBLE | HOTSPOT_SELECTABLE);
    }

    if (selectable)
      this.contentState |= TEXT_SELECTABLE;
    else {
      this.contentState &= ~TEXT_SELECTABLE;
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enabled
  /**
   ** Convenience method to determine if the node can interact with the user.
   **
   ** @return                    <code>true</code> if either the node supplies
   **                            text or hotspot or both, <code>false</code>
   **                            otherwise.
   */
  public boolean enabled() {
    return checkContentState(TEXT_ENABLED) || checkHotspotState(HOTSPOT_ENABLED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enableContent
  /**
   ** Convenience method to enable that the node can interact with the user.
   **
   ** @param  enabled            <code>true</code> if either the node supplies
   **                            text and the text part is also selectable,
   **                            <code>false</code> otherwise.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData enableContent(final boolean enabled) {
    return enableContent(enabled, enabled);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enableContent
  /**
   ** Convenience method to enable that the node can interact with the user.
   **
   ** @param  enabled            <code>true</code> if either the node supplies
   **                            text , <code>false</code> otherwise.
   ** @param  selectable         <code>true</code> if either the node supplies
   **                            selectable text, <code>false</code> otherwise.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData enableContent(final boolean enabled, final boolean selectable) {
    if (enabled) {
      this.contentState |= TEXT_ENABLED;
      if (selectable)
        this.contentState |= TEXT_SELECTABLE;
      else {
        this.contentState &= ~TEXT_SELECTABLE;
      }
    }
    else {
      this.contentState &= ~(TEXT_ENABLED | TEXT_SELECTABLE);
    }

    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enableHotspot
  /**
   ** Convenience method to enable that the node can interact with the user.
   **
   ** @param  enabled            <code>true</code> if either the node supplies
   **                            hotspot , <code>false</code> otherwise.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData enableHotspot(final boolean enabled) {
    final int mask = HOTSPOT_ENABLED | HOTSPOT_VISIBLE | HOTSPOT_SELECTABLE;
    if (enabled) {
      this.hotspotState |= mask;
    }
    else {
      this.hotspotState &= ~mask;
    }

    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   text
  /**
   ** Sets the text to display in a tree node.
   **
   ** @param  value              the text to display in a tree node.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData text(final String value) {
    this.text = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   text
  /**
   ** Returns the text to display in a tree node.
   **
   ** @return                    the text to display in a tree node.
   */
  public String text() {
    return this.text;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enableText
  /**
   ** Sets the text part to enabled.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData enableText() {
    this.contentState |= TEXT_ENABLED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disableText
  /**
   ** Sets the text part to disabled.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData disableText() {
    this.contentState &= ~TEXT_ENABLED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toggleTextState
  /**
   ** Inverts the text part from enable to disabled or vice versa.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData toggleTextState() {
    this.contentState ^= TEXT_ENABLED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   textEnabled
  /**
   ** Returns whether the text part is enabled or not.
   **
   ** @return                    <code>true</code> if the text part is
   **                            refelecting enabled, <code>false</code>
   **                            otherwise.
   */
  public boolean textEnabled() {
    return checkContentState(TEXT_ENABLED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   showText
  /**
   ** Sets the text part to visible.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData showText() {
    this.contentState |= TEXT_VISIBLE;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hideText
  /**
   ** Sets the text part to hidden.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData hideText() {
    this.contentState &= ~TEXT_VISIBLE;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toggleTextVisibility
  /**
   ** Inverts the text part from visible to hidden or vice versa.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData toggleTextVisibility() {
    this.contentState ^= TEXT_VISIBLE;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   textVisible
  /**
   ** Returns whether the text part is visible or not.
   **
   ** @return                    <code>true</code> if the text part is visible,
   **                            <code>false</code> if the text part is hidden.
   */
  public boolean textVisible() {
    return checkContentState(TEXT_VISIBLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectableText
  /**
   ** Sets the text part as selectable.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData selectableText() {
    this.contentState |= TEXT_SELECTABLE;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nonselectableText
  /**
   ** Sets the text part as not selectable.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData nonselectableText() {
    this.contentState &= ~TEXT_SELECTABLE;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   textSelectable
  /**
   ** Returns whether the text part is selectable or not.
   **
   ** @return                    <code>true</code> if the text part can be
   **                            selected, <code>false</code> otherwise.
   */
  public boolean textSelectable() {
    return checkContentState(TEXT_SELECTABLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectText
  /**
   ** Sets the text part to selected.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData selectText() {
    this.contentState |= TEXT_SELECTED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unselectText
  /**
   ** Sets the text part to unselected.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData unselectText() {
    this.contentState &= ~TEXT_SELECTED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toggleTextSelection
  /**
   ** Inverts the text part from selected to unselected or vice versa.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData toggleTextSelection() {
    this.contentState ^= TEXT_SELECTED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   textSelected
  /**
   ** Returns whether the text part is selected or not.
   **
   ** @return                    <code>true</code> if the text part is
   **                            refelecting selected, <code>false</code>
   **                            otherwise.
   */
  public boolean textSelected() {
    return checkContentState(TEXT_SELECTED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   textFocusGained
  /**
   ** Sets the text part to reflect that it has the focus.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData textFocusGained() {
    this.contentState |= TEXT_FOCUSED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   textFocusLost
  /**
   ** Sets the text part to disabled.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData textFocusLost() {
    this.contentState &= ~TEXT_FOCUSED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toggleTextFocus
  /**
   ** Inverts the text part from gaining to losing focus or vice versa.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData toggleTextFocus() {
    this.contentState ^= TEXT_FOCUSED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   textFocused
  /**
   ** Returns whether the text part is refelecting that it has the focus or not.
   **
   ** @return                    <code>true</code> if the text part is
   **                            refelecting that it has the focus,
   **                            <code>false</code> otherwise.
   */
  public boolean textFocused() {
    return checkContentState(TEXT_FOCUSED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   editableText
  /**
   ** Sets the text part as editable.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData editableText() {
    this.contentState |= TEXT_EDITABLE;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   noneditableText
  /**
   ** Sets the text part as not editable.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData noneditableText() {
    this.contentState &= ~TEXT_EDITABLE;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   textEditable
  /**
   ** Toggles whether the text part is editable or not.
   **
   ** @param  state              <code>true</code> if the text part is editable;
   **                            otherwise <code>flase</code>.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData textEditable(final boolean state) {
    return state ?  editableText() : noneditableText();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   textEditable
  /**
   ** Returns whether the text part is editable or not.
   **
   ** @return                    <code>true</code> if the text part can be
   **                            selected, <code>false</code> otherwise.
   */
  public boolean textEditable() {
    return checkContentState(TEXT_EDITABLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Sets the symbol to display in a tree node.
   **
   ** @param  value              the symbol to display in a tree node.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData icon(final Icon value) {
    this.icon = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Returns the symbol to display in a tree node.
   **
   ** @return                    the symbol to display in a tree node.
   */
  public Icon icon() {
    return this.icon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enableIcon
  /**
   ** Sets the icon part to enabled.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData enableIcon() {
    this.contentState |= ICON_ENABLED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disableIcon
  /**
   ** Sets the icon part to disabled.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData disableIcon() {
    this.contentState &= ~ICON_ENABLED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toggleIconState
  /**
   ** Inverts the icon part from enable to disabled or vice versa.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData toggleIconState() {
    this.contentState ^= ICON_ENABLED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iconEnabled
  /**
   ** Returns whether the icon part is enabled or not.
   **
   ** @return                    <code>true</code> if the icon part is
   **                            refelecting enabled, <code>false</code>
   **                            otherwise.
   */
  public boolean iconEnabled() {
    return checkContentState(ICON_ENABLED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   showIcon
  /**
   ** Sets the icon part to visible.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData showIcon() {
    this.contentState |= ICON_VISIBLE;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hideIcon
  /**
   ** Sets the icon part to hidden.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData hideIcon() {
    this.contentState &= ~ICON_VISIBLE;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toggleIconVisibility
  /**
   ** Inverts the icon part from visible to hidden or vice versa.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData toggleIconVisibility() {
    this.contentState ^= ICON_VISIBLE;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iconVisible
  /**
   ** Returns whether the icon part is visible or not.
   **
   ** @return                    <code>true</code> if the icon part is visible,
   **                            <code>false</code> if the icon part is hidden.
   */
  public boolean iconVisible() {
    return checkContentState(ICON_VISIBLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectIcon
  /**
   ** Sets the icon part to selected.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData selectIcon() {
    this.contentState |= ICON_SELECTED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unselectIcon
  /**
   ** Sets the icon part to unselected.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData unselectIcon() {
    this.contentState &= ~ICON_SELECTED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toggleIconSelection
  /**
   ** Inverts the icon part from selected to unselected or vice versa.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData toggleIconSelection() {
    this.contentState ^= ICON_SELECTED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iconSelected
  /**
   ** Returns whether the icon part is selected or not.
   **
   ** @return                    <code>true</code> if the icon part is
   **                            refelecting selected, <code>false</code>
   **                            otherwise.
   */
  public boolean iconSelected() {
    return checkContentState(ICON_SELECTED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iconFocusGained
  /**
   ** Sets the icon part to reflect that it has the focus.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData iconFocusGained() {
    this.contentState |= ICON_FOCUSED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iconFocusLost
  /**
   ** Sets the icon part to disabled.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData iconFocusLost() {
    this.contentState &= ~ICON_FOCUSED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toggleIconFocus
  /**
   ** Inverts the icon part from gaining to losing focus or vice versa.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData toggleIconFocus() {
    this.contentState ^= ICON_FOCUSED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iconFocused
  /**
   ** Returns whether the icon part is refelecting that it has the focus or not.
   **
   ** @return                    <code>true</code> if the icon part is
   **                            refelecting that it has the focus,
   **                            <code>false</code> otherwise.
   */
  public boolean iconFocused() {
    return checkContentState(ICON_FOCUSED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enableHotspot
  /**
   ** Sets the hotspot part to enabled.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData enableHotspot() {
    this.hotspotState |= HOTSPOT_ENABLED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disableHotspot
  /**
   ** Sets the hotspot part to disabled.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData disableHotspot() {
    this.hotspotState &= ~HOTSPOT_ENABLED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toggleHotspotState
  /**
   ** Inverts the hotspot part from enable to disabled or vice versa.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData toggleHotspotState() {
    this.hotspotState ^= HOTSPOT_ENABLED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hotspotEnabled
  /**
   ** Returns whether the hotspot part is enabled or not.
   **
   ** @return                    <code>true</code> if the hotspot part is
   **                            refelecting enabled, <code>false</code>
   **                            otherwise.
   */
  public boolean hotspotEnabled() {
    return checkHotspotState(HOTSPOT_ENABLED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   showHotspot
  /**
   ** Sets the hotspot part to visible.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData showHotspot() {
    this.hotspotState |= HOTSPOT_VISIBLE;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hideHotspot
  /**
   ** Sets the hotspot part to hidden.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData hideHotspot() {
    this.hotspotState &= ~HOTSPOT_VISIBLE;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toggleHotspotVisibility
  /**
   ** Inverts the hotspot part from visible to hidden or vice versa.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData toggleHotspotVisibility() {
    this.hotspotState ^= HOTSPOT_VISIBLE;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hotspotVisible
  /**
   ** Returns whether the hotspot part is visible or not.
   **
   ** @return                    <code>true</code> if the hotspot part is
   **                            visible, <code>false</code> if the hotspot part
   **                            is hidden.
   */
  public boolean hotspotVisible() {
    return checkHotspotState(HOTSPOT_VISIBLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectableHotspot
  /**
   ** Sets the hotspot part as selectable.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData selectableHotspot() {
    this.hotspotState |= HOTSPOT_SELECTABLE;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nonselectableHotspot
  /**
   ** Sets the hotspot part as not selectable.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData nonselectableHotspot() {
    this.hotspotState &= ~HOTSPOT_SELECTABLE;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hotspotSelectable
  /**
   ** Returns whether the hotspot part is selectable or not.
   **
   ** @return                    <code>true</code> if the hotspot part can be
   **                            selected, <code>false</code> otherwise.
   */
  public boolean hotspotSelectable() {
    return checkHotspotState(HOTSPOT_SELECTABLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectHotspot
  /**
   ** Sets the hotspot part to selected.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData selectHotspot() {
    this.hotspotState |= HOTSPOT_SELECTED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unselectHotspot
  /**
   ** Sets the hotspot part to unselected.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData unselectHotspot() {
    this.hotspotState &= ~HOTSPOT_SELECTED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toggleHotspotSelection
  /**
   ** Inverts the hotspot part from selected to unselected or vice versa.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData toggleHotspotSelection() {
    this.hotspotState ^= HOTSPOT_SELECTED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hotspotSelected
  /**
   ** Convenience method to change the state of the hotspot.
   **
   ** @param  selected           <code>true</code> if the hotspot is selected,
   **                            <code>false</code> otherwise.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData hotspotSelected(final boolean selected) {
    final int mask = HOTSPOT_SELECTED;
    if (selected) {
      this.hotspotState |= mask;
    }
    else {
      this.hotspotState &= ~mask;
    }

    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hotspotSelected
  /**
   ** Returns whether the hotspot part is selected or not.
   **
   ** @return                    <code>true</code> if the hotspot part is
   **                            refelecting selected, <code>false</code>
   **                            otherwise.
   */
  public boolean hotspotSelected() {
    return checkHotspotState(HOTSPOT_SELECTED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hotspotFocusGained
  /**
   ** Sets the hotspot part to reflect that it has the focus.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData hotspotFocusGained() {
    this.hotspotState |= HOTSPOT_FOCUSED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hotspotFocusLost
  /**
   ** Sets the hotspot part to disabled.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData hotspotFocusLost() {
    this.hotspotState &= ~HOTSPOT_FOCUSED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toggleHotspotFocus
  /**
   ** Inverts the hotspot part from gaining to losing focus or vice versa.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData toggleHotspotFocus() {
    this.hotspotState ^= HOTSPOT_FOCUSED;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hotspotFocused
  /**
   ** Returns whether the hotspot part is refelecting that it has the focus or not.
   **
   ** @return                    <code>true</code> if the hotspot part is
   **                            refelecting that it has the focus,
   **                            <code>false</code> otherwise.
   */
  public boolean hotspotFocused() {
    return checkHotspotState(HOTSPOT_FOCUSED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tooltip
  /**
   ** Sets the tooltip to display for a tree node.
   **
   ** @param  value              the tooltip to display for a tree node.
   **                            hierarchy.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData tooltip(final String value) {
    this.tooltip = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tooltip
  /**
   ** Returns the tooltip to display for a tree node.
   **
   ** @return                    the tooltip to display for a tree node.
   */
  public String tooltip() {
    return this.tooltip;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verticalAdjustment
  /**
   ** Sets the adjustment of the node contents along the Y axis.
   **
   ** @param  value              the adjustment of the node contents along the Y
   **                            axis.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData verticalAdjustment(final int value) {
    this.verticalAdjustment = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verticalAdjustment
  /**
   ** Returns the adjustment of the node contents along the Y axis.
   **
   ** @return                    the adjustment of the node contents along the Y
   **                            axis.
   */
  public int verticalAdjustment() {
    return this.verticalAdjustment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userObject
  /**
   ** Sets the value provider of the tree node.
   **
   ** @param  value              the value provider of the tree node.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionListItemData userObject(final Object value) {
    this.userObject = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userObject
  /**
   ** Returns the value provider of the tree node.
   **
   ** @return                    the value provider of the tree node.
   */
  public Object userObject() {
    return this.userObject;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compareTo (Comparable)
  /**
   ** Compares this object with the specified object for order.
   ** <p>
   ** Returns a negative integer, zero, or a positive integer as this object is
   ** less than, equal to, or greater than the specified object.
   ** <p>
   ** The implementation ensures that
   ** <code>sgn(x.compareTo(y)) == -sgn(y.compareTo(x))</code> for all
   ** <code>x</code> and <code>y</code>.
   ** <p>
   ** The implementation also ensures that the relation is transitive:
   ** <code>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</code> implies
   ** <code>x.compareTo(z)&gt;0</code>.
   ** <p>
   ** Finally it is ensured that <code>x.compareTo(y)==0</code> implies that
   ** <code>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</code>, for all
   ** <code>z</code>.
   ** <p>
   ** In the foregoing description, the notation
   ** <code>sgn(</code><i>expression</i><code>)</code> designates the
   ** mathematical <i>signum</i> function, which is defined to return one of
   ** <code>-1</code>, <code>0</code>, or <code>1</code> according to whether
   ** the value of <i>expression</i> is negative, zero or positive.
   **
   ** @param  other              the object to be compared.
   **
   ** @return                    a negative integer, zero, or a positive integer
   **                            as this object is less than, equal to, or
   **                            greater than the specified object.
   **
   ** @throws ClassCastException if the specified object's type prevents it
   **                             from being compared to this object.
   */
  @Override
  public int compareTo(final Object other) {
    if (!(other instanceof OptionListItemData))
      throw new ClassCastException("In order to sort, all tree nodes must be an instance of OptionListItemData.");

    return compareTo((OptionListItemData)other);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compareTo
  /**
   ** Compares this object with the specified object for order.
   ** <p>
   ** Returns a negative integer, zero, or a positive integer as this object is
   ** less than, equal to, or greater than the specified object.
   ** <p>
   ** The implementation ensures that
   ** <code>sgn(x.compareTo(y)) == -sgn(y.compareTo(x))</code> for all
   ** <code>x</code> and <code>y</code>.
   ** <p>
   ** The implementation also ensures that the relation is transitive:
   ** <code>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</code> implies
   ** <code>x.compareTo(z)&gt;0</code>.
   ** <p>
   ** Finally it is ensured that <code>x.compareTo(y)==0</code> implies that
   ** <code>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</code>, for all
   ** <code>z</code>.
   ** <p>
   ** In the foregoing description, the notation
   ** <code>sgn(</code><i>expression</i><code>)</code> designates the
   ** mathematical <i>signum</i> function, which is defined to return one of
   ** <code>-1</code>, <code>0</code>, or <code>1</code> according to whether
   ** the value of <i>expression</i> is negative, zero or positive.
   **
   ** @param  other              the object to be compared.
   **
   ** @return                    a negative integer, zero, or a positive integer
   **                            as this object is less than, equal to, or
   **                            greater than the specified object.
   **
   ** @throws ClassCastException if the specified object's type prevents it
   **                             from being compared to this object.
   */
  public int compareTo(final OptionListItemData other) {
    if (other == null)
      return 1;

    if (this.text == null)
      return (other.text == null) ? 0 : -1;

    if (other.text == null)
      return 1;

    return COLLATOR.compare(this.text, other.text);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   checkContentState
  /**
   ** Returns whether the specified bit mask ist set or not in the state of the
   ** content.
   **
   ** @param  mask               the bit mask to test.
   **
   ** @return                    <code>true</code> if the specified bit mask is
   **                            set in the state of the content,
   **                            <code>false</code> otherwise.
   */
  public boolean checkContentState(final int mask) {
    return (this.contentState & mask) == mask;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   checkHotspotState
  /**
   ** Returns whether the specified bit mask ist set or not in the state of the
   ** hotspot.
   **
   ** @param  mask               the bit mask to test.
   **
   ** @return                    <code>true</code> if the specified bit mask is
   **                            set in the state of the hotspot,
   **                            <code>false</code> otherwise.
   */
  public boolean checkHotspotState(final int mask) {
    return (this.hotspotState & mask) == mask;
  }
}