package oracle.hst.foundation;

import java.io.IOException;

////////////////////////////////////////////////////////////////////////////////
// class SystemDialog
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** A simple utility class to handle user interaction.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface SystemDialog {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Prompt
  // ~~~~~ ~~~~~~
  public static class Prompt implements SystemDialog {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Prompt</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Prompt() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: confirm (SystemDialog)
    /**
     ** Presents a confirmation prompt with the given message.
     **
     ** @param  prompt           the confirmation prompt message to display.
     **
     ** @return                  <code>true</code> if the user has selected a
     **                          positive confirmation; otherwise
     **                          <code>false</code>
     */
    @Override
    public boolean confirm(final String prompt) {
      try {
        while (true) {
          System.out.println();
          System.out.print(prompt);
          System.out.print("(y|n|?) : ");

          int c = read();
          System.out.println();
          // check c
          switch (c) {
            case 'y' : return true;
            case 'n' : return false;
            case '?' : System.out.println("y = yes, n = no");
                       break;
            default  : System.out.print("Invalid input, expecting ");
                       System.out.println("'y', 'n', or '?'.");
                        break;
          }
        }
      }
      catch (IOException e) {
        System.out.println(e);
      }
      return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: confirm (SystemDialog)
    /**
     ** Presents a confirmation prompt for values with the given messge.
     **
     ** @param  prompt           the confirmation prompt message to display.
     ** @param  values           a list of valid characters to accept.
     **
     ** @return                  whatever character the user presses.
     */
    @Override
    public char confirm(final String prompt, final String values) {
      return confirm(prompt, values, "no help available...");
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: confirm (SystemDialog)
    /**
     ** Presents a confirmation prompt for values with the given messge.
     **
     ** @param  prompt           the confirmation prompt message to display.
     ** @param  values           a list of valid characters to accept.
     ** @param  help             a help message when the user presses '?'
     **
     ** @return                  whatever character the user presses.
     */
    @Override
    public char confirm(final String prompt, final String values, final String help) {
      final String message = prompt + makeOption(values);
      try {
        while (true) {
          System.out.println();
          System.out.print(message);
          int c = read();
          System.out.println();

          // check ch
          if (values.indexOf(c) != -1)
            return (char)c;

          if (c == '?')
            notification(help);
          else {
            notification("Invalid input, expecting ");
            notification(listOption(values));
          }
        }
      }
      catch (IOException e) {
        System.out.println(e);
      }
      return '\0';
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: notification (SystemDialog)
    /**
     ** Displays the given message to the user. No input is returned from the
     ** user.
     **
     ** @param  message          the message to display to the user.
     */
    @Override
    public void notification(final String message) {
      System.out.println(message);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   confirmNotification
    /**
     ** Presents a confirmation prompt with the given message.
     **
     ** @param  message          the confirmation prompt message to display.
     ** @param  prompt           the confirmation prompt message to display.
     **
     ** @return                  <code>true</code> if the user has selected a
     **                          positive confirmation; otherwise
     **                          <code>false</code>
     */
    public static boolean confirmNotification(final String message, final String prompt) {
      final Prompt dialog = new Prompt();
      dialog.notification(message);
      return dialog.confirm(prompt);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   confirmNotification
    /**
     ** Presents a confirmation prompt with the given message.
     **
     ** @param  message          the confirmation prompt message to display.
     ** @param  prompt           the confirmation prompt message to display.
     ** @param  values           a list of valid characters to accept.
     **
     ** @return                  whatever character the user presses.
     */
    public static char confirmNotification(final String message, final String prompt, final String values) {
      final Prompt dialog = new Prompt();
      dialog.notification(message);
      return dialog.confirm(prompt, values);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   confirmNotification
    /**
     ** Presents a confirmation prompt with the given message.
     **
     ** @param  message          the confirmation prompt message to display.
     ** @param  prompt           the confirmation prompt message to display.
     ** @param  values           a list of valid characters to accept.
     ** @param  help             a help message when the user presses '?'
     **
     ** @return                  whatever character the user presses.
     */
    public static char confirmNotification(final String message, final String prompt, final String values, final String help) {
      final Prompt dialog = new Prompt();
      dialog.notification(message);
      return dialog.confirm(prompt, values, help);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: read
    /**
     ** Returns a single char from System.in.
     **
     ** @return                  the character entered, or null if more than one
     **                          was entered (not including EOLs)
     **
     ** @throws IOException      if one is encountered in System.in.read()
     */
    private int read()
      throws IOException {

      int c = System.in.read();
      // read eoln, or extra characters
      while (System.in.available() > 0) {
        switch (System.in.read()) {
          case '\n' :
          case '\r' : break;
          default   : c = '\0';
        }
      }
      return c;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: makeOption
    /**
     ** Converts a list of characters into a delimited option prompt.
     ** <br>
     ** A question mark <code>'?'<code> is automatically appended.
     **
     ** @param  values           a list of valid characters to accept.
     **
     ** @return                  each character separated by a pipe and in
     **                          parenthesis,
     */
    private String makeOption(final String values) {
      final StringBuilder builder = new StringBuilder(values.length() * 2);
      builder.append(" (");
      for (int i = 0; i < values.length(); i++)
        builder.append(values.charAt(i)).append('|');
      builder.append("?) ");
      return builder.toString();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: listOption
    /**
     ** Creates a list of valid input options to give a better explanation to
     ** the user.
     **
     ** @param  values           a list of valid characters to accept.
     **
     ** @return                  each character in single quotes, comma
     **                          separated
     */
    private String listOption(final String values) {
      final StringBuilder builder = new StringBuilder(values.length() * 4);
      for (int i = 0; i < values.length(); i++)
        builder.append('\'').append(values.charAt(i)).append("', ");
      builder.append("or '?'");
      return builder.toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   confirm
  /**
   ** Presents a confirmation prompt with the given message.
   **
   ** @param  message            the confirmation prompt message to display.
   **
   ** @return                    <code>true</code> if the user has selected a
   **                            positive confirmation; otherwise
   **                            <code>false</code>
   */
  boolean confirm(final String message);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   confirm
  /**
   ** Presents a confirmation prompt for values with the given messge.
   **
   ** @param  prompt             the confirmation prompt message to display.
   ** @param  values             a list of valid characters to accept.
   **
   ** @return                    whatever character the user presses.
   */
  char confirm(final String prompt, final String values);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   confirm
  /**
   ** Presents a confirmation prompt for values with the given messge.
   **
   ** @param  prompt             the confirmation prompt message to display.
   ** @param  values             a list of valid characters to accept.
   ** @param  help               a help message when the user presses '?'
   **
   ** @return                    whatever character the user presses.
   */
  char confirm(final String prompt, final String values, final String help);

  ////////////////////////////////////////////////////////////////////////////
  // Method:   notification
  /**
   ** Displays the given message to the user. No input is returned from the
   ** user.
   **
   ** @param  message            the message to display to the user.
   */
  void notification(final String message);
}