-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppAplication\src\main\dbs\qms\api\qms-err.pkb
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

/*******************************************************************************
Purpose  Server Side implementation of the qms$errors packages.
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
PROMPT Creating Package Implementation 'qms$errors'
CREATE or REPLACE PACKAGE BODY qms$errors
AS
  -- ---------------------------------------------------------------------------
  -- private constants
  -- ---------------------------------------------------------------------------
  revision_label        CONSTANT VARCHAR2(8)  := '1.0.0.0';

  c_errortype           CONSTANT VARCHAR2(1)  := 'E';
  c_warningtype         CONSTANT VARCHAR2(1)  := 'W';
  c_informationtype     CONSTANT VARCHAR2(1)  := 'I';
  qms$invalid_errortype CONSTANT VARCHAR2(100):= 'Employee Portal internal Error : Invalid error type passed to procedure show_message';

  -- ---------------------------------------------------------------------------
  -- private program units
  -- ---------------------------------------------------------------------------

  --
  -- Purpose  Get the unhandled exception message and put the
  --          unhandled exception on the stack
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE unhandledServerException ( p_errtxt   IN VARCHAR2
                                     , p_procname IN VARCHAR2
                                     )
  IS
    l_error   hil$message.row_type;
  BEGIN
    -- get unhandled error message
    hil$message.get_message(l_error, 'QMS-00100');

    l_error.msg_text         := replace(l_error.msg_text, '<p1>', p_errtxt);
    l_error.msg_text         := replace(l_error.msg_text, '<p2>', p_procname);
    l_error.severity         := 'E';
    l_error.suppress_warning := 'N';
    l_error.suppress_always  := 'N';
    l_error.help_text        := null;
    l_error.lang_code        := hil$profile.get_profile_value ('LANGUAGE', user);
    l_error.raise_error      := true;

    cg$errors.push(l_error);
    raiseFailure;
  END unhandledServerException;

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
  -- Purpose  Verify that the error message is in the format of our error
  --          messages XXX-00000 or is the name of a constraint.
  --
  -- Usage    -
  --
  -- Remarks  currently no longer required but in here to catch future
  --          found bugs (they are sure)
  --
  FUNCTION checkErrorFormat(p_message IN VARCHAR2 )
    RETURN BOOLEAN
  IS
    l_returnval   boolean := false;
  BEGIN
    IF translate( SUBSTR(p_message, 1, c_errorlength)
                , '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'
                , 'NNNNNNNNNNAAAAAAAAAAAAAAAAAAAAAAAAAA'
                ) = c_errorformat
    THEN
      l_returnval := true;
    ELSE
      l_returnval := false;
    END IF;

    RETURN l_returnval;
  END checkErrorFormat;


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
  PROCEDURE unhandled_exception( p_location  IN VARCHAR2
                               , p_code      IN NUMBER   DEFAULT SQLCODE
                               , p_message   IN VARCHAR2 DEFAULT SQLERRM
                               )
  IS
    c_qmsservererror CONSTANT VARCHAR2 (60) := 'ORA-20998';
    l_error          hil$message.row_type;
  BEGIN
    IF (instr(p_message, c_qmsservererror) = 0)
    THEN
      unhandledserverexception (p_message, p_location);
    END IF;
    raiseFailure;
  END unhandled_exception;

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
                               , p_severity IN VARCHAR2 DEFAULT 'E'
                               , p_raise    IN BOOLEAN  DEFAULT TRUE
                                )
  IS
    l_error    hil$message.row_type;
    l_store    hil$message.row_type;
  BEGIN
    l_error.msg_text := p_message;
    IF p_severity NOT IN ('M', 'I', 'E', 'W')
    THEN
      internal_exception(qms$invalid_errortype);
    ELSE
      l_error.severity := p_severity;
    END IF;

    l_error.suppress_warning := 'N';
    l_error.suppress_always  := 'N';
    l_error.help_text        := null;
    l_error.lang_code        := hil$profile.get_profile_value('LANGUAGE', user);
    l_error.raise_error      := p_raise;

    l_store := l_error;
    cg$errors.push(l_store);

    IF l_error.raise_error
    THEN
      raiseFailure;
    END IF;
  END internal_exception;

  --
  -- Purpose  Replace the old Headstart raised errors by a new
  --          error message
  --          unhandled exception on the stack
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE legacy_exception(p_error IN OUT hil$message.row_type, p_message IN VARCHAR2)
  IS
    l_start      NUMBER  (4, 0);
    l_end        NUMBER  (4, 0);
    l_paramno    NUMBER  (2, 0);
    l_code       VARCHAR2(10);
    l_message    VARCHAR2(2000);
  BEGIN
    l_message   := SUBSTR(p_message, instr(p_message, 'ORA-20000:') + 11);
    l_code      := SUBSTR(l_message, 1, c_errorlength);
    IF qms$errors.checkErrorFormat(l_code)
    THEN
      hil$message.get_message(p_error, l_code);
      -- replace parameters
      -- substitute message parameter
      l_start   := 1;
      l_paramno := 0;
      WHILE instr(l_message, '#', l_start) > 0
      LOOP
        l_paramno := l_paramno + 1;
        l_start   := instr(l_message, '#', l_start) + 2;
        l_end     := instr(l_message, '#', l_start) - 1;
        IF l_end = -1
        THEN
          -- this is the last parameter
          l_end := NVL(length (l_message), 0);
        END IF;
        p_error.msg_text  := replace( p_error.msg_text
                                    , '<p'||to_char(l_paramno)||'>'
                                    , SUBSTR (l_message, l_start, (l_end - l_start + 1))
                                    );
        p_error.help_text := replace(p_error.help_text
                                    , '<p'||to_char(l_paramno)||'>'
                                    , SUBSTR (l_message, l_start, (l_end - l_start + 1))
                                    );
        l_start := l_end + 1;
      END LOOP;
    ELSE
      p_error.code             := '';
      p_error.msg_text         := l_message;
      p_error.severity         := 'E';
      p_error.suppress_warning := 'N';
      p_error.suppress_always  := 'N';
      p_error.help_text        := null;
      p_error.lang_code        := hil$profile.get_profile_value('LANGUAGE', user);
      p_error.raise_error      := true;
    END IF;
  END legacy_exception;

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
  PROCEDURE show_message( p_message     IN VARCHAR2
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
                      )
  IS
    l_error hil$message.row_type;
    l_store hil$message.row_type;
  BEGIN
    -- get error message
    IF checkErrorFormat(p_message)
    THEN
      hil$message.get_message(l_error, p_message);
    ELSE
      l_error.msg_text := p_message;
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
        internal_exception(qms$invalid_errortype);
      END IF;
    END IF;

    IF p_raise IS NOT NULL
    THEN
      l_error.raise_error := p_raise;
    END IF;

    -- replace parameters
    l_error.msg_text := replace(l_error.msg_text, '<p1>', p_param0);
    l_error.msg_text := replace(l_error.msg_text, '<p2>', p_param1);
    l_error.msg_text := replace(l_error.msg_text, '<p3>', p_param2);
    l_error.msg_text := replace(l_error.msg_text, '<p4>', p_param3);
    l_error.msg_text := replace(l_error.msg_text, '<p5>', p_param4);
    l_error.msg_text := replace(l_error.msg_text, '<p6>', p_param5);
    l_error.msg_text := replace(l_error.msg_text, '<p7>', p_param6);
    l_error.msg_text := replace(l_error.msg_text, '<p8>', p_param7);
    l_error.msg_text := replace(l_error.msg_text, '<p9>', p_param8);
    l_error.msg_text := replace(l_error.msg_text, '<p10>', p_param9);

    l_error.help_text := replace(l_error.help_text, '<p1>', p_param0);
    l_error.help_text := replace(l_error.help_text, '<p2>', p_param1);
    l_error.help_text := replace(l_error.help_text, '<p3>', p_param2);
    l_error.help_text := replace(l_error.help_text, '<p4>', p_param3);
    l_error.help_text := replace(l_error.help_text, '<p5>', p_param4);
    l_error.help_text := replace(l_error.help_text, '<p6>', p_param5);
    l_error.help_text := replace(l_error.help_text, '<p7>', p_param6);
    l_error.help_text := replace(l_error.help_text, '<p8>', p_param7);
    l_error.help_text := replace(l_error.help_text, '<p9>', p_param8);
    l_error.help_text := replace(l_error.help_text, '<p10>', p_param9);

    l_error.table_name  := p_table_name;
    l_error.table_rowid := p_table_rowid;

    l_store := l_error;
    cg$errors.push(l_store);

    IF l_error.raise_error
    THEN
      raiseFailure;
    ELSIF l_error.severity = 'E'
--    AND   qms$transaction.transaction_open
    THEN
      cg$errors.set_qms_error(true);
      -- An error is raised during an open transaction, but raise_failure is false.
      -- It is assumed that the error must be put on the stack
      -- and that close transaction needs to see the error.
      -- The first part is already done,
      -- so we only need to set a flag in cg$errors for the second part.
      cg$errors.raise_failure;
    END IF;
  END show_message;


  PROCEDURE raiseFailure
  IS
  BEGIN
    raise qms$exception;
  EXCEPTION
    WHEN OTHERS
    THEN
      raise_application_error(-20998, 'Transaction Failed');
  END raiseFailure;


-- For some reason, we need to close the package body here. Designer will remove it
-- when it generates the .pkb file.
END qms$errors;
/
SHOW ERROR