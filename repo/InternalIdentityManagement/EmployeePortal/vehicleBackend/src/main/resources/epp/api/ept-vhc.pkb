-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\portalBackend\..\vehicleBackend\src\main\resources\dbs\epp\api\ept-vhc.pkb
--
-- Generated for Oracle Database 12c on Sun May 24 17:30:57 2020 by Server Generator 10.1.2.93.10

PROMPT Creating API Package Implementation for Table 'ept_vehicle_colors'
--------------------------------------------------------------------------------
-- Name:        cg$ept_vehicle_colors
-- Description: cg$ept_vehicle_colors table API package definitions
--------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY cg$ept_vehicle_colors
IS

PROCEDURE validate_mandatory( cg$val_rec IN     cg$row_type
                            , loc        IN     VARCHAR2 DEFAULT ''
                            );
PROCEDURE up_autogen_columns( cg$rec     IN OUT cg$row_type
                            , cg$ind     IN OUT cg$ind_type
                            , operation  IN     VARCHAR2 DEFAULT 'INS'
                            , do_denorm  IN     BOOLEAN DEFAULT TRUE
                            );
PROCEDURE err_msg           ( msg        IN     VARCHAR2
                            , type       IN     INTEGER
                            , loc        IN     VARCHAR2 DEFAULT ''
                            );

--------------------------------------------------------------------------------
-- Name:        doLobs
--
-- Description: This function is updating lob columns
--
-- Parameters:  cg$rec  Record of row to be inserted
--              cg$ind  Record of columns specifically set
--------------------------------------------------------------------------------
PROCEDURE doLobs(cg$rec IN OUT cg$row_type, cg$ind IN OUT cg$ind_type)
IS
BEGIN
  NULL;
