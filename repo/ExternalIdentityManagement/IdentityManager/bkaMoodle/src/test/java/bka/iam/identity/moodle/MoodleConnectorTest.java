package bka.iam.identity.moodle;

import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.exceptions.UnknownUidException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;
import org.junit.jupiter.api.*;

import java.util.HashSet;
import java.util.Set;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MoodleConnectorTest {
    private static MoodleConfiguration config = new MoodleConfiguration();
    private static ObjectClass obj = new ObjectClass("__ACCOUNT__");
    static {
        config.setHost("172.30.250.194");
        config.setPort(80);
        config.setToken("4c5a59d6f5777b4d145a851622baefbd");
        config.setSslEnabled(false);
    }

    private MoodleConnector connector;
    private static Uid uid;

    @BeforeAll
    static void setUpAll() {
        uid = null;
    }

    @BeforeEach
    void setUp() {
        connector = new MoodleConnector();
        connector.init(config);
    }

    @AfterEach
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
        Assertions.assertInstanceOf(Integer.class, Integer.valueOf(uid.getUidValue()));
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
    void delete_success() {
        connector.delete(obj, uid, null);
    }

    @Test
    @Order(5)
    void delete_noSuchId() {
        Assertions.assertThrows(UnknownUidException.class, () -> {
            connector.delete(obj, uid, null);
        });
    }

    private static Set<Attribute> getCreateAttrs() {
        Set<Attribute> attrs = new HashSet<Attribute>();
        attrs.add(AttributeBuilder.build("username", "intellij_test"));
        attrs.add(AttributeBuilder.build("firstname", "in"));
        attrs.add(AttributeBuilder.build("lastname", "tellij"));
        attrs.add(AttributeBuilder.build("email", "intellij@ide.com"));
        attrs.add(AttributeBuilder.build("password", "P@ssword1"));
        return attrs;
    }
}