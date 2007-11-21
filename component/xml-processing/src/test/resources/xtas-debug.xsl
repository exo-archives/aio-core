<xsl:stylesheet
  version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:axsl="http://www.w3.org/1999/XSL/TransformAlias"
  xmlns:xtas="http://xtas.sourceforge.net"
  xmlns:jcr="http://www.jcp.org/jcr/1.0"
  >

<!-- Ver 1.2 -->

<xsl:namespace-alias stylesheet-prefix="axsl" result-prefix="xsl"/>

<xsl:output method="xml" indent="yes"/>

<xsl:template match="/">
  <axsl:stylesheet  version="1.0">
  <axsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
  <axsl:param name="generate-info" select="0"/>
  <axsl:param name="sourceId" select="null"/>

    <xsl:apply-templates/>

  </axsl:stylesheet>
</xsl:template>

<xsl:template match="query">

    <xsl:apply-templates select="select|append|delete|update|create|drop"/>

</xsl:template>

<xsl:template match="select">

   <axsl:template  match="/">
       <xsl:element name="xtas:query">
          <axsl:attribute name="xtas:source">
            <axsl:value-of select="$sourceId"/> 
          </axsl:attribute>
          <axsl:attribute name="xtas:xpath">
            <xsl:value-of select="@xpath"/> 
          </axsl:attribute>
          <axsl:copy-of select="{@xpath}"/>
       </xsl:element>
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
      <xsl:copy-of select="//append/value/node()"/>
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

<!-- Resource queries EXPERIMENTAL! 
    (worked but query does not use it for a while) -->

<xsl:template match="create">

   <axsl:template  match="/">

      <axsl:processing-instruction name="resource">
         type="create" id="<xsl:value-of select="@resource"/>"
      </axsl:processing-instruction>

      <xsl:copy-of select="/create/*"/>

   </axsl:template>

</xsl:template>

<xsl:template match="drop">

   <axsl:template  match="/">

      <axsl:processing-instruction name="resource">
         type="drop" id="<xsl:value-of select="@resource"/>"
      </axsl:processing-instruction>

   </axsl:template>

</xsl:template>


</xsl:stylesheet>
