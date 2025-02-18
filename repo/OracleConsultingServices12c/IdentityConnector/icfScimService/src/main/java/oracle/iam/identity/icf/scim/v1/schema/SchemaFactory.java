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
    Subsystem   :   Generic SCIM Library

    File        :   SchemaFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SchemaFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.v1.schema;

import java.util.Collection;

import oracle.iam.identity.icf.scim.annotation.Schema;
import oracle.iam.identity.icf.scim.annotation.Definition;

import oracle.iam.identity.icf.scim.schema.Factory;
import oracle.iam.identity.icf.scim.schema.Metadata;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class SchemaFactory
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** <code>SchemaFactory</code> implements the basic functionality for common
 ** SCIM 1.0 schema operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class SchemaFactory extends Factory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the attribute definition for the SCIM 1 standard schemas attribute */
  public static final Definition             SCHEMAS;

  /** the attribute definition for the SCIM 1 standard id attribute */
  public static final Definition             ID;

  /** the attribute definition for the SCIM 1 standard meta attribute */
  public static final Definition             METADATA;

  /**
   ** The collection of attribute definitions for SCIM 2 standard common
   ** attributes: schemas, id, externalId, and meta.
   */
  public static final Collection<Definition> COMMON;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    Definition.Factory factory = new Definition.Factory()
      .name("schemas")
      .type(Definition.Type.STRING)
      .required(true)
      .caseExact(true)
      .multiValued(true)
      .returned(Definition.Returned.ALWAYS)
      .mutability(Definition.Mutability.READ_WRITE);
    SCHEMAS = factory.build();

    factory = new Definition.Factory()
      .name("id")
      .type(Definition.Type.STRING)
      .caseExact(true)
      .returned(Definition.Returned.ALWAYS)
      .mutability(Definition.Mutability.READ_ONLY);
    ID = factory.build();

    factory = new Definition.Factory()
      .name(Metadata.PREFIX)
      .type(Definition.Type.COMPLEX)
      .mutability(Definition.Mutability.READ_ONLY);

    Collection<Definition> subAttributes = attributes(Metadata.class);
    factory.subAttributes(subAttributes.toArray(new Definition[subAttributes.size()]));

    METADATA = factory.build();
    COMMON   = CollectionUtility.unmodifiableList(SCHEMAS, ID, METADATA);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>SchemaFactory</code> discoverer that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new SchemaFactory()" and enforces use of the public method below.
   */
  private SchemaFactory() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Returns the schema for a class.
   ** <br>
   ** This will walk the inheritance tree looking for information about the
   ** SCIM schema of the objects represented. This information comes from
   ** annotations and introspection.
   **
   ** @param  clazz              the class to get the schema for.
   **
   ** @return                    the schema.
   */
  public static SchemaResource schema(final Class<?> clazz) {
    final Schema annotation = clazz.getAnnotation(Schema.class);
    // only generate schema for annotated classes.
    if (annotation == null) {
      return null;
    }
    return new SchemaResource(annotation.id(), annotation.name(), annotation.description(), attributes(clazz));
  }
}