package fr.swynn.core.impl;

import java.io.Serial;

/**
 * Exception thrown when an error occurs in the home repository.
 */
public class HomeRepositoryException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public HomeRepositoryException(String message) {
        super(message);
    }

    public HomeRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
