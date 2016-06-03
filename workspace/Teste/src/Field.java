
public class Field {
	private String id, label, type, datatype, requeried, name, title, format;
		
	public Field(String id, String label, String type, String datatype, String requeried, 
			     String name, String title, String format) {
        this.id = id;
        this.label = label;
        this.type = type;
        this.datatype = datatype;
        this.requeried = requeried;
        this.name = name;
        this.title = title;
        this.format = format;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getDatatype() {
        return datatype;
    }
    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }
    public String getRequeried() {
        return requeried;
    }
    public void setRequeried(String requeried) {
        this.requeried = requeried;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
}