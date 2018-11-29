package com.syrovama.fragmentstask;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import java.util.Timer;
import java.util.TimerTask;

/*
Holds Timer to start service every 15 seconds
Retained
Calls activity when user changes EditText value
 */

public class TextFragment extends Fragment {
    private static final String TAG = "TextFragment";
    private EditText mEditText;
    private Timer mTimer;
    private EditTextCallback mCallback;
    private Activity mParent;

    public interface EditTextCallback {
        void onEditTextChanged(String result);
    }

    public void setEditTextCallback(EditTextCallback activity) {
        mCallback = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                RandomService.enqueueWork(getActivity(), RandomService.newIntent(getActivity()));
            }
        }, 0, 15000);
        Log.d(TAG, "Timer set");
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_with_text, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mParent = getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEditText = view.findViewById(R.id.edit_text);
        mEditText.addTextChangedListener(new UserInputWatcher());
    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
        mParent.stopService(RandomService.newIntent(mParent));
        super.onDestroy();
    }

    public void setEditTextValue(String textValue) {
        mEditText.setText(textValue);
    }

    private class UserInputWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mCallback.onEditTextChanged(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
