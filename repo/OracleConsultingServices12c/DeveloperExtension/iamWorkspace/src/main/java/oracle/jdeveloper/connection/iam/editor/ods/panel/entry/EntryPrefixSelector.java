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

    Copyright © 2023. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   EntryPrefixSelector.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EntryPrefixSelector.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.panel.entry;

import java.util.Set;
import java.util.HashSet;

import javax.swing.JTextField;

import oracle.jdeveloper.connection.iam.model.DirectoryValue;
import oracle.jdeveloper.connection.iam.model.DirectoryEntry;

import oracle.jdeveloper.workspace.iam.swing.Searchable;

import oracle.jdeveloper.workspace.iam.swing.widget.SuggestionComponent;
import oracle.jdeveloper.workspace.iam.swing.widget.SuggestionDecorator;
import oracle.jdeveloper.workspace.iam.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class EntryPrefixSelector
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** An editor component suitable to select a relative distinguished name as the
 ** prefix of an entry.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public class EntryPrefixSelector extends JTextField {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2300779774928030686")
  private static final long serialVersionUID = -1851746891087763748L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final DirectoryEntry      entry;
  final Set<String>         inventory;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new empty <code>EntryPrefixSelector</code> with the
   ** specified number of columns.
   ** <br>
   ** A default model is created and the initial string is set to
   ** <code>null</code>.
   **
   ** @param  columns            the number of columns to use to calculate the
   **                            preferred width; if columns is set to zero, the
   **                            preferred width will be whatever naturally
   **                            results from the component implementation.
   **                            <br>
   **                            Allowed object is <code>int</code>
   */
  private EntryPrefixSelector(final int columns, final DirectoryEntry entry) {
    // ensure inheritance
    super(columns);

    // initialize instance attributes
    this.entry     = entry;
    this.inventory = new HashSet<String>();

    // initialize instance
    update();

    // decorate the editor with the model to filter and select elements
    SuggestionDecorator.decorate(this, SuggestionComponent.<JTextField>literal(Searchable.literal(this.inventory)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods gouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>EntryPrefixSelector</code> that populates
   ** its data to visualize from the specified collection.
   **
   ** @param  columns            the number of columns to use to calculate the
   **                            preferred width; if columns is set to zero, the
   **                            preferred width will be whatever naturally
   **                            results from the component implementation.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  entry              the underlying entry providing the collected
   **                            data for the selector.
   **                            <br>
   **                            Allowed object is {@link DirectoryEntry}.
   **
   ** @return                    the <code>EntryPrefixSelector</code>.
   **                            <br>
   **                            Possible object
   **                            <code>EntryPrefixSelector</code>.
   */
  public static EntryPrefixSelector build(final int columns, final DirectoryEntry entry) {
    return new EntryPrefixSelector(columns, entry);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update
  /**
   ** Updates an <code>EntryPrefixSelector</code> object from the specified
   ** properties.
   */
  public void update() {
    refresh();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Populates the model responsible to display the populate relative
   ** distiguished name.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Any attribute/value combination is populated.
   */
  private void refresh() {
    // cleanup any previously populated values
    this.inventory.clear();
    if (this.entry.size() > 0) {
      // populate the possible combination for a relatve distinguished name
      for (DirectoryValue cursor : this.entry.value().values()) {
        for (DirectoryValue.Item item : cursor) {
          final String value = item.value();
          // shorten the posibilities to valid combinations
          if (!StringUtility.empty(value))
            this.inventory.add(cursor.type.name + "=" + item.value());
        }
      }
    }
  }
}