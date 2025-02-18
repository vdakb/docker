-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\qms_txc.pkb
--
-- Generated for Oracle 10g on Tue Mar 30 08:58:19 2010 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Implementation 'qms$transaction'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE PACKAGE BODY qms$transaction
IS
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private constants
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/* Revision label of package */
REVISION_LABEL               CONSTANT VARCHAR2( 8) := '2.0.0.0';
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private variables
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
g_current_id    NUMBER(10);
g_opened_by     VARCHAR2(40);
g_failed        BOOLEAN        := false;
g_closing       BOOLEAN        := false;

g_rules_cev     NUMBER(10)     := 0;
g_rules_cew     NUMBER(10)     := 0;
g_rules_con     NUMBER(10)     := 0;
-- total number of rules on the 3 stacks
g_rule_count    NUMBER(10)     := 0;

-- temporary store for aggregate rules between not_on_stack and
-- add_business_rule
g_is_next       BOOLEAN        := false;
g_uk_comp1      VARCHAR2(128);
g_uk_comp2      VARCHAR2(128);
g_uk_comp3      VARCHAR2(128);
g_uk_comp4      VARCHAR2(128);
g_uk_comp5      VARCHAR2(128);
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private exceptions
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
qms$failed      EXCEPTION;
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private program units
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
--
-- Purpose  Clear all rule stacks, clear the message stack and free the memory.
--
-- Usage
--
-- Remarks
--
--------------------------------------------------------------------------------
PROCEDURE clear_all_stacks
IS
BEGIN
  -- clear the 3 rule stack tables
  DELETE FROM qms_rulestack;
  g_rules_cev  := 0;
  g_rules_cew  := 0;
  g_rules_con  := 0;
  g_rule_count := 0;
  -- clear the message stack
  cg$errors.clear;
  -- free the memory
  dbms_session.free_unused_user_memory;
END clear_all_stacks;
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- public program units
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
--
-- Purpose  Returns the revision label of the transaction management package.
--
-- Usage
--
-- Remarks
--
--------------------------------------------------------------------------------
FUNCTION revision
  RETURN VARCHAR2
IS
BEGIN
  RETURN REVISION_LABEL;
END revision;

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
-- Remarks  Not necessary if only its own name is supplied, this is already
--          taken care of in add_business_rule.
--
--          Make use of temporary db table qms_rowstack instead of pl/sql
--          rowstack.
--          NOCOPY performance solution is not necessary anymore in subfunction
--
--------------------------------------------------------------------------------
FUNCTION not_on_stack( p_name     IN VARCHAR2
                     , p_uk_comp1 IN VARCHAR2 DEFAULT NULL
                     , p_uk_comp2 IN VARCHAR2 DEFAULT NULL
                     , p_uk_comp3 IN VARCHAR2 DEFAULT NULL
                     , p_uk_comp4 IN VARCHAR2 DEFAULT NULL
                     , p_uk_comp5 IN VARCHAR2 DEFAULT NULL
                     )
  RETURN BOOLEAN
IS
  CURSOR c_rls( b_name     IN VARCHAR2
              , b_uk_comp1 IN VARCHAR2
              , b_uk_comp2 IN VARCHAR2
              , b_uk_comp3 IN VARCHAR2
              , b_uk_comp4 IN VARCHAR2
              , b_uk_comp5 IN VARCHAR2
              )
  IS
    SELECT 'x'
    FROM   qms_rulestack rls
    WHERE  rls.name                = b_name
    AND    NVL(rls.uk_comp1, '-1') = NVL(b_uk_comp1, '-1')
    AND    NVL(rls.uk_comp2, '-1') = NVL(b_uk_comp2, '-1')
    AND    NVL(rls.uk_comp3, '-1') = NVL(b_uk_comp3, '-1')
    AND    NVL(rls.uk_comp4, '-1') = NVL(b_uk_comp4, '-1')
    AND    NVL(rls.uk_comp5, '-1') = NVL(b_uk_comp5, '-1')
    ;

  r_rls      c_rls%ROWTYPE;
  l_on_stack BOOLEAN := true;
