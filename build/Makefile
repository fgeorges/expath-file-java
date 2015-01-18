j.ver  := 0.1.0
j.mod  := file-java
j.jar  := expath-$(j.mod)-$(j.ver).jar
j.proj := ../file-java
j.dist := $(j.proj)/dist

s.ver  := 0.5.0
s.mod  := file-saxon
s.root := expath-$(s.mod)-$(s.ver)
s.file := $(s.root)/content
s.pkg  := saxon-xar
s.proj := ../file-saxon
s.dist := $(s.proj)/dist

j.prop=$(j.proj)/src/org/expath/file/version.properties
s.prop=$(s.proj)/src/org/expath/file/saxon/version.properties
rev=$(shell git describe --always)

all: clean dist

dist: j.dist s.dist

clean: j.clean s.clean

j.dist:
	echo "org.expath.file.version=$(j.ver)" >  $(j.prop)
	echo "org.expath.file.revision=$(rev)"  >> $(j.prop)
	( cd "$(j.proj)" && ant )
	cp "$(j.dist)/file-java.jar" "$(j.jar)"

j.clean:
	rm -f "$(j.jar)"

s.dist: s.pkg
	zip "$(s.root).zip" "$(s.root).xar"

s.pkg:
	echo "org.expath.file.saxon.version=$(s.ver)" >  $(s.prop)
	echo "org.expath.file.saxon.revision=$(rev)"  >> $(s.prop)
	( cd "$(s.proj)" && ant )
	mkdir "$(s.root)"
	mkdir "$(s.file)"
	cp "$(s.pkg)/expath-pkg.xml"  "$(s.root)"
	cp "$(s.pkg)/saxon.xml"       "$(s.root)"
	cp "$(s.pkg)/cxan.xml"        "$(s.root)"
	cp "$(j.dist)/file-java.jar"  "$(s.file)/$(j.mod).jar"
	cp "$(s.dist)/file-saxon.jar" "$(s.file)/$(s.mod).jar"
	cp "$(s.pkg)/wrapper.xsl"     "$(s.file)/$(s.mod).xsl"
	cp "$(s.pkg)/wrapper.xq"      "$(s.file)/$(s.mod).xql"
	cd "$(s.root)" && zip -r "../$(s.root).xar" *

s.clean:
	rm -rf "$(s.root)"
	rm -rf "$(s.root).xar"

.PHONY: all dist clean j.dist j.clean s.dist s.pkg s.clean
