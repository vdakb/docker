-- /Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityService/identifierBackend/src/main/resources/install/pid/api/pit_aid.pkb
--
--   Anonymous Identifier (AID)
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Body for Table 'pit_identifiers'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
-- Name:        cg$pit_identifiers
-- Description: pit_identifiers table API package definitions
--------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY cg$pit_identifiers
IS

--  Constant default values
C10ACTIVE   CONSTANT pit_identifiers.active%TYPE := 1;

--------------------------------------------------------------------------------
-- Name:        err_msg
--
-- Description: Pushes onto stack appropriate user defined error message
--              depending on the rule violated
--
-- Parameters:  msg     Oracle error message
--              type    Type of violation e.g. check_constraint: ERR_CHECK_CON
--              loc     Place where this procedure was called for error
--                      trapping
--------------------------------------------------------------------------------
PROCEDURE err_msg(msg IN VARCHAR2, type IN INTEGER, loc IN VARCHAR2 DEFAULT '')
IS
  con_name VARCHAR2(240);
BEGIN
  con_name := cg$errors.parse_constraint(msg, type);
  IF (con_name = 'PIT_AID_PK')
  THEN
    cg$errors.push( NVL(PIT_AID_PK, cg$errors.string(cg$errors.API_PK_CON_VIOLATED, cg$errors.APIMSG_PK_VIOLAT, 'PIT_AID_PK', 'pit_identifiers'))
                  , 'E'
                  , 'API'
                  , cg$errors.API_PK_CON_VIOLATED
                  , loc
                  );
  ELSIF (con_name = 'PIT_AID_CK1' AND PIT_AID_CK1 IS NOT NULL)
  THEN
    cg$errors.push(PIT_AID_CK1, 'E', 'API', cg$errors.API_CK_CON_VIOLATED, loc);
  ELSE
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, loc);
  END IF;
END err_msg;

--------------------------------------------------------------------------------
-- Name:        raise_uk_not_updateable
--
-- Description: Raise appropriate error when unique key updated
--
-- Parameters:  none
--------------------------------------------------------------------------------
PROCEDURE raise_uk_not_updateable(uk IN VARCHAR2)
IS
BEGIN
  cg$errors.push( cg$errors.string(cg$errors.API_UNIQUE_KEY_UPDATE, cg$errors.ERR_UK_UPDATE, uk)
                , 'E'
                , 'API'
                , cg$errors.API_UNIQUE_KEY_UPDATE
                , 'cg$pit_identifiers.raise_uk_not_updateable'
                );
  cg$errors.raise_failure;
END raise_uk_not_updateable;

--------------------------------------------------------------------------------
-- Name:        raise_fk_not_transferable
--
-- Description: Raise appropriate error when foreign key updated
--
-- Parameters:  none
--------------------------------------------------------------------------------
PROCEDURE raise_fk_not_transferable(fk IN VARCHAR2)
IS
BEGIN
  cg$errors.push( cg$errors.string(cg$errors.API_FOREIGN_KEY_TRANS, cg$errors.ERR_FK_TRANS, fk)
                , 'E'
                , 'API'
                , cg$errors.API_FOREIGN_KEY_TRANS
                , 'cg$pit_identifiers.raise_fk_not_transferable'
                );
  cg$errors.raise_failure;
END raise_fk_not_transferable;

--------------------------------------------------------------------------------
-- Name:        up_autogen_columns
--
-- Description: Specific autogeneration of column values and conversion to
--              uppercase
--
-- Parameters:  cg$rec    Record of row to be manipulated
--              cg$ind    Indicators for row
--              operation Procedure where this procedure was called
--------------------------------------------------------------------------------
PROCEDURE up_autogen_columns(cg$rec IN OUT cg$row_type, cg$ind IN OUT cg$ind_type, operation IN VARCHAR2 DEFAULT 'INS', do_denorm IN BOOLEAN DEFAULT TRUE)
IS
BEGIN
  IF (operation = 'INS')
  THEN
    cg$rec.rowversion := hextoraw(0);
    cg$rec.created_by := user;-- //cg$sessions.get_identity;
    cg$rec.created_on := sysdate;
  END IF;
  cg$rec.rowversion := hextoraw(rawtohex(cg$rec.rowversion) + 1);
  cg$rec.updated_by := user;-- //cg$sessions.get_identity;
  cg$rec.updated_on := sysdate;
