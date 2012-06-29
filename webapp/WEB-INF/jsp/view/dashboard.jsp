<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="com.technoetic.xplanner.domain.Integration,
                 com.technoetic.xplanner.format.DecimalFormat,
                 org.apache.struts.Globals" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://xplanner.org/displaytag.sf.net" prefix="dt" %>


<bean:parameter id="iterationId" name="iterationId"/>

<xplanner:content>
    <xplanner:contentTitle titleKey="integrations.title"/>
    <xplanner:navigation oid='<%= Integer.parseInt(iterationId) %>' type="net.sf.xplanner.domain.Iteration"/>
<script src="<html:rewrite page="/js/jquery.jqote2.js"/>" type="text/javascript"></script>

<style type="text/css">
html,body {
	height: 100%;
	width: 100%;
}

* {
	padding: 0px;
	margin: 0px;
}

html,body,.container {
	height: 100%;
	-webkit-user-select: none;
	clear: both;
}
.container {
	height:25px;
	line-height:25px;
	background:#CCC;
	border-radius:3px;
	padding-left:10px;
	margin-bottom:3px;
}

.leftColumn,.midColumn,.rightColumn {
	width: 33%;
	float: left;
	height: 170px;
	display: table;
	margin-top:5px;
}

.leftColumnTitle,.midColumnTitle,.rightColumnTitle {
	width: 33%;
	float: left;
	text-align: center;
	height: 30px;
	line-height:30px;	
}

.transit {
}

.animated {
	-webkit-transition: all 0.6s ease-in-out;
	-moz-transition: all 0.6s ease-in-out;
	-o-transition: all 0.6s ease-in-out;
}

.dragable {
	position: absolute;
	background-color: red;
	z-index: 9999;
	opacity: .5;
}

.columnsBox {
	clear: both;
}
</style>

<div id="test"> </div>

<script type="text/html" class="my_tmpl">
  <![CDATA[
 <div class='ticket' style='width:<*=this.width*>px'>
    <div class='ticketHeader'><strong><*= this.name *>  ID=<*= this.id *> </strong></div> 
	<div class='ticketDescription'><*= this.description *> </div>
  </div>
  ]]>
</script>
<script type="text/html" id="userStoryTemplate">
  <![CDATA[
	<div class='container'><span class='title'>User Story: </span><h1> <a class='storyHeader' href='#'><*= this.name *> ID=<*= this.id *></a></h1></div>
	<div class='taskb<*= this.id *>'>
		<div class='leftColumn'></div><div class='midColumn'></div><div class='rightColumn'></div>	
	</div>
  ]]>
</script>

<div class="leftColumnTitle"><span class="title"> not started</span></div>
<div class="midColumnTitle"><span class="title"> in progress</span></div>
<div class="rightColumnTitle"><span class="title"> done</span></div>

<div id="dash">
 
</div>

<div style="clear:both;"></div>


<script type="text/javascript">
'use_strict';
var columns=[];
(function(){
	function Column(className){
		var object = $(className + "Title");
		var position = object.position();
		var left = position.left, top = position.top, right = position.left + object.width();
		this.className = className;
		this.left = left;
		this.right= right;
		this.isInside=function(x){return left<=x && x<=right;}
		return this;
	}
	columns.push(new Column(".leftColumn"));
	columns.push(new Column(".midColumn"));
	columns.push(new Column(".rightColumn"));
})();

