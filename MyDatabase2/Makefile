
all: clean 

clean: 
	
	rm build/database/*.class
	rm *.db

aspects:
	ajc -1.5 -inpath lib/jakarta-poi.jar:lib/junit.jar:lib/log4j.jar:lib/org.restlet.jar:lib/java-json.jar -sourceroots src -d build

java:
	ajc -1.5 -inpath lib/jakarta-poi.jar:lib/junit.jar:lib/log4j.jar:lib/org.restlet.jar:lib/java-json.jar -d build src/database/*.java

compile:
	javac -cp lib/jakarta-poi.jar:lib/junit.jar:lib/log4j.jar -d build src/database/*.java
	
vol:
	java -cp ./build:lib/aspectjrt.jar Volume

run0:
	java -cp ./build:lib/aspectjrt.jar SMImplVersion0

run1:
	java -cp ./build:lib/aspectjrt.jar SMImplVersion1
	
run2:
	java -cp ./build:lib/aspectjrt.jar SMImplVersion2
	
test1:
	java -cp ./build:lib/aspectjrt.jar:lib/junit.jar junit.textui.TestRunner TestAcceptanceBasic
	
test2:
	java -cp ./build:lib/aspectjrt.jar:lib/junit.jar junit.textui.TestRunner TestAcceptanceCrossFunctional
	
test3:
	java -cp ./build:lib/aspectjrt.jar:lib/junit.jar junit.textui.TestRunner TestAcceptanceOID
	
test4:
	java -cp ./build:lib/aspectjrt.jar:lib/junit.jar junit.textui.TestRunner TestStressLargeRecords
	
test5:
	java -cp ./build:lib/aspectjrt.jar:lib/junit.jar junit.textui.TestRunner TestXMLFileLoad
	
	
	
	
	
	