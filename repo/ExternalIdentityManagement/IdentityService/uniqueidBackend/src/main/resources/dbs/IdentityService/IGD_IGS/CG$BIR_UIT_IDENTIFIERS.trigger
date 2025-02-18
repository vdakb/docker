-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BIR_UIT_IDENTIFIERS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BIR_UIT_IDENTIFIERS</name>
--         <identifier class="java.math.BigDecimal">46838</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bir_uit_identifiers
BEFORE INSERT ON uit_identifiers FOR EACH ROW
DECLARE
  cg$rec cg$uit_identifiers.cg$row_type;
  cg$ind cg$uit_identifiers.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Insert-row <<Start>>
  -- Application_logic Pre-Before-Insert-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.tnt_id := :new.tnt_id;
  IF (:new.tnt_id IS NULL)
  THEN
    cg$ind.tnt_id := FALSE;
  ELSE
    cg$ind.tnt_id := TRUE;
  END IF;
  cg$rec.typ_id := :new.typ_id;
  IF (:new.typ_id IS NULL)
  THEN
    cg$ind.typ_id := FALSE;
  ELSE
    cg$ind.typ_id := TRUE;
  END IF;
  cg$rec.ext_id := :new.ext_id;
  IF (:new.ext_id IS NULL)
  THEN
    cg$ind.ext_id := FALSE;
  ELSE
    cg$ind.ext_id := TRUE;
  END IF;
  cg$rec.rowversion := :new.rowversion;
  IF (:new.rowversion IS NULL)
  THEN
    cg$ind.rowversion := FALSE;
  ELSE
    cg$ind.rowversion := TRUE;
  END IF;
  cg$rec.created_by := :new.created_by;
  IF (:new.created_by IS NULL)
  THEN
    cg$ind.created_by := FALSE;
  ELSE
    cg$ind.created_by := TRUE;
  END IF;
  cg$rec.created_on := :new.created_on;
  IF (:new.created_on IS NULL)
  THEN
    cg$ind.created_on := FALSE;
  ELSE
    cg$ind.created_on := TRUE;
  END IF;
  cg$rec.updated_by := :new.updated_by;
  IF (:new.updated_by IS NULL)
  THEN
    cg$ind.updated_by := FALSE;
  ELSE
    cg$ind.updated_by := TRUE;
  END IF;
  cg$rec.updated_on := :new.updated_on;
  IF (:new.updated_on IS NULL)
  THEN
    cg$ind.updated_on := FALSE;
  ELSE
    cg$ind.updated_on := TRUE;
  END IF;
  cg$rec.state := :new.state;
  IF (:new.state IS NULL)
  THEN
    cg$ind.state := FALSE;
  ELSE
    cg$ind.state := TRUE;
  END IF;

  IF NOT (cg$uit_identifiers.called_from_package)
  THEN
    cg$uit_identifiers.ins(cg$rec, cg$ind, FALSE);
    cg$uit_identifiers.called_from_package := FALSE;
  END IF;

  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).tnt_id            := cg$rec.tnt_id;
  cg$uit_identifiers.cg$tableind(cg$uit_identifiers.idx).tnt_id         := cg$ind.tnt_id;

  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).typ_id            := cg$rec.typ_id;
  cg$uit_identifiers.cg$tableind(cg$uit_identifiers.idx).typ_id         := cg$ind.typ_id;

  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).ext_id            := cg$rec.ext_id;
  cg$uit_identifiers.cg$tableind(cg$uit_identifiers.idx).ext_id         := cg$ind.ext_id;

  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).rowversion        := cg$rec.rowversion;
  cg$uit_identifiers.cg$tableind(cg$uit_identifiers.idx).rowversion     := cg$ind.rowversion;

  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).created_by        := cg$rec.created_by;
  cg$uit_identifiers.cg$tableind(cg$uit_identifiers.idx).created_by     := cg$ind.created_by;

  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).created_on        := cg$rec.created_on;
  cg$uit_identifiers.cg$tableind(cg$uit_identifiers.idx).created_on     := cg$ind.created_on;

  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).updated_by        := cg$rec.updated_by;
  cg$uit_identifiers.cg$tableind(cg$uit_identifiers.idx).updated_by     := cg$ind.updated_by;

  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).updated_on        := cg$rec.updated_on;
  cg$uit_identifiers.cg$tableind(cg$uit_identifiers.idx).updated_on     := cg$ind.updated_on;

  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).state             := cg$rec.state;
  cg$uit_identifiers.cg$tableind(cg$uit_identifiers.idx).state          := cg$ind.state;

  cg$uit_identifiers.idx := cg$uit_identifiers.idx + 1;

  :new.tnt_id         := cg$rec.tnt_id;
  :new.typ_id         := cg$rec.typ_id;
  :new.ext_id         := cg$rec.ext_id;
  :new.rowversion     := cg$rec.rowversion;
  :new.created_by     := cg$rec.created_by;
  :new.created_on     := cg$rec.created_on;
  :new.updated_by     := cg$rec.updated_by;
  :new.updated_on     := cg$rec.updated_on;
  :new.state          := cg$rec.state;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
