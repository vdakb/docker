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
Rem     @<path>/messages_qms
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0    2020-03-30  DSteding    First release version
Rem

PROMPT
PROMPT Create messages for Employee Self Service Vehicle
BEGIN
  --
  qms$message.delete_message('EPT-00104');
  --
  qms$message.create_message('EPT-00104'
                            ,'Vehicle Color with ID already exists.'
                            ,'en'
                            ,'Vehicle Color with ID already exists.'
                            ,''
                            ,p_constraint_name => 'EPT_VHC_PK'
                            );
  qms$message.add_language  ('EPT-00104'
                            ,'de'
                            ,'Fahrzeugfarbe mit ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('EPT-00105');
  --
  qms$message.create_message('EPT-00105'
                            ,'Vehicle Type with ID already exists.'
                            ,'en'
                            ,'Vehicle Type with ID already exists.'
                            ,''
                            ,p_constraint_name => 'EPT_VHT_PK'
                            );
  qms$message.add_language  ('EPT-00105'
                            ,'de'
                            ,'Fahrzeugtyp mit ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('EPT-00106');
  --
  qms$message.create_message('EPT-00106'
                            ,'Vehicle Brand with ID already exists.'
                            ,'en'
                            ,'Vehicle Brand with ID already exists.'
                            ,''
                            ,p_constraint_name => 'EPT_VMB_PK'
                            );
  qms$message.add_language  ('EPT-00106'
                            ,'de'
                            ,'Fahrzeughersteller mit ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('EPT-00107');
  --
  qms$message.create_message('EPT-00107'
                            ,'Vehicle Brand and Vehicle Type association already exists.'
                            ,'en'
                            ,'Vehicle Brand and Vehicle Type association already exists.'
                            ,''
                            ,p_constraint_name => 'EPT_VMT_PK'
                            );
  qms$message.add_language  ('EPT-00107'
                            ,'de'
                            ,'Fahrzeugtyp ist Fahrzeughersteller bereits zugewiesen.'
                            ,''
                            );
  --
  qms$message.delete_message('EPT-00108');
  --
  qms$message.create_message('EPT-00108'
                            ,'Vehicle with ID already exists.'
                            ,'en'
                            ,'Vehicle with ID already exists.'
                            ,''
                            ,p_constraint_name => 'EPT_VHL_PK'
                            );
  qms$message.add_language  ('EPT-00108'
                            ,'de'
                            ,'Fahrzeug mit ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('EPT-00402');
  --
  qms$message.create_message('EPT-00402'
                            ,'Vehicle Brand with this id does not exists.'
                            ,'en'
                            ,'Vehicle Brand with this id does not exists.'
                            ,''
                            ,p_constraint_name => 'EPT_VMT_VMB_FK'
                            );
  qms$message.add_language  ('EPT-00402'
                            ,'de'
                            ,'Fahrzeughersteller mit dieser ID existiert nicht.'
                            ,''
                            );
  --
  qms$message.delete_message('EPT-00403');
  --
  qms$message.create_message('EPT-00403'
                            ,'Vehicle Type with this id does not exists.'
                            ,'en'
                            ,'Vehicle Type with this id does not exists.'
                            ,''
                            ,p_constraint_name => 'EPT_VMT_VHT_FK'
                            );
  qms$message.add_language  ('EPT-00403'
                            ,'de'
                            ,'Fahrzeugtyp mit dieser ID existiert nicht.'
                            ,''
                            );
  --
  qms$message.delete_message('EPT-00404');
  --
  qms$message.create_message('EPT-00404'
                            ,'Vehicle Brand Type with this id does not exists.'
                            ,'en'
                            ,'Vehicle Brand Type with this id does not exists.'
                            ,''
                            ,p_constraint_name => 'EPT_VHL_VMT_FK'
                            );
  qms$message.add_language  ('EPT-00404'
                            ,'de'
                            ,'Fahrzeugherstellertyp mit dieser ID existiert nicht.'
                            ,''
                            );
  --
  qms$message.delete_message('EPT-00405');
  --
  qms$message.create_message('EPT-00405'
                            ,'Vehicle Color with this id does not exists.'
                            ,'en'
                            ,'Vehicle Color with this id does not exists.'
                            ,''
                            ,p_constraint_name => 'EPT_VHL_VHC_FK'
                            );
  qms$message.add_language  ('EPT-00405'
                            ,'de'
                            ,'Fahrzeugfarbe mit dieser ID existiert nicht.'
                            ,''
                            );
  END;
/
COMMIT
/
