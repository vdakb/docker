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

    File        :   FeatureSchema.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FeatureSchema.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.dbs;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashSet;

import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.SchemaBuilder;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.AttributeInfoBuilder;
import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;
import oracle.iam.identity.icf.foundation.logging.AbstractLogger;

import oracle.iam.identity.icf.foundation.utility.StringUtility;

import oracle.iam.identity.icf.dbms.DatabaseEntity;
import oracle.iam.identity.icf.dbms.DatabaseFilter;
import oracle.iam.identity.icf.dbms.DatabaseSelect;

import oracle.iam.identity.icf.connector.DatabaseSchema;
import oracle.iam.identity.icf.connector.DatabaseDialect;
import oracle.iam.identity.icf.connector.DatabaseContext;

public class FeatureSchema extends AbstractLogger {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FeatureSchema</code> which is associated with the
   ** specified logging category.
   **
   ** @param  category           the category for the {@link Logger}.
   */
  private FeatureSchema(final String category) {
    // ensure inheritance
    super(category);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Builds schema meta-data from configuration and by obtaining meta-data from
   ** target environment. Can't override this method because static, so this
   ** requires a new class.
   **
   ** @param  context            to target system.
   **
   ** @return                    the {@link Schema}.
   **
   ** @throws SystemException    if the requested {@link DatabaseDialect} is not
   **                            found on the classpath or could not be either
   **                            created or accessed by the given
   **                            {@link DatabaseContext}.
   */
  public static Schema build(final DatabaseContext context)
    throws SystemException {

    final FeatureSchema schema  = new FeatureSchema(Main.CATEGORY);
    final SchemaBuilder builder = new SchemaBuilder(Main.class);
    builder.defineObjectClass(schema.describeAccount(context));
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   describeAccount
  /**
   ** Returns the schema using a SELECT query.
   **
   ** @return                    Schema based on a empty SELECT query.
   */
  private ObjectClassInfo describeAccount(final DatabaseContext context)
    throws SystemException {

    final String method = "describeAccount";
    trace(method, Loggable.METHOD_ENTRY);
    final ObjectClassInfoBuilder builder = new ObjectClassInfoBuilder();

    // first, compute the account attributes based on the database schema
    builder.setType(ObjectClass.ACCOUNT_NAME);
    builder.addAllAttributeInfo(describeEntity(context, DatabaseSchema.Entity.Account));

    trace(method, Loggable.METHOD_EXIT);
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   describeEntity
  private Set<AttributeInfo> describeEntity(final DatabaseContext context, final DatabaseSchema.Entity object)
    throws SystemException {
    final String method = "describeEntity";
    trace(method, Loggable.METHOD_ENTRY);

    final DatabaseEntity schema = context.dialectEntity(DatabaseSchema.Entity.Catalog);
    final DatabaseEntity entity = context.dialectEntity(object);
    final DatabaseFilter filter = DatabaseFilter.build(schema.primary(), entity.id(), DatabaseFilter.Operator.EQUAL);
    final DatabaseSelect query  = DatabaseSelect.build(this, schema, filter, schema.attribute());

    final List<Map<String, Object>> result = query.execute(10000);
//    "SELECT DDDD FROM dba_tables where table_name = ?";
    for (Map<String, Object> cursor : result) {
      info(StringUtility.formatCollection(cursor));
    }

    final Set<AttributeInfo> detail = new HashSet<AttributeInfo>();

    final String[] fake = {"A", "B", "C" };
    for (String cursor : fake) {
      final AttributeInfoBuilder builder = new AttributeInfoBuilder();
      builder.setName(cursor);
      builder.setRequired(true);
      detail.add(builder.build());
    }

    trace(method, Loggable.METHOD_EXIT);
    return detail;
  }
}