-- /Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityService/governanceBackend/src/main/resources/install/adm/api/igt_usr.pkb
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Body for Table 'igt_users'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
-- Name:        cg$igt_users
-- Description: igt_users table API package definitions
--------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY cg$igt_users
IS

--  Constant default values
C10ACTIVE   CONSTANT igt_users.active%TYPE   := 1;
C15LANGUAGE CONSTANT igt_users.language%TYPE := 'en';

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
  IF (con_name = 'IGT_USR_PK')
  THEN
    cg$errors.push( NVL(IGT_USR_PK, cg$errors.string(cg$errors.API_PK_CON_VIOLATED, cg$errors.APIMSG_PK_VIOLAT, 'IGT_USR_PK', 'igt_users'))
                  , 'E'
                  , 'API'
                  , cg$errors.API_PK_CON_VIOLATED
                  , loc
                  );
  ELSIF (con_name = 'IGT_USR_UK1')
  THEN
    cg$errors.push( NVL(IGT_USR_UK1, cg$errors.string(cg$errors.API_UQ_CON_VIOLATED, cg$errors.APIMSG_UK_VIOLAT, 'IGT_USR_UK1', 'igt_users'))
                  , 'E'
                  , 'API'
                  , cg$errors.API_UQ_CON_VIOLATED
                  , loc
                  );
  ELSIF (con_name = 'IGT_USR_UK2')
  THEN
    cg$errors.push( NVL(IGT_USR_UK2, cg$errors.string(cg$errors.API_UQ_CON_VIOLATED, cg$errors.APIMSG_UK_VIOLAT, 'IGT_USR_UK2', 'igt_users'))
                  , 'E'
                  , 'API'
                  , cg$errors.API_UQ_CON_VIOLATED
                  , loc
                  );
  ELSIF (con_name = 'IGT_USR_CK1' AND IGT_USR_CK1 IS NOT NULL)
  THEN
    cg$errors.push('IGT-00302', 'E', 'API', cg$errors.API_CK_CON_VIOLATED, loc);
  ELSIF (con_name = 'IGT_USR_CK2' AND IGT_USR_CK2 IS NOT NULL)
  THEN
    cg$errors.push('IGT-00303', 'E', 'API', cg$errors.API_CK_CON_VIOLATED, loc);
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
                , 'cg$igt_users.raise_uk_not_updateable'
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
                , 'cg$igt_users.raise_fk_not_transferable'
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
    BEGIN
      IF (cg$ind.id = FALSE OR cg$rec.id IS NULL)
      THEN
        SELECT igs_adm_seq.NEXTVAL
        INTO   cg$rec.id
        FROM   DUAL;
        cg$ind.id := TRUE;
      END IF;
    EXCEPTION
      WHEN OTHERS
      THEN
        cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$igt_users.up_autogen.ID.others');
        cg$errors.raise_failure;
    END;
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
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$igt_users.up_autogen_columns.denorm');
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
  IF (cg$val_rec.username IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P45USERNAME), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.credential IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P50CREDENTIAL), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.lastname IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P55LASTNAME), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.language IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P65LANGUAGE), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
  END IF;
  IF (cg$val_rec.email IS NULL)
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P70EMAIL), 'E', 'API', cg$errors.API_MAND_COLUMN_ISNULL, loc);
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
  IF NOT (cg$ind.active)
  THEN
    cg$rec.active := C10ACTIVE;
  END IF;
  IF NOT (cg$ind.language)
  THEN
    cg$rec.language := C15LANGUAGE;
  END IF;

  -- Auto-generated and uppercased columns
  up_autogen_columns(cg$rec, cg$ind, 'INS', do_ins);

  called_from_package := TRUE;
  IF (do_ins)
  THEN
    validate_foreign_keys_ins(cg$rec);
    INSERT INTO igt_users (
      id
    , rowversion
    , created_by
    , created_on
    , updated_by
    , updated_on
    , active
    , username
    , credential
    , lastname
    , firstname
    , language
    , email
    , phone
    , mobile
    ) VALUES (
      cg$rec.id
    , cg$rec.rowversion
    , cg$rec.created_by
    , cg$rec.created_on
    , cg$rec.updated_by
    , cg$rec.updated_on
    , cg$rec.active
    , cg$rec.username
    , cg$rec.credential
    , cg$rec.lastname
    , cg$rec.firstname
    , cg$rec.language
    , cg$rec.email
    , cg$rec.phone
    , cg$rec.mobile
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
    validate_mandatory(cg$rec, 'cg$igt_users.ins.mandatory_missing');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.check_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_CHECK_CON, 'cg$igt_users.ins.check_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.fk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_FOREIGN_KEY, 'cg$igt_users.ins.fk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.uk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_UNIQUE_KEY, 'cg$igt_users.ins.uk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$igt_users.ins.others');
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
    -- Report error if attempt to update non updateable Primary Key IGT_USR_PK
    IF (cg$ind.id AND cg$rec.id != cg$upd_rec.id)
    THEN
      raise_uk_not_updateable('IGT_USR_PK');
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
    IF NOT (cg$ind.username)
    THEN
      cg$rec.username := cg$upd_rec.username;
    END IF;
    IF NOT (cg$ind.credential)
    THEN
      cg$rec.credential := cg$upd_rec.credential;
    END IF;
    IF NOT (cg$ind.lastname)
    THEN
      cg$rec.lastname := cg$upd_rec.lastname;
    END IF;
    IF NOT (cg$ind.firstname)
    THEN
      cg$rec.firstname := cg$upd_rec.firstname;
    END IF;
    IF NOT (cg$ind.language)
    THEN
      cg$rec.language := cg$upd_rec.language;
    END IF;
    IF NOT (cg$ind.email)
    THEN
      cg$rec.email := cg$upd_rec.email;
    END IF;
    IF NOT (cg$ind.phone)
    THEN
      cg$rec.phone := cg$upd_rec.phone;
    END IF;
    IF NOT (cg$ind.mobile)
    THEN
      cg$rec.mobile := cg$upd_rec.mobile;
    END IF;
  ELSE
    -- Report error if attempt to update non updateable Primary Key IGT_USR_PK
    IF (cg$ind.id)
    THEN
      raise_uk_not_updateable('IGT_USR_PK');
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
        UPDATE igt_users
        SET    rowversion = cg$rec.rowversion
        ,      created_by = cg$rec.created_by
        ,      created_on = cg$rec.created_on
        ,      updated_by = cg$rec.updated_by
        ,      updated_on = cg$rec.updated_on
        ,      active     = cg$rec.active
        ,      username   = cg$rec.username
        ,      credential = cg$rec.credential
        ,      lastname   = cg$rec.lastname
        ,      firstname  = cg$rec.firstname
        ,      language   = cg$rec.language
        ,      email      = cg$rec.email
        ,      phone      = cg$rec.phone
        ,      mobile     = cg$rec.mobile
        WHERE  id         = cg$rec.id
        ;
        NULL;
      ELSE
        UPDATE igt_users
        SET    rowversion = cg$rec.rowversion
        ,      created_by = cg$rec.created_by
        ,      created_on = cg$rec.created_on
        ,      updated_by = cg$rec.updated_by
        ,      updated_on = cg$rec.updated_on
        ,      active     = cg$rec.active
        ,      username   = cg$rec.username
        ,      credential = cg$rec.credential
        ,      lastname   = cg$rec.lastname
        ,      firstname  = cg$rec.firstname
        ,      language   = cg$rec.language
        ,      email      = cg$rec.email
        ,      phone      = cg$rec.phone
        ,      mobile     = cg$rec.mobile
        WHERE  rowid      = cg$rec.the_rowid
        ;
        NULL;
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
    cg$table(idx).username      := cg$rec.username;
    cg$tableind(idx).username   := cg$ind.username;
    cg$table(idx).credential    := cg$rec.credential;
    cg$tableind(idx).credential := cg$ind.credential;
    cg$table(idx).lastname      := cg$rec.lastname;
    cg$tableind(idx).lastname   := cg$ind.lastname;
    cg$table(idx).firstname     := cg$rec.firstname;
    cg$tableind(idx).firstname  := cg$ind.firstname;
    cg$table(idx).language      := cg$rec.language;
    cg$tableind(idx).language   := cg$ind.language;
    cg$table(idx).email         := cg$rec.email;
    cg$tableind(idx).email      := cg$ind.email;
    cg$table(idx).phone         := cg$rec.phone;
    cg$tableind(idx).phone      := cg$ind.phone;
    cg$table(idx).mobile        := cg$rec.mobile;
    cg$tableind(idx).mobile     := cg$ind.mobile;

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
    validate_mandatory(cg$rec, 'cg$igt_users.upd.upd_mandatory_null');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.check_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_CHECK_CON, 'cg$igt_users.upd.check_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.fk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_FOREIGN_KEY, 'cg$igt_users.upd.fk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.uk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_UNIQUE_KEY, 'cg$igt_users.upd.uk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$igt_users.upd.others');
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
        DELETE igt_users
        WHERE  id = cg$pk.id;
      ELSE
        DELETE igt_users
        WHERE  rowid = cg$pk.the_rowid;
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
    err_msg(SQLERRM, cg$errors.ERR_DELETE_RESTRICT, 'cg$igt_users.del.delete_restrict');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN NO_DATA_FOUND
  THEN
    cg$errors.push(cg$errors.string(cg$errors.API_ROW_DEL, cg$errors.ROW_DEL), 'E', 'ORA', SQLCODE, 'cg$igt_users.del.NO_DATA_FOUND');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$igt_users.del.others');
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
    ,      username
    ,      credential
    ,      lastname
    ,      firstname
    ,      language
    ,      email
    ,      phone
    ,      mobile
    ,      rowid
    INTO   cg$sel_rec.id
    ,      cg$sel_rec.rowversion
    ,      cg$sel_rec.created_by
    ,      cg$sel_rec.created_on
    ,      cg$sel_rec.updated_by
    ,      cg$sel_rec.updated_on
    ,      cg$sel_rec.active
    ,      cg$sel_rec.username
    ,      cg$sel_rec.credential
    ,      cg$sel_rec.lastname
    ,      cg$sel_rec.firstname
    ,      cg$sel_rec.language
    ,      cg$sel_rec.email
    ,      cg$sel_rec.phone
    ,      cg$sel_rec.mobile
    ,      cg$sel_rec.the_rowid
    FROM   igt_users
    WHERE  id = cg$sel_rec.id;
  ELSE
    SELECT id
    ,      rowversion
    ,      created_by
    ,      created_on
    ,      updated_by
    ,      updated_on
    ,      active
    ,      username
    ,      credential
    ,      lastname
    ,      firstname
    ,      language
    ,      email
    ,      phone
    ,      mobile
    ,      rowid
    INTO   cg$sel_rec.id
    ,      cg$sel_rec.rowversion
    ,      cg$sel_rec.created_by
    ,      cg$sel_rec.created_on
    ,      cg$sel_rec.updated_by
    ,      cg$sel_rec.updated_on
    ,      cg$sel_rec.active
    ,      cg$sel_rec.username
    ,      cg$sel_rec.credential
    ,      cg$sel_rec.lastname
    ,      cg$sel_rec.firstname
    ,      cg$sel_rec.language
    ,      cg$sel_rec.email
    ,      cg$sel_rec.phone
    ,      cg$sel_rec.mobile
    ,      cg$sel_rec.the_rowid
    FROM   igt_users
    WHERE  rowid = cg$sel_rec.the_rowid;
  END IF;