END doLobs;

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
  cg$errors.push( cg$errors.get_text(cg$errors.API_UNIQUE_KEY_UPDATE, cg$errors.ERR_UK_UPDATE, uk)
                , 'E'
                , 'API'
                , cg$errors.API_UNIQUE_KEY_UPDATE
                , 'cg$ept_vehicle_colors.raise_uk_not_updateable'
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
  cg$errors.push( cg$errors.get_text(cg$errors.API_FOREIGN_KEY_TRANS, cg$errors.ERR_FK_TRANS, fk)
                , 'E'
                , 'API'
                , cg$errors.API_FOREIGN_KEY_TRANS
                , 'cg$ept_vehicle_colors.raise_fk_not_transferable'
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
    cg$rec.created_by := UPPER(NVL(cg$rec.created_by, epp$session.get_identity));
    cg$rec.created_on := sysdate;
  END IF;
  cg$rec.rowversion := TO_CHAR(systimestamp, 'YYYYMMDDHH24MISS.FF');
  cg$rec.updated_by := UPPER(NVL(cg$rec.created_by, epp$session.get_identity));
  cg$rec.updated_on := sysdate;
EXCEPTION
  WHEN no_data_found
  THEN
    NULL;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$ept_vehicle_colors.up_autogen_columns.denorm');
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
    cg$errors.push(cg$errors.get_text(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P10ID), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.rowversion IS NULL)
  THEN
    cg$errors.push(cg$errors.get_text(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P15ROWVERSION), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.created_by IS NULL)
  THEN
    cg$errors.push(cg$errors.get_text(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P20CREATED_BY), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.created_on IS NULL)
  THEN
    cg$errors.push(cg$errors.get_text(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P25CREATED_ON), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.updated_by IS NULL)
  THEN
    cg$errors.push(cg$errors.get_text(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P30UPDATED_BY), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.updated_on IS NULL)
  THEN
    cg$errors.push(cg$errors.get_text(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P35UPDATED_ON), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.meaning IS NULL)
  THEN
    cg$errors.push(cg$errors.get_text(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P40MEANING), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
END validate_mandatory;

--------------------------------------------------------------------------------
-- Name:        validate_foreign_keys
--
-- Description: Checks all mandatory columns are not null and raises appropriate
--              error if not satisfied
--
-- Parameters:  cg$rec Record of row to be checked
--------------------------------------------------------------------------------
PROCEDURE validate_foreign_keys_ins(cg$rec IN cg$row_type)
IS
  fk_check INTEGER;
BEGIN
  NULL;
END;

PROCEDURE validate_foreign_keys_upd(cg$rec IN cg$row_type, cg$old_rec IN cg$row_type, cg$ind IN cg$ind_type)
IS
  fk_check INTEGER;
BEGIN
  NULL;
END;

PROCEDURE validate_foreign_keys_del(cg$rec IN cg$row_type)
IS
  fk_check INTEGER;
BEGIN
  NULL;
END;

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

  --  Defaulted

  -- Auto-generated and uppercased columns
  up_autogen_columns(cg$rec, cg$ind, 'INS', do_ins);

  called_from_package := TRUE;
  IF (do_ins)
  THEN
    validate_foreign_keys_ins(cg$rec);
    validate_arc(cg$rec);
    validate_domain(cg$rec);
    INSERT INTO ept_vehicle_colors (
      id
    , rowversion
    , created_by
    , created_on
    , updated_by
    , updated_on
    , meaning
    )
    VALUES (
      cg$rec.id
    , cg$rec.rowversion
    , cg$rec.created_by
    , cg$rec.created_on
    , cg$rec.updated_by
    , cg$rec.updated_on
    , cg$rec.meaning
    );
    doLobs(cg$rec, cg$ind);
    sel(cg$rec);
    upd_oper_denorm2(cg$rec, cg$tmp_rec, cg$ind, 'INS');
  END IF;
  called_from_package := FALSE;

  -- Application_logic Post-Insert <<Start>>
  -- Application_logic Post-Insert << End >>
EXCEPTION
  WHEN cg$errors.cg$error
  THEN
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.mandatory_missing
  THEN
    validate_mandatory(cg$rec, 'cg$ept_vehicle_colors.ins.mandatory_missing');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.check_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_CHECK_CON, 'cg$ept_vehicle_colors.ins.check_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.fk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_FOREIGN_KEY, 'cg$ept_vehicle_colors.ins.fk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.uk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_UNIQUE_KEY, 'cg$ept_vehicle_colors.ins.uk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$$ept_vehicle_colors.ins.others');
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
    -- Report error if attempt to update non updateable Primary Key EPT_VHC_PK
    IF (cg$ind.id AND cg$rec.id != cg$upd_rec.id)
    THEN
      raise_uk_not_updateable('EPP_VHC_PK');
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
    IF NOT (cg$ind.meaning)
    THEN
      cg$rec.meaning := cg$upd_rec.meaning;
    END IF;
  ELSE
    -- Perform checks if called from a trigger
    -- Indicators are only set on changed values
    NULL;
    -- Report error if attempt to update non updateable Primary Key EPT_VHC_PK
    IF (cg$ind.id)
    THEN
      raise_uk_not_updateable('EPP_VHC_PK');
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
      validate_arc(cg$rec);
      validate_domain(cg$rec, cg$ind);
      validate_domain_cascade_update(cg$old_rec);
      IF cg$rec.the_rowid IS NULL
      THEN
        UPDATE ept_vehicle_colors
        SET    rowversion  = cg$rec.rowversion
        ,      created_by  = cg$rec.created_by
        ,      created_on  = cg$rec.created_on
        ,      updated_by  = cg$rec.updated_by
        ,      updated_on  = cg$rec.updated_on
        ,      meaning     = cg$rec.meaning
        WHERE  id          = cg$rec.id
        ;
      ELSE
        UPDATE ept_vehicle_colors
        SET    rowversion  = cg$rec.rowversion
        ,      created_by  = cg$rec.created_by
        ,      created_on  = cg$rec.created_on
        ,      updated_by  = cg$rec.updated_by
        ,      updated_on  = cg$rec.updated_on
        ,      meaning     = cg$rec.meaning
        WHERE  rowid       = cg$rec.the_rowid
        ;
      END IF;
      sel(cg$rec);
      upd_denorm2(cg$rec, cg$ind);
      upd_oper_denorm2(cg$rec, cg$old_rec, cg$ind, 'UPD');
      cascade_update(cg$rec, cg$old_rec);
      domain_cascade_update(cg$rec, cg$ind, cg$old_rec);
      called_from_package := called_from;
    END;
  END IF;

  IF NOT (do_upd)
  THEN
    cg$table(idx).id         := cg$rec.id;
    cg$table(idx).rowversion := cg$rec.rowversion;
    cg$table(idx).created_by := cg$rec.created_by;
    cg$table(idx).created_on := cg$rec.created_on;
    cg$table(idx).updated_by := cg$rec.updated_by;
    cg$table(idx).updated_on := cg$rec.updated_on;
    cg$table(idx).meaning    := cg$rec.meaning;

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
    validate_mandatory(cg$rec, 'cg$ept_vehicle_colors.upd.upd_mandatory_null');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.check_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_CHECK_CON, 'cg$ept_vehicle_colors.upd.check_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.fk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_FOREIGN_KEY, 'cg$ept_vehicle_colors.upd.fk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.uk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_UNIQUE_KEY, 'cg$ept_vehicle_colors.upd.uk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$ept_vehicle_colors.upd.others');
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
      validate_domain_cascade_delete(cg$rec);

      IF cg$pk.the_rowid IS NULL
      THEN
        DELETE ept_vehicle_colors
        WHERE  id = cg$pk.id
        ;
      ELSE
        DELETE ept_vehicle_colors
        WHERE  rowid = cg$pk.the_rowid
        ;
      END IF;
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
    err_msg(SQLERRM, cg$errors.ERR_DELETE_RESTRICT, 'cg$ept_vehicle_colors.upd.delete_restrict');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN no_data_found
  THEN
    cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_DEL, cg$errors.ROW_DEL), 'E', 'ORA', SQLCODE, 'cg$ept_vehicle_colors.del.no_data_found');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$ept_vehicle_colors.del.others');
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
    ,      meaning
    ,      rowid
    INTO   cg$sel_rec.id
    ,      cg$sel_rec.rowversion
    ,      cg$sel_rec.created_by
    ,      cg$sel_rec.created_on
    ,      cg$sel_rec.updated_by
    ,      cg$sel_rec.updated_on
    ,      cg$sel_rec.meaning
    ,      cg$sel_rec.the_rowid
    FROM   ept_vehicle_colors
    WHERE  id = cg$sel_rec.id
    ;
  ELSE
    SELECT id
    ,      rowversion
    ,      created_by
    ,      created_on
    ,      updated_by
    ,      updated_on
    ,      meaning
    ,      rowid
    INTO   cg$sel_rec.id
    ,      cg$sel_rec.rowversion
    ,      cg$sel_rec.created_by
    ,      cg$sel_rec.created_on
    ,      cg$sel_rec.updated_by
    ,      cg$sel_rec.updated_on
    ,      cg$sel_rec.meaning
    ,      cg$sel_rec.the_rowid
    FROM   ept_vehicle_colors
    WHERE  rowid = cg$sel_rec.the_rowid
    ;
  END IF;
