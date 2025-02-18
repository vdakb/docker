package bka.iam.identity.splunk;

import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;
import org.junit.jupiter.api.*;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SplunkConnectorTest {
    private static SplunkConfiguration config = new SplunkConfiguration();
    private static ObjectClass obj = new ObjectClass("__ACCOUNT__");
    static {
        config.setHost("SplunkServerDefaultCert");
        config.setPort(8089);
        config.setToken("eyJraWQiOiJzcGx1bmsuc2VjcmV0IiwiYWxnIjoiSFM1MTIiLCJ2ZXIiOiJ2MiIsInR0eXAiOiJzdGF0aWMifQ");
        config.setUsername("admin");
        config.setPassword("Password1");
        config.setSslEnabled(true);
    }
    private SplunkConnector connector;
    private static Uid uid;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws NoSuchAlgorithmException, KeyManagementException {
        connector = new SplunkConnector();
        connector.init(config);

        trustAllSSL();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        connector.dispose();
    }

    @Test
    @Order(1)
    void create_missingPassword() {
        Set<Attribute> attrs = getCreateAttrs();
        for (Attribute attr : attrs) {
            if (attr.getName().equals("password")) {
                attrs.remove(attr);
                break;
            }
        }

        Assertions.assertThrows(ConnectorException.class, () -> {
            connector.create(obj, attrs, null);
        });
    }

    @Test
    @Order(2)
    void create_success() {
        Set<Attribute> attrs = getCreateAttrs();
        uid = connector.create(obj, attrs, null);

        Assertions.assertNotNull(uid);
        Assertions.assertEquals(uid.getUidValue(), "intellij_test");
    }

    @Test
    @Order(3)
    void create_alreadyExists() {
        Set<Attribute> attrs = getCreateAttrs();
        Assertions.assertThrows(ConnectorException.class, () -> {
            connector.create(obj, attrs, null);
        });
    }

    @Test
    @Order(4)
    void update_success() {
        uid = SplunkConnector.buildUidForUserId("intellij_test");

        Set<Attribute> attrs = new HashSet<Attribute>();
        attrs.add(AttributeBuilder.build("realname", "intellij2"));

        uid = connector.update(obj, uid, attrs, null);
        Assertions.assertEquals(uid.getUidValue(), "intellij_test");
    }

    @Test
    @Order(5)
    void delete_success() {
        uid = SplunkConnector.buildUidForUserId("intellij_test");
        connector.delete(obj, uid, null);
    }

    @Test
    @Order(6)
    void delete_alreadyDeleted() {
        uid = SplunkConnector.buildUidForUserId("intellij_test");
        Assertions.assertThrows(ConnectorException.class, () -> {
            connector.delete(obj, uid, null);
        });
    }

    private static Set<Attribute> getCreateAttrs() {
        Set<Attribute> attrs = new HashSet<Attribute>();
        attrs.add(AttributeBuilder.build("name", "intellij_test"));
        attrs.add(AttributeBuilder.build("realname", "intellij"));
        attrs.add(AttributeBuilder.build("email", "intellij@ide.com"));
        attrs.add(AttributeBuilder.build("password", "Password1"));
        return attrs;
    }

    private static final void trustAllSSL() throws NoSuchAlgorithmException, KeyManagementException {

        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
}