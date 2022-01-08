package com.recordstore.exceptions;

public class NoRowSelectedException extends Exception{
    public NoRowSelectedException() {
        super("Please select at least one row to delete"); }
}
