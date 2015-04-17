var friends = [];
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
		console.log("in response");
		responseObject = JSON.parse(responseJSON);
		friends = responseObject.friends;
		createFriends();
	});
	
	/*
friends = ["William Truong", "Andrew Osgood", "Rohan Chandra", "Dylan Gattey", "Patrick Zhang", "Felege Gebru"];
	
*/
	createFriends();
	
});

function createFriends() {
	/* populate each with an image and the name from friends list */
	var count = 0;
	var len = friends.length;
	console.log(len);
	var rows = Math.floor(len/5) + 1;
	console.log(rows);
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
				var name = document.createTextNode(friends[count][0]);
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