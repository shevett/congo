/*	
	Super Simple Admin Theme
	Jeff Adams	
*/

$(document).ready(function(){
	/* Zebra Striping for Tables */					   
	$("tr:nth-child(even)").addClass("even");
	
	/* Load Facebox */	
	$('a[rel*=facebox]').facebox();
	
	
	$("#myTable").tablesorter(); 

	/* Closing Divs */
	$(".canhide").append("<div class='close-notification'></div>").css("position", "relative");
	$(".close-notification").click(function() {
		$(this).hide();
		$(this).parent().fadeOut(600);
	});
	
	/* Tabs Animation */
	$('#tabs div').hide();
	$('#tabs div:first').show();
	$('#tabs ul li:first').addClass('active');
	$('#tabs ul li a').click(function(){ 
	$('#tabs ul li').removeClass('active');
	$(this).parent().addClass('active'); 
	var currentTab = $(this).attr('href'); 
	$('#tabs div').hide();
	$(currentTab).show();
	return false;
	});

});