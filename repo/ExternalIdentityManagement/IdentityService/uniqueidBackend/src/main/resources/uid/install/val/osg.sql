Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Unique Identifier Generator messages.
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
PROMPT Create Unique Identifier Generator Message Properties
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
BEGIN
  qms$message.delete_message('UIT-00101');
  --
  qms$message.create_message('UIT-00101'
                            ,'Participant Type with ID already exists.'
                            ,'en'
                            ,'Participant Type with ID already exists.'
                            ,''
                            ,p_constraint_name => 'UIT_PTT_PK'
                            );
  qms$message.add_language  ('UIT-00101'
                            ,'de'
                            ,'Partnertype mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00102');
  --
  qms$message.create_message('UIT-00102'
                            ,'Country with ID already exists.'
                            ,'en'
                            ,'Country with ID already exists.'
                            ,''
                            ,p_constraint_name => 'UIT_CNT_PK'
                            );
  qms$message.add_language  ('UIT-00102'
                            ,'de'
                            ,'Land mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00103');
  --
  qms$message.create_message('UIT-00103'
                            ,'State with ID already exists.'
                            ,'en'
                            ,'State with ID already exists.'
                            ,''
                            ,p_constraint_name => 'UIT_STS_PK'
                            );
  qms$message.add_language  ('UIT-00103'
                            ,'de'
                            ,'Bundesland mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00104');
  --
  qms$message.create_message('UIT-00104'
                            ,'Participant with ID already exists.'
                            ,'en'
                            ,'Participant with ID already exists.'
                            ,''
                            ,p_constraint_name => 'UIT_PTS_PK'
                            );
  qms$message.add_language  ('UIT-00104'
                            ,'de'
                            ,'Teilnehmer mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00105');
  --
  qms$message.create_message('UIT-00105'
                            ,'Tenant with ID already exists.'
                            ,'en'
                            ,'Tenant with ID already exists.'
                            ,''
                            ,p_constraint_name => 'UIT_TNT_PK'
                            );
  qms$message.add_language  ('UIT-00105'
                            ,'de'
                            ,'Mandant mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00106');
  --
  qms$message.create_message('UIT-00106'
                            ,'Claim with ID already exists.'
                            ,'en'
                            ,'Claim with ID already exists.'
                            ,''
                            ,p_constraint_name => 'UIT_CLM_PK'
                            );
  qms$message.add_language  ('UIT-00106'
                            ,'de'
                            ,'Anspruch mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00107');
  --
  qms$message.create_message('UIT-00107'
                            ,'Identity Type with ID already exists.'
                            ,'en'
                            ,'Identity Type with ID already exists.'
                            ,''
                            ,p_constraint_name => 'UIT_TYP_PK'
                            );
  qms$message.add_language  ('UIT-00107'
                            ,'de'
                            ,'Identit?tstype mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00108');
  --
  qms$message.create_message('UIT-00108'
                            ,'Unique Identifier with ID already exists.'
                            ,'en'
                            ,'Unique Identifier with ID already exists.'
                            ,''
                            ,p_constraint_name => 'UIT_UID_PK'
                            );
  qms$message.add_language  ('UIT-00108'
                            ,'de'
                            ,'Identifier mit dieser ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00201');
  --
  qms$message.create_message('UIT-00201'
                            ,'Participant Type with name already exists.'
                            ,'en'
                            ,'Participant Type with name already exists.'
                            ,''
                            ,p_constraint_name => 'UIT_PTT_UK'
                            );
  qms$message.add_language  ('UIT-00201'
                            ,'de'
                            ,'Partnertyp mit diesem Namen existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00202');
  --
  qms$message.create_message('UIT-00202'
                            ,'Country with name already exists.'
                            ,'en'
                            ,'Country with name already exists.'
                            ,''
                            ,p_constraint_name => 'UIT_CNT_UK'
                            );
  qms$message.add_language  ('UIT-00202'
                            ,'de'
                            ,'Land mit diesem Namen existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00203');
  --
  qms$message.create_message('UIT-00203'
                            ,'State with name already exists.'
                            ,'en'
                            ,'State with name already exists.'
                            ,''
                            ,p_constraint_name => 'UIT_STS_UK'
                            );
  qms$message.add_language  ('UIT-00203'
                            ,'de'
                            ,'Bundesland mit diesem Namen existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00204');
  --
  qms$message.create_message('UIT-00204'
                            ,'Participant with name already exists.'
                            ,'en'
                            ,'Participant with name already exists.'
                            ,''
                            ,p_constraint_name => 'UIT_PTS_UK'
                            );
  qms$message.add_language  ('UIT-00204'
                            ,'de'
                            ,'Teilnehmer mit diesem Namen existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00205');
  --
  qms$message.create_message('UIT-00205'
                            ,'Identity Type with name already exists.'
                            ,'en'
                            ,'Identity Type with name already exists.'
                            ,''
                            ,p_constraint_name => 'UIT_TYP_UK'
                            );
  qms$message.add_language  ('UIT-00205'
                            ,'de'
                            ,'Identit?tstyp mit diesem Namen existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00301');
  --
  qms$message.create_message('UIT-00301'
                            ,'Active must have only values 0 or 1.'
                            ,'en'
                            ,'Active must have only values 0 or 1.'
                            ,''
                            ,p_constraint_name => 'UIT_PTT_CK1'
                            );
  qms$message.add_language  ('UIT-00301'
                            ,'de'
                            ,'Active kann nur die Werte 0 oder 1 annehmen.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00302');
  --
  qms$message.create_message('UIT-00302'
                            ,'Active must have only values 0 or 1.'
                            ,'en'
                            ,'Active must have only values 0 or 1.'
                            ,''
                            ,p_constraint_name => 'UIT_CNT_CK1'
                            );
  qms$message.add_language  ('UIT-00302'
                            ,'de'
                            ,'Active kann nur die Werte 0 oder 1 annehmen.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00303');
  --
  qms$message.create_message('UIT-00303'
                            ,'Active must have only values 0 or 1.'
                            ,'en'
                            ,'Active must have only values 0 or 1.'
                            ,''
                            ,p_constraint_name => 'UIT_STS_CK1'
                            );
  qms$message.add_language  ('UIT-00303'
                            ,'de'
                            ,'Active kann nur die Werte 0 oder 1 annehmen.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00304');
  --
  qms$message.create_message('UIT-00304'
                            ,'Active must have only values 0 or 1.'
                            ,'en'
                            ,'Active must have only values 0 or 1.'
                            ,''
                            ,p_constraint_name => 'UIT_PTS_CK1'
                            );
  qms$message.add_language  ('UIT-00304'
                            ,'de'
                            ,'Active kann nur die Werte 0 oder 1 annehmen.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00305');
  --
  qms$message.create_message('UIT-00305'
                            ,'Active must have only values 0 or 1.'
                            ,'en'
                            ,'Active must have only values 0 or 1.'
                            ,''
                            ,p_constraint_name => 'UIT_TNT_CK1'
                            );
  qms$message.add_language  ('UIT-00305'
                            ,'de'
                            ,'Active kann nur die Werte 0 oder 1 annehmen.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00306');
  --
  qms$message.create_message('UIT-00306'
                            ,'Active must have only values 0 or 1.'
                            ,'en'
                            ,'Active must have only values 0 or 1.'
                            ,''
                            ,p_constraint_name => 'UIT_TYP_CK1'
                            );
  qms$message.add_language  ('UIT-00306'
                            ,'de'
                            ,'Active kann nur die Werte 0 oder 1 annehmen.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00307');
  --
  qms$message.create_message('UIT-00307'
                            ,'State must have only values 0, 1, 2 or 4.'
                            ,'en'
                            ,'State must have only values 0, 1, 2 or 4.'
                            ,''
                            ,p_constraint_name => 'UIT_UID_CK1'
                            );
  qms$message.add_language  ('UIT-00307'
                            ,'de'
                            ,'State kann nur die Werte 0, 1, 2 oder 4 annehmen.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00401');
  --
  qms$message.create_message('UIT-00401'
                            ,'Tenant with this id does not exists.'
                            ,'en'
                            ,'Tenant with this id does not exists.'
                            ,''
                            ,p_constraint_name => 'UIT_UID_TNT_FK'
                            );
  qms$message.add_language  ('UIT-00401'
                            ,'de'
                            ,'Mandant mit dieser ID existiert nicht.'
                            ,''
                            );
  --
  qms$message.delete_message('UIT-00402');
  --
  qms$message.create_message('UIT-00402'
                            ,'Identity Type with this id does not exists.'
                            ,'en'
                            ,'Identity Type with this id does not exists.'
                            ,''
                            ,p_constraint_name => 'UIT_UID_TYP_FK'
                            );
  qms$message.add_language  ('UIT-00402'
                            ,'de'
                            ,'Identit?tstyp mit dieser ID existiert nicht.'
                            ,''
                            );
END;
/