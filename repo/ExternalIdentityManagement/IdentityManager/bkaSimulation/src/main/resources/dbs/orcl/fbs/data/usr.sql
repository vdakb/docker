Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates eFBS User Repository seeded accounts.
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
PROMPT Create seeded user for eFBS User Repository
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'rssuper'
, 'rssuper'
, 'rssuper'
, 'GLOBAL'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user'
, 'property_user'
, 'property_user'
, 'GLOBAL'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_glob'
, 'property_user_glob'
, 'property_user_glob'
, 'GLOBAL'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_an'
, 'property_user_an'
, 'property_user_an'
, 'AN'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_bb'
, 'property_user_bb'
, 'property_user_bb'
, 'BB'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_be'
, 'property_user_be'
, 'property_user_be'
, 'BE'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_bk'
, 'property_user_bk'
, 'property_user_bk'
, 'BK'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_bp'
, 'property_user_bp'
, 'property_user_bp'
, 'BP'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_bw'
, 'property_user_bw'
, 'property_user_bw'
, 'BW'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_by'
, 'property_user_by'
, 'property_user_by'
, 'BY'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_hb'
, 'property_user_hb'
, 'property_user_hb'
, 'HB'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_he'
, 'property_user_he'
, 'property_user_he'
, 'HE'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_hh'
, 'property_user_hh'
, 'property_user_hh'
, 'HH'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_mv'
, 'property_user_mv'
, 'property_user_mv'
, 'MV'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_ni'
, 'property_user_ni'
, 'property_user_ni'
, 'NI'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_nw'
, 'property_user_nw'
, 'property_user_nw'
, 'NW'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_rp'
, 'property_user_rp'
, 'property_user_rp'
, 'RP'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_sh'
, 'property_user_sh'
, 'property_user_sh'
, 'SH'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_sl'
, 'property_user_sl'
, 'property_user_sl'
, 'SL'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_sn'
, 'property_user_sn'
, 'property_user_sn'
, 'SN'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_th'
, 'property_user_th'
, 'property_user_th'
, 'TH'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'property_user_zf'
, 'property_user_zf'
, 'property_user_zf'
, 'ZF'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'service_user_an'
, 'service_user_an'
, 'service_user_an'
, 'AN'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'service_user_bb'
, 'service_user_bb'
, 'service_user_bb'
, 'BB'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'service_user_be'
, 'service_user_be'
, 'service_user_be'
, 'BE'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'service_user_bk'
, 'service_user_bk'
, 'service_user_bk'
, 'BK'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'service_user_bp'
, 'service_user_bp'
, 'service_user_bp'
, 'BP'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'service_user_bw'
, 'service_user_bw'
, 'service_user_bw'
, 'BW'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'service_user_by'
, 'service_user_by'
, 'service_user_by'
, 'BY'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'service_user_hb'
, 'service_user_hb'
, 'service_user_hb'
, 'HB'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'service_user_he'
, 'service_user_he'
, 'service_user_he'
, 'HE'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'service_user_hh'
, 'service_user_hh'
, 'service_user_hh'
, 'HH'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'service_user_mv'
, 'service_user_mv'
, 'service_user_mv'
, 'MV'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'service_user_ni'
, 'service_user_ni'
, 'service_user_ni'
, 'NI'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'service_user_nw'
, 'service_user_nw'
, 'service_user_nw'
, 'NW'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'service_user_rp'
, 'service_user_rp'
, 'service_user_rp'
, 'RP'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'service_user_sh'
, 'service_user_sh'
, 'service_user_sh'
, 'SH'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'service_user_sl'
, 'service_user_sl'
, 'service_user_sl'
, 'SL'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'service_user_sn'
, 'service_user_sn'
, 'service_user_sn'
, 'SN'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'service_user_th'
, 'service_user_th'
, 'service_user_th'
, 'TH'
, 'System-Administrator'
)
/
INSERT INTO fbt_users (
  username
, lastname
, firstname
, organization
, division
)
VALUES (
  'service_user_zf'
, 'service_user_zf'
, 'service_user_zf'
, 'ZF'
, 'System-Administrator'
)
/
COMMIT
/