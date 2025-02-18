-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BIR_UIT_TENANTS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BIR_UIT_TENANTS</name>
--         <identifier class="java.math.BigDecimal">46829</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bir_uit_tenants
BEFORE INSERT ON uit_tenants FOR EACH ROW
DECLARE
  cg$rec cg$uit_tenants.cg$row_type;
  cg$ind cg$uit_tenants.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Insert-row <<Start>>
  -- Application_logic Pre-Before-Insert-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.id := :new.id;
  IF (:new.id IS NULL)
  THEN
    cg$ind.id := FALSE;
  ELSE
    cg$ind.id := TRUE;
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
  cg$rec.name := :new.name;
  IF (:new.name IS NULL)
  THEN
    cg$ind.name := FALSE;
  ELSE
    cg$ind.name := TRUE;
  END IF;

  IF NOT (cg$uit_tenants.called_from_package)
  THEN
    cg$uit_tenants.ins(cg$rec, cg$ind, FALSE);
    cg$uit_tenants.called_from_package := FALSE;
  END IF;

  cg$uit_tenants.cg$table(cg$uit_tenants.idx).id            := cg$rec.id;
  cg$uit_tenants.cg$tableind(cg$uit_tenants.idx).id         := cg$ind.id;

  cg$uit_tenants.cg$table(cg$uit_tenants.idx).rowversion    := cg$rec.rowversion;
  cg$uit_tenants.cg$tableind(cg$uit_tenants.idx).rowversion := cg$ind.rowversion;

  cg$uit_tenants.cg$table(cg$uit_tenants.idx).created_by    := cg$rec.created_by;
  cg$uit_tenants.cg$tableind(cg$uit_tenants.idx).created_by := cg$ind.created_by;

  cg$uit_tenants.cg$table(cg$uit_tenants.idx).created_on    := cg$rec.created_on;
  cg$uit_tenants.cg$tableind(cg$uit_tenants.idx).created_on := cg$ind.created_on;

  cg$uit_tenants.cg$table(cg$uit_tenants.idx).updated_by    := cg$rec.updated_by;
  cg$uit_tenants.cg$tableind(cg$uit_tenants.idx).updated_by := cg$ind.updated_by;

  cg$uit_tenants.cg$table(cg$uit_tenants.idx).updated_on    := cg$rec.updated_on;
  cg$uit_tenants.cg$tableind(cg$uit_tenants.idx).updated_on := cg$ind.updated_on;

  cg$uit_tenants.cg$table(cg$uit_tenants.idx).name          := cg$rec.name;
  cg$uit_tenants.cg$tableind(cg$uit_tenants.idx).name       := cg$ind.name;

  cg$uit_tenants.idx := cg$uit_tenants.idx + 1;

  :new.id             := cg$rec.id;
  :new.rowversion     := cg$rec.rowversion;
  :new.created_by     := cg$rec.created_by;
  :new.created_on     := cg$rec.created_on;
  :new.updated_by     := cg$rec.updated_by;
  :new.updated_on     := cg$rec.updated_on;
  :new.name           := cg$rec.name;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
