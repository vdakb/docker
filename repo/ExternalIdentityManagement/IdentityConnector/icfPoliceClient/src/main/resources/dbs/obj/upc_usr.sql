CREATE TABLE prt_users (
  usr_name         VARCHAR2(32 CHAR) not null, -- kennung
  usr_password     VARCHAR2(128 CHAR) not null,
  usr_description  VARCHAR2(1000 CHAR),  -- nachname
  usr_behoerde     VARCHAR2(100 CHAR),
  usr_email        VARCHAR2(100 CHAR),
  usr_telefon      VARCHAR2(50 CHAR),
  usr_pwd_change   CHAR(1 CHAR) default 'J' not null,
  usr_login_failed NUMBER(3) default 0 not null,
  usr_insert       TIMESTAMP(6) default SYSTIMESTAMP not null,
  usr_id           NUMBER(10) -- not used
  USR_LOCKED       CHAR(1 CHAR) Default 'N',
  USR_SURNAME      VARCHAR2(128 CHAR), -- vorname
  USR_LAST_LOGIN   TIMESTAMP(6),
  USR_DELETE_TST   TIMESTAMP(6),
  USR_UPDATE_TST   TIMESTAMP(6),
);