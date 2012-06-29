if (!dojo._hasResource["mytheme.green"]) {
	dojo._hasResource["mytheme.green"] = true;
	dojo.provide("mytheme.green");
	dojo.require("dojox.charting.Theme");
	( function() {
		var dc = dojox.charting, pk = mytheme;

		pk.green = new dc.Theme({
			chart:{
				stroke:null,
				fill:   "white"
			},
			plotarea:{
				stroke:null,
				fill:   "white"
			},
			axis:{
				 // override major tick color as original color is too dark for gridlines
                majorTick:      { //  major ticks on axis, and used for major gridlines
                        color:"red",
                        width:1,
                        length:6,
                        position:"center"
                },
                font: "1em  Arial,Helvetica,sans-serif", // labels on axis
                fontColor:"red"                                   //      color of labels
        },
			series:{
				stroke:    {color:"#fff", width: 2},
				fill:      "#000",
				font:      "12pt Arial,Helvetica,sans-serif",	// label
				fontColor: "#205483"
			},
			marker:{	// any markers on a series.
				stroke:    {color:"#fff", width: 2},
				fill:      "#333",
				font:      "12pt Arial,Helvetica,sans-serif",	// label
				fontColor: "#205483"
			},
			colors: ["#008000", "lightgreen", "#2bb22b", "#2fab2f", "#5583ac"]
		});
		
	
		

	})();
}