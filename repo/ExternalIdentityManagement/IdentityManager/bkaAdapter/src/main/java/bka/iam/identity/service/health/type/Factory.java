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

    Copyright 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Access Policy House Keeping

    File        :   Factory.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Factory.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet    First release version
*/

package bka.iam.identity.service.health.type;

import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import oracle.mds.query.Condition;
import oracle.mds.query.QueryItem;
import oracle.mds.query.QueryFactory;
import oracle.mds.query.DocumentQuery;
import oracle.mds.query.DocumentResult;
import oracle.mds.query.ConditionFactory;

import oracle.mds.core.MDSSession;
import oracle.mds.core.MDSInstance;
import oracle.mds.core.MOReference;
import oracle.mds.core.IsolationLevel;
import oracle.mds.core.MetadataObject;
import oracle.mds.core.SessionOptions;
import oracle.mds.core.MetadataNotFoundException;

import oracle.iam.platform.Platform;

////////////////////////////////////////////////////////////////////////////////
// class Factory
// ~~~~~ ~~~~~~~
/**
 ** <code>Factory</code> provide methods to populate extended
 ** <code>IT Resource</code> attributes from Matadata Service.
 ** <p>
 ** <code>IT Resource</code> attributes extending the information already
 ** provided by <code>ResourceConfig</code> belonging to the physical endpoint
 ** configuration with additional information about enttitlement and mutli-value
 ** attributes of the connector model.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class Factory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String NAMESPACE        = "http://www.oracle.com/schema/oim/pol/housekeeping/endpoint";
  public static final String ELEMENT_MULTIPLE = "endpoints";
  public static final String ELEMENT_SINGLE   = "endpoint";
  public static final String ELEMENT_TYPE     = "type";
  public static final String ELEMENT_NAME     = "name";
  public static final String ELEMENT_PREFIX   = "prefix";

  public static final String ATTRIBUTE_NAME   = "name";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Factory</code> instance that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Factory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** The initialization task.
   **
   ** @param  name               the name of the <code>IT Resource</code> to
   **                            create the descriptor with additional
   **                            properties for.
   **
   ** @return                    the populated {@link Descriptor}.
   **                            May be <code>null</code>.
   */
  public static Descriptor build(final String name) {
    final Condition   condition  = ConditionFactory.createNameCondition("/", "%", true)
     .and(
           ConditionFactory.createElementCondition(new QueryItem("", ELEMENT_MULTIPLE))
       .or(ConditionFactory.createElementCondition(new QueryItem(NAMESPACE, ELEMENT_MULTIPLE)))
     );

    final MDSInstance              instance = Platform.getMDSInstance();
    final MDSSession               session  = instance.createSession(new SessionOptions(IsolationLevel.READ_COMMITTED, null, null), null);
    final DocumentQuery            query    = QueryFactory.createDocumentQuery(instance, condition);
    final Iterator<DocumentResult> results  = query.execute();

    Descriptor result = null;
    while (results.hasNext()) {
      MOReference    mr = results.next().getMOName().getMDSReference().getMOReference();
      MetadataObject mo  = null;
      try {
        mo = session.getMetadataObject(mr);
      }
      catch (MetadataNotFoundException e) {
        e.printStackTrace();
        continue;
      }
      final Document doc = mo.getDocument();
      final NodeList res = doc.getElementsByTagName(ELEMENT_SINGLE);
      for (int i = 0; i < res.getLength(); i++) {
        final String local = res.item(i).getAttributes().getNamedItem(ATTRIBUTE_NAME).getNodeValue();
        if (local.equalsIgnoreCase(name)) {
          result = Descriptor.build(name);
          final NodeList types = res.item(i).getChildNodes();
          for (int j = 0; j < types.getLength(); j++) {
            if (ELEMENT_TYPE.equals(types.item(j).getNodeName())) {
              final Descriptor.Type item = new Descriptor.Type();
              final NodeList        type = types.item(j).getChildNodes();
              for (int k = 0; k < type.getLength(); k++) {
                if (ELEMENT_NAME.equals(type.item(k).getNodeName())) {
                  item.name = type.item(k).getTextContent();
                }
                if (ELEMENT_PREFIX.equals(type.item(k).getNodeName())) {
                  item.prefix = Boolean.valueOf(type.item(k).getTextContent());
                }
              }
              result.add(item);
            }
          }
          break;
        }
      }
    }
    return result;
  }
}