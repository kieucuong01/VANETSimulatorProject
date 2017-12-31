These classes are needed for backwards compatibility with Java 1.5.


The classes are taken from OpenJDK 1.6 (openjdk-6-src-b11-10_jul_2008.tar.gz from http://download.java.net/openjdk/jdk6/ , 
last visited on 10.09.2008). The files are licenced under GPL (see file headers).
The following changes were made to the files:
- supress some warnings
- change path of java.* to java16.* as the security settings of Java JRE would not allow usage otherwise.