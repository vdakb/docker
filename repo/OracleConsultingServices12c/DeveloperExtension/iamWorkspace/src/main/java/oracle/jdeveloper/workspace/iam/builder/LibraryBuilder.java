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
    Subsystem   :   Identity and Access Management Facility

    File        :   LibraryBuilder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    LibraryBuilder.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.builder;

import java.util.Map;
import java.util.List;
import java.util.Stack;
import java.util.HashMap;
import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.net.MalformedURLException;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import oracle.ide.Ide;

import oracle.ide.net.URLPath;
import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;

import oracle.javatools.dialogs.MessageDialog;
import oracle.javatools.dialogs.ExceptionDialog;

import oracle.ide.dialogs.ProgressBar;

import oracle.jdeveloper.library.JLibrary;
import oracle.jdeveloper.library.JLibraryNode;
import oracle.jdeveloper.library.JLibraryList;
import oracle.jdeveloper.library.JLibraryManager;

import oracle.jdeveloper.workspace.iam.Bundle;

import oracle.jdeveloper.workspace.iam.xml.SAXInput;

import oracle.jdeveloper.workspace.iam.utility.FileSystem;
import oracle.jdeveloper.workspace.iam.utility.ClassUtility;
import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.builder.library.Path;
import oracle.jdeveloper.workspace.iam.builder.library.Item;
import oracle.jdeveloper.workspace.iam.builder.library.Library;
import oracle.jdeveloper.workspace.iam.builder.library.ItemType;
import oracle.jdeveloper.workspace.iam.builder.library.Libraries;
import oracle.jdeveloper.workspace.iam.builder.library.ObjectFactory;

