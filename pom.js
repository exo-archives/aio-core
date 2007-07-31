eXo.require("eXo.projects.Project")  ;
if(eXo.module.kernel == null) eXo.load('pom.js', eXo.env.eXoProjectsDir + "/kernel/trunk" ) ;

function Core(version) {
  this.version = version ;
  this.relativeMavenRepo = "org/exoplatform/core" ;
  this.relativeSRCRepo = "core/trunk" ;
  this.name = "core" ;

  this.component = {}
  this.component.common = 
    new Project("org.exoplatform.core", "exo.core.component.common", "jar", version) .
    addDependency(new Project("directory-naming", "naming-core", "jar", "0.8")).
    addDependency(new Project("directory-naming", "naming-java", "jar", "0.8")).
    addDependency(new Project("jotm", "jotm", "jar", "2.0.10")).
    addDependency(new Project("javax.resource", "connector", "jar", "1.5"));

  this.component.ldap = new Project("org.exoplatform.core", "exo.core.component.ldap", "jar", version) ;

  this.component.database = 
    new Project("org.exoplatform.core", "exo.core.component.database", "jar", version) .
    addDependency(new Project("com.experlog", "xapool", "jar", "1.5.0")).
    addDependency(new Project("org.hibernate", "hibernate", "jar", "3.1.2")).
    addDependency(new Project("commons-collections", "commons-collections", "jar", "3.1")).
    addDependency(new Project("c3p0", "c3p0", "jar", "0.8.4.5")).
    addDependency(new Project("antlr", "antlr", "jar", "2.7.5")).
    addDependency(new Project("javax.transaction", "jta", "jar", "1.0.1B")).
    addDependency(new Project("jotm", "jotm_jrmp_stubs", "jar", "2.0.10")).
    addDependency(new Project("jotm", "jotm", "jar", "2.0.10")).
    addDependency(new Project("howl", "howl-logger", "jar", "0.1.11")).
    addDependency(new Project("hsqldb", "hsqldb", "jar", "1.8.0.7")).
    addDependency(new Project("javax.resource", "connector-api", "jar", "1.5"));

  this.component.documents =
    new Project("org.exoplatform.core", "exo.core.component.document", "jar", version).
    addDependency(new Project("pdfbox", "pdfbox", "jar", "0.7.2")).
    addDependency(new Project("html-parser", "html-parser", "jar", "1.6")).
    addDependency(new Project("poi", "poi", "jar", "3.0-alpha1")).
    addDependency(new Project("poi", "poi-scratchpad", "jar", "3.0-alpha1"));
    
  this.component.organization = 
    new Project("org.exoplatform.core", "exo.core.component.organization.api", "jar", version).
    addDependency(new Project("org.exoplatform.core", "exo.core.component.organization.jdbc", "jar", version));

  this.component.security = 
    new Project("org.exoplatform.core", "exo.core.component.security", "jar", version) ;

  this.component.xmlProcessing = 
    new Project("org.exoplatform.core", "exo.core.component.xml-processing", "jar", version) ;

  this.component.resources = 
    new Project("org.exoplatform.core", "exo.core.component.resources.api", "jar", version).
    addDependency(new Project("org.exoplatform.core", "exo.core.component.database", "jar", version));
}

eXo.module.core = new Core('2.0.3') ;
