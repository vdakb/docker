-- /Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityService/uniqueidBackend/src/main/resources/install/uid/api/uit_uid.pks
--
--   Einfuehrung einer personenbezogenen ID im Progamm Polizei 2020
--   Unique Identifier (UID)
--   Version 1.2 | Stand 15.03.2022
--
--   Reference to Page 21 Table 2
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Specification for Table 'uit_identifiers'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
-- Name:        cg$uit_identifiers
-- Description: uit_identifiers table API package declarations
--------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE cg$uit_identifiers
IS

  called_from_package BOOLEAN := FALSE;

  -- Repository User-Defined Error Messages
  UIT_UID_PK        CONSTANT VARCHAR2(10) := 'UIT-00108';
  UIT_UID_CK1       CONSTANT VARCHAR2(10) := 'UIT-00307';
  UIT_UID_TNT_FK    CONSTANT VARCHAR2(10) := 'UIT-00404';
  UIT_UID_TYP_FK    CONSTANT VARCHAR2(10) := 'UIT-00405';

  -- Column default prompts. Format PSEQNO_COL
  P10TNT_ID     CONSTANT VARCHAR2(30) := 'Tenant';
  P15TYP_ID     CONSTANT VARCHAR2(30) := 'Type';
  P20EXT_ID     CONSTANT VARCHAR2(30) := 'External';
  P25ROWVERSION CONSTANT VARCHAR2(30) := 'Rowversion';
  P30CREATED_BY CONSTANT VARCHAR2(30) := 'Created By';
  P35CREATED_ON CONSTANT VARCHAR2(30) := 'Created On';
  P40UPDATED_BY CONSTANT VARCHAR2(30) := 'Modified By';
  P45UPDATED_ON CONSTANT VARCHAR2(30) := 'Modified On';
  P50STATE      CONSTANT VARCHAR2(30) := 'State';

  cg$row uit_identifiers%ROWTYPE;

  -- uit_identifiers row type variable
  TYPE cg$row_type IS RECORD (
    tnt_id      cg$row.tnt_id%TYPE
  , typ_id      cg$row.typ_id%TYPE
  , ext_id      cg$row.ext_id%TYPE
  , rowversion  cg$row.rowversion%TYPE
  , created_by  cg$row.created_by%TYPE
  , created_on  cg$row.created_on%TYPE
  , updated_by  cg$row.updated_by%TYPE
  , updated_on  cg$row.updated_on%TYPE
  , state       cg$row.state%TYPE
  , the_rowid   ROWID
  );

  -- uit_identifiers indicator type variable
  TYPE cg$ind_type IS RECORD (
    tnt_id      BOOLEAN DEFAULT FALSE
  , typ_id      BOOLEAN DEFAULT FALSE
  , ext_id      BOOLEAN DEFAULT FALSE
  , rowversion  BOOLEAN DEFAULT FALSE
  , created_by  BOOLEAN DEFAULT FALSE
  , created_on  BOOLEAN DEFAULT FALSE
  , updated_by  BOOLEAN DEFAULT FALSE
  , updated_on  BOOLEAN DEFAULT FALSE
  , state       BOOLEAN DEFAULT FALSE
  );

  -- uit_identifiers primary key type variable
  TYPE cg$pk_type IS RECORD (
    tnt_id      cg$row.tnt_id%TYPE
  , typ_id      cg$row.typ_id%TYPE
  , ext_id      cg$row.ext_id%TYPE
  , the_rowid   ROWID
  );

  -- PL/SQL Table Type variable for triggers
  TYPE cg$table_type IS TABLE OF uit_identifiers%ROWTYPE
    INDEX BY BINARY_INTEGER;

  TYPE cg$tableind_type IS TABLE OF cg$ind_type
    INDEX BY BINARY_INTEGER;

  cg$table       cg$table_type;
  cg$tableind    cg$tableind_type;
  idx            BINARY_INTEGER := 1;

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
END cg$uit_identifiers;
/
SHOW ERROR