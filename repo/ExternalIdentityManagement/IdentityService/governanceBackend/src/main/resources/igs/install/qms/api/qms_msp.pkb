-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\qms_msp.pkb
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Implementation for Table 'qms$message_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
-- Name:        qms$message_properties
-- Description: qms$message_properties table API package definitions
--------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY qms$message_properties
IS

--  Constant default values
D40_SEVERITY         CONSTANT qms_message_properties.severity%TYPE         := 'E';
D50_LOGGING          CONSTANT qms_message_properties.logging%TYPE          := 'N';
D70_SUPPRESS_WARNING CONSTANT qms_message_properties.suppress_warning%TYPE := 'N';
D90_SUPPRESS_ALWAYS  CONSTANT qms_message_properties.suppress_always%TYPE  := 'N';

PROCEDURE   validate_mandatory(cg$val_rec IN cg$row_type, loc IN VARCHAR2 DEFAULT '');
PROCEDURE   up_autogen_columns(cg$rec IN OUT cg$row_type, cg$ind IN OUT cg$ind_type, operation IN VARCHAR2 DEFAULT 'INS', do_denorm IN BOOLEAN DEFAULT TRUE);
PROCEDURE   err_msg(msg  IN VARCHAR2, type IN INTEGER, loc IN VARCHAR2 DEFAULT '');
PROCEDURE   uk_key_updateable(uk IN VARCHAR2);
PROCEDURE   fk_key_transferable(fk IN VARCHAR2);

--------------------------------------------------------------------------------
-- Name:        uk_key_updateable
--
-- Description: Raise appropriate error when unique key updated
--
-- Parameters:  none
--------------------------------------------------------------------------------
PROCEDURE uk_key_updateable(uk IN VARCHAR2)
IS
BEGIN
  cg$errors.push( cg$errors.string(cg$errors.API_UNIQUE_KEY_UPDATE, cg$errors.ERR_UK_UPDATE, uk)
                , 'E'
                , 'API'
                , cg$errors.API_UNIQUE_KEY_UPDATE
                , 'cg$err.uk_key_updateable'
                );
  cg$errors.raise_failure;
END uk_key_updateable;

--------------------------------------------------------------------------------
-- Name:        fk_key_transferable
--
-- Description: Raise appropriate error when foreign key updated
--
-- Parameters:  none
--------------------------------------------------------------------------------
PROCEDURE fk_key_transferable(fk IN VARCHAR2) IS
BEGIN
  cg$errors.push( cg$errors.string(cg$errors.API_FOREIGN_KEY_TRANS, cg$errors.ERR_FK_TRANS, fk)
                , 'E'
                , 'API'
                , cg$errors.API_FOREIGN_KEY_TRANS
                , 'cg$err.fk_key_transferable'
                );
   cg$errors.raise_failure;
END fk_key_transferable;

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
    cg$rec.created_by := user;
    cg$rec.created_on := trunc(sysdate);
  END IF;

  cg$rec.code             := upper(cg$rec.code);
  cg$rec.severity         := upper(cg$rec.severity);
  cg$rec.logging          := upper(cg$rec.logging);
  cg$rec.suppress_warning := upper(cg$rec.suppress_warning);
  cg$rec.suppress_always  := upper(cg$rec.suppress_always);
  cg$rec.constraint_name  := upper(cg$rec.constraint_name);
  cg$rec.created_by       := upper(cg$rec.created_by);
  cg$rec.updated_by       := user;
  cg$rec.updated_on       := trunc(sysdate);
