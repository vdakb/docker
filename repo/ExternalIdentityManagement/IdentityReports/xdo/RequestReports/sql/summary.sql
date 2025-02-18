SELECT out.request_key requestID
,      dbms_lob.substr(
         oim_report_utils.xl_sfg_returnmultiple(
           CURSOR(
             SELECT usr_login
             FROM (
               SELECT req.request_key     request_id
               ,      usr.usr_login       usr_login
               FROM   request             req
               ,      request_beneficiary ben
               ,      usr                 usr
               WHERE  req.request_key     = ben.request_key
               AND    ben.beneficiary_key = usr.usr_key
               UNION
               SELECT req.request_key request_id
               ,      usr.usr_login   usr_login
               FROM   request          req
               ,      request_entities re1
               ,      usr              usr
               WHERE  req.request_key = re1.request_key
               AND    re1.entity_key  = usr.usr_key
             )
             WHERE request_id = out.request_key
           )
           , 'Multiple'
         )
         , 4000
         , 1
       ) beneficiaryLoginName
FROM   request out
) user1