/**
 ** The orphaned account summary report includes the accounts for the input
 ** resource where the account got deleted from the target system but existed
 ** in Oracle Identity Manager.
 */
SELECT obj.obj_name       AS "resourceName"
,      act.act_name       AS "organizationName"
,      usr.usr_login      AS "userLoginName"
,      usr.usr_first_name AS "userFirstName"
,      usr.usr_last_name  AS "userLastName"
,      rex.rex_create     AS "createdOn"
,      rex.rex_exception  AS "exception"
FROM   obj              obj
,      recon_exceptions rex
,      usr usr
,      act act
WHERE  obj.obj_key IN (
         SELECT oug.obj_key
         FROM   ugp ugp
         ,      usg usg
         ,      oug oug
         WHERE  ugp.ugp_key = usg.ugp_key
         AND    oug.ugp_key = ugp.ugp_key
         AND    usg.usr_key = (
                  SELECT adm.usr_key
                  FROM   usr adm
                  WHERE  adm.usr_login = UPPER(:xdo_user_name)
                )
       )
AND    (NVL(:p_resourceName, 'x') = 'x' OR (obj.obj_name LIKE :p_resourceName))
AND    rex.rex_obj_key = obj.obj_key
AND    rex.usr_key     = usr.usr_key
AND    act.act_key     = usr.act_key
AND    rex.rex_create BETWEEN :p_period_start AND :p_period_end
/