var html = "";
var params = {
    count: true,
    offset: 0,
    limit: 10,
    state: true,
    selects: "auditState = 0"
};

var ach = {
    domainType: 0,
    title: "",
    author: "",
    auditState: 0,
    levelId: 0,
    createUser: "",
    attachment: "",
    achType: 0,
    content: "",
    proportion: 0.1,
    auditUser: ""
};

$(document).ready(function () {
    loadingAudit(params);
});

function loadingAudit(params) {
    $.ajax({
        type: "GET",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/achievement/page",
        data: params,
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        dataType: "JSON",
        success: function (data) {
            applyAchList(data);
        }
    });
}

function applyAchList(data) {
    var html = "";
    var datalist = data.list;
    if (datalist != null) {
        for (var i = 0, l = datalist.length; i < l; i++) {
            html += "<tr>"
                + "<td><a data-toggle=\"modal\" data-target=\"#achInfo\" href='#' onclick='getAch(" + datalist[i].id + ")'>查看</a>" +"</td>"
                + "<td>" + datalist[i].achTypeName + "</td>"
                + "<td>" + datalist[i].title + "</td>"
                + "<td>" + datalist[i].createUser + "</td>"
                + "<td>" + formatDate(datalist[i].createTime) + "</td>"
                + "<td>" + datalist[i].auditUser + "</td>"
                + "<td>" + datalist[i].auditStateName + "</td>"
                + "<td class=\"audit\"" + "><a href='#' style=\"margin-right:10px \" onclick='audit(" + datalist[i].id + ")' "+"><i class=\"ti-check\"></i>&nbsp; 通过</a></td>";
                if(datalist[i].attachment.trim() !== "") {
                   html += "<td><a href=\"#\" onclick='download_file("+ datalist[i].id + ")'>下载附件</a></td></tr>";
                } else {
                    html += "<td></td></tr>";
                }
        }
    }
    $('#data_table').html(html);
    if (params.offset === 0) {
        initPage(data);
    }
}

$('.audit_state').change(function () {
    $('.audit_state').parent().removeClass('active');
    $(this).parent().addClass('active');
    var state = getAuditState();
    var select = "auditState = " + state;
    params.selects = select;
    loadingAudit(params);
});

function getAch(id) {
    $.ajax({
        type: "GET",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/achievement/" + id,
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        dataType: "JSON",
        success: function (data) {
            $('#achType').val(data.achTypeName);
            $('#title').val(data.title);
            $('#authors').val(data.author);
            $('#content').val(data.content);
            $('#domainType').val(data.domainTypeName);
            $('#level').val(data.levelName);
            $('#createUserName').val(data.createUserName);
            $('#proportion').val(data.proportion);
        }
    });
}

function download_file(id) {
    window.location.href = "/v0.1/achievement/download/" + id;
}

function audit(id) {
    $.ajax({
        type: "PUT",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/achievement/" + id + "/action/audit",
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        success: function () {
            $(location).prop('href',"ach_info_show.html");
        }
    });
}