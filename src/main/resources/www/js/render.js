/* 
 * render.js - D3 code responsible for reponsible for receiving graph 
 * related upcalls from java backend and drawing network graph.
 */



/* Globals Constants */
var N_NODE = nNodes,
    START_ANGLE = 10,
    END_ANGLE = 100;

var width = $(window).width(),
    height = $(window).height();

var sm_r = width/30,
    bg_r = width/25,
    R = width*2/5;

var CENTER_X = 2 * sm_r,
    CENTER_Y = height - height/5;

var SELECTED_COLOR = "#E37B40";


/* Globals */
var nodes = [], 
    links = [];

// parameter of the graph view point
var viewportX = 0,
    viewportY = 0;

var appendedNodes = 0;
var uid = 0;


/* keep track of how many node user has selected before */
var nClickedNodes = 1;


/* ---------- node class function; setter of attributes */

function setX(x) {
    this.x = x;
}

function setY(y) {
    this.y = y;
}

function setIndex(i) {
    this.i = i;
}

function setName(name) {
    this.name = name;
}


/* Globals */
var COLS = 12; // matrialize grid system has 12 columns
var rows, cols;
var nNodes = 3;
var startPoint = "hello";
var neighborNodes = null;


// set the number of children in network
function setnNodes(n) {
    start(n);
		return true;
}

// return the number of children specified
function getnNodes() {
    return nNodes;
}


/*
 * getter of start point
 */
function getStartPoint() {
    return startPoint;
}

/*
 * setter of start point
 */
function setStartPoint(name) {	
    startPoint = name;
    nodes[(nClickedNodes-1)].setName(name);
    restart();
		return true;
}

/* ---------- a arbitrary placeholder data */

var startNode = {name: "", 
                 x: CENTER_X, y: CENTER_Y, 
                 i: 0, fixed:true,
                id : uid,
                setIndex : setIndex, 
                setX: setX,
                setY: setY,
                setName: setName};



/* ---------- initiation functions */

/* 
 * Add a starting node to the node list
 */
function addStartNode(node) {
    nodes.push(node);
}


/*
 * add starting point to the graph
 */
function addStartPoint(name) {
    var name = getStartPoint();
    var newNode = {name: name, 
                    x: CENTER_X, y: CENTER_Y, 
                    i: 0, fixed: true,
                    id: uid,
                    setIndex : setIndex, 
                    setX: setX,
                    setY: setY,
                    setName: setName};
    addStartNode(newNode);
}

/*
 * populateNodes populate the nodes list with placeholders recursively
 */
function populateNodes(n, r, cx, cy, startAngle, endAngle, startIndex, level) {
	var tab = ""
	for (var i = 0; i<(2-level); i++) {
		tab = tab + "    ";
	}
	
    var len, delta, angle, x, y, thisNode;

    // base case: if level = 0, stop appending nodes
    if (level === 0) {
        return;
    }

    // add neigboring nodes
    len = nodes.length;
    delta = (endAngle - startAngle) / n;
    // calculate appropriate coordindate of each child
    for (var i = 0; i < n; i++) {

        // calculate increment angle
        angle = startAngle + delta * i;

        // obtain x, y coordinate
        x = cx + r * Math.cos(-angle * (Math.PI/180));
        y = cy + r * Math.sin(-angle * (Math.PI/180));

        // create instance of the node
        thisNode = {name: "", 
                    x: x, y: y, i: len+i, fixed: true, id: uid,
                    setIndex : setIndex, setX: setX, setY: setY,
                    setName: setName};
        // push node to nodes list 
        nodes.push(thisNode);

        // push an edge with parent node
        links.push({source: nodes[startIndex], target: thisNode});
        uid++;
    }

    // for every newly appended node, add children nodes
    for (var i = len; i < parseInt(len)+parseInt(n); i++) {
			if (i >= nodes.length) {
			}
			// get parent node
        thisNode = nodes[i]; //
        populateNodes(n, r, thisNode.x, thisNode.y, 
                      startAngle, endAngle, i, level-1);
    }
}

/*
 *  getNode finds node that matches the name
 */
function getNode(findID) {
    for (var i = nClickedNodes; i < nodes.length; i++) {
        if (nodes[i].id == findID) {
            return i;
        }
    }
    return null;
}


