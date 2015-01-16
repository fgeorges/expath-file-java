<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:test="http://www.jenitennison.com/xslt/unit-test"
                xmlns:x="http://www.jenitennison.com/xslt/xspec"
                xmlns:__x="http://www.w3.org/1999/XSL/TransformAliasAlias"
                xmlns:pkg="http://expath.org/ns/pkg"
                xmlns:impl="urn:x-xspec:compile:xslt:impl"
                xmlns:f="http://expath.org/ns/file"
                version="2.0"
                exclude-result-prefixes="pkg impl">
   <xsl:import href="http://expath.org/ns/file.xsl"/>
   <xsl:import href="file:/Users/fgeorges/projects/xslt/xspec/src/compiler/generate-tests-utils.xsl"/>
   <xsl:namespace-alias stylesheet-prefix="__x" result-prefix="xsl"/>
   <xsl:variable name="x:stylesheet-uri"
                 as="xs:string"
                 select="'http://expath.org/ns/file.xsl'"/>
   <xsl:output name="x:report" method="xml" indent="yes"/>
   <xsl:template name="x:main">
      <xsl:message>
         <xsl:text>Testing with </xsl:text>
         <xsl:value-of select="system-property('xsl:product-name')"/>
         <xsl:text> </xsl:text>
         <xsl:value-of select="system-property('xsl:product-version')"/>
      </xsl:message>
      <xsl:result-document format="x:report">
         <xsl:processing-instruction name="xml-stylesheet">type="text/xsl" href="file:/Users/fgeorges/projects/xslt/xspec/src/compiler/format-xspec-report.xsl"</xsl:processing-instruction>
         <x:report stylesheet="{$x:stylesheet-uri}" date="{current-dateTime()}">
            <xsl:call-template name="x:d5e2"/>
         </x:report>
      </xsl:result-document>
   </xsl:template>
   <xsl:template name="x:d5e2">
      <xsl:message>f:list</xsl:message>
      <x:scenario>
         <x:label>f:list</x:label>
         <x:call function="f:list">
            <x:param select="              string-join(                tokenize(static-base-uri(), '/')[position() lt last()], '/')"/>
         </x:call>
         <xsl:variable name="x:result" as="item()*">
            <xsl:variable select="              string-join(                tokenize(static-base-uri(), '/')[position() lt last()], '/')"
                          name="d6e1"/>
            <xsl:sequence select="f:list($d6e1)"/>
         </xsl:variable>
         <xsl:call-template name="test:report-value">
            <xsl:with-param name="value" select="$x:result"/>
            <xsl:with-param name="wrapper-name" select="'x:result'"/>
            <xsl:with-param name="wrapper-ns" select="'http://www.jenitennison.com/xslt/xspec'"/>
         </xsl:call-template>
         <xsl:call-template name="x:d5e5">
            <xsl:with-param name="x:result" select="$x:result"/>
         </xsl:call-template>
      </x:scenario>
   </xsl:template>
   <xsl:template name="x:d5e5">
      <xsl:param name="x:result" required="yes"/>
      <xsl:message>result of the function call</xsl:message>
      <xsl:variable name="impl:with-context"
                    select="                          exists($x:result) and empty($x:result[2])"/>
      <xsl:variable name="impl:context" as="item()?">
         <xsl:choose>
            <xsl:when test="$impl:with-context">
               <xsl:sequence select="$x:result"/>
            </xsl:when>
            <xsl:otherwise/>
         </xsl:choose>
      </xsl:variable>
      <xsl:variable name="impl:expected-doc" as="document-node()">
         <xsl:document>
            <f:dir href="file:/Users/fgeorges/projects/expath/file-java/test" name="test">
               <f:file href="file:/Users/fgeorges/projects/expath/file-java/test/file-xsl.html"
                       name="file-xsl.html"/>
               <f:file href="file:/Users/fgeorges/projects/expath/file-java/test/file-xsl.xml"
                       name="file-xsl.xml"/>
               <f:file href="file:/Users/fgeorges/projects/expath/file-java/test/file-xsl.xsl"
                       name="file-xsl.xsl"/>
               <f:file href="file:/Users/fgeorges/projects/expath/file-java/test/file.xspec"
                       name="file.xspec"/>
               <f:file href="file:/Users/fgeorges/projects/expath/file-java/test/Makefile"
                       name="Makefile"/>
            </f:dir>
         </xsl:document>
      </xsl:variable>
      <xsl:variable name="impl:expected" select="$impl:expected-doc/node()"/>
      <xsl:variable name="impl:successful"
                    as="xs:boolean"
                    select="                    test:deep-equal(                      $impl:expected,                      if ( $impl:with-context ) then $impl:context else $x:result,                      2)"/>
      <xsl:if test="not($impl:successful)">
         <xsl:message>      FAILED</xsl:message>
      </xsl:if>
      <x:test successful="{ $impl:successful }">
         <x:label>result of the function call</x:label>
         <xsl:call-template name="test:report-value">
            <xsl:with-param name="value"
                            select="if ( $impl:with-context ) then $impl:context else $x:result"/>
            <xsl:with-param name="wrapper-name" select="'x:result'"/>
            <xsl:with-param name="wrapper-ns" select="'http://www.jenitennison.com/xslt/xspec'"/>
         </xsl:call-template>
         <xsl:call-template name="test:report-value">
            <xsl:with-param name="value" select="$impl:expected"/>
            <xsl:with-param name="wrapper-name" select="'x:expect'"/>
            <xsl:with-param name="wrapper-ns" select="'http://www.jenitennison.com/xslt/xspec'"/>
         </xsl:call-template>
      </x:test>
   </xsl:template>
</xsl:stylesheet>
