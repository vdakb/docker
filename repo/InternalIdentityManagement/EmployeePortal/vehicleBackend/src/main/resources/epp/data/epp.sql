Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Employee Portal messages.
Rem
Rem Usage Information:
Rem     sqlplus hcmowner/<password>
Rem     @<path>/messages_qms
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0    2020-03-30  DSteding    First release version
Rem

PROMPT
PROMPT Create messages for Employee Portal Business Rules
BEGIN
  qms$message.delete_message('EPP-00001');
  --
  qms$message.create_message('EPP-00001'
                            ,'User does not exists on system Employeee Portal.'
                            ,'en'
                            ,'User <p1> does not exists on system HCM.'
                            ,''
                            );
  --
  qms$message.add_language  ('EPP-00001'
                            ,'de'
                            ,'Benutzer <p1> existiert nicht im System Mitarbeiter Portal.'
                            ,''
                            );
  --
  qms$message.delete_message('EPP-01001');
  --
  qms$message.create_message('EPP-01001'
                            ,'User has no permissions to change data.'
                            ,'en'
                            ,'User has no permissions to change or create data.'
                           || ' [<p1> , <p2> , <p3>]'
                            ,''
                            );
  qms$message.add_language  ('EPP-01001'
                            ,'de'
                            ,'Benutzer hat keine Berechtigung Datenänderungen vorzunehmen.'
                           || ' [<p1> , <p2> , <p3>]'
                            ,''
                            );
  --
  qms$message.delete_message('EPP-01002');
  --
  qms$message.create_message('EPP-01002'
                            ,'Record has been changed by another user.'
                            ,'en'
                            ,'Record has been changed by another user. Requery to see change.'
                            || ' [<p1> : <p2>]'
                            ,''
                            );
  qms$message.add_language  ('EPP-01002'
                            ,'de'
                            ,'Datensatz wurde durch anderen Benutzer geändert. Bitte erneut abfragen.'
                            || ' [<p1> : <p2>]'
                            ,''
                            );
END;
/
COMMIT
/