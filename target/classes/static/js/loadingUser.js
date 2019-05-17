var html = "";
var params = {
    count: true,
    offset: 0,
    limit: 10
};
$(document).ready(function () {
    loadingUser(params);
});

function loadingUser(params) {
    $.ajax({
        type: "GET",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/user/page",
        data: params,
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        dataType: "JSON",
        success: function (data) {
           applyUserList(data);
        }
    });
}

function applyUserList(data) {
    var html = "";
    var datalist = data.list;
    if (datalist != null) {
        for (var i = 0, l = datalist.length; i < l; i++) {
            html += "<tr>"
                + "<td>" + datalist[i].userName + "</td>"
                + "<td>" + datalist[i].realName + "</td>"
                + "<td>" + datalist[i].email + "</td>"
                + "<td>" + datalist[i].phone + "</td>"
                + "<td>" + datalist[i].address + "</td>"
                + "<td>" + formatDate(datalist[i].createTime) + "</td>"
                + "<td>" + datalist[i].nodeName + "</td>"
                + "<td>" + datalist[i].role + "</td>"
                + "<td class=\"edit_user\"" + "><button onclick='editUser(" + datalist[i].id + ")' "+"class=\"btn btn-link\" data-toggle=\"modal\" data-target=\"#myModal\"><i class=\"fa fa-edit\"></i>&nbsp; 修改</button></td>"
                + "</tr>";
        }
    }
    $('#data_table').html(html);
    if (params.offset === 0) {
        initPage(data);
    }
}

function editUser(id) {
    getUserInfoForm(id);
    getNodeSelect();
    $('#node').html(html);
    $('#real_name').val(user_info.real_name);
    $('#email').val(user_info.email);
    $('#phone').val(user_info.phone);
    $('#address').val(user_info.address);
    $('#node').val(user_info.node_id);
    $('#role').val(user_info.role);
}

function getUserInfoForm(id) {
    $.ajax({
        type: "GET",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/user/" + id,
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        async: false,
        dataType: "JSON",
        success: function (data) {
            user_info.user_name = data.userName;
            user_info.real_name = data.realName;
            user_info.email = data.email;
            user_info.phone = data.phone;
            user_info.address = data.address;
            user_info.node_id = data.nodeId;
            user_info.role = data.role;
        }
    });
}

function getNodeSelect() {
    $.ajax({
        type: "GET",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/node/",
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        async: false,
        dataType: "JSON",
        success: function (data) {
            html = "";
            for (var i = 0; i < data.length; i++) {
                html += "<option value='" + data[i].id + "'>" + data[i].nodeName + "</option>";
            }
        }
    });
}