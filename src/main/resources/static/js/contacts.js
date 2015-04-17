var friends = [];
var pendingFriends = [];
$(document).ready(function(e) {
	$('#calWrap').show(0);
	$('#contacts').hide(0);
	
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
	
	/* friends will be a post request to the back end */
	
	/* grabbing id for client */
	
	var url = window.location.href;
	url = url.substr(url.lastIndexOf('/') + 1);
	var postParameters = {url : url};
	
	$.post('/getfriends', postParameters, function(responseJSON) {
		responseObject = JSON.parse(responseJSON);
		temp = responseObject.friends;
		for (i = 0; i < temp.length; i++) {
			if (temp[i][1] == "pending") {
				pendingFriends.push(temp[i][0]);
			} else {
				friends.push(temp[i][0]);
			}
		};
		
		createFriends();
	});
});

function createFriends() {
	/* populate each with an image and the name from friends list */
	if (pendingFriends.length > 0) {
		var pending = document.createElement('div');
		pending.id = 'pending';
		pending.appendChild(document.createTextNode("Pending"));
		var grid = document.getElementById('contactsGrid');
		grid.appendChild(pending);
		var scrollRow = document.createElement('div');
		scrollRow.id = 'scrollRow';
		grid.appendChild(scrollRow);
		for (i = 0; i < pendingFriends.length; i++) {
			var friend = document.createElement('div');
				friend.className = 'friend';
				friend.id = i;
				var name = document.createTextNode(pendingFriends[i]);
				var img = document.createElement('img');
				img.src = '/img/placeholder.jpg';
				friend.appendChild(img);
				friend.appendChild(name);
				scrollRow.appendChild(friend);
		}
	}
	var count = 0;
	var len = friends.length;
	var rows = Math.floor(len/5) + 1;
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
				var name = document.createTextNode(friends[count]);
				var img = document.createElement('img');
				img.src = '/img/placeholder.jpg';
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
/*

var len = friends.length
var rows = len/5;
var con = 0;
for (var i = 0; i < rows; i++) {
	//create row div
	for (var j = 0; j < 5; j++) {
		append friend con to div;
		con++;
	}
}
*/