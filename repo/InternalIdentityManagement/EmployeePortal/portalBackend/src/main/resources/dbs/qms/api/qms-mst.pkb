-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppAplication\src\main\dbs\qms\api\qms-mst.pkb
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

PROMPT Creating API Package Body for Table 'qms$message_text'
--------------------------------------------------------------------------------
-- Name:        qms$message_text
-- Description: qms$message_text table API package definitions
--------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY qms$message_text
IS

PROCEDURE   validate_mandatory(cg$val_rec IN cg$row_type, loc IN VARCHAR2 DEFAULT '');
PROCEDURE   up_autogen_columns(cg$rec IN OUT cg$row_type, cg$ind IN OUT cg$ind_type, operation IN VARCHAR2 DEFAULT 'INS', do_denorm IN BOOLEAN DEFAULT TRUE);
PROCEDURE   err_msg(msg  IN VARCHAR2, type IN INTEGER, loc  IN VARCHAR2 DEFAULT '');
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
  cg$errors.push( cg$errors.get_text(cg$errors.API_UNIQUE_KEY_UPDATE, cg$errors.ERR_UK_UPDATE, uk)
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
PROCEDURE fk_key_transferable(fk IN VARCHAR2)
IS
BEGIN
  cg$errors.push( cg$errors.get_text(cg$errors.API_FOREIGN_KEY_TRANS, cg$errors.ERR_FK_TRANS, fk)
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

  cg$rec.msp_code   := upper(cg$rec.msp_code);
  cg$rec.updated_by := user;
  cg$rec.updated_on := trunc(sysdate);
EXCEPTION
  WHEN no_data_found
  THEN
    NULL;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'qms$message_text.up_autogen_columns.denorm');
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
  IF (cg$val_rec.language IS NULL)
  THEN
    cg$errors.push( cg$errors.get_text(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P10LANGUAGE)
                  , 'E'
                  , 'API'
                  , cg$errors.API_MAND_COLUMN_ISNULL
                  , loc
                  );
  END IF;
  IF (cg$val_rec.text IS NULL)
  THEN
    cg$errors.push( cg$errors.get_text(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P20TEXT)
                  , 'E'
                  , 'API'
                  , cg$errors.API_MAND_COLUMN_ISNULL
                  , loc
                  );
  END IF;
  IF (cg$val_rec.msp_code IS NULL)
  THEN
    cg$errors.push( cg$errors.get_text(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P40MSP_CODE)
                  , 'E'
                  , 'API'
                  , cg$errors.API_MAND_COLUMN_ISNULL
                  , loc
                  );
  END IF;
  IF (cg$val_rec.created_by IS NULL)
  THEN
    cg$errors.push( cg$errors.get_text(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P45CREATED_BY)
                  , 'E'
                  , 'API'
                  , cg$errors.API_MAND_COLUMN_ISNULL
                  , loc
                  );
  END IF;
  IF (cg$val_rec.created_on IS NULL)
  THEN
    cg$errors.push( cg$errors.get_text(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P50CREATED_ON)
                  , 'E'
                  , 'API'
                  , cg$errors.API_MAND_COLUMN_ISNULL
                  , loc
                  );
  END IF;
  IF (cg$val_rec.updated_by IS NULL)
  THEN
    cg$errors.push( cg$errors.get_text(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P55UPDATED_BY)
                  , 'E'
                  , 'API'
                  , cg$errors.API_MAND_COLUMN_ISNULL
                  , loc
                  );
  END IF;
  IF (cg$val_rec.updated_on IS NULL)
  THEN
    cg$errors.push( cg$errors.get_text(cg$errors.API_MAND_COLUMN_ISNULL, cg$errors.VAL_MAND, P60UPDATED_ON)
                  , 'E'
                  , 'API'
                  , cg$errors.API_MAND_COLUMN_ISNULL
                  , loc
                  );
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
BEGIN
  NULL;
END;

PROCEDURE validate_foreign_keys_upd(cg$rec IN cg$row_type, cg$old_rec IN cg$row_type, cg$ind IN cg$ind_type)
IS
BEGIN
  NULL;
END;

PROCEDURE validate_foreign_keys_del(cg$rec IN cg$row_type)
IS
BEGIN
  NULL;
END;

--------------------------------------------------------------------------------
-- Name:        cascade_update
--
-- Description: Updates all child tables affected by a change to qms_message_text
--
-- Parameters:  cg$rec     Record of qms_message_text current values
--              cg$old_rec Record of qms_message_text previous values
--------------------------------------------------------------------------------
PROCEDURE cascade_update(cg$new_rec IN OUT cg$row_type, cg$old_rec IN cg$row_type)
IS
BEGIN
  NULL;
END cascade_update;

--------------------------------------------------------------------------------
-- Name:        cascade_delete
--
-- Description: Delete all child tables affected by a delete to qms_message_text
--
-- Parameters:  cg$rec     Record of qms_message_text current values
--------------------------------------------------------------------------------
PROCEDURE cascade_delete(cg$old_rec IN OUT cg$row_type)
IS
BEGIN
  NULL;
END cascade_delete;

--------------------------------------------------------------------------------
-- Name:        domain_cascade_delete
--
-- Description: Delete all child tables affected by a delete to qms_message_text
--
-- Parameters:  cg$rec     Record of qms_message_text current values
--------------------------------------------------------------------------------
PROCEDURE domain_cascade_delete(cg$old_rec IN OUT cg$row_type)
IS
BEGIN
  NULL;
END domain_cascade_delete;

--------------------------------------------------------------------------------
-- Name:        validate_arc
--
-- Description: Checks for adherence to arc relationship
--
-- Parameters:  cg$rec     Record of qms_message_text current values
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
-- Parameters:  cg$rec     Record of qms_message_text current values
--------------------------------------------------------------------------------
PROCEDURE validate_domain(cg$rec IN OUT cg$row_type)
IS
BEGIN
  NULL;
END validate_domain;

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
  IF (con_name = 'QMS_MST_PK')
  THEN
    cg$errors.push( NVL(QMS_MST_PK, cg$errors.get_text(cg$errors.API_PK_CON_VIOLATED, cg$errors.APIMSG_PK_VIOLAT, 'QMS_MST_PK', 'qms_message_text'))
                  , 'E'
                  , 'API'
                  , cg$errors.API_PK_CON_VIOLATED
                  , loc
                  );
  ELSIF (con_name = 'QMS_MST_MSP_FK1')
  THEN
    cg$errors.push( NVL(QMS_MST_MSP_FK1, cg$errors.get_text(cg$errors.API_FK_CON_VIOLATED, cg$errors.APIMSG_FK_VIOLAT, 'QMS_MST_MSP_FK1', 'qms_message_text'))
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
  --  Constant default values
  D10_LANGUAGE CONSTANT qms_message_text.language%TYPE := 'ENG';

  cg$tmp_rec cg$row_type;
BEGIN
  --  Application_logic Pre-Insert <<Start>>
  --  Application_logic Pre-Insert << End >>

  --  Defaulted

  IF NOT (cg$ind.language)
  THEN
    cg$rec.language := D10_LANGUAGE;
  END IF;
  --  Auto-generated and uppercased columns
  up_autogen_columns(cg$rec, cg$ind, 'INS', do_ins);

  called_from_package := TRUE;
  IF (do_ins)
  THEN
    validate_foreign_keys_ins(cg$rec);
    validate_arc(cg$rec);
    validate_domain(cg$rec);

    INSERT INTO qms_message_text(
      language
    , text
    , help_text
    , msp_code
    , created_by
    , created_on
    , updated_by
    , updated_on
    ) VALUES (
      cg$rec.language
    , cg$rec.text
    , cg$rec.help_text
    , cg$rec.msp_code
    , cg$rec.created_by
    , cg$rec.created_on
    , cg$rec.updated_by
    , cg$rec.updated_on
    );
    doLobs(cg$rec, cg$ind);
    sel(cg$rec);

    upd_oper_denorm2(cg$rec, cg$tmp_rec, cg$ind, 'INS');
  END IF;
  called_from_package := FALSE;

  IF NOT (do_ins)
  THEN
    cg$table(idx).language    := cg$rec.language;
    cg$tableind(idx).language := cg$ind.language;

    cg$table(idx).text    := cg$rec.text;
    cg$tableind(idx).text := cg$ind.text;

    cg$table(idx).help_text    := cg$rec.help_text;
    cg$tableind(idx).help_text := cg$ind.help_text;

    cg$table(idx).msp_code    := cg$rec.msp_code;
    cg$tableind(idx).msp_code := cg$ind.msp_code;

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
    validate_mandatory(cg$rec, 'qms$message_text.ins.mandatory_missing');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.check_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_CHECK_CON, 'qms$message_text.ins.check_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.fk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_FOREIGN_KEY, 'qms$message_text.ins.fk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.uk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_UNIQUE_KEY, 'qms$message_text.ins.uk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'qms$message_text.ins.others');
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
PROCEDURE upd(cg$rec IN OUT cg$row_type, cg$ind IN OUT cg$ind_type, do_upd IN BOOLEAN DEFAULT TRUE)
IS
  cg$upd_rec cg$row_type;
  cg$old_rec cg$row_type;
  RECORD_LOGGED BOOLEAN := FALSE;
BEGIN
  --  Application_logic Pre-Update <<Start>>
  --  Application_logic Pre-Update << End >>

  cg$upd_rec.msp_code := cg$rec.msp_code;
  cg$old_rec.msp_code := cg$rec.msp_code;
  cg$upd_rec.language := cg$rec.language;
  cg$old_rec.language := cg$rec.language;

  cg$upd_rec.the_rowid := cg$rec.the_rowid;
  cg$old_rec.the_rowid := cg$rec.the_rowid;

  IF (do_upd)
  THEN
    sel(cg$upd_rec);

    IF NOT (cg$ind.language)
    THEN
      cg$rec.language := cg$upd_rec.language;
    END IF;
    IF NOT (cg$ind.text)
    THEN
      cg$rec.text := cg$upd_rec.text;
    END IF;
    IF NOT (cg$ind.help_text)
    THEN
      cg$rec.help_text := cg$upd_rec.help_text;
    END IF;
    IF NOT (cg$ind.msp_code)
    THEN
      cg$rec.msp_code := cg$upd_rec.msp_code;
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

      validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
      validate_arc(cg$rec);
      validate_domain(cg$rec);

      sel(cg$old_rec);

      IF cg$rec.the_rowid IS NULL
      THEN
        UPDATE qms_message_text
        SET   text       = cg$rec.text
        ,     help_text  = cg$rec.help_text
        ,     created_by = cg$rec.created_by
        ,     created_on = cg$rec.created_on
        ,     updated_by = cg$rec.updated_by
        ,     updated_on = cg$rec.updated_on
        WHERE msp_code   = cg$rec.msp_code
        AND   language   = cg$rec.language;
      ELSE
        UPDATE qms_message_text
        SET   text       = cg$rec.text
        ,     help_text  = cg$rec.help_text
        ,     created_by = cg$rec.created_by
        ,     created_on = cg$rec.created_on
        ,     updated_by = cg$rec.updated_by
        ,     updated_on = cg$rec.updated_on
        WHERE rowid      = cg$rec.the_rowid;
      END IF;

      sel(cg$rec);

      upd_denorm2(cg$rec, cg$ind);
      upd_oper_denorm2(cg$rec, cg$old_rec, cg$ind, 'UPD');
      cascade_update(cg$rec, cg$old_rec);
      called_from_package := old_called;
    END;
  END IF;

  IF NOT (do_upd)
  THEN
    cg$table(idx).language    := cg$rec.language;
    cg$tableind(idx).language := cg$ind.language;

    cg$table(idx).text    := cg$rec.text;
    cg$tableind(idx).text := cg$ind.text;

    cg$table(idx).help_text    := cg$rec.help_text;
    cg$tableind(idx).help_text := cg$ind.help_text;

    cg$table(idx).msp_code    := cg$rec.msp_code;
    cg$tableind(idx).msp_code := cg$ind.msp_code;

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
    validate_mandatory(cg$rec, 'qms$message_text.upd.upd_mandatory_null');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.check_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_CHECK_CON, 'qms$message_text.upd.check_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.fk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_FOREIGN_KEY, 'qms$message_text.upd.fk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN cg$errors.uk_violation
  THEN
    err_msg(SQLERRM, cg$errors.ERR_UNIQUE_KEY, 'qms$message_text.upd.uk_violation');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'qms$message_text.upd.others');
    called_from_package := FALSE;
    cg$errors.raise_failure;
END upd;

--------------------------------------------------------------------------------
-- Name:        upd_denorm
--
-- Description: API procedure for simple denormalization
--
-- Parameters:  cg$rec  Record of row to be updated
--              cg$ind  Record of columns specifically set
--              do_upd  Whether we want the actual UPDATE to occur
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
      cg$rec.msp_code := cg$pk.msp_code;
      cg$rec.language := cg$pk.language;
      sel(cg$rec);

      validate_foreign_keys_del(cg$rec);
      domain_cascade_delete(cg$rec);

      IF cg$pk.the_rowid IS NULL
      THEN
        DELETE qms_message_text
        WHERE  msp_code = cg$pk.msp_code
        AND    language = cg$pk.language;
      ELSE
        DELETE qms_message_text
        WHERE  rowid = cg$pk.the_rowid;
      END IF;

      upd_oper_denorm2(cg$rec, cg$old_rec, cg$ind, 'DEL');
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
    err_msg(SQLERRM, cg$errors.ERR_DELETE_RESTRICT, 'qms$message_text.del.delete_restrict');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN no_data_found
  THEN
    cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_DEL, cg$errors.ROW_DEL), 'E', 'ORA', SQLCODE, 'qms$message_text.del.no_data_found');
    called_from_package := FALSE;
    cg$errors.raise_failure;
  WHEN OTHERS
  THEN
    cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'qms$message_text.del.others');
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
    SELECT language
    ,      text
    ,      help_text
    ,      msp_code
    ,      created_by
    ,      created_on
    ,      updated_by
    ,      updated_on
    ,      rowid
    INTO   cg$sel_rec.language
    ,      cg$sel_rec.text
    ,      cg$sel_rec.help_text
    ,      cg$sel_rec.msp_code
    ,      cg$sel_rec.created_by
    ,      cg$sel_rec.created_on
    ,      cg$sel_rec.updated_by
    ,      cg$sel_rec.updated_on
    ,      cg$sel_rec.the_rowid
    FROM   qms_message_text
    WHERE  msp_code = cg$sel_rec.msp_code
    AND    language = cg$sel_rec.language;
  ELSE
    SELECT language
    ,      text
    ,      help_text
    ,      msp_code
    ,      created_by
    ,      created_on
    ,      updated_by
    ,      updated_on
    ,      rowid
    INTO   cg$sel_rec.language
    ,      cg$sel_rec.text
    ,      cg$sel_rec.help_text
    ,      cg$sel_rec.msp_code
    ,      cg$sel_rec.created_by
    ,      cg$sel_rec.created_on
    ,      cg$sel_rec.updated_by
    ,      cg$sel_rec.updated_on
    ,      cg$sel_rec.the_rowid
    FROM   qms_message_text
    WHERE  rowid = cg$sel_rec.the_rowid;
  END IF;
