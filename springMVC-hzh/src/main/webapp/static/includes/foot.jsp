<%@ page pageEncoding="UTF-8"%>
<%-- <jsp:include page="/includes/include.jsp"/> --%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="now" class="java.util.Date" />


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<div id="footer">
	<div class="container">
		<hr class="index_footer_division" />
		<!-- 		<p class="text-muted" style="text-align:center;">
			建议使用FireFox，IE10以上版本，Chrome等现代浏览器。1024*768以上浏览</p> -->
		<p class="text-muted" style="text-align:center;">
			<!-- 站长统计 -->
			<script type="text/javascript">
				var cnzz_protocol = (("https:" == document.location.protocol) ? " https://"
						: " http://");
				document
						.write(unescape("%3Cspan id='cnzz_stat_icon_1000438327'%3E%3C/span%3E%3Cscript src='"
								+ cnzz_protocol
								+ "v1.cnzz.com/z_stat.php%3Fid%3D1000438327%26online%3D1%26show%3Dline' type='text/javascript'%3E%3C/script%3E"));
			</script>
			<%-- 在线人数：<%=session.getServletContext().getAttribute("onlineNum") %>&nbsp;&nbsp;&nbsp;&nbsp;
			总访问量：<%=session.getServletContext().getAttribute("totalNum") %> --%>
		</p>
		<p class="text-muted" style="text-align:center;">
			Copyright&copy;1993-
			<fmt:formatDate value="${now}" type="both" dateStyle="long"
				pattern="yyyy" />
			葫芦娃&nbsp;&nbsp;&nbsp;&nbsp;Designed
			by&nbsp;葫芦娃&nbsp;&nbsp;&nbsp;&nbsp; <img
				src="static/img/poweredby.png"></img>
		</p>
	</div>
</div>
<!-- <footer style="padding-top:10px">
	<hr class="index_footer_division" />
	<ul class="footer_list clearfix main_center" style="list-style-type:none;">
		<li class="center" style="margin:-10px;">Copyright &copy;1993-20xx 葫芦娃</li>
	</ul>
</footer> -->