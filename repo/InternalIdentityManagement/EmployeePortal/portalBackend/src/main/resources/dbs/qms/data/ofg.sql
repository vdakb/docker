Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Oracle Headstart messages.
Rem
Rem Usage Information:
Rem     sqlplus hcmowner/<password>
Rem     @<path>/messages_ofg
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2010-03-30  DSteding    First release version
Rem

PROMPT
PROMPT Create messages Oracle Forms Generator
BEGIN
  qms$message.delete_message('OFG-00003');
  --
  qms$message.create_message('OFG-00003'
                            ,'No row in table <p1>.'
                            ,'en'
                            ,'No row in table <p1>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00003'
                            ,'de'
                            ,'Keine Datensätze in Tabelle <p1>.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00004');
  --
  qms$message.create_message('OFG-00004'
                            ,'Cannot update <p1> while dependent <p2> exists.'
                            ,'en'
                            ,'Cannot update <p1> while dependent <p2> exists'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00004'
                            ,'de'
                            ,'<p1> kann nicht geändert werden, da Abhängigkeiten zu <p2> existieren.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00005');
  --
  qms$message.create_message('OFG-00005'
                            ,'Cannot delete <p1> while dependent <p2> exists.'
                            ,'en'
                            ,'Cannot delete <p1> while dependent <p2> exists.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00005'
                            ,'de'
                            ,'<p1> kann nicht gelöscht werden, da Abhängigkeiten zu <p2> existieren.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00006');
  --
  qms$message.create_message('OFG-00006'
                            ,'Row exists already with same <p1>.'
                            ,'en'
                            ,'Row exists already with same <p1>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00006'
                            ,'de'
                            ,'Datensatz mit gleichem <p1> existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00010');
  --
  qms$message.create_message('OFG-00010'
                            ,'Invalid value for <p1>.'
                            ,'en'
                            ,'Invalid value for <p1>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00010'
                            ,'de'
                            ,'Ungültiger Wert für <p1>.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00013');
  --
  qms$message.create_message('OFG-00013'
                            ,'This <p1> does not exist.'
                            ,'en'
                            ,'This <p1> does not exist.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00013'
                            ,'de'
                            ,'<p1> existiert nicht.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00014');
  --
  qms$message.create_message('OFG-00014'
                            ,'Invalid combination of <p1>.'
                            ,'en'
                            ,'Invalid combination of <p1>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00014'
                            ,'de'
                            ,' Kombination von <p1>.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00016');
  --
  qms$message.create_message('OFG-00016'
                            ,'Call to procedure <p1> failed.'
                            ,'en'
                            ,'Call to procedure <p1> failed.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00016'
                            ,'de'
                            ,'Ausführung der Prozedure <p1> ist fehlgeschlagen.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00017');
  --
  qms$message.create_message('OFG-00017'
                            ,'A reason for record deletion must be entered.'
                            ,'en'
                            ,'A reason for record deletion must be entered.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00017'
                            ,'de'
                            ,'Für das Löschen des Datensatzes muss ein Grund angegeben werden.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00018');
  --
  qms$message.create_message('OFG-00018'
                            ,'A reason for record creation must be entered.'
                            ,'en'
                            ,'A reason for record creation must be entered.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00018'
                            ,'de'
                            ,'Für das Anlegend des Datensatzes muss ein Grund angegeben werden.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00019');
  --
  qms$message.create_message('OFG-00019'
                            ,'A reason for record modification must be entered.'
                            ,'en'
                            ,'A reason for record modification must be entered.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00019'
                            ,'de'
                            ,'Für die Modifikation des Datensatzes muss ein Grund angegeben werden.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00020');
  --
  qms$message.create_message('OFG-00020'
                            ,'Derived item <p1> not updated.'
                            ,'en'
                            ,'Derived item <p1> not updated.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00020'
                            ,'de'
                            ,'Abgeleitetes Element <p1> wurde nicht geändert.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00021');
  --
  qms$message.create_message('OFG-00021'
                            ,'Value entered does not uniquely define a record.'
                            ,'en'
                            ,'Value entered does not uniquely define a record.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00021'
                            ,'de'
                            ,'Der eingegebene Wert identifiziert den Datensatz nicht eindeutig.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00022');
  --
  qms$message.create_message('OFG-00022'
                            ,'Press [Delete Record] again to confirm cascade delete.'
                            ,'en'
                            ,'Press [Delete Record] again to confirm cascade delete.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00022'
                            ,'de'
                            ,'Betätigen Sie [Delete Record], um das kaskadierende Löschen zu bestätigen.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00023');
  --
  qms$message.create_message('OFG-00023'
                            ,'Help file is missing or incorrect.'
                            ,'en'
                            ,'Help file is missing or incorrect.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00023'
                            ,'de'
                            ,'Die Hilfedatei ist inkorrekt oder kann nicht gefunden werden.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00024');
  --
  qms$message.create_message('OFG-00024'
                            ,'More than one row in table <p1>.'
                            ,'en'
                            ,'More than one row in table <p1>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00024'
                            ,'de'
                            ,'Mehr als ein Datensatz in Tabelle <p1>.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00029');
  --
  qms$message.create_message('OFG-00029'
                            ,'More than one code controls row for given domain.'
                            ,'en'
                            ,'More than one code controls row for given domain.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00029'
                            ,'de'
                            ,'Mehr als ein Datensatz in Steuertabelle für die geforderte Domäne.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00030');
  --
  qms$message.create_message('OFG-00030'
                            ,'Key not valid in this context.'
                            ,'en'
                            ,'Key not valid in this context.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00030'
                            ,'de'
                            ,'Schlüssel ist innerhalb dieses Kontexts nicht gültig.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00033');
  --
  qms$message.create_message('OFG-00033'
                            ,'Invalid query criteria on lookup items.'
                            ,'en'
                            ,'Invalid query criteria on lookup items.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00033'
                            ,'de'
                            ,'Ungültige Abfragekriterien für Lookup Elementen.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00034');
  --
  qms$message.create_message('OFG-00034'
                            ,'Invalid data in derived column <p1>.'
                            ,'en'
                            ,'Invalid data in derived column <p1>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00034'
                            ,'de'
                            ,'Ungültige Daten in abgeleiteter Spalte <p1>.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00035');
  --
  qms$message.create_message('OFG-00035'
                            ,'Query of <p1> must be in context of <p2>.'
                            ,'en'
                            ,'Query of <p1> must be in context of <p2>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00035'
                            ,'de'
                            ,'Abfrage von <p1> muss im Kontext von <p2> ausgeführt werden.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00036');
  --
  qms$message.create_message('OFG-00036'
                            ,'Insert of <p1> must be in context of <p2>.'
                            ,'en'
                            ,'Insert of <p1> must be in context of <p2>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00036'
                            ,'de'
                            ,'Anlegen von <p1> muss im Kontext von <p2> ausgeführt werden.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00037');
  --
  qms$message.create_message('OFG-00037'
                            ,'Query not allowed in this block.'
                            ,'en'
                            ,'Query not allowed in this block.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00037'
                            ,'de'
                            ,'Abfragen sind auf diesem Block nicht erlaubt.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00049');
  --
  qms$message.create_message('OFG-00049'
                            ,'More than one existing row with same key value.'
                            ,'en'
                            ,'More than one existing row with same key value.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00049'
                            ,'de'
                            ,'Es existiert mehr als ein Datensatz mit dem gleichen Schlüsselwerten.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00052');
  --
  qms$message.create_message('OFG-00052'
                            ,'Only one of <p1> may be entered.'
                            ,'en'
                            ,'Only one of <p1> may be entered.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00052'
                            ,'de'
                            ,'Nur ein Wert von <p1> darf eingegeben werden.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00053');
  --
  qms$message.create_message('OFG-00053'
                            ,'One of <p1> required.'
                            ,'en'
                            ,'One of <p1> required.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00053'
                            ,'de'
                            ,'Ein Wert aus <p1> ist erforderlich.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00056');
  --
  qms$message.create_message('OFG-00056'
                            ,'Summary data not accurate on restricted queries.'
                            ,'en'
                            ,'Summary data not accurate on restricted queries.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00056'
                            ,'de'
                            ,'Summary data not accurate on restricted queries.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00061');
  --
  qms$message.create_message('OFG-00061'
                            ,'More than one primary key row for foreign key value..'
                            ,'en'
                            ,'More than one primary key row for foreign key value.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00061'
                            ,'de'
                            ,'Mehr als ein Primärschlüssel für den Wert des Fremdschlüssel.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00062');
  --
  qms$message.create_message('OFG-00062'
                            ,'Cleared row no longer exists requery to display current rows..'
                            ,'en'
                            ,'Cleared row no longer exists requery to display current rows.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00062'
                            ,'de'
                            ,'Die geleerte Zeile existiert nicht länger. Um Sie erneut anzuzeigen muss der aktuelle Datensatz erneut abgefragt werden.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00063');
  --
  qms$message.create_message('OFG-00063'
                            ,'Unable to call list of values form <p1>.'
                            ,'en'
                            ,'Unable to call list of values form <p1>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00063'
                            ,'de'
                            ,'Die Werteliste <p1> kann nicht aufgerufen werden.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00067');
  --
  qms$message.create_message('OFG-00067'
                            ,'This form has outstanding changes to commit.'
                            ,'en'
                            ,'This form has outstanding changes to commit.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00067'
                            ,'de'
                            ,'Dieses Formular weißt Änderungen auf, die noch nicht gespeichert wurden.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00068');
  --
  qms$message.create_message('OFG-00068'
                            ,'Unable to call module <p1>.'
                            ,'en'
                            ,'Unable to call module <p1>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00068'
                            ,'de'
                            ,'Modul <p1> kann nicht aufgerufen werden.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00071');
  --
  qms$message.create_message('OFG-00071'
                            ,'Item is protected against update.'
                            ,'en'
                            ,'Item is protected against update.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00071'
                            ,'de'
                            ,'Element ist gegen Änderung geschützt.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00078');
  --
  qms$message.create_message('OFG-00078'
                            ,'<p1> must be entered.'
                            ,'en'
                            ,'<p1> must be entered.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00078'
                            ,'de'
                            ,'<p1> muss eingegeben werden.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00082');
  --
  qms$message.create_message('OFG-00082'
                            ,'<p1> on <p2> caused the following violation:'
                            ,'en'
                            ,'<p1> on <p2> caused the following violation:'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00082'
                            ,'de'
                            ,'<p1> auf <p2> verursachte folgende Verletzungen:'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00083');
  --
  qms$message.create_message('OFG-00083'
                            ,'Validation failed on <p1>.'
                            ,'en'
                            ,'Validation failed on <p1>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00083'
                            ,'de'
                            ,'Die Prüfung von <p1> ist fehlgeschlagen.'
                            ,''
                            );
  --
  qms$message.delete_message('OFG-00084');
  --
  qms$message.create_message('OFG-00084'
                            ,'Validation failed on constraint <p1>.'
                            ,'en'
                            ,'Validation failed on constraint <p1>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('OFG-00084'
                            ,'de'
                            ,'Die Prüfung des Constraints <p1> ist fehlgeschlagen.'
                            ,''
                            );
END;
/
COMMIT
/
