package uk.co.jatra.palette;

import android.graphics.Color;
import android.util.Log;

public class Colour {
	private static final String TAG = Colour.class.getSimpleName();
	public String hex;
	public int colour;
	
	private Colour(String hex, int colour) {
		this.hex = hex;
		this.colour = colour;
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
}
