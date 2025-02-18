<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--
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

  Copyright (c) 2011. All Rights reserved
-->
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="oracle.iam.access.bundle.Resource"/>
<%--
translate the message key used by inline expansion and store it in a variable
--%>
<fmt:message key="signup.submit" var="submit"/>
<html lang="${pageContext.request.locale}">
  <head>
    <meta name="robots"              content="noindex, nofollow">
    <meta name="viewport"            content="width=device-width, initial-scale=1.0">
    <meta http-equiv="expires"       content="0">
    <meta http-equiv="pragma"        content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="description"   content="Page to signin">
    <meta http-equiv="content-type"  content="text/html; charset=UTF-8"/>
    <title><fmt:message key="signup.title"/></title>
    <link type="text/css" rel="stylesheet" href="css/font-awesome.min.css">
    <link type="text/css" rel="stylesheet" href="css/bootstrap.min.css">
    <link type="text/css" rel="stylesheet" href="css/bootstrap-theme.min.css">
    <link type="text/css" rel="stylesheet" href="css/bootstrap-social.css">
    <link type="text/css" rel="stylesheet" href="css/avatar.min.css">
  </head>
  <body class="background-gray">
    <section id="header-bg">
      <div id="header-bg-top">
        <div id="header-bg-wrapper">
          <div class="header-bg-avatar"></div>
          <div class="header-bg-speech">
            <h3><fmt:message key="signin.signup"/></h3>
          </div>
        </div>
      </div>
    </section>
    <div class="container margin-top-15">
      <div class="col-md-12">
        <form class="form-horizontal form-canvas needs-validation" role="form" action="/oam/server/auth_cred_submit" method="post" novalidate>
          <div class="form-inner">
            <div class="row">
              <div class="col-md-12">
                <label for="fc-email" class="control-label"><fmt:message key="signup.email.label"/></label>
                <input id="fc-email" name="emailaddress" type="email" class="form-control" required>
                <div id="fb-email" class="invalid-feedback"><fmt:message key='signup.email.invalid'/></div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-6">
                <label for="fc-gn" class="control-label"><fmt:message key="signup.firstname.label"/></label>
                <input id="fc-gn" name="firstname" type="text" class="form-control" required>
                <div id="fb-gn" class="invalid-feedback"><fmt:message key='signup.firstname.invalid'/></div>
              </div>
              <div class="col-md-6">
                <label for="fc-sn" class="control-label"><fmt:message key="signup.lastname.label"/></label>
                <input id="fc-sn" name="lastname" type="text" class="form-control" required>
                <div id="fb-sn" class="invalid-feedback"><fmt:message key='signup.lastname.invalid'/></div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-12">
                <label for="fc-username" class="control-label"><fmt:message key="signup.username.label"/></label>
                <input id="fc-username" name="username" type="text" class="form-control" required>
                <div id="fb-username" class="invalid-feedback"><fmt:message key='signup.username.invalid'/></div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-6">
                <label for="fc-password" class="control-label"><fmt:message key="signup.password.label"/></label>
                <input id="fc-password" name="password" type="password" class="form-control" required>
                <div id="fb-password" class="invalid-feedback"><fmt:message key='signup.password.invalid'/></div>
              </div>
              <div class="col-md-6">
                <label for="fc-confirm" class="control-label"><fmt:message key="signup.confirm.label"/></label>
                <input id="fc-confirm" name="confirmation" type="password" class="form-control" required>
                <div id="fb-confirm" class="invalid-feedback"><fmt:message key='signup.confirm.invalid'/></div>
              </div>
            </div>
            <hr>
            <div class="form-group">
              <div class="col-md-12">
                <label><fmt:message key="signup.agree.pre"/>&nbsp;<a class="text-primary" href="javascript:;" data-toggle="modal" data-target="#terms"><fmt:message key="signup.agree.terms"/></a>&nbsp;<fmt:message key="signup.agree.and"/>&nbsp;<a class="text-primary" href="javascript:;" data-toggle="modal" data-target="#privacy"><fmt:message key="signup.agree.privacy"/></a><fmt:message key="signup.agree.post"/></label>
              </div>
            </div>
            <div class="form-group">
              <div class="col-md-12">
                <input type="submit" value="${submit}" class="btn btn-info">
                <div class="pull-right inline-block">
                  <label><fmt:message key="signup.return"/></label>&nbsp;<a href="signin" class="text-primary pull-right"><fmt:message key="signin.action"/></a>
                </div>
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
    <!-- Modal -->
    <div id="terms" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="terms" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only"><fmt:message key="close"/></span></button>
            <h4 class="modal-title" id="terms"><fmt:message key="terms"/></h4>
          </div>
          <div class="modal-body">
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam fringilla eros tincidunt lectus vestibulum molestie. Donec sed lacus sed orci volutpat cursus posuere sed erat. Etiam laoreet et massa ut faucibus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. In hac habitasse platea dictumst. Duis rhoncus, felis vitae convallis varius, sem nunc vulputate quam, non consequat magna nunc eget eros. Pellentesque varius dui sed erat consequat aliquet.</p>
            <p>Nam ullamcorper quam eu felis lacinia ornare. Proin tristique nulla eget sagittis sodales. Fusce porttitor in justo nec lacinia. In hac habitasse platea dictumst. Mauris gravida porta felis vitae euismod. In porta dapibus dui a eleifend. Donec a accumsan orci. Donec at feugiat urna.</p>
            <p>Mauris id tempus mauris, eget malesuada felis. Nam efficitur tempus tortor eget fringilla. Phasellus egestas, ipsum eu eleifend elementum, urna leo elementum lacus, malesuada facilisis nisl augue vel lacus. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Sed ac accumsan mi. Donec mattis viverra porttitor. Morbi sit amet mi pharetra, iaculis nibh vel, suscipit nibh. Sed sed hendrerit purus, in blandit dolor. Mauris cursus non purus nec elementum. Curabitur elementum pretium lectus, id iaculis erat accumsan a. Sed sed nunc non velit fringilla efficitur vitae eu arcu. Nulla et malesuada dolor.</p>
            <p>Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Quisque imperdiet pellentesque ex condimentum ultrices. Praesent ac mi orci. Maecenas consequat maximus metus ut aliquet. Curabitur a lacinia diam, at mollis mi. Aliquam augue libero, vehicula eget tellus sed, iaculis suscipit risus. Cras lacus dui, porttitor eget lacus a, lacinia pulvinar augue. Pellentesque mollis, orci a mollis eleifend, tellus erat tempor felis, eget gravida enim augue ut dolor. Nunc sed congue lacus. Duis lobortis eget massa et elementum. Curabitur quam est, placerat eget elementum ac, ultrices eget nunc. Ut sed lacinia enim, ac ornare quam.</p>
            <p>Morbi ac magna nunc. Aenean pretium orci in enim placerat faucibus. Etiam ac nisi viverra, accumsan erat quis, sollicitudin sem. Suspendisse potenti. Nullam in lectus condimentum tortor tincidunt semper vel a sem. Donec ultricies augue non metus iaculis eleifend. Suspendisse tempus nibh felis, eu feugiat mauris pretium ut. In hac habitasse platea dictumst. Aenean vestibulum augue a tellus vestibulum, bibendum rutrum urna luctus. Nam dui odio, placerat feugiat vestibulum in, efficitur sit amet dui. Sed id justo sit amet neque commodo mollis quis id nibh. Nunc non laoreet dolor, ac pellentesque ante. Curabitur iaculis mauris dui, vitae blandit arcu porttitor vel.</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="close"/></button>
          </div>
        </div>
      </div>
    </div>
    <div id="privacy" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="privacy" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only"><fmt:message key="close"/></span></button>
            <h4 class="modal-title" id="privacy"><fmt:message key="privacy"/></h4>
          </div>
          <div class="modal-body">
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam fringilla eros tincidunt lectus vestibulum molestie. Donec sed lacus sed orci volutpat cursus posuere sed erat. Etiam laoreet et massa ut faucibus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. In hac habitasse platea dictumst. Duis rhoncus, felis vitae convallis varius, sem nunc vulputate quam, non consequat magna nunc eget eros. Pellentesque varius dui sed erat consequat aliquet.</p>
            <p>Nam ullamcorper quam eu felis lacinia ornare. Proin tristique nulla eget sagittis sodales. Fusce porttitor in justo nec lacinia. In hac habitasse platea dictumst. Mauris gravida porta felis vitae euismod. In porta dapibus dui a eleifend. Donec a accumsan orci. Donec at feugiat urna.</p>
            <p>Mauris id tempus mauris, eget malesuada felis. Nam efficitur tempus tortor eget fringilla. Phasellus egestas, ipsum eu eleifend elementum, urna leo elementum lacus, malesuada facilisis nisl augue vel lacus. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Sed ac accumsan mi. Donec mattis viverra porttitor. Morbi sit amet mi pharetra, iaculis nibh vel, suscipit nibh. Sed sed hendrerit purus, in blandit dolor. Mauris cursus non purus nec elementum. Curabitur elementum pretium lectus, id iaculis erat accumsan a. Sed sed nunc non velit fringilla efficitur vitae eu arcu. Nulla et malesuada dolor.</p>
            <p>Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Quisque imperdiet pellentesque ex condimentum ultrices. Praesent ac mi orci. Maecenas consequat maximus metus ut aliquet. Curabitur a lacinia diam, at mollis mi. Aliquam augue libero, vehicula eget tellus sed, iaculis suscipit risus. Cras lacus dui, porttitor eget lacus a, lacinia pulvinar augue. Pellentesque mollis, orci a mollis eleifend, tellus erat tempor felis, eget gravida enim augue ut dolor. Nunc sed congue lacus. Duis lobortis eget massa et elementum. Curabitur quam est, placerat eget elementum ac, ultrices eget nunc. Ut sed lacinia enim, ac ornare quam.</p>
            <p>Morbi ac magna nunc. Aenean pretium orci in enim placerat faucibus. Etiam ac nisi viverra, accumsan erat quis, sollicitudin sem. Suspendisse potenti. Nullam in lectus condimentum tortor tincidunt semper vel a sem. Donec ultricies augue non metus iaculis eleifend. Suspendisse tempus nibh felis, eu feugiat mauris pretium ut. In hac habitasse platea dictumst. Aenean vestibulum augue a tellus vestibulum, bibendum rutrum urna luctus. Nam dui odio, placerat feugiat vestibulum in, efficitur sit amet dui. Sed id justo sit amet neque commodo mollis quis id nibh. Nunc non laoreet dolor, ac pellentesque ante. Curabitur iaculis mauris dui, vitae blandit arcu porttitor vel.</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="close"/></button>
          </div>
        </div>
      </div>
    </div>
    <script type="text/javascript" src="lib/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="lib/bootstrap.min.js"></script>
    <script type="text/javascript" src="lib/avatar.min.js"></script>
  </body>
</html>