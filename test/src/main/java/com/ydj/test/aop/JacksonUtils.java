package com.ydj.test.aop;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class JacksonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonUtils.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JacksonUtils() {
    }

    static {
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //MAPPER.enable(SerializationFeature.INDENT_OUTPUT);

        MAPPER.disable(SerializationFeature.FLUSH_AFTER_WRITE_VALUE);
        MAPPER.disable(SerializationFeature.CLOSE_CLOSEABLE);
        MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        MAPPER.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        MAPPER.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        MAPPER.configure(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT, false);
    }

    public static <T> T objectToBean(Object object, Class<T> clazz) {
        if (object == null || clazz == null) {
            LOGGER.error("param is null! object: {}, clazz: {}", object, clazz);
            return null;
        }
        try {
            return stringToBean(MAPPER.writeValueAsString(object), clazz);
        } catch (Exception e) {
            LOGGER.error("convert bean exception! object: {}, clazz: {}", object, clazz, e);
            return null;
        }
    }

    /**
     * map 集合转换对象
     *
     * @param map   待转换map集合
     * @param clazz 转换对象 class 类型
     * @param <T>   转换对象范型
     * @return 转换后对象
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
        if (map == null || map.isEmpty() || clazz == null) {
            LOGGER.error("param is null! map: {}, clazz: {}", map, clazz);
            return null;
        }
        try {
            return stringToBean(MAPPER.writeValueAsString(map), clazz);
        } catch (Exception e) {
            LOGGER.error("convert bean exception! map: {}, clazz: {}", map, clazz, e);
            return null;
        }
    }

    /**
     * 字符串转换对象
     *
     * @param s     待转换字符串
     * @param clazz 转换对象 class 类型
     * @param <T>   转换对象范型
     * @return 转换后对象
     */
    public static <T> T stringToBean(String s, Class<T> clazz) throws IOException {
        if (s == null || s.trim().length() == 0 || clazz == null) {
            LOGGER.error("param is null! s: {}, clazz: {}", s, clazz);
            return null;
        }
        try {
            return MAPPER.readValue(s, MAPPER.getTypeFactory().constructType(clazz));
        } catch (JsonProcessingException e) {
            LOGGER.error("convert bean exception! s: {}, clazz: {}", s, clazz, e);
            return null;
        }
    }

    /**
     * 对象转换字符串
     *
     * @param o 待转换对象
     * @return 转换后字符串
     */
    public static String beanToString(Object o) {
        if (o == null) {
            LOGGER.error("param is null! object: {}", o);
            return null;
        }
        try {
            return MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            LOGGER.error("convert bean exception! object: {}", o, e);
            return null;
        }
    }

    /**
     * 字符串转换对象 list
     *
     * @param s     待转换字符串
     * @param clazz 转换对象 class 类型
     * @param <T>   转换对象范型
     * @return 转换后对象 list
     */
    public static <T> List<T> stringToBeanList(String s, Class<T> clazz) throws IOException {
        if (s == null || s.trim().length() == 0 || clazz == null) {
            LOGGER.error("param is null! s: {}, clazz: {}", s, clazz);
            return null;
        }

        try {
            return MAPPER.readValue(s, MAPPER.getTypeFactory().constructParametricType(ArrayList.class, clazz));
        } catch (JsonProcessingException e) {
            LOGGER.error("convert bean list exception! s: {}, clazz: {}", s, clazz, e);
            return null;
        }
    }
}