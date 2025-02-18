-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\qms_msg.pkb
--
-- Generated for Oracle 10g on Tue Mar 30 08:58:19 2010 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Implementation 'qms$message'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE PACKAGE BODY qms$message IS
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private constants
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/* Revision label of package */
REVISION_LABEL               CONSTANT VARCHAR2( 8) := '2.0.0.0';

qms$severity_error_msg       CONSTANT VARCHAR2(10) := 'QMS-00152';
qms$severity_warning_msg     CONSTANT VARCHAR2(10) := 'QMS-00153';
qms$severity_information_msg CONSTANT VARCHAR2(10) := 'QMS-00154';
qms$severity_question_msg    CONSTANT VARCHAR2(10) := 'QMS-00155';
qms$severity_message_msg     CONSTANT VARCHAR2(10) := 'QMS-00156';

-- list of qms internal headstart messages
-- message are hardcoded here to avoid recursive calling of error procedures if
-- QMS-00101 or QMS-00102 are missing from the qms error tables.
qms$no_language             CONSTANT VARCHAR2(200) := 'Internal Error : Language preference not set';
qms$no_language_code        CONSTANT VARCHAR2(15)  := 'QMS-00101';
qms$no_message              CONSTANT VARCHAR2(200) := 'Internal Error : no error text for error <p1> in language <p2>';
qms$no_message_code         CONSTANT VARCHAR2(15)  := 'QMS-00102';
qms$default_language        CONSTANT VARCHAR2(3)   := 'en';
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private package variables
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
g_errorrec                  hil$message.hil$row_type;
g_debugflag                 BOOLEAN      := false;
-- package variables to hold translated severity strings
g_severity_language         VARCHAR2( 10);
g_error_string              VARCHAR2(200);
g_warning_string            VARCHAR2(200);
g_information_string        VARCHAR2(200);
g_message_string            VARCHAR2(200);
g_question_string           VARCHAR2(200);
g_translating_severities    BOOLEAN := false;
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private program units
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
-- Purpose  Populate package variables to hold translated severity strings
--------------------------------------------------------------------------------
PROCEDURE translate_severity(p_language IN VARCHAR2)
IS
  l_errorrec hil$message.hil$row_type;
  l_language VARCHAR2(10) := p_language;
BEGIN
  g_translating_severities := true;

  get_message(l_errorrec, qms$severity_error_msg, p_language => l_language);
  g_error_string := l_errorrec.text;

  get_message(l_errorrec, qms$severity_warning_msg, p_language => l_language);
  g_warning_string := l_errorrec.text;

  get_message(l_errorrec, qms$severity_information_msg, p_language => l_language);
  g_information_string := l_errorrec.text;

  get_message(l_errorrec, qms$severity_question_msg, p_language => l_language);
  g_question_string := l_errorrec.text;

  get_message(l_errorrec, qms$severity_message_msg, p_language => l_language);
  g_message_string := l_errorrec.text;

  g_severity_language      := p_language;
  g_translating_severities := false;
END translate_severity;
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- public program units
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
--
-- Purpose  Returns the revision label of this package
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION revision
  RETURN VARCHAR2
IS
BEGIN
  RETURN REVISION_LABEL;
END revision;
--------------------------------------------------------------------------------
--
-- Purpose  Returns translated description of severity (error, warning, etc.)
--
-- Usage    Call with message severity type as input: E, W, Q, I or M
--
-- Remarks
--
--------------------------------------------------------------------------------
FUNCTION severity_string(p_severity IN VARCHAR2)
RETURN VARCHAR2
IS
  l_string VARCHAR2(200);
BEGIN
  CASE p_severity
    WHEN 'E' THEN l_string := g_error_string;
    WHEN 'W' THEN l_string := g_warning_string;
    WHEN 'I' THEN l_string := g_information_string;
    WHEN 'Q' THEN l_string := g_question_string;
    WHEN 'M' THEN l_string := g_message_string;
  END CASE;
  RETURN l_string;
