package org.exoplatform.services.xml.querying.impl.xtas;

import java.util.ArrayList;


public class City
{
  private String name;
  private String country;
  private String zip;
  private ArrayList streets;

  public City()
  {
    streets = new ArrayList();
  }

  public City(String country, String name, String zip)
  {
     this();
     this.name = name;
     this.country = country;
     this.zip = zip;
     this.streets.add(new Street("One", new Integer(100)));
     this.streets.add(new Street("Two", new Integer(200)));
     this.streets.add(new Street("Three", new Integer(300)));
  }

  public ArrayList getStreets(){

       return streets;
  }

  public String getName() {
    return name;}

  /**
   * @return void
* Sets the value of the city property.
*
* @param aCity the new value of the city property
   */
  public void setName(String aName) {
    name = aName;}

  /**
* Access method for the country property.
*
* @return   the current value of the country property
   */
  public String getCountry() {
    return country;}

  /**
   * @return void
* Sets the value of the country property.
*
* @param aCountry the new value of the country property
   */
  public void setCountry(String aCountry) {
    country = aCountry;}

  public String getZip() {
    return zip;}

  public void setZip(String aZip) {
    zip = aZip;}


    public String toString()
    { 
      return name+","+country+","+zip; 
    }

}
