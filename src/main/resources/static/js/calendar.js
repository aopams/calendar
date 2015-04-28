/* GLOBAL VARIABLES */
//global hashmap that holds all the events in the calendar.
var eventMap = {};

//global array to hold information about the week we are displaying.
var weekInfo = [];

//colors that we assign to event boxes
var eventColors = ["#FF9393", "#98FB98", "#FFFF99", "#c0aee0", "#e0d9ae", "#A9D8B6", "lightgoldenrodyellow"]
/* END GLOBAL VARIABLES */

/* POST HANDLERS */
/* updates all the events on the caledar at the given moment. */
function updateDisplayedEvents() {
	var postParameters = {string: window.location.pathname};
	$.post("/getevents", postParameters, function(responseJSON){
		parseData(responseJSON);
	})
}

/* switch to previous week */
function leftArrow() {
	var daydetails = weekInfo[0].day  + "-" + weekInfo[0].month + "-" + weekInfo[0].year;
	var postParameters = {string: window.location.pathname, date: daydetails};
	$.post("/leftarrow", postParameters, function(responseJSON){
		parseData(responseJSON);
	})
}

/* switch to next week */
function rightArrow() {
	var daydetails = weekInfo[6].day  + "-" + weekInfo[6].month + "-" + weekInfo[6].year;
	var postParameters = {string: window.location.pathname, date: daydetails };
	$.post("/rightarrow", postParameters, function(responseJSON){
		parseData(responseJSON);
	})
}

/* create new event */
function newEvent() {
	var title = document.getElementById('title').value;
	var date = document.getElementById('datepicker').value;
	var time = document.getElementById('dialog-time').value;
	var dur = document.getElementById('duration').value;
	var descrip = document.getElementById('descrip').value;
	var atten = document.getElementById('attendees').value + ",";
	var group = document.getElementById('group').value;
	var correctTime = getDBTime(date, time);
	var postParameters = {string: window.location.pathname, title: title, date: correctTime,
		time: time, duration: dur, description: descrip, attendees: atten,
		group: group
	};

	$.post("/newevent", postParameters, function(responseJSON){
/*
	add code to handle messaging for ranking, etc
		if(responseJSON.status == 1) {
			$dialog.dialog('destroy');
		} else {
			alert('ranking: ' + responseJSON.message);
		}
*/
	})
}

/* delete event */
function deleteEvent(id) {
	var id = id;
	var postParameters = {string: window.location.pathname, id: id};

	$.post("/removeevent", postParameters, function(responseJSON){
/*
	add code to handle messaging for ranking, etc
		if(responseJSON.status == 1) {
			$dialog.dialog('destroy');
		} else {
			alert('ranking: ' + responseJSON.message);
		}
*/
	})
}

function removeUserEvent(id) {
	var id = id;
	var postParameters = {string: window.location.pathname, id: id};

	$.post("/removeuserevent", postParameters, function(responseJSON){
/*
	add code to handle messaging for ranking, etc
		if(responseJSON.status == 1) {
			$dialog.dialog('destroy');
		} else {
			alert('ranking: ' + responseJSON.message);
		}
*/
	})
}

/* END POST HANDLERS */

