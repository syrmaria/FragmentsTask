package com.syrovama.fragmentstask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ButtonFragment extends Fragment {
    private Button mButton;
    private ButtonPressedCallback mCallback;
    private ChildFragment mChildFragment;

    public interface ButtonPressedCallback {
        void onButtonPressed();
    }

    public void setButtonPressedCallback(ButtonPressedCallback activity) {
        mCallback = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_with_button, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager manager = getChildFragmentManager();
        mChildFragment =  (ChildFragment)manager.findFragmentById(R.id.childFragment);
        mButton = view.findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onButtonPressed();
            }
        });
    }

    public void setTextView(String textValue) {
        mChildFragment.setTextView(textValue);
    }
}
