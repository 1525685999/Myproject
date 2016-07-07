package com.example.user.myapplication2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.Style.PhoneHead;
import com.adapter.HistoryAdapter;
import com.modle.History;
import com.storage.UserSharedPreferences;

import java.util.LinkedList;

public class Voiceprint_historyActivty extends Activity implements View.OnClickListener{
	private TextView esc_text;
	private HistoryAdapter adapter;
	private LinkedList<History> list;
	private UserSharedPreferences userSharedPreferences;
	private ListView history_lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		//PhoneHead.HeadStyle(this);

		setContentView(R.layout.voiceprint_history);
		esc_text= (TextView) findViewById(R.id.esc_text);
		history_lv= (ListView) findViewById(R.id.history_lv);
		esc_text.setOnClickListener(this);
		userSharedPreferences=new UserSharedPreferences(this);
		list= (LinkedList<History>) userSharedPreferences.getHistory(userSharedPreferences.getspeakerID());
		adapter=new HistoryAdapter(list,this);
		history_lv.setAdapter(adapter);
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
