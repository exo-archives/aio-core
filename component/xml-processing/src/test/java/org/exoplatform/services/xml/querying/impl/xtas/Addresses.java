package org.exoplatform.services.xml.querying.impl.xtas;

import java.util.ArrayList;

public class Addresses
{
  private Address[] addresses;// = new ArrayList();
  private String country;

  public Addresses()
  {
  }

  public Address[] getAddresses() {
    return addresses;}

  public void setAddresses(Address[] addresses) {
    this.addresses = addresses;}

  public String getCountry() {
    return country;}

  public void setCountry(String country) {
    this.country = country;}

}