EXCEPTION
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$ept_vehicle_colors.sel.others');
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
  -- Application_logic Pre-Lock <<End>>

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
        ,      meaning
        INTO   cg$tmp_rec.id
        ,      cg$tmp_rec.rowversion
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        ,      cg$tmp_rec.meaning
        FROM   ept_vehicle_colors
        WHERE  id = cg$old_rec.id
        FOR UPDATE NOWAIT
        ;
      ELSE
        SELECT id
        ,      rowversion
        ,      created_by
        ,      created_on
        ,      updated_by
        ,      updated_on
        ,      meaning
        INTO   cg$tmp_rec.id
        ,      cg$tmp_rec.rowversion
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        ,      cg$tmp_rec.meaning
        FROM   ept_vehicle_colors
        WHERE  rowid = cg$old_rec.the_rowid
        FOR UPDATE NOWAIT
        ;
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
        ,      meaning
        INTO   cg$tmp_rec.id
        ,      cg$tmp_rec.rowversion
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        ,      cg$tmp_rec.meaning
        FROM   ept_vehicle_colors
        WHERE  id = cg$old_rec.id
        FOR UPDATE
        ;
      ELSE
        SELECT id
        ,      rowversion
        ,      created_by
        ,      created_on
        ,      updated_by
        ,      updated_on
        ,      meaning
        INTO   cg$tmp_rec.id
        ,      cg$tmp_rec.rowversion
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        ,      cg$tmp_rec.meaning
        FROM   ept_vehicle_colors
        WHERE  rowid = cg$old_rec.the_rowid
        FOR UPDATE
        ;
      END IF;
    END IF;
  EXCEPTION
    WHEN cg$errors.cg$error
    THEN
      cg$errors.raise_failure;
    WHEN cg$errors.resource_busy
    THEN
      cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_LCK, cg$errors.ROW_LCK), 'E', 'ORA', SQLCODE, 'cg$ept_vehicle_colors.lck.resource_busy');
      cg$errors.raise_failure;
    WHEN no_data_found
    THEN
      cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_DEL, cg$errors.ROW_DEL), 'E', 'ORA', SQLCODE, 'cg$$ept_vehicle_colors.lck.no_data_found');
      cg$errors.raise_failure;
    WHEN OTHERS
    THEN
      cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$ept_vehicle_colors.lck.others');
      cg$errors.raise_failure;
  END;

  -- Optional Columns
  -- Mandatory Columns
  IF (cg$old_ind.id)
  THEN
    IF (cg$tmp_rec.id != cg$old_rec.id)
    THEN
      cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P10ID), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$ept_vehicle_colors.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.rowversion)
  THEN
    IF (cg$tmp_rec.rowversion != cg$old_rec.rowversion)
    THEN
      cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P15ROWVERSION), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$ept_vehicle_colors.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.created_by)
  THEN
    IF (cg$tmp_rec.created_by != cg$old_rec.created_by)
    THEN
      cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P20CREATED_BY), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$ept_vehicle_colors.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.created_on)
  THEN
    IF (cg$tmp_rec.created_on != cg$old_rec.created_on)
    THEN
      cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P25CREATED_ON), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$ept_vehicle_colors.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.updated_by)
  THEN
    IF (cg$tmp_rec.updated_by != cg$old_rec.updated_by)
    THEN
      cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P30UPDATED_BY), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$ept_vehicle_colors.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.updated_on)
  THEN
    IF (cg$tmp_rec.updated_on != cg$old_rec.updated_on)
    THEN
      cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P35UPDATED_ON), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$ept_vehicle_colors.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.meaning)
  THEN
    IF (cg$tmp_rec.meaning != cg$old_rec.meaning)
    THEN
      cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P40MEANING), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$ept_vehicle_colors.lck');
      any_modified := TRUE;
    END IF;
  END IF;

  IF (any_modified)
  THEN
    cg$errors.raise_failure;
  END IF;

  -- Application_logic Post-Lock <<Start>>
  -- Application_logic Post-Lock <<End>>
