package com.digitalibrary.polyvorlabs;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.Intent;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Typeface;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.json.*;

public class BookdetailActivity extends AppCompatActivity {
	
	private LinearLayout linear1;
	private LinearLayout toolbar;
	private LinearLayout linear2;
	private ScrollView vscroll1;
	private LinearLayout btn;
	private ImageView imageview1;
	private TextView textview1;
	private CardView cardview1;
	private LinearLayout linear3;
	private ImageView imageview2;
	private TextView title;
	private LinearLayout linear5;
	private LinearLayout linear8;
	private LinearLayout linear6;
	private TextView textview5;
	private TextView date;
	private TextView textview8;
	private TextView page;
	private TextView textview6;
	private TextView author;
	private LinearLayout linear7;
	private TextView bookurl;
	private TextView description;
	private TextView textview10;
	
	private Intent i = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.bookdetail);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		toolbar = findViewById(R.id.toolbar);
		linear2 = findViewById(R.id.linear2);
		vscroll1 = findViewById(R.id.vscroll1);
		btn = findViewById(R.id.btn);
		imageview1 = findViewById(R.id.imageview1);
		textview1 = findViewById(R.id.textview1);
		cardview1 = findViewById(R.id.cardview1);
		linear3 = findViewById(R.id.linear3);
		imageview2 = findViewById(R.id.imageview2);
		title = findViewById(R.id.title);
		linear5 = findViewById(R.id.linear5);
		linear8 = findViewById(R.id.linear8);
		linear6 = findViewById(R.id.linear6);
		textview5 = findViewById(R.id.textview5);
		date = findViewById(R.id.date);
		textview8 = findViewById(R.id.textview8);
		page = findViewById(R.id.page);
		textview6 = findViewById(R.id.textview6);
		author = findViewById(R.id.author);
		linear7 = findViewById(R.id.linear7);
		bookurl = findViewById(R.id.bookurl);
		description = findViewById(R.id.description);
		textview10 = findViewById(R.id.textview10);
		
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				i.setClass(getApplicationContext(), ReadbookActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.putExtra("bookurl", bookurl.getText().toString());
				startActivity(i);
			}
		});
		
		imageview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				finish();
			}
		});
	}
	
	private void initializeLogic() {
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		getWindow().setStatusBarColor(0xFFFFFFFF);
		Glide.with(getApplicationContext()).load(Uri.parse(getIntent().getStringExtra("img"))).into(imageview2);
		title.setText(getIntent().getStringExtra("title"));
		date.setText(getIntent().getStringExtra("date"));
		page.setText(getIntent().getStringExtra("page"));
		author.setText(getIntent().getStringExtra("author"));
		description.setText(getIntent().getStringExtra("description"));
		bookurl.setText(getIntent().getStringExtra("book"));
		textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/opensansbold.ttf"), 1);
		title.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/opensansbold.ttf"), 1);
		textview5.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/opensansbold.ttf"), 1);
		date.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/opensansbold.ttf"), 1);
		textview6.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/opensansbold.ttf"), 1);
		author.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/opensansbold.ttf"), 1);
		description.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/opensansbold.ttf"), 1);
		textview8.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/opensansbold.ttf"), 1);
		page.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/opensansbold.ttf"), 1);
		textview10.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/opensansbold.ttf"), 1);
		toolbar.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)12, 0xFFFFFFFF));
		btn.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)12, 0xFF1B2E8D));
		btn.setElevation((float)5);
		toolbar.setElevation((float)5);
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}