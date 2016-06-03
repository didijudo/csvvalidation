
//import java.awt.List;
import java.io.FileNotFoundException; 
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException; 
/*import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;*/

import org.json.*;

public class LoadArquivoJason { 
	
	private JSONObject jsonObject;
	private String erro, linha, nomeArquivoCsv, jsonString = ""; 
	//private ArrayList<String> listaReadJason = new ArrayList<String>();
	private Fields field;
	private FileReader fileReader; 
	private BufferedReader lerArq;
	
	public LoadArquivoJason(String pathMetadados, String nomeArquivoCsv){
				
		try { 			
				this.nomeArquivoCsv = nomeArquivoCsv;
				this.fileReader = new FileReader(pathMetadados);
				this.lerArq = new BufferedReader(fileReader);
			} 
		
		catch (FileNotFoundException e) 
			{ 
				e.printStackTrace();
			} 
		catch (Exception e) 
			{ 
				e.printStackTrace(); 
			}
	}
	
	public Fields getFieldJson(){
		try { 			
			
			this.linha = lerArq.readLine();
		
			while (linha != null) {  
				jsonString += linha; 
				linha = lerArq.readLine();
			}
			
			jsonObject = new JSONObject(jsonString);
			Fields field = new Fields(jsonObject,nomeArquivoCsv);
						
		    lerArq.close();
		    
		    return field;
		} 
	
	catch (FileNotFoundException e) 
		{ 
			e.printStackTrace(); 
		} 
	catch (IOException e) 
		{ 
			e.printStackTrace(); 
		} 
	catch (Exception e) 
		{ 
			e.printStackTrace(); 
		}
	
	return field;
	}
	
}