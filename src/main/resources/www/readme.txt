This document outlines the files in the www directory and their purposes.


html document
    - start.html: 
        beginning page that awaits users to select plug in
    - index.html: 
        index page that allows users to type in start string
    - graph.html: 
        html page that renders actual data visualization
    
    
javascript document
    - main.js:
        reponsible for upcalling to java backend, page transition, and display error msg 
    - update.js:
        reponsible for receiving upcalls from java backend and updating content of main panel that can be specified in java's display plug-in.
    - network.js:
        responsible for fading in and out of graph.html to ensure smooth transition
    - graph.js:
        D3 code responsible for background network animation
    - render.js:
        D3 code responsible for rendering network graph in graph.html
        
        
Event handling functions
    - for button clicks, located at the bottom of main.js