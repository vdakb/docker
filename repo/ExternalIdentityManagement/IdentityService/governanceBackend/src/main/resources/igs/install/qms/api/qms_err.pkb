-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\qms_err.pks
--
-- Generated for Oracle 10g on Tue Mar 30 08:58:19 2010 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Implementation 'qms$errors'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE or REPLACE PACKAGE BODY qms$errors
AS
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private constants
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/* Revision label of package */
REVISION_LABEL               CONSTANT VARCHAR2( 8) := '2.0.0.0';

c_error                      CONSTANT VARCHAR2( 1) := 'E';
c_warning                    CONSTANT VARCHAR2( 1) := 'W';
c_information                CONSTANT VARCHAR2( 1) := 'I';
c_invalid                    CONSTANT VARCHAR2(80) := 'Headstart internal Error : Invalid error type passed to procedure show';
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private program units
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
-- Purpose  Captures all unhandled exceptions and displays the error code and
--          text and the program unit where the exception occured. On the server
--          side it will also log the call stack (call sequence of program units
--          before reaching the program unit with the error.
--
-- Usage    Add an extra line to the exception handler :
--             WHEN OTHERS
--             THEN
--               qms$error.unhandledXXXX('<program unit name>');
--          This should be added to all program units
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE unhandledXXXX( p_message  IN VARCHAR2
                       , p_location IN VARCHAR2
                       )
IS
  l_error hil$message.hil$row_type;
BEGIN
  -- obtain unhandled rrror message
  hil$message.get_message(l_error, 'QMS-00100');

  l_error.text             := replace(l_error.text, '<p1>', p_message);
  l_error.text             := replace(l_error.text, '<p2>', p_location);
  l_error.severity         := 'E';
  l_error.suppress_warning := 'N';
  l_error.suppress_always  := 'N';
  l_error.help             := null;
  l_error.language         := 'en';
  l_error.raise_error      := true;

  cg$errors.push(l_error);
  raiseError;
END unhandledXXXX;
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
-- Purpose  Raise the standard QMS exception
--
-- Usage    If you only want to make sure the transaction fails but
--          don't want to specify an error message at the same time.
--
-- Remarks  Used in qms$transaction package
--
--------------------------------------------------------------------------------
PROCEDURE raiseError
IS
BEGIN
  raise qms$exception;
EXCEPTION
  WHEN OTHERS
  THEN
    raise_application_error(-20998, 'Transaction Failed');
END raiseError;
--------------------------------------------------------------------------------
--
-- Purpose  Verify that the error message is in the format of our error messages
--          XXX-00000 or is the name of a constraint.
--          function that is used to work around bugs.
--
-- Usage    -
--
-- Remarks  currently no longer required but in here to catch future found
--          bugs (they are sure)
--
--------------------------------------------------------------------------------
FUNCTION checkFormat(p_msg IN VARCHAR2)
  RETURN BOOLEAN
IS
  l_return BOOLEAN := false;
BEGIN
  IF translate( SUBSTR(p_msg, 1, qms$length)
              , '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'
              , 'NNNNNNNNNNAAAAAAAAAAAAAAAAAAAAAAAAAA'
              ) = qms$format
  THEN
    l_return := true;
  ELSE
    l_return := false;
  END IF;
  --
  RETURN l_return;
END checkFormat;
--------------------------------------------------------------------------------
--
-- Purpose  Raise and display any internal error information. This may refer to
--          incorrect usage of Headstart routines. f.e. setting Headstart
--          parameters to an invalid value using a set_xxx procedure.
--          The procedure must only be used in the QMS Headstart code and never
--          be a developer.
--
-- Usage    The procedure can be called in PL/SQL program units using the
--          following parameters:
--          p_msg      : Hardcoded message. The message will not be translated
--                       since this is an internal QMS Headstart message.
--          p_severity : error type, default to 'E'
--          p_raise    : raise an exception TRUE or FALSE. If TRUE processing
--                       will be stopped.
--                       By default TRUE.
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE internal( p_msg      IN VARCHAR2
                  , p_severity IN VARCHAR2 default 'E'
                  , p_raise    IN boolean default true
                  )
IS
  l_error hil$message.hil$row_type;
  l_store hil$message.hil$row_type;
BEGIN
  l_error.text := p_msg;
  IF p_severity not IN ('M', 'I', 'E', 'W')
  THEN
    internal(c_invalid);
  ELSE
    l_error.severity := p_severity;
  END IF;

  l_error.suppress_warning := 'N';
  l_error.suppress_always  := 'N';
  l_error.help             := null;
  l_error.language         := 'en';
  l_error.raise_error      := p_raise;

  l_store := l_error;
  cg$errors.push(l_store);

  IF l_error.raise_error
  THEN
    raiseError;
  END IF;
END internal;
--------------------------------------------------------------------------------
--
-- Purpose  Captures all unhandled exceptions and displays the error code and
--          text and the program unit where the exception occured. On the server
--          side it will also log the call stack (call sequence of program units
--          before reaching the program unit with the error.
--          Verify the error and evaluate wether the unhandled exception is due
--          to a previously raised error or a new unhandled exception.
--
-- Usage    Add an extra line to the exception handler :
--             WHEN OTHERS THEN qms$error.unhandled('<program unit name>');
--          This should be added to all program units
--
-- Remarks  - Both displaying of the error message and raise of the form trigger
--            failure must be done on the client side.
--
--------------------------------------------------------------------------------
PROCEDURE unhandled( p_name    IN VARCHAR2
                   , p_code    IN NUMBER DEFAULT SQLCODE
                   , p_message IN VARCHAR2 DEFAULT SQLERRM
                   )
IS
  c_error CONSTANT VARCHAR2(10) := 'ORA-20998';
  l_error hil$message.hil$row_type;
BEGIN
  IF (instr(p_message, 'ORA-20000') <> 0)
  THEN
    cg$errors.push(l_error);
  ELSIF (instr(p_message, c_error) = 0)
  THEN
    unhandledXXXX(p_message, p_name);
  END IF;
  raiseError;
END unhandled;
--------------------------------------------------------------------------------
--
-- Purpose  Show a message or error to the end user. The developer will call
--          this procedure to display a message to the end user and raise an
--          exception if necessary.
--
-- Usage    The procedure can be called in PL/SQL program units using the
--          following parameters:
--             p_msg       : message code as it is stored in themessage table.
--             p_param0..9 : substitution parameters.
--             p_severity  : error type ('E', 'I', 'W', 'M'), by default the
--                           error type stored in the message table is used.
--                           You can specify the error type if you want to
--                           override the default in the message table
--             p_raise     : raise exception.
--                           By default the exception is raised dependent on the
--                           error type (either in the message table or the
--                           p_severity parameter).
--                           You can use this to override the default
--                           processing.
--          The parameters are used to replace the <p0> till <p9> parameters in
--          the message text and the help text.
--
-- Remarks  -
--------------------------------------------------------------------------------
PROCEDURE show( p_msg      IN VARCHAR2
              , p_param0   IN VARCHAR2 DEFAULT NULL
              , p_param1   IN VARCHAR2 DEFAULT NULL
              , p_param2   IN VARCHAR2 DEFAULT NULL
              , p_param3   IN VARCHAR2 DEFAULT NULL
              , p_param4   IN VARCHAR2 DEFAULT NULL
              , p_param5   IN VARCHAR2 DEFAULT NULL
              , p_param6   IN VARCHAR2 DEFAULT NULL
              , p_param7   IN VARCHAR2 DEFAULT NULL
              , p_param8   IN VARCHAR2 DEFAULT NULL
              , p_param9   IN VARCHAR2 DEFAULT NULL
              , p_severity IN VARCHAR2 DEFAULT NULL
              , p_raise    IN BOOLEAN  DEFAULT NULL
              , p_table    IN VARCHAR2 DEFAULT NULL
              , p_rowid    IN ROWID    DEFAULT NULL
              )
IS
  l_error hil$message.hil$row_type;
  l_store hil$message.hil$row_type;
BEGIN
  -- get error message
  IF checkFormat(p_msg)
  THEN
    hil$message.get_message(l_error, p_msg);
  ELSE
    l_error.text := p_msg;
  END IF;

  IF p_severity IS NOT NULL
  THEN
    IF p_severity IN ('E', 'I', 'M', 'Q', 'W')
    THEN
      l_error.severity := p_severity;
      IF p_severity = 'E'
      THEN
        l_error.raise_error := true;
      ELSE
        l_error.raise_error := false;
      END IF;
    ELSE
      internal(c_invalid);
    END IF;
  END IF;

  IF p_raise IS NOT NULL
  THEN
    l_error.raise_error := p_raise;
  END IF;

  -- replace parameters
  l_error.text := replace(l_error.text, '<p1>',  p_param0);
  l_error.text := replace(l_error.text, '<p2>',  p_param1);
  l_error.text := replace(l_error.text, '<p3>',  p_param2);
  l_error.text := replace(l_error.text, '<p4>',  p_param3);
  l_error.text := replace(l_error.text, '<p5>',  p_param4);
  l_error.text := replace(l_error.text, '<p6>',  p_param5);
  l_error.text := replace(l_error.text, '<p7>',  p_param6);
  l_error.text := replace(l_error.text, '<p8>',  p_param7);
  l_error.text := replace(l_error.text, '<p9>',  p_param8);
  l_error.text := replace(l_error.text, '<p10>', p_param9);

  l_error.help := replace(l_error.help, '<p1>',  p_param0);
  l_error.help := replace(l_error.help, '<p2>',  p_param1);
  l_error.help := replace(l_error.help, '<p3>',  p_param2);
  l_error.help := replace(l_error.help, '<p4>',  p_param3);
  l_error.help := replace(l_error.help, '<p5>',  p_param4);
  l_error.help := replace(l_error.help, '<p6>',  p_param5);
  l_error.help := replace(l_error.help, '<p7>',  p_param6);
  l_error.help := replace(l_error.help, '<p8>',  p_param7);
  l_error.help := replace(l_error.help, '<p9>',  p_param8);
  l_error.help := replace(l_error.help, '<p10>', p_param9);

  l_error.table_name  := p_table;
  l_error.table_rowid := p_rowid;

  l_store := l_error;
  cg$errors.push(l_store);

  IF l_error.raise_error
  THEN
    raiseError;
  ELSIF l_error.severity = 'E'
  THEN
    cg$errors.set_qms_error(true);
    -- An error is raised during an open transaction, but raise_failure is false.
    -- It is assumed that the error must be put on the stack
    -- and that close transaction needs to see the error.
    -- The first part is already done,
    -- so we only need to set a flag in cg$errors for the second part.
    cg$errors.raise_failure;
  END IF;
END show;

-- For some reason, we need to close the package specification here.
-- Designer will remove it when it generates the .pkb file.
END qms$errors;
/
SHOW ERROR