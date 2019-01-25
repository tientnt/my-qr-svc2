package com.am.common.utils;

import com.google.common.base.CaseFormat;
import play.mvc.Http;

import java.util.HashMap;
import java.util.Map;

public class FormHelper {
    private static final String UNDERSCORE = "_";

    public static Map<String, String> requestDataCamelCase(Http.Request request) {
        Map<String, String[]> urlFormEncoded = new HashMap<>();
        if (request.body().asFormUrlEncoded() != null) {
            urlFormEncoded = request.body().asFormUrlEncoded();
        }

        Map<String, String[]> multipartFormData = new HashMap<>();
        if (request.body().asMultipartFormData() != null) {
            multipartFormData = request.body().asMultipartFormData().asFormUrlEncoded();
        }

        Map<String, String> jsonData = new HashMap<>();
        if (request.body().asJson() != null) {
            jsonData = play.libs.Scala.asJava(
                    play.api.data.FormUtils.fromJson("",
                                                     play.api.libs.json.Json.parse(
                                                             play.libs.Json.stringify(request.body().asJson())
                                                     )
                    )
            );
        }

        Map<String, String> data = new HashMap<>();

        fillDataWith(data, urlFormEncoded);
        fillDataWith(data, multipartFormData);

        fillDataWithMap(data, jsonData);

        if (!request.method().equalsIgnoreCase(Http.HttpVerbs.POST) &&
            !request.method().equalsIgnoreCase(Http.HttpVerbs.PUT) && !request.method().equalsIgnoreCase(
                Http.HttpVerbs.PATCH)) {
            fillDataWith(data, request.queryString());
        }

        return data;
    }

    private static void fillDataWith(Map<String, String> data, Map<String, String[]> urlFormEncoded) {
        urlFormEncoded.forEach((key, values) -> {
            if (key.endsWith("[]")) {
                String k = key.substring(0, key.length() - 2);
                for (int i = 0; i < values.length; i++) {
                    if (k.contains(UNDERSCORE)) {
                        data.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, k) + "[" + i + "]", values[i]);
                    } else {
                        data.put(k + "[" + i + "]", values[i]);
                    }
                }
            } else if (values.length > 0) {
                if (key.contains(UNDERSCORE)) {
                    data.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key), values[0]);
                } else {
                    data.put(key, values[0]);
                }
            }
        });
    }

    private static void fillDataWithMap(Map<String, String> data, Map<String, String> map) {
        map.forEach((key, value) -> {
            if (key.contains(UNDERSCORE)) {
                data.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key), value);
            } else {
                data.put(key, value);
            }
        });
    }
}