EXCEPTION
  WHEN NO_DATA_FOUND
  THEN
    NULL;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'qms_message_properties.up_autogen_columns.denorm');
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
  IF (cg$val_rec.code IS NULL)
  THEN
    cg$errors.push( cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P10CODE)
                  , 'E'
                  , 'API'
                  , cg$errors.API_MAND_COLUMN_ISNULL
                  , loc
                  );
  END IF;
  IF (cg$val_rec.description IS NULL)
  THEN
    cg$errors.push( cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P15DESCRIPTION)
                  , 'E'
                  , 'API'
                  , cg$errors.API_MAND_COLUMN_ISNULL
                  , loc
                  );
  END IF;
  IF (cg$val_rec.severity IS NULL)
  THEN
    cg$errors.push( cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P20SEVERITY)
                  , 'E'
                  , 'API'
                  , cg$errors.API_MAND_COLUMN_ISNULL
                  , loc
                  );
  END IF;
  IF (cg$val_rec.logging IS NULL)
  THEN
    cg$errors.push( cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P25LOGGING)
                  , 'E'
                  , 'API'
                  , cg$errors.API_MAND_COLUMN_ISNULL
                  , loc
                  );
  END IF;
  IF (cg$val_rec.suppress_warning IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P30SUPPRESS_WARNING)
                  , 'E'
                  , 'API'
                  , cg$errors.API_MAND_COLUMN_ISNULL
                  , loc
                  );
  END IF;
  IF (cg$val_rec.suppress_always IS NULL)
  THEN
    cg$errors.push( cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P35SUPPRESS_ALWAYS)
                  , 'E'
                  , 'API'
                  , cg$errors.API_MAND_COLUMN_ISNULL
                  , loc
                  );
  END IF;
  IF (cg$val_rec.created_by IS NULL)
  THEN
    cg$errors.push( cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P30CREATED_BY)
                  , 'E'
                  , 'API'
                  , cg$errors.API_MAND_COLUMN_ISNULL
                  , loc
                  );
  END IF;
  IF (cg$val_rec.created_on IS NULL)
  THEN
    cg$errors.push( cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P35CREATED_ON)
                  , 'E'
                  , 'API'
                  , cg$errors.API_MAND_COLUMN_ISNULL
                  , loc
                  );
  END IF;
  IF (cg$val_rec.updated_by IS NULL)
  THEN
    cg$errors.push( cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P40UPDATED_BY)
                  , 'E'
                  , 'API'
                  , cg$errors.API_MAND_COLUMN_ISNULL
                  , loc
                  );
  END IF;
  IF (cg$val_rec.updated_on IS NULL)
  THEN
    cg$errors.push( cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P600UPDATED_ON)
                  , 'E'
                  , 'API'
                  , cg$errors.API_MAND_COLUMN_ISNULL
                  , loc
                  );
  END IF;
END validate_mandatory;

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
  IF (con_name = 'QMS_MSP_PK')
  THEN
    cg$errors.push( NVL(QMS_MSP_PK, cg$errors.string(cg$errors.API_PK_CON_VIOLATED ,cg$errors.APIMSG_PK_VIOLAT, 'QMS_MSP_PK', 'qms$message_properties'))
                  , 'E'
                  , 'API'
                  , cg$errors.API_PK_CON_VIOLATED
                  , loc
                  );
  ELSIF (con_name = 'QMS_MSP_UK1')
  THEN
    cg$errors.push( NVL(QMS_MSP_UK1, cg$errors.string(cg$errors.API_UQ_CON_VIOLATED, cg$errors.APIMSG_UK_VIOLAT, 'QMS_MSP_UK1', 'qms$message_properties'))
                  , 'E'
                  , 'API'
                  , cg$errors.API_UQ_CON_VIOLATED
                  , loc
                  );
  ELSE
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, loc);
  END IF;
END err_msg;

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
BEGIN
  --  Application_logic Pre-Insert <<Start>>
  --  Application_logic Pre-Insert << End >>

  --  Defaulted
  IF NOT (cg$ind.severity)
  THEN
    cg$rec.severity := D40_SEVERITY;
  END IF;
  IF NOT (cg$ind.logging)
  THEN
    cg$rec.logging := D50_LOGGING;
  END IF;
  IF NOT (cg$ind.suppress_warning)
  THEN
    cg$rec.suppress_warning := D70_SUPPRESS_WARNING;
  END IF;
  IF NOT (cg$ind.suppress_always)
  THEN
    cg$rec.suppress_always := D90_SUPPRESS_ALWAYS;
  END IF;

  --  Auto-generated and uppercased columns
  up_autogen_columns(cg$rec, cg$ind, 'INS', do_ins);

  called_from_package := TRUE;
  IF (do_ins)
  THEN
    INSERT INTO qms_message_properties (
      code
    , description
    , severity
    , logging
    , suppress_warning
    , suppress_always
    , constraint_name
    , created_by
    , created_on
    , updated_by
    , updated_on
    ) VALUES (
      cg$rec.code
    , cg$rec.description
    , cg$rec.severity
    , cg$rec.logging
    , cg$rec.suppress_warning
    , cg$rec.suppress_always
    , cg$rec.constraint_name
    , cg$rec.created_by
    , cg$rec.created_on
    , cg$rec.updated_by
    , cg$rec.updated_on
    );
    sel(cg$rec);
  END IF;
  called_from_package := FALSE;

  IF NOT (do_ins)
  THEN
    cg$table(idx).code    := cg$rec.code;
    cg$tableind(idx).code := cg$ind.code;

    cg$table(idx).description    := cg$rec.description;
    cg$tableind(idx).description := cg$ind.description;

    cg$table(idx).severity    := cg$rec.severity;
    cg$tableind(idx).severity := cg$ind.severity;

    cg$table(idx).logging    := cg$rec.logging;
    cg$tableind(idx).logging := cg$ind.logging;

    cg$table(idx).suppress_warning    := cg$rec.suppress_warning;
    cg$tableind(idx).suppress_warning := cg$ind.suppress_warning;

    cg$table(idx).suppress_always    := cg$rec.suppress_always;
    cg$tableind(idx).suppress_always := cg$ind.suppress_always;

    cg$table(idx).constraint_name    := cg$rec.constraint_name;
    cg$tableind(idx).constraint_name := cg$ind.constraint_name;

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

  --  Application logic Post-Insert <<Start>>
  --  Application logic Post-Insert << End >>
