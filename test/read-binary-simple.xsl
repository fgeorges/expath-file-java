<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:f="http://expath.org/ns/file"
                exclude-result-prefixes="f"
                version="2.0">

   <xsl:output indent="yes"/>

   <xsl:template name="main" match="/">
      <binary>
         <xsl:value-of select="f:read-binary('read-binary-simple.xsl')"/>
      </binary>
   </xsl:template>

</xsl:stylesheet>
