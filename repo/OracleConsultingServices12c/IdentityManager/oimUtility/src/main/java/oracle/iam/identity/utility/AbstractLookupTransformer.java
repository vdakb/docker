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

    Copyright © 2006. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Utility Facilities

    File        :   AbstractLookupTransformer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractLookupTransformer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2006-04-08  DSteding    First release version
*/

package oracle.iam.identity.utility;

import java.util.Map;

import oracle.hst.foundation.logging.Logger;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractLookup;
import oracle.iam.identity.foundation.AbstractAttributeTransformer;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractLookupTransformer
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractLookupTransformer</code> is the base class to transform a
 ** field based on a Lookup Definition in Oracle Identity Manager to the decoded
 ** value (or a default is no code key is mapped) representation and vice versa.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractLookupTransformer extends AbstractAttributeTransformer {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private AbstractLookup      lookup;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractLookupTransformer</code> that use the
   ** specified {@link Logger} for logging purpose.
   **
   ** @param  lookup             the name of the Lookup Definition to resolve
   **                            the value to an appropriate representation
   ** @param  logger             the {@link Logger} for logging purpose.
   **
   ** @throws TaskException      if the <code>Metadata Descriptor</code> is not
   **                            defined in  Oracle Identity Manager metadata
   **                            entries or one or more attributes are missing
   **                            on the <code>Metadata Descriptor</code>.
   */
  public AbstractLookupTransformer(final String lookup, final Logger logger)
    throws TaskException {

    // ensure inheritance
    super(logger);

    // instantiate the dictionary
    this.lookup = new AbstractLookup(this, lookup);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns the Lookup Definition used to resolved the feeded
   ** value to a decoded value.
   **
   ** @return                    the Lookup Definition used to resolved the
   **                            feeded value to a decoded value.
   */
  protected final AbstractLookup lookup() {
    return this.lookup;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultValue
  /**
   ** Returns the value that will be returned by the transformation if a
   ** passed in value feeded from the source system is not mapped in the
   ** associated Lookup Definition.
   **
   ** @return                    the value that will be returned by the
   **                            transformation if a passed in value is not
   **                            mapped in the associated Lookup Definition.
   */
  protected abstract Object defaultValue();

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform (AttributeTransformer)
  /**
   ** Returns the specified <code>origin</code> as an appropriate
   ** transformation.
   ** <br>
   ** The {@link Map} <code>origin</code> contains all untouched values. The
   ** {@link Map} <code>subject</code> contains all transformed values
   **
   ** @param  attributeName      the specific attribute in the {@link Map}
   **                            <code>origin</code> that has to be transformed.
   ** @param  origin             the {@link Map} to transform.
   ** @param  subject            the transformation of the specified
   **                            {@link Map} <code>origin</code>.
   */
  public void transform(final String attributeName, final Map<String, Object> origin, final Map<String, Object> subject) {
     Object value = origin.get(attributeName);
    // if we not got a String return the Oracle Identity Manager default
    // Organization
    if (value == null)
      value = defaultValue();
    else if (this.lookup.containsKey(value))
      value = this.lookup.get(value);
    else
      value = defaultValue();

    // if we not got an empty String return the Oracle Identity Manager default
    // status; otherwise return the converted value as is
    subject.put(attributeName, (StringUtility.isEmpty((String)value)) ?  defaultValue() : value);
  }
}