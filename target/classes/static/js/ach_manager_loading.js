var edit_id = 0;
$(document).ready(function () {
    loadingAchLevel();
    loadingDomain();
});
function loadingAchLevel() {
    $.ajax({
        type: "GET",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/achievement/levels",
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        dataType: "JSON",
        success: function (data) {
            var html = "";
            var datalist = data.list;
            if (datalist != null) {
                for (var i = 0, l = datalist.length; i < l; i++) {
                    html += "<tr>"
                        + "<td>" + datalist[i].levelName + "</td>"
                        + "<td>" + datalist[i].achType + "</td>"
                        + "<td>" + datalist[i].point + "</td>"
                        + "<td class=\"edit_level\" onclick='editAchLevel(" + datalist[i].id + ")'" + "><button class=\"btn btn-link\" data-toggle=\"modal\" data-target=\"#achLevelModal\"><i class=\"fa fa-edit\"></i>&nbsp; 修改</button></td>"
                        + "</tr>";
                }
            }
            $('#ach_level_data').html(html);
        }
    });
}

function loadingDomain() {
    $.ajax({
        type: "GET",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/achievement/domains",
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        dataType: "JSON",
        success: function (data) {
            var html = "";
            var datalist = data.list;
            if (datalist != null) {
                for (var i = 0, l = datalist.length; i < l; i++) {
                    html += "<tr>"
                        + "<td>" + datalist[i].name + "</td>"
                        + "<td class=\"edit_level\" onclick='editDomain(" + datalist[i].id + ")'" + "><button class=\"btn btn-link\" data-toggle=\"modal\" data-target=\"#domainTypeModal\"><i class=\"fa fa-edit\"></i>&nbsp; 修改</button></td>"
                        + "</tr>";
                }
            }
            $('#domain_data').html(html);
        }
    });
}

function editAchLevel(id) {
    console.log("test");
    $.ajax({
        type: "GET",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/level/" + id,
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        dataType: "JSON",
        success: function (data) {
            $('#level_name').val(data.levelName);
            $('#ach_type').val(data.achType);
            $('#point').val(data.point);
            edit_id = id;
        }
    });
}

function editDomain(id) {
    console.log("test");
    $.ajax({
        type: "GET",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/user/" + id,
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        dataType: "JSON",
        success: function (data) {
            $('#domain_name').val(data.name);
            edit_id = id;
        }
    });
}

$('#submit_level_edit').click(function () {
    var ach_level = {
        achType:document.getElementById("ach_type").value,
        levelName:document.getElementById("level_name").value,
        point:document.getElementById("point").value,
    };
    $.ajax({
        type: "PUT",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/level/" + edit_id,
        data:JSON.stringify(ach_level),
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        dataType: "JSON",
        success: function () {
            loadingAchLevel();
        }
    });
});

$('#submit_domain_edit').click(function () {
    var ach_domain = {
        name:document.getElementById("domain_name").value,
    };
    $.ajax({
        type: "PUT",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/domain/" + edit_id,
        data:JSON.stringify(user_info),
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        dataType: "JSON",
        success: function () {
            loadingDomain();
        }
    });
});