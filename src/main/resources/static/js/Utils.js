Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

function formatDate(date) {
    return new Date(date).Format("yyyy-MM-dd HH:mm:ss");
}

function initPage(data) {
    var count = data.count;
    $('#bootstrap-data-table_info').html("当前记录总数：" + count + "条");
    var pagehtml = "";
    var limit = params.limit;
    var page = 1;
    if (count > limit) {
        page = count % limit + 1;
    }
    for (var i = 1; i <= page; i++) {
        pagehtml += "   <li onclick='page(" + i + ")'" + "class=\"paginate_button page-item \"><a href=\"#\"\n" +
            "               aria-controls=\"bootstrap-data-table\"\n" +
            "               data-dt-idx='" + i + "' tabindex=\"0\"\n" +
            "               class=\"page-link\"> " + i + "</a></li>"
    }
    $('#page').html(pagehtml);
}

function page(page) {
    params.offset = page - 1;
    loadingUser(params);
}