/* opens dialog window for events that creators have control over editing */
function openDialog(key, google) {
	value = eventMap[key.id];
	var date = value.date.split(" ");
	var day = date[0] + " " + date[1] + " " + date[2];
	var T = date[3];
	var am = date[4];
	var time = date[3].substring(0, T.length - 3) + " " + am;
	var dur = value.duration;
	if (google == 1 || google == 2) {
		var ds = 'disabled';
	} else {
		var ds = '';
	}
	form =
	'<form class="form-inline" id ="newEventForm">' +
	'<div id="dialog-event-id">' + value.id + '</div>' +
	'<div class="form-group dialog-form">' +
		'<img id="x-button" src="/img/x.png"/>' +
	    '<div class="input-group">' +
		    '<input type="text" class="form-control" id="title" placeholder="Title" value="' + value.title + '"'+ ds +'>' +
	    '</div>' +
	    '<div class="input-group margin-group">' +
		    '<div class="input-group-addon"><span class="glyphicon glyphicon-calendar" aria-hidden="true"></span></div>' + 
		    '<input type="text" class="form-control" id="datepicker" placeholder="date" value="' + day + '"'+ ds +'>' +
	    '</div>' +
	    '<div class="input-group margin-group">' +
	    	'<div class="formatted">At </div>' +
	    		'<input id="dialog-time" type="text" class="form-control" onclick="" value="' + time + '"'+ ds +' >' +
			'<div class="formatted">   for </div>' +
				'<input id="duration" type="text" class="form-control" value="' + dur + '"'+ ds +'><div class="formatted"> min </div>' +
	    '</div>' +
	    '<div class="input-group margin-group">' +
	    	'<textarea type="text" class="form-control" id="descrip" placeholder="description..."'+ ds +'>'+ value.description + '</textarea>' +
	    '</div>' +
	    '<div class="input-group margin-group">' +
	    	'<div class="input-group-addon">@</div><input type="text" class="form-control" id="attendees" placeholder="People" value="' 	 				+value.attendees +'"'+ ds +'/>' +
		'</div>' +
	  	'<div class="input-group margin-group">' +
		    '<div class="input-group-addon">@</div>' +
		    '<input type="text" class="form-control" id="group" placeholder="Group" value="'+ value.group +'"'+ ds +'/>' +
		'</div>' +
		'<div class="margin-group-xl">';
		
	if (google == 1) {
		form = form + '<img id="google-button" src="\\img/google.png"/>';
	} else if (google == 2) {
		form = form + '<img id="remove-button" src="\\img/minus.png"/>';
	} else {
		form = form + '<img id="delete-button" src="\\img/minus.png"/><img id="edit-button" src="\\img/check.png"/>';
	}
	form = form + '</div> </div> </form>';
	$(form).dialog({ modal: true, resizable: false});
}

/* opens dialog window for events that creators have control over editing */
function newEventDialog(date, time) {
	form =
	'<form class="form-inline" id ="newEventForm">' +
	'<div class="form-group dialog-form">' +
		'<img id="x-button" src="/img/x.png"/>' +
	    '<div class="input-group">' +
		    '<input type="text" class="form-control" id="title" placeholder="Title">' +
	    '</div>' +
	    '<div class="input-group margin-group">' +
		    '<div class="input-group-addon"><span class="glyphicon glyphicon-calendar" aria-hidden="true"></span></div>' + 
		    '<input type="text" class="form-control" id="datepicker" placeholder="date" value="' + date + '">' +
	    '</div>' +
	    '<div class="input-group margin-group">' +
	    	'<div class="formatted">At </div>' +
	    		'<input id="dialog-time" type="text" class="form-control" value="' + time + '">' +
			'<div class="formatted">  for </div>' +
				'<input id="duration" type="text" class="form-control"><div class="formatted"> min </div>' +
	    '</div>' +
	    '<div class="input-group margin-group">' +
	    	'<textarea type="text" class="form-control" id="descrip" placeholder="description..."></textarea>' +
	    '</div>' +
	    '<div class="input-group margin-group">' +
	    	'<div class="input-group-addon">@</div><input type="text" class="form-control" id="attendees" placeholder="People" value=""/>'+
		'</div>' +
	  	'<div class="input-group margin-group">' +
		    '<div class="input-group-addon">@</div>' +
		    '<input type="text" class="form-control" id="group" placeholder="Group"/>' +
		'</div>' +
		'<div class="margin-group-xl">'+ 
		'<img id="new-event-button" src="\\img/check.png"/>' +
		'</div> </div> </form>';
	$(form).dialog({ modal: true, resizable: false});
}

/* opens calendar view selection */
function datePicker() {
	$( "#datepicker" ).datepicker({
	    'format': 'M d, yyyy',
	    'autoclose': true
	});
}

/* opens time view selection */
function timePicker() {
	$('#dialog-time').timepicker();
}

/* opens dialog to create a new event */
function createEvent(eventSlot) {
	var slot = eventSlot.id;
	var day = getDay(slot.substring(0, 1));
	var time = getTime(slot.substring(1,3));
	newEventDialog(day, time);
}

