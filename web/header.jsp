<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link rel="stylesheet" href="res/css/global.css">
<div class="fly-header layui-bg-black">
    <div class="layui-container">
        <ul class="layui-nav layui-hide-xs">
            <li class="layui-nav-item layui-this">
                <a href="/"><i class="iconfont icon-jiaoliu"></i>视频会议系统</a>
            </li>
        </ul>

        <ul class="layui-nav fly-nav-user">
            <!-- 未登入的状态 -->
            <c:if test="${empty user}">
                <li class="layui-nav-item">
                    <a class="iconfont icon-touxiang layui-hide-xs" href="login.jsp"></a>
                </li>
                <li class="layui-nav-item">
                    <a href="login.jsp">登入</a>
                </li>
                <li class="layui-nav-item">
                    <a href="reg.jsp">注册</a>
                </li>
            </c:if>
            <c:if test="${!empty user}">
                <li class="layui-nav-item">
                    <a class="iconfont icon-touxiang layui-hide-xs" href="javascript:;"></a>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;">${user.name}</a>
                </li>
                <li class="layui-nav-item">
                    <a href="/ActionServlet?method=logout">退出</a>
                </li>
            </c:if>
        </ul>
    </div>
</div>
