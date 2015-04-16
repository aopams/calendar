var friends = [];

$(document).ready(function(e) {
	console.log("here");
	
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
	
	friends = ["William Truong", "Andrew Osgood", "Rohan Chandra", "Dylan Gattey"];
	
	createFriends();
	
});

function createFriends() {
	/* populate each with an image and the name from friends list */
	var count = 0;
	for (i = 0; i < friends.length; i++) {
		var friend = document.createElement('div');
		friend.className = 'friend';
		friend.id = i;
		friend.style.backgroundColor="red";
		if (count == 5) {
			
		}
/* 		var col = document.getElementByClass('contacts-row').getElementById('1'); */
		col.appendChild(friend);
		count++;
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