Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Identity Governance Services messages.
Rem
Rem Usage Information:
Rem     sqlplus igd_igs/<password>
Rem     @<path>/osg
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Create Identity Governance Services Message Properties
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
BEGIN
  qms$message.delete_message('IGT-00101');
  --
  qms$message.create_message('IGT-00101'
                            ,'Role with ID already exists.'
                            ,'en'
                            ,'Role with ID already exists.'
                            ,''
                            ,p_constraint_name => 'IGT_ROL_PK'
                            );
  qms$message.add_language  ('IGT-00101'
                            ,'de'
                            ,'Rolle mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('IGT-00102');
  --
  qms$message.create_message('IGT-00102'
                            ,'User with ID already exists.'
                            ,'en'
                            ,'User with ID already exists.'
                            ,''
                            ,p_constraint_name => 'IGT_USR_PK'
                            );
  qms$message.add_language  ('IGT-00102'
                            ,'de'
                            ,'Benutzer mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.create_message('IGT-00103'
                            ,'Userrole relation already exists.'
                            ,'en'
                            ,'Userrole relation already exists.'
                            ,''
                            ,p_constraint_name => 'IGT_URL_PK'
                            );
  qms$message.add_language  ('IGT-00103'
                            ,'de'
                            ,'Benutzerzuordnung existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('IGT-00201');
  --
  qms$message.create_message('IGT-00201'
                            ,'User with username already exists.'
                            ,'en'
                            ,'User with username already exists.'
                            ,''
                            ,p_constraint_name => 'IGT_USR_UK1'
                            );
  qms$message.add_language  ('IGT-00201'
                            ,'de'
                            ,'Benutzer mit diesem Benutzernamen existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('IGT-00202');
  --
  qms$message.create_message('IGT-00202'
                            ,'User with e-Mail already exists.'
                            ,'en'
                            ,'User with e-Mail address already exists.'
                            ,''
                            ,p_constraint_name => 'IGT_USR_UK2'
                            );
  qms$message.add_language  ('IGT-00202'
                            ,'de'
                            ,'Benutzer mit dieser e-Mail Adresse existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('IGT-00301');
  --
  qms$message.create_message('IGT-00301'
                            ,'Active must have only values 0 or 1.'
                            ,'en'
                            ,'Active must have only values 0 or 1.'
                            ,''
                            ,p_constraint_name => 'IGT_ROL_CK1'
                            );
  qms$message.add_language  ('IGT-00301'
                            ,'de'
                            ,'Active kann nur die Werte 0 oder 1 annehmen.'
                            ,''
                            );
  --
  qms$message.delete_message('IGT-00302');
  --
  qms$message.create_message('IGT-00302'
                            ,'Active must have only values 0 or 1.'
                            ,'en'
                            ,'Active must have only values 0 or 1.'
                            ,''
                            ,p_constraint_name => 'IGT_USR_CK1'
                            );
  qms$message.add_language  ('IGT-00302'
                            ,'de'
                            ,'Active kann nur die Werte 0 oder 1 annehmen.'
                            ,''
                            );
  --
  qms$message.delete_message('IGT-00303');
  --
  qms$message.create_message('IGT-00303'
                            ,'Language must have only values ''en'' or ''de''.'
                            ,'en'
                            ,'Language must have only values ''en'' or ''de''.'
                            ,''
                            ,p_constraint_name => 'IGT_USR_CK2'
                            );
  qms$message.add_language  ('IGT-00303'
                            ,'de'
                            ,'Language kann nur die Werte ''en'' oder ''de'' annehmen.'
                            ,''
                            );
END;
/