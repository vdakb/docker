-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$ADS_UIT_PARTICIPANT_TYPES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$ADS_UIT_PARTICIPANT_TYPES</name>
--         <identifier class="java.math.BigDecimal">46791</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$ads_uit_participant_types
AFTER DELETE ON uit_participant_types
DECLARE
  idx        BINARY_INTEGER := cg$uit_participant_types.cg$table.FIRST;
  cg$rec     cg$uit_participant_types.cg$row_type;
  cg$old_rec cg$uit_participant_types.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Delete-statement <<Start>>
  -- Application_logic Pre-After-Delete-statement << End >>

  IF NOT (cg$uit_participant_types.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.id := cg$uit_participant_types.cg$table(idx).id;
      cg$uit_participant_types.cg$tableind(idx).id := TRUE;

      cg$rec.rowversion := cg$uit_participant_types.cg$table(idx).rowversion;
      cg$uit_participant_types.cg$tableind(idx).rowversion := TRUE;

      cg$rec.created_by := cg$uit_participant_types.cg$table(idx).created_by;
      cg$uit_participant_types.cg$tableind(idx).created_by := TRUE;

      cg$rec.created_on := cg$uit_participant_types.cg$table(idx).created_on;
      cg$uit_participant_types.cg$tableind(idx).created_on := TRUE;

      cg$rec.updated_by := cg$uit_participant_types.cg$table(idx).updated_by;
      cg$uit_participant_types.cg$tableind(idx).updated_by := TRUE;

      cg$rec.updated_on := cg$uit_participant_types.cg$table(idx).updated_on;
      cg$uit_participant_types.cg$tableind(idx).updated_on := TRUE;

      cg$uit_participant_types.validate_foreign_keys_del(cg$rec);
      cg$uit_participant_types.cascade_delete(cg$rec);

      cg$rec.name := cg$uit_participant_types.cg$table(idx).name;
      cg$uit_participant_types.cg$tableind(idx).name := TRUE;

      idx := cg$uit_participant_types.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Delete-statement <<Start>>
  -- Application_logic Post-After-Delete-statement << End >>
END;
/
