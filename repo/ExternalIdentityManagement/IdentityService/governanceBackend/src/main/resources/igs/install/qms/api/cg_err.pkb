-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\cg_err.pkb
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Implementation 'cg$errors'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE or REPLACE PACKAGE BODY cg$errors
IS
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private constants
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/* Revision label of package */
REVISION_LABEL               CONSTANT VARCHAR2( 8) := '2.0.0.0';
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private varables
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  cg$err_msg       cg$err_msg_t;
  cg$err_error     cg$err_error_t;
  cg$err_msg_type  cg$err_msg_type_t;
  cg$err_msgid     cg$err_msgid_t;
  cg$err_loc       cg$err_loc_t;

  qms$err_tab      hil$message.hil$table_type;
  qms$err_rec      hil$message.hil$row_type;
  qms$empty        hil$message.hil$row_type;

  g_failure_raised BOOLEAN := false;
  g_qms_error      BOOLEAN := false;
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private program units
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
--
-- Purpose  Verify if a constraint error is present in the error message.
--          If an constraint error is found, return the error code (not the
--          constraint name), else return 0.
--          The following constraints are verified :
--            o ORA-02090
--            o ORA-02091
--            o ORA-02092
--            o ORA-02090
--            o ORA-00001
--
-- Usage    parameters
--            p_error       : Error message
--            p_code : returns the error number if it is a constraint, otherwise 0.
--
-- Remarks  -
--
PROCEDURE checkconstrainterror(p_error IN VARCHAR2, p_code IN OUT NUMBER)
IS
BEGIN
  IF (INSTR(p_error, 'ORA-'||TO_CHAR(ABS(ERR_UNIQUE_KEY), 'FM09999')) > 0)
  THEN
    p_code := ERR_UNIQUE_KEY;
  ELSIF (instr (p_error, 'ORA-'||TO_CHAR(ABS(ERR_DELETE_RESTRICT), 'FM09999')) > 0)
  THEN
    p_code := ERR_DELETE_RESTRICT;
  ELSIF (instr (p_error, 'ORA-'||TO_CHAR(ABS(ERR_FOREIGN_KEY), 'FM09999')) > 0)
  THEN
    p_code := ERR_FOREIGN_KEY;
  ELSIF (instr (p_error, 'ORA-'||TO_CHAR(ABS(ERR_CHECK_CON), 'FM09999')) > 0)
  THEN
    p_code := ERR_CHECK_CON;
  ELSE
    p_code := 0;
  END IF;
END checkconstrainterror;
--------------------------------------------------------------------------------
-- Name:        raise_error
-- Description: Pops all messages off the stack and returns them in the
--              reverse order in which they were raised.
--
-- Parameters:  none
--
-- Returns:     the messages
--------------------------------------------------------------------------------
FUNCTION raise_error
  RETURN VARCHAR2
IS
  l_length     CONSTANT INTEGER := 32767;
  l_next       VARCHAR2(512);
  l_aggregated VARCHAR2(32767);
BEGIN
  -- return errors in reverse order like errors
  FOR i IN REVERSE 1..cg$err_tab_i - 1
  LOOP
    IF l_aggregated IS NULL
    THEN
      l_aggregated := 'TAPI-' || TO_CHAR(cg$err_msgid(i)) || ':' || cg$err_msg(i);
    ELSE
      IF ((length(l_aggregated)+ length(cg$err_msg(i)) + length(to_char(cg$err_msgid(i))) + 16) < l_length)  -- B2401878 : add length check
      THEN
        l_aggregated := l_aggregated || chr(10) || '           TAPI-' || to_char(cg$err_msgid(i)) || ':' || cg$err_msg(i);
      END IF;
    END IF;
  END LOOP;
  RETURN l_aggregated;
END;
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- public program units
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
-- Name:        revision
-- Description: Returns the revision label of this package
--
-- Parameters:  none
--
-- Returns:     the revision label
--------------------------------------------------------------------------------
FUNCTION revision
 RETURN VARCHAR2
IS
BEGIN
  RETURN REVISION_LABEL;
END revision;
--------------------------------------------------------------------------------

-- Name:        display_string
-- Description: Create a display string IN the format
--              'severity msg_code: msg_text' in the appropriate end user
--              language.
--
-- Usage        Use the function to display error messages to the end user.
-- Parameters:  p_code
--              p_text
--              p_type
--
--
-- Remarks
--
--------------------------------------------------------------------------------

FUNCTION display_string( p_code IN VARCHAR2
                       , p_text IN VARCHAR2
                       , p_type IN VARCHAR2
                       )
  RETURN VARCHAR2
