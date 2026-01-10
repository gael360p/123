@echo off
setlocal

set DIR=%~dp0
set WRAPPER_DIR=%DIR%gradle\wrapper
set WRAPPER_JAR=%WRAPPER_DIR%\gradle-wrapper.jar
set WRAPPER_URL=https://services.gradle.org/distributions/gradle-9.2.1-wrapper.jar

if not exist "%WRAPPER_JAR%" (
  echo Missing %WRAPPER_JAR%
  echo Downloading Gradle wrapper jar...
  if not exist "%WRAPPER_DIR%" mkdir "%WRAPPER_DIR%"
  powershell -NoProfile -ExecutionPolicy Bypass -Command ^
    "try { Invoke-WebRequest -Uri '%WRAPPER_URL%' -OutFile '%WRAPPER_JAR%' } catch { exit 1 }"
  if not exist "%WRAPPER_JAR%" (
    echo Failed to download %WRAPPER_URL%
    exit /b 1
  )
)

java -classpath "%WRAPPER_JAR%" org.gradle.wrapper.GradleWrapperMain %*
endlocal
