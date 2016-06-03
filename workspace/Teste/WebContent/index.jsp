<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-type" content="text/html;charset=ISO-8859-1" />
	<title>Csv Validator</title>
	<link href="CsvValidator.css" rel="stylesheet" type="text/css" />
</head>

<body>

<div id="todo">
	<div id="topo">
		<div id="logo">CSVValidation</div>
			
	</div>
    <div id="espacoembranco">
	</div>
    <div id="rodapesuperior2">
		Valide seus arquivos CSV com o CSVValidation
	</div>

	<div id="meio2">
		<div id="esquerda2">
        	Produzir um arquivo CSV é fácil, mas gerar um arquivo válido estruturalmente e que possa ser interpretado por computadores é mais difícil. <br><br> CSV Validation ajuda você a validar se seu arquivo CSV é válido de acordo com restrições definidas no arquivo de metadados JSON. <br><br> Para realizar a validação, faça upload do arquivo CSV e metadados. O sistema irá processar e retornar o resultado da validação.
		</div>
		
        <div id="divisoria"></div>
		
        <div id="miolo2">
	        <div class="secao_miolo2"></div>
	        <form action="upload" method="post" enctype="multipart/form-data">
	        	<div class="secao_miolo2">Informe o local do arquivo CSV, do arquivo de Metadados, e clique no botão UPLOAD. </div>
				<div class="secao_miolo2">Selecione o arquivo CSV: <input type="file" name="file" /> </div>
				<div class="secao_miolo2">Selecione o arquivo de Metadados: <input type="file" name="file" /> </div>
				<div class="botao_upload"><input type="submit" value="ULPLOAD" /></div>
			</form>	
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
