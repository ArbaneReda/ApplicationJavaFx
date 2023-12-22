#!/bin/bash

# Set the path to the lib directory
LIB_PATH="lib"

# Set the path to the bin directory
BIN_PATH="bin"

# Compile the Java code with UTF-8 encoding
javac --module-path "$LIB_PATH" --add-modules javafx.controls,javafx.fxml -encoding UTF-8 -d "$BIN_PATH" "App.java"

# Run the compiled Java program with UTF-8 encoding
java --module-path "$LIB_PATH" --add-modules javafx.controls,javafx.fxml -Dfile.encoding=UTF-8 -cp "$BIN_PATH" App

# Keep the terminal open after execution (for debugging purposes)
read -p "Press Enter to exit"