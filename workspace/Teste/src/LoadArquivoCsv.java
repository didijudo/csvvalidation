
import java.io.FileNotFoundException; 
import java.io.FileReader;
import java.io.BufferedReader;



public class LoadArquivoCsv { 
	private FileReader fileReader; 
	private BufferedReader bufferReader;
	
	public LoadArquivoCsv(String pathCsv){
				
		try {
				this.fileReader = new FileReader(pathCsv);
				this.bufferReader = new BufferedReader(fileReader);
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
	
	public BufferedReader getBufferArquivoCsv(){
		return this.bufferReader;
	}
}
