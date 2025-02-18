# catalog select
catalog.select=\
  SELECT cat.catalog_id          AS cat_id\
  ,      cat.entity_key          AS ent_id\
  ,      cat.entity_type         AS ent_type\
  ,      cat.entity_name         AS ent_name\
  ,      cat.entity_display_name AS ent_display_name\
  ,      cat.entity_description  AS ent_description\
  ,      cat.parent_entity_key   AS ent_key_parent\
  ,      cat.parent_entity_type  AS ent_type_parent\
  ,      cat.category            AS category\
  ,      cat.item_risk           AS item_risk\
  ,      cat.risk_update_date    AS risk_update_date\
  ,      cat.is_deleted          AS deleted\
  ,      cat.is_auditable        AS auditable\
  ,      cat.is_requestable      AS requestable\
  ,      cat.certifiable         AS certifiable\
  ,      cat.audit_objective     AS audit_objective\
  ,      cat.approver_user       AS approver_user\
  ,      apu.usr_login           AS apu_name\
  ,      apu.usr_display_name    AS apu_display_name\
  ,      cat.approver_role       AS approver_role\
  ,      apr.ugp_name            AS apr_name\
  ,      apr.ugp_display_name    AS apr_display_name\
  ,      cat.certifier_user      AS certifier_user\
  ,      ctu.usr_login           AS ctu_name\
  ,      ctu.usr_display_name    AS ctu_display_name\
  ,      cat.certifier_role      AS certifier_role\
  ,      ctr.ugp_name            AS ctr_name\
  ,      ctr.ugp_display_name    AS ctr_display_name\
  ,      cat.fulfillment_user    AS fulfillment_user\
  ,      ffu.usr_login           AS ffu_name\
  ,      ffu.usr_display_name    AS ffu_display_name\
  ,      cat.fulfillment_role    AS fulfillment_role\
  ,      ffr.ugp_name            AS ffr_name\
  ,      ffr.ugp_display_name    AS ffr_display_name\
  ,      cat.tags                AS tags\
  ,      cat.user_defined_tags   AS tags_udf\
  ,      rownum                  AS rownumber \
  FROM   catalog cat\
  ,      usr apu\
  ,      ugp apr\
  ,      usr ctu\
  ,      ugp ctr\
  ,      usr ffu\
  ,      ugp ffr \
  WHERE (cat.approver_user    = apu.usr_key(+) \
    AND  cat.approver_role    = apr.ugp_key(+) \
    AND  cat.certifier_user   = ctu.usr_key(+) \
    AND  cat.certifier_role   = ctr.ugp_key(+) \
    AND  cat.fulfillment_user = ffu.usr_key(+) \
    AND  cat.fulfillment_role = ffr.ugp_key(+))

# catalog user lookup
catalog.lookup.user=\
  SELECT usr.usr_key          AS ent_id\
  ,      usr.usr_login        AS ent_name\
  ,      usr.usr_display_name AS ent_display_name \
  FROM   usr usr

# catalog role lookup
catalog.lookup.role=\
  SELECT ugp.ugp_key          AS ent_id\
  ,      ugp.ugp_name         AS ent_name\
  ,      ugp.ugp_display_name AS ent_display_name \
  FROM   ugp ugp

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