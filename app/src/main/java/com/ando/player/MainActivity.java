package com.ando.player;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ando.player.example.CustomPlayerFragment;
import com.ando.player.example.FragmentContainerActivity;
import com.ando.player.example.FullScreenPlayerFragment;
import com.ando.player.example.PlayerFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //通用
        findViewById(R.id.bt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new PlayerFragment());
            }
        });

        //直接全屏
        findViewById(R.id.bt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FragmentContainerActivity.class);
                intent.putExtra(FragmentContainerActivity.FRAGMENT_NAME, FullScreenPlayerFragment.class.getName());
                startActivity(intent);
               // showFragment(new FullScreenPlayerFragment());
            }
        });

        //自定义样式
        findViewById(R.id.bt3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new CustomPlayerFragment());
            }
        });

        showFragment(new PlayerFragment());
    }

    private void showFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, fragment, "tag-player");
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commitAllowingStateLoss();
    }
}