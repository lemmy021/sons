$(function () {
	$('#client_type').change(function(){
		if($('#client_type').val()==0){
			//fizicko
			$("#client_company").val("n/a").attr('readonly', true).closest('.form-line').addClass('focused');
			$("#client_pib").val("0").attr('readonly', true).closest('.form-line').addClass('focused');
			$("#client_identification_number").val("0").attr('readonly', true).closest('.form-line').addClass('focused');
		} else {
			$("#client_company").val("").removeAttr('readonly').closest('.form-line').removeClass('focused');
			$("#client_pib").val("").removeAttr('readonly').closest('.form-line').removeClass('focused');
			$("#client_identification_number").val("").removeAttr('readonly').closest('.form-line').removeClass('focused');
		}
	});
	


	$("#client_reset_button").click(function() {
	//$("#client_reset_button").on("click", function () {
	    $(this).closest('form').find("input[type=text], textarea").val("").removeAttr('readonly').closest('.form-line').removeClass('focused');
	    
	    $("#client_type").val("0").parent("div").find("button").find('span').first().text("No");//set value and text
	    var xx= $("#client_type").parent("div").find("div").find('ul').find('li').removeClass('selected').first().addClass('selected'); //set shadow
	    
	    $("#country\\.country_id").val("189").parent("div").find("button").find('span').first().text("Srbija");//set value and text
	    $("#country\\.country_id").parent("div").find("div").find('ul').find('li').removeClass('selected').parent("ul").find("li:eq(205)").addClass('selected'); //set shadow
	    
	    $("#client_company").val("n/a").attr('readonly', true).closest('.form-line').addClass('focused');
		$("#client_pib").val("0").attr('readonly', true).closest('.form-line').addClass('focused');
		$("#client_identification_number").val("0").attr('readonly', true).closest('.form-line').addClass('focused');
	    
		/*$('form input, form select').each(
    	    function(index){  
    	        var input = $(this);
    	        alert('Type: ' + input.attr('type') + 'Name: ' + input.attr('name') + 'Value: ' + input.val());
    	    }
    	);*/
	});

})