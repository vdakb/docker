Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Anonymous Identifier Generator messages.
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
PROMPT Create Anonymous Identifier Generator Message Properties
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
BEGIN
  qms$message.delete_message('PIT-00101');
  --
  qms$message.create_message('PIT-00101'
                            ,'Anonymous ID already exists.'
                            ,'en'
                            ,'Anonymous ID already exists.'
                            ,''
                            ,p_constraint_name => 'PIT_AID_PK'
                            );
  qms$message.add_language  ('PIT-00101'
                            ,'de'
                            ,'Anonymisierte ID existiert bereits.'
                            ,''
                            );
  --
  qms$message.delete_message('PIT-00301');
  --
  qms$message.create_message('PIT-00301'
                            ,'Active must have only values 0 or 1.'
                            ,'en'
                            ,'Active must have only values 0 or 1.'
                            ,''
                            ,p_constraint_name => 'PIT_AID_CK1'
                            );
  qms$message.add_language  ('PIT-00301'
                            ,'de'
                            ,'Active kann nur die Werte 0 oder 1 annehmen.'
                            ,''
                            );
END;
/