-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppAplication\src\main\dbs\qms\api\cg-err.pks
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
    - PROCEDURE get_error_messages( p_message_rectype_tbl IN OUT hil$message.message_type
                                  , p_message_count       IN OUT NUMBER
                                  , p_raise_error         IN OUT BOOLEAN
                                  )
    - FUNCTION  get_message_rectype( p_index IN NUMBER ) RETURN hil$message.row_type
    - FUNCTION  failure_raised return BOOLEAN
    - FUNCTION  get_display_string ( p_msg_code IN VARCHAR2
                                   , p_msg_text IN VARCHAR2
                                   , p_msg_type IN VARCHAR2
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
PROMPT Creating Package Specification 'cg$errors'
CREATE OR REPLACE PACKAGE cg$errors
AS

  -- ---------------------------------------------------------------------------
  -- public constants
  -- ---------------------------------------------------------------------------

  --
  -- Standard API error messages, the message text has been replaced with the
  -- message code as stored inthe QMS message tables because the API message
  -- code is not passed to the MsgGettext function. Original text is included as
  -- comment.
  --
  ERR_UK_UPDATE            CONSTANT VARCHAR2(10) := 'OSG-01001'; -- 'Unique key <p1> not updateable'
  ERR_FK_TRANS             CONSTANT VARCHAR2(10) := 'OSG-01002'; -- 'Foreign key <p1> not transferable'
  ERR_DEL_RESTRICT         CONSTANT VARCHAR2(10) := 'OFG-00005'; -- 'Cannot delete <p1> row, matching <p2> found'
  VAL_MAND                 CONSTANT VARCHAR2(10) := 'OFG-00078'; -- 'Value for <p1> is required'
  ROW_MOD	           CONSTANT VARCHAR2(10) := 'OSG-01003'; -- 'Update failed - please re-query as value for <p1> has been modified by another user<p2><p3>4
  ROW_LCK                  CONSTANT VARCHAR2(10) := 'OSG-01101'; -- 'Row is locked by another user'
  ROW_DEL                  CONSTANT VARCHAR2(10) := 'OSG-01102'; -- 'Row no longer exists'
  APIMSG_PK_VIOLAT         CONSTANT VARCHAR2(10) := 'OSG-01007'; -- 'Primary key <p1> on table <p2> violated'
  APIMSG_UK_VIOLAT         CONSTANT VARCHAR2(10) := 'OSG-01006'; -- 'Unique constraint <p1> on table <p2> violated'
  APIMSG_FK_VIOLAT         CONSTANT VARCHAR2(10) := 'OSG-01005'; -- 'Foreign key <p1> on table <p2> violated'
  APIMSG_CK_VIOLAT         CONSTANT VARCHAR2(10) := 'OSG-01004'; -- 'Check constraint <p1> on table <p2> violated'
  APIMSG_ARC_MAND_VIOLAT   CONSTANT VARCHAR2(10) := 'OSG-01009'; -- 'Mandatory Arc <p1> on <p2> has not been satisfied'
  APIMSG_ARC_VIOLAT        CONSTANT VARCHAR2(10) := 'OSG-01010'; -- 'Too many members in Arc <p1> on <p2>'
  APIMSG_RV_TAB_NOT_FOUND  CONSTANT VARCHAR2(10) := 'OSG-01012'; -- 'Reference code table <p1> has not been created used for <p2>'
  APIMSG_RV_LOOKUP_FAIL    CONSTANT VARCHAR2(10) := 'OSG-01013'; -- 'Invalid value <p1> for column <p2>.<p3>'
  APIMSG_RV_LOOKUP_DO_FAIL CONSTANT VARCHAR2(10) := 'OSG-01014'; -- 'Invalid value <p1> in domain <p2> for column <p3>.<p4>'
  APIMSG_CODE_CTL_LOCKED   CONSTANT VARCHAR2(10) := 'OSG-01015'; -- 'Control table sequence value <p1> is being used by another user'
  APIMSG_FK_VALUE_REQUIRED CONSTANT VARCHAR2(10) := 'OSG-01016'; -- 'Value required for <p1> foreign key';
  APIMSG_CASC_ERROR        CONSTANT VARCHAR2(10) := 'OSG-01017'; -- 'Error in cascade <p1>';
  APIMSG_DO_LOOKUP_DO_FAIL CONSTANT VARCHAR2(10) := 'OSG-01201'; -- 'Invalid values in domain constraint <p1> referring domain table <p2> for table <p3>'

  API_UNIQUE_KEY_UPDATE    CONSTANT INTEGER      := 1001;
  API_FOREIGN_KEY_TRANS    CONSTANT INTEGER      := 1002;
  API_MODIFIED             CONSTANT INTEGER      := 1003;
  API_CK_CON_VIOLATED      CONSTANT INTEGER      := 1004;
  API_FK_CON_VIOLATED      CONSTANT INTEGER      := 1005;
  API_UQ_CON_VIOLATED      CONSTANT INTEGER      := 1006;
  API_PK_CON_VIOLATED      CONSTANT INTEGER      := 1007;
  API_MAND_COLUMN_ISNULL   CONSTANT INTEGER      := 1008;
  API_MAND_ARC_EMPTY       CONSTANT INTEGER      := 1009;
  API_ARC_TOO_MANY_VAL     CONSTANT INTEGER      := 1010;
  API_DEL_RESTRICT         CONSTANT INTEGER      := 1011;
  API_RV_TAB_NOT_FOUND     CONSTANT INTEGER      := 1012;
  API_RV_LOOKUP_FAIL       CONSTANT INTEGER      := 1013;
  API_RV_LOOKUP_DO_FAIL    CONSTANT INTEGER      := 1014;
  API_CODE_CTL_LOCKED      CONSTANT INTEGER      := 1015;
  API_FK_VALUE_REQUIRED    CONSTANT INTEGER      := 1016;
  API_CASC_ERROR           CONSTANT INTEGER      := 1017;
  API_ROW_MOD              CONSTANT INTEGER      := 1018;
  API_ROW_LCK              CONSTANT INTEGER      := 1019;
  API_ROW_DEL              CONSTANT INTEGER      := 1020;

  ERR_FOREIGN_KEY          CONSTANT INTEGER      := -2291;
  ERR_CHECK_CON            CONSTANT INTEGER      := -2290;
  ERR_UNIQUE_KEY           CONSTANT INTEGER      := -1;
  ERR_MAND_MISSING         CONSTANT INTEGER      := -1400;
  ERR_UPDATE_MAND          CONSTANT INTEGER      := -1407;
  ERR_RESOURCE_BUSY        CONSTANT INTEGER      := -54;
  ERR_NO_DATA              CONSTANT INTEGER      :=  1403;
  ERR_DELETE_RESTRICT      CONSTANT INTEGER      := -2292;
  ERR_MUTATING_TABLE       CONSTANT INTEGER      := -4091;

  -- ---------------------------------------------------------------------------
  -- public exceptions
  -- ---------------------------------------------------------------------------

  --
  -- Standard Oracle Excqmsions that are caught and processed by the API
  --
  mandatory_missing        EXCEPTION;
  check_violation          EXCEPTION;
  fk_violation             EXCEPTION;
  upd_mandatory_null       EXCEPTION;
  delete_restrict          EXCEPTION;
  uk_violation             EXCEPTION;
  resource_busy            EXCEPTION;
  no_data_found            EXCEPTION;
  trg_mutate               EXCEPTION;
  --
  -- Excqmsion raised when any API validation fails, pragma initilisation to
  -- ORA-20999 added to make the error easier to track.
  -- (No call to cg$errors.cg$error is required
  --
  cg$error                 EXCEPTION;

  PRAGMA EXCEPTION_INIT(mandatory_missing,  -1400);
  PRAGMA EXCEPTION_INIT(check_violation,    -2290);
  PRAGMA EXCEPTION_INIT(fk_violation,       -2291);
  PRAGMA EXCEPTION_INIT(upd_mandatory_null, -1407);
  PRAGMA EXCEPTION_INIT(delete_restrict,    -2292);
  PRAGMA EXCEPTION_INIT(uk_violation,       -1);
  PRAGMA EXCEPTION_INIT(resource_busy,      -54);
  PRAGMA EXCEPTION_INIT(trg_mutate,         -4091);
  PRAGMA EXCEPTION_INIT(cg$error,           -20999);

  -- ---------------------------------------------------------------------------
  -- public types
  -- ---------------------------------------------------------------------------

  TYPE cg$err_msg_t       IS TABLE OF VARCHAR2(512)
    INDEX BY BINARY_INTEGER;
  TYPE cg$err_error_t     IS TABLE OF VARCHAR2(1)
    INDEX BY BINARY_INTEGER;
  TYPE cg$err_msg_type_t  IS TABLE OF VARCHAR2(3)
    INDEX BY BINARY_INTEGER;
  TYPE cg$err_msgid_t     IS TABLE OF INTEGER
    INDEX BY BINARY_INTEGER;
  TYPE cg$err_loc_t       IS TABLE OF VARCHAR2(240)
    INDEX BY BINARY_INTEGER;

  cg$err_tab_i INTEGER := 1;

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
  PROCEDURE raise_failure;
  PRAGMA RESTRICT_REFERENCES(raise_failure, WNDS);

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
    RETURN BOOLEAN;

  --
  -- Purpose Make a distinction between a QMS call to raise_failure
  --         and a direct TAPI call to raise_failure
  --
  -- Usage   Set extended error to true in qms$errors.show_message, just before
  --         raise_failure
  --
  -- Remarks -
  --
  PROCEDURE set_qms_error(p_value BOOLEAN);
  PRAGMA RESTRICT_REFERENCES(set_qms_error, WNDS);

  --
  -- Purpose Clears the stack
  --
  -- Usage
  --
  -- Remarks -
  --
  PROCEDURE clear;

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
                );

  --
  -- Purpose Put a message on stack with full info
  --
  -- Usage   Called by the error handling routines in server version of
  --         qms$errors.
  --         Parameters : p_error message info
  --
  -- Remarks -
  --
  PROCEDURE push(p_error  IN OUT hil$message.row_type);
  PRAGMA RESTRICT_REFERENCES(push, WNDS);

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
  PROCEDURE push_error(p_error IN OUT hil$message.row_type);
  PRAGMA RESTRICT_REFERENCES(push_error, WNDS);

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
    RETURN NUMBER;

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
  -- Remarks -
  --
  FUNCTION pop( msg      OUT VARCHAR2
              , error    OUT VARCHAR2
              , msg_type OUT VARCHAR2
              , msgid    OUT INTEGER
              , loc      OUT VARCHAR2
              )
    RETURN NUMBER;

  --
  -- Purpose  Takes the error message that was last raised off the stack, remove
  --          it from the stack and returns the QMS error/message information.
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE pop(p_error IN OUT hil$message.row_type);

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
  PROCEDURE pop_error(p_error IN OUT hil$message.row_type);

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
    RETURN BOOLEAN;

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
    RETURN BOOLEAN;

  --
  -- Purpose  returns the message record found in position p_index in
  --          hil$message.message_type
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_message_rectype(p_index IN NUMBER)
    RETURN hil$message.row_type;

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
                              );

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
    RETURN VARCHAR2;
  PRAGMA RESTRICT_REFERENCES(get_display_string, WNDS);

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
    RETURN VARCHAR2;
  PRAGMA RESTRICT_REFERENCES(parse_constraint, WNDS);

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
    RETURN VARCHAR2;

  --
  -- Purpose  Pops all messages off the stack and returns them in the order
  --          in which they were raised.
  --
  -- Usage  function returns the message as a string
  --
  -- Remarks
  --
  FUNCTION get_errors
    RETURN VARCHAR2;

-- For some reason, we need to close the package specification here. Designer will remove it
-- when it generates the .pkb file.
END cg$errors;
/
SHOW ERROR