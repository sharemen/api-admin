<!DOCTYPE html>
<html>
<head>
  	<title>API管理平台</title>
  	<#import "common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/iCheck/square/blue.css">
</head>
<body class="hold-transition login-page">
	<div class="login-box">
		<div class="login-logo">
			<a><b>APIAdmin</b></a>
		</div>
		
			<div class="login-box-body">
			<form id="loginForm" method="post" >
				<p class="login-box-msg">API管理平台</p>
				<div class="form-group has-feedback">
	            	<input type="text" name="userName" class="form-control" placeholder="请输入登陆账号" value="" maxlength="50" >
	            	<span class="glyphicon glyphicon-envelope form-control-feedback"></span>
				</div>
	          	<div class="form-group has-feedback">
	            	<input type="password" name="password" class="form-control" placeholder="请输入登陆密码" value="" maxlength="50" >
	            	<span class="glyphicon glyphicon-lock form-control-feedback"></span>
	          	</div>
				<div class="row">
					<div class="col-xs-8">
		              	<div class="checkbox icheck">
		                	<label>
		                  		<input type="checkbox" name="ifRemember" > Remember Me
		                	</label>
						</div>
		            </div><!-- /.col -->
					<button type="submit" class="btn btn-primary btn-block btn-flat">登陆</button>
				</div>
				</form>
				<div class="row">
				<form id="guestLoginForm" method="post" >
						<button type="button" id="guestLogin" class="btn btn-primary btn-block btn-flat">访客访问</button>
				</form>
				</div>
			</div>
	
		
		
		
	</div>
<@netCommon.commonScript />
<script src="${request.contextPath}/static/adminlte/plugins/iCheck/icheck.min.js"></script>
<script src="${request.contextPath}/static/js/login.1.js"></script>

</body>
</html>