END severity_string;
--------------------------------------------------------------------------------
--
-- Purpose  Derive next unused message code within messages with prefix p_prefix
--          It is assumed that following the prefix are a '-' and then only
--          numbers up to a code length of qms$errors.qms$length
--
-- Usage    For instance from utilities that create new messages.
--
-- Remarks
--
--------------------------------------------------------------------------------
FUNCTION code_next(p_prefix in VARCHAR2)
  RETURN VARCHAR2
IS
  CURSOR c_msp(b_prefix IN VARCHAR2)
  IS
    SELECT NVL(MAX(TO_NUMBER(SUBSTR(msp.code, LENGTH(b_prefix) + 2))), 0) max_number
    FROM   qms_message_properties msp
    WHERE  SUBSTR(msp.code, 1, LENGTH(b_prefix) + 1) = b_prefix||'-'
    -- select only codes where rest is numbers only
    AND    translate(SUBSTR(msp.code, LENGTH(b_prefix) + 2), '0123456789', '9999999999')
         = SUBSTR('999999999999999', 1, LENGTH(SUBSTR(msp.code, LENGTH(b_prefix) + 2)))
    ;
   l_max_number    NUMBER(15,0);
   l_number_length NUMBER(2,0);
BEGIN
   -- get max used NUMBER
   OPEN  c_msp(UPPER(p_prefix));
   FETCH c_msp INTO l_max_number;
   CLOSE c_msp;
   -- derive length of NUMBER part from total length
   l_number_length := qms$errors.qms$length - LENGTH(p_prefix) - 1;
   -- concatenate everything
   RETURN UPPER(p_prefix)||'-'||lpad(to_char(l_max_number + 1), l_number_length, '0');
END code_next;
--------------------------------------------------------------------------------
--
-- Purpose  Return true (and p_found then contains the message code)
--          if specified message code or constraint exists in
--          qms_message_properties.
--
-- Usage    Either the message code or the constraint name must be specified.
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION lookup( p_code       IN  VARCHAR2 := null
               , p_constraint IN  VARCHAR2 := null
               , p_found      OUT VARCHAR2
               )
  RETURN BOOLEAN
IS
  CURSOR c_msp(b_code IN qms_message_properties.code%type, b_constraint_name IN qms_message_properties.constraint_name%type)
  IS
    SELECT msp.code
    FROM   qms_message_properties msp
    WHERE  msp.code = NVL(b_code, msp.code)
    AND    (b_constraint_name IS NULL OR msp.constraint_name = b_constraint_name);

  r_msp    c_msp%rowtype;
  l_exists BOOLEAN;
BEGIN
  IF  p_code IS NULL
  AND p_constraint IS NULL
  THEN -- one of these parameters should be specified
    RETURN NULL;
  END IF;

  OPEN  c_msp(UPPER(p_code), UPPER(p_constraint));
  FETCH c_msp INTO r_msp;
  l_exists := c_msp%found;
  CLOSE c_msp;

  IF l_exists
  THEN
    p_found := r_msp.code;
  END IF;

  RETURN l_exists;
END lookup;
--------------------------------------------------------------------------------
--
-- Purpose  Return true if specified message code or constraint exists in
--          qms_message_properties and a message text exists in the specified
--          language.
--
-- Usage    Either the message code or the constraint name must be specified.
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION exists( p_code       IN VARCHAR2 := null
               , p_constraint IN VARCHAR2 := null
               , p_language   IN VARCHAR2
               )
  RETURN BOOLEAN
IS
  CURSOR c_mst(b_code IN VARCHAR2, b_language IN VARCHAR2)
  IS
    SELECT 1
    FROM   qms_message_text mst
    WHERE  mst.code     = b_code
    AND    mst.language = b_language
    ;

  r_mst  c_mst%rowtype;
  l_code VARCHAR2(200);
