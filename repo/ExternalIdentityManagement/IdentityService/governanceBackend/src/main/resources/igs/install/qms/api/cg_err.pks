-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\cg_err.pks
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Specification 'cg$errors'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*******************************************************************************
Purpose  cg$errors package used by the Table and Module Component API packages.
         All error handling routines are included in this package.
         The package has been changed to cover all QMS Headstart Error/Message
         handling requirements.

Usage    This version of the package is needed to replace the standard version
         of cg$errors delivered with Designer/2000 R2.1

Remarks  With new versions of Designer/2000, the Headstart team will verify
         changes in the standard cg$errors package and adapt their version.

Changes to Original cg$errors package specification (cdsapers.pks), see
cg$error_package_version for base version of the file.

  Procedures/Functions Added :
    - FUNCTION  revision RETURN VARCHAR2
    - PROCEDURE pop               ( p_errorrec IN OUT hil$message.message_rectype )
    - PROCEDURE pop_qms_errorinfo ( p_errorrec IN OUT hil$message.message_rectype )
    - PROCEDURE push              ( p_errorrec IN OUT hil$message.message_rectype )
    - PROCEDURE qmsrecord2stack   ( p_errorrec IN OUT hil$message.message_rectype )
    - PROCEDURE get_error_messages( p_message_rectype_tbl IN OUT hil$message.message_tabtype
                                  , p_message_count       IN OUT NUMBER
                                  , p_raise_error         IN OUT BOOLEAN
                                  )
    - FUNCTION  get_message_rectype( p_index IN NUMBER ) RETURN hil$message.message_rectype
    - FUNCTION  failure_raised return BOOLEAN
    - FUNCTION  get_display_string ( p_msg_code IN VARCHAR2
                                   , p_msg_text IN VARCHAR2
                                   , p_msg_type IN VARCHAR2
                                   )
        RETURN VARCHAR2

  Procedures/Functions Changed :
    - PROCEDURE push ( msg          IN VARCHAR2
                     , error        IN VARCHAR2 DEFAULT 'E'
                     , msg_type     IN VARCHAR2 DEFAULT NULL
                     , msgid        IN INTEGER  DEFAULT 0
                     , loc          IN VARCHAR2 DEFAULT NULL
       Added ->      , p_unexpected IN VARCHAR2 DEFAULT SQLERRM
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
CREATE OR REPLACE PACKAGE cg$errors
IS
  CG$ERROR_PACKAGE_VERSION CONSTANT VARCHAR2(20) := '1.1.0';

  --
  -- Exception raised when any API validation fails
  --
  cg$error                 EXCEPTION;
  --
  -- Standard Oracle Errors that are caught and processed by the API
  --
  mandatory_missing        EXCEPTION;
  check_violation          EXCEPTION;
  fk_violation             EXCEPTION;
  upd_mandatory_null       EXCEPTION;
  delete_restrict          EXCEPTION;
  uk_violation             EXCEPTION;
  resource_busy            EXCEPTION;
  NO_DATA_FOUND            EXCEPTION;
  trg_mutate               EXCEPTION;

  API_UNIQUE_KEY_UPDATE    CONSTANT INTEGER       := 1001;
  API_FOREIGN_KEY_TRANS    CONSTANT INTEGER       := 1002;
  API_MODIFIED             CONSTANT INTEGER       := 1003;
  API_CK_CON_VIOLATED      CONSTANT INTEGER       := 1004;
  API_FK_CON_VIOLATED      CONSTANT INTEGER       := 1005;
  API_UQ_CON_VIOLATED      CONSTANT INTEGER       := 1006;
  API_PK_CON_VIOLATED      CONSTANT INTEGER       := 1007;
  API_MAND_COLUMN_ISNULL   CONSTANT INTEGER       := 1008;
  API_MAND_ARC_EMPTY       CONSTANT INTEGER       := 1009;
  API_ARC_TOO_MANY_VAL     CONSTANT INTEGER       := 1010;
  API_DEL_RESTRICT         CONSTANT INTEGER       := 1011;
  API_RV_TAB_NOT_FOUND     CONSTANT INTEGER       := 1012;
  API_RV_LOOKUP_FAIL       CONSTANT INTEGER       := 1013;
  API_RV_LOOKUP_DO_FAIL    CONSTANT INTEGER       := 1014;
  API_CODE_CTL_LOCKED      CONSTANT INTEGER       := 1015;
  API_FK_VALUE_REQUIRED    CONSTANT INTEGER       := 1016;
  API_CASC_ERROR           CONSTANT INTEGER       := 1017;
  API_ROW_MOD              CONSTANT INTEGER       := 1018;
  API_ROW_LCK              CONSTANT INTEGER       := 1019;
  API_ROW_DEL              CONSTANT INTEGER       := 1020;

  ERR_FOREIGN_KEY          CONSTANT INTEGER       := -2291;
  ERR_CHECK_CON            CONSTANT INTEGER       := -2290;
  ERR_UNIQUE_KEY           CONSTANT INTEGER       := -1;
  ERR_MAND_MISSING         CONSTANT INTEGER       := -1400;
  ERR_UPDATE_MAND          CONSTANT INTEGER       := -1407;
  ERR_RESOURCE_BUSY        CONSTANT INTEGER       := -54;
  ERR_NO_DATA              CONSTANT INTEGER       :=  1403;
  ERR_DELETE_RESTRICT      CONSTANT INTEGER       := -2292;

  ERR_UK_UPDATE            CONSTANT VARCHAR2(128) := 'Unique key <p1> not updateable';
  ERR_FK_TRANS             CONSTANT VARCHAR2(128) := 'Foreign key <p1> not transferable';
  ERR_DEL_RESTRICT         CONSTANT VARCHAR2(128) := 'Cannot delete <p1> row, matching <p2> found';
  VAL_MAND                 CONSTANT VARCHAR2(128) := 'Value for <p1> is required';
  ROW_MOD	           CONSTANT VARCHAR2(128) := 'Update failed - please re-query as value for <p1> has been modified by another user<p2><p3>';
  ROW_LCK                  CONSTANT VARCHAR2(128) := 'Row is locked by another user';
  ROW_DEL                  CONSTANT VARCHAR2(128) := 'Row no longer exists';
  APIMSG_PK_VIOLAT         CONSTANT VARCHAR2(128) := 'Primary key <p1> on table <p2> violated';
  APIMSG_UK_VIOLAT         CONSTANT VARCHAR2(128) := 'Unique constraint <p1> on table <p2> violated';
  APIMSG_FK_VIOLAT         CONSTANT VARCHAR2(128) := 'Foreign key <p1> on table <p2> violated';
  APIMSG_CK_VIOLAT         CONSTANT VARCHAR2(128) := 'Check constraint <p1> on table <p2> violated';
  APIMSG_ARC_MAND_VIOLAT   CONSTANT VARCHAR2(128) := 'Mandatory Arc <p1> on <p2> has not been satisfied';
  APIMSG_ARC_VIOLAT        CONSTANT VARCHAR2(128) := 'Too many members in Arc <p1> on <p2>';
  APIMSG_RV_TAB_NOT_FOUND  CONSTANT VARCHAR2(128) := 'Reference code table <p1> has not been created used for <p2>';
  APIMSG_RV_LOOKUP_FAIL    CONSTANT VARCHAR2(128) := 'Invalid value <p1> for column <p2>.<p3>';
  APIMSG_RV_LOOKUP_DO_FAIL CONSTANT VARCHAR2(128) := 'Invalid value <p1> in domain <p2> for column <p3>.<p4>';
  APIMSG_CODE_CTL_LOCKED   CONSTANT VARCHAR2(128) := 'Control table sequence value <p1> is being used by another user';
  APIMSG_FK_VALUE_REQUIRED CONSTANT VARCHAR2(128) := 'Value required for <p1> foreign key';
  APIMSG_CASC_ERROR        CONSTANT VARCHAR2(128) := 'Error in cascade <p1>';
  APIMSG_DO_LOOKUP_DO_FAIL CONSTANT VARCHAR2(128) := 'Invalid values in domain constraint <p1> referring domain table <p2> for table <p3>';

  PRAGMA EXCEPTION_INIT(mandatory_missing,    -1400);
  PRAGMA EXCEPTION_INIT(check_violation,      -2290);
  PRAGMA EXCEPTION_INIT(fk_violation,         -2291);
  PRAGMA EXCEPTION_INIT(upd_mandatory_null,   -1407);
  PRAGMA EXCEPTION_INIT(delete_restrict,      -2292);
  PRAGMA EXCEPTION_INIT(uk_violation,         -0001);
  PRAGMA EXCEPTION_INIT(resource_busy,        -0054);
  PRAGMA EXCEPTION_INIT(trg_mutate,           -4091);

  PRAGMA EXCEPTION_INIT(cg$error,           -20999);

  TYPE cg$err_msg_t       IS TABLE OF VARCHAR2(512)   INDEX BY BINARY_INTEGER;
  TYPE cg$err_error_t     IS TABLE OF VARCHAR2(1)     INDEX BY BINARY_INTEGER;
  TYPE cg$err_msg_type_t  IS TABLE OF VARCHAR2(3)     INDEX BY BINARY_INTEGER;
  TYPE cg$err_msgid_t     IS TABLE OF INTEGER         INDEX BY BINARY_INTEGER;
  TYPE cg$err_loc_t       IS TABLE OF VARCHAR2(240)   INDEX BY BINARY_INTEGER;
  cg$err_tab_i INTEGER := 1;

-----------------------------------------------------------------------------
  -- Name:        revision
  -- Description: Returns the revision label
  --
  -- Parameters:  none
  --
  -- Returns:     the revision label
-----------------------------------------------------------------------------
FUNCTION revision
  RETURN VARCHAR2;
PRAGMA RESTRICT_REFERENCES(revision, WNDS, WNPS);
-----------------------------------------------------------------------------
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
-----------------------------------------------------------------------------
FUNCTION display_string( p_code IN VARCHAR2
                       , p_text IN VARCHAR2
                       , p_type IN VARCHAR2
                       )
  RETURN VARCHAR2;
PRAGMA RESTRICT_REFERENCES(display_string, WNDS);
-----------------------------------------------------------------------------
-- Name:        clear
-- Description: Clears the stack
--
-- Parameters:  none
-----------------------------------------------------------------------------
  PROCEDURE clear;
-----------------------------------------------------------------------------
-- Name:        errors
-- Description: Pops all messages off the stack and returns them in the order
--              in which they were raised.
--
-- Parameters:  none
--
-- Returns:     the messages
-----------------------------------------------------------------------------
FUNCTION errors
  RETURN VARCHAR2;
-----------------------------------------------------------------------------
-- Name:        push
-- Description: Put a message on stack with full info
--
-- Parameters:  p_msg    the text message
--              p_error  ERRor or WARNing
--              p_type   ORA, API or user TLA
--              p_msgid  the id of message
--              p_loc    the location where error occured
-----------------------------------------------------------------------------
PROCEDURE push( p_msg        IN VARCHAR2
              , p_error      IN VARCHAR2 DEFAULT 'E'
              , p_type       IN VARCHAR2 DEFAULT NULL
              , p_msgid      IN INTEGER  DEFAULT 0
              , p_loc        IN VARCHAR2 DEFAULT NULL
              , p_unexpected IN VARCHAR2 DEFAULT SQLERRM
              );
PRAGMA RESTRICT_REFERENCES(push, WNDS);
-----------------------------------------------------------------------------
-- Name:        pop
-- Description: Take a message off stack
--              Gets the error message that was last raised and removes it
--              from the error stack.
--
-- Parameters:  p_msg    the text message
--
-- Returns:     TRUE     message popped successfully
--              FALSE    stack was empty
-----------------------------------------------------------------------------
FUNCTION pop(p_msg OUT VARCHAR2)
  RETURN BOOLEAN;
PRAGMA RESTRICT_REFERENCES(pop, WNDS);
-----------------------------------------------------------------------------
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
-----------------------------------------------------------------------------
FUNCTION pop( p_msg   OUT VARCHAR2
            , p_error OUT VARCHAR2
            , p_type  OUT VARCHAR2
            , p_msgid OUT INTEGER
            , p_loc   OUT VARCHAR2
            )
  RETURN BOOLEAN;
PRAGMA RESTRICT_REFERENCES(pop, WNDS);
-----------------------------------------------------------------------------
-- Name:        pop_head
-- Description: Take a message off stack from head
--              Gets the error message that was last raised and removes it
--              from the error stack.
--
-- Parameters:  p_msg    the text message
--
-- Returns:     TRUE     message popped successfully
--              FALSE    stack was empty
-----------------------------------------------------------------------------
FUNCTION pop_head(p_msg OUT VARCHAR2)
  RETURN BOOLEAN;
PRAGMA RESTRICT_REFERENCES(pop_head, WNDS);
-----------------------------------------------------------------------------
-- Name:        pop_head (overload)
-- Description: Take a message off stack from head with full info
--
-- Parameters:  p_msg    the text message
--              p_error  the ERRor or WARNing
--              p_type   ORA, API or user TLA
--              p_msgid  the id of message
--              p_loc    the location where error occured
--
-- Returns:     TRUE     message popped successfully
--              FALSE    stack was empty
-----------------------------------------------------------------------------
FUNCTION pop_head( p_msg   OUT VARCHAR2
                 , p_error OUT VARCHAR2
                 , p_type  OUT VARCHAR2
                 , p_msgid OUT INTEGER
                 , p_loc   OUT VARCHAR2
                 )
  RETURN BOOLEAN;
PRAGMA RESTRICT_REFERENCES(pop_head, WNDS);
-----------------------------------------------------------------------------
-- Name:        push
-- Description: Put a headstart message on stack with full info
--
-- Usage   Called by the error handling routines in server version of
--         qms$errors.
--         Parameters: p_error  the headstart message info
--
-- Remarks
--
-----------------------------------------------------------------------------
PROCEDURE push(p_error IN OUT hil$message.hil$row_type);
PRAGMA RESTRICT_REFERENCES(push, WNDS);
-----------------------------------------------------------------------------
-----------------------------------------------------------------------------
PROCEDURE qmsrecord2stack(p_error IN OUT hil$message.hil$row_type);
PRAGMA RESTRICT_REFERENCES(qmsrecord2stack, WNDS);
-----------------------------------------------------------------------------
--
-- Purpose  Returns the information of the last error taken from the stack
--
-- Usage    Called from the client to retrieve the QMS error information
--          after the error has been identified as a server side error.
--          Parameter: p_error the qms error record type
--
-- Remarks
--
-----------------------------------------------------------------------------
PROCEDURE pop_qms_errorinfo(p_error IN OUT hil$message.hil$row_type);
-----------------------------------------------------------------------------
-- Purpose Make a distinction between a QMS call to raise_failure and a
--         direct TAPI call to raise_failure
--
-- Usage   Set qms error to true in qms$errors.show, just before
--         raise_failure
-----------------------------------------------------------------------------
PROCEDURE set_qms_error(p_value BOOLEAN);
 PRAGMA RESTRICT_REFERENCES(set_qms_error, WNDS);
-----------------------------------------------------------------------------
-- Name:        string
-- Description: Provides a mechanism for text translation.
--
-- Parameters:  p_msgid         the id of the message
--              p_default       the Default Text
--              p_param1 (to 4) the substitution strings
 --
-- Returns:		          the translated message
-----------------------------------------------------------------------------
FUNCTION string( p_msgid    IN NUMBER
               , p_default  IN VARCHAR2 DEFAULT NULL
               , p_param1   IN VARCHAR2 DEFAULT NULL
               , p_param2   IN VARCHAR2 DEFAULT NULL
               , p_param3   IN VARCHAR2 DEFAULT NULL
               , p_param4   IN VARCHAR2 DEFAULT NULL
               )
  RETURN VARCHAR2;
PRAGMA RESTRICT_REFERENCES(string, WNDS);
-----------------------------------------------------------------------------
--
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
-----------------------------------------------------------------------------
FUNCTION parse_constraint( msg  IN VARCHAR2
                         , type IN INTEGER
                         )
  RETURN VARCHAR2;
PRAGMA RESTRICT_REFERENCES(parse_constraint, WNDS);
-----------------------------------------------------------------------------
-- Name:        raise_failure
--
-- Description: To raise the cg$error failure exception handler
-----------------------------------------------------------------------------
PROCEDURE raise_failure;
PRAGMA RESTRICT_REFERENCES(raise_failure, WNDS);
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
  RETURN BOOLEAN;

END cg$errors;
/
SHOW ERRORS