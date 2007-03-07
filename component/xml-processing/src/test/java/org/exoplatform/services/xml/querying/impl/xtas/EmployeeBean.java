package org.exoplatform.services.xml.querying.impl.xtas;

public class EmployeeBean {
    private String id;
    private String firstname;
    private String lastname;
    private String telephone;
    private Address address;

    public EmployeeBean() {
        address = new Address();
    }

    public String getId(){ return id; }

    public void setId(String id){ this.id = id; }

    public String getFirstname(){ return firstname; }

    public void setFirstname(String firstname){ this.firstname = firstname; }

    public String getLastname(){ return lastname; }

    public void setLastname(String lastname){ this.lastname = lastname; }

    public String getTelephone(){ return telephone; }

    public void setTelephone(String telephone){ this.telephone = telephone; }

    public Address getAddress()
    { 
      return address; 
    }

    public void setAddress(Address address){ this.address = address; }

    public static String generateId()
    {  
      long cTime = new java.util.Date().getTime();
// Must comply with XML id ...
      return "N"+(new Long (cTime)).toString();
    }

    public String toString()
    { 
      return id+":"+firstname+":"+lastname+":"+telephone+
             "\nAddress:"+getAddress().toString(); 
    }

}