/*
 *  getChildNode returns the index of the child
 */
function getChildNode(parentIndex) {
    var children = [];
    for (var i = 0; i < N_NODE; i++) {
        children.push(N_NODE * (parentIndex - nClickedNodes + 1) + i + nClickedNodes);
    }
    return children;
}

/* 
 * getParentNode returns the index of the parent
 */ 
function getParentNode(childIndex) {
    return nClickedNodes - 1;
}

/* populate the network with appropriate dimension and location */
var force, svg, link, node;

function start(n) {
    uid++;
    N_NODE = n;
    addStartNode(startNode);
    populateNodes(N_NODE, R, 
                  CENTER_X, CENTER_Y, 
                  START_ANGLE, END_ANGLE, 0, 2);
    

    force = d3.layout.force()
    .nodes(nodes)
    .links(links)
    .size([width, height])
    .linkDistance(R)
    .charge(0)
    .start();

    /* create SVG within destinated div */
    svg = d3.select("#network").append("svg")
    .attr("width", width)
    .attr("height", height);

    /* add edges to graph */
    link = svg.append("g")
    .selectAll(".link")
    .data(force.links())
    .enter().append("line")
    .attr("class", "link");

    /* add node to graph */
    node = svg.append("g")
        .selectAll(".node")
        .data(force.nodes())
        .enter().append("g")
        .attr("class", "node")
        .on("mouseover", mouseover)
        .on("mouseout", mouseout)  
        .on("click", onClick)
        .attr("id", function(d) { return d.id; });

    /* add circle to node */
    node.append("circle")
        .attr("r", bg_r)
        .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });

    /* add text to node */
    node.append("text")
        .attr("class", "label")
        .attr("dx", 12)
        .attr("dy", ".35em")
        .text(function(d) { return d.name })
        .attr("transform", function(d) { return "translate(" + (d.x-10) + "," + d.y + ")"; })
        .attr("text-anchor", "middle");
    
    restart();
}



/* -- D3 Network Rendering */


/* ----- upcall interface for java */

/*
 * setter of neighboring ndoes
 */
function setNeighborNodes(nodeList) {
    if (nodeList.length < N_NODE) {
        return;
    }
    neighborNodes = nodeList;
    for (var i = nClickedNodes; i < nClickedNodes + N_NODE; i++) {
        nodes[i].setName(nodeList[i-nClickedNodes]);
    }
    
	  return true;
}


/* --- event handling function */
// when hover over node
function mouseover() {
  d3.select(this).select("circle").transition()
      .duration(500)
      .attr("r", sm_r);
}

// when leave node
function mouseout() {
  d3.select(this).select("circle").transition()
      .duration(500)
      .attr("r", bg_r);
}


/*
 * register a event handling function
 */ 
function onClick() {

    // if selected parent node, ignore
    var selectedNode = d3.select(this);
    if (selectedNode == null) {
        return;
    }

    var id = selectedNode.attr('id');
    var matchIndex = getNode(id);
    var matchNode = nodes[matchIndex];
	  var name = matchNode.name;
    var start = nodes[0];

    if (matchNode == null) {
        return;
    }

    // if selected a grandchild node, return
    if (matchIndex < nClickedNodes || 
        matchIndex > (nClickedNodes + N_NODE)) {
        return;
    }
	
	  if (selectedNode.attr("id") == start.id) {
        return;
		}

    /* invoking java function with current node name */
    java.neighborClicked(name);

    selectedNode
        .attr("fill", SELECTED_COLOR);

    // shift graph to make child node the new center
    var parentNode = nodes[getParentNode(matchIndex)];
    var dx = matchNode.x - parentNode.x;
    var dy = matchNode.y - parentNode.y;
    shift(dx, dy);

    // delete unrelevant nodes and awaits for return
    var status = deleteSiblingNodes(matchIndex);
    status = shiftIndex();
	
	   // increment count
    nClickedNodes++;

    // add new children layer
    addNewGrandChildLevel();
	
    java.neighborClicked(name);

}




/*
 * locationUpdate updates the location of nodes and edges
 */