EXCEPTION
  WHEN NO_DATA_FOUND
  THEN
    NULL;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$pit_identifiers.up_autogen_columns.denorm');
    cg$errors.raise_failure;
END up_autogen_columns;

--------------------------------------------------------------------------------
-- Name:        validate_mandatory
--
-- Description: Checks all mandatory columns are not null and raises appropriate
--              error if not satisfied
--
-- Parameters:  cg$val_rec Record of row to be checked
--              loc        Place where this procedure was called for error
--                         trapping
--------------------------------------------------------------------------------
PROCEDURE validate_mandatory(cg$val_rec IN cg$row_type, loc IN VARCHAR2 DEFAULT '')
IS
BEGIN
  IF (cg$val_rec.id IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P10ID), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.rowversion IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P15ROWVERSION), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.created_by IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P20CREATED_BY), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.created_on IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P25CREATED_ON), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.updated_by IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P30UPDATED_BY), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.updated_on IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P35UPDATED_ON), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.active IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P40ACTIVE), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.usedby IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P45USEDBY), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
END validate_mandatory;

--------------------------------------------------------------------------------
-- Name:        validate_foreign_keys_ins
--
-- Description: Checks all foreign keys on insert and raises appropriate error
-- if not satisfied
--
-- Parameters:  cg$rec Record of row to be checked
--------------------------------------------------------------------------------
PROCEDURE validate_foreign_keys_ins(cg$rec IN cg$row_type)
IS
  fk_check INTEGER;
BEGIN
  NULL;
END validate_foreign_keys_ins;

--------------------------------------------------------------------------------
-- Name:        validate_foreign_keys_upd
--
-- Description: Checks all foreign keys on update and raises appropriate error
-- if not satisfied
--
-- Parameters:  cg$rec Record of row to be checked
--------------------------------------------------------------------------------
PROCEDURE validate_foreign_keys_upd(cg$rec IN cg$row_type, cg$old_rec IN cg$row_type, cg$ind IN cg$ind_type)
IS
  fk_check INTEGER;
BEGIN
  NULL;
END validate_foreign_keys_upd;

--------------------------------------------------------------------------------
-- Name:        validate_foreign_keys_del
--
-- Description: Checks all foreign keys on delete and raises appropriate error
-- if not satisfied
--
-- Parameters:  cg$rec Record of row to be checked
--------------------------------------------------------------------------------
PROCEDURE validate_foreign_keys_del(cg$rec IN cg$row_type)
IS
  fk_check INTEGER;
BEGIN
  NULL;
END validate_foreign_keys_del;

--------------------------------------------------------------------------------
-- Name:        ins
--
-- Description: API insert procedure
--
-- Parameters:  cg$rec  Record of row to be inserted
--              cg$ind  Record of columns specifically set
--              do_ins  Whether we want the actual INSERT to occur
--------------------------------------------------------------------------------
PROCEDURE ins(cg$rec IN OUT cg$row_type, cg$ind IN OUT cg$ind_type, do_ins IN BOOLEAN DEFAULT TRUE)
IS
  cg$tmp_rec cg$row_type;
  -- Constant default values
BEGIN
  -- Application_logic Pre-Insert <<Start>>
  -- Application_logic Pre-Insert << End >>

  -- Defaulted
  IF NOT (cg$ind.active)
  THEN
    cg$rec.active := C10ACTIVE;
  END IF;

  -- Auto-generated and uppercased columns
  up_autogen_columns(cg$rec, cg$ind, 'INS', do_ins);

  called_from_package := TRUE;
  IF (do_ins)
  THEN
    validate_foreign_keys_ins(cg$rec);
    INSERT INTO pit_identifiers (
      id
    , rowversion
    , created_by
    , created_on
    , updated_by
    , updated_on
    , active
    , usedby
    ) VALUES (
      cg$rec.id
    , cg$rec.rowversion
    , cg$rec.created_by
    , cg$rec.created_on
    , cg$rec.updated_by
    , cg$rec.updated_on
    , cg$rec.active
    , cg$rec.usedby
    );
    sel(cg$rec);
  END IF;
  called_from_package := FALSE;

  -- Application logic Post-Insert <<Start>>
  -- Application logic Post-Insert << End >>
