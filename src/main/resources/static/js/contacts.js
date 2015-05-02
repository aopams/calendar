friends = [];
pendingFriends = [];
username = "";
url = "";

/** for groups: functionality is to be able to create groups where creation involves specifying users to add
				and also be able to remove yourself from the group **/
				
/** CHECK FOR VALID GROUP NAME!!!! **/
/** CHECK FOR VALID GROUP NAME!!!! **/
/** CHECK FOR VALID GROUP NAME!!!! **/
/** CHECK FOR VALID GROUP NAME!!!! **/
/** CHECK FOR VALID GROUP NAME!!!! **/
/** ADD CLICK GROUP FUNCTIONALITY TO DISPLAY MEMBERS OF GROUP!!!! **/

/** MODIFY FRIENDS STUFF TO NOT DISPLAY ALERTS!!! **/

$(document).ready(function(e) {
	$('#calWrap').show(0);
	$('#contacts').hide(0);
	$('#contactsWindow').show(0);
	$('#groupsWindow').hide(0);
	$("#contactsbutton").bind('click', function(e) {
		$('#calWrap').hide(0);
		$('#contacts').show(0);
		$('#calendarbutton').removeClass("btn btn-default btn-primary").addClass("btn btn-default");
		$('#contactsbutton').removeClass("btn btn-default").addClass("btn btn-default btn-primary");
	});
	
	$("#calendarbutton").bind('click', function(e) {
		$('#calWrap').show(0);
		$('#contacts').hide(0);
		$('#contactsbutton').removeClass("btn btn-default btn-primary").addClass("btn btn-default");
		$('#calendarbutton').removeClass("btn btn-default").addClass("btn btn-default btn-primary");
	});
	
	$('#friendsTab').bind('click', function(e) {
		$('#friendsTab').addClass("active");		
		$('#groupsTab').removeClass("active");
		$('#contactsWindow').show(0);
		$('#groupsWindow').hide(0);
	});
	$('#groupsTab').bind('click', function(e) {
		$('#groupsTab').addClass("active");		
		$('#friendsTab').removeClass("active");
		$('#contactsWindow').hide(0);
		$('#groupsWindow').show(0);
	});	
	/* grabbing id for client */
	
	url = window.location.href;
	url = url.substr(url.lastIndexOf('/') + 1);
	var postParameters = {url : url};
	
	//grabs friends from database and populates the appropriate arrays for friends
	//to be created and displayed
	$.post('/getfriends', postParameters, function(responseJSON) {
		responseObject = JSON.parse(responseJSON);
		temp = responseObject.friends;
		for (i = 0; i < temp.length; i++) {
			if (temp[i][1] == "pending") {
				pendingFriends.push(temp[i][0]);
			} else if (temp[i][1] == "accepted") {
				friends.push(temp[i][0]);
			}
		};
		createFriends();
	});
	
	//create groups
	createGroups();
	
	//button to add friend at top of contacts page
	$("#sendInvite").bind('click', function(e) {
		var user = $("#addFriend").val();
		var command = "add";
		user = JSON.stringify(user);
		command = JSON.stringify(command);
		var postParameters = {url : url, user : user, command : command};
		$.post('/editfriends', postParameters, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			message = responseObject.message;
			alert(message);
			
			//send same post request as on document load to grab newly updated friend's list
			postParameters = {url : url};
			$.post('/getfriends', postParameters, function(responseJSON) {
				//empty arrays
				friends = [];
				pendingFriends = [];
				responseObject = JSON.parse(responseJSON);
				temp = responseObject.friends;
				for (i = 0; i < temp.length; i++) {
					if (temp[i][1] == "pending") {
						pendingFriends.push(temp[i][0]);
					} else if (temp[i][1] == "accepted") {
						friends.push(temp[i][0]);
					}
				};
				createFriends();
			});
		});
	});
	
	$("#makeGroup").bind('click', function(e) {
		var groupName = $("#groupName").val().trim();
		if (groupName && !/^[a-zA-Z0-9- ]*$/.test(groupName) == true) {
			document.getElementById('message').innerHTML = "";
			document.getElementById('message').style.textAlign = 'center';
			document.getElementById('message').style.color = 'red';
			document.getElementById('message').innerHTML = "Group name contains special characters, which is invalid. Please try another group name.";
		} else if (!groupName) {
			document.getElementById('message').innerHTML = "";
			document.getElementById('message').style.textAlign = 'center';
			document.getElementById('message').style.color = 'red';
			document.getElementById('message').innerHTML = "Group name is empty, please specify a group name.";
		} else {
			document.getElementById('message').innerHTML = "";
			form =
			'<form class="form-inline" id ="newEventForm">' +
			'<div class="form-group dialog-form">' +
				'<img id="x-button" src="/img/x.png"/>' +
			    '<div class="input-group">' +
			    	'<h3 id="groupname" align="center">' + groupName + '</h3>' +
				    '<p>Please separate users with commas.</p>' +
			    '</div>' +
			    '<div class="input-group margin-group">' +
			    	'<div class="input-group-addon">@</div><input type="text" class="form-control" id="attendees" placeholder="Users to add"/>'+
				'</div>' +
				'<div class="margin-group-xl">'+ 
				'<img id="new-group-button" src="\\img/check.png"/>' +
				'</div> </div> </form>';
			$(form).dialog({ modal: true, resizable: false});
		}
	});
	
	//new group button
	$(document).on('click','#new-group-button', function(e) {
	    newGroup();
	    var $dialog = $(this).parents('.ui-dialog-content');
	    $dialog.dialog('destroy');
	});
	
	//close dialog box on escape
	$(document).keyup(function(e) {
		if(e.keyCode === 27) {
			var $dialog = $('.ui-dialog-content');
			$dialog.dialog('destroy');
		}
	});
	
	var postParameters = {url : url};
	$.post('/getusername', postParameters, function(responseJSON) {
		responseObject = JSON.parse(responseJSON);
		username = responseObject.name;
		if (username != '') {
			$('#cal-owner').text(username + "'s Calendar");
		}
	});
	
});