IS
  l_severity VARCHAR2(100);
BEGIN
  l_severity := hil$message.severity_string(p_type);
  RETURN SUBSTR( l_severity
              ||' '
              ||p_code
              ||': '
              ||p_text
              , 1
              , 512
              );
END display_string;
--------------------------------------------------------------------------------
-- Name:        clear
-- Description: Clears the stack
--
-- Parameters:  none
--------------------------------------------------------------------------------
PROCEDURE clear
IS
BEGIN
  cg$err_tab_i := 1;
END clear;
--------------------------------------------------------------------------------
-- Name:        push
-- Description: Put a message on stack with full info
--
-- Parameters:  p_msg   the text message
--              p_error ERRor or WARNing
--              p_type  ORA, API or user TLA
--              p_msg   the id of message
--              p_loc   the location where error occured
--
-- Remarks      p_unexpected added as workaround for bug 649440 unhandled
--              exceptions in cascade procedures not handled correctly.
--              Check constraints are passed directly to the push PROCEDURE and
--              don't resolve the error message (code in Designer) by calling
--              msggettext need to workaround this before the issue get solved in
--              Designer.
--------------------------------------------------------------------------------
PROCEDURE push( p_msg        IN VARCHAR2
              , p_error      IN VARCHAR2 DEFAULT 'E'
              , p_type       IN VARCHAR2 DEFAULT NULL
              , p_msgid      IN INTEGER  DEFAULT 0
              , p_loc        IN VARCHAR2 DEFAULT NULL
              , p_unexpected IN VARCHAR2 DEFAULT SQLERRM
              )
IS
  l_msg   VARCHAR2 (2000) := p_msg;
  l_code  NUMBER  (32, 0) := 0;
  l_name  VARCHAR2   (60);
BEGIN
  IF p_type = 'API'
  THEN
    IF p_msgid = API_CK_CON_VIOLATED
    THEN
      hil$message.get_message(qms$err_rec, p_code => l_msg);
    ELSIF p_msgid = API_MAND_COLUMN_ISNULL
    THEN
      -- retrieve correct message code for display to user
      -- Procedure validate_mandatory in the TAPI already fetched
      -- the correct message text
      l_msg := VAL_MAND;
    ELSIF p_msgid = API_FOREIGN_KEY_TRANS
    THEN
      l_msg := qms$err_rec.code;
    ELSIF p_msgid = API_UNIQUE_KEY_UPDATE
    THEN
      l_msg := qms$err_rec.code;
    END IF;
  ELSE
    -- initialize
    qms$err_rec.text             := l_msg;
    qms$err_rec.severity         := p_error;
    qms$err_rec.severity_desc    := hil$message.severity_string(p_error);
    qms$err_rec.suppress_warning := 'N';
    qms$err_rec.suppress_always  := 'N';
    qms$err_rec.logging          := 'N';
    qms$err_rec.help             := NULL;
    qms$err_rec.language         := 'en';
  END IF;

  IF  p_type = 'ORA'
  AND ABS(p_msgid) <> 20998
  THEN
    checkconstrainterror(l_msg, l_code);
    IF l_code <> 0
    THEN
      l_name := parse_constraint(l_msg, l_code);
      hil$message.get_message(qms$err_rec, p_constraint => l_name);
    ELSE
      -- API has an unhanled exception v_msg in PROCEDURE loc
      hil$message.get_message(qms$err_rec, p_code => 'QMS-00100');
      qms$err_rec.text := replace(qms$err_rec.text, '<p1>', p_msg);
      qms$err_rec.text := replace(qms$err_rec.text, '<p2>', p_loc);
      qms$err_rec.help := replace(qms$err_rec.help, '<p1>', p_msg);
      qms$err_rec.help := replace(qms$err_rec.help, '<p2>', p_loc);
    END IF;
  END IF;
  -- don't store the record again if it was an developer raised
  -- error using qms$errors.show
  IF  p_type = 'ORA'
  AND ABS(p_msgid) = 20998
  THEN
    qms$err_rec := qms$empty;
  ELSE
    cg$err_msg(cg$err_tab_i)      := display_string(l_msg, qms$err_rec.text, p_error);
    cg$err_error(cg$err_tab_i)    := p_error;
    cg$err_msg_type(cg$err_tab_i) := p_type;
    cg$err_msgid(cg$err_tab_i)    := p_msgid;
    cg$err_loc(cg$err_tab_i)      := p_loc;
    IF qms$err_rec.severity = 'E'
    THEN
      qms$err_rec.raise_error := true;
    ELSE
      qms$err_rec.raise_error := false;
    END IF;

    qmsrecord2stack(qms$err_rec);

    cg$err_tab_i := cg$err_tab_i + 1;
  END IF;
