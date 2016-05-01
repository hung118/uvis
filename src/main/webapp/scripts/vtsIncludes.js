//alert('includes.js loaded');

/* ------------------------------------------------   Magic Vars   ------------------------------------------------ */
var globalPath = '/uvis/' //this is the path to root that we can use for building common elements

var uiiOverride = true; //false allows the uii banner to load. true turns it off. used with the loadUII() function below

var bannerSourceXML = globalPath + 'common/randomBanner/banners.xml'; // the source for the xml banners. this is used with loadBanner() below.

var theDate = new Date();
var currentYear = theDate.getYear();
if (currentYear < 2000) {
	currentYear += 1900;
}

/* ------------------------------------------------   Menu Arrays   ------------------------------------------------ */
//in the arrays below, the first set of quotes is the url, the second is the graphical button, the third is the alt attribute and title value.
var mainNavArray = new Array();
	mainNavArray[0] = new Array('index.html', 'imageHere', 'Home');
	mainNavArray[1] = new Array('index.html', 'imageHere', 'Reports');
	mainNavArray[2] = new Array('index.html', 'imageHere', 'Search');
	mainNavArray[3] = new Array('index.html', 'imageHere', 'New Entry');
	mainNavArray[4] = new Array('index.html', 'imageHere', 'Administration');

//buildGraphicMenu(mainNavArray);
function buildGraphicMenu(theArray) { // !! this uses an array to build menu items. it relies on the global variable called globalPath (defined at top of page) to point to the current domain. 
	var htmlCode = '';

	for(i=0; i<theArray.length; i++) {
		if( theArray[i][0].indexOf('https://') != -1 ){ //check for absolute url
			htmlCode += '<a href="' + theArray[i][0] + '"><img src="' + globalPath + theArray[i][1] + '" alt="' + theArray[i][2] + '" title="' + theArray[i][2] + '" /></a>';
		} else { //if url is not absolute, then add the global path
			htmlCode += '<a href="' + globalPath + theArray[i][0] + '"><img src="' + globalPath + theArray[i][1] + '" alt="' + theArray[i][2] + '" title="' + theArray[i][2] + '" /></a>';
		}
	}
	
	document.write(htmlCode);
}


// ------------------------------------------------   Banner Loader  ----------------------------------------------- //

	var photo_array = new Array();
		photo_array[0]  = new Array('images/rotatingphotos/army.jpg', 'Army');
		photo_array[1]  = new Array('images/rotatingphotos/navy.jpg', 'Navy');
		photo_array[2]  = new Array('images/rotatingphotos/airforce.jpg', 'Air Force');
		photo_array[3]  = new Array('images/rotatingphotos/marines.jpg', 'Marines');
		photo_array[4]  = new Array('images/rotatingphotos/coastguard.jpg', 'Coast Guard');
			
function rotatingPhoto(theArray) {	
	//the code below is for when you want more'n one image on your banner, like uddc which uses 4 banner images
	/*for (i=0; i<4; i++) {
		var randomNumber = Math.floor( Math.random()*theArray.length );
		htmlCode += '<img src="../2007/common/' + globalPath + 'bannerImages/' + theArray[randomNumber][0] + '"alt="' + bannerArray[randomNumber][1] + '" title="' + bannerArray[randomNumber][1] + '" />';
		bannerArray.splice(randomNumber, 1);
	}*/
	var htmlCode = '';
	var randomNumber = Math.floor( Math.random()*theArray.length );
		htmlCode += '<a href="' + globalPath + 'index.jsp"><img src="' + globalPath + theArray[randomNumber][0] + '"alt="' + theArray[randomNumber][1] + '" title="' + theArray[randomNumber][1] + '" /></a>';
		
	document.write(htmlCode);
}



/* ------------------------------------------------   Content   ------------------------------------------------ */
function copyRight() {
	var htmlCode = '';
	htmlCode += currentYear + ' &copy; Utah Department of Veteran Affairs';
	document.write(htmlCode);
}


/* ------------------------------------------------   IE Style   ------------------------------------------------ */
function ieStyle() {
	//alert('ieStyle() fired');
	var htmlCode = '';
	/*htmlCode += '<!--[if IE 5]><link href="' + globalPath + 'common/css/ieStyle.css" rel="stylesheet" type="text/css"><![endif]-->';
	htmlCode += '<!--[if IE 6]><link href="' + globalPath + 'common/css/ieStyle.css" rel="stylesheet" type="text/css"><![endif]-->';*/
	htmlCode += '<!--[if IE 7]><link href="' + globalPath + 'common/css/ieStyle7.css" rel="stylesheet" type="text/css"><![endif]-->';
	
	//document.write(htmlCode);
}



/* ------------------------------------------------   UII Loader   ------------------------------------------------ */
function checkUIIblock() {
	if (uiiOverride == true) {
		document.getElementById('uiiStage').innerHTML = '<img src="common/images/uii/uiiLeft.gif" alt="Utah.gov" title="Utah.gov" />';
	}
}

