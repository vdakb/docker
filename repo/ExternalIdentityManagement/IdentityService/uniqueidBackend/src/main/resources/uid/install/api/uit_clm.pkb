-- /Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityService/uniqueidBackend/src/main/resources/install/uid/api/uit_clm.pkb
--
--   Einfuehrung einer personenbezogenen ID im Progamm Polizei 2020
--   Unique Identifier (CLM)
--   Version 1.2 | Stand 15.03.2022
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Body for Table 'uit_claims'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
-- Name:        cg$uit_claims
-- Description: uit_claims table API package definitions
--------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY cg$uit_claims
IS

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
  IF (con_name = 'UIT_CLM_PK')
  THEN
    cg$errors.push( NVL(UIT_CLM_PK, cg$errors.string(cg$errors.API_PK_CON_VIOLATED, cg$errors.APIMSG_PK_VIOLAT, 'UIT_CLM_PK', 'uit_claims'))
                  , 'E'
                  , 'API'
                  , cg$errors.API_PK_CON_VIOLATED
                  , loc
                  );
  ELSIF (con_name = 'UIT_CLM_TNT_FK')
  THEN
    cg$errors.push( NVL(UIT_CLM_TNT_FK, cg$errors.string(cg$errors.API_FK_CON_VIOLATED, cg$errors.APIMSG_FK_VIOLAT, 'UIT_CLM_TNT_FK', 'uit_claims'))
                  , 'E'
                  , 'API'
                  , cg$errors.API_FK_CON_VIOLATED
                  , loc
                  );
  ELSIF (con_name = 'UIT_CLM_ROL_FK')
  THEN
    cg$errors.push( NVL(UIT_CLM_ROL_FK, cg$errors.string(cg$errors.API_FK_CON_VIOLATED, cg$errors.APIMSG_FK_VIOLAT, 'UIT_CLM_ROL_FK', 'uit_claims'))
                  , 'E'
                  , 'API'
                  , cg$errors.API_FK_CON_VIOLATED
                  , loc
                  );
  ELSIF (con_name = 'UIT_CLM_USR_FK')
  THEN
    cg$errors.push( NVL(UIT_CLM_USR_FK, cg$errors.string(cg$errors.API_FK_CON_VIOLATED, cg$errors.APIMSG_FK_VIOLAT, 'UIT_CLM_USR_FK', 'uit_claims'))
                  , 'E'
                  , 'API'
                  , cg$errors.API_FK_CON_VIOLATED
                  , loc
                  );
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
                , 'cg$uit_claims.raise_uk_not_updateable'
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
                , 'cg$uit_claims.raise_fk_not_transferable'
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
    cg$rec.created_by := cg$sessions.get_identity;
    cg$rec.created_on := sysdate;
  END IF;
  cg$rec.rowversion := hextoraw(rawtohex(cg$rec.rowversion) + 1);
  cg$rec.updated_by := cg$sessions.get_identity;
  cg$rec.updated_on := sysdate;