//function that loops through two arrays (pendingFriends and
//friends) to populate proper divs and display them on the 
//contacts page
function createFriends() {
	
	//figure out how to remove everything from existing divs!!!!!
	//might have to modify id's and change themto classes
	$('.friend').remove();
	$('.pending').remove();
	$('.scrollRow').remove();
	$('.contacts-row').remove();
		
	//loop through pending friends list to show on contacts page
	if (pendingFriends.length > 0) {
		var pending = document.createElement('div');
		pending.className = 'pending';
		var text = document.createElement('h3');
		text.innerHTML = "Pending";
		pending.appendChild(text);
		var grid = document.getElementById('contactsGrid');
		grid.appendChild(pending);
		var scrollRow = document.createElement('div');
		scrollRow.className = 'scrollRow';
		grid.appendChild(scrollRow);
		for (i = 0; i < pendingFriends.length; i++) {
			var friend = document.createElement('div');
				friend.className = 'friend';
				friend.id = i;
				//set this attribute to be grabbed later for accepting/refusing friend request
				//**must know about users
				friend.setAttribute('username', pendingFriends[i]);
				var text = document.createElement('div');
				text.id = 'text';
				var name = document.createTextNode(pendingFriends[i]);
				text.appendChild(name);
				var img = document.createElement('img');
				var refuse = document.createElement('img');
				var accept = document.createElement('img');
				refuse.id = 'refuse';
				accept.id = 'accept';
				img.src = getProfPic(pendingFriends[i]);
				refuse.src = '/img/minus.png';
				refuse.onclick=function() {removeFriend(this);};
				accept.src = '/img/check.png';
				accept.onclick=function() {acceptFriend(this);};
				friend.appendChild(img);
				friend.appendChild(text);
				friend.appendChild(refuse);
				friend.appendChild(accept);
				scrollRow.appendChild(friend);
		}
	}
	//loops through accepted friends list to show on contacts page
	var count = 0;
	var len = friends.length;
	var rows = Math.ceil(len/5);
	var grid = document.getElementById('contactsGrid');
	if (len != 0 ) {
		for (i = 0; i < rows; i++) {
			var row = document.createElement('div');
			row.className = 'contacts-row';
			row.id = i;
			grid.appendChild(row);
			for (j = 0; j < 5; j++) {
				var friend = document.createElement('div');
				friend.className = 'friend';
				friend.id = count;
				friend.setAttribute('username', friends[count]);
				var name = document.createElement('div');
				name.id = 'name';
				name.style.marginLeft='auto';
				name.style.marginRight='auto';
				name.style.display='block';
				name.innerHTML = friends[count];
				var img = document.createElement('img');
				img.src = getProfPic(friends[count]);
				var rem = document.createElement('img');
				rem.id = 'rem';
				rem.src = '/img/minus.png';
				rem.onclick=function() {removeFriend(this);};
				rem.style.marginRight='175px';
				friend.appendChild(rem);
				friend.appendChild(img);
				friend.appendChild(name);
				row.appendChild(friend);
				count++;
				if (count == len) {
					break;
				}
			}
		}
	}
};

