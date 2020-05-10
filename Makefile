.PHONY: all datamodel webserver run clean

MVN=mvn
CLASSPATH=./target/*.jar
JAVA_OPT=-cp $(CLASSPATH)
JAVA=java $(JAVA_OPT)
JAVADOC=$(MVN) javadoc:javadoc
TARGET=fr.univparis.metro.Webserver
TERMINAL_TARGET=fr.univparis.metro.App
GRAPH_EXPORTER_TARGET=fr.univparis.metro.GraphExporter

# Target all builds the project.
all: datamodel webserver

# Target datamodel builds only datamodel
datamodel:
	cd datamodel && $(MVN) package install

# Target webserver builds only the webserver
webserver:
	cd webserver && $(MVN) compile test assembly:single

# Target run launch the webserver if the project is already built
run :
	cd webserver && $(JAVA) $(TARGET)

# Target run_terminal launch a termianl version of this project with less features
run_terminal :
	cd webserver && $(JAVA) $(TERMINAL_TARGET)

# Target export_to_dot export a city network into a dot file
export_to_dot :
	cd webserver && $(JAVA) $(GRAPH_EXPORTER_TARGET) $(ARGS)

# Target test launch all the test
test:
	cd datamodel && $(MVN) test
	cd webserver && $(MVN) test

# Target javadoc_webserver create the javadoc of the webserver and put it in doc/api/webserver
javadoc_webserver:
	rm -rf doc/api/webserver
	mkdir -p doc/api/webserver/
	cd webserver && $(JAVADOC) && mv target/site/apidocs/* ../doc/api/webserver/

# Target javadoc_datamodel create the javadoc of datamodel and put it in doc/api/datamodel
javadoc_datamodel:
	rm -rf doc/api/datamodel/
	mkdir -p doc/api/datamodel/
	cd datamodel && $(JAVADOC) && mv target/site/apidocs/* ../doc/api/datamodel/

# Target javadoc create all the javadoc
javadoc: javadoc_datamodel javadoc_webserver

# Target diagram create a plantuml file which correspond to the diagram class of datamodel
diagram:
	cd datamodel && $(MVN) process-classes

# Target clean removes all files produced during build.
clean :
	cd datamodel && $(MVN) clean
	cd webserver && $(MVN) clean
