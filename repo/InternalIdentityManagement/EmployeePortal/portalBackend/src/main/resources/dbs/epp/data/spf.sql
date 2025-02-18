Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem $Header$
Rem
Rem Program.....: sqlplus
Rem
Rem Requirements: sqlplus
Rem     The database must be up and running.
Rem
Rem Purpose.....:
Rem     This script creates Employee Self Service Portal properties.
Rem
Rem     This option is optional if the database is used as an
Rem     repository for Custom Application Development.
Rem
Rem Author......: Dieter Steding, Oracle Consulting Berlin
Rem
Rem
Rem Revision   Date        Editor      Comment
Rem ----------+-----------+-----------+------------------------------------
Rem 1.0.0.0    2020-03-30  DSteding    First release version
Rem

PROMPT ********************************************************************
PROMPT Create preferences for Employee Self Service Portal Core
PROMPT ********************************************************************
BEGIN
  qms$preference.remove_group('security');
  --
  qms$preference.add_value(p_group         => 'security'
                          ,p_name          => 'session.secret'
                          ,p_value         => '^#$5sX(Hf6KUo!#65^'
                          ,p_display_order => 0
                          ,p_description   => 'This is the secret used to sign the session ID cookie. This can be either a string for a single secret, or an array of multiple secrets. If an array of secrets is provided, only the first element will be used to sign the session ID cookie, while all the elements will be considered when verifying the signature in requests.'
                          );
  --
  qms$preference.add_value(p_group         => 'security'
                          ,p_name          => 'session.name'
                          ,p_value         => 'EPP_SESSION_ID'
                          ,p_display_order => 1
                          ,p_description   => 'The name of the session ID cookie to set in the response (and read from in the request). The default value is ''connect.sid''.'
                          );
  --
  qms$preference.add_value(p_group         => 'security'
                          ,p_name          => 'session.resave'
                          ,p_value         => 'true'
                          ,p_display_order => 2
                          ,p_description   => 'Forces the session to be saved back to the session store, even if the session was never modified during the request. Depending on your store this may be necessary, but it can also create race conditions where a client makes two parallel requests to your server and changes made to the session in one request may get overwritten when the other request ends, even if it made no changes (this behavior also depends on what store you''re using).'
                          );
  --
  qms$preference.add_value(p_group         => 'security'
                          ,p_name          => 'session.saveUninitialized'
                          ,p_value         => 'true'
                          ,p_display_order => 3
                          ,p_description   => 'Forces a session that is ''uninitialized'' to be saved to the store. A session is uninitialized when it is new but not modified. Choosing false is useful for implementing login sessions, reducing server storage usage, or complying with laws that require permission before setting a cookie. Choosing false will also help with race conditions where a client makes multiple parallel requests without a session.'
                          );
  --
  qms$preference.add_value(p_group         => 'security'
                          ,p_name          => 'session.rolling'
                          ,p_value         => 'false'
                          ,p_display_order => 4
                          ,p_description   => 'Force a session identifier cookie to be set on every response. The expiration is reset to the original maxAge, resetting the expiration countdown. The default value is false.'
                          );
  --
  qms$preference.add_value(p_group         => 'security'
                          ,p_name          => 'cookie.name'
                          ,p_value         => 'oimjsessionid'
                          ,p_display_order => 5
                          ,p_description   => 'The name of cookie valid for application system.'
                                             ||chr(10)
                                             ||'NOTE: Must only be configured for testing.'
                                             ||chr(10)
                                             ||'      Web Tier configure this in web.xml.'
                          );
  --
  qms$preference.add_value(p_group         => 'security'
                          ,p_name          => 'cookie.domain'
                          ,p_value         => '${system.host}'
                          ,p_display_order => 6
                          ,p_description   => 'The domain of cookie valid for application system.'
                          );
  --
  qms$preference.add_value(p_group         => 'security'
                          ,p_name          => 'cookie.path'
                          ,p_value         => '/'
                          ,p_display_order => 7
                          ,p_description   => ''
                                             ||chr(10)
                                             ||'NOTE: Must only be configured for testing.'
                                             ||chr(10)
                                             ||'      Web Tier ignore this value.'
                          );
  --
  qms$preference.add_value(p_group         => 'security'
                          ,p_name          => 'cookie.expires'
                          ,p_value         => '10'
                          ,p_display_order => 8
                          ,p_description   => 'The duration after that the cookie will expire. In most cases 10 hours'
                          );
  --
  qms$preference.remove_group('system');
  --
  qms$preference.add_value(p_group         => 'system'
                          ,p_name          => 'owner'
                          ,p_value         => 'igd_epp'
                          ,p_display_order => 0
                          ,p_description   => 'The database user which owns the schema.'
                          );
  --
  qms$preference.add_value(p_group         => 'system'
                          ,p_name          => 'user'
                          ,p_value         => 'xelsysadm'
                          ,p_display_order => 1
                          ,p_description   => 'The database user which access the schema.'
                          );
  --
  qms$preference.add_value(p_group         => 'system'
                          ,p_name          => 'admin'
                          ,p_value         => 'xelsysadm'
                          ,p_display_order => 2
                          ,p_description   => 'The database user which administer the schema.'
                          );
  --
  qms$preference.add_value(p_group         => 'system'
                          ,p_name          => 'host'
                          ,p_value         => 'igd.cinnamonstar.net'
                          ,p_display_order => 3
                          ,p_description   => 'The logical machine on which middletier for this application system resides.'
                          );
  qms$preference.add_value(p_group         => 'system'
                          ,p_name          => 'port'
                          ,p_value         => '80'
                          ,p_display_order => 4
                          ,p_description   => 'The ports on which middletier for this application system is listening.'
                          );
  qms$preference.add_value(p_group         => 'system'
                          ,p_name          => 'root'
                          ,p_value         => 'epp'
                          ,p_display_order => 5
                          ,p_description   => 'JEE context root configured in application server.'
                          );
  qms$preference.add_value(p_group         => 'system'
                          ,p_name          => 'home'
                          ,p_value         => 'http://${system.host}/${system.root}'
                          ,p_display_order => 6
                          ,p_description   => 'The home page of application system.'
                                             ||chr(10)
                                             ||'NOTE: Must only be configured for testing.'
                                             ||chr(10)
                                             ||'      Depended value is sso.url.home.'
                          );
  qms$preference.add_value(p_group         => 'system'
                          ,p_name          => 'signon'
                          ,p_value         => 'http://${system.host}/${system.root}/faces/index'
                          ,p_display_order => 7
                          ,p_description   => 'The URL to which will SSO server redirect after authentication.'
                                             ||chr(10)
                                             ||'NOTE: Must only be configured for testing.'
                          );
  qms$preference.add_value(p_group         => 'system'
                          ,p_name          => 'signout'
                          ,p_value         => 'http://${system.host}/${system.root}/faces/signout'
                          ,p_display_order => 8
                          ,p_description   => 'The URL to which will response of a SSO server request after signout.'
                                             ||chr(10)
                                             ||'NOTE: Must only be configured for testing.'
                          );
  END;
/
COMMIT
/