EXCEPTION
  WHEN cg$errors.cg$error
  THEN
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.mandatory_missing
  THEN
    validate_mandatory(cg$rec, 'cg$pit_identifiers.ins.mandatory_missing');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.check_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_CHECK_CON, 'cg$pit_identifiers.ins.check_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.fk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_FOREIGN_KEY, 'cg$pit_identifiers.ins.fk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.uk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_UNIQUE_KEY, 'cg$pit_identifiers.ins.uk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$pit_identifiers.ins.others');
    called_from_package := FALSE;
    cg$errors.raise_failure;
END ins;

--------------------------------------------------------------------------------
-- Name:        upd
--
-- Description: API update procedure
--
-- Parameters:  cg$rec  Record of row to be updated
--              cg$ind  Record of columns specifically set
--              do_upd  Whether we want the actual UPDATE to occur
--------------------------------------------------------------------------------
PROCEDURE upd(cg$rec IN OUT cg$row_type, cg$ind IN OUT cg$ind_type, do_upd IN BOOLEAN DEFAULT TRUE, cg$pk IN cg$row_type DEFAULT NULL)
IS
  cg$upd_rec    cg$row_type;
  cg$old_rec    cg$row_type;
  RECORD_LOGGED BOOLEAN     := FALSE;
BEGIN
  -- Application_logic Pre-Update <<Start>>
  -- Application_logic Pre-Update << End >>

  IF (cg$pk.id IS NULL)
  THEN
    cg$upd_rec.id := cg$rec.id;
  ELSE
    cg$upd_rec.id := cg$pk.id;
  END IF;
  cg$old_rec.id := cg$upd_rec.id;
  IF (cg$pk.the_rowid IS NULL)
  THEN
    cg$upd_rec.the_rowid := cg$rec.the_rowid;
  ELSE
    cg$upd_rec.the_rowid := cg$pk.the_rowid;
  END IF;
  cg$old_rec.the_rowid := cg$upd_rec.the_rowid;

  IF (do_upd)
  THEN
    sel(cg$upd_rec);
    -- Report error if attempt to update non updateable Primary Key PIT_AID_PK
    IF (cg$ind.id AND cg$rec.id != cg$upd_rec.id)
    THEN
      raise_uk_not_updateable('PIT_AID_PK');
    END IF;
    IF NOT (cg$ind.id)
    THEN
      cg$rec.id := cg$upd_rec.id;
    END IF;
    IF NOT (cg$ind.rowversion)
    THEN
      cg$rec.rowversion := cg$upd_rec.rowversion;
    END IF;
    IF NOT (cg$ind.created_by)
    THEN
      cg$rec.created_by := cg$upd_rec.created_by;
    END IF;
    IF NOT (cg$ind.created_on)
    THEN
      cg$rec.created_on := cg$upd_rec.created_on;
    END IF;
    IF NOT (cg$ind.updated_by)
    THEN
      cg$rec.updated_by := cg$upd_rec.updated_by;
    END IF;
    IF NOT (cg$ind.updated_on)
    THEN
      cg$rec.updated_on := cg$upd_rec.updated_on;
    END IF;
    IF NOT (cg$ind.active)
    THEN
      cg$rec.active := cg$upd_rec.active;
    END IF;
    IF NOT (cg$ind.usedby)
    THEN
      cg$rec.usedby := cg$upd_rec.usedby;
    END IF;
  ELSE
    -- Perform checks if called from a trigger
    -- Indicators are only set on changed values
    NULL;
    -- Report error if attempt to update non updateable Primary Key PIT_AID_PK
    IF (cg$ind.id) OR (cg$ind.id)
    THEN
      raise_uk_not_updateable('PIT_AID_PK');
    END IF;
  END IF;
  -- Auto-generated and uppercased columns
  up_autogen_columns(cg$rec, cg$ind, 'UPD', do_upd);
  -- Now do update if updateable columns exist
  IF (do_upd)
  THEN
    DECLARE
      called_from BOOLEAN := called_from_package;
    BEGIN
      called_from_package := TRUE;
      sel(cg$old_rec);
      validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
      IF cg$rec.the_rowid IS NULL
      THEN
        UPDATE pit_identifiers
        SET    rowversion   = cg$rec.rowversion
        ,      created_by   = cg$rec.created_by
        ,      created_on   = cg$rec.created_on
        ,      updated_by   = cg$rec.updated_by
        ,      active       = cg$rec.active
        ,      usedby       = cg$rec.usedby
        WHERE  id           = cg$rec.id
        ;
      ELSE
        UPDATE pit_identifiers
        SET    rowversion   = cg$rec.rowversion
        ,      created_by   = cg$rec.created_by
        ,      created_on   = cg$rec.created_on
        ,      updated_by   = cg$rec.updated_by
        ,      active       = cg$rec.active
        ,      usedby       = cg$rec.usedby
        WHERE  rowid        = cg$rec.the_rowid
        ;
      END IF;
      sel(cg$rec);
      cascade_update(cg$rec, cg$old_rec);
      called_from_package := called_from;
    END;
  END IF;

  IF NOT (do_upd)
  THEN
    cg$table(idx).id            := cg$rec.id;
    cg$tableind(idx).id         := cg$ind.id;
    cg$table(idx).rowversion    := cg$rec.rowversion;
    cg$tableind(idx).rowversion := cg$ind.rowversion;
    cg$table(idx).created_by    := cg$rec.created_by;
    cg$tableind(idx).created_by := cg$ind.created_by;
    cg$table(idx).created_on    := cg$rec.created_on;
    cg$tableind(idx).created_on := cg$ind.created_on;
    cg$table(idx).updated_by    := cg$rec.updated_by;
    cg$tableind(idx).updated_by := cg$ind.updated_by;
    cg$table(idx).updated_on    := cg$rec.updated_on;
    cg$tableind(idx).updated_on := cg$ind.updated_on;
    cg$table(idx).active        := cg$rec.active;
    cg$tableind(idx).active     := cg$ind.active;
    cg$table(idx).usedby        := cg$rec.usedby;
    cg$tableind(idx).usedby     := cg$ind.usedby;

    idx := idx + 1;
  END IF;

  -- Application_logic Post-Update <<Start>>
  -- Application_logic Post-Update << End >>
