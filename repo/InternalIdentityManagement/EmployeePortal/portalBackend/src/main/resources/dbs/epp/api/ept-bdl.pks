-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\portalBackend\src\main\resources\dbs\epp\api\ept-bdl.pks
--
-- Generated for Oracle Database 12c on Sun May 24 17:02:48 2020 by Server Generator 10.1.2.93.10

PROMPT Creating API Package Specification for Table 'ept_bundles'
--------------------------------------------------------------------------------
-- Name:        cg$ept_bundles
-- Description: cg$ept_bundles table API package declarations
--------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE cg$ept_bundles
IS

  called_from_package BOOLEAN := FALSE;

  -- Repository User-Defined Error Messages
  EPT_BDL_PK        CONSTANT VARCHAR2(10) := 'EPT-00103';
  EPT_BDL_LOC_FK    CONSTANT VARCHAR2(10) := 'EPT-00401';

  -- Column default prompts. Format PSEQNO_COL
  P10LOC_ID         CONSTANT VARCHAR2(30) := 'Locale';
  P15ENTITY         CONSTANT VARCHAR2(30) := 'Entity';
  P20ITEM           CONSTANT VARCHAR2(30) := 'Item';
  P25ROWVERSION     CONSTANT VARCHAR2(30) := 'Row Version';
  P30CREATED_BY     CONSTANT VARCHAR2(30) := 'Created By';
  P35CREATED_ON     CONSTANT VARCHAR2(30) := 'Created On';
  P40UPDATED_BY     CONSTANT VARCHAR2(30) := 'Updated By';
  P45UPDATED_ON     CONSTANT VARCHAR2(30) := 'Updated On';
  P50MEANING        CONSTANT VARCHAR2(30) := 'Meaning';

  cg$row ept_bundles%ROWTYPE;

  -- ept_bundles row type variable
  TYPE cg$row_type IS RECORD (
    loc_id          cg$row.loc_id%TYPE
  , entity          cg$row.entity%TYPE
  , item            cg$row.item%TYPE
  , rowversion      cg$row.rowversion%TYPE
  , created_by      cg$row.created_by%TYPE
  , created_on      cg$row.created_on%TYPE
  , updated_by      cg$row.updated_by%TYPE
  , updated_on      cg$row.updated_on%TYPE
  , meaning         cg$row.meaning%TYPE
  , the_rowid       ROWID
  );

  -- ept_bundles indicator type variable
  TYPE cg$ind_type IS RECORD (
    loc_id          BOOLEAN DEFAULT FALSE
  , entity          BOOLEAN DEFAULT FALSE
  , item            BOOLEAN DEFAULT FALSE
  , rowversion      BOOLEAN DEFAULT FALSE
  , created_by      BOOLEAN DEFAULT FALSE
  , created_on      BOOLEAN DEFAULT FALSE
  , updated_by      BOOLEAN DEFAULT FALSE
  , updated_on      BOOLEAN DEFAULT FALSE
  , meaning         BOOLEAN DEFAULT FALSE
  );

  cg$ind_true cg$ind_type;

  -- ept_bundles primary key type variable
  TYPE cg$pk_type IS RECORD (
    loc_id          cg$row.loc_id%TYPE
  , entity          cg$row.entity%TYPE
  , item            cg$row.item%TYPE
  , the_rowid       ROWID
  );

  -- PL/SQL Table Type variable for triggers
  TYPE cg$table_type IS TABLE of ept_bundles%ROWTYPE
    INDEX BY BINARY_INTEGER;
  cg$table cg$table_type;

  TYPE cg$tableind_type IS TABLE OF cg$ind_type
    INDEX BY BINARY_INTEGER;
  cg$tableind cg$tableind_type;

  idx BINARY_INTEGER := 1;

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

  PROCEDURE validate_arc                  ( cg$rec      IN OUT cg$row_type );
  PROCEDURE validate_domain               ( cg$rec      IN OUT cg$row_type
                                          , cg$ind      IN     cg$ind_type DEFAULT cg$ind_true
                                          );

  PROCEDURE validate_foreign_keys_ins     ( cg$rec      IN     cg$row_type );
  PROCEDURE validate_foreign_keys_upd     ( cg$rec      IN     cg$row_type
                                          , cg$old_rec  IN     cg$row_type
                                          , cg$ind      IN     cg$ind_type
                                          );
  PROCEDURE validate_foreign_keys_del     ( cg$rec      IN     cg$row_type );

  PROCEDURE validate_domain_cascade_delete( cg$old_rec  IN     cg$row_type );
  PROCEDURE validate_domain_cascade_update( cg$old_rec  IN     cg$row_type );

  PROCEDURE cascade_update                ( cg$new_rec  IN OUT cg$row_type
                                          , cg$old_rec  IN     cg$row_type
                                          );
  PROCEDURE domain_cascade_update         ( cg$new_rec  IN OUT cg$row_type
                                          , cg$new_ind  IN OUT cg$ind_type
                                          , cg$old_rec  IN     cg$row_type
                                          );
  PROCEDURE domain_cascade_upd            ( cg$rec      IN OUT cg$row_type
                                          , cg$ind      IN OUT cg$ind_type
                                          , cg$old_rec  IN     cg$row_type
                                          );
  PROCEDURE cascade_delete                ( cg$old_rec  IN OUT cg$row_type );
  PROCEDURE domain_cascade_delete         ( cg$old_rec  IN     cg$row_type );

  PROCEDURE upd_denorm2                   ( cg$rec      IN     cg$row_type
                                          , cg$ind      IN     cg$ind_type
                                          );
  PROCEDURE upd_oper_denorm2              ( cg$rec      IN     cg$row_type
                                          , cg$old_rec  IN     cg$row_type
                                          , cg$ind      IN     cg$ind_type
                                          , operation   IN     VARCHAR2 DEFAULT 'UPD'
                                          );
END cg$ept_bundles;
/
SHOW ERROR
