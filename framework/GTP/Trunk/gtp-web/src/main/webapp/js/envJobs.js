/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    $('#envJobs_list').DataTable({
        "bLengthChange": true, //每页显示条数可选 
        "processing": false,
        "sorting": [0, "desc"],
        ajax: {
            "url": "/envJobs/allInfo",
            "type": "POST"
        },
        columns: [
            {data: "id"},
            {data: "jobname"},
            {data: "scheduleWeek"},
            {data: "scheduleTime"},
            {data: "modifyUser"},
            {data: "modifyTime"},
            {data: "enable"}
        ],
        columnDefs: [
            {
                targets: [2],
                data: "scheduleWeek",
                render: function (data, type, full) {
                    return getWeekDay(data);
                }
            },
            {
                targets: [3],
                data: "scheduleTime",
                render: function (data, type, full) {
                    return timeStamp2SimpleTime(data);
                }
            },
            {
                targets: [5],
                data: "modifyTime",
                render: function (data, type, full) {
                    return timeStamp2String(data);
                }
            },
            {
                targets: [7],
                data: "id",
                render: function (data, type, full) {
                    return  "<div class='btn-group task-operation-div dropup'>\n\
                                        <button class='btn btn-success dropdown-toggle' data-toggle='dropdown' aira-expanded='false'>\n\
                                            Operations <span class='caret'></span>\n\
                                        </button>\n\
                                        <ul class='dropdown-menu pull-right' role='menu'>\n\
                                            <li><a id='evn-job-detail' href='/envJobs/edit/" + data + "'>Edit</a></li>\n\
                                            <li><a id='evn-job-detail' href='/envJobs/detail/" + data + "'>Detail</a></li>\n\
                                        </ul>\n\
                                    </div>";
                }
            }
        ]
    });
});


