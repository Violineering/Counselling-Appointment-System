package compile;

public class User {
	private String Username;
    private String Password;
    private String email;	   
    private String PhoneNo;

    public User(String Username, String Password, String email, String PhoneNo) {	    	
        this.Username = Username;
        this.Password = Password;
        this.email = email;	       
        this.PhoneNo = PhoneNo;
    }

    // Getter and Setter methods
    
    	    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username.trim();
    }
    
    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }
   	   
    
    public String getPhoneNo() {
        return PhoneNo;
    }
   
    public void setPhoneNo(String PhoneNo) {
        this.PhoneNo = PhoneNo.trim();
    }

    public String getFileString() {
        return this.Username + ";"+this.Password+";"+ this.email+";"+ this.PhoneNo;
    }

}
