<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form id="" method="post" action="upload" enctype="multipart/form-data" name="form0">
产品<input style="vertical-align:middle;" type="text" name="product"><br/>
标价<input style="vertical-align:middle;" type="text" name="price"><br/>
数量<input style="vertical-align:middle;" type="text" name="quantity"><br/>
买入<input style="vertical-align:middle;" type="text" name="buy"> 
<select id="currency" name="currency" >
<option value="0">人民币</option>
<option value="1">美元</option>
<option value="2">英镑</option>
<option value="3">日元</option>
</select><br/>
卖出<input style="vertical-align:middle;" type="text" name="sell"><br/>
图片<input style="vertical-align:middle;" type="file" name="photo"><br/>
网站<input style="vertical-align:middle;" type="text" name="website"><br/>

<input style="vertical-align:middle;" type="submit" name="enter" value="录入"><br/>

</form>
<form id="upload" method="post" action="db" name="form1">

<input type="submit" name="db" value="所有商品详情"/>

</form>

<form>
<%
List<List<String>> rows = (List<List<String>>)request.getAttribute("tables");

if (rows != null) {
	out.println(request.getSession().getServletContext().getRealPath("/") );
	%>
	<table border="1" cellpadding="0" cellspacing="0">
	<tr>
	<td>商品</td><td>标价</td><td>数量</td><td>买入价</td>
	<td>卖出价格</td><td>图片</td><td>来源</td>
	<td>进货日</td><td>收益</td><td>库存</td>
	</tr>
	<%
	
	for (List<String> row: rows) {
		%>
		<tr>
		<%
		int i = 0;
		for (String cell: row) {
			if (i == 5) {
			%>
			<td><img src=<%="image/"+cell %> width="115" height="115"></td>
			<% } else { %>
			<td><%=cell %></td>
		<%}
			i++;
		}%>
		</tr><br/>
	<%}%>
	</table>
<%}%>
</form>
</body>
</html>