END push;
--------------------------------------------------------------------------------
-- Name:        pop
-- Description: Take a message off stack
--
-- Parameters:  p_msg    the text message
--
-- Returns:     TRUE     message popped successfully
--              FALSE    stack was empty
--------------------------------------------------------------------------------
FUNCTION pop(p_msg OUT VARCHAR2)
  RETURN BOOLEAN
IS
BEGIN
  IF (cg$err_tab_i > 1 AND cg$err_msg(cg$err_tab_i - 1) IS NOT NULL)
  THEN
    cg$err_tab_i := cg$err_tab_i - 1;
    p_msg        := cg$err_msg(cg$err_tab_i);
    cg$err_msg(cg$err_tab_i) := '';
    RETURN TRUE;
  ELSE
    RETURN FALSE;
  END IF;
END pop;
--------------------------------------------------------------------------------
-- Name:        pop (overload)
-- Description: Take a message off stack with full info
--
-- Parameters:  p_msg    the text message
--              p_error  ERRor or WARNing
--              p_type   ORA, API or user TLA
--              p_msgid  the id of message
--              p_loc    the location where error occured
--
-- Returns:     TRUE     message popped successfully
--              FALSE    stack was empty
--------------------------------------------------------------------------------
FUNCTION pop( p_msg   OUT VARCHAR2
            , p_error OUT VARCHAR2
            , p_type  OUT VARCHAR2
            , p_msgid OUT INTEGER
            , p_loc   OUT VARCHAR2
            )
  RETURN BOOLEAN
IS
BEGIN
  IF (cg$err_tab_i > 1 AND cg$err_msg(cg$err_tab_i - 1) IS NOT NULL)
  THEN
    cg$err_tab_i := cg$err_tab_i - 1;
    p_msg   := cg$err_msg(cg$err_tab_i);
    p_error := cg$err_error(cg$err_tab_i);
    p_type  := cg$err_msg_type(cg$err_tab_i);
    p_msgid := cg$err_msgid(cg$err_tab_i);
    p_loc   := cg$err_loc(cg$err_tab_i);
    cg$err_msg(cg$err_tab_i) := '';
    RETURN TRUE;
  ELSE
    RETURN FALSE;
  END IF;
END pop;
--------------------------------------------------------------------------------
-- Name:        pop_head
-- Description: Take a message off stack from head
--
-- Parameters:  p_msg    the text message
--
-- Returns:     TRUE     message popped successfully
--              FALSE    stack was empty
--------------------------------------------------------------------------------
FUNCTION pop_head(p_msg OUT VARCHAR2)
  RETURN BOOLEAN
IS
BEGIN
  IF (cg$err_tab_i > 1 AND cg$err_msg(cg$err_tab_i - 1) IS NOT NULL)
  THEN
    p_msg := cg$err_msg(1);
    FOR i IN 1..cg$err_tab_i - 2
    LOOP
      cg$err_msg(i)      := cg$err_msg(i + 1);
      cg$err_error(i)    := cg$err_error(i + 1);
      cg$err_msg_type(i) := cg$err_msg_type(i + 1);
      cg$err_msgid(i)    := cg$err_msgid(i + 1);
      cg$err_loc(i)      := cg$err_loc(i + 1);
    END LOOP;
    --
    cg$err_tab_i := cg$err_tab_i - 1;
    cg$err_msg(cg$err_tab_i) := '';
    RETURN TRUE;
  ELSE
    RETURN FALSE;
  END IF;
END pop_head;
--------------------------------------------------------------------------------
-- Name:        pop_head (overload)
-- Description: Take a message off stack from head with full info
--
-- Parameters:  p_msg    the text message
--              p_error  ERRor or WARNing
--              p_type   ORA, API or user TLA
--              p_msgid  the id of message
--              p_loc    the location where error occured
--
-- Returns:     TRUE     message popped successfully
--              FALSE    stack was empty
--------------------------------------------------------------------------------
FUNCTION pop_head( p_msg    OUT VARCHAR2
                 , p_error  OUT VARCHAR2
                 , p_type   OUT VARCHAR2
                 , p_msgid  OUT INTEGER
                 , p_loc    OUT VARCHAR2
                 )
  RETURN BOOLEAN
