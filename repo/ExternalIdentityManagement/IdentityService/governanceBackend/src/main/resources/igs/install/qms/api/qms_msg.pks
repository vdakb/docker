-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\qms_msg.pks
--
-- Generated for Oracle 10g on Tue Mar 30 08:58:19 2010 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Specification 'qms$message'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE PACKAGE qms$message IS
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- public constants
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
c_errornotfound    CONSTANT VARCHAR2(9) := 'QMS-00101';
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- public program units
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
--
-- Purpose  Returns the revision label of this package
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION revision
  RETURN VARCHAR2;
PRAGMA RESTRICT_REFERENCES(revision, WNDS);
--------------------------------------------------------------------------------
-- Purpose  Returns translated description of severity (error, warning, etc.)
--
-- Usage    Call with message severity type as input: E, W, Q, I or M
--
-- Remarks
--------------------------------------------------------------------------------
FUNCTION severity_string(p_severity IN VARCHAR2)
  RETURN VARCHAR2;
PRAGMA RESTRICT_REFERENCES(severity_string, WNDS);
--------------------------------------------------------------------------------
-- Purpose  Derive next unused message code within messages with prefix p_prefix
--          It is assumed that following the prefix are a '-' and then only
--          numbers up to a code length of qms$errors.qms$length
--
-- Usage    for instance from utilities that create new messages
--
-- Remarks
--
--------------------------------------------------------------------------------
FUNCTION code_next(p_prefix IN VARCHAR2)
  RETURN VARCHAR2;
--------------------------------------------------------------------------------
--
-- Purpose  Return true (and p_existing_code then contains the message code)
--          if specified message code or constraint exists in
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
  RETURN BOOLEAN;
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
FUNCTION exists( p_code       IN VARCHAR2 := NULL
               , p_constraint IN VARCHAR2 := NULL
               , p_language   IN VARCHAR2
               )
  RETURN BOOLEAN;
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
PROCEDURE get_suppress_messages(p_suppmesg IN OUT hil$message.hil$ind_type);
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
PROCEDURE get_oracle_messages(p_errortab IN OUT hil$message.hil$table_type);
--------------------------------------------------------------------------------
--
-- Purpose  Returns all error/message information from qms$message_properties
--          and qms$message_text table for a specified error message or
--          constraint name.
--          Either the error code (p_errcode) or the constraint name must be
--          specified
--
-- Usage    Parameters
--              p_error      : returns the error/message information in a PL/SQL
--                             record
--              p_errcode    : error/message code
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
                     );
PRAGMA RESTRICT_REFERENCES(get_message, WNDS);
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
                        );
--------------------------------------------------------------------------------
-- Purpose  Delete message properties and all translations
--
-- Usage    from utilities
--
-- Remarks
--
--------------------------------------------------------------------------------
PROCEDURE delete_message(p_code IN VARCHAR2);
PRAGMA RESTRICT_REFERENCES(qms$message, WNDS);
--------------------------------------------------------------------------------
--
-- Purpose  Update the message code of a specific message
--
-- Usage    Called from headstart utility hsu_emsg
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE update_code(p_old IN VARCHAR2, p_new IN VARCHAR2);
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
                      );
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
PROCEDURE pop_qms_errorinfo(p_error IN OUT hil$message.hil$row_type);

-- For some reason, we need to close the package specification here.
-- Designer will remove it when it generates the .pks file.
END qms$message;
/
SHOW ERROR
