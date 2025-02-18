# accounts provisioned
account.provisioned=\
SELECT oiu.oiu_key\
,      oiu.app_instance_key\
,      app.app_instance_name\
,      app.app_instance_display_name\
,      oiu.account_type\
,      oiu.request_key\
,      oiu.pol_key\
,      usr.usr_key\
,      usr.usr_login\
,      usr.usr_first_name\
,      usr.usr_last_name\
,      act.act_key\
,      act.act_name\
,      oiu.oiu_serviceaccount\
,      oiu.oiu_prov_start_date\
,      oiu.oiu_prov_end_date\
,      oiu.oiu_prov_mech_risk\
,      oiu.oiu_summary_risk\
,      oiu.oiu_item_risk\
,      oiu.oiu_open_sod_violation\
,      oiu.oiu_last_cert_decision\
,      oiu.oiu_last_cert_risk\
,      oiu.oiu_risk_update_date\
,      oiu.oiu_prov_by\
,      oiu.oiu_prov_mechanism\
,      obi.obi_key\
,      obi.obi_status\
,      obj.obj_key\
,      obj.obj_name\
,      obj.obj_type\
,      orc.orc_key\
,      orc.orc_create\
,      orc.orc_update\
,      orc.orc_status\
,      orc.orc_tos_instance_key\
,      orc_tasks_archived\
,      ost_status\
,      rownum AS rownumber \
FROM   obj obj\
,      oiu oiu\
,      orc orc\
,      obi obi\
,      ost ost\
,      usr usr\
,      act act\
,      app_instance app \
WHERE  ((oiu.obi_key = obi.obi_key) AND (usr.usr_key = oiu.usr_key) AND (act.act_key = usr.act_key) AND (oiu.orc_key = orc.orc_key) AND (oiu.ost_key = ost.ost_key) AND (obi.obj_key = obj.obj_key) AND (oiu.app_instance_key = app.app_instance_key(+)))

# entitlements provisioned
entitlement.provisioned=\
SELECT ent_assign.ent_assign_key\
,      ent_list.ent_list_key\
,      ent_list.ent_code\
,      ent_list.ent_value\
,      ent_list.ent_display_name\
,      ent_assign.ent_status\
,      ent_assign.valid_from_date\
,      ent_assign.valid_to_date\
,      ent_assign.ent_assign_prov_mechanism\
,      ent_assign.ent_assign_risk_update_date\
,      ent_assign.ent_assign_item_risk\
,      ent_assign.ent_assign_prov_mech_risk\
,      ent_assign.ent_assign_summary_risk\
,      obj.obj_key\
,      obj.obj_name\
,      oiu.oiu_key\
,      oiu.account_type\
,      orc.orc_key\
,      orc.orc_tos_instance_key\
,      usr.usr_key\
,      usr.usr_login\
,      usr.usr_first_name\
,      usr.usr_last_name\
,      act.act_key\
,      act.act_name\
,      rownum AS rownumber \
FROM   ent_list\
,      ent_assign\
,      oiu\
,      orc\
,      obi\
,      obj\
,      usr\
,      act \
WHERE  ((ent_assign.ent_list_key = ent_list.ent_list_key) AND (oiu.oiu_key = ent_assign.oiu_key) AND (obj.obj_key = obi.obj_key) AND (obi.obi_key = oiu.obi_key) AND (orc.orc_key = oiu.orc_key) AND (usr.act_key = act.act_key) AND (ent_assign.usr_key = usr.usr_key))

# reconciliation events
event.reconciled=\
SELECT rce.re_key\
,      rce.rj_key\
,      rce.rb_key\
,      rce.obj_key\
,      obj.obj_name\
,      rce.usr_key\
,      rce.act_key\
,      rce.orc_key\
,      rce.re_entity_type\
,      rce.re_change_type\
,      rce.re_status\
,      rce.re_link_source\
,      rce.re_note\
,      rce.re_reason\
,      rce.re_action_date\
,      rce.re_keyfield\
,      rownum AS rownumber \
FROM   recon_events rce\
,      obj          obj \
WHERE (rce.obj_key = obj.obj_key)

# reconciliation event histories
event.history=\
SELECT reh.rh_key\
,      reh.re_key\
,      reh.rh_status\
,      reh.rh_action_performed\
,      reh.rh_status\
,      reh.rh_note \
FROM   recon_history reh