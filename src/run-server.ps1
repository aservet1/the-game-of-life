# run-server.ps1

$Content_Generator_Job = Start-Job {
	while($true) {
		C:\Users\aservett\mycode\the-game-of-life\bin\gameoflife.exe `
			-f 'C:\Users\aservett\mycode\the-game-of-life\public\content.csv' `
			-o 'C:\Users\aservett\mycode\the-game-of-life\tmp.csv' `
			--steps 1
		Get-Content 'C:\Users\aservett\mycode\the-game-of-life\tmp.csv' | Out-File -FilePath 'C:\Users\aservett\mycode\the-game-of-life\public\content.csv'
		Remove-Item 'C:\Users\aservett\mycode\the-game-of-life\tmp.csv'
		Start-Sleep -Seconds 1
	}
}

$_ = Read-Host "Enter any key to stop the content generator server portion."

$Content_Generator_Job | Receive-Job

# $javaserver = "JavaHTTPServer"
# 
# if ( -not $(Test-Path "$javaserver.class") ) {
# 	javac "$javaserver.java"
# }
# 
# try {
# 
# 	ls .
# 
# 	$javaserver = Start-Job {
# 		java -cp . JavaHTTPServer 267
# 	}
# 
# 
# 
# } finally {
# 
# 	$javaserver | Receive-Job -Wait
# 	
# }