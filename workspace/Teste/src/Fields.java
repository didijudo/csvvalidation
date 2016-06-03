 
import java.util.ArrayList;

import org.json.*;

public class Fields {
	private String nomeArquivoCsv, url, encoding, quoteChar, commentPrefix, delimiter, trim;
	
	private int headerRowCount, skipColumns, skipRows;  
	
	private boolean doubleQuote, header, skipBlankRows, skipInitialSpace;
	
	private ArrayList<String> lineTerminators = new ArrayList<String>();
	
	private ArrayList<Field> arrayFields = new ArrayList<Field>();
	
	public Fields(JSONObject objJson, String nomeArquivoCsv) {
		try {
			this.nomeArquivoCsv = nomeArquivoCsv;
			
			if (objJson.has("resources")) {
				setResources(objJson);
			}
			if (objJson.has("tables")) {
				setTables(objJson);
			}
			if (objJson.has("tableSchema")) {
				setTableSchema(objJson);
			}
			if (objJson.has("columns")) {
				setColumns(objJson);
			}
							
		} 
		catch (Exception e) 
			{ 
				e.printStackTrace(); 
			}
		
    }
	
	private void setTables(JSONObject objJson){
		try{
			if (objJson.get("tables") instanceof JSONArray){
				JSONArray arrTables = objJson.getJSONArray("tables");
				for (int i=0; i < arrTables.length(); i++) {
					JSONObject objTable = arrTables.getJSONObject(i);
					if (objTable.has("dialect")) {
						setDialect(objTable);
					}
					if (objTable.has("url")) {
						url = objTable.getString("url");
						if (compararNomeArquivoComUrl(nomeArquivoCsv,url)){
							setTableSchema(objTable);
						}
					} else {
						setTableSchema(objTable);
					} 
				}
			}
		} catch (Exception e) 
		{ 
			e.printStackTrace(); 
		}	
	}
	
	private void setDialect(JSONObject objJson){
		try{
			
			if (objJson.get("dialect") instanceof JSONObject){
				JSONObject objDialect = (JSONObject)objJson.getJSONObject("dialect");
				if (objDialect.has("encoding")) {
					this.encoding = objDialect.getString("encoding");
			    } 
				if (objDialect.has("lineTerminators")) {
					if (objDialect.get("lineTerminators") instanceof JSONArray){
						JSONArray arrLineTerminators = objDialect.getJSONArray("lineTerminators");
						for (int i=0; i < arrLineTerminators.length(); i++) {
							this.lineTerminators.add(arrLineTerminators.get(i).toString());
						}
					} else {
						if (objDialect.get("lineTerminators") instanceof JSONObject){
							lineTerminators.add(objDialect.getString("lineTerminators"));
						}	
					}
			    }
			    if (objDialect.has("quoteChar")) {
			    	this.quoteChar = objDialect.getString("quoteChar");
			    }
			    if (objDialect.has("doubleQuote")) {
			    	this.doubleQuote = objDialect.getBoolean("doubleQuote");
			    }
			    if (objDialect.has("skipRows")) {
			    	this.skipRows = objDialect.getInt("skipRows");
			    }
			    if (objDialect.has("commentPrefix")) {
			    	this.commentPrefix = objDialect.getString("commentPrefix");
			    }
			    if (objDialect.has("header")) {
			    	this.header = objDialect.getBoolean("header");
			    }
			    if (objDialect.has("headerRowCount")) {
			    	this.headerRowCount = objDialect.getInt("headerRowCount");
			    }
			    if (objDialect.has("delimiter")) {
			    	this.delimiter = objDialect.getString("delimiter");
			    }
			    if (objDialect.has("skipColumns")) {
			    	this.skipColumns = objDialect.getInt("skipColumns");
			    }
			    if (objDialect.has("skipBlankRows")) {
			    	this.skipBlankRows = objDialect.getBoolean("skipBlankRows");
			    }
			    if (objDialect.has("skipInitialSpace")) {
			    	this.skipInitialSpace = objDialect.getBoolean("skipInitialSpace");
			    }
			}
				
		} catch (Exception e) 
		{ 
			e.printStackTrace(); 
		}	
	}
	
