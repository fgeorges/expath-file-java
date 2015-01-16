<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:f="http://expath.org/ns/file"
                xmlns:out="http://www.w3.org/2010/xslt-xquery-serialization"
                exclude-result-prefixes="f out"
                version="2.0">

   <xsl:output indent="yes"/>

   <xsl:template name="main" match="/">
      <append path="file.txt">
         <arity-2>
            <xsl:value-of select="f:append('file.txt', 'Hello, you!&#10;')"/>
         </arity-2>
         <arity-3>
            <xsl:variable name="params" as="element()">
               <out:serialization-parameters>
                  <out:omit-xml-declaration value="yes"/>
               </out:serialization-parameters>
	    </xsl:variable>
            <xsl:value-of select="f:append('file.txt', 'Hello, serial!&#10;', $params)"/>
         </arity-3>
      </append>
   </xsl:template>

</xsl:stylesheet>
