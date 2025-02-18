package oracle.iam.identity.common.spi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Visitor that walks through the specified directory recursively
 * and compress the content into a zip file.
 */
public class SandboxVisitor extends SimpleFileVisitor<Path> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private Path source;
    // files and folders inside source folder as zip entries
    private List<ZipEntry> entries;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a new <code>Visitor</code> instance by converting the given
     ** pathname string into an abstract pathname. If the given string is the
     ** empty string, then the result is the empty abstract pathname.
     **
     ** @param  source           source path
     **
     ** @throws NullPointerException if the <code>pathname</code> argument is
     **                              <code>null</code>
     */
    public SandboxVisitor(final Path source) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.source = source;
      this.entries = new ArrayList<ZipEntry>();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: entries
    /**
     ** Returns the value of the entry property.
     ** <p>
     ** This accessor method returns a reference to the live list, not a
     ** snapshot. Therefore any modification you make to the returned list will
     ** be present inside object. This is why there is not a <code>set</code>
     ** method for the attribute property.
     ** <p>
     ** For example, to add a new item, do as follows:
     ** <pre>
     **   entries().add(newItem);
     ** </pre>
     ** Objects of the following type(s) are allowed in the list
     ** {@link ZipEntry}
     **
     ** @return                  the value of the entry property.
     */
    public List<ZipEntry> entries() {
      return this.entries;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: preVisitDirectory (overridden)
    /**
     ** Invoked for a directory before entries in the directory are visited.
     ** <p>
     ** If this method returns {@link FileVisitResult#CONTINUE CONTINUE}, then
     ** entries in the directory are visited. If this method returns
     ** {@link FileVisitResult#SKIP_SUBTREE SKIP_SUBTREE} or
     ** {@link FileVisitResult#SKIP_SIBLINGS SKIP_SIBLINGS} then entries in the
     ** directory (and any descendants) will not be visited.
     ** <p>
     ** Unless overridden, this method returns
     ** always {@link FileVisitResult#CONTINUE CONTINUE}.
     **
     ** @param  path             the {@link Path} reference to a directory.
     ** @param  attributes       the directory's basic attributes.
     **
     ** @return                  the visit result.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    @Override
    public FileVisitResult preVisitDirectory(final Path path, final BasicFileAttributes attributes)
      throws IOException {

      final String fileName = this.source.relativize(path).toString().replace("\\", "/");
      // zipped directory entries should end with a forward slash
      if (!fileName.isEmpty()) {
        this.entries.add(new ZipEntry(fileName + "/"));
      }
      return FileVisitResult.CONTINUE;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: visitFile (overridden)
    /**
     ** Invoked for a file in a directory.
     ** <p>
     ** Unless overridden, this method returns
     ** always {@link FileVisitResult#CONTINUE CONTINUE}.
     **
     ** @param  path             the {@link Path} reference to a file.
     ** @param  attributes       the files's basic attributes.
     **
     ** @return                  the visit result.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    @Override
    public FileVisitResult visitFile(final Path path, final BasicFileAttributes attributes)
      throws IOException {

      // according to zip standard backslashes should not be used in zip entries
      final String fileName = this.source.relativize(path).toString().replace("\\", "/");
      this.entries.add(new ZipEntry(fileName));
      return FileVisitResult.CONTINUE;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: visitFileFailed (overridden)
    /**
     ** Invoked for a file that could not be visited. This method is invoked if
     ** the file's attributes could not be read, the file is a directory that
     ** could not be opened, and other reasons.
     ** <p>
     ** Unless overridden, this method re-throws the I/O exception that
     ** prevented the file from being visited.
     **
     ** @param  path             the {@link Path} reference to a file.
     ** @param  error            the I/O exception that prevented the file from
     **                          being visited.
     **
     ** @return                  the visit result.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    @Override
    public FileVisitResult visitFileFailed(final Path path, final IOException error)
      throws IOException {

      throw error;
    }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assemblePath
  /**
   ** This method vists the sandbox directory and adds all of the child files
   ** and directories (including empty directories) into a collection of
   ** {@link ZipEntries}.
   **
   ** @param  source             the {@link Path} to the directory to zip.
   */
  private static List<ZipEntry> assemblePath(final Path source)
    throws IOException {

    final SandboxVisitor visitor = new SandboxVisitor(source);
    Files.walkFileTree(source, visitor);
    return visitor.entries();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compressPath
  /**
   ** This method zips the sandbox directory and adds all of the child files and
   ** directories (including empty directories) into the zip file.
   **
   ** @param  source               the {@link Path} to the directory to zip.
   ** @param  target             the {@link Path} to the zip file to create.
   **
   ** @throws IOException
   */
  public static void compressPath(final Path source, final Path target)
    throws IOException {

    final List<ZipEntry>  entries = assemblePath(source);
    final byte[]          buffer  = new byte[1024];
    ZipOutputStream stream  = null;
    try {
      stream = new ZipOutputStream(new FileOutputStream(target.toString()));
      for (ZipEntry entry : entries) {
        stream.putNextEntry(entry);
        if (!entry.isDirectory()) {
          FileInputStream in = new FileInputStream(Paths.get(source.toString(), entry.toString()).toString());
          int             len;
          while ((len = in.read(buffer)) > 0) {
            stream.write(buffer, 0, len);
          }
          in.close();
        }
        stream.closeEntry();
      }
    }
    // ensure we close the stream always
    finally {
      if (stream != null)
        stream.close();
    }
  }
}
