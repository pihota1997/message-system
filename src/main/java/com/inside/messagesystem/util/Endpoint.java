package com.inside.messagesystem.util;

public enum Endpoint {

    APPLICATION_ENDPOINT(Url.API_ENDPOINT),
    LOGIN_ENDPOINT(Url.LOGIN_ENDPOINT),
    REGISTRATION_ENDPOINT(Url.REGISTRATION_ENDPOINT),
    MESSAGE_ENDPOINT(Url.MESSAGE_ENDPOINT);

    public final String url;

    Endpoint(String url) {
        this.url = url;
    }

    public static class Url {
        public static final String API_ENDPOINT = "/api/v1";
        public static final String LOGIN_ENDPOINT = API_ENDPOINT + "/login";
        public static final String REGISTRATION_ENDPOINT = API_ENDPOINT + "/registration";
        public static final String MESSAGE_ENDPOINT = API_ENDPOINT + "/message";
    }
}
