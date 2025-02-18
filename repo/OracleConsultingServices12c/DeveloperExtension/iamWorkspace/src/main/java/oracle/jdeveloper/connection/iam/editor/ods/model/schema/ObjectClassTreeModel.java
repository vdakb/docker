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

    File        :   ObjectClassTreeModel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ObjectClassTreeModel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.model.schema;

import java.util.Set;
import java.util.List;
import java.util.Collections;

import javax.swing.JTree;

import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

////////////////////////////////////////////////////////////////////////////////
// class ObjectClassTreeModel
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A model suitable for a particular object class of a Directory Service
 ** entry in a tree.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public class ObjectClassTreeModel extends DefaultTreeModel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8038265305025737011")
  private static final long    serialVersionUID = 3916206918201207724L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // cleanup any previously populated values
  final DefaultMutableTreeNode root;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ObjectClassTreeModel</code> that allows use as
   ** a JavaBean.
   ** <p>
   ** This constructor is protected to prevent other classes to use
   ** "new ObjectClassTreeModel(DefaultMutableTreeNode)".
   **
   ** @param  root               the top node of the tree to render
   **                            <br>
   **                            Allowed object is
   **                            {@link DefaultMutableTreeNode}.
   */
  private ObjectClassTreeModel(final DefaultMutableTreeNode root) {
    // ensure inheritance
    super(root);

    // initialize instance attributes
    this.root = root;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>ObjectClassTreeModel</code> object
   ** from the specified properties.
   **
   ** @return                    the validated
   **                            <code>ObjectClassTreeModel</code>.
   **                            <br>
   **                            Possible object
   **                            <code>ObjectClassTreeModel</code>.
   */
  public static ObjectClassTreeModel build() {
    return new ObjectClassTreeModel(new DefaultMutableTreeNode("invisible"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Populates an <code>ObjectClassDetailModel</code> responsible to display
   ** the populate data with labels and values of attribute types.
   **
   ** @param  tree               the view component this model belongs too
   **                            <br>
   **                            Allowed object is {@link JTree}.
   ** @param  data               the data mapping providing access to the
   **                            context.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public void refresh(final JTree tree, final Set<String> data) {
    // cleanup any previously populated values
    this.root.removeAllChildren();

    for (String cursor : data) {
      this.root.add(new DefaultMutableTreeNode(cursor));
    }
    nodeStructureChanged(this.root);
    expand(tree, this.root);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expand
  @SuppressWarnings("unchecked")
  private static void expand(final JTree tree, final DefaultMutableTreeNode node) {
    List<DefaultMutableTreeNode> list = Collections.<DefaultMutableTreeNode>list(node.children());
    for (DefaultMutableTreeNode treeNode : list) {
      expand(tree, treeNode);
    }
    if (node.isRoot()) {
     return;
    }
    final TreePath path = new TreePath(node.getPath());
    tree.expandPath(path);
  }
}