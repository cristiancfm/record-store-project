package com.recordstore.controller;


/**
 *   This interface is meant to be implemented by all the controllers
 *   that add information to the database.
 *
 *   It contains one method, {@code areInputsCorrect()}, which should
 *   check if the data written by the user is valid to be stored in the database.
 *
 *   @author Cristian Ferreiro Montoiro
 */
public interface AddControllerInterface {
    void areInputsCorrect() throws Exception;
}
