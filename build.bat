@echo off
setlocal enabledelayedexpansion

Title Building TextBasedPoker...

echo ==================================
echo Compiling Java Project: TextBasedPoker
echo ==================================

REM Remove old build folder if it exists

if exist out (
    echo Cleaning old build
    rmdir /s /q out
)

REM make new out directory
mkdir out

REM ---------------------------------------------------
REM Collect all .java files under src\ and src\poker\
REM CMD can't expand *.java properly so we do it manually
REM ---------------------------------------------------

echo Compiling Project

set SOURCES=

for %%f in (src\poker\*.java) do (
    set SOURCES=!SOURCES! %%f
)

for %%f in (src\*.java) do (
    set SOURCES=!SOURCES! %%f
)

REM Enable delayed expansion for SOURCES concat


javac -d out !SOURCES!

if %errorlevel% neq 0 (
    echo.
    echo Compilation Failed
    echo.
    pause
    exit /b
)

echo.
echo Build Successful
echo.

REM Run main section

set MAIN_CLASS=poker.Game
echo running %MAIN_CLASS%...
echo ==================================

java -cp out %MAIN_CLASS%


echo.

pause
