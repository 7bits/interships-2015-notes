package it.sevenbits.springboottutorial.web.service;

/**
 * Created by sevenbits on 04.08.15.
 */
public class ExampleService {

    private final String echoFormat;

    public ExampleService(String echoFormat) {
        this.echoFormat = (echoFormat != null) ? echoFormat : "%s";
    }

    public String getMessage(String message) {
        return String.format(this.echoFormat, message);
    }
}
