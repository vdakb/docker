# lookup definitions
lku.select=\
SELECT lku.lku_key\
,      lku.lku_lookup_key\
,      lku.lku_type_string_key\
,      lku.lku_meaning\
,      lku.lku_type\
,      lku.lku_group\
,      lku.lku_field\
,      lku.lku_required\
,      rownum AS rownumber \
FROM   lku lku

# lookup values
lkv.select=\
SELECT lkv.lku_key\
,      lkv.lkv_key\
,      lkv.lkv_encoded\
,      lkv.lkv_decoded\
,      lkv.lkv_disabled\
,      rownum AS rownumber \
FROM   lku lku\
,      lkv lkv \
WHERE  lkv.lku_key = lku.lku_key

# system properties
pty.select=\
SELECT pty_key\
,      lku_key\
,      pty_keyword\
,      pty_value\
,      pty_name\
,      pty_data_level\
,      pty_system\
,      pty_loginrequired\
,      pty_run_on\
,      rownum AS rownumber \
FROM   pty pty

# notification templates
tpl.select=\
SELECT id\
,      templatename\
,      description\
,      eventname\
,      status\
,      snmpsupported\
,      datalevel\
,      rownum AS rownumber \
FROM   notificationtemplate tpl
