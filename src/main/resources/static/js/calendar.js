var eventMap = {};
eventMap['harsha'] = {title: 'Soccer Game w/ Botswana'};
var eventArray = [];

function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");
    ev.target.appendChild(document.getElementById(data));
}

function updateDisplayedEvents() {
	var postParameters = {string: "test"};
	$.post("/getevents", postParameters, function(responseJSON){

		var responseObject = JSON.parse(responseJSON);
		var list = responseObject.events;

		//clear current map of events
		eventMap = {};
		eventArray = [];
		var paras = document.getElementsByClassName('event');
		while(paras[0]) {
			paras[0].parentNode.removeChild(paras[0]);
		}
		//add all events
		for (var i = 0; i < list.length; i++) {
			var obj = JSON.parse(list[i]);
			console.log(obj);
			eventMap[obj.id] = obj;
			eventArray.push(obj)
		}
		//display all events
		console.log(eventArray);
		for (var i = 0; i < eventArray.length; i++) {
			console.log(eventArray[i].title);
			var newElem = document.createElement("div");
			newElem.className = "event";
			newElem.setAttribute("draggable", "true"); 
			newElem.setAttribute("ondragstart", "drag(event)");
			var p = document.createTextNode(eventArray[i].title);
			console.log(ev.title);
			p.id = "description";
			newElem.appendChild(p);
			placeEvents(newElem, ev);
		}
	})
}

function placeEvents(var elem, var event) {
	var day = event.dayOfWeek;
	var date = event.date.split(" ");
	var time = date[3].split(":")[0];
	var dayInt;
	switch(day) {
		case "Monday":
			dayInt = 1;
			break;
		case "Tuesday":
			dayInt = 2;
			break;
		case "Wednesday":
			dayInt = 3;
			break;
		case "Thursday":
			dayInt = 4;
			break;
		case "Friday":
			dayInt = 5;
			break;
		case "Saturday":
			dayInt = 6;
			break;
		case "Sunday":
			dayInt = 7;
			break;
	elem.setAttribute(paddingBottom, 10px);
	document.getElementById(dayInt + time).appendChild(elem);
	}
}


$(document).ready(function(e) {
	updateDisplayedEvents();

    $("#calendar").click(function(e) {
	    var calendarRect = document.getElementById('cal-days').getBoundingClientRect();
	    var offsetx = calendarRect.left;
	    var offsety = (calendarRect.top + Math.abs(calendarRect.top))/2;
	    var relativePosition = {
	      left: $(document.getElementById('calendar')).scrollLeft(),
	      top : $(document.getElementById('calendar')).scrollTop()
	    };
	    var x = e.clientX - offsetx + relativePosition.left;
	    var y = e.clientY - offsety + relativePosition.top;
	    // console.log('relative x: ' + relativePosition.left + ' y: ' + relativePosition.top);
	     console.log('offset x: ' + offsetx + ' y: ' + offsety);
	    // console.log('position x: ' + e.clientX + 'position y: ' + e.clientY);
	    console.log('x: ' + x + ' y: ' + y);
	});

	$(function() {
    	$( "#dialog" ).dialog();
  	});

	$(".eventSlot").on("drop", drop(event));

	$(".eventSlot").on("drag", drag(event));	

	$(".eventSlot").on("allowDrop", allowDrop(event));	



 //    $("eventSlot").click(function(){
 //    	console.log('here');
	//   $(this).ondragover = function(event) {
	//     allowDrop(event);
	//   };
	//   $(this).ondrop = function(event) {
	//     drop(event);
	//   };
	// });
       
	// /* Events fired on the drop target */
	// document.getElementById("eventSlot").ondragover = function(event) {
	//     allowDrop(event);
	// };

	// document.getElementById("eventSlot").ondrop = function(event) {
	//     drop(event);
	// };
});