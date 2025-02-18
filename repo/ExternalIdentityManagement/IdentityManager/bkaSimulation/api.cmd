SETLOCAL ENABLEDELAYEDEXPANSION

Rem
Rem Set Fusion Middleware Home and appropriate environment
Rem
set FMW=D:\Oracle\product\ide\12.2.1
set JRF=%FMW%\oracle_common\modules

Rem
Rem Build Callspath
Rem
set JARPATH=%JRF%/oracle.odl/ojdl.jar^
;%JRF%/javax.annotation.javax.annotation-api.jar^
;%JRF%/javax.inject.javax.inject.jar^
;%JRF%/javax.ws.rs.javax.ws.rs-api.jar^
;%JRF%/com.sun.jersey.jersey-core.jar^
;%JRF%/com.sun.jersey.jersey-client.jar^
;%JRF%/org.glassfish.hk2.hk2-api.jar^
;%JRF%/org.glassfish.hk2.hk2-utils.jar^
;%JRF%/org.glassfish.hk2.hk2-locator.jar^
;%JRF%/org.glassfish.jersey.core.jersey-common.jar^
;%JRF%/org.glassfish.jersey.core.jersey-client.jar^
;%JRF%/org.glassfish.jersey.ext.jersey-entity-filtering.jar^
;%JRF%/org.glassfish.jersey.bundles.repackaged.jersey-guava.jar^
;%JRF%/thirdparty/jackson-core-2.7.9.jar^
;%JRF%/thirdparty/jackson-databind-2.7.9.1.jar^
;%JRF%/thirdparty/jackson-annotations-2.7.9.jar^
;%JRF%/thirdparty/jackson-jaxrs-base-2.7.9.jar^
;%JRF%/thirdparty/jackson-module-jsonSchema-2.7.9.jar^
;%JRF%/thirdparty/jackson-jaxrs-json-provider-2.7.9.jar^
;%JRF%/oracle.dms/dms.jar^
;%JRF%/oracle.ucp.jar^
;%JRF%/oracle.jdbc/ojdbc8.jar^
;%JRF%/oracle.nlsrtl/orai18n-mapping.jar^
;..\..\IdentityTransition\lib\hst-foundation.jar

Rem -f orcl -h buster.cinnamonstar.net -s mdr.cinnamonstar.net -u igd_agt -w Sophie20061990$
java -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=8020,server=y,suspend=n -server -classpath %JARPATH%;.\lib\ocs-simulator.jar oracle.iam.system.simulation.apigee.Main %*