BEGIN
  OPEN  c_rls ( p_name
              , p_uk_comp1
              , p_uk_comp2
              , p_uk_comp3
              , p_uk_comp4
              , p_uk_comp5
              );
  FETCH c_rls INTO  r_rls;
  l_on_stack := c_rls%NOTFOUND;
  CLOSE c_rls;
  --
  IF l_on_stack
  THEN
    -- make sure that when this rule is added shortly after (in proc
    -- add_business_rule), the uk components are also stored on the stack
    g_is_next  := true;

    g_uk_comp1 := p_uk_comp1;
    g_uk_comp2 := p_uk_comp2;
    g_uk_comp3 := p_uk_comp3;
    g_uk_comp4 := p_uk_comp4;
    g_uk_comp5 := p_uk_comp5;
  END IF;
  RETURN l_on_stack;
EXCEPTION
  WHEN others
  THEN
    IF c_rls%isopen
    THEN
      CLOSE c_rls;
      RETURN true;
    END IF;
END not_on_stack;
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
--          Inserts business rules in temporary db table qms_rowstack instead of
--          PL/SQL rowstack.
--          NOCOPY performance solution is not necessary anymore.
--
--------------------------------------------------------------------------------
PROCEDURE add_business_rule( p_owner   IN VARCHAR2
                           , p_package IN VARCHAR2
                           , p_name    IN VARCHAR2
                           , p_type    IN VARCHAR2
                           , p_api     IN NUMBER
                           )
IS
  l_on_stack BOOLEAN       := false;
  l_uk_comp1 VARCHAR2(128) := null;
  l_uk_comp2 VARCHAR2(128) := null;
  l_uk_comp3 VARCHAR2(128) := null;
  l_uk_comp4 VARCHAR2(128) := null;
  l_uk_comp5 VARCHAR2(128) := null;
  l_type     VARCHAR2(3);
  l_next     NUMBER(10);

  -- sub procedure of add_business_rule
  FUNCTION business_rule_on_stack( p_name    IN VARCHAR2
                                 , p_api     IN NUMBER
                                 , p_package IN VARCHAR2
                                 , p_owner   IN VARCHAR2
                                 )
  RETURN BOOLEAN
  IS
    CURSOR c_rls( b_name    IN VARCHAR2
                , b_api     IN NUMBER
                , b_owner   IN VARCHAR2
                , b_package IN VARCHAR2
                )
    IS
      SELECT 'x'
      FROM   qms_rulestack rls
      WHERE  rls.name    = b_name
      AND    rls.api     = b_api
      AND    rls.package = b_package
      AND    rls.owner   = b_owner
      ;
    r_rls c_rls%ROWTYPE;
    l_return BOOLEAN := false;
  BEGIN
    OPEN  c_rls ( p_name
                , p_api
                , p_owner
                , p_package
                );
    FETCH c_rls INTO  r_rls;
    l_return := c_rls%found;
    CLOSE c_rls;
    --
    RETURN l_return;
  EXCEPTION
    WHEN others
    THEN
      IF c_rls%isopen
      THEN
        CLOSE c_rls;
        RETURN false;
       END IF;
  END business_rule_on_stack;

BEGIN
  l_on_stack := business_rule_on_stack( p_name
                                      , p_api
                                      , p_package
                                      , p_owner
                                      );
  IF NOT l_on_stack
  THEN
    -- add rule to stack
    g_rule_count  := g_rule_count + 1;
    -- if aggregate rule, also store uk components
    IF g_is_next
    THEN
      -- there is a slight chance that this is not the aggregate rule,
      -- because other conditions in the need function were false
      -- but it does not do any harm to store uk components for non-aggregate rules
      -- and if there was a next aggregate rule, the uk components were overwritten
      l_uk_comp1 := g_uk_comp1;
      l_uk_comp2 := g_uk_comp2;
      l_uk_comp3 := g_uk_comp3;
      l_uk_comp4 := g_uk_comp4;
      l_uk_comp5 := g_uk_comp5;

      g_is_next  := false;
    END IF;

    -- increment rule count depending on rule type
    IF p_type = 'W' -- Change Event with DML
    THEN
      g_rules_cev := g_rules_cev + 1;
      l_type      := 'CEV';
      l_next      := g_rules_cev;
    ELSIF p_type = 'O' -- Change Event without DML
    THEN
      g_rules_cew := g_rules_cew + 1;
      l_type      := 'CEW';
      l_next      := g_rules_cew;
    ELSE -- Constraints: Static or Dynamic
      g_rules_con := g_rules_con + 1;
      l_type      := 'CON';
      l_next      := g_rules_con;
    END IF;

    INSERT INTO qms_rulestack(
      id
    , type
    , name
    , api
    , owner
    , package
    , uk_comp1
    , uk_comp2
    , uk_comp3
    , uk_comp4
    , uk_comp5
    , remove
    ) VALUES (
      l_next
    , l_type
    , p_name
    , p_api
    , p_owner
    , p_package
    , l_uk_comp1
    , l_uk_comp2
    , l_uk_comp3
    , l_uk_comp4
    , l_uk_comp5
    , 'Y'
    );
  END IF;
