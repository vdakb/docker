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

    File        :   Rules.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Rule.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.type;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceDataType;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.access.common.spi.schema.CombinerMode;
import oracle.iam.access.common.spi.schema.ConditionNameList;
import oracle.iam.access.common.spi.schema.ConditionCombiner;

////////////////////////////////////////////////////////////////////////////////
// class Rules
// ~~~~~ ~~~~~
/**
 ** <code>Rules</code> specifies ...
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Rules extends DelegatingDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final oracle.iam.access.common.spi.schema.Rules delegate = factory.createRules();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Rule
  // ~~~~~ ~~~~
  public static class Rule extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final oracle.iam.access.common.spi.schema.Rule delegate = factory.createRule();

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Combiner
    // ~~~~~ ~~~~~~~~
    public static class Combiner extends ServiceDataType {

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      final ConditionCombiner delegate = factory.createConditionCombiner();

      //////////////////////////////////////////////////////////////////////////
      // Member classes
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // class Mode
      // ~~~~~ ~~~~
      public static class Mode extends ServiceDataType {

        ////////////////////////////////////////////////////////////////////////
        // instance attributes
        ////////////////////////////////////////////////////////////////////////

        final CombinerMode delegate = factory.createCombinerMode();

        ////////////////////////////////////////////////////////////////////////
        // Member classes
        ////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////
        // class ConditionNames
        // ~~~~~ ~~~~~~~~~~~~~~
        public static class ConditionNames extends ServiceDataType {

          //////////////////////////////////////////////////////////////////////
          // instance attributes
          //////////////////////////////////////////////////////////////////////

          final ConditionNameList delegate = factory.createConditionNameList();

          //////////////////////////////////////////////////////////////////////
          // Member classes
          //////////////////////////////////////////////////////////////////////

          //////////////////////////////////////////////////////////////////////
          // class Ref
          // ~~~~~ ~~~
          public static class Ref extends ServiceDataType {

            ////////////////////////////////////////////////////////////////////
            // instance attributes
            ////////////////////////////////////////////////////////////////////

            String value;

            ////////////////////////////////////////////////////////////////////
            // Constructors
            ////////////////////////////////////////////////////////////////////

            ////////////////////////////////////////////////////////////////////
            // Method: Ctor
            /**
             ** Constructs a <code>Ref</code> type that allows use as a
             ** JavaBean.
             ** <br>
             ** Zero argument constructor required by the framework.
             ** <br>
             ** Default Constructor
             */
            public Ref() {
              // ensure inheritance
              super();
            }

            ////////////////////////////////////////////////////////////////////
            // Mutator methods
            ////////////////////////////////////////////////////////////////////

            ////////////////////////////////////////////////////////////////////
            // Method: setValue
            /**
             ** Sets the value of the <code>value</code> property.
             **
             ** @param  value    the value of the <code>value</code> property.
             **                  Allowed object is {@link String}.
             */
            public void setValue(final String value) {
              this.value= value;
            }
          }

          //////////////////////////////////////////////////////////////////////
          // Constructors
          //////////////////////////////////////////////////////////////////////

          //////////////////////////////////////////////////////////////////////
          // Method: Ctor
          /**
           ** Constructs a <code>ConditionNames</code> type that allows use as a
           ** JavaBean.
           ** <br>
           ** Zero argument constructor required by the framework.
           ** <br>
           ** Default Constructor
           */
          public ConditionNames() {
            // ensure inheritance
            super();
          }

          //////////////////////////////////////////////////////////////////////
          // Methods group by functionality
          //////////////////////////////////////////////////////////////////////

          //////////////////////////////////////////////////////////////////////
          // Method: addConfiguredRef
          /**
           ** Call by the ANT deployment to inject the argument for adding a
           ** <code>Condition Name Reference</code>.
           **
           ** @param  value      the {@link Ref} instance to set.
           **                    Allowed object is {@link Ref}.
           **
           ** @throws BuildException if the rule instance is already set.
           */
          public void addConfiguredRef(final Ref value)
            throws BuildException {

            // verify if the reference id is set on the element to prevent
            // inconsistency
            if (isReference())
              throw noChildrenAllowed();

            // prevent bogus input
            if (value == null)
              throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "ref"));

            // prevent bogus state
            if (this.delegate.getConditionName().contains(value.value))
              throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "ref"));

            this.delegate.getConditionName().add(value.value);
          }
        }

        ////////////////////////////////////////////////////////////////////////
        // Constructors
        ////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////
        // Method: Ctor
        /**
         ** Constructs a <code>Mode</code> type that allows use as a JavaBean.
         ** <br>
         ** Zero argument constructor required by the framework.
         ** <br>
         ** Default Constructor
         */
        public Mode() {
          // ensure inheritance
          super();
        }

        ////////////////////////////////////////////////////////////////////////
        // Mutator methods
        ////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////
        // Method: setMode
        /**
         ** Sets the value of the <code>mode</code> property.
         **
         ** @param  value        the value of the <code>mode</code> property.
         **                      Allowed object is {@link String}.
         */
        public void setMode(final String value) {
          this.delegate.setMode(value);
        }

        ////////////////////////////////////////////////////////////////////////
        // Methods group by functionality
        ////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////
        // Method: addConfiguredConditionNames
        /**
         ** Call by the ANT deployment to inject the argument for setting the
         ** <code>ConditionNames</code>.
         **
         ** @param  value        the {@link ConditionNames} instance to set.
         **                      Allowed object is {@link ConditionNames}.
         **
         ** @throws BuildException if the rule instance is already set.
         */
        public void addConfiguredConditionNames(final ConditionNames value)
          throws BuildException {

          // verify if the reference id is set on the element to prevent
          // inconsistency
          if (isReference())
            throw noChildrenAllowed();

          // prevent bogus input
          if (value == null)
            throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "conditionNames"));

          // prevent bogus state
          if (this.delegate.getConditionNameList() != null)
            throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "conditionNames"));

          this.delegate.setConditionNameList(value.delegate);
        }
      }

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>Combiner</code> type that allows use as a JavaBean.
       ** <br>
       ** Zero argument constructor required by the framework.
       ** <br>
       ** Default Constructor
       */
      public Combiner() {
        // ensure inheritance
        super();
      }

      //////////////////////////////////////////////////////////////////////////
      // Mutator methods
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: setType
      /**
       ** Sets the value of the <code>type</code> property.
       **
       ** @param  value          the value of the <code>type</code> property.
       **                        Allowed object is {@link String}.
       */
      public void setType(final String value) {
        this.delegate.setCombinerType(value);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: setExpression
      /**
       ** Sets the value of the <code>expression</code> property.
       **
       ** @param  value          the value of the <code>expression</code>
       **                        property.
       **                        Allowed object is {@link String}.
       */
      public void setExpression(final String value) {
        this.delegate.setExpression(value);
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods group by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method:   addConfiguredMode
      /**
       ** Call by the ANT deployment to inject the argument for adding a
       ** <code>Mode</code>.
       **
       ** @param  value          the {@link Mode} instance to set.
       **                        Allowed object is {@link Mode}.
       **
       ** @throws BuildException if the rule instance is already set.
       */
      public void addConfiguredMode(final Mode value)
        throws BuildException {

        // verify if the reference id is set on the element to prevent
        // inconsistency
        if (isReference())
          throw noChildrenAllowed();

        // prevent bogus input
        if (value == null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "mode"));

        // prevent bogus state
        if (this.delegate.getCombinerMode() != null)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "mode"));

        this.delegate.setCombinerMode(value.delegate);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Rule</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Rule() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setEffect
    /**
     ** Sets the value of the <code>effect</code> property.
     **
     ** @param  value            the value of the <code>effect</code> property.
     **                          Allowed object is {@link String}.
     */
    public void setEffect(final String value) {
      this.delegate.setRuleEffect(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredCombiner
    /**
     ** Call by the ANT deployment to inject the argument for setting a
     ** combiner.
     **
     ** @param  value            the {@link Combiner} instance to set.
     **
     ** @throws BuildException   if this instance is already referencing a
     **                          {@link Combiner}.
     */
    public void addConfiguredCombiner(final Combiner value)
      throws BuildException {

      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // prevent bogus input
      if (value == null)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "combiner"));

      // prevent bogus state
      if (this.delegate.getConditionCombiner() != null)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "combiner"));

      // assign the correspondending temporal property
      this.delegate.setConditionCombiner(value.delegate);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Rules</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Rules() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredRule
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** <code>Rule</code>.
   **
   ** @param  value              the {@link Rule} instance to add.
   **                            Allowed object is {@link Rule}.
   **
   ** @throws BuildException     if the rule instance is already added.
   */
  public void addConfiguredRule(final Rule value)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "rule"));

    // add the correspondending rule property
    this.delegate.getRule().add(value.delegate);
  }
}