-- /Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityService/uniqueidBackend/src/main/resources/install/uid/api/uit_ptt.pks
--
--   Einfuehrung einer personenbezogenen ID im Progamm Polizei 2020
--   Unique Identifier (UID)
--   Version 1.2 | Stand 15.03.2022
--
--   Reference to Page 20 Table Row 1 CodeList
--   Vorgabe durch P20 IAM: CodeList: TH=T Partner:P
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Specification for Table 'uit_participant_types'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
-- Name:        cg$uit_participant_types
-- Description: uit_participant_types table API package declarations
--------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE cg$uit_participant_types
IS

  called_from_package BOOLEAN := FALSE;

  -- Repository User-Defined Error Messages
  UIT_PTT_PK        CONSTANT VARCHAR2(10) := 'UIT-00101';
  UIT_PTT_UK        CONSTANT VARCHAR2(10) := 'UIT-00201';
  UIT_PTT_CK1       CONSTANT VARCHAR2(10) := 'UIT-00301';

  -- Column default prompts. Format PSEQNO_COL
  P10ID         CONSTANT VARCHAR2(30) := 'Id';
  P15ROWVERSION CONSTANT VARCHAR2(30) := 'Rowversion';
  P20CREATED_BY CONSTANT VARCHAR2(30) := 'Created By';
  P25CREATED_ON CONSTANT VARCHAR2(30) := 'Created On';
  P30UPDATED_BY CONSTANT VARCHAR2(30) := 'Modified By';
  P35UPDATED_ON CONSTANT VARCHAR2(30) := 'Modified On';
  P40ACTIVE     CONSTANT VARCHAR2(30) := 'Active';
  P45NAME       CONSTANT VARCHAR2(30) := 'Name';

  cg$row uit_participant_types%ROWTYPE;

  -- uit_participant_types row type variable
  TYPE cg$row_type IS RECORD (
    id          cg$row.id%TYPE
  , rowversion  cg$row.rowversion%TYPE
  , created_by  cg$row.created_by%TYPE
  , created_on  cg$row.created_on%TYPE
  , updated_by  cg$row.updated_by%TYPE
  , updated_on  cg$row.updated_on%TYPE
  , active      cg$row.active%TYPE
  , name        cg$row.name%TYPE
  , the_rowid   ROWID
  );

  -- uit_participant_types indicator type variable
  TYPE cg$ind_type IS RECORD (
    id          BOOLEAN DEFAULT FALSE
  , rowversion  BOOLEAN DEFAULT FALSE
  , created_by  BOOLEAN DEFAULT FALSE
  , created_on  BOOLEAN DEFAULT FALSE
  , updated_by  BOOLEAN DEFAULT FALSE
  , updated_on  BOOLEAN DEFAULT FALSE
  , active      BOOLEAN DEFAULT FALSE
  , name        BOOLEAN DEFAULT FALSE
  );

  -- uit_participant_types primary key type variable
  TYPE cg$pk_type IS RECORD (
    id          cg$row.id%TYPE
  , the_rowid   ROWID
  );

  -- PL/SQL Table Type variable for triggers
  TYPE cg$table_type IS TABLE OF uit_participant_types%ROWTYPE
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
END cg$uit_participant_types;
/
SHOW ERROR