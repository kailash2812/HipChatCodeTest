package com.atlassian.codetest.hipchatcodetest;


import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.atlassian.codetest.hipchatcodetest.Activities.MainActivity;

/**
 * Test Activity to do functional testing for MainActivity
 */
public class HipChatCodeTestActivity extends ActivityInstrumentationTestCase2<MainActivity> {

    public HipChatCodeTestActivity() {
        super(MainActivity.class);
    }

    public void testActivityExists() {
        MainActivity activity = getActivity();
        assertNotNull(activity);
    }

    public void testInput() {
        MainActivity activity = getActivity();

        final EditText nameEditText =
                (EditText) activity.findViewById(R.id.editInput);

        // Send string input value
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                nameEditText.requestFocus();
            }
        });

        getInstrumentation().waitForIdleSync();
        getInstrumentation().sendStringSync("Atlassian");
        getInstrumentation().waitForIdleSync();

    }

    public void testEnter() {
        MainActivity activity = getActivity();

        Button enterBtn =
                (Button) activity.findViewById(R.id.enterBtn);

        TouchUtils.clickView(this, enterBtn);

    }

    public void testOutput() {
        MainActivity activity = getActivity();

        TextView output = (TextView) activity.findViewById(R.id.outputTextView);
        String actualText = output.getText().toString();
        assertEquals("", actualText);
    }

}