IS
BEGIN
  IF (cg$err_tab_i > 1 AND cg$err_msg(cg$err_tab_i - 1) IS NOT NULL)
  THEN
    p_msg   := cg$err_msg(1);
    p_error := cg$err_error(1);
    p_type  := cg$err_msg_type(1);
    p_msgid := cg$err_msgid(1);
    p_loc   := cg$err_loc(1);
    FOR i IN 1..cg$err_tab_i - 2
    LOOP
      cg$err_msg(i)      := cg$err_msg(i + 1);
      cg$err_error(i)    := cg$err_error(i + 1);
      cg$err_msg_type(i) := cg$err_msg_type(i + 1);
      cg$err_msgid(i)    := cg$err_msgid(i + 1);
      cg$err_loc(i)      := cg$err_loc(i + 1);
    END LOOP;
    --
    cg$err_tab_i := cg$err_tab_i - 1;
    cg$err_msg(cg$err_tab_i) := '';
    RETURN TRUE;
  ELSE
    RETURN FALSE;
  END IF;
END pop_head;
--------------------------------------------------------------------------------
-- Name:        push
-- Description: Put a headstart message on stack with full info
--
-- Usage   Called by the error handling routines in server version of
--         qms$errors.
--         Parameters: p_error  the headstart message info
--
-- Remarks
--
--------------------------------------------------------------------------------
PROCEDURE push(p_error IN OUT hil$message.hil$row_type)
IS
BEGIN
  cg$err_msg(cg$err_tab_i)      := display_string(p_error.code, p_error.text, p_error.severity);
  cg$err_error(cg$err_tab_i)    := p_error.severity;
  cg$err_msg_type(cg$err_tab_i) := 'QMS';
  cg$err_msgid(cg$err_tab_i)    := 0;
  cg$err_loc(cg$err_tab_i)      := '';

  qmsrecord2stack (p_error);

  cg$err_tab_i := cg$err_tab_i + 1;
END push;
--------------------------------------------------------------------------------
-- Purpose  Put the global record onto the QMS Headstart error stack. This way the
--          QMS error information is available to the client simular to the
--          Designer/2000 error stack.
--
-- Usage    Procedure is called by each push procedure/function in the package to make sure
--          that the QMS error stack is synchronised with the Designer/2000 error stack.
--
-- Remarks
--         TO DO !! This procedure needs to do the tracing/logging of the errors to a file !!!
--
--------------------------------------------------------------------------------
PROCEDURE qmsrecord2stack(p_error IN OUT hil$message.hil$row_type)
IS
BEGIN
  qms$err_tab(cg$err_tab_i).code             := p_error.code;
  qms$err_tab(cg$err_tab_i).severity         := p_error.severity;
  qms$err_tab(cg$err_tab_i).severity_desc    := p_error.severity_desc;
  qms$err_tab(cg$err_tab_i).suppress_warning := p_error.suppress_warning;
  qms$err_tab(cg$err_tab_i).suppress_always  := p_error.suppress_always;
  qms$err_tab(cg$err_tab_i).logging          := p_error.logging;
  qms$err_tab(cg$err_tab_i).text             := p_error.text;
  qms$err_tab(cg$err_tab_i).help             := p_error.help;
  qms$err_tab(cg$err_tab_i).language         := p_error.language;
  qms$err_tab(cg$err_tab_i).raise_error      := p_error.raise_error;
  qms$err_tab(cg$err_tab_i).table_name       := p_error.table_name;
  qms$err_tab(cg$err_tab_i).table_rowid      := p_error.table_rowid;

  p_error := qms$empty;
END qmsrecord2stack;
--------------------------------------------------------------------------------
--
-- Purpose  Returns the information of the last error taken from the stack.
--          The information of the last error is stored into the global
--          record structure.
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
  p_error := p_error;
END pop_qms_errorinfo;
--------------------------------------------------------------------------------
-- Purpose Make a distinction between a QMS call to raise_failure
--         and a direct TAPI call to raise_failure
--
-- Usage   Set qms error to true in qms$errors.show, just before raise_failure
--------------------------------------------------------------------------------
PROCEDURE set_qms_error(p_value BOOLEAN)
IS
BEGIN
  g_qms_error := p_value;
END set_qms_error;
--------------------------------------------------------------------------------
-- Name:        errors
-- Description: Pops all messages off the stack and returns them in the
--              reverse order in which they were raised.
--
-- Parameters:  none
--
-- Returns:     The messages
--------------------------------------------------------------------------------
FUNCTION errors
  RETURN VARCHAR2
IS
  l_length     CONSTANT INTEGER := 32767;
  l_next       VARCHAR2(512);
  l_adgregated VARCHAR2(32767);
