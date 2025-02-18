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
Rem     sqlplus igd_igs/<password>
Rem     @<path>/qms
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Create Oracle Headstart Message Properties
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
BEGIN
  qms$message.delete_message('QMS-00001');
  --
  qms$message.create_message('QMS-00001'
                            ,'Message code already exists.'
                            ,'en'
                            ,'Message code already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_MSP_PK'
                            );
  qms$message.add_language  ('QMS-00001'
                            ,'de'
                            ,'Message-Code existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00009');
  --
  qms$message.create_message('QMS-00009'
                            ,'Reference to unknown parameter found.'
                            ,'en'
                            ,'Reference to unknown parameter found.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00009'
                            ,'de'
                            ,'Es wurde eine Referenz auf einen unbekannten Parameter gefunden.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00014');
  --
  qms$message.create_message('QMS-00014'
                            ,'Do you want to exit the form ?'
                            ,'en'
                            ,'Do you want to exit the form ?'
                            ,'You cancelled the query. To be able to work in this form you must perform a query first'
                            ,p_constraint_name => ''
                            ,p_severity => 'Q'
                            );
  qms$message.add_language  ('QMS-00014'
                            ,'de'
                            ,'Soll das Formular beendet werden ?'
                            ,'Die Abfrage wurde abgebrochen. Um die Fortsetzung der Arbeit in diesem Formular zu gewährleisten muss zunächst eine Abfrage ausgeführt werden.'
                            );
  --
  qms$message.delete_message('QMS-00033');
  --
  qms$message.create_message('QMS-00033'
                            ,'Parameter <p1>  is mandatory.'
                            ,'en'
                            ,'Parameter <p1>  is mandatory.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00033'
                            ,'de'
                            ,'Der Parameter <p1> muss eingegeben werden.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00034');
  --
  qms$message.create_message('QMS-00034'
                            ,'Incorrect date, must be of format <p1>.'
                            ,'en'
                            ,'Incorrect date, must be of format <p1>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00034'
                            ,'de'
                            ,'Ungültiges Datum, es muss im Format <p1> angegeben werden.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00035');
  --
  qms$message.create_message('QMS-00035'
                            ,'No more than <p1> characters allowed for this parameter.'
                            ,'en'
                            ,'No more than <p1> characters allowed for this parameter.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00035'
                            ,'de'
                            ,'Für diesen Parameter sind  nicht mehr als <p1> Zeichen zulässig.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00036');
  --
  qms$message.create_message('QMS-00036'
                            ,'Parameter must be numeric.'
                            ,'en'
                            ,'Parameter must be numeric.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00036'
                            ,'de'
                            ,'Der Parameter muss numerisch sein.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00037');
  --
  qms$message.create_message('QMS-00037'
                            ,'No quotes allowed in value.'
                            ,'en'
                            ,'No quotes allowed in value.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00037'
                            ,'de'
                            ,'Es sind keine Anführungszeichen innerhalb des Wertes zulässig.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00038');
  --
  qms$message.create_message('QMS-00038'
                            ,'Invalid value for parameter <p1>.'
                            ,'en'
                            ,'Invalid value for parameter <p1>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00038'
                            ,'de'
                            ,'Ungültiger Wert für Parameter <p1>.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00039');
  --
  qms$message.create_message('QMS-00039'
                            ,'Assign value to parameter <p1> first.'
                            ,'en'
                            ,'Assign value to parameter <p1> first.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00039'
                            ,'de'
                            ,'Dem Parameter <p1> muss zuerst ein Wert zugewiesen werden.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00040');
  --
  qms$message.create_message('QMS-00040'
                            ,'List of values contains no entries.'
                            ,'en'
                            ,'List of values contains no entries.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00040'
                            ,'de'
                            ,'Die Werteliste beinhaltet keine Einträge.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00041');
  --
  qms$message.create_message('QMS-00041'
                            ,'List of Values query is not of the correct syntax.'
                            ,'en'
                            ,'List of Values query is not of the correct syntax.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00041'
                            ,'de'
                            ,'Die Abfrage der Werteliste weist einen Syntaxfehler auf.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00042');
  --
  qms$message.create_message('QMS-00042'
                            ,'At last parameter.'
                            ,'en'
                            ,'At last parameter.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00042'
                            ,'de'
                            ,'Beim letzten Parameter.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00043');
  --
  qms$message.create_message('QMS-00043'
                            ,'Module with this short name already exists.'
                            ,'en'
                            ,'Module with this short name already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_MDE_UK1'
                            );
  qms$message.add_language  ('QMS-00043'
                            ,'de'
                            ,'Module mit diesem Kurznamen existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00044');
  --
  qms$message.create_message('QMS-00044'
                            ,'Parameter length only needed for datatypes other then date(time).'
                            ,'en'
                            ,'Parameter length only needed for datatypes other then date(time).'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00044'
                            ,'de'
                            ,'Eine Parameterlänge ist nur andere Datentypen als Datum notwendig.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00048');
  --
  qms$message.create_message('QMS-00048'
                            ,'Module parameter with this name already exist.'
                            ,'en'
                            ,'Module parameter with this name already exist.'
                            ,''
                            ,p_constraint_name => 'QMS_MPM_UK1'
                            );
  qms$message.add_language  ('QMS-00048'
                            ,'de'
                            ,'Module Parameter mit diesem Namen existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00064');
  --
  qms$message.create_message('QMS-00064'
                            ,'Module with this ID does not exist.'
                            ,'en'
                            ,'Module with this ID does not exist.'
                            ,''
                            ,p_constraint_name => 'QMS_MPM_MDE_FK1'
                            );
  qms$message.add_language  ('QMS-00064'
                            ,'de'
                            ,'Module mit dieser ID existiert nicht.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00065');
  --
  qms$message.create_message('QMS-00065'
                            ,'Module parameter with this ID already exist.'
                            ,'en'
                            ,'Module parameter with this ID already exist.'
                            ,''
                            ,p_constraint_name => 'QMS_MPM_PK'
                            );
  qms$message.add_language  ('QMS-00065'
                            ,'de'
                            ,'Module Parameter mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00066');
  --
  qms$message.create_message('QMS-00066'
                            ,'Standard report parameter ID already exists.'
                            ,'en'
                            ,'Standard report parameter ID already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_SRP_PK'
                            );
  qms$message.add_language  ('QMS-00066'
                            ,'de'
                            ,'Standard Reportparameter mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00067');
  --
  qms$message.create_message('QMS-00067'
                            ,'Standard report parameter name already exists.'
                            ,'en'
                            ,'Standard report parameter name already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_SRP_UK1'
                            );
  qms$message.add_language  ('QMS-00067'
                            ,'de'
                            ,'Standard Reportparameter mit diesem Namen existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00070');
  --
  qms$message.create_message('QMS-00070'
                            ,'Auto query mode switched off.'
                            ,'en'
                            ,'Auto query mode switched off.'
                            ,''
                            ,p_constraint_name => ''
                            ,p_severity => 'I'
                            );
  qms$message.add_language  ('QMS-00070'
                            ,'de'
                            ,'Automatischer Abfragemodus wurde ausgeschaltet.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00071');
  --
  qms$message.create_message('QMS-00071'
                            ,'Auto query mode switched on.'
                            ,'en'
                            ,'Auto query mode switched on.'
                            ,''
                            ,p_constraint_name => ''
                            ,p_severity => 'I'
                            );
  qms$message.add_language  ('QMS-00071'
                            ,'de'
                            ,'Automatischer Abfragemodus wurde eingeschaltet.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00075');
  --
  qms$message.create_message('QMS-00075'
                            ,'Module with this ID already exists.'
                            ,'en'
                            ,'Module with this ID already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_MDE_PK'
                            );
  qms$message.add_language  ('QMS-00075'
                            ,'de'
                            ,'Module mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00077');
  --
  qms$message.create_message('QMS-00077'
                            ,'Parameter length is required for this datatype.'
                            ,'en'
                            ,'Parameter length is required for this datatype.'
                            ,''
                            ,p_constraint_name => 'QMS_MPM_CK2'
                            );
  qms$message.add_language  ('QMS-00077'
                            ,'de'
                            ,'Die Angabe einer Länge für diesen Datentyp ist erforderlich.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00078');
  --
  qms$message.create_message('QMS-00078'
                            ,'Message code already exists for this language.'
                            ,'en'
                            ,'Message code already exists for this language.'
                            ,''
                            ,p_constraint_name => 'QMS_MST_PK'
                            );
  qms$message.add_language  ('QMS-00078'
                            ,'de'
                            ,'Message-Code existiert bereits in dieser Sprache.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00079');
  --
  qms$message.create_message('QMS-00079'
                            ,'Message code does not exist.'
                            ,'en'
                            ,'Message code does not exist.'
                            ,''
                            ,p_constraint_name => 'QMS_MST_MSP_FK1'
                            );
  qms$message.add_language  ('QMS-00079'
                            ,'de'
                            ,'Message-Code existiert nicht.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00080');
  --
  qms$message.create_message('QMS-00080'
                            ,'Option not available for HTML help.'
                            ,'en'
                            ,'Option not available for HTML help.'
                            ,''
                            ,p_constraint_name => ''
                            ,p_severity => 'I'
                            );
  qms$message.add_language  ('QMS-00080'
                            ,'de'
                            ,'Diese Option ist für dei HTML-Hilfe nicht verfügbar.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00081');
  --
  qms$message.create_message('QMS-00081'
                            ,'Auditing information only available for existing records.'
                            ,'en'
                            ,'Auditing information only available for existing records.'
                            ,''
                            ,p_constraint_name => ''
                            ,p_severity => 'I'
                            );
  qms$message.add_language  ('QMS-00081'
                            ,'de'
                            ,'Für diesen Datensatz existieren keine Audit-Informationen.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00082');
  --
  qms$message.create_message('QMS-00082'
                            ,'No records selected to perform operation on.'
                            ,'en'
                            ,'No records selected to perform operation on.'
                            ,'Clicking on a record will select the record. Pressing the CTRL-key or SHIFT-key while clicking, allows you to select'||chr(10)||
                             'multiple records.'
                            ,p_constraint_name => ''
                            ,p_severity => 'I'
                            );
  qms$message.add_language  ('QMS-00082'
                            ,'de'
                            ,'Es wurden keine Datensätze zur Ausführung der Operation ausgewählt.'
                            ,'Durch den Klick auf einen Datensatz wird dieser ausgewählt. Durch Drücken der STRG- oder SHIFT-Taste während der Klicks,'||CHR(10)||
                             'besteht die Möglichkeit der Auswahl mehrerer Datensätze.'
                            );
  --
  qms$message.delete_message('QMS-00083');
  --
  qms$message.create_message('QMS-00083'
                            ,'Cannot move selected record with outstanding changes.'
                            ,'en'
                            ,'Cannot move selected record with outstanding changes.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00083'
                            ,'de'
                            ,'Der Datensatz kann auf Grund nicht gespeicherter Änderungen verschoben werden.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00084');
  --
  qms$message.create_message('QMS-00084'
                            ,'Length must be empty for date parameter.'
                            ,'en'
                            ,'Length must be empty for date parameter.'
                            ,''
                            ,p_constraint_name => 'QMS_MPM_CK1'
                            );
  qms$message.add_language  ('QMS-00084'
                            ,'de'
                            ,'Längenangabe für Datumsinformationen muss leer sein.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00085');
  --
  qms$message.create_message('QMS-00085'
                            ,'No about this record information available.'
                            ,'en'
                            ,'No about this record information available.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00085'
                            ,'de'
                            ,'Keine Datensatzinformationen verfügbar.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00086');
  --
  qms$message.create_message('QMS-00086'
                            ,'Format must be empty for date parameter.'
                            ,'en'
                            ,'Format must be empty for date parameter.'
                            ,'The date format is picked up from an environment variable and should not be specified for a date parameter.'
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00086'
                            ,'de'
                            ,'Format für Datumsangaben muss leer sein.'
                            ,'Das Datumsformat wird aus Umgebungsvariablen entnommen und sollte für einen derartigen Parameter nicht angegeben werden.'
                            );
  --
  qms$message.delete_message('QMS-00087');
  --
  qms$message.create_message('QMS-00087'
                            ,'Message with this constraint name already exists.'
                            ,'en'
                            ,'Message with this constraint name already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_MSP_UK1'
                            );
  qms$message.add_language  ('QMS-00087'
                            ,'de'
                            ,'Message für diesen Constraint-Namen existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00088');
  --
  qms$message.create_message('QMS-00088'
                            ,'Connection changed succesfully, press Cancel on login dialog appearing after this message.'
                            ,'en'
                            ,'Connection changed succesfully, press Cancel on login dialog appearing after this message.'
                            ,''
                            ,p_constraint_name => ''
                            ,p_severity => 'I'
                            );
  qms$message.add_language  ('QMS-00088'
                            ,'de'
                            ,'Verbindung wurde erfolgreich geändert, betätigen Sie im Dialogfeld Abbrechen nach Erscheinen dieser Meldung.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00100');
  --
  qms$message.create_message('QMS-00100'
                            ,'Error message for unhandled exceptions.'
                            ,'en'
                            ,'Unhandled Exception <p1> in PL/SQL Program Unit <p2>.'||chr(10)||'Contact Helpdesk.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00100'
                            ,'de'
                            ,'Unbehandelte Ausnahme <p1> in PL/SQL Programmeinheit <p2>.'||chr(10)||'Informieren Sie Ihren Helpdesk.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00101');
  --
  qms$message.create_message('QMS-00101'
                            ,'Error message language parameter not set.'
                            ,'en'
                            ,'Internal Error : Language parameter not set for user.'||chr(10)||'Contact Helpdesk'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00101'
                            ,'de'
                            ,'Interner Fehler : Der Sprachparameter ist für den Benutzer nicht gesetzt.'||chr(10)||'Informieren Sie Ihren Helpdesk.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00102');
  --
  qms$message.create_message('QMS-00102'
                            ,'Error message not found in a language.'
                            ,'en'
                            ,'Internal Error : Error message <p1> not found in language <p2>.'||chr(10)||'Contact Helpdesk.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00102'
                            ,'de'
                            ,'Interner Fehler : Die Fehlermeldung <p1> konnte für die Sprache <p2> nicht gefunden werden.'||chr(10)||'Informieren Sie Ihren Helpdesk.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00103');
  --
  qms$message.create_message('QMS-00103'
                            ,'An old password must be entered.'
                            ,'en'
                            ,'An old password must be entered.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00103'
                            ,'de'
                            ,'Das bestehende Passwort muss eingegeben werden.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00104');
  --
  qms$message.create_message('QMS-00104'
                            ,'A new password must be entered.'
                            ,'en'
                            ,'A new password must be entered.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00104'
                            ,'de'
                            ,'Ein neues Passwort muss eingegeben werden.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00105');
  --
  qms$message.create_message('QMS-00105'
                            ,'Password confirmation must be entered.'
                            ,'en'
                            ,'Password confirmation must be entered.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00105'
                            ,'de'
                            ,'Die Passwortbestätigung muss eingegeben werden.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00106');
  --
  qms$message.create_message('QMS-00106'
                            ,'The old password entered is wrong.'
                            ,'en'
                            ,'The old password entered is wrong.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00106'
                            ,'de'
                            ,'Die Eingabe des alten Passworts ist falsch.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00107');
  --
  qms$message.create_message('QMS-00107'
                            ,'Old and new password cannot be identical.'
                            ,'en'
                            ,'Old and new password cannot be identical.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00107'
                            ,'de'
                            ,'Altes und neues Passwort dürfen nicht identisch sein.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00108');
  --
  qms$message.create_message('QMS-00108'
                            ,'New password and confirmation must be identical.'
                            ,'en'
                            ,'New password and confirmation must be identical.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00108'
                            ,'de'
                            ,'Neues Passwort und Passwortbestätigung müssen identisch sein.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00109');
  --
  qms$message.create_message('QMS-00109'
                            ,'New password cannot be the same as the user name.'
                            ,'en'
                            ,'New password cannot be the same as the user name.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00109'
                            ,'de'
                            ,'Neues Passwort und Benutzername dürfen nicht identisch sein.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00110');
  --
  qms$message.create_message('QMS-00110'
                            ,'New password must contain at least one numeric character.'
                            ,'en'
                            ,'New password must contain at least one numeric character.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00110'
                            ,'de'
                            ,'Neues Passwort muss mindestens ein numerisches Zeichen beinhalten.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00111');
  --
  qms$message.create_message('QMS-00111'
                            ,'New password must be longer than 5 characters.'
                            ,'en'
                            ,'New password must be longer than 5 characters.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00111'
                            ,'de'
                            ,'Neues Passwort muss länger als 5 Zeichen sein.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00112');
  --
  qms$message.create_message('QMS-00112'
                            ,'New password cannot (partly) consist of the user name.'
                            ,'en'
                            ,'New password cannot (partly) consist of the user name.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00112'
                            ,'de'
                            ,'Neues Passwort darf nicht ganz oder teilweise aus dem Benutzernamen bestehen.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00113');
  --
  qms$message.create_message('QMS-00113'
                            ,'New password cannot (partly) consist of the old password.'
                            ,'en'
                            ,'New password cannot (partly) consist of the old password.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00113'
                            ,'de'
                            ,'Neues Passwort darf nicht ganz oder teilweise aus alten Passwort bestehen.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00114');
  --
  qms$message.create_message('QMS-00114'
                            ,'New password cannot (partly) consist of the name of a month.'
                            ,'en'
                            ,'New password cannot (partly) consist of the name of a month.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00114'
                            ,'de'
                            ,'Neues Passwort darf nicht ganz oder teilweise aus einem Monatsnamen bestehen.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00115');
  --
  qms$message.create_message('QMS-00115'
                            ,'New password cannot (partly) consist of the name of a day.'
                            ,'en'
                            ,'New password cannot (partly) consist of the name of a day.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00115'
                            ,'de'
                            ,'Neues Passwort darf nicht ganz oder teilweise aus dem Namen eines Tages bestehen.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00116');
  --
  qms$message.create_message('QMS-00116'
                            ,'New password cannot (partly) consist of one of the "common" words.'
                            ,'en'
                            ,'New password cannot (partly) consist of one of the "common" words.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00116'
                            ,'de'
                            ,'Neues Passwort darf nicht ganz oder teilweise aus "bekannten" Begriffen bestehen.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00117');
  --
  qms$message.create_message('QMS-00117'
                            ,'New password cannot contain the same character 2 times without another character between.'
                            ,'en'
                            ,'New password cannot contain the same character 2 times without another character between.'
                            ,'Example: the password ABCDEE1 is not valid, ABCDE1E is.'
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00117'
                            ,'de'
                            ,'New password cannot contain the same character 2 times without another character between.'
                            ,'Example: the password ABCDEE1 is not valid, ABCDE1E is.'
                            );
  --
  qms$message.delete_message('QMS-00118');
  --
  qms$message.create_message('QMS-00118'
                            ,'New password cannot have a character in the same position as it was in the old password.'
                            ,'en'
                            ,'New password cannot have a character in the same position as it was in the old password.'
                            ,'Example: Old pwd = SMITH123, New pwd = CLIFF456 is not valid, because the character ''I'' is in the same position.'
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00118'
                            ,'de'
                            ,'New password cannot have a character in the same position as it was in the old password.'
                            ,'Example: Old pwd = SMITH123, New pwd = CLIFF456 is not valid, because the character ''I'' is in the same position.'
                            );
  --
  qms$message.delete_message('QMS-00119');
  --
  qms$message.create_message('QMS-00119'
                            ,'Password has been changed.'
                            ,'en'
                            ,'Password has been changed.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00119'
                            ,'de'
                            ,'Passwort wurde geändert.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00120');
  --
  qms$message.create_message('QMS-00120'
                            ,'Password has NOT been changed.'
                            ,'en'
                            ,'Password has NOT been changed.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00120'
                            ,'de'
                            ,'Passwort wurde NICHT geändert.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00121');
  --
  qms$message.create_message('QMS-00121'
                            ,'Close all forms before changing the password.'
                            ,'en'
                            ,'Close all open forms before changing the password.'
                            ,'You can use the Close All option under the File menu to accomplish this task quickly'
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00121'
                            ,'de'
                            ,'Um das Passwort ändern zu können, müssen alle offenen Formulare geschlossen werden.'
                            ,'Über die Option Alle Schließen im Dateimenu kann diese Forderung sehr schnell erfüllt werden.'
                            );
  --
  qms$message.delete_message('QMS-00122');
  --
  qms$message.create_message('QMS-00122'
                            ,'Close all open forms before changing the connection.'
                            ,'en'
                            ,'Close all forms before changing the connection.'
                            ,'You can use the Close All option under the File menu to accomplish this task quickly'
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00122'
                            ,'de'
                            ,'Um die Verbindung ändern zu können, müssen alle offenen Formulare geschlossen werden.'
                            ,'Über die Option Alle Schließen im Dateimenu kann diese Forderung sehr schnell erfüllt werden.'
                            );
  --
  qms$message.delete_message('QMS-00123');
  --
  qms$message.create_message('QMS-00123'
                            ,'Cannot move record again.'
                            ,'en'
                            ,'Cannot move record again, requery the record first.'
                            ,'You have moved this record back and forth. And now you want to move it again, looks like you have no idea what you are doing. Anyway, you will have to requery the record to be able to move it again.'
                            ,p_constraint_name => ''
                            ,p_severity => 'I'
                            );
  qms$message.add_language  ('QMS-00123'
                            ,'de'
                            ,'Der Datensatz kann nicht erneut verschoben werden.'
                            ,'Dieser Datensatz wurde mehrfach vor und zurück verschoben. Nun soll er erneut verschoben werden. Es hat den Anschein, dass Sie kein Idee haben, was Sie damit anfangen sollen. Sei es wie es sei, die Datensätze müssen neu abgefragt werden, damit ein erneutes Verschieben stattfinden kann.'
                            );
  --
  qms$message.delete_message('QMS-00124');
  --
  qms$message.create_message('QMS-00124'
                            ,'Message format does not comply to CDM standards.'
                            ,'en'
                            ,'Message format does not comply to CDM standards.'
                            ,'The message code you entered does not comply with CDM standard OMG-10040, which states it should consist of 3 letters, a dash, and 5 numbers (for instance "QMS-00100").'||chr(10)
                             ||chr(10)||
                             'If you want to deviate from this standard, you will have to modify function CheckErrorFormat and/or public variable c_errorformat in package QMS$Errors in the Headstart core library, otherwise the message will not be shown when it''s raised in a Headstart generated form.'
                            ,p_constraint_name => ''
                            ,p_severity => 'W'
                            );
  qms$message.add_language  ('QMS-00124'
                            ,'de'
                            ,'Das Message-Format entspricht nicht den CDM-Standards.'
                            ,'Der eingebene Message Code entspricht nicht den CDM-Standards nach OMG-10040. In diesem Guide ist festgelegt, dass ein Message Code aus 3 Buchstaben, einem Bindestrich und 5 Ziffern besteht (zum Beispiel "QMS-00100").'||chr(10)
                             ||chr(10)||
                             'Wenn von diesem Standard abgewichen wird, so muss die Funktion CheckErrorFormat und/oder die öffentliche Variable c_errorformat im Package qms$errors der Headstart Cora Library geändert werden, anderenfalls, wird die Message nicht angezeigt, wenn sie in einer mit Headstart generierten Form ausgelöst wird.'
                            );
  --
  qms$message.delete_message('QMS-00125');
  --
  qms$message.create_message('QMS-00125'
                            ,'Debugging has been turned off.'
                            ,'en'
                            ,'Debugging has been turned off.'
                            ,''
                            ,p_constraint_name => ''
                            ,p_severity => 'I'
                            );
  qms$message.add_language  ('QMS-00125'
                            ,'de'
                            ,'Debugging wurde ausgeschaltet.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00150');
  --
  qms$message.create_message('QMS-00150'
                            ,'You must select a message first.'
                            ,'en'
                            ,'You must select a message first.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00150'
                            ,'de'
                            ,'Es muss erst eine Message ausgewählt werden.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00151');
  --
  qms$message.create_message('QMS-00151'
                            ,'Dummy message for Change Event business rules.'
                            ,'en'
                            ,'Change Event business rules'
                            ,'This message will never be raised, because Change Event rules are never violated. However, CDM RuleFrame requires a message for each business rule, so that is why we need this dummy message.'
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00151'
                            ,'de'
                            ,'Change Event business rules'
                            ,'Diese Message wird niemals erscheinen, da Change Event Regeln niemals verletzt werden können. Wie auch immer CDM RuleFrame erfordert eine Message Code für jede Geschäftsregel, weshalb wir diese hier benötigen.'
                            );
  --
  qms$message.delete_message('QMS-00152');
  --
  qms$message.create_message('QMS-00152'
                            ,'Dummy message for word ''Error'' in multiple languages'
                            ,'en'
                            ,'Error'
                            ,''
                            ,p_constraint_name => ''
                            ,p_severity => 'I'
                            );
  qms$message.add_language  ('QMS-00152'
                            ,'de'
                            ,'Fehler'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00153');
  --
  qms$message.create_message('QMS-00153'
                            ,'Dummy message for word ''Warning'' in multiple languages'
                            ,'en'
                            ,'Warning'
                            ,''
                            ,p_constraint_name => ''
                            ,p_severity => 'I'
                            );
  qms$message.add_language  ('QMS-00153'
                            ,'de'
                            ,'Warnung'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00154');
  --
  qms$message.create_message('QMS-00154'
                            ,'Dummy message for word ''Information'' in multiple languages'
                            ,'en'
                            ,'Information'
                            ,''
                            ,p_constraint_name => ''
                            ,p_severity => 'I'
                            );
  qms$message.add_language  ('QMS-00154'
                            ,'de'
                            ,'Information'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00155');
  --
  qms$message.create_message('QMS-00155'
                            ,'Dummy message for word ''Question'' in multiple languages'
                            ,'en'
                            ,'Question'
                            ,''
                            ,p_constraint_name => ''
                            ,p_severity => 'I'
                            );
  qms$message.add_language  ('QMS-00155'
                            ,'de'
                            ,'Frage'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00156');
  --
  qms$message.create_message('QMS-00156'
                            ,'Dummy message for word ''Message'' in multiple languages'
                            ,'en'
                            ,'Message'
                            ,''
                            ,p_constraint_name => ''
                            ,p_severity => 'I'
                            );
  qms$message.add_language  ('QMS-00156'
                            ,'de'
                            ,'Message'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00157');
  --
  qms$message.create_message('QMS-00157'
                            ,'Only User Parameters can be defined as Multi-Select parameters.'
                            ,'en'
                            ,'Only User Parameters can be defined as Multi-Select parameters.'
                            ,'System parameters cannot be defined as Multi-Select parameters. '
                           ||'They are predefined report parameters, and Oracle Reports '
                           ||'does not provide a mechanism for handling multiple values '
                           ||'in these parameters.'
                            ,p_constraint_name => 'QMS_MPM_CK3'
                            );
  qms$message.add_language  ('QMS-00157'
                            ,'de'
                            ,'Nur benutzerdefinierte Parameter können als Parameter mit Mehrfachauswahl definiert werden.'
                            ,'System Parameter können nicht als Parameter mit Mehrfachauswahl definiert. '
                           ||'Es existieren vordefinierte Parameter, für die Oracle Reports keinen '
                           ||'Mechanismus anbietet, um Mehrfach-Werte in diesen Parametern zu behandeln.'
                            );
  --
  qms$message.delete_message('QMS-00158');
  --
  qms$message.create_message('QMS-00158'
                            ,'Do you want to save your changes ?'
                            ,'en'
                            ,'Do you want to save your changes ?'
                            ,''
                            ,p_constraint_name => ''
                            ,p_severity => 'Q'
                            );
  qms$message.add_language  ('QMS-00158'
                            ,'de'
                            ,'Sollen die Änderungen gespeichert werden ?'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00159');
  --
  qms$message.create_message('QMS-00159'
                            ,'Cannot delete Modules row, matching Parameters found.'
                            ,'en'
                            ,'Cannot delete Modules row, matching Parameters found.'
                            ,''
                            ,p_constraint_name => 'QMS_MODULES_QMS_MDE_PARAMS_CASDEL'
                            );
  qms$message.add_language  ('QMS-00159'
                            ,'de'
                            ,'Modul kann nicht gelöscht werden, da noch Parameter existieren.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00160');
  --
  qms$message.create_message('QMS-00160'
                            ,'Cannot delete Message row, matching Message Text found.'
                            ,'en'
                            ,'Cannot delete Message row, matching Message Text found.'
                            ,''
                            ,p_constraint_name => 'qms$message_PROPERTIES_qms$message_TEXT_CASDEL'
                            );
  qms$message.add_language  ('QMS-00160'
                            ,'de'
                            ,'Message kann nicht gelöscht werden, da noch Texte existieren.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00160');
  --
  qms$message.create_message('QMS-00160'
                            ,'Cannot delete Message row, matching Message Text found.'
                            ,'en'
                            ,'Cannot delete Message row, matching Message Text found.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-00160'
                            ,'de'
                            ,'Message kann nicht gelöscht werden, da noch Texte existieren.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00161');
  --
  qms$message.create_message('QMS-00161'
                            ,'QMS_NEED_TO_CLOSE_TRANSACTION (status <> ''OPEN'')'
                            ,'en'
                            ,'QMS_NEED_TO_CLOSE_TRANSACTION (status <> ''OPEN'')'
                            ,''
                            ,p_constraint_name => 'QMS_NEED_TO_CLOSE_TRANSACTION'
                            );
  qms$message.add_language  ('QMS-00161'
                            ,'de'
                            ,'QMS_NEED_TO_CLOSE_TRANSACTION (status <> ''OPEN'')'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00162');
  --
  qms$message.create_message('QMS-00162'
                            ,'Transaction with this ID already exists.'
                            ,'en'
                            ,'Transaction with this ID already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_TRNS_PK'
                            );
  qms$message.add_language  ('QMS-00162'
                            ,'de'
                            ,'Transaktion mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-00163');
  --
  qms$message.create_message('QMS-00163'
                            ,'User Preferences with this User_Name already exists.'
                            ,'en'
                            ,'User Preferences with this User_Name already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_USO_PK'
                            );
  qms$message.add_language  ('QMS-00163'
                            ,'de'
                            ,'Benutzereinstellungen exitieren bereits für diesen Benutzer.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01006');
  --
  qms$message.create_message('QMS-01006'
                            ,'No connection for command.'
                            ,'en'
                            ,'No connection for command <p1> with user <p2>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-01006'
                            ,'de'
                            ,'Keine Verbindung für Kommando <p1> mit Benutzer <p2>.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01007');
  --
  qms$message.create_message('QMS-01007'
                            ,'Program not found.'
                            ,'en'
                            ,'Program <p1> not found.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-01007'
                            ,'de'
                            ,'Das Programm <p1> konnte nicht gefunden werden.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01008');
  --
  qms$message.create_message('QMS-01008'
                            ,'Error while executing.'
                            ,'en'
                            ,'Error while executing program <p1>.'||CHR(10)||'<p2>'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-01008'
                            ,'de'
                            ,'Während der Ausführung des Programms <p1> ist ein Fehler aufgetreten.'||CHR(10)||'<p2>'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01009');
  --
  qms$message.create_message('QMS-01009'
                            ,'Error while waiting for termination.'
                            ,'en'
                            ,'Error while waiting for termination of program <p1>.'||CHR(10)||'<p2>'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-01009'
                            ,'de'
                            ,'Beim Warten auf die Beendigung des Programms <p1> ist ein Fehler aufgetreten.'||CHR(10)||'<p2>'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01010');
  --
  qms$message.create_message('QMS-01010'
                            ,'Background process has errors.'
                            ,'en'
                            ,'Background process <p1> was aborted with error <p2>.'||CHR(10)||'<p3>'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-01010'
                            ,'de'
                            ,'Hintergrundprozess <p1> wurde mit Fehler <p2> abgebrochen.'||CHR(10)||'<p3>'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01011');
  --
  qms$message.create_message('QMS-01011'
                            ,'Background process was aborted.'
                            ,'en'
                            ,'Background process <p1> was aborted with error <p2>.'||CHR(10)||'<p3>'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-01011'
                            ,'de'
                            ,'Hintergrundprozess <p1> wurde mit Fehler <p2> abgebrochen.'||CHR(10)||'<p3>'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01012');
  --
  qms$message.create_message('QMS-01012'
                            ,'Background process was started.'
                            ,'en'
                            ,'Background process <p1> was started with next time <p2>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-01012'
                            ,'de'
                            ,'Hintergrundprozess <p1> wurde mit Wiederholung <p2> gestartet.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01013');
  --
  qms$message.create_message('QMS-01013'
                            ,'Background process was started.'
                            ,'en'
                            ,'Background process <p1> was started without next time <p2>.'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-01013'
                            ,'de'
                            ,'Hintergrundprozess <p1> wurde ohne Wiederholung <p2> gestartet.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01014');
  --
  qms$message.create_message('QMS-01014'
                            ,'Background process was stoped.'
                            ,'en'
                            ,'Background process <p1> was stoped..'
                            ,''
                            ,p_constraint_name => ''
                            );
  qms$message.add_language  ('QMS-01014'
                            ,'de'
                            ,'Hintergrundprozess <p1> wurde beendet.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01331');
  --
  qms$message.create_message('QMS-01331'
                            ,'Serverity is invalid.'
                            ,'en'
                            ,'Severity must be a value of ''E'', ''W'', ''I'' or ''M''.'
                            ,''
                            ,p_constraint_name => 'QMS_LOG_SEVERITY'
                            );
  qms$message.add_language  ('QMS-01331'
                            ,'de'
                            ,'Severity kann nur aus dem Wertebereich ''E'', ''W'', ''I'' oder ''M'' stammen.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01101');
  --
  qms$message.create_message('QMS-01101'
                            ,'Application with this ID already exists.'
                            ,'en'
                            ,'Application with this IDalready exists.'
                            ,''
                            ,p_constraint_name => 'QMS_APP_PK'
                            );
  qms$message.add_language  ('QMS-01101'
                            ,'de'
                            ,'Applikation mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01201');
  --
  qms$message.create_message('QMS-01201'
                            ,'Application with this name already exists.'
                            ,'en'
                            ,'Application with this name already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_APP_UK'
                            );
  qms$message.add_language  ('QMS-01201'
                            ,'de'
                            ,'Applikation mit diesem Namen existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01102');
  --
  qms$message.create_message('QMS-01102'
                            ,'User with this ID already exists.'
                            ,'en'
                            ,'User with this ID already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_USR_PK'
                            );
  qms$message.add_language  ('QMS-01102'
                            ,'de'
                            ,'Benutzer mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01202');
  --
  qms$message.create_message('QMS-01202'
                            ,'User with this username already exists.'
                            ,'en'
                            ,'User with this username already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_USR_UK'
                            );
  qms$message.add_language  ('QMS-01202'
                            ,'de'
                            ,'Benutzer mit diesem Benutzernamen existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01103');
  --
  qms$message.create_message('QMS-01103'
                            ,'Role with this ID already exists.'
                            ,'en'
                            ,'Role with this ID already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_ROL_PK'
                            );
  qms$message.add_language  ('QMS-01103'
                            ,'de'
                            ,'Rolle mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01203');
  --
  qms$message.create_message('QMS-01203'
                            ,'Role with this name already exists.'
                            ,'en'
                            ,'Role with this name already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_ROL_UK'
                            );
  qms$message.add_language  ('QMS-01203'
                            ,'de'
                            ,'Rolle mit diesem Namen existiert bereits.'
                            ,''
                            );

  --
  qms$message.delete_message('QMS-01121');
  --
  qms$message.create_message('QMS-01121'
                            ,'Property group with this ID already exists.'
                            ,'en'
                            ,'Property group with this ID already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_SPG_PK'
                            );
  qms$message.add_language  ('QMS-01121'
                            ,'de'
                            ,'Eigenschaftsgruppe mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01221');
  --
  qms$message.create_message('QMS-01221'
                            ,'Property group with this name already exists.'
                            ,'en'
                            ,'Property group with this name already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_SPG_UK'
                            );
  qms$message.add_language  ('QMS-01221'
                            ,'de'
                            ,'Eigenschaftsgruppe mit diesem Namen existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01122');
  --
  qms$message.create_message('QMS-01122'
                            ,'Property name with this ID already exists.'
                            ,'en'
                            ,'Property name with this ID already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_SPN_PK'
                            );
  qms$message.add_language  ('QMS-01122'
                            ,'de'
                            ,'Eigenschaftsname mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01421');
  --
  qms$message.create_message('QMS-01421'
                            ,'No matching preference group found.'
                            ,'en'
                            ,'Property group with this ID does not exists.'
                            ,''
                            ,p_constraint_name => 'QMS_SPN_SPG_FK'
                            );
  qms$message.add_language  ('QMS-01421'
                            ,'de'
                            ,'Eigenschaftsgruppe mit dieser ID existiert nicht.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01123');
  --
  qms$message.create_message('QMS-01123'
                            ,'Property with this ID already exists.'
                            ,'en'
                            ,'Property with this ID already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_SPR_PK'
                            );
  qms$message.add_language  ('QMS-01123'
                            ,'de'
                            ,'Eigenschaft mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01422');
  --
  qms$message.create_message('QMS-01422'
                            ,'No matching preference name found.'
                            ,'en'
                            ,'Property name with this ID does not exists.'
                            ,''
                            ,p_constraint_name => 'QMS_SPR_SPR_FK'
                            );
  qms$message.add_language  ('QMS-01422'
                            ,'de'
                            ,'Eigenschaftsname mit dieser ID existiert nicht.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01131');
  --
  qms$message.create_message('QMS-01131'
                            ,'Job application with this ID already exists.'
                            ,'en'
                            ,'Job application with this ID already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_JAP_PK'
                            );
  qms$message.add_language  ('QMS-01131'
                            ,'de'
                            ,'Job-Application mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01431');
  --
  qms$message.create_message('QMS-01431'
                            ,'No matching user found.'
                            ,'en'
                            ,'User with this ID does not exists.'
                            ,''
                            ,p_constraint_name => 'QMS_JAP_USR_PK'
                            );
  qms$message.add_language  ('QMS-01431'
                            ,'de'
                            ,'Benutzer mit dieser ID existiert nicht.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01132');
  --
  qms$message.create_message('QMS-01132'
                            ,'Job worker with this ID already exists.'
                            ,'en'
                            ,'Job worker with this ID already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_WKR_PK'
                            );
  qms$message.add_language  ('QMS-01132'
                            ,'de'
                            ,'Job-Worker mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01133');
  --
  qms$message.create_message('QMS-01133'
                            ,'Job command with this ID already exists.'
                            ,'en'
                            ,'Job command with this ID already exists.'
                            ,''
                            ,p_constraint_name => 'QMS_CMD_PK'
                            );
  qms$message.add_language  ('QMS-01133'
                            ,'de'
                            ,'Job-Kommando mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('QMS-01333');
  --
  qms$message.create_message('QMS-01333'
                            ,'One date must be supplied.'
                            ,'en'
                            ,'One of LAST_DATE, START_DATE or NEXT_DATE mus be provided.'
                            ,''
                            ,p_constraint_name => 'QMS_CMD_DATE'
                            );
  qms$message.add_language  ('QMS-01333'
                            ,'de'
                            ,'Eines der Datenfelder LAST_DATE, START_DATE oder NEXT_DATE muss angegeben werden.'
                            ,''
                            );
END;
/