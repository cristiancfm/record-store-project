package com.recordstore.exceptions;

public class InvalidInputException extends Exception{
    public InvalidInputException(){
        super("Invalid input (empty or invalid values)");
    }
}
