# netcatj
Quick and dirty netcat in Java in a single source file

Two steps to create a jar:
 * wget https://raw.githubusercontent.com/jbrazile/netcatj/master/Netcatj.java
 * javac -source 1.7 -target 1.7 Netcatj.java && jar cef Netcatj netcatj.jar Netcatj*.class
