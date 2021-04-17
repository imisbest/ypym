<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	request.setAttribute("path", request.getContextPath());
%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link href="${path}/css/file.css" rel='stylesheet' type='text/css'/>
	<title>照片</title>
</head>
<body>
	<table>
		<tr>
			<th></th>
			<th>文件名</th>
			<th>文件类型</th>
			<th>大小</th>
			<th>上传时间</th>
			<th>删除</th>
		</tr>
		<c:forEach var="list" items="${requestScope.fileList }">
			<tr>
				<td><a href="download?file=${list.id }&folder=${requestScope.folderId}"><img src="/cloud/images/download.png" alt="download" /></a></td>
				<td>${list.name}</td>
				<td>${list.fileType }</td>
				<td>${list.size } KB</td>
				<td>${list.uploadTime }</td>
				<td><a href="delete?file=${list.id }&folder=${requestScope.folderId}"><img src="/cloud/images/delete.png" alt="delete" /></a></td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>