EXCEPTION
  WHEN NO_DATA_FOUND
  THEN
    NULL;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$uit_claims.up_autogen_columns.denorm');
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
  IF (cg$val_rec.tnt_id IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P10TNT_ID), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.rol_id IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P15ROL_ID), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.usr_id IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P20USR_ID), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.rowversion IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P25ROWVERSION), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.created_by IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P30CREATED_BY), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.created_on IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P35CREATED_ON), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.updated_by IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P40UPDATED_BY), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.updated_on IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P45UPDATED_ON), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
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

  -- Auto-generated and uppercased columns
  up_autogen_columns(cg$rec, cg$ind, 'INS', do_ins);

  called_from_package := TRUE;
  IF (do_ins)
  THEN
    validate_foreign_keys_ins(cg$rec);
    INSERT INTO uit_claims (
      tnt_id
    , rol_id
    , usr_id
    , rowversion
    , created_by
    , created_on
    , updated_by
    , updated_on
    ) VALUES (
      cg$rec.tnt_id
    , cg$rec.rol_id
    , cg$rec.usr_id
    , cg$rec.rowversion
    , cg$rec.created_by
    , cg$rec.created_on
    , cg$rec.updated_by
    , cg$rec.updated_on
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
    validate_mandatory(cg$rec, 'cg$uit_claims.ins.mandatory_missing');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.check_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_CHECK_CON, 'cg$uit_claims.ins.check_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.fk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_FOREIGN_KEY, 'cg$uit_claims.ins.fk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.uk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_UNIQUE_KEY, 'cg$uit_claims.ins.uk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$uit_claims.ins.others');
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

  IF (cg$pk.tnt_id IS NULL)
  THEN
    cg$upd_rec.tnt_id := cg$rec.tnt_id;
  ELSE
    cg$upd_rec.tnt_id := cg$pk.tnt_id;
  END IF;
  cg$old_rec.tnt_id := cg$upd_rec.tnt_id;
  IF (cg$pk.rol_id IS NULL)
  THEN
    cg$upd_rec.rol_id := cg$rec.rol_id;
  ELSE
    cg$upd_rec.rol_id := cg$pk.rol_id;
  END IF;
  cg$old_rec.rol_id := cg$upd_rec.rol_id;
  IF (cg$pk.usr_id IS NULL)
  THEN
    cg$upd_rec.usr_id := cg$rec.usr_id;
  ELSE
    cg$upd_rec.usr_id := cg$pk.usr_id;
  END IF;
  cg$old_rec.usr_id := cg$upd_rec.usr_id;
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
    -- Report error if attempt to update non updateable Primary Key UIT_CLM_PK
    IF NOT (cg$ind.tnt_id)
    THEN
      cg$rec.tnt_id := cg$upd_rec.tnt_id;
    END IF;
    IF NOT (cg$ind.rol_id)
    THEN
      cg$rec.rol_id := cg$upd_rec.rol_id;
    END IF;
    IF NOT (cg$ind.usr_id)
    THEN
      cg$rec.usr_id := cg$upd_rec.usr_id;
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
  ELSE
    -- Report error if attempt to update non updateable Primary Key UIT_CLM_PK
    IF cg$ind.tnt_id OR cg$ind.rol_id OR cg$ind.usr_id
    THEN
      raise_uk_not_updateable('UIT_CLM_PK');
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
        UPDATE uit_claims
        SET    rowversion = cg$rec.rowversion
        ,      created_by = cg$rec.created_by
        ,      created_on = cg$rec.created_on
        ,      updated_by = cg$rec.updated_by
        WHERE  tnt_id     = cg$rec.tnt_id
        AND    rol_id     = cg$rec.rol_id
        AND    usr_id     = cg$rec.usr_id
        ;
      ELSE
        UPDATE uit_claims
        SET    rowversion = cg$rec.rowversion
        ,      created_by = cg$rec.created_by
        ,      created_on = cg$rec.created_on
        ,      updated_by = cg$rec.updated_by
        WHERE  rowid      = cg$rec.the_rowid
        ;
      END IF;
      sel(cg$rec);
      cascade_update(cg$rec, cg$old_rec);
      called_from_package := called_from;
    END;
  END IF;

  IF NOT (do_upd)
  THEN
    cg$table(idx).tnt_id        := cg$rec.tnt_id;
    cg$tableind(idx).tnt_id     := cg$ind.tnt_id;
    cg$table(idx).rol_id        := cg$rec.rol_id;
    cg$tableind(idx).rol_id     := cg$ind.rol_id;
    cg$table(idx).usr_id        := cg$rec.usr_id;
    cg$tableind(idx).usr_id     := cg$ind.usr_id;
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
    validate_mandatory(cg$rec, 'cg$uit_claims.upd.upd_mandatory_null');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.check_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_CHECK_CON, 'cg$uit_claims.upd.check_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.fk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_FOREIGN_KEY, 'cg$uit_claims.upd.fk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.uk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_UNIQUE_KEY, 'cg$uit_claims.upd.uk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$uit_claims.upd.others');
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
      cg$rec.tnt_id := cg$pk.tnt_id;
      cg$rec.rol_id := cg$pk.rol_id;
      cg$rec.usr_id := cg$pk.usr_id;
      sel(cg$rec);

      validate_foreign_keys_del(cg$rec);

      IF cg$pk.the_rowid IS NULL
      THEN
        DELETE uit_claims
        WHERE  tnt_id = cg$pk.tnt_id
        AND    rol_id = cg$pk.rol_id
        AND    usr_id = cg$pk.usr_id
        ;
      ELSE
        DELETE uit_claims
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
    err_msg(SQLERRM, cg$errors.ERR_DELETE_RESTRICT, 'cg$uit_claims.del.delete_restrict');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN NO_DATA_FOUND
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_ROW_DEL, cg$errors.ROW_DEL), 'E', 'ORA', SQLCODE, 'cg$uit_claims.del.NO_DATA_FOUND');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$uit_claims.del.others');
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
    SELECT tnt_id
    ,      rol_id
    ,      usr_id
    ,      rowversion
    ,      created_by
    ,      created_on
    ,      updated_by
    ,      updated_on
    ,      rowid
    INTO   cg$sel_rec.tnt_id
    ,      cg$sel_rec.rol_id
    ,      cg$sel_rec.usr_id
    ,      cg$sel_rec.rowversion
    ,      cg$sel_rec.created_by
    ,      cg$sel_rec.created_on
    ,      cg$sel_rec.updated_by
    ,      cg$sel_rec.updated_on
    ,      cg$sel_rec.the_rowid
    FROM   uit_claims
    WHERE  tnt_id = cg$sel_rec.tnt_id
    AND    rol_id = cg$sel_rec.rol_id
    AND    usr_id = cg$sel_rec.usr_id
    ;
  ELSE
    SELECT tnt_id
    ,      rol_id
    ,      usr_id
    ,      rowversion
    ,      created_by
    ,      created_on
    ,      updated_by
    ,      updated_on
    ,      rowid
    INTO   cg$sel_rec.tnt_id
    ,      cg$sel_rec.rol_id
    ,      cg$sel_rec.usr_id
    ,      cg$sel_rec.rowversion
    ,      cg$sel_rec.created_by
    ,      cg$sel_rec.created_on
    ,      cg$sel_rec.updated_by
    ,      cg$sel_rec.updated_on
    ,      cg$sel_rec.the_rowid
    FROM   uit_claims
    WHERE  rowid = cg$sel_rec.the_rowid
    ;
  END IF;
