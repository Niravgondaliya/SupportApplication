package com.support.me.exception;

public abstract class ServiceException extends Exception {
    ServiceException(String expMessage) {
        super(expMessage);
    }
}
