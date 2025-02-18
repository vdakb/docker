CREATE TABLE prt_groupmembers (
  grm_name   VARCHAR2(32 CHAR) not null,
  grm_member VARCHAR2(32 CHAR) not null,
  grm_insert TIMESTAMP(6) default SYSTIMESTAMP,
  grm_id     NUMBER(10) -- not used
);