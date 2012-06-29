package com.technoetic.xplanner.db;

public class QueryException extends Exception {
    public QueryException() {
    }

    public QueryException(Throwable cause) {
        super(cause);
    }

    public QueryException(String message) {
        super(message);
    }

    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
