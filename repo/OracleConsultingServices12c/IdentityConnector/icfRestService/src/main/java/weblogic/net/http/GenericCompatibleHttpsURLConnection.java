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

import weblogic.security.SSL.SSLClientInfo;
import weblogic.security.SSL.SSLSocketFactory;
import weblogic.security.SSL.TrustManager;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.security.cert.CertificateEncodingException;
import java.io.*;
import java.net.*;
import java.security.Permission;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 ** A copy of a {@link CompatibleSOAPHttpsURLConnection} to provide its functionalities
 ** for the custom implementation.
 */
public class GenericCompatibleHttpsURLConnection extends javax.net.ssl.HttpsURLConnection {

  private static HostnameVerifier DEFAULT_HOSTNAME_VERIFIER;
  private static SSLSocketFactory DEFAULT_SOCKET_FACTORY;
  private GenericHttpsURLConnection wlsHttps;
  private boolean isHVSet;
  private boolean isSFSet;

  public GenericCompatibleHttpsURLConnection(URL theURL) {
    this(theURL, (Proxy)null);
  }

  public GenericCompatibleHttpsURLConnection(URL theURL, Proxy theProxy) {
    super(theURL);
    this.wlsHttps = new GenericHttpsURLConnection(theURL, theProxy);
    if (DEFAULT_SOCKET_FACTORY != null) {
      this.wlsHttps.setSSLSocketFactory(DEFAULT_SOCKET_FACTORY);
    }

    if (DEFAULT_HOSTNAME_VERIFIER != null) {
      this.wlsHttps.setHostnameVerifier(this.convert(DEFAULT_HOSTNAME_VERIFIER));
    }

  }

  public String getCipherSuite() {
    this.checkConnect();
    return this.wlsHttps.getCipherSuite();
  }

  public static HostnameVerifier getDefaultHostnameVerifier() {
    return DEFAULT_HOSTNAME_VERIFIER;
  }

  public static void setDefaultHostnameVerifier(HostnameVerifier hostnameVerifier) {
    DEFAULT_HOSTNAME_VERIFIER = hostnameVerifier;
  }

  public static javax.net.ssl.SSLSocketFactory getDefaultSSLSocketFactory() {
    if (DEFAULT_SOCKET_FACTORY == null) {
      Class cls = WLSSSLSocketFactoryAdapter.class;
      synchronized(WLSSSLSocketFactoryAdapter.class) {
        if (DEFAULT_SOCKET_FACTORY == null) {
          DEFAULT_SOCKET_FACTORY = HttpsURLConnection.getDefaultSSLSocketFactory();
        }
      }
    }

    return new CompatibleSSLSocketFactoryAdapter(DEFAULT_SOCKET_FACTORY);
  }

  public static void setDefaultSSLSocketFactory(javax.net.ssl.SSLSocketFactory sslSocketFactory) {
    Class cls = WLSSSLSocketFactoryAdapter.class;
    synchronized(WLSSSLSocketFactoryAdapter.class) {
      DEFAULT_SOCKET_FACTORY = new WLSSSLSocketFactoryAdapter(sslSocketFactory);
    }
  }

  public HostnameVerifier getHostnameVerifier() {
    weblogic.security.SSL.HostnameVerifier hv = this.wlsHttps.getHostnameVerifier();
    return hv == null ? getDefaultHostnameVerifier() : this.convert(hv);
  }

