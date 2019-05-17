var websocket = null;
function getWebsocket(data) {
//判断当前浏览器是否支持WebSocket
    if('WebSocket' in window){
        if (websocket === null) {
            websocket = new WebSocket("ws://localhost:8080/websocket/" + data.userName);
        }
    }
    else{
        alert('Not support websocket')
    }

    //连接发生错误的回调方法
    websocket.onerror = function(){
        setMessageInnerHTML("error");
    };

//连接成功建立的回调方法
    websocket.onopen = function(event){
        //setMessageInnerHTML("open");
    }

//接收到消息的回调方法
    websocket.onmessage = function(event){
        var user_info = getUserInfo();
        var message = JSON.parse(event.data);
        if (message.uid === user_info.userName){
            getMessageContent(message)
        }
        //setMessageInnerHTML(event.data);
    }

//连接关闭的回调方法
    websocket.onclose = function(){
        //setMessageInnerHTML("close");
    }

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function(){
        websocket.close();
    }
}

//将消息显示在网页上
    function setMessageInnerHTML(innerHTML){
        //$("#message").append(innerHTML+"<br/>");
        $("#message").empty().html(innerHTML.trim());
    }

//关闭连接
    function closeWebSocket(){
        websocket.close();
    }

//发送消息
    function sendMessage(message){
        websocket.send(message);
    }

    function getMessageContent(message) {
        var myDate = new Date().toLocaleTimeString();
        var html = "<ul><li>\n" +
            "                                                <div class=\"msg-received msg-container\">\n" +
            "                                                    <div class=\"avatar\">\n" +
            "                                                        <img src=\"assets/images/avatar/64-2.jpg\" alt=\"\">\n" +
            "                                                        <div class=\"send-time\">"+ myDate +"</div>\n" +
            "                                                    </div>\n" +
            "                                                    <div class=\"msg-box\">\n" +
            "                                                        <div class=\"inner-box\">\n" +
            "                                                            <div class=\"name\">\n" + message.fromUid +
            "                                                            </div>\n" +
            "                                                            <div class=\"meg\">\n" + message.message +
            "                                                            </div>\n" +
            "                                                        </div>\n" +
            "                                                    </div>\n" +
            "                                                </div><!-- /.msg-received -->\n" +
            "                                            </li></ul>"
        var message_content = $('#message_content');
        message_content.append(html);
        message_content[0].scrollTop = message_content[0].scrollHeight;
    }

