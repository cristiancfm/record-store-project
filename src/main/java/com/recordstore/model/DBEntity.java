package com.recordstore.model;

/**
 *   This interface is meant to be implemented by all the entities
 *   of the database.
 *
 *   It contains one method, {@code getInfo()}, which should return
 *   a String with all the fields of the entity and its values.
 *
 *   @author Cristian Ferreiro Montoiro
 */
public interface DBEntity {
    String getInfo();
}
