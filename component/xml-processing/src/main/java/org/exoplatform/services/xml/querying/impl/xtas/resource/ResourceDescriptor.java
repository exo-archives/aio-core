package org.exoplatform.services.xml.querying.impl.xtas.resource;

public class ResourceDescriptor {

    private String name;
    private String description;
    private String className;
    private String contextClassName;
    private String prefix;

    public String getName(){ return name; }

    public void setName(String name){ this.name = name; }

    public String getDescription(){ return description; }

    public void setDescription(String description){ this.description = description; }

    public String getClassname(){ return className; }

    public void setClassname(String className){ this.className = className; }

    public String getPrefix(){ return prefix; }

    public void setPrefix(String prefix){ this.prefix = prefix; }

    public String toString(){ return "Descriptor \n name: "+name+"\n description: "+description+
                              "\n class: "+className+"\n prefix:"+prefix;  }

}
