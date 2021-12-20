# run-server.ps1

$javaserver = "JavaHTTPServer"

if ( -not $(Test-Path "$javaserver.class") ) {
	javac "$javaserver.java"
}