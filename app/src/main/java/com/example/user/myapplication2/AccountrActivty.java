package com.example.user.myapplication2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Style.PhoneHead;

public class AccountrActivty extends Activity implements OnClickListener {
    private LinearLayout personinfo_linear;
    private LinearLayout payment_linear;
    private TextView lookhistory;
    private TextView exit_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
       // PhoneHead.HeadStyle(this);
        setContentView(R.layout.accountr);
        personinfo_linear = (LinearLayout) this.findViewById(R.id.personinfo_linear);
        payment_linear = (LinearLayout) this.findViewById(R.id.payment_linear);
        lookhistory = (TextView) this.findViewById(R.id.lookhistory);
        exit_text = (TextView) this.findViewById(R.id.exit_text);
        exit_text.setOnClickListener(this);
        personinfo_linear.setOnClickListener(this);
        payment_linear.setOnClickListener(this);
        lookhistory.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent;
        switch (v.getId()) {
            case R.id.personinfo_linear:
                intent = new Intent(this, PersoninfoActivty.class);
                startActivity(intent);
                break;
            case R.id.payment_linear:
                intent = new Intent(this, PaymentActivty.class);
                startActivity(intent);
                break;
            case R.id.lookhistory:
                intent = new Intent(this, Voiceprint_historyActivty.class);
                startActivity(intent);
                break;
            case R.id.exit_text:
                this.finish();
                break;
            default:
                break;
        }
    }
}
