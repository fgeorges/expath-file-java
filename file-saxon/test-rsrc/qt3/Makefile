driver=org.expath.file.saxon.qt3.Driver

all:
	( cd ../../ && ant compile-test )
	java -cp .:../../lib/saxon9he.jar:../../lib/saxon9-test.jar:../../lib/expath-tools-java.jar:../../lib/expath-tools-saxon.jar:../../lib/expath-file-java.jar:../../build/classes/:../../build/test/classes/ \
	    $(driver) \
	    `pwd`
	rm -rf sandpit/{dir2,dir3,dir4,dir5,dirtarget,target.txt,temp,test.txt,test2.txt,test3.txt,test4.txt,testdir}
