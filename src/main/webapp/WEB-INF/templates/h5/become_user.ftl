<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>成为会员</title>
		<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<link rel="stylesheet" href="${request.contextPath}/plugins/mui/css/mui.min.css">
		<style>
			p {
				text-indent: 22px;
			}
			span.mui-icon {
				font-size: 14px;
				color: #007aff;
				margin-left: -15px;
				padding-right: 10px;
			}
			.mui-content {
				padding: 10px;
			}
		</style>
	</head>

	<body>
		<header class="mui-bar mui-bar-nav">
			<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
			<h1 class="mui-title">成为会员</h1>
		</header>
		<footer class="mui-bar mui-bar-footer">
			<a href="#bottomPopover" class="mui-btn mui-btn-link mui-pull-right">菜单</a>
		</footer>
		<div class="mui-content">
			<div class="mui-content-padded">
				<p>popover（弹出菜单是）mobile App中常见的UI组件，在用户点击位置附近弹出悬浮菜单，向用户展示更多信息或提供快捷操作。</p>
				<p>popover最常用的两个位置：顶部导航栏右侧和底部工具栏右侧； 点击本页右上角的
					<span class="mui-icon mui-icon-bars"></span> 图标体验，接着点击本页面右下角的“菜单”按钮体验。
				</p>
				<p>除了页面顶部导航栏、底部工具栏固定位置之外，其它区域要使用弹出菜单，都要准确计算物理位置，从而实现弹出菜单的绝对定位； mui封装的手势事件中，可以获得点击位置，通过这些位置可实现任意区域的弹出菜单显示，点击如下按钮体验：
				</p>
				<h1 align="center">支付：10元</h1>
				<p align="center">
					<a href="${request.contextPath}/testpay/abc" class="mui-btn mui-btn-primary mui-btn-block mui-btn-outlined" style="padding: 5px 20px;">立即支付</a>
				</p>
			</div>
		</div>
		<script src="${request.contextPath}/plugins/mui/js/mui.min.js"></script>
		<script>
			mui.init({
				swipeBack: true //启用右滑关闭功能
			});
			
			var payObj = document.getElementById("pay_money");
			payObj.addEventListener("tap",function(){
				mui.alert('a');
			});
		</script>
	</body>

</html>