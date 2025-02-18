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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------
*/

package weblogic.net.http;

import weblogic.utils.http.HttpChunkOutputStream;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.UnsyncByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;

/**
 ** A class to represent an HTTP connection to a remote object.
 *÷
 ** Extends the {@link HttpURLConnection} class to make it
 ** possible to send HTTP Delete request with a payload.
 */
public class GenericHttpsURLConnection extends HttpsURLConnection {

  /**
   ** Constructs an {@link GenericHttpsURLConnection} instance.
   **
   ** @param url                 An {@link URL} instance to connect.
   */
  public GenericHttpsURLConnection(URL url) {
    super(url);
  }

  /**
   ** Constructs an {@link GenericHttpsURLConnection} instance.
   **
   ** @param url                 An {@link URL} instance to connect.
   ** @param proxy               A {@link Proxy} instance for the connection.
   */
  public GenericHttpsURLConnection(URL url, Proxy proxy) {
    super(url, proxy);
  }


  /**
   ** Returns an input stream that reads from this open connection.
   ** A SocketTimeoutException can be thrown when reading from the
   ** returned input stream if the read timeout expires before data
   ** is available for read.
   **
   ** @return                    an input stream that reads from this open
   **                            connection.
   **
   ** @throws IOException        if an I/O error occurs while creating the
   **                            input stream.
   */
  @Override
  public InputStream getInputStream() throws IOException {
    try {
      return super.getInputStream();
    } catch (FileNotFoundException e) {
      if (this.getResponseCode() == 500) {
        return this.http.getInputStream();
      } else {
        throw e;
      }
    }
  }

  /**
   ** Returns an output stream that writes to this connection.
   **
   ** @return                    an output stream that writes to this
   **                            connection.
   **
   ** @exception IOException     if an I/O error occurs while creating
   **                            the output stream.
   */
  @Override
  public synchronized OutputStream getOutputStream() throws IOException {
    try {
      if (!this.doOutput) {
        throw new ProtocolException("cannot write to a URLConnection if doOutput=false - call setDoOutput(true)");
      } else {
        if (this.method.equals("GET")) {
          this.setRequestMethod("POST");
        }

        if (("HEAD".equals(this.method) || "OPTIONS".equals(this.method) || "TRACE".equals(this.method)) && this.getProtocol().equals(this.url.getProtocol())) {
          throw new ProtocolException("HTTP method " + this.method + " doesn't support output");
        } else if (this.inputStream != null) {
          throw new ProtocolException("Cannot write output after reading input.");
        } else {
          if (!field("bufferPostForRetry", Boolean.class)) {
            if (this.useHttp11 && field("chunkStreamingModeEnabled", Boolean.class)) {
              this.connect();
              this.writeRequests();

              int chunkLength = field("chunkLength", Integer.class);
              this.streamedPostOS = chunkLength > 0 ? new HttpChunkOutputStream(this.http.getOutputStream(), chunkLength) : new HttpChunkOutputStream(this.http.getOutputStream());
              if (field("debug", Boolean.class)) {
                p("using chunked streaming. ChunkSize=" + (chunkLength > 0 ? chunkLength : Chunk.CHUNK_SIZE - 6 - 2));
              }

              return this.streamedPostOS;
            }

            if (method("hasFixedContentLength", Boolean.class)) {
              this.connect();
              this.writeRequests();
              String clen = this.getRequestProperty("Content-Length");
              this.streamedPostOS = new ContentLengthOutputStream(this.http.getOutputStream(), Integer.parseInt(clen));
              if (field("debug", Boolean.class)) {
                p("using content length streaming. CL=" + Integer.parseInt(clen));
              }

              return this.streamedPostOS;
            }
          }

          if (this.bufferedPostOS == null) {
            this.bufferedPostOS = new UnsyncByteArrayOutputStream();
          } else if (!this.connected) {
            this.bufferedPostOS.reset();
          }

          if (field("debug", Boolean.class)) {
            p("using buffered stream");
          }

          return this.bufferedPostOS;
        }
      }
    } catch (RuntimeException | IOException e) {
      this.disconnect();
      throw e;
    }
  }

  /**
   ** Returns the not accessible field value of the parent class
   ** {@link HttpURLConnection}.
   **
   ** @param name                The name of the field to read.
   ** @param type                The {@link Class} of the field
   **                            to cast the value.
   **
   ** @return                    The value of the field.
   */
  private <T> T field(final String name, final Class<T> type) {
    try {
      Field field = HttpURLConnection.class.getDeclaredField(name);
      field.setAccessible(true);
      Object object = field.get(this);
      field.setAccessible(false);
      return type.cast(object);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   ** Returns the not accessible method return value of the parent class
   ** {@link HttpURLConnection}.
   **
   ** @param name                The name of the method to read.
   ** @param type                The {@link Class} of the field
   **                            to cast the value.
   **
   ** @return                    The return value of the method.
   */
  private <T> T method(final String name, final Class<T> type) {
    try {
      Method method = HttpURLConnection.class.getDeclaredMethod(name);
      method.setAccessible(true);
      Object object = method.invoke(this);
      method.setAccessible(false);
      return type.cast(object);
    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

}
