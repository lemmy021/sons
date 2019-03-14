//https://www.gyrocode.com/articles/jquery-datatables-why-click-event-handler-does-not-work/
$(function () {
    $('.js-basic-example').on('click', '.button-remove', function () {
        var type = $(this).data('type');
        var table = $('.js-basic-example').DataTable();
        var id = $(this).attr('id');
		var row = table.row( $(this).parents('tr') );
        
		showConfirmMessage(id, row);
    });
    
    $('.js-basic-example').on('click', '.button-modal', function () {
    	var info_id = $(this).attr('id');
    	
    	$.post("/userdetails", {jwt_user_id : info_id}, function(response){
    		try{
	    		var obj = JSON.parse(JSON.stringify(response));
	    		$("#modal_name").text(obj.user_name);
	    		$("#modal_surname").text(obj.user_surname);
	    		$("#modal_address").text(obj.user_address_street);
	    		$("#modal_city").text(obj.user_address_city);
	    		$("#modal_phone").text(obj.user_phone);
	    		$("#modal_email").text(obj.user_email);
	    		$("#modal_role").text(obj.role.role_name);
	    		$("#modal_gender").text(obj.gender.gender_name);
	    		$("#defaultModal").modal("show");
    		} catch(error){
    			$("#user-data").html("<h5>Current user doesn't exist anymore !!!</h5>");
    			$("#defaultModal").modal("show");
    		}
    	})
    	
    	
    	/*$.ajax({

            type: "POST",
            contentType: "application/json;charset=utf-8",
            url: "userdetails",
            data: "{}",
            dataType: "json",
            success: function (data) {
            	var result = JSON.parse(JSON.stringify(data));
            	alert( result.ime );
            },
            error: function (result) {
                alert("Error");
            }
        });*/
    })
    
    
    $('.js-clients-table').on('click', '.button-remove', function () {
        
    	var table = $('.js-clients-table').DataTable();
		var row = table.row( $(this).parents('tr') );
		var textMsg = "Client will be deleted permanently !";
		var url_id = $(this).attr('id');
		var url = url_id.split("__")[0];
		var id = url_id.split("__")[1];
		var swalMsg = "Client has been deleted !";
        
		showConfirmMessageNew(id, row, url, textMsg, swalMsg);
    });
});


function showConfirmMessage(id, row) {
    swal({
        title: "Are you sure?",
        text: "User account will be deleted permanently !",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes, delete it!",
        closeOnConfirm: false
    }, function () {
		$.post("/deleteuser", {jwt_user_id : id}, function(data){
			if(data == "1"){
				swal("Deleted!", "User account has been deleted.", "success");
				row.remove().draw(false);
			}
		})
    });
}

function showConfirmMessageNew(id, row, url, textMsg, swalMsg) {
    swal({
        title: "Are you sure?",
        text: textMsg,
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes, delete it!",
        closeOnConfirm: false
    }, function () {
		$.post(url, {jwt_client_id : id}, function(data){
			if(data == "1"){
				swal("Deleted!", swalMsg, "success");
				row.remove().draw(false);
			}
		})
    });
}
