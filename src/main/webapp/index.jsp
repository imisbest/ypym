<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	request.setAttribute("path", request.getContextPath());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>APlus云盘-登录</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="Eminent Login Form Widget Responsive, Login form web template,Flat Pricing tables,Flat Drop downs  Sign up Web Templates, Flat Web Templates, Login signup Responsive web template, Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyEricsson, Motorola web design" />
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
<!-- font files -->
<!--<link href='//fonts.googleapis.com/css?family=Raleway:400,100,200,300,500,600,700,800,900' rel='stylesheet' type='text/css'>-->
<!--<link href='//fonts.googleapis.com/css?family=Poiret+One' rel='stylesheet' type='text/css'>-->
<!-- /font files -->
<!-- css files -->
<link href="css/style.css" rel='stylesheet' type='text/css' media="all" />
<!-- /css files -->
</head>
<body>
<script type="text/javascript">
	var sysMsg = "${sysMsg}"
	if(sysMsg != "")
		alert(sysMsg);
</script>
<h1>云盘</h1>
<div class="form-w3ls">
    <ul class="tab-group cl-effect-4">
        <li class="tab active"><a href="#signin-agile">登录</a></li>
		<li class="tab"><a href="#signup-agile">注册</a></li>        
    </ul>
    <div class="tab-content">
        <div id="signin-agile">   
			<form action="${path}/lr/login" method="post">
				<p class="header">用户名</p>
				<input type="text" name="username" value="" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = '';}">
				<p class="header">密码</p>
				<input type="password" name="password" value="" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = '';}">
				<input type="submit" class="sign-in" value="登录">
			</form>
		</div>
		<div id="signup-agile">   
			<form action="${path}/lr/register" method="post">
				
				<p class="header">用户名(登录名)</p>
				<input id="username" type="text" name="username" value="" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = '';}">
				
				<p class="header">邮箱</p>
				<input id="email" type="text" name="email" value="" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = '';}">
				
				<p class="header">密码</p>
				<input id="password" type="password" name="password" value="" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = '';}">
				
				<p class="header">确认密码</p>
				<input id="rePassword" type="password" name="rePassword" value="" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = '';}">
				
				<input type="submit" class="register" value="注册" />
			</form>
		</div> 
    </div>
</div> 
<p class="copyright">© 2017-2018 APlus. All Rights Reserved | Design by APlus</p>
<script src='js/jquery.min.js'></script>
<script src="js/index.js"></script>
<script type="text/javascript">
</script>
</body>
</html>