/* 
 * graph.js - reponsible for background network animation of index.html
 *
 * Code cited from http://bl.ocks.org/d3noob/8043434
 */

/* initial nodes */
var links = [
 {source: "f", target: "g", type: "suit"},
 {source: "f", target: "h", type: "suit"},
 {source: "h", target: "i", type: "suit"},
 {source: "g", target: "i", type: "suit"},
 {source: "i", target: "j", type: "suit"},
    
 {source: "k", target: "m", type: "suit"},
 {source: "l", target: "m", type: "suit"},
 {source: "k", target: "l", type: "suit"},
 {source: "m", target: "n", type: "suit"},
 {source: "l", target: "n", type: "suit"},
    
 {source: "a", target: "b", type: "suit"},
 {source: "b", target: "c", type: "suit"},
 {source: "c", target: "d", type: "suit"},
 {source: "c", target: "e", type: "suit"},
    
 {source: "p", target: "p", type: "suit"},
 {source: "p", target: "q", type: "suit"},
 {source: "q", target: "r", type: "suit"},
 {source: "r", target: "s", type: "suit"},
 {source: "r", target: "t", type: "suit"},
 {source: "t", target: "u", type: "suit"},
 {source: "s", target: "v", type: "suit"},
 {source: "u", target: "v", type: "suit"},
 {source: "v", target: "x", type: "suit"},
 {source: "y", target: "x", type: "suit"},
 {source: "x", target: "w", type: "suit"},
    
  {source: "aa", target: "bb", type: "suit"},
  {source: "bb", target: "cc", type: "suit"},
  {source: "dd", target: "cc", type: "suit"},
  {source: "dd", target: "cc", type: "suit"},
  {source: "ee", target: "ee", type: "suit"},
  {source: "ee", target: "ff", type: "suit"},
  {source: "ff", target: "gg", type: "suit"},
  {source: "gg", target: "ee", type: "suit"},
  {source: "hh", target: "ii", type: "suit"},
  {source: "jj", target: "jj", type: "suit"},
  {source: "kk", target: "kk", type: "suit"},
];


var nodes = {};

// Compute the distinct nodes from the links.
links.forEach(function(link) {
  link.source = nodes[link.source] || (nodes[link.source] = {name: link.source});
  link.target = nodes[link.target] || (nodes[link.target] = {name: link.target});
});

var width = $(window).width(),
    height = $(window).height()/3;

var force = d3.layout.force()
    .nodes(d3.values(nodes))
    .links(links)
    .size([width, height])
    .linkDistance(100)
    .charge(-1000)
    .on("tick", tick)
    .start();

var svg = d3.select("#graph").append("svg")
    .attr("width", width)
    .attr("height", height);

var link = svg.selectAll(".link")
    .data(force.links())
    .enter().append("line")
    .attr("class", "link");

var node = svg.selectAll(".node")
    .data(force.nodes())
  .enter().append("g")
    .attr("class", "node")
    .on("mouseover", mouseover)
    .on("mouseout", mouseout)
    .call(force.drag);

node.append("circle")
    .attr("r", 8)
    .attr("fill", "rgb(175,175,175)");


function tick() {
  link
      .attr("x1", function(d) { return d.source.x; })
      .attr("y1", function(d) { return d.source.y; })
      .attr("x2", function(d) { return d.target.x; })
      .attr("y2", function(d) { return d.target.y; });

  node
      .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
}

function mouseover() {
  d3.select(this).select("circle").transition()
      .duration(750)
      .attr("r", 16);
}

function mouseout() {
  d3.select(this).select("circle").transition()
      .duration(750)
      .attr("r", 8);
}