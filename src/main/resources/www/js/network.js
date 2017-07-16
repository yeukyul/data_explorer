
/* control fade in and out of the network html */
$(document).ready(function() {
    $('#mainPane, #network').hide();
    $('#network').fadeIn(500, function() { $('#mainPane').fadeIn(1500);});
});