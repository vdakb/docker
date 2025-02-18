CREATE TABLE prt_groups (
  grp_name        VARCHAR2(32 CHAR) not null,
  grp_description VARCHAR2(1000 CHAR),
  grp_insert      TIMESTAMP(6) default SYSTIMESTAMP,
  grp_id          NUMBER(10) -- not used
);