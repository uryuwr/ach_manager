var html = "";
var params = {
    count: true,
    offset: 0,
    limit: 10
};

$(document).ready(function () {
    loadingAchInfo(params);
});

function loadingAchInfo(params) {
    $.ajax({
        type: "GET",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/achievement/infos",
        data: params,
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        dataType: "JSON",
        success: function (data) {
            applyAchInfoList(data);
        }
    });
}

function applyAchInfoList(data) {
    var html = "";
    var datalist = data.list;
    if (datalist != null) {
        for (var i = 0, l = datalist.length; i < l; i++) {
            html += "<tr>"
                + "<td>" + datalist[i].achTypeName + "</td>"
                + "<td>" + datalist[i].domainTypeName + "</td>"
                + "<td>" + datalist[i].title + "</td>"
                + "<td>" + datalist[i].levelName + "</td>"
                + "<td>" + datalist[i].createUserName + "</td>"
                + "<td>" + formatDate(datalist[i].createTime) + "</td>"
                + "<td>" + datalist[i].point + "</td>"
        }
    }
    $('#data_table').html(html);
    if (params.offset === 0) {
        initPage(data);
    }
}

$('#export').click(function () {
    window.location.href = "/v0.1/excel";
});