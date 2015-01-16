## 
## TODO: For now, use the default repository when calling Saxon.  Must
## use a dedicated repo only for tests (somewhere in bin/ or something).
## 
## TODO: Only for Saxon now.  Actually, the suite itself is at the
## level of the package, but the way to run it for Saxon is of course
## Saxon-specific.
## 

SAXON=/Users/fgeorges/bin/saxon
XREPO=/Users/fgeorges/bin/xrepo
XSPEC=/Users/fgeorges/projects/xslt/xspec/bin/xspec.sh
XSPEC.xsl=/Users/fgeorges/projects/xslt/xspec/src/compiler/generate-xspec-tests.xsl
XSPEC.xq=/Users/fgeorges/projects/xslt/xspec/src/compiler/generate-query-tests.xsl
XSPEC.report=/Users/fgeorges/projects/xslt/xspec/src/reporter/format-xspec-report.xsl

REPO=repo

xsl:
	rm -rf "$(REPO)"
	xrepo create "$(REPO)"
	xrepo --repo "$(REPO)" list
	"$(XREPO)" --repo "$(REPO)" install ../build/expath-file-saxon-0.5.0pre1.xar
	"$(SAXON)" --repo "$(REPO)" -xsl:append-simple.xsl -it:main
	"$(SAXON)" --repo "$(REPO)" -xsl:exists-simple.xsl -it:main
	"$(SAXON)" --repo "$(REPO)" -xsl:list-simple.xsl -it:main
	"$(SAXON)" --repo "$(REPO)" -xsl:list-recursive.xsl -it:main
	"$(SAXON)" --repo "$(REPO)" -xsl:list-pattern.xsl -it:main
	"$(SAXON)" --repo "$(REPO)" -xsl:read-binary-simple.xsl -it:main

all: file

file: file-xsl file-xq

file-xsl:
	"$(SAXON)" -xsl:"$(XSPEC.xsl)" -s:file.xspec -o:file-xsl.xsl
	"$(SAXON)" --repo "$(REPO)" -xsl:file-xsl.xsl -o:file-xsl.xml \
	    -it:{http://www.jenitennison.com/xslt/xspec}main
	"$(SAXON)" -xsl:"$(XSPEC.report)" -s:file-xsl.xml -o:file-xsl.html

file-xq:
	"$(SAXON)" -xsl:"$(XSPEC.xq)" -s:file.xspec -o:file-xq.xq
	"$(SAXON)" --repo "$(REPO)" --xq -o:file-xq.xml file-xq.xq
	"$(SAXON)" -xsl:"$(XSPEC.report)" -s:file-xq.xml -o:file-xq.html

.PHONY: all file file-xsl file-xq
