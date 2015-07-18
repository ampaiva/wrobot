<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.ampaiva.wrobotserver.Proxy"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>WRobot Server</title>
<script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script>
	window.jQuery
			|| document
					.write(unescape('%3Cscript src="http://jquery.com/jquery-wp-content/themes/jquery/js/jquery-1.10.2.min.js"%3E%3C/script%3E'))
</script>
</head>
<body style="width: 80%; padding: 0; margin-left: 10%">
	<div style="width: 80%; padding: 0">
		<a href="http://www.cobrarr.com.br/"> <img src="cobrarr.png"
			width="20%" align="left" alt="Cobrarr" />
		</a>
	</div>
	<br>
	<div
		style="width: 100%; height: 50px; background-color: #f0fbd2; display: table;">
		<div id="header" style="width: 80%; padding: 0; margin: 10">
			<h3><%=Proxy.startDate == null ? "Server not started" : "Server running since: " + Proxy.startDate%></h3>
			<table>
				<tr>
					<th>Operadores</th>
				</tr>
			<jsp:useBean id="op"
				class="com.ampaiva.wrobotserver.Operadores" scope="request"/>
				<c:forEach var="operador" items="${op.clientes}">
					<tr>
						<td>${operador}</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
	<br>
	<!--  table>
  <tr>
    <th><input type="button" id="tog" onclick="toggleLogs()" value="Start" /></th>
  </tr>
  <tr>
	<div id="log" style="width: 100%; padding: 0; margin: 0"></div>
  </tr>
</table>
-->


</body>
</html>