$.getJSON(
	"${appPath}rest/view/iteration/${param.fkey}/userstories",
	function(userStories) {
		var ticketWidth;
		
		ticketWidth = ($(".page_body").width()/3)-(($(".page_body").width()/3)/100*4);
		
		$(userStories).each(function(index, userStory){
			$("#dash").append($("#userStoryTemplate").jqote(userStory, "*"));
			$(userStory.tasks).each(function(j, task){
				task.width = ticketWidth;
				if($(task).children('originalEstimate').text() == 0.0){
					$(".taskb"+userStory.id + " .leftColumn").append($(".my_tmpl").jqote(task, "*"));
				} else if($(task).children('completed').text() == "true"){
					$(".taskb"+userStory.id + " .rightColumn").append($(".my_tmpl").jqote(task, "*"));
				} else {
					$(".taskb"+userStory.id + " .midColumn").append($(".my_tmpl").jqote(task, "*"));
				}
		    });
			
		});
		trackMouse();
	});


  function trackMouse(){
      
      var position;
      var mid;          
      var inSameColumn;
	var dragTarget=dragClon=undefined, i=offsetX=offsetY=coordsX=coordsY=0;
	
	
    $("body").mousemove(function(event){
        if(!dragClon){return;}
        coordsX =  event.clientX - offsetX;
        coordsY =  event.clientY - offsetY;
        dragTarget.css("left", coordsX);
        dragTarget.css("top", coordsY);
        //!!!!!!!! console.log(offsetX, offsetY, ">")
        /*if (coordsY>(offset.top-30)&& coordsY<(offset.top+30)){                          
           
        }   else {
                 
        }*/
    });
    $("body").mouseup(function(event){
    	if(!dragTarget){return;}

    	function dragCleanUP(){
        	if(!dragTarget){return;}                       
            dragTarget.removeClass("animated");
        	dragTarget.removeClass("dragable");
            dragTarget = undefined;
        }
        
        function transition(columnObject){
        	dragTarget.addClass("animated");
        	window.setTimeout(dragCleanUP, 1000);
        	column = $("." + dragTarget.parent().parent().attr("class") + " " + columnObject.className);
			if($(document).scrollTop() + event.clientY < (column.offset().top + $(column.children()[0]).height()/2)){
            	dragTarget.detach();
                column.prepend(dragTarget);
            } else if($(document).scrollTop() + event.clientY > (column.offset().top + column.height() - $(column.children()[0]).height()/2)){
                dragTarget.detach();
                column.append(dragTarget);
            } else {
                var matched = false;
                for(i=0; i<column.children().length-1; i++){
                    var currentSticker = $(column.children()[i]);
                    var isAfterCurrentSticker = $(document).scrollTop() + event.clientY > (currentSticker.offset().top + currentSticker.height()/2);
                    var nextSticker = $(column.children()[i+1]);
                    var isBeforeNextSticker = $(document).scrollTop() +  event.clientY < (nextSticker.offset().top + nextSticker.height()/2);
                    console.log(isAfterCurrentSticker, isBeforeNextSticker, event.clientY, currentSticker.offset().top, nextSticker.offset().top, column.height(), i, currentSticker);
        			
                   if(isAfterCurrentSticker && isBeforeNextSticker){
                    	dragTarget.detach();
                        currentSticker.after(dragTarget);
                        matched = true;
                        break;
                    }
                }
                if(!matched){
                	dragTarget.detach();
                    column.append(dragTarget);
                }
            }
            dragTarget.css({"left": column.position().left, "top": dragTarget.offset().top});
            $(".clone").remove();
            dragClon = undefined;                    
        }
                
        for(i=0; i<columns.length; i++){
        	if(columns[i].isInside(dragClon.position().left + dragClon.width()/2) && columns[i].isInside(event.clientX)){
        		inSameColumn = true;
        		transition(columns[i]);
        		break;
        	}else if(columns[i].isInside(event.clientX)){
        		transition(columns[i]);
        		break;
        	}
        }
    });
    
    $(".ticket").mousedown(function(e){
        if(dragTarget){return;}
        dragTarget = $(e.currentTarget);
        var offset = dragTarget.offset();
        dragClon = dragTarget.clone();
        dragClon.addClass("clone");
        dragTarget.before(dragClon);
        dragTarget.addClass("dragable");
        offsetX = e.clientX - offset.left;
        offsetY = e.clientY - offset.top;
        dragTarget.css("left", offset.left);
        dragTarget.css("top", offset.top);
    })
  }
</script>


</xplanner:content>
