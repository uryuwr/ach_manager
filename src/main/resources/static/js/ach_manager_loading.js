var edit_id = null;
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
            if (data != null) {
                for (var i = 0, l = data.length; i < l; i++) {
                    html += "<tr>"
                        + "<td>" + data[i].id + "</td>"
                        + "<td>" + data[i].levelName + "</td>"
                        + "<td>" + data[i].point + "</td>"
                        + "<td class=\"edit_level\" ><button onclick='editAchLevel(" + data[i].id + ")' class=\"btn btn-link\" data-toggle=\"modal\" data-target=\"#achLevelModal\"><i class=\"fa fa-edit\"></i>&nbsp; 修改</button>"
                        + "<button onclick='deleteLevel(" + data[i].id + ")' class='btn btn-link'><i class='fa fa-times'></i>&nbsp; 删除</button></td>"
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
            if (data != null) {
                for (var i = 0, l = data.length; i < l; i++) {
                    html += "<tr>"
                        + "<td>" + data[i].id + "</td>"
                        + "<td>" + data[i].domainName + "</td>"
                        + "<td class=\"edit_domain\"><button onclick='editDomain(" + data[i].id +  ")' class=\"btn btn-link\" data-toggle=\"modal\" data-target=\"#domainTypeModal\"><i class=\"fa fa-edit\"></i>&nbsp; 修改</button>"
                        + "<button onclick='deleteDomain(" + data[i].id + ")' class='btn btn-link'><i class='fa fa-times'></i>&nbsp; 删除</button></td>"
                        + "</tr>";
                }
            }
            $('#domain_data').html(html);
        }
    });
}

function editAchLevel(id) {
    $.ajax({
        type: "GET",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/achievement/levels/" + id,
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        dataType: "JSON",
        success: function (data) {
            $('#level_name').val(data.levelName);
            $('#point').val(data.point);
            edit_id = id;
        }
    });
}

function deleteLevel(id) {
    alert(id);
}

function editDomain(id) {
    $.ajax({
        type: "GET",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/achievement/domains/" + id,
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        dataType: "JSON",
        success: function (data) {
            $('#domain_name').val(data.domainName);
            edit_id = id;
        }
    });
}

function deleteDomain(id) {
    alert(id);
}

$('#submit_level_edit').click(function () {
    var ach_level = {
        id:edit_id,
        levelName:document.getElementById("level_name").value,
        point:document.getElementById("point").value,
    };
    $.ajax({
        type: "PUT",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/achievement/levels",
        data:JSON.stringify(ach_level),
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        success: function () {
            edit_id = null;
            loadingAchLevel();
        },
        error: function () {
            edit_id = null;
        }
    });
});

$('#submit_domain_edit').click(function () {
    var ach_domain = {
        id:edit_id,
        domainName:document.getElementById("domain_name").value,
    };
    $.ajax({
        type: "PUT",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/achievement/domains",
        data:JSON.stringify(ach_domain),
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        success: function () {
            edit_id = null;
            loadingDomain();
        },
        error: function () {
            edit_id = null;
        }
    });
});