EXCEPTION
  WHEN cg$errors.cg$error
  THEN
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.upd_mandatory_null
  THEN
    validate_mandatory(cg$rec, 'cg$pit_identifiers.upd.upd_mandatory_null');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.check_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_CHECK_CON, 'cg$pit_identifiers.upd.check_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.fk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_FOREIGN_KEY, 'cg$pit_identifiers.upd.fk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.uk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_UNIQUE_KEY, 'cg$pit_identifiers.upd.uk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$pit_identifiers.upd.others');
    called_from_package := FALSE;
    cg$errors.raise_failure;
END upd;

--------------------------------------------------------------------------------
-- Name:        del
--
-- Description: API delete procedure
--
-- Parameters:  cg$pk  Primary key record of row to be deleted
--------------------------------------------------------------------------------
PROCEDURE del(cg$pk IN cg$pk_type, do_del IN BOOLEAN DEFAULT TRUE)
IS
BEGIN
  -- Application_logic Pre-Delete <<Start>>
  -- Application_logic Pre-Delete << End >>

  -- Delete the record
  called_from_package := TRUE;
  IF (do_del)
  THEN
    DECLARE
      cg$rec cg$row_type;
      cg$old_rec cg$row_type;
      cg$ind cg$ind_type;
    BEGIN
      cg$rec.id := cg$pk.id;
      sel(cg$rec);

      validate_foreign_keys_del(cg$rec);

      IF cg$pk.the_rowid IS NULL
      THEN
        DELETE pit_identifiers
        WHERE  id = cg$pk.id
        ;
      ELSE
        DELETE pit_identifiers
        WHERE  rowid = cg$pk.the_rowid
        ;
      END IF;

      cascade_delete(cg$rec);
    END;
  END IF;
  called_from_package := FALSE;

  -- Application_logic Post-Delete <<Start>>
  -- Application_logic Post-Delete << End >>
