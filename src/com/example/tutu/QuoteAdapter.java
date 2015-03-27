package com.example.tutu;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.zip.Inflater;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.storage.StorageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class QuoteAdapter extends BaseAdapter implements ListAdapter {
	LayoutInflater inflater;
	Context context
	;
	MainActivity mainActivity;
	private DataHandler dataHandler;
	int progressInterval;
	DataHandler mDataHandler = new DataHandler(context);

	public QuoteAdapter(MainActivity aController, Context mContext,
			DataHandler mdataHandler) {
		context = mContext;
		mainActivity = aController;
		dataHandler = mdataHandler;
	}

	@Override
	public int getCount() {
		return dataHandler.getSize();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		Data quote;
		inflater = LayoutInflater.from(context);
		RelativeLayout cellLayout = (RelativeLayout) inflater.inflate(
				R.layout.list_view, null);
		cellLayout.setMinimumWidth(parent.getWidth());
		int color;
		mainActivity.setProgress(progressInterval * (position + 1));
		if (position % 2 > 0) {
			color = Color.rgb(48, 92, 131);
		} else {
			color = Color.rgb(119, 138, 170);
		}
		cellLayout.setBackgroundColor(color);
		quote = dataHandler.getQuoteForIndex(position);
		TextView field = (TextView) cellLayout.findViewById(R.id.symbol);
		field.setText(quote.getNo());
		field.setClickable(true);
		field.setOnClickListener(mainActivity);
		field = (TextView) cellLayout.findViewById(R.id.name);
		field.setText(quote.getName());
		field.setClickable(true);
		field.setOnClickListener(mainActivity);
		field = (TextView) cellLayout.findViewById(R.id.current);
		double current = Double.parseDouble(quote.getCurrent_price());
		double closing = Double.parseDouble(quote.getClosing_price());
		DecimalFormat df = new DecimalFormat("#0.00");
		String percent = df.format(((current - closing) * 100 / closing)) + "%";
		field.setText(df.format(current));
		field.setClickable(true);
		field.setOnClickListener(mainActivity);
		field = (TextView) cellLayout.findViewById(R.id.percent);
		if (current > closing) {
			field.setTextColor(0Xffee3b3b);
		} else {
			field.setTextColor(0Xff2e8b57);
		}
		field.setText(percent);
		cellLayout.setId(position + 33);
		cellLayout.setClickable(true);
		cellLayout.setOnClickListener(mainActivity);
		return cellLayout;
	}
	public void addSymbolsToFile(ArrayList<String> symbols){
		//强行更新页面数据
		//forceUpdate = true;
		//添加股票到文件中
		mDataHandler.addSymbolsToFile(symbols);
		//mDataHandler._addQuotes(symbols);
		//添加消息到消息队列
		//messageHandler.post(this);
	}
	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO 自动生成的方法存根

	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO 自动生成的方法存根
		return false;
	}

}