EXCEPTION
  WHEN cg$errors.cg$error
  THEN
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.mandatory_missing
  THEN
    validate_mandatory(cg$rec, 'qms$message_properties.ins.mandatory_missing');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.check_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_CHECK_CON, 'qms$message_properties.ins.check_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.fk_violation THEN
    err_msg(SQLERRM, cg$errors.ERR_FOREIGN_KEY, 'qms$message_properties.ins.fk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.uk_violation THEN
    err_msg(SQLERRM, cg$errors.ERR_UNIQUE_KEY, 'qms$message_properties.ins.uk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'qms$message_properties.ins.others');
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
  --  Application_logic Pre-Update <<Start>>
  --  Application_logic Pre-Update << End >>

  cg$upd_rec.code := cg$rec.code;
  cg$old_rec.code := cg$rec.code;

  cg$upd_rec.the_rowid := cg$rec.the_rowid;
  cg$old_rec.the_rowid := cg$rec.the_rowid;

  IF (do_upd)
  THEN
    sel(cg$upd_rec);

    --  Check updates to Primary Key QMS_MSP_PK allowed
    IF (cg$ind.code AND cg$rec.code != cg$upd_rec.code)
    THEN
      uk_key_updateable('QMS_MSP_PK');
    END IF;
    IF NOT (cg$ind.code)
    THEN
      cg$rec.code := cg$upd_rec.code;
    END IF;
    IF NOT (cg$ind.description)
    THEN
      cg$rec.description := cg$upd_rec.description;
    END IF;
    IF NOT (cg$ind.severity)
    THEN
      cg$rec.severity := cg$upd_rec.severity;
    END IF;
    IF NOT (cg$ind.logging)
    THEN
      cg$rec.logging := cg$upd_rec.logging;
    END IF;
    IF NOT (cg$ind.suppress_warning)
    THEN
      cg$rec.suppress_warning := cg$upd_rec.suppress_warning;
    END IF;
    IF NOT (cg$ind.suppress_always)
    THEN
      cg$rec.suppress_always := cg$upd_rec.suppress_always;
    END IF;
    IF NOT (cg$ind.constraint_name)
    THEN
      cg$rec.constraint_name := cg$upd_rec.constraint_name;
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
    -- Perform checks if called from a trigger
    -- Indicators are only set on changed values
    NULL;
    --  Check updates to Primary Key QMS_MSP_PK allowed
    IF (cg$ind.code)
    THEN
      uk_key_updateable('QMS_MSP_PK');
    END IF;
  END IF;

  --  Auto-generated and uppercased columns
  up_autogen_columns(cg$rec, cg$ind, 'UPD', do_upd);
  --  Now do update if updateable columns exist
  IF (do_upd)
  THEN
    DECLARE
      old_called BOOLEAN;
    BEGIN
      old_called := called_from_package;
      called_from_package := TRUE;
      sel(cg$old_rec);
      IF cg$rec.the_rowid IS NULL
      THEN
        UPDATE qms_message_properties
        SET    description      = cg$rec.description
        ,      severity         = cg$rec.severity
        ,      logging          = cg$rec.logging
        ,      suppress_warning = cg$rec.suppress_warning
        ,      suppress_always  = cg$rec.suppress_always
        ,      constraint_name  = cg$rec.constraint_name
        ,      created_by       = cg$rec.created_by
        ,      created_on       = cg$rec.created_on
        ,      updated_by       = cg$rec.updated_by
        ,      updated_on       = cg$rec.updated_on
        WHERE  code = cg$rec.code;
      ELSE
        UPDATE qms_message_properties
        SET    description      = cg$rec.description
        ,      severity         = cg$rec.severity
        ,      logging          = cg$rec.logging
        ,      suppress_warning = cg$rec.suppress_warning
        ,      suppress_always  = cg$rec.suppress_always
        ,      constraint_name  = cg$rec.constraint_name
        ,      created_by       = cg$rec.created_by
        ,      created_on       = cg$rec.created_on
        ,      updated_by       = cg$rec.updated_by
        ,      updated_on       = cg$rec.updated_on
        WHERE  rowid = cg$rec.the_rowid;
      END IF;

      sel(cg$rec);

      cascade_update(cg$rec, cg$old_rec);
      called_from_package := old_called;
    END;
  END IF;

  IF NOT (do_upd)
  THEN
    cg$table(idx).code    := cg$rec.code;
    cg$tableind(idx).code := cg$ind.code;

    cg$table(idx).description    := cg$rec.description;
    cg$tableind(idx).description := cg$ind.description;

    cg$table(idx).severity    := cg$rec.severity;
    cg$tableind(idx).severity := cg$ind.severity;

    cg$table(idx).logging    := cg$rec.logging;
    cg$tableind(idx).logging := cg$ind.logging;

    cg$table(idx).suppress_warning    := cg$rec.suppress_warning;
    cg$tableind(idx).suppress_warning := cg$ind.suppress_warning;

    cg$table(idx).suppress_always    := cg$rec.suppress_always;
    cg$tableind(idx).suppress_always := cg$ind.suppress_always;

    cg$table(idx).constraint_name    := cg$rec.constraint_name;
    cg$tableind(idx).constraint_name := cg$ind.constraint_name;

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

  --  Application_logic Post-Update <<Start>>
  --  Application_logic Post-Update << End >>
EXCEPTION
  WHEN cg$errors.cg$error
  THEN
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.upd_mandatory_null
  THEN
    validate_mandatory(cg$rec, 'qms$message_properties.upd.upd_mandatory_null');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.check_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_CHECK_CON, 'qms$message_properties.upd.check_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.fk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_FOREIGN_KEY, 'qms$message_properties.upd.fk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.uk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_UNIQUE_KEY, 'qms$message_properties.upd.uk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'qms$message_properties.upd.others');
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
  --  Application_logic Pre-Delete <<Start>>
  --  Application_logic Pre-Delete << End >>

  --  Delete the record

  called_from_package := TRUE;
  IF (do_del)
  THEN
    DECLARE
      cg$rec cg$row_type;
      cg$old_rec cg$row_type;
      cg$ind cg$ind_type;
    BEGIN
      cg$rec.code := cg$pk.code;
      sel(cg$rec);
      IF cg$pk.the_rowid IS NULL
      THEN
        DELETE qms_message_properties
        WHERE  code = cg$pk.code;
      ELSE
        DELETE qms_message_properties
        WHERE  rowid = cg$pk.the_rowid;
      END IF;
      cascade_delete(cg$rec);
    END;
  END IF;
  called_from_package := FALSE;

  --  Application_logic Post-Delete <<Start>>
  --  Application_logic Post-Delete << End >>
EXCEPTION
  WHEN cg$errors.cg$error
  THEN
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.delete_restrict
  THEN
    err_msg(SQLERRM, cg$errors.ERR_DELETE_RESTRICT, 'qms$message_properties.del.delete_restrict');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN NO_DATA_FOUND
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_ROW_DEL, cg$errors.ROW_DEL), 'E', 'ORA', SQLCODE, 'qms$message_properties.del.NO_DATA_FOUND');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'qms$message_properties.del.others');
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
    SELECT code
    ,      description
    ,      severity
    ,      logging
    ,      suppress_warning
    ,      suppress_always
    ,      constraint_name
    ,      created_by
    ,      created_on
    ,      updated_by
    ,      updated_on
    ,      rowid
    INTO   cg$sel_rec.code
    ,      cg$sel_rec.description
    ,      cg$sel_rec.severity
    ,      cg$sel_rec.logging
    ,      cg$sel_rec.suppress_warning
    ,      cg$sel_rec.suppress_always
    ,      cg$sel_rec.constraint_name
    ,      cg$sel_rec.created_by
    ,      cg$sel_rec.created_on
    ,      cg$sel_rec.updated_by
    ,      cg$sel_rec.updated_on
    ,      cg$sel_rec.the_rowid
    FROM   qms_message_properties
    WHERE  code = cg$sel_rec.code;
  ELSE
    SELECT code
    ,      description
    ,      severity
    ,      logging
    ,      suppress_warning
    ,      suppress_always
    ,      constraint_name
    ,      created_by
    ,      created_on
    ,      updated_by
    ,      updated_on
    ,      rowid
    INTO   cg$sel_rec.code
    ,      cg$sel_rec.description
    ,      cg$sel_rec.severity
    ,      cg$sel_rec.logging
    ,      cg$sel_rec.suppress_warning
    ,      cg$sel_rec.suppress_always
    ,      cg$sel_rec.constraint_name
    ,      cg$sel_rec.created_by
    ,      cg$sel_rec.created_on
    ,      cg$sel_rec.updated_by
    ,      cg$sel_rec.updated_on
    ,      cg$sel_rec.the_rowid
    FROM   qms_message_properties
    WHERE  rowid = cg$sel_rec.the_rowid;
  END IF;
