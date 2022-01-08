package com.recordstore.exceptions;

public class ArtistNotFoundException extends Exception{
    public ArtistNotFoundException() {
        super("Artist ID not found in database"); }
}
