<xsl:stylesheet
  version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:axsl="http://www.w3.org/1999/XSL/TransformAlias"
  xmlns:jcr="http://www.jcp.org/jcr/1.0"
  xmlns:nt="http://www.jcp.org/jcr/nt/1.0"
  xmlns:pt="http://www.jcp.org/jcr/pt/1.0"
  xmlns:sv="http://www.jcp.org/jcr/sv/1.0"
  >

<!-- Ver 1.1 (with jcr ns) -->

<xsl:namespace-alias stylesheet-prefix="axsl" result-prefix="xsl"/>

<xsl:output method="xml" indent="yes"/>

<xsl:template match="/">
  <axsl:stylesheet  version="1.0">
  <axsl:output method="xml" indent="no" omit-xml-declaration="yes"/>

    <xsl:apply-templates/>

  </axsl:stylesheet>
</xsl:template>

<xsl:template match="query">

    <xsl:apply-templates select="select|append|delete|update|create|drop"/>

</xsl:template>

<xsl:template match="select">


   <axsl:template  match="/">

      <axsl:copy-of select="{@xpath}"/>

   </axsl:template>



</xsl:template>

<xsl:template match="append">

    <axsl:template name="copy"
         match="@*|*|text()|processing-instruction()">

    <axsl:copy>
       <axsl:apply-templates 
         select="@*|*|text()|processing-instruction()"/>
    </axsl:copy>

    </axsl:template>


   <axsl:template  match="{@xpath}">

       <axsl:copy>
         <axsl:apply-templates 
           select="@*|*|text()|processing-instruction()"/>
       </axsl:copy>

      <xsl:copy-of select="//append/*"/>

   </axsl:template>

</xsl:template>

<xsl:template match="delete">

    <axsl:template name="copy"
         match="@*|*|text()|processing-instruction()">

    <axsl:copy>
       <axsl:apply-templates 
         select="@*|*|text()|processing-instruction()"/>
    </axsl:copy>

    </axsl:template>


   <axsl:template  match="{@xpath}"/>

</xsl:template>

<xsl:template match="update">

    <axsl:template name="copy"
         match="@*|*|text()|processing-instruction()">

    <axsl:copy>
       <axsl:apply-templates 
         select="@*|*|text()|processing-instruction()"/>
    </axsl:copy>

    </axsl:template>


   <axsl:template  match="{@xpath}">

      <xsl:copy-of select="//update/node()"/>

   </axsl:template>

</xsl:template>


</xsl:stylesheet>
