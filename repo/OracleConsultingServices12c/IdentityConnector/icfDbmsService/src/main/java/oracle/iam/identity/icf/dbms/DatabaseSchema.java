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

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Database Connector

    File        :   DatabaseSchema.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseSchema.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.dbms;

import java.math.BigDecimal;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Stack;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.ArrayList;

import java.io.Reader;
import java.io.InputStream;
import java.io.IOException;

import java.net.URL;

import java.sql.Types;

import java.util.LinkedHashSet;

import org.identityconnectors.framework.spi.Connector;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.SchemaBuilder;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.AttributeInfoBuilder;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.foundation.parser.JsonArray;
import oracle.iam.identity.icf.foundation.parser.JsonValue;
import oracle.iam.identity.icf.foundation.parser.JsonObject;
import oracle.iam.identity.icf.foundation.parser.JsonParser;
import oracle.iam.identity.icf.foundation.parser.JsonContext;

import oracle.iam.identity.icf.foundation.utility.SchemaUtility;
import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.foundation.logging.AbstractLoggable;

public class DatabaseSchema extends AbstractLoggable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String TYPE     = "type";
  private static final String NAME     = "name";
  private static final String FLAG     = "flag";
  private static final String CLASS    = "class";
  private static final String ALIAS    = "alias";
  private static final String ENTITY   = "entity";
  private static final String SOURCE   = "source";
  private static final String PREFIX   = "prefix";
  private static final String LAYERED  = "layered";
  private static final String EMBEDDED = "embedded";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Same of the data types must be converted. */
  private final DatabaseEndpoint                        endpoint;

  /** The chached schema. */
  protected Schema                                 schema     = null;

  /**
   ** The mapping between the ICF ObjectClass and the fullqualified name of the
   ** database object to access.
   */
  protected final Map<String, Entity>              structural = CollectionUtility.caseInsensitiveMap();
  /**
   ** The mapping between the fullqualified entity name and the entities to
   ** embbed.
   */
  protected final Map<String, Map<String, String>> embedded   = CollectionUtility.caseInsensitiveMap();
  /**
   ** The mapping between the fullqualified entity name and the entities which
   ** are layered.
   */
  protected final Map<String, List<Layer>>         layered    = CollectionUtility.caseInsensitiveMap();
  /**
   ** The mapping between the fullqualified entity name and the entity
   ** descriptor.
   */
  protected final Map<String, DatabaseEntity>      dictionary = CollectionUtility.caseInsensitiveMap();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Layer
  // ~~~~~ ~~~~~
  /**
   ** The descriptor to handle layered schema entities.
   */
  public static class Layer {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The schema entity the layered attribute belongs to. */
    public final String      entity;

    /** The alias name of the layer prefix. */
    public final String      prefix;

    /** The named values of the layered attribute. */
    public final Set<String> name;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new <code>layer</code>.
     **
     ** @param  entity           the schema entity the layered attribute belongs
     **                          to.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  prefix           the alias name of the layer prefix.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  name             the collection of name valeus
     **                          <br>
     **                          Allowed object is {@link Set} wehere each
     **                          element is of type {@link String}.
     */
    Layer(final String entity, final String prefix, final Set<String> name) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.entity = entity;
      this.prefix = prefix;
      this.name   = name;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Entity
  // ~~~~~ ~~~~~~
  /**
   ** The descriptor to handle structural schema entities.
   */
  public static class Entity {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    public final String id;
    public final String primary;
    public final String secondary;
    public final String status;
    public final String password;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new <code>Entity</code>.
     **
     ** @param  id               the identifying name of the entity at all.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  primary          the attribute name of the primary identifier
     **                          in a record.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  secondary        the attribute name of the secondary identifier
     **                          in a record.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  status           the attribute name of the status information
     **                          in a record if any.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  password         the attribute name of the password in a record
     **                          if any.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Entity(final String id, final String primary, final String secondary, final String status, final String password) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.id        = id;
      this.primary   = primary;
      this.secondary = secondary;
      this.status    = status;
      this.password  = password;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Parser
  // ~~~~~ ~~~~~~
  /**
   ** A parser for attribute filter expressions.
   */
  public static class Parser {

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class StringReader
    // ~~~~ ~~~~~~~~~~~~~
    /**
     ** A simple implemantation of the {@link Reader} interface to walk through
     ** {@link String}s.
     */
    private static final class StringReader extends Reader {

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      private int          pos;
      private int          mark;
      private final String string;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      ////////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructor for <code>StringReader</code> with the string to read from.
       **
       ** @param  string         the string to read from.
       **                        <br>
       **                        Allowed object is {@link String}.
       */
      private StringReader(final String string) {
        // ensure inheritance
        super();

        // initialize instance attributes
        this.string = string;
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods of abstrat base classes
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: read (Reader)
      /**
       ** Reads characters into a portion of an array.
       ** <br>
       ** This method will block until some input is available, an I/O error
       ** occurs, or the end of the stream is reached.
       **
       ** @param  buffer         the destination buffer.
       **                        <br>
       **                        Allowed object is array of <code>char</code>.
       ** @param  offset         the offset at which to start storing
       **                        characters.
       **                        <br>
       **                        Allowed object is <code>int</code>.
       ** @param  length         the maximum number of characters to read.
       **                        <br>
       **                        Allowed object is <code>int</code>.
       **
       ** @return                the number of characters read, or -1 if the end
       **                        of the stream has been reached.
       **                        <br>
       **                        Possible object is <code>int</code>.
       **
       ** @throws IOException    if an I/O error occurs.
       */
      @Override
      public int read(final char[] buffer, final int off, final int len) {
        if (this.pos >= this.string.length()) {
          return -1;
        }
        int chars = Math.min(this.string.length() - this.pos, len);
        System.arraycopy(this.string.toCharArray(), this.pos, buffer, off, chars);
        this.pos += chars;
        return chars;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: close (Reader)
      /**
       ** Closes the stream and releases any system resources associated with
       ** it. Once the stream has been closed, further read(), ready(), mark(),
       ** reset(), or skip() invocations will throw an IOException.
       ** <br>
       ** Closing a previously closed stream has no effect.
       */
      @Override
      public void close() {
        // do nothing.
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods group by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: markSupported (overridden)
      /**
       ** Tells whether this stream supports the mark() operation.
       **
       ** @return                always <code>true</code>.
       */
      @Override
      public boolean markSupported() {
        return true;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: mark (overridden)
      /**
       ** Marks the present position in the stream.
       ** <br>
       ** Subsequent calls to reset() will attempt to reposition the stream to
       ** this point.
       **
       ** @param limit           the limit on the number of characters that may
       **                        be read while still preserving the mark. After
       **                        reading this many characters, attempting to
       **                        reset the stream may fail.
       **                        <br>
       **                        Allowed object is <code>int</code>.
       */
      @Override
      public void mark(final int limit) {
        this.mark = this.pos;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: ready (overridden)
      /**
       ** Tells whether this stream is ready to be read.
       **
       ** @return                always <code>true</code>.
       */
      @Override
      public boolean ready() {
        return true;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: reset (overridden)
      /**
       ** Resets the stream.
       ** <br>
       ** If the stream has been marked, then attempt to reposition it at the
       ** mark. If the stream has not been marked, then attempt to reset it in
       ** some way appropriate to the particular stream, for example by
       ** repositioning it to its starting point. Not all character-input
       ** streams support the reset() operation, and some support reset()
       ** without supporting mark().
       */
      @Override
      public void reset() {
        this.pos = this.mark;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: read (overridden)
      /**
       ** Reads a single character.
       ** <br>
       ** This method will block until a character is available, an I/O error
       ** occurs, or the end of the stream is reached.
       **
       ** @return                the character read, as an integer in the range
       **                        0 to 65535 (<code>0x00-0xffff</code>), or -1 if
       **                        the end of the stream has been reached.
       **                        <br>
       **                        Possible object is <code>int</code>.
       */
      @Override
      public int read() {
        if (this.pos >= this.string.length()) {
          return -1;
        }
        return this.string.charAt(this.pos++);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: read (overridden)
      /**
       ** Skips characters.
       ** <bR>
       ** This method will block until some characters are available, an I/O
       ** error occurs, or the end of the stream is reached.
       **
       ** @param  n              the number of characters to skip.
       **                        <br>
       **                        Allowed object is <code>long</code>.
       **
       ** @return                the number of characters actually skipped.
       **                        <br>
       **                        Possible object is <code>long</code>.
       */
      @Override
      public long skip(final long n) {
        final long chars = Math.min(this.string.length() - this.pos, n);
        this.pos += chars;
        return chars;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: unread
      /**
       ** Move the current read position back one character.
       */
      public void unread() {
        this.pos--;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   path
    /**
     ** Parse a path string.
     **
     ** @param  expression       the path expression to parse.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the path from the expression.
     **                          <br>
     **                          Possible object is {@link Pair}.
     **
     ** @throws SystemException  if the path string could not be parsed.
     */
    public static Pair<String, Pair<String, String>> path(final String expression)
      throws SystemException {

      final StringReader reader  = new StringReader(expression.trim());
      final String       token   = parseToken(reader);
      if (token == null || token.isEmpty()) {
        throw DatabaseException.attributePathExpected(reader.mark);
      }
      else {
        String name = parseToken(reader);
        if (name == null || name.isEmpty() || !name.endsWith("[")) {
          throw DatabaseException.attributeNameExpected(reader.mark);
        }
        // there is a value path.
        name = name.substring(0, name.length() - 1);
        return Pair.of(token, Pair.of(name, parseToken(reader)));
      }
    }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseToken
  /**
   ** Read a path token. A token is either:
   ** <ul>
   **   <li>An attribute name terminated by a period.
   **   <li>An attribute name terminated by an opening bracket.
   ** </ul>
   **
   ** @param  reader             the reader to read from.
   **                            <br>
   **                            Allowed object is {@link StringReader}.
   **
   ** @return                    the token at the current position, or
   **                            <code>null</code> if the end of the input has
   **                            been reached.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws DatabaseException  if the path string could not be parsed.
   */
  private static String parseToken(final StringReader reader)
    throws DatabaseException {
    reader.mark(0);
    int c = reader.read();
    final StringBuilder b = new StringBuilder();
    while (c > 0) {
      if (c == '.') {
        if (reader.pos >= reader.string.length()) {
          // there is nothing after the period.
          throw DatabaseException.unexpectedEOS();
        }
        // terminating period
        // consume it and return token
        return b.toString();
      }
      else if (c == '[') {
        // terminating opening bracket
        // consume it and return token.
        b.append((char)c);
        return b.toString();
      }
      else if (c == ']') {
        // terminating closing bracket
        // consume it and return token.
//        b.append((char)c);
        return b.toString();
      }
      else if (c == '-' || c == '_' || c == '$' || Character.isLetterOrDigit(c)) {
        b.append((char)c);
      }
      else {
        throw DatabaseException.unexpectedCharacter((char)c, reader.pos - 1, reader.mark);
      }
      c = reader.read();
    }
    return (b.length() > 0) ?  b.toString() : null;
  }
}

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Use the {@link DatabaseEndpoint} passed into to connect immediately to a
   ** Database Service.
   **
   ** @param  endpoint           the Database Service endpoint connection which
   **                            is used to discover this
   **                            <code>DatabaseSchema</code>.
   **                            <br>
   **                            Allowed object is {@link DatabaseEndpoint}.
   */
  private DatabaseSchema(final DatabaseEndpoint endpoint) {
    // ensure inheritance
    super(endpoint);

    // initialize instance attributes
    this.endpoint = endpoint;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   structural
  /**
   ** Returns an {@link Entity} of the structural object classes for the given
   ** string.
   **
   ** @param  objectClass        the name of the {@link Entity} to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an unmodifiable {@link Map} of the structural
   **                            object classes mapping for the connected
   **                            Database Service.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Entity} for the value.
   */
  public final Entity structural(final String objectClass) {
    return this.structural.get(objectClass);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   layered
  /**
   ** Returns an {@link Entity} of the layered object for the given string.
   ** <br>
   ** <b>Note</b>:
   ** Layered objects are part of an entity definition it self and not exposed
   ** as an object class.
   **
   ** @param  prefix             the prefix name of the layered {@link Entity}
   **                            to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an unmodifiable collection of the layered
   **                            object class mapping for the connected
   **                            Database Service.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Layer}.
   */
  public final List<Layer> layered(final String prefix) {
    return this.layered.get(prefix);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   embedded
  /**
   ** Returns an {@link Entity} of the embedded object class for the given
   ** string.
   ** <br>
   ** <b>Note</b>:
   ** Only structural object classes can be embedded in an object class.
   **
   ** @param  objectClass        the name of the {@link Entity} to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an unmodifiable {@link Set} of the embedded
   **                            object class mapping for the connected
   **                            Database Service.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link String} for the value.
   */
  public final Map<String, String> embedded(final String objectClass) {
    return this.embedded.get(objectClass);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dictionary
  /**
   ** Returns a {@link DatabaseEntity} for the given string.
   ** <p>
   ** The name to lookup the {@link DatabaseEntity} from the dictionary has to
   ** be a fullqualified object name.
   ** <p>
   ** Whenever object names appear fully-qualified names are used; thus, an
   ** object name may consist of up to three nodes separated by periods.
   ** <br>
   ** The JDBC specification allows for up to three levels of qualification for
   ** an object name
   ** <ol>
   **   <li>Catalog
   **   <li>Schema
   **   <li>Object
   ** </ol>
   ** For databases that support all three levels of qualification, object names
   ** take the form:
   ** <pre>
   **   &lt;catalog&gt;.&lt;schema&gt;.&lt;object&gt;
   ** </pre>
   ** Microsoft SQL Server uses all three levels of qualification. For example,
   ** PatientRecords.dbo.Patient.
   ** <br>
   ** Others, such as Oracle, do not use Catalog Name, using only schema and
   ** object. For example, dbo.Patient.
   **
   ** @param  objectName         the object name of the {@link DatabaseEntity}
   **                            to lookup.
   **                            <br>
   **                            The name is automaticalliy extended with the
   **                            required levels of qualification by checking
   **                            for <code>null</code> values in the endpoint
   **                            metadata. 
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a {@link DatabaseEntity} for the given
   **                            fullqualified objec name.
   **                            <br>
   **                            Possible object is {@link DatabaseEntity}.
   */
  public DatabaseEntity dictionary(final String objectName) {
    // check if the object name is a qualified object name
    final int pos = objectName.indexOf('.');
    return (pos == -1) ? this.dictionary.get(qualified(this.endpoint.databaseCatalog(), qualified(this.endpoint.databaseSchema(), objectName))) : this.dictionary.get(objectName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   load
  /**
   ** Return the schema upon request.
   ** <p>
   ** The schema is cached over the lifetime of this connector.
   **
   ** @param  endpoint           the Database Service endpoint connection which
   **                            is used to discover the {@link DatabaseSchema}.
   **                            <br>
   **                            Allowed object is {@link DatabaseEndpoint}.
   ** @param  url                the {@link URL} of the descriptor file to fetch
   **                            from the class path.
   **                            <br>
   **                            Allowed object is {@link URL}.
   **
   ** @return                    the prepared schema.
   **                            <br>
   **                            Possible object is {@link DatabaseSchema}.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  public static DatabaseSchema load(final DatabaseEndpoint endpoint, final URL url)
    throws SystemException {

    if (url == null)
      throw new DatabaseException(DatabaseError.SCHEMA_DESCRIPTOR_EMPTY);

    try {
      return load(endpoint, url.openStream());
    }
    catch (IOException e) {
      throw new DatabaseException(DatabaseError.SCHEMA_DESCRIPTOR_EMPTY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   load
  /**
   ** Return the schema upon request.
   ** <p>
   ** The schema is cached over the lifetime of this connector.
   **
   ** @param  endpoint           the Database Service endpoint connection which
   **                            is used to discover the {@link DatabaseSchema}.
   **                            <br>
   **                            Allowed object is {@link DatabaseEndpoint}.
   ** @param  stream             the {@link InputStream} of the descriptor file
   **                            to fetch from the class path.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    the prepared schema.
   **                            <br>
   **                            Possible object is {@link DatabaseSchema}.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  public static DatabaseSchema load(final DatabaseEndpoint endpoint, final InputStream stream)
    throws SystemException {

    final DatabaseSchema schema = new DatabaseSchema(endpoint);
    return schema.load(endpoint.databaseCatalog(), StringUtility.empty(endpoint.databaseSchema()) ? endpoint.principalUsername() : endpoint.databaseSchema(), stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Convert database type to connector supported set of attribute types.
   **
   ** @param  type               the databse {@link Types} to convert.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a ICF framework supported class.
   **                            <br>
   **                            Possibe object is {@link Class} for any type.
   */
  public static Class<?> type(final int type) {
    switch (type) {
      case Types.BIT           :
      case Types.BOOLEAN       : return Boolean.class;
      case Types.FLOAT         :
      case Types.REAL          : return Float.class;
      case Types.INTEGER       : return Integer.class;
      case Types.BIGINT        : return Long.class;
      case Types.TINYINT       : return Byte.class;
      case Types.DECIMAL       :
      case Types.NUMERIC       : return BigDecimal.class;
      case Types.DOUBLE        : return Double.class;
      case Types.BLOB          :
      case Types.BINARY        :
      case Types.VARBINARY     :
      case Types.LONGVARBINARY : return byte[].class;
      default                  : return  String.class;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   qualified
  /**
   ** Returns a qualified string that can be used to join this entity with
   ** another criteria like a WHERE-clause of a database statement or a
   ** fullqualified name of an database object using the specified
   ** column name.
   ** <br>
   ** Some dialects, including MariaDB, MemSQL, MySQL needs to set the catalog
   ** if the database is not specified, the connection is made with no default
   ** database. In this case, either call the setCatalog() method on the
   ** Connection instance, or fully specify table names using the database name
   ** in SQL.
   **
   ** @param  catalog            the catalog prefix of the fullqualified name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  prefix             the qualifier prefix of the fullqualified name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  name               the name to be qualified.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   * @return                     the full qualified name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String qualified(final String catalog, final String prefix, final String name) {
    return StringUtility.empty(catalog) ? qualified(prefix, name) : qualified(qualified(catalog, prefix), name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   qualified
  /**
   ** Returns a qualified string that can be used to join this entity with
   ** another criteria like a WHERE-clause of a database statement or a
   ** fullqualified name of an database object using the specified
   ** column name.
   **
   ** @param  prefix             the qualifier prefix of the fullqualified name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  name               the name to be qualified.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   * @return                     the full qualified name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String qualified(final String prefix, final String name) {
    return StringUtility.empty(prefix) ? name : prefix.concat(".").concat(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Return the schema upon request.
   ** <p>
   ** The schema is cached over the lifetime of this connector.
   **
   ** @param  clazz              the connector class type for which the schema
   **                            are built.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            {@link Connector}.
   **
   ** @return                    the schema.
   **                            <br>
   **                            Possible object is {@link DatabaseSchema}.
   */
  public Schema build(final Class<? extends Connector> clazz) {
    final Stack<String> visited = new Stack<String>();
    final SchemaBuilder builder = new SchemaBuilder(clazz);
    for (Map.Entry<String, Entity> cursor : this.structural.entrySet()) {
      builder.defineObjectClass(attributes(builder, visited, cursor.getKey(), cursor.getValue()));
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   load
  /**
   ** Configure the schema by fetching the descriptor from a file resource at
   ** the class path.
   ** <p>
   ** The usually location of the descriptor location is in
   ** <code>/META-INF/dbs/&lt;schema&gt;.json</code>.
   ** <p>
   ** The database entities are build the given <code>schema</code> name that is
   ** usually configured in the {@link DatabaseEndpoint}.
   ** <br>
   ** Some dialects, including MariaDB, MemSQL, MySQL needs to set the catalog
   ** if the database is not specified, the connection is made with no default
   ** database. In this case, either call the setCatalog() method on the
   ** Connection instance, or fully specify table names using the database name
   ** in SQL.
   **
   ** @param  catalog            the optional catalog prefix of the
   **                            fullqualified name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  schema             the optional schema name of the database
   **                            entities to apply.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  stream             the {@link InputStream} of the descriptor file
   **                            to fetch from the class path.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    the prepared schema.
   **                            <br>
   **                            Possible object is {@link DatabaseSchema}.
   **
   ** @throws SystemException    if the jave type class defined for an attribute
   **                            is not found at the class path.
   */
  protected DatabaseSchema load(final String catalog, final String schema, final InputStream stream)
    throws SystemException {

    // prevent bogus state
    if (stream == null)
      throw new DatabaseException(DatabaseError.SCHEMA_DESCRIPTOR_EMPTY);

    // create a JSON stream parser to interprete the schema definition
    final JsonParser parser = JsonContext.deserializer(stream);
    if (!parser.hasNext())
      throw new DatabaseException(DatabaseError.SCHEMA_DESCRIPTOR_INVALID);

    // walk through the file
    try {
      // there is only one object expected in the stream
      final JsonObject descriptor = parser.next().asObject();
      // obtain the object class mapping from the configuration
      final JsonArray  structural = descriptor.get("structural").asArray();
      for (JsonValue cursor : structural) {
        final JsonObject objectClass = cursor.asObject();
        // build the fullqualified name of the database object to map
        JsonObject   entity = objectClass.get(ENTITY).asObject();
        final String object = SchemaUtility.createSpecialName(objectClass.getString(TYPE));
        // check if required attributes are defined at the schema definition
        final String primary = entity.getString(Uid.NAME);
        if (StringUtility.empty(primary))
          throw new DatabaseException(DatabaseError.SCHEMA_DESCRIPTOR_PRIMARY, entity.getString(NAME));
        final String secondary = entity.getString(Name.NAME);
        if (StringUtility.empty(secondary))
          throw new DatabaseException(DatabaseError.SCHEMA_DESCRIPTOR_SECONDARY, entity.getString(NAME));
        // register the entity as an structural object
        this.structural.put(object, new Entity(qualified(catalog, schema, entity.getString(NAME)), primary, secondary, entity.getString(OperationalAttributes.ENABLE_NAME), entity.getString(OperationalAttributes.PASSWORD_NAME)));

        JsonValue layered = objectClass.get(LAYERED);
        if (layered != null) {
          final JsonArray   array     = layered.asArray();
          final List<Layer> collector = new ArrayList<Layer>(array.size());
          for (int i = 0; i < array.size(); i++) {
            final JsonObject item = array.get(i).asObject();
            final JsonArray  name = item.get("name").asArray();
            if (!name.isEmpty()) {
              final Set<String> value = new LinkedHashSet<String>(name.size());
              for (int j = 0; j < name.size(); j++)
                value.add(name.get(j).toString());

              final JsonObject source = item.get(SOURCE).asObject();
              entity = source.get(ENTITY).asObject();
              collector.add(new Layer(qualified(catalog, schema, entity.getString(NAME)), item.getString(PREFIX), value));
            }
          }
          this.layered.put(object, collector);
        }

        layered = objectClass.get(EMBEDDED);
        final Map<String, String> pairs = new HashMap<>();
        if (layered != null) {
          final JsonArray embed = layered.asArray();
          this.embedded.put(object, pairs);
          for (int i = 0; i < embed.size(); i++) {
            final JsonObject item   = embed.get(i).asObject();
            final JsonObject source = item.get(SOURCE).asObject();
            entity = source.get(ENTITY).asObject();
            pairs.put(SchemaUtility.createSpecialName(item.getString(PREFIX)), qualified(catalog, schema, entity.getString(NAME)));
          }
        }
      }
      // obtain the database object from the configuration
      JsonArray mapping = descriptor.get(ENTITY).asArray();
      for (JsonValue cursor : mapping) {
        final DatabaseEntity subject = entity(catalog, schema, cursor.asObject());
        this.dictionary.put(subject.id(), subject);
      }
    }
    catch (RuntimeException e) {
      throw new DatabaseException(DatabaseError.SCHEMA_DESCRIPTOR_PARSE, e.getLocalizedMessage());
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entity
  /**
   ** Factory method to create a {@link DatabaseEntity}s.
   ** <p>
   ** The method exspect following properties:
   ** <br>
   ** <table border="0" cellspacing="10" cellpadding="5" summary="">
   ** <tr><th align="left">Property</th><th align="left">Type</th><th align="left">Description</th><th align="left">required</th></tr>
   ** <tr><td>name</td><td>String</td><td>The name of the database column.</td><td>yes</td></tr>
   ** <tr><td>primary</td><td>array</td><td>The array of attributes making up the primary key of the database object.</td><td>no</td></tr>
   ** <tr><td>attribute</td><td>array</td><td>The array of attributes completing the database object.</td><td>yes</td></tr>
   ** </table>
   ** <p>
   ** The database entities are build the given <code>schema</code> name that is
   ** usually configured in the {@link DatabaseEndpoint}.
   ** <br>
   ** Some dialects, including MariaDB, MemSQL, MySQL needs to set the catalog
   ** if the database is not specified, the connection is made with no default
   ** database. In this case, either call the setCatalog() method on the
   ** Connection instance, or fully specify table names using the database name
   ** in SQL.
   **
   ** @param  catalog            the optional catalog prefix of the
   **                            fullqualified name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  schema             the optional schema name of the database
   **                            entities to apply.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @param  config             the {@link JsonObject} provided by the
   **                            configuration.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the {@link DatabaseEntity} descriptor declared
   **                            in the configuration.
   **                            <br>
   **                            Possible object is {@link DatabaseEntity}.
   **
   ** @throws SystemException    if the jave type class defined for an attribute
   **                            is not found at the class path.
   */
  protected DatabaseEntity entity(final String catalog, final String schema, final JsonObject config)
    throws SystemException {

    final String                  qualified = qualified(catalog, schema);
    final List<DatabaseAttribute> collector = attribute(config.get("primary").asArray());
    int[] primary = new int[collector.size()];
    for (int i = 0; i < collector.size(); i++)
      primary[i] = i;
    collector.addAll(attribute(config.get("attribute").asArray()));
    return DatabaseEntity.build(qualified, config.getString(NAME), primary, collector);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Factory method to create the array of {@link DatabaseAttribute}s.
   ** <p>
   ** The method exspect following properties:
   ** <br>
   ** <table border="0" cellspacing="10" cellpadding="5" summary="">
   ** <tr><th align="left">Property</th><th align="left">Type</th><th align="left">Description</th><th align="left">required</th></tr>
   ** <tr><td>name</td><td>String</td><td>The name of the database column.</td><td>yes</td></tr>
   ** <tr><td>type</td><td>String</td><td>The SQL type of the database column.</td><td>no</td></tr>
   ** <tr><td>clazz</td><td>String</td><td>The Java type class of the database column as String.</td><td>yes</td></tr>
   ** <tr><td>nilable</td><td>Boolean</td><td>Determines if the database column is mandatory.</td><td>yes</td></tr>
   ** <tr><td>length</td><td>Integer</td><td>The length of the database column as String.</td><td>yes</td></tr>
   ** <tr><td>sclae</td><td>Integer</td><td>The length of the database column as String.</td><td>no</td></tr>
   ** </table>
   **
   ** @param  config             the {@link JsonArray} provided by the
   **                            configuration.
   **                            <br>
   **                            Allowed object is {@link JsonArray}.
   **
   ** @return                    the collection of {@link DatabaseAttribute}s
   **                            declared in the configuration.
   **                            <br>
   **                            Possible object is array of
   **                            {@link DatabaseAttribute}.
   **
   ** @throws SystemException    if the jave type class defined for an attribute
   **                            is not found at the class path.
   */
  protected List<DatabaseAttribute> attribute(final JsonArray config)
    throws SystemException {

    final List<DatabaseAttribute> vector = new ArrayList<>(config.size());
    for (int i = 0; i < config.size(); i++) {
      final JsonObject               attribute  = config.get(i).asObject();
      final Set<AttributeInfo.Flags> flag       = EnumSet.noneOf(AttributeInfo.Flags.class);
      final JsonValue                temp       = attribute.get(FLAG);
      if (temp != null) {
        final JsonArray json = temp.asArray();
        for (int j = 0; j < json.size(); j++) {
          final String option = json.get(j).toString();
          flag.add(AttributeInfo.Flags.valueOf(option.toUpperCase()));
        }
      }
      try {
        vector.add(DatabaseAttribute.build(attribute.getString(NAME), attribute.getString(ALIAS), DatabaseAttribute.type(Class.forName(attribute.getString(CLASS)), flag)));
      }
      catch (ClassNotFoundException e) {
        throw SystemException.classNotFound(attribute.getString(CLASS));
      }
    }
    return vector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classBuilder
  /**
   ** Returns the schema builder for a class.
   **
   ** @param  className          the object class name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the object class builder.
   **                            <br>
   **                            Possible object is
   **                            {@link ObjectClassInfoBuilder}.
   */
  private static ObjectClassInfoBuilder classBuilder(final String className) {
    final ObjectClassInfoBuilder builder = new ObjectClassInfoBuilder();
    builder.setType(className);
    return builder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes
  /**
   ** Returns ICF schema attributes for a class.
   **
   ** @param  schema             the {@link SchemaBuilder} populating the entire
   **                            schema to build for a class.
   **                            <br>
   **                            Allowed object is {@link SchemaBuilder}.
   ** @param  visited            a {@link Stack} containing the classes visited
   **                            prior to this class.
   **                            <br>
   **                            Allowed object is {@link Stack} where each
   **                            element is of type {@link String}.
   **                            This is used for cycle detection.
   ** @param  className          the object class name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  structural         the {@link Entity} to get the schema attributes
   **                            for.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @return                    the ICF {@link ObjectClassInfo} for the given
   **                            class.
   **                            <br>
   **                            Possible object is {@link ObjectClassInfo}.
   */
  private ObjectClassInfo attributes(final SchemaBuilder schema, final Stack<String> visited, final String className, final Entity structural) {
    if (visited.contains(structural.id))
      throw new RuntimeException("Cycles detected in Schema " + structural.id);

    // add the entity id to the collection to allow cycle detection
    visited.push(structural.id);
    final ObjectClassInfoBuilder builder = classBuilder(className);
    DatabaseEntity entity = this.dictionary.get(structural.id);
    for (DatabaseAttribute cursor : entity.attribute) {
      if (cursor.alias.equals(structural.primary)) {
        builder.addAttributeInfo(cursor.type.build(Uid.NAME));
      }
      else if (cursor.alias.equals(structural.secondary)) {
        builder.addAttributeInfo(cursor.type.build(Name.NAME));
      }
      else if (cursor.alias.equals(structural.status)) {
        builder.addAttributeInfo(cursor.type.build(OperationalAttributes.ENABLE_NAME));
      }
      else if (cursor.alias.equals(structural.password)) {
        builder.addAttributeInfo(cursor.type.build(OperationalAttributes.PASSWORD_NAME));
      }
      else {
        builder.addAttributeInfo(cursor.type.build(cursor.alias));
      }
    }
    if (this.layered.containsKey(className)) {
      final List<Layer> layer = this.layered.get(className);
      for (Layer cursor : layer) {
        for (String name : cursor.name) {
          builder.addAttributeInfo(AttributeInfoBuilder.build(qualified(cursor.prefix, name)));
        }
      }
    }
    // get the current class from the cycle detection collector
    visited.pop();
    return builder.build();
  }
}