-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\qms_err.pkb
--
-- Generated for Oracle 10g on Tue Mar 30 08:58:19 2010 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Specification 'qms$errors'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE or REPLACE PACKAGE qms$errors
AS
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- public types
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
TYPE qms$table_type IS TABLE OF hil$message.hil$row_type INDEX BY BINARY_INTEGER;
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- public constants
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- Constants that define the format of the error code / message code
-- change the qms$format if the format is different from AAA-NNNNN
-- change the qms$length if the length if the code is more than 9
qms$length CONSTANT NUMBER (3, 0) := 9;
-- where A is a letter from A till Z and N is a number from 0 till 9
qms$format CONSTANT VARCHAR2 (15) := 'AAA-NNNNN';
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- public exceptions
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
qms$exception   EXCEPTION;
PRAGMA EXCEPTION_INIT(qms$exception, -20998);
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
  RETURN VARCHAR2;
PRAGMA RESTRICT_REFERENCES(revision, WNDS, RNDS, WNPS);
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
PROCEDURE raiseError;
PRAGMA RESTRICT_REFERENCES(raiseError, WNDS);
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
  RETURN BOOLEAN;
PRAGMA RESTRICT_REFERENCES(checkFormat, WNDS);
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
                  , p_severity IN VARCHAR2 := 'E'
                  , p_raise    IN BOOLEAN := TRUE);
PRAGMA RESTRICT_REFERENCES(internal, WNDS);
--------------------------------------------------------------------------------
-- Purpose  Captures all unhandled exceptions and displays the error code and
--          text and the program unit where the exception occured. On the server
--          side it will also log the call stack (call sequence of program units
--          before reaching the program unit with the error.
--
-- Usage    Add an extra line to the exception handler :
--             WHEN OTHERS
--             THEN
--               qms$error.unhandled('<program unit name>');
--          This should be added to all program units
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE unhandled( p_name    IN VARCHAR2
                   , p_code    IN NUMBER   DEFAULT SQLCODE
                   , p_message IN VARCHAR2 DEFAULT SQLERRM);
PRAGMA RESTRICT_REFERENCES(unhandled, WNDS);
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
-----------------------------------------------------------------------------
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
              );
PRAGMA RESTRICT_REFERENCES(show, WNDS);
-----------------------------------------------------------------------------

PRAGMA RESTRICT_REFERENCES(qms$errors, WNDS);
-- For some reason, we need to close the package specification here.
-- Designer will remove it when it generates the .pks file.
END qms$errors;
/
SHOW ERROR