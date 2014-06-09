package rinkimai.pro;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class info extends Activity {
	
		Button vote;
		// langelis kuriame rodo kad kraunasi(pvz balsuojama...)
		private ProgressDialog pDialog;
		 
	    // JSON parser class
	    JSONParser jsonParser = new JSONParser();
	    
	    //php register script
	    
	    
	    //linkas y php failiuka kuris perduoda duomenis y DB:
	    private static final String REGISTER_URL = "http://rinkimai2014.coxslot.com/webservisas/vote.php";
	       
	    //Json tagai
	    private static final String TAG_SUCCESS = "success";
	    private static final String TAG_MESSAGE = "message";
	    /*Kintamasis isgauti esama vartotoja is Home klases (ten issaugojamas  
	     *statiniame kintamajame prisijungiant) Paspaudus vote keliauja y db*/
	    private String username = Home.thisUser;
	    /*Kintamasis isgauti balsavima kuriame esu is Tab_Vote klases (ten issaugojamas  
	     *statiniame kintamajame paspaudus ant pacio balsavimo)Paspaudus vote keliauja y db*/
	    private String balsavimas = Tab_Vote.balsId;
	    /*Kintamasis isgauti kuri varianta pasirinkome(uzpildymas bus veliau)*/
	    private String variantas;
	    //private Button b = (Button) findViewById(R.id.vote);
	
	
	 public void onCreate(Bundle savedInstanceState) { 
		 
	        super.onCreate(savedInstanceState);    
	        setContentView(R.layout.info);
	        TextView tv = (TextView)findViewById(R.id.variantas);
	        TextView inf = (TextView)findViewById(R.id.info);
	        Intent iin= getIntent();
	      //* isgaunami duomenys siunciami is praejusio activicio
	        Bundle b = iin.getExtras();
	        
	        if(b!=null)
	        {
	        	// isgaunamas varianto pavadinimas (jy parodau siame activity)
	            tv.setText((String) b.get("pavadinimas"));
	            // isgaunama informacija apie pasirinkta varianta (jy parodau siame activity)
	            inf.setText((String) b.get("info"));
	            // isgaunama pasirinkto varianto id. Paspaudus vote keliauja y db
		        variantas = (String) b.get("ats_id");
	        }
	        
	        backBtn();
	        voteBtn();
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
	 }
	 
	 	private void voteBtn(){
		 
	 		vote = (Button) findViewById(R.id.vote);
		    vote.setOnClickListener(new View.OnClickListener() {
		    	@Override
		    	public void onClick(View v) {
		    		
		    		new Voting().execute();
		    		 	/*
		    			Context context = getApplicationContext();
				        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
				        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
				        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(     ConnectivityManager.TYPE_MOBILE );
				        if ( activeNetInfo != null || mobNetInfo != null){
				        	Toast.makeText(getApplicationContext(), "aciu uz jusu balsa", Toast.LENGTH_LONG).show();
				        	
				        	//Home.dbcontroller.vidinisBalsavimas("1",tx);
				        	
				        	
				        }
				        else {
				        	Toast.makeText(getApplicationContext(), "atsiradus interneto rysiui jusu balsas bus uzskaitytas", Toast.LENGTH_LONG).show();
						}*/
				        
				}
			});
		 
		 
	 }
	 
	 
	 class Voting extends AsyncTask<String, String, String> {

			
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(info.this);
	            pDialog.setMessage("Voting...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }
			
			@Override
			protected String doInBackground(String... args) {
				// TODO Auto-generated method stub
				 // Check for success tag
	            int success;
	           
	            try {
	                // Building Parameters
	                List<NameValuePair> params = new ArrayList<NameValuePair>();
	                /* taip duomenys siunciami y php failiuka kuris nusius juos
	                 * y DB*/
	                params.add(new BasicNameValuePair("vart_id", username));
	                params.add(new BasicNameValuePair("bals_id", balsavimas));
	                params.add(new BasicNameValuePair("ats_id", variantas));
	                
	 
	                Log.d("request!", "starting");
	                
	                //Posting user data to script 
	                JSONObject json = jsonParser.makeHttpRequest(
	                       REGISTER_URL, "POST", params);
	 
	                // full json response
	                Log.d("Pabalsuota!", json.toString());
	 
	                // json success element
	                success = json.getInt(TAG_SUCCESS);
	                if (success == 1) {
	                	Log.d("Voted!", json.toString()); 
	                	
	                	return json.getString(TAG_MESSAGE);
	                }else{
	                	Log.d("Voting Fail!", json.getString(TAG_MESSAGE));
	                	return json.getString(TAG_MESSAGE);
	                	
	                }
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
	 
	            return null;
				
			}
			
	        protected void onPostExecute(String file_url) {
	            // dismiss the dialog once product deleted
	            pDialog.dismiss();
	            if (file_url != null){
	            	Toast.makeText(info.this, file_url, Toast.LENGTH_LONG).show();
	            }
	 
	        }
			
		}
	 


}
