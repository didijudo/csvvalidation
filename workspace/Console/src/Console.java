import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.*;
import javax.xml.namespace.QName;

import org.apache.axis.components.encoding.XMLEncoderFactory;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.types.*;
import org.apache.axis.types.Duration;
import org.apache.axis.utils.Messages;
import org.apache.axis.utils.XMLUtils;



import com.opencsv.CSVReader;

public class Console {
    private String regex;
    
    
    
	public static void main(String[] args) {
		
		Console obj = new Console();
	    obj.run();
		
	}
	
	public void run() {

	    String strArquivoCSV = "C:\\Users\\Webfis13\\Desktop\\arquivo.csv";
	    
	    try {
		    strArquivoCSV = "C:\\Users\\Webfis13\\Desktop\\arquivo.csv";
		    CSVReader reader = new CSVReader(new FileReader(strArquivoCSV));
		    String [] linha;
		    String type = "int";
		    int nrLinha = 0;
		    
		    linha = reader.readNext();
		    
		    while (linha != null) {
		    	nrLinha++;
		    	
		    	if (type.compareToIgnoreCase("int") == 0) {
		    		if ( isDate(linha[0], "M/d/yyyy") ){
		    			System.out.println("Nr linha: " + linha[0] + " Sigla Pais: " + linha[0] + " Nome Pais: " + linha[0]);
		    		} else {
		    			System.out.println("Ocorreu erro de tipo de dados na linha " + nrLinha); 
		    		}	
		    	}
		    	
		    	linha = reader.readNext();
		    }
		    reader.close();	
	    }
	    catch (IOException e)
	    {
	    	e.printStackTrace();
	    }
	}
	public boolean isInt(String v) {  
	    try {  
	        Integer.parseInt(v);  
	        return true;  
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
	        Boolean.parseBoolean(v);  
	        return true;  
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
	        	DateFormat formatter = new SimpleDateFormat(format);  
	        	Date date = (java.util.Date)formatter.parse(value);
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
