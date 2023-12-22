@echo off

rem Compile the Java code
javac --module-path "lib" --add-modules javafx.controls,javafx.fxml -d "bin" "app.java"

rem Run the compiled Java program
java --module-path "lib" --add-modules javafx.controls,javafx.fxml -cp "bin" app

pause
