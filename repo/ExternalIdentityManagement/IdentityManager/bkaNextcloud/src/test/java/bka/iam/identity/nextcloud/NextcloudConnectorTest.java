package bka.iam.identity.nextcloud;

import bka.iam.identity.nextcloud.model.UserDetails;
import bka.iam.identity.nextcloud.model.apitop.UserResponse;
import org.identityconnectors.common.security.GuardedString;
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
class NextcloudConnectorTest {
    private static NextcloudConfiguration config = new NextcloudConfiguration();
    private static ObjectClass obj = new ObjectClass("__ACCOUNT__");
    static {
        config.setHost("nextcloud");
        config.setPort(443);
        config.setUsername("admin");
        config.setPassword("P@sword123!");
        config.setSslEnabled(true);
    }
    private NextcloudConnector connector;
    private static Uid uid;
    private static String userid = "intellij_test";

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws NoSuchAlgorithmException, KeyManagementException {
        connector = new NextcloudConnector();
        connector.init(config);

        trustAllSSL();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        connector.dispose();
    }

    @Test
    @Order(1)
    void create_missingPasswordAndEmail() {
        Set<Attribute> attrs = getCreateAttrs();
        for (Attribute attr : attrs) {
            if (attr.getName().equals("__PASSWORD__")) {
                attrs.remove(attr);
                break;
            }
        }
        for (Attribute attr : attrs) {
            if (attr.getName().equals("email")) {
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
        Assertions.assertEquals(uid.getUidValue(), userid);
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
        UserDetails user = getUserDetailsFromAPI(userid);
        Assertions.assertNotEquals(user.getDisplayname(), "intellij Test Account");
        // ------ PRE CHECKS END

        uid = NextcloudConnector.buildUidForUserId(userid);

        Set<Attribute> attrs = new HashSet<Attribute>();
        attrs.add(AttributeBuilder.build("displayname", "intellij Test Account"));

        uid = connector.update(obj, uid, attrs, null);

        // ------ POST CHECKS
        Assertions.assertEquals(uid.getUidValue(), userid);
        user = getUserDetailsFromAPI(userid);
        Assertions.assertEquals(user.getDisplayname(), "intellij Test Account");
    }

    @Test
    @Order(5)
    void disable_success() {
        UserDetails user = getUserDetailsFromAPI(userid);
        Assertions.assertTrue(user.isEnabled());
        // ------ PRE CHECKS END

        uid = NextcloudConnector.buildUidForUserId(userid);

        Set<Attribute> attrs = new HashSet<Attribute>();
        attrs.add(AttributeBuilder.build("__ENABLE__", false));

        uid = connector.update(obj, uid, attrs, null);

        // ------ POST CHECKS
        Assertions.assertEquals(uid.getUidValue(), userid);
        user = getUserDetailsFromAPI(userid);
        Assertions.assertFalse(user.isEnabled());
    }

    @Test
    @Order(6)
    void enable_success() {
        UserDetails user = getUserDetailsFromAPI(userid);
        Assertions.assertFalse(user.isEnabled());
        // ------ PRE CHECKS END

        uid = NextcloudConnector.buildUidForUserId(userid);

        Set<Attribute> attrs = new HashSet<Attribute>();
        attrs.add(AttributeBuilder.build("__ENABLE__", true));

        uid = connector.update(obj, uid, attrs, null);

        // ------ POST CHECKS
        Assertions.assertEquals(uid.getUidValue(), userid);
        user = getUserDetailsFromAPI(userid);
        Assertions.assertTrue(user.isEnabled());
    }

    @Test
    @Order(7)
    void delete_success() {
        uid = NextcloudConnector.buildUidForUserId("intellij_test");
        connector.delete(obj, uid, null);
    }

    @Test
    @Order(8)
    void delete_alreadyDeleted() {
        uid = NextcloudConnector.buildUidForUserId("intellij_test");
        Assertions.assertThrows(ConnectorException.class, () -> {
            connector.delete(obj, uid, null);
        });
    }

    private UserDetails getUserDetailsFromAPI(String userid) {
        JsonParser parser = connector.pullUser(userid);
        return parser.as(UserResponse.class).getOcs().getData();
    }

    private static Set<Attribute> getCreateAttrs() {
        Set<Attribute> attrs = new HashSet<Attribute>();
        attrs.add(AttributeBuilder.build("__UID__", userid));
        attrs.add(AttributeBuilder.build("__NAME__", userid));
        attrs.add(AttributeBuilder.build("__PASSWORD__", new GuardedString("P@sword123!".toCharArray())));
        attrs.add(AttributeBuilder.build("displayname", "intellij"));
        attrs.add(AttributeBuilder.build("email", "intellij@ide.com"));
        attrs.add(AttributeBuilder.build("phone", "+447767007579"));
        attrs.add(AttributeBuilder.build("language", "de"));
        attrs.add(AttributeBuilder.build("locale", "de_DE"));
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