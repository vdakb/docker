-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppAplication\src\main\dbs\qms\api\cg-err.pkb
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

/*******************************************************************************
Purpose  cg$errors package used by the Table and Module Component API packages.
         All error handling routines are included in this package.
         The package has been changed to cover all Employee Portal Error/Message
         handling requirements.

Usage    This version of the package is needed to replace the standard version
         of cg$errors delivered with Designer/2000 R2.1

Remarks  With new versions of Designer/2000, the development team will verify
         changes in the standard cg$errors package and adapt their version.

Changes to Original cg$errors package specification (cdsapers.pks), see
cg$error_package_version for base version of the file.

  Procedures/Functions Added :
    - FUNCTION  revision RETURN VARCHAR2
    - PROCEDURE pop               ( p_error IN OUT hil$message.row_type )
    - PROCEDURE push              ( p_error IN OUT hil$message.row_type )
    - PROCEDURE pop_error         ( p_error IN OUT hil$message.row_type )
    - PROCEDURE push_error        ( p_error IN OUT hil$message.row_type )
    - PROCEDURE get_error_messages( p_row_type_tbl IN OUT hil$message.message_type
                                  , p_message_count       IN OUT NUMBER
                                  , p_raise_error         IN OUT BOOLEAN
                                  )
    - FUNCTION  get_message_rectype( p_index IN NUMBER ) RETURN hil$message.row_type
    - FUNCTION  failure_raised return BOOLEAN
    - FUNCTION  get_display_string ( p_code IN VARCHAR2
                                   , p_text IN VARCHAR2
                                   , p_type IN VARCHAR2
                                   )
        RETURN VARCHAR2

  Procedures/Functions Changed :
    - PROCEDURE push ( msg        IN VARCHAR2
                     , error      IN VARCHAR2 DEFAULT 'E'
                     , msg_type   IN VARCHAR2 DEFAULT NULL
                     , msgid      IN INTEGER  DEFAULT 0
                     , loc        IN VARCHAR2 DEFAULT NULL
       Added ->      , unexpected IN VARCHAR2 DEFAULT SQLERRM
                     );
  Constants Changed
    - all standard api messages get as default value an error code instead of
      the error message. The original message is placed in comment after the
      error code.
      This is done as workaround for bug 683865 (msgno is incorrect when
      cg$errors.msggettext is called).

  Other changes
    - PROCEDURE raise_failure added pragma RESTRICT_REFERENCES, WNDS
    - FUNCTION parse_constraint added pragma RESTRICT_REFERENCES, WNDS

********************************************************************************/
PROMPT Creating Package Implementation 'cg$errors'
CREATE OR REPLACE PACKAGE BODY cg$errors
AS
  -- ---------------------------------------------------------------------------
  -- private types
  -- ---------------------------------------------------------------------------

  cg$err_msg       cg$err_msg_t;
  cg$err_error     cg$err_error_t;
  cg$err_msg_type  cg$err_msg_type_t;
  cg$err_msgid     cg$err_msgid_t;
  cg$err_loc       cg$err_loc_t;

  qms$err_rec      hil$message.message_type;

  g_error          hil$message.row_type;
  g_empty          hil$message.row_type;

  -- ---------------------------------------------------------------------------
  -- private constants
  -- ---------------------------------------------------------------------------
  revision_label   CONSTANT VARCHAR2(8) := '1.0.0.0';

  -- ---------------------------------------------------------------------------
  -- private variables
  -- ---------------------------------------------------------------------------
  g_failure_raised BOOLEAN := false;
  -- flag indicates that raise_failure was called by QMS
  g_qms_error      BOOLEAN := false;

  -- ---------------------------------------------------------------------------
  -- private program units
  -- ---------------------------------------------------------------------------

  --
  -- Purpose  Verify if a constraint error is present in the error message.
  --          If an constraint error is found, return the error code (not the
  --          constraint name), else return 0.
  --          The following constraints are verified : ORA-02090, ORA-02091,
  --          ORA-02092, ORA-02090 and ORA-00001
  --
  -- Usage    parameters
  --            p_errm       : Error message
  --            p_constrcode : returns the error number if it is a constraint, otherwise 0.
  --
  -- Remarks  -
  --
  PROCEDURE checkConstraintError ( p_error IN     VARCHAR2
                                 , p_code  IN OUT NUMBER
                                 )
  IS
  BEGIN
    IF (instr(p_error, 'ORA-'||to_char(abs(ERR_UNIQUE_KEY), 'FM09999')) > 0)
    THEN
      p_code := ERR_UNIQUE_KEY;
    ELSIF (instr(p_error, 'ORA-'||to_char(abs(ERR_DELETE_RESTRICT), 'FM09999')) > 0)
    THEN
      p_code := ERR_DELETE_RESTRICT;
    ELSIF (instr(p_error, 'ORA-'||to_char(abs(ERR_FOREIGN_KEY), 'FM09999')) > 0)
    THEN
      p_code := ERR_FOREIGN_KEY;
    ELSIF (instr(p_error, 'ORA-'||to_char(abs(ERR_CHECK_CON), 'FM09999')) > 0)
    THEN
      p_code := ERR_CHECK_CON;
    ELSE
      p_code := 0;
    END IF;
  END checkConstraintError;

  --
  -- Purpose  Take the last error from the error stack and put it into the
  --          global record structure. This global record structure will be
  --          returned to the client by another procedure.
  --          Remove the last error from the stack.
  --
  -- Usage    Procedure is called by each pop procedure/function in the package
  --          to make sure that the error stack is synchronised with the
  --          Designer/2000 error stack.
  --
  -- Remarks  -
  --
  PROCEDURE stack2record
  IS
  BEGIN
    g_error := g_empty;

    g_error.code             := qms$err_rec(cg$err_tab_i).code;
    g_error.severity         := qms$err_rec(cg$err_tab_i).severity;
    g_error.severity_desc    := qms$err_rec(cg$err_tab_i).severity_desc;
    g_error.suppress_warning := qms$err_rec(cg$err_tab_i).suppress_warning;
    g_error.suppress_always  := qms$err_rec(cg$err_tab_i).suppress_always;
    g_error.logging          := qms$err_rec(cg$err_tab_i).logging;
    g_error.msg_text         := qms$err_rec(cg$err_tab_i).msg_text;
    g_error.help_text        := qms$err_rec(cg$err_tab_i).help_text;
    g_error.lang_code        := qms$err_rec(cg$err_tab_i).lang_code;
    g_error.raise_error      := qms$err_rec(cg$err_tab_i).raise_error;
    g_error.table_name       := qms$err_rec(cg$err_tab_i).table_name;
    g_error.table_rowid      := qms$err_rec(cg$err_tab_i).table_rowid;

    qms$err_rec.delete(cg$err_tab_i);
  END stack2record;

  PROCEDURE qmsMsgGetText ( p_temp    IN OUT VARCHAR2
                          , p_default IN     VARCHAR2
                          , p_subst1  IN     VARCHAR2
                          , p_subst2  IN     VARCHAR2
                          , p_subst3  IN     VARCHAR2
                          , p_subst4  IN     VARCHAR2
                          )
  IS
    l_errcd VARCHAR2(100) := NULL;
  BEGIN
    -- check for message by comparing the message and if it is a constraint, read the text
    IF p_default IN (APIMSG_FK_VIOLAT, APIMSG_PK_VIOLAT, APIMSG_UK_VIOLAT, APIMSG_CK_VIOLAT)
    THEN
      -- uk/pk/fk constraint validation error, p_subst1 is constraint name, p_subst2 is table name
      hil$message.get_message(g_error, p_constraint => p_subst1);
    ELSIF p_default = APIMSG_ARC_MAND_VIOLAT
    THEN
      -- arc violation, missing an entry of arc (p_subst1) on table p_subst2
      l_errcd := p_subst2||'_ARC'||p_subst1||'_M';
      hil$message.get_message(g_error, p_constraint => l_errcd);
    ELSIF p_default = APIMSG_ARC_VIOLAT
    THEN
      -- arc violation, too many entries of arc (p_subst1) on table p_subst2
      l_errcd := p_subst2||'_ARC'||p_subst1||'_T';
      hil$message.get_message(g_error, p_constraint => l_errcd);
    ELSIF p_default = APIMSG_CASC_ERROR
    THEN
      -- p_subst1 contains cascade action : this is an unhandled exception.
      l_errcd := p_default;
      hil$message.Get_message (g_error, l_errcd);
    ELSIF p_default = ERR_DEL_RESTRICT
    THEN
      -- Delete failed because record of master table p_subst1 was used in detail table p_subst2
      -- 6.5.1.2  Note:
      -- p_subst1 and p_subst2 need not always be the table names,
      -- it can also be the display titles.
      -- That is complex to support, so we took this message type
      -- out of the User Guide.
      -- It does work if you put the display names in the message constraint name,
      -- (in uppercase!!)
      -- as long as it fits in the message constraint column (length 70)
      l_errcd := UPPER(p_subst1||'_'||p_subst2||'_CASDEL');
      hil$message.get_message(g_error, p_constraint => l_errcd);
    ELSIF p_default = ERR_UK_UPDATE
    THEN
      -- Unique key was updated where it wasn't allowed
      l_errcd := p_subst1||'_NU';
      hil$message.get_message(g_error, p_constraint => l_errcd);
    ELSIF p_default = ERR_FK_TRANS
    THEN
       -- Foreign Key was set as non transferable but was updated
       l_errcd := p_subst1||'_NT';
       hil$message.get_message(g_error, p_constraint => l_errcd);
    ELSE
      -- replace API error codes with a OFG error message
      l_errcd := p_default;
      hil$message.Get_message (g_error, l_errcd);
   END IF;

    -- if error code was not found and we were replacing the default message
    -- with a more meaningfull message, use the default message.
    IF  (g_error.code = 'QMS-00102')
    AND (p_default IN (ERR_DEL_RESTRICT, ERR_UK_UPDATE, ERR_FK_TRANS, APIMSG_ARC_VIOLAT, APIMSG_ARC_MAND_VIOLAT))
    THEN
      l_errcd := p_default;
      hil$message.get_message(g_error, l_errcd);
    END IF;

    p_temp := g_error.msg_text;

    g_error.msg_text := replace(g_error.msg_text, '<p>',  p_subst1);
    g_error.msg_text := replace(g_error.msg_text, '<p1>', p_subst1);
    g_error.msg_text := replace(g_error.msg_text, '<p2>', p_subst2);
    g_error.msg_text := replace(g_error.msg_text, '<p3>', p_subst3);
    g_error.msg_text := replace(g_error.msg_text, '<p4>', p_subst4);

    g_error.help_text := replace(g_error.help_text, '<p>',  p_subst1);
    g_error.help_text := replace(g_error.help_text, '<p1>', p_subst1);
    g_error.help_text := replace(g_error.help_text, '<p2>', p_subst2);
    g_error.help_text := replace(g_error.help_text, '<p3>', p_subst3);
    g_error.help_text := replace(g_error.help_text, '<p4>', p_subst4);
  END qmsMsgGetText;

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
  -- Purpose If CDM RuleFrame transaction is open: set package
  --         variable that error has been raised, instead of raising
  --         exception. If no transaction is open, raise failure.
  --
  -- Usage   Called by all TAPI routines hat encounter an error
  --
  -- Remarks When a transaction is open, no exception is raised to allow
  --         all rule violations (custom and TAPI rules) to be presented
  --         to the user at once.
  --
  PROCEDURE raise_failure
  IS
  BEGIN
    raise cg$errors.cg$error;
  END raise_failure;

  --
  -- Purpose return whether procedure raise_failure was invoked by one
  --         of the TAPI packages.
  --
  -- Usage   Called by QMS_TRANSCTION_MGT when a transaction is closed.
  --         If no custom rule violations were found, but this function returns
  --         true, the transaction fails.
  --
  -- Remarks After invocation, the flag that an error ocurred is reset to false,
  --         This means that this function can only be invoked once during a transaction!
  --
  FUNCTION failure_raised
    RETURN BOOLEAN
  IS
    l_failure_raised BOOLEAN := g_failure_raised;
  BEGIN
    g_failure_raised := false;
    RETURN l_failure_raised;
  END failure_raised;

  --
  -- Purpose Make a distinction between a QMS call to raise_failure
  --         and a direct TAPI call to raise_failure
  --
  -- Usage   Set extended error to true in qms$errors.show_message, just before
  --         raise_failure
  --
  -- Remarks -
  --
  PROCEDURE set_qms_error(p_value BOOLEAN)
  IS
  BEGIN
    g_qms_error := p_value;
  END set_qms_error;

  --
  -- Purpose Clears the stack
  --
  -- Usage   -
  --
  -- Remarks -
  --
  PROCEDURE clear
  IS
  BEGIN
    cg$err_tab_i := 1;
    -- QMS : Line added to clear the QMS error stack together with the cg$error stack
    qms$err_rec.delete;
  END clear;

  --
  -- Purpose Put a message on stack with full info
  --
  -- Usage   Called by the Module Component and Table API packages
  --         Parameters : msg      Text message
  --                      error    error or WARNing
  --                      msg_type ORA, API or user TLA
  --                      msg_id   Id of message
  --                      loc      Location where error occured
  --
  -- Remarks unexpected added as workaround for bug 649440 : unhandled exceptions
  --         in cascade procedures not handled correctly
  --         Check constraints are passed directly to the push PROCEDURE and don't
  --         resolve the error message (code in Designer) by calling msggettext
  --         need to workaround this before the issue get solved in Designer
  --
  PROCEDURE push( msg        IN VARCHAR2
                , error      IN VARCHAR2 DEFAULT 'E'
                , msg_type   IN VARCHAR2 DEFAULT NULL
                , msgid      IN INTEGER  DEFAULT 0
                , loc        IN VARCHAR2 DEFAULT NULL
                , unexpected IN VARCHAR2 DEFAULT SQLERRM
                )
  IS
    c_casforeign CONSTANT VARCHAR2 (20) := 'Foreign Key ';
    c_casupdate  CONSTANT VARCHAR2 (20) := 'violated on update';
    c_casdelete  CONSTANT VARCHAR2 (20) := 'violated on delete';
    c_casmissing CONSTANT VARCHAR2 (20) := 'Value required for';

    l_msgcode   NUMBER  (32, 0) := 0;
    l_conname   VARCHAR2(60);
    l_msgtype   VARCHAR2 (3)    := msg_type;
    l_error     VARCHAR2 (1)    := error;
    l_msg       VARCHAR2 (2000) := msg;
  BEGIN
    IF l_msgtype = 'API'
    THEN
      IF msgid = API_CK_CON_VIOLATED
      THEN
        hil$message.get_message(g_error, p_code => l_msg);
      ELSIF msgid = API_MAND_COLUMN_ISNULL
      THEN
        -- retrieve correct message code for display to user
        -- Procedure valiudate_mandatory in the TAPI already fetched
        -- the correct message text
        l_msg := VAL_MAND;
      ELSIF msgid = API_FOREIGN_KEY_TRANS
      THEN
        l_msg := g_error.code;
      ELSIF msgid = API_UNIQUE_KEY_UPDATE
      THEN
        l_msg := g_error.code;
      END IF;
    ELSE
      -- initialise g_error
      g_error.msg_text         := l_msg;
      g_error.severity         := l_error;
      g_error.severity_desc    := hil$message.get_severity_string(l_error);
      g_error.suppress_warning := 'N';
      g_error.suppress_always  := 'N';
      g_error.logging          := 'N';
      g_error.help_text        := NULL;
      g_error.lang_code        := hil$profile.get_profile_value ('LANGUAGE', user);
    END IF;

    IF  l_msgtype = 'ORA'
    AND abs (msgid) <> 20998
    THEN
      checkconstrainterror(l_msg, l_msgcode);
      IF l_msgcode <> 0
      THEN
        l_conname := parse_constraint(l_msg, l_msgcode);
        hil$message.get_message(g_error, p_constraint => l_conname);
      ELSE
        IF instr(l_msg, 'ORA-20000') <> 0
        THEN
          qms$errors.legacy_exception(g_error, l_msg);
        ELSE
          -- API has an unhanled exception v_msg in PROCEDURE loc
          hil$message.get_message( g_error
                                 , p_code     => 'QMS-00100'
                                 , p_language => hil$profile.get_profile_value ('LANGUAGE', user)
                                 );
          g_error.msg_text  := replace(g_error.msg_text,  '<p1>', msg);
          g_error.msg_text  := replace(g_error.msg_text,  '<p2>', loc);
          g_error.help_text := replace(g_error.help_text, '<p1>', msg);
          g_error.help_text := replace(g_error.help_text, '<p2>', loc);
        END IF;
      END IF;
    END IF;

    -- don't store the record again if it was an developer raised
    -- error using qms$errors.show_message
    IF  l_msgtype = 'ORA'
    AND abs(msgid) = 20998
    THEN
      g_error := g_empty;
    ELSE
      cg$err_msg(cg$err_tab_i)      := get_display_string(l_msg, g_error.msg_text, l_error);
      cg$err_error(cg$err_tab_i)    := l_error;
      cg$err_msg_type(cg$err_tab_i) := l_msgtype;
      cg$err_msgid(cg$err_tab_i)    := msgid;
      cg$err_loc(cg$err_tab_i)      := loc;

      IF g_error.severity = 'E'
      THEN
        g_error.raise_error := true;
      ELSE
        g_error.raise_error := false;
      END IF;

      push_error(g_error);

      cg$err_tab_i := cg$err_tab_i + 1;
    END IF;
  END push;

  --
  -- Purpose Put a message on stack with full info
  --
  -- Usage   Called by the error handling routines in server version of
  --         qms$errors.
  --         Parameters : p_error message info
  --
  -- Remarks -
  --
  PROCEDURE push(p_error IN OUT hil$message.row_type)
  IS
  BEGIN
    cg$err_msg(cg$err_tab_i)      := get_display_string(p_error.code, p_error.msg_text, p_error.severity);
    cg$err_error(cg$err_tab_i)    := p_error.severity;
    cg$err_msg_type(cg$err_tab_i) := 'QMS';
    cg$err_msgid(cg$err_tab_i)    := 0;
    cg$err_loc(cg$err_tab_i)      := '';

    push_error(p_error);

    cg$err_tab_i := cg$err_tab_i + 1;
  END push;

  --
  -- Purpose Put the global record onto the error stack.
  --         This way the error information is available to the client simular
  --         to the Designer/2000 error stack.
  --
  -- Usage   Procedure is called by each push procedure/function in the package
  --         to make sure that the error stack is synchronised with the
  --         Designer/2000 error stack.
  --
  -- Remarks This procedure needs to do the tracing/logging of the errors to a
  --         file !!!
  --
  --
  PROCEDURE push_error(p_error IN OUT hil$message.row_type)
  IS
  BEGIN
    qms$err_rec(cg$err_tab_i).code             := p_error.code;
    qms$err_rec(cg$err_tab_i).severity         := p_error.severity;
    qms$err_rec(cg$err_tab_i).severity_desc    := p_error.severity_desc;
    qms$err_rec(cg$err_tab_i).suppress_warning := p_error.suppress_warning;
    qms$err_rec(cg$err_tab_i).suppress_always  := p_error.suppress_always;
    qms$err_rec(cg$err_tab_i).logging          := p_error.logging;
    qms$err_rec(cg$err_tab_i).msg_text         := p_error.msg_text;
    qms$err_rec(cg$err_tab_i).help_text        := p_error.help_text;
    qms$err_rec(cg$err_tab_i).lang_code        := p_error.lang_code;
    qms$err_rec(cg$err_tab_i).raise_error      := p_error.raise_error;
    qms$err_rec(cg$err_tab_i).table_name       := p_error.table_name;
    qms$err_rec(cg$err_tab_i).table_rowid      := p_error.table_rowid;

    p_error := g_empty;
  END push_error;

  --
  -- Purpose  Takes the error message that was last raised off the stack
  --          and removes it from the error stack.
  --
  -- Usage    function is called from the on-error trigger in OF or by the WSG
  --          Parameters : msg     Text message
  --          Return     : TRUE    Message popped successfully
  --                      FALSE   Stack was empty
  --
  -- Remarks  -
  --
  FUNCTION pop(msg OUT VARCHAR2)
    RETURN NUMBER
  IS
  BEGIN
    IF  cg$err_tab_i > 1
    AND cg$err_msg(cg$err_tab_i - 1) IS NOT NULL
    THEN
      cg$err_tab_i := cg$err_tab_i - 1;
      msg          := cg$err_msg(cg$err_tab_i);
      -- added to pop message info from stack into a PL/SQL record
      stack2record;
      cg$err_msg(cg$err_tab_i) := '';
      RETURN 1;
    ELSE
      RETURN 0;
    END IF;
  END pop;

  --
  -- Purpose  Takes the error message that was last raised off stack with full
  --          info and removes it from the error stack.
  --
  -- Usage    Parameters :  msg      text message
  --                        error    error or warning
  --                        msg_type ORA, API or user TLA
  --                        msg_id   Id of message
  --                        loc      Location where error occured
  --          Return     :  TRUE     Message popped successfully
  --                        FALSE    Stack was empty
  --
  -- Remarks  Call to PROCEDURE PopQMSStack2Reocrd added to retrieve information
  --          from the QMS error PL/SQL Table stack into a PL/SQL record that
  --          will be retrieved by the client side error hanlding routines
  --
  FUNCTION pop( msg      OUT VARCHAR2
              , error    OUT VARCHAR2
              , msg_type OUT VARCHAR2
              , msgid    OUT INTEGER
              , loc      OUT VARCHAR2
              )
    RETURN NUMBER
  IS
  BEGIN
    IF  cg$err_tab_i > 1
    AND cg$err_msg(cg$err_tab_i - 1) IS NOT NULL
    THEN
      cg$err_tab_i := cg$err_tab_i - 1;
      msg          := cg$err_msg(cg$err_tab_i);
      cg$err_msg(cg$err_tab_i) := '';

      error        := cg$err_error(cg$err_tab_i);
      msg_type     := cg$err_msg_type(cg$err_tab_i);
      msgid        := cg$err_msgid(cg$err_tab_i);
      loc          := cg$err_loc(cg$err_tab_i);
      -- added to pop message info from stack into a PL/SQL record
      stack2record;
      RETURN 1;
    ELSE
      RETURN 0;
    END IF;
  END pop;

  --
  -- Purpose  Takes the error message that was last raised off the stack, remove
  --          it from the stack and returns the QMS error/message information.
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE pop(p_error IN OUT hil$message.row_type)
  IS
  BEGIN
    p_error := g_empty;

    IF  cg$err_tab_i > 1
    AND cg$err_msg(cg$err_tab_i - 1) IS NOT NULL
    THEN
      cg$err_tab_i := cg$err_tab_i - 1;
      cg$err_msg(cg$err_tab_i) := '';
      -- added to pop message info from stack into a PL/SQL record
      stack2record;
      pop_error(p_error);
    END IF;
  END pop;

  --
  -- Purpose  Returns the information of the last error taken from the stack
  --
  -- Usage    Called from the client to retrieve the QMS error information
  --          after the error has been identified as a server side error.
  --          Parameter
  --            p_errorrec : qms error record type
  --
  -- Remarks
  --
  PROCEDURE pop_error(p_error IN OUT hil$message.row_type)
  IS
  BEGIN
    p_error := g_error;
  END pop_error;

  --
  -- Purpose  Take a message off stack from head.
  --          Gets the error message that was last raised and removes it
  --          from the error stack
  --
  -- Usage    msg is a Text message
  --          returns TRUE if message popped successfully
  --          returns FALSE if stack was empty
  --
  -- Remarks
  --
  FUNCTION pop_head(msg OUT VARCHAR2)
    RETURN BOOLEAN
  IS
  BEGIN
    IF  cg$err_tab_i > 1
    AND cg$err_msg(cg$err_tab_i - 1) IS NOT NULL
    THEN
      msg := cg$err_msg(1);

      FOR i IN 1..cg$err_tab_i-2
      LOOP
        cg$err_msg(i)      := cg$err_msg(i+1);
        cg$err_error(i)    := cg$err_error(i+1);
        cg$err_msg_type(i) := cg$err_msg_type(i+1);
        cg$err_msgid(i)    := cg$err_msgid(i+1);
        cg$err_loc(i)      := cg$err_loc(i+1);
      END LOOP;

      cg$err_tab_i := cg$err_tab_i - 1;
      -- added to pop message info from stack into a PL/SQL record
      stack2record;

      cg$err_msg(cg$err_tab_i) := '';
      RETURN true;
    ELSE
      RETURN false;
    END IF;
  END pop_head;

  --
  -- Purpose  Take a message off stack from head.
  --          Gets the error message that was last raised and removes it
  --          from the error stack
  --
  -- Usage    msg      Text message
  --          error    error or WARNing
  --          msg_type ORA, API, or user TLA
  --          msgid is id of message
  --          loc      location where message occurred
  --          returns TRUE if message popped successfully
  --          returns FALSE if stack was empty
  --
  -- Remarks
  FUNCTION pop_head( msg      OUT VARCHAR2
                   , error    OUT VARCHAR2
                   , msg_type OUT VARCHAR2
                   , msgid    OUT INTEGER
                   , loc      OUT VARCHAR2
                   )
    RETURN BOOLEAN
  IS
  BEGIN
    IF cg$err_tab_i > 1 AND cg$err_msg(cg$err_tab_i - 1) IS NOT NULL
    THEN
      msg      := cg$err_msg(1);
      error    := cg$err_error(1);
      msg_type := cg$err_msg_type(1);
      msgid    := cg$err_msgid(1);
      loc      := cg$err_loc(1);

      FOR i IN 1..cg$err_tab_i-2
      LOOP
        cg$err_msg(i)      := cg$err_msg(i+1);
        cg$err_error(i)    := cg$err_error(i+1);
        cg$err_msg_type(i) := cg$err_msg_type(i+1);
        cg$err_msgid(i)    := cg$err_msgid(i+1);
        cg$err_loc(i)      := cg$err_loc(i+1);
      END LOOP;

      cg$err_tab_i             := cg$err_tab_i - 1;
      cg$err_msg(cg$err_tab_i) := '';

      -- added to pop message info from stack into a PL/SQL record
      stack2record;

      RETURN true;
    ELSE
      RETURN false;
    END IF;
  END pop_head;

  --
  -- Purpose  returns the message record found in position p_index in
  --          hil$message.message_type
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_message_rectype(p_index IN NUMBER)
    RETURN hil$message.row_type
  IS
  BEGIN
    RETURN qms$err_rec(p_index);
  END get_message_rectype;

  --
  -- Purpose  returns all messages on the message stack
  --
  -- Usage    p_message contains a message record
  --                    for each message stored on the msg stack.
  --          p_count   contains the total number of messages
  --                    returned
  --          p_raise   is true if at least one of the messages
  --                    returned is an error message.
  --
  -- Remarks
  --
  PROCEDURE get_error_messages( p_message IN OUT hil$message.message_type
                              , p_count   IN OUT NUMBER
                              , p_raise   IN OUT BOOLEAN
                              )
  IS
    l_message hil$message.row_type;
    l_index   number(10);
  BEGIN
    p_count := 0;
    pop(l_message);
    WHILE (l_message.msg_text IS NOT NULL)
    LOOP
      p_count := p_count + 1;
      p_message(p_count) := l_message;

      p_raise := p_raise OR l_message.raise_error;
      pop(l_message);
    END LOOP;
  END get_error_messages;

  --
  -- Purpose  create a display string IN the format 'severity code: text'
  --          in the appropriate end user language.
  --
  -- Usage    use the function to display error messages to the end user.
  --
  -- Remarks
  --
  FUNCTION get_display_string( p_code IN VARCHAR2
                             , p_text IN VARCHAR2
                             , p_type IN VARCHAR2
                             )
    RETURN VARCHAR2
  IS
    l_severity VARCHAR2(100);
  BEGIN
    l_severity := hil$message.get_severity_string(p_type);
    RETURN SUBSTR( l_severity
                ||' '
                ||p_code
                ||': '
                ||p_text
                , 1
                , 512
                );
  END get_display_string;

  --
  -- Purpose Isolate constraint name from an Oracle Error message
  --
  -- Usage   Parameters : msg     The actual Oracle Error message
  --                      type    type of constraint to find
  --                              (ERR_FOREIGN_KEY     Foreign key,
  --                               ERR_CHECK_CON       Check,
  --                               ERR_UNIQUE_KEY      Unique key,
  --                               ERR_DELETE_RESTRICT Restricted delete)
  --         Return     : con_name Constraint found (NULL if none found)
  --
  -- Remarks
  --
  FUNCTION parse_constraint(msg IN VARCHAR2, type IN INTEGER)
    RETURN VARCHAR2
  IS
    con_name    VARCHAR2(100) := '';
  BEGIN
    IF type = ERR_FOREIGN_KEY
    OR type = ERR_CHECK_CON
    OR type = ERR_UNIQUE_KEY
    OR type = ERR_DELETE_RESTRICT
    THEN
      con_name := SUBSTR(msg, instr(msg, '.') + 1, instr(msg, ')') - instr(msg, '.') - 1);
    END IF;

    RETURN con_name;
  END parse_constraint;

  --
  -- Purpose Provides a mechanism for text translation.
  --
  -- Usage   Parameters : p_MsgNo    the Id of the message
  --                      p_default  the Default Text
  --                      p_Subst1 (to 4) Substitution strings
  --                      p_LangId   The Language ID
  --         Return     : Translated message
  --
  -- Remarks
  --
  FUNCTION get_text( p_MsgNo    IN NUMBER
                   , p_default  IN VARCHAR2 DEFAULT NULL
                   , p_subst1   IN VARCHAR2 DEFAULT NULL
                   , p_subst2   IN VARCHAR2 DEFAULT NULL
                   , p_subst3   IN VARCHAR2 DEFAULT NULL
                   , p_subst4   IN VARCHAR2 DEFAULT NULL
                   , p_LangId   IN NUMBER   DEFAULT NULL
                   )
    RETURN VARCHAR2
  IS
    l_temp VARCHAR2(10000) := p_default;
  BEGIN
    -- added to get correct message text from tables
    qmsmsggettext(l_temp, p_default, p_subst1, p_subst2, p_subst3, p_subst4);

    l_temp := replace(l_temp, '<p>',  p_subst1);
    l_temp := replace(l_temp, '<p1>', p_subst1);
    l_temp := replace(l_temp, '<p2>', p_subst2);
    l_temp := replace(l_temp, '<p3>', p_subst3);
    l_temp := replace(l_temp, '<p4>', p_subst4);

    RETURN l_temp;
  END get_text;

  --
  -- Purpose  Pops all messages off the stack and returns them in the order
  --          in which they were raised.
  --
  -- Usage  function returns the message as a string
  --
  -- Remarks
  --
  FUNCTION get_errors
    RETURN VARCHAR2
  IS
    l_error VARCHAR2 (2000):='';
    l_next  VARCHAR2 (240) :='';
  BEGIN
    WHILE cg$errors.pop(l_next) = 1
    LOOP
      IF l_error IS NULL
      THEN
        l_error := l_next;
      ELSE
        -- l_next := l_next ||'   '|| l_error ;
        -- S. Davelaar, 14-01-2000: show each message on a new line
        l_error := l_next ||'<br>'|| l_error ;
      END IF;
    END LOOP;
    RETURN l_error;
  END get_errors;

-- For some reason, we need to close the package body here. Designer will remove it
-- when it generates the .pkb file.
END cg$errors;
/
SHOW ERROR