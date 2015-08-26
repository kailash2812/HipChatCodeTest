package com.atlassian.codetest.hipchatcodetest;

import org.json.JSONArray;

/**
 * interface being used as callback for retrieving title
 */
public interface OnTaskCompleted {
    void onTaskCompleted(JSONArray result);
}