function locationUpdate() {
    link
        .transition()
        .attr("x1", function(d) { return d.source.x - viewportX; })      
        .attr("y1", function(d) { return d.source.y - viewportY; })
        .attr("x2", function(d) { return d.target.x - viewportX; })
        .attr("y2", function(d) { return d.target.y - viewportY; });

    node
        .selectAll("circle")
        .transition()
        .attr("transform", function(d) { return "translate(" + (d.x - viewportX) + "," + (d.y - viewportY) + ")"; });

    node
        .selectAll("text")
        .text(function(d) { return d.name })
        .transition()
        .attr("transform", function(d) { 
            return "translate(" + (d.x - viewportX - 10) + "," 
                + (d.y-viewportY) + ")"; 
        });
}


/*
 * shift shifts the graph by dx and dy amount
 */
function shift(dx, dy) {
    viewportX += dx;
    viewportY += dy;
}


/*
 * shiftIndex re-assign all indices to the list of nodes
 */
function shiftIndex() {
    // shift the indices after nodes are deleted
    for (var i = 0; i < nodes.length; i++) {
        nodes[i].setIndex(i);
    }
}

/*
 * addNewGrandChildLevel appends new layer of grandchildren of a given
 * parent node
 */
function addNewGrandChildLevel() {
    var len = nodes.length;
    for (var i = nClickedNodes; i < len; i++) {
        var thisNode = nodes[i];
        populateNodes(N_NODE, R, thisNode.x + bg_r, thisNode.y, 
                    START_ANGLE, END_ANGLE, thisNode.i, 1);
    }
    return;
}

/*
 * Delete all the sibling nodes of a given index into nodes
 */
function deleteSiblingNodes(matchIndex) {

    // retain all past parent nodes
    var nodeToRetain = rep(0, nClickedNodes);

    // retain the matching node
    nodeToRetain.push(matchIndex);

    // retain the children of the matching nodes
    nodeToRetain.push.apply(nodeToRetain, getChildNode(matchIndex));

    // obtain index of the nodes to be delete
    var nodeToDelete = getDeleteNode(nodeToRetain);

    // delete sibling nodes
    for (var i = nodeToDelete.length - 1; i >= 0; i--) {
        deleteNode(nodeToDelete[i]);
    }
}

/*
 * rep creates an array from start to end, increment by 1
 */
function rep(start, end) {
    var arr = [];
    if (end < start) return;
    for (var i = start; i < end; i++) {
        arr.push(i);
    }
    return arr;
}

/*
 * GetNodes that should be deleted
 */
function getDeleteNode(nodesToRetain) {
    var nodeIndex = [];
    var j = 0;
    for (var i = 0; i < nodes.length; i++) {
        if (i === nodesToRetain[j]) {
            j++;
        } else {
            nodeIndex.push(i);
        }
    }
    return nodeIndex;
}

/*
 * contains - check array containment
 */
function contains(a, obj) {
    for (var i = 0; i < a.length; i++) {
        if (a[i] === obj) {
            return true;
        }
    }
    return false;
}

/*
 * deleteNode deletes node at certain index in the nodes list
 */
function deleteNode(nodeIndex) {
    nodes.splice(nodeIndex, 1);
    links = links.filter(function(l) {
        return l.source.i !== nodeIndex && l.target.i !== nodeIndex;
    });
    d3.event.stopPropagation();
}


/*
 * restart function for redrawing network graph
 */
function restart() {
    /* add links to graph */
    link = link.data(links, function(d) { return d.source.id + "_" + d.target.id; });
    link.exit().remove();
    link.enter().append("line")
        .attr("class", "link");

    /* add node to graph */
    node = node.data(nodes, function(d) { return d.id; });
    node.exit().remove();
    var newNode = node.enter()
                    .append("g")
                    .attr("class", "node")
                    .on("mouseover", mouseover)
                    .on("mouseout", mouseout)
                    .on("click", onClick)
                    .attr("id", function(d) { return d.id; });
	
    newNode.append("circle")
            .attr("r", bg_r)
            .attr("transform", function(d) { return "translate(" + (d.x - viewportX) + "," + (d.y - viewportY) + ")"; });

    newNode.append("text")
            .attr("class", "label")
            .attr("dx", 12)
            .attr("dy", ".35em")
            .text(function(d) { return d.name })
            .attr("text-anchor", "middle")
						.attr("transform", function(d) { return "translate(" + (d.x - viewportX) + "," + (d.y - viewportY) + ")"; });

    // restart layout
    force.start();
    locationUpdate();
}
