$(document).ready(function () {
    var url = document.URL;
    if (url.split('?') !== undefined) {
        //根据？切割url获取 右边的数据id=1 /xxx.html?id=1
        var getId = url.split('?')[1];
        //根据 = 切割 确定取的是id
        if (getId.split('=')[0] === 'id') {
            //拿到id值
            var id = getId.split('=')[1];
            //业务逻辑......
            alert(id);
        }
    }
});
$('#commit').click(function () {
    //跳转的时候把id传过去
    var id = 1;
    $(location).prop('href',"ach_point_manager.html?id="+id);
});