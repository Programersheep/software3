<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; utf-8">
	<title>南航</title>
	<link rel="stylesheet" type="text/css" href="Index.css">
</head>
<body>
<div class="Head">南航行李资费查询
</div>
<div class="Main">
<form action="Search" method="post" id="form1">
<div class="Left">
<div class="Select">
<label>航线选择</label>
</div>
<select class="Select" name="S_Line">
<option>国内航线</option>
<option>国际航线一</option>
<option>国际航线二</option>
<option>国际航线三</option>
<option>国际航线四</option>
<option>涉及韩国始发与中国间的航线</option>
<option>涉及兰州/乌鲁木齐与迪拜之间的航线</option>
</select>
<div class="Select">
<label >舱位选择</label>
</div>
<select class="Select" name="S_Cabin">
<option>头等舱</option>
<option>公务舱</option>
<option>明珠经济舱</option>
<option>经济舱</option>

</select>
<div class="Select">
<label>旅客类型选择</label>
</div>
<select class="Select" name="S_Type">
<option>普通旅客</option>
<option>南航明珠金卡会员或天合联盟超级精英</option>
<option>南航明珠银卡会员或天合联盟精英</option>
<option>留学生、劳务、海员</option>
<option>儿童，占座婴儿</option>
<option>不占座婴儿</option>
</select>
<%
	String s=(String)request.getAttribute("Info");
    if(s!=null){
	String res[]=s.split("@");
	for(int i=0;i<res.length;i++){
		out.print("<div class='Select1'>");
		out.print("<p>");
		out.print("<input type='checkbox' name='Baggage1' value='");
		out.print(i);
		out.print("' checked='checked'>");
		out.print(res[i]);
		out.print("</p></div>");
	}
  }
%>

<%
	String s1=(String)request.getAttribute("Money");
	if(s1!=null){
		int s2=Integer.parseInt(s1);
		if(s2==-2){
			out.print("<script type='text/javascript'>window.alert('婴儿的行李仅限10KG一件，超出部分请算入陪同成人名下');</script>");
		}
		else if(s2==-1){
			out.print("<script type='text/javascript'>window.alert('所携带行李超出最大限额规定，请重新调整后查询');</script>");
		}
		else if(s2==-3){
			out.print("<script type='text/javascript'>window.alert('请添加行李信息');</script>");
		}
		else{
		out.print("<div class='Result1'>");
		out.print("<label>价格：");
		out.print(s2);
		out.print("</label></div>");
	}
	}
%>

<div class="Select">
<label>机票价格</label>
</div>
<textarea class="Text" placeholder="票价：单位 元" name="Money" required></textarea>
<div class="Button1"><button type="submit" name="button" value="Search">查询价格</button></div>
</div>
</form>


<div class="Right">
<div class="Select1">
<label>行李参数设置</label>
</div>
<form action="Search" method="post" id="form2">
<input type="text" class="Text" placeholder="长:单位CM" name="Long" required>
<input type="text" class="Text" placeholder="宽：单位CM" name="Width" required>
<input type="text" class="Text" placeholder="高：单位CM" name="Height" required>

<textarea class="Text" placeholder="行李重量:单位KG" name="Weight" required></textarea>
<div class="Button1"><button type="submit"  name="button" value="ADD">添加行李</button></div>
</form>

<form action="Search" method="post" id="form3">
<div class="Button1"><button type="submit" name="button" value="Refresh">清空行李数据</button></div>
</form>
</div>

</div>
</body>
</html>