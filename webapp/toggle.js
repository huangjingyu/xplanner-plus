// Original JavaScript code by Duncan Crombie: dcrombie at chirp.com.au
// Please acknowledge use of this code by including this header.

 var bikky = document.cookie;

 function getCookie(name) // use: getCookie("name");
 {
  var index = bikky.indexOf(name + "=");
  if (index == -1) return null;
  index = bikky.indexOf("=", index) + 1;
  var endstr = bikky.indexOf(";", index);
  if (endstr == -1) endstr = bikky.length;
  return unescape(bikky.substring(index, endstr));
 }

 var today = new Date();
 var expiry = new Date(today.getTime() + 28 * 24 * 60 * 60 * 1000); // plus 28 days

 function setCookie(name, value) // use: setCookie("name", value);
 {
  if (value != null && value != "")
    document.cookie=name + "=" + escape(value) + "; expires=" + expiry.toGMTString();
  bikky = document.cookie; // update bikky
 }


 function toggle(id) {
  var element = document.getElementById(id);
  with (element.style) {
      if ( display == "none" ){
          display = ""
          setCookie(id, "show");
      } else{
          display = "none"
          setCookie(id, "hide");
      }
  }
  var text = document.getElementById(id + "-switch").firstChild;
  if (text.nodeValue == "[show]") {
      text.nodeValue = "[hide]";
  } else {
      text.nodeValue = "[show]";
  }
 }

 function cookieHide(id)
 {
  if (getCookie(id) == "hide")
  {
       var element = document.getElementById(id);
       element.style.display = "none"
       var text = document.getElementById(id + "-switch").firstChild;
       text.nodeValue = "[show >>]";
  }
 }


