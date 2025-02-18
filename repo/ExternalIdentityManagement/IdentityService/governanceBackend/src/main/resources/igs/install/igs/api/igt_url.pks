-- /Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityService/governanceBackend/src/main/resources/install/adm/api/igt_url.pks
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Specification for Table 'igt_userroles'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
-- Name:        igt_userroles
-- Description: igt_userroles table API package declarations
--------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE cg$igt_userroles
IS

  called_from_package BOOLEAN := FALSE;

  -- Repository User-Defined Error Messages
  IGT_URL_PK      CONSTANT VARCHAR2(10) := 'IGT-00103';

  -- Column default prompts. Format PSEQNO_COL
  P10USRID        CONSTANT VARCHAR2(30) := 'User Id';
  P15ROLID        CONSTANT VARCHAR2(30) := 'Role Id';
  P20ROWVERSION   CONSTANT VARCHAR2(30) := 'Rowversion';
  P25CREATED_BY   CONSTANT VARCHAR2(30) := 'Created By';
  P30CREATED_ON   CONSTANT VARCHAR2(30) := 'Created On';
  P35UPDATED_BY   CONSTANT VARCHAR2(30) := 'Modified By';
  P40UPDATED_ON   CONSTANT VARCHAR2(30) := 'Modified On';

  cg$row igt_userroles%ROWTYPE;

  -- cg$igt_userroles row type variable
  TYPE cg$row_type IS RECORD (
    usr_id        cg$row.usr_id%TYPE
  , rol_id        cg$row.rol_id%TYPE
  , rowversion    cg$row.rowversion%TYPE
  , created_by    cg$row.created_by%TYPE
  , created_on    cg$row.created_on%TYPE
  , updated_by    cg$row.updated_by%TYPE
  , updated_on    cg$row.updated_on%TYPE
  , the_rowid     ROWID
  );

  -- cg$igt_userroles indicator type variable
  TYPE cg$ind_type IS RECORD (
    usr_id        BOOLEAN DEFAULT FALSE
  , rol_id        BOOLEAN DEFAULT FALSE
  , rowversion    BOOLEAN DEFAULT FALSE
  , created_by    BOOLEAN DEFAULT FALSE
  , created_on    BOOLEAN DEFAULT FALSE
  , updated_by    BOOLEAN DEFAULT FALSE
  , updated_on    BOOLEAN DEFAULT FALSE
  );

  -- cg$igt_userroles primary key type variable
  TYPE cg$pk_type IS RECORD (
    usr_id        cg$row.usr_id%TYPE
  , rol_id        cg$row.rol_id%TYPE
  , the_rowid     ROWID
  );

  -- PL/SQL Table Type variable for triggers
  TYPE cg$table_type IS TABLE OF igt_userroles%ROWTYPE
    INDEX BY BINARY_INTEGER;

  TYPE cg$tableind_type IS TABLE OF cg$ind_type
    INDEX BY BINARY_INTEGER;

  cg$table    cg$table_type;
  cg$tableind cg$tableind_type;
  idx         BINARY_INTEGER := 1;

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

  PROCEDURE validate_foreign_keys_ins     ( cg$rec      IN     cg$row_type );
  PROCEDURE validate_foreign_keys_upd     ( cg$rec      IN     cg$row_type
                                          , cg$old_rec  IN     cg$row_type
                                          , cg$ind      IN     cg$ind_type
                                          );
  PROCEDURE validate_foreign_keys_del     ( cg$rec      IN     cg$row_type );

  PROCEDURE cascade_update                ( cg$new_rec  IN OUT cg$row_type
                                          , cg$old_rec  IN     cg$row_type
                                          );
  PROCEDURE cascade_delete                ( cg$old_rec  IN OUT cg$row_type );
END cg$igt_userroles;
/
SHOW ERROR