EXCEPTION
  WHEN cg$errors.cg$error
  THEN
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.delete_restrict
  THEN
    err_msg(SQLERRM, cg$errors.ERR_DELETE_RESTRICT, 'cg$pit_identifiers.del.delete_restrict');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN NO_DATA_FOUND
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_ROW_DEL, cg$errors.ROW_DEL), 'E', 'ORA', SQLCODE, 'cg$pit_identifiers.del.NO_DATA_FOUND');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$pit_identifiers.del.others');
    called_from_package := FALSE;
    cg$errors.raise_failure;
END del;

--------------------------------------------------------------------------------
-- Name:        sel
--
-- Description: Selects into the given parameter all the attributes for the row
--              given by the primary key
--
-- Parameters:  cg$sel_rec  Record of row to be selected into using its PK
--------------------------------------------------------------------------------
PROCEDURE sel(cg$sel_rec IN OUT cg$row_type)
IS
BEGIN
  IF cg$sel_rec.the_rowid IS NULL
  THEN
    SELECT id
    ,      rowversion
    ,      created_by
    ,      created_on
    ,      updated_by
    ,      updated_on
    ,      active
    ,      usedby
    ,      rowid
    INTO   cg$sel_rec.id
    ,      cg$sel_rec.rowversion
    ,      cg$sel_rec.created_by
    ,      cg$sel_rec.created_on
    ,      cg$sel_rec.updated_by
    ,      cg$sel_rec.updated_on
    ,      cg$sel_rec.active
    ,      cg$sel_rec.usedby
    ,      cg$sel_rec.the_rowid
    FROM   pit_identifiers
    WHERE  id = cg$sel_rec.id
    ;
  ELSE
    SELECT id
    ,      rowversion
    ,      created_by
    ,      created_on
    ,      updated_by
    ,      updated_on
    ,      active
    ,      usedby
    ,      rowid
    INTO   cg$sel_rec.id
    ,      cg$sel_rec.rowversion
    ,      cg$sel_rec.created_by
    ,      cg$sel_rec.created_on
    ,      cg$sel_rec.updated_by
    ,      cg$sel_rec.updated_on
    ,      cg$sel_rec.active
    ,      cg$sel_rec.usedby
    ,      cg$sel_rec.the_rowid
    FROM   pit_identifiers
    WHERE  rowid = cg$sel_rec.the_rowid
    ;
  END IF;
EXCEPTION
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$pit_identifiers.sel.others');
    cg$errors.raise_failure;
END sel;


--------------------------------------------------------------------------------
-- Name:        lck
--
-- Description: API lock procedure
--
-- Parameters:  cg$old_rec  Calling apps view of record of row to be locked
--              cg$old_ind  Record of columns to raise error if modified
--              nowait_flag TRUE lock with NOWAIT, FALSE don't fail if busy
--------------------------------------------------------------------------------
PROCEDURE lck(cg$old_rec IN cg$row_type, cg$old_ind IN cg$ind_type, nowait_flag IN BOOLEAN DEFAULT TRUE)
IS
  cg$tmp_rec   cg$row_type;
  any_modified BOOLEAN     := FALSE;
