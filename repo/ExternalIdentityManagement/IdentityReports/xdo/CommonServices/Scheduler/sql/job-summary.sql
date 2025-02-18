 SELECT job_name       AS name
 ,      status         AS status
 ,      job_start_time AS startTime
 ,      job_end_time   AS stopTime
 FROM   (
   SELECT job.id
   ,      job.job_name
   ,      DECODE(job.status, 2, 'Stopped', 5, 'Running', 6, 'Failed', 7, 'Interrupted', job.status) AS status
   ,      job.job_start_time
   ,      job.job_end_time
   FROM   job_history job
   WHERE  (NVL(:p_name,   'x') = 'x' OR job.job_name LIKE :p_name)
   AND    (NVL(:p_status, 'x') = 'x' OR job.status   =    :p_status)
   AND    TRUNC(job.job_start_time) BETWEEN NVL(:p_period_start, TO_DATE('01-JAN-1970', 'dd-MON-YYYY')) AND NVL(:p_period_end, TO_DATE(sysdate, 'dd-MON-YYYY'))
)
ORDER BY startTime,name
/