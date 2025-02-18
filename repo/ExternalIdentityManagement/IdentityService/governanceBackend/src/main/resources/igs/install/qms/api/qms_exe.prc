-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\qms_exe.prc
--
-- Generated for Oracle 10g on Tue Mar 30 08:58:19 2010 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Procedure 'qms$execute'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE PROCEDURE qms$execute(p_command in varchar2)
IS
BEGIN
  -- performance enhancement: use NDS instead-of dbms_sql
  EXECUTE IMMEDIATE p_command;
END qms$execute;
/
SHOW ERROR