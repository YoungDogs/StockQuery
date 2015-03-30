package com.example.tutu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity implements OnClickListener{
	
	private EditText symbolText = null;
	private QuoteAdapter quoteAdaptor;
	private DataHandler dataHandler;
	private Button addButton;
	MainActivity mContext;
	DataHandler mDataHandler;
	//返回按钮
	//private Button cancelButton;
	//删除按钮
	//private Button deleteButton;
	//对话框
	//private Dialog dialog = null;
	//股票详细信息
	//private TextView currentTextView,noTextView, openTextView, closeTextView, dayLowTextView, dayHighTextView;
	//日K线图
	//private ImageView chartView;
	//当前选中的股票的序号
	int currentSelectedIndex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);		
		mContext=this;
		//验证当前存放股票代码的文件是否存在
//		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().build());

//		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedRegistrationObjects().penaltyLog().penaltyDeath().build());
		File mFile = new File("/data/data/com.example.tutu/files/symbols.txt");
		if(mFile.exists())
		{
			Log.e("guojs","file exist");
		}else{
			try {
				//新建存放股票代码的文件
				FileOutputStream outputStream=openFileOutput("symbols.txt",MODE_PRIVATE);
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("guojs","file no exist");
		}
		//初始化股票代码处理类
		mDataHandler = new DataHandler(mContext);
		//如果adapter数据为空显示的内容
		this.getListView().setEmptyView(findViewById(R.id.empty));
		quoteAdaptor = new QuoteAdapter(this, this,mDataHandler);
//		//为列表设置适配器
		this.setListAdapter(quoteAdaptor);
//		//添加股票按钮
		addButton = (Button) findViewById(R.id.add_symbols_button);
//		//设置添加按钮监听器
		addButton.setOnClickListener(this);

	}

	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private void addSymbol(){
		symbolText = (EditText) findViewById(R.id.stock_symbols);
		String symbolsStr = symbolText.getText().toString();
		symbolsStr = symbolsStr.replace("\n", " ");
		String symbolArray[] = symbolsStr.split(" ");
		ArrayList<String> symboList = new ArrayList<String>();
		for(String str :symbolArray){
			System.out.println(str);
			symboList.add(str);
		}
		mDataHandler.addSymbolsToFile(symboList);
		symbolText.setText(null);	
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		addButton = (Button) findViewById(R.id.add_symbols_button);
		if(v == addButton);{

			addSymbol();
		}
		
	}


}
