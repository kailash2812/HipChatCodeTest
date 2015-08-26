package com.atlassian.codetest.hipchatcodetest.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.atlassian.codetest.hipchatcodetest.OnTaskCompleted;
import com.atlassian.codetest.hipchatcodetest.R;
import com.atlassian.codetest.hipchatcodetest.RegexStringMatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents Main Fragment to enter input, pattern match the input and get the output.
 */
public class MainFragment extends Fragment {

    //Tag for logging
    public static String TAG = MainFragment.class.getName();

    //Constants
    public static String MENTIONS = "mentions";
    public static String EMOTICONS = "emoticons";
    public static String TITLE = "title";
    public static String URL = "url";
    public static String LINKS = "links";


    private EditText inputEditText;
    private TextView outputView;
    private Button enterBtn;
    private Context context;
    private JSONObject totalObject;
    private JSONArray mentionsArray, emoticonsArray, urlsArray, titleArray, linksArray;

    public MainFragment() {

    }

    public static MainFragment newInstance(Context context) {
        MainFragment fragment = new MainFragment();
        fragment.context = context;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        inputEditText = (EditText) view.findViewById(R.id.editInput);
        outputView = (TextView) view.findViewById(R.id.outputTextView);
        enterBtn = (Button) view.findViewById(R.id.enterBtn);

        enterBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String inputText = inputEditText.getText().toString();
                        outputView.setText("Waiting for Output...");

                        InputMethodManager imm = (InputMethodManager) context.
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                        mentionsArray = new JSONArray();
                        emoticonsArray = new JSONArray();
                        urlsArray = new JSONArray();
                        titleArray = new JSONArray();
                        linksArray = new JSONArray();


                        totalObject = new JSONObject();
                        mentionsArray = RegexStringMatcher.getMentions(inputText);
                        emoticonsArray = RegexStringMatcher.getEmoticons(inputText);
                        urlsArray = RegexStringMatcher.getUrls(inputText);

                        try {
                            if (mentionsArray.length() > 0) {
                                totalObject.put(MENTIONS, mentionsArray);
                            }
                            if (emoticonsArray.length() > 0) {
                                totalObject.put(EMOTICONS, emoticonsArray);
                            }

                            if (urlsArray.length() > 0) {
                                RegexStringMatcher.getTitles(inputText, new OnTaskCompleted() {
                                    @Override
                                    public void onTaskCompleted(JSONArray result) {
                                        try {
                                            if (result.length() > 0 && urlsArray.length() > 0) {
                                                titleArray = result;
                                                for (int i = 0; i < urlsArray.length(); i++) {
                                                    JSONObject obj = new JSONObject();
                                                    obj.put(URL, urlsArray.get(i).toString().replace("\\",""));
                                                    obj.put(TITLE, titleArray.get(i));
                                                    linksArray.put(obj);
                                                    if (linksArray.length() > 0) {
                                                        totalObject.put(LINKS,linksArray);
                                                    }
                                                }
                                            }

                                            String output = totalObject.toString(4).replace("\\","");

                                            outputView.setText(output);
                                        } catch (JSONException e) {
                                            Log.e(TAG, e.getMessage());
                                        }
                                    }
                                });
                            } else {
                                outputView.setText(totalObject.toString(4));
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }
        );

        return view;
    }

}
