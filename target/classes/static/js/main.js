$(document).ready(function(){
	$('#tbox').bind('keyup', function() {
		console.log($('#tbox').val());
		var x = $('#tbox').val();
		var postParameters = JSON.stringify(x);
		console.log(postParameters);
		$.post("/correction", postParameters)
	});
});
console.log($('#tbox').val());
