package com.yuluedu.easyshop.user.user.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.yuluedu.easyshop.R;
import com.yuluedu.easyshop.commons.ActivityUtils;
import com.yuluedu.easyshop.commons.RegexUtils;
import com.yuluedu.easyshop.components.AlertDialogFragment;
import com.yuluedu.easyshop.components.ProgressDialogFragment;
import com.yuluedu.easyshop.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends MvpActivity<RegisterView,RegisterPresenter> implements RegisterView{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_username)
    EditText et_Username;
    @BindView(R.id.et_pwd)
    EditText et_Pwd;
    @BindView(R.id.et_pwdAgain)
    EditText et_PwdAgain;
    @BindView(R.id.btn_register)
    Button btn_Register;

    private ActivityUtils activityUtils;
    private String username;
    private String passward;
    private String pwd_again;
    private ProgressDialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);

        init();

    }

    @NonNull
    @Override
    public RegisterPresenter createPresenter() {
        //创建注册的业务类
        return new RegisterPresenter();
    }

    private void init() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        et_Username.addTextChangedListener(textWatcher);
        et_Pwd.addTextChangedListener(textWatcher);
        et_PwdAgain.addTextChangedListener(textWatcher);
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
            passward = et_Pwd.getText().toString();
            pwd_again = et_PwdAgain.getText().toString();

            boolean canRegister = !(TextUtils.isEmpty(username)
                    || TextUtils.isEmpty(passward)
                    || TextUtils.isEmpty(pwd_again));

            btn_Register.setEnabled(canRegister);
        }
    };

    @OnClick(R.id.btn_register)
    public void onClick(){
        if (RegexUtils.verifyUsername(username)!=RegexUtils.VERIFY_SUCCESS){
            activityUtils.showToast(R.string.username_rules);
            return;
        }else if(RegexUtils.verifyPassword(passward) != RegexUtils.VERIFY_SUCCESS){
            activityUtils.showToast(R.string.password_rules);
            return;
        }else if (!TextUtils.equals(passward,pwd_again)){
            activityUtils.showToast(R.string.username_equal_pwd);
            return;
        }
        //activityUtils.showToast("猪猪猪猪");
        //业务类执行注册的业务
        presenter.register(username,passward);

//        Call call = EasyShopClient.getIntencse().register(username, passward);
//
//        call.enqueue(new UICallBack() {
//            @Override
//            public void onFailureUI(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponseUI(Call call, String body) {
//                UserResult userResult = new Gson().fromJson(body,UserResult.class);
//            }
//        });

        /**call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("aaa", "网络连接失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                    Gson是一个生成和解析json数据的第三方库
//                    生成：gson可以将一个类或者一个字符串生成为json格式的数据
//                    解析：gson可以将json格式的数据，解析为一个类
                if (response.isSuccessful()){
                    String json = response.body().toString();

                    UserResult userResult = new Gson().fromJson(json,UserResult.class);

                    Log.e("aaa","code = " + userResult.getCode());
                    Log.e("aaa","msg = " + userResult.getMessage());
                }
            }
        });*/

        /**
        //okhttp
        //1.构建请求

        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("username",username)
                .add("password",passward)
                .build();
        //2.
        Request request = new Request.Builder()
                .url("http://wx.feicuiedu.com:9094/yitao/UserWeb?method=register")
                .post(requestBody)
                .build();

        //3.
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
        });
         */
    }

    @Override
    public void showPrb() {
        //关闭软键盘
        activityUtils.hideSoftKeyboard();
        //进度条
        //初始化进度条
        if (dialogFragment == null)dialogFragment = new ProgressDialogFragment();
        //如果进度条已经显示，则跳出
        if (dialogFragment.isVisible()) return;
        //否则进度条显示
        dialogFragment.show(getSupportFragmentManager(),"dialogFragment");
    }

    @Override
    public void hidePrb() {
        dialogFragment.dismiss();
    }

    @Override
    public void registerSuccess() {
        //成功跳转到主页
        activityUtils.startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void registerFailed() {
        et_Username.setText("");
    }

    @Override
    public void showMsg(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void showUserPasswordError(String msg) {
//展示弹窗，提示错误信息
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(msg);
        fragment.show(getSupportFragmentManager(),getString(R.string.username_pwd_rule));
    }
}