BEGIN
  --
  -- check if message property exists
  --
  IF lookup( p_code       => p_code
           , p_constraint => p_constraint
           , p_found      => l_code
           )
  THEN
    -- message property exist
    OPEN  c_mst(l_code, p_language);
    FETCH c_mst INTO r_mst;
    IF c_mst%NOTFOUND
    THEN
      CLOSE c_mst;
      RETURN false;
    ELSE
      CLOSE c_mst;
      RETURN true;
    END IF;
  ELSE
    RETURN false;
  END IF;
END exists;



--
-- Purpose  returns all error/message information from qms_message_properties
--          and qms_message_text table for a specified error message or
--          constraint name.
--          Either the error code (p_errcode) or the constraint name must be specified
--
-- Usage    Parameters
--              p_error      : returns the error/message information in a PL/SQL record
--              p_errcode    : error/message code
--              p_constraint : constraint name
--              p_language   : language in which the message text is required
--
-- Remarks  -
--
PROCEDURE get_message( p_error      IN OUT hil$message.hil$row_type
                     , p_errcode    IN     VARCHAR2 := null
                     , p_constraint IN     VARCHAR2 := null
                     , p_language   IN     VARCHAR2 := 'en'
                     )
IS
  CURSOR c_error(s_code VARCHAR2, s_language VARCHAR2, s_constraint VARCHAR2)
  IS
    SELECT msp.code
    ,      msp.severity
    ,      msp.suppress_warning
    ,      msp.logging
    ,      mst.text
    ,      mst.help
    ,      NVL(mst.language, s_language)
    FROM   qms_message_properties msp
    ,      qms_message_text       mst
    WHERE  ((msp.code = s_code AND s_constraint IS NULL) OR (msp.constraint_name = s_constraint AND s_code IS NULL))
    AND    mst.language = s_language
    AND    msp.code     = mst.code;

  l_errcode    VARCHAR2(2000) := ltrim (p_errcode);
BEGIN
  -- check that translated severities strings are properly initialized
  IF (g_severity_language IS NULL
  OR  g_severity_language <> p_language)
  AND NOT g_translating_severities
  THEN
    translate_severity(p_language);
  END IF;

  -- verify that error text is not longer than 200 characters - truncate and give
  -- internal QMS error message for problem/tracking
  IF p_language IS NULL
  THEN
    p_error.code             := qms$no_language_code;
    p_error.text             := qms$no_language;
    p_error.severity         := 'E';
    p_error.severity_desc    := severity_string('E');
    p_error.suppress_warning := 'N';
    p_error.help             := null;
    p_error.language         := qms$default_language;
    p_error.raise_error      := true;
  ELSE
    OPEN  c_error(l_errcode, p_language, p_constraint);
    FETCH c_error
    INTO  p_error.code
    ,     p_error.severity
    ,     p_error.suppress_warning
    ,     p_error.logging
    ,     p_error.text
    ,     p_error.help
    ,     p_error.language;
    IF c_error%NOTFOUND
    THEN
      p_error.text             := qms$no_message;
      p_error.code             := qms$no_message_code;
      p_error.severity         := 'E';
      p_error.severity_desc    := severity_string('E');
      p_error.suppress_warning := 'N';
      p_error.help             := null;
      p_error.language         := p_language;
      p_error.raise_error      := true;
      IF p_constraint IS NULL
      THEN
        p_error.text := replace(p_error.text, '<p1>', p_errcode);
      ELSE
        p_error.text := replace(p_error.text, '<p1>', p_constraint);
      END IF;
      p_error.text := replace(p_error.text, '<p2>', p_language);
    ELSE
      p_error.severity_desc := severity_string(p_error.severity);
      IF p_error.severity IN ('E', 'W')
      THEN
        p_error.raise_error := true;
      ELSE
        p_error.raise_error := false;
      END IF;
    END IF;
    CLOSE c_error;
  END IF;
END get_message;

