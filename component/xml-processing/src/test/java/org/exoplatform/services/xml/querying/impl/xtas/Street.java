package org.exoplatform.services.xml.querying.impl.xtas;

public class Street
{
  private String name;
  private Integer length;

  public Street()
  {
  }

  public Street(String name,Integer length )
  {
     this.name = name;
     this.length = length;

  }

  public String getName() {
    return name;}

  public Integer getLength() {
    return length;}

}
