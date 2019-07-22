<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="layui/layui/css/layui.css"/>
<script src="layui/layui/layui.all.js"></script>
<script src="jquery-1.8.2.js"></script>
<script src="base.js"></script>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script>
    webos.BASEPATH = "${ctx}";
</script>
