$previous=$env:JAVA_HOME
try {
 $env:JAVA_HOME='C:\Users\Asus\.jdks\ms-17.0.20.1'
 Push-Location $PSScriptRoot
 try {
  if($args.Count -eq 0) { & 'C:\Program Files\JetBrains\IntelliJ IDEA 2026.1.4\plugins\maven\lib\maven3\bin\mvn.cmd' clean test }
  else { & 'C:\Program Files\JetBrains\IntelliJ IDEA 2026.1.4\plugins\maven\lib\maven3\bin\mvn.cmd' @args }
  $result=$LASTEXITCODE
 } finally { Pop-Location }
} finally { $env:JAVA_HOME=$previous }
exit $result
