-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\portalBackend\..\vehicleBackend\src\main\resources\dbs\epp\api\ept-vhl.pks
--
-- Generated for Oracle Database 12c on Sun May 24 17:30:58 2020 by Server Generator 10.1.2.93.10

PROMPT Creating API Package Specification for Table 'ept_vehicles'
--------------------------------------------------------------------------------
-- Name:        cg$ept_vehicles
-- Description: cg$ept_vehicles table API package declarations
--------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE cg$ept_vehicles
IS

  called_from_package BOOLEAN := FALSE;

  -- Repository User-Defined Error Messages
  EPT_VHL_PK        CONSTANT VARCHAR2(10) := 'EPT-00108';
  EPT_VHL_VMT_FK    CONSTANT VARCHAR2(10) := 'EPT-00404';
  EPT_VHL_VHC_FK    CONSTANT VARCHAR2(10) := 'EPT-00405';

  -- Column default prompts. Format PSEQNO_COL
  P10PREFIX         CONSTANT VARCHAR2(30) := 'Prefix';
  P15CODE           CONSTANT VARCHAR2(30) := 'Code';
  P20ROWVERSION     CONSTANT VARCHAR2(30) := 'Row Version';
  P25CREATED_BY     CONSTANT VARCHAR2(30) := 'Created By';
  P30CREATED_ON     CONSTANT VARCHAR2(30) := 'Created On';
  P35UPDATED_BY     CONSTANT VARCHAR2(30) := 'Updated By';
  P40UPDATED_ON     CONSTANT VARCHAR2(30) := 'Updated On';
  P45VMB_ID         CONSTANT VARCHAR2(30) := 'Brand';
  P50VHT_ID         CONSTANT VARCHAR2(30) := 'Type';
  P55VHC_ID         CONSTANT VARCHAR2(30) := 'Color';
  P60DRIVER         CONSTANT VARCHAR2(30) := 'Driver';

  cg$row ept_vehicles%ROWTYPE;

  -- ept_vehicles row type variable
  TYPE cg$row_type IS RECORD (
    prefix          cg$row.prefix%TYPE
  , code            cg$row.code%TYPE
  , rowversion      cg$row.rowversion%TYPE
  , created_by      cg$row.created_by%TYPE
  , created_on      cg$row.created_on%TYPE
  , updated_by      cg$row.updated_by%TYPE
  , updated_on      cg$row.updated_on%TYPE
  , vmb_id          cg$row.vmb_id%TYPE
  , vht_id          cg$row.vht_id%TYPE
  , vhc_id          cg$row.vhc_id%TYPE
  , driver          cg$row.driver%TYPE
  , the_rowid       ROWID
  );

  -- ept_vehicles indicator type variable
  TYPE cg$ind_type IS RECORD (
    prefix          BOOLEAN DEFAULT FALSE
  , code            BOOLEAN DEFAULT FALSE
  , rowversion      BOOLEAN DEFAULT FALSE
  , created_by      BOOLEAN DEFAULT FALSE
  , created_on      BOOLEAN DEFAULT FALSE
  , updated_by      BOOLEAN DEFAULT FALSE
  , updated_on      BOOLEAN DEFAULT FALSE
  , vmb_id          BOOLEAN DEFAULT FALSE
  , vht_id          BOOLEAN DEFAULT FALSE
  , vhc_id          BOOLEAN DEFAULT FALSE
  , driver          BOOLEAN DEFAULT FALSE
  );

  cg$ind_true cg$ind_type;

  -- ept_vehicles primary key type variable
  TYPE cg$pk_type IS RECORD (
    prefix          cg$row.prefix%TYPE
  , the_rowid       ROWID
  );

  -- PL/SQL Table Type variable for triggers
  TYPE cg$table_type IS TABLE of ept_vehicles%ROWTYPE
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
END cg$ept_vehicles;
/
SHOW ERROR
