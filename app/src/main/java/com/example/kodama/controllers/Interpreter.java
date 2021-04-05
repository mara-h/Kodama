package com.example.kodama.controllers;

import org.jetbrains.annotations.NotNull;

import java.io.File;


public class Interpreter(@NotNull File modelFile){
    try (Interpreter interpreter = new Interpreter(file_of_a_tensorflowlite_model)) {
        interpreter.run(input, output);
    }
}
