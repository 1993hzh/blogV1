<%@ page language="java" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="true">
	<head>
		<title>Exception.jsp</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<STYLE type=text/css>
BODY {
	FONT-SIZE: 9pt;
	COLOR: #842b00;
	LINE-HEIGHT: 16pt;
	FONT-FAMILY: "Tahoma", "宋体";
	TEXT-DECORATION: none
}

TABLE {
	FONT-SIZE: 9pt;
	COLOR: #842b00;
	LINE-HEIGHT: 16pt;
	FONT-FAMILY: "Tahoma", "宋体";
	TEXT-DECORATION: none
}

TD {
	FONT-SIZE: 9pt;
	COLOR: #842b00;
	LINE-HEIGHT: 16pt;
	FONT-FAMILY: "Tahoma", "宋体";
	TEXT-DECORATION: none
}

BODY {
	SCROLLBAR-HIGHLIGHT-COLOR: buttonface;
	SCROLLBAR-SHADOW-COLOR: buttonface;
	SCROLLBAR-3DLIGHT-COLOR: buttonhighlight;
	SCROLLBAR-TRACK-COLOR: #eeeeee;
	BACKGROUND-COLOR: #ffffff
}

A {
	FONT-SIZE: 9pt;
	COLOR: #842b00;
	LINE-HEIGHT: 16pt;
	FONT-FAMILY: "Tahoma", "宋体";
	TEXT-DECORATION: none
}

A:hover {
	FONT-SIZE: 9pt;
	COLOR: #0188d2;
	LINE-HEIGHT: 16pt;
	FONT-FAMILY: "Tahoma", "宋体";
	TEXT-DECORATION: underline
}

H1 {
	FONT-SIZE: 9pt;
	FONT-FAMILY: "Tahoma", "宋体"
}
</STYLE>
	</head>
	<BODY topMargin=20>
		<TABLE cellSpacing=0 width=600 align=center border=0 cepadding="0">
			<TBODY>
				<TR colspan="2">
					<TD vAlign=top align=middle>
						<IMG height=100 src="images/404.jpg" width=100 border=0>
						<TD>
							<TD>
								<!--------System Return Begin------------>
								<H1>
									系统错误
								</H1>
								<s:actionerror />
								<p>
									<s:property value="%{exception.message}" />
								</p>

								<HR noShade SIZE=0>

								<P>
									☉ 请尝试以下操作：
								</P>
								<UL>


									<LI>
										单击
										<A href="javascript:history.back(1)"><FONT color=#ff0000>后退</FONT>
										</A>
								</UL>
								<HR noShade SIZE=0>

								<P>
									☉ 如果您有任何疑问、意见、建议、咨询，请联系管理员 QQ:823287929
									</A>
									<BR>
									&nbsp;&nbsp;&nbsp;
									<BR>
								</P>
								<!------------End this!--------------->
							</TD>
				</TR>
			</TBODY>
		</TABLE>
	</BODY>
</html>
