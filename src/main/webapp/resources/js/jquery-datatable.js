$(function () {
    var usertable = $('.js-basic-example').DataTable({
        responsive: true,
        "columnDefs": [
            { "searchable": false, "targets": 7 }
          ],
          "order": [[ 1, 'asc' ]]
    });
    
    var clienttable = $('.js-clients-table').DataTable({
        responsive: true,
        "columnDefs": [
            { "searchable": false, "targets": 5 }
          ],
          "order": [[ 1, 'asc' ]]
    });
    
    usertable.on( 'order.dt search.dt', function () {
    	usertable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );
    } ).draw();
    
    //Exportable table
    $('.js-exportable').DataTable({
        dom: 'Bfrtip',
        responsive: true,
        buttons: [
            'copy', 'csv', 'excel', 'pdf', 'print'
        ]
    });
});