function createGroups() {
	//grabs groups from database and populates
	//empty groups list;
	
	$('.group').remove();
	$('.group-row').remove();
	var groups = [];
	var postParameters = {url: url};
	$.post('/getgroups', postParameters, function(responseJSON) {
		responseObject = JSON.parse(responseJSON);
		temp = responseObject.groups;
		for (i = 0; i < temp.length; i++) {
			//push into group the group ID and group name
			console.log("looping through groups");
			groups.push([temp[i][0],temp[i][1]]);
		}
			
		//loops through accepted friends list to show on contacts page
		var count = 0;
		var len = groups.length;
		var rows = Math.ceil(len/5);
		var grid = document.getElementById('groupsGrid');
		if (len != 0 ) {
			for (i = 0; i < rows; i++) {
				var row = document.createElement('div');
				row.className = 'group-row';
				row.id = i;
				grid.appendChild(row);
				for (j = 0; j < 5; j++) {
					console.log("id");
					console.log(groups[count][0]);
					console.log("name");
					console.log(groups[count][1]);
					var group = document.createElement('div');
					group.className = 'group';
					group.id = count;
					group.setAttribute('groupid', groups[count][0])
					group.setAttribute('groupname', groups[count][1]);
					var name = document.createElement('div');
					name.id = 'name';
					name.style.marginLeft='auto';
					name.style.marginRight='auto';
					name.style.display='block';
					name.innerHTML = groups[count][1];
					var img = document.createElement('img');
					img.src = getProfPic(groups[count][1]);
					img.onclick=function() {viewGroupMembers(this);};
					var rem = document.createElement('img');
					rem.id = 'rem';
					rem.src = '/img/minus.png';
					rem.onclick=function() {removeGroup(this);};
					rem.style.marginRight='175px';
					group.appendChild(rem);
					group.appendChild(img);
					group.appendChild(name);
					row.appendChild(group);
					count++;
					if (count == len) {
						break;
					}
				}
			}
		}
	});
}

function getProfPic(name) {
	name = name.toString();
/* 	console.log(name); */
	var let = name.substring(0, 1).toLowerCase();
	var toReturn = "/img/placeholder.jpg";
/* 	console.log('let is ' + let); */
	if (/[a-z]/i.test(let)) {
/* 		console.log('is string'); */
		toReturn = "\\img/let/" + let + ".png";
	}
	return toReturn;
}

function acceptFriend(elem) {
	var parent = elem.parentNode;
	var user = parent.getAttribute('username');
	var command = "accept";
	console.log(user);
	user = JSON.stringify(user);
	command = JSON.stringify(command);
	var postParameters = {url : url, user : user, command : command};
	
	$.post('/editfriends', postParameters, function(responseJSON) {
		responseObject = JSON.parse(responseJSON);
		message = responseObject.message;
		alert(message);
		//send same post request as on document load to grab newly updated friend's list
		postParameters = {url : url};
		$.post('/getfriends', postParameters, function(responseJSON) {
			//empty arrays
			friends = [];
			pendingFriends = [];
			responseObject = JSON.parse(responseJSON);
			temp = responseObject.friends;
			for (i = 0; i < temp.length; i++) {
				if (temp[i][1] == "pending") {
					pendingFriends.push(temp[i][0]);
				} else if (temp[i][1] == "accepted") {
					friends.push(temp[i][0]);
				}
			};
			createFriends();
		});
	});
};

function removeFriend(elem) {
	var parent = elem.parentNode;
	var user = parent.getAttribute('username');
	var command = "remove";
	console.log(user);
	console.log(command);
	user = JSON.stringify(user);
	command = JSON.stringify(command);
	var postParameters = {
		url : url,
		user : user,
		command : command
	};
	
	$.post('/editfriends', postParameters, function(responseJSON) {
		console.log("remove friends");
		responseObject = JSON.parse(responseJSON);
		message = responseObject.message;
		alert(message);
		//send same post request as on document load to grab newly updated friend's list
		postParameters = {url : url};
		$.post('/getfriends', postParameters, function(responseJSON) {
			console.log("should update removed friend");
			//empty arrays
			friends = [];
			pendingFriends = [];
			responseObject = JSON.parse(responseJSON);
			temp = responseObject.friends;
			for (i = 0; i < temp.length; i++) {
				if (temp[i][1] == "pending") {
					pendingFriends.push(temp[i][0]);
				} else if (temp[i][1] == "accepted") {
					friends.push(temp[i][0]);
				}
			};
			createFriends();
		});
	});
};

