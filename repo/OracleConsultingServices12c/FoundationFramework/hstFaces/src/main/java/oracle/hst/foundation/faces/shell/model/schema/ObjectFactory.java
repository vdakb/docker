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

    System      :   Foundation Shared Library
    Subsystem   :   Java Server Faces Foundation

    File        :   ObjectFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ObjectFactory.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces.shell.model.schema;

import javax.xml.bind.annotation.XmlRegistry;

////////////////////////////////////////////////////////////////////////////////
// class ObjectFactory
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This object contains factory methods for each Java content interface and
 ** Java element interface generated in the
 ** oracle.hst.foundation.faces.shell.model.schema package.
 ** <p>
 ** An ObjectFactory allows you to programatically construct new instances of
 ** the Java representation for XML content. The Java representation of XML
 ** content can consist of schema derived interfaces and classes representing
 ** the binding of schema type definitions, element declarations and model
 ** groups. Factory methods for each of these are provided in this class.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@XmlRegistry
public class ObjectFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  final static String NAMESPACE = "http://xmlns.oracle.com/hst/adf/shell";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ObjectFactory</code> that can be used to create new
   ** instances of schema derived classes for package:
   ** <code>oracle.hst.foundation.faces.shell.model.schema</code>
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public ObjectFactory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createShellConfig
  /**
   ** Create an instance of {@link ShellConfig}.
   **
   ** @return                    an instance of {@link ShellConfig}.
   */
  public ShellConfig createShellConfig() {
    return new ShellConfig();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createShellConfigConsoles
  /**
   ** Create an instance of {@link ShellConfig.Consoles}.
   **
   ** @return                    an instance of {@link ShellConfig.Consoles}.
   */
  public ShellConfig.Consoles createShellConfigConsoles() {
    return new ShellConfig.Consoles();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createShellConfigModules
  /**
   ** Create an instance of {@link ShellConfig.Modules}.
   **
   ** @return                    an instance of {@link ShellConfig.Modules}.
   */
  public ShellConfig.Modules createShellConfigModules() {
    return new ShellConfig.Modules();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createShellConfigTaskflows
  /**
   ** Create an instance of {@link ShellConfig.Taskflows}.
   **
   ** @return                    an instance of {@link ShellConfig.Taskflows}.
   */
  public ShellConfig.Taskflows createShellConfigTaskflows() {
    return new ShellConfig.Taskflows();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createConsoleType
  /**
   ** Create an instance of {@link ConsoleType}.
   **
   ** @return                    an instance of {@link ConsoleType}.
   */
  public ConsoleType createConsoleType() {
    return new ConsoleType();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createConsoleTypeModules
  /**
   ** Create an instance of {@link ConsoleType.Modules}.
   **
   ** @return                    an instance of {@link ConsoleType.Modules}.
   */
  public ConsoleType.Modules createConsoleTypeModules() {
    return new ConsoleType.Modules();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createConsoleTypeModulesModule
  /**
   ** Create an instance of {@link ConsoleType.Modules.Module}.
   **
   ** @return                    an instance of
   **                            {@link ConsoleType.Modules.Module}.
   */
  public ConsoleType.Modules.Module createConsoleTypeModulesModule() {
    return new ConsoleType.Modules.Module();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createModuleType
  /**
   ** Create an instance of {@link ModuleType}.
   **
   ** @return                    an instance of {@link ModuleType}.
   */
  public ModuleType createModuleType() {
    return new ModuleType();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createModuleTypeDefaultTaskflowList
  /**
   ** Create an instance of {@link ModuleType.DefaultTaskflowList}.
   **
   ** @return                    an instance of
   **                            {@link ModuleType.DefaultTaskflowList}.
   */
  public ModuleType.DefaultTaskflowList createModuleTypeDefaultTaskflowList() {
    return new ModuleType.DefaultTaskflowList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createModuleTypeLhsArea
  /**
   ** Create an instance of {@link ModuleType.LhsArea}.
   **
   ** @return                    an instance of
   **                            {@link ModuleType.LhsArea}.
   */
  public ModuleType.LhsArea createModuleTypeLhsArea() {
    return new ModuleType.LhsArea();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createModuleTypeRhsArea
  /**
   ** Create an instance of {@link ModuleType.RhsArea}.
   **
   ** @return                    an instance of
   **                            {@link ModuleType.RhsArea}.
   */
  public ModuleType.RhsArea createModuleTypeRhsArea() {
    return new ModuleType.RhsArea();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createModuleTypeToolbarArea
  /**
   ** Create an instance of {@link ModuleType.ToolbarArea}.
   **
   ** @return                    an instance of
   **                            {@link ModuleType.ToolbarArea}.
   */
  public ModuleType.ToolbarArea createModuleTypeToolbarArea() {
    return new ModuleType.ToolbarArea();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createModuleTypeDefaultTaskflow
  /**
   ** Create an instance of {@link ModuleType.DefaultTaskflow}.
   **
   ** @return                    an instance of
   **                            {@link ModuleType.DefaultTaskflow}.
   */
  public ModuleType.DefaultTaskflow createModuleTypeDefaultTaskflow() {
    return new ModuleType.DefaultTaskflow();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createModuleTypeDefaultTaskflowListTaskflow
  /**
   ** Create an instance of {@link ModuleType.DefaultTaskflowList.Taskflow}.
   **
   ** @return                    an instance of
   **                            {@link ModuleType.DefaultTaskflowList.Taskflow}.
   */
  public ModuleType.DefaultTaskflowList.Taskflow createModuleTypeDefaultTaskflowListTaskflow() {
    return new ModuleType.DefaultTaskflowList.Taskflow();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createModuleTypeLhsAreaTaskflow
  /**
   ** Create an instance of {@link ModuleType.LhsArea.Taskflow}.
   **
   ** @return                    an instance of
   **                            {@link ModuleType.LhsArea.Taskflow}.
   */
  public ModuleType.LhsArea.Taskflow createModuleTypeLhsAreaTaskflow() {
    return new ModuleType.LhsArea.Taskflow();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createModuleTypeRhsAreaTaskflow
  /**
   ** Create an instance of {@link ModuleType.RhsArea.Taskflow}.
   **
   ** @return                    an instance of
   **                            {@link ModuleType.RhsArea.Taskflow}.
   */
  public ModuleType.RhsArea.Taskflow createModuleTypeRhsAreaTaskflow() {
    return new ModuleType.RhsArea.Taskflow();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createModuleTypeToolbarAreaTaskflow
  /**
   ** Create an instance of {@link ModuleType.ToolbarArea.Taskflow}.
   **
   ** @return                    an instance of
   **                            {@link ModuleType.ToolbarArea.Taskflow}.
   */
  public ModuleType.ToolbarArea.Taskflow createModuleTypeToolbarAreaTaskflow() {
    return new ModuleType.ToolbarArea.Taskflow();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTaskFlowType
  /**
   ** Create an instance of {@link TaskFlowType}.
   **
   ** @return                    an instance of {@link TaskFlowType}.
   */
  public TaskFlowType createTaskFlowType() {
    return new TaskFlowType();
  }
}