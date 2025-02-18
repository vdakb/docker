-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppAplication\src\main\dbs\qms\api\hil-msg.pkb
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

PROMPT Creating Package Implementation 'hil$message'
CREATE or REPLACE PACKAGE BODY hil$message
AS
  -- ---------------------------------------------------------------------------
  -- private constants
  -- ---------------------------------------------------------------------------
  revision_label CONSTANT VARCHAR2(8) := '1.0.0.0';

  -- ---------------------------------------------------------------------------
  -- public program units
  -- ---------------------------------------------------------------------------

  --
  -- Purpose  returns the revision label of this package
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION revision
    RETURN VARCHAR2
  IS
  BEGIN
    RETURN revision_label;
  END revision;

  --
  -- Purpose  Initialise stack with error that may not be displayed by retrieving
  --          the records with suppress_always set to 'Y'
  --
  -- Usage    Procedure is called during package initialisation
  --
  -- Remarks  -
  --
  PROCEDURE get_suppress_messages(p_message IN OUT hil$message.suppress_type)
  IS
  BEGIN
    qms$message.get_suppress_messages(p_message);
  END get_suppress_messages;

  --
  -- Purpose  returns all error/message information from qms_message_properties
  --          and qms_message_text table for a specified oracle error message that
  --          needs to be replaced at runtime.
  --
  -- Usage    Parameters
  --              p_errorrec   : returns the error/message information in a PL/SQL record
  --
  -- Remarks  - Currently only ORA and FRM can be replaced.
  --          - Only on the client side you can add/remove messages dynamically to the stack
  --
  PROCEDURE get_native_messages(p_error IN OUT hil$message.message_type)
  IS
  BEGIN
    qms$message.get_native_messages(p_error);
  END get_native_messages;

  --
  -- Purpose  Returns the information of the last error taken from the stack
  --
  -- Usage    Called from the client to retrieve the error information after the
  --          error has been identified as a server side error.
  --          Parameter
  --            p_error : error record type
  --
  -- Remarks  -
  --
  PROCEDURE pop_error(p_error IN OUT hil$message.row_type)
  IS
  BEGIN
    qms$message.pop_error(p_error);
  END pop_error;

  --
  -- Purpose  return true (and p_existing_msg_code then contains the message code)
  --          if specified message code or constraint exists in qms_message_properties.
  --
  -- Usage    Either the message code or the constraint name must be specified.
  --
  -- Remarks  -
  --
  FUNCTION msg_code_exists( p_code       IN  VARCHAR2 := null
                          , p_constraint IN  VARCHAR2 := null
                          , p_existing   OUT VARCHAR2
                          )
    RETURN BOOLEAN
  IS
  BEGIN
    RETURN qms$message.msg_code_exists(p_code, p_constraint, p_existing);
  END msg_code_exists;

  --
  -- Purpose  returns all error/message information from qms_message_properties
  --          and qms_message_text table for a specified error message or
  --          constraint name.
  --          Either the error code (p_errcode) or the constraint name must be specified
  --
  -- Usage    Parameters
  --              p_error      : returns the error/message information in a PL/SQL record
  --              p_code       : error/message code
  --              p_constraint : constraint name
  --              p_language   : language in which the message text is required
  --
  -- Remarks  -
  --
  PROCEDURE get_message( p_error      IN OUT hil$message.row_type
                       , p_code       IN     VARCHAR2 := null
                       , p_constraint IN     VARCHAR2 := null
                       , p_language   IN     VARCHAR2 := hil$profile.get_profile_value('LANGUAGE', user)
                       )
  IS
  BEGIN
    qms$message.get_message(p_error, p_code, p_constraint, p_language);
  END get_message;

  --
  -- Purpose  Add translation in new language to existing message
  --
  -- Usage    From hil$message package
  --
  -- Remarks
  --
  PROCEDURE add_language( p_code      qms_message_text.msp_code%TYPE
                        , p_language  qms_message_text.language%TYPE
                        , p_text      qms_message_text.text%TYPE
                        , p_help      qms_message_text.help_text%TYPE
                        )
  IS
  BEGIN
    qms$message.add_language(p_code, p_language, p_text, p_help);
  END add_language;

  --
  -- Purpose  Create record in message properties
  --          and first record in message text
  --
  -- Usage    From hil$message package
  --
  -- Remarks
  --
  PROCEDURE create_message( p_code              qms_message_properties.code%TYPE
                          , p_description      qms_message_properties.description%TYPE
                          , p_language         qms_message_text.language%TYPE
                          , p_text             qms_message_text.text%TYPE
                          , p_help             qms_message_text.help_text%TYPE              DEFAULT NULL
                          , p_severity         qms_message_properties.severity%TYPE         DEFAULT 'E'
                          , p_logging          qms_message_properties.logging%TYPE          DEFAULT 'N'
                          , p_suppress_warning qms_message_properties.suppress_warning%TYPE DEFAULT 'N'
                          , p_suppress_always  qms_message_properties.suppress_always%TYPE  DEFAULT 'N'
                          , p_constraint_name  qms_message_properties.constraint_name%TYPE  DEFAULT NULL
                          )
  IS
  BEGIN
    qms$message.create_message( p_code
                              , p_description
                              , p_language
                              , p_text
                              , p_help
                              , p_severity
                              , p_logging
                              , p_suppress_warning
                              , p_suppress_always
                              , p_constraint_name
                              );
  END create_message;

  --
  -- Purpose  Derive next unused message code within messages with prefix p_prefix
  --          It is assumed that following the prefix are a '-' and then only numbers
  --          up to a code length of qms$errors.c_errorlength
  --
  -- Usage    for instance from utilities that create new messages
  --
  -- Remarks  -
  --
  FUNCTION get_next_msg_code(p_prefix IN VARCHAR2)
    RETURN VARCHAR2
  IS
  BEGIN
    RETURN qms$message.get_next_msg_code(p_prefix);
  END get_next_msg_code;

  --
  -- Purpose  Delete message properties and all translations
  --
  -- Usage    from utilities
  --
  -- Remarks  -
  --
  PROCEDURE delete_message(p_code IN VARCHAR2)
  IS
  BEGIN
    qms$message.delete_message(p_code);
  END delete_message;

  --
  -- Purpose  Get translated description of severity (error, warning, etc.)
  --
  -- Usage    Call with message severity type as input: E, W, Q, I or M
  --
  -- Remarks  -
  --
  FUNCTION get_severity_string(p_severity IN VARCHAR2)
    RETURN VARCHAR2
  IS
  BEGIN
    RETURN qms$message.get_severity_string(p_severity);
  END get_severity_string;

  --
  -- Purpose  return true if specified message code or constraint exists in
  --          qms_message_properties and a message text exists in the specified
  --          language
  --
  -- Usage    Either the message code or the constraint name must be specified.
  --
  -- Remarks  -
  --
  FUNCTION msg_code_language_exists( p_code       IN VARCHAR2 := NULL
                                   , p_constraint IN VARCHAR2 := NULL
                                   , p_language   IN VARCHAR2
                                   )
    RETURN BOOLEAN
  IS
  BEGIN
    RETURN qms$message.msg_code_language_exists( p_code       => p_code
                                               , p_constraint => p_constraint
                                               , p_language   => p_language
                                               );
  END msg_code_language_exists;

  --
  -- Purpose  Update the message code of a specific message
  --
  -- Usage    Called from headstart utility hsu_emsg
  --
  -- Remarks  -
  --
  --
  PROCEDURE update_msg_code( p_code_old IN VARCHAR2
                           , p_code_new IN VARCHAR2
                           )
  IS
  BEGIN
    qms$message.update_msg_code(p_code_old => p_code_old, p_code_new => p_code_new);
  END update_msg_code;

-- For some reason, we need to close the package body here. Designer will remove it
-- when it generates the .pkb file.
END hil$message;
/
SHOW ERROR