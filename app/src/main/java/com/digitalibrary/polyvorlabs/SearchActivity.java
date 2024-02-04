package com.digitalibrary.polyvorlabs;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.Intent;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class SearchActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private Toolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	private double r = 0;
	private double length = 0;
	private String save = "";
	private String ListMapNum = "";
	private String value1 = "";
	
	private ArrayList<HashMap<String, Object>> map = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> lmap = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout linear2;
	private TextView textview2;
	private GridView gridview1;
	private LinearLayout linear4;
	private LinearLayout linear3;
	private ImageView imageview2;
	private ImageView imageview3;
	private TextView textview1;
	private EditText edittext1;
	
	private DatabaseReference ebook = _firebase.getReference("pdf");
	private ChildEventListener _ebook_child_listener;
	private Intent i = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.search);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		_app_bar = findViewById(R.id._app_bar);
		_coordinator = findViewById(R.id._coordinator);
		_toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		linear1 = findViewById(R.id.linear1);
		linear2 = findViewById(R.id.linear2);
		textview2 = findViewById(R.id.textview2);
		gridview1 = findViewById(R.id.gridview1);
		linear4 = findViewById(R.id.linear4);
		linear3 = findViewById(R.id.linear3);
		imageview2 = findViewById(R.id.imageview2);
		imageview3 = findViewById(R.id.imageview3);
		textview1 = findViewById(R.id.textview1);
		edittext1 = findViewById(R.id.edittext1);
		
		textview2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		gridview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				i.setClass(getApplicationContext(), BookdetailActivity.class);
				i.putExtra("img", map.get((int)_position).get("image").toString());
				i.putExtra("page", map.get((int)_position).get("page").toString());
				i.putExtra("description", map.get((int)_position).get("description").toString());
				i.putExtra("date", map.get((int)_position).get("date").toString());
				i.putExtra("author", map.get((int)_position).get("author").toString());
				i.putExtra("title", map.get((int)_position).get("title").toString());
				i.putExtra("book", map.get((int)_position).get("book_url").toString());
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});
		
		imageview2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				edittext1.setVisibility(View.VISIBLE);
			}
		});
		
		imageview3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				finish();
			}
		});
		
		edittext1.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				r = map.size() - 1;
				length = map.size();
				for(int _repeat192 = 0; _repeat192 < (int)(length); _repeat192++) {
					value1 = map.get((int)r).get("title").toString();
					if (!(_charSeq.length() > value1.length()) && value1.toLowerCase().contains(_charSeq.toLowerCase())) {
						gridview1.setAdapter(new Gridview1Adapter(map));
						textview2.setVisibility(View.GONE);
					}
					else {
						if (!map.get((int)r).get("title").toString().toLowerCase().contains(_charSeq.toLowerCase()) || _charSeq.equals("")) {
							map.remove((int)(r));
							textview2.setVisibility(View.VISIBLE);
							gridview1.setAdapter(new Gridview1Adapter(map));
						}
					}
					r--;
				}
				gridview1.setAdapter(new Gridview1Adapter(map));
				imageview2.setVisibility(View.GONE);
				imageview3.setVisibility(View.VISIBLE);
				textview1.setVisibility(View.GONE);
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		_ebook_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				ebook.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						map = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								map.add(_map);
							}
						}
						catch (Exception _e) {
							_e.printStackTrace();
						}
						gridview1.setAdapter(new Gridview1Adapter(map));
						gridview1.setNumColumns((int)2);
						linear4.setVisibility(View.GONE);
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onCancelled(DatabaseError _param1) {
				final int _errorCode = _param1.getCode();
				final String _errorMessage = _param1.getMessage();
				
			}
		};
		ebook.addChildEventListener(_ebook_child_listener);
	}
	
	private void initializeLogic() {
		getSupportActionBar().hide();
		//Ui
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		getWindow().setStatusBarColor(0xFFFFFFFF);
		linear2.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)12, 0xFFFFFFFF));
		linear2.setElevation((float)5);
		_Icon_Colour(imageview2, "#000000");
		edittext1.setVisibility(View.GONE);
		textview2.setVisibility(View.GONE);
		imageview2.setVisibility(View.VISIBLE);
		imageview3.setVisibility(View.VISIBLE);
		gridview1.setVerticalScrollBarEnabled(false);
	}
	
	public void _Icon_Colour(final ImageView _iconview, final String _colour) {
		_iconview.getDrawable().setColorFilter(Color.parseColor(_colour), PorterDuff.Mode.SRC_IN);
	}
	
	
	public void _reloadData() {
		value1 = "";
		try {
			map.clear();
			for(int _repeat13 = 0; _repeat13 < (int)(50); _repeat13++) {
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("image", value1);
					map.add(_item);
				}
				
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("title", value1);
					map.add(_item);
				}
				
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("description", value1);
					map.add(_item);
				}
				
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("page", value1);
					map.add(_item);
				}
				
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("date", value1);
					map.add(_item);
				}
				
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("author", value1);
					map.add(_item);
				}
				
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("bookurl", value1);
					map.add(_item);
				}
				
				gridview1.setAdapter(new Gridview1Adapter(map));
			}
		} catch (Exception e) {
			SketchwareUtil.showMessage(getApplicationContext(), e.getMessage());
		}
	}
	
	
	public void _h() {
		ListMapNum = "0";
		try {
			lmap.clear();
			for(int _repeat13 = 0; _repeat13 < (int)(50); _repeat13++) {
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("key", ListMapNum);
					lmap.add(_item);
				}
				
				ListMapNum = String.valueOf((long)(0));
				gridview1.setAdapter(new Gridview1Adapter(lmap));
			}
		} catch (Exception e) {
			SketchwareUtil.showMessage(getApplicationContext(), e.getMessage());
		}
	}
	
	
	public void _j() {
		ListMapNum = "0";
		try {
			lmap.clear();
			for(int _repeat13 = 0; _repeat13 < (int)(50); _repeat13++) {
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("key", ListMapNum);
					lmap.add(_item);
				}
				
				ListMapNum = "";
				gridview1.setAdapter(new Gridview1Adapter(lmap));
			}
		} catch (Exception e) {
			SketchwareUtil.showMessage(getApplicationContext(), e.getMessage());
		}
	}
	
	
	public void _reload() {
		try {
			map.clear();
			gridview1.setAdapter(new Gridview1Adapter(map));
		} catch (Exception e) {
			SketchwareUtil.showMessage(getApplicationContext(), e.getMessage());
		}
	}
	
	
	public void _relo() {
		value1 = "";
		try {
			map.clear();
			for(int _repeat13 = 0; _repeat13 < (int)(length); _repeat13++) {
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("title", value1);
					map.add(_item);
				}
				
				value1 = String.valueOf((long)(Double.parseDouble(value1) + 1));
				gridview1.setAdapter(new Gridview1Adapter(map));
			}
		} catch (Exception e) {
			SketchwareUtil.showMessage(getApplicationContext(), e.getMessage());
		}
	}
	
	public class Gridview1Adapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Gridview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.book, null);
			}
			
			final androidx.cardview.widget.CardView cardview1 = _view.findViewById(R.id.cardview1);
			final TextView title = _view.findViewById(R.id.title);
			final TextView page = _view.findViewById(R.id.page);
			final TextView description = _view.findViewById(R.id.description);
			final TextView author = _view.findViewById(R.id.author);
			final TextView date = _view.findViewById(R.id.date);
			final TextView bookurl = _view.findViewById(R.id.bookurl);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			
			Glide.with(getApplicationContext()).load(Uri.parse(map.get((int)_position).get("image").toString())).into(imageview1);
			title.setText(map.get((int)_position).get("title").toString());
			description.setText(map.get((int)_position).get("description").toString());
			page.setText(map.get((int)_position).get("page").toString());
			date.setText(map.get((int)_position).get("date").toString());
			author.setText(map.get((int)_position).get("author").toString());
			bookurl.setText(map.get((int)_position).get("book_url").toString());
			
			return _view;
		}
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