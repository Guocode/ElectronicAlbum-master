<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh">
<head>
<%@	taglib prefix="s" uri="/struts-tags" %>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>登陆注册界面</title>
	<link rel="stylesheet" type="text/css" href="css/normalize.css" />
	<link rel="stylesheet" type="text/css" href="css/htmleaf-demo.css">
	<style type="text/css">
		.login-page {
		  width: 360px;
		  padding: 8% 0 0;
		  margin: auto;
		}
		.form {
		  position: relative;
		  z-index: 1;
		  background: #FFFFFF;
		  max-width: 360px;
		  margin: 0 auto 100px;
		  padding: 45px;
		  text-align: center;
		  box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.2), 0 5px 5px 0 rgba(0, 0, 0, 0.24);
		}
		.form input {
		  font-family: "Roboto", sans-serif;
		  outline: 0;
		  background: #f2f2f2;
		  width: 100%;
		  border: 0;
		  margin: 0 0 15px;
		  padding: 15px;
		  box-sizing: border-box;
		  font-size: 14px;
		}
		.form .button {
		  font-family: "Microsoft YaHei","Roboto", sans-serif;
		  text-transform: uppercase;
		  outline: 0;
		  background: #4CAF50;
		  width: 100%;
		  border: 0;
		  padding: 15px;
		  color: #FFFFFF;
		  font-size: 14px;
		  -webkit-transition: all 0.3 ease;
		  transition: all 0.3 ease;
		  cursor: pointer;
		}
		.form button:hover,.form button:active,.form button:focus {
		  background: #43A047;
		}
		.form .message {
		  margin: 15px 0 0;
		  color: #b3b3b3;
		  font-size: 12px;
		}
		.form .message a {
		  color: #4CAF50;
		  text-decoration: none;
		}
		.form .register-form {
		  display: none;
		}
		.container {
		  position: relative;
		  z-index: 1;
		  max-width: 300px;
		  margin: 0 auto;
		}
		.container:before, .container:after {
		  content: "";
		  display: block;
		  clear: both;
		}
		.container .info {
		  margin: 50px auto;
		  text-align: center;
		}
		.container .info h1 {
		  margin: 0 0 15px;
		  padding: 0;
		  font-size: 36px;
		  font-weight: 300;
		  color: #1a1a1a;
		}
		.container .info span {
		  color: #4d4d4d;
		  font-size: 12px;
		}
		.container .info span a {
		  color: #000000;
		  text-decoration: none;
		}
		.container .info span .fa {
		  color: #EF3B3A;
		}
		body {
		  background: #9BCD9B; /* fallback for old browsers */
		  /**
		  background: -webkit-linear-gradient(right, #76b852, #8DC26F);
		  background: -moz-linear-gradient(right, #76b852, #8DC26F);
		  background: -o-linear-gradient(right, #76b852, #8DC26F);
		  background: linear-gradient(to left, #76b852, #8DC26F);
		  */
		  font-family: "Roboto", sans-serif;
		  -webkit-font-smoothing: antialiased;
		  -moz-osx-font-smoothing: grayscale;      
		}
		.shake_effect{
		 	-webkit-animation-name: shake;
  			animation-name: shake;
  			-webkit-animation-duration: 1s;
  			animation-duration: 1s;
		}
		@-webkit-keyframes shake {
		  from, to {
		    -webkit-transform: translate3d(0, 0, 0);
		    transform: translate3d(0, 0, 0);
		  }

		  10%, 30%, 50%, 70%, 90% {
		    -webkit-transform: translate3d(-10px, 0, 0);
		    transform: translate3d(-10px, 0, 0);
		  }

		  20%, 40%, 60%, 80% {
		    -webkit-transform: translate3d(10px, 0, 0);
		    transform: translate3d(10px, 0, 0);
		  }
		}

		@keyframes shake {
		  from, to {
		    -webkit-transform: translate3d(0, 0, 0);
		    transform: translate3d(0, 0, 0);
		  }
		  10%, 30%, 50%, 70%, 90% {
		    -webkit-transform: translate3d(-10px, 0, 0);
		    transform: translate3d(-10px, 0, 0);
		  }

		  20%, 40%, 60%, 80% {
		    -webkit-transform: translate3d(10px, 0, 0);
		    transform: translate3d(10px, 0, 0);
		  }
		}
		p.center{
			color: #fff;font-family: "Microsoft YaHei";
		}
	</style>
	<!--[if IE]>
		<script src="http://libs.useso.com/js/html5shiv/3.7/html5shiv.min.js"></script>
	<![endif]-->
</head>
<body>

		<div id="wrapper" class="login-page">
		  <div id="login_form" class="form">
		  
		  	<s:form action="user_login" class="login-form">
			<div>
			<font color="red">
				<s:fielderror/>
			</font>
			</div>	
	        <s:textfield name="loginName" size="20" tabindex="1" id="loginNameInput" placeholder="请输入用户名"></s:textfield>
	        <input name="password" type="password" placeholder="请输入密码">
	        <br>
	        <s:submit value="登录" class="button" id="login"></s:submit>
		      <p class="message">还没有账户? <a href="#">立刻创建</a></p>
		    </s:form>
		  
		    <s:form action="user_add" class="register-form">
		      	<tr>
					<td><s:textfield name="loginName" cssClass="InputStyle" placeholder="请输入账号"/> </td>
				</tr>
				<tr>
					<td><s:textfield name="password" cssClass="InputStyle" placeholder="请输入密码"/></td>
				</tr>
				
				<tr>
					<td><s:textfield name="name" cssClass="InputStyle" placeholder="请输入姓名"/> </td>
				</tr>

				<tr>
					<td><s:textfield name="phoneNumber" cssClass="InputStyle" placeholder="请输入电话"/></td>
				</tr>
				<tr>
					<td><s:textfield name="email" cssClass="InputStyle" placeholder="请输入邮箱"/></td>
				</tr>
		      <s:submit value="立即注册" class="button"></s:submit>
		      <p class="message">已经有了一个账户? <a href="#">立刻登录</a></p>
		    </s:form>
		    

		    
		  </div>
		</div>

	
	<script src="https://cdn.bootcss.com/jquery/2.1.1/jquery.min.js"></script>
	<script>window.jQuery || document.write('<script src="js/jquery-2.1.1.min.js"><\/script>')</script>
	<script type="text/javascript">
	$(function(){
		$('.message a').click(function () {
		    $('form').animate({
		        height: 'toggle',
		        opacity: 'toggle'
		    }, 'slow');
		});
	})
	</script>
</body>
</html>

