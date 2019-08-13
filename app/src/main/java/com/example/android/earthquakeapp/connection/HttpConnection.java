package com.example.android.earthquakeapp.connection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class HttpConnection {
    /**
     * create http connection handler
     *
     * @param url {@code String} URL to connect
     * @throws MalformedURLException throw if url is malformed
     */
    public HttpConnection(@NonNull final String url) throws MalformedURLException {
        this.mUrl = new URL(url);
    }

    /**
     * sync send a GET request and wait for response
     *
     * @return {@code String} response from URL
     * @throws HttpException throw if there are problem
     */
    public String makeHttpGetRequest() throws HttpException {
        String jsonResponse = null;

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) this.mUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                try (InputStream inputStream = urlConnection.getInputStream()) {
                    jsonResponse = readFromStream(inputStream);
                }
            } else {
                throw new HttpException(urlConnection.getResponseCode(), urlConnection.getResponseMessage());
            }
        } catch (IOException e) {
            throw new HttpException(e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return jsonResponse;
    }

    public class HttpException extends Exception {
        /**
         * constructor for http error code
         * @param statusCode int for http error (i.e. 404, 400,...)
         * @param statusMessage {@code String} with message error
         */
        HttpException(final int statusCode, @Nullable final String statusMessage) {
            super(String.format("Problem with HttpURLConnection response code: %1$s, with message %2$s", statusCode, statusMessage));
            this.mStatusCode = statusCode;
            this.mStatusMessage = statusMessage;

        }

        /**
         * constructor for exception
         * @param throwable original exception
         */
        HttpException(@NonNull final Throwable throwable) {
            super(String.format("Problem with HttpURLConnection from another exception %1$s with message %2$s", throwable.getClass(), throwable.getMessage()));
            this.setStackTrace(throwable.getStackTrace());
            this.mStatusCode = -1;
            this.mStatusMessage = null;
        }


        public int getStatusCode() {
            return mStatusCode;
        }

        public String getStatusMessage() {
            return mStatusMessage;
        }

        private final int mStatusCode;
        private final String mStatusMessage;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        final StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();
        }
        return output.toString();
    }

    private final URL mUrl;

    private static final String TAG = HttpConnection.class.getSimpleName();
}
