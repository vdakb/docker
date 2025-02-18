-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppAplication\src\main\dbs\qms\api\hil-msg.pks
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

PROMPT Creating Package Specification 'hil$message'
CREATE or REPLACE PACKAGE hil$message
AS

  -- ---------------------------------------------------------------------------
  -- public structure units
  -- ---------------------------------------------------------------------------

  TYPE row_type IS RECORD (
    code              VARCHAR2   (15)
  , severity          VARCHAR2    (1)
  , severity_desc     VARCHAR2  (100) -- Translated description of severity
  , suppress_warning  VARCHAR2    (1)
  , suppress_always   VARCHAR2    (1)
  , logging           VARCHAR2    (1)
  , msg_text          VARCHAR2 (2000)
  , help_text         VARCHAR2 (2000)
  , lang_code         VARCHAR2    (2)
  , raise_error       BOOLEAN
  , message_found     BOOLEAN
  , table_name        VARCHAR2   (30)
  , table_rowid       ROWID
  );

  TYPE message_type IS TABLE OF row_type
    INDEX BY BINARY_INTEGER;

  TYPE suppress_type IS TABLE OF VARCHAR2(15)
    INDEX BY BINARY_INTEGER;

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
    RETURN VARCHAR2;
  PRAGMA RESTRICT_REFERENCES(revision, WNDS);

  --
  -- Purpose  Initialise stack with error that may not be displayed by
  --          retrieving the records with suppress_always set to 'Y'
  --
  -- Usage    Procedure is called during package initialisation
  --
  -- Remarks  -
  --
  PROCEDURE get_suppress_messages (p_message IN OUT hil$message.suppress_type);

  --
  -- Purpose  returns all error/message information from qms_message_properties
  --          and qms_message_text table for a specified oracle error message
  --          that needs to be replaced at runtime.
  --
  -- Usage    Parameters
  --            p_error   : returns the error/message information in a PL/SQL
  --                        record
  --
  -- Remarks  - Currently only ORA and FRM can be replaced.
  --          - Only on the client side you can add/remove messages dynamically
  --            to the stack
  --
  PROCEDURE get_native_messages (p_error IN OUT hil$message.message_type);

  --
  -- Purpose  Returns the information of the last error taken from the stack
  --
  -- Usage    Called from the client to retrieve the QMS error information
  --          after the error has been identified as a server side error.
  --          Parameter
  --               p_error : qms error record type
  --
  -- Remarks
  --
  PROCEDURE pop_error(p_error IN OUT hil$message.row_type);

  --
  -- Purpose  return true (and p_existing_msg_code then contains the message
  --          code) if specified message code or constraint exists in
  --          qms_message_properties.
  --
  -- Usage    Either the message code or the constraint name must be specified.
  --
  -- Remarks  -
  --
  FUNCTION msg_code_exists( p_code       IN  VARCHAR2 := null
                          , p_constraint IN  VARCHAR2 := null
                          , p_existing   OUT VARCHAR2
                          )
    RETURN BOOLEAN;

  --
  -- Purpose  returns all error/message information from qms_message_properties
  --          and qms_message_text table for a specified error message or
  --          constraint name.
  --          Either the error code (p_errcode) or the constraint name must be
  --          specified
  --
  -- Usage    Parameters
  --            p_error      : returns the error/message information in a PL/SQL
  --                           record
  --            p_code       : error/message code
  --            p_constraint : constraint name
  --            p_language   : language in which the message text is required
  --
  -- Remarks  -
  --
  PROCEDURE get_message( p_error      IN OUT hil$message.row_type
                       , p_code       IN     VARCHAR2 := null
                       , p_constraint IN     VARCHAR2 := null
                       , p_language   IN     VARCHAR2 := hil$profile.get_profile_value('LANGUAGE',user)
                       );
  PRAGMA RESTRICT_REFERENCES(get_message, WNDS);

  --
  -- Purpose  Add translation in new language to existing message
  --
  -- Usage    From hil$message package
  --
  -- Remarks
  --
  PROCEDURE add_language( p_code       qms_message_text.msp_code%TYPE
                        , p_language   qms_message_text.language%TYPE
                        , p_text       qms_message_text.text%TYPE
                        , p_help       qms_message_text.help_text%TYPE
                        );

  --
  -- Purpose  Create record in message properties
  --          and first record in message text
  --
  -- Usage    From hil$message package
  --
  -- Remarks
  --
  PROCEDURE create_message( p_code             qms_message_properties.code%TYPE
                          , p_description      qms_message_properties.description%TYPE
                          , p_language         qms_message_text.language%TYPE
                          , p_text             qms_message_text.text%TYPE
                          , p_help             qms_message_text.help_text%TYPE              DEFAULT NULL
                          , p_severity         qms_message_properties.severity%TYPE         DEFAULT 'E'
                          , p_logging          qms_message_properties.logging%TYPE          DEFAULT 'N'
                          , p_suppress_warning qms_message_properties.suppress_warning%TYPE DEFAULT 'N'
                          , p_suppress_always  qms_message_properties.suppress_always%TYPE  DEFAULT 'N'
                          , p_constraint_name  qms_message_properties.constraint_name%TYPE  DEFAULT NULL
                          );

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
    RETURN VARCHAR2;

  --
  -- Purpose  Delete message properties and all translations
  --
  -- Usage    from utilities
  --
  -- Remarks  -
  --
  PROCEDURE delete_message(p_code IN VARCHAR2);

  --
  -- Purpose  Get translated description of severity (error, warning, etc.)
  --
  -- Usage    Call with message severity type as input: E, W, Q, I or M
  --
  -- Remarks  -
  --
  FUNCTION get_severity_string(p_severity IN VARCHAR2)
    RETURN VARCHAR2;
  PRAGMA RESTRICT_REFERENCES(get_severity_string, WNDS);

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
    RETURN BOOLEAN;

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
                           );

PRAGMA RESTRICT_REFERENCES(hil$message, WNDS);

-- For some reason, we need to close the package specification here. Designer will remove it
-- when it generates the .pkb file.
END hil$message;
/
SHOW ERROR