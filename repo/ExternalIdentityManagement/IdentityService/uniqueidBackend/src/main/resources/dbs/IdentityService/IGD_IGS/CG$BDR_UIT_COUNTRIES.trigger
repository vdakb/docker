-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BDR_UIT_COUNTRIES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BDR_UIT_COUNTRIES</name>
--         <identifier class="java.math.BigDecimal">46799</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bdr_uit_countries
BEFORE DELETE ON uit_countries FOR EACH ROW
DECLARE
  cg$pk  cg$uit_countries.cg$pk_type;
  cg$rec cg$uit_countries.cg$row_type;
  cg$ind cg$uit_countries.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Delete-row <<Start>>
  -- Application_logic Pre-Before-Delete-row << End >>

  -- Load cg$rec/cg$ind values from old
  cg$pk.id  := :old.id;
  cg$rec.id := :old.id;
  cg$uit_countries.cg$table(cg$uit_countries.idx).id := :old.id;

  cg$uit_countries.idx := cg$uit_countries.idx + 1;
  IF NOT (cg$uit_countries.called_from_package)
  THEN
    cg$uit_countries.del(cg$pk, FALSE);
    cg$uit_countries.called_from_package := FALSE;
  END IF;

  -- Application_logic Post-Before-Delete-row <<Start>>
  -- Application_logic Post-Before-Delete-row << End >>
END;
/