BEGIN
  -- Application_logic Pre-Lock <<Start>>
  -- Application_logic Pre-Lock << End >>

  -- Do the row lock
  BEGIN
    IF (nowait_flag)
    THEN
      IF cg$old_rec.the_rowid IS NULL
      THEN
        SELECT id
        ,      rowversion
        ,      created_by
        ,      created_on
        ,      updated_by
        ,      updated_on
        ,      active
        ,      usedby
        INTO   cg$tmp_rec.id
        ,      cg$tmp_rec.rowversion
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        ,      cg$tmp_rec.active
        ,      cg$tmp_rec.usedby
        FROM   pit_identifiers
        WHERE  id = cg$old_rec.id
        FOR UPDATE NOWAIT;
      ELSE
        SELECT id
        ,      rowversion
        ,      created_by
        ,      created_on
        ,      updated_by
        ,      updated_on
        ,      active
        ,      usedby
        INTO   cg$tmp_rec.id
        ,      cg$tmp_rec.rowversion
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        ,      cg$tmp_rec.active
        ,      cg$tmp_rec.usedby
        FROM   pit_identifiers
        WHERE  rowid = cg$old_rec.the_rowid
        FOR UPDATE NOWAIT;
      END IF;
    ELSE
      IF cg$old_rec.the_rowid IS NULL
      THEN
        SELECT id
        ,      rowversion
        ,      created_by
        ,      created_on
        ,      updated_by
        ,      updated_on
        ,      active
        ,      usedby
        INTO   cg$tmp_rec.id
        ,      cg$tmp_rec.rowversion
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        ,      cg$tmp_rec.active
        ,      cg$tmp_rec.usedby
        FROM   pit_identifiers
        WHERE  id = cg$old_rec.id
        FOR UPDATE;
      ELSE
        SELECT id
        ,      rowversion
        ,      created_by
        ,      created_on
        ,      updated_by
        ,      updated_on
        ,      active
        ,      usedby
        INTO   cg$tmp_rec.id
        ,      cg$tmp_rec.rowversion
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        ,      cg$tmp_rec.active
        ,      cg$tmp_rec.usedby
        FROM   pit_identifiers
        WHERE  rowid = cg$old_rec.the_rowid
        FOR UPDATE;
      END IF;
    END IF;
  EXCEPTION
    WHEN cg$errors.cg$error
    THEN
      cg$errors.raise_failure;
    WHEN cg$errors.resource_busy
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_LCK, cg$errors.ROW_LCK), 'E', 'ORA', SQLCODE, 'cg$pit_identifiers.lck.resource_busy');
      cg$errors.raise_failure;
    WHEN NO_DATA_FOUND
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_DEL, cg$errors.ROW_DEL), 'E', 'ORA', SQLCODE, 'cg$pit_identifiers.lck.NO_DATA_FOUND');
      cg$errors.raise_failure;
    WHEN OTHERS
    THEN
      cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$pit_identifiers.lck.others');
      cg$errors.raise_failure;
    END;

  --
  -- Optional Columns
  --

  --
  -- Mandatory Columns
  --
  IF (cg$old_ind.id)
  THEN
    IF (cg$tmp_rec.id != cg$old_rec.id)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P10ID), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$pit_identifiers.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.rowversion)
  THEN
    IF (cg$tmp_rec.rowversion != cg$old_rec.rowversion)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P15ROWVERSION), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$pit_identifiers.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.created_by)
  THEN
    IF (cg$tmp_rec.created_by != cg$old_rec.created_by)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P20CREATED_BY), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$pit_identifiers.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.created_on)
  THEN
    IF (cg$tmp_rec.created_on != cg$old_rec.created_on)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P25CREATED_ON), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$pit_identifiers.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.updated_by)
  THEN
    IF (cg$tmp_rec.updated_by != cg$old_rec.updated_by)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P30UPDATED_BY), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$pit_identifiers.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.updated_on)
  THEN
    IF (cg$tmp_rec.updated_on != cg$old_rec.updated_on)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P35UPDATED_ON), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$pit_identifiers.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.active)
  THEN
    IF (cg$tmp_rec.active != cg$old_rec.active)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P40ACTIVE), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$pit_identifiers.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.usedby)
  THEN
    IF (cg$tmp_rec.usedby != cg$old_rec.usedby)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P45USEDBY), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$pit_identifiers.lck');
      any_modified := TRUE;
    END IF;
  END IF;

  IF (any_modified)
  THEN
    cg$errors.raise_failure;
  END IF;

  -- Application_logic Post-Lock <<Start>>
  -- Application_logic Post-Lock << End >>
END lck;

--------------------------------------------------------------------------------
-- Name:        cascade_update
-- Description: Updates all child tables affected by a change to pit_identifiers
--
-- Parameters:  cg$rec     the record of pit_identifiers current values
--              cg$old_rec the record of pit_identifiers previous values
--------------------------------------------------------------------------------
PROCEDURE cascade_update( cg$new_rec IN OUT cg$row_type
                        , cg$old_rec IN cg$row_type
                        )
IS
BEGIN
  NULL;
END cascade_update;

--------------------------------------------------------------------------------
-- Name:        cascade_delete
-- Description: Delete all child tables affected by a delete to pit_identifiers
--
-- Parameters:  cg$rec     the record of pit_identifiers current values
--------------------------------------------------------------------------------
PROCEDURE cascade_delete(cg$old_rec IN OUT cg$row_type)
IS
BEGIN
  NULL;
END cascade_delete;

END cg$pit_identifiers;
/
SHOW ERROR