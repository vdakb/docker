-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\qms_txc.pks
--
-- Generated for Oracle 10g on Tue Mar 30 08:58:19 2010 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Specification 'qms$transaction'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE PACKAGE qms$transaction
AS
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- public variables
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- the purpose of the following public variables is to speed up the usage of
-- the dynamic sql construct, invoked via procedure perform_business rule
-- with these variables, the nds command only has to be parsed again for every
-- distinct capi package involved in a transaction. Without these variables there
-- is an expensive parse necessary for every business rule
g_current_name VARCHAR2(30);
g_current_api  NUMBER(10);
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- public program units
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
--
-- Purpose  Returns the revision label of the transaction management package
--
-- Usage
--
-- Remarks
--
--------------------------------------------------------------------------------
FUNCTION revision
  RETURN VARCHAR2;
PRAGMA RESTRICT_REFERENCES(revision, WNDS, RNDS, WNPS);

-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- PROGRAM UNITS FOR BUSINESS RULES
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
--
-- Purpose  Return true if a rule with this name and uk components is not on
--          any rule stack yet.
--
-- Usage    In when condition of Business Rule Design Definition, to avoid
--          multiple execution of aggregate rule when only one is needed.
--
-- Remarks  Not necessary if only its own br_name is supplied, this is already
--          taken care of in add_business_rule.
--
--------------------------------------------------------------------------------
FUNCTION not_on_stack( p_name     IN VARCHAR2
                     , p_uk_comp1 IN VARCHAR2 DEFAULT NULL
                     , p_uk_comp2 IN VARCHAR2 DEFAULT NULL
                     , p_uk_comp3 IN VARCHAR2 DEFAULT NULL
                     , p_uk_comp4 IN VARCHAR2 DEFAULT NULL
                     , p_uk_comp5 IN VARCHAR2 DEFAULT NULL
                     )
  RETURN BOOLEAN;
--------------------------------------------------------------------------------
--
-- Purpose  Adds a business rule to the right Business Rule stack so that it can
--          be enforced when the transaction is closed.
--
-- Usage
--
-- Remarks  If the same rule for the same api is already on stack, it does not
--          add again.
--
--------------------------------------------------------------------------------
PROCEDURE add_business_rule( p_owner   IN VARCHAR2
                           , p_package IN VARCHAR2
                           , p_name    IN VARCHAR2
                           , p_type    IN VARCHAR2
                           , p_api     IN NUMBER
                           );
--------------------------------------------------------------------------------
--
-- Purpose  Push a message on the message stack.
--          If the message is an error, it will increase g_errors_in_transaction
--
-- Usage    from CAPI's
--
-- Remarks
--
--------------------------------------------------------------------------------
PROCEDURE process_rule_violation( p_name  IN VARCHAR2 DEFAULT NULL
                                , p_code  IN VARCHAR2 DEFAULT NULL
                                , p_label IN VARCHAR2 DEFAULT NULL
                                , p_table IN VARCHAR2 DEFAULT NULL
                                , p_rowid IN ROWID    DEFAULT NULL
                                );
--------------------------------------------------------------------------------
--
-- Purpose  This procedure will remove from the rules_stack all rules connected
--          to the indicated package and api.
--
-- Usage
--
-- Remarks  For aggregate rules: don't remove when aggregate flag
--
--------------------------------------------------------------------------------
PROCEDURE remove_business_rules( p_owner   IN VARCHAR2
                               , p_package IN VARCHAR2
                               , p_api     IN NUMBER
                               );
--------------------------------------------------------------------------------
--
-- Purpose  Return the total number of business rules stored on the stacks.
--
-- Usage    From the CAPI (to determine if CAPI must be put on its own
--          stack).
--
-- Remarks  Not always equal to g_cev_rules_ctr + g_cew_rules_ctr + g_con_rules_ctr
--          because of removing of rules.
--
--------------------------------------------------------------------------------
FUNCTION get_rule_count
  RETURN NUMBER;

