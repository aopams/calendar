var eventMap = {};
var weekInfo = [];

/* opens dialog window for events that creators have control over editing */
function openDialog(key, google) {
	value = eventMap[key.id];
	var date = value.date.split(" ");
	var day = date[0] + " " + date[1] + " " + date[2];
	var T = date[3];
	var am = date[4];
	var time = date[3].substring(0, T.length - 3) + " " + am;
	var dur = value.duration;
	if (google == 1) {
		var ds = 'disabled';
	} else {
		var ds = '';
	}
	form =
	'<form class="form-inline" id ="newEventForm">' +
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
		    '<input type="text" class="form-control" id="group" placeholder="Groups" value="'+ value.group +'"'+ ds +'/>' +
		'</div>' +
		'<div class="margin-group-xl">';
		
	if (google == 1) {
		form = form + '<img id="google-button" src="\\img/google.png"/>';
	} else {
		form = form + '<img id="delete-button" src="/img/minus.png"/><img id="check-button" src="\\img/check.png"/>';
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
	    		'<input id="dialog-time" type="text" class="form-control" onclick="timePicker()" value="' + time + '">' +
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
		    '<input type="text" class="form-control" id="group" placeholder="Groups"/>' +
		'</div>' +
		'<div class="margin-group-xl">'+ 
		'<img id="new-event-button" src="\\img/check.png"/>' +
		'</div> </div> </form>';
	$(form).dialog({ modal: true, resizable: false});
}

function datePicker() {
	$( "#datepicker" ).datepicker({
	    'format': 'M d, yyyy',
	    'autoclose': true
	});
}

function timePicker() {
	console.log('timepicker');
	$('#dialog-time').timepicker();
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

/* updates all the events on the caledar at the given moment. */
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
			placeEvents(newElem, value);
		}
		/* change the date title on the top of the calendar */
		dateTitle();
		/* sets z-indices of events so they overlay each other appropriately. */
		zindexByDuration();
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

function dateRegex(date) {
	date = date.replace(",", "");
	console.log(date);
	arr = date.split(' ');
	if (arr.length != 3) {
		return false;
	} else {
		var month = false;
		var mon = arr[0];
		if( mon === "Jan" | mon == "Feb" | mon === "Mar" | mon == "Apr" |
			mon === "May" | mon == "Jun" | mon === "Jul" | mon == "Aug" |
			mon === "Sep" | mon == "Oct" | mon === "Nov" | mon == "Dec") {
				month = true;
		}
		var day = false;
		var d = arr[1];
		if (d >= 1 && d <= 31) {
			day = true;
		}
		var year = false;
		var y = arr[2];
		if (y >= 2015) {
			y = true;
		}
	return month && day && year;
	}
}

function zindexByDuration() {
	//cycle through all events...
	for (var i = 100; i < 725; i++) {
		if ((i % 100) > 12) {
			i += 86;
		} else {
			var height = 0;
			var id = -1;
			/* go thru all the children of an event slot and find the tallest one. */
			$('.eventSlot#' + i).children().each(function () {
				/* set z index by date and time */
				this.style.zIndex = i;
				currHeight = this.style.height;
				currHeight = currHeight.substring(0, currHeight.length - 2);
				if (currHeight > height) {
		    		height = currHeight;
		    		console.log(height);
		    		id = this.id;
				}
			});
			if (id != -1) {
				addNegativeMargins(id, i);
			}
		}
	}
}

function addNegativeMargins(id, slot) {
	console.log('.event#' + id);
	// we get the margin and height of the event
	//get margin of event and add it to adjustedHeight
	boxHeight = $('.event#' + id).height();
	var adjustedHeight = boxHeight - 37.5;
	if (adjustedHeight > 0) {
		adjustMargin(adjustedHeight, slot);
	}
}

function adjustMargin(ah, slot) {
	rows = Math.ceil(ah/37.5);
	console.log("slot- " + slot + " // rows- " + rows + "// ah - " + ah);
	while (rows > 0) {
		slot += 1;
		//if condition for events that span more than one day 
		$('.eventSlot#' + slot).children().each(function () {
			currMarg = this.style.marginTop;
			currMarg = (currMarg.substring(0, currMarg.length) * 1);
			newMarg= currMarg - ah;
			this.style.marginTop = newMarg + "px";
		});
		rows--;
		ah -= 37.5;
	}
}

$(document).ready(function(e) {
	$(".ui-dialog-titlebar").hide()  
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
	    openDialog(e.target, 1);
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
		console.log(date);
		var res = dateRegex();
		if (res) {
			$('#datepicker').removeClass('has-error');
		} else {
			$('#datepicker').addClass('has-error');
		}
	});

	$(document).on('click','#dialog-time', function(e) {		    
		timePicker();
	});
	
	$(document).on('keyup','#dialog-time', function(e) {
		var time = $('#dialog-time').val().match(/^(0?[1-9]|1[012])(:[0-5]\d) [APap][mM]$/i);
		if (time) {
			$('#dialog-time').removeClass('has-error');
			document.getElementById("new-event-button").style.visibility = "visible";
		} else {
			$('#dialog-time').addClass('has-error');
			document.getElementById("new-event-button").style.visibility = "hidden";
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
});
