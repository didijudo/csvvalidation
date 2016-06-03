import java.io.BufferedReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.axis.types.NCName;
import org.apache.axis.types.NegativeInteger;
import org.apache.axis.types.NonNegativeInteger;
import org.apache.axis.types.NonPositiveInteger;
import org.apache.axis.types.NormalizedString;
import org.apache.axis.types.PositiveInteger;
import org.apache.axis.types.Token;
import org.apache.axis.types.UnsignedByte;
import org.apache.axis.types.UnsignedInt;
import org.apache.axis.types.UnsignedLong;
import org.apache.axis.types.UnsignedShort;


public class CoreValidation {
	private LoadArquivoJason objLoadArquivoJason;
	private LoadArquivoCsv objLoadArquivoCsv;
	private ArrayList<String> listResultValidation = new ArrayList<String>();
	private ArrayList<String> listColunasCSV = new ArrayList<String>();
	private int nrLinhaProcessada = 0;
	private String linhaArquivoCSV, linhaHeader, erro, regex;
	
	
	public CoreValidation(String pathMetadados, String pathCSV, String nomeArquivo){
		
		this.objLoadArquivoJason = new LoadArquivoJason(pathMetadados, nomeArquivo);
		this.objLoadArquivoCsv = new LoadArquivoCsv(pathCSV);
		
	}
	
