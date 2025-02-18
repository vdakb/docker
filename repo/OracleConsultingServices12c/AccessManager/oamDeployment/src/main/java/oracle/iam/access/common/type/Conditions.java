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

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Conditions.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Conditions.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.type;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceDataType;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.access.common.spi.schema.AttributeCondition;
import oracle.iam.access.common.spi.schema.Ip4Range;
import oracle.iam.access.common.spi.schema.Ip4RangeList;
import oracle.iam.access.common.spi.schema.ConditionsList;

////////////////////////////////////////////////////////////////////////////////
// class Conditions
// ~~~~~ ~~~~~~~~~~
/**
 ** <code>Conditions</code> specifies ...
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Conditions extends DelegatingDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final ConditionsList delegate = factory.createConditionsList();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Condition
  // ~~~~~ ~~~~~~~~~
  public static class Condition extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final oracle.iam.access.common.spi.schema.Condition delegate = factory.createCondition();

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class IP
    // ~~~~~ ~~
    public static class IP extends ServiceDataType {

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      final Ip4RangeList delegate = factory.createIp4RangeList();

      //////////////////////////////////////////////////////////////////////////
      // Member classes
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // class Range
      // ~~~~~ ~~~~~
      public static class Range extends ServiceDataType {

        ////////////////////////////////////////////////////////////////////////
        // instance attributes
        ////////////////////////////////////////////////////////////////////////

        final Ip4Range delegate = factory.createIp4Range();

        ////////////////////////////////////////////////////////////////////////
        // Constructors
        ////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////
        // Method: Ctor
        /**
         ** Constructs a <code>Range</code> type that allows use as a JavaBean.
         ** <br>
         ** Zero argument constructor required by the framework.
         ** <br>
         ** Default Constructor
         */
        public Range() {
          // ensure inheritance
          super();
        }

        ////////////////////////////////////////////////////////////////////////
        // Mutator methods
        ////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////
        // Method: setFrom
        /**
         ** Sets the value of the <code>from</code> property.
         **
         ** @param  value        the value of the <code>from</code> property.
         **                      Allowed object is {@link String}.
         */
        public void setFrom(final String value) {
          this.delegate.setFromIp(value);
        }

        ////////////////////////////////////////////////////////////////////////
        // Method: setTo
        /**
         ** Sets the value of the <code>to</code> property.
         **
         ** @param  value        the value of the <code>to</code> property.
         **                      Allowed object is {@link String}.
         */
        public void setTo(final String value) {
          this.delegate.setToIp(value);
        }
      }

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>IP</code> type that allows use as a JavaBean.
       ** <br>
       ** Zero argument constructor required by the framework.
       ** <br>
       ** Default Constructor
       */
      public IP() {
        // ensure inheritance
        super();
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods group by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: addConfiguredRange
      /**
       ** Call by the ANT deployment to inject the argument for adding an
       ** IP address range.
       **
       ** @param  value          the {@link Range} instance to add.
       */
      public void addConfiguredRange(final Range value) {
        // verify if the reference id is set on the element to prevent
        // inconsistency
        if (isReference())
          throw noChildrenAllowed();

        // assign the correspondending ip range property
        this.delegate.getIp4Range().add(value.delegate);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // class Temporal
    // ~~~~~ ~~~~~~~~
    public static class Temporal extends ServiceDataType {

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      final oracle.iam.access.common.spi.schema.Temporal delegate = factory.createTemporal();

      //////////////////////////////////////////////////////////////////////////
      // Member classes
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // class DayOfWeek
      // ~~~~~ ~~~~~~~~~
      public static class DayOfWeek extends ServiceDataType {

        ////////////////////////////////////////////////////////////////////////
        // instance attributes
        ////////////////////////////////////////////////////////////////////////

        final oracle.iam.access.common.spi.schema.DayOfWeek delegate = factory.createDayOfWeek();

        ////////////////////////////////////////////////////////////////////////
        // Constructors
        ////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////
        // Method: Ctor
        /**
         ** Constructs a <code>DayOfWeek</code> type that allows use as a
         ** JavaBean.
         ** <br>
         ** Zero argument constructor required by the framework.
         ** <br>
         ** Default Constructor
         */
        public DayOfWeek() {
          // ensure inheritance
          super();
        }

        ////////////////////////////////////////////////////////////////////////
        // Mutator methods
        ////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////
        // Method: setMonday
        /**
         ** Sets the value of the <code>monday</code> property.
         **
         ** @param  value        the value of the <code>monday</code> property.
         **                      Allowed object is {@link String}.
         */
        public void setMonday(final String value) {
          this.delegate.setMonday(value);
        }

        ////////////////////////////////////////////////////////////////////////
        // Method: setTuesday
        /**
         ** Sets the value of the <code>tuesday</code> property.
         **
         ** @param  value        the value of the <code>tuesday</code> property.
         **                      Allowed object is {@link String}.
         */
        public void setTuesday(final String value) {
          this.delegate.setTuesday(value);
        }

        ////////////////////////////////////////////////////////////////////////
        // Method: setWednesday
        /**
         ** Sets the value of the <code>wednesday</code> property.
         **
         ** @param  value        the value of the <code>wednesday</code>
         **                      property.
         **                      Allowed object is {@link String}.
         */
        public void setWednesday(final String value) {
          this.delegate.setWednesday(value);
        }

        ////////////////////////////////////////////////////////////////////////
        // Method: setThursday
        /**
         ** Sets the value of the <code>thursday</code> property.
         **
         ** @param  value        the value of the <code>thursday</code>
         **                      property.
         **                      Allowed object is {@link String}.
         */
        public void setThursday(final String value) {
          this.delegate.setThursday(value);
        }

        ////////////////////////////////////////////////////////////////////////
        // Method: setFriday
        /**
         ** Sets the value of the <code>friday</code> property.
         **
         ** @param  value        the value of the <code>friday</code> property.
         **                      Allowed object is {@link String}.
         */
        public void setFriday(final String value) {
          this.delegate.setFriday(value);
        }

        ////////////////////////////////////////////////////////////////////////
        // Method: setSaturday
        /**
         ** Sets the value of the <code>saturday</code> property.
         **
         ** @param  value        the value of the <code>saturday</code>
         **                      property.
         **                      Allowed object is {@link String}.
         */
        public void setSaturday(final String value) {
          this.delegate.setSaturday(value);
        }

        ////////////////////////////////////////////////////////////////////////
        // Method: setSunday
        /**
         ** Sets the value of the <code>sunday</code> property.
         **
         ** @param  value        the value of the <code>sunday</code> property.
         **                      Allowed object is {@link String}.
         */
        public void setSunday(final String value) {
          this.delegate.setSunday(value);
        }
      }

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>Temporal</code> type that allows use as a JavaBean.
       ** <br>
       ** Zero argument constructor required by the framework.
       ** <br>
       ** Default Constructor
       */
      public Temporal() {
        // ensure inheritance
        super();
      }

      //////////////////////////////////////////////////////////////////////////
      // Mutator methods
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: setStartTime
      /**
       ** Sets the value of the <code>startTime</code> property.
       **
       ** @param  value          the value of the <code>startTime</code>
       **                        property.
       **                        Allowed object is {@link String}.
       */
      public void setStartTime(final String value) {
        this.delegate.setStartTime(value);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: setEndTime
      /**
       ** Sets the value of the <code>endTime</code> property.
       **
       ** @param  value          the value of the <code>endTime</code> property.
       **                        Allowed object is {@link String}.
       */
      public void setEndTime(final String value) {
        this.delegate.setEndTime(value);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: setDaysToString
      /**
       ** Sets the value of the <code>daysToString</code> property.
       **
       ** @param  value          the value of the <code>daysToString</code>
       **                        property.
       **                        Allowed object is {@link String}.
       */
      public void setDaysToString(final String value) {
        this.delegate.setDaysToString(value);
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods group by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: addConfiguredDayOfWeek
      /**
       ** Call by the ANT deployment to inject the argument for adding a
       ** day of week.
       **
       ** @param  value          the {@link DayOfWeek} instance to add.
       **
       ** @throws BuildException if this instance is already referencing a
       **                        {@link DayOfWeek}.
       */
      public void addConfiguredDayOfWeek(final DayOfWeek value)
        throws BuildException {

        // verify if the reference id is set on the element to prevent
        // inconsistency
        if (isReference())
          throw noChildrenAllowed();

        // prevent bogus input
        if (value == null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "dayOfWeek"));

        // prevent bogus state
        if (this.delegate.getDayOfWeek() != null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "dayOfWeek"));

        // assign the correspondending temporal property
        this.delegate.setDayOfWeek(value.delegate);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // class Identity
    // ~~~~~ ~~~~~~~~
    public static class Identity extends ServiceDataType {

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      final oracle.iam.access.common.spi.schema.Identity delegate = factory.createIdentity();

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>Identity</code> type that allows use as a JavaBean.
       ** <br>
       ** Zero argument constructor required by the framework.
       ** <br>
       ** Default Constructor
       */
      public Identity() {
        // ensure inheritance
        super();
      }

      //////////////////////////////////////////////////////////////////////////
      // Mutator methods
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: setStore
      /**
       ** Sets the value of the <code>store</code> property.
       **
       ** @param  value          the value of the <code>store</code> property.
       **                        Allowed object is {@link String}.
       */
      public void setStore(final String value) {
        this.delegate.setStoreName(value);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: setType
      /**
       ** Sets the value of the <code>type</code> property.
       **
       ** @param  value          the value of the <code>type</code> property.
       **                        Allowed object is {@link String}.
       */
      public void setType(final String value) {
        this.delegate.setEntityType(value);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: setEntity
      /**
       ** Sets the value of the <code>entity</code> property.
       **
       ** @param  value          the value of the <code>entity</code> property.
       **                        Allowed object is {@link String}.
       */
      public void setEntity(final String value) {
        this.delegate.setEntityName(value);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // class Attribute
    // ~~~~~ ~~~~~~~~~
    public static class Attribute extends ServiceDataType {

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      final AttributeCondition delegate = factory.createAttributeCondition();

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>Attribute</code> type that allows use as a
       ** JavaBean.
       ** <br>
       ** Zero argument constructor required by the framework.
       ** <br>
       ** Default Constructor
       */
      public Attribute() {
        // ensure inheritance
        super();
      }

      //////////////////////////////////////////////////////////////////////////
      // Mutator methods
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: setName
      /**
       ** Sets the value of the <code>name</code> property.
       **
       ** @param  value          the value of the <code>name</code> property.
       **                        Allowed object is {@link String}.
       */
      public void setName(final String value) {
        this.delegate.setAttributeName(value);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: setNamespace
      /**
       ** Sets the value of the <code>namespace</code> property.
       **
       ** @param  value          the value of the <code>namespace</code>
       **                        property.
       **                        Allowed object is {@link String}.
       */
      public void setNamespace(final String value) {
        this.delegate.setNamespace(value);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: setValue
      /**
       ** Sets the value of the <code>value</code> property.
       **
       ** @param  value          the value of the <code>value</code> property.
       **                        Allowed object is {@link String}.
       */
      public void setValue(final String value) {
        this.delegate.setValue(value);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: setOperator
      /**
       ** Sets the value of the <code>operator</code> property.
       **
       ** @param  value          the value of the <code>operator</code>
       **                        property.
       **                        Allowed object is {@link String}.
       */
      public void setOperator(final String value) {
        this.delegate.setOperator(value);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Condition</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Condition() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setName
    /**
     ** Sets the value of the <code>name</code> property.
     **
     ** @param  value            the value of the <code>name</code> property.
     **                          Allowed object is {@link String}.
     */
    public void setName(final String value) {
      this.delegate.setName(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setType
    /**
     ** Sets the value of the <code>type</code> property.
     **
     ** @param  value            the value of the <code>type</code> property.
     **                          Allowed object is {@link String}.
     */
    public void setType(final String value) {
      this.delegate.setConditionClassType(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setCombiner
    /**
     ** Sets the value of the <code>combiner</code> property.
     **
     ** @param  value            the value of the <code>combiner</code>
     **                          property.
     **                          Allowed object is {@link String}.
     */
    public void setCombiner(final String value) {
      this.delegate.setConditionCombiner(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   addConfiguredIP
    /**
     ** Call by the ANT deployment to inject the argument for adding a IP list.
     **
     ** @param  value            the list of {@link AccessServer} instance to add.
     **
     ** @throws BuildException   if this instance is already referencing an
     **                          {@link IP}.
     */
    public void addConfiguredIP(final IP value)
      throws BuildException {

      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // prevent bogus input
      if (value == null)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "ip"));

      // prevent bogus state
      if (this.delegate.getIp4RangeList() != null)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "ip"));

      // assign the correspondending condition property
      this.delegate.setIp4RangeList(value.delegate);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   addConfiguredTemporal
    /**
     ** Call by the ANT deployment to inject the argument for adding a temporal.
     **
     ** @param  value            the list of {@link Temporal} instance to set.
     **
     ** @throws BuildException   if this instance is already referencing a
     **                          {@link Temporal}.
     */
    public void addConfiguredTemporal(final Temporal value)
      throws BuildException {

      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // prevent bogus input
      if (value == null)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "temporal"));

      // prevent bogus state
      if (this.delegate.getTemporal() != null)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "temporal"));

      // assign the correspondending condition property
      this.delegate.setTemporal(value.delegate);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   addConfiguredIdentity
    /**
     ** Call by the ANT deployment to inject the argument for adding an
     ** identity.
     **
     ** @param  value           the list of {@link Identity} instance to add.
     */
    public void addConfiguredIdentity(final Identity value) {
      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // prevent bogus input
      if (value == null)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "identity"));

      // assign the correspondending condition property
      this.delegate.getIdentity().add(value.delegate);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   addConfiguredAttribute
    /**
     ** Call by the ANT deployment to inject the argument for adding an
     ** attribute.
     **
     ** @param  value           the list of {@link Attribute} instance to add.
     */
    public void addConfiguredAttribute(final Attribute value) {
      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // prevent bogus input
      if (value == null)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "identity"));

      // assign the correspondending condition property
      this.delegate.getAttributeCondition().add(value.delegate);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Conditions</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Conditions() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredCondition
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** <code>Condition</code>.
   **
   ** @param  condition          the {@link Condition} instance to add.
   **                            Allowed object is {@link Condition}.
   **
   ** @throws BuildException     if the condition instance is already added.
   */
  public void addConfiguredCondition(final Condition condition)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // prevent bogus input
    if (condition == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "condition"));

    // add the correspondending rule property
    this.delegate.getCondition().add(condition.delegate);
  }
}