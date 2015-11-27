<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>豆妈私享库</title>
</head>
<body>
<table width="800"><tr>
<td>
<form id="0" method="post" action="upload" enctype="multipart/form-data" name="form0">
产品<input style="vertical-align:middle;" type="text" name="product"><br/>
数量<input style="vertical-align:middle;" type="text" name="quantity"><br/>
买入<input style="vertical-align:middle;" type="text" name="buy"> 
<select id="currency" name="currency" >
<option value="0">美元</option>
<option value="1">人民币</option>
<option value="2">英镑</option>
<option value="3">日元</option>
<option value="4">港币</option>
<option value="5">欧元</option>
</select><br/>
图片<input style="vertical-align:middle;" type="file" name="photo"><br/>
网站<input style="vertical-align:middle;" type="text" name="website"><br/>

<input style="vertical-align:middle;" type="submit" name="enter" value="录入"><br/>

</form>
</td>
<td valign="bottom">
<form id="1" method="post" action="upload" name="form_sale">
产品<input style="vertical-align:middle;" type="text" name="product_sell"><br/>
数量<input style="vertical-align:middle;" type="text" name="quantity_sell"><br/>
价格<input style="vertical-align:middle;" type="text" name="price_sell"><br/>
备注<input style="vertical-align:middle;" type="text" name="comment"><br/>
<input style="vertical-align:middle;" type="submit" name="enter" value="卖出"><br/>

</form>
</td>
</tr>
</table>
<hr/>
<form id="2" method="post" action="db" name="form1">
<table width="800"><tr>
<td>
产品名<input type="text" name="product_query"><br/>
只显示有库存的商品<input type="checkbox" name="check"><br/>
</td>
<td>
<% Object o = request.getAttribute("profit");
String profit="0";
if (o!=null) profit = (String)o;%>
当前收益：<%=profit %>元
</td>
</tr>
</table>

<input type="submit" name="db" value="查询商品，不填查所有"/>
<%
List<List<String>> rows = (List<List<String>>)request.getAttribute("tables");

if (rows != null) {
	%>
	<table border="0">
	<tr style="background-color:#AFEEEE;width:100px;text-align:center;">
	<td width="150px" style="word-wrap:break-word;">商品</td><td>图片</td><td>来源</td><td>进货日</td><td>买入价</td>
	<td>买入数量</td><td>已售数量</td>
	<td>收益</td><td>库存</td><td>备注</td>
	</tr>
	<%
	
	for (List<String> row: rows) {
		%>
		<tr>
		<%
		int i = 0;
		for (String cell: row) {
			if (i == 1) {
			%>
			<td align="center"><img src=<%="image/"+cell %> width="125" height="125"></td>
			<% } else { %>
			<td align="center" style="width:100px;text-align:top;"><%=cell %></td>
		<%}
			i++;
		}%>
		</tr>
	<%}%>
	</table>
<%}%>
</form>

<form id="4" method="post" name="form3">

</form>
</body>
</html>