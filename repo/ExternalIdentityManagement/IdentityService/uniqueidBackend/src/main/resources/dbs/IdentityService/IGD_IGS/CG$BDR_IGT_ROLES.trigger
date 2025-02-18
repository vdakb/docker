-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BDR_IGT_ROLES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BDR_IGT_ROLES</name>
--         <identifier class="java.math.BigDecimal">46724</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bdr_igt_roles
BEFORE DELETE ON igt_roles FOR EACH ROW
DECLARE
  cg$pk  cg$igt_roles.cg$pk_type;
  cg$rec cg$igt_roles.cg$row_type;
  cg$ind cg$igt_roles.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Delete-row <<Start>>
  -- Application_logic Pre-Before-Delete-row << End >>

  -- Load cg$rec/cg$ind values from old
  cg$pk.id  := :old.id;
  cg$rec.id := :old.id;
  cg$igt_roles.cg$table(cg$igt_roles.idx).id := :old.id;

  cg$igt_roles.idx := cg$igt_roles.idx + 1;
  IF NOT (cg$igt_roles.called_from_package)
  THEN
    cg$igt_roles.del(cg$pk, FALSE);
    cg$igt_roles.called_from_package := FALSE;
  END IF;

  -- Application_logic Post-Before-Delete-row <<Start>>
  -- Application_logic Post-Before-Delete-row << End >>
END;
/