END lck;

--------------------------------------------------------------------------------
-- Name:        cascade_update
--
-- Description: Updates all child tables affected by a change to ept_vehicle_colors
--
-- Parameters:  cg$rec     Record of ept_vehicle_colors current values
--              cg$old_rec Record of ept_vehicle_colors previous values
--------------------------------------------------------------------------------
PROCEDURE cascade_update(cg$new_rec IN OUT cg$row_type, cg$old_rec IN cg$row_type)
IS
BEGIN
  NULL;
END cascade_update;

--------------------------------------------------------------------------------
-- Name:        validate_domain_cascade_update
--
-- Description: Implement the Domain Key Constraint Cascade Updates Resticts rule
--              of each child table that references this table ept_vehicle_colors
--
-- Parameters:  cg$old_rec     Record of ept_vehicle_colors current values
--------------------------------------------------------------------------------
PROCEDURE validate_domain_cascade_update(cg$old_rec IN cg$row_type)
IS
  dk_check INTEGER;
BEGIN
  NULL;
END validate_domain_cascade_update;

--------------------------------------------------------------------------------
-- Name:        domain_cascade_update
--
-- Description: Implement the Domain Key Constraint Cascade Updates rules of each
--              child table that references this table ept_vehicle_colors
--
-- Parameters:  cg$new_rec  New values for ept_vehicle_colors's domain key constraint columns
--              cg$new_ind  Indicates changed ept_vehicle_colors's domain key constraint columns
--              cg$old_rec  Current values for ept_vehicle_colors's domain key constraint columns
--------------------------------------------------------------------------------
PROCEDURE domain_cascade_update(cg$new_rec IN OUT cg$row_type, cg$new_ind IN OUT cg$ind_type, cg$old_rec IN cg$row_type)
IS
BEGIN
  NULL;
END domain_cascade_update;

--------------------------------------------------------------------------------
-- Name:        cascade_delete
--
-- Description: Delete all child tables affected by a delete to ept_vehicle_colors
--
-- Parameters:  cg$rec     Record of ept_vehicle_colors current values
--------------------------------------------------------------------------------
PROCEDURE cascade_delete(cg$old_rec IN OUT cg$row_type)
IS
BEGIN
  NULL;
END cascade_delete;

--------------------------------------------------------------------------------
-- Name:        domain_cascade_delete
--
-- Description: Implement the Domain Key Constraint Cascade Delete rules of each
--              child table that references this table ept_vehicle_colors
--
-- Parameters:  cg$old_rec     Record of ept_vehicle_colors current values
--------------------------------------------------------------------------------
PROCEDURE domain_cascade_delete(cg$old_rec IN cg$row_type)
IS
BEGIN
  NULL;
END domain_cascade_delete;

--------------------------------------------------------------------------------
-- Name:        validate_domain_cascade_delete
--
-- Description: Implement the Domain Key Constraint Cascade Delete Restricts rule
--              of each child table that references this table ept_vehicle_colors
--
-- Parameters:  cg$old_rec     Record of ept_vehicle_colors current values
--------------------------------------------------------------------------------
PROCEDURE validate_domain_cascade_delete(cg$old_rec IN cg$row_type)
IS
  dk_check INTEGER;
BEGIN
  NULL;
END validate_domain_cascade_delete;

