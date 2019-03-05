package com.example.leoneyi.game_animals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnStart=(Button)findViewById(R.id.btn_start);//声明一个按钮，将其映射到START按钮
        Button btnExit=(Button)findViewById(R.id.btn_exit);
        btnStart.setOnClickListener(new View.OnClickListener() {//给这个按钮绑定监听事件，并使用匿名类
            @Override
            public void onClick(View v) {//按钮的点击响应事件
                //创建一个Intent类，转到新的活动页面
                Intent intentStartGame=new Intent(MainActivity.this,PlayActivity.class);
                startActivity(intentStartGame);
                //Toast.makeText(MainActivity.this, "执行", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });


    }
}
