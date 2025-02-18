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

    File        :   Domain.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Domain.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.uid.state;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.util.stream.Collectors;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import javax.inject.Named;

import javax.faces.bean.ApplicationScoped;

import oracle.hst.platform.jsf.Message;

import bka.iam.identity.igs.model.Active;
import bka.iam.identity.igs.model.Status;
import bka.iam.identity.igs.model.Language;

////////////////////////////////////////////////////////////////////////////////
// class Domain
// ~~~~~ ~~~~~~
/**
 ** The application domain value provider.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Named("domain")
@ApplicationScoped
public class Domain implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3760038478752193349")
  private static final long serialVersionUID = -3838814048651280702L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final List<Active>    active           = new ArrayList<>();
  private Map<Boolean, Active>  activeMapping;  

  private final List<Status>    status           = new ArrayList<>();
  private Map<Integer, Status>  statusMapping;  

  private final List<Language>  locale           = new ArrayList<>();
  private Map<String, Language> localeMapping;  

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Domain</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Domain() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getStatus
  /**
   ** Returns an unmodifiable collection of available generator states.
   **
   ** @return                    the collection of available generator states.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Status}.
   */
  public List<Status> getStatus() {
    return Collections.unmodifiableList(this.status);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getStatusMapping
  public Map<Integer, Status> getStatusMapping() {
    if (this.statusMapping == null) {
      this.statusMapping = getStatus().stream().collect(Collectors.toMap(Status::getState, generator -> generator));
    }
    return this.statusMapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getActive
  /**
   ** Returns an unmodifiable collection of available activation states.
   **
   ** @return                    the collection of available activation states.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Active}.
   */
  public List<Active> getActive() {
    return Collections.unmodifiableList(this.active);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getActiveMapping
  public Map<Boolean, Active> getActiveMapping() {
    if (this.activeMapping == null) {
      this.activeMapping = getActive().stream().collect(Collectors.toMap(Active::getState, activation -> activation));
    }
    return this.activeMapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLocale
  /**
   ** Returns an unmodifiable collection of available language locales.
   **
   ** @return                    the collection of available language locales.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Language}.
   */
  public List<Language> getLocale() {
    return Collections.unmodifiableList(this.locale);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLocaleMapping
  public Map<String, Language> getLocaleMapping() {
    if (this.localeMapping == null) {
      this.localeMapping = getLocale().stream().collect(Collectors.toMap(Language::getCode, language -> language));
    }
    return this.localeMapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by funtionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** Callback notification to signal dependency injection is done to perform
   ** any initialization and the instance becomes to put in service.
   */
  @PostConstruct
  public void initialize() {
    this.active.add(new Active(Boolean.TRUE,  Message.resourceValue("app$bundle", "active.true")));
    this.active.add(new Active(Boolean.FALSE, Message.resourceValue("app$bundle", "active.false")));

    this.status.add(new Status(0, Message.resourceValue("app$bundle", "status.0")));
    this.status.add(new Status(1, Message.resourceValue("app$bundle", "status.1")));
    this.status.add(new Status(2, Message.resourceValue("app$bundle", "status.2")));
    this.status.add(new Status(4, Message.resourceValue("app$bundle", "status.4")));

    this.locale.add(new Language("de", Message.resourceValue("app$bundle", "locale.de")));
    this.locale.add(new Language("en", Message.resourceValue("app$bundle", "locale.en")));
    this.locale.add(new Language("fr", Message.resourceValue("app$bundle", "locale.fr")));
  }
}