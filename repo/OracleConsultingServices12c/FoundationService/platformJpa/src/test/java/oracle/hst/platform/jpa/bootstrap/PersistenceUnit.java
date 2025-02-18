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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Presistence Foundation Shared Library
    Subsystem   :   JPA Unit Testing

    File        :   PersistenceUnit.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PersistenceUnit.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa.bootstrap;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Collections;

import java.net.URL;

import java.util.stream.Collectors;

import javax.sql.DataSource;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;

import oracle.hst.platform.core.utility.FileSystem;

////////////////////////////////////////////////////////////////////////////////
// class PersistenceUnit
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>PersistenceUnit</code> implements the basic functionality to
 ** bootstrap an <code>EntityManagerFactory</code>.
 ** <p>
 ** Implemented by an extra class to keep it outside of the test case classes
 ** itself.
 ** <p>
 ** The JPA specification {@link PersistenceUnitInfo} interface encapsulates
 ** everything is needed for bootstrapping an <code>EntityManagerFactory</code>.
 ** <br>
 ** Typically, this interface is implemented by the JPA provider to store the
 ** info retrieved after parsing the <code>persistence.xml</code> configuration
 ** file.
 ** <p>
 ** Because we will not use a <code>persistence.xml</code> configuration file,
 ** we have to implement this interface ourselves.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PersistenceUnit implements PersistenceUnitInfo {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String VERSION  = "2.1";
  public static final String PROVIDER = "org.eclipse.persistence.jpa.PersistenceProvider";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String       name;
  private final List<String> managed;
  private final List<String> mapping          = new ArrayList<>();

  private Properties         properties;
  private DataSource         jtaDataSource;
  private DataSource         nonJtaDataSource;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PersistenceUnit</code> with the properties specified.
   **
   ** @param  name               the name of the peristence unit.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  managed            the list of the names of the classes that the
   **                            persistence provider must add to its set of
   **                            managed classes.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   ** @param  properties         the properties object.
   **                            <br>
   **                            Allowed object is {@link Properties}.
   */
  public PersistenceUnit(final String name, final List<String> managed, final Properties properties) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.name       = name;
    this.managed    = managed;
    this.properties = properties;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jtaDataSource
  /**
   ** Sets the JTA-enabled data source to be used by the persistence provider.
   ** <br>
   ** The data source corresponds to the <code>jta-data-source</code> element in
   ** the <code>persistence.xml</code> file or is provided at deployment or by
   ** the container.
   **
   ** @param  value              the JTA-enabled data source to be used by the
   **                            persistence provider.
   **                            <br>
   **                            Allowed object is {@link DataSource}.
   **
   ** @return                    this instance of <code>PersistenceUnit</code>
   **                            to allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceUnit</code>.
   */
  public final PersistenceUnit jtaDataSource(final DataSource value) {
    this.jtaDataSource = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nonJtaDataSource
  /**
   ** Sets the non-JTA-enabled data source to be used by the persistence
   ** provider for accessing data outside a JTA transaction.
   ** <br>
   ** The data source corresponds to the named <code>non-jta-data-source</code>
   ** element in the <code>persistence.xml</code> file or provided at deployment
   ** or by the container.
   **
   ** @param  value              the non-JTA-enabled data source to be used by
   **                            the  persistence provider for accessing data
   **                            outside a JTA  transaction.
   **                            <br>
   **                            Allowed object is {@link DataSource}.
   **
   ** @return                    this instance of <code>PersistenceUnit</code>
   **                            to allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceUnit</code>.
   */
  public final PersistenceUnit nonJtaDataSource(final DataSource value) {
    this.nonJtaDataSource = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   properties
  /**
   ** Add or ovveride a configuration properties of a persistence unit.
   **
   ** @param  name
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  value
   **                            <br>
   **                            Possible object is {@link Object}.
   **
   ** @return                    this instance of <code>PersistenceUnit</code>
   **                            to allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceUnit</code>.
   */
  public final PersistenceUnit property(final String name, final Object value) {
    if (this.properties == null)
      this.properties = new Properties();
    this.properties.put(name, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPersistenceUnitName (PersistenceUnitInfo)
  /**
   ** Returns the name of the persistence unit.
   ** <br>
   ** Corresponds to the
   ** <code>name</code> attribute in the <code>persistence.xml</code> file.
   **
   ** @return                    the name of the persistence unit.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getPersistenceUnitName() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPersistenceProviderClassName (PersistenceUnitInfo)
  /**
   ** Returns the fully qualified name of the persistence provider
   ** implementation class.
   ** <br>
   ** Corresponds to the <code>provider</code> element in the
   ** <code>persistence.xml</code> file.
   **
   ** @return                    the fully qualified name of the persistence
   **                            provider  implementation class.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getPersistenceProviderClassName() {
    return PROVIDER;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTransactionType (PersistenceUnitInfo)
  /**
   ** Returns the transaction type of the entity managers created by the
   ** <code>EntityManagerFactory</code>.
   ** <br>
   ** The transaction type corresponds to the <code>transaction-type</code>
   ** attribute in the <code>persistence.xml</code> file.
   ** <p>
   ** We need to control any transaction locally hence this methoc returns
   ** always {@link PersistenceUnitTransactionType#RESOURCE_LOCAL}.
   **
   ** @return                    the transaction type of the entity managers
   **                            created by the
   **                            <code>EntityManagerFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            {@link PersistenceUnitTransactionType}.
   */
  @Override
  public final PersistenceUnitTransactionType getTransactionType() {
    return PersistenceUnitTransactionType.RESOURCE_LOCAL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getJtaDataSource (PersistenceUnitInfo)
  /**
   ** Returns the JTA-enabled data source to be used by the persistence
   ** provider.
   ** <br>
   ** The data source corresponds to the <code>jta-data-source</code> element in
   ** the <code>persistence.xml</code> file or is provided at deployment or by
   ** the container.
   **
   ** @return                    the JTA-enabled data source to be used by the
   **                            persistence provider.
   **                            <br>
   **                            Possible object is {@link DataSource}.
   */
  @Override
  public final DataSource getJtaDataSource() {
    return this.jtaDataSource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNonJtaDataSource (PersistenceUnitInfo)
  /**
   ** Returns the non-JTA-enabled data source to be used by the persistence
   ** provider for accessing data outside a JTA transaction.
   ** <br>
   ** The data source corresponds to the named <code>non-jta-data-source</code>
   ** element in the <code>persistence.xml</code> file or provided at deployment
   ** or by the container.
   **
   ** @return                    the non-JTA-enabled data source to be used by
   **                            the  persistence provider for accessing data
   **                            outside a JTA  transaction.
   **                            <br>
   **                            Possible object is {@link DataSource}.
   */
  @Override
  public final DataSource getNonJtaDataSource() {
    return this.nonJtaDataSource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getJarFileUrls (PersistenceUnitInfo)
  /**
   ** Returns a list of {@link URL}s for the jar files or exploded jar file
   ** directories that the persistence provider must examine for managed
   ** classes of the persistence unit.
   ** <br>
   ** Each {@link URL} corresponds to a <code>jar-file</code> element in the
   ** <code>persistence.xml</code> file. A {@link URL} will either be a file:
   ** {@link URL} referring to a jar file or referring to a directory that
   ** contains an exploded jar file, or some other {@link URL} from which an
   ** <code>InputStream</code> in jar format can be obtained.
   ** <p>
   ** Everything has to be provided by a test case itself hence there is no
   ** reason to integrate jars. Therefore this method returns an empty
   ** collection.
   **
   ** @return                    a list of URL objects referring to jar files or
   **                            directories.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link URL}.
   */
  @Override
  public final List<URL> getJarFileUrls() {
    return Collections.emptyList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPersistenceUnitRootUrl (PersistenceUnitInfo)
  /**
   ** Returns the {@link URL} for the jar file or directory that is the root of
   ** the persistence unit. (If the persistence unit is rooted in the
   ** WEB-INF/classes directory, this will be the {@link URL} of that
   ** directory.)
   ** <p>
   ** The {@link URL} will either be a file: {@link URL} referring to a jar file
   ** or referring to a directory that contains an exploded jar file, or some
   ** other URL from which an InputStream in jar format can be obtained.
   ** <p>
   ** Everything has to be provided by a test case itself hence there is no
   ** need to point to a specific root {@link URL}.
   **
   ** @return                    a @link URL} referring to a jar file or
   **                            directory.
   **                            <br>
   **                            Possible object is {@link URL}.
   */
  @Override
  public final URL getPersistenceUnitRootUrl() {
    // org.eclipse.persistence.jpa.PersistenceProvider calls
    // org.eclipse.persistence.internal.jpa.deployment.PersistenceUnitProcessor
    // inside of createContainerEntityManagerFactory which raise a NPE if the
    // url returned by this method is null
    // we fake a valid URL by obtaining the current working directory
    return FileSystem.url("file://./bin");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   excludeUnlistedClasses (PersistenceUnitInfo)
  /**
   ** Returns whether classes in the root of the persistence unit that have not
   ** been explicitly listed are to be included in the set of managed classes.
   ** <br>
   ** This value corresponds to the <code>exclude-unlisted-classes</code>
   ** element in the <code>persistence.xml</code> file.
   ** <p>
   ** The usage of this class is responsible for which entities are part of the
   ** test case hence we return always <code>false</code>.
   **
   ** @return                    whether classes in the root of the persistence
   **                            unit that have not been explicitly listed are
   **                            to be included in the set of managed classes.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public final boolean excludeUnlistedClasses() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getManagedClassNames (PersistenceUnitInfo)
  /**
   ** Returns the list of the names of the classes that the persistence provider
   ** must add to its set of managed classes.
   ** <br>
   ** Each name corresponds to a named <code>class</code> element in the
   ** <code>persistence.xml</code> file.
   ** <p>
   ** This accessor method returns a reference to the live {@link List}, not a
   ** snapshot. Therefore any modification you make to the returned list will be
   ** present inside the object instance.
   **
   ** @return                    the list of the names of the classes that the
   **                            persistence provider must add to its set of
   **                            managed classes.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  @Override
  public final List<String> getManagedClassNames() {
    return this.managed;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMappingFileNames (PersistenceUnitInfo)
  /**
   ** Returns the list of the names of the mapping files that the persistence
   ** provider must load to determine the mappings for the entity classes.
   ** <br>
   ** The mapping files must be in the standard XML mapping format, be uniquely
   ** named and be resource-loadable from the application classpath.
   ** <br>
   ** Each mapping file name corresponds to a <code>mapping-file</code> element
   ** in the <code>persistence.xml</code> file.
   ** <p>
   ** This accessor method returns a reference to the live {@link List}, not a
   ** snapshot. Therefore any modification you make to the returned list will be
   ** present inside the object instance.
   **
   ** @return                    the list of mapping file names that the
   **                            persistence provider must load to determine the
   **                            mappings for the entity classes.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  @Override
  public final List<String> getMappingFileNames() {
    return this.mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSharedCacheMode (PersistenceUnitInfo)
  /**
   ** Returns the specification of how the provider must use a second-level
   ** cache for the persistence unit.
   ** <br>
   ** The result of this method corresponds to the
   ** <code>shared-cache-mode</code> element in the <code>persistence.xml</code>
   ** file.
   ** <p>
   ** Caching is not so important hence this method returns always
   ** {@link SharedCacheMode#UNSPECIFIED}.
   **
   ** @return                    the second-level cache mode that must be used
   **                            by the provider for the persistence unit.
   **                            <br>
   **                            Possible object is {@link SharedCacheMode}.
   */
  @Override
  public final SharedCacheMode getSharedCacheMode() {
    return SharedCacheMode.UNSPECIFIED;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValidationMode (PersistenceUnitInfo)
  /**
   ** Returns the validation mode to be used by the persistence provider for the
   ** persistence unit.
   ** <br>
   ** The validation mode corresponds to the <code>validation-mode</code>
   ** element in the <code>persistence.xml</code> file.
   ** <p>
   ** Per deafult we let decide the persistence provider how to validate hence
   ** this method returns always {@link ValidationMode#AUTO}.
   **
   ** @return                    the validation mode to be used by the
   **                            persistence provider for the persistence unit.
   **                            <br>
   **                            Possible object is {@link ValidationMode}.
   */
  @Override
  public final ValidationMode getValidationMode() {
    return ValidationMode.AUTO;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProperties (PersistenceUnitInfo)
  /**
   ** Returns a properties object.
   ** <br>
   ** Each property corresponds to a  <code>property</code> element in the
   ** <code>persistence.xml</code> file or to a property set by the container.
   ** <p>
   ** This accessor method returns a reference to the live {@link Properties},
   ** not a snapshot. Therefore any modification you make to the returned
   ** collection will be present inside the object instance.
   **
   ** @return                    the properties object.
   **                            <br>
   **                            Possible object is {@link Properties}.
   */
  @Override
  public final Properties getProperties() {
    return this.properties;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPersistenceXMLSchemaVersion (PersistenceUnitInfo)
  /**
   ** Returns the schema version of the <code>persistence.xml</code> file.
   **
   ** @return                    the persistence.xml schema version
   **                            <br>
   **                            Possible object is {@link ValidationMode}.
   */
  @Override
  public final String getPersistenceXMLSchemaVersion() {
    return VERSION;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getClassLoader (PersistenceUnitInfo)
  /**
   ** Returns {@link ClassLoader} that the provider may use to load any classes,
   ** resources, or open {@link URL}s.
   **
   ** @return                    the {@link ClassLoader} that the provider may
   **                            use to load any  classes, resources, or open
   **                            {@link URL}s.
   **                            <br>
   **                            Possible object is {@link ClassLoader}.
   */
  @Override
  public final ClassLoader getClassLoader() {
    return Thread.currentThread().getContextClassLoader();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addTransformer (PersistenceUnitInfo)
  /**
   ** Add a transformer supplied by the provider that will be  called for every
   ** new class definition or class redefinition that gets loaded by the loader
   ** returned by the {@link PersistenceUnitInfo#getClassLoader} method.
   ** <br>
   ** The transformer has no effect on the result returned by the
   ** {@link PersistenceUnitInfo#getNewTempClassLoader} method.
   ** <br>
   ** Classes are only transformed once within the same classloading scope,
   ** regardless of how many persistence units they may be  a part of.
   **
   ** @param  transformer        the provider-supplied transformer that the
   **                            container invokes at class-(re)definition time.
   **                            <br>
   **                            Allowed object is {@link ClassTransformer}.
   */
  @Override
  public void addTransformer(final ClassTransformer transformer) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNewTempClassLoader (PersistenceUnitInfo)
  /**
   ** Returns a new instance of a {@link ClassLoader} that the provider may use
   ** to temporarily load any classes, resources, or open {@link URL}s. The
   ** scope and classpath of this loader is exactly the same as that of the
   ** loader returned by {@link PersistenceUnitInfo#getClassLoader}. None of the
   ** classes loaded by this class loader will be visible to application
   ** components.
   ** <br>
   ** The provider may only use this {@link ClassLoader} within the scope of the
   ** {@link PersistenceProvider#createContainerEntityManagerFactory} call.
   **
   ** @return                    temporary {@link ClassLoader} with same
   **                            visibility as current loader.
   **                            <br>
   **                            Possible object is {@link ClassLoader}.
   */
  @Override
  public final ClassLoader getNewTempClassLoader() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Factory method to create a new <code>PersistenceUnit</code> without
   ** further configuration properties.
   **
   ** @param  name               the name of the persistence unit.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  entity             the entity classes related to a test case.
   **                            <br>
   **                            Possible object is array of {@link Class} of
   **                            any type.                           
   **
   ** @return                    the <code>PersistenceUnit</code> suitable for a
   **                            test case represented by given persistence unit
   **                            name.
   **                            <br>
   **                            Possible object is <code>PersistenceUnit</code>.
   */
  public static PersistenceUnit instance(final String name, final Class<?>... entity) {
    return instance(name, null, entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Factory method to create a new <code>PersistenceUnit</code> without
   ** further configuration properties.
   **
   ** @param  name               the name of the persistence unit.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  entity             the entity classes related to a test case.
   **                            <br>
   **                            Possible object is array of {@link Class} of
   **                            any type.                           
   ** @param  properties         the properties object.
   **                            <br>
   **                            Allowed object is {@link Properties}.
   **
   ** @return                    the <code>PersistenceUnit</code> suitable for a
   **                            test case represented by given persistence unit
   **                            name.
   **                            <br>
   **                            Possible object is <code>PersistenceUnit</code>.
   */
  public static PersistenceUnit instance(final String name, final Properties properties, final Class<?>... entity) {
    return new PersistenceUnit(name, Arrays.asList(entity).stream().map(Class::getName).collect(Collectors.toList()), properties);
  }
}