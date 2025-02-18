package bka.iam.identity.moodle;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ObjectClass;

import java.util.HashSet;
import java.util.Set;

public class TempTest {
    public static void main(String[] args) {
        MoodleConfiguration config = new MoodleConfiguration();
        config.setHost("172.30.250.194");
        config.setPort(80);
        config.setToken("4c5a59d6f5777b4d145a851622baefbd");
        config.setSslEnabled(false);

        ObjectClass obj = new ObjectClass("__ACCOUNT__");
        Set<Attribute> attrs = new HashSet<Attribute>();
        attrs.add(AttributeBuilder.build("username", "intellij"));
        attrs.add(AttributeBuilder.build("firstname", "in"));
        attrs.add(AttributeBuilder.build("lastname", "tellij"));
        attrs.add(AttributeBuilder.build("email", "intellij@ide.com"));
        attrs.add(AttributeBuilder.build("password", "P@ssword1"));

        MoodleConnector c = new MoodleConnector();
        c.init(config);
        c.create(obj, attrs, null);

        System.out.println("Done");
    }
}