EXCEPTION WHEN OTHERS
THEN
  cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'qms$message_text.slct.others');
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
        SELECT language
        ,      text
        ,      help_text
        ,      msp_code
        ,      created_by
        ,      created_on
        ,      updated_by
        ,      updated_on
        INTO   cg$tmp_rec.language
        ,      cg$tmp_rec.text
        ,      cg$tmp_rec.help_text
        ,      cg$tmp_rec.msp_code
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        FROM   qms_message_text
        WHERE  msp_code = cg$old_rec.msp_code
        AND    language = cg$old_rec.language
        FOR UPDATE NOWAIT;
      ELSE
        SELECT language
        ,      text
        ,      help_text
        ,      msp_code
        ,      created_by
        ,      created_on
        ,      updated_by
        ,      updated_on
        INTO   cg$tmp_rec.language
        ,      cg$tmp_rec.text
        ,      cg$tmp_rec.help_text
        ,      cg$tmp_rec.msp_code
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        FROM   qms_message_text
        WHERE  rowid = cg$old_rec.the_rowid
        FOR UPDATE NOWAIT;
      END IF;
    ELSE
       IF cg$old_rec.the_rowid IS NULL
       THEN
        SELECT language
        ,      text
        ,      help_text
        ,      msp_code
        ,      created_by
        ,      created_on
        ,      updated_by
        ,      updated_on
        INTO   cg$tmp_rec.language
        ,      cg$tmp_rec.text
        ,      cg$tmp_rec.help_text
        ,      cg$tmp_rec.msp_code
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        FROM   qms_message_text
        WHERE  msp_code = cg$old_rec.msp_code
        AND    language = cg$old_rec.language
        FOR UPDATE;
      ELSE
        SELECT language
        ,      text
        ,      help_text
        ,      msp_code
        ,      created_by
        ,      created_on
        ,      updated_by
        ,      updated_on
        INTO   cg$tmp_rec.language
        ,      cg$tmp_rec.text
        ,      cg$tmp_rec.help_text
        ,      cg$tmp_rec.msp_code
        ,      cg$tmp_rec.created_by
        ,      cg$tmp_rec.created_on
        ,      cg$tmp_rec.updated_by
        ,      cg$tmp_rec.updated_on
        FROM   qms_message_text
        WHERE rowid = cg$old_rec.the_rowid
        FOR UPDATE;
      END IF;
    END IF;
  EXCEPTION
    WHEN cg$errors.cg$error
    THEN
      cg$errors.raise_failure;
    WHEN cg$errors.resource_busy
    THEN
      cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_LCK, cg$errors.ROW_LCK), 'E', 'ORA', SQLCODE, 'qms$message_text.lck.resource_busy');
      cg$errors.raise_failure;
    WHEN no_data_found
    THEN
      cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_DEL, cg$errors.ROW_DEL), 'E', 'ORA', SQLCODE, 'qms$message_text.lck.no_data_found');
      cg$errors.raise_failure;
    WHEN OTHERS
    THEN
      cg$errors.push(SQLERRM, 'E', 'ORA', SQLCODE, 'qms$message_text.lck.others');
      cg$errors.raise_failure;
  END;

  -- Optional Columns
  IF (cg$old_ind.help_text)
  THEN
    IF (cg$tmp_rec.help_text IS NOT NULL AND cg$old_rec.help_text IS NOT NULL)
    THEN
      IF (cg$tmp_rec.help_text != cg$old_rec.help_text)
      THEN
        cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P30HELP_TEXT),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_text.lck');
        any_modified := TRUE;
      END IF;
    ELSIF (cg$tmp_rec.help_text IS NOT NULL OR cg$old_rec.help_text IS NOT NULL)
    THEN
      cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P30HELP_TEXT),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_text.lck');
      any_modified := TRUE;
    END IF;
  END IF;

  -- Mandatory Columns
  IF (cg$old_ind.language)
  THEN
    IF (cg$tmp_rec.language != cg$old_rec.language)
    THEN
      cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P10LANGUAGE),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_text.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.text)
  THEN
    IF (cg$tmp_rec.text != cg$old_rec.text)
    THEN
      cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P20TEXT),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_text.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.msp_code)
  THEN
    IF (cg$tmp_rec.msp_code != cg$old_rec.msp_code)
    THEN
      cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P40MSP_CODE),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_text.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.created_by)
  THEN
    IF (cg$tmp_rec.created_by != cg$old_rec.created_by)
    THEN
      cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P45CREATED_BY),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_text.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.created_on)
  THEN
    IF (cg$tmp_rec.created_on != cg$old_rec.created_on)
    THEN
      cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P50CREATED_ON),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_text.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.updated_by)
  THEN
    IF (cg$tmp_rec.updated_by != cg$old_rec.updated_by)
    THEN
      cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P55UPDATED_BY),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_text.lck');
      any_modified := TRUE;
    END IF;
  END IF;
  IF (cg$old_ind.updated_on)
  THEN
    IF (cg$tmp_rec.updated_on != cg$old_rec.updated_on)
    THEN
      cg$errors.push(cg$errors.get_text(cg$errors.API_ROW_MOD, cg$errors.ROW_MOD, P60UPDATED_ON),'E', 'API', CG$ERRORS.API_MODIFIED, 'qms$message_text.lck');
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

END qms$message_text;
/
SHOW ERROR