.PHONY: all run clean

MVN=mvn
CLASSPATH=./target/*.jar
JAVA_OPT=-cp $(CLASSPATH)
JAVA=java $(JAVA_OPT)
TARGET=fr.univparis.metro.App

# Target all builds the project.
all:
	$(MVN) package

# Target run executes the program and start with target all to build the
# project.
run : all
	$(JAVA) $(TARGET)

# Target clean removes all files produced during build.
clean :
	$(MVN) clean
