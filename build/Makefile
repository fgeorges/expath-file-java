FILE.ver := 0.5.0pre1
FILE.mod := file-saxon
PKG.root := $(FILE.mod)-$(FILE.ver)
PKG.file := $(PKG.root)/$(FILE.mod)
PKG      := saxon-xar
DIST     := ../file-saxon/dist

all: clean pkg

pkg:
	mkdir    "$(PKG.root)"
	mkdir -p "$(PKG.file)"
	cp "$(PKG)/expath-pkg.xml"   "$(PKG.root)"
	cp "$(PKG)/saxon.xml"        "$(PKG.root)"
	cp "$(PKG)/cxan.xml"         "$(PKG.root)"
	cp "$(DIST)/files-saxon.jar" "$(PKG.file)/$(FILE.mod).jar"
	cp "$(PKG)/wrapper.xsl"      "$(PKG.file)/$(FILE.mod).xsl"
	cp "$(PKG)/wrapper.xq"       "$(PKG.file)/$(FILE.mod).xql"
	cd "$(PKG.root)" && zip -r "../$(PKG.root).xar" *

clean:
	rm -rf "$(PKG.root)"
	rm -rf "$(PKG.root).xar"

.PHONY: pkg clean
