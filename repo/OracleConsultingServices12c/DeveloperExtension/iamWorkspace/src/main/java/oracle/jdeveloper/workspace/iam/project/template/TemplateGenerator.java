/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facilities

    File        :   TemplateGenerator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    TemplateGenerator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15  2012-02-06  DSteding    TemplateFile and TemplateStream
                                               handled specific to allow the
                                               load of XML descriptor files
                                               itself in the previewer.
    11.1.1.3.37.60.38  2012-02-06  DSteding    TemplateFile and TemplateStream
                                               handled specific to allow the
                                               load of ADF descriptor files
                                               itself in the previewer.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.project.template;

import java.util.Map;
import java.util.HashMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.text.MessageFormat;

import java.io.File;
import java.io.Reader;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import oracle.javatools.dialogs.MessageDialog;

import oracle.ide.Ide;

import oracle.ide.dialogs.ProgressBar;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.model.Property;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

////////////////////////////////////////////////////////////////////////////////
// class TemplateGenerator
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Utility used to maintain our custom ANT build file hierarchy.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class TemplateGenerator implements Runnable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String              START          = "#{";
  public static final String              VALUE          = "{0}";
  public static final String              SUFFIX         = "." + VALUE + "}";

  /**
   ** This regular expression uses only one groups.
   ** <p>
   ** Groups are defined by parentheses. Note that ?: will define a group as
   ** "non-contributing"; that is, it will not contribute to the return values
   ** of the <code>group</code> method.
   */
  public static final String              PATTERN_GROUP  = "#\\{((?:\\w|\\s)+)\\.((?:\\w|\\s|\\.)+)\\}";

  /**
   ** This text set will replace a match regular expresssion if the property
   ** that is desired by the expression cannot be resolved.
   */
  private static final String              PATTERN_ERROR = "???{0}.{1}-{2}???";

  protected static final String            ADF_TAG       = "adf";
  protected static final String            ADF_KEY       = START + ADF_TAG  + SUFFIX;
  protected static final String            ANT_TAG       = "ant";
  protected static final String            ANT_KEY       = START + ANT_TAG  + SUFFIX;
  protected static final String            DIP_TAG       = "dip";
  protected static final String            DIP_KEY       = START + DIP_TAG  + SUFFIX;
  protected static final String            FMW_TAG       = "fmw";
  protected static final String            FMW_KEY       = START + FMW_TAG  + SUFFIX;
  protected static final String            IAM_TAG       = "iam";
  protected static final String            IAM_KEY       = START + IAM_TAG  + SUFFIX;
  protected static final String            JDK_TAG       = "jdk";
  protected static final String            JDK_KEY       = START + JDK_TAG + SUFFIX;
  protected static final String            JEE_TAG       = "jee";
  protected static final String            JEE_KEY       = START + JEE_TAG  + SUFFIX;
  protected static final String            MDS_TAG       = "mds";
  protected static final String            MDS_KEY       = START + MDS_TAG  + SUFFIX;
  protected static final String            OAM_TAG       = "oam";
  protected static final String            OAM_KEY       = START + OAM_TAG  + SUFFIX;
  protected static final String            OCS_TAG       = "ocs";
  protected static final String            OCS_KEY       = START + OCS_TAG  + SUFFIX;
  protected static final String            OES_TAG       = "oes";
  protected static final String            OES_KEY       = START + OES_TAG  + SUFFIX;
  protected static final String            OID_TAG       = "oid";
  protected static final String            OID_KEY       = START + OID_TAG  + SUFFIX;
  protected static final String            OIM_TAG       = "oim";
  protected static final String            OIM_KEY       = START + OIM_TAG  + SUFFIX;
  protected static final String            ORA_TAG       = "ora";
  protected static final String            ORA_KEY       = START + ORA_TAG + SUFFIX;
  protected static final String            OVD_TAG       = "ovd";
  protected static final String            OVD_KEY       = START + OVD_TAG  + SUFFIX;
  protected static final String            SCA_TAG       = "sca";
  protected static final String            SCA_KEY       = START + SCA_TAG  + SUFFIX;
  protected static final String            SOA_TAG       = "soa";
  protected static final String            SOA_KEY       = START + SOA_TAG  + SUFFIX;
  protected static final String            SCP_TAG       = "scp";
  protected static final String            SCP_KEY       = START + SCP_TAG  + SUFFIX;
  protected static final String            WKS_TAG       = "wks";
  protected static final String            WKS_KEY       = START + WKS_TAG  + SUFFIX;

  private static final Map<String, String> DICTIONARY    = new HashMap<String, String>();

  /** the class loader to stream the template files. */
  private static final ClassLoader         LOADER        = TemplateGenerator.class.getClassLoader();

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    DICTIONARY.put(ADF_TAG, ADF_KEY);
    DICTIONARY.put(ANT_TAG, ANT_KEY);
    DICTIONARY.put(DIP_TAG, DIP_KEY);
    DICTIONARY.put(FMW_TAG, FMW_KEY);
    DICTIONARY.put(IAM_TAG, IAM_KEY);
    DICTIONARY.put(JDK_TAG, JDK_KEY);
    DICTIONARY.put(JEE_TAG, JEE_KEY);
    DICTIONARY.put(MDS_TAG, MDS_KEY);
    DICTIONARY.put(OAM_TAG, OAM_KEY);
    DICTIONARY.put(OCS_TAG, OCS_KEY);
    DICTIONARY.put(OES_TAG, OES_KEY);
    DICTIONARY.put(OID_TAG, OID_KEY);
    DICTIONARY.put(OIM_TAG, OIM_KEY);
    DICTIONARY.put(ORA_TAG, ORA_KEY);
    DICTIONARY.put(OVD_TAG, OVD_KEY);
    DICTIONARY.put(SCA_TAG, SCA_KEY);
    DICTIONARY.put(SCP_TAG, SCP_KEY);
    DICTIONARY.put(SOA_TAG, SOA_KEY);
    DICTIONARY.put(WKS_TAG, WKS_KEY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean                   cancelled;

  /** Progress bar used to thread the "long running" transfer process. */
  private ProgressBar               view;

  private int                       size;
  private int                       range;

  private TemplateFolder            root;

  // If you will be using a particular regular expression often, should create
  // a Pattern object to store the regular expression.
  // You can then reuse the regex as often as you want by reusing the Pattern
  // object.
  private Pattern                   pattern;

  // If you will be using a particular regular expression often, should create
  // a Pattern object to store the regular expression.
  // You can then reuse the regex as often as you want by reusing the Pattern
  // object.
  private String                    template;

  // the flat substitution dictionary mapped on-to-one
  private final Map<String, String> substitution = new HashMap<String, String>();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>TemplateGenerator</code> the will generate the
   ** buildfile hierarchy starting at the {@link TemplateData}
   ** <code>root</code>.
   */
  public TemplateGenerator() {
    // ensure inheritance
    super();

    // initalize instance attributes
    this.view = new ProgressBar(Ide.getMainWindow(), TemplateBundle.string(TemplateBundle.DIALOG_TITLE), this, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>TemplateGenerator</code> the will generate the
   ** buildfile hierarchy starting at the {@link TemplateFolder}
   ** <code>root</code>.
   **
   ** @param  root               the descriptors of the Oracle JDeveloper
   **                            libraries to generate.
   */
  public TemplateGenerator(final TemplateFolder root) {
    // ensure inheritance
    this();

    // initalize instance attributes
    this.root = root;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   run (Runnable)
  /**
   ** To be called in a separately executing  thread.
   ** <p>
   ** The general contract of the method <code>run</code> is that it may take
   ** any action what ever.
   */
  public synchronized void run() {
    try {
      generate(this.root);
    }
    finally {
      stopProgress();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preview
  /**
   ** Generates a ANT build file for preview.
   **
   ** @param  data               the {@link TemplateStream} path to preview.
   **
   ** @return                    the string with the substituted palceholders.
   */
  public static final String preview(final TemplateStream data) {
    final TemplateGenerator generator = new TemplateGenerator();
    generator.parse(LOADER.getResourceAsStream(data.template()));
    // substitute all expressions and store it in the target file location
    return generator.replace(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preview
  /**
   ** Generates a XML descriptor file for preview.
   **
   ** @param  data               the {@link TemplateFile} path to preview.
   **
   ** @return                    the template string containing palceholders.
   */
  public static final String preview(final TemplateFile data) {
    final TemplateGenerator generator = new TemplateGenerator();
    generator.parse(new File(data.template()));
    // substitute all expressions and store it in the target file location
    return generator.template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: startProgress
  /**
   ** Starts the ProgressBar with a default range (0-100).
   */
  public final void startProgress() {
    this.size  = count(this.root);
    this.range = 0;
    this.view.start(null, null, this.range, 100);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: stopProgress
  /**
   ** Stops the ProgressBar.
   */
  public final void stopProgress() {
    this.view.setDoneStatus();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   count
  /**
   ** Returns the amount of build files the passed in folder depends on.
   **
   ** @param  folder             the buildfile folder provider to count.
   **
   ** @return                    the amount of build files the passed in folder
   **                            depends on.
   */
  protected int count(final TemplateFolder folder) {
    int i = 0;
    for (TemplateData dependant : folder.dependant()) {
      // don't count the file if it's not selected
      if (dependant.hotspotSelected())
        i++;
    }

    for (TemplateFolder hierarchy : folder.hierarchy())
      i += count(hierarchy);

    return i;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generates the ANT build folder.
   **
   ** @param  folder             the ANT build folder provider to generate.
   */
  protected void generate(final TemplateFolder folder) {
    // iterate over all templates that depend from the folder
    for (TemplateData dependant : folder.dependant())
      generate(dependant);

    // iterate over the hierarchy of folders
    for (TemplateFolder hierarchy : folder.hierarchy())
      generate(hierarchy);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generates the ANT build file.
   **
   ** @param  data             the ANT build file data provider to generate.
   */
  protected void generate(final TemplateData data) {
    // don't touch the file if it's already exists and it has not to be
    // maintained manually
    if (data.hotspotSelected()) {
      if (data instanceof TemplateStream) {
        // check the resource stream at first to avoid exceptions if the
        // template is not packaged correctly in the underlying java archive
        final InputStream stream = LOADER.getResourceAsStream(data.template());
        if (stream != null)
          generate((TemplateStream)data, stream);
        else
          // display an error alert about that the template could not be loaded
          // from the resources.
          MessageDialog.error(
            Ide.getMainWindow()
          , ComponentBundle.format(ComponentBundle.TEMPALTE_INTERNAL_FAILED, data.template())
          , ComponentBundle.string(ComponentBundle.TEMPALTE_TITLE)
          , null
          );
      }
      else {
        try {
          // check the resource stream at first to avoid exceptions if the template
          // is not packaged correctly in the underlying java archive
          final InputStream stream = new FileInputStream(data.template());
          generate((TemplateFile)data, stream);
        }
        catch (FileNotFoundException e) {
          // display an error alert about that the template could not be loaded
          // from the resources.
          MessageDialog.error(
            Ide.getMainWindow()
          , ComponentBundle.format(ComponentBundle.TEMPALTE_EXTERNAL_FAILED, data.template())
          , ComponentBundle.string(ComponentBundle.TEMPALTE_TITLE)
          , null
          );
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: updateProgress
  /**
   ** Use this method to update the {@link ProgressBar} to indicate the
   ** progress made in the long-running process.
   ** <p>
   ** Use this method only when the indicator used is the JProgressBar not the
   ** BusyBar.
   **
   ** @param  progressText       the text to display just above the progress
   **                            indicator. If a <code>null</code> string or
   **                            zero-length string is passed in the text will
   **                            not be updated.
   ** @param  stepText           the text to display just below the progress
   **                            indicator. If a <code>null</code> string or
   **                            zero-length string is passed in the text will
   **                            not be updated.
   */
  protected final void updateProgress(final String progressText, final String stepText) {
    this.checkNotCanceled();
    this.view.updateProgress(progressText, stepText);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: updateProgress
  /**
   ** Use this method to update the {@link ProgressBar} to indicate the
   ** progress made in the long-running process.
   ** <p>
   ** Use this method only when the indicator used is the JProgressBar not the
   ** BusyBar.
   **
   ** @param  completionStatus   the amount of progress that has been made.
   ** @param  progressText       the text to display just above the progress
   **                            indicator. If a <code>null</code> string or
   **                            zero-length string is passed in the text will
   **                            not be updated.
   ** @param  stepText           the text to display just below the progress
   **                            indicator. If a <code>null</code> string or
   **                            zero-length string is passed in the text will
   **                            not be updated.
   */
  private final void updateProgress(final int completionStatus, final String progressText, final String stepText) {
    this.checkNotCanceled();
    this.view.updateProgress(completionStatus, progressText, stepText);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: checkNotCanceled
  /**
   ** Check if a user has canceled the process.
   */
  private final void checkNotCanceled() {
    if (this.cancelled)
      throw new RuntimeException("Canceled");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generates a ANT build file for the project.
   **
   ** @param  data               the {@link TemplateStream} path to create.
   ** @param  template           the {@link InputStream} to the file that will
   **                            be used as the template for the file to
   **                            generate.
   */
  private void generate(final TemplateStream data, final InputStream template) {
    int completion = this.range / this.size * 100;
    updateProgress(completion, null, TemplateBundle.format(TemplateBundle.BUILDFILE_PARSE_TEMPLATE, data.text()));
    parse(template);
    // substitute all expressions and store it in the target file location
    updateProgress(null, TemplateBundle.format(TemplateBundle.BUILDFILE_SUBSTITUTE_PARAM, data.text()));
    final String content = replace(data);
    updateProgress(null, TemplateBundle.format(TemplateBundle.BUILDFILE_WRITE_TEMPLATE, data.text()));
    write(data, content);
    this.range++;
    completion = this.range / this.size * 100;
    updateProgress(completion, null, TemplateBundle.format(TemplateBundle.BUILDFILE_PARSE_TEMPLATE, data.text()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generates a ANT build file for the project.
   **
   ** @param  data               the {@link TemplateFile} path to create.
   ** @param  template           the {@link InputStream} to the file that will
   **                            be used as the template for the file to
   **                            generate.
   */
  private void generate(final TemplateFile data, final InputStream template) {
    int completion = this.range / this.size * 100;
    updateProgress(completion, null, TemplateBundle.format(TemplateBundle.BUILDFILE_PARSE_TEMPLATE, data.text()));
    parse(template);
    updateProgress(null, TemplateBundle.format(TemplateBundle.BUILDFILE_WRITE_TEMPLATE, data.text()));
    write(data, this.template);
    this.range++;
    completion = this.range / this.size * 100;
    updateProgress(completion, null, TemplateBundle.format(TemplateBundle.BUILDFILE_PARSE_TEMPLATE, data.text()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Creates the {@link Map} with all placeholders from the given subject.
   ** <p>
   ** A placeholder in generell is specified by
   ** <code>#{<i>placeholder name</i>}</code>.
   **
   ** @param  file               the {@link File} path to the file that will
   **                            be used as the template for parsing.
   */
  private void parse(final File file) {
    try {
      // fetch the template from the local file system and converts it to a
      this.template = readTemplate(new FileInputStream(file));
      if (StringUtility.empty(this.template))
        return;
    }
    catch (FileNotFoundException e) {
      this.template = null;
      // display an error alert about that the template could not be loaded
      // from the resources.
      MessageDialog.error(
        Ide.getMainWindow()
      , ComponentBundle.format(ComponentBundle.TEMPALTE_EXTERNAL_FAILED, file.getAbsolutePath())
      , ComponentBundle.string(ComponentBundle.TEMPALTE_TITLE)
      , null
      );
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Creates the {@link Map} with all placeholders from the given subject.
   ** <p>
   ** A placeholder in generell is specified by
   ** <code>#{<i>placeholder name</i>}</code>.
   **
   ** @param  stream             the {@link InputStream} to the file that will
   **                            be used as the template for parsing.
   */
  private void parse(final InputStream stream) {
    // fetch the template from the local file system and converts it to a
    this.template = readTemplate(stream);
    if (StringUtility.empty(this.template))
      return;

    this.pattern  = Pattern.compile(PATTERN_GROUP, Pattern.MULTILINE);

    // To use the regular expression on a string, create a Matcher object by
    // calling pattern.matcher() passing the subject string to it. The Matcher
    // will do the actual searching, replacing or splitting.
    Matcher match = this.pattern.matcher(this.template);
    while (match.find()) {
      if (match.groupCount() > 0) {
        // Capturing parentheses are numbered 1..groupCount() group number
        // zero is the entire regex match
        final String dictionaryGroup = match.group(1);
        if (!StringUtility.empty(dictionaryGroup)) {
          // check if placeholder prefix is well known
          if (DICTIONARY.containsKey(dictionaryGroup)) {
            final String keyPattern = DICTIONARY.get(dictionaryGroup);
            // add a composed key entry with an empty value to the substitution
            // mapping
            this.substitution.put(keyPattern.replace(VALUE, match.group(2)), StringUtility.EMPTY);
          }
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readTemplate
  /**
   ** Fetchs the template from the local file system and converts it to a
   ** string.
   **
   ** @param  stream             the {@link InputStream} to the file that will
   **                            be used as the template for parsing.
   **
   ** @return                    the content of the template.
   */
  private String readTemplate(final InputStream stream) {
    // prevent bogus input
    if (stream == null)
      return null;

    try {
      return readTemplate(new InputStreamReader(stream, "UTF-8"));
    }
    catch (UnsupportedEncodingException e) {
      // display an error alert about that the template could not be loaded
      // from the resources.
      MessageDialog.error(
        Ide.getMainWindow()
      , ComponentBundle.format(ComponentBundle.TEMPALTE_ENCODING_ERROR, e.getLocalizedMessage())
      , ComponentBundle.string(ComponentBundle.TEMPALTE_TITLE)
      , null
      );
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readTemplate
  /**
   ** Fetchs the template from the local file system and converts it to a
   ** string.
   **
   ** @param  reader             the {@link Reader} to the file that will
   **                            be used as the template for parsing.
   **
   ** @return                    the content of the template.
   */
  private String readTemplate(final Reader reader) {
    // we will read the stream in chunks of 1024 bytes
    final char[]       buffer   = new char[1024];
    final StringBuffer template = new StringBuffer();
    int size;
    try {
      do {
        size = reader.read(buffer, 0, buffer.length);
        if (size > 0) {
          template.append(buffer, 0, size);
        }
      } while (size >= 0);
      return template.toString();
    }
    catch (IOException e) {
      // display an error alert about that the template could not be loaded
      // from the resources.
      MessageDialog.error(
        Ide.getMainWindow()
      , ComponentBundle.format(ComponentBundle.TEMPALTE_READING_ERROR, e.getLocalizedMessage())
      , ComponentBundle.string(ComponentBundle.TEMPALTE_TITLE)
      , null
      );
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Substitutes the placeholders with the values form the specified mapping
   ** and returns the resulting string.
   ** <p>
   ** A placeholder in generell is specified by
   ** <code>#{<i>placeholder name</i>}</code>.
   **
   ** @param  data               the abstract {@link TemplateData} path to
   **                            create.
   **
   ** @return                    the string with the substituted palceholders.
   **
   ** @throws IllegalStateException if this method is invoked before the
   **                               template has been parsed.
   */
  private String replace(final TemplateStream data) {
    // prevent bogus state
    if (this.pattern == null)
      throw new IllegalStateException(TemplateBundle.string(TemplateBundle.REWRITE_BEFORE_PARSE));

    StringBuffer replaced  = new StringBuffer();

    // To use the regular expression on a string, create a Matcher object by
    // calling pattern.matcher() passing the subject string to it. The Matcher
    // will do the actual searching, replacing or splitting.
    Matcher match = this.pattern.matcher(this.template);
    while (match.find()) {
      if (match.groupCount() > 0) {
        // Capturing parentheses are numbered 1..groupCount() group number zero
        // is the entire regex match
        final String dictionaryGroup = match.group(1);
        if (dictionaryGroup != null) {
          if (this.substitution.containsKey(match.group())) {
            final String   providerKey = match.group().substring(START.length(), match.group().length() - 1);
            final Property property    = data.property(providerKey);
            if (property != null) {
              final Object providerValue = property.value();
              if (providerValue == null)
                match.appendReplacement(replaced, replacementError(match.group(1), match.group(2), "null"));
              else if (providerValue instanceof File)
                match.appendReplacement(replaced, ((File)providerValue).getPath().replaceAll("\\\\", "/"));
              else {
                // may be the property value contains regx character like $
                // which has a special meaning, therefor we have to handle it
                // specially
                String replacement = providerValue.toString();
                replacement = replacement.replaceAll("\\$", "\\\\\\$");
                match.appendReplacement(replaced, replacement);
              }
            }
            // in all other cases we mark the tag as unresolvable and should be
            // replaced manually
            else {
              match.appendReplacement(replaced, replacementError(match.group(1), match.group(2), "unresolvable"));
            }
          }
        }
      }
    }
    // write the remaining text to the buffer
    match.appendTail(replaced);
    return replaced.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Store the template in the local file system.
   **
   ** @param  data               the {@link TemplateData} to the file in the
   **                            underlying file system.
   ** @param  content            the content of the template.
   */
  private void write(final TemplateData data, final String content) {

    // if the requested path to the file location does not exist yet create the
    // path
    File folder = data.folder().folder();
    if (!folder.exists())
      folder.mkdirs();

    OutputStream stream = null;
    try {
      // create a byte buffer that is huge enough to take the entire file content
      stream = new BufferedOutputStream(new FileOutputStream(data.file()));
      stream.write(content.getBytes());
    }
    catch (IOException e) {
      // display an error alert about that the template could not be written
      // to the resources.
      MessageDialog.error(
        Ide.getMainWindow()
      , ComponentBundle.format(ComponentBundle.TEMPALTE_WRITING_ERROR, e.getLocalizedMessage())
      , ComponentBundle.string(ComponentBundle.TEMPALTE_TITLE)
      , null
      );
    }
    finally {
      if (stream != null)
        try {
          stream.flush();
          stream.close();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replacemantError
  /**
   ** Returns text set will replace a match regular expresssion if the property
   ** that is desired by the expression cannot be resolved.
   **
   ** @param  matchIdentifier    the main key word of the regualr expression
   **                            where the dismatch has occured.
   ** @param  matchProperty      the property key word of the regualr expression
   **                            where the dismatch has occured.
   ** @param  suffix             the appendix that specifies the error.
   **
   ** @return                    text set will replace a match regular
   **                            expresssion if the property that is desired by
   **                            the expression cannot be resolved.
   */
  private String replacementError(final String matchIdentifier, final String matchProperty, final String suffix) {
    final Object[] parameter = {matchIdentifier, matchProperty, suffix};
    return MessageFormat.format(PATTERN_ERROR, parameter);
  }
}