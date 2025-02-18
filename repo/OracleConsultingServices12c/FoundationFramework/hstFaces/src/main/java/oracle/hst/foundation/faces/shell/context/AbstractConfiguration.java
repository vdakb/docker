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

    File        :   AbstractConfiguration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractConfiguration.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces.shell.context;

import java.util.List;

import java.io.InputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Locale;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBException;

import javax.faces.context.ExternalContext;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ResourceBundle;

import oracle.hst.foundation.faces.shell.model.Entity;
import oracle.hst.foundation.faces.shell.model.Module;
import oracle.hst.foundation.faces.shell.model.TaskFlow;

import oracle.hst.foundation.faces.shell.model.schema.ConsoleType;
import oracle.hst.foundation.faces.shell.model.schema.ModuleType;
import oracle.hst.foundation.faces.shell.model.schema.ShellConfig;
import oracle.hst.foundation.faces.shell.model.schema.TaskFlowType;

////////////////////////////////////////////////////////////////////////////////
// class AbstractConfiguration
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Internal use only.
 ** <p>
 ** Backing bean for fetching the taskflow and module configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AbstractConfiguration implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1330421237113324715")
  private static final long     serialVersionUID = 3495842198664134617L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient ShellConfig config;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractConfiguration</code> event handler that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractConfiguration() {
    // ensure inheritance
    this("/WEB-INF/hstshell-config.xml");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractConfiguration</code> which use the specified
   ** path to obtain the configuration.
   ** <p>
   ** This constructor is intent to use for testing purpose only on the local
   ** file system.
   **
   ** @param  path               the path to the configuration to use.
   */
  public AbstractConfiguration(final String path) {
    // ensure inheritance
    super();

    // initialize instance
    load(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractConfiguration</code> which use the specified
   ** {@link InputStream} to obtain the configuration.
   **
   ** @param  stream             the {@link InputStream} the configuration has
   **                            to read from.
   */
  public AbstractConfiguration(final InputStream stream) {
    // initialize instance
    load(stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundle
  /**
   ** Returns the value of the resourceBundle property.
   **
   ** @return                    the value of the resourceBundle property.
   **                            Possible object is {@link String}.
   */
  protected final String resourceBundle() {
    // prevent bogus instance state
    if (this.config == null)
      throw new NullPointerException("config");

    return this.config.getResourceBundle();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locale
  /**
   ** Returns the value of the locale property.
   **
   ** @return                    the value of the locale property.
   **                            Possible object is {@link String}.
   */
  protected final String locale() {
    // prevent bogus instance state
    if (this.config == null)
      throw new NullPointerException("config");

    return this.config.getLocale();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   detailAreaTabWidth
  /**
   ** Returns the default display width of the tab for the detail area of a
   ** task flow.
   **
   ** @return                    the default display width of the tab for the
   **                            detail area of a task flow.
   */
  public Integer detailAreaTabWidth() {
    // prevent bogus instance state
    if (this.config == null)
      throw new NullPointerException("config");

    return this.config.getDetailAreaTabWidth();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   console
  protected ConsoleType console(final String consoleId) {
    // prevent bogus instance state
    if (this.config == null)
      throw new NullPointerException("config");

    ConsoleType console = null;
    // prevent bogus configuration
    if (this.config.getConsoles() == null)
      return console;

    for (ConsoleType cursor : this.config.getConsoles().getConsole()) {
      if (cursor.getId().equals(consoleId)) {
        console = cursor;
        break;
      }
    }
    return console;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   module
  protected Module module(final String moduleId, final ResourceBundle srb) {
    // prevent bogus instance state
    if (this.config == null)
      throw new NullPointerException("config");

    Module module = null;
    // prevent bogus configuration
    if (this.config.getModules() == null)
      return module;

    for (ModuleType m : this.config.getModules().getModule()) {
      if (m.getId().equals(moduleId)) {
        String rendered = m.getRendered();
        if (rendered != null) {
          final Boolean b = JSF.contextObject(rendered, Boolean.class);
          if ((b != null) && (!b.booleanValue()))
            return module;
        }
        else if ("false".equals(rendered)) {
          return module;
        }

        module = new Module();
        String name      = null;
        String icon      = null;
        String hover     = null;
        String depressed = null;
        String disabled  = null;
        String desc      = null;
        module.setId(m.getId());
        if (srb != null) {
          name      = (String)srb.get(moduleKey(m.getId(), Entity.NAME));
          icon      = (String)srb.get(moduleKey(m.getId(), Entity.ICON));
          hover     = (String)srb.get(moduleKey(m.getId(), Entity.HOVER));
          depressed = (String)srb.get(moduleKey(m.getId(), Entity.DEPRESSED));
          disabled  = (String)srb.get(moduleKey(m.getId(), Entity.DISABLED));
          desc      = (String)srb.get(moduleKey(m.getId(), Entity.DESCRIPTION));
        }
        module.setName(name == null ? m.getName() : name);
        module.setDescription(desc == null ? m.getDescription() : desc);
        module.setIcon(icon == null ? m.getIcon() : icon);
        module.setHoverIcon(hover == null ? m.getHoverIcon() : hover);
        module.setDisabledIcon(disabled == null ? m.getDisabledIcon() : disabled);
        module.setDepressedIcon(depressed == null ? m.getDepressedIcon() : depressed);
        String url = m.getUrl();
        if (url != null)
          url = JSF.contextObject(url, String.class);
        module.url(url);
        ModuleType.LhsArea navigationArea = m.getLhsArea();
        if (navigationArea != null) {
          TaskFlow tf = null;
          String navigationRendered = navigationArea.getRendered();
          if (navigationRendered != null) {
            final Boolean b = JSF.contextObject(navigationRendered, Boolean.class);
            tf = (b != null) && (b.booleanValue() == true) ? taskFlow(navigationArea.getTaskflow().getRefId(), srb) : null;
          }
          else if ("false".equals(navigationRendered)) {
            tf = null;
          }
          else {
            tf = taskFlow(navigationArea.getTaskflow().getRefId(), srb);
          }
          module.navigationTaskFlow(tf);
        }
        ModuleType.ToolbarArea toolBarArea = m.getToolbarArea();
        if (toolBarArea != null) {
          TaskFlow tf = null;
          final String tbRendered = toolBarArea.getRendered();
          if (tbRendered != null) {
            final Boolean b = JSF.contextObject(tbRendered, Boolean.class);
            tf = (b != null) && (b.booleanValue() == true) ? taskFlow(toolBarArea.getTaskflow().getRefId(), srb) : null;
          }
          else if ("false".equals(tbRendered)) {
            tf = null;
          }
          else {
            tf = taskFlow(toolBarArea.getTaskflow().getRefId(), srb);
          }
          module.toolBarTaskFlow(tf);
          String tbHeight = toolBarArea.getTaskflow().getHeight();
          if (tbHeight != null) {
            tbHeight = JSF.contextObject(tbHeight, String.class);
          }
          module.toolBarTaskFlowHeight(tbHeight);
        }
        final ModuleType.DefaultTaskflowList detailAreaTfs = m.getDefaultTaskflowList();
        final ModuleType.DefaultTaskflow     defaultTf     = m.getDefaultTaskflow();
        final List<TaskFlow>                 tfs = module.detailTaskFlow();
        if (defaultTf != null) {
          final TaskFlow tf = taskFlow(defaultTf.getRefId(), srb);
          if (tf != null)
            tfs.add(tf);
        }
        else if (detailAreaTfs != null) {
          for (ModuleType.DefaultTaskflowList.Taskflow tf : detailAreaTfs.getTaskflow()) {
            final TaskFlow dtf = taskFlow(tf.getRefId(), srb);
            if (dtf != null) {
              tfs.add(dtf);
            }
          }
        }
        String model = m.getModel();
        if (model != null) {
          model = JSF.contextObject(model, String.class);
        }
        if ("single".equals(model)) {
          module.regionModel(Module.RegionModel.SINGLE);
        }
        module.setTopic(m.getTopic());
        module.setProduct(m.getProduct());
        return module;
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   taskflows
  /**
   ** Returns the configured taskflows.
   **
   ** @param  locale             the {@link Locale} used to translate string
   **                            resources.
   **
   ** @return                    the configured taskflows.
   */
  public List<TaskFlow> taskflows(final Locale locale) {
    final List<TaskFlow> registered = new ArrayList<TaskFlow>();
    // prevent bogus configuration
    if (this.config.getTaskflows() == null)
      return registered;

    final String r = resourceBundle();
    ResourceBundle srb = null;
    if ((locale != null) && (!StringUtility.isEmpty(r)))
      srb = ResourceBundle.instance(r, locale, null);

    for (TaskFlowType cursor : this.config.getTaskflows().getTaskflow()) {
      final TaskFlow taskFlow = new TaskFlow();
      taskFlow.setId(cursor.getId());
      taskFlow.setInstance(cursor.getTaskFlow());
      taskFlow.setCloseable(cursor.isCloseable());
      taskFlow.setDialog(cursor.isDialog());
      taskFlow.setTopic(cursor.getTopic());
      taskFlow.setProduct(cursor.getProduct());
      taskFlow.setEnableLinking(Boolean.valueOf(cursor.getEnableLinking()));
      taskFlow.setAllowedURLParams(cursor.getAllowedURLParams());

      String name = null;
      String icon = null;
      String desc = null;
      if (srb != null) {
        name = (String)srb.get(taskFlowKey(cursor.getId(), Entity.NAME));
        icon = (String)srb.get(taskFlowKey(cursor.getId(), Entity.ICON));
        desc = (String)srb.get(taskFlowKey(cursor.getId(), Entity.DESCRIPTION));
      }

      taskFlow.setName(name == null ? cursor.getName() : name);
      taskFlow.setDescription(desc == null ? cursor.getDescription() : desc);
      taskFlow.setIcon(icon == null ? cursor.getIcon() : icon);

      registered.add(taskFlow);
    }
    return registered;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   taskFlow
  protected TaskFlow taskFlow(String taskflowId, final ResourceBundle srb) {
    // prevent bogus instance state
    if (this.config == null)
      throw new NullPointerException("config");

    if (taskflowId != null) {
      Object o = JSF.contextObject(taskflowId, Object.class);
      if ((o instanceof String)) {
        taskflowId = (String)o;
        // don't know why its called recursivly because it will not change
        // anything in the evaluated result
//        if (taskflowId != null) {
//          return taskFlow(taskflowId, srb);
//        }
      }
      else if ((o instanceof TaskFlow)) {
        return (TaskFlow)o;
      }
    }
    for (TaskFlowType t : this.config.getTaskflows().getTaskflow()) {
      if (t.getId().equals(taskflowId)) {
        String name = null;
        String icon = null;
        String desc = null;
        TaskFlow taskFlow = new TaskFlow();
        taskFlow.setId(t.getId());
        taskFlow.setInstance(t.getTaskFlow());
        taskFlow.setCloseable(t.isCloseable());
        taskFlow.setDialog(t.isDialog());
        taskFlow.setTopic(t.getTopic());
        taskFlow.setProduct(t.getProduct());
        if (srb != null) {
          name = (String)srb.get(taskFlowKey(t.getId(), Entity.NAME));
          icon = (String)srb.get(taskFlowKey(t.getId(), Entity.ICON));
          desc = (String)srb.get(taskFlowKey(t.getId(), Entity.DESCRIPTION));
        }
        taskFlow.name(name == null ? t.getName() : name);
        taskFlow.setDescription(desc == null ? t.getDescription() : desc);
        taskFlow.setIcon(icon == null ? t.getIcon() : icon);
        return taskFlow;
      }
    }
    return null;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   taskFlowKey
  protected String taskFlowKey(final String id, final String key) {
    return String.format("TaskFlow[%s].%s",id, key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   moduleKey
  protected String moduleKey(String id, String key) {
    return String.format("Module[%s].%s",id, key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   load
  /**
   ** Fetch the configuration from an embedded resource.
   */
  private void load(final String path) {
    final ExternalContext external = JSF.externalContext();
    load(external.getResourceAsStream(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   load
  /**
   ** Fetch the configuration from
   */
  private void load(final InputStream stream) {
    try {
      final JAXBContext  context    = JAXBContext.newInstance("oracle.hst.foundation.faces.shell.model.schema");
      final Unmarshaller marshaller = context.createUnmarshaller();
      this.config = (stream == null) ? new ShellConfig() : (ShellConfig)marshaller.unmarshal(stream);
    }
    catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }
}