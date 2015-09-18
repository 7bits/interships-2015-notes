package it.sevenbits.telenote.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andy on 14.09.15.
 */
@Component
public class HttpMessage {
    @Autowired
    private MessageSource messageSource;

    private Map<Integer, String> map = new HashMap<>();

    public String getHttpMessage(Integer statusCode) {
        if (map.isEmpty()) {
            map.put(403, messageSource.getMessage("message.error.403", null,  LocaleContextHolder.getLocale()));
            map.put(404, messageSource.getMessage("message.error.404", null,  LocaleContextHolder.getLocale()));
            map.put(405, messageSource.getMessage("message.error.405", null,  LocaleContextHolder.getLocale()));
            map.put(500, messageSource.getMessage("message.error.500", null,  LocaleContextHolder.getLocale()));
        }

        if(map.containsKey(statusCode)) {
            return map.get(statusCode);
        } else {
            return messageSource.getMessage("message.error.unknown", null,  LocaleContextHolder.getLocale());
        }
    }
}
