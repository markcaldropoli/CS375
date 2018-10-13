JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
        $(JC) $(JFLAGS) $*.java

CLASSES = \
          Program1.java
          
default: Program1

Program1: $(CLASSES:.java=.class)

clean:
        $(RM) *.class