--
-- Purpose  Initialise stack with error that may not be displayed by retrieving
--          the records with suppress_always set to 'Y'
--
-- Usage    Procedure is called during package initialisation
--
-- Remarks  -
--
PROCEDURE get_suppress_messages(p_suppmesg IN OUT hil$message.hil$ind_type)
IS
  CURSOR c_suppresserror
  IS
    SELECT msp.code
    FROM   qms_message_properties msp
    WHERE  msp.suppress_always = 'Y';

  l_rowindex NUMBER (5, 0) := 0;
BEGIN
  p_suppmesg.delete;

  FOR r_suppresserror IN c_suppresserror
  LOOP
    l_rowindex := l_rowindex + 1;
    p_suppmesg(l_rowindex) := r_suppresserror.code;
  END LOOP;
END get_suppress_messages;
--------------------------------------------------------------------------------
--
-- Purpose  Returns all error/message information from qms$message_properties
--          and qms$message_text table for a specified oracle error message that
--          needs to be replaced at runtime.
--          These will be passed to the client and cached in a global
--          record_group on the client.
--
-- Usage    Parameters
--              p_error   : returns the error/message information in a PL/SQL
--                          record
--
-- Remarks  - Currently only ORA and FRM can be replaced.
--          - Only on the client side you can add/remove messages dynamically to
--            the stack
--
--------------------------------------------------------------------------------
PROCEDURE get_oracle_messages(p_errortab IN OUT hil$message.hil$table_type)
IS
  CURSOR c_oracle_error
  is
    SELECT msp.code
    ,      msp.severity
    ,      msp.suppress_warning
    ,      msp.suppress_always
    ,      msp.logging
    ,      mst.text
    ,      mst.help
    ,      mst.language
    FROM   qms_message_properties msp
    ,      qms_message_text       mst
    WHERE (msp.code like 'ORA%' or msp.code like 'FRM%')
    AND    msp.code = mst.code
    ;

  l_index NUMBER (5, 0) := 0;
BEGIN
  p_errortab.delete;
  FOR r_oracle_error IN c_oracle_error
  LOOP
    l_index := l_index + 1;
    p_errortab(l_index).code             := r_oracle_error.code;
    p_errortab(l_index).severity         := r_oracle_error.severity;
    p_errortab(l_index).severity_desc    := severity_string(r_oracle_error.severity);
    p_errortab(l_index).logging          := r_oracle_error.logging;
    p_errortab(l_index).suppress_warning := r_oracle_error.suppress_warning;
    p_errortab(l_index).suppress_always  := r_oracle_error.suppress_always;
    p_errortab(l_index).text             := r_oracle_error.text;
    p_errortab(l_index).help             := r_oracle_error.help;
    p_errortab(l_index).language         := r_oracle_error.language;
  END LOOP;
END get_oracle_messages;
--------------------------------------------------------------------------------
--
-- Purpose  Returns all error/message information from qms$message_properties
--          and qms$message_text table for a specified error message or
--          constraint name.
--          Either the error code (p_errcode) or the constraint name must be
--          specified
--
-- Usage    Parameters
--              p_error      : returns the error/message information in a PL/SQL
--                             record
--              p_errcode    : error/message code
--              p_constraint : constraint name
--              p_language   : language in which the message text is required
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE get_message( p_error      IN OUT hil$message.hil$row_type
                     , p_code       IN     VARCHAR2 := null
                     , p_constraint IN     VARCHAR2 := null
                     , p_language   IN     VARCHAR2 := 'en'
                     )
IS
  CURSOR c_error(s_code VARCHAR2, s_language VARCHAR2, s_constraint VARCHAR2)
  IS
    SELECT msp.code
    ,      msp.severity
    ,      msp.suppress_warning
    ,      msp.logging
    ,      mst.text
    ,      mst.help
    ,      NVL(mst.language, s_language)
    FROM   qms_message_properties msp
    ,      qms_message_text       mst
    WHERE  ((msp.code = s_code AND s_constraint IS NULL) OR (msp.constraint_name = s_constraint AND s_code IS NULL))
    AND    mst.language = s_language
    AND    msp.code     = mst.code;

  l_code  VARCHAR2(2000) := ltrim(p_code);