EXCEPTION WHEN OTHERS
THEN
  cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'qms$message_properties.slct.others');
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
  cg$tmp_rec cg$row_type;
  any_modified BOOLEAN := FALSE;
BEGIN
  --  Application_logic Pre-Lock <<Start>>
  --  Application_logic Pre-Lock << End >>

  --  Do the row lock
  BEGIN
    IF (nowait_flag)
    THEN
      IF cg$old_rec.the_rowid IS NULL
      THEN
        SELECT code
        ,      description
        ,      severity
        ,      logging
        ,      suppress_warning
        ,      suppress_always
        ,      constraint_name
        ,      created_by
        ,      created_on
        ,      updated_by
        ,      updated_on
        INTO   cg$tmp_rec.code
        ,      cg$tmp_rec.description
        ,      cg$tmp_rec.severity
        ,      cg$tmp_rec.logging
        ,      cg$tmp_rec.suppress_warning
        ,      cg$tmp_rec.suppress_always
        ,      cg$tmp_rec.constraint_name
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        FROM   qms_message_properties
        WHERE  code = cg$old_rec.code
        FOR UPDATE NOWAIT;
      ELSE
        SELECT code
        ,      description
        ,      severity
        ,      logging
        ,      suppress_warning
        ,      suppress_always
        ,      constraint_name
        ,      created_by
        ,      created_on
        ,      updated_by
        ,      updated_on
        INTO   cg$tmp_rec.code
        ,      cg$tmp_rec.description
        ,      cg$tmp_rec.severity
        ,      cg$tmp_rec.logging
        ,      cg$tmp_rec.suppress_warning
        ,      cg$tmp_rec.suppress_always
        ,      cg$tmp_rec.constraint_name
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        FROM   qms_message_properties
        WHERE  rowid = cg$old_rec.the_rowid
        FOR UPDATE NOWAIT;
      END IF;
    ELSE
      IF cg$old_rec.the_rowid IS NULL
      THEN
        SELECT code
        ,      description
        ,      severity
        ,      logging
        ,      suppress_warning
        ,      suppress_always
        ,      constraint_name
        ,      created_by
        ,      created_on
        ,      updated_by
        ,      updated_on
        INTO   cg$tmp_rec.code
        ,      cg$tmp_rec.description
        ,      cg$tmp_rec.severity
        ,      cg$tmp_rec.logging
        ,      cg$tmp_rec.suppress_warning
        ,      cg$tmp_rec.suppress_always
        ,      cg$tmp_rec.constraint_name
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        FROM   qms_message_properties
        WHERE  code = cg$old_rec.code
        FOR UPDATE;
      ELSE
        SELECT code
        ,      description
        ,      severity
        ,      logging
        ,      suppress_warning
        ,      suppress_always
        ,      constraint_name
        ,      created_by
        ,      created_on
        ,      updated_by
        ,      updated_on
        INTO   cg$tmp_rec.code
        ,      cg$tmp_rec.description
        ,      cg$tmp_rec.severity
        ,      cg$tmp_rec.logging
        ,      cg$tmp_rec.suppress_warning
        ,      cg$tmp_rec.suppress_always
        ,      cg$tmp_rec.constraint_name
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        FROM   qms_message_properties
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
    cg$errors.push(cg$errors.string(cg$errors.API_ROW_LCK, cg$errors.ROW_LCK), 'E', 'ORA', SQLCODE, 'qms$message_properties.lck.resource_busy');
    cg$errors.raise_failure;
  WHEN NO_DATA_FOUND
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_ROW_DEL, cg$errors.ROW_DEL), 'E', 'ORA', SQLCODE, 'qms$message_properties.lck.NO_DATA_FOUND');
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'qms$message_properties.lck.others');
    cg$errors.raise_failure;
  END;

  -- Optional Columns
  IF (cg$old_ind.constraint_name)
  THEN
    IF  (cg$tmp_rec.constraint_name IS NOT NULL
    AND cg$old_rec.constraint_name IS NOT NULL)
    THEN
      IF (cg$tmp_rec.constraint_name != cg$old_rec.constraint_name)
      THEN
        cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P40CONSTRAINT_NAME),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_properties.lck');
         any_modified := TRUE;
      END IF;
    ELSIF (cg$tmp_rec.constraint_name IS NOT NULL
    OR     cg$old_rec.constraint_name IS NOT NULL)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P40CONSTRAINT_NAME),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_properties.lck');
       any_modified := TRUE;
    END IF;
  END IF;

  -- Mandatory Columns
  IF (cg$old_ind.code)
  THEN
    IF (cg$tmp_rec.code != cg$old_rec.code)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P10CODE),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_properties.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.description)
  THEN
    IF (cg$tmp_rec.description != cg$old_rec.description)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P15DESCRIPTION),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_properties.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.severity)
  THEN
    IF (cg$tmp_rec.severity != cg$old_rec.severity)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P20SEVERITY),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_properties.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.logging)
  THEN
    IF (cg$tmp_rec.logging != cg$old_rec.logging)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P25LOGGING),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_properties.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.suppress_warning)
  THEN
    IF (cg$tmp_rec.suppress_warning != cg$old_rec.suppress_warning)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P30SUPPRESS_WARNING),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_properties.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.suppress_always)
  THEN
    IF (cg$tmp_rec.suppress_always != cg$old_rec.suppress_always)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P35SUPPRESS_ALWAYS),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_properties.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.created_by)
  THEN
    IF (cg$tmp_rec.created_by != cg$old_rec.created_by)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P30CREATED_BY),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_properties.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.created_on)
  THEN
    IF (cg$tmp_rec.created_on != cg$old_rec.created_on)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P35CREATED_ON),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_properties.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.updated_by)
  THEN
    IF (cg$tmp_rec.updated_by != cg$old_rec.updated_by)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P40UPDATED_BY),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_properties.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.updated_on)
  THEN
    IF (cg$tmp_rec.updated_on != cg$old_rec.updated_on)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P600UPDATED_ON),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_properties.lck');
      any_modified := TRUE;
    END IF;
  END IF;

  IF (any_modified)
  THEN
    cg$errors.raise_failure;
  END IF;

  --  Application_logic Post-Lock <<Start>>
  --  Application_logic Post-Lock << End >>
END lck;

--------------------------------------------------------------------------------
-- Name:        cascade_update
--
-- Description: Updates all child tables affected by a change to qms_message_properties
--
-- Parameters:  cg$rec     Record of qms_message_properties current values
--              cg$old_rec Record of qms_message_properties previous values
--------------------------------------------------------------------------------
PROCEDURE cascade_update(cg$new_rec IN OUT cg$row_type, cg$old_rec IN cg$row_type)
IS
BEGIN
  NULL;
END cascade_update;

--------------------------------------------------------------------------------
-- Name:        cascade_delete
--
-- Description: Delete all child tables affected by a delete to qms_message_properties
--
-- Parameters:  cg$rec     Record of qms_message_properties current values
--------------------------------------------------------------------------------
PROCEDURE cascade_delete(cg$old_rec IN OUT cg$row_type)
IS
BEGIN
  NULL;
END cascade_delete;

END qms$message_properties;
/
SHOW ERROR