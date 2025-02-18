-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\portalBackend\..\vehicleBackend\src\main\resources\dbs\epp\api\ept-vmt.pks
--
-- Generated for Oracle Database 12c on Sun May 24 17:30:58 2020 by Server Generator 10.1.2.93.10

PROMPT Creating API Package Specification for Table 'ept_vehicle_brand_types'
--------------------------------------------------------------------------------
-- Name:        cg$ept_vehicle_brand_types
-- Description: cg$ept_vehicle_brand_types table API package declarations
--------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE cg$ept_vehicle_brand_types
IS

  called_from_package BOOLEAN := FALSE;

  -- Repository User-Defined Error Messages
  EPT_VMT_PK        CONSTANT VARCHAR2(10) := 'EPT-00107';
  EPT_VMT_VMB_FK    CONSTANT VARCHAR2(10) := 'EPT-00402';
  EPT_VMT_VHT_FK    CONSTANT VARCHAR2(10) := 'EPT-00403';

  -- Column default prompts. Format PSEQNO_COL
  P10VMB_ID         CONSTANT VARCHAR2(30) := 'Brand';
  P15VHT_ID         CONSTANT VARCHAR2(30) := 'Type';
  P20ROWVERSION     CONSTANT VARCHAR2(30) := 'Row Version';
  P25CREATED_BY     CONSTANT VARCHAR2(30) := 'Created By';
  P30CREATED_ON     CONSTANT VARCHAR2(30) := 'Created On';
  P35UPDATED_BY     CONSTANT VARCHAR2(30) := 'Updated By';
  P40UPDATED_ON     CONSTANT VARCHAR2(30) := 'Updated On';

  cg$row ept_vehicle_brand_types%ROWTYPE;

  -- ept_vehicle_brand_types row type variable
  TYPE cg$row_type IS RECORD (
    vmb_id          cg$row.vmb_id%TYPE
  , vht_id          cg$row.vht_id%TYPE
  , rowversion      cg$row.rowversion%TYPE
  , created_by      cg$row.created_by%TYPE
  , created_on      cg$row.created_on%TYPE
  , updated_by      cg$row.updated_by%TYPE
  , updated_on      cg$row.updated_on%TYPE
  , the_rowid       ROWID
  );

  -- ept_vehicle_brand_types indicator type variable
  TYPE cg$ind_type IS RECORD (
    vmb_id          BOOLEAN DEFAULT FALSE
  , vht_id          BOOLEAN DEFAULT FALSE
  , rowversion      BOOLEAN DEFAULT FALSE
  , created_by      BOOLEAN DEFAULT FALSE
  , created_on      BOOLEAN DEFAULT FALSE
  , updated_by      BOOLEAN DEFAULT FALSE
  , updated_on      BOOLEAN DEFAULT FALSE
  );

  cg$ind_true cg$ind_type;

  -- ept_vehicle_brand_types primary key type variable
  TYPE cg$pk_type IS RECORD (
    vmb_id          cg$row.vmb_id%TYPE
  , vht_id          cg$row.vht_id%TYPE
  , the_rowid       ROWID
  );

  -- PL/SQL Table Type variable for triggers
  TYPE cg$table_type IS TABLE of ept_vehicle_brand_types%ROWTYPE
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
END cg$ept_vehicle_brand_types;
/
SHOW ERROR
