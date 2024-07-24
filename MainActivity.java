package com.example.loginregistration;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    private static final int RESULT_CODE_REGISTER =1;
    private static final int REQUEST_CODE_REGISTER = 1;
    private EditText etaccount,etpassword,etverify;
    private Button btnregister,btnlogin;
    private CodeUtils codeUtils;
    private ImageView img_code;
    private CheckBox cbRemember;
    private String userName="";
    private String pass="";


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

    public void onClicklogin(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btnlogin) { // 当出现验证码为空或者验证码错误时要及时更新验证码(防止机器暴力破解口令)
            String Code = etverify.getText().toString();
            if (TextUtils.isEmpty(Code)) { // 核查验证码是否为空
                Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                // 设置验证码动态变化
                Bitmap bitmap = codeUtils.createBitmap();
                img_code.setImageBitmap(bitmap);
            } else {
                String code = codeUtils.getCode();
                if (!Code.equals(code)) { // 查验输入的验证码是否正确
                    Toast.makeText(this, "验证码不正确", Toast.LENGTH_SHORT).show();
                    // 设置验证码动态变化
                    Bitmap bitmap = codeUtils.createBitmap();
                    img_code.setImageBitmap(bitmap);
                } else {
                    // 继续登录逻辑
                    String account = etaccount.getText().toString().trim();
                    String password = etpassword.getText().toString().trim();
                    if (TextUtils.isEmpty(userName)){
                        Toast.makeText(MainActivity.this,"还没有注册账号！",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (TextUtils.equals(account,userName)){
                        if (TextUtils.equals(password,pass)){
                            Toast.makeText(MainActivity.this,"恭喜你，登录成功！",Toast.LENGTH_LONG).show();
                            if (cbRemember.isChecked()){
                                SharedPreferences spf=getSharedPreferences("spfRecorid",MODE_PRIVATE);
                                SharedPreferences.Editor edit=spf.edit();
                                edit.putString("account",account);
                                edit.putString("password",password);
                                edit.putBoolean("isRemember",true);
                                edit.apply();
                            }else {
                                SharedPreferences spf=getSharedPreferences("spfRecorid",MODE_PRIVATE);
                                SharedPreferences.Editor edit=spf.edit();
                                edit.putBoolean("isRemember",false);
                                edit.apply();
                            }
                            //实现跳转
                            Intent intent=new Intent(MainActivity.this,MainActivity3.class);
                            //传递用户名
                            intent.putExtra("account",account);
                            startActivity(intent);
                            //接收自己
                            MainActivity.this.finish();
                        }else {
                            Toast.makeText(MainActivity.this,"密码错误！",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(MainActivity.this,"用户名错误",Toast.LENGTH_LONG).show();
                    }

                }

            }
            }
        }
    //记住密码（取出数据）
    private void initData() {
        SharedPreferences spf=getSharedPreferences("spfRecorid",MODE_PRIVATE);
        boolean isRemember=spf.getBoolean("isRemember",false);
        String account=spf.getString("account","");
        String password=spf.getString("password","");
        //更新用户名密码（注册的用户名密码）
        userName=account;
        pass=password;
        if (isRemember){
            etaccount.setText(account);
            etpassword.setText(password);
            cbRemember.setChecked(true);
        }
    }
    private void onClickregister(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
    private void onClickimg_code(View view) {
        Bitmap bitmap = codeUtils.createBitmap();
        String code = codeUtils.getCode();
        Log.i("验证码",code);
        img_code.setImageBitmap(bitmap);
    }
    private void initViews() {
        SharedPreferences userInfo=getSharedPreferences("userInfo",MODE_PRIVATE);
        etaccount= findViewById(R.id.etname);
        etpassword= findViewById(R.id.etpassword);
        etverify= findViewById(R.id.etverify);
        img_code= findViewById(R.id.img_code);
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        img_code.setImageBitmap(bitmap);
        btnregister= findViewById(R.id.btnregister);
        btnregister.setOnClickListener(this::onClickregister);
        btnlogin= findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(this::onClicklogin);
        img_code.setOnClickListener(this::onClickimg_code);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_REGISTER&&resultCode==MainActivity.RESULT_CODE_REGISTER&&data!=null){
            Bundle extras=data.getExtras();
            //获取用户密码
            String account=extras.getString("account","");
            String password=extras.getString("password","");
            etaccount.setText(account);
            etpassword.setText(password);
            //更新用户名密码（注册的用户名密码）
            userName=account;
            pass=password;
        }
    }
}