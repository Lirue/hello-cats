package com.martynatyran.domain.cat.exception;

/**
 * Am exception class indicating that entity is not
 * persisted yet in database and therefore cannot be
 * used.
 */
public class EntityInStateNewException
        extends RuntimeException {

    private static final long serialVersionUID = 8615255199251864095L;

    public EntityInStateNewException() {
        super("Entity is not persisted yet and cannot be used");
    }

}