//SUB createEvent()
/* gets day information to autofill in new event dialog */
function getDay(day) {
	var int = parseInt(day) - 1;
	return weekInfo[int].month + " " + weekInfo[int].day + ", " + weekInfo[int].year;
}

//SUB createEvent()
/* gets time of the event slot clicked to create new event */
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

/* changes date on top of the calendar to reflect current week we are looking at. */
function weekTitle() {
	var toReturn = getWrittenDate(0) + " - " + getWrittenDate(6);
	document.getElementById("date-title").innerHTML = toReturn;
}

//SUB weekTitle()
/* gets written form of a date */
function getWrittenDate(index) {
	return weekInfo[index].month + " " + weekInfo[index].day + ", " + weekInfo[index].year;
}

// IMPORTANT!
/* 
	parses large piece of data given by the backend, adds events to the calendar
	updates margins, and call approriate functions to adjust event elements
*/
function parseData(responseJSON) {
	var responseObject = JSON.parse(responseJSON);
		var list = responseObject.events;
		var weekList = responseObject.week;
		//clear week array
		var weekArray = [];

		//append week list to global week array
		for (var i = 0; i < weekList.length; i++) {
			weekArray.push(weekList[i]);
		}
		// update week numbers
		changeWeekNumbers(weekArray);
		weekInfo = weekArray;

		// clear current map of events
		eventMap = {};

		// remove all html event objects currently on the calendar
		var a = document.getElementsByClassName('event'),
			b = document.getElementsByClassName('noncreator-event'),
			c = document.getElementsByClassName('google-event');
		while(a[0]) {
			a[0].parentNode.removeChild(a[0]);
		}
		while(b[0]) {
			b[0].parentNode.removeChild(b[0]);
		}
		while(c[0]) {
			c[0].parentNode.removeChild(c[0]);
		}

		// add all new event objects
		for (var i = 0; i < list.length; i++) {
			var obj = JSON.parse(list[i]);
			eventMap[obj.id] = obj;
		}

		// display all events
		var key;
		for(key in eventMap) {
			value = eventMap[key];
			// create new event div
			var newElem = document.createElement("div");
			
			// based on id/creator status make it noncreator-event, google-event or regular event
			if (key < 0) {
				newElem.className = "google-event";
			} else if (value.creator != 1) {
				newElem.className = "noncreator-event";
			} else {
				newElem.className = "event";
			}
			
			/* we set event id, height */
			var time = value.date.split(" ")[3].split(":")[1];
			newElem.setAttribute("id", key);
			newElem.style.height = getEventHeight(value.duration) + "px";
			newElem.style.backgroundColor = getEventColor(value.duration);
			
			// place the events on the calendar
			placeEvents(newElem, value);
		}

		/* change the week title on the top of the calendar */
		weekTitle();
		/* sets z-indices of events so they overlay each other appropriately. */
		zindexByDuration();
		/* offset events based on their minute start time */
		timeOffsetMargin();
}

/* places event correctly on the page based on the main.ftl skeleton */
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
	placeEventDiv(slot_id, elem, event);
}