function loadUII(pleaseLoad) {
	var htmlCode =  '';

	if (pleaseLoad == true && uiiOverride == false) {
		//alert("pleaseLoad = true");
		document.getElementById('uiiBanner').innerHTML = document.getElementById('uiiStage').innerHTML;
		document.getElementById('uiiStage').innerHTML = ' ';
	}
}
/*<script language="javascript" src="http://www.utah.gov/nav/fluidheader.js" type="text/javascript"></script>*/

function popupWindow(path, winName, width, height) {
	var popupWin;

    var opt = "width=" + width;
	opt += ",height=" + height;
	opt += ",menubar=0";
	opt += ",toolbar=0";
	opt += ",scrollbars=yes";
	opt += ",status=yes";
	opt += ",resizable=yes";
	opt += ",location=0";
    opt += ",left=0";
    opt += ",top=0";
	popupWin = window.open(path, winName, opt);
    popupWin.focus();
}

// Function to give auto tab for ssn and phone number
function autoTab(input, len, e) {
	var keyCode = (isNaN) ? e.which : e.keyCode;
	var filter = (isNaN) ? [ 0, 8, 9 ] : [ 0, 8, 9, 16, 17, 18, 37, 38, 39, 40,
			46 ];
	if (input.value.length >= len && !containsElement(filter, keyCode)) {
		input.value = input.value.slice(0, len);
		input.form[(getIndex(input) + 1) % input.form.length].focus();
	}
	function containsElement(arr, ele) {
		var found = false, index = 0;
		while (!found && index < arr.length)
			if (arr[index] == ele)
				found = true;
			else
				index++;
		return found;
	}
	function getIndex(input) {
		var index = -1, i = 0, found = false;
		while (i < input.form.length && index == -1)
			if (input.form[i] == input)
				index = i;
			else
				i++;
		return index;
	}
	return true;
}

/**
 * Sends link as POST form instead of GET.
 * 
 * Using the createElement function provided, which is necessary due to IE's brokenness with the name attribute on elements 
 * created normally with document.createElement
 * Usage var rb = createElement("input", {type: "radio", checked: true});
 * 
 * @param url
 * @param values
 */
function postToUrl(url, values) {

	var createElement = (function() {
		// Detect IE using conditional compilation
		if (/* @cc_on @ *//* @if (@_win32)!/*@end @ */false) {
			// Translations for attribute names which IE would otherwise choke
			// on
			var attrTranslations = {
				"class" : "className",
				"for" : "htmlFor"
			};

			var setAttribute = function(element, attr, value) {
				if (attrTranslations.hasOwnProperty(attr)) {
					element[attrTranslations[attr]] = value;
				} else if (attr == "style") {
					element.style.cssText = value;
				} else {
					element.setAttribute(attr, value);
				}
			};

			return function(tagName, attributes) {
				attributes = attributes || {};

				// See
				// http://channel9.msdn.com/Wiki/InternetExplorerProgrammingBugs
				if (attributes.hasOwnProperty("name")
						|| attributes.hasOwnProperty("checked")
						|| attributes.hasOwnProperty("multiple")) {
					var tagParts = [ "<" + tagName ];
					if (attributes.hasOwnProperty("name")) {
						tagParts[tagParts.length] = ' name="' + attributes.name
								+ '"';
						delete attributes.name;
					}
					if (attributes.hasOwnProperty("checked")
							&& "" + attributes.checked == "true") {
						tagParts[tagParts.length] = " checked";
						delete attributes.checked;
					}
					if (attributes.hasOwnProperty("multiple")
							&& "" + attributes.multiple == "true") {
						tagParts[tagParts.length] = " multiple";
						delete attributes.multiple;
					}
					tagParts[tagParts.length] = ">";

					var element = document.createElement(tagParts.join(""));
				} else {
					var element = document.createElement(tagName);
				}

				for ( var attr in attributes) {
					if (attributes.hasOwnProperty(attr)) {
						setAttribute(element, attr, attributes[attr]);
					}
				}

				return element;
			};
		}
		// All other browsers
		else {
			return function(tagName, attributes) {
				attributes = attributes || {};
				var element = document.createElement(tagName);
				for ( var attr in attributes) {
					if (attributes.hasOwnProperty(attr)) {
						element.setAttribute(attr, attributes[attr]);
					}
				}
				return element;
			};
		}
	})();

	// begin of postToUrl()
	values = values || {};

	var form = createElement("form", {
		action : url,
		method : "POST",
		style : "display: none"
	});
	for (var property in values) {
		if (values.hasOwnProperty(property)) {
			var value = values[property];
			if (value instanceof Array) {
				for ( var i = 0, l = value.length; i < l; i++) {
					form.appendChild(createElement("input", {
						type : "hidden",
						name : property,
						value : value[i]
					}));
				}
			} else {
				form.appendChild(createElement("input", {
					type : "hidden",
					name : property,
					value : value
				}));
			}
		}
	}
	document.body.appendChild(form);
	form.submit();
}
