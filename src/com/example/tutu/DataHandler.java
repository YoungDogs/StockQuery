package com.example.tutu;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class DataHandler {
	private static final String TAG="DataHandler";
	private static final String QUERY_URL = "http://hq.sinajs.cn/list=";
	private static final String SYMBOL_FILE_NAME = "symbols.txt";
	private int BUF_SIZE = 16384;
	private ArrayList<String> stocks;
	private ArrayList<Data> stockInfo = new ArrayList<Data>();
	private final int NAME = 0;
	private final int OPENING_PRICE = 1;
	private final int CLOSING_PRICE=2;
	private final int CURRENT_PRICE=3;
	private final int MAX_PRICE=4;
	private final int MIN_PRICE=5;
	Context context ;
	
	public DataHandler(Context mContext){
		super();
		context = mContext;
		readStockFromFile();
		
	}
	//读取存储在文件中的股票代码信息
		private void readStockFromFile(){
			File fullPath;
			if(stocks == null) 
			{
				//初始化股票代码数组
				stocks = new ArrayList<String>();
			}
			FileInputStream inStream;
			BufferedReader bReader;
			String quoteStr="";
			//获取存储股票的文件
			fullPath = new File("/data/data/com.example.tutu/files/symbols.txt");
				//读取文件
			try {
				inStream = new FileInputStream(fullPath);
				bReader = new BufferedReader(new InputStreamReader(inStream));			
				quoteStr = bReader.readLine();
				bReader.close();
				inStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//如果文件中不包含股票代码数据
			if(quoteStr == "" || quoteStr == null)
			{
				//数组清空
				stocks.clear();
				return;
			}
			//将字符串切割成数组
			String strArray[] = quoteStr.split(",");
			int index, count = strArray.length;
			//重置数组
			stocks.clear();
			//将数组存储到数组中
			for(index = 0; index < count; index++) 
				stocks.add(strArray[index]);
		}
	public synchronized  void addSymbolsToFile(ArrayList<String> stockList){
		boolean foundSymbol=false;
		if(stockList != null){
			if(stocks == null || stocks.size() == 0){
				stocks = new ArrayList<String>();
				stocks.addAll(stockList);	
				if(stockList.size()>0){
					_addQuotes(stockList);
				}
			}
			else{
				ArrayList<String> newStocks =new ArrayList<String>();
				//foundSymbol = false;
				int i1,i2;
				int c1=stocks.size();
				int c2 =stockList.size();
				for(i2 = 0;i2<c2;i2++){
					String newSymbol = stockList.get(i2);
					for(i1=0;i1<c1;i1++){
						if(newSymbol.equals(stocks.get(i1))){
							foundSymbol =true;
							break;
						}
					}
				}
//				for(String str1:stockList){
//					for(String str2:stocks){
//						if(str1.equals(str2)){
//							foundSymbol = true;
//							break;
//						}
//					}
//					if(!foundSymbol){
//						newStocks.add(str1);
//					}
//				}
				if(newStocks.size()>0){
					_addQuotes(newStocks);
				}
			}
			savePortfolio();
		}
		
	}
	//根据股票代码从网上获取数据
	protected void _addQuotes(ArrayList<String> stockSymbols){
		if(stockSymbols != null && stockSymbols.size()>0){
		//获取http客户端；
		HttpClient req = new DefaultHttpClient();
		//用于存放网址
		StringBuffer buf =new StringBuffer();
		buf.append(QUERY_URL);
		buf.append(stockSymbols.get(0));
		for(int index =1;index<stockSymbols.size();index++){
			buf.append(",");
			buf.append(stockSymbols.get(index));
		}
		try{
			//采用get方法获取网页数据
			HttpGet httpGet = new HttpGet(buf.toString());
			HttpResponse response =req.execute(httpGet);
			InputStream iStream = response.getEntity().getContent();
			if(parseQuotesFromStream(iStream)){
				for(String str3:stockSymbols){
					stocks.add(str3);
				}
				Toast.makeText(context, "股票添加成功", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(context, "股票添加失败", Toast.LENGTH_SHORT).show();
			}
		}catch(IOException e){
			Log.e(TAG,e.getMessage());
		}
		}
	}
	//保存股票
	private void savePortfolio(){
		if(stocks.size()>0){
			FileOutputStream outStream = null;
			OutputStreamWriter oWriter  ;
			try{
				outStream = context.openFileOutput(SYMBOL_FILE_NAME, Context.MODE_APPEND);
				oWriter = new OutputStreamWriter(outStream);
				StringBuffer buf1 = new StringBuffer();
				buf1.append(stocks.get(0));
				for(int index =1;index<stocks.size();index++){
					buf1.append(",");
					buf1.append(stocks.get(index));
				}
				String outStr = buf1.toString();
				oWriter.write(outStr,0,outStr.length());
				oWriter.close();
				outStream.close();
			}catch(IOException e){
				Log.e(TAG,e.getMessage());
			}
		}
		
	}
	//解析股票数据
	private boolean parseQuotesFromStream(InputStream aStream){
		boolean flag = false;
		if(aStream != null){
			stockInfo.clear();
			BufferedInputStream iStream = new BufferedInputStream(aStream);
			InputStreamReader iReader = null;
			try{
				iReader = new InputStreamReader(iStream,"GBK");
			}catch(UnsupportedEncodingException e){
				e.printStackTrace();
			}
			StringBuffer strBuf = new StringBuffer();
			char buf[] = new char[BUF_SIZE];
			try{
				int charsRead;
				//将数据读取到StringBuffer中
				while((charsRead = iReader.read(buf,0,buf.length))!=-1){
					strBuf.append(buf,0,charsRead);
				}
				//匹配股票数据
				Pattern pattern = Pattern.compile("str_(.+)=\"(.+)\"");
				Matcher matcher =pattern.matcher(strBuf);
				while(matcher.find()){
					String result = matcher.group(2);
					String[] data =result.split(",");
					Data mStockInfo = new Data();
					mStockInfo.setNo(matcher.group(1));
					mStockInfo.setName(data[NAME]);
					mStockInfo.setOpening_price(data[OPENING_PRICE]);
					mStockInfo.setClosing_price(data[CLOSING_PRICE]);
					mStockInfo.setCurrent_price(Double.parseDouble(data[CURRENT_PRICE])+0.01*(int)(10*Math.random())+"");
					mStockInfo.setMax_price(data[MAX_PRICE]);
					mStockInfo.setMin_price(data[MIN_PRICE]);
					stockInfo.add(mStockInfo);
					flag = true;	
				}
				}catch(IOException iox){
					Log.e(TAG,iox.getMessage());
				}
			}	
		return flag;
	}
	public Data getQuoteForIndex(int index){
		return stockInfo.get(index);
	}
	
	public int getSize() {
		return stocks.size();
	}
}
