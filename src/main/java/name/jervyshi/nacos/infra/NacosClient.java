package name.jervyshi.nacos.infra;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Nacos client.
 * @author JervyShi
 * @version $Id : NacosClient.java, v 0.1 2019-02-09 18:49 JervyShi Exp $$
 */
public class NacosClient {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(NacosClient.class);

    private String              baseUrl;

    /**
     * Instantiates a new Nacos client.
     *
     * @param host the host 
     * @param port the port
     */
    public NacosClient(String host, int port) {
        this.baseUrl = String.format("http://%s:%d", host, port);
    }

    /**
     * Is healthy boolean.
     *
     * @return the boolean
     */
    public boolean isHealthy() {
        try {
            URL url = new URL(baseUrl + "/nacos/actuator/health");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(1000);
            urlConnection.setReadTimeout(1000);
            int status = urlConnection.getResponseCode();
            if (status == 200) {
                return true;
            }
        } catch (IOException e) {
        }

        return false;
    }
}
