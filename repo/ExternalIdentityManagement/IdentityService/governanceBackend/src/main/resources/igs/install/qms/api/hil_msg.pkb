-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\hil_msg.pkb
--
-- Generated for Oracle 10g on Tue Mar 30 08:58:19 2010 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Implementation 'hil$message'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE or REPLACE PACKAGE BODY hil$message
AS
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private constants
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/* Revision label of package */
REVISION_LABEL               CONSTANT VARCHAR2( 8) := '2.0.0.0';
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- public program units
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
--
-- Purpose  returns the revision label of this package
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION revision
  RETURN VARCHAR2
IS
BEGIN
  RETURN REVISION_LABEL;
END revision;
--------------------------------------------------------------------------------
--
-- Purpose  Returns translated description of severity (error, warning, etc.)
--
-- Usage    Call with message severity type as input: E, W, Q, I or M
--
-- Remarks
--
--------------------------------------------------------------------------------
FUNCTION severity_string(p_severity IN VARCHAR2)
  RETURN VARCHAR2
IS
BEGIN
  RETURN qms$message.severity_string(p_severity);
END severity_string;
--------------------------------------------------------------------------------
-- Purpose  Derive next unused message code within messages with prefix p_prefix
--          It is assumed that following the prefix are a '-' and then only
--          numbers up to a code length of qms$errors.qms$length
--
-- Usage    For instance from utilities that create new messages
--
-- Remarks
--
--------------------------------------------------------------------------------
FUNCTION code_next(p_prefix IN VARCHAR2)
  RETURN VARCHAR2
IS
BEGIN
  RETURN qms$message.code_next(p_prefix);
END code_next;
--------------------------------------------------------------------------------
--
-- Purpose  Return true (and p_found then contains the message code) if
--          specified message code or constraint exists in
--          qms$message_properties.
--
-- Usage    Either the message code or the constraint name must be specified.
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION lookup( p_code       IN  VARCHAR2 := null
               , p_constraint IN  VARCHAR2 := null
               , p_found      OUT VARCHAR2
               )
  RETURN BOOLEAN
IS
BEGIN
  RETURN qms$message.lookup(p_code, p_constraint, p_found);
END lookup;
--------------------------------------------------------------------------------
--
-- Purpose  Return true if specified message code or constraint exists in
--          qms_message_properties and a message text exists in the specified
--          language.
--
-- Usage    Either the message code or the constraint name must be specified.
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION exists( p_code       IN VARCHAR2 := null
               , p_constraint IN VARCHAR2 := null
               , p_language   IN VARCHAR2
               )
  RETURN BOOLEAN
IS
BEGIN
  RETURN qms$message.exists(p_code, p_constraint, p_language);
END exists;
--------------------------------------------------------------------------------
--
-- Purpose  Initialise table with error codes that may not be displayed by
--          retrieving the records from the database with suppress_always set to
--          'Y'. These will be passed to the client and cached in a global
--          record_group on the client.
--
-- Usage    Procedure is called during package initialisation
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE get_suppress_messages(p_message IN OUT hil$message.hil$ind_type)
IS
BEGIN
  qms$message.get_suppress_messages(p_message);
END get_suppress_messages;
--------------------------------------------------------------------------------
--
-- Purpose  Returns all error/message information from qms$message_properties
--          and qms$message_text table for a specified oracle error message that
--          needs to be replaced at runtime.
--          These will be passed to the client and cached in a global
--          record_group on the client.
--
-- Usage    Parameters
--              p_error   : returns the error/message information in a PL/SQL
--                          record
--
-- Remarks  - Currently only ORA and FRM can be replaced.
--          - Only on the client side you can add/remove messages dynamically to
--            the stack
--
--------------------------------------------------------------------------------
PROCEDURE get_oracle_messages(p_error IN OUT hil$message.hil$table_type)
IS
BEGIN
  qms$message.get_oracle_messages(p_error);
END get_oracle_messages;
--------------------------------------------------------------------------------
--
-- Purpose  Returns all error/message information from qms$message_properties
--          and qms$message_text table for a specified error message or
--          constraint name.
--          Either the error code (p_errcode) or the constraint name must be
--          specified.
--
-- Usage    Parameters
--              p_error      : returns the error/message information in a PL/SQL
--                             record
--              p_code       : error/message code
--              p_constraint : constraint name
--              p_language   : language in which the message text is required
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE get_message( p_error      IN OUT hil$message.hil$row_type
                     , p_code       IN     VARCHAR2 := null
                     , p_constraint IN     VARCHAR2 := null
                     , p_language   IN     VARCHAR2 := 'en'
                     )
IS
BEGIN
  qms$message.get_message(p_error, p_code, p_constraint, p_language);
END get_message;
--------------------------------------------------------------------------------
-- Purpose  Create record in message properties and first record in message text
--
-- Usage    From hil$message package
--
-- Remarks
--
--------------------------------------------------------------------------------
PROCEDURE create_message( p_code             qms_message_properties.code%type
                        , p_description      qms_message_properties.description%type
                        , p_language         qms_message_text.language%type
                        , p_text             qms_message_text.text%type
                        , p_help             qms_message_text.help%type                   DEFAULT NULL
                        , p_severity         qms_message_properties.severity%type         DEFAULT 'E'
                        , p_logging          qms_message_properties.logging%type          DEFAULT 'N'
                        , p_suppress_warning qms_message_properties.suppress_warning%type DEFAULT 'N'
                        , p_suppress_always  qms_message_properties.suppress_always%type  DEFAULT 'N'
                        , p_constraint_name  qms_message_properties.constraint_name%type  DEFAULT NULL
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
--------------------------------------------------------------------------------
-- Purpose  Delete message properties and all translations
--
-- Usage    from utilities
--
-- Remarks
--
--------------------------------------------------------------------------------
PROCEDURE delete_message(p_code IN VARCHAR2)
IS
BEGIN
  qms$message.delete_message(p_code);
END delete_message;
--------------------------------------------------------------------------------
--
-- Purpose  Update the message code of a specific message
--
-- Usage    Called from headstart utility hsu_emsg
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE update_code(p_old IN VARCHAR2, p_new IN VARCHAR2)
IS
BEGIN
  qms$message.update_code(p_old => p_old, p_new => p_new);
END update_code;
--------------------------------------------------------------------------------
--
-- Purpose  Add translation in new language to existing message
--
-- Usage    From hil$message package
--
-- Remarks
--
--------------------------------------------------------------------------------
PROCEDURE add_language( p_code     qms_message_text.code%type
                      , p_language qms_message_text.language%type
                      , p_text     qms_message_text.text%type
                      , p_help     qms_message_text.help%type
                      )
IS
BEGIN
  qms$message.add_language(p_code, p_language, p_text, p_help);
END add_language;
--------------------------------------------------------------------------------
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
--------------------------------------------------------------------------------
PROCEDURE pop_qms_errorinfo(p_error IN OUT hil$message.hil$row_type)
IS
BEGIN
  qms$message.pop_qms_errorinfo(p_error);
END pop_qms_errorinfo;

-- For some reason, we need to close the package body here.
-- Designer will remove it when it generates the .pkb file.
END hil$message;
/
SHOW ERROR