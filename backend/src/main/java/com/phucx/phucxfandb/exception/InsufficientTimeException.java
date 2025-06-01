package com.phucx.phucxfandb.exception;

public class InsufficientTimeException extends RuntimeException {
    private final Integer maxWalkInDurationMinutes;

    public InsufficientTimeException(String message, Integer maxWalkInDurationMinutes) {
      super(message);
      this.maxWalkInDurationMinutes = maxWalkInDurationMinutes;
    }

    public Integer getMaxWalkInDurationMinutes() {
      return maxWalkInDurationMinutes;
    }
}