BEGIN
  -- check that translated severities strings are properly initialized
  IF (g_severity_language IS NULL
  OR  g_severity_language <> p_language)
  AND NOT g_translating_severities
  THEN
    translate_severity(p_language);
  END IF;

  -- verify that error text is not longer than 200 characters - truncate and
  -- give internal error message for problem/tracking
  IF p_language IS NULL
  THEN
    p_error.text             := qms$no_language;
    p_error.code             := qms$no_language_code;
    p_error.severity         := 'E';
    p_error.severity_desc    := severity_string('E');
    p_error.suppress_warning := 'N';
    p_error.help             := null;
    p_error.language         := qms$default_language;
    p_error.raise_error      := true;
  ELSE
    OPEN  c_error(l_code, p_language, p_constraint);
    FETCH c_error
    INTO  p_error.code
    ,     p_error.severity
    ,     p_error.suppress_warning
    ,     p_error.logging
    ,     p_error.text
    ,     p_error.help
    ,     p_error.language;
    IF c_error%NOTFOUND
    THEN
      p_error.code             := qms$no_message_code;
      p_error.severity         := 'E';
      p_error.severity_desc    := severity_string('E');
      p_error.suppress_warning := 'N';
      p_error.text             := qms$no_message;
      p_error.help             := null;
      p_error.language         := p_language;
      p_error.raise_error      := true;
      IF p_constraint IS NULL
      THEN
        p_error.text := replace(p_error.text, '<p1>', p_code);
      ELSE
        p_error.text := replace(p_error.text, '<p1>', p_constraint);
      END IF;
      p_error.text := replace(p_error.text, '<p2>', p_language);
    ELSE
      p_error.severity_desc := severity_string(p_error.severity);
      IF p_error.severity IN ('E', 'W')
      THEN
        p_error.raise_error := true;
      ELSE
        p_error.raise_error := false;
      END IF;
    END IF;
    CLOSE c_error;
  END IF;
END get_message;
--------------------------------------------------------------------------------
-- Purpose  Create record in message properties and first record in message text
--
-- Usage    From hil$message package
--
-- Remarks
--
--------------------------------------------------------------------------------
PROCEDURE create_message( p_code             qms_message_properties.code%type
                        , p_description      qms_message_properties.description%type
                        , p_language         qms_message_text.language%type
                        , p_text             qms_message_text.text%type
                        , p_help             qms_message_text.help%type                   DEFAULT NULL
                        , p_severity         qms_message_properties.severity%type         DEFAULT 'E'
                        , p_logging          qms_message_properties.logging%type          DEFAULT 'N'
                        , p_suppress_warning qms_message_properties.suppress_warning%type DEFAULT 'N'
                        , p_suppress_always  qms_message_properties.suppress_always%type  DEFAULT 'N'
                        , p_constraint_name  qms_message_properties.constraint_name%type  DEFAULT NULL
                        )
IS
BEGIN
  INSERT INTO qms_message_properties (
    code
  , description
  , severity
  , logging
  , suppress_warning
  , suppress_always
  , constraint_name
  ) VALUES (
    p_code
  , p_description
  , p_severity
  , p_logging
  , p_suppress_warning
  , p_suppress_always
  , p_constraint_name
  );

  add_language(p_code, p_language, p_text, p_help);
END create_message;
--------------------------------------------------------------------------------
-- Purpose  Delete message properties and all translations
--
-- Usage    from utilities
--
-- Remarks
--
--------------------------------------------------------------------------------
PROCEDURE delete_message(p_code IN VARCHAR2)
IS
BEGIN
  DELETE FROM qms_message_text
  WHERE code = p_code;

  DELETE FROM qms_message_properties
  WHERE code = p_code;
