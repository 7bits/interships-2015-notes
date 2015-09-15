package it.sevenbits.telenote.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andy on 14.09.15.
 */
public class HttpMessage {
    private static Map<Integer, String> map = new HashMap<Integer, String>(){{
        put(403,  "Вы сделали что-то запрещённое для сервера");
        put(404, "Страница не найдена");
        put(405,   "Данное действие сейчас не доступно");
        put(500,   "Ошибка на сервере,\n" + "когда вы вернётесь на главную, всё должно быть в норме");
    }};
    private static String unknownError = "Упс, что-то пошло не так, попробуйте повторить поздней";

    public static String getHttpMessage(Integer statusCode) {
        if(map.containsKey(statusCode)) {
            return map.get(statusCode);
        } else {
            return unknownError;
        }
    }
}
