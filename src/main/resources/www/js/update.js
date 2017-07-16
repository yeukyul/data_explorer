/* 
 * update.js - reponsible for receiving upcalls from java backend 
 * and updating content of main panel that can be specified in java's
 * display plug-in.
 */

/* Globals */
var COLS = 12; // matrialize grid system has 12 columns
var rows, cols;
var nNodes = 3;

/*
 * setAppName sets the name of the application
 */
function setAppName(name) {
		$("#title").html(name + " Explorer");
		return true;
}

/*
 * setAppName sets the name of the application
 */
function setSearchPrompt(prompt) {
    $('#textinput').attr("placeholder", prompt);
		return true;
}

/*
 * addTitle add the new title to the media panel
 */
function setTitle(htmlString) {
    $("#name").html(htmlString);
}

/*
 *  addMainMedia add the new main media to the media panel
 */
function setMainMedia(htmlString) {
  $("#mainMedia").html(htmlString);
	return true;
}

/*
 *  addSubMedia add the new sub media to the media panel following materialize layout
 */
function setSubMedia(mediaList) {
    
    /* mediaList should at least contain dimension for the media */
    if (mediaList.length < 2 ) {
        return;
    }
    
    /* obtain the dimension of the media panel */
    rows = parseInt(mediaList[0]);
    cols = Math.min(COLS, parseInt(mediaList[1]));
    var colPerCol = Math.floor(COLS / cols);
    var subMediaString = "";
    var i = 2;
    
    for (var row = 0; row < rows; row++) {
        
        subMediaString += "<div class='row'>";
        
        for (var col = 0; col < cols; col++) {
            
            subMediaString += "<div class='col s" + colPerCol + "'>";
            subMediaString += mediaList[i];
            subMediaString += "</div>";
            
            i++;
            // if media available is less than the slots assigned
            if ( i >= mediaList.length ) {
                subMediaString += "</div>";
                $("#subMedia").html(subMediaString);
                return;
            }
        }
        subMediaString += "</div>";
    }
    
    // add subMedia HTML string into the subMedia tag
    $("#subMedia").html(subMediaString);
}

/*
 *  getRow returns the number of row the current subMedia panel has
 */
function getRow() {
    return rows;
}

/*
 * getCol returns the number of column the current subMedia panel has
 */
function getCol() {
    return cols;
}

/*
 * updateColor updates the color scheme of the framework. Ignore if
 * any of the colors is not specified.
 */
function updateColor(colors) {
    lightColor, mediumColor, darkColor, textColor
    
    var lightColor = colors[0],
        mediumColor = colors[1],
        darkColor = colors[2],
        textColor = colors[3];
    
    if (lightColor != "") {
        $('.link').css({"fill": lightColor});
        $('.node circle').css({"fill": lightColor,
                           "stroke": lightColor});
    }
    
    if (mediumColor != "") {
        $('html').css({"background": mediumColor});
        // button color when hover
        $('.btn').css({"background-color": mediumColor});
        $('.btn:hover').css({"border": "2px solid " + mediumColor,
                            "color": mediumColor});
    }

    if (darkColor !== "") {
        // main pane color
        $('#mainPane').css({"background": darkColor,
                        "box-shadow": "box-shadow: 10px 10px 5px rgba(150, 150,150, 0.2)"});
        $('.underline input[type="text"]:focus').css({"border-bottom": "2px solid " + darkColor});
    }

    if (textColor != "") {
        // network graph main color
        $('.label').css({"color": textColor});
        
        // background 
        $('html').css({"color": textColor});
        
        // button background
        $('.btn').css({"background-color": textColor});
        $('.btn:hover').css({"border": "2px solid " + textColor,
                            "color": textColor});
        
        // set color of the text
        $('#title').css("color", textColor);
        $('#description').css("color", textColor);
        $('.underline input[type="text"]').css({"color": textColor,
                                               "border-bottom": "2px solid " 
                                               + textColor});
        
        // set color of the links
        $(".link").css("stroke", textColor);
        $(".link").css("fill", textColor);

    }
    

    
}
