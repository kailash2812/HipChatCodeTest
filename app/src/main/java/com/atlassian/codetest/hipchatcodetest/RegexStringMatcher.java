package com.atlassian.codetest.hipchatcodetest;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RegexStringMatcher is the helper class to retrieve mentions, emoticons and url from the given
 * input string
 */
public class RegexStringMatcher {

    //Tag for Logging
    private static String TAG = RegexStringMatcher.class.getName();

    public static String HOST_UNRESOLVED = "Unable to resolve host";

    //Pattern for gathering @mentiones from the input string
    public static Pattern mentionsPattern = Pattern.compile("@([a-zA-Z_0-9.+]+)");

    //Pattern for paranthesis - emoticons
    public static Pattern emoticonsPattern = Pattern.compile("\\((\\w{1,15}+)\\)", Pattern.DOTALL);

    //Pattern for gathering urls from the input string
    public static Pattern UrlPattern =
            Pattern.compile("\\b(([\\w-]+://?|www[.])[^\\s()<>]+(?:\\([\\w\\d]+\\)|([^[:punct:]\\s]|/)))");

    //Pattern for gathering title from the url
    private static final Pattern TITLE_TAG =
            Pattern.compile("\\<title>(.*)\\</title>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private static Matcher matcher;

    public static JSONArray titleArray;

    /**
     * Method to return mentions from input
     *
     * @param input
     * @return JSONArray of mentions
     */
    public static JSONArray getMentions(String input) {
        matcher = mentionsPattern.matcher(input);
        JSONArray mentionsArray = new JSONArray();

        while (matcher.find()) {
            mentionsArray.put(matcher.group(1));
        }

        return mentionsArray;
    }

    /**
     * Method to return emoticons
     *
     * @param input
     * @return JSONArray of emoticons
     */
    public static JSONArray getEmoticons(String input) {
        matcher = emoticonsPattern.matcher(input);
        JSONArray emoticonsArray = new JSONArray();

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                emoticonsArray.put(matcher.group(1));
            }
        }
        return emoticonsArray;
    }

    /**
     * Method to get urls from the input string
     *
     * @param input
     * @return JSON Array of urls
     */
    public static JSONArray getUrls(String input) {
        matcher = UrlPattern.matcher(input);
        JSONArray urlArray = new JSONArray();
        titleArray = new JSONArray();
        while (matcher.find()) {
            urlArray.put(matcher.group(1));
        }

        return urlArray;
    }

    /**
     * Method to get Titles from the input
     *
     * @param input
     * @param onTaskCompleted Callback
     */
    public static void getTitles(String input, OnTaskCompleted onTaskCompleted) {
        matcher = UrlPattern.matcher(input);
        titleArray = new JSONArray();
        while (matcher.find()) {
            new RetrievePageTitleTask(onTaskCompleted).execute(matcher.group(1));
        }
    }


    /**
     * Async Task to retrieve titles from url
     */
    public static class RetrievePageTitleTask extends AsyncTask<String, Void, String> {
        final OnTaskCompleted callback;

        RetrievePageTitleTask(OnTaskCompleted callback) {
            this.callback = callback;
        }

        protected String doInBackground(String... urls) {
            try {
                String url = null;
                if (!(urls[0].startsWith("http") || urls[0].startsWith("https"))) {
                    url = "http://" + urls[0];
                } else {
                    url = urls[0];
                }
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(url);
                // Get the response
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String response_str = client.execute(request, responseHandler);

                // extract the title
                Matcher matcher = TITLE_TAG.matcher(response_str);
                if (matcher.find()) {
                /* replace any occurrences of whitespace (which may
                 * include line feeds and other uglies) as well
                 * as HTML brackets with a space */
                    return matcher.group(1).replaceAll("[\\s\\<>]+", " ").trim();
                } else {
                    return HOST_UNRESOLVED;
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } catch (RuntimeException e) {
                Log.e(TAG, e.getMessage());
            }
            return HOST_UNRESOLVED;
        }

        protected void onPostExecute(String pageTitle) {
            titleArray.put(pageTitle);
            callback.onTaskCompleted(titleArray);
        }
    }

}