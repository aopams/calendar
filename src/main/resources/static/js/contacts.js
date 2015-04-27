friends = [];
pendingFriends = [];
groups = [];
username = "";
url = "";

/** for groups: functionality is to be able to create groups where creation involves specifying users to add
				and also be able to remove yourself from the group **/

$(document).ready(function(e) {
	$('#calWrap').show(0);
	$('#contacts').hide(0);
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
	
	//grabs groups from database and populates
	var postParameters = {url: url};
	$.post('/getgroups', postParameters, function(responseJSON) {
		responseObject = JSON.parse(responseJSON);
		temp = responseObject.groups;
		
		for (i = 0; i < temp.length; i++) {
			
		}
		
	})
	
	//button to add friend at top of contacts page
	$("#sendInvite").bind('click', function(e) {
		var user = $("#addFriend").val();
		var command = "add";
		user = JSON.stringify(user);
		command = JSON.stringify(command);
		var postParameters = {url : url, user : user, command : command};
		$.post('/editfriends', postParameters, function(responseJSON) {
			console.log("response received");
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
		var groupName = $("#groupName").val();
		
		console.log(groupName);
	});
	
	var postParameters = {url : url};
	$.post('/getusername', postParameters, function(responseJSON) {
		responseObject = JSON.parse(responseJSON);
		username = responseObject.name;
		console.log("here");
		console.log(username);
		document.getElementById('#cal-owner').innerHTML = username;
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
	
	console.log("pending friends");
	for (i = 0; i < pendingFriends.length; i++) {
		console.log(pendingFriends[i]);
	}
	console.log("friends");
	for (i = 0; i < friends.length; i++) {
		console.log(friends[i]);
	}
		
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
				img.src = getProfPic(name);
				refuse.src = '/img/x.png';
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
				rem.src = '/img/x.png';
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

function getProfPic(name) {
	name = name.toString();
	console.log(name);
	var let = name.substring(0, 1).toLowerCase();
	var toReturn = "/img/placeholder.jpg";
	console.log('let is ' + let);
	if (/[a-z]/i.test(let)) {
		console.log('is string');
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