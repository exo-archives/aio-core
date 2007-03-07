package org.exoplatform.services.xml.querying.impl.xtas;

public class Address
{
  private String street1;
  private String street2;
  private String city;
  private String country;
  private String zip;

  public Address()
  {
  }

  /**
* Access method for the street1 property.
*
* @return   the current value of the street1 property
   */
  public String getStreet1() {
    return street1;}

  /**
   * @return void
* Sets the value of the street1 property.
*
* @param aStreet1 the new value of the street1 property
   */
  public void setStreet1(String aStreet1) {
    street1 = aStreet1;}

  /**
* Access method for the street2 property.
*
* @return   the current value of the street2 property
   */
  public String getStreet2() {
    return street2;}

  /**
   * @return void
* Sets the value of the street2 property.
*
* @param aStreet2 the new value of the street2 property
   */
  public void setStreet2(String aStreet2) {
    street2 = aStreet2;}

  /**
* Access method for the city property.
*
* @return   the current value of the city property
   */
  public String getCity() {
    return city;}

  /**
   * @return void
* Sets the value of the city property.
*
* @param aCity the new value of the city property
   */
  public void setCity(String aCity) {
    city = aCity;}

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
      return street1+" "+street2+","+city+","+country+","+zip; 
    }

}
