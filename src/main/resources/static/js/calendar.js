var eventMap = {};
var weekInfo = [];

/* opens dialog window for events that creators have control over editing */
function openDialog(key) {
	value = eventMap[key.id];
	var date = value.date.split(" ");
	var day = date[0] + " " + date[1] + " " + date[2];
	var T = date[3];
	var am = date[4];
	var time = date[3].substring(0, T.length - 3) + " " + am;
	var dur = value.duration;
	form =
	'<form class="form-inline">' +
	'<img id="x-button" src="/img/x.png"/>' +
	'<div class="form-group">' +
	    '<div class="input-group">' +
		    '<input type="text" class="form-control" id="title" placeholder="Title" value="' + value.title + '">' +
	    '</div>' +
	    '<div class="input-group">' +
		    '<div class="input-group-addon"><span class="glyphicon glyphicon-calendar" aria-hidden="true"></span></div>' +
		    '<input type="text" id="datepicker" value="' + day + '"/>' +
	    '</div>' +
	    '<div class="input-group">' +
	    	'At <input id="dialog-time" type="text" class="time" onclick="timePicker()" value="' +  time + '" />' +
			' for <input id="duration" type="text" value="' + dur + '"/> minutes ' +
	    '</div>' +
	    '<div class="input-group">' +
	    	'<textarea type="text" class="form-control" id="descrip">'+ value.description + '</textarea>' +
	    '</div>' +
	    '<div class="input-group">' +
	    	'<div class="input-group-addon">@</div><input type="text" class="form-control" id="title" placeholder="People" value="' 	 				+value.attendees +'"/>' +
		'</div>' +
	  	'<div class="input-group">' +
		    '<div class="input-group-addon">@</div>' +
		    '<input type="text" class="form-control" id="title" placeholder="Groups" value="'+ value.group +'"/>' +
		'</div>' +
	 '<img id="delete-button" src="/img/minus.png"/><img id="check-button" src="\\img/check.png"/>' +
	'</form>'
	//$('<div>' + value.title + '<p>'+ value.description + '</p>' + '</div>').dialog({modal: true});
	$(form).dialog({modal: true});
}

/* opens dialog window for events that creators have control over editing */
function newEventDialog(date, time) {
	form =	
	'<form class="form-inline">' +
	'<img id="x-button" src="\\img/x.png"/>' +
	'<div class="form-group">' +
	    '<div class="input-group">' +
		    '<input type="text" class="form-control" id="title" placeholder="Title">' +
	    '</div>' +
	    '<div class="input-group">' +
		    '<div class="input-group-addon"><span class="glyphicon glyphicon-calendar" aria-hidden="true"></span></div>' +
		    '<input type="text" id="datepicker" value="' + date + '"/>' +
	    '</div>' +
	    '<div class="input-group">' +
	    	'At <input id="dialog-time" type="text" class="time" onclick="timePicker()" value="' +  time + '" />' +
			' for <input id="duration" type="text"/> minutes' +
	    '</div>' +
	    '<div class="input-group">' +
	    	'<textarea type="text" class="form-control" id="descrip" placeholder="description..."></textarea>' +
	    '</div>' +
	    '<div class="input-group">' +
	    	'<div class="input-group-addon">@</div><input type="text" class="form-control" id="title" placeholder="People"/>' +
		'</div>' +
	  	'<div class="input-group">' +
		    '<div class="input-group-addon">@</div>' +
		    '<input type="text" class="form-control" id="title" placeholder="Groups"/>' +
		'</div>' +
	 '<img id="check-button" src="\\img/check.png"/>' +
	'</form>'
	$(form).dialog({modal: true});
}

function datePicker() {
	$( "#datepicker" ).datepicker({
	    'format': 'M d, yyyy',
	    'autoclose': true
	});
}
  
function timePicker() {
	console.log('timepicker');
	$('#basicExample').timepicker();
}

                
function isTime(time) {
    return time.match(/(^([0-9]|[0-1][0-9]|[2][0-3]):([0-5][0-9])$)|(^([0-9]|[1][0-9]|[2][0-3])$)/);
}

function createEvent(eventSlot) {
	var slot = eventSlot.id;
	var day = getDay(slot.substring(0, 1));
	var time = getTime(slot.substring(1,3));
	newEventDialog(day, time);
}

function getDay(day) {
	var int = parseInt(day) - 1;
	return weekInfo[int].month + " " + weekInfo[int].day + ", " + weekInfo[int].year;
}

function getTime(time) {
	var int = parseInt(time);
	var hr = time % 12;
	if (int < 12) {
		if (hr == 0) {
			return "12:00 AM";
		} else {
			return hr + ":00 AM";
		}
	} else {
		if (hr == 0) {
			return "12:00 PM";
		} else {
			return hr + ":00 PM";
		}
	}
}

