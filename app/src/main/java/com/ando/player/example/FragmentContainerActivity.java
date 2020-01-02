package com.ando.player.example;

import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ando.player.R;

public class FragmentContainerActivity extends AppCompatActivity {
    public static final String FRAGMENT_NAME = "FragmentContainerActivity.fragmentName";
    public static final String BUNDLE = "FragmentContainerActivity.bundle";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fragment_container);

        openFragment();
    }

    private void openFragment() {
        String fragmentName = getIntent().getStringExtra(FRAGMENT_NAME);
        Fragment fragment = null;
        try {
            fragment = (Fragment) Class.forName(fragmentName).newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (fragment == null) {
            return;
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        inTransaction(fragment);
    }

    private void inTransaction(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, fragment);
        transaction.commitAllowingStateLoss();
        //supportFragmentManager.executePendingTransactions()
    }

}