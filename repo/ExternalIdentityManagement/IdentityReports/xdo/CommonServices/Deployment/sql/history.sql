SELECT eih.eih_key           AS history
,      eif.eif_filename      AS fileName
,      DECODE(
         eih.eih_operation
       , 'I', 'Import'
       , 'E', 'Export'
       , 'Oooops'
       )                     AS operation
,      eih.eih_status        AS status
,      eih.eih_start_date    AS started
,      eih.eih_end_date      AS stopped
,      eif.eif_fileexptstamp AS exported
,      eif.eif_description   AS description
,      eif.eif_version       AS version
FROM   eif eif
,      eih eih
WHERE  eif.eif_key = eih.eih_key
AND    (NVL(:p_filename,  'x') = 'x' OR eif_filename      LIKE :p_filename)
AND    (NVL(:p_operation, 'x') = 'x' OR eih.eih_operation =    :p_operation)
AND    (NVL(:p_status,    'x') = 'x' OR eih.eih_status    =    :p_status)
AND    TRUNC(eih.eih_start_date) BETWEEN NVL(:p_period_start, TO_DATE('01-JAN-1970', 'dd-MON-YYYY')) AND NVL(:p_period_end, TO_DATE(sysdate, 'dd-MON-YYYY'))
/

SELECT eis.eih_key         AS history
,      eis.eis_type        AS type
,      eis.eis_source_name AS source
,      eis.eis_target_name AS target
FROM   eis eis
WHERE  eis.eih_key = :history
/

SELECT eio.eih_key        AS history
,      eio.eio_type       AS type
,      eio.eio_objectname AS object
,      eio.eio_resolution AS resolution
FROM   eio eio
WHERE  eio.eih_key = :history
AND    (NVL(:p_name, 'x') = 'x' OR eio.eio_objectname LIKE :p_name)
AND    (NVL(:p_type, 'x') = 'x' OR eio.eio_type       LIKE :p_type)
/