	public ArrayList<String> doValidation(){
		
		Fields objField = objLoadArquivoJason.getFieldJson();
				
		//Guarda na lista as colunas lidas do arquivo de metadados. 
		ArrayList<Field> listColunasMetadados = objField.getArrayFields();
		
		//Validando se conseguiu ler as colunas do metadados.
		if (listColunasMetadados.size() == 0){
			erro = "Não foi possível carregar os tipos de dados das colunas do arquivo de metadados.";
			listResultValidation.add(erro);
			return listResultValidation;
		}
		
		//Recuperando as variaveis do Dialect Description do arquivo de metadados
		int headerRowCount = 0, skipColumns = 0, skipRows = 0;  
		boolean doubleQuote, header = false, skipBlankRows, skipInitialSpace;
		if (objField != null){
			headerRowCount = objField.getHeaderRowCount(); 
			skipColumns = objField.getSkipColumns();
			skipRows = objField.getSkipRows();
			header = objField.getHeader();
		}
		
		//Carregando os dados do arquivo CSV.
		BufferedReader bufferArqCsv = objLoadArquivoCsv.getBufferArquivoCsv();
		try{
			if (bufferArqCsv != null){
				if (skipRows > 0){ //Pulando as linhas definida no dialect skiprows
					for (int i=0; i < skipRows; i++){
						linhaArquivoCSV = bufferArqCsv.readLine();
						nrLinhaProcessada++;
					}
				}
			} else {
				erro = "Não foi possível carregar os dados do arquivo CSV.";
				listResultValidation.add(erro);
				return listResultValidation;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
		try {
			//Carregando as colunas do Header do arquivo CSV.
			if (objField.getHeader()){ 
				listColunasCSV = getListColunas(bufferArqCsv,headerRowCount,skipColumns, header);
			} else {
				erro = "Não foi possível carregar os dados do header do arquivo de metadados.";
				listResultValidation.add(erro);
				return listResultValidation;
			}
			
			//Agora ler o arquivo CSV e validar os tipos de dados...
			linhaArquivoCSV = bufferArqCsv.readLine(); //Lendo a primeira linha do CSV
			nrLinhaProcessada++;
			
			String arrLinhaArquivoCSV[], 
		       conteudoColuna,      //Conteudo da coluna a ser validado. 
		       tipoMetadados = "",  //Datatype da coluna definido na metadados.
		       nomeColunaMetadados, //Nome da coluna definida no metadados.
		       nomeColunaCSV,       //Nome da coluna definida no CSV.
			   format = "";         //Formato da coluna definido no metadados.
		       
			while (linhaArquivoCSV != null) {
				 				
				arrLinhaArquivoCSV = linhaArquivoCSV.split("\\,");
				
				for (int indexColunaCSV=0; indexColunaCSV < arrLinhaArquivoCSV.length; indexColunaCSV++){ //Percorrer cada coluna da linha do arquivo CSV e validar o tipo.
					conteudoColuna = arrLinhaArquivoCSV[indexColunaCSV];
					
					if (indexColunaCSV <= listColunasMetadados.size()){
						nomeColunaCSV = listColunasCSV.get(indexColunaCSV).toString();
						
						for (int indexColunaMetadados=0; indexColunaMetadados < listColunasMetadados.size(); indexColunaMetadados++){
							nomeColunaMetadados = listColunasMetadados.get(indexColunaMetadados).getName();
							if (nomeColunaCSV.compareToIgnoreCase(nomeColunaMetadados) == 0){
								tipoMetadados = listColunasMetadados.get(indexColunaMetadados).getDatatype();
								format = listColunasMetadados.get(indexColunaMetadados).getFormat();
							}
						}
						
						if (tipoMetadados.isEmpty()){
							erro = "Não foi possível localizar os metadados para a coluna " + nomeColunaCSV + ".";
							listResultValidation.add(erro);
						}
						
						//Chama o metodo de validacao de tipos
						compareType(tipoMetadados, conteudoColuna, nrLinhaProcessada, nomeColunaCSV, format);
						
						//Reset Variaveis
						tipoMetadados = "";
						nomeColunaCSV = "";
						nomeColunaMetadados = "";
						
					} else {
						erro = "Diferença do numero de coluna entre o header e os dados do arquivo CSV.";
						listResultValidation.add(erro);
						return listResultValidation;
					}	
				}
				linhaArquivoCSV = bufferArqCsv.readLine(); //Lendo a proxima linha do CSV
				nrLinhaProcessada++;
			}
			  
			
		}	
		catch (Exception e){
			e.printStackTrace();
		}
		
		if (listResultValidation.size() == 0){
			listResultValidation.add("Arquivo validado com sucesso. Nenhum erro foi detectado.");
		}
		
		return listResultValidation;
	}
	
	//Retorna uma lista com as colunas lidas do cabeçalho do arquivo CSV. 
	private ArrayList<String> getListColunas(BufferedReader bufferArqCsv, int headerRowCount, int skipColumns, boolean temHeader){
		
		ArrayList<String> listColunas = new ArrayList<String>();
		String linha = "";
		int ignorarColunas, nrlinhasCabecalho;
		ignorarColunas = skipColumns;
		nrlinhasCabecalho = headerRowCount;
		
		try{
			if ( (bufferArqCsv != null) && (temHeader)){   
				for (int i=0; i < nrlinhasCabecalho; i++){ 
					linha = bufferArqCsv.readLine();
					nrLinhaProcessada++;
					if (i == 0){
						linhaHeader = linha;
					}  else {
						linhaHeader += "," + linha;
					}
				}
			}	
			
			String[] arrColunasHeader = linhaHeader.split("\\,");
			for (int posCorrente=0; posCorrente < arrColunasHeader.length; posCorrente++){
				if (posCorrente >= ignorarColunas){ 
					if (!arrColunasHeader[posCorrente].toString().isEmpty()){
						listColunas.add(arrColunasHeader[posCorrente].toString()); 
					} else {
						throw new Exception(); 
					}
				}
			}
		} 
		catch (Exception e)
		{
			listResultValidation.add("Ocorreu erro durante o processamento do cabeçalho do arquivo.");
		}
		
		return listColunas;
	} 
	
	private void compareType(String tipoMetadados, String conteudoColuna, int nrLinhaProcessada, String nomeColunaProcessada, String format){
		
		if (!tipoMetadados.toString().isEmpty()){
			int index = tipoMetadados.indexOf("#");
			if( index > 0 ){ //Significa que o tipo usa o nome definido no w3c
				tipoMetadados = tipoMetadados.substring(index, tipoMetadados.length()-index);
			}
			if (tipoMetadados.equalsIgnoreCase("int")){
				if (!isInt(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("integer")){
				if (!isInteger(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("long")){
				if (!isLong(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("short")){
				if (!isShort(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("byte")){
				if (!isByte(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("boolean")){
				if (!isBoolean(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("double")){
				if (!isDouble(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("decimal")){
				if (!isDecimal(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("nonnegativeinteger")){
				if (!isNonNegativeInteger(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("positiveinteger")){
				if (!isPositiveInteger(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("unsignedlong")){
				if (!isUnsignedLong(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("unsignedint")){
				if (!isUnsignedInt(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("unsignedshort")){
				if (!isUnsignedShort(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("unsignedbyte")){
				if (!isUnsignedByte(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("nonpositiveinteger")){
				if (!isNonPositiveInteger(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("negativeinteger")){
				if (!isNegativeInteger(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("duration")){
				if (!isDuration(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("daytimeduration")){
				if (!isDayTimeDuration(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("yearmonthduration")){
				if (!isYearMonthDuration(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("gday")){
				if (!isgDay(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("gmonth")){
				if (!isgMonth(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("gmonthday")){
				if (!isgMonthDay(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("gyear")){
				if (!isgYear(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("gyearmonth")){
				if (!isgYearMonth(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("time")){
				if (!isTime(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("date")){
				if (!isDate(conteudoColuna,format)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("datetime")){
				if (!isDateTime(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("datetimestamp")){
				if (!isDateTimeStamp(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("hexbinary")){
				if (!isHexBinary(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("base64binary")){
				if (!isBase64Binary(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("anyuri")){
				if (!isAnyURI(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("normalizedstring")){
				if (!isDateTime(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("token")){
				if (!isToken(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("language")){
				if (!isLanguage(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("ncname")){
				if (!isNCName(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("nmtoken")){
				if (!isNMToken(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("name")){
				if (!isName(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			if (tipoMetadados.equalsIgnoreCase("string")){
				if (!isString(conteudoColuna)){
					listResultValidation.add("Erro na linha " + String.valueOf(nrLinhaProcessada) + ". Dados da coluna " + nomeColunaProcessada + " não é do tipo " + tipoMetadados); 
				}
			}
			
		}
	}
	private boolean isInt(String value) {  
	    try {  
	        Integer.parseInt(value);  
	        return true;  
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	private boolean isString(String value) {  
	    try {  
	        if (value instanceof String) {  
	        	return true;
	        } else {
	        	return false;
	        }		
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isInteger(String value) {  
	    try {  
	        regex = "[\\-+]?[0-9]+";
	    	Pattern pattern = Pattern.compile(regex); 
	    	Matcher matcher = pattern.matcher(value);
	    	
	    	if (matcher.matches()){
	        	return true;
	        } else {
	        	return false;
	        }	
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isLong(String v) {  
	    try {  
	        Long.parseLong(v);  
	        return true;  
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isShort(String v) {  
	    try {  
	        Short.parseShort(v);  
	        return true;  
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isByte(String v) {  
	    try {  
	        Byte.parseByte(v);  
	        return true;  
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isBoolean(String v) {  
	    try {  
	        if (Boolean.parseBoolean(v)) {  
	        	return true;
	        } else { return false; }	
	    } catch (Exception e) {  
	        return false;  
	    }  
	} 
	public boolean isDouble(String v) {  
	    try {  
	        Double.parseDouble(v);
	        return true;  
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isDecimal(String value) {  
	    try {  
	        regex = "(\\+|-)?([0-9]+(\\.[0-9]*)?|\\.[0-9]+)";
	        Pattern pattern = Pattern.compile(regex); 
	    	Matcher matcher = pattern.matcher(value);  
	        if (matcher.matches()){
	        	return true;
	        } else {
	        	return false;
	        }  
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isNonNegativeInteger(String v) {  
	    try {  
	    	NonNegativeInteger objNomNegativeInteger = new NonNegativeInteger(v);
	        return true;  
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isPositiveInteger(String v) {  
	    try {  
	    	PositiveInteger objPositiveInteger = new PositiveInteger(v);
	        return true;  
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isUnsignedLong(String v) {  
	    try {  
	    	UnsignedLong objUnsignedLong = new UnsignedLong(v);
	        return true;  
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isUnsignedInt(String v) {  
	    try {  
	    	UnsignedInt objUnsignedInt = new UnsignedInt(v);
	        return true;  
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isUnsignedShort(String v) {  
	    try {  
	    	UnsignedShort objUnsignedShort = new UnsignedShort(v);
	        return true;  
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isUnsignedByte(String v) {  
	    try {  
	    	UnsignedByte objUnsigneByte = new UnsignedByte(v);
	        return true;  
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isNonPositiveInteger(String v) {  
	    try {  
	    	NonPositiveInteger objNonPositiveInteger = new NonPositiveInteger(v);
	        return true;  
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isNegativeInteger(String v) {  
	    try {  
	    	NegativeInteger objNegativeInteger = new NegativeInteger(v);
	        return true;  
	    } catch (Exception e) {  
	        return false;  
	    }  
	} 
	public boolean isDuration(String value) {  
	    try {  
	        regex = "-?P((([0-9]+Y([0-9]+M)?([0-9]+D)?|([0-9]+M)([0-9]+D)?|([0-9]+D))(T(([0-9]+H)([0-9]+M)?([0-9]+(\\.[0-9]+)?S)?|([0-9]+M)([0-9]+(\\.[0-9]+)?S)?|([0-9]+(\\.[0-9]+)?S)))?)|(T(([0-9]+H)([0-9]+M)?([0-9]+(\\.[0-9]+)?S)?|([0-9]+M)([0-9]+(\\.[0-9]+)?S)?|([0-9]+(\\.[0-9]+)?S))))";
	    	Pattern pattern = Pattern.compile(regex); 
	    	Matcher matcher = pattern.matcher(value);  
	        if (matcher.matches()){
	        	return true;
	        } else {
	        	return false;
	        }	
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isDayTimeDuration(String value) {  //
		try {
			regex = "[-]?P([0-9]+D(T([0-9]+(H([0-9]+(M([0-9]+(\\.[0-9]*)?S|\\.[0-9]+S)?|(\\.[0-9]*)?S)|(\\.[0-9]*)?S)?|M([0-9]+(\\.[0-9]*)?S|\\.[0-9]+S)?|(\\.[0-9]*)?S)|\\.[0-9]+S))?|T([0-9]+(H([0-9]+(M([0-9]+(\\.[0-9]*)?S|\\.[0-9]+S)?|(\\.[0-9]*)?S)|(\\.[0-9]*)?S)?|M([0-9]+(\\.[0-9]*)?S|\\.[0-9]+S)?|(\\.[0-9]*)?S)|\\.[0-9]+S))";
			Pattern pattern = Pattern.compile(regex); 
	    	Matcher matcher = pattern.matcher(value);  
	        if (matcher.matches()){
	        	return true;
	        	
	        } else {
	        	return false;
	        }
	    } catch (Exception e) {  
	        return false;  
	    }   
	}
	public boolean isYearMonthDuration(String value) {  //
		try {
			regex = "-?P((([0-9]+Y)([0-9]+M)?)|([0-9]+M))";
			Pattern pattern = Pattern.compile(regex); 
	    	Matcher matcher = pattern.matcher(value);  
	        if (matcher.matches()){
	        	return true;
	        	
	        } else {
	        	return false;
	        }
	    } catch (Exception e) {  
	        return false;  
	    }   
	}
	public boolean isgDay(String value) {  
	    try {  
	        regex = "---(0[1-9]|[12][0-9]|3[01])(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?";
	    	Pattern pattern = Pattern.compile(regex); 
	    	Matcher matcher = pattern.matcher(value);  
	        if (matcher.matches()){
	        	return true;
	        } else {
	        	return false;
	        }	
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isgMonth(String value) {  
	    try {  
	        regex = "--(0[1-9]|1[0-2])(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?";
	    	Pattern pattern = Pattern.compile(regex); 
	    	Matcher matcher = pattern.matcher(value);  
	        if (matcher.matches()){
	        	return true;
	        } else {
	        	return false;
	        }	
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isgMonthDay(String value) {  
	    try {  
	    	regex = "--(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?";
	    	Pattern pattern = Pattern.compile(regex); 
	    	Matcher matcher = pattern.matcher(value);  
	        if (matcher.matches()){
	        	return true;
	        } else {
	        	return false;
	        }	
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isgYear(String value) {  
	    try {  
	        regex = "-?([1-9][0-9]{3,}|0[0-9]{3})(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?";
	    	Pattern pattern = Pattern.compile(regex); 
	    	Matcher matcher = pattern.matcher(value);  
	        if (matcher.matches()){
	        	return true;
	        } else {
	        	return false;
	        }	
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isgYearMonth(String value) {  
	    try {  
	        regex = "-?([1-9][0-9]{3,}|0[0-9]{3})-(0[1-9]|1[0-2])(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?";
	    	Pattern pattern = Pattern.compile(regex); 
	    	Matcher matcher = pattern.matcher(value);  	    	
	    	if (matcher.matches()){
	        	return true;
	        } else {
	        	return false;
	        }	
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isTime(String value) {  
	    try {  
	        regex = "(([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?|(24:00:00(\\.0+)?))(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?";
	    	Pattern pattern = Pattern.compile(regex); 
	    	Matcher matcher = pattern.matcher(value);  	    	
	    	if (matcher.matches()){
	        	return true;
	        } else {
	        	return false;
	        }	
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isDate(String value, String format) {  
	    try {  
	        if (!format.isEmpty()){
	        	DateFormat formater = new SimpleDateFormat(format);  
	        	Date date = (java.util.Date)formater.parse(value);
	        	return true;
	        } else {
		    	regex = "-?([1-9][0-9]{3,}|0[0-9]{3})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?";
		    	Pattern pattern = Pattern.compile(regex); 
		    	Matcher matcher = pattern.matcher(value);  	    	
		    	if (matcher.matches()){
		        	return true;
		        } else {
		        	return false;
		        }
	        }	
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isDateTime(String value) {  
	    try {  
	        regex = "-?([1-9][0-9]{3,}|0[0-9]{3})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])T(([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?|(24:00:00(\\.0+)?))(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?";
	    	Pattern pattern = Pattern.compile(regex); 
	    	Matcher matcher = pattern.matcher(value);  	    	
	    	if (matcher.matches()){
	        	return true;
	        } else {
	        	return false;
	        }	
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isDateTimeStamp(String value) {  
	    try {  
	        regex = "-?([1-9][0-9]{3,}|0[0-9]{3})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])T(([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?|(24:00:00(\\.0+)?))(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))";
	    	Pattern pattern = Pattern.compile(regex); 
	    	Matcher matcher = pattern.matcher(value);  	    	
	    	if (matcher.matches()){
	        	return true;
	        } else {
	        	return false;
	        }	
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isHexBinary(String value) {  
	    try {  
	        regex = "([0-9a-fA-F]{2})*";
	    	Pattern pattern = Pattern.compile(regex); 
	    	Matcher matcher = pattern.matcher(value);  	    	
	    	if (matcher.matches()){
	        	return true;
	        } else {
	        	return false;
	        }	
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isBase64Binary(String value) {  
	    try {  
	        regex = "((([A-Za-z0-9+/] ?){4})*(([A-Za-z0-9+/] ?){3}[A-Za-z0-9+/]|([A-Za-z0-9+/] ?){2}[AEIMQUYcgkosw048] ?=|[A-Za-z0-9+/] ?[AQgw] ?= ?=))?";
	    	Pattern pattern = Pattern.compile(regex); 
	    	Matcher matcher = pattern.matcher(value);  	    	
	    	if (matcher.matches()){
	        	return true;
	        } else {
	        	return false;
	        }	
	    } catch (Exception e) {  
	        return false;  
	    }  
	}		
	public boolean isAnyURI(String v) {  
	    try {  
	        java.net.URI URI = new java.net.URI(v);
	        return true;  
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isNormalizedString(String v) {  
	    try {  
	        NormalizedString objNormalizedString = new NormalizedString(v);
	        if (objNormalizedString.isValid(v)){
	        	return true;
	        } else {
	        	return false;
	        }  
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isToken(String v) {  
	    try {  
	        Token objToken = new Token(v);
	        if (objToken.isValid(v)){
	        	return true;
	        } else {
	        	return false;
	        }  
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isLanguage(String value) {  
		regex = "[a-zA-Z]{1,8}(-[a-zA-Z0-9]{1,8})*";
    	Pattern pattern = Pattern.compile(regex); 
    	Matcher matcher = pattern.matcher(value);  	    	
    	if (matcher.matches()){
        	return true;
        } else {
        	return false;
        }
	}
	public boolean isNCName(String v) {  
	    try {  
	        NCName objNCName = new NCName(v);
	    	if (objNCName.isValid(v)){
	        	return true;
	        } else {
	        	return false;
	        }	
	    } catch (Exception e) {  
	        return false;  
	    }  
	}
	public boolean isNMToken(String value) {  
		regex = "(:|[A-Z]|_|[a-z]|[\\\\#xC0-\\\\#xD6]|[\\\\#xD8-\\\\#xF6]|[\\\\#xF8-\\\\#x2FF]|[\\\\#x370-\\\\#x37D]|[\\\\#x37F-\\\\#x1FFF]|[\\\\#x200C-\\\\#x200D]|[\\\\#x2070-\\\\#x218F]|[\\\\#x2C00-\\\\#x2FEF]|[\\\\#x3001-\\\\#xD7FF]|[\\\\#xF900-\\\\#xFDCF]|[\\\\#xFDF0-\\\\#xFFFD]|[\\\\#x10000-\\\\#xEFFFF]|-|.|[0-9]|\\\\#xB7|[\\\\#x0300-\\\\#x036F]|[\\\\#x203F-\\\\#x2040])+";
    	Pattern pattern = Pattern.compile(regex); 
    	Matcher matcher = pattern.matcher(value);  	    	
    	if (matcher.matches()){
        	return true;
        } else {
        	return false;
        }
	}
	public boolean isName(String value) {  
		regex = ":|[A-Z]|_|[a-z]|[\\\\#xC0-\\\\#xD6]|[\\\\#xD8-\\\\#xF6]|[\\\\#xF8-\\\\#x2FF]|[\\\\#x370-\\\\#x37D]|[\\\\#x37F-\\\\#x1FFF]|[\\\\#x200C-\\\\#x200D]|[\\\\#x2070-\\\\#x218F]|[\\\\#x2C00-\\\\#x2FEF]|[\\\\#x3001-\\\\#xD7FF]|[\\\\#xF900-\\\\#xFDCF]|[\\\\#xFDF0-\\\\#xFFFD]|[\\\\#x10000-\\\\#xEFFFF](:|[A-Z]|_|[a-z]|[\\\\#xC0-\\\\#xD6]|[\\\\#xD8-\\\\#xF6]|[\\\\#xF8-\\\\#x2FF]|[\\\\#x370-\\\\#x37D]|[\\\\#x37F-\\\\#x1FFF]|[\\\\#x200C-\\\\#x200D]|[\\\\#x2070-\\\\#x218F]|[\\\\#x2C00-\\\\#x2FEF]|[\\\\#x3001-\\\\#xD7FF]|[\\\\#xF900-\\\\#xFDCF]|[\\\\#xFDF0-\\\\#xFFFD]|[\\\\#x10000-\\\\#xEFFFF]|-|.|[0-9]|\\\\#xB7|[\\\\#x0300-\\\\#x036F]|[\\\\#x203F-\\\\#x2040])*";
    	Pattern pattern = Pattern.compile(regex); 
    	Matcher matcher = pattern.matcher(value);  	    	
    	if (matcher.matches()){
        	return true;
        } else {
        	return false;
        }
	}
}
