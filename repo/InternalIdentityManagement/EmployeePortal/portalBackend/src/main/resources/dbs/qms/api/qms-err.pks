-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppAplication\src\main\dbs\qms\api\qms-err.pks
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

/*******************************************************************************
Purpose  Server Side specification of the qms$errors packages.
         This package contains the procedures to handle unexcepted errors
         (unhandled exceptions from the WHEN OTHERS clause in the exception
         handling in PL/SQL program units), internal EPP errors, debug messages.
         The package also has a client side version.
         Both implementations allow us to use the same source code on both
         client and server side.

Usage    Two procedures (unhandled_exception, show_message) in this package
         should be used. Unhandled_exception must be added to the WHEN OTHERS
         exception to catch all errors (needs to be added to each PL/SQL program
         unit by the developers) and show_message is the procedure used by the
         developer to show messages/raise errors in his PL/SQL code.

         The third procedure (internal_exception) is used internally to handle
         errors that occur by f.e. passing a wrong value for a parameter.

         For more information look at the program unit specification.

Remarks  -

*******************************************************************************/
PROMPT Creating Package Specification 'qms$errors'
CREATE or REPLACE PACKAGE qms$errors
AS
  -- ---------------------------------------------------------------------------
  -- public constants
  -- ---------------------------------------------------------------------------

  -- Constants that define the format of the error code / message code
  -- change the c_errorlength if the length if the code is more than 9
  -- change the c_errorformat if the format is different from AAA-NNNNN
  -- where A is a letter from A till Z and N is a number from 0 till 9
  c_errorformat CONSTANT VARCHAR2 (15) := 'AAA-NNNNN';
  c_errorlength CONSTANT NUMBER (3, 0) := 9;

  -- ---------------------------------------------------------------------------
  -- public exceptions
  -- ---------------------------------------------------------------------------

  qms$exception   EXCEPTION;
  PRAGMA EXCEPTION_INIT(qms$exception, -20998);

  -- ---------------------------------------------------------------------------
  -- public types
  -- ---------------------------------------------------------------------------

  TYPE t_message_rectype_tbl IS TABLE OF hil$message.row_type
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
  --
  --
  FUNCTION revision
    RETURN VARCHAR2;
  PRAGMA RESTRICT_REFERENCES(revision, WNDS, RNDS, WNPS);

  --
  -- Purpose  Verify that the error message is in the format of our error
  --          messages XXX-00000 or is the name of a constraint.
  --
  -- Usage    -
  --
  -- Remarks  currently no longer required but in here to catch future
  --          found bugs (they are sure)
  --
  FUNCTION checkErrorFormat(p_message IN VARCHAR2)
    RETURN boolean;
  PRAGMA RESTRICT_REFERENCES(checkErrorFormat, WNDS);

  --
  -- Purpose  Captures all unhandled exceptions and displays the error code and text
  --          and the program unit where the exception occured. On the server side
  --          it will also log the call stack (call sequence of program units before
  --          reaching the program unit with the error.
  --
  -- Usage    Add an extra line to the exception handler :
  --             WHEN OTHERS THEN qms$error.unhandled_exception('<program unit name>');
  --          This should be added to all program units
  --
  -- Remarks  Both displaying of the error message and raise of the form trigger
  --          failure must be done on the client side.
  --
  PROCEDURE unhandled_exception ( p_location  IN VARCHAR2
                                , p_code      IN NUMBER   DEFAULT SQLCODE
                                , p_message   IN VARCHAR2 DEFAULT SQLERRM
                                );
  PRAGMA RESTRICT_REFERENCES(unhandled_exception, WNDS);

  --
  -- Purpose  Raise and display any internal error information. This may refer
  --          to incorrect usage of poratl routines. f.e. setting parameters to
  --          an invalid value using a set_xxx procedure.
  --          The procedure must only be used in the internal code and never be
  --          a developer.
  --
  -- Usage    Call the procedure in the code with parameters
  --              p_message  : Hardcoded message. The message will not be
  --                           translated since this is an internal message.
  --              p_severity : severity type, default to 'E'
  --              p_raise    : raise an exception TRUE or FALSE.
  --                           If TRUE processing will be stopped.
  --                           By default TRUE.
  --
  -- Remarks  -
  --
  PROCEDURE internal_exception( p_message  IN VARCHAR2
                              , p_severity IN VARCHAR2 := 'E'
                              , p_raise    IN BOOLEAN  := TRUE
                              );
  PRAGMA RESTRICT_REFERENCES(internal_exception, WNDS);

  --
  -- Purpose  Replace the old Headstart raised errors by a new
  --          error message
  --          unhandled exception on the stack
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE legacy_exception(p_error IN OUT hil$message.row_type, p_message IN VARCHAR2);
  PRAGMA RESTRICT_REFERENCES(legacy_exception, WNDS);

  --
  -- Purpose  Show a message or error to the end user. The developer will call
  --          this procedure to display a message to the end user and raise an
  --          exception if necessary.
  --
  -- Usage    The procedure can be called in PL/SQL program units using the
  --          following parameters
  --             p_message  : message code as it is stored in the message
  --                          tables.
  --             p_param0,
  --             p_param9   : parameters used to replace the <p1> till <p10>
  --                          placeholders in the message text and the help
  --                          text.
  --             p_severity : error type ('E', 'I', 'W', 'M'), by default the
  --                          error type stored in the message table is used.
  --                          You can specify the error type if you want to
  --                          override the default in the message table.
  --             p_raise    : raise exception.
  --                          By default the exception is raised dependent on
  --                          the error type (either in the qms message table
  --                          or the p_severity parameter).
  --                          You can use this to override the default
  --                          processing
  --
  -- Remarks  Currently only errors can be passed, there will always be a raise
  --          of an exception. We need to find a mechanism to pass back warning
  --          / information messages and notify the client of what happened.
  --
  PROCEDURE show_message ( p_message     IN VARCHAR2
                         , p_param0      IN VARCHAR2 DEFAULT NULL
                         , p_param1      IN VARCHAR2 DEFAULT NULL
                         , p_param2      IN VARCHAR2 DEFAULT NULL
                         , p_param3      IN VARCHAR2 DEFAULT NULL
                         , p_param4      IN VARCHAR2 DEFAULT NULL
                         , p_param5      IN VARCHAR2 DEFAULT NULL
                         , p_param6      IN VARCHAR2 DEFAULT NULL
                         , p_param7      IN VARCHAR2 DEFAULT NULL
                         , p_param8      IN VARCHAR2 DEFAULT NULL
                         , p_param9      IN VARCHAR2 DEFAULT NULL
                         , p_severity    IN VARCHAR2 DEFAULT NULL
                         , p_raise       IN BOOLEAN  DEFAULT NULL
                         , p_table_name  IN VARCHAR2 DEFAULT NULL
                         , p_table_rowid IN rowid    DEFAULT NULL
                         );
  PRAGMA RESTRICT_REFERENCES(show_message, WNDS);

  --
  -- Purpose  raise the standard exception
  --
  -- Usage    If you only want to make sure the transaction fails but
  --          don't want to specify an error message at the same time.
  --
  -- Remarks  Used in qms$transaction package
  --
  PROCEDURE raiseFailure;
  PRAGMA RESTRICT_REFERENCES(raiseFailure, WNDS);

  PRAGMA RESTRICT_REFERENCES(qms$errors, WNDS);
END;
/
SHOW ERROR