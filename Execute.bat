@echo off

rem Set the path to the lib directory
set LIB_PATH=lib

rem Set the path to the bin directory
set BIN_PATH=bin

rem Compile the Java code with UTF-8 encoding
javac --module-path "%LIB_PATH%" --add-modules javafx.controls,javafx.fxml -encoding UTF-8 -d "%BIN_PATH%" "App.java"

rem Run the compiled Java program with UTF-8 encoding
java --module-path "%LIB_PATH%" --add-modules javafx.controls,javafx.fxml -Dfile.encoding=UTF-8 -cp "%BIN_PATH%" App