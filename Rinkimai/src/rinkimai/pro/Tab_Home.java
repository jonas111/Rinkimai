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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Tab_Home extends Activity {
	
 	private ProgressDialog pDialog;
	private static final String URL = "http://rinkimai2014.coxslot.com/webservisas/vartotojai.php";

    
    private static final String POSTS = "posts";
    private static final String NAME = "name";
    private static final String SURENAME = "sure_name";
    private static final String EMAIL = "email";
    private static final String ID = "user_id";
    public static String user_id;
    
    private JSONArray jsonTemp = null;
    private ArrayList<HashMap<String, String>> vartotojai;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_home);	
		new Hello().execute();
		
	}
	
	public class Hello extends AsyncTask<Void, Void, Boolean> {

    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Tab_Home.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
        @Override
        protected Boolean doInBackground(Void... arg0) {
            updateJSONdata();
            return null;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            getUserData();
        }
    }
	
	public void updateJSONdata() {
    	
        vartotojai = new ArrayList<HashMap<String, String>>();
        
        JSONObject json = JSONParser.getJSONFromUrl(URL);
        
        try {
        	        	
            jsonTemp = json.getJSONArray(POSTS);

            // looping through all posts according to the json object returned
            for (int i = 0; i < jsonTemp.length(); i++) {
                JSONObject c = jsonTemp.getJSONObject(i);

                //gets the content of each tag
                
                String name = c.getString(NAME);
                String surename = c.getString(SURENAME);
                String email = c.getString(EMAIL);
                user_id = c.getString(ID);
                             
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();
                
                map.put(NAME, name);
                map.put(SURENAME, surename);
                map.put(EMAIL, email);
               
                vartotojai.add(map);
             
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
               
	}
	
	 public void SayHello(final String name, final String surename, final String email){
			
			LinearLayout panele = (LinearLayout) findViewById(R.id.panele5);
			TextView tt = (TextView) findViewById(R.id.pasisveikinimas);
			tt.setText("Sveiki "+name+" "+surename);
						
		}
	 
	 public void getUserData(){
		 for(int i = 0; i < vartotojai.size(); i++){
			 String a = vartotojai.get(i).get(NAME);
			 String b = vartotojai.get(i).get(SURENAME);
			 String c = vartotojai.get(i).get(EMAIL);
			 if(Home.thisUser.equals(c)) SayHello(a, b, c);
		 }
	 }

}
