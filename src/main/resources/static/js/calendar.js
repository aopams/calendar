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
	'<form class="form-inline" id ="newEventForm">' +
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
	    	'<div class="input-group-addon">@</div><input type="text" class="form-control" id="attendees" placeholder="People" value="' 	 				+value.attendees +'"/>' +
		'</div>' +
	  	'<div class="input-group">' +
		    '<div class="input-group-addon">@</div>' +
		    '<input type="text" class="form-control" id="group" placeholder="Groups" value="'+ value.group +'"/>' +
		'</div>' +
	 '<img id="delete-button" src="/img/minus.png"/><img id="check-button" src="\\img/check.png"/>' +
	'</form>'
	//form = $(form).resizable({disabled: true});
	$(form).dialog({ modal: true}).resizable("disable");
}

function openGoogleEvent(key) {
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
		    '<input type="text" class="form-control" id="title" placeholder="Title" value="' + value.title + '" readonly>' +
	    '</div>' +
	    '<div class="input-group">' +
		    '<div class="input-group-addon"><span class="glyphicon glyphicon-calendar" aria-hidden="true" readonly></span></div>' +
		    '<input type="text" value="' + day + '" readonly />' +
	    '</div>' +
	    '<div class="input-group">' +
	    	'At <input id="dialog-time" type="text" class="time" value="' +  time + '" readonly="true"/>' +
			' for <input id="duration" value="' + dur + '" readonly/> minutes ' +
	    '</div>' +
	    '<div class="input-group">' +
	    	'<textarea type="text" class="form-control" id="descrip" readonly>'+ value.description + '</textarea>' +
	    '</div>' +
	    '<div class="input-group">' +
	    	'<div class="input-group-addon">@</div><input type="text" class="form-control" id="attendees" placeholder="People" value="' 	 				+value.attendees +'" readonly/>' +
		'</div>' +
	  	'<div class="input-group">' +
		    '<div class="input-group-addon">@</div>' +
		    '<input type="text" class="form-control" id="group" placeholder="Groups" value="'+ value.group +'" readonly/>' +
		'</div>' +
	 '<img id="google-button" src="\\img/google.png"/>' +
	'</form>'
	$(form).dialog({modal: true, resizeable: false});
}


/* opens dialog window for events that creators have control over editing */
function newEventDialog(date, time) {
	form =	
	'<form class="input-form-inline">' +
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
	    	'<div class="input-group-addon">@</div><input type="text" class="form-control" id="attendees"placeholder="People"/>' +
		'</div>' +
	  	'<div class="input-group">' +
		    '<div class="input-group-addon">@</div>' +
		    '<input type="text" class="form-control" id="group" placeholder="Groups"/>' +
		'</div>' +
	 '<img id="new-event-button" src="\\img/check.png"/>' +
	'</form>'
	$(form).dialog({modal: true, resizeable: false});
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
	return weekInfo[index].month + " " + weekInfo[index].day + ", " + weekInfo[index].year;
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
			if (key < 0) {
				newElem.className = "google-event";
			} else {
				newElem.className = "event";
			}
			newElem.setAttribute("id", key);
			newElem.setAttribute("style", "height:"+getEventHeight(value.duration)+"px");
			var p = document.createTextNode(value.title);
			console.log(value.title);
			p.id = "description";
			newElem.appendChild(p);
			placeEvents(newElem, value);
		}
		//change the date title on the top of the calendar
		dateTitle();
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
	
	var slot_id = dayInt + "" + time;
	placeEventDiv(slot_id, elem);
}

function placeEventDiv(id, elem) {
	var size = $("#" + id + " > div").size() + 1;
	document.getElementById(id).appendChild(elem);
	$( "#" + id ).children().css( "width", (1/size * 100) + "%");
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

function newEvent() {
	var title = document.getElementById('title').value;
	var date = document.getElementById('datepicker').value;
	var time = document.getElementById('dialog-time').value;
	var dur = document.getElementById('duration').value;
	var descrip = document.getElementById('descrip').value;
	var atten = document.getElementById('attendees').value;
	var group = document.getElementById('group').value;
	var correctTime = getDBTime(date, time);
	console.log(correctTime);
	var postParameters = {string: window.location.pathname, title: title, date: correctTime,
		time: time, duration: dur, description: descrip, attendees: atten,
		group: group
	};

	$.post("/newevent", postParameters, function(responseJSON){

		
		
/*
		
		if(responseJSON.status == 1) {
			$dialog.dialog('destroy');
		} else {
			alert('ranking: ' + responseJSON.message);
		}
*/
		
	})
}

function getDBTime(date, time) {
	date = date.replace(",", "");
	console.log(date);
	arr = date.split(" ");
	var hours = Number(time.match(/^(\d+)/)[1]);
	var minutes = Number(time.match(/:(\d+)/)[1]);
	var AMPM = time.match(/\s(.*)$/)[1];
	if(AMPM == "PM" && hours<12) hours = hours+12;
	if(AMPM == "AM" && hours==12) hours = hours-12;
	var sHours = hours.toString();
	var sMinutes = minutes.toString();
	if(hours<10) sHours = "0" + sHours;
	if(minutes<10) sMinutes = "0" + sMinutes;
	console.log(sHours + ":" + sMinutes);
	return arr[1] + "-" + arr[0] + "-" +
		arr[2] + " " + sHours + ":" + sMinutes;
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
	
	$(document).on('click','.google-event', function(e) {
		console.log('here');
		e.stopPropagation();
	    openGoogleEvent(e.target);
	});

	$(document).on('click','#x-button', function(e) {
	    var $dialog = $(this).parents('.ui-dialog-content');
	    $dialog.dialog('destroy');
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
	
	$(document).on('click','#new-event-button', function(e) {
	    newEvent();
	    var $dialog = $(this).parents('.ui-dialog-content');
	    $dialog.dialog('destroy');
	    updateDisplayedEvents();
	});
});
