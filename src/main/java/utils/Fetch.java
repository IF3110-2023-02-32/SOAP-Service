package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Fetch {
    private String url = "";
    private String method = "GET";
    private String[] headers = null;
    private String body = "";

    public Fetch(String url) {
        this.url = url;
    }

    public Fetch method(String method) {
        this.method = method;
        return this;
    }

    public Fetch headers(String[] headers) {
        this.headers = headers;
        return this;
    }

    public Fetch body(String body) {
        this.body = body;
        return this;
    }

    public String send() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(this.url).openConnection();
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("Accept", "text/plain");
            connection.setDoOutput(true);
            connection.setRequestMethod(this.method);
            if (this.headers != null) {
                for (String header : this.headers) {
                    String[] headerSplit = header.split(":");
                    connection.setRequestProperty(headerSplit[0], headerSplit[1]);
                }
            }
            if (this.body != "") {
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = this.body.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
