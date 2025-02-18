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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   ReplacePasswords.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ReplacePasswords.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.jps.task;

import java.util.Map;
import java.util.TreeMap;
import java.util.Properties;

import java.io.Reader;
import java.io.InputStream;
import java.io.IOException;

import java.util.SortedMap;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.Parameter;

import org.apache.tools.ant.filters.ReplaceTokens;
import org.apache.tools.ant.filters.ChainableReader;

import org.apache.tools.ant.util.FileUtils;

import oracle.security.jps.JpsContext;
import oracle.security.jps.JpsException;
import oracle.security.jps.JpsContextFactory;

import oracle.security.jps.service.credstore.CredStoreException;
import oracle.security.jps.service.credstore.PasswordCredential;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceResourceBundle;
import oracle.hst.deployment.AbstractFilterReader;

////////////////////////////////////////////////////////////////////////////////
// class ReplacePasswords
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Replaces tokens in the original input with user-supplied values.
 ** <p>
 ** Example:
 ** <pre>
 **   &lt;replacetokens begintoken=&quot;#&quot; endtoken=&quot;#&quot;&gt;
 **     &lt;token key=&quot;SCP_USER&quot; value=&quot;${SCP_USER}&quot;/&gt;
 **   &lt;/replacetokens&gt;</pre>
 ** Or:
 ** <pre>
 **   &lt;filterreader classname="org.apache.tools.ant.filters.ReplaceTokens"&gt;
 **     &lt;param type="token"     name="SCP_USER"   value="${SCP_USER}"/&gt;
 **     &lt;param type="tokenchar" name="begintoken" value="#"/&gt;
 **     &lt;param type="tokenchar" name="endtoken"   value="#"/&gt;
 **   &lt;/filterreader&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ReplacePasswords  extends    AbstractFilterReader
                               implements ChainableReader {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the path to the JPS configuration file. */
  private String                        config;

  /** the name of jps context to get the credential store from. */
  private String                        context;

  /** the map name of the credential to replace. */
  private String                        map;

  /** the alias name of the credential to replace. */
  private String                        alias;

  /** index into replacement data */
  private int                           index         = -1;

  /** replacement test from a token */
  private String                        replace       = null;

  /** used for comparisons and lookup into the resolved map. */
  private String                        buffer        = "";

  /**
   ** This map holds the "resolved" tokens (begin- and end-tokens are added to
   ** make searching simpler)
   */
  private final TreeMap<String, String> resolved      = new TreeMap<String, String>();
  private boolean                       resolvedBuilt = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ReplacePasswords</code> Ant filter that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ReplacePasswords() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ReplacePasswords</code> as an Ant filtered reader.
   **
   ** @param  reader             a {@link Reader} object providing the
   **                            underlying stream.
   **                            Must not be <code>null</code>.
   */
  public ReplacePasswords(final Reader reader) {
    // ensure inheritance
    super(reader);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setConfig
  /**
   ** Sets the path to the JPS configuration file.
   **
   ** @param  value              the path to the JPS configuration file.
   */
  public final void setConfig(final String value) {
    this.config = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   config
  /**
   ** Returns the path to the JPS configuration file.
   **
   ** @return                    the path to the JPS configuration file.
   */
  private String config() {
    return this.config;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContext
  /**
   ** Sets the name of jps context to get the credential store from.
   **
   ** @param  value              the name of jps context to get the credential
   **                            store from.
   */
  public final void setContext(final String value) {
    this.context = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   context
  /**
   ** Returns the name of jps context to get the credential store from.
   **
   ** @return                    the name of jps context to get the credential
   **                            store from.
   */
  private String context() {
    return this.context;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMap
  /**
   ** Sets map name of the credential to replace.
   **
   ** @param  value              map name of the credential to replace.
   */
  public final void setMap(final String value) {
    this.map = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   map
  /**
   ** Returns map name of the credential to replace.
   **
   ** @return                    map name of the credential to replace.
   */
  private String map() {
    return this.map;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAlias
  /**
   ** Sets the alias name of the credential to replace.
   **
   ** @param  value              the alias name of the credential to replace.
   */
  public final void setAlias(final String value) {
    this.alias = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   alias
  /**
   ** Returns the alias name of the credential to replace.
   **
   ** @return                    the alias name of the credential to replace.
   */
  private String alias() {
    return this.alias;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented intefaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   chain (ChainableReader)
  /**
   ** Factory method to createsa new <code>ReplacePasswords</code> filter using
   ** the passed in {@link Reader} for instantiation.
   **
   ** @param  reader             a {@link Reader} object providing the
   **                            underlying stream.
   **                            Must not be <code>null</code>.
   **
   ** @return                    a new filter based on this configuration, but
   **                            filtering the specified reader.
   */
  @Override
  public Reader chain(final Reader reader) {
    final ReplacePasswords filter = new ReplacePasswords(reader);
    filter.setBeginToken(tokenBegin());
    filter.setEndToken(tokenEnd());
    filter.token(token());
    filter.setInitialized(true);
    filter.setConfig(config());
    filter.setContext(context());
    filter.setMap(map());
    filter.setAlias(alias());
    return filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredToken
  /**
   ** Adds a token element to the map of tokens to replace.
   **
   ** @param  token              the token to add to the map of replacements.
   **                            Must not be <code>null</code>.
   */
  public void addConfiguredToken(final ReplaceTokens.Token token) {
    this.token.put(token.getKey(), token.getValue());
    // invalidate to build them again if they have been built already.
    this.resolvedBuilt = false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read
  @Override
  public int read()
    throws IOException {

    if (!getInitialized()) {
      initialize();
      setInitialized(true);
    }

    if (!this.resolvedBuilt) {
      // initialize the JPS context to read the wallet
      try {
    		System.setProperty("opss.tenant.mode", "JPS_API");
        // path of the jps-config-jse.xml
		    System.setProperty("oracle.security.jps.config", this.config);
        debug(ServiceResourceBundle.string(ServiceMessage.OPSS_CONTEXT_CREATE), this.context);
        final JpsContext context = JpsContextFactory.getContextFactory().getContext(this.context);
        debug(ServiceResourceBundle.string(ServiceMessage.OPSS_CONTEXT_CREATED), this.context);
        debug(ServiceResourceBundle.string(ServiceMessage.OPSS_CREDENTIALSTORE_OBTAIN), this.context);
        final oracle.security.jps.service.credstore.CredentialStore store = context.getServiceInstance(oracle.security.jps.service.credstore.CredentialStore.class);
        if (store == null) {
          if (failonerror())
            throw new BuildException(ServiceResourceBundle.format(ServiceMessage.OPSS_CREDENTIALSTORE_OBTAIN, this.context));
          error(ServiceResourceBundle.string(ServiceMessage.OPSS_CREDENTIALSTORE_OBTAIN), this.context);
        }
        else {
          debug(ServiceResourceBundle.string(ServiceMessage.OPSS_CREDENTIALSTORE_OBTAINED), this.context);
          store.refresh();
          if (!store.containsMap(this.map)) {
            if (failonerror()) {
              throw new BuildException(ServiceResourceBundle.format(ServiceError.OPSS_CREDENTIALMAP_MISSING, this.context, this.map));
            }
            error(ServiceResourceBundle.string(ServiceError.OPSS_CREDENTIALMAP_MISSING), this.map);
          }
          // build the resolved tokens tree map.
          for (Map.Entry<String, String> entry : this.token.entrySet()) {
            final String[] alias = {this.map, entry.getValue()};
            debug(ServiceResourceBundle.string(ServiceMessage.OPSS_CREDENTIAL_OBTAIN), alias);
            if (store.containsCredential(alias[0], alias[1])) {
              final PasswordCredential credential = (PasswordCredential) store.getCredential(alias[0], alias[1]);
              this.resolved.put(this.tokenBegin + entry.getKey() + this.tokenEnd, new String(credential.getPassword()));
              debug(ServiceResourceBundle.string(ServiceMessage.OPSS_CREDENTIAL_OBTAINED), alias);
            }
            else {
              final String[] arguments = {this.context, this.map, entry.getValue()};
              if (failonerror()) {
                throw new BuildException(ServiceResourceBundle.format(ServiceError.OPSS_CREDENTIALALIAS_MISSING, arguments));
              }
              error(ServiceResourceBundle.string(ServiceError.OPSS_CREDENTIALALIAS_MISSING), arguments);
             }
          }
        }
      }
      catch (CredStoreException e) {
        final String[] arguments = {this.context, this.context};
        if (failonerror()) {
          fatal(e);
          throw new BuildException(ServiceResourceBundle.format(ServiceError.OPSS_CREDENTIALSTORE_ERROR, arguments));
        }
        error(ServiceResourceBundle.string(ServiceError.OPSS_CONTEXT_ERROR), arguments);
      }
      catch (JpsException e) {
        if (failonerror()) {
          fatal(e);
          throw new BuildException(ServiceResourceBundle.format(ServiceError.OPSS_CONTEXT_ERROR, this.context));
        }
        error(ServiceResourceBundle.string(ServiceError.OPSS_CONTEXT_ERROR), this.context);
      }
      this.resolvedBuilt = true;
    }

    // are we currently serving replace data?
    if (this.replace != null) {
      if (this.index < this.replace.length()) {
        return this.replace.charAt(this.index++);
      }
      else {
        this.replace = null;
      }
    }

    // is the read buffer empty?
    if (this.buffer.isEmpty()) {
      int next = in.read();
      if (next == -1) {
        // end of stream. all buffers empty.
        return next;
      }
      this.buffer += (char)next;
    }
    for (;;) {
      // get the closest tokens
      final SortedMap<String, String> opportunity = this.resolved.tailMap(this.buffer);
      // if there is none, then deliver the first char from the buffer.
      if (opportunity.isEmpty() || !opportunity.firstKey().startsWith(this.buffer)) {
        return firstCharacter();
      }
      // there exists a nearest token - is it an exact match?
      else if (this.buffer.equals(opportunity.firstKey())) {
        // we have found a token. prepare the replaceData buffer.
        this.replace = this.resolved.get(this.buffer);
        this.index   = 0;
        // destroy the readBuffer - it's contents are being replaced entirely.
        this.buffer  = "";
        // get the first character via recursive call.
        return read();
      }
      // nearest token is not matching exactly - read one character more.
      else {
        int next = in.read();
        if (next != -1) {
          this.buffer += (char)next;
        }
        else {
          // end of stream. deliver remaining characters from buffer.
          return firstCharacter();
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** Initializes tokens and loads the replacee-replacer hashtable.
   */
  @Override
  protected void initialize() {
    final Parameter[] params = getParameters();
    if (params != null) {
      for (Parameter param : params) {
        if (param != null) {
          final String type = param.getType();
          if ("config".equals(type)) {
            final String name = param.getName();
            if ("path".equals(name)) {
              this.config = param.getValue();
            }
            else if ("context".equals(name)) {
              this.context = param.getValue();
            }
            else if ("map".equals(name)) {
              this.map = param.getValue();
            }
            else if ("alias".equals(name)) {
              this.alias = param.getValue();
            }
          }
        }
      }
    }
    // ensure inheritance
    super.initialize();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   firstCharacter
  /**
   ** Returns the first character from the read buffer or <code>-1</code> if
   ** read buffer is empty.
   **
   ** @return                    the first character from the read buffer or
   **                            <code>-1</code> if read buffer is empty.
   */
  private int firstCharacter() {
    if (this.buffer.isEmpty()) {
      return -1;
    }

    int c = this.buffer.charAt(0);
    this.buffer = this.buffer.substring(1);
    return c;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   make
  /**
   ** Adds properties from a specified properties file in the token map.
   **
   ** @param  resource           the resource to load properties from.
   */
  private void make(final Resource resource) {
    final Properties props = properties(resource);
    props.stringPropertyNames().forEach(key -> token.put(key, props.getProperty(key)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   properties
  /**
   ** Returns properties from a specified properties file.
   **
   ** @param  resource           the resource to load properties from.
   */
  private Properties properties(final Resource resource) {
    InputStream in = null;
    Properties props = new Properties();
    try {
      in = resource.getInputStream();
      props.load(in);
    }
    catch (IOException e) {
      if (getProject() != null) {
        getProject().log("getProperties failed, " + e.getMessage(), Project.MSG_ERR);
      }
      else {
        e.printStackTrace(); //NOSONAR
      }
    }
    finally {
      FileUtils.close(in);
    }

    return props;
  }
}