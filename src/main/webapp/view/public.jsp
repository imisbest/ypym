<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	request.setAttribute("path", request.getContextPath());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link href="${path}/css/file.css" rel='stylesheet' type='text/css'/>
	<title>文件分享区</title>
</head>
<body>
	<table>
		<tr>
			<th></th>
			<th>文件名</th>
			<th>文件类型</th>
			<th>大小</th>
			<th>上传时间</th>
			<th>上传用户</th>
		</tr>
		<c:forEach var="list" items="${requestScope.fileList }">
			<tr>
				<td><a href="${path}/fm/download?file=${list.id }&folder=${list.fatherFolder.id}"><img src="/cloud/images/download.png" alt="download" /></a></td>
				<td>${list.name }</td>
				<td>${list.fileType }</td>
				<td>${list.size } KB</td>
				<td>${list.uploadTime }</td>
				<td>${list.uploadUser.username }</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>