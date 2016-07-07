package com.example.user.myapplication2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Style.PhoneHead;
import com.storage.UserSharedPreferences;

public class RegisterActivty extends Activity implements OnClickListener{
	EditText password,againpassword,userName;
	Button registerbnt;
	UserSharedPreferences userSharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		//PhoneHead.HeadStyle(this);

		setContentView(R.layout.register);
		password= (EditText) this.findViewById(R.id.password);
		againpassword= (EditText) findViewById(R.id.againpassword);
		userName= (EditText) findViewById(R.id.userName);
		registerbnt= (Button) findViewById(R.id.registerbnt);
		registerbnt.setOnClickListener(this);
		userSharedPreferences=new UserSharedPreferences(this);
	}

	@Override
	public void onClick(View v) {
		String uName=userName.getText().toString();
		String uPassword=password.getText().toString();
		String uAgainPassword=againpassword.getText().toString();
		if(!uPassword.equals(uAgainPassword)){
			againpassword.setError("两次密码输入不一致，请重新输入");
		}
		else if (uName.equals("") || uPassword.equals("") || uAgainPassword.equals("")){
			Toast.makeText(RegisterActivty.this,"用户名，密码，以及再次输入密码都不能为空",Toast.LENGTH_SHORT).show();
		}
		else{
			if (!userSharedPreferences.getUserName(uName).equals("")){
				userName.setError("该用户名已经被注册");
			}else{
				userSharedPreferences.putUserName(uName);
				userSharedPreferences.putPassword(uPassword);
				Toast.makeText(RegisterActivty.this,"恭喜注册成功请登录去吧",Toast.LENGTH_SHORT).show();
				LoginDialog();
			}
		}
	}

	public void  LoginDialog(){
		AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
		builder.setTitle("提示"); //设置标题
		builder.setMessage("注册成功，会否返回登陆页面"); //设置内容
		builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss(); //关闭dialog
				Intent intent=new Intent(RegisterActivty.this,MainActivity.class);
				startActivity(intent);
				RegisterActivty.this.finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		//参数都设置完成了，创建并显示出来
		builder.create().show();
	}
}
