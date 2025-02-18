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

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Phrase Generator

    File        :   Generator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Generator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.phrase;

import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

import java.security.SecureRandom;

////////////////////////////////////////////////////////////////////////////////
// class Generator
// ~~~~~ ~~~~~~~~~
/**
 ** Generates a random String using a cryptographically secure random number
 ** generator.
 ** <p>
 ** The alphabet (characters used in the passwords generated) may be specified,
 ** and the random number generator can be externally supplied.
 ** <p>
 ** Care should be taken when using methods that limit the types of passwords
 ** may be generated. Using an alphabet that is too small, using passwords that
 ** are too short, requiring too many of a certain type of character, or not
 ** allowing repetition, may decrease security.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class Generator {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the mapping key for the alphabet internally defined as the default
   ** (same as Non-Confusing)
   */
  public static final String DEFAULT                       = "Default";

  /**
   ** the mapping key for the alphabet consisting of all the printable ASCII
   ** symbols.
   */
  public static final String SYMBOLS                       = "Symbols";

  /**
   ** the mapping key for the alphabet consisting of upper and lowercase letters
   ** A-Z.
   */
  public static final String LETTERS                       = "Letters";

  /**
   ** the mapping key for the alphabet consisting of numeric digits ** A-Z.
   */
  public static final String DIGITS                        = "Digits";

  /**
   ** the mapping key for the alphabet consisting of the upper letters A-Z.
   */
  public static final String UPPERCASE_LETTERS             = "Uppercase Letters";

  /**
   ** the mapping key for the alphabet consisting of the upper letters A-Z and
   ** the digits 0-9.
   */
  public static final String UPPERCASE_LETTERS_AND_NUMBERS = "Uppercase Letters and Numbers";

  /**
   ** the mapping key for the alphabet consisting of the lowercase letters A-Z.
   */
  public static final String LOWERCASE_LETTERS             = "Lowercase Letters";

  /**
   ** the mapping key for the alphabet consisting of the lowercase letters A-Z
   ** and the digits 0-9.
   */
  public static final String LOWERCASE_LETTERS_AND_NUMBERS = "Lowercase Letters and Numbers";

  /**
   ** the mapping key for the alphabet consisting of upper and lowercase letters
   ** A-Z and the digits 0-9.
   */
  public static final String LETTERS_AND_NUMBERS           = "Letters and Numbers";

  /**
   ** the mapping key for the alphabet consisting of all the printable ASCII
   ** characters.
   */
  public static final String PRINTABLE                     = "Printable";

  /**
   ** the mapping key for the alphabet consisting of upper and lowercase letters
   ** A-Z and the digits 0-9 but with characters that are often mistaken for
   ** each other when typed removed.
   ** (I,L,O,U,V,i,l,o,u,v,0,1)
   */
  public static final String NON_CONFUSING                 = "Non-Confusing";

  /** Alphabet consisting of all the printable ASCII symbols. */
  public static final char[] SYMBOLS_ALPHABET = {
    '!','\"','#','$','%','&','\'','(',')','*','+',',','-','.','/',':',';','<'
  , '?','@','[','\\',']','^','_','`','{','|','}','~'
  };

  /** Alphabet consisting of the upper letters A-Z and the digits 0-9. */
  public static final char[] DIGITS_ALPHABET = {
    '0','1','2','3','4','5','6','7','8','9'
  };

  /** Alphabet consisting of upper and lowercase letters A-Z. */
  public static final char[] LETTERS_ALPHABET = {
    'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S'
  , 'T','U','V','W','X','Y','Z'
  , 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s'
  , 't','u','v','w','x','y','z'
  };

  /** Alphabet consisting of the upper letters A-Z. */
  public static final char[] UPPERCASE_LETTERS_ALPHABET = {
    'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S'
  , 'T','U','V','W','X','Y','Z'
  };

  /** Alphabet consisting of the upper letters A-Z and the digits 0-9. */
  public static final char[] UPPERCASE_LETTERS_AND_NUMBERS_ALPHABET = {
    'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S'
  , 'T','U','V','W','X','Y','Z'
  , '0','1','2','3','4','5','6','7','8','9'
  };

  /** Alphabet consisting of the lowercase letters A-Z. */
  public static final char[] LOWERCASE_LETTERS_ALPHABET = {
    'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s'
  , 't','u','v','w','x','y','z'
  };

  /** Alphabet consisting of upper and lowercase letters A-Z and the digits 0-9. */
  public static final char[] LETTERS_AND_NUMBERS_ALPHABET = {
    'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S'
  , 'T','U','V','W','X','Y','Z'
  , 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s'
  , 't','u','v','w','x','y','z'
  , '0','1','2','3','4','5','6','7','8','9'
  };

  /** Alphabet consisting of the lowercase letters A-Z and the digits 0-9. */
  public static final char[] LOWERCASE_LETTERS_AND_NUMBERS_ALPHABET = {
    'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s'
  , 't','u','v','w','x','y','z'
  , '0','1','2','3','4','5','6','7','8','9'
  };

  /** Alphabet consisting of all the printable ASCII characters. */
  public static final char[] PRINTABLE_ALPHABET   = {
    '!','\"','#','$','%','&','\'','(',')','*','+',',','-','.','/'
   , '0','1','2','3','4','5','6','7','8','9',':',';','<','?','@'
   , 'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S'
   , 'T','U','V','W','X','Y','Z'
   , '[','\\',']','^','_','`'
   , 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s'
   , 't','u','v','w','x','y','z'
   , '{','|','}','~'
  };

  /**
   ** Alphabet consisting of upper and lowercase letters A-Z and the digits 0-9
   ** but with characters that are often mistaken for each other when typed
   ** removed. (I,L,O,U,V,i,l,o,u,v,0,1)
   */
  public static final char[] NONCONFUSING_ALPHABET  = {
    'A','B','C','D','E','F','G','H','J','K','M','N','P','Q','R','S','T','W','X','Y','Z'
   ,'a','b','c','d','e','f','g','h','j','k','m','n','p','q','r','s','t','w','x','y','z'
   ,'2','3','4','5','6','7','8','9'
  };

  /** Default length for passwords */
  public static final int   DEFAULT_PHRASE_LENGTH   = 8;

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the map to resolve aplhabet names */
  private static Map<String, char[]> alphabets               = new HashMap<String, char[]>();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** One less than the maximum number of repeated characters that are allowed
   ** in a password.
   ** Set to -1 to disable this feature.
   */
  protected int                      repetition              = -1;

  /** Random number generator used. */
  protected SecureRandom             secureRandom;

  /**
   ** Set of characters which may be used in the generated passwords.
   ** <p>
   ** This value may not be null or have no elements.
   */
  protected char[]                   alphabet;

  /**
   ** Set of characters which may be used for the first character in the
   ** generated passwords.
   ** <p>
   ** This value may be null but it must have at least one element otherwise.
   */
  protected char[]                   firstAlphabet;

  /**
   ** Set of characters which may be used for the last character in the
   ** generated passwords.
   ** <p>
   ** This value may be null but it must have at least one element otherwise.
   */
  protected char[]                   lastAlphabet;

  private boolean[]                  touched                 = null;
  private int[]                      available               = null;
  private Vector<Requirement>        requirements            = null;
  private Vector<Verifier>           verifiers               = null;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    alphabets.put(DEFAULT,                       NONCONFUSING_ALPHABET);
    alphabets.put(LETTERS,                       LETTERS_ALPHABET);
    alphabets.put(DIGITS,                        DIGITS_ALPHABET);
    alphabets.put(LETTERS_AND_NUMBERS,           LETTERS_AND_NUMBERS_ALPHABET);
    alphabets.put(UPPERCASE_LETTERS,             UPPERCASE_LETTERS_ALPHABET);
    alphabets.put(UPPERCASE_LETTERS_AND_NUMBERS, UPPERCASE_LETTERS_AND_NUMBERS_ALPHABET);
    alphabets.put(LOWERCASE_LETTERS,             LOWERCASE_LETTERS_ALPHABET);
    alphabets.put(LOWERCASE_LETTERS_AND_NUMBERS, LOWERCASE_LETTERS_AND_NUMBERS_ALPHABET);
    alphabets.put(PRINTABLE,                     PRINTABLE_ALPHABET);
    alphabets.put(NON_CONFUSING,                 NONCONFUSING_ALPHABET);
    alphabets.put(SYMBOLS,                       SYMBOLS_ALPHABET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new random password generator with the default secure random
   ** number generator and default NONCONFUSING alphabet for all characters.
   */
  public Generator() {
    this(new SecureRandom(), NONCONFUSING_ALPHABET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new random password generator with the default secure random
   ** number generator and given alphabet for all characters.
   **
   ** @param  alphabet           the mapping key for the desired alphabet.
   */
  public Generator(String alphabet) {
    this(new SecureRandom(), (char[])Generator.alphabets.get(alphabet));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new random password generator with the default secure random
   ** number generator and given alphabet for all characters.
   **
   ** @param  alphabet           Characters allowed in generated passwords.
   */
  public Generator(char[] alphabet){
    this(new SecureRandom(), alphabet);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new random password generator with the given secure random number
   ** generator and default NONCONFUSING alphabet for all characters.
   **
   ** @param  secureRandom       Secure random number generator to use when
   **                            generating passwords.
   **
   */
  public Generator(SecureRandom secureRandom){
    this(secureRandom, NONCONFUSING_ALPHABET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new random password generator with the given secure random number
   ** generator and default NONCONFUSING alphabet for all characters.
   **
   ** @param  secureRandom       Secure random number generator to use when
   **                            generating passwords.
   ** @param  alphabet           the mapping key for the desired alphabet.
   **
   */
  public Generator(SecureRandom secureRandom, String alphabet){
    this(secureRandom, (char[])Generator.alphabets.get(alphabet));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new random password generator with the given secure random number
   ** generator and given alphabet for all characters.
   **
   ** @param  secureRandom       Secure random number generator to use when
   **                            generating passwords.
   ** @param  alphabet           Characters allowed in generated passwords.
   */
  public Generator(SecureRandom secureRandom, char[] alphabet) {
    this.setRandomGenerator(secureRandom);
    this.setAlphabet(alphabet);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors and Mutators
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addRequirement
  /**
   ** Require that a certain number of characters from an alphabet be present in
   ** generated passwords.
   **
   ** @param  alphabet           set of letters that must be present
   ** @param  num                number of letters from the alphabet that must
   **                            be present.
   */
  public void addRequirement(String alphabet, int num) {
    addRequirement(alphabet.toCharArray(), num);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addRequirement
  /**
   ** Require that a certain number of characters from an alphabet be present in
   ** generated passwords.
   **
   ** @param  alphabet           set of letters that must be present
   ** @param  num                number of letters from the alphabet that must
   **                            be present.
   */
  public void addRequirement(final char[] alphabet, final int num){
    if (requirements == null)
      requirements = new Vector<Requirement>();

    requirements.add(new Requirement(alphabet, num));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addVerifier
  /**
   ** Add a class that will verify passwords. No password will be returned
   ** unless all verifiers approve of it.
   **
   ** @param  verifier           class that performs verification of password.
   */
  public void addVerifier(final Verifier verifier){
    if (verifiers == null)
      verifiers = new Vector<Verifier>();

    verifiers.add(verifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaxRepetition
  /**
   ** Set the maximum number of characters that may appear in sequence more than
   ** once in a password. The alphabet must be large enough to handle this
   * option.  If your alphabet is {'a', 'b'} and you want 8 character passwords
   * in which no character appears twice (repetition 1) you are out of luck.
   * In such instances your request for no repetition will be ignored.
   * <p>
   * For example setRepetition(3) will allow a password ababab but not allow
   * abcabc.
   * <p>
   * Using this method can greatly reduce the pool of passwords that are generated.
   * For example if only one repetition is allowed then the pool of passwords
   * is the permutation of the alphabet rather than the combination.
   *
   ** @param  repetition         maximum character repetition.
   */
  public void setMaxRepetition(int repetition){
    this.repetition = repetition - 1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAlphabet
  /**
   ** Set the alphabet used by this random password generator.
   **
   ** @param  alphabet           the mapping key for the Characters allowed in
   **                            generated passwords.
   **
   ** @throws NullPointerException           if the alphabet is null.
   ** @throws ArrayIndexOutOfBoundsException if the alphabet has no elements.
   */
  public void setAlphabet(String alphabet) {
    setAlphabet((char[])Generator.alphabets.get(alphabet));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAlphabet
  /**
   ** Set the alphabet used by this random password generator.
   **
   ** @param  alphabet           Characters allowed in generated passwords.
   **
   ** @throws NullPointerException           if the alphabet is null.
   ** @throws ArrayIndexOutOfBoundsException if the alphabet has no elements.
   */
  public void setAlphabet(char[] alphabet){
    if (alphabet == null)
      throw new NullPointerException("Null alphabet");

    if (alphabet.length == 0)
      throw new ArrayIndexOutOfBoundsException("No characters in alphabet");

    this.alphabet = alphabet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRandomGenerator
  /**
   ** Set the random number generator used by this random password generator.
   **
   ** @param  secureRandom       Secure random number generator to use when
   **                            generating passwords.
   */
  public void setRandomGenerator(SecureRandom secureRandom){
    if (secureRandom == null)
      throw new NullPointerException("Null secureRandom");

    this.secureRandom = secureRandom;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFirstAlphabet
  /**
   ** Set the alphabet used by this random password generator for the first
   ** character of passwords.
   ** <p>
   ** If the alphabet for the first character is set to null or has no elements,
   ** the main alphabet will be used for the first character.
   **
   ** @param  alphabet           the mapping key for the Characters allowed in
   **                            generated passwords.
   */
  public void setFirstAlphabet(String alphabet){
    setFirstAlphabet((char[])Generator.alphabets.get(alphabet));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFirstAlphabet
  /**
   ** Set the alphabet used by this random password generator for the first
   ** character of passwords.
   ** <p>
   ** If the alphabet for the first character is set to null or has no elements,
   ** the main alphabet will be used for the first character.
   **
   ** @param  alphabet           Characters allowed for the first character of
   **                            the passwords.
   */
  public void setFirstAlphabet(char[] alphabet){
    this.firstAlphabet = (alphabet == null || alphabet.length == 0) ? null : alphabet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLastAlphabet
  /**
   ** Set the alphabet used by this random password generator for the last
   ** character of passwords.
   ** <p>
   ** If the alphabet for the last character is set to null or has no elements,
   ** the main alphabet will be used for the last character.
   **
   ** @param  alphabet           the mapping key for the Characters allowed in
   **                            generated passwords.
   */
  public void setLastAlphabet(String alphabet){
    setLastAlphabet((char[])Generator.alphabets.get(alphabet));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLastAlphabet
  /**
   ** Set the alphabet used by this random password generator for the last
   ** character of passwords.
   ** <p>
   ** If the alphabet for the last character is set to null or has no elements,
   ** the main alphabet will be used for the last character.
   **
   ** @param  alphabet           Characters allowed for the last character of
   **                            the passwords.
   */
  public void setLastAlphabet(char[] alphabet){
   this.lastAlphabet = (alphabet == null || alphabet.length == 0) ? null : alphabet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   phraseString
  /**
   ** Generate a random phrase of the default length (8).
   ** <p>
   ** <strong>NOTE:</strong>
   ** <br>
   ** Strings can not be modified. If it is possible for a hacker to examine
   ** memory to find phrases, {@link #phraseCharacter()} should be used so
   ** that the phrase can be zeroed out of memory when no longer in use.
   **
   ** @return                    a random password
   **
   ** @see #phraseCharacter()
   */
  public String phraseString(){
    return passwordString(DEFAULT_PHRASE_LENGTH);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   phraseString
  /**
   ** Generate a random password of the given length.
   ** <p>
   ** <strong>NOTE:</strong>
   ** <br>
   ** Strings can not be modified. If it is possible for a hacker to examine
   ** memory to find phrases, {@link #phraseCharacter()} should be used so
   ** that the phrase can be zeroed out of memory when no longer in use.
   **
   ** @param  length             the desired length of the generated phrase.
   **
   ** @return                    a random phrase
   **
   ** @see #phraseCharacter()
   */
  public String passwordString(int length){
    String password = new String(phraseCharacter(new char[length]));
    return password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   phraseCharacter
  /**
   ** Generate a random phrase of the default length (8).
   ** <p>
   ** <strong>NOTE:</strong>
   ** <br>
   ** If it is possible for a hacker to examine memory to find phrases, the
   ** phrase should be overwritten in memory as soon as possible after it is
   ** no longer in use.
   **
   ** @return                    a random password
   */
  public char[] phraseCharacter() {
    return (phraseCharacter(DEFAULT_PHRASE_LENGTH));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   phraseCharacter
  /**
   ** Generate a random phrase of the given length.
   ** <p>
   ** <strong>NOTE:</strong>
   ** <br>
   ** If it is possible for a hacker to examine memory to find phrases, the
   ** phrase should be overwritten in memory as soon as possible after it is
   ** no longer in use.
   **
   ** @param  length             the desired length of the generated phrase.
   **
   ** @return                    a random phrase
   */
  public char[] phraseCharacter(int length) {
    return (phraseCharacter(new char[length]));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   phraseCharacter
  /**
   ** Fill the given buffer with random characters.
   ** <p>
   ** Using this method, the phrase character array can easily be reused for
   ** efficiency, or overwritten with new random characters for security.
   ** <strong>NOTE:</strong>
   ** <br>
   ** If it is possible for a hacker to examine memory to find phrases, the
   ** phrase should be overwritten in memory as soon as possible after it is
   ** no longer in use.
   **
   ** @param phrase              buffer that will hold the phrase.
   **
   ** @return                    the buffer, filled with random characters.
   */
  public char[] phraseCharacter(char[] phrase) {
    boolean verified = false;
    while (!verified) {
      int length = phrase.length;
      for (int i = 0; i < length; i++) {
        char[] useAlph = alphabet;
        if (i == 0 && firstAlphabet != null)
          useAlph = firstAlphabet;
        else if (i == length - 1 && lastAlphabet != null)
          useAlph = lastAlphabet;

        int size    = avoidRepetition(useAlph, phrase, i, repetition, useAlph.length);
        phrase[i] = useAlph[this.secureRandom.nextInt(size)];
      }
      if (requirements != null)
        applyRequirements(phrase);

      verified = true;
      for (int i = 0; verified && verifiers != null && i < verifiers.size(); i++)
        verified = ((Verifier)verifiers.elementAt(i)).verify(phrase);
    }
    return phrase;
  }

  private void applyRequirements(char[] phrase) {
    int size = requirements.size();
    if (size > 0) {
      int length = phrase.length;
      if (touched == null || touched.length < length)
        touched = new boolean[length];
      if (available == null || available.length < length)
        available = new int[length];
      for (int i = 0; i < length; i++) {
        touched[i] = false;
      }
      for (int reqNum = 0; reqNum < size; reqNum++) {
        Requirement req = (Requirement)requirements.elementAt(reqNum);
        // set the portion of this alphabet available for use.
        int reqUsedInd = req.alphabet.length;
        // figure out how much of this requirement is already fulfilled
        // and what is available to fulfill the rest of it.
        int fufilledInd = 0;
        int availableInd = 0;
        for (int i = 0; i < length; i++) {
          if (arrayContains(req.alphabet, phrase[i]) && fufilledInd < req.num) {
            fufilledInd++;
            touched[i] = true;
            if (repetition >= 0) {
              // move already used characters so they can'
              // be used again.  This prevents repetition.
              reqUsedInd -= moveTo(req.alphabet, reqUsedInd, phrase[i]);
              // allow repetition if we have no other choice
              if (reqUsedInd < 0)
                reqUsedInd = req.alphabet.length;
            }
          }
          else if (!touched[i]) {
            available[availableInd] = i;
            availableInd++;
          }
        }
        // fulfill the requirement
        int toDo = req.num - fufilledInd;
        for (int i = 0; i < toDo && availableInd > 0; i++) {
          // pick a random available slot
          // and a random member of the available alphabet
          int slot = this.secureRandom.nextInt(availableInd);
          char phraseChar = req.alphabet[this.secureRandom.nextInt(reqUsedInd)];
          phrase[available[slot]] = phraseChar;
          touched[available[slot]]  = true;
          // make the slot no longer available
          availableInd--;
          available[slot] = available[availableInd];
          if (repetition >= 0) {
            // move already used characters so they can'
            // be used again.  This prevents repetition.
            reqUsedInd -= moveTo(req.alphabet, reqUsedInd, phraseChar);
            // allow repetition if we have no other choice
            if (reqUsedInd < 0)
              reqUsedInd = req.alphabet.length;
          }
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   avoidRepetition
  /**
   ** Avoid repetition (if possible) by moving all characters that would cause
   ** repetition to the end of the alphabet and returning the size of the
   ** alphabet that may be used.
   */
  private int avoidRepetition(char[] alph, char[] pass, int passSize, int repetition, int alphSize){
    if (repetition > -1) {
      // limit the alphabet to those characters that
      // will not cause repeating sequences
      int repPos = 0;
      while ((repPos = findRepetition(pass, repPos, passSize, repetition)) != -1) {
        // shuffle characters that would cause repetition
        // to the end of the alphabet and adjust the size
        // so that they will not be used.
        alphSize -= moveTo(alph, alphSize, pass[repPos + repetition]);
        repPos++;
      }
      if (alphSize == 0)
        alphSize = alph.length;
    }
    return alphSize;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findRepetition
  /**
   ** Find a repetition of the desired length. The characters to search for are
   ** located at pass[end-length] to pass[end]
   */
  private int findRepetition(char[] pass, int start, int end, int length) {
    for (int i = start; i < end - length; i++) {
      boolean onTrack = true;
      for (int j = 0; onTrack && j < length; j++) {
        if (pass[i + j] != pass[end - length + j])
          onTrack = false;
      }
      if (onTrack)
        return i;
    }
    return -1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   moveTo
  /**
   ** Move all of the given character to the end of the array and return the
   ** number of characters moved.
   */
  private static int moveTo(char[] alph, int numGood, char c) {
    int count = 0;
    for (int i = 0; i < numGood; i++) {
      if (alph[i] == c) {
        numGood--;
        char temp = alph[numGood];
        alph[numGood] = alph[i];
        alph[i] = temp;
        count++;
      }
    }
    return count;
  }

  private boolean arrayContains(char[] alph, char c) {
    for (int i = 0; i < alph.length; i++) {
      if (alph[i] == c)
        return true;
    }
    return false;
  }
}