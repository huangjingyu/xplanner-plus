if(window.localStorage){
	if(window.localStorage.widescreen=="true"){$(".page").addClass("wide");}
	$(document).ready(function(){
		$(".page_footer").click(function(){
			$(".page").addClass("animated");
			$(".page").toggleClass("wide");
			window.localStorage.widescreen = (window.localStorage.widescreen=="true"?"false":"true");
		})
	})
}

/*
Use this function to trigger the form submit when Enter is pressed in a specific field
<FORM ACTION="login.do">
name:     <INPUT NAME=realname SIZE=15><BR>
password: <INPUT NAME=password TYPE=PASSWORD SIZE=10
           onKeyPress="return submitOnEnter(this,event)"><BR>
<INPUT TYPE=SUBMIT VALUE="Log In">
</FORM>

 */
function submitOnEnter(myfield, e)
{
   var keycode;
   if (window.event) keycode = window.event.keyCode;
   else if (e) keycode = e.which;
   else return true;

   if (keycode == 13)
   {
      myfield.form.submit();
      return false;
   }
   else
      return true;
}

$(document).ready(function(){
	$(".dateField").datepicker({dateFormat: 'yy-mm-dd',changeYear:true});
	
});