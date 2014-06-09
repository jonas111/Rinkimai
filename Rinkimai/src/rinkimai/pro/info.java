package rinkimai.pro;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class info extends Activity {
	
	Button vote;
//	String var_id;
//	String id;
	String tx;
	 public void onCreate(Bundle savedInstanceState) { 
		 
		 	LinearLayout panele = (LinearLayout) findViewById(R.id.panele3);

	        super.onCreate(savedInstanceState);    
	        setContentView(R.layout.info);
	        TextView tv = (TextView)findViewById(R.id.variantas);
	        TextView inf = (TextView)findViewById(R.id.info);
	        Intent iin= getIntent();
	        Bundle b = iin.getExtras();
	        
//	        var_id = b.getString("var_id");
//	        id = b.getString("id");
	    	tx = b.getString("pavadinimas");
	        vote = (Button) findViewById(R.id.vote);
		    vote.setOnClickListener(new View.OnClickListener() {
		    	@Override
		    	public void onClick(View v) {
		    		 
		    			Context context = getApplicationContext();
				        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
				        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
				        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(     ConnectivityManager.TYPE_MOBILE );
				        if ( activeNetInfo != null || mobNetInfo != null){
				        	Toast.makeText(getApplicationContext(), "aciu uz jusu balsa", Toast.LENGTH_LONG).show();
				        	
				        	Home.dbcontroller.vidinisBalsavimas("1",tx);
				        	
				        	
				        }
				        else {
				        	Toast.makeText(getApplicationContext(), "atsiradus interneto rysiui jusu balsas bus uzskaitytas", Toast.LENGTH_LONG).show();
						}
				        
				}
			});

	        if(b!=null)
	        {
	            tv.setText((String) b.get("pavadinimas"));
	            inf.setText((String) b.get("info"));
	        }
	        backBtn();
	    }
	 
	 public void backBtn(){
		 Button a = (Button) findViewById(R.id.back_b);
		 a.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
			    	onBackPressed();
			    }
			});
	 }
	 @Override
	    public void onBackPressed() {
	        super.onBackPressed();   
	        //    finish();

	    }


}
