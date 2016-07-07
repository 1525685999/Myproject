package com.com.xiaoi.view;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class MyAutoCompleteTextView extends AutoCompleteTextView {

	Drawable[] rbs; // �������Լ����ıߵ�ͼƬ
	Drawable rightImg;// �������ұߵ�ͼƬ

	public MyAutoCompleteTextView(Context context) {
		super(context);
		init();
	}

	public MyAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MyAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		// �õ������ܵ�ͼƬ left top right bottom;//˳ʱ��
		rbs = this.getCompoundDrawables();
		rightImg = rbs[2];
		
		this.setCompoundDrawables(rbs[0], rbs[1], null, rbs[3]);
		// ���㷢���ı��ʱ��������¼�
		this.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View view, boolean flag) {
				if (flag) {
					if (MyAutoCompleteTextView.this.getText().length() >= 1) {
						MyAutoCompleteTextView.this.setCompoundDrawables(rbs[0], rbs[1], rightImg,rbs[3]);
					}
				} 
			}
		});
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				//event.getAction() �õ���ǰ��ָ��״̬�� down ����ȥ   move  �ƶ�     up  ̧����
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN://down ����ȥ
					
					break;
				case MotionEvent.ACTION_MOVE:// move  �ƶ�
					
					break;
				case MotionEvent.ACTION_UP://up  ̧����
						
						int etWidth = MyAutoCompleteTextView.this.getWidth(); //�����ĳ���
						int leftPadding = MyAutoCompleteTextView.this.getTotalPaddingRight(); //�ұ�ͼƬ��߸��ؼ��ұߵľ���
						int rightPadding = MyAutoCompleteTextView.this.getPaddingRight();//�ұ�ͼƬ�ұ߸��ؼ��ұߵľ���
						
						int start = etWidth - leftPadding; //��ָ��ʼ�ķ�Χ
					    int end = etWidth - rightPadding;
					    //��������ͼƬ�����ʱ�������������򣬲�������ͼƬ
					    if(start<=event.getX() && event.getX()<=end){
					    	MyAutoCompleteTextView.this.setText("");
					    	MyAutoCompleteTextView.this.setCompoundDrawables(rbs[0], rbs[1],null, rbs[3]);
					    }
						break;
				default:
					break;
				}
				return false;
			}
		});
		this.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//����ı��ʱ�򣬻ᴥ���������
				int a = MyAutoCompleteTextView.this.getText().length(); //�õ�edittext�е����ֵĳ���
				if(a>=1){
					MyAutoCompleteTextView.this.setCompoundDrawables(rbs[0], rbs[1],rightImg, rbs[3]);
				}else{
					MyAutoCompleteTextView.this.setCompoundDrawables(rbs[0], rbs[1],null, rbs[3]);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}
		});
	}

}
