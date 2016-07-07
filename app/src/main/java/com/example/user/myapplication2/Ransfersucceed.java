package com.example.user.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.Style.PhoneHead;

public class Ransfersucceed extends Activity implements View.OnClickListener{
    Button shurebnt;
    TextView esc_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
       // PhoneHead.HeadStyle(this);

        setContentView(R.layout.activity_ransfersucceed);
        shurebnt= (Button) this.findViewById(R.id.shurebnt);
        esc_text= (TextView) this.findViewById(R.id.esc_text);
        esc_text.setOnClickListener(this);
        shurebnt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){

        Ransfersucceed.this.finish();
    }
}
