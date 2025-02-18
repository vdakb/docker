-- /Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityService/governanceBackend/src/main/resources/install/adm/api/igt_usr.pks
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Specification for Table 'igt_users'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
-- Name:        cg$igt_users
-- Description: igt_users table API package declarations
--------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE cg$igt_users
IS

  called_from_package BOOLEAN := FALSE;

  -- Repository User-Defined Error Messages
  IGT_USR_PK     CONSTANT VARCHAR2(10) := 'IGT-00102';
  IGT_USR_UK1    CONSTANT VARCHAR2(10) := 'IGT-00202';
  IGT_USR_UK2    CONSTANT VARCHAR2(10) := 'IGT-00203';
  IGT_USR_CK1    CONSTANT VARCHAR2(10) := 'IGT-00302';
  IGT_USR_CK2    CONSTANT VARCHAR2(10) := 'IGT-00303';

  -- Column default prompts. Format PSEQNO_COL
  P10ID         CONSTANT VARCHAR2(30) := 'Id';
  P15ROWVERSION CONSTANT VARCHAR2(30) := 'Rowversion';
  P20CREATED_BY CONSTANT VARCHAR2(30) := 'Created By';
  P25CREATED_ON CONSTANT VARCHAR2(30) := 'Created On';
  P30UPDATED_BY CONSTANT VARCHAR2(30) := 'Modified By';
  P35UPDATED_ON CONSTANT VARCHAR2(30) := 'Modified On';
  P40ACTIVE     CONSTANT VARCHAR2(30) := 'Active';
  P45USERNAME   CONSTANT VARCHAR2(30) := 'User Name';
  P50CREDENTIAL CONSTANT VARCHAR2(30) := 'Credential';
  P55LASTNAME   CONSTANT VARCHAR2(30) := 'Last Name';
  P60FIRSTNAME  CONSTANT VARCHAR2(30) := 'First Name';
  P65LANGUAGE   CONSTANT VARCHAR2(30) := 'Preferred Language';
  P70EMAIL      CONSTANT VARCHAR2(30) := 'e-Mail Address';
  P75PHONE      CONSTANT VARCHAR2(30) := 'Phone Number';
  P80MOBILE     CONSTANT VARCHAR2(30) := 'Mobile Phone Number';

  cg$row igt_users%ROWTYPE;

  -- igt_users row type variable
  TYPE cg$row_type IS RECORD (
    id          cg$row.id%TYPE
  , rowversion  cg$row.rowversion%TYPE
  , created_by  cg$row.created_by%TYPE
  , created_on  cg$row.created_on%TYPE
  , updated_by  cg$row.updated_by%TYPE
  , updated_on  cg$row.updated_on%TYPE
  , active      cg$row.active%TYPE
  , username    cg$row.username%TYPE
  , credential  cg$row.credential%TYPE
  , lastname    cg$row.lastname%TYPE
  , firstname   cg$row.firstname%TYPE
  , language    cg$row.language%TYPE
  , email       cg$row.email%TYPE
  , phone       cg$row.phone%TYPE
  , mobile      cg$row.mobile%TYPE
  , the_rowid   ROWID
  );

  -- igt_users indicator type variable
  TYPE cg$ind_type IS RECORD (
    id          BOOLEAN DEFAULT FALSE
  , rowversion  BOOLEAN DEFAULT FALSE
  , created_by  BOOLEAN DEFAULT FALSE
  , created_on  BOOLEAN DEFAULT FALSE
  , updated_by  BOOLEAN DEFAULT FALSE
  , updated_on  BOOLEAN DEFAULT FALSE
  , active      BOOLEAN DEFAULT FALSE
  , username    BOOLEAN DEFAULT FALSE
  , credential  BOOLEAN DEFAULT FALSE
  , lastname    BOOLEAN DEFAULT FALSE
  , firstname   BOOLEAN DEFAULT FALSE
  , language    BOOLEAN DEFAULT FALSE
  , email       BOOLEAN DEFAULT FALSE
  , phone       BOOLEAN DEFAULT FALSE
  , mobile      BOOLEAN DEFAULT FALSE
  );

  -- igt_users primary key type variable
  TYPE cg$pk_type IS RECORD (
    id          cg$row.id%TYPE
  , the_rowid   ROWID
  );

  -- PL/SQL Table Type variable for triggers
  TYPE cg$table_type IS TABLE OF igt_users%ROWTYPE
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
END cg$igt_users;
/
SHOW ERROR