-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BDR_UIT_PARTICIPANTS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BDR_UIT_PARTICIPANTS</name>
--         <identifier class="java.math.BigDecimal">46817</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bdr_uit_participants
BEFORE DELETE ON uit_participants FOR EACH ROW
DECLARE
  cg$pk  cg$uit_participants.cg$pk_type;
  cg$rec cg$uit_participants.cg$row_type;
  cg$ind cg$uit_participants.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Delete-row <<Start>>
  -- Application_logic Pre-Before-Delete-row << End >>

  -- Load cg$rec/cg$ind values from old
  cg$pk.id  := :old.id;
  cg$rec.id := :old.id;
  cg$uit_participants.cg$table(cg$uit_participants.idx).id := :old.id;

  cg$uit_participants.idx := cg$uit_participants.idx + 1;
  IF NOT (cg$uit_participants.called_from_package)
  THEN
    cg$uit_participants.del(cg$pk, FALSE);
    cg$uit_participants.called_from_package := FALSE;
  END IF;

  -- Application_logic Post-Before-Delete-row <<Start>>
  -- Application_logic Post-Before-Delete-row << End >>
END;
/
