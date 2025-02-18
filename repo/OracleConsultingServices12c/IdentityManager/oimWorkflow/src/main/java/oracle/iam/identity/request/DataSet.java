package oracle.iam.identity.request;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

public class DataSet {

  public static final String              declaredTypeClass;
  public static final QName               qname;

  // this map holds mapping between design console widget to the widget
  // supported in 11gR1 request engine
  public static final Map<String, String> widget_mapping = new HashMap<String, String>();

  // this map holds mapping of the variable in forms to variable in request
  // Datasets
  public static final Map<String, String> variant_mapping = new HashMap<String, String>();
  public static Map<String, String>       properties = null;
  public static Map<String, String>       configMap = null;

  static {
    variant_mapping.put("byte",                 "Byte");
    variant_mapping.put("double",               "Double");
    variant_mapping.put("int",                  "Integer");
    variant_mapping.put("String",               "String");
    variant_mapping.put("short",                "Short");
    variant_mapping.put("long",                 "Long");
    variant_mapping.put("Date",                 "Date");
    variant_mapping.put("boolean",              "Boolean");
    variant_mapping.put("byte[]",               "ByteArray");

    widget_mapping.put("TextField",             "text");
    widget_mapping.put("RadioButton",           "radio");
    widget_mapping.put("PasswordField",         "text");
    // as per bug 7564037
    widget_mapping.put("LookupField",           "lookup");
    widget_mapping.put("TextArea",              "textarea");

    widget_mapping.put("DateFieldDlg",          "date");
    widget_mapping.put("DOField",               "text");

    widget_mapping.put("ComboBox",              "dropdown");
    widget_mapping.put("CheckBox",              "checkbox");
    widget_mapping.put("ITResourceLookupField", "text");

    declaredTypeClass = "oracle.iam.requestDataSetGeneration.jaxb.RequestDataSet";

    qname = QName.valueOf("http://www.example.org}request-data-set");

  }

  public DataSet() {
    super();
  }
}
