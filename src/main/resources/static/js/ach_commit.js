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
var domain_html = "";
var level_html = "";
var error = false;

$(document).ready(function () {
    getdomainHtml();
    getLevelHtml();
    $('#domain_type').html(domain_html);
    $('#level').html(level_html);
});

$('#commit').click(function () {
    error = false;
    uploadFile();
    if (!error) {
        ach.title = $('#title').val();
        ach.author = $('#author').val();
        ach.content = $('#content').val();
        ach.domainType = $('#domain_type').val();
        ach.levelId = $('#level').val();
        ach.proportion = $('#proportion').val();
        var file = document.getElementById("file-multiple-input").files[0];
        if (file !== undefined) {
            ach.attachment = document.getElementById("file-multiple-input").files[0].name;
        }
        commitAch(ach);
    }
});

function uploadFile() {
    var formData = new FormData();
    var file = document.getElementById("file-multiple-input").files[0];
    if (file !== undefined) {
        formData.append("files",file );
        $.ajax({
            url: "/v0.1/achievement/upload",
            type: "POST",
            data: formData,
            async: false,
            contentType: false,
            processData: false,
            success: function () {
                error = false;
            },
            error: function (XMLHttpRequest) {
                var ex = XMLHttpRequest.responseText;
                if (ex !== "") {
                    error = true;
                    alert(JSON.parse(ex).msg);
                }
            }
        });
    }
}

$('.ach_type').change(function () {
    $('.ach_type').parent().removeClass('active');
    $(this).parent().addClass('active');
    ach.achType =  $('.ach_type:checked').val();
});

function commitAch(ach) {
    $.ajax({
        type: "POST",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/achievement",
        data:JSON.stringify(ach),
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        success: function () {
            alert("提交成功");
        }
    });
}

function getLevelHtml() {
    $.ajax({
        type: "GET",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/achievement/levels",
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        async: false,
        dataType: "JSON",
        success: function (data) {
            if (data != null) {
                for (var i = 0, l = data.length; i < l; i++) {
                    level_html += "<option value='" + data[i].id + "'>" + data[i].levelName + "</option>";
                }
            }
        }
    });
}

function getdomainHtml() {
    $.ajax({
        type: "GET",
        contentType: 'application/json;charset=utf-8',
        url: "/v0.1/achievement/domains",
        traditional: true,//这使json格式的字符不会被转码
        timeout: 20000,
        async: false,
        dataType: "JSON",
        success: function (data) {
            if (data != null) {
                for (var i = 0, l = data.length; i < l; i++) {
                    domain_html += "<option value='" + data[i].id + "'>" + data[i].domainName + "</option>";
                }
            }
        }
    });
}