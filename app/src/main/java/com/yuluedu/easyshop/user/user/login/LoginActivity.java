package com.yuluedu.easyshop.user.user.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.yuluedu.easyshop.R;
import com.yuluedu.easyshop.commons.ActivityUtils;
import com.yuluedu.easyshop.components.ProgressDialogFragment;
import com.yuluedu.easyshop.main.MainActivity;
import com.yuluedu.easyshop.user.user.register.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends MvpActivity<LoginView,LoginPrestenter> implements LoginView{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_username)
    EditText et_Username;
    @BindView(R.id.et_pwd)
    EditText et_Pwd;
    @BindView(R.id.btn_login)
    Button btn_Login;

    private ActivityUtils activityUtils;
    private String username;
    private String password;

    private ProgressDialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);
        
        
        //初始化
        init();
        
    }

    @NonNull
    @Override
    public LoginPrestenter createPresenter() {

        return new LoginPrestenter();
    }

    private void init() {
        //给左上角添加一个返回图标
        setSupportActionBar(toolbar);
       //设置为true，显示返回按钮，但点击事件需要实现菜单点击事件
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //给Editext添加监听
        et_Username.addTextChangedListener(textWatcher);
        et_Pwd.addTextChangedListener(textWatcher);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)finish();
        return super.onOptionsItemSelected(item);
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            username = et_Username.getText().toString();
            password = et_Pwd.getText().toString();

            boolean canLogin = !(TextUtils.isEmpty(username) || TextUtils.isEmpty(password));
            btn_Login.setEnabled(canLogin);

        }
    };

    @OnClick({R.id.btn_login,R.id.tv_register})
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_login:
                //activityUtils.showToast("hhhhh");
                presenter.login(username,password);

               /** Call call = EasyShopClient.getInstance().login(username, password);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("aaa", "网络连接失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.e("aaa", "网络连接成功");
                        if (response.isSuccessful()){
                            Log.e("aaa", "服务器成功响应");
                        }else {
                            Log.e("aaa", "请求失败。");
                        }
                    }
                });
                */
                /** //1.构建请求体
                OkHttpClient okHttpClient = new OkHttpClient();

                RequestBody requestBody = new FormBody.Builder()
                        .add("username",username)
                        .add("password",password)
                        .build();

                //2.
                Request request = new Request.Builder()
                        .url("http://wx.feicuiedu.com:9094/yitao/UserWeb?method=login")
                        .post(requestBody)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("aaa", "网络连接失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.e("aaa", "网络连接成功");
                        if (response.isSuccessful()){
                            Log.e("aaa", "服务器成功响应");
                        }else {
                            Log.e("aaa", "请求失败。");
                        }
                    }
                });*/
                break;
            case R.id.tv_register:
                activityUtils.startActivity(RegisterActivity.class);
                break;
        }
    }

    @Override
    public void showPrb() {
        activityUtils.hideSoftKeyboard();
        if (dialogFragment == null) dialogFragment = new ProgressDialogFragment();
        if (dialogFragment.isVisible()) return;
        dialogFragment.show(getSupportFragmentManager(), "dialogFragment");
    }

    @Override
    public void hidePrb() {
        dialogFragment.dismiss();
    }

    @Override
    public void loginFailed() {
        et_Username.setText("");
        et_Pwd.setText("");
    }

    @Override
    public void loginSuccess() {
        activityUtils.startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void showMsg(String msg) {
        activityUtils.showToast(msg);
    }
}
