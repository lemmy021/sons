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
	
	$(".invoice_pay_yes").click(function() {
		var error = 0;
		
		$('.form-line input').each(
				function(index){
					var input = $(this);
					
					if(input.val() == "") {
						error = 1;
					}
				}
	    );
		
		var id = $(this).attr("id");
		var delivery_date = $("#delivery_date").val();
		var payment_date = $("#payment_date").val();
		
		if(error > 0){
			swal("Please, fill the date(s)", "", "error");
		} else if($(this).attr("name")) {
			/*var id = $(this).attr("id");
			var delivery_date = $("#delivery_date").val();
			var payment_date = $("#payment_date").val();*/
			
			createInvoice($(this).attr("id"), $("#delivery_date").val(), $("#payment_date").val(), "Create invoice", "Yes", "Invoice has been paid successfully", 3);
		} else {
			createInvoice($(this).attr("id"), $("#delivery_date").val(), $("#payment_date").val(), "Create invoice", "Yes", "Invoice has been created successfully", 1)
		}
	})
	
	$(".invoice_pay_no").click(function() {
		var id = $(this).attr("id");
		var delivery_date = $("#delivery_date").val();
		
		if($("#delivery_date").val() != "") {
			createInvoice($(this).attr("id"), $("#delivery_date").val(), "", "Create invoice", "Yes", "Invoice has been created successfully", 2);
		} else {
			swal("Delivery date cannot be empty", "", "error");
		}
	})

})

/*
 * @action 
 * 1 - create invoice with paying
 * 2 - create invoice without paying
 * 3 - invoice created but now is going to be paid
 */
function createInvoice(invoiceId, deliveryDate = null, paymentDate = null, textMsg, confirmBtnText, successText, action){
	swal({
        title: "Are you sure?",
        text: textMsg,
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: confirmBtnText,
        closeOnConfirm: false
    }, function () {
		$.post("/createinvoice", {jwt_invoice_id : invoiceId, payment_date : paymentDate, delivery_date : deliveryDate, action : action}, function(data){
			if(data == "1"){
				swal("Done!", successText, "success");
			}
		})
    });
}