function dateTitle() {
	var toReturn = getWrittenDate(0) + " - " + getWrittenDate(6);
	document.getElementById("date-title").innerHTML = toReturn;
}

function getWrittenDate(index) {
	return weekInfo[0].month + " " + weekInfo[0].day + " " + weekInfo[0].year;
}

function updateDisplayedEvents() {
	var postParameters = {string: window.location.pathname};
	$.post("/getevents", postParameters, function(responseJSON){
		parseData(responseJSON);
	})
}

function leftArrow() {
	var daydetails = weekInfo[0].day  + "-" + weekInfo[0].month + "-" + weekInfo[0].year;
	var postParameters = {string: window.location.pathname, date: daydetails};
	$.post("/leftarrow", postParameters, function(responseJSON){
		parseData(responseJSON);
	})
}

function rightArrow() {
	var daydetails = weekInfo[6].day  + "-" + weekInfo[6].month + "-" + weekInfo[6].year;
	var postParameters = {string: window.location.pathname, date: daydetails };
	$.post("/rightarrow", postParameters, function(responseJSON){
		parseData(responseJSON);
	})
}

function parseData(responseJSON) {
	var responseObject = JSON.parse(responseJSON);
		var list = responseObject.events;
		var weekList = responseObject.week;
		console.log(weekList);
		//clear week array
		var weekArray = [];

		for (var i = 0; i < weekList.length; i++) {
			weekArray.push(weekList[i]);
		}
		// update week numbers
		changeWeekNumbers(weekArray);
		weekInfo = weekArray;

		//clear current map of events
		eventMap = {};
		
		var paras = document.getElementsByClassName('event');
		while(paras[0]) {
			paras[0].parentNode.removeChild(paras[0]);
		}
		//add all events
		for (var i = 0; i < list.length; i++) {
			var obj = JSON.parse(list[i]);
			eventMap[obj.id] = obj;
		}

		//display all events
		var key;
		for(key in eventMap) {
			value = eventMap[key];
			var newElem = document.createElement("div");
			newElem.className = "event";
			newElem.setAttribute("id", key);
			newElem.setAttribute("style", "height:"+getEventHeight(value.duration)+"px");
			var p = document.createTextNode(value.title);
			console.log(value.title);
			p.id = "description";
			newElem.appendChild(p);
			placeEvents(newElem, value);
		}
}


function placeEvents(elem, event) {
	var day = event.dayOfWeek;
	var date = event.date.split(" ");
	var time = date[3].split(":")[0];
	var am = date[4];
	var dayInt;
	switch(day) {
		case "Sunday":
			dayInt = 1;
			break;
		case "Monday":
			dayInt = 2;
			break;
		case "Tuesday":
			dayInt = 3;
			break;
		case "Wednesday":
			dayInt = 4;
			break;
		case "Thursday":
			dayInt = 5;
			break;
		case "Friday":
			dayInt = 6;
			break;
		case "Saturday":
			dayInt = 7;
			break;
	}

	// set time
	var givenTime;
	switch(am) {
		case "AM":
			time = time%12;
			break;
		case "PM":
			time = (time%12) + 12;
			break;
	}

	// adjust time by AM/PM
	if(time < 10) {
		time = "0" + time;
	}

	document.getElementById(dayInt + "" + time).appendChild(elem);
}

function getEventHeight(dur) {
	return (dur/60) * 37.5;
}

function changeWeekNumbers(weekArray) {
	for (var i = 0; i < 7; i++) {
		var imgID = "day" + (i+1);
		document.getElementById(imgID).src="\\img/num/" + weekArray[i].day + ".png";
	}
}

$(document).ready(function(e) {
	updateDisplayedEvents();
	
	$("#x-button").click(function(e) {
		console.log("here");
		dialog.close();
		$('#terms').dialog('close');
	});

	/* create new event when they click eventSlot */
	$(document).on('click','.eventSlot', function(e) {
		createEvent(e.target);
	});

	/* when they click an event open the dialog
		based on their ownership, so they can
		edit if they've created the event and view/remove
		themselves if they were not the creators. */
	$(document).on('click','.event', function(e) {
		e.stopPropagation();
		openDialog(e.target);
	});

	$(document).on('click','#x-button', function(e) {
	    var $dialog = $(this).parents('.ui-dialog-content');
	    $dialog.dialog('close');
	});
	
	$(document).on('click','#datepicker', function(e) {
	    datePicker();
	});
	
	$(document).on('click','#leftarrow', function(e) {
	    leftArrow();
	});
	
	$(document).on('click','#rightarrow', function(e) {
	    rightArrow();
	});
});
