-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BIR_QMS_MESSAGE_PROPERTIES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BIR_QMS_MESSAGE_PROPERTIES</name>
--         <identifier class="java.math.BigDecimal">46677</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bir_qms_message_properties
BEFORE INSERT ON qms_message_properties FOR EACH ROW
DECLARE
  cg$rec qms$message_properties.cg$row_type;
  cg$ind qms$message_properties.cg$ind_type;
BEGIN
  --  Application_logic Pre-Before-Insert-row <<Start>>
  --  Application_logic Pre-Before-Insert-row << End >>

  --  Load cg$rec/cg$ind values from new
  cg$rec.code             := :new.code;
  cg$ind.code             := TRUE;
  cg$rec.description      := :new.description;
  cg$ind.description      := TRUE;
  cg$rec.severity         := :new.severity;
  cg$ind.severity         := TRUE;
  cg$rec.logging          := :new.logging;
  cg$ind.logging          := TRUE;
  cg$rec.suppress_warning := :new.suppress_warning;
  cg$ind.suppress_warning := TRUE;
  cg$rec.suppress_always  := :new.suppress_always;
  cg$ind.suppress_always  := TRUE;
  cg$rec.constraint_name  := :new.constraint_name;
  cg$ind.constraint_name  := TRUE;

  cg$rec.created_by      := :new.created_by;
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

  IF NOT (qms$message_properties.called_from_package)
  THEN
    qms$message_properties.ins(cg$rec, cg$ind, FALSE);
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

  --  Application_logic Post-Before-Insert-row <<Start>>
  --  Application_logic Post-Before-Insert-row << End >>
END;
/
