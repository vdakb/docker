-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\qms_rlp.pks
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Specification for Table 'qms$qms_rule_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
-- Name:        qms$rule_properties
-- Description: qms_rule_properties table API package declarations
--------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE qms$rule_properties IS

  called_from_package BOOLEAN := FALSE;

  --  Repository User-Defined Error Messages
  QMS_RLP_PK    CONSTANT VARCHAR2(10) := 'QMS-01141';

  --  Column default prompts. Format PSEQNO_COL
  P10PROCESS    CONSTANT VARCHAR2(30) := 'Process';
  P15CODE       CONSTANT VARCHAR2(30) := 'Code';
  P20NAME       CONSTANT VARCHAR2(30) := 'Description';
  P25ENABLED    CONSTANT VARCHAR2(30) := 'Enabled';
  P30REMARK     CONSTANT VARCHAR2(30) := 'Remark';
  P35CREATED_BY CONSTANT VARCHAR2(30) := 'Created By';
  P40CREATED_ON CONSTANT VARCHAR2(30) := 'Created On';
  P45UPDATED_BY CONSTANT VARCHAR2(30) := 'Updated By';
  P60UPDATED_ON CONSTANT VARCHAR2(30) := 'Updated On';

  cg$row qms_rule_properties%ROWTYPE;

  --  qms_rule_properties row type variable
  TYPE cg$row_type IS RECORD (
    process    cg$row.process%TYPE
  , code       cg$row.code%TYPE
  , name       cg$row.name%TYPE
  , enabled    cg$row.enabled%TYPE
  , remark     cg$row.remark%TYPE
  , created_by cg$row.created_by%TYPE
  , created_on cg$row.created_on%TYPE
  , updated_by cg$row.updated_by%TYPE
  , updated_on cg$row.updated_on%TYPE
  , the_rowid  ROWID
  );

  --  qms_rule_properties indicator type variable
  TYPE cg$ind_type IS RECORD(
    process    BOOLEAN DEFAULT FALSE
  , code       BOOLEAN DEFAULT FALSE
  , name       BOOLEAN DEFAULT FALSE
  , enabled    BOOLEAN DEFAULT FALSE
  , remark     BOOLEAN DEFAULT FALSE
  , created_by BOOLEAN DEFAULT FALSE
  , created_on BOOLEAN DEFAULT FALSE
  , updated_by BOOLEAN DEFAULT FALSE
  , updated_on BOOLEAN DEFAULT FALSE
  );

  --  qms_rule_properties primary key type variable
  TYPE cg$pk_type IS RECORD(
    process   cg$row.process%TYPE
  , code      cg$row.code%TYPE
  , the_rowid ROWID
  );

  --  PL/SQL Table Type variable for triggers
  TYPE cg$table_type IS TABLE OF qms_rule_properties%ROWTYPE INDEX BY BINARY_INTEGER;
  cg$table cg$table_type;

  TYPE cg$tableind_type IS TABLE OF cg$ind_type INDEX BY BINARY_INTEGER;
  cg$tableind cg$tableind_type;

  idx BINARY_INTEGER := 1;
  PROCEDURE validate_foreign_keys_ins     ( cg$rec      IN     cg$row_type );
  PROCEDURE validate_foreign_keys_upd     ( cg$rec      IN     cg$row_type
                                          , cg$old_rec  IN     cg$row_type
                                          , cg$ind      IN     cg$ind_type
                                          );
  PROCEDURE validate_foreign_keys_del     ( cg$rec      IN     cg$row_type );

  PROCEDURE ins                           ( cg$rec      IN OUT  cg$row_type
                                          , cg$ind      IN OUT  cg$ind_type
                                          , do_ins      IN      BOOLEAN DEFAULT TRUE
                                          );
  PROCEDURE upd                           ( cg$rec      IN OUT  cg$row_type
                                          , cg$ind      IN OUT  cg$ind_type
                                          , do_upd      IN      BOOLEAN DEFAULT TRUE
                                          , cg$pk       IN      cg$row_type DEFAULT NULL
                                          );
  PROCEDURE del                           ( cg$pk       IN      cg$pk_type
                                          , do_del      IN      BOOLEAN DEFAULT TRUE
                                          );
  PROCEDURE sel                           ( cg$sel_rec  IN OUT cg$row_type);
  PROCEDURE lck                           ( cg$old_rec  IN     cg$row_type
                                          , cg$old_ind  IN     cg$ind_type
                                          , nowait_flag IN     BOOLEAN DEFAULT TRUE
                                          );
  PROCEDURE cascade_update                ( cg$new_rec  IN OUT cg$row_type
                                          , cg$old_rec  IN     cg$row_type
                                          );
  PROCEDURE cascade_delete                ( cg$old_rec  IN OUT cg$row_type );
END qms$rule_properties;
/
SHOW ERROR