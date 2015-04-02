<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Calendar</title>
<script src="js/jquery-2.1.1.js"></script>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js"></script>
<script src="js/calendar.js"></script>
<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="css/calendar.css">
</head>

<body>
	<div class="row header">
		<img src="img/logo.png" style="width:140px;">
	</div>
	<div class="row choices">
		<a class="btn btn-default btn-primary" href="#" role="button">Calendar</a>
		<a class="btn btn-default" href="#" role="button">Contacts</a>
		<a class="btn btn-default" href="#" role="button" id="logout-btn">Log Out</a>
	</div>

	<div class="row cal-info">
		<div class="col-md-4">
			<!-- nothing here -->
		</div>
		<div class="col-md-4">
			<div class="date">
				January 1 - 7, 2015
			</div>
		</div>
		<div class="col-md-4">
			<img class="arrow" src="img/rightarrow.png">
			<img class="arrow" src="img/leftarrow.png">
		</div>
	</div>
	<div id="calendar" class="row calendar btn-outline">
		<div class="calendar-times">
			<div class="time">12 am</div>
			<div class="time">&nbsp;1 am</div>
			<div class="time"> &nbsp;2 am</div>
			<div class="time"> &nbsp;3 am</div>
			<div class="time"> &nbsp;4 am</div>
			<div class="time"> &nbsp;5 am</div>
			<div class="time"> &nbsp;6 am</div>
			<div class="time"> &nbsp;7 am</div>
			<div class="time"> &nbsp;8 am</div>
			<div class="time"> &nbsp;9 am</div>
			<div class="time">10 am</div>
			<div class="time">11 am</div>
			<div class="time">12 pm</div>
			<div class="time"> &nbsp;1 pm</div>
			<div class="time"> &nbsp;2 pm</div>
			<div class="time"> &nbsp;3 pm</div>
			<div class="time"> &nbsp;4 pm</div>
			<div class="time"> &nbsp;5 pm</div>
			<div class="time"> &nbsp;6 pm</div>
			<div class="time"> &nbsp;7 pm</div>
			<div class="time"> &nbsp;8 pm</div>
			<div class="time"> &nbsp;9 pm</div>
			<div class="time">10 pm</div>
			<div class="time">11 pm</div>
		</div>
		<div id="cal-days" class="calendar-days">
			<div class="cal-col">
				<img class="numeric-date" src="img/one.png">
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"></div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
			</div>

			<div class="cal-col"><img class="numeric-date"  src="img/two.png">
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)">
					<div class="event" draggable="true" ondragstart="drag(event)" id="drag1"> 
						<p class="description">Soccer Game w/ Botswana</p>
					</div>
				</div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
			</div>
			<div class="cal-col"><img class="numeric-date"  src="img/three.png">
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
			</div>
			<div class="cal-col"><img class="numeric-date"  src="img/four.png">
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
				<div class="eventSlot"> </div>
			</div>
	    	<div class="cal-col"><img class="numeric-date"  src="img/five.png">
	    		<div class="eventSlot" ondrop="" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover=""> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
	    	</div>
	    	<div class="cal-col"><img class="numeric-date"  src="img/six.png">
	    		<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
	    	</div>
	    	<div class="cal-col border-none"><img  class="numeric-date"  src="img/seven.png">
	    		<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
	    	</div>
	    </div>
	</div>
</body>

</html>
