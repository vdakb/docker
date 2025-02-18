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

    File        :   FileSystemType.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    FileSystemType.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.104 2023-08-13  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.adapter;

import javax.naming.Referenceable;

import oracle.adf.share.connection.ConnectionType;

import oracle.jdeveloper.connection.iam.model.FileSystem;

////////////////////////////////////////////////////////////////////////////////
// class FileSystemType
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Describes a type of connection and provides the name of the
 ** InitialContextFactory that should be used to create an resource catalog
 ** adapter for a provider of this type.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.104
 ** @since   12.2.1.3.42.60.104
 */
public abstract class FileSystemType implements ConnectionType {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final Class<FileSystem> CLASS = FileSystem.class;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>FileSystemType</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected FileSystemType() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getId (ConnectionType)
  /**
   ** Returns the unique id of the connection type.
   ** <b>Note</b>:
   ** <br>
   ** Changing the return value of this method will require a migrator to update
   ** ADF Library jar deployment profile connection exclusion values.
   **
   ** @return                    the unique id of the connection type.
   */
  @Override
  public String getId() {
    return CLASS.getName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isInstanceOf (ConnectionType)
  /**
   ** Returns <code>true</code> if an the named connection class is represented
   ** by this <code>ConnectionType</code>.
   **
   ** @param className           the name of the connection class whose type
   **                            should be compared to this
   **                            <code>ConnectionType</code>.
   **
   ** @return                    <code>true</code> if an instance of the named
   **                            connection class would match this
   **                            <code>ConnectionType</code>; otherwise
   **                            <code>false</code>.
   */
  @Override
  public boolean isInstanceOf(final String className) {
    return CLASS.getName().equals(className);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isInstanceOf (ConnectionType)
  /**
   ** Returns <code>true</code> if an instance of the connection class would
   ** match this <code>ConnectionType</code>.
   **
   ** @param clazz               the connection class whose type should be
   **                            compared to this <code>ConnectionType</code>.
   **
   ** @return                    <code>true</code> if an instance of the
   **                            specified connection class would match this
   **                            <code>ConnectionType</code>; otherwise
   **                            <code>false</code>.
   */
  @Override
  public boolean isInstanceOf(final Class clazz) {
    return CLASS.equals(clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isInstanceOf (ConnectionType)
  /**
   ** Returns <code>true</code> if the specified connection object instance
   ** matches this <code>ConnectionType</code>.
   **
   ** @param connection          the connection instance whose type should be
   **                            compared to this <code>ConnectionType</code>.
   **
   ** @return                    <code>true</code> if the specified connection
   **                            instance matches this
   **                            <code>ConnectionType</code>; otherwise
   **                            <code>false</code>.
   */
  @Override
  public boolean isInstanceOf(final Referenceable connection) {
    return connection instanceof FileSystem;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getADFConnectionsChildMBeanImplementor (ConnectionType)
  /**
   ** Returns classname if this connection type has implemented connection mbean
   ** as specified by the
   ** <code>oracle.adf.mbean.share.connection.ADFConnectionsChild</code>.
   **
   ** @return                    a className if this connection type has
   **                            implemented child connection mbean, else return
   **                            <code>null</code>
   */
  @Override
  public String getADFConnectionsChildMBeanImplementor() {
    return null;
  }
}