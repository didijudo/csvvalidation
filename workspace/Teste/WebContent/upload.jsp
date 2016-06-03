<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>File Upload JSP</title>
    </head>
 
    <body> 
        <div>
        	<h1>File Upload Example in JSP and Servlet - Java web application</h1>
            <h3> Choose File to Upload in Server </h3>
            <h3> Pagina 1 </h3>
            <form action="upload" method="post" enctype="multipart/form-data">
                <input type="file" name="file" />
                <input type="submit" value="upload" />
            </form>          
        </div>
      
    </body>
</html>