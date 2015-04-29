<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Calendar</title>
<script src="\js/jquery-2.1.1.js"></script>
<script src="\js/calendar.js"></script>
<script src="\js/contacts.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<script type="text/javascript" src="\lib/jquery.timepicker.js"></script>
<script type="text/javascript" src="\lib/bootstrap-datepicker.js"></script>

<link rel="stylesheet" type="text/css" href="\css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="\css/bootstrap.css.map">
<link rel="stylesheet" href="\//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="\css/calendar.css">
<link rel="stylesheet" type="text/css" href="\lib/bootstrap-datepicker.css" />
<link rel="stylesheet" type="text/css" href="\lib/jquery.timepicker.css" />

</head>

<body>
	<div class="row header">
		<img src="\img/logo.png" style="width:140px;">
	</div>
	<div class="row choices">
		<div class="col-md-4 col-sm-4 col-xs-4">
			<a class="btn btn-default btn-primary" href="#" id="calendarbutton" role="button">Calendar</a>
			<a class="btn btn-default" id="contactsbutton" href="#" role="button">Contacts</a>
			<a class="btn btn-default" id="googlebutton" href="#" role="button">Google</a>
		</div>
		<div class="col-md-4 col-sm-4 col-xs-4" style="text-align: center">
			<p id="cal-owner"> </p>
		</div>
		<div class="col-md-4 col-sm-4 col-xs-4">
			<input type="button" class="btn btn-default" href="#" role="button" id="logout-btn" value="Log out" onclick="window.location='/login';">
		</div>
	</div>
	
	<div id="contacts">
			<ul class="nav nav-tabs" id="tabs">
			  <li role="presentation" class="active" id="friendsTab"><a href="#">Friends</a></li>
			  <li role="presentation" id="groupsTab"><a href="#">Groups</a></li>
			</ul>
		<div id="contactsWindow">
			<div id="addFriendBar">
				<div class="form-inline" id="send">
					<div class="form-group">
						<input type="text" class="form-control" id="addFriend" placeholder="@username">
					</div>
					<button type="submit" class="btn btn-default" id="sendInvite">Send Invitation</button>
				</div>
				<p id="friendMess"></p>
			</div>
			
			<!--populated through javascript-->
			<div id="contactsGrid">

			</div>
		</div>
		
		<div id="groupsWindow">
			<div id="makeGroupBar">
				<div class="form-inline" id="send">
					<div class="form-group">
						<input type="text" class="form-control" id="groupName" placeholder="@groupname">
					</div>
					<button type="submit" class="btn btn-default" id="makeGroup">Make Group</button>
				</div>
				<p id="message"></p>
			</div>
			<!--populated through javascript-->
			<div id="groupsGrid">

			</div>
		</div>
	</div>
	
	<div id="calWrap">
		<div class="row cal-info">
			<div class="col-md-4 col-sm-4 col-xs-4">
				<!-- nothing here -->
			</div>
			<div class="col-md-4 col-sm-4 col-xs-4">
				<div class="date" id = "date-title">
				</div>
			</div>
			<div class="col-md-4 col-sm-4 col-xs-4">
				<img class="arrow" src="\img/rightarrow.png" id="rightarrow">
				<img class="arrow" src="\img/leftarrow.png" id="leftarrow">
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
					<img class="numeric-date"id="day1"  src="">
					<div id="100" class="eventSlot"></div>
					<div id="101" class="eventSlot"> </div>
					<div id="102" class="eventSlot"> </div>
					<div id="103" class="eventSlot"> </div>
					<div id="104" class="eventSlot"> </div>
					<div id="105" class="eventSlot"> </div>
					<div id="106" class="eventSlot"> </div>
					<div id="107" class="eventSlot"> </div>
					<div id="108" class="eventSlot"> </div>
					<div id="119" class="eventSlot"> </div>
					<div id="110" class="eventSlot"> </div>
					<div id="111" class="eventSlot"> </div>
					<div id="112" class="eventSlot"> </div>
					<div id="113" class="eventSlot"> </div>
					<div id="114" class="eventSlot"> </div>
					<div id="115" class="eventSlot"> </div>
					<div id="116" class="eventSlot"> </div>
					<div id="117" class="eventSlot"> </div>
					<div id="118" class="eventSlot"> </div>
					<div id="119" class="eventSlot"> </div>
					<div id="120" class="eventSlot"> </div>
					<div id="121" class="eventSlot"> </div>
					<div id="122" class="eventSlot"> </div>
					<div id="123" class="eventSlot"> </div>
				</div>
				<div class="cal-col"><img class="numeric-date" id="day2"  src="">
					<div id="200" class="eventSlot"></div>
					<div id="201" class="eventSlot"> </div>
					<div id="202" class="eventSlot"> </div>
					<div id="203" class="eventSlot"> </div>
					<div id="204" class="eventSlot"> </div>
					<div id="205" class="eventSlot"> </div>
					<div id="206" class="eventSlot"> </div>
					<div id="207" class="eventSlot"> </div>
					<div id="208" class="eventSlot"> </div>
					<div id="209" class="eventSlot"> </div>
					<div id="210" class="eventSlot"> </div>
					<div id="211" class="eventSlot"> </div>
					<div id="212" class="eventSlot"> </div>
					<div id="213" class="eventSlot"> </div>
					<div id="214" class="eventSlot"> </div>
					<div id="215" class="eventSlot"> </div>
					<div id="216" class="eventSlot"> </div>
					<div id="217" class="eventSlot"> </div>
					<div id="218" class="eventSlot"> </div>
					<div id="219" class="eventSlot"> </div>
					<div id="220" class="eventSlot"> </div>
					<div id="221" class="eventSlot"> </div>
					<div id="222" class="eventSlot"> </div>
					<div id="223" class="eventSlot"> </div>
				</div>
				<div class="cal-col"><img class="numeric-date" id="day3"  src="">
					<div id="300" class="eventSlot"></div>
					<div id="301" class="eventSlot"> </div>
					<div id="302" class="eventSlot"> </div>
					<div id="303" class="eventSlot"> </div>
					<div id="304" class="eventSlot"> </div>
					<div id="305" class="eventSlot"> </div>
					<div id="306" class="eventSlot"> </div>
					<div id="307" class="eventSlot"> </div>
					<div id="308" class="eventSlot"> </div>
					<div id="309" class="eventSlot"> </div>
					<div id="310" class="eventSlot"> </div>
					<div id="311" class="eventSlot"> </div>
					<div id="312" class="eventSlot"> </div>
					<div id="313" class="eventSlot"> </div>
					<div id="314" class="eventSlot"> </div>
					<div id="315" class="eventSlot"> </div>
					<div id="316" class="eventSlot"> </div>
					<div id="317" class="eventSlot"> </div>
					<div id="318" class="eventSlot"> </div>
					<div id="319" class="eventSlot"> </div>
					<div id="320" class="eventSlot"> </div>
					<div id="321" class="eventSlot"> </div>
					<div id="322" class="eventSlot"> </div>
					<div id="323" class="eventSlot"> </div>
				</div>
				<div class="cal-col"><img id="day4" class="numeric-date"  src="">
					<div id="400" class="eventSlot"></div>
					<div id="401" class="eventSlot"> </div>
					<div id="402" class="eventSlot"> </div>
					<div id="403" class="eventSlot"> </div>
					<div id="404" class="eventSlot"> </div>
					<div id="405" class="eventSlot"> </div>
					<div id="406" class="eventSlot"> </div>
					<div id="407" class="eventSlot"> </div>
					<div id="408" class="eventSlot"> </div>
					<div id="409" class="eventSlot"> </div>
					<div id="410" class="eventSlot"> </div>
					<div id="411" class="eventSlot"> </div>
					<div id="412" class="eventSlot"> </div>
					<div id="413" class="eventSlot"> </div>
					<div id="414" class="eventSlot"> </div>
					<div id="415" class="eventSlot"> </div>
					<div id="416" class="eventSlot"> </div>
					<div id="417" class="eventSlot"> </div>
					<div id="418" class="eventSlot"> </div>
					<div id="419" class="eventSlot"> </div>
					<div id="420" class="eventSlot"> </div>
					<div id="421" class="eventSlot"> </div>
					<div id="422" class="eventSlot"> </div>
					<div id="423" class="eventSlot"> </div>
				</div>
		    	<div class="cal-col"><img class="numeric-date" id="day5"  src="">
					<div id="500" class="eventSlot"></div>
					<div id="501" class="eventSlot"> </div>
					<div id="502" class="eventSlot"> </div>
					<div id="503" class="eventSlot"> </div>
					<div id="504" class="eventSlot"> </div>
					<div id="505" class="eventSlot"> </div>
					<div id="506" class="eventSlot"> </div>
					<div id="507" class="eventSlot"> </div>
					<div id="508" class="eventSlot"> </div>
					<div id="509" class="eventSlot"> </div>
					<div id="510" class="eventSlot"> </div>
					<div id="511" class="eventSlot"> </div>
					<div id="512" class="eventSlot"> </div>
					<div id="513" class="eventSlot"> </div>
					<div id="514" class="eventSlot"> </div>
					<div id="515" class="eventSlot"> </div>
					<div id="516" class="eventSlot"> </div>
					<div id="517" class="eventSlot"> </div>
					<div id="518" class="eventSlot"> </div>
					<div id="519" class="eventSlot"> </div>
					<div id="520" class="eventSlot"> </div>
					<div id="521" class="eventSlot"> </div>
					<div id="522" class="eventSlot"> </div>
					<div id="523" class="eventSlot"> </div>
		    	</div>
		    	<div class="cal-col"><img class="numeric-date" id="day6"  src="">
		    		<div id="600" class="eventSlot"></div>
					<div id="601" class="eventSlot"> </div>
					<div id="602" class="eventSlot"> </div>
					<div id="603" class="eventSlot"> </div>
					<div id="604" class="eventSlot"> </div>
					<div id="605" class="eventSlot"> </div>
					<div id="606" class="eventSlot"> </div>
					<div id="607" class="eventSlot"> </div>
					<div id="608" class="eventSlot"> </div>
					<div id="609" class="eventSlot"> </div>
					<div id="610" class="eventSlot"> </div>
					<div id="611" class="eventSlot"> </div>
					<div id="612" class="eventSlot"> </div>
					<div id="613" class="eventSlot"> </div>
					<div id="614" class="eventSlot"> </div>
					<div id="615" class="eventSlot"> </div>
					<div id="616" class="eventSlot"> </div>
					<div id="617" class="eventSlot"> </div>
					<div id="618" class="eventSlot"> </div>
					<div id="619" class="eventSlot"> </div>
					<div id="620" class="eventSlot"> </div>
					<div id="621" class="eventSlot"> </div>
					<div id="622" class="eventSlot"> </div>
					<div id="623" class="eventSlot"> </div>
		    	</div>
		    	<div class="cal-col border-none"><img  class="numeric-date" id="day7"  src="">
		    		<div id="700" class="eventSlot"></div>
					<div id="701" class="eventSlot"> </div>
					<div id="702" class="eventSlot"> </div>
					<div id="703" class="eventSlot"> </div>
					<div id="704" class="eventSlot"> </div>
					<div id="705" class="eventSlot"> </div>
					<div id="706" class="eventSlot"> </div>
					<div id="707" class="eventSlot"> </div>
					<div id="708" class="eventSlot"> </div>
					<div id="709" class="eventSlot"> </div>
					<div id="710" class="eventSlot"> </div>
					<div id="711" class="eventSlot"> </div>
					<div id="712" class="eventSlot"> </div>
					<div id="713" class="eventSlot"> </div>
					<div id="714" class="eventSlot"> </div>
					<div id="715" class="eventSlot"> </div>
					<div id="716" class="eventSlot"> </div>
					<div id="717" class="eventSlot"> </div>
					<div id="718" class="eventSlot"> </div>
					<div id="719" class="eventSlot"> </div>
					<div id="720" class="eventSlot"> </div>
					<div id="721" class="eventSlot"> </div>
					<div id="722" class="eventSlot"> </div>
					<div id="723" class="eventSlot"> </div>
		    	</div>
		    </div>
		</div>
	</div>
	<div class="footer">
	</div>
</body>

</html>
