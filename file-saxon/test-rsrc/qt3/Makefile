saxon=/Library/Java/com/saxonica/saxonhe9-6-0-4j
driver=net.sf.saxon.testdriver.QT3TestDriverHE

all:
	java -cp .:$(saxon)/saxon9he.jar:$(saxon)/saxon9-test.jar $(driver) \
	    . catalog.xml -o:results -lang:XQ30
