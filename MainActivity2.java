package com.example.loginregistration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {
    private static final int RESULT_CODE_REGISTER = 0;
    private EditText etname,etpassword1,etpassword2,etemail;
    private Button btnregister,btnback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onClickregister (View v){
        String name = etname.getText().toString().trim();
        String password1 = etpassword1.getText().toString().trim();
        String password2 = etpassword2.getText().toString().trim();
        String email = etemail.getText().toString().trim();
        if (TextUtils.isEmpty(name)){
            Toast.makeText(MainActivity2.this,"用户名不能为空！",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password1)){
            Toast.makeText(MainActivity2.this,"密码不能为空！",Toast.LENGTH_LONG).show();
            return;
        }
        if (!TextUtils.equals(password1,password2)){
            Toast.makeText(MainActivity2.this,"密码不一致！",Toast.LENGTH_LONG).show();
            return;
        }
        //存储注册的用户名和密码
        SharedPreferences spf=getSharedPreferences("spfRecorid",MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString("account",name);
        edit.putString("password",password1);
        edit.putString("email",email);
        //注册成功后，回到登录页面,数据回传
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putString("account",name);
        bundle.putString("password",password1);
        bundle.putString("email",email);
        intent.putExtras(bundle);
        setResult(RESULT_CODE_REGISTER,intent);
        Toast.makeText(MainActivity2.this,"注册成功！",Toast.LENGTH_LONG).show();
    }


    private void initViews() {
        SharedPreferences userInfo=getSharedPreferences("userInfo",MODE_PRIVATE);
        etname= findViewById(R.id.etname);
        etpassword1= findViewById(R.id.etpassword1);
        etpassword2= findViewById(R.id.etpassword2);
        etemail= findViewById(R.id.etemail);
        btnregister= findViewById(R.id.btnregister);
        btnregister.setOnClickListener(this::onClickregister);
    }
}