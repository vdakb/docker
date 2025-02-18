-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\qms_mst.pks
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Specification for Table 'qms$message_text'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
-- Name:        qms$message_text
-- Description: qms$message_text table API package declarations
--------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE qms$message_text IS

  called_from_package BOOLEAN := FALSE;

  --  Repository User-Defined Error Messages
  QMS_MST_PK       CONSTANT VARCHAR2(10) := 'QMS-00078';
  QMS_MST_MSP_FK1  CONSTANT VARCHAR2(10) := 'QMS-00079';

  --  Column default prompts. Format PSEQNO_COL
  P10CODE       CONSTANT VARCHAR2(30) := 'Message Code';
  P10LANGUAGE   CONSTANT VARCHAR2(30) := 'Lang Code';
  P20TEXT       CONSTANT VARCHAR2(30) := 'Msg Text';
  P25HELP       CONSTANT VARCHAR2(30) := 'Help Text';
  P30CREATED_BY CONSTANT VARCHAR2(30) := 'Created By';
  P35CREATED_ON CONSTANT VARCHAR2(30) := 'Created On';
  P40UPDATED_BY CONSTANT VARCHAR2(30) := 'Updated By';
  P45UPDATED_ON CONSTANT VARCHAR2(30) := 'Updated On';

  cg$row qms_message_text%ROWTYPE;

  --  qms_message_text row type variable
  TYPE cg$row_type IS RECORD(
    code      cg$row.code%TYPE
  , language  cg$row.language%TYPE
  , text      cg$row.text%TYPE
  , help      cg$row.help%TYPE
  ,created_by cg$row.created_by%TYPE
  ,created_on cg$row.created_on%TYPE
  ,updated_by cg$row.updated_by%TYPE
  ,updated_on cg$row.updated_on%TYPE
  ,the_rowid  ROWID
  ,jn_notes   VARCHAR2(240)
  );

  --  qms_message_text indicator type variable
  TYPE cg$ind_type IS RECORD(
    code       BOOLEAN DEFAULT FALSE
  , language   BOOLEAN DEFAULT FALSE
  , text       BOOLEAN DEFAULT FALSE
  , help       BOOLEAN DEFAULT FALSE
  , created_by BOOLEAN DEFAULT FALSE
  , created_on BOOLEAN DEFAULT FALSE
  , updated_by BOOLEAN DEFAULT FALSE
  , updated_on BOOLEAN DEFAULT FALSE
  );

  --  qms_message_text primary key type variable
  TYPE cg$pk_type IS RECORD(
    code      cg$row.code%TYPE
  , language  cg$row.language%TYPE
  , the_rowid ROWID
  , jn_notes  VARCHAR2(240));

  --  PL/SQL Table Type variable for triggers
  TYPE cg$table_type IS TABLE OF qms_message_text%ROWTYPE INDEX BY BINARY_INTEGER;
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
END qms$message_text;
/
SHOW ERROR