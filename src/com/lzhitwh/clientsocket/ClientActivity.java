package com.lzhitwh.clientsocket;

import android.annotation.SuppressLint;
import android.app.Activity;  
import android.os.Bundle;
import android.util.Log;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.widget.Button;  
import android.widget.EditText;  
import android.widget.TextView;  
import android.content.Context;  
import android.widget.Toast;  
import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.InputStreamReader;  
import java.io.OutputStreamWriter;  
import java.io.PrintWriter;  
import android.net.wifi.WifiManager;  
import java.net.Socket;  

@SuppressLint("NewApi")
public class ClientActivity extends Activity {  
    /** Called when the activity is first created. */ 
     private Button startButton = null;  
     private Button stopButton = null;  
     private Button checkButton = null;  
     private WifiManager wifiManager = null;  
     private final String   DEBUG_TAG= "Activity01";  
     private TextView   mTextView=null;  
     private EditText   mEditText=null;  
     private Button     mButton=null;  
   
	  @Override 
	 public void onCreate(Bundle savedInstanceState) {  
	     super.onCreate(savedInstanceState);  
	     setContentView(R.layout.activity_main); 
	     
	     mButton = (Button)findViewById(R.id.Button01);  
	     mTextView=(TextView)findViewById(R.id.TextView01);  
	     mEditText=(EditText)findViewById(R.id.EditText01);  
	     startButton = (Button)findViewById(R.id.startWifi);  
	     stopButton = (Button)findViewById(R.id.stopWifi);  
	     checkButton = (Button)findViewById(R.id.checkWifi);  
	     startButton.setOnClickListener(new StartWifiListener());  
	     stopButton.setOnClickListener(new StopWifiListener());  
	     checkButton.setOnClickListener(new CheckWifiListener());
	     
	     mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Thread(new SocketSend()).start();
			}
		});
	 }   
	  
	class StartWifiListener implements OnClickListener{  
	    public void onClick(View v) {  
		    wifiManager = (WifiManager)ClientActivity.this.getSystemService(Context.WIFI_SERVICE);  
		    wifiManager.setWifiEnabled(true);  
		    System.out.println("wifi state --->" + wifiManager.getWifiState());  
		    Toast.makeText(ClientActivity.this, "当前Wifi网卡状态为" +   
		    		wifiManager.getWifiState(), Toast.LENGTH_SHORT).show();  
	     }  
	} 
	
	class StopWifiListener implements OnClickListener{  
		 public void onClick(View arg0) {  
			wifiManager = (WifiManager)ClientActivity.this.getSystemService(Context.WIFI_SERVICE);  
			wifiManager.setWifiEnabled(false);  
			System.out.println("wifi state --->" + wifiManager.getWifiState());  
			Toast.makeText(ClientActivity.this, "当前Wifi网卡状态为" +   
				wifiManager.getWifiState(), Toast.LENGTH_SHORT).show();  
		 }  
	}  
	class CheckWifiListener implements OnClickListener{  
		 public void onClick(View v) {  
			  wifiManager = (WifiManager)ClientActivity.this.getSystemService(Context.WIFI_SERVICE);  
			  System.out.println("wifi state --->" + wifiManager.getWifiState());  
			  Toast.makeText(ClientActivity.this, "当前Wifi网卡状态为" +   
			    	wifiManager.getWifiState(), Toast.LENGTH_SHORT).show();  
		 }  
	}

	class SocketSend implements Runnable{
		@Override
		public void run() {
		    Socket socket = null;  
		    String message = mEditText.getText().toString() + "/r/n";   
		    try{     
		        //创建Socket  
		        socket = new Socket("192.168.43.131",54321);   
		        //向服务器端发送消息  
		        PrintWriter out = new PrintWriter(new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
		        out.write(message);
		        out.flush();// 刷新输出流，使Server马上收到该字符串  
		        //接收来自服务器端的消息  
		        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));   
		        String msg = br.readLine();   
		           
		        if ( msg != null ){  
		            mTextView.setText(msg);  
		        }else{  
		            mTextView.setText("数据错误!");  
		        }  
		        //关闭流  
		        out.close();  
		        br.close();  
		        //关闭Socket  
		        socket.close();   
		    }  
		    catch (Exception e){  
		        // TODO: handle exception  
		        Log.e(DEBUG_TAG, e.toString());  
		    }  
		}
	} 
}