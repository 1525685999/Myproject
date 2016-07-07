package com.example.user.myapplication2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.Style.PhoneHead;

public class PersoninfoActivty extends Activity implements OnClickListener {
    private TextView esc_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //PhoneHead.HeadStyle(this);

        setContentView(R.layout.person_info);
        esc_text= (TextView) findViewById(R.id.esc_text);
        esc_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.esc_text:
                this.finish();
            default:
                break;

        }
    }
}
