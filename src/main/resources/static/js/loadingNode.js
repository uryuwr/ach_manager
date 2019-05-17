var params = {
    count: true,
    offset: 0,
    limit: 10
};

var node_info = {
    id:null,
    nodeName:""
};

$(document).ready(function () {
    loadingNode(params);
});

function loadingNode(params) {
    $.ajax({
        type: "GET",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/node/page",
        data: params,
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        async: false,
        dataType: "JSON",
        success: function (data) {
            applyNodeList(data);
        }
    });
}

function applyNodeList(data) {
    var html = "";
    var datalist = data.list;
    if (datalist != null) {
        for (var i = 0, l = datalist.length; i < l; i++) {
            html += "<tr>"
                + "<td>" + datalist[i].nodeName + "</td>"
                + "<td class=\"edit_node\"" + "><button onclick='editNode("+ datalist[i].id + ")' class=\"btn btn-link\" data-toggle=\"modal\" data-target=\"#nodeModal\"><i class=\"fa fa-edit\"></i>&nbsp; 修改</button>"
                + "<button class=\"btn btn-link\" onclick='deleteNode("+ datalist[i].id + ")'>"
                + "<i class=\"fa fa-times\"></i>"
                + "&nbsp; 删除\n"
                + "</button>"
                + "</td>"
                + "</tr>";
        }
    }
    $('#data_table').html(html);
    if (params.offset === 0) {
        initPage(data);
    }
}

function editNode(id) {
    getNodeInfo(id);
    $("#node_name").val(node_info.nodeName);
}

function getNodeInfo(id) {
    $.ajax({
        type: "GET",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/node/" + id,
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        async: false,
        dataType: "JSON",
        success: function (data) {
            node_info.id = data.id;
            node_info.nodeName = data.nodeName;
        }
    });
}

function deleteNode(id) {
    $.ajax({
        type: "DELETE",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/node/" + id,
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        async: false,
        success: function () {
        }
    });
}
