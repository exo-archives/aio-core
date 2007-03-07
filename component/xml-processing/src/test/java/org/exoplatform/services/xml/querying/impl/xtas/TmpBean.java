package org.exoplatform.services.xml.querying.impl.xtas;

import java.io.Serializable;

public class TmpBean implements Serializable 
{
    private String property;
    private Integer e1;
    private Float f2;

    public TmpBean() 
    {
    }

    public TmpBean(String property, Integer e1, Float f2)
    {
        this.property = property;
        this.e1 = e1;
        this.f2 = f2;
    }

    public String getProperty(){ return property; }

    public void setProperty(String property){ this.property = property; }

    public Integer getE1(){ return e1; }

    public void setE1(Integer e1){ this.e1 = e1; }

    public Float getF2(){ return f2; }

    public void setF2(Float f2){ this.f2 = f2; }

}
