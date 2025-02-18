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
PROMPT Create messages for Employee Portal core
BEGIN
  qms$message.delete_message( 'EPT-00101');
  --
  qms$message.create_message( 'EPT-00101'
                            , 'Session with ID already exists.'
                            , 'en'
                            , 'Session with ID already exists.'
                            , ''
                            , p_constraint_name => 'EPT_SES_PK'
                            );
  qms$message.add_language  ('EPT-00101'
                            ,'de'
                            ,'Session mit ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('EPT-00102');
  --
  qms$message.create_message('EPT-00102'
                            ,'Locale with ID already exists.'
                            ,'en'
                            ,'Locale with ID already exists.'
                            ,''
                            ,p_constraint_name => 'EPT_LOC_PK'
                            );
  qms$message.add_language  ('EPT-00102'
                            ,'de'
                            ,'Locale mit ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('EPT-00103');
  --
  qms$message.create_message('EPT-00103'
                            ,'Bundle with ID already exists.'
                            ,'en'
                            ,'Bundle with ID already exists.'
                            ,''
                            ,p_constraint_name => 'EPT_BDL_PK'
                            );
  qms$message.add_language  ('EPT-00103'
                            ,'de'
                            ,'Bundle mit ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('EPT-00301');
  --
  qms$message.create_message('EPT-00301'
                            ,'Locale must have values ''en'', ''de'' or ''fr'' only .'
                            ,'en'
                            ,'Locale must have values ''en'', ''de'' or ''fr'' only .'
                            ,''
                            ,p_constraint_name => 'EPT_LOC_CK'
                            );
  qms$message.add_language  ('EPT-00301'
                            ,'de'
                            ,'Locale kann nur die Werte ''en'', ''de'' oder ''fr'' annehmen.'
                            ,''
                            );
  --
  qms$message.delete_message('EPT-00401');
  --
  qms$message.create_message('EPT-00401'
                            ,'Locale with this id does not exists.'
                            ,'en'
                            ,'Locale with this id does not exists.'
                            ,''
                            ,p_constraint_name => 'EPT_BDL_LOC_FK'
                            );
  qms$message.add_language  ('EPT-00401'
                            ,'de'
                            ,'Locale mit dieser ID existiert nicht.'
                            ,''
                            );
  END;
/
COMMIT
/
