package com.boray.main.Util;

import com.boray.Data.Data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class IpConfig {
    public void getIpConfig() {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("ipConfig.properties");
        Properties properties = new Properties();
        try {
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Data.ipPort = "http://" + properties.getProperty("ip") + ":" + properties.getProperty("port") + "/";
        Data.ip = "http://" + properties.getProperty("ip") + "/";
        Data.downloadIp = "http://"+ properties.getProperty("ip") + ":8080/";
    }
}