END add_business_rule;
--------------------------------------------------------------------------------
--
-- Purpose  Performs the requested business rule by calling CAPI enforce_rule
--          procedure.
--
-- Usage    From close_transaction
--
-- Remarks  This procedure calls qms$execute, which in turn calls the
--          appropriate CAPI package using dynamic sql. Because the qms$execute
--          procedure is located in the owner of the CAPI package, there is no
--          need to grant execute on (and create synonyms for) all the CAPI's to
--          the owner of qms$transaction.
--
--------------------------------------------------------------------------------
PROCEDURE perform_business_rule( p_owner   IN VARCHAR2
                               , p_package IN VARCHAR2
                               , p_name    IN VARCHAR2
                               , p_api     IN NUMBER
                               )
IS
BEGIN
  -- pass name and api via global package variables, speeds up performance of
  -- dynamic sql construct
  g_current_name := p_name;
  g_current_api  := p_api;
  qms$execute(p_command => 'BEGIN '
                        || p_owner||'.qms$execute('
                        || '''BEGIN '
                        || p_package
                        || '.enforce_rule;'
                        || ' END;'
                        || '''); END;'
              );
EXCEPTION
  WHEN others
  THEN
    qms$errors.unhandled('qms$transaction.perform_business_rule');
END perform_business_rule;
--------------------------------------------------------------------------------
--
-- Purpose  push a message on the message stack
--          if the message is an error, it will increase g_errors_in_transaction
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
                                )
IS
  l_severity  qms_message_properties.severity%type;
BEGIN
  -- determine message severity
  BEGIN
    SELECT severity
    INTO   l_severity
    FROM   qms_message_properties
    WHERE  code = p_code;
  EXCEPTION
    WHEN others
    THEN
      l_severity := 'E';
  END;
  -- add message on error stack
  qms$errors.show( p_msg      => p_code
                 , p_param0   => p_label
                 , p_severity => l_severity
                 , p_raise    => false
                 , p_table    => p_table
                 , p_rowid    => p_rowid
                 );
END process_rule_violation;
--------------------------------------------------------------------------------
--
-- Purpose  This procedure will remove from the rules_stack all rules connected
--          to the indicated package and api.
--
-- Usage
--
-- Remarks  For aggregate rules: don't remove when aggregate flag.
--
--          Make use of temporary db table qms_rowstack instead of pl/sql
--          rowstack.
--          NOCOPY performance solution in subprocedure is not necessary
--          anymore.
--
--------------------------------------------------------------------------------
PROCEDURE remove_business_rules( p_owner   IN VARCHAR2
                               , p_package IN VARCHAR2
                               , p_api     IN NUMBER
                               )
IS
  l_to_delete NUMBER(10) := 0;
BEGIN
  IF p_api IS NOT NULL
  THEN
    -- first count number of api's to delete to decrease g_rule_count properly
    SELECT count(*)
    INTO   l_to_delete
    FROM   qms_rulestack rls
    WHERE  rls.owner   = p_owner
    AND    rls.package = p_package
    AND    rls.api     = p_api
    ;
    IF l_to_delete > 0
    THEN
      -- remove rules from all rule stacks
      DELETE FROM qms_rulestack rls
      WHERE rls.owner   = p_owner
      AND   rls.package = p_package
      AND   rls.api     = p_api
      ;
      g_rule_count := g_rule_count - l_to_delete;
    END IF;
  END IF;
