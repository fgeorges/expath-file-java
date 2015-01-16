<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:f="http://expath.org/ns/file"
                exclude-result-prefixes="xs"
                version="2.0">

   <xsl:output indent="yes"/>

   <xsl:template name="main" match="/">
      <result>
         <xsl:for-each select="f:list('/tmp')">
            <path>
               <xsl:value-of select="."/>
            </path>
         </xsl:for-each>
      </result>
   </xsl:template>

</xsl:stylesheet>
