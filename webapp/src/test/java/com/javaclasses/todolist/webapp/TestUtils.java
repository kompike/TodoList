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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.http.HttpHeaders.USER_AGENT;

/**
 * Util methods for web tests
 */

/*package*/ final class TestUtils {

    // URL constants
    private static final String URL = "http://localhost:8080/api/";
    private static final String REGISTRATION_URL = URL + "register";
    private static final String LOGIN_URL = URL + "login";
    private static final String TASK_ADDITION_URL = URL + "tasks";
    private static final String TASK_COMPLETION_URL = URL + "tasks/update";
    private static final String TASK_REOPENING_URL = URL + "tasks/reopen";
    private static final String TASK_DELETION_URL = URL + "tasks/delete";

    // Parameter constants
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String CONFIRM_PASSWORD = "confirmPassword";
    private static final String TOKEN_ID = "tokenId";
    private static final String DESCRIPTION = "description";
    private static final String TASK_ID = "taskId";


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

    /*package*/ static HttpEntity registerUser(String email, String password, String confirmPassword)
            throws IOException {

        final List<NameValuePair> registrationUrlParameters = new ArrayList<>();
        registrationUrlParameters.add(new BasicNameValuePair(EMAIL, email));
        registrationUrlParameters.add(new BasicNameValuePair(PASSWORD, password));
        registrationUrlParameters.add(new BasicNameValuePair(CONFIRM_PASSWORD, confirmPassword));

        return generatePostResponse(REGISTRATION_URL, registrationUrlParameters);
    }

    /*package*/ static HttpEntity loginUser(String email, String password) throws IOException {

        final List<NameValuePair> loginUrlParameters = new ArrayList<>();
        loginUrlParameters.add(new BasicNameValuePair(EMAIL, email));
        loginUrlParameters.add(new BasicNameValuePair(PASSWORD, password));

        return generatePostResponse(LOGIN_URL, loginUrlParameters);
    }

    /*package*/ static HttpEntity addNewTask(String tokenId, String description)
            throws IOException {

        final List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair(TOKEN_ID, tokenId));
        urlParameters.add(new BasicNameValuePair(DESCRIPTION, description));

        return generatePostResponse(TASK_ADDITION_URL, urlParameters);
    }

    /*package*/ static HttpEntity completeTask(String tokenId, String taskId)
            throws IOException {

        final List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair(TOKEN_ID, tokenId));
        urlParameters.add(new BasicNameValuePair(TASK_ID, taskId));

        return generatePostResponse(TASK_COMPLETION_URL, urlParameters);
    }

    /*package*/ static HttpEntity reopenTask(String tokenId, String taskId)
            throws IOException {

        final List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair(TOKEN_ID, tokenId));
        urlParameters.add(new BasicNameValuePair(TASK_ID, taskId));

        return generatePostResponse(TASK_REOPENING_URL, urlParameters);
    }

    /*package*/ static HttpEntity deleteTask(String tokenId, String taskId)
            throws IOException {

        final List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair(TOKEN_ID, tokenId));
        urlParameters.add(new BasicNameValuePair(TASK_ID, taskId));

        return generatePostResponse(TASK_DELETION_URL, urlParameters);
    }

    /*package*/ static String getParameterFromResponse(HttpEntity httpEntity, String parameter)
            throws IOException {
        final String loginResponse = getResponseContent(httpEntity);
        final Pattern pattern = Pattern.compile(String.format("\"%s\":\"(\\d+|\\w+)\"", parameter));
        final Matcher matcher = pattern.matcher(loginResponse);
        if (matcher.find()) {
            return matcher.group(matcher.groupCount());
        }

        return "";
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
