.PHONY: all datamodel webserver run clean

MVN=mvn
CLASSPATH=./target/*.jar
JAVA_OPT=-cp $(CLASSPATH)
JAVA=java $(JAVA_OPT)
JAVADOC=$(MVN) javadoc:javadoc
TARGET=fr.univparis.metro.Webserver
TERMINAL_TARGET=fr.univparis.metro.App

# Target all builds the project.
all: datamodel webserver

# Target run executes the program and start with target all to build the
# project.
datamodel:
	cd datamodel && $(MVN) package install

webserver:
	cd webserver && $(MVN) compile test assembly:single

run :
	cd webserver && $(JAVA) $(TARGET)


run_terminal :
	cd webserver && $(JAVA) $(TERMINAL_TARGET)


test:
	cd datamodel && $(MVN) test
	cd webserver && $(MVN) test

javadoc_webserver:
	cd webserver && $(JAVADOC)

javadoc_datamodel:
	cd datamodel && $(JAVADOC)

javadoc: javadoc_datamodel javadoc_webserver
# Target clean removes all files produced during build.
clean :
	cd datamodel && $(MVN) clean
	cd webserver && $(MVN) clean
