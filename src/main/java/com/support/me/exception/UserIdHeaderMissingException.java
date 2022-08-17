package com.support.me.exception;

public class UserIdHeaderMissingException
    extends
    ServiceException {

    public UserIdHeaderMissingException(String expMessage) {
        super(expMessage);
    }

}
