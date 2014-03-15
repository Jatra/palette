package uk.co.jatra.palette;

import afzkl.development.colorpickerview.dialog.ColorPickerDialog;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MainActivity extends Activity {

	protected static final String TAG = "Colors";
	
	private static final String BG = "bg";
	private static final String LIST = "list";

	private EditText mEt;
	private View mSwatch;
	private ListView mList;
	private ColourAdapter mColourAdapter;
	private Button mButton;
	private SharedPreferences mPrefs;
	private int mBackgroundColour;
	private View mBackground;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mBackgroundColour = mPrefs.getInt(BG, 0xfff);

		mColourAdapter = new ColourAdapter(this, null);
		setContentView(R.layout.activity_main);
		
		mBackground = findViewById(R.id.background);
		mBackground.setBackgroundColor(mBackgroundColour);
		
		mEt = (EditText)findViewById(R.id.tv1);
		mButton = (Button)findViewById(R.id.add_button);
		
		
		mSwatch = findViewById(R.id.swatch);
		
		mEt.setOnEditorActionListener(new OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        boolean handled = false;
		        if (actionId == EditorInfo.IME_ACTION_SEND) {
					Colour colour = (Colour)mSwatch.getTag();
					mColourAdapter.addColour(colour);
		            handled = true;
		        }
		        return handled;
		    }
		});		
		mEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String input = s.toString();
				Colour colour = Colour.makeColour(input);
				if (colour != null) {
					mSwatch.setBackgroundColor(colour.colour);
					float[] hsv = new float[3];
					Color.colorToHSV(0xffffff & colour.colour, hsv);
					Log.d(TAG, "Color, "+(0xffffff & colour.colour)+", Hue "+hsv[0]);
					mSwatch.setTag(colour);
					mButton.setEnabled(true);
				} else {
					mButton.setEnabled(false);
				}
			}

		});

		mList = (ListView)findViewById(R.id.listView1);
		mList.setAdapter(mColourAdapter);
		
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Colour colour = (Colour)mSwatch.getTag();
				mColourAdapter.addColour(colour);
			}
		});
	
	}
	
	
	
	private String colourToARGB(int color) {
		return String.format("0x%02X%02X%02X%02X", Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()) {
		case R.id.action_background:
			onClickColorPickerDialog(item);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	public void onClickColorPickerDialog(MenuItem item) {
		//The color picker menu item as been clicked. Show 
		//a dialog using the custom ColorPickerDialog class.
		
		
		Log.d("mColorPicker", "initial value:" + mBackgroundColour);
				
		final ColorPickerDialog colorDialog = new ColorPickerDialog(this, mBackgroundColour);
		
		colorDialog.setAlphaSliderVisible(true);
		colorDialog.setTitle("Background Colour");
		
		colorDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mBackgroundColour = colorDialog.getColor();
				Toast.makeText(MainActivity.this, "Selected Color: " + colorToHexString(colorDialog.getColor()), Toast.LENGTH_LONG).show();
				
				mBackground.setBackgroundColor(mBackgroundColour);
				
				//Save the value in our preferences.
				SharedPreferences.Editor editor = mPrefs.edit();
				editor.putInt(BG, colorDialog.getColor());
				editor.commit();
			}
		});
		
		colorDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Nothing to do here.
			}
		});
		
		colorDialog.show();
	}
	
	
	private String colorToHexString(int color) {
		return String.format("#%06X", 0xFFFFFFFF & color);
	}
	

}