  public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
    if (hostnameVerifier != null) {
      this.wlsHttps.setHostnameVerifier(this.convert(hostnameVerifier));
      this.isHVSet = true;
    } else {
      this.isHVSet = false;
    }

  }

  public void setHostnameVerifier(weblogic.security.SSL.HostnameVerifier hostnameVerifier) {
    if (hostnameVerifier != null) {
      this.wlsHttps.setHostnameVerifier(hostnameVerifier);
      this.isHVSet = true;
    } else {
      this.isHVSet = false;
    }

  }

  public Certificate[] getLocalCertificates() {
    this.checkConnect();
    return this.wlsHttps.getSSLClientInfo().getClientLocalIdentityCert();
  }

  public Principal getLocalPrincipal() {
    this.checkConnect();
    Certificate[] certs = this.getLocalCertificates();
    return certs != null && certs.length > 0 && certs[0] instanceof X509Certificate ? ((X509Certificate)certs[0]).getIssuerX500Principal() : null;
  }

  public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
    this.checkConnect();
    Certificate[] certs = this.getServerCertificates();
    return certs != null && certs.length > 0 && certs[0] instanceof X509Certificate ? ((X509Certificate)certs[0]).getIssuerX500Principal() : null;
  }

  public Certificate[] getServerCertificates() throws SSLPeerUnverifiedException {
    this.checkConnect();
    javax.security.cert.X509Certificate[] certs = this.wlsHttps.getServerCertificateChain();
    Certificate[] serverCerts = null;

    try {
      if (certs != null && certs.length > 0) {
        serverCerts = new Certificate[certs.length];

        for(int i = 0; i < certs.length; ++i) {
          serverCerts[i] = this.convert(certs[i]);
        }
      } else {
        serverCerts = new Certificate[0];
      }

      return serverCerts;
    } catch (Exception var4) {
      throw new SSLPeerUnverifiedException(var4.toString());
    }
  }

  public javax.net.ssl.SSLSocketFactory getSSLSocketFactory() {
    SSLSocketFactory sf = this.wlsHttps.getSSLSocketFactory();
    return (javax.net.ssl.SSLSocketFactory)(sf == null ? getDefaultSSLSocketFactory() : new CompatibleSSLSocketFactoryAdapter(sf));
  }

  public void setSSLSocketFactory(javax.net.ssl.SSLSocketFactory sslSocketFactory) {
    if (sslSocketFactory != null) {
      this.wlsHttps.setSSLSocketFactory(new WLSSSLSocketFactoryAdapter(sslSocketFactory));
      this.isSFSet = true;
    } else {
      this.isSFSet = false;
    }

  }

  public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
    if (sslSocketFactory != null) {
      this.wlsHttps.setSSLSocketFactory(sslSocketFactory);
      this.isSFSet = true;
    } else {
      this.isSFSet = false;
    }

  }

  public void connect() throws IOException {
    if (!this.isSFSet && DEFAULT_SOCKET_FACTORY != null) {
      this.wlsHttps.setSSLSocketFactory(DEFAULT_SOCKET_FACTORY);
    }

    if (!this.isHVSet && DEFAULT_HOSTNAME_VERIFIER != null) {
      this.wlsHttps.setHostnameVerifier(this.convert(DEFAULT_HOSTNAME_VERIFIER));
    }

    this.wlsHttps.connect();
  }

  public void disconnect() {
    this.wlsHttps.disconnect();
  }

  public boolean usingProxy() {
    return this.wlsHttps.usingProxy();
  }

  public SSLClientInfo getSSLClientInfo() {
    return this.wlsHttps.getSSLClientInfo();
  }

  public SSLSession getSSLSession() {
    return this.wlsHttps.getSSLSession();
  }

  public TrustManager getTrustManager() {
    return this.wlsHttps.getTrustManager();
  }

  public void u11() {
    this.wlsHttps.u11();
  }

  public InputStream getInputStream() throws IOException {
    try {
      return this.wlsHttps.getInputStream();
    } catch (FileNotFoundException e) {
      if (this.getResponseCode() == 500) {
        return this.wlsHttps.getHttp().getInputStream();
      } else {
        throw e;
      }
    }
  }

  public void loadLocalIdentity(Certificate[] certs, PrivateKey privateKey) {
    this.wlsHttps.loadLocalIdentity(certs, privateKey);
  }

  public void loadLocalIdentity(InputStream certStream, InputStream keyStream, char[] password) {
    this.wlsHttps.loadLocalIdentity(certStream, keyStream, password);
  }

  public void loadLocalIdentity(InputStream[] stream) {
    this.wlsHttps.loadLocalIdentity(stream);
  }

  public void setSSLClientCertificate(InputStream[] certs) {
    this.wlsHttps.setSSLClientCertificate(certs);
  }

  public void setTrustManager(TrustManager tm) {
    this.wlsHttps.setTrustManager(tm);
  }

  public void setIgnoreProxy(boolean v) {
    this.wlsHttps.setIgnoreProxy(v);
  }

  public static String getAuthInfo(String host, int port, String header) throws IOException {
    return GenericHttpURLConnection.getAuthInfo(host, port, header);
  }

  public static String getProxyBasicCredentials(String proxyHost, int proxyPort, String authHeader, URL requestUrl) throws IOException {
    return GenericHttpURLConnection.getProxyBasicCredentials(proxyHost, proxyPort, authHeader, requestUrl);
  }

  public static String getServerBasicCredentials(URL requestUrl, String authHeader) throws IOException {
    return GenericHttpURLConnection.getServerBasicCredentials(requestUrl, authHeader);
  }

  void finish() {
    this.wlsHttps.finish();
  }

  static final void p(String s) {
    GenericHttpURLConnection.p(s);
  }

  public static void setDefaultSocketFactory(SocketFactory nuDefaultSF) {
    GenericHttpURLConnection.setDefaultSocketFactory(nuDefaultSF);
  }

  public void addRequestProperty(String key, String value) {
    this.wlsHttps.addRequestProperty(key, value);
  }

  public int getConnectTimeout() {
    return this.wlsHttps.getConnectTimeout();
  }

  public InputStream getErrorStream() {
    return this.wlsHttps.getErrorStream();
  }

  public String getHeaderField(int n) {
    return this.wlsHttps.getHeaderField(n);
  }

  public String getHeaderField(String name) {
    return this.wlsHttps.getHeaderField(name);
  }

  public String getHeaderFieldKey(int n) {
    return this.wlsHttps.getHeaderFieldKey(n);
  }

  public Map getHeaderFields() {
    return this.wlsHttps.getHeaderFields();
  }

  String getHttpVersion() {
    return this.wlsHttps.getHttpVersion();
  }

  String getMethod() {
    return this.wlsHttps.getMethod();
  }

  Object getMuxableSocket() {
    return this.wlsHttps.getMuxableSocket();
  }

  public OutputStream getOutputStream() throws IOException {
    return this.wlsHttps.getOutputStream();
  }

  public int getReadTimeout() {
    return this.wlsHttps.getReadTimeout();
  }

  public Map getRequestProperties() {
    return this.wlsHttps.getRequestProperties();
  }

  public String getRequestProperty(String key) {
    return this.wlsHttps.getRequestProperty(key);
  }

  public int getResponseCode() throws IOException {
    return this.wlsHttps.getResponseCode();
  }

  Socket getSocket() {
    return this.wlsHttps.getSocket();
  }

  public final SocketFactory getSocketFactory() {
    return this.wlsHttps.getSocketFactory();
  }

  public int getTimeout() {
    return this.wlsHttps.getTimeout();
  }

  boolean isConnected() {
    return this.wlsHttps.isConnected();
  }

  public void setChunkedStreamingMode(int chunkLength) {
    this.wlsHttps.setChunkedStreamingMode(chunkLength);
  }

  public void setConnectTimeout(int i) {
    this.wlsHttps.setConnectTimeout(i);
  }

  public void setEmptyRequestProperty(String key) {
    this.wlsHttps.setEmptyRequestProperty(key);
  }

  public void setFixedLengthStreamingMode(int fixedContentLength) {
    this.wlsHttps.setFixedLengthStreamingMode(fixedContentLength);
  }

  public void setIgnoreSystemNonProxyHosts(boolean v) {
    this.wlsHttps.setIgnoreSystemNonProxyHosts(v);
  }

  void setInputStream(InputStream inputStream) {
    this.wlsHttps.setInputStream(inputStream);
  }

  public void setMuxableSocket(Object socket) {
    this.wlsHttps.setMuxableSocket(socket);
  }

  public void setReadTimeout(int i) {
    this.wlsHttps.setReadTimeout(i);
  }

  public void setRequestMethod(String method) throws ProtocolException {
    this.wlsHttps.setRequestMethod(method);
  }

  public void setRequestProperty(String key, String value) {
    this.wlsHttps.setRequestProperty(key, value);
  }

  void setScavenger(Runnable scavenger) {
    this.wlsHttps.setScavenger(scavenger);
  }

  public final void setSocketFactory(SocketFactory fact) {
    this.wlsHttps.setSocketFactory(fact);
  }

  public void setTimeout(int i) {
    this.wlsHttps.setTimeout(i);
  }

  void writeRequestForAsyncResponse() throws IOException {
    this.wlsHttps.writeRequestForAsyncResponse();
  }

  public static boolean getFollowRedirects() {
    return HttpURLConnection.getFollowRedirects();
  }

  public long getHeaderFieldDate(String name, long Default) {
    return this.wlsHttps.getHeaderFieldDate(name, Default);
  }

  public boolean getInstanceFollowRedirects() {
    return this.wlsHttps.getInstanceFollowRedirects();
  }

  public Permission getPermission() throws IOException {
    return this.wlsHttps.getPermission();
  }

  public String getRequestMethod() {
    return this.wlsHttps.getRequestMethod();
  }

  public String getResponseMessage() throws IOException {
    return this.wlsHttps.getResponseMessage();
  }

  public void setFixedLengthStreamingMode(long contentLength) {
    this.wlsHttps.setFixedLengthStreamingMode(contentLength);
  }

  public static void setFollowRedirects(boolean set) {
    HttpURLConnection.setFollowRedirects(set);
  }

  public void setInstanceFollowRedirects(boolean followRedirects) {
    this.wlsHttps.setInstanceFollowRedirects(followRedirects);
  }

  public static FileNameMap getFileNameMap() {
    return GenericHttpURLConnection.getFileNameMap();
  }

  public static void setFileNameMap(FileNameMap map) {
    GenericHttpURLConnection.setFileNameMap(map);
  }

  public URL getURL() {
    return this.wlsHttps.getURL();
  }

  public int getContentLength() {
    return this.wlsHttps.getContentLength();
  }

  public long getContentLengthLong() {
    return this.wlsHttps.getContentLengthLong();
  }

  public String getContentType() {
    return this.wlsHttps.getContentType();
  }

  public String getContentEncoding() {
    return this.wlsHttps.getContentEncoding();
  }

  public long getExpiration() {
    return this.wlsHttps.getExpiration();
  }

  public long getDate() {
    return this.wlsHttps.getDate();
  }

  public long getLastModified() {
    return this.wlsHttps.getLastModified();
  }

  public int getHeaderFieldInt(String name, int Default) {
    return this.wlsHttps.getHeaderFieldInt(name, Default);
  }

  public long getHeaderFieldLong(String name, long Default) {
    return this.wlsHttps.getHeaderFieldLong(name, Default);
  }

  public Object getContent() throws IOException {
    return this.wlsHttps.getContent();
  }

  public Object getContent(Class[] classes) throws IOException {
    return this.wlsHttps.getContent(classes);
  }

  public String toString() {
    return "weblogic.net.http.CompatibleSOAPHttpsURLConnection: [" + this.wlsHttps.toString() + "]";
  }

  public void setDoInput(boolean doinput) {
    this.wlsHttps.setDoInput(doinput);
  }

  public boolean getDoInput() {
    return this.wlsHttps.getDoInput();
  }

  public void setDoOutput(boolean dooutput) {
    this.wlsHttps.setDoOutput(dooutput);
  }

  public boolean getDoOutput() {
    return this.wlsHttps.getDoOutput();
  }

  public void setAllowUserInteraction(boolean allowuserinteraction) {
    this.wlsHttps.setAllowUserInteraction(allowuserinteraction);
  }

  public boolean getAllowUserInteraction() {
    return this.wlsHttps.getAllowUserInteraction();
  }

  public static void setDefaultAllowUserInteraction(boolean defaultallowuserinteraction) {
    GenericHttpURLConnection.setDefaultAllowUserInteraction(defaultallowuserinteraction);
  }

  public static boolean getDefaultAllowUserInteraction() {
    return GenericHttpURLConnection.getDefaultAllowUserInteraction();
  }

  public void setUseCaches(boolean usecaches) {
    this.wlsHttps.setUseCaches(usecaches);
  }

  public boolean getUseCaches() {
    return this.wlsHttps.getUseCaches();
  }

  public void setIfModifiedSince(long ifmodifiedsince) {
    this.wlsHttps.setIfModifiedSince(ifmodifiedsince);
  }

  public long getIfModifiedSince() {
    return this.wlsHttps.getIfModifiedSince();
  }

  public boolean getDefaultUseCaches() {
    return this.wlsHttps.getDefaultUseCaches();
  }

  public void setDefaultUseCaches(boolean defaultusecaches) {
    this.wlsHttps.setDefaultUseCaches(defaultusecaches);
  }

  /** @deprecated */
  @Deprecated
  public static void setDefaultRequestProperty(String key, String value) {
    GenericHttpURLConnection.setDefaultRequestProperty(key, value);
  }

  /** @deprecated */
  @Deprecated
  public static String getDefaultRequestProperty(String key) {
    return GenericHttpURLConnection.getDefaultRequestProperty(key);
  }

  public static void setContentHandlerFactory(ContentHandlerFactory fac) {
    GenericHttpURLConnection.setContentHandlerFactory(fac);
  }

  public static String guessContentTypeFromName(String fname) {
    return GenericHttpURLConnection.guessContentTypeFromName(fname);
  }

  public static String guessContentTypeFromStream(InputStream is) throws IOException {
    return GenericHttpURLConnection.guessContentTypeFromStream(is);
  }

  private weblogic.security.SSL.HostnameVerifier convert(final HostnameVerifier hostnameVerifier) {
    return new weblogic.security.SSL.HostnameVerifier() {
      public boolean verify(String hostname, SSLSession session) {
        return hostnameVerifier.verify(hostname, session);
      }
    };
  }

  private HostnameVerifier convert(final weblogic.security.SSL.HostnameVerifier hostnameVerifier) {
    return new HostnameVerifier() {
      public boolean verify(String hostname, SSLSession session) {
        return hostnameVerifier.verify(hostname, session);
      }
    };
  }

  private Certificate convert(javax.security.cert.X509Certificate xcert) throws CertificateException, CertificateEncodingException {
    ByteArrayInputStream bis = new ByteArrayInputStream(xcert.getEncoded());
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    Certificate cert = null;
    if (bis.available() > 0) {
      cert = cf.generateCertificate(bis);
    }

    return cert;
  }

  private void checkConnect() {
    if (this.wlsHttps.getHttp() == null) {
      throw new IllegalStateException("connection not yet open");
    }
  }
}
