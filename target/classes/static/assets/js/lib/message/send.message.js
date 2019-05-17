$(document).ready(function () {
    var user_info = getUserInfo();
    $('#send').click(function () {
        var message_input = "#send_message";
        var message =  $.trim($(message_input).val())
        var id = $("#to_uid").val();
        var url = "v0.1/message/send/"+id;
        //sendMessage(message);
        $.ajax({
            type: "GET",
            contentType: 'application/json;charset=utf-8',
            url: url,
            data: {fromUid:user_info.userName,message:message},
            traditional: true,//这使json格式的字符不会被转码
            timeout: 20000,
            dataType: "json",
            success: function (data) {
                sendMessageContent(data);
            }
        });
        $(message_input).val("");
    })
    function sendMessageContent(message) {
        var myDate = new Date().toLocaleTimeString();
        var message_content = $("#message_content");
        var html = "<ul><li>\n" +
            "                                            <div class=\"msg-sent msg-container\">\n" +
            "                                                <div class=\"avatar\">\n" +
            "                                                    <img src=\"assets/images/avatar/64-2.jpg\" alt=\"\">\n" +
            "                                                    <div class=\"send-time\">"+myDate+"</div>\n" +
            "                                                </div>\n" +
            "                                                <div class=\"msg-box\">\n" +
            "                                                    <div class=\"inner-box\">\n" +
            "                                                        <div class=\"name\">\n" + user_info.userName +
            "                                                        </div>\n" +
            "                                                        <div class=\"meg\">\n" +
                                                                        message.message +
            "                                                        </div>\n" +
            "                                                    </div>\n" +
            "                                                </div>\n" +
            "                                            </div><!-- /.msg-sent -->\n" +
            "                                        </li></ul>"

        message_content.append(html);
        message_content[0].scrollTop = message_content[0].scrollHeight;
    }
});