////////////////////////////////////////////////////////////////////////////////
// class LibraryBuilder
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Utility used by maintain the product and framework libraries in
 ** Oracle JDeveloper.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class LibraryBuilder implements Runnable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String ELEMENT_LIBRARIES  = "libraries";
  private static final String ELEMENT_LIBRARY    = "library";
  private static final String ELEMENT_CLASSPATH  = "classpath";
  private static final String ELEMENT_SOURCEPATH = "sourcepath";
  private static final String ELEMENT_JAVADOC    = "javadocpath";
  private static final String ELEMENT_ITEM       = "item";

  private static final String ATTRIBUTE_ID       = "id";
  private static final String ATTRIBUTE_NAME     = "name";
  private static final String ATTRIBUTE_TYPE     = "type";
  private static final String ATTRIBUTE_FILE     = "file";
  private static final String ATTRIBUTE_PATH     = "path";
  private static final String ATTRIBUTE_RELEASE  = "releaseAware";

  private static final List<String> IGNORE       = new ArrayList<String>();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String              title;
  private final String              descriptor;
  private final String              release;
  private final URL                 searchBase;
  private final boolean             recursive;

  private       Progress            progress;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    // exclude all stuff that in the SubVersion directories
    IGNORE.add(".svn");
    // exclude all stuff that in the compile output directory
    IGNORE.add("bin");
    // exclude all stuff that in the source code directory
    IGNORE.add("src");
    // exclude all stuff that we are synchronize in the local deployment folders
    IGNORE.add("deployment");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Progress
  // ~~~~~ ~~~~~~~~
  /**
   ** The view presented in the UI to visualize the progress of generation.
   */
  class Progress implements Runnable {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private boolean       cancelled;

    /** Progress bar used to thread the "long running" transfer process. */
    private ProgressBar   view;

    private List<Library> library;

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a new {@link Progress} the will generate the Oracle
     ** JDeveloper libraries specified by the {@link List} <code>payload</code>.
     ** <br>
     ** This constructor is protected to prevent other classes to use
     ** "new Progress()".
     **
     ** @param  payload          the descriptors of the Oracle JDeveloper
     **                          libraries to generate.
     */
    protected Progress(final List<Library> payload) {
      // ensure inheritance
      super();

      // initalize instance attributes
      this.library = payload;
      this.view    = new ProgressBar(Ide.getMainWindow(), title, this, true);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   run (Runnable)
    /**
     ** To be called in a separately executing  thread.
     ** <p>
     ** The general contract of the method <code>run</code> is that it may take
     ** any action whatsoever.
     ** <p>
     **  Compose the {@link URL} of a JAR file that will be assigned to a library.
     */
    public synchronized void run() {
      try {
        for (int i = 0; i < this.library.size(); i++)
          LibraryBuilder.this.maintainLibrary(i, this.library.get(i));
      }
      catch (IOException e) {
        ExceptionDialog.showExceptionDialog(Ide.getMainWindow(), e);
      }
      finally {
        stopProgress();
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: startProgress
    /**
     ** Starts the ProgressBar with a default range (0-100).
     */
    protected final void startProgress() {
      this.view.start(null, null);//, 0, this.library.size());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: stopProgress
    /**
     ** Stops the ProgressBar.
     */
    protected final void stopProgress() {
      this.view.setDoneStatus();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: updateProgress
    /**
     ** Use this method to update the {@link ProgressBar} to indicate the
     ** progress made in the long-running process.
     ** <p>
     ** Use this method only when the indicator used is the JProgressBar not the
     ** BusyBar.
     **
     ** @param  completionStatus the amount of progress that has been made.
     ** @param  progressText     the text to display just above the progress
     **                          indicator. If a <code>null</code> string or
     **                          zero-length string is passed in the text will
     **                          not be updated.
     ** @param  stepText         the text to display just below the progress
     **                          indicator. If a <code>null</code> string or
     **                          zero-length string is passed in the text will
     **                          not be updated.
     */
    protected final void updateProgress(final int completionStatus, final String progressText, final String stepText) {
      this.view.updateProgress(completionStatus, progressText, stepText);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: updateProgress
    /**
     ** Use this method to update the {@link ProgressBar} to indicate the
     ** progress made in the long-running process.
     ** <p>
     ** Use this method only when the indicator used is the JProgressBar not the
     ** BusyBar.
     **
     ** @param  progressText     the text to display just above the progress
     **                          indicator. If a <code>null</code> string or
     **                          zero-length string is passed in the text will
     **                          not be updated.
     ** @param  stepText         the text to display just below the progress
     **                          indicator. If a <code>null</code> string or
     **                          zero-length string is passed in the text will
     **                          not be updated.
     */
    protected final void updateProgress(final String progressText, final String stepText) {
      this.view.updateProgress(progressText, stepText);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: checkNotCancelled
    /**
     ** Check if a user has canceled the process.
     */
    protected final void checkNotCancelled() {
      if (this.cancelled)
        throw new RuntimeException("Canceled");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Grammar
  // ~~~~ ~~~~~~~
  /**
   ** This enum store the grammar's constants.
   */
  enum Grammar {
      /** the encoded valid grammar state transitions in the parser. */
      INIT(new Grammar[0], StringUtility.EMPTY)
    , LIBRARIES(INIT,                                      ELEMENT_LIBRARIES)
    , LIBRARY(LIBRARIES,                                   ELEMENT_LIBRARY)
    , CLASSPATH(LIBRARY,                                   ELEMENT_CLASSPATH)
    , SOURCEPATH(LIBRARY,                                  ELEMENT_SOURCEPATH)
    , JAVADOC(LIBRARY,                                     ELEMENT_JAVADOC)
    , ITEM(new Grammar[] {CLASSPATH, SOURCEPATH, JAVADOC}, ELEMENT_ITEM)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Map<String, Grammar> lookup = new HashMap<String, Grammar>();

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** array of parent states to this one. */
    final Grammar parents[];

    /** the name of the tag for this state. */
    final String  tag;

    ////////////////////////////////////////////////////////////////////////////
    // static init block
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** This code executes after the enums have been constructed.
     ** <p>
     ** Because of order of execution when initializing an enum, you can't call
     ** static functions in an enum constructor. (They are constructed before
     ** static initialization).
     ** <p>
     ** Instead, we use a static initializer to populate the lookup map after
     ** all the enums are constructed.
      */
    static {
      for (Grammar state : Grammar.values()) {
        lookup.put(state.tag, state);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Grammar</code> with a single parent state.
     **
     ** @param  parent           the predecessor where this grammar state can
     **                          occure within.
     ** @param  tag              the logical name of this grammar state to
     **                          lookup.
     */
    Grammar(final Grammar parent, final String tag) {
      this(new Grammar[]{parent}, tag);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Grammar</code> with a multiple parent states.
     **
     ** @param  parents          the predecessors where this grammar state can
     **                          occure within.
     ** @param  tag              the logical name of this grammar state to
     **                          lookup.
     */
    Grammar(final Grammar[] parents, String tag) {
      this.parents = parents;
      this.tag     = tag;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: lookup
    /**
     ** Lookups the <code>Grammar</code> for the specified a tag name.
     **
     ** @param  tag              the name of the tag the <code>Grammar</code>
     **                          is requested for.
     **
     ** @return                  the <code>Grammar</code> for that tag.
     */
    public static Grammar lookup(final String tag) {
      return lookup.get(tag);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   transition
    /**
     ** Checks whether it is valid to transition to the specified state from
     ** this state.
     **
     ** @param  state
     **
     ** @return
     */
    public boolean transition(final Grammar state) {
      if (this.equals(state))
        return true;

      for (int i = 0; i < state.parents.length; i++) {
        if (state.parents[i].equals(this))
          return true;
      }
      for (int i = 0; i < this.parents.length; i++) {
        if (this.parents[i].equals(state))
          return true;
      }
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Parser
  // ~~~~~ ~~~~~~
  /**
   ** Handles parsing of a XML file which defines the mapping descriptor.
   */
  private class Parser extends SAXInput {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private Libraries            descriptor = new Libraries();
    private ObjectFactory        factory    = new ObjectFactory();
    private Grammar              cursor     = Grammar.INIT;
    private final Stack<Grammar> state      = new Stack<Grammar>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Parser</code>.
     **
     ** @throws SAXException     in case {@link SAXInput} is not able to create
     **                          an appropriate parser.
     */
    private Parser()
      throws SAXException {

      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: startElement (overridden)
    /**
     ** Receive notification of the beginning of an element.
     ** <p>
     ** The Parser will invoke this method at the beginning of every element in
     ** the XML document; there will be a corresponding
     ** {@link #endElement(String, String, String) endElement} event for every
     ** startElement event (even when the element is empty). All of the
     ** element's content will be reported, in order, before the corresponding
     ** endElement event.
     ** <p>
     ** This event allows up to three name components for each element:
     ** <ol>
     **   <li>the Namespace URI;</li>
     **   <li>the local name; and</li>
     **   <li>the qualified (prefixed) name.</li>
     ** </ol>
     ** Any or all of these may be provided, depending on the values of the
     ** <code>http://xml.org/sax/features/namespaces</code> and the
     ** <code>http://xml.org/sax/features/namespace-prefixes</code> properties:
     ** <ul>
     **   <li>the Namespace URI and local name are required when the namespaces
     **       property is <code>true</code> (the default), and are optional when
     **       the namespaces property is <code>false</code> (if one is
     **       specified, both must be);
     **   <li>the qualified name is required when the namespace-prefixes
     **       property is <code>true</code>, and is optional when the
     **       namespace-prefixes property is <code>false</code> (the default).
     ** </ul>
     ** Note that the attribute list provided will contain only attributes with
     ** explicit values (specified or defaulted):
     **   #IMPLIED attributes will be omitted.
     **   The attribute list will contain attributes used for Namespace
     **   declarations (xmlns* attributes) only if the
     **   <code>http://xml.org/sax/features/namespace-prefixes</code> property
     **   is <code>true</code> (it is <code>false</code> by default, and support
     **   for a <code>true</code> value is optional).
     ** <p>
     ** Like {@link #characters(char[], int, int) characters()}, attribute
     ** values may have characters that need more than one <code>char</code>
     ** value.
     **
     ** @param  uri              the Namespace URI, or the empty string if the
     **                          element has no Namespace URI or if Namespace
     **                          processing is not being performed.
     ** @param  localName        the local name (without prefix), or the empty
     **                          string if Namespace processing is not being
     **                          performed
     ** @param  qualifiedName    the qualified name (with prefix), or the empty
     **                          string if qualified names are not available.
     ** @param  attributes       the attributes attachd to the element.
     **                          If there are no attributes, it shall be an
     **                          empty {@link Attributes} object. The value of
     **                          this object after startElement returns is
     **                          undefined.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception.
     **
     ** @see    #endElement(String, String, String)
     ** @see    org.xml.sax.Attributes
     ** @see    org.xml.sax.helpers.AttributesImpl
     */
    @Override
    public void startElement(final String uri, final String localName, final String qualifiedName, final Attributes attributes)
      throws SAXException {

      // check for state transition
      Grammar state = Grammar.lookup(localName);
      if (state == null) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), localName };
        throw new SAXException(LibraryBuilderBundle.format(LibraryBuilderBundle.PARSE_ELEMENT_UNKNOWN, arguments));
      }
      // we know it's a valid tag
      if (!this.cursor.transition(state)) {
        // invalid transition
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), this.cursor.toString(), state.toString() };
        throw new SAXException(LibraryBuilderBundle.format(LibraryBuilderBundle.PARSE_TRANSITION, arguments));
      }
      // change FSA state
      this.state.push(this.cursor);
      this.cursor = state;
      switch (this.cursor) {
        case LIBRARIES  : break;
        case LIBRARY    : push(this.factory.createLibrary(attributes.getValue(ATTRIBUTE_ID), attributes.getValue(ATTRIBUTE_NAME)));
                          break;
        case CLASSPATH  :
        case SOURCEPATH :
        case JAVADOC    : push(this.factory.createPath());
                          break;
        case ITEM       : final ItemType type = ItemType.fromValue(attributes.getValue(ATTRIBUTE_TYPE));
                          push(this.factory.createItem(type, attributes.getValue(ATTRIBUTE_FILE), attributes.getValue(ATTRIBUTE_PATH), Boolean.valueOf(attributes.getValue(ATTRIBUTE_RELEASE))));
                          break;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endElement (overridden)
    /**
     ** Receive notification of the end of an element.
     ** <p>
     ** The SAX parser will invoke this method at the end of every element in
     ** the XML document; there will be a corresponding
     ** {@link #startElement(String, String, String, Attributes) startElement} event for every endElement event
     ** (even when the element is empty).
     ** <p>
     ** For information on the names, see
     ** {@link #startElement (String, String, String, Attributes)}.
     **
     ** @param  uri              the Namespace URI, or the empty string if the
     **                          element has no Namespace URI or if Namespace
     **                          processing is not being performed.
     ** @param  localName        the local name (without prefix), or the empty
     **                          string if Namespace processing is not being
     **                          performed
     ** @param  qualifiedName    the qualified name (with prefix), or the empty
     **                          string if qualified names are not available.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception
     */
    @Override
    public void endElement(final String uri, final String localName, final String qualifiedName)
      throws SAXException {

      // dispatch to handling method
      switch (this.cursor) {
        case LIBRARIES  : break;
        case LIBRARY    : // remove the collection provider for pathes and
                          // their transformations from the value stack
                          final Library library = (Library)pop();
                          // put the Library to the right element
                          this.descriptor.getLibrary().add(library);
                          break;
        case CLASSPATH  : // remove the collection provider for classpath from
                          // the value stack
                          final Path clazz = (Path)pop();
                          // put the path to the right element
                          ((Library)peek()).setClasspath(clazz);
                          break;
        case SOURCEPATH : // remove the collection provider for source path from
                          // the value stack
                          final Path source = (Path)pop();
                          // put the path to the right element
                          ((Library)peek()).setSourcepath(source);
                          break;
        case JAVADOC    : // remove the collection provider for javadoc path
                          // from the value stack
                          final Path javadoc = (Path)pop();
                          // put the path to the right element
                          ((Library)peek()).setJavadocpath(javadoc);
                          break;
        case ITEM       : // remove the collection provider for pathes and
                          // their transformations from the value stack
                          final Item item = (Item)pop();
                          // put the path to the right element
                          ((Path)peek()).getItem().add(item);
                          break;
      }
      // change FSA state
      this.cursor = this.state.pop();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LibraryBuilder</code> that will manage Oracle
   ** JDeveloper uUser Libraries from the specified {@link InputStream}.
   **
   ** @param  title              the string to display as the title of the
   **                            {@link ProgressBar} shown during the generation
   **                            process.
   ** @param  descriptor         the file name of the descriptor to maintain.
   ** @param  release            the release extension of the descriptor.
   ** @param  searchBase         the path to the file system to search for the
   **                            files specified  by <code>descriptor</code>.
   */
  protected LibraryBuilder(final String title, final String descriptor, final String release, final URL searchBase) {
    // ensure inhertitance
    this(title, descriptor, release, searchBase, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LibraryBuilder</code> that will manage Oracle
   ** JDeveloper uUser Libraries from the specified {@link InputStream}.
   **
   ** @param  title              the string to display as the title of the
   **                            {@link ProgressBar} shown during the genaration
   **                            process.
   ** @param  descriptor         the file name of the descriptor to maintain.
   ** @param  release            the release extension of the descriptor.
   ** @param  searchBase         the path to the file system to search for the
   **                            files specified  by <code>descriptor</code>.
   ** @param  recursive          <code>true</code> if the search should by done
   **                            also in the directories beneath
   **                            <code>root</code>.
   */
  protected LibraryBuilder(final String title, final String descriptor, final String release, final URL searchBase, final boolean recursive) {
    // ensure inhertitance
    super();

    // initialize instance attributes
    this.title      = title;
    this.descriptor = descriptor;
    this.searchBase = searchBase;
    this.release    = release;
    this.recursive  = recursive;
 }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   run (Runnable)
  /**
   ** To be called in a separately executing thread.
   ** <p>
   ** The general contract of the method <code>run</code> is that it may take
   ** any action whatever.
   ** <p>
   ** Compose the {@link URL} of a JAR file that will be assigned to a library.
   */
  public synchronized void run() {
    final String      file   = composeDescriptor(this.descriptor, this.release);
    final InputStream stream = FileSystem.getInputStream(file);
    if (stream == null) {
      MessageDialog.error(Ide.getMainWindow(), Bundle.format(Bundle.PARSER_FILE_NOTEXISTS, file), Bundle.string(Bundle.GENERAL_HEADER), "???");
      return;
    }
    try {
      final Parser parser = new Parser();
      parser.processDocument(new InputSource(stream));
      this.progress = new Progress(parser.descriptor.getLibrary());
    }
    catch (Exception e) {
      ExceptionDialog.showExceptionDialog(Ide.getMainWindow(), e);
    }
    this.progress.startProgress();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   composeDescriptor
  /**
   ** Returns an instance of this {@link LibraryBuilder}.
   **
   ** @param  descriptor         the feature specific filename of a descriptor.
   ** @param  release            the release number to complete the filename of
   **                            a library descriptor.
   **
   ** @return                    the complete filename of a libarry descriptor
   */
  public static String composeDescriptor(final String descriptor, final String release) {
    if (StringUtility.empty(release))
      return String.format("%s.%s", descriptor, ClassUtility.XML);
    else
      return String.format("%s-%s.%s", descriptor, release, ClassUtility.XML);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   composeRoot
  /**
   ** Compose the {@link URL} path of a JAR file that will be assigned to a
   ** library.
   **
   ** @param  root               the path to the file system to search for the
   **                            file specified  by <code>library</code>.
   ** @param  feature            the  path to the Java Archives specific for a
   **                            featured component.
   ** @param  release            the path to the Java Archives specific for a
   **                            releass of a featured component.
   **
   ** @return                    the {@link URL} path of a JAR file that will be
   **                            assignable to a library.
   */
  public static URL composeRoot(final URL root, final String feature, final String release) {
    // compose the final path the library files will be searched within
    URL path = root;

    // extend the root path with the feature related subfolder
    if (!StringUtility.empty(feature)) {
      path = URLFactory.newDirURL(path, feature);
      // extend the path with specific release if requested
      if (!StringUtility.empty(release))
        path = URLFactory.newDirURL(path, release);
    }
    return path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   maintainLibrary
  /**
   ** Compose the {@link URL}s of an Oracle JDeveloper Library.
   **
   ** @param  completionStatus   the amount of progress that has been made.
   ** @param  descriptor         the {@link Library} to build the paths.
   **
   ** @throws IOException        an I/O exception of some sort occurred.
   */
  private void maintainLibrary(int completionStatus, final Library descriptor)
    throws IOException {

    JLibrary library = createLibrary(completionStatus, descriptor);

    final String subject = descriptor.getName();
    this.progress.updateProgress(null, LibraryBuilderBundle.format(LibraryBuilderBundle.BUILD_CLASSPATH, subject));
    library.setClassPath(buildPath(descriptor.getClasspath()));
    this.progress.updateProgress(null, LibraryBuilderBundle.format(LibraryBuilderBundle.BUILD_SOURCEPATH, subject));
    library.setSourcePath(buildPath(descriptor.getSourcepath()));
    this.progress.updateProgress(null, LibraryBuilderBundle.format(LibraryBuilderBundle.BUILD_JAVADOCPATH, subject));
    library.setDocPath(buildPath(descriptor.getJavadocpath()));
    library.setDeployedByDefault(false);
    // Saves the contents of the Node.
    ((JLibraryNode)library).save();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLibrary
  /**
   ** Compose the {@link JLibrary}.
   **
   ** @param  completionStatus   the amount of progress that has been made.
   ** @param  descriptor         the {@link Library} the Oracle
   **                            JDeveloper User Library will be created for.
   **
   ** @return                    the {@link JLibrary} wrapper to a Oracle
   **                            KDeveloper User library.
   */
  private JLibrary createLibrary(final int completionStatus, final Library descriptor) {
    final String physicalName = descriptor.getId();
    final String logicalName  = descriptor.getName();
    this.progress.updateProgress(completionStatus, LibraryBuilderBundle.format(LibraryBuilderBundle.GENERATING, logicalName), LibraryBuilderBundle.format(LibraryBuilderBundle.CREATE_BEGIN, logicalName, physicalName));

    final JLibraryList usr = JLibraryManager.getUserLibraries();
    final URL          id  = (URL)usr.createIDFromName(physicalName, false);
    JLibrary           lib = usr.findLibrary(id);
    // we must remove the library direct before we can change it due to the lack
    // of update capabilties of a library
    if (lib != null)
      usr.remove(lib);

    lib = usr.addLibrary(logicalName, id);
    this.progress.updateProgress(null, LibraryBuilderBundle.format(LibraryBuilderBundle.CREATE_FINISHED, logicalName));
    return lib;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildPath
  /**
   ** Build the path the library comprise.
   **
   ** @param  element            the List of {@link Item}s to search
   **
   ** @return                    the {@link URLPath} containing all files
   **                            specified by <code>fileName</code> that was
   **                            found within {@link #searchBase}.
   */
  private URLPath buildPath(final Path element) {
    final URLPath path = new URLPath();
    if (element == null)
      return path;

    final List<Item> item = element.getItem();
    for(Item i : item) {
      final ItemType type = i.getType();
      switch (type) {
        case ZIP :
        case JAR : buildFilePath(path, i);
                   break;
        case DIR : buildFolderPath(path, i);
                   break;
        case URL : try {
                     path.add(new URL(i.getFile()));
                   }
                   catch (MalformedURLException e) {
                     // TODO: provide a better handling of the exception
                     e.printStackTrace();
                   }
                   break;
      }
    }
    return path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildFilePath
  /**
   ** Lookup of the specified file in the specified folder
   **
   ** @param  receiver           the {@link URLPath} composed with all files the
   **                            match the specified <code>fileName</code>.
   ** @param  item               the {@link Item} to search
   */
  private void buildFilePath(final URLPath receiver, final Item item) {
    // build the final destination for the file to lookup
    final boolean releaseAware = (item.isReleaseAware() == null) ? false : item.isReleaseAware().booleanValue();
    final URL     searchPath   = composeRoot(this.searchBase, item.getPath(), releaseAware ? this.release : null );
    // request all files from the file system
    URL[] fileList = URLFileSystem.list(searchPath);
    if (fileList != null) {
      // If you will be using a particular regular expression often, should
      // create a Pattern object to store the regular expression.
      // You can then reuse the regex as often as you want by reusing the
      // Pattern object.
      Pattern pattern = Pattern.compile(item.getFile(), Pattern.CASE_INSENSITIVE);
      buildFilePath(receiver, searchPath, pattern, fileList);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildFilePath
  /**
   ** Lookup of the specified file in the specified folder.
   **
   ** @param   receiver          the {@link URLPath} composed with all files the
   **                            match the specified <code>fileName</code>.
   ** @param  root               the file system folder to search for the file.
   ** @param  pattern            the {@link Pattern} a retrieved file name must
   **                            match.
   ** @param  fileList           the array of file names to search within for
   **                            the occurence of <code>pattern</code> in their
   **                            names.
   */
  private void buildFilePath(final URLPath receiver, final URL root, final Pattern pattern, final URL[] fileList) {
    for (int i = 0; i < fileList.length; i++) {
      // checkout if the current path has to be ignored
      if (ignorePath(root, fileList[i]))
        continue;

      if (URLFileSystem.isDirectory(fileList[i]) && this.recursive)
        buildFilePath(receiver, fileList[i], pattern, URLFileSystem.list(fileList[i]));
      else {
        // To use the regular expression on a string, create a Matcher object by
        // calling pattern.matcher() passing the subject string to it. The
        // Matcher will do the actual searching, replacing or splitting.
        Matcher match = pattern.matcher(URLFileSystem.getFileName(fileList[i]));
        if (match.find()) {
          receiver.add(fileList[i]);
          break;
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildFolderPath
  /**
   ** Lookup of the specified folder in the specified folder
   **
   ** @param  receiver           the {@link URLPath} composed with all files the
   **                            match the specified <code>fileName</code>.
   ** @param  item               the {@link Item} to search
   */
  private void buildFolderPath(final URLPath receiver, final Item item) {
    // build the final destination for the folders to lookup
    final boolean releaseAware = (item.isReleaseAware() == null) ? false : item.isReleaseAware().booleanValue();
    final URL     searchPath   = composeRoot(this.searchBase, item.getPath(), releaseAware ? this.release: null );
    // request all files from the file system
    URL[] fileList = URLFileSystem.list(searchPath);
    if (fileList != null)
      buildFolderPath(receiver, searchPath, item.getFile(), fileList);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildFolderPath
  /**
   ** Lookup of the specified file in the specified folder.
   **
   ** @param   receiver          the {@link URLPath} composed with all files the
   **                            match the specified <code>fileName</code>.
   ** @param  root               the file system folder to search for the file.
   ** @param  suffix             the {@link String} a retrieved folder name
   **                            must match at the end of it's name.
   ** @param  folderList         the array of {@link URL}s to search within for
   **                            the occurence of <code>pattern</code> in their
   **                            names.
   */
  private void buildFolderPath(final URLPath receiver, final URL root, final String suffix, final URL[] folderList) {
    for (int i = 0; i < folderList.length; i++) {
      if (ignorePath(root, folderList[i]))
        continue;

      if (URLFileSystem.isDirectory(folderList[i])) {
        // To use the regular expression on a string, create a Matcher object by
        // calling pattern.matcher() passing the subject string to it. The
        // Matcher will do the actual searching, replacing or splitting.
        if (URLFileSystem.getSuffix(folderList[i]).endsWith(suffix)) {
          receiver.add(folderList[i]);
          break;
        }

        if (this.recursive)
          buildFolderPath(receiver, folderList[i], suffix, URLFileSystem.list(folderList[i]));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ignorePath
  /**
   ** Lookup of the specified path has to be ignored.
   **
   ** @param  root               the file system folder to search for the file.
   ** @param  path               the {@link URL} to check if it has to be
   **                            ignored.
   */
  private boolean ignorePath(final URL root, final URL path) {
    // be optimistic that we have not to ignore the specified path
    boolean ignored = false;
    // checkout if the specified path has to be ignored
    for (int i = 0; i < IGNORE.size(); i++) {
      // build the URL base on the specified searchBase
      final URL ignore = URLFactory.newURL(root, IGNORE.get(0));
      // check if the URL has to be ignored
      if (URLFileSystem.equals(ignore, path)) {
        ignored = true;
        break;
      }
    }
    return ignored;
  }
}