package com.laboacamedy.docyline.exception;

/** Exception levee lorsque la requete du client est incoherente ou refusee metier (-> HTTP 400). */
public class RequeteInvalideException extends RuntimeException {
    public RequeteInvalideException(String message) {
        super(message);
    }
}
