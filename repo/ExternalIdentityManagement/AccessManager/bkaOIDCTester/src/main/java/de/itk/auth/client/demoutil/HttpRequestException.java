package de.itk.auth.client.demoutil;

import java.util.List;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;

import redsecurity.platform.core.entity.Pair;

@SuppressWarnings("serial") public class HttpRequestException extends ServletException {

  private final String    action;
  private final Exception exception;
  private final String    requestAsHtml;

  public HttpRequestException(String action, Exception exception, String requestAsHtml) {
    this.action = action;
    this.exception = exception;
    this.requestAsHtml = requestAsHtml;
  }

  public HttpRequestException(String action, Exception exception, HttpServletRequest getRequest) {
    this.action = action;
    this.exception = exception;
    this.requestAsHtml = DemoHtmlUtil.getRequestToHtml(getRequest);
  }

  public HttpRequestException(String action, Exception exception, String uri, List<Pair> headerParameters, List<Pair> parameters) {
    this.action = action;
    this.exception = exception;
    this.requestAsHtml = DemoHtmlUtil.postRequestToHtml(uri, headerParameters, parameters);
  }

  public String getAction() {
    return action;
  }

  public Exception getException() {
    return exception;
  }

  public String getRequestAsHtml() {
    return requestAsHtml;
  }

}
