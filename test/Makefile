## 
## TODO: For now, use the default repository when calling Saxon.  Must
## use a dedicated repo only for tests (somewhere in bin/ or something).
## 
## TODO: Only for Saxon now.  Actually, the suite itself is at the
## level of the package, but the way to run it for Saxon is of course
## Saxon-specific.
## 

SAXON=/Users/fgeorges/bin/saxon
XSPEC=/Users/fgeorges/projects/xslt/xspec/trunk/xspec.sh
XSPEC.xsl=/Users/fgeorges/projects/xslt/xspec/trunk/generate-xspec-tests.xsl
XSPEC.xq=/Users/fgeorges/projects/xslt/xspec/trunk/generate-query-tests.xsl
XSPEC.report=/Users/fgeorges/projects/xslt/xspec/trunk/format-xspec-report.xsl

all: file

file: file-xsl file-xq

file-xsl:
	"$(SAXON)" -xsl:"$(XSPEC.xsl)" -s:file.xspec -o:file-xsl.xsl
	"$(SAXON)" -xsl:file-xsl.xsl -o:file-xsl.xml \
	    -it:{http://www.jenitennison.com/xslt/xspec}main
	"$(SAXON)" -xsl:"$(XSPEC.report)" -s:file-xsl.xml -o:file-xsl.html

file-xq:
	"$(SAXON)" -xsl:"$(XSPEC.xq)" -s:file.xspec -o:file-xq.xq
	"$(SAXON)" --xq -o:file-xq.xml file-xq.xq
	"$(SAXON)" -xsl:"$(XSPEC.report)" -s:file-xq.xml -o:file-xq.html

.PHONY: all file file-xsl file-xq
