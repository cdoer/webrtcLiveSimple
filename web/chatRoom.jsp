<%@ page import="com.yxy.webrtc.model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
    User user = (User) request.getSession().getAttribute("user");
    if(user==null||user.getRoom()==null){
        response.sendRedirect("/index.jsp");
    }
    List<String> colors = new ArrayList<String>();
    colors.add("layui-bg-orange");
    colors.add("layui-bg-green");
    colors.add("layui-bg-cyan");
    colors.add("layui-bg-blue");
    colors.add("layui-bg-black");
    colors.add("layui-bg-gray");
    request.getSession().setAttribute("colors",colors);
%>
<html>
<!DOCTYPE html>
<head>
    <title>视频会议室 Create by yang</title>
    <jsp:include page="src-import.jsp"></jsp:include>
    <script src="chatRoom.js"></script>
</head>
<body>
<jsp:include page="header.jsp"/>


<div class="layui-container fly-marginTop">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md7">
            <div class="layui-card">
                <div class="layui-card-header">
                    <c:if test="${user.room.creator==user}">
                        正在主持会议
                    </c:if>
                    <c:if test="${user.room.creator!=user}">
                        主持人:${user.room.creator.name}
                    </c:if>
                    ,会议ID【${user.room.roomName}】
                </div>
                <div class="layui-card-body">
                    <div style="height:80%; width: 100%;">
                        <video id="mainVideo" style="height: 80%; width:100%;" autoplay></video>
                        <div id="joinUsers" style="height: 19%;padding-top:1%; line-height: 1;">参与者：
                            <c:forEach items="${user.room.userList}" var="user2" varStatus="index">
                                <span class="layui-badge ${colors[index.index%fn:length(colors)]}" data-id="${user2.id}">${user2.name}</span>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-col-md5">
            <div class="layui-card">
                <div class="layui-card-header">聊天室</div>
                <div class="layui-card-body">
                    <div style="height:80%;">
                        <div class="chatbox" style="width: 100%;height:60%;">
                            <ul>

                            </ul>
                        </div>
                        <div class="inputbox" style="width: 100%;height:39%;padding-top:1%;box-sizing: border-box;  border-top:1px solid #ccc;">
                            <textarea style="width: 100%;height:100%;resize: none; border: none;" id="inputcontent"></textarea>
                            <div class="input_btns" style="position: absolute; bottom: 20px;right:20px;">
                                <button class="layui-btn" id="sendMsg">
                                    <i class="layui-icon layui-icon-release"></i>
                                    发送
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<style>
    .chatbox{
        overflow-x: hidden;
        overflow-y: auto;
        padding-right: 15px;
        box-sizing: content-box;
    }
    .chatbox ul .layim-chat-mine{
        text-align: right;
        padding-left: 0;
        padding-right: 60px;
    }
    .chatbox ul li {
        position: relative;
        margin-bottom: 10px;
        padding-left: 60px;
        min-height: 68px;
    }
    .layim-chat-user img {
        width: 40px;
        height: 40px;
        border-radius: 100%;
    }
    .layim-chat-mine .layim-chat-user {
        left: auto;
        right: 3px;
    }
    .layim-chat-user {
        position: absolute;
        left: 3px;
    }
    .layim-chat-mine .layim-chat-user cite {
        left: auto;
        right: 60px;
        text-align: right;
    }
    .layim-chat-user cite {
        position: absolute;
        left: 60px;
        top: -2px;
        width: 500px;
        line-height: 24px;
        font-size: 12px;
        white-space: nowrap;
        color: #999;
        text-align: left;
        font-style: normal;
    }
    .layim-chat-mine .layim-chat-text {
        margin-left: 0;
        text-align: left;
        background-color: #5FB878;
        color: #fff;
    }
    .layim-chat-text {
        position: relative;
        line-height: 22px;
        margin-top: 25px;
        padding: 8px 15px;
        background-color: #e2e2e2;
        border-radius: 3px;
        color: #333;
        word-break: break-all;
        max-width: 462px\9;
    }
    .layim-chat-text, .layim-chat-user {
        display: inline-block;
        *display: inline;
        *zoom: 1;
        vertical-align: top;
        font-size: 14px;
    }

    .layim-chat-mine .layim-chat-user cite i {
        padding-right: 15px;
    }
    .layim-chat-user cite i {
        padding-right: 15px;
        font-style: normal;
    }

    .layim-chat-system i {
        padding-right: 15px;
        font-style: normal;
    }

    .layim-chat-system span {
        display: inline-block;
        line-height: 30px;
        padding: 0 15px;
        border-radius: 3px;
        background-color: #e2e2e2;
        cursor: default;
        font-size: 13px;
    }
</style>
<script>
    $(function () {
        var users = [];
        <c:forEach items="${user.room.userList}" var="user3">
            users.push("${user3.id}");
        </c:forEach>

        webos.execute("chatRoom",{
           me:{
               name:"${user.name}",
               id:"${user.id}"
           },
            room:{
               creator:"${user.room.creator.id}",
               title:"${user.room.title}",
               roomName:"${user.room.roomName}",
               users:users
            }
        });
    })
</script>
</body>
</html>