BEGIN
  WHILE cg$errors.pop(l_next)
  LOOP
    IF l_adgregated IS NULL
    THEN
      l_adgregated := l_next;
    ELSE
      IF ((length(l_adgregated) + length(l_next) + 4) < l_length)
      THEN
        l_adgregated := l_next || chr(10) || '   ' || l_adgregated;
      END IF;
    END IF;
  END LOOP;
  RETURN l_adgregated;
END;
--------------------------------------------------------------------------------
-- Name:        string
-- Description: Provides a mechanism for text translation.
--
-- Parameters:  p_msgid         the Id of the message
--              p_default       the Default Text
--              p_param1 (to 4) the substitution strings
--
-- Returns:		Translated message
--------------------------------------------------------------------------------
FUNCTION string( p_msgid    IN NUMBER
               , p_default  IN VARCHAR2 DEFAULT NULL
               , p_param1   IN VARCHAR2 DEFAULT NULL
               , p_param2   IN VARCHAR2 DEFAULT NULL
               , p_param3   IN VARCHAR2 DEFAULT NULL
               , p_param4   IN VARCHAR2 DEFAULT NULL
               )
  RETURN VARCHAR2
IS
  l_temp VARCHAR2(10000) := p_default;
BEGIN
  l_temp := replace(l_temp, '<p>',  p_param1);
  l_temp := replace(l_temp, '<p1>', p_param1);
  l_temp := replace(l_temp, '<p2>', p_param2);
  l_temp := replace(l_temp, '<p3>', p_param3);
  l_temp := replace(l_temp, '<p4>', p_param4);
  return l_temp;
END string;
--------------------------------------------------------------------------------
-- Name:        parse_constraint
-- Description: Isolate constraint name from an Oracle error message
--
-- Parameters:  msg     the actual Oracle error message
--              type    the type of constraint to find
--                      (ERR_FOREIGN_KEY     Foreign key,
--                       ERR_CHECK_CON       Check,
--                       ERR_UNIQUE_KEY      Unique key,
--                       ERR_DELETE_RESTRICT Restricted delete)
--
-- Returns:              the name of the constraint found (NULL if none found)
--------------------------------------------------------------------------------
FUNCTION parse_constraint(msg  IN VARCHAR2, type IN INTEGER)
  RETURN VARCHAR2
IS
  con_name VARCHAR2(100) := '';
BEGIN
  IF (type = ERR_FOREIGN_KEY
  OR  type = ERR_CHECK_CON
  OR  type = ERR_UNIQUE_KEY
  OR  type = ERR_DELETE_RESTRICT
  )
  THEN
    con_name := substr(msg, instr(msg, '.') + 1, instr(msg, ')') - instr(msg, '.') - 1);
  END IF;
  RETURN con_name;
END;
--------------------------------------------------------------------------------
-- Name:        raise_failure
--
-- Description: To raise the cg$error failure exception handler
--------------------------------------------------------------------------------
PROCEDURE raise_failure
IS
BEGIN
  IF      qms$transaction.transaction_open
  AND NOT qms$transaction.close_transaction_failed
  THEN
    -- if it is not a qms error but we are within a transaction then check if
    -- call was made by QMS or TAPI
    IF g_qms_error
    THEN
      -- it was a qms_error, reset the flag and record there was a transaction error
      g_qms_error      := false;
      g_failure_raised := true;
    ELSE
      -- The TAPI called raise_failure directly, which means there was a server
      -- constraint violation, and the transaction could not be closed.
      -- An exception must be raised.
      -- No need to close or abort transaction, because statement will fail if we
      -- raise an exception!
      raise cg$errors.cg$error;
    END IF;
  ELSE
    raise cg$errors.cg$error;
  END IF;
END raise_failure;

-----------------------------------------------------------------------------
--
-- Name:        failure_raised
-- Description: Return whether procedure raise_failure was invoked by one of
--              the TAPI packages.
--
-- Usage        Called by QMS_TRSNACTION_MGT when a transaction is closed.
--              If no custom rule violations were found, but this function
--              returns true, the transaction fails.
--
-- Remarks      After invocation, the flag that an error ocurred is reset to
--              false, this means that this function can only be invoked once
--              during a transaction!
--
-----------------------------------------------------------------------------
FUNCTION failure_raised
  RETURN BOOLEAN
IS
  l_failure_raised BOOLEAN := g_failure_raised;
BEGIN
  g_failure_raised := false;
  RETURN l_failure_raised;
END failure_raised;

END cg$errors;
/
SHOW ERROR