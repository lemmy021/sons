$(document).ready(function(){
	$('#delivery_date_datepicker_container input').datepicker({
		format: 'dd.mm.yyyy.',
		todayHighlight: true,
		autoclose: true,
		container: '#delivery_date_datepicker_container'
	});
	
	$('#payment_date_datepicker_container input').datepicker({
		format: 'dd.mm.yyyy.',
		todayHighlight: true,
		autoclose: true,
		container: '#payment_date_datepicker_container'
	});
})