package com.javaclasses.todolist.webapp;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpHeaders.USER_AGENT;

/**
 * Util methods for web tests
 */

/*package*/ final class TestUtils {

    // URL constants
    private static final String URL = "http://localhost:8080/api/";
    private static final String REGISTRATION_URL = URL + "register";

    // Parameter constants
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String CONFIRM_PASSWORD = "confirmPassword";


    private TestUtils() {
    }

    /*package*/ static String getResponseContent(HttpEntity httpEntity) throws IOException {

        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(httpEntity.getContent()));

        final StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    /*package*/ static HttpEntity registerUser(String nickname, String password, String confirmPassword)
            throws IOException {

        final List<NameValuePair> registrationUrlParameters = new ArrayList<>();
        registrationUrlParameters.add(new BasicNameValuePair(EMAIL, nickname));
        registrationUrlParameters.add(new BasicNameValuePair(PASSWORD, password));
        registrationUrlParameters.add(new BasicNameValuePair(CONFIRM_PASSWORD, confirmPassword));

        return generatePostResponse(REGISTRATION_URL, registrationUrlParameters);
    }

    private static HttpEntity generatePostResponse(String url, List<NameValuePair> urlParameters)
            throws IOException {

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(url);
        post.setHeader("User-Agent", USER_AGENT);
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        final HttpResponse response = client.execute(post);
        return response.getEntity();
    }
}
