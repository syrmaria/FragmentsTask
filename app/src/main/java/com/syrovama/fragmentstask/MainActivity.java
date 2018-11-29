package com.syrovama.fragmentstask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

/*
Holds BroadcastReceiver to get service results
Interacts with fragments in updateEditText, onEditTextChanged, onButtonPressed methods
 */

public class MainActivity extends FragmentActivity implements TextFragment.EditTextCallback, ButtonFragment.ButtonPressedCallback {
    private TextFragment mTextFragment;
    private ButtonFragment mButtonFragment;
    private String mTextValue;
    private NumberReceiver numberReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextFragment = (TextFragment) addAndGetFragment(R.id.fragmentTextContainer);
        mTextFragment.setEditTextCallback(this);
        mButtonFragment = (ButtonFragment) addAndGetFragment(R.id.fragmentButtonContainer);
        mButtonFragment.setButtonPressedCallback(this);
    }

    private Fragment addAndGetFragment(int id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(id);
        if (fragment == null) {
            if (id == R.id.fragmentTextContainer) {
                fragment = new TextFragment();
            } else {
                fragment = new ButtonFragment();
            }
            fragmentManager.beginTransaction()
                    .add(id, fragment)
                    .commit();
        }
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        numberReceiver = new NumberReceiver(new Handler());
        IntentFilter intentFilter = new IntentFilter(RandomService.ACTION);
        registerReceiver(numberReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        unregisterReceiver(numberReceiver);
        super.onPause();
    }

    @Override
    public void onEditTextChanged(String result) {
        mTextValue = result;
    }

    @Override
    public void onButtonPressed() {
        mButtonFragment.setTextView(mTextValue);
    }

    private void updateEditText() {
        mTextFragment.setEditTextValue(mTextValue);
    }

    public class NumberReceiver extends BroadcastReceiver {
        private final Handler handler;

        public NumberReceiver(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Integer number = intent.getIntExtra(RandomService.EXTRA_NUMBER, 0);
            mTextValue = number.toString();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    updateEditText();
                }
            });
        }
    }
}
