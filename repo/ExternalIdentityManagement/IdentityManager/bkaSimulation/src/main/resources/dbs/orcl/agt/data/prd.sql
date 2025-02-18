INSERT INTO agt_products (
  rowversion
, created_by
, updated_by
, org
, name
, displayname
, description
, uris
, proxy
, environment
, timequota
, timeinterval
, timeunit
)
VALUES (
  '00000000'
, 'apigee@bka.bund.de'
, 'apigee@bka.bund.de'
, 'apigee'
, 'GISPOC-mitquota'
, 'GISPOC-mitquota'
, ''
, '/,/**'
, 'GISPoC'
, 'prod'
, '5'
, '10'
, 'minute'
)
/
INSERT INTO agt_products (
  rowversion
, created_by
, updated_by
, org
, name
, displayname
, description
, uris
, proxy
, scope
, environment
, timequota
, timeinterval
, timeunit
)
VALUES (
  '00000000'
, 'apigee@bka.bund.de'
, 'apigee@bka.bund.de'
, 'apigee'
, 'GIS - nur Vorwahlen'
, 'GIS - keine Auswahl'
, ''
, '/**,/,'
, 'gis'
, 'none'
, 'prod'
, '5'
, '10'
, 'minute'
)
/
INSERT INTO agt_products (
  rowversion
, created_by
, updated_by
, org
, name
, displayname
, description
, uris
, proxy
, scope
, environment
, timequota
, timeinterval
, timeunit
)
VALUES (
  '00000000'
, 'apigee@bka.bund.de'
, 'apigee@bka.bund.de'
, 'apigee'
, 'GIS - alles'
, 'GIS - alles'
, ''
, '/**,/,'
, 'gis'
, 'all'
, 'prod'
, ''
, ''
, ''
)
/
INSERT INTO agt_products (
  rowversion
, created_by
, updated_by
, org
, name
, displayname
, description
, uris
, proxy
, scope
, environment
, timequota
, timeinterval
, timeunit
)
VALUES (
  '00000000'
, 'apigee@bka.bund.de'
, 'apigee@bka.bund.de'
, 'apigee'
, 'GISPOC-reduziert'
, 'GISPOC-reduziert'
, ''
, ''
, 'GIS-PoC'
, ''
, 'prod'
, ''
, ''
, ''
)
/
INSERT INTO agt_products (
  rowversion
, created_by
, updated_by
, org
, name
, displayname
, description
, uris
, proxy
, scope
, environment
, timequota
, timeinterval
, timeunit
)
VALUES (
  '00000000'
, 'apigee@bka.bund.de'
, 'apigee@bka.bund.de'
, 'apigee'
, 'GISPOC-komplett'
, 'GISPOC-komplett'
, ''
, '/**,/'
, 'GIS-PoC'
, ''
, 'prod'
, ''
, ''
, ''
)
/