function placeEventDiv(id, elem, event) {
	var size = $("#" + id + " > div").size() + 1;

	if (size == 1) {
		var len = event.title.length;
		var hgt = elem.style.height;
		hgt = hgt.substring(0, hgt.length - 2);
		var lines = hgt/18.75;
		if ((lines * 15) > len) {
			elem.appendChild(document.createTextNode(event.title));
		} else {
			elem.appendChild(document.createTextNode('...'));
		}
		document.getElementById(id).appendChild(elem);
	} else {
		elem.appendChild(document.createTextNode('...'));
		document.getElementById(id).appendChild(elem);
		$(".eventSlot#" + id).children().each(function () {
		    this.innerHTML = '...';
		    this.style.width = (1/size * 100) + "%";
		});
	}
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

function getDBTime(date, time) {
	date = date.replace(",", "");
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
	return arr[1] + "-" + arr[0] + "-" +
		arr[2] + " " + sHours + ":" + sMinutes;
}

function zindexByDuration() {
	//cycle through all events chronologically...
	for (var i = 100; i < 725; i++) {
		if ((i % 100) > 12) {
			i += 86;
		} else {
			/* add appropriate z index to every child */
			$('.eventSlot#' + i).children().each(function () {
				this.style.zIndex = i;
			});

			/* sort events by height, so tallest event is last */
			sortEventsByHeight('.eventSlot#' + i);
			
			/* go thru all the children of an event slot and find the tallest one. */
			var last = $('.eventSlot#' + i).children().last().attr('id');
			
			/* if last exists then we should see if we can add negative margins */
			if (last !== undefined) {
				addNegativeMargins(last, i);
			}
		}
	}
}

function addNegativeMargins(id, slot) {
	// we get the margin and height of the event
	boxHeight = $('.event#' + id).height();
	//subtract expected length and add any margin that might exist on top
	var evTime = eventMap[id].date.split(" ")[3].split(":")[1] * 1;
	var tOffset = getTimeOffsetMargin(evTime);
	var adjustedHeight = boxHeight - 37.5 + tOffset;
	if (adjustedHeight > 0) {
		adjustMargin(adjustedHeight, slot);
	}
}

function adjustMargin(ah, slot) {
	rows = Math.ceil(ah / 37.5);
	while (rows > 0) {
		slot += 1;
		//if condition for events that span more than one day 
		var len = $('.eventSlot#' + slot).children().length;
		var index = len - 1;
		$('.eventSlot#' + slot).children().each(function () {
			var currMarg = this.style.marginTop;
			var currMarg = (currMarg.substring(0, currMarg.length) * 1);
			var newMarg = (currMarg * 1) - ah;
			this.style.marginTop = newMarg + "px";
			this.style.marginRight = ((index/len)*133.984) + "px";
			index--;
		});
		rows--;
		ah -= 37.5;
	}
}

/* sorts events by their height, so longer events are at the end. */
function sortEventsByHeight(parent) {
	$(parent + ' > div').sort(function (a, b) {
	    return $(a).height() > $(b).height() ? 1 : -1;  
	}).appendTo($(parent));
}

/* assigns events color based on their duration. */
function getEventColor(duration) {
	if (duration <= 30) {
		return eventColors[0];
	} else if (duration <= 60) {
		return eventColors[1];
	} else if (duration <= 90) {
		return eventColors[2];
	} else if (duration <= 120) {
		return eventColors[3];
	} else if (duration <= 180) {
		return eventColors[4];
	} else if (duration <= 240) {
		return eventColors[5];
	} else {
		return eventColors[6];
	}
}

function timeOffsetMargin() {
	var key;
	for(key in eventMap) {
		value = eventMap[key];
		var evTime = eventMap[key].date.split(" ")[3].split(":")[1] * 1;
		var currMarg = $('.event#' + key).css('margin-top');
		if (currMarg == undefined) {
			currMarg = 0;
		} else {
			currMarg = currMarg.substring(0, currMarg.length - 2);
		}
		var newMarg = (currMarg * 1) + getTimeOffsetMargin(evTime);
		$('.event#' + key).css('margin-top', newMarg+'px');
	}
}

function getTimeOffsetMargin(t) {
	var margin = (t/60) * 37.5;
	return margin;
}

/* basic "regex" to check if date is valid */
function dateRegex(date) {
	var comma = false;
	if (date.indexOf(",") > -1) {
		comma = true;
	}
	date = date.replace(",", "");
	arr = date.split(' ');
	console.log(arr[0] + " " + arr[1] + " " + arr[2]);
	if (arr.length != 3) {
		comma = false;
	} else {
		var month = false;
		var day = false;
		var mon = arr[0];
		var d = parseInt(arr[1]);
		if( mon === "Jan" || mon === "Mar" ||
			mon === "May" || mon === "Jul" || mon === "Aug" ||
			mon === "Oct" || mon === "Dec") {
			month = true;
			if (d >= 1 && d <= 31) {
				day = true;
			}
		} else if ( mon === "Apr" || mon === "Jun" ||
					mon === "Sep" || mon === "Nov") {
			month = true;
			if (d >= 1 && d <= 30) {
				day = true;
			}
		} else if (mon === "Feb") {
			month = true;
			if (d >= 1 && d <= 29) {
				day = true;
			}
		}
		var year = false;
		var y = parseInt(arr[2]);
		console.log(y);
		if (y >= 2015) {
			year = true;
		}
	console.log(month + " " + day + " " + year);
	return comma && month && day && year;
	}
}

$(document).ready(function(e) {
	$(".ui-state-default").hide()  
	/* update the displayed events to get events on page */
	updateDisplayedEvents();
	
	/* update calendar every 5 seconds */
	window.setInterval(function() { 
		updateDisplayedEvents();
	}, 5000);

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
		e.stopPropagation();
	    openDialog(e.target, 1);
	});
	
	$(document).on('click','.noncreator-event', function(e) {
		e.stopPropagation();
	    openDialog(e.target, 2);
	});

	$(document).on('click','#x-button', function(e) {
	    var $dialog = $(this).parents('.ui-dialog-content');
	    $dialog.dialog('destroy');
	});
	
	$(document).on('click','#datepicker', function(e) {
	    datePicker();
	});
	
	$(document).on('keyup','#datepicker', function(e) {
		date = $('#datepicker').val();
		var res = dateRegex(date);
		if (res) {
			$('#datepicker').removeClass('has-error');
			if ($('#new-event-button').length) {
				document.getElementById("new-event-button").style.visibility = "visible";
			} else {
				document.getElementById("check-button").style.visibility = "visible";
			}
		} else {
			$('#datepicker').addClass('has-error');
			console.log($('#new-event-button').length);
			if ($('#new-event-button').length) {
				document.getElementById("new-event-button").style.visibility = "hidden";
			} else {
				document.getElementById("check-button").style.visibility = "hidden";
			}
		}
	});

	$(document).on('click','#dialog-time', function(e) {		    
		timePicker();
	});
	
	$(document).on('keyup','#dialog-time', function(e) {
		var time = $('#dialog-time').val().match(/^(0?[1-9]|1[012])(:[0-5]\d) [APap][mM]$/i);
		if (time) {
			$('#dialog-time').removeClass('has-error');
			if ($('#new-event-button').length) {
				document.getElementById("new-event-button").style.visibility = "visible";
			} else {
				document.getElementById("check-button").style.visibility = "visible";
			}
		} else {
			$('#dialog-time').addClass('has-error');
			if ($('#new-event-button').length) {
				document.getElementById("new-event-button").style.visibility = "hidden";
			} else {
				document.getElementById("check-button").style.visibility = "hidden";
			}
		}
	});
	
	$(document).on('keyup','#duration', function(e) {
		var dur = $('#duration').val();
		if (!isNaN(dur) && dur > 0 && dur < 1440) {
			$('#duration').removeClass('has-error');
			document.getElementById("check-button").style.visibility = "visible";
		} else {
			$('#duration').addClass('has-error');
			document.getElementById("check-button").style.visibility = "hidden";
		}
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
	
	/* owner wants to delete the entire event */
	$(document).on('click','#delete-button', function(e) {
		var id = document.getElementById("dialog-event-id").innerHTML;
		deleteEvent(id);
	    var $dialog = $(this).parents('.ui-dialog-content');
	    $dialog.dialog('destroy');
		updateDisplayedEvents();
	});
	
	/* participant doesn't want to attend event */
	$(document).on('click','#remove-button', function(e) {
		var id = document.getElementById("dialog-event-id").innerHTML;
		removeUserEvent(id);
	    var $dialog = $(this).parents('.ui-dialog-content');
	    $dialog.dialog('destroy');
		updateDisplayedEvents();
	});
	
	/* owner wants to edit event */
	$(document).on('click','#edit-button', function(e) {
		var id = document.getElementById("dialog-event-id").innerHTML;
		console.log(id);
		//removeUserEvent(id);
		updateDisplayedEvents();
	});
});
