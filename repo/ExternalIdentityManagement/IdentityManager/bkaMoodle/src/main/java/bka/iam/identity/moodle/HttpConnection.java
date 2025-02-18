package bka.iam.identity.moodle;

import org.identityconnectors.framework.common.exceptions.ConnectionBrokenException;
import org.identityconnectors.framework.common.exceptions.ConnectionFailedException;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpConnection {
    private static final Logger logger = Logger.getLogger(HttpConnection.class.getName());

    private MoodleConfiguration config;
    private HttpURLConnection conn;

    public HttpConnection(MoodleConfiguration config, String wsfunction, Map<String, String> params) {
        this.config = config;

        URL url = null;
        try {
            String urlStr = buildFinalUrl(wsfunction, params);
            logger.info("Creating request to: " + wsfunction);

            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            logger.finest("Connection to Moodle prepared.");

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create a connection to Moodle.");
            throw ConnectorException.wrap(e);
        }
    }

    private String buildFinalUrl(String wsfunction, Map<String, String> params) {
        StringBuilder url = new StringBuilder().append(this.config.getBaseUrl() + "&wsfunction=" + wsfunction);

        if (params == null) {
            return url.toString();
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            url.append("&" + entry.getKey() + "=" + entry.getValue());
        }

        return url.toString();
    }

    public void sendRequest(String method, String body) {
        logger.finer("Sending request to Moodle. METHOD=" + method);
        try {
            conn.setRequestMethod(method);
            conn.setUseCaches(false);

            if (! method.equals("GET")) {
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(body);
                wr.flush();
                wr.close();
            }

            try {
                conn.connect(); // If Moodle server is down, this is where it errors
            }catch (IOException e) {
                String err = "Moodle - Unable to establish connection. Server might be down";
                logger.log(Level.WARNING, err, e);
                throw new ConnectionFailedException(err, e);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                String err = "Moodle - Unexpected response code from the server. This is likely a development error or something has changed with the topology. Response was: " + responseCode;
                logger.severe(err);
                throw new ConnectionBrokenException(err);
            }


        } catch (IOException e) {
            String err = "Moodle - Something unexpected went wrong with the request. Something might be unexpected about the actual data we're sending.";
            logger.log(Level.SEVERE, err, e);
            throw new ConnectionBrokenException(err, e);
        }
    }

    public String getResponse() throws IOException {

        String response = "";
        Scanner scanner = new Scanner(conn.getInputStream());

        //Write all the JSON data into a string using a scanner
        while (scanner.hasNext()) {
            response += scanner.nextLine();
        }

        return response;
    }

    public void dispose() {
        logger.finest("dispose()...");
        // From the JavaDocs: "Indicates that other requests to the server are unlikely in the near future."
        // I suspect this may be better unclosed for bulk recon. Re-explore this if desiring better performance
        conn.disconnect();
    }
}
