package com.example.user.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.Style.PhoneHead;

public class VoiceintruvtionActivty extends Activity implements View.OnClickListener{
    private TextView esc_text;
    private TextView next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //PhoneHead.HeadStyle(this);

        setContentView(R.layout.voiceintruvtion);
        esc_text= (TextView) findViewById(R.id.esc_text);
        next= (TextView) findViewById(R.id.next);
        next.setOnClickListener(this);
        esc_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        {
            switch (view.getId()) {
                case R.id.esc_text:
                    this.finish();
                    break;
                case R.id.next:
                    Intent intent=new Intent(VoiceintruvtionActivty.this,RegistervoicepasswordActivty.class);
                    startActivity(intent);
                    VoiceintruvtionActivty.this.finish();
                    break;
                default:
                    break;
            }
        }
    }
}
