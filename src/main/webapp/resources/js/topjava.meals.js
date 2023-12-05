const mealAjaxUrl = "meals/";

const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: updateFilteredTable
};

function updateFilteredTable() {
    $.ajax({
        type: "POST",
        url: mealAjaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableData);
}

function clearFilter() {
    $.ajax({
        type: "POST",
        url: mealAjaxUrl + "filter",
        data: $("#filter")[0].reset()
    }).done(updateTableData);
}
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            //https://syntaxfix.com/question/17073/datatables-warning-requested-unknown-parameter-0-from-the-data-source-for-row-0#:~:text=For%20null%20or%20undefined%20value%20error
            //without columnDefs throws DataTables warning: ... Requested unknown parameter ...
            "columnDefs": [{
                "defaultContent": "-",
                "targets": "_all"
            }],
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContext": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});