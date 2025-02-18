-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BUR_QMS_MESSAGE_PROPERTIES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BUR_QMS_MESSAGE_PROPERTIES</name>
--         <identifier class="java.math.BigDecimal">46681</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bur_qms_message_properties
BEFORE UPDATE ON qms_message_properties FOR EACH ROW
DECLARE
  cg$rec qms$message_properties.cg$row_type;
  cg$ind qms$message_properties.cg$ind_type;
BEGIN
  --  Application_logic Pre-Before-Update-row <<Start>>
  --  Application_logic Pre-Before-Update-row << End >>

  --  Load cg$rec/cg$ind values from new
  cg$rec.code := :new.code;
  cg$ind.code := (:new.code IS NULL AND :old.code IS NOT NULL )
              OR (:old.code IS NULL AND :new.code IS NOT NULL)
              OR NOT(:new.code = :old.code);
  cg$rec.description := :new.description;
  cg$ind.description := (:new.description IS NULL AND :old.description IS NOT NULL )
                     OR (:new.description IS NOT NULL AND :old.description IS NULL)
                     OR NOT(:new.description = :old.description);
  cg$rec.severity := :new.severity;
  cg$ind.severity := (:new.severity IS NULL AND :old.severity IS NOT NULL )
                  OR (:new.severity IS NOT NULL AND :old.severity IS NULL)
                  OR NOT(:new.severity = :old.severity);
  cg$rec.logging := :new.logging;
  cg$ind.logging := (:new.logging IS NULL AND :old.logging IS NOT NULL )
                 OR (:new.logging IS NOT NULL AND :old.logging IS NULL)
                 OR NOT(:new.logging = :old.logging);
  cg$rec.suppress_warning := :new.suppress_warning;
  cg$ind.suppress_warning := (:new.suppress_warning IS NULL AND :old.suppress_warning IS NOT NULL )
                          OR (:new.suppress_warning IS NOT NULL AND :old.suppress_warning IS NULL)
                          OR NOT(:new.suppress_warning = :old.suppress_warning);
  cg$rec.suppress_always := :new.suppress_always;
  cg$ind.suppress_always := (:new.suppress_always IS NULL AND :old.suppress_always IS NOT NULL )
                         OR (:new.suppress_always IS NOT NULL AND :old.suppress_always IS NULL)
                         OR NOT(:new.suppress_always = :old.suppress_always);
  cg$rec.constraint_name := :new.constraint_name;
  cg$ind.constraint_name := (:new.constraint_name IS NULL AND :old.constraint_name IS NOT NULL )
                         OR (:new.constraint_name IS NOT NULL AND :old.constraint_name IS NULL)
                         OR NOT(:new.constraint_name = :old.constraint_name);
  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL )
                    OR (:new.created_by IS NOT NULL AND :old.created_by IS NULL)
                    OR NOT(:new.created_by = :old.created_by);
  cg$rec.created_on := :new.created_on;
  cg$ind.created_on :=  (:new.created_on IS NULL AND :old.created_on IS NOT NULL )
                    OR (:new.created_on IS NOT NULL AND :old.created_on IS NULL)
                    OR NOT(:new.created_on = :old.created_on);
  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL )
                    OR (:new.updated_by IS NOT NULL AND :old.updated_by IS NULL)
                    OR NOT(:new.updated_by = :old.updated_by);
  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on :=  (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL )
                    OR (:new.updated_on IS NOT NULL AND :old.updated_on IS NULL)
                    OR NOT(:new.updated_on = :old.updated_on);

  qms$message_properties.cg$table(qms$message_properties.idx).code             := :old.code;
  qms$message_properties.cg$table(qms$message_properties.idx).description      := :old.description;
  qms$message_properties.cg$table(qms$message_properties.idx).severity         := :old.severity;
  qms$message_properties.cg$table(qms$message_properties.idx).logging          := :old.logging;
  qms$message_properties.cg$table(qms$message_properties.idx).suppress_warning := :old.suppress_warning;
  qms$message_properties.cg$table(qms$message_properties.idx).suppress_always  := :old.suppress_always;
  qms$message_properties.cg$table(qms$message_properties.idx).constraint_name  := :old.constraint_name;
  qms$message_properties.cg$table(qms$message_properties.idx).created_by       := :old.created_by;
  qms$message_properties.cg$table(qms$message_properties.idx).created_on       := :old.created_on;
  qms$message_properties.cg$table(qms$message_properties.idx).updated_by       := :old.updated_by;
  qms$message_properties.cg$table(qms$message_properties.idx).updated_on       := :old.updated_on;

  qms$message_properties.idx := qms$message_properties.idx + 1;

  IF NOT (qms$message_properties.called_from_package)
  THEN
    qms$message_properties.upd(cg$rec, cg$ind, FALSE);
    qms$message_properties.called_from_package := FALSE;
  END IF;

  :new.code             := cg$rec.code;
  :new.description      := cg$rec.description;
  :new.severity         := cg$rec.severity;
  :new.logging          := cg$rec.logging;
  :new.suppress_warning := cg$rec.suppress_warning;
  :new.suppress_always  := cg$rec.suppress_always;
  :new.constraint_name  := cg$rec.constraint_name;
  :new.created_by       := cg$rec.created_by;
  :new.created_on       := cg$rec.created_on;
  :new.updated_by       := cg$rec.updated_by;
  :new.updated_on       := cg$rec.updated_on;

  --  Application_logic Post-Before-Update-row <<Start>>
  --  Application_logic Post-Before-Update-row << End >>
END;
/
