var eventMap = {};

/* opens dialog window for events that creators have control over editing */
function openDialog(key) {
	value = eventMap[key.id];
	console.log(value);
	var date = value.date.split(" ");
	var day = date[0] + " " + date[1] + " " + date[2];
	var T = date[3];
	var am = date[4];
	var time = date[3].substring(0, T.length - 3) + " " + am;
	var dur = value.duration;
	console.log(time);
	form = '<form class="form-inline">' +
	  '<img id="x-button" src="img/x.png"/> ' +
	  '<div class="form-group">' +
	    '<label class="sr-only" for="exampleInputAmount">Amount (in dollars)</label>' +
	    '<div class="input-group">' +
		    '<div class="input-group-addon">T</div>' +
		    '<input type="text" class="form-control" id="title" value="' + value.title + '">' +
	    '</div>' +
	    '<div class="input-group">' +
		    '<div class="input-group-addon"><span class="glyphicon glyphicon-calendar" aria-hidden="true"></span></div>' +
		    '<input type="text" id="datepicker" onclick="datePicker()" value="' + day + '"/>' +
	    '</div>' +
	    '<div class="input-group">' +
	    '<input id="basicExample" type="text" class="time" data-scroll-default="6:00am" onclick="timePicker()" value="' + 
	    time + '" />' +
	    'Duration <input type="text" value="' + dur + '"/>' +
	    '</div>' +
	    '<div class="input-group">' +
	      '<textarea type="text" class="form-control" id="descrip">'+ value.description + '</textarea>' +
	    '</div>' +
	  '</div>' +
	 '<img id="check-button" src="img/check.png"/>' +
	'</form>';
	//$('<div>' + value.title + '<p>'+ value.description + '</p>' + '</div>').dialog({modal: true});
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

function updateDisplayedEvents() {
	var postParameters = {string: "test"};
	$.post("/getevents", postParameters, function(responseJSON){

		var responseObject = JSON.parse(responseJSON);
		var list = responseObject.events;

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
			//newElem.setAttribute("onclick", "openDialog(this)");
			newElem.setAttribute("style", "height:"+getEventHeight(value.duration)+"px");
			var p = document.createTextNode(value.title);
			console.log(value.title);
			p.id = "description";
			newElem.appendChild(p);
			placeEvents(newElem, value);
		}
	})
}

function placeEvents(elem, event) {
	var day = event.dayOfWeek;
	var date = event.date.split(" ");
	var time = date[3].split(":")[0];
	var am = date[4];
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
	}

	//set time
	var givenTime;
	switch(am) {
		case "AM":
			time = time%12;
			break;
		case "PM":
			time = (time%12) + 12;
			break;
	}

	//adjust time by AM/PM
	if(time < 10) {
		time = "0" + time;
	}

	document.getElementById(dayInt + "" + time).appendChild(elem);
}

function getEventHeight(dur) {
	return (dur/60) * 37.5;
}

$(document).ready(function(e) {
	updateDisplayedEvents();
	
	$("#x-button").click(function(e) {
		console.log("here");
		dialog.close();
		$('#terms').dialog('close');
	});

	$(document).on('click','.event', function(e) {
		console.log();
	   openDialog(e.target);
	});
	
	$("#check-button").bind("click", function() {
		console.log("here");
		dialog.close();
		$('#terms').dialog('close');
	});
	
	$(document).on('click','#x-button', function(e) {
	    var $dialog = $(this).parents('.ui-dialog-content');
	    $dialog.dialog('close');
	});
	
});