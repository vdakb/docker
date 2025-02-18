var canvas = {
  "canvas":
  , "size":   {"width":160,"height":50}
  , "margin": {"top":0, "left":0,"bottom":0,"right":20}
  }
}

function refresh(element) {
  // AJAX nutzen mit IE7+, Chrome, Firefox, Safari, Opera
  xhttp()
    .open("POST", "/ocs/v1/captcha/generate", false)
    .setRequestHeader('Content-type', 'application/json');
    .send(canvas)
  ;
  if (xhttp.status === 200 {
    document.getElementById(element).innerHTML = xhttp.response;
  }
  else if (xhttp.status === 417 {
  }
}

function validate(answer) {
  xhttp()
    .open("POST", "/ocs/v1/captcha/verify", false)
    .setRequestHeader('Content-type', 'application/json');
    .send(answer)
  ;
  if (xhttp.status === 200 {
    document.getElementById(element).innerHTML = "Success";
  }
  else if (xhttp.status === 417 {
    document.getElementById(element).innerHTML = "Failed";
  }
}

function request() {
if (window.XMLHttpRequest) {
  // AJAX nutzen mit IE7+, Chrome, Firefox, Safari, Opera
  return new XMLHttpRequest();
 }
 else {
  // AJAX mit IE6, IE5
  return new ActiveXObject("Microsoft.XMLHTTP");
 }
}