package cn.youmi.framework.http;

public class CookieModel {

	private long id;
	private String name;
	private String value;
	private String expires;
	private String domain;
	
	public void setDomain(String domain){
		this.domain = domain;
	}
	
	public String getDomain(){
		return this.domain;
	}
	
	public CookieModel() {
		this(null, null, null, null);
	}
	
	public CookieModel(String name, String value) {
		this(null, name, value);
	}
	
	public CookieModel(String name, String value, String expires) {
		this(null, name, value, expires);
	}
	
	public CookieModel(Long id, String name, String value, String expires) {
		if(id!=null) {
			this.setId(id);
		}
		if( name != null) {
			this.setName(name);
		}
		if(value != null ) {
			this.setValue(value);
		}
		if(expires != null) {
			this.setExpires(expires);
		}
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public void setExpires(String expires) {
		this.expires = expires;
	}
	
	public String getName() {
		return name;
	}
	

	public String getValue() {
		return value;
	}
 
	

	public String getExpires() {
		return expires;
	}
 
	
 
	@Override
	public String toString() {
		return id + ". " + name;
	}
}