@ECHO OFF
SETLOCAL

set APP_HOME=%~dp0
set WRAPPER_JAR=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar
set WRAPPER_VERSION=8.2
set WRAPPER_URL=https://repo.gradle.org/gradle/libs-releases-local/org/gradle/gradle-wrapper/%WRAPPER_VERSION%/gradle-wrapper-%WRAPPER_VERSION%.jar
set MAIN_CLASS=org.gradle.wrapper.GradleWrapperMain

if not exist "%WRAPPER_JAR%" (
  for %%I in ("%WRAPPER_JAR%") do set WRAPPER_DIR=%%~dpI
  if not exist "%WRAPPER_DIR%" mkdir "%WRAPPER_DIR%"
  echo Gradle wrapper JAR not found, downloading %WRAPPER_VERSION%...
  where curl >NUL 2>&1
  if %ERRORLEVEL%==0 (
    curl --fail --location --output "%WRAPPER_JAR%.tmp" "%WRAPPER_URL%"
  ) else (
    where wget >NUL 2>&1
    if %ERRORLEVEL%==0 (
      wget --output-document="%WRAPPER_JAR%.tmp" "%WRAPPER_URL%"
    ) else (
      powershell -NoProfile -Command "try { Invoke-WebRequest -Uri '%WRAPPER_URL%' -OutFile '%WRAPPER_JAR%.tmp' -UseBasicParsing } catch { (New-Object System.Net.WebClient).DownloadFile('%WRAPPER_URL%', '%WRAPPER_JAR%.tmp') }"
    )
  )
  if exist "%WRAPPER_JAR%.tmp" (
    move /Y "%WRAPPER_JAR%.tmp" "%WRAPPER_JAR%" >NUL
  ) else (
    echo Failed to download the Gradle wrapper JAR. Ensure curl, wget, or PowerShell is available.
    exit /b 1
  )
)

if not defined JAVA_HOME (
  set JAVA_EXE=java
) else (
  set JAVA_EXE=%JAVA_HOME%\bin\java.exe
)

"%JAVA_EXE%" -cp "%WRAPPER_JAR%" %MAIN_CLASS% %*
ENDLOCAL
