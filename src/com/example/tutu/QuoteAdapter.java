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
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO �Զ����ɵķ������
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO �Զ����ɵķ������
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO �Զ����ɵķ������
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
		//ǿ�и���ҳ������
		//forceUpdate = true;
		//��ӹ�Ʊ���ļ���
		mDataHandler.addSymbolsToFile(symbols);
		//mDataHandler._addQuotes(symbols);
		//�����Ϣ����Ϣ����
		//messageHandler.post(this);
	}
	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		// TODO �Զ����ɵķ������
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO �Զ����ɵķ������
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO �Զ����ɵķ������
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO �Զ����ɵķ������
		return false;
	}

}
