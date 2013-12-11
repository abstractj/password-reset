package org.abstractj.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

public class URLUtil {


    private static final String CONFIG_FILE = "META-INF/config.properties";
    private static final Properties props;

    static {
        props = new Properties();
        InputStream in = URLUtil.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
        try {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String uri(String id) {

        String url = null;

        try {
            url = props.getProperty("config.url");
            return String.format(url + "%s%s", "reset?id=", URLEncoder.encode(id, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