END remove_business_rules;
--------------------------------------------------------------------------------
--
-- Purpose  Return the total number of business rules stored on the stacks.
--
-- Usage    From the CAPI (to determine if CAPI must be put on its own stack).
--
-- Remarks  Not always equal to g_rules_cev + g_rules_cew + g_rules_con because
--          of removing of rules.
--
--------------------------------------------------------------------------------
FUNCTION get_rule_count
RETURN NUMBER
IS
BEGIN
  RETURN g_rule_count;
END get_rule_count;

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
RETURN BOOLEAN
IS
  l_test VARCHAR2(1);
BEGIN
  IF g_current_id IS NULL
  THEN
    RETURN false;
  ELSE
    SELECT   ''
    INTO     l_test
    FROM     qms_transactions trs
    WHERE    trs.id     = g_current_id
    AND      trs.status = 'OPEN';
    RETURN true;
  END IF;
EXCEPTION
  WHEN NO_DATA_FOUND
  THEN
    RETURN false;
END transaction_open;
--------------------------------------------------------------------------------
--
-- Purpose  Opens a new transaction
--          p_trans_opened_by identifies what type of program unit opened the
--          transaction.
--          This same program unit must close the transaction.
--
-- Usage    From front-end application, or VAPI/TAPI/CAPI.
--
-- Remarks
--
--------------------------------------------------------------------------------
PROCEDURE open_transaction(p_opened_by IN VARCHAR2)
IS
BEGIN
  IF not transaction_open
  THEN
    SELECT qms_txs_seq.nextval
    INTO   g_current_id
    FROM   dual
    ;

    INSERT INTO qms_transactions(
      id
    , status
    ) VALUES (
      g_current_id
    , 'OPEN'
    );

    g_opened_by := p_opened_by;
    clear_all_stacks;
    g_failed := false;
  END IF;
EXCEPTION
  WHEN others
  THEN
    qms$errors.unhandled('qms$transaction.open_transaction');
END open_transaction;
--------------------------------------------------------------------------------
--
-- Purpose  Returns the id of the current transaction
--
-- Usage    In CAPI, to recognize the first action in a new transaction
--
-- Remarks
--
--------------------------------------------------------------------------------
FUNCTION get_current_id
RETURN NUMBER
IS
BEGIN
  RETURN g_current_id;
END get_current_id;
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
RETURN VARCHAR2
IS
BEGIN
  RETURN g_opened_by;
END get_opened_by;
--------------------------------------------------------------------------------
--
-- Purpose  Closes the current transaction, if it was opened by
--          p_trans_opened_by. When closing, loops through all business rules in
--          the following order.
--            - Change Events with DML
--            - Static and Dynamic Data Constraints
--            - Change Events without DML
--          For each rule, calls the correct CAPI to enforce the rule.
--          If any errors returned, raise an application error to notify the user
--          they need to look at the messages.
--
-- Usage    From front end, or VAPI/TAPI/CAPI
--
-- Remarks  Make use of temporary db table qms_rowstack instead of pl/sql
--          rowstack.
--
--------------------------------------------------------------------------------
PROCEDURE close_transaction(p_opened_by IN VARCHAR2)
IS
  CURSOR c_rls(b_type IN VARCHAR2, b_start_id IN NUMBER)
  IS
    SELECT   rls.owner
    ,        rls.package
    ,        rls.name
    ,        rls.api
    FROM     qms_rulestack rls
    WHERE    rls.type   = b_type
    AND      rls.id    >= b_start_id
    ORDER BY rls.owner  -- important for performance in perform_business_rule via nds
    ,        rls.package
    ,        rls.api
    ;

  r_rls        c_rls%ROWTYPE;
  l_rule_index NUMBER(10);
