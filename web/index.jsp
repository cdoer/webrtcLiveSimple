<%@ page import="com.yxy.webrtc.utils.ChatRoomUtils" %>
<%@ page import="java.util.Collection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
    Collection rooms =  ChatRoomUtils.chatRoomMap.values();
    request.setAttribute("rooms",rooms);
%>
<html>
<!DOCTYPE html>
<head>
    <title>视频会议室 Create by yang</title>
    <jsp:include page="src-import.jsp"></jsp:include>
    <script src="index.js"></script>
</head>
<body>
<jsp:include page="header.jsp"/>


<div class="layui-container fly-marginTop">
    <div class="layui-row layui-col-space15">
        <c:if test="${empty user.room}">
            <div class="layui-col-md6">
                <div class="layui-card">
                    <div class="layui-card-header">创建会议</div>
                    <div class="layui-card-body" style="text-align: center;">
                        <button class="layui-btn-primary layui-btn layui-btn-lg layui-anim-rotate layui-anim " id="createRoom">
                            <i class="layui-icon layui-icon-release"></i>
                            创建会议
                        </button>
                    </div>
                </div>
            </div>
            <div class="layui-col-md6">
                <div class="layui-card">
                    <div class="layui-card-header">加入会议</div>
                    <div class="layui-card-body" style="text-align: center;">
                        <button class="layui-btn-primary layui-btn layui-btn-lg layui-anim-rotate layui-anim " id="joinRoom">
                            <i class="layui-icon layui-icon-search"></i>
                            加入会议
                        </button>
                    </div>
                </div>
            </div>
        </c:if>
        <c:if test="${! empty user.room}">
            <div class="layui-col-md12">
                <div class="layui-card">
                    <div class="layui-card-header">正在参加会议【${user.room.title}】</div>
                    <div class="layui-card-body" style="text-align: center;">
                        <c:if test="${user.room.creator==user}">
                            <button class="layui-btn-primary layui-btn layui-btn-lg" id="endChat">
                                <i class="layui-icon layui-icon-close"></i>
                                解散会议
                            </button>
                        </c:if>
                        <c:if test="${user.room.creator!=user}">
                            <button class="layui-btn-primary layui-btn layui-btn-lg" id="exitRoom">
                                <i class="layui-icon layui-icon-close"></i>
                                退出会议
                            </button>
                        </c:if>
                    </div>
                </div>
            </div>
        </c:if>
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-header">正在进行的会议</div>
                <div class="layui-card-body">
                    <table class="layui-table">
                        <colgroup>
                            <col width="300">
                            <col width="200">
                            <col width="100">
                            <col width="100">
                            <col>
                        </colgroup>
                        <thead>
                        <tr>
                            <th>会议名称</th>
                            <th>会议ID</th>
                            <th>主持人</th>
                            <th>人数</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${rooms}" var="room">
                                <tr>
                                    <td>${room.title}</td>
                                    <td>${room.roomName}</td>
                                    <td>${room.creator.name}</td>
                                    <td>${fn:length(room.userList)}</td>
                                    <td>
                                        <button roomName="${room.roomName}" hasPwd="${! empty room.pwd}"
                                            class="layui-btn <c:if test="${user.room!=null&&user.room==room}">layui-btn-disabled</c:if>"
                                            id="joinRoom2">
                                            <i class="layui-icon layui-icon-add-circle-fine"></i>
                                            加入会议
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${fn:length(rooms)==0}">
                                    <td colspan="5" align="center">当前无会议</td>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $(function () {
        webos.execute("index",{

        });
    })
</script>
</body>
</html>
