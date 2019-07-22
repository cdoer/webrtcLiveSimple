/**
 * Created by yang on 2018-08-03.
 */
webos.register("chatRoom", function (params) {
    var me = params.me;
    var room = params.room;
    //消息类型 这个demo总共就5种
    var MSG_TYPE = {
        exit: "exit",
        join: "join",
        online: "online",
        room_chat: "room_chat",
        end_chat: "end_chat",
        offer: "offer",//webrtc请求
        answer: "answer",//webrtc回复
        candidate: "candidate"//candidate消息
    };


    $("#inputcontent").keyup(function (e) {
        if (event.keyCode == 13) {
            sendMsg();
        }
    });

    $("#sendMsg").click(sendMsg);


    function sendMsg(){
        var content = $("#inputcontent").val();
        if(content.trim()){
            $("#inputcontent").val("");
            send({
                msg:content,
                type:MSG_TYPE.room_chat
            });
            var data = {
                fromName:me.id==room.creator?me.name+"<span style='color:red;'>[主持人]</span>":me.name,
                time:new Date().getTime(),
                msg:content
            };
            appendMsg(true,data);
        }
    }

    function appendMsg(ismine,data){
        var box = $(".chatbox ul");
        var content = '<div class="layim-chat-user">\
        <img src="http://cdn.firstlinkapp.com/upload/2016_6/1465575923433_33812.jpg">\
            <cite><i>'+new Date(data.time).format("yyyy-MM-dd hh:mm:ss")+'</i>'+data.fromName+'</cite>\
        </div>\
        <div class="layim-chat-text">'+data.msg+'</div>';
        if(ismine){
            box.append("<li class='layim-chat-mine'>"+content+"</li>")
        }else{
            box.append("<li>"+content+"</li>")
        }
        $(".chatbox")[0].scrollTop =  $(".chatbox")[0].scrollHeight;
    }

    function appendSystemMsg(data){
        var box = $(".chatbox ul").append("<li class='layim-chat-system'>" +
            "<span>" +
            "<i>"+new Date(data.time).format("yyyy-MM-dd hh:mm:ss")+"</i>"
            +data.msg+"</span></li>");
        $(".chatbox")[0].scrollTop =  $(".chatbox")[0].scrollHeight;
    }


    Date.prototype.format = function(fmt)
    { //author: meizz
        var o = {
            "M+" : this.getMonth()+1,                 //月份
            "d+" : this.getDate(),                    //日
            "h+" : this.getHours(),                   //小时
            "m+" : this.getMinutes(),                 //分
            "s+" : this.getSeconds(),                 //秒
            "q+" : Math.floor((this.getMonth()+3)/3), //季度
            "S"  : this.getMilliseconds()             //毫秒
        };
        if(/(y+)/.test(fmt))
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(var k in o)
            if(new RegExp("("+ k +")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        return fmt;
    }


    var websocket = null;
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        websocket = new WebSocket("wss://" + document.location.host + "/websocket");
    } else {
        layer.msg('当前浏览器 Not support websocket')
    }

    //连接发生错误的回调方法
    websocket.onerror = function () {
        layer.msg("WebSocket连接发生错误");
    };

    //连接成功建立的回调方法
    websocket.onopen = function () {
        layer.msg("WebSocket连接成功");
    }

    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        var data = JSON.parse(event.data);
        console.log(data);
        receive[data.type](data);
    }

    //连接关闭的回调方法
    websocket.onclose = function () {
        layer.msg("WebSocket连接关闭");
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        closeWebSocket();
    }

    //关闭WebSocket连接
    function closeWebSocket() {
        websocket.close();
    }

    function msg(msg) {
        layer.msg(msg);
    }

    function send(data) {
        var data = data || {};
        data.from = name;
        websocket.send(JSON.stringify(data));
    }

    function setVideo(id, stream) {
        var video = $("#" + id);
        if (video[0]) {
            //video[0].src = window.URL.createObjectURL(stream);
            video[0].srcObject = stream;
        }
    }

    //以下为接收到消息处理
    var receive = {
        room_chat:function(data){
            appendMsg(false,data);
        },
        join:function(data){
            data.msg = data.fromName+"已加入会议";
            appendSystemMsg(data);
            var from = $("[data-id="+data.from+"]");
            if(!from[0]){
                $("#joinUsers").append('<span class="layui-badge" data-id="'+data.from+'">'+data.fromName+'</span>');
            }
        },
        online:function(data){
            //收到次此消息说明成员以及准备就绪
            if(me.id==room.creator&&me.id!=data.from){
               setTimeout(function(){
                   pushVideo(data.from);
               },2000)
            }
        },
        exit:function(data){
            data.msg = data.fromName+"已退出会议";
            appendSystemMsg(data);
            var from = $("[data-id="+data.from+"]");
            if(from[0]){
                from.remove();
            }
            if(me.id==data.from){
                location.href = "/index.jsp";
            }
        },
        end_chat:function(data){
            layer.msg("会议已解散");
            location.href = "/index.jsp";
        },
        offer: function (data) {//主持人收到offer请求 发送answer回复
            var conn = rtc.connect(function (e) {
                rtc.remotestream = e.stream;
                setVideo("mainVideo", rtc.remotestream);
            }, function (e) {
                send({
                    type: MSG_TYPE.candidate,
                    candidate: e.candidate,
                    to: data.from
                });
            });
            var rtcs = new RTCSessionDescription(data.offer);
            conn.setRemoteDescription(rtcs, function (answer) {
                conn.createAnswer(function (answer) {
                    conn.setLocalDescription(answer);
                    send({
                        type: MSG_TYPE.answer,
                        answer: answer,
                        to: data.from
                    })
                }, function (e) {
                    console.log("发送answer失败：" + e);
                });
            }, function (e) {
                console.log("建立连接失败，失败原因：" + e);
            });
        },
        answer: function (data) {
            $.each(rtc.candidates, function (index, item) {
                rtc.conn.addIceCandidate(item, function () {
                }, function (ee) {
                });
            });
            var rtcs = new RTCSessionDescription(data.answer);
            rtc.conn.setRemoteDescription(rtcs);
        },
        candidate: function (data) {
            var candidate = new RTCIceCandidate(data.candidate);
            rtc.conn.addIceCandidate(candidate, function (e) {
            }, function (ee) {
                rtc.candidates.push(candidate);
            });
        }
    };


    function WebRtc(socket, config) {
        var cfg = {
            keyField: "id",
            servers: {
                iceServers: []
            },
            options: {video: true, audio: true}
        };
        this.remotestream = null;
        this.localstream = null;
        this.config = $.extend(cfg, config);
        this.socket = socket ? socket : this.config.socket;
        this.PeerConnection = (window.webkitRTCPeerConnection || window.mozRTCPeerConnection || window.RTCPeerConnection || undefined);
        this.RTCSessionDescription = (window.webkitRTCSessionDescription || window.mozRTCSessionDescription || window.RTCSessionDescription || undefined);
        this.getUserMedia = (navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia);
        this.successCallback = null;
        this.errorCallback = null;
        this.conn = null;
        this.candidates = [];
    }

    WebRtc.prototype.startGetUserMedia = function (options, successCallback, errorCallback) {
        var _this = this;
        this.getUserMedia.call(navigator, options, function (localstreamMedia) {
            _this.localstream = localstreamMedia;
            if (typeof successCallback === "function") successCallback(localstreamMedia);
        }, function (e) {
            layer.alert("打开摄像头错误！", {icon: 5});
            if (typeof errorCallback === "function") errorCallback(e);
        });
    }

    WebRtc.prototype.connect = function (onaddstream, onicecandidate) {
        var conn = this.conn = new this.PeerConnection();
        /*var conn = this.conn = new this.PeerConnection({
         此参数打洞用   实现广域网通信  局域网不需要
         });*/
        if (this.localstream != null) {
            conn.addStream(this.localstream);
        }
        //连接成功的回调函数 显示对方的画面
        conn.onaddstream = function (e) {
            if ($.isFunction(onaddstream)) onaddstream(e);
        };
        conn.onicecandidate = function (e) {
            if (e.candidate) {
                if ($.isFunction(onicecandidate)) onicecandidate(e);
            }
        };
        return conn;
    }

    var rtc = new WebRtc(websocket);
    if(me.id==room.creator){
        rtc.startGetUserMedia({video:true,audio:true},function(local){
            setVideo("mainVideo",local);
            //准备就绪
            $.each(room.users,function(index,item){
                if(me.id!=item){
                    pushVideo(item);
                }
            });
        },function(){
            msg("打开摄像头失败！");
        });
    }

    var first = true;
    function pushVideo(to){
        //主持人发送offer给进来的成员
        var conn;
        if(first) {
            conn = rtc.connect(function (e) {
                rtc.remotestream = e.stream;
                //setVideo("you",rtc.remotestream);
            }, function (e) {
                send({
                    type: MSG_TYPE.candidate,
                    candidate: e.candidate,
                    to: to
                });
            });
            //first=false;
        }else{
            conn = rtc.conn;
        }
        conn.createOffer(function(offer){
            conn.setLocalDescription(offer);
            //发送offer
            send({
                type:MSG_TYPE.offer,
                offer:offer,
                to:to
            })
        },function(e){
            console.log("发送offer失败!失败原因"+e);
        });
    }
});


