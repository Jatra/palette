package uk.co.jatra.palette;

import android.graphics.Color;
import android.util.Log;

public class Colour {
	private static final String TAG = Colour.class.getSimpleName();
	public String mHex;
	public int mColour;
	public int mTextColour;
	
	private Colour(String hex, int colour) {
		this.mHex = hex;
		this.mColour = colour;
//		int red = Color.red(colour);
//		int green = Color.green(colour);
//		int blue = Color.blue(colour);
//		
//		int yiq = ((red*299)+(green*587)+(blue*114))/1000;
//		mTextColour = (yiq >= 128) ? Color.BLACK : Color.WHITE;
		mTextColour = textColourForBackground(colour);
	}
	
	public static Colour makeColour(String typed) {
		Colour ret = null;
		try {
			int colour = getColour(typed);
			if (!typed.startsWith("#")) {
				typed = "#"+typed;
			}
			ret = new Colour(typed, colour);
		} catch (IllegalArgumentException ile) {
			//bummer. make sure ret is null
			ret = null;
		}
		return ret;
	}
	
	private static int getColour(String input) {
		Log.d(TAG, "input: "+input);
		int colour;
		if (input.startsWith("#")) {
			input = input.substring(1);
		}
		int len = input.length();
		switch (len) {
		case 3:
		case 4:
			input = doubleUp(input);
		case 6:
		case 8:
			input = "#"+input;
			colour = Color.parseColor(input);
			Log.d(TAG, "Colour from: "+input+" = "+colour);
			break;
		default:
			throw new IllegalArgumentException("Invalid colour spec: "+input);
		}
		return colour;
		
	}
	private static String doubleUp(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<s.length(); i++) {
			sb.append(s.charAt(i));
			sb.append(s.charAt(i));
		}
		return sb.toString();
	}
	
	public static int textColourForBackground(int bgColour) {
		int red = Color.red(bgColour);
		int green = Color.green(bgColour);
		int blue = Color.blue(bgColour);
		
		int yiq = ((red*299)+(green*587)+(blue*114))/1000;
		return (yiq >= 128) ? Color.BLACK : Color.WHITE;
	}
}
