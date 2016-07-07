package com.example.user.myapplication2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.Style.PhoneHead;
import com.google.android.gms.common.api.GoogleApiClient;

public class PaymentActivty extends Activity implements OnClickListener {
	private Window window = null;
	private Button shurebnt;
	private TextView esc_text;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		//PhoneHead.HeadStyle(this);

		setContentView(R.layout.payment);
		shurebnt = (Button) this.findViewById(R.id.shurebnt);
		esc_text = (TextView) this.findViewById(R.id.esc_text);
		esc_text.setOnClickListener(this);
		shurebnt.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
			case R.id.esc_text:
				this.finish();
				break;
			case R.id.shurebnt:
				Intent intent = new Intent(this, AuthenticationActivty.class);
				startActivity(intent);
				PaymentActivty.this.finish();
				break;
			default:
				break;

		}
	}



}
