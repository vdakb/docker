/**
 ** This report provides administrators or auditors the ability to view a
 ** current snapshot of all the policies defined in the system, along with key
 ** information about each policy, and the number of instances in which each
 ** policy has been activated.
 **
 ** This report can be used for operational and compliance purposes.
 */
SELECT ugp.ugp_key          AS "roleKey"
,      ugp.ugp_name         AS "roleName"
,      ugp.ugp_display_name AS "roleDisplayName"
,      ugp.ugp_description  AS "roleDescription"
,      ugp.ugp_create       AS "createdOn"
,      cre.usr_login        AS "createdLoginName"
,      cre.usr_first_name   AS "createdFirstName"
,      cre.usr_last_name    AS "createdLastName"
,      ugp.ugp_update       AS "updatedOn"
,      upd.usr_login        AS "updatedLoginName"
,      upd.usr_first_name   AS "updatedFirstName"
,      upd.usr_last_name    AS "updatedLastName"
FROM   ugp            ugp
,      usr            cre
,      usr            upd
,      role_category  cat
WHERE  ugp.ugp_name IN (
         SELECT DISTINCT ugp.ugp_name
         FROM   usr usr
         ,      usg usg
         ,      ugp ugp
         WHERE  usr.usr_key(+) = usg.usr_key
         AND    usg.ugp_key(+) = ugp.ugp_key
         AND    (NVL(:p_name, 'x') = 'x' OR ugp.ugp_name LIKE :p_name)
       )
AND    ugp.ugp_createby          = cre.usr_key
AND    ugp.ugp_updateby          = upd.usr_key
AND    ugp.ugp_role_category_key = cat.role_category_key
AND    (nvl(:p_category, 'x') = 'x' OR cat.role_category_name LIKE :p_category)
ORDER BY ugp.ugp_name
/

SELECT pol.pol_key         AS "policyKey"
,      pol.pol_name        AS "policyName"
,      pol.pol_description AS "policyDescription"
,      pol.pol_priority    AS "policyPriority"
,      DECODE(pol.pol_request,         1, 'Yes', 'No') AS "policyApproval"
,      DECODE(pol.pol_retrofit_policy, 1, 'Yes', 'No') AS "policyRetrofit"
,      pol.pol_create      AS "createdOn"
,      NVL(usr.usr_first_name, ' ') || ' ' || NVL(usr.usr_middle_name, ' ') ||' ' || NVL(usr.usr_last_name,' ') AS "createdBy"
FROM   pol pol
,      usr usr
WHERE  (NVL(:p_policyName, 'x') = 'x' OR (pol.pol_name LIKE :p_policyName))
AND    pol.pol_createby         = usr.usr_key
AND    TRUNC(pol.pol_create) BETWEEN NVL(:p_period_start, TO_DATE('01-JAN-1907', 'dd-MON-YYYY')) AND NVL(:p_period_end, TO_DATE(sysdate, 'dd-MON-YYYY'))
ORDER BY pol.pol_name
/
/**
 ** Associated Roles
 */
SELECT ugp.ugp_name
FROM   ugp ugp
WHERE  ugp.ugp_key IN (
         SELECT pog.ugp_key
         FROM   pog pog
         ,      pol pol
         WHERE  pol.pol_key = :policyKey
         AND    pog.pol_key = pol.pol_key
       )
ORDER BY ugp.ugp_name
/
/**
 ** Allowed Resources
 */
SELECT app.app_instance_display_name AS "applicationAllowed"
FROM   app_instance app
WHERE  app.app_instance_key IN (
         SELECT pop.APP_INSTANCE_KEY
         FROM   pop pop
         ,      pol pol
         WHERE  pol.pol_key    = :policyKey
         AND    pop.pol_key    = pol.pol_key
         AND    pop.pop_denial = '0'
       )
ORDER BY "applicationAllowed"
/
/**
 ** Denied Resources
 */
SELECT app.app_instance_display_name AS "applicationDenied"
FROM   app_instance app
WHERE  app.app_instance_key IN (
         SELECT pop.APP_INSTANCE_KEY
         FROM   pop pop
         ,      pol pol
         WHERE  pol.pol_key    = :policyKey
         AND    pop.pol_key    = pol.pol_key
         AND    pop.pop_denial = '1'
       )
ORDER BY "applicationDenied"
/
