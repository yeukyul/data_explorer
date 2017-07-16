/* 
 * main.js - reponsible for upcalling to java backend, 
 * page transition, and display error msg 
 *
 * @author Yeuk Yu Lee
 */

var ERROR_MSG = "INVALID START POINT. PLEASE ENTER A DIFFERENT KEYWORD.";
var startPoint;

// ensures smooth element entry
$(document).ready(function() {
    $('#title, #description, .row').hide();
    $('#title').fadeIn(500);
    $('#description').fadeIn(1500);
    $('.row').fadeIn(3000);
});


/* ------ Error handling */

// displayErrorMessage displays error message in index.html
function displayErrorMessage() {
    $('#error').html(ERROR_MSG);
}

// clearErrorMessage clears error message in index.html
function clearErrorMessage() {
    $('#error').html("");
}


/* ------ Page Redirection */

// moveToGraph bring the browser to graph.html
function moveToGraph() {
        
    // having the graph fade out
    $("#graph").fadeOut(800);
    $(".center-block").fadeOut(1200);
    
    window.location = "graph.html";
    
}

// moveToGraph brings the browser to index.html
function moveToMain() {
    window.location = "index.html";
}

// move to blank brings the browser to blank.html
function moveToBlank() {
    window.location = "blank.html";
}


/* ------ Accessor Function */

/*
 * Access startint point of the graph
 */
function getStartPoint() {
    return startPoint;
}

/* ------ Event Handling Function */
$('#submit').click(function() {
    // reading user input
    startPoint = $('#textinput').val();
    
    // invoking java function 
    java.exploreClicked(startPoint);
});

$('#start').click(function() {
    // invoking java function 
    java.startClicked();
})


/* ----------- print message for debugging purpose */
function printToConsole(msg) {
    java.printMessage(msg);
}