--------------------------------------------------------------------------------
-- Name:        validate_arc
--
-- Description: Checks for adherence to arc relationship
--
-- Parameters:  cg$rec     Record of ept_vehicle_colors current values
--------------------------------------------------------------------------------
PROCEDURE validate_arc(cg$rec IN OUT cg$row_type)
IS
BEGIN
  NULL;
END validate_arc;

--------------------------------------------------------------------------------
-- Name:        validate_domain
--
-- Description: Checks against reference table for values lying in a domain
--
-- Parameters:  cg$rec     Record of ept_vehicle_colors current values
--------------------------------------------------------------------------------
PROCEDURE validate_domain(cg$rec IN OUT cg$row_type, cg$ind IN cg$ind_type DEFAULT cg$ind_true)
IS
  dummy      NUMBER;
  found      BOOLEAN;
  no_tabview EXCEPTION;
  PRAGMA EXCEPTION_INIT(no_tabview, -942);
BEGIN
  NULL;
EXCEPTION
  WHEN cg$errors.cg$error
  THEN
    cg$errors.raise_failure;
  WHEN no_tabview
  THEN
    cg$errors.push( cg$errors.get_text(cg$errors.API_RV_TAB_NOT_FOUND, cg$errors.APIMSG_RV_TAB_NOT_FOUND, '"epp REF_CODES"','ept_vehicle_colors')
                  , 'E'
                  , 'API'
                  , 'cg$errors.API_RV_TAB_NOT_FOUND'
                  , 'cg$ept_vehicle_colors.v_domain.no_reftable_found'
                  );
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$ept_vehicle_colors.v_domain.no_reftable_found');
    cg$errors.raise_failure;
END validate_domain;

--------------------------------------------------------------------------------
-- Name:        domain_cascade_upd
--
-- Description: Update the Domain Constraint Key columns of ept_vehicle_colors when the
--              Cascade Update rule is Cascades and the domain table has been
--              updated. Called from <Domain Table pkg>.domain_cascade_update().
--
-- Parameters:  cg$rec      New values for ept_vehicle_colors's domain key constraint columns
--              cg$ind      Indicates changed ept_vehicle_colors's domain key constraint columns
--              cg$old_rec  Current values for ept_vehicle_colors's domain key constraint columns
--------------------------------------------------------------------------------
PROCEDURE domain_cascade_upd(cg$rec IN OUT cg$row_type, cg$ind IN OUT cg$ind_type, cg$old_rec IN cg$row_type)
IS
  called_from BOOLEAN := called_from_package;
BEGIN
  NULL;
END domain_cascade_upd;

--------------------------------------------------------------------------------
-- Name:        upd_denorm2
--
-- Description: API procedure for simple denormalization
--
-- Parameters:  cg$rec  Record of row to be updated
--              cg$ind  Record of columns specifically set
--------------------------------------------------------------------------------
PROCEDURE upd_denorm2(cg$rec IN cg$row_type, cg$ind IN cg$ind_type)
IS
BEGIN
  NULL;
END upd_denorm2;

--------------------------------------------------------------------------------
-- Name:        upd_oper_denorm
--
-- Description: API procedure for operation denormalization
--
-- Parameters:  cg$rec  Record of row to be updated
--              cg$ind  Record of columns specifically set
--              do_upd  Whether we want the actual UPDATE to occur
--------------------------------------------------------------------------------
PROCEDURE upd_oper_denorm2(cg$rec IN cg$row_type, cg$old_rec IN cg$row_type, cg$ind IN cg$ind_type, operation IN VARCHAR2 DEFAULT 'UPD')
IS
BEGIN
  NULL;
END upd_oper_denorm2;

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
  IF (con_name = 'EPT_VHC_PK')
  THEN
    cg$errors.push( NVL(EPT_VHC_PK, cg$errors.get_text(cg$errors.API_PK_CON_VIOLATED, cg$errors.APIMSG_PK_VIOLAT, 'EPT_VHC_PK', 'ept_vehicle_colors'))
                  , 'E'
                  , 'API'
                  , 'cg$errors.API_PK_CON_VIOLATED'
                  , loc
                  );
  ELSE
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, loc);
  END IF;
END err_msg;

BEGIN
  cg$ind_true.id         := TRUE;
  cg$ind_true.rowversion := TRUE;
  cg$ind_true.created_by := TRUE;
  cg$ind_true.created_on := TRUE;
  cg$ind_true.updated_by := TRUE;
  cg$ind_true.updated_on := TRUE;
  cg$ind_true.meaning    := TRUE;
END cg$ept_vehicle_colors;
/
SHOW ERROR
