Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Oracle Server Generator messages.
Rem
Rem Usage Information:
Rem     sqlplus hcmowner/<password>
Rem     @<path>/messages_osg
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2010-03-30  DSteding    First release version
Rem

PROMPT
PROMPT Create messages Oracle Server Generator
BEGIN
  qms$message.delete_message('OSG-01001');
  --
  qms$message.create_message('OSG-01001'
                            ,'Unique key <p1> not updateable.'
                            ,'en'
                            ,'Unique key <p1> not updateable.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OSG-01001'
                            ,'de'
                            ,'Eindeutiger Schlüssel <p1> kann nicht geändert werden.'
                            ,''
                            );
  --
  qms$message.delete_message('OSG-01002');
  --
  qms$message.create_message('OSG-01002'
                            ,'Foreign key <p1> not transferable.'
                            ,'en'
                            ,'Foreign key <p1> not transferable.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OSG-01002'
                            ,'de'
                            ,'Fremdschlüssel <p1> ist kann nicht weitergegeben werden.'
                            ,''
                            );
  --
  qms$message.delete_message('OSG-01003');
  --
  qms$message.create_message('OSG-01003'
                            ,'Update failed - please re-query as value for <p1> has been modified by another user<p2><p3>.'
                            ,'en'
                            ,'Update failed - please re-query as value for <p1> has been modified by another user<p2><p3>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OSG-01003'
                            ,'de'
                            ,'Änderung fehlgeschlagen - Es muss neu abgefragt werden, da die Werte von <p1> durch den Benutzer <p2><p3> geändert wurden.'
                            ,''
                            );
  --
  qms$message.delete_message('OSG-01004');
  --
  qms$message.create_message('OSG-01004'
                            ,'Check constraint <p1> on table <p2> violated.'
                            ,'en'
                            ,'Check constraint <p1> on table <p2> violated'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OSG-01004'
                            ,'de'
                            ,'Check Constraint <p1> auf Tabelle <p2> wurde verletzt.'
                            ,''
                            );
  --
  qms$message.delete_message('OSG-01005');
  --
  qms$message.create_message('OSG-01005'
                            ,'Foreign Key <p1> on table <p2> violated.'
                            ,'en'
                            ,'Foreign Key <p1> on table <p2> violated.'
                            ,'You have entered an invalid value for the foreign key. The parent record cannot be found.'
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OSG-01005'
                            ,'de'
                            ,'Fremdschlüssel <p1> auf Tabelle <p2> wurde verletzt.'
                            ,'Für den Fremdschlüssel wurde ein falscher Wert angegeben. Der Master Datensatz wurde nicht gefunden.'
                            );
  --
  qms$message.delete_message('OSG-01006');
  --
  qms$message.create_message('OSG-01006'
                            ,'Unique constraint <p1> on table <p2> violated.'
                            ,'en'
                            ,'Unique constraint <p1> on table <p2> violated.'
                            ,'You must enter a unique value for the unique constraint.'
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OSG-01006'
                            ,'de'
                            ,'Unique Constraint <p1> auf Tabelle <p2> wurde verletzt.'
                            ,'Es muss ein eindeutiger Wert zur Einhaltung dieses Constraints eingegeben werden.'
                            );
  --
  qms$message.delete_message('OSG-01007');
  --
  qms$message.create_message('OSG-01007'
                            ,'Primary key <p1> on table <p2> violated.'
                            ,'en'
                            ,'Primary key <p1> on table <p2> violated.'
                            ,'You must enter a unique value for the primary key.'
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OSG-01007'
                            ,'de'
                            ,'Primär Schlüssel <p1> auf Tabelle <p2> wurde verletzt.'
                            ,'Es muss ein eindeutiger Wert für den Primärschlüssel eingegeben werden.'
                            );
  --
  qms$message.delete_message('OSG-01009');
  --
  qms$message.create_message('OSG-01009'
                            ,'Mandatory Arc <p1> on <p2> has not been satisfied.'
                            ,'en'
                            ,'Mandatory Arc <p1> on <p2> has not been satisfied.'
                            ,'You must enter at least one of the members of Mandatory Arc <p1> on <p2>.'
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OSG-01009'
                            ,'de'
                            ,'Mandatory Arc <p1> on <p2> has not been satisfied.'
                            ,'Es muss mindestens eines der Bogen-Mitglieder <p1> auf <p2> eigegeben werden.'
                            );
  --
  qms$message.delete_message('OSG-01010');
  --
  qms$message.create_message('OSG-01010'
                            ,'Too many members in Arc <p1> on <p2> have been entered.'
                            ,'en'
                            ,'Too many members in Arc <p1> on <p2> have been entered.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OSG-01010'
                            ,'de'
                            ,'Too many members in Arc <p1> on <p2> have been entered.'
                            ,'Es darf nur eines der Mitglieder eines Bogens <p1> auf <p2> eingeben werden.'
                            );
  --
  qms$message.delete_message('OSG-01012');
  --
  qms$message.create_message('OSG-01012'
                            ,'Reference code table <p1> has not been created used for <p2>.'
                            ,'en'
                            ,'Reference code table <p1> has not been created used for <p2>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OSG-01012'
                            ,'de'
                            ,'Referenz Code Tabelle <p1> wurde für Benutzer <p2> nicht erzeugt.'
                            ,''
                            );
  --
  qms$message.delete_message('OSG-01013');
  --
  qms$message.create_message('OSG-01013'
                            ,'Invalid value <p1> for column <p2>.<p3>.'
                            ,'en'
                            ,'Invalid value <p1> for column <p2>.<p3>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OSG-01013'
                            ,'de'
                            ,'Ungültiger Wert <p1> für Spalte <p2>.<p3>.'
                            ,''
                            );
  --
  qms$message.delete_message('OSG-01014');
  --
  qms$message.create_message('OSG-01014'
                            ,'Invalid value <p1> in domain <p2> for column <p3>.<p4>.'
                            ,'en'
                            ,'Invalid value <p1> in domain <p2> for column <p3>.<p4>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OSG-01014'
                            ,'de'
                            ,'Ungültiger Wert <p1> in Domain <p2> für Spalte <p3>.<p4>.'
                            ,''
                            );
  --
  qms$message.delete_message('OSG-01015');
  --
  qms$message.create_message('OSG-01015'
                            ,'Control table sequence value <p1> is being used by another user.'
                            ,'en'
                            ,'Control table sequence value <p1> is being used by another user.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OSG-01015'
                            ,'de'
                            ,'Control table sequence value <p1> is being used by another user.'
                            ,''
                            );
  --
  qms$message.delete_message('OSG-01016');
  --
  qms$message.create_message('OSG-01016'
                            ,'Value required for <p1> foreign key.'
                            ,'en'
                            ,'Value required for <p1> foreign key.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OSG-01016'
                            ,'de'
                            ,'Für den Fremdschlüssel <p1> ist ein Wert notwendig.'
                            ,''
                            );
  --
  qms$message.delete_message('OSG-01017');
  --
  qms$message.create_message('OSG-01017'
                            ,'Error in Cascade <p1>.'
                            ,'en'
                            ,'Error in Cascade <p1>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OSG-01017'
                            ,'de'
                            ,'Fehler in Kaskade <p1>.'
                            ,''
                            );
  --
  qms$message.delete_message('OSG-01101');
  --
  qms$message.create_message('OSG-01101'
                            ,'Row is locked by another user.'
                            ,'en'
                            ,'Row is locked by another user.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OSG-01101'
                            ,'de'
                            ,'Datensatz wurde durch einen anderen Benutzer gesperrt.'
                            ,''
                            );
  --
  qms$message.delete_message('OSG-01102');
  --
  qms$message.create_message('OSG-01102'
                            ,'Row no longer exists.'
                            ,'en'
                            ,'Row no longer exists.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OSG-01102'
                            ,'de'
                            ,'Datensatz existiert nicht mehr.'
                            ,''
                            );
  --
  qms$message.delete_message('OSG-01201');
  --
  qms$message.create_message('OSG-01201'
                            ,'Invalid values in domain constraint <p1> referring domain table <p2> for table <p3>.'
                            ,'en'
                            ,'Invalid values in domain constraint <p1> referring domain table <p2> for table <p3>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OSG-01201'
                            ,'de'
                            ,'Ungültige Werte in Domain Constraint <p1> verweisend auf Domain Tabelle <p2> für Tabelle <p3>.'
                            ,''
                            );
END;
/
COMMIT
/