	private void setTableSchema(JSONObject objJson){
		try{
			if (objJson.has("tableSchema")) {
				if (objJson.get("tableSchema") instanceof JSONObject){
					JSONObject objTableSchema = (JSONObject)objJson.get("tableSchema");
					if (objTableSchema.has("dialect")){
						setDialect(objTableSchema);
					}
					if (objTableSchema.has("columns")) {
						JSONArray arrColumns = objTableSchema.getJSONArray("columns");
						for (int i=0; i < arrColumns.length(); i++) {
							JSONObject objColumns = arrColumns.getJSONObject(i);
							setColumns(objColumns);
						}
					}
				}
			}
		} catch (Exception e) 
		{ 
			e.printStackTrace(); 
		}	
	}
	
	private void setColumns(JSONObject objColumns){
		
		try{
			String id = "", label ="", type = "", datatype = "", requeried = "", name = "", title = "", format = "";
			if (objColumns.has("id")) {
		    	id = objColumns.getString("id");
		    } 
		    if (objColumns.has("label")) {
		    	label = objColumns.getString("label");
		    }
		    if (objColumns.has("type")) {
		    	type = objColumns.getString("type");
		    }
		    if (objColumns.has("datatype")) {
		    	if (objColumns.get("datatype") instanceof String){
		    		datatype = objColumns.getString("datatype");
				} else if (objColumns.get("datatype") instanceof JSONObject) {
					JSONObject objDatatype = (JSONObject)objColumns.get("datatype");
					if (objDatatype.has("base")) {
						datatype = objDatatype.getString("base");
					}
					if (objDatatype.has("format")) {
						format = objDatatype.getString("format");
					}
				}
		    }
		    if (objColumns.has("requeried")) {
		    	requeried = objColumns.getString("requeried");
		    }
		    if (objColumns.has("name")) {
		    	name = objColumns.getString("name");
		    }
		    if (objColumns.has("title")) {
		    	title = objColumns.getString("title");
		    }
		    
			Field objField = new Field(id, label, type, datatype, requeried, name, title, format);
			setArrayFields(objField);
		} 
		catch (Exception e) 
			{ 
				e.printStackTrace(); 
			}
	}
	
	private void setResources(JSONObject objJson){
		try{
			if (objJson.has("resources")) {
				if (objJson.get("resources") instanceof JSONArray){
					JSONArray arrResources = objJson.getJSONArray("resources");
					for (int i=0; i < arrResources.length(); i++) {
						JSONObject objResources = arrResources.getJSONObject(i);
						if (objResources.has("url") || objResources.has("path")) {
							url = objResources.getString("url");
							if (compararNomeArquivoComUrl(nomeArquivoCsv,url)){
								setSchema(objResources);
							}
						} else {
							setSchema(objResources);
						}
					}
				}
			}	
		} catch (Exception e) 
		{ 
			e.printStackTrace(); 
		}	
	}
	
	private void setSchema(JSONObject objJson){
		try{
			if (objJson.has("schema")) {
		    	JSONObject objSchema = (JSONObject)objJson.get("schema");
		    	if (objSchema.has("fields")) {
		    		JSONArray arrFields = objSchema.getJSONArray("fields");
		    		for (int i=0; i < arrFields.length(); i++) {
						JSONObject objResources = arrFields.getJSONObject(i);
						setColumns(objResources);
					}
		    	}
			}
			
    	} catch (Exception e) 
		{ 
			e.printStackTrace(); 
		}
	}
	
	private boolean compararNomeArquivoComUrl(String nomeArquivoCsv, String url){
		if (url.contains(nomeArquivoCsv)){
			return true;
		} else {
			return false;
		}
	}
	
	public ArrayList<Field> getArrayFields() {
        return arrayFields;
    }

    public void setArrayFields(Field field) {
        arrayFields.add(field);
    }
    
    public String getEncoding() {
        return this.encoding;
    }
    public ArrayList<String> getLineTerminators() {
        return this.lineTerminators;
    }
    public String getQuoteChar() {
        return this.quoteChar;
    }
    public boolean getDoubleQuote() {
        return this.doubleQuote;
    }
    public int getSkipRows() {
        return this.skipRows;
    }
    public String getCommentPrefix() {
        return this.commentPrefix;
    }
    public boolean getHeader() {
        return this.header;
    }
    public int getHeaderRowCount() {
        return this.headerRowCount;
    }
    public String getDelimiter() {
        return this.delimiter;
    }
    public int getSkipColumns() {
        return this.skipColumns;
    }
    public boolean getSkipBlankRows() {
        return this.skipBlankRows;
    }
    public boolean getSkipInitialSpace() {
        return this.skipInitialSpace;
    }
    public String getTrim() {
        return this.trim;
    }
   
}
