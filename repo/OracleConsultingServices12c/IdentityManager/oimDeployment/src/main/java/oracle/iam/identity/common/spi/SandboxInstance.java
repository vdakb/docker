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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   SandboxInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SandboxInstance.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.LinkedHashMap;

import java.io.File;

import org.apache.tools.ant.BuildException;

import oracle.iam.provisioning.vo.FormInfo;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.foundation.utility.DateUtility;
import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.deployment.spi.AbstractInstance;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeaturePlatformTask;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class SandboxInstance
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>SandboxInstance</code> represents the Form Designer in Identity
 ** Manager that is used to create forms and the ADF taskflows.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SandboxInstance extends AbstractInstance implements SandboxMetadata {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the working directory */
  private File                 path          = null;

  /** the user id written to the sandbox descriptor during generation */
  private String               user          = FeaturePlatformTask.USERNAME;

  /** the comment written to the sandbox descriptor during generation */
  private String               comment       = "IdM Long Running Sandbox";

  /** the description written to the sandbox descriptor during generation */
  private String               description   = "IdM Long Running Sandbox";

  /** the timestamp the sandbox was created on */
  private Date                 created       = DateUtility.now();

  /** the timestamp the sandbox was last updated */
  private Date                 updated       = DateUtility.now();

  /**
   ** The Identity Manager release specific version data.
   */
  Version                       version      = Version.PS3;
  /**
   ** The <code>Resource Object</code> of Identity Manager an
   ** <code>Application Instance</code> belongs to.
   */
  String                       resource      = null;

  /**
   ** The <code>Bundle</code> wrappes the resource bundle metadata to generate
   ** the ADF pages.
   */
  List<Bundle>                 bundle        = null;

  /**
   ** The <codeRequest Form</code> to be generated in Identity Manager and to be
   ** associated with the an <code>Application Instance</code>.
   */
  String                       dataSet       = null;

  /**
   ** The <code>metadata</code> wrappes the process form data discovered form
   ** Identitiy Manager.
   ** <br>
   ** Access to this instance attribute is restricted to package
   */
  Metadata                     metadata      = null;

  /**
   ** The <code>Account</code> form in Identity Manager the request dataset of
   ** an <code>Application Instance</code> belongs to.
   */
  FormInstance.Account         account       = null;

  /**
   ** Indicates that output of an previously exceuted marshalling process needs
   ** to be overriden at the next time
   */
  private ForceOverride        forceOverride = ForceOverride.FALSE;

  /**
   ** Indicates that the working directory has to be deleted after the compression.
   */
  private boolean              cleanup       = false;

  /**
   ** Represents the minor version of the CatalogAM file.
   */
  private int                  minor         = 0;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Version
  // ~~~~ ~~~~~~~
  /**
   ** <code>Version</code> enumerates the applicable Identity Manager relases.
   */
  public static enum Version {
      PS3("12.2.1.3.0", "11.1.1.4.0", "12.2.1.9.14",  "12.2.1_20170820.0914")
    , PS4("12.2.1.4.0", "11.1.1.4.0", "12.2.1.22.48", "12.2.1_20190911.2248")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    public final String id;
    public final String sandbox;
    public final String persistence;
    public final String customization;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Version</code> with a specific version indicattor.
     **
     **
     ** @param  id               the textual expression of this
     **                          <code>Version</code>.
     ** @param  sandbox          the version number of the sandbox metadata.
     ** @param  customization    the version number of the customization model
     **                          metadata.
     */
    Version(final String id, final String sandbox, final String persistence, final String customization) {
      this.id            = id;
      this.sandbox       = sandbox;
      this.persistence   = persistence;
      this.customization = customization;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Version</code> form the given
     ** string value.
     **
     ** @param  value              the string value the <code>Version</code>
     **                            should be returned
     **                            for.
     **
     ** @return                    the <code>Version</code>.
     */
    public static Version from(final String value) {
      // create is the default version nothing else hence if an empty value is
      // passed in we have to assume the attribute value isn't specified hence
      // the default behavior have to be applied
      if (StringUtility.isEmpty(value))
        return PS3;

      for (Version cursor : Version.values()) {
        if (cursor.id.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Bundle
  // ~~~~~ ~~~~~~
  /**
   ** <code>Bundle</code> wrappes the resource bundle that is used to apply
   ** proper labels, headers and hints on a ADF page.
   */
  public static class Bundle {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String clazz = null;
    private String scope = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Bundle</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Bundle() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Bundle</code> with the <code>name</code> of the
     ** implementing class for the resource bundle and the name of the exported
     ** <code>scope</code>.
     **
     ** @param  clazz            the class name of the resource bundle.
     ** @param  scope            the name of the exported scoped variable to
     **                          hold the value used in in an ADF page view.
     */
    private Bundle(final String clazz, final String scope) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.clazz = clazz;
      this.scope = scope;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: clazz
    /**
     ** Call by the ANT kernel to inject the argument for clazz option.
     **
     ** @param  clazz            the class name of the resource bundle.
     */
    public void clazz(final String clazz) {
      // prevent bogus input
      if (StringUtility.isEmpty(clazz))
        handleAttributeMissing("class");

      // prevent bogus state
      if (!StringUtility.isEmpty(this.clazz))
        handleAttributeError("class");

      this.clazz = clazz;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: clazz
    /**
     ** Returns the class name of the resource bundle.
     **
     ** @return                  the class name of the resource bundle.
     */
    public final String clazz() {
      return this.clazz;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: scope
    /**
     ** Call by the ANT kernel to inject the argument for scope option.
     **
     ** @param  scope            the name of the exported scoped variable to
     **                          hold the value used in in an ADF page view.
     */
    public void scope(final String scope) {
      // prevent bogus input
      if (StringUtility.isEmpty(scope))
        handleAttributeMissing("scope");

      // prevent bogus state
      if (!StringUtility.isEmpty(this.scope))
        handleAttributeError("scope");

      this.scope = scope;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: scope
    /**
     ** Returns the name of the exported scoped variable to hold the value used
     ** in in an ADF page view.
     **
     ** @return                  the name of the exported scoped variable to
     **                          hold the value used in in an ADF page view.
     */
    public final String scope() {
      return this.scope;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
    /**
     ** Returns a hash code value for the object.
     ** <br>
     ** This method is supported for the benefit of hash tables such as those
     ** provided by {@link java.util.HashMap}.
     ** <p>
     ** The general contract of <code>hashCode</code> is:
     ** <ul>
     **   <li>Whenever it is invoked on the same object more than once during an
     **       execution of a Java application, the <code>hashCode</code> method
     **       must consistently return the same integer, provided no information
     **       used in <code>equals</code> comparisons on the object is modified.
     **       This integer need not remain consistent form one execution of an
     **       application to another execution of the same application.
     **   <li>If two objects are equal according to the
     **       <code>equals(Object)</code> method, then calling the
     **       <code>hashCode</code> method on each of the two objects must
     **       produce the same integer result.
     **   <li>It is <em>not</em> required that if two objects are unequal
     **       according to the {@link java.lang.Object#equals(java.lang.Object)}
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     */
    @Override
    public int hashCode() {
      long hc = getClass().hashCode();
      if (this.clazz != null) {
        hc += this.clazz.hashCode();
      }
      return (int)hc;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overriden)
    /**
     ** Compares this instance with the specified object.
     ** <p>
     ** The result is <code>true</code> if and only if the argument is not
     ** <code>null</code> and is a <code>Bundle</code> object that represents
     ** the same <code>name</code> as this object.
     **
     ** @param other               the object to compare this
     **                            <code>Bundle</code> against.
     **
     ** @return                   <code>true</code> if the
     **                           <code>Bundle</code>s are
     **                           equal; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(final Object other) {
      if (!(other instanceof Bundle))
        return false;

      final Bundle that = (Bundle)other;
      return this.clazz.equals(that.clazz);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   validate
    /**
     ** The entry point to validate the type to use.
     **
     ** @throws BuildException   in case the instance does not meet the
     **                          requirements.
     */
    private void validate()
      throws BuildException {

      // enforce validation of mandatory attributes if requested
      if (StringUtility.isEmpty(this.clazz))
        handleAttributeMissing("clazz");

      if (StringUtility.isEmpty(this.scope))
        handleAttributeMissing("scope");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Metadata
  // ~~~~~ ~~~~~~~~
  /**
   ** <code>Metadata</code> wrappes the process form data discovered form
   ** Identitiy Manager.
   */
  public static class Metadata {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** the metadata data of a process form discovered form Identitiy Manager.
     ** <br>
     ** Access to these instance attribute is restricted to package
     */
    long                  objectKey;
    String                objectName;
    long                  processFormKey;
    FormInfo              account;
    Map<String, Integer>  action = new LinkedHashMap<String, Integer>();
    Map<String, FormInfo> others = new LinkedHashMap<String, FormInfo>();

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Metadata</code> with the specified system identifier
     ** and name of the <code>Resource Object</code> this wrapper belongs to.
     ** <br>
     ** Access to this constructor is packege protcted hence any instantiation
     ** of the class can only be done by using this package.
     **
     ** @param  key                the value set for the key property of the
     **                            associated of the
     **                            <code>Resource Object</code>.
     ** @param  name               the value set for the name property of the
     **                            associated of the
     **                            <code>Resource Object</code>.
     */
    Metadata(final long key, final String name) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.objectKey  = key;
      this.objectName = name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: account
    /**
     ** Returns the metadata of the account process form.
     **
     ** @return                  the metadata of the account process form.
     */
    public final FormInfo account() {
      return this.account;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: others
    /**
     ** Returns the metadata of the other process actions.
     ** <p>
     ** This accessor method returns a reference to the live map, not a
     ** snapshot. Therefore any modification you make to the returned map will
     ** be present inside the object.
     ** <br>
     ** This is why there is not a <code>set</code> method for the resource
     ** property.
     ** <p>
     ** For example, to add a new item, do as follows:
     ** <pre>
     **   others().put(name, newItem);
     ** </pre>
     ** Objects of the following type(s) are allowed in the list
     ** {@link FormInfo}.
     **
     ** @return                  the {@link Map} of {@link Integer}s.
     */
    public final Map<String, Integer> actions() {
      return this.action;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: others
    /**
     ** Returns the metadata of the other process forms.
     ** <p>
     ** This accessor method returns a reference to the live map, not a
     ** snapshot. Therefore any modification you make to the returned map will
     ** be present inside the object.
     ** <br>
     ** This is why there is not a <code>set</code> method for the resource
     ** property.
     ** <p>
     ** For example, to add a new item, do as follows:
     ** <pre>
     **   others().put(name, newItem);
     ** </pre>
     ** Objects of the following type(s) are allowed in the list
     ** {@link FormInfo}.
     **
     ** @return                  the {@link List} of {@link FormInfo}s.
     */
    public final Map<String, FormInfo> others() {
      return this.others;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>SandboxInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SandboxInstance() {
    // ensure inheritance
    super();

    // add the default bundle Identity Manager use so it can never be forgotten
    // to declare
    this.bundle = CollectionUtility.list(new Bundle("oracle.iam.ui.OIMUIBundle", "resBundle"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SandboxInstance</code> with the specified name.
   **
   ** @param  working            the directory a <code>Sandbox</code> will be
   **                            generated within.
   ** @param  name               the value set for the name property of the
   **                            sandbox.
   ** @param  dataSet            the <code>Request Form</code> to be generated
   **                            in Identity Manager and to be associated with
   **                            the an <code>Application Instance</code>.
   ** @param  resource           the <code>Resource Object</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public SandboxInstance(final File working, final String name, final String dataSet, final String resource) {
    // ensure inheritance
    super(name);

    // initialize instance attributes
    this.path     = new File(working, name);
    this.dataSet  = dataSet;
    this.resource = resource;

    // add the default bundle Identity Manager use so it can never be forgotten
    // to declare
    this.bundle = CollectionUtility.list(new Bundle("oracle.iam.ui.OIMUIBundle", "resBundle"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (overriden)
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name context to handle in Oracle Weblogic
   **                            Domain server entity instance.
   **
   ** @return                    the <code>SandboxInstance</code> for method
   **                            chaining purpose.
   */
  public SandboxInstance name(final String name) {
    // ensure inheritance
    super.name(name);

    this.path = (this.path != null) ? new File(this.path.getParentFile(), name()) : new File(name);

    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Returns the directory the <code>Sandbox</code> will be generated within.
   ** <p>
   ** The directoty is the abstract path to the filesystem composed with the
   ** base working directory an the name of the sandbox to isolate sandboxs
   ** during generation.
   **
   ** @return                    the directory the <code>Sandbox</code> will be
   **                            generated within.
   */
  public File path() {
    return this.path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Called to inject the <code>working</code> directory a <code>Sandbox</code>
   ** will be generated within.
   **
   ** @param  path               the directory a <code>Sandbox</code> will be
   **                            generated within.
   **
   ** @return                    the <code>SandboxInstance</code> for method
   **                            chaining purpose.
   */
  public SandboxInstance path(final File path) {
    this.path = (name() != null) ? new File(path, name()) : path;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   user
  /**
   ** Called to inject the <code>user</code> a <code>Sandbox</code>
   ** descriptor contains to be imported into Identity Manager.
   **
   ** @param  user               the <code>user</code> a <code>Sandbox</code>
   **                            descriptor contains to be imported into
   **                            Identity Manager.
   **
   ** @return                    the <code>SandboxInstance</code> for method
   **                            chaining purpose.
   */
  public SandboxInstance user(final String user) {
    this.user = user;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   user
  /**
   ** Returns the <code>user</code> a <code>Sandbox</code> descriptor
   ** contains to be imported into Identity Manager.
   **
   ** @return                    the <code>user</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public String user() {
    return this.user;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   version
  /**
   ** Called to inject the <code>version</code> a <code>Sandbox</code>
   ** descriptor contains to be imported into Identity Manager.
   **
   ** @param  version            the <code>Version</code> a <code>Sandbox</code>
   **                            descriptor contains to be imported into
   **                            Identity Manager.
   **
   ** @return                    the <code>SandboxInstance</code> for method
   **                            chaining purpose.
   */
  public SandboxInstance version(final Version version) {
    this.version = version;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   version
  /**
   ** Returns the <code>Version</code> a <code>Sandbox</code> descriptor
   ** contains to be imported into Identity Manager.
   **
   ** @return                    the <code>Version</code> a <code>Sandbox</code>
   **                            belongs to.
   */
  public Version version() {
    return this.version;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   comment
  /**
   ** Called to inject the <code>comment</code> a <code>Sandbox</code>
   ** descriptor contains to be imported into Identity Manager.
   **
   ** @param  comment            the <code>comment</code> a <code>Sandbox</code>
   **                            descriptor contains to be imported into
   **                            Identity Manager.
   **
   ** @return                    the <code>SandboxInstance</code> for method
   **                            chaining purpose.
   */
  public SandboxInstance comment(final String comment) {
    this.comment = comment;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   comment
  /**
   ** Returns the <code>comment</code> a <code>Sandbox</code> descriptor
   ** contains to be imported into Identity Manager.
   **
   ** @return                    the <code>comment</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public String comment() {
    return this.comment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Called to inject the <code>description</code> a <code>Sandbox</code>
   ** descriptor contains to be imported into Identity Manager.
   **
   ** @param  description        the <code>description</code> a
   **                            <code>Sandbox</code> descriptor contains to be
   **                            imported into Identity Manager.
   **
   ** @return                    the <code>SandboxInstance</code> for method
   **                            chaining purpose.
   */
  public SandboxInstance description(final String description) {
    this.description = description;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the <code>description</code> a <code>Sandbox</code> descriptor
   ** contains to be imported into Identity Manager.
   **
   ** @return                    the <code>description</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public String description() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   created
  /**
   ** Called to inject the <code>created</code> a <code>Sandbox</code>
   ** descriptor contains to be imported into Identity Manager.
   **
   ** @param  created            the <code>created</code> a
   **                            <code>Sandbox</code> descriptor contains to be
   **                            imported into Identity Manager.
   **
   ** @return                    the <code>SandboxInstance</code> for method
   **                            chaining purpose.
   */
  public SandboxInstance created(final Date created) {
    this.created = created;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   created
  /**
   ** Returns the <code>created</code> a <code>Sandbox</code> descriptor
   ** contains to be imported into Identity Manager.
   **
   ** @return                    the <code>created</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public Date created() {
    return this.created;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updated
  /**
   ** Called to inject the <code>updated</code> a <code>Sandbox</code>
   ** descriptor contains to be imported into Identity Manager.
   **
   ** @param  updated            the <code>updated</code> a
   **                            <code>Sandbox</code> descriptor contains to be
   **                            imported into Identity Manager.
   **
   ** @return                    the <code>SandboxInstance</code> for method
   **                            chaining purpose.
   */
  public SandboxInstance updated(final Date updated) {
    this.updated = updated;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updated
  /**
   ** Returns the <code>updated</code> a <code>Sandbox</code> descriptor
   ** contains to be imported into Identity Manager.
   **
   ** @return                    the <code>updated</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public Date updated() {
    return this.updated;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataSet
  /**
   ** Called to inject the <code>Request Form</code> to be generated in Oracle
   ** Identity Manager and to be associated with the an
   ** <code>Application Instance</code>.
   **
   ** @param  dataSet            the <code>Request Form</code> to be generated in
   **                            Identity Manager and to be associated with the
   **                            an <code>Application Instance</code>.
   **
   ** @return                    the <code>SandboxInstance</code> for method
   **                            chaining purpose.
   */
  public SandboxInstance dataSet(final String dataSet) {
    this.dataSet = dataSet;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataSet
  /**
   ** Returns the <code>Request Form</code> to be generated in Identity Manager
   ** and to be associated with the an <code>Application Instance</code>.
   **
   ** @return                    the <code>Request Form</code> to be generated
   **                            in Identity Manager and to be associated with
   **                            the an <code>Application Instance</code>.
   */
  public String dataSet() {
    return this.dataSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Called to inject the <code>Resource Object</code> an
   ** <code>Application Instance</code> belongs to in Identity Manager.
   **
   ** @param  resource           the <code>Resource Object</code> an
   **                            <code>Application Instance</code> belongs to.
   **
   ** @return                    the <code>SandboxInstance</code> for method
   **                            chaining purpose.
   */
  public SandboxInstance resource(final String resource) {
    this.resource = resource;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Returns the <code>Resource Object</code> an
   ** <code>Application Instance</code> belongs to in Identity Manager.
   **
   ** @return                    the <code>Resource Object</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public String resource() {
    return this.resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   account
  /**
   ** Called to inject the <code>Account</code> an
   ** <code>Application Instance</code> belongs to in Identity Manager.
   **
   ** @param  account            the <code>Account</code> an
   **                            <code>Application Instance</code> belongs to.
   **
   ** @return                    the <code>SandboxInstance</code> for method
   **                            chaining purpose.
   */
  public SandboxInstance account(final FormInstance.Account account) {
    // prevent bogus state
    if (this.account != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TASK_ELEMENT_ONLYONCE, "account"));

    this.account = account;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   account
  /**
   ** Returns the <code>AccountInstance</code> an
   ** <code>Application Instance</code> belongs to in Identity Manager.
   **
   ** @return                    the <code>AccountInstance</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public FormInstance.Account account() {
    return this.account;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bundle
  /**
   ** Returns the {@link Bundle} attached to this <code>Sandbox</code> instance.
   ** Manager.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the object. This is why there is not a <code>set</code> method for
   ** the attribute property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **   bundle().add(newItem);
   ** </pre>
   ** Objects of the following type(s) are allowed in the list {@link Bundle}
   **
   ** @return                    the {@link Bundle} attached to this
   **                            <code>Sandbox</code> instance.
   */
  public List<Bundle> bundle() {
    return this.bundle;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   metadata
  /**
   ** Returns the {@link Metadata} for a process form discovered form Identitiy
   ** Manager.
   **
   ** @return                    the {@link Metadata} for a process form
   **                            discovered form Identitiy Manager.
   */
  public Metadata metadata() {
    return this.metadata;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   forceOverride
  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>forceOverride</code>.
   **
   ** @param  forceOverride      <code>true</code> to override the existing
   **                            files without to aks for user confirmation.
   */
  public void forceOverride(final String forceOverride) {
    this.forceOverride = ForceOverride.from(forceOverride);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   forceOverride
  /**
   ** Returns the how to handle existing files.
   **
   ** @return                    <code>ForceOverride.TRUE</code> if the existing files will be
   **                            overridden without any further confirmation.
   */
  public final ForceOverride forceOverride() {
    return this.forceOverride;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cleanup
  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>cleanup</code>.
   **
   ** @param  cleanup           <code>true</code> to delete the working directory.
   */
  public void cleanup(final boolean cleanup) {
    this.cleanup = cleanup;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   cleanup
  /**
   ** Returns the how to handle working directory.
   **
   ** @return                    <code>true</code> if the working directory
   **                            has to be deleted after compression.
   */
  public final boolean cleanup() {
    return this.cleanup;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   minor
  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>minor</code>.
   **
   ** @param  minor           <code>int</code> which represents the minor
   **                         version of the CatalogAM file.
   */
  public void minor(final int minor) {
    this.minor = minor;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   minor
  /**
   ** Returns the minor version of the CatalogAM file.
   **
   ** @return                    <code>int</code> which represents the minor
   **                            version of the CatalogAM file.
   */
  public final int minor() {
    return this.minor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the type to use.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate()
    throws BuildException {

    // enforce validation of mandatory attributes if requested
    if (this.path == null)
      handleAttributeMissing("path");

    // enforce validation of mandatory attributes if requested
    if (StringUtility.isEmpty(this.resource))
      handleAttributeMissing("resource");

    // enforce validation of mandatory attributes if requested
    if (this.account == null)
      handleAttributeMissing("account");

    // enforce validation of mandatory attributes if requested
    if (CollectionUtility.empty(this.bundle))
      for (Bundle cursor : this.bundle)
        cursor.validate();

    // ensure inheritance
    super.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addBundle
  /**
   ** Call by the ANT deployment to inject the argument for adding an
   ** {@link Bundle}.
   **
   **
   ** @param  clazz              the class name of the resource bundle.
   ** @param  scope              the name of the exported scoped variable to
   **                            hold the value used in in an ADF page view.
   **
   ** @return                    the <code>SandboxInstance</code> for method
   **                            chaining purpose.
   **
   ** @throws BuildException     if the {@link Bundle} is contained in the
   **                            collection is of this sandbox instance.
   */
  public SandboxInstance addBundle(final String clazz, final String scope)
    throws BuildException {

    return add(new Bundle(clazz, scope));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Call by the ANT deployment to inject the argument for adding an
   ** {@link Bundle}.
   **
   ** @param  bundle             the bundle a sandbox will use to localize
   **                            strings.
   **
   ** @return                    the <code>SandboxInstance</code> for method
   **                            chaining purpose.
   **
   ** @throws BuildException     if the {@link Bundle} is contained in the
   **                            collection is of this sandbox instance.
   */
  public SandboxInstance add(final Bundle bundle)
    throws BuildException {

    // check if a bundle can be added to this instannce or if it has to stick on
    // a single file
    if (this.bundle.contains(bundle))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.SANDBOX_BUNDLE_ONLYONCE, bundle.clazz));

    this.bundle.add(bundle);
    return this;
  }
}