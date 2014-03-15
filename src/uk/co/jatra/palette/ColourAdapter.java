package uk.co.jatra.palette;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class ColourAdapter extends BaseAdapter {
	
	

	private static final String TAG = ColourAdapter.class.getSimpleName();
	private Context mContext;
	private List<Colour> mColours;
	
	ColourAdapter(Context context) {
		this(context, null);
	}
	
	ColourAdapter(Context context, List<Colour> colours) {
		mContext = context;
		if (colours != null) {
			mColours = colours;
		} else {
			mColours = new ArrayList<Colour>();
		}
	}
	
	public void addColour(Colour colour) {
		mColours.add(0, colour);
		notifyDataSetChanged();
	}
	
	
	@Override
	public int getCount() {
		return mColours.size();
	}

	@Override
	public Colour getItem(int position) {
		return mColours.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.swatch, parent, false);
		}
		TextView swatch = (TextView)convertView.findViewById(R.id.swatch);
		ImageButton dismiss = (ImageButton)convertView.findViewById(R.id.dismiss);
		Colour colour = getItem(position);
		swatch.setBackgroundColor(colour.colour);
		swatch.setText(colour.hex);
		
		int red = Color.red(colour.colour);
		int green = Color.green(colour.colour);
		int blue = Color.blue(colour.colour);
		
		int yiq = ((red*299)+(green*587)+(blue*114))/1000;
		swatch.setTextColor((yiq >= 128) ? Color.BLACK : Color.WHITE);

		dismiss.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mColours.remove(position);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}


}
