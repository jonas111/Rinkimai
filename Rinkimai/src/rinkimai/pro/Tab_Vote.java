package rinkimai.pro;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Tab_Vote extends Activity {
	
	private ProgressDialog pDialog;
	private static final String URL = "http://rinkimai2014.coxslot.com/webservisas/balsavimai.php";

    
    private static final String POSTS = "posts";
    private static final String ID = "id";
    private static final String PAVADINIMAS = "pavadinimas";
    private static final String IKELTAS = "ikeltas";
    private static final String PABAIGA = "pabaiga";
    public static String balsId;
    
    private JSONArray balsavimai = null;
    private ArrayList<HashMap<String, String>> balsavimuSarasas;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_vote);
		new LoadBalsavimai().execute();
		//getAll();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	public void CreateList(final String text, final String id){
		
		LinearLayout panele = (LinearLayout) findViewById(R.id.panele);
		Button bt = new Button(getApplicationContext());
		bt.setText(text);
		bt.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Intent intent = new Intent(Tab_Vote.this, variantai.class);
		    	intent.putExtra("id", id);
		    	intent.putExtra("pavadinimas", text);
		    	balsId = id;
		    	startActivityForResult(intent, 0);
		    }
		});
		panele.addView(bt);
		
	}
	
	 public void updateJSONdata() {
	    	
	        balsavimuSarasas = new ArrayList<HashMap<String, String>>();
	        //JSONParser jParser = new JSONParser();
	        
	        if(Home.networkStateListener.isInternetOn())
	        {
	        JSONObject json = JSONParser.getJSONFromUrl(URL);
	        
	        try {
	        	SQLiteCommandCenter.balsavimaiJsonToSqlite(json);
	        	
	            balsavimai = json.getJSONArray(POSTS);

	            // looping through all posts according to the json object returned
	            for (int i = 0; i < balsavimai.length(); i++) {
	                JSONObject c = balsavimai.getJSONObject(i);

	                //gets the content of each tag
	                
	                String pavadinimas = c.getString(PAVADINIMAS);
	                String ikeltas = c.getString(IKELTAS);
	                String pabaiga = c.getString(PABAIGA);
	                String id = c.getString(ID);
	                

	                // creating new HashMap
	                HashMap<String, String> map = new HashMap<String, String>();
	              
	                
	                map.put(PAVADINIMAS, pavadinimas);
	                map.put(IKELTAS, ikeltas);
	                map.put(PABAIGA, pabaiga);
	                map.put(ID, id);
	             
	                // adding HashList to ArrayList
	                balsavimuSarasas.add(map);
	                
	                //annndddd, our JSON data is up to date same with our array list
	            }

	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	        }
	        else {
	        	balsavimuSarasas = SQLiteCommandCenter.getTableBalsavimai();
			}
	    }
	 
	 public void getAll(){
		 for(int i = 0; i < balsavimuSarasas.size(); i++){
			 String a = balsavimuSarasas.get(i).get(PAVADINIMAS);
			 String b = balsavimuSarasas.get(i).get(ID);
			 CreateList(a, b);
		 }
	 }
	 
	 public class LoadBalsavimai extends AsyncTask<Void, Void, Boolean> {

	    	@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(Tab_Vote.this);
				pDialog.setMessage("Loading List...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}
	        @Override
	        protected Boolean doInBackground(Void... arg0) {
	        	//we will develop this method in version 2
	            updateJSONdata();
	            return null;

	        }


	        @Override
	        protected void onPostExecute(Boolean result) {
	            super.onPostExecute(result);
	            pDialog.dismiss();
	          //we will develop this method in version 2
	            getAll();
	        }
	    }
	

}
