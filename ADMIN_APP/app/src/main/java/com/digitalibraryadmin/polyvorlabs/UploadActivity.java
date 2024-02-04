package com.digitalibraryadmin.polyvorlabs;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.*;
import java.io.File;
import java.text.*;
import java.util.*;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class UploadActivity extends AppCompatActivity {
	
	public final int REQ_CD_PICKER = 101;
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();
	
	private double n = 0;
	private String path = "";
	private String fileName = "";
	private String url = "";
	private String title = "";
	private String description = "";
	private String page = "";
	private String date = "";
	private String author = "";
	private HashMap<String, Object> map = new HashMap<>();
	
	private ScrollView vscroll1;
	private LinearLayout linear1;
	private LinearLayout linear2;
	private EditText title_;
	private EditText description_;
	private EditText page_;
	private EditText date_;
	private EditText author_;
	private EditText edittext6;
	private Button button1;
	private ProgressBar progressbar1;
	private TextView textview1;
	private CardView cardview1;
	private TextView textview2;
	private ImageView imageview1;
	
	private Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
	private DatabaseReference Ebook = _firebase.getReference("pdf");
	private ChildEventListener _Ebook_child_listener;
	private StorageReference ebook = _firebase_storage.getReference("public");
	private OnCompleteListener<Uri> _ebook_upload_success_listener;
	private OnSuccessListener<FileDownloadTask.TaskSnapshot> _ebook_download_success_listener;
	private OnSuccessListener _ebook_delete_success_listener;
	private OnProgressListener _ebook_upload_progress_listener;
	private OnProgressListener _ebook_download_progress_listener;
	private OnFailureListener _ebook_failure_listener;
	
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.upload);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
		} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		vscroll1 = findViewById(R.id.vscroll1);
		linear1 = findViewById(R.id.linear1);
		linear2 = findViewById(R.id.linear2);
		title_ = findViewById(R.id.title_);
		description_ = findViewById(R.id.description_);
		page_ = findViewById(R.id.page_);
		date_ = findViewById(R.id.date_);
		author_ = findViewById(R.id.author_);
		edittext6 = findViewById(R.id.edittext6);
		button1 = findViewById(R.id.button1);
		progressbar1 = findViewById(R.id.progressbar1);
		textview1 = findViewById(R.id.textview1);
		cardview1 = findViewById(R.id.cardview1);
		textview2 = findViewById(R.id.textview2);
		imageview1 = findViewById(R.id.imageview1);
		picker.setType("image/*");
		picker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (n == 0) {
					SketchwareUtil.showMessage(getApplicationContext(), "Select image");
				}
				else {
					if (title_.getText().toString().equals("")) {
						SketchwareUtil.showMessage(getApplicationContext(), "enter title");
					}
					else {
						if (description_.getText().toString().equals("")) {
							SketchwareUtil.showMessage(getApplicationContext(), "enter description");
						}
						else {
							if (page_.getText().toString().equals("")) {
								SketchwareUtil.showMessage(getApplicationContext(), "enter page");
							}
							else {
								if (date_.getText().toString().equals("")) {
									SketchwareUtil.showMessage(getApplicationContext(), "enter date");
								}
								else {
									if (author_.getText().toString().equals("")) {
										SketchwareUtil.showMessage(getApplicationContext(), "enter author");
									}
									else {
										if (edittext6.getText().toString().equals("")) {
											SketchwareUtil.showMessage(getApplicationContext(), "enter book url");
										}
										else {
											if (path.equals("")) {
												SketchwareUtil.showMessage(getApplicationContext(), "null path !");
											}
											else {
												if (fileName.equals("")) {
													SketchwareUtil.showMessage(getApplicationContext(), "null file name!");
												}
												else {
													ebook.child(fileName).putFile(Uri.fromFile(new File(path))).addOnFailureListener(_ebook_failure_listener).addOnProgressListener(_ebook_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
														@Override
														public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
															return ebook.child(fileName).getDownloadUrl();
														}}).addOnCompleteListener(_ebook_upload_success_listener);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		});
		
		textview2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				startActivityForResult(picker, REQ_CD_PICKER);
			}
		});
		
		imageview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				startActivityForResult(picker, REQ_CD_PICKER);
			}
		});
		
		_Ebook_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
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
		Ebook.addChildEventListener(_Ebook_child_listener);
		
		_ebook_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onProgress(UploadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				imageview1.setVisibility(View.GONE);
				edittext6.setVisibility(View.GONE);
				title_.setVisibility(View.GONE);
				description_.setVisibility(View.GONE);
				page_.setVisibility(View.GONE);
				date_.setVisibility(View.GONE);
				author_.setVisibility(View.GONE);
				button1.setVisibility(View.GONE);
				progressbar1.setVisibility(View.VISIBLE);
				textview1.setVisibility(View.VISIBLE);
				textview2.setVisibility(View.GONE);
				textview1.setText("Uploading".concat(" - ".concat(String.valueOf((long)(_progressValue)))));
			}
		};
		
		_ebook_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onProgress(FileDownloadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				
			}
		};
		
		_ebook_upload_success_listener = new OnCompleteListener<Uri>() {
			@Override
			public void onComplete(Task<Uri> _param1) {
				final String _downloadUrl = _param1.getResult().toString();
				if (title_.getText().toString().equals("")) {
					SketchwareUtil.showMessage(getApplicationContext(), "null title");
					imageview1.setVisibility(View.VISIBLE);
					progressbar1.setVisibility(View.GONE);
					textview1.setVisibility(View.GONE);
					edittext6.setVisibility(View.VISIBLE);
					title_.setVisibility(View.VISIBLE);
					description_.setVisibility(View.VISIBLE);
					page_.setVisibility(View.VISIBLE);
					date_.setVisibility(View.VISIBLE);
					author_.setVisibility(View.VISIBLE);
					textview2.setVisibility(View.VISIBLE);
					_firebase_storage.getReferenceFromUrl(_downloadUrl).delete().addOnSuccessListener(_ebook_delete_success_listener).addOnFailureListener(_ebook_failure_listener);
					button1.setVisibility(View.VISIBLE);
				}
				else {
					url = _downloadUrl;
					map.clear();
					map = new HashMap<>();
					map.put("image", _downloadUrl);
					map.put("title", title_.getText().toString());
					map.put("description", description_.getText().toString());
					map.put("page", page_.getText().toString());
					map.put("date", date_.getText().toString());
					map.put("author", author_.getText().toString());
					map.put("book_url", edittext6.getText().toString());
					Ebook.child(Ebook.push().getKey()).updateChildren(map);
					imageview1.setVisibility(View.VISIBLE);
					progressbar1.setVisibility(View.GONE);
					textview1.setVisibility(View.GONE);
					edittext6.setVisibility(View.VISIBLE);
					title_.setVisibility(View.VISIBLE);
					description_.setVisibility(View.VISIBLE);
					page_.setVisibility(View.VISIBLE);
					date_.setVisibility(View.VISIBLE);
					author_.setVisibility(View.VISIBLE);
					button1.setVisibility(View.VISIBLE);
					n = 0;
					imageview1.setImageResource(R.drawable.portrait_thumbnail);
					edittext6.setText("");
					title_.setText("");
					description_.setText("");
					page_.setText("");
					date_.setText("");
					author_.setText("");
				}
			}
		};
		
		_ebook_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(FileDownloadTask.TaskSnapshot _param1) {
				final long _totalByteCount = _param1.getTotalByteCount();
				
			}
		};
		
		_ebook_delete_success_listener = new OnSuccessListener() {
			@Override
			public void onSuccess(Object _param1) {
				
			}
		};
		
		_ebook_failure_listener = new OnFailureListener() {
			@Override
			public void onFailure(Exception _param1) {
				final String _message = _param1.getMessage();
				imageview1.setVisibility(View.VISIBLE);
				progressbar1.setVisibility(View.GONE);
				textview1.setVisibility(View.GONE);
				edittext6.setVisibility(View.VISIBLE);
				title_.setVisibility(View.VISIBLE);
				description_.setVisibility(View.VISIBLE);
				page_.setVisibility(View.VISIBLE);
				date_.setVisibility(View.VISIBLE);
				author_.setVisibility(View.VISIBLE);
				button1.setVisibility(View.VISIBLE);
			}
		};
	}
	
	private void initializeLogic() {
		n = 0;
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		getWindow().setStatusBarColor(0xFFFFFFFF);
		imageview1.setVisibility(View.VISIBLE);
		progressbar1.setVisibility(View.GONE);
		textview1.setVisibility(View.GONE);
		edittext6.setVisibility(View.VISIBLE);
		title_.setVisibility(View.VISIBLE);
		description_.setVisibility(View.VISIBLE);
		page_.setVisibility(View.VISIBLE);
		date_.setVisibility(View.VISIBLE);
		author_.setVisibility(View.VISIBLE);
		button1.setVisibility(View.VISIBLE);
		title_.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)12, (int)5, 0xFF1B2E8D, 0xFFFFFFFF));
		description_.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)12, (int)5, 0xFF1B2E8D, 0xFFFFFFFF));
		page_.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)12, (int)5, 0xFF1B2E8D, 0xFFFFFFFF));
		date_.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)12, (int)5, 0xFF1B2E8D, 0xFFFFFFFF));
		author_.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)12, (int)5, 0xFF1B2E8D, 0xFFFFFFFF));
		edittext6.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)12, (int)5, 0xFF1B2E8D, 0xFFFFFFFF));
		button1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)12, 0xFF1B2E8D));
		title_.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/quicksand.ttf"), 1);
		description_.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/quicksand.ttf"), 1);
		page_.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/quicksand.ttf"), 1);
		date_.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/quicksand.ttf"), 1);
		author_.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/quicksand.ttf"), 1);
		edittext6.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/quicksand.ttf"), 1);
		button1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/quicksand.ttf"), 1);
		textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/quicksand.ttf"), 1);
		textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/quicksand.ttf"), 1);
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_PICKER:
			if (_resultCode == Activity.RESULT_OK) {
				ArrayList<String> _filePath = new ArrayList<>();
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
						}
					}
					else {
						_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
					}
				}
				path = _filePath.get((int)(0));
				fileName = Uri.parse(path).getLastPathSegment();
				imageview1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(path, 1024, 1024));
				n = 1;
			}
			else {
				
			}
			break;
			default:
			break;
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