-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- PROGRAM UNITS FOR TRANSACTIONS
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
--
-- Purpose  Returns true if a transaction is currently open.
--
-- Usage
--
-- Remarks
--
--------------------------------------------------------------------------------
FUNCTION transaction_open
  RETURN BOOLEAN;
PRAGMA RESTRICT_REFERENCES(transaction_open, WNDS);
--------------------------------------------------------------------------------
--
-- Purpose  Opens a new transaction.
--          p_trans_opened_by identifies what type of program unit opened the
--          transaction.
--          This same program unit must close the transaction.
--
-- Usage    From front-end application, or VAPI/TAPI/CAPI
--
-- Remarks
--
--------------------------------------------------------------------------------
PROCEDURE open_transaction(p_opened_by IN VARCHAR2);
--------------------------------------------------------------------------------
--
-- Purpose  Returns the id of the current transaction.
--
-- Usage    In CAPI, to recognize the first action in a new transaction.
--
-- Remarks
--
FUNCTION get_current_id
  RETURN NUMBER;
--------------------------------------------------------------------------------
--
-- Purpose  Returns the type of program unit that opened the transaction.
--          Examples:
--              FORM  = Oracle Developer Form
--              JAVA  = JAVA form
--              BATCH = PL/SQL batch program
--              VAPI + view name  = VAPI instead-of trigger (used by WebDB for
--                                  example)
--              TAPI + table name = TAPI before statement database triggers
--                                  (used by WSGen HTML for example)
--              CAPI + capi name  = in the CAPI itself
--
-- Usage    In VAPI/TAPI/CAPI before closing transaction, to check if
--          transaction was opened by the same program unit.
--
-- Remarks  From front end, this function does not need to be called because
--          it will know for sure that the front end also opened the
--          transaction.
--
--------------------------------------------------------------------------------
FUNCTION get_opened_by
  RETURN VARCHAR2;
--------------------------------------------------------------------------------
--
-- Purpose  Closes the current transaction, if it was opened by
--          p_trans_opened_by.
--          When closing, loops through all business rules in the following
--          order.
--            o Change Events with DML
--            o Static and Dynamic Data Constraints
--            o Change Events without DML
--          For each rule, calls the correct CAPI to enforce the rule.
--          If any errors returned, raise an application error to notify the
--          user they need to look at the messages.
--
-- Usage    From front end, or VAPI/TAPI/CAPI
--
-- Remarks
--
--------------------------------------------------------------------------------
PROCEDURE close_transaction(p_opened_by IN VARCHAR2);
--------------------------------------------------------------------------------
--
-- Purpose  Allow cg$errors.raise_failure to actually raise an exception when it
--          was called at the time that close_transaction failed as opposed to
--          the time during close_transaction when business rules or other
--          program units raise failures.
--
-- Usage    From cg$errors.raise_failure
--
-- Remarks  The flag g_close_transaction_failed must not be reset to false until
--          open_transaction, we must be sure that when cg$errors.raise_failure
--          is called as the result of the failed close_transaction (maybe
--          indirectly) the flag still has the value true.
--
--------------------------------------------------------------------------------
FUNCTION close_transaction_failed
  RETURN boolean;
PRAGMA RESTRICT_REFERENCES(close_transaction_failed, WNDS);
--------------------------------------------------------------------------------
--
-- Purpose  Closes the current transaction (if it was opened by
--          p_trans_opened_by), and if no errors occurred, issues a commit.
--          If errors occurred, an exception will be raised.
--
-- Usage    From PL/SQL batch programs.
--
-- Remarks
--
--------------------------------------------------------------------------------
PROCEDURE commit_transaction(p_trans_opened_by IN VARCHAR2);
--------------------------------------------------------------------------------
--
-- Purpose  Combines rollback with clean up of transaction management, clears
--          all business rules from the rule stacks and clears all messages from
--          the cg$errors message stack.
--
-- Usage    From PL/SQL batch programs
--
-- Remarks
--
--------------------------------------------------------------------------------
PROCEDURE abort_transaction;

-- For some reason, we need to close the package specification here.
-- Designer will remove it when it generates the .pks file.
END qms$transaction;
/
SHOW ERROR