EXCEPTION
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$igt_users.sel.others');
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
        ,      username
        ,      credential
        ,      lastname
        ,      firstname
        ,      language
        ,      email
        ,      phone
        ,      mobile
        INTO   cg$tmp_rec.id
        ,      cg$tmp_rec.rowversion
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        ,      cg$tmp_rec.active
        ,      cg$tmp_rec.username
        ,      cg$tmp_rec.credential
        ,      cg$tmp_rec.lastname
        ,      cg$tmp_rec.firstname
        ,      cg$tmp_rec.language
        ,      cg$tmp_rec.email
        ,      cg$tmp_rec.phone
        ,      cg$tmp_rec.mobile
        FROM   igt_users
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
        ,      username
        ,      credential
        ,      lastname
        ,      firstname
        ,      language
        ,      email
        ,      phone
        ,      mobile
        INTO   cg$tmp_rec.id
        ,      cg$tmp_rec.rowversion
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        ,      cg$tmp_rec.active
        ,      cg$tmp_rec.username
        ,      cg$tmp_rec.credential
        ,      cg$tmp_rec.lastname
        ,      cg$tmp_rec.firstname
        ,      cg$tmp_rec.language
        ,      cg$tmp_rec.email
        ,      cg$tmp_rec.phone
        ,      cg$tmp_rec.mobile
        FROM   igt_users
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
        ,      username
        ,      credential
        ,      lastname
        ,      firstname
        ,      language
        ,      email
        ,      phone
        ,      mobile
        INTO   cg$tmp_rec.id
        ,      cg$tmp_rec.rowversion
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        ,      cg$tmp_rec.active
        ,      cg$tmp_rec.username
        ,      cg$tmp_rec.credential
        ,      cg$tmp_rec.lastname
        ,      cg$tmp_rec.firstname
        ,      cg$tmp_rec.language
        ,      cg$tmp_rec.email
        ,      cg$tmp_rec.phone
        ,      cg$tmp_rec.mobile
        FROM   igt_users
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
        ,      username
        ,      credential
        ,      lastname
        ,      firstname
        ,      language
        ,      email
        ,      phone
        ,      mobile
        INTO   cg$tmp_rec.id
        ,      cg$tmp_rec.rowversion
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        ,      cg$tmp_rec.active
        ,      cg$tmp_rec.username
        ,      cg$tmp_rec.credential
        ,      cg$tmp_rec.lastname
        ,      cg$tmp_rec.firstname
        ,      cg$tmp_rec.language
        ,      cg$tmp_rec.email
        ,      cg$tmp_rec.phone
        ,      cg$tmp_rec.mobile
        FROM   igt_users
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
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_LCK, cg$errors.ROW_LCK), 'E', 'ORA', SQLCODE, 'cg$igt_users.lck.resource_busy');
      cg$errors.raise_failure;
    WHEN NO_DATA_FOUND
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_DEL, cg$errors.ROW_DEL), 'E', 'ORA', SQLCODE, 'cg$igt_users.lck.NO_DATA_FOUND');
      cg$errors.raise_failure;
    WHEN OTHERS
    THEN
      cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'cg$igt_users.lck.others');
      cg$errors.raise_failure;
    END;

  -- Optional Columns
  IF (cg$old_ind.firstname)
  THEN
    IF  (cg$tmp_rec.firstname IS NOT NULL
    AND  cg$old_rec.firstname IS NOT NULL)
    THEN
      IF (cg$tmp_rec.firstname != cg$old_rec.firstname)
      THEN
        cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P60FIRSTNAME), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$igt_users.lck');
        any_modified := TRUE;
      END IF;
    ELSIF (cg$tmp_rec.firstname IS NOT NULL
    OR     cg$old_rec.firstname IS NOT NULL)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P60FIRSTNAME), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$igt_users.lck');
      any_modified := TRUE;
    END IF;
  END IF;

  -- Mandatory Columns
  IF (cg$old_ind.id)
  THEN
    IF (cg$tmp_rec.id != cg$old_rec.id)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P10ID), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$igt_users.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.rowversion)
  THEN
    IF (cg$tmp_rec.rowversion != cg$old_rec.rowversion)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P15ROWVERSION), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$igt_users.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.created_by)
  THEN
    IF (cg$tmp_rec.created_by != cg$old_rec.created_by)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P20CREATED_BY), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$igt_users.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.created_on)
  THEN
    IF (cg$tmp_rec.created_on != cg$old_rec.created_on)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P25CREATED_ON), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$igt_users.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.updated_by)
  THEN
    IF (cg$tmp_rec.updated_by != cg$old_rec.updated_by)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P30UPDATED_BY), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$igt_users.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.updated_on)
  THEN
    IF (cg$tmp_rec.updated_on != cg$old_rec.updated_on)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P35UPDATED_ON), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$igt_users.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.active)
  THEN
    IF (cg$tmp_rec.active != cg$old_rec.active)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P40ACTIVE), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$igt_users.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.username)
  THEN
    IF (cg$tmp_rec.username != cg$old_rec.username)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P45USERNAME), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$igt_users.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.credential)
  THEN
    IF (cg$tmp_rec.credential != cg$old_rec.credential)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P50CREDENTIAL), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$igt_users.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.lastname)
  THEN
    IF (cg$tmp_rec.lastname != cg$old_rec.lastname)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P55LASTNAME), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$igt_users.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.language)
  THEN
    IF (cg$tmp_rec.language != cg$old_rec.language)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P65LANGUAGE), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$igt_users.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.email)
  THEN
    IF (cg$tmp_rec.email != cg$old_rec.email)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P70EMAIL), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$igt_users.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.phone)
  THEN
    IF (cg$tmp_rec.phone != cg$old_rec.phone)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P75PHONE), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$igt_users.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.mobile)
  THEN
    IF (cg$tmp_rec.mobile != cg$old_rec.mobile)
    THEN
      cg$errors.push(cg$errors.string(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P80MOBILE), 'E', 'API', CG$ERRORS.API_MODIFIED, 'cg$igt_users.lck');
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
--
-- Description: Updates all child tables affected by a change to igt_users
--
-- Parameters:  cg$rec     Record of igt_users current values
--              cg$old_rec Record of igt_users previous values
--------------------------------------------------------------------------------
PROCEDURE cascade_update(cg$new_rec IN OUT cg$row_type, cg$old_rec IN cg$row_type)
IS
BEGIN
  NULL;
END cascade_update;

--------------------------------------------------------------------------------
-- Name:        cascade_delete
--
-- Description: Delete all child tables affected by a delete to igt_users
--
-- Parameters:  cg$rec     Record of igt_users current values
--------------------------------------------------------------------------------
PROCEDURE cascade_delete(cg$old_rec IN OUT cg$row_type)
IS
BEGIN
  NULL;
END cascade_delete;

END cg$igt_users;
/
SHOW ERROR