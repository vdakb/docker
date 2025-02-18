Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Identity Governance Services System Properties.
Rem
Rem Usage Information:
Rem     sqlplus igd_igs/<password>
Rem     @<path>/spf
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Create Identity Governance Services System Properties
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
BEGIN
  qms$preference.remove_group('debug');
  --
  qms$preference.add_value(p_group         => 'debug'
                          ,p_name          => 'enabled'
                          ,p_value         => 'true'
                          ,p_display_order => 0
                          ,p_description   => 'The flag to control is debug facility enabled or not.'
                          );
  --
  qms$preference.add_value(p_group         => 'debug'
                          ,p_name          => 'threshold'
                          ,p_value         => '20'
                          ,p_display_order => 1
                          ,p_description   => 'The threshold which will prevent output of persistent debug messages.'||chr(10)
                                            ||'Following values are valid:'||chr(10)
                                            ||chr(09)||' 40 high '||chr(10)
                                            ||chr(09)||' 30 medium priority'||chr(10)
                                            ||chr(09)||' 20 normal'||chr(10)
                                            ||chr(09)||' 10 low'||chr(10)
                                            ||chr(09)||'  0 none'
                          );
  --
  qms$preference.add_value(p_group         => 'debug'
                          ,p_name          => 'trace.mode'
                          ,p_value         => '3'
                          ,p_display_order => 2
                          ,p_description   => 'The mode which will used by debug facility to display trace messages persistent.'||chr(10)
                                            ||'Following values are valid:'||chr(10)
                                            ||chr(09)||' 0 no message will be delivered'||chr(10)
                                            ||chr(09)||' 1 all message will be delivered to server output'||chr(10)
                                            ||chr(09)||' 2 all message will be delivered to a pipe'||chr(10)
                                            ||chr(09)||' 3 all message will be delivered to the log'
                          );
  --
  qms$preference.add_value(p_group         => 'debug'
                          ,p_name          => 'timepoints'
                          ,p_value         => 'false'
                          ,p_display_order => 3
                          ,p_description   => 'The flag to control the gathering of timepoints is enabled or not.'
                          );
  --
  qms$preference.remove_group('logging');
  --
  qms$preference.add_value(p_group         => 'logging'
                          ,p_name          => 'enabled'
                          ,p_value         => 'true'
                          ,p_display_order => 0
                          ,p_description   => 'The flag to control is file logging facility enabled or not.'
                          );
  --
  qms$preference.add_value(p_group         => 'logging'
                          ,p_name          => 'waittime'
                          ,p_value         => '1'
                          ,p_display_order => 1
                          ,p_description   => 'The time to wait for a queue event in minutes.'
                          );
  --
  qms$preference.add_value(p_group         => 'logging'
                          ,p_name          => 'interval'
                          ,p_value         => 'SYSDATE + 1/1440'
                          ,p_display_order => 2
                          ,p_description   => 'The interval for the background logging process.'
                          );
  --
  qms$preference.add_value(p_group         => 'logging'
                          ,p_name          => 'directory'
                          ,p_value         => '/var/oracle/QMS/log'
                          ,p_display_order => 3
                          ,p_description   => 'The directory where the file should be created.'
                          );
  --
  qms$preference.add_value(p_group         => 'logging'
                          ,p_name          => 'filename'
                          ,p_value         => 'monitor.log'
                          ,p_display_order => 4
                          ,p_description   => 'The name of file that should be created.'
                          );
  --
  qms$preference.add_value(p_group         => 'logging'
                          ,p_name          => 'delemiter'
                          ,p_value         => '|'
                          ,p_display_order => 5
                          ,p_description   => 'The character to separate entries in a line.'
                          );
  --
  qms$preference.add_value(p_group         => 'logging'
                          ,p_name          => 'dateformat'
                          ,p_value         => 'dd.MM.yyyy${logging.delemiter}HH:mm:ss:SSS'
                          ,p_display_order => 6
                          ,p_description   => 'The format for timestamps.'
                                            ||chr(10)
                                            ||'(see java.text.SimpleDateFormat for the list of options)'
                          );
END;
/