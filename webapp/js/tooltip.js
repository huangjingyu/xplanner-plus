var tooltip = function(copy, className, obj) {
		var box = "<span class='deleteTooltip'>" + copy + "</span>"
	    $("body").append(box);
	    var deleteTooltipShown = false;
	    var dontDhow = false;
	    var targ = $("img") || $("input");
	
	    $(document).ready( function() {
	    	 
	    		    
	    	
	     	obj.mouseover(function(event) {
			var box = $(".deleteTooltip");
			var target = $(event.target);
			if (target.hasClass(className)) {
				//box = "<span class='deleteTooltip'>" + copy + "</span>"
				box.html(copy);
				box.css("left", target.offset().left + 30);
				
				if(target[0].name=="searchedContent"){
					box.css("top", target.offset().top+20);
				}else{
				
				box.css("top", target.offset().top);
					
				}
				$(".deleteTooltip").stop(true, true);
				box.fadeIn("300");
				// $(this).parent("a").after(box).fadeIn(500);
				deleteTooltipShown = true;
			}
			
		} );
	    	 
	    	obj.mouseout( function(event) {
			if (deleteTooltipShown) {
				$(".deleteTooltip").stop(true, true);
				$(".deleteTooltip").fadeOut("300");
				deleteTooltipShown = false;				
			}			
	    	});
	    	
	    	
	    	obj.focusin( function(event) {
				if (deleteTooltipShown) {
					$(".deleteTooltip").stop(true, true);
					$(".deleteTooltip").fadeOut("300");
					deleteTooltipShown = false;
					
				}
				
			});
	});
	   
}
