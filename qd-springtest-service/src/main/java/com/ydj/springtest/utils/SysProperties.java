package com.ydj.springtest.utils;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-21 下午05:46:04
 * @version : 1.0
 * @description :
 *
 */
public class SysProperties {

	private static Properties properties;


    static {
        properties = new Properties();
		InputStream is = null;

        try {
//          is = SysProperties.class.getClassLoader().getResourceAsStream("/META-INF/app.properties");
            is = SysProperties.class.getResourceAsStream("/META-INF/app.properties");
            properties.load(is);
            is.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }

        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String name = (String) enumeration.nextElement();
            String value = properties.getProperty(name);
            System.out.println(name+" = "+value);
            if (value != null) {
                System.setProperty(name, value);
            }
        }
		
	}

    public static String getAppName() {
        String appName = properties.getProperty("app.name");
        if (appName == null || "".equals(appName)){
            appName = "defAppName";
        }
        return appName;
    }

    public static String getProperty(String name) {
        return properties.getProperty(name);
    }

    public static String getProperty(String name, String defaultValue) {
        return properties.getProperty(name, defaultValue);
    }
    

}