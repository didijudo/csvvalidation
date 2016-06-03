 <%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <%@ page import="java.util.*" %>
 
 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-type" content="text/html;charset=ISO-8859-1" />
	<title>Resultado da validação</title>
	<link href="CsvValidator.css" rel="stylesheet" type="text/css" />
</head>

<body>

<div id="todo">
	<div id="topo">
		<div id="logo">CSVValidation</div>
			
	</div>
    <div id="rodapesuperior">
		Resultado da validação
	</div>

	<div id="meio">
		<div id="esquerda">
		</div>

		<div id="miolo">
			<%
				out.println((String)request.getAttribute("AtributoListaResultados"));
	    		
	    	%>
		</div>

		<div id="direita">
		</div>
		<div style="clear: both;"></div>
	</div>
    <div id="rodapeinferior">
		<div id="rodape_direita">
			Todos os direitos reservados Universidade Federal de Pernambuco - UFPE
		</div>
	</div>
</div>

</body>
</html>
 