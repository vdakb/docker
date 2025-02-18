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

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facilities

    File        :   Filter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    Filter.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.60.63  2015-02-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Content Set's shipped to standard
                                               PathConfiguration container
*/

package oracle.jdeveloper.workspace.iam.project.content.build;

import java.util.List;
import java.util.Iterator;

import oracle.ide.Context;

import oracle.ide.net.URLPath;

import oracle.ide.model.Node;
import oracle.ide.model.Element;
import oracle.ide.model.ContentSet;
import oracle.ide.model.ContentLevel;
import oracle.ide.model.ContentLevelFilter;
import oracle.ide.model.ElementAttributes;
import oracle.ide.model.RelativeDirectoryContextFolder;

////////////////////////////////////////////////////////////////////////////////
// class Filter
// ~~~~~ ~~~~~~
/**
 ** This class is responsible for filtering the breadth-first traversal
 ** implemented by the {@link ContentLevel} class to provide a virtual
 ** representation of each level that differs from its physical representation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.60.63
 */
class Filter extends ContentLevelFilter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a {@link ContentLevelFilter} that works on the Ant Build content
   ** set with the specified key.
   **
   ** @param  contentSet         a non-<code>null</code>, non-empty string for
   **                            which this {@link ContentLevelFilter} will
   **                            apply.
   **
   ** @throws IllegalArgumentException if <code>contentSet</code> is
   **                                  <code>null</code> or empty.
   */
  public Filter(final String contentSet) {
    // ensure inheritance
    super(new String[] { contentSet });
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateDir (ContentLevelFilter)
  /**
   ** This method is called whenever a level is expanded during the
   ** breadth-first traversal of the project content. Implementations of this
   ** method have the opportunity to modify the lists of {@link Element}s and
   ** subdirectories for each level before they are displayed. Whenever there
   ** are multiple {@link ContentLevelFilter} applying changes to the same
   ** level, later filters will see the effect of earlier filters and could
   ** possibly perform further filtering on them.
   ** <p>
   ** Filtering at the file level is done by adding/removing {@link Element}s
   ** to/from the given <code>element</code> list.
   ** <p>
   ** Filering subdirectories involves first removing the Element representing
   ** the subdirectory from the given subdirList. If no further action is taken,
   ** the subdirectory will not be displayed. However if the removed
   ** subdirectory is then added to the <code>element</code> list, the
   ** subdirectory will be displayed, even if it's empty (unless a later
   ** {@link ContentLevelFilter} removes it). Nothing can be added to the
   ** subdirList; a runtime exception will be thrown on any attempt to add or
   ** modify an item to subdirList.
   **
   ** @param  root               the list of source root directories that
   **                            underly the current content level being
   **                            expanded.
   ** @param  relativePath       the relative path within the
   **                            <code>root</code> where the content level
   **                            expansion is occurring. When expanding the
   **                            very first level, <code>relativePath</code> is
   **                            &quot;&quot; (i.e. the empty string).
   ** @param  element            this is a <code>List</code> of {@link Element}s
   **                            that initially contains {@link Node} instances
   **                            for all files in the <code>relativePath</code>.
   **                            If an item is removed from
   **                            <code>element</code>, it will not be displayed.
   **                            If an {@link Element} is added to the
   **                            <code>element</code> list, it will be
   **                            displayed. An attempt to add a
   **                            non-{@link Element} type to the
   **                            <code>element</code> list will cause a runtime
   **                            exception to be thrown.
   **                            When multiple filters modify the
   **                            <code>element</code> for the same
   **                            <code>relativePath</code>, later filters will
   **                            see the effect of earlier filters.
   **  @param  directory         this is a <code>List</code> of
   **                            {@link RelativeDirectoryContextFolder}
   **                            instances that initially contains all the
   **                            subdirectories under the
   **                            <code>relativePath</code>, accounting for the
   **                            flat level currently being used by the
   **                            {@link ContentLevel}.
   **                            (This means that when expanding the root-level
   **                            &quot;&quot; relativePath, the subdirList will
   **                            contain all subdirectories up to the flat
   **                            level. So if the flat level is 3, the
   **                            subdirList at relativePath &quot;&quot; will
   **                            contain the first 3 levels of subdirectories.)
   **                            The filter implementation may take one of three
   **                            actions for each item in subdirList:
   **                            <ul>
   **                              <li><b>Remove the item</b>.
   **                                  <br>
   **                                  This results in the corresponding folder
   **                                  not being displayed at all.
   **                              <li><b>Move the item to <code>element</code> list</b>.
   **                                  <br>
   **                                  This forces the corresponding folder to
   **                                  be displayed, even if it is empty.
   **                              <li><b>Leave the item in subdirList</b>.
   **                                  <br>
   **                                  This means that the decision to show or
   **                                  hide the folder is left to later filters
   **                                  and/or the IDE. After all filters have
   **                                  been called, any remaining items in the
   **                                  subdirList will be processed as follows:
   **                                  <ul>
   **                                    <li>If the subdirectory contains files,
   **                                        the folder is displayed.
   **                                    <li>Else If the subdirectory also has
   **                                        no subdirectories, the folder is
   **                                        not displayed.
   **                                   <li>Else if the dirRelPath is ""
   **                                       <ul>
   **                                         <li>If the folder's depth is less
   **                                             than the navigator's
   **                                             flat-level setting, the folder
   **                                             is not displayed (because
   **                                             deeper levels are also
   **                                             flattened).
   **                                         <li>Else the folder is displayed.
   **                                       </ul>
   **                                    <li>Else the folder is displayed.
   **                                  </ul>
   **                            </ul>
   **  @param context            the context in which the level expansion is
   **                            occurring.
   */
  @Override
  public void updateDir(final URLPath root, final String relativePath, final List element, final List directory, final Context context) {
    final Settings settings = Provider.settings(context.getProject());
    if (settings == null)
      return;

    final ContentSet contentSet = settings.contentSet();
    if (contentSet == null)
      return;

    // BUILDABLE and DEPLOYABLE are an immutable attribute that controls the
    // Make and Rebuild menu items.
    // we don't want to have any of build related files in the output of the
    // build process itself hence we try to advice the internal JDeveloper
    // process to ignore all files which belongs to the content set
    // TODO: Unfortunately it doesn't work in the way below all files are still
    // transfered to the output directory
    final Iterator cursor = element.iterator();
    while (cursor.hasNext()) {
      final Element e1 = (Element)cursor.next();
      e1.getAttributes().unset(ElementAttributes.BUILDABLE | ElementAttributes.DEPLOYABLE);
    }
  }
}