BEGIN
  -- if the transaction was already closed for some reason,
  -- or if the transaction was opened by someone else, do nothing
  IF      transaction_open
  AND NOT g_closing
  AND     g_opened_by = p_opened_by
  THEN
    g_failed  := false;
    g_closing := true;
    -- loop through Change Events with DML
    -- embed for loop in while construction: cev rules might be added while the cev
    -- rules are inspected. This is determined by looking at the g_rules_cev before
    -- and after the for loop.
     -- start at cev rule 1
    l_rule_index := 1;
    --
    <<all_cev_rules_inspected>>
    WHILE l_rule_index <= g_rules_cev
    LOOP
      <<cev_rules>>
      FOR r_rls IN c_rls(b_type => 'CEV', b_start_id => l_rule_index)
      LOOP
        perform_business_rule( r_rls.owner
                             , r_rls.package
                             , r_rls.name
                             , r_rls.api
                             );
        l_rule_index := l_rule_index + 1;
      END LOOP cev_rules;
    END LOOP all_cev_rules_inspected;

    -- loop through static and dynamic Data constraints
    IF g_rules_con > 0
    THEN
      l_rule_index := 1;
      <<con_rules>>
      FOR r_rls IN c_rls(b_type => 'CON', b_start_id => l_rule_index)
      LOOP
        perform_business_rule( r_rls.owner
                             , r_rls.package
                             , r_rls.name
                             , r_rls.api
                             );
      END LOOP con_rules;
    END IF;

    -- before doing the Change Events without DML,
    -- we should check whether any errors have been found
    -- and if so, skip the cew-rules and go to exception handler
    -- also resets the failure_raised flag
    IF cg$errors.failure_raised
    THEN
      raise qms$failed;
    END IF;

    -- loop through Change Events without DML
    IF g_rules_cew > 0
    THEN
      l_rule_index := 1;
      FOR r_rls IN c_rls(b_type => 'CEW', b_start_id => l_rule_index)
      LOOP
        perform_business_rule( r_rls.owner
                             , r_rls.package
                             , r_rls.name
                             , r_rls.api
                             );
      END LOOP;
    END IF;

    -- if somehow, a change event without dml raised an error,
    -- also go to the exception handler
    -- also resets the failure_raised flag
    IF cg$errors.failure_raised
    THEN
      raise qms$failed;
    END IF;

    -- remove api that violates deferred check constraint QMS_NEED_TO_CLOSE_TRANSACTION
    DELETE FROM qms_transactions
    WHERE  id = g_current_id;

    -- go into IDLE or undefined state
    g_current_id := null;
    g_closing    := false;
  END IF;
EXCEPTION
  -- including qms$failed
  WHEN OTHERS
  THEN
    -- transaction was not without errors
    g_failed  := true;
    g_closing := false;
    update qms_transactions
    set    status          = 'OPEN'
    ,      timestamp_close = sysdate
    WHERE  id              = g_current_id;
    qms$errors.raiseError;
END close_transaction;
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
--          open_transaction or another call to close_transaction, we must be
--          sure that when cg$errors.raise_failure is called as the result of the
--          failed close_transaction (maybe indirectly) the flag still has the
--          value true.
--
--------------------------------------------------------------------------------
FUNCTION close_transaction_failed
RETURN BOOLEAN
IS
BEGIN
  RETURN g_failed;
END close_transaction_failed;
--------------------------------------------------------------------------------
--
-- Purpose  Closes the current transaction (if it was opened by
--          p_trans_opened_by), and if no errors occurred, issues a commit.
--          If errors occurred, an exception will be raised.
--
-- Usage    from PL/SQL batch programs
--
-- Remarks
--
--------------------------------------------------------------------------------
PROCEDURE commit_transaction(p_trans_opened_by IN VARCHAR2)
IS
BEGIN
  close_transaction(p_trans_opened_by);
  commit;
  -- no exception handler, because if close_transaction raises an exception,
  -- this should be passed through to the calling program
END commit_transaction;
--------------------------------------------------------------------------------
--
-- Purpose  Combines rollback with clean up of transaction management,
--          clears all business rules from the rule stacks
--          and clears all messages from the cg$errors message stack
--
-- Usage    from PL/SQL batch programs
--
-- Remarks
--
--------------------------------------------------------------------------------
PROCEDURE abort_transaction
IS
BEGIN
  clear_all_stacks;
  g_current_id := null;

  -- now rollback all changes to the database, including the creation
  -- of the transaction record in qms_transactions.
  rollback;
EXCEPTION
  WHEN others
  THEN
    qms$errors.unhandled('qms$transaction.abort_transaction');
END abort_transaction;

-- For some reason, we need to close the package specification here.
-- Designer will remove it when it generates the .pkb file.
END qms$transaction;
/
SHOW ERROR