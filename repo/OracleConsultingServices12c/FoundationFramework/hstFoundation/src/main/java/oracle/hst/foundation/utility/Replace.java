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

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Utility Facility

    File        :   Replace.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Replace.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

import java.util.Map;
import java.util.HashMap;

////////////////////////////////////////////////////////////////////////////////
// interface Replace
// ~~~~~~~~~ ~~~~~~~
/**
 ** <code>Replace</code> is a token replacer for Strings.
 ** <br>
 ** It will replace the found token with a provided static value or a
 ** dynamically generated value created by a {@link Token.Value}.
 ** <p>
 ** <code>Replace</code> itself <b>IS NOT THREAD SAFE</b>. so handling
 ** <code>Replace</code> in a multi threaded environment should be synchronized
 ** by the client.
 ** <p>
 ** Simplest use case, only <b>static values</b>
 ** <pre>
 **   final Replace replace = new Replace.Default().register(&quot;number&quot;, &quot;123&quot;);
 **   replace.substitute(&quot;i can count to {number}&quot;);
 ** </pre>
 ** Is same as registering an <b>explicit {@link Token}</b>
 ** <pre>
 **  final Replace replace = new Replace.Default()(new Token(&quot;number&quot;).replacedBy(&quot;123&quot;));
 **  replace.substitute(&quot;i can count to {number}&quot;);
 ** </pre>
 ** We can also use a <b>{@link Token.Value}</b> to <b>dynamically</b> get the
 ** value (which here does not really make sense ;-)
 ** <pre>
 **  final Replace replace = new Replace.Default().register(
 **    new Token(&quot;number&quot;).replacedBy(
 **      new Token.Value() {
 **        &#064;Override
 **        public void inject(final String[] args) {
 **          // intentionally left blank
 **        }
 **        &#064;Override
 **        public String get() {
 **          return &quot;123&quot;
 **        }
 **      }
 **    )
 **  );
 ** </pre>
 ** Here we use a {@link Token.Value} and <b>pass the arguments</b> "a,b,c" to
 ** it, they will be injected via {@link Token.Value#inject(String[] args)}
 ** before the call to {@link Token.Value#get()} is done. It is up to the
 ** generator to decide what to do with them. This feature makes handling tokens
 ** pretty powerful because you can write very dynamic generators.
 ** <pre>
 **   replace.substitute(&quot;i can count to {number(a,b,c)}&quot;);
 ** </pre>
 ** If its prefered to use <b>index based tokens</b>, you can also use this:
 ** <pre>
 **   replace.register(new String[] { &quot;one&quot;, &quot;two&quot;, &quot;three&quot; });
 **   replace.substitute(&quot;abc {0} {1} {2} def&quot;)); // will produce &quot;abc one two three def&quot;
 ** </pre>
 ** Of course you can replace all default <b>delimiters</b> with your preferred
 ** ones, just make sure start and end are different.
 ** <pre>
 **   replace.tokenStart(&quot;*&quot;); // default is '{'
 **   replace.tokenEnd(&quot;#&quot;); // default is '}'
 **   replace.argumentStart(&quot;[&quot;); // default is '('
 **   replace.argumentEnd(&quot;]&quot;); // default is ')'
 **   replace.argumentSeparator(&quot;&quot;); // default is ','
 ** </pre>
 ** By default throw IllegalStateExceptions will be thrown if there was no
 ** matching value or {@link Token.Value} found for a token. you can
 ** <b>enable/disable generating exceptions</b>.
 ** <pre>
 **  replace.strict(); // which is the DEFAULT
 ** </pre>
 ** will turn error reporting for missing values <b>OFF</b>
 ** <pre>
 **   replace.tolerant();
 ** </pre>
 ** You can <b>enable/disable value caching</b>. if you enable caching once a
 ** value for a token returned a value this value will be used for all
 ** subsequent tokens with the same name otherwise the {@link Token.Value} will
 ** be called once for every token.
 ** <br>
 ** e.g. {counter}{counter}{counter}<br>
 ** <br>
 ** with a registered generator will result in 3 calls to the value
 ** (resulting in poorer performance). so, if you know your generator will
 ** always return the same value enable caching.
 ** <pre>
 **   replace.enableValueCaching();
 **   replace.disableValueCaching();
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public interface Replace {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final char EOS            = '\0';
	static final char TOKEN_END      = '}';
	static final char TOKEN_START    = '{';
	static final char ARGS_END       = ')';
	static final char ARGS_START     = '(';
	static final char ARGS_SEPARATOR = ',';

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Default
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Default</code> is a token replacer for Strings.
   ** <br>
   ** It will replace the found token with a provided static value or a
   ** dynamically generated value created by a {@link Token.Value}.
   ** <p>
   ** <code>Replace</code> itself <b>IS NOT THREAD SAFE</b>. so handling
   ** <code>Replace</code> in a multi threaded environment should be synchronized
   ** by the client.
   */
  public static class Default implements Replace {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** the actual underlaying implementation that will be used. allows us to
     ** replace it without changing clients working with {@link Default} solely
     ** based on the {@link Replace} API.
     */
     private final Replace delegate;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
	  /**
	   ** Constructor for the <code>Default</code> implementation of the
     ** {@link Replace} API.
     */
  	public Default() {
      // ensure inheritance
      this(new Parser());
	  }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
	  /**
	   ** Constructor to allow clients of {@link Replace} to provided their own
     ** implementation of the {@link Replace} that will be called. Use this if
     ** you know what you are doing :-) otherwise always use the non-argument
     ** <code>Default()</code> constructor that will use the correct
     ** implementation!
     **
     ** @param  delegate         an implementation of the {@link Replace} API.
	   */
  	public Default(final Replace delegate) {
      // ensure inheritance
      super();

      // initialize instance
	  	this.delegate = delegate;
	  }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: strict (Replace}
    /**
     ** Tells the <code>Replace</code> to report any tokens that can not be
     ** replaced.
     ** <br>
     ** If turned on an {@link IllegalStateException} will be thrown during
     ** token replacement.
     ** <br>
     ** Reporting errors is turned on by default.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace strict() {
      return this.delegate.strict();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: tolerant (Replace}
    /**
     ** Tells the {@link Replace} to ignore any tokens that can not be replaced.
     ** <br>
     ** If turned off no Exceptions will be thrown during token replacement.
     ** <br>
     ** Reporting errors is turned on by default.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace tolerant() {
      return this.delegate.tolerant();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: tokenStart (Replace}
    /**
     ** Sets the token start identifier to the given value e.g.
     ** [dynamic] -&gt; '[' would be the start identifier, e.g. '['.
     **
     ** @param  start            the token start identifier to the given value
     **                          e.g. [dynamic] -&gt; '[' would be the start
     **                          identifier, e.g. '[', must not be
     **                          <code>null</code> or empty.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace tokenStart(final String start) {
      return this.delegate.tokenStart(start);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: tokenEnd (Replace}
    /**
     ** Sets the token end identifier to the given value e.g. [dynamic] -&gt; ']'
     ** would be the end identifier, e.g. '['.
     **
     ** @param  end              the token end identifier to the given value
     **                          e.g. [dynamic] -&gt; ']' would be the end
     **                          identifier, e.g. '[', must not be
     **                          <code>null</code> or empty.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace tokenEnd(final String end) {
      return this.delegate.tokenEnd(end);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: argumentStart (Replace}
    /**
     ** Sets the argument start identifier to the given value e.g.
     ** {dynamic[1;2;3]} -&gt; '[' would be the delimiter e.g. '['.
     **
     ** @param start             the argument start identifier to the given
     **                          value e.g. {dynamic[1;2;3]} -&gt; '[' would be
     **                          the delimiter e.g. '[', must not be
     **                          <code>null</code> or empty.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace argumentStart(final String start) {
      return this.delegate.argumentStart(start);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: argumentEnd (Replace}
    /**
     ** Sets the argument end identifier to the given value e.g.
     ** {dynamic[1;2;3]} -&gt; ']' would be the delimiter e.g. ']'.
     **
     ** @param  end              the argument end identifier to the given
     **                          value e.g. {dynamic[1;2;3]} -&gt; ']' would be
     **                          the delimiter e.g. ']', must not be
     **                          <code>null</code> or empty.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace argumentEnd(final String end) {
      return this.delegate.argumentEnd(end);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: argumentSeparator (Replace}
    /**
     ** Changes the separator of the arguments to the given value e.g.
     ** {dynamic(1;2;3)} -&gt; ';' would be the delimiter.
     **
     ** @param  separator        hanges the delimiter of the arguments to the
     **                          iven value e.g. {dynamic(1;2;3)} -&gt; ';'
     **                          would be the delimiter, must not be
     **                          <code>null</code> or empty.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace argumentSeparator(final String separator) {
      return this.delegate.argumentSeparator(separator);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: enableValueCaching (Replace}
    /**
     ** Turns value caching ON.
     ** <br>
     ** Once a value is determined through a {@link Token.Value} all remaining
     ** values with the same token name will be replaced by the cached version.
     ** <p>
     ** Use {@link #disableValueCaching()} to turn caching off.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace enableValueCaching() {
      return this.delegate.enableValueCaching();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: disableValueCaching (Replace}
    /**
     ** Turns value caching OFF.
     ** <p>
     ** Use {@link #enableValueCaching()} to turn caching on.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace disableValueCaching() {
      return this.delegate.disableValueCaching();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: register (Replace}
    /**
     ** Registers a static value for a given token.
     ** <br>
     ** If dynamic behaviour is required then {@link #register(Token)} can be
     ** used instead.
     ** <br>
     ** Is the same as registering a token via {@link #register(Token)} and
     ** supplying a replacement value via {@link Token#value(String)}.
     **
     ** @param  token            the name of the token to be replaced e.g. for
     **                          ${date} -&gt; "date" would be the token, must
     **                          not be <code>null</code> or empty.
     ** @param  value            the static value that will be used when
     **                          replacing the token, must not
     **                          be <code>null</code> or empty.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace register(final String token, final String value) {
      return this.delegate.register(token, value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: register (Replace}
    /**
     ** Registers a {@link Token} that will be replaced by the given
     ** {@link Token.Value}.
     ** <br>
     ** Same as registering a token via {@link #register(Token)} and supplying a
     ** {@link Token.Value} via {@link Token#value(Value)}.
     **
     ** @param  token            the name of the token to be replaced e.g. for
     **                          ${date} -&gt; "date" would be the token, must
     **                          not be <code>null</code> or empty.
     ** @param  value            the {@link Token.Value} to use when replacing
     **                          the value, must not be <code>null</code> or
     **                          empty.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace register(final String token, final Token.Value value) {
      return this.delegate.register(token, value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: register (Replace}
    /**
     ** Registers a {@link Token} that needs to be replaced.
     ** <br>
     ** The {@link Token} must have a valid static value or {@link Token.Value}
     ** associated with it which was set via {@link Token#value(String)} or
     ** {@link Token#value(Value)}.
     **
     ** @param  token            the {@link Token}, must not
     **                          be <code>null</code>.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace register(final Token token) {
      return this.delegate.register(token);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: register (Replace}
    /**
     ** Registers an array of replacements for a string based in indexed tokens.
     ** <br>
     ** the tokens will be replaced in the order they were added to the array.
     ** e.g.
     ** <pre>
     **   replacer.register(new String[] { "one", "two", "three" });
     **   replacer.substitute("{0} {1} {2}")); // will result in "one two three"
     ** </pre>
     **
     ** @param  replacements     the array of replacements that will be used
     **                          when replacing an indexed strings, must not be
     **                          <code>null</code> but can be empty.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace register(final String[] replacements) {
      return this.delegate.register(replacements);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: execute (Replace}
    /**
     ** Replaces all {@link Token} with one of the following:
     ** <ul>
     **   <li>the provided static values set via
     **       {@link #register(String, String)}
     **   <li>the token registered via {@link #register(Token)}
     **   <li>the vallue registered via {@link #register(String, Token.Value)}
     ** </ul>
     **
     ** @param  subject             the string that contains the tokens, will
     **                             be returned as-is in case of null or empty
     **                             string.
     **
     ** @return                     the result after replacing all tokens with the
     **                             proper values.
     **
     ** @throws RuntimeException    when the internal state is incorrect and
     **                             error reporting was turned on via
     **                             {@link #strict()}
     */
    @Override
    public final String execute(final String subject) {
      return this.delegate.execute(subject);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Parser
  // ~~~~~ ~~~~~~
  /**
   ** Parser implementation based on Finite State machine design.
   ** <br>
   ** NOT PART OF THE PUBLIC API! STILL HERE AND PUBLIC IN CASE YOU NEED TO
   ** 'EMERGENCY' SUBCLASS.
   */
  public class Parser implements Replace {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    protected char tokenStart                = TOKEN_START;
    protected char tokenEnd                  = TOKEN_END;

	  protected char argumentStart             = ARGS_START;
	  protected char argumentEnd               = ARGS_END;
	  protected char argumentSep               = ARGS_SEPARATOR;

	  protected boolean tolerant               = false;
	  protected boolean caching                = false;

	  protected final Map<String, Token> token = new HashMap<String, Token>();

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // enum State
    // ~~~~ ~~~~~
  	protected enum State {
	  	READING_INPUT
    , TOKEN_STARTED
    , READING_TOKEN
    , TOKEN_ARGS_STARTED
    , READING_TOKEN_ARGS
    , TOKEN_ARGS_END
    , ERROR
	  }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: startOfToken
    /**
     ** Verifies if the specified character is the registered token start
     ** character.
     **
     ** @param  c                the character to test
     **
     ** @return                  <code>true</code> if the character specified is
     **                          the registered token start where <code>{}</code>
     **                          is the default.
     ** @see     #TOKEN_START
     */
    protected boolean startOfToken(final char c) {
      return tokenStart(c);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endOfToken
    /**
     ** Verifies if the specified character is the registered token end
     ** character.
     **
     ** @param  c                the character to test
     **
     ** @return                  <code>true</code> if the character specified is
     **                          the registered token end where <code>}</code>
     **                          is the default.
     ** @see     #TOKEN_END
     */
    protected boolean endOfToken(final char c) {
      return tokenEnd(c);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: startOfArguments
    /**
     ** Verifies if the specified character is the registered token argument
     ** start character.
     **
     ** @param  c                the character to test
     **
     ** @return                  <code>true</code> if the character specified is
     **                          the registered token start where <code>(</code>
     **                          is the default.
     ** @see     #ARGS_START
     */
    protected boolean startOfArguments(final char c) {
      return argumentStart(c);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endOfArguments
    /**
     ** Verifies if the specified character is the registered token argument
     ** start character.
     **
     ** @param  c                the character to test
     **
     ** @return                  <code>true</code> if the character specified is
     **                          the registered token start where <code>)</code>
     **                          is the default.
     ** @see     #ARGS_END
     */
    protected boolean endOfArguments(final char c) {
      return argumentEnd(c);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: finalState
    private boolean finalState(final State state) {
      return state == State.READING_INPUT;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: defaultInput
    /**
     **
     ** @param  c                the character to test
     **
     ** @return                  <code>true</code> if the character specified is
     **                          not one of:
     **                          <ul>
     **                            <li>the registered token start character
     **                                where <code>{</code> is the default
     **                                <br>{@see #TOKEN_START]
     **                            <li>the registered token end character where
     **                                <code>}</code> is the default
     **                                <br>{@see #TOKEN_END]
     **                            <li>the registered token argument start
     **                                character where <code>(</code> is the
     **                                default
     **                                <br>{@see #ARGS_START]
     **                            <li>the registered token argument end
     **                                character where <code>)</code> is the
     **                                default
     **                                <br>{@see #ARGS_END]
     **                            <li>End Of String <code>0x00</code>
     **                          </ul>
     **                          otherwise <code>false</code>.
     */
    private boolean defaultInput(final char c) {
      boolean isIdentifier = tokenStart(c) || tokenEnd(c) || argumentStart(c) || argumentEnd(c) || (c == EOS);
      return (!isIdentifier);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: tokenStart
    /**
     ** Verifies if the specified character is the registered token start
     ** character.
     **
     ** @param  c                the character to test
     **
     ** @return                  <code>true</code> if the character specified is
     **                          the registered token start where <code>{}</code>
     **                          is the default.
     ** @see     #TOKEN_START
     */
    private boolean tokenStart(final char c) {
      return this.tokenStart == c;
    }


    ////////////////////////////////////////////////////////////////////////////
    // Method: tokenEnd
    /**
     ** Verifies if the specified character is the registered token end
     ** character.
     **
     ** @param  c                the character to test
     **
     ** @return                  <code>true</code> if the character specified is
     **                          the registered token end where <code>}</code>
     **                          is the default.
     ** @see     #TOKEN_END
     */
    private boolean tokenEnd(char c) {
      return this.tokenEnd == c;
    }


    ////////////////////////////////////////////////////////////////////////////
    // Method: argumentStart
    /**
     ** Verifies if the specified character is the registered token argument
     ** start character.
     **
     ** @param  c                the character to test
     **
     ** @return                  <code>true</code> if the character specified is
     **                          the registered token start where <code>(</code>
     **                          is the default.
     ** @see     #ARGS_START
     */
    private boolean argumentStart(final char c) {
      return this.argumentStart == c;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: argumentEnd
    /**
     ** Verifies if the specified character is the registered token argument
     ** start character.
     **
     ** @param  c                the character to test
     **
     ** @return                  <code>true</code> if the character specified is
     **                          the registered token start where <code>)</code>
     **                          is the default.
     ** @see     #ARGS_END
     */
    private boolean argumentEnd(final char c) {
      return this.argumentEnd == c;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: strict (Replace}
    /**
     ** Tells the <code>Replace</code> to report any tokens that can not be
     ** replaced.
     ** <br>
     ** If turned on an {@link IllegalStateException} will be thrown during
     ** token replacement if a token value isn't mapped.
     ** <br>
     ** Reporting errors is turned ON by DEFAULT.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace strict() {
      this.tolerant = false;
		  return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: tolerant (Replace}
    /**
     ** Tells the {@link Replace} to ignore any tokens that can not be replaced.
     ** <br>
     ** If turned OFF no Exceptions will be thrown during token replacement.
     ** <br>
     ** Reporting errors is turned ON by DEFAULT.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace tolerant() {
      this.tolerant = true;
		  return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: tokenStart (Replace}
    /**
     ** Sets the token start identifier to the given value e.g.
     ** [dynamic] -&gt; '[' would be the start identifier, e.g. '['.
     **
     ** @param  start            the token start identifier to the given value
     **                          e.g. [dynamic] -&gt; '[' would be the start
     **                          identifier, e.g. '[', must not be
     **                          <code>null</code> or empty.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace tokenStart(final String start) {
      assertOneChar(start);
      this.tokenStart = start.charAt(0);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: tokenEnd (Replace}
    /**
     ** Sets the token end identifier to the given value e.g.
     ** [dynamic] -&gt; ']' would be the end identifier, e.g. '['.
     **
     ** @param  end              the token end identifier to the given value
     **                          e.g. [dynamic] -&gt; ']' would be the end
     **                          identifier, e.g. '[', must not be
     **                          <code>null</code> or empty.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace tokenEnd(final String end) {
  		assertOneChar(end);
	  	this.tokenEnd = end.charAt(0);
		  return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: argumentStart (Replace}
    /**
     ** Sets the argument start identifier to the given value e.g.
     ** {dynamic[1;2;3]} -&gt; '[' would be the delimiter e.g. '['.
     **
     ** @param start             the argument start identifier to the given
     **                          value e.g. {dynamic[1;2;3]} -&gt; '[' would be
     **                          the delimiter e.g. '[', must not be
     **                          <code>null</code> or empty.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace argumentStart(final String start) {
  		assertOneChar(start);
	  	this.argumentStart = start.charAt(0);
  		return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: argumentEnd (Replace}
    /**
     ** Sets the argument end identifier to the given value e.g.
     ** {dynamic[1;2;3]} -&gt; ']' would be the delimiter e.g. ']'.
     **
     ** @param  end              the argument end identifier to the given
     **                          value e.g. {dynamic[1;2;3]} -&gt; ']' would be
     **                          the delimiter e.g. ']', must not be
     **                          <code>null</code> or empty.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace argumentEnd(final String end) {
      assertOneChar(end);
      this.argumentEnd = end.charAt(0);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: argumentSeparator (Replace}
    /**
     ** Changes the separator of the arguments to the given value e.g.
     ** {dynamic(1;2;3)} -&gt; ';' would be the delimiter.
     **
     ** @param  separator        hanges the delimiter of the arguments to the
     **                          iven value e.g. {dynamic(1;2;3)} -&gt; ';'
     **                          would be the delimiter, must not be
     **                          <code>null</code> or empty.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace argumentSeparator(final String separator) {
		  assertOneChar(separator);
  		this.argumentSep = separator.charAt(0);
	  	return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: enableValueCaching (Replace}
    /**
     ** Turns value caching ON.
     ** <br>
     ** Once a value is determined through a {@link Token.Value} all remaining
     ** values with the same token name will be replaced by the cached version.
     ** <p>
     ** Use {@link #disableValueCaching()} to turn caching off.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace enableValueCaching() {
      this.caching = true;
		  return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: disableValueCaching (Replace}
    /**
     ** Turns value caching OFF.
     ** <p>
     ** Use {@link #enableValueCaching()} to turn caching on.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace disableValueCaching() {
      this.caching = false;
		  return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: register (Replace}
    /**
     ** Registers a static value for a given token.
     ** <br>
     ** If dynamic behaviour is required then {@link #register(Token)} can be
     ** used instead.
     ** <br>
     ** Is the same as registering a token via {@link #register(Token)} and
     ** supplying a replacement value via {@link Token#value(String)}.
     **
     ** @param  token            the name of the token to be replaced e.g. for
     **                          ${date} -&gt; "date" would be the token, must
     **                          not be <code>null</code> or empty.
     ** @param  value            the static value that will be used when
     **                          replacing the token, must not
     **                          be <code>null</code> or empty.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace register(final String token, final String value) {
      assertNotEmpty(token);
      assertNotNull(value);
      this.register(new Token(token).value(value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: register  (Replace}
    /**
     ** Registers a {@link Token} that will be replaced by the given
     ** {@link Token.Value}.
     ** <br>
     ** Same as registering a token via {@link #register(Token)} and supplying a
     ** {@link Token.Value} via {@link Token#value(Value)}.
     **
     ** @param  token            the name of the token to be replaced e.g. for
     **                          ${date} -&gt; "date" would be the token, must
     **                          not be <code>null</code> or empty.
     ** @param  value            the {@link Token.Value} to use when replacing
     **                          the value, must not be <code>null</code> or
     **                          empty.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace register(final String token, final Token.Value value) {
		  assertNotEmpty(token);
		  assertNotNull(value);
      return this.register(new Token(token).value(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: register  (Replace}
    /**
     ** Registers a {@link Token} that needs to be replaced.
     ** <br>
     ** The {@link Token} must have a valid static value or {@link Token.Value}
     ** associated with it which was set via {@link Token#value(String)} or
     ** {@link Token#value(Value)}.
     **
     ** @param  token            the {@link Token}, must not
     **                          be <code>null</code>.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace register(final Token token) {
		  assertNotNull(token);
		  assertNotNull(token.value, "please specifiy a value or a generator for the token!");
      this.token.put(token.name, token);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: register  (Replace}
    /**
     ** Registers an array of replacements for a string based in indexed tokens.
     ** <br>
     ** the tokens will be replaced in the order they were added to the array.
     ** e.g.
     ** <pre>
     **   replacer.register(new String[] { "one", "two", "three" });
     **   replacer.substitute("{0} {1} {2}")); // will result in "one two three"
     ** </pre>
     **
     ** @param  replacements     the array of replacements that will be used
     **                          when replacing an indexed strings, must not be
     **                          <code>null</code> but can be empty.
     **
     ** @return                  the <code>Replace</code> to allow method
     **                          chaining.
     */
    @Override
    public final Replace register(final String[] replacements) {
      assertNotNull(replacements);
      int i = 0;
      for (String replacement : replacements) {
        this.register(new Token(String.valueOf(i)).value(replacement));
        i++;
      }
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: execute (Replace}
    /**
     ** Replaces all {@link Token} with one of the following:
     ** <ul>
     **   <li>the provided static values set via
     **       {@link #register(String, String)}
     **   <li>the token registered via {@link #register(Token)}
     **   <li>the vallue registered via {@link #register(String, Token.Value)}
     ** </ul>
     **
     ** @param  subject             the string that contains the tokens, will
     **                             be returned as-is in case of null or empty
     **                             string.
     **
     ** @return                     the result after replacing all tokens with the
     **                             proper values.
     **
     ** @throws RuntimeException    when the internal state is incorrect and
     **                             error reporting was turned on via
     **                             {@link #strict()}
     */
    @Override
    public final String execute(String subject) {
      // prevent bogus input
      if (subject == null) {
			  return null;
		  }

		        StringBuilder       token  = new StringBuilder();
		        StringBuilder       args   = new StringBuilder();
		  final StringBuilder       result = new StringBuilder();
		  final Map<String, String> cache  = new HashMap<String, String>();

  		State state = State.READING_INPUT;
	  	subject = subject + EOS;
      for (int i = 0; i < subject.length(); ++i) {
        char c = subject.charAt(i);
        switch (state) {
          case READING_INPUT  : if (defaultInput(c)) {
                                  state = State.READING_INPUT;
                                  result.append(c);
                                }
                                else if (c == EOS) {
                                  state = State.READING_INPUT;
                                }
                                else if (tokenStart(c)) {
                                  state = State.TOKEN_STARTED;
                                  token = new StringBuilder();
                                }
                                else {
                                  result.append(c);
                                }
                                break;
    			case TOKEN_STARTED      : if (defaultInput(c)) {
                                      state = State.READING_TOKEN;
                                      token.append(c);
                                    }
                                    else {
                                      state = State.ERROR;
                                     }
                                     break;
    			case READING_TOKEN      : if (defaultInput(c)) {
                                      state = State.READING_TOKEN;
                                      token.append(c);
                                    }
                                    else if (argumentStart(c)) {
                                      state = State.TOKEN_ARGS_STARTED;
                                      args = new StringBuilder();
                                    }
                                    else if (tokenEnd(c)) {
                                      state = State.READING_INPUT;
                                      result.append(eval(token, args, cache));
                                    }
                                    else {
                                      state = State.ERROR;
                                    }
                                    break;
    			case TOKEN_ARGS_STARTED : if (argumentEnd(c)) {
                                      state = State.TOKEN_ARGS_END;
                                    }
                                    else if (defaultInput(c)) {
                                      state = State.READING_TOKEN_ARGS;
                                      args.append(c);
                                    }
                                    else {
                                      state = State.ERROR;
                                    }
		    		                        break;
    			case READING_TOKEN_ARGS : if (argumentEnd(c)) {
                                      state = State.TOKEN_ARGS_END;
                                    }
                                    else if (defaultInput(c)) {
                                      state = State.READING_TOKEN_ARGS;
                                      args.append(c);
                                    }
                                    else {
                                      state = State.ERROR;
                                    }
		    		                        break;
			    case TOKEN_ARGS_END     : if (argumentEnd(c)) {
                                      state = State.TOKEN_ARGS_END;
                                    }
                                    else if (tokenEnd(c)) {
                                      state = State.READING_INPUT;
                                      result.append(eval(token, args, cache));
                                    }
                                    else {
                                      state = State.ERROR;
                                    }
                                    break;
    			case ERROR              :
		    	default                 : error();
			  }
		  }
      if (!finalState(state)) {
        error();
      }
      return result.toString();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: eval
    /**
     ** Returns the value for the token <code>token</code> from the specified
     ** <code>arguments</code>.
     **
     ** @param  name             the token the value is requested for
     ** @param  args             the possible values for token
     **                          <code>name</code>.
     ** @param  cache            the token values proceed so far if caching is
     **                          enabled
     **
     ** @return                  the string where the token is replaceced by the
     **                          equivalent
     */
    protected String eval(final StringBuilder name, final StringBuilder args, final Map<String, String> cache) {
      final String   token = name.toString();
      final String[] value = arguments(token, args);
      if (!this.token.containsKey(token)) {
        if (this.tolerant) {
          return guess(name, args);
        }
        else {
          throw new RuntimeException(String.format("no value or generator for token '%s' found!", token));
        }
      }
      return value(token, value, cache);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: arguments
    /**
     ** Obtains the argument from the source for token <code>name</code>
     **
     ** @param  name             the name of the token the specified argument
     **                          belongs to.
     ** @param  source           the argument for token <code>name</code>.
     **
     ** @return                  the array of possible arguments for token
     **                          <code>name</code>.
     */
    protected String[] arguments(final String name, final StringBuilder source) {
      if (source.length() == 0)
        return new String[]{};

      assertArguments(name, source);
      return source.toString().split(String.valueOf(this.argumentSep));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: assertArguments
    /**
     ** stuff like {dynamic(1,)} or {dynamic(1,2,)} seems to be invalid TODO do
     ** proper parsing instead of regex
     **
     ** @param  name             the name of the token the specified argument
     **                          belongs to.
     ** @param  argument         the argument for token <code>name</code>.
     */
    protected void assertArguments(final String name, final StringBuilder argument) {
      if (argument.length() > 0 && (argument.toString().matches("^,.*") || argument.toString().matches(".*,$"))) {
        throw new RuntimeException(String.format("The given arguments '%s' for token '%s' seems to be incorrect!", argument.toString(), name));
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    private String value(final String tokenName, final String[] args, final Map<String, String> cache) {
      if (this.caching && cache.containsKey(tokenName)) {
        return cache.get(tokenName);
      }
      final Token.Value tokenValue = this.token.get(tokenName).value();
      tokenValue.inject(args);
      String value = tokenValue.get();
      if (this.caching) {
        cache.put(tokenName, value);
      }
      return value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: guess
    private String guess(final StringBuilder token, final StringBuilder args) {
      if (args.length() > 0) {
        return this.tokenStart + token.toString() + this.argumentStart + args + this.argumentEnd + this.tokenEnd;
      }
      else {
        return this.tokenStart + token.toString() + this.tokenEnd;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: guess
    private void error() {
      throw new RuntimeException("Invalid input. The given String could not be parsed. Please check if all tokens, brackets etc. are correct.");
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // class Token
  // ~~~~~ ~~~~~~
  public class Token {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////
  
  	private final String name;
	  private Value        value;
	  private String       qualified;

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // interface Value
    // ~~~~~~~~~ ~~~~~
    public interface Value {

    ////////////////////////////////////////////////////////////////////////////
    // Method group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: get
    /**
     ** Generates string that will be used to replace a {@link Token}.
     **
     ** @return                  the generated String that will be used to
     **                          replace a {@link Token}.
     */
    String get();

    ////////////////////////////////////////////////////////////////////////////
    // Method: inject
    /**
     ** @param  args            to inject into the Value.
     **                         <br>
     **                         will be called before the call to
     **                         {@link #get()}, must not be <code>null</code>
     **                         but can be empty.
     */
    void inject(final String[] args);
  }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** @param  name             e.g. {amount} -&gt; 'amount' would be the
     **                          token, must not be <code>null</code> or empty.
     */
    public Token(final String name) {
      assertNotEmpty(name);
      this.name = name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
	  /**
     ** Retuns the token name.
     **
     ** @return                  the {@link Token} name.
  	 */
	  public String name() {
		  return this.name;
	  }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Retuns the <code>Value</code> associated with the {@link Token}.
     **
     ** @return                  the <code>Value</code> associated with the
     **                          {@link Token}, may be <code>null</code>.
     */
    public Value value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** @param  value            the <code>Value</code> to use when replacing
     **                          the token.
     **                          <br>
     **                          If there is only a static value (something
     **                          constant) than {@link #value(String)} can also
     **                          be used, must not be <code>null</code> or
     **                          empty.
     **
     ** @return                  the {@link Token} to allow method chaining.
     */
    public Token value(final Value value) {
      this.value = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
  	/**
     ** Sets the the static value to use for the token.
     ** <br>
     ** If you want to dynamically generate a value (and possibly supply
     ** arguments) then use {@link #value(Value)}.
     **
     ** @param  value            the static value to use for the token, must not
     **                          be <code>null</code>
     **
     ** @return                  the {@link Token} to allow method chaining.
     */
    public Token value(final String value) {
      assertNotNull(value);
      this.value = new Value() {
        @Override
        public String get() {
          return value;
        }
        @Override
        public void inject(final String[] args) {
          // intentionally left blank
        }
      };
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
    /**
     ** Returns the hash code value for this <code>Repository Object</code>.
     ** <p>
     ** This ensures that <code>s1.equals(s2)</code> implies that
     ** <code>s1.hashCode()==s2.hashCode()</code> for any two instances
     ** <code>s1</code> and <code>s2</code>, as required by the general contract
     ** of <code>Object.hashCode()</code>.
     **
     ** @return                  the hash code value for this
     **                          <code>Repository Object</code>.
     */
    @Override
    public int hashCode() {
      return 31 + ((this.name == null) ? 0 : this.name.hashCode());
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Compares the specified object with this nested set for equality.
     ** Returns <code>true</code> if the specified object is also a set, the two
     ** sets have the same size, and every element of the specified set is
     ** contained in this set.
     ** <p>
     ** This implementation first checks if the given object is a
     ** <code>Replace</code>. If so, the hash code values of this nested set
     ** and the specified <code>HashSetOfSets</code> are compared. Since the
     ** hash code values are being cached, this represents a quick solution if
     ** the sets aren't equal. However, if the hash code values are equal, it
     ** cannot be assumed that the sets themselves are equal, since different
     ** sets can have the same hash code value. In this case, the result of the
     ** super method <code>equals()</code> is returned.
     **
     ** @param  other            object to be compared for equality with this
     **                          nested set.
     **
     ** @return                  <code>true</code> if the specified object is
     **                          equal to this nested set, <code>false</code>
     **                          otherwise.
     */
    @Override
    public boolean equals(final Object other) {
      if (this == other)
        return true;
      if (other == null)
        return false;
      if (getClass() != other.getClass())
        return false;

      return StringUtility.isEqual(this.name, ((Token)other).name);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns a string representation of the object.
     ** <p>
     ** In general, the <code>toString</code> method returns a string that
     ** "textually represents" this object. The result is a concise but
     ** informative representation that is easy for a person to read.
     ** <p>
     ** The <code>toString</code> method for class <code>Accessor</code> returns
     ** a string consisting of the name of the class of which the object is an
     ** instance.
     **
     ** @return                   a string representation of the object.
     */
    @Override
	  public String toString() {
		  return String.format("Token: [qualified=%s, value=%s, name=%s]", this.qualified, this.value, this.name);
	  }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors and Mutators
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   strict
  /**
   ** Tells the <code>Replace</code> to report any tokens that can not be
   ** replaced.
   ** <br>
   ** If turned on an {@link IllegalStateException} will be thrown during token
   ** replacement.
   ** <br>
   ** Reporting errors is turned on by default.
   **
   ** @return                    the <code>Replace</code> to allow method
   **                            chaining.
   */
  Replace strict();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tolerant
  /**
   ** Tells the <code>Replace</code> to ignore any tokens that can not be
   ** replaced.
   ** <br>
   ** If turned OFF no Exceptions will be thrown during token replacement.
   ** <br>
   ** Reporting errors is turned on by default.
   **
   ** @return                    the <code>Replace</code> to allow method
   **                            chaining.
   */
  Replace tolerant();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tokenStart
  /**
   ** Sets the token start identifier to the given value e.g.
   ** [dynamic] -&gt; '[' would be the start identifier, e.g. '['.
   **
   ** @param  start              the token start identifier to the given value
   **                            e.g. [dynamic] -&gt; '[' would be the start
   **                            identifier, e.g. '[', must not be
   **                            <code>null</code> or empty.
   **
   ** @return                    the <code>Replace</code> to allow method
   **                            chaining.
   */
  Replace tokenStart(final String start);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tokenEnd
  /**
   ** Sets the token end identifier to the given value e.g. [dynamic] -&gt; ']'
   ** would be the end identifier, e.g. '['.
   **
   ** @param  end                the token end identifier to the given value
   **                            e.g. [dynamic] -&gt; ']' would be the end
   **                            identifier, e.g. '[', must not be
   **                            <code>null</code> or empty.
   **
   ** @return                    the <code>Replace</code> to allow method
   **                            chaining.
   */
  Replace tokenEnd(final String end);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   argumentStart
  /**
   ** Sets the argument start identifier to the given value e.g.
   ** {dynamic[1;2;3]} -&gt; '[' would be the delimiter e.g. '['.
   **
   ** @param start               the argument start identifier to the given
   **                            value e.g. {dynamic[1;2;3]} -&gt; '[' would be
   **                            the delimiter e.g. '[', must not be
   **                            <code>null</code> or empty.
   **
   ** @return                    the <code>Replace</code> to allow method
   **                            chaining.
   */
  Replace argumentStart(final String start);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   argumentEnd
  /**
   ** Sets the argument end identifier to the given value e.g.
   ** {dynamic[1;2;3]} -&gt; ']' would be the delimiter e.g. ']'.
   **
   ** @param  end                the argument end identifier to the given
   **                            value e.g. {dynamic[1;2;3]} -&gt; ']' would be
   **                            the delimiter e.g. ']', must not be
   **                            <code>null</code> or empty.
   **
   ** @return                    the <code>Replace</code> to allow method
   **                            chaining.
   */
  Replace argumentEnd(final String end);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   argumentSeparator
  /**
   ** Changes the separator of the arguments to the given value e.g.
   ** {dynamic(1;2;3)} -&gt; ';' would be the delimiter.
   **
   ** @param  separator         changes the delimiter of the arguments to the
   **                           given value e.g. {dynamic(1;2;3)} -&gt; ';'
   **                           would be the delimiter, must not be
   **                           <code>null</code> or empty.
   **
   ** @return                    the <code>Replace</code> to allow method
   **                            chaining.
   */
  Replace argumentSeparator(final String separator);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enableValueCaching
  /**
   ** Turns value caching ON.
   ** <br>
   ** Once a value is determined through a {@link Token.Value} all remaining
   ** values with the same token name will be replaced by the cached version.
   ** <p>
   ** Use {@link #disableValueCaching()} to turn caching off.
   **
   ** @return                    the <code>Replace</code> to allow method
   **                            chaining.
   */
  Replace enableValueCaching();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disableValueCaching
  /**
   ** Turns value caching OFF.
   ** <p>
   ** Use {@link #enableValueCaching()} to turn caching on.
   **
   ** @return                    the <code>Replace</code> to allow method
   **                            chaining.
   */
  Replace disableValueCaching();

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Registers a static value for a given token.
   ** <br>
   ** If dynamic behaviour is required then {@link #register(Token)} can be
   ** used instead.
   ** <br>
   ** Is the same as registering a token via {@link #register(Token)} and
   ** supplying a replacement value via {@link Token#value(String)}.
   **
   ** @param  token              the name of the token to be replaced e.g. for
   **                            ${date} -&gt; "date" would be the token, must
   **                            not be <code>null</code> or empty.
   ** @param  value              the static value that will be used when
   **                            replacing the token, must not
   **                            be <code>null</code> or empty.
   **
   ** @return                    the <code>Replace</code> to allow method
   **                            chaining.
   */
  Replace register(final String token, final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Registers a {@link Token} that will be replaced by the given
   ** {@link Token.Value}.
   ** <br>
   ** Same as registering a token via {@link #register(Token)} and supplying a
   ** {@link Token.Value} via {@link Token#value(Value)}.
   **
   ** @param  token              the name of the token to be replaced e.g. for
   **                            ${date} -&gt; "date" would be the token, must
   **                            not be <code>null</code> or empty.
   ** @param  value              the {@link Token.Value} to use when replacing
   **                            the value, must not be <code>null</code> or
   **                            empty.
   **
   ** @return                    the <code>Replace</code> to allow method
   **                            chaining.
   */
  Replace register(final String token, final Token.Value value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Registers a {@link Token} that needs to be replaced.
   ** <br>
   ** The {@link Token} must have a valid static value or {@link Token.Value}
   ** associated with it which was set via {@link Token#value(String)} or
   ** {@link Token#value(Value)}.
   **
   ** @param  token              the {@link Token}, must not
   **                            be <code>null</code>.
   **
   ** @return                    the <code>Replace</code> to allow method
   **                            chaining.
   */
  Replace register(final Token token);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Registers an array of replacements for a string based in indexed tokens.
   ** <br>
   ** The tokens will be replaced in the order they were added to the array.
   ** e.g.
   ** <pre>
   **   replacer.register(new String[] { "one", "two", "three" });
   **   replacer.substitute("{0} {1} {2}")); // will result in "one two three"
   ** </pre>
   **
   ** @param  replacements       the array of replacements that will be used
   **                            when replacing an indexed strings, must not be
   **                            <code>null</code> but can be empty.
   **
   ** @return                    the <code>Replace</code> to allow method
   **                            chaining.
   */
  Replace register(final String[] replacements);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Replaces all {@link Token} with one of the following:
   ** <ul>
   **   <li>the provided static values set via {@link #register(String, String)}
   **   <li>the token registered via {@link #register(Token)}
   **   <li>the vallue registered via {@link #register(String, Token.Value)}
   ** </ul>
   **
   ** @param  subject             the string that contains the tokens, will
   **                             be returned as-is in case of null or empty
   **                             string.
   **
   ** @return                     the result after replacing all tokens with the
   **                             proper values.
   **
   ** @throws RuntimeException    when the internal state is incorrect and error
   **                             reporting was turned on via
   **                             {@link #strict()}
   */
  String execute(final String subject);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertNotEmpty
	static void assertNotEmpty(final String string) {
		assertNotEmpty(string, "the provided string is empty!");
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:  assertNotEmpty
  static void assertNotEmpty(final String string, final String message) {
		if (StringUtility.isEmpty(string)) {
			throw new IllegalArgumentException(message);
		}
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:  assertNotNull
	static void assertNotNull(final Object o) {
		assertNotNull(o, "the object was null!");
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:  assertNotNull
	static void assertNotNull(final Object o, final String message) {
		if (o == null) {
			throw new IllegalArgumentException(message);
		}
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  assertOneChar
	static void assertOneChar(String character) {
    if (character.length() != 1) {
      throw new IllegalArgumentException(String.format("the given string '%s' must be exactly of size 1", character));
		}
	}
}