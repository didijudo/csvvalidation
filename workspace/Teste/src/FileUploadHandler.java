import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;



/**
 * Servlet to handle File upload request from Client
 * @author
 */
public class FileUploadHandler extends HttpServlet {
    //local onde sera salvo o arquivo no servidor
		//private final String UPLOAD_DIRECTORY = "/opt/apache-tomcat-7.0.67/arquivos";
	
	//path para uso de testes em máuina local 
	private final String UPLOAD_DIRECTORY = "D:\\uploads";
	
	private String pathArquivoCsv, pathArquivoMetadados, pathArquivo, nomeArquivo, nomeArquivoCsv, extensaoArquivo = "";
	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      
        if(ServletFileUpload.isMultipartContent(request)){
            try {
            		List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
	              
	                for(FileItem item : multiparts){
	                	if(!item.isFormField()){
	                        nomeArquivo = new File(item.getName()).getName();
	                        pathArquivo = UPLOAD_DIRECTORY + File.separator + nomeArquivo;
	                        File arquivo = new File(pathArquivo); 
	                        item.write(arquivo);
	                        
	                        extensaoArquivo = nomeArquivo.substring(nomeArquivo.lastIndexOf(".") + 1);
	                        if (extensaoArquivo.compareToIgnoreCase("csv") == 0){
	                        	this.pathArquivoCsv = pathArquivo;
	                        	nomeArquivoCsv = nomeArquivo;
	                        } else {
	                        	this.pathArquivoMetadados = pathArquivo;
	                        }
	                    
	                         
	                    }
	                }
	           
	           ArrayList<String> listaCL = returnResultValidacaoJson();
	 	       request.setAttribute("AtributoListaResultados", montarHTMLresultJson(listaCL));     
            } catch (Exception ex) {
               request.setAttribute("message", "Falha no upload do arquivo devido ao erro " + ex);
            }          
         
        }else{
            request.setAttribute("message", "Problema no request do Servlet.");
        }
    
        request.getRequestDispatcher("/result.jsp").forward(request, response);
     
    }
    
    private ArrayList<String> returnResultValidacaoJson(){
       	CoreValidation objCoreValidation = new CoreValidation(pathArquivoMetadados, pathArquivoCsv, nomeArquivoCsv);
    	
    	return objCoreValidation.doValidation();
    	
    }
    	
    private String montarHTMLresultJson(ArrayList<String> lst){
    	int i; String html = "";
    	for (i = 0; i < lst.size(); i++) {
			html += "<div class=\"mensagemerro\"> Info: " + lst.get(i).toString() + "</div>";
		}
    	return html;
    }
    
  
}