END delete_message;
--------------------------------------------------------------------------------
--
-- Purpose  Update the message code of a specific message
--
-- Usage    Called from headstart utility hsu_emsg
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE update_code( p_old IN VARCHAR2
                     , p_new IN VARCHAR2
                     )
IS
  CURSOR c_msp(b_code IN VARCHAR2)
  IS
    SELECT msp.description
    ,      msp.severity
    ,      msp.logging
    ,      msp.suppress_warning
    ,      msp.suppress_always
    ,      msp.constraint_name
    ,      msp.created_by
    ,      msp.created_on
    FROM   qms_message_properties msp
    WHERE  msp.code = b_code
    ;

  CURSOR c_mst(b_code IN VARCHAR2)
  is
    SELECT language
    ,      text
    ,      help
    ,      created_by
    ,      created_on
    FROM   qms_message_text mst
    WHERE  mst.code = b_code
    ;

  r_msp c_msp%rowtype;
  r_mst c_mst%rowtype;
BEGIN
  --
  -- The update of a message code is implemented via a delete and INSERT.
  -- This has to be done because the code is the primary key which is not updateable
  --
  OPEN   c_msp(b_code => p_old);
  FETCH  c_msp INTO r_msp;
  INSERT INTO qms_message_properties(
    code
  , description
  , severity
  , logging
  , suppress_warning
  , suppress_always
  , constraint_name
  , created_by
  , created_on
  , updated_by
  , updated_on
  ) VALUES (
    p_new
  , r_msp.description
  , r_msp.severity
  , r_msp.logging
  , r_msp.suppress_warning
  , r_msp.suppress_always
  , NULL -- needs to be null during INSERT because the old message has the same
    -- constraint name
  , r_msp.created_by
  , r_msp.created_on
  , user
  , sysdate
  );
  FOR r_mst IN c_mst(b_code => p_old)
  LOOP
    INSERT INTO qms_message_text (
      code
    , language
    , text
    , help
    , created_by
    , created_on
    , updated_by
    , updated_on
    ) VALUES (
      p_new
    , r_mst.language
    , r_mst.text
    , r_mst.help
    , r_mst.created_by
    , r_mst.created_on
    , user
    , sysdate
    );
    --
    DELETE FROM qms_message_text mst
    WHERE mst.code     = p_old
    AND   mst.language = r_mst.language
    ;
   END LOOP; -- r_mst
   CLOSE c_msp;

   DELETE FROM  qms_message_properties msp
   WHERE  msp.code = p_old
   ;
   -- update the constraint name that initially was set to null to prevent uniqueness
   -- conflicts with existing record
   UPDATE qms_message_properties msp
   SET    msp.constraint_name = r_msp.constraint_name
   WHERE  msp.code = p_new
   ;
END update_code;
--------------------------------------------------------------------------------
--
-- Purpose  Add translation in new language to existing message
--
-- Usage    From hil$message package
--
-- Remarks
--
--------------------------------------------------------------------------------
PROCEDURE add_language( p_code     qms_message_text.code%type
                      , p_language qms_message_text.language%type
                      , p_text     qms_message_text.text%type
                      , p_help     qms_message_text.help%type
                      )
IS
BEGIN
  INSERT INTO qms_message_text(
    code
  , language
  , text
  , help
  ) VALUES (
    p_code
  , p_language
  , p_text
  , p_help
  );
END add_language;
--------------------------------------------------------------------------------
--
-- Purpose  Returns the information of the last error taken from the stack
--
-- Usage    Called from the client to retrieve the QMS error information
--          after the error has been identified as a server side error.
--          Parameter
--               p_error : qms error record type
--
-- Remarks
--
--------------------------------------------------------------------------------
PROCEDURE pop_qms_errorinfo(p_error IN OUT hil$message.hil$row_type)
IS
BEGIN
  cg$errors.pop_qms_errorinfo(p_error);
END;

-- For some reason, we need to close the package body here.
-- Designer will remove it when it generates the .pkb file.
END qms$message;
/
SHOW ERROR