EXCEPTION
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$uit_claims.sel.others');
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
        SELECT tnt_id
        ,      rol_id
        ,      usr_id
        ,      rowversion
        ,      created_by
        ,      created_on
        ,      updated_by
        ,      updated_on
        INTO   cg$tmp_rec.tnt_id
        ,      cg$tmp_rec.rol_id
        ,      cg$tmp_rec.usr_id
        ,      cg$tmp_rec.rowversion
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        FROM   uit_claims
        WHERE  tnt_id = cg$old_rec.tnt_id
        AND    rol_id = cg$old_rec.rol_id
        AND    usr_id = cg$old_rec.usr_id
        FOR UPDATE NOWAIT;
      ELSE
        SELECT tnt_id
        ,      rol_id
        ,      usr_id
        ,      rowversion
        ,      created_by
        ,      created_on
        ,      updated_by
        ,      updated_on
        INTO   cg$tmp_rec.tnt_id
        ,      cg$tmp_rec.rol_id
        ,      cg$tmp_rec.usr_id
        ,      cg$tmp_rec.rowversion
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        FROM   uit_claims
        WHERE  rowid = cg$old_rec.the_rowid
        FOR UPDATE NOWAIT;
      END IF;
    ELSE
      IF cg$old_rec.the_rowid IS NULL
      THEN
        SELECT tnt_id
        ,      rol_id
        ,      usr_id
        ,      rowversion
        ,      created_by
        ,      created_on
        ,      updated_by
        ,      updated_on
        INTO   cg$tmp_rec.tnt_id
        ,      cg$tmp_rec.rol_id
        ,      cg$tmp_rec.usr_id
        ,      cg$tmp_rec.rowversion
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        FROM   uit_claims
        WHERE  tnt_id = cg$old_rec.tnt_id
        AND    rol_id = cg$old_rec.rol_id
        AND    usr_id = cg$old_rec.usr_id
        FOR UPDATE;
      ELSE
        SELECT tnt_id
        ,      rol_id
        ,      usr_id
        ,      rowversion
        ,      created_by
        ,      created_on
        ,      updated_by
        ,      updated_on
        INTO   cg$tmp_rec.tnt_id
        ,      cg$tmp_rec.rol_id
        ,      cg$tmp_rec.usr_id
        ,      cg$tmp_rec.rowversion
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        FROM   uit_claims
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
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_LCK, cg$errors.ROW_LCK), 'E', 'ORA', SQLCODE, 'cg$uit_claims.lck.resource_busy');
      cg$errors.raise_failure;
    WHEN NO_DATA_FOUND
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_DEL, cg$errors.ROW_DEL), 'E', 'ORA', SQLCODE, 'cg$uit_claims.lck.NO_DATA_FOUND');
      cg$errors.raise_failure;
    WHEN OTHERS
    THEN
      cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$uit_claims.lck.others');
      cg$errors.raise_failure;
    END;

  --
  -- Optional Columns
  --

  --
  -- Mandatory Columns
  --
  IF (cg$old_ind.tnt_id)
  THEN
    IF (cg$tmp_rec.tnt_id != cg$old_rec.tnt_id)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P10TNT_ID), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$uit_claims.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.rol_id)
  THEN
    IF (cg$tmp_rec.rol_id != cg$old_rec.rol_id)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P15ROL_ID), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$uit_claims.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.usr_id)
  THEN
    IF (cg$tmp_rec.usr_id != cg$old_rec.usr_id)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P20USR_ID), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$uit_claims.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.rowversion)
  THEN
    IF (cg$tmp_rec.rowversion != cg$old_rec.rowversion)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P25ROWVERSION), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$uit_claims.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.created_by)
  THEN
    IF (cg$tmp_rec.created_by != cg$old_rec.created_by)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P30CREATED_BY), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$uit_claims.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.created_on)
  THEN
    IF (cg$tmp_rec.created_on != cg$old_rec.created_on)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P35CREATED_ON), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$uit_claims.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.updated_by)
  THEN
    IF (cg$tmp_rec.updated_by != cg$old_rec.updated_by)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P40UPDATED_BY), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$uit_claims.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.updated_on)
  THEN
    IF (cg$tmp_rec.updated_on != cg$old_rec.updated_on)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P45UPDATED_ON), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$uit_claims.lck');
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
-- Description: Updates all child tables affected by a change to uit_claims
--
-- Parameters:  cg$rec     the record of uit_claims current values
--              cg$old_rec the record of uit_claims previous values
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
-- Description: Delete all child tables affected by a delete to uit_claims
--
-- Parameters:  cg$rec     the record of uit_claims current values
--------------------------------------------------------------------------------
PROCEDURE cascade_delete(cg$old_rec IN OUT cg$row_type)
IS
BEGIN
  NULL;
END cascade_delete;

END cg$uit_claims;
/
SHOW ERROR