/*
	Copyright (c) 2004-2009, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
 */

window[(typeof (djConfig) != "undefined" && djConfig.scopeMap && djConfig.scopeMap[0][1])
		|| "dojo"]._xdResourceLoaded( function(_1, _2, _3) {
	return {
		depends : [ [ "provide", "mytheme.green" ],
				[ "require", "dojox.charting.Theme" ] ],
		defineResource : function(_4, _5, _6) {
			if (!_4._hasResource["mytheme.green"]) {
				_4._hasResource["mytheme.green"] = true;
				_4.provide("mytheme.green");
				_4.require("dojox.charting.Theme");
				( function() {
					var _7 = _6.charting;
					_7.themes.PlotKit.green = new _7.Theme( {
						chart : {
							stroke :null,
							fill :"white"
						},
						plotarea : {
							stroke :null,
							fill :"#eff5e6"
						},
						axis : {
							stroke : {
								color :"#fff",
								width :2
							},
							line : {
								color :"#fff",
								width :1
							},
							majorTick : {
								color :"#fff",
								width :2,
								length :12
							},
							minorTick : {
								color :"#fff",
								width :1,
								length :8
							},
							font :"normal normal normal 8pt Tahoma",
							fontColor :"#999"
						},
						series : {
							outline : {
								width :1,
								color :"#fff"
							},
							stroke : {
								width :2,
								color :"#666"
							},
							fill :new _4.Color( [ 102, 102, 102, 0.8 ]),
							font :"normal normal normal 7pt Tahoma",
							fontColor :"#000"
						},
						marker : {
							stroke : {
								width :2
							},
							fill :"#333",
							font :"normal normal normal 7pt Tahoma",
							fontColor :"#000"
						},
						colors : []
					});
					_7.themes.mytheme.green.defineColors( {
						hue :82,
						saturation :60,
						low :40,
						high :88
					});
				})();
			}
		}
	};
});