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
				<div id="100" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"></div>
				<div id="101" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="102" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="103" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="104" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="105" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="106" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="107" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="108" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="119" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="110" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="111" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="112" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="113" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="114" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="115" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="116" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="117" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="118" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="119" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="120" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="121" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="122" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="123" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
			</div>

			<div class="cal-col"><img class="numeric-date"  src="img/two.png">
				<div id="200" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"></div>
				<div id="201" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="202" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="203" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="204" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="205" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="206" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)">
					<div class="event" draggable="true" ondragstart="drag(event)" id="drag1"> 
						<p class="description">Soccer Game w/ Botswana</p>
					</div>
				</div>
				<div id="207" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="208" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="209" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="210" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="211" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="212" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="213" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="214" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="215" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="216" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="217" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="218" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="219" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="220" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="221" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="222" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="223" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
			</div>
			<div class="cal-col"><img class="numeric-date"  src="img/three.png">
				<div id="300" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"></div>
				<div id="301" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="302" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="303" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="304" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="305" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="306" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="307" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="308" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="309" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="310" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="311" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="312" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="313" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="314" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="315" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="316" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="317" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="318" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="319" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="320" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="321" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="322" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="323" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
			</div>
			<div class="cal-col"><img class="numeric-date"  src="img/four.png">
				<div id="400" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"></div>
				<div id="401" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="402" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="403" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="404" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="405" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="406" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="407" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="408" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="409" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="410" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="411" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="412" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="413" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="414" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="415" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="416" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="417" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="418" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="419" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="420" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="421" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="422" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="423" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
			</div>
	    	<div class="cal-col"><img class="numeric-date"  src="img/five.png">
	    		<div class="eventSlot" ondrop="" ondragover="allowDrop(event)"> </div>
				<div id="500" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"></div>
				<div id="501" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="502" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="503" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="504" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="505" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="506" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="507" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="508" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="509" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="510" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="511" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="512" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="513" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="514" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="515" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="516" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="517" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="518" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="519" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="520" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="521" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="522" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="523" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
	    	</div>
	    	<div class="cal-col"><img class="numeric-date"  src="img/six.png">
	    		<div id="600" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"></div>
				<div id="601" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="602" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="603" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="604" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="605" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="606" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="607" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="608" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="609" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="610" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="611" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="612" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="613" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="614" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="615" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="616" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="617" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="618" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="619" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="620" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="621" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="622" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="623" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
	    	</div>
	    	<div class="cal-col border-none"><img  class="numeric-date"  src="img/seven.png">
	    		<div id="700" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"></div>
				<div id="701" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="702" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="703" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="704" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="705" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="706" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="707" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="708" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="709" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="710" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="711" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="712" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="713" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="714" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="715" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="716" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="717" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="718" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="719" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="720" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="721" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="722" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
				<div id="723" class="eventSlot" ondrop="drop(event)" ondragover="allowDrop(event)"> </div>
	    	</div>
	    </div>
	</div>
	<div class="footer">
</body>

</html>