//FINISH REMOVING GROUPS, grab ID from front end;
function removeGroup(elem) {
	var parent = elem.parentNode;
	var groupid = parent.getAttribute('groupid');
	var groupname = parent.getAttribute('groupname');
	var command = "remove";
	console.log(groupid);
	console.log(groupname);
	var postParameters = {url : url, groupid : groupid, groupname : groupname, command :command}
	$.post('/editgroups', postParameters, function(responseJSON) {
		console.log("removed from group");
		createGroups();
	});
}

function newGroup() {
	var groupname = document.getElementById('groupname').innerText;
	var users = document.getElementById('attendees').value;
	var command = "add";
	var postParameters = {url : url, users : users, groupname : groupname, command : command};
	$.post('/editgroups', postParameters, function(responseJSON) {
		responseObject = JSON.parse(responseJSON);
		var message = responseObject.message;
		console.log("editing groups");
		console.log(message);
		//case for when groupname already exists
		if (message == "failure") {
			console.log("group name already exists");
			document.getElementById('message').style.textAlign = "center";
			document.getElementById('message').style.color = "red";
			document.getElementById('message').innerHTML = "Group name already exists, please try another."
		//SQL error occured, report to user;
		} else if (message == "error") {
			document.getElementById('message').style.textAlign = "center";
			document.getElementById('message').style.color = "red";
			document.getElementById('message').innerHTML = "An error has occured with our database, please try again."
		//check for invalid usernames to post back to front end.
		} else {
			console.log("should print back invalid users");
			var invalidUsers = message.split(",");
			var string = "The following usernames were invalid: ";
			for (i = 0; i < invalidUsers.length; i++) {
				console.log("in invalid users loop");
				console.log(invalidUsers[i]);
				string += invalidUsers[i];
				string += " ";
			}
			console.log(string);
			document.getElementById('message').style.textAlign = "center";
			document.getElementById('message').style.color = "red";
			document.getElementById('message').innerHTML = string;
			createGroups();
		}
	});
}

function viewGroupMembers(elem) {
	var parent = elem.parentNode;
	var groupid = parent.getAttribute('groupid');
	var groupname = parent.getAttribute('groupname');
	var command = "members";
	var members;
	console.log(groupid);
	var postParameters = {url : url, groupid : groupid, groupname : groupname, command :command}
	$.post('/editgroups', postParameters, function(responseJSON) {
		console.log("fetching members");
		responseObject = JSON.parse(responseJSON);
		members = responseObject.members;
		form =
			'<form class="form-inline" id="membersForm">' +
				'<div class="form-group dialog-form">' +
					'<img id="x-button" src="/img/x.png"/>' +
				'</div>' +
				'<div class="addMembersWrap">' +
					'<div class="input-group margin-group" id="membersToAdd">' +
				    	'<div class="input-group-addon">@</div><input type="text" class="form-control" id="attendees" placeholder="Users to add"/>'+
					'</div>' +
					'<img id="new-members-button" src="\\img/check.png"/>' +
				'</div>' +
			'</form>';
		$(form).dialog({ modal: true, resizable: false});
		
		//adding members to group
		$(document).on('click','#new-members-button', function(e) {
			var users = document.getElementById('attendees').value;
			if (users) {
				$(".members").remove();
				var command = "newmembers";
				var postParameters = {url : url, groupid : groupid, groupname : groupname, command : command, users : users};
				$.post('/editgroups', postParameters, function(responseJSON) {
					console.log("adding members");
					command = "members";
					var postParameters = {url : url, groupid : groupid, groupname : groupname, command : command, users : users};
					//view members again
					$.post('/editgroups', postParameters, function(responseJSON) {
						$(".members").remove();
						responseObject = JSON.parse(responseJSON);
						members = responseObject.members;
						for (i = 0; i < members.length; i++) {
							var mem = document.createElement('div');
							mem.className = 'members';
							var name = document.createElement('div');
							name.id = 'name';
							name.style.marginLeft='auto';
							name.style.marginRight='auto';
							name.style.display='block';
							name.innerHTML = members[i];
							var img = document.createElement('img');
							img.src = getProfPic(members[i]);
							mem.appendChild(img);
							mem.appendChild(name);
							$("#membersForm").append(mem);
						};
					});	
				});
			}
		});
		for (i = 0; i < members.length; i++) {
			var mem = document.createElement('div');
			mem.className = 'members';
			var name = document.createElement('div');
			name.id = 'name';
			name.style.marginLeft='auto';
			name.style.marginRight='auto';
			name.style.display='block';
			name.innerHTML = members[i];
			var img = document.createElement('img');
			img.src = getProfPic(members[i]);
			mem.appendChild(img);
			mem.appendChild(name);
			$("#membersForm").append(mem);
		};
	});
};