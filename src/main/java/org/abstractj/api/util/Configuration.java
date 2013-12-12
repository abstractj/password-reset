package org.abstractj.api.util;

import javax.servlet.FilterConfig;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

public class Configuration {


    private static final String CONFIG_FILE = "META-INF/config.properties";
    private static final Properties props;

    private static String applicationUrl;
    private static String redirectPage;

    static {
        props = new Properties();
        InputStream in = Configuration.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
        try {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadFilterConfig(FilterConfig config) {
        Configuration.applicationUrl = config.getInitParameter("url");
        Configuration.redirectPage = config.getInitParameter("redirect-page");
    }

    public static String uri(String id) {

        try {
            return String.format(applicationUrl + "%s%s", "reset?id=", URLEncoder.encode(id, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static String getSecret() {
        return props.getProperty("config.secret");
    }

    public static String getRedirectPage() {
        return redirectPage;
    }


}
