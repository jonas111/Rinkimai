package rinkimai.pro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Tab_Me extends Activity {
	// cia saugosim balsavimo pavadinima ir varianta uz kuri balsuota
	
    HashMap<String, String> balsai = new HashMap<String, String>();
      
	protected void onCreate(Bundle savedInstanceState) {
	 
		//fake duomenys
		//balsai.put("Prezidento rinkimai", "Paulauskas");
	//	balsai.put("Prezidento rinkimai", "Zuokas");
		//balsai.put("Pralamento rinkimai", "Sernys");
		

	balsai= 	SQLiteCommandCenter.metodasArturui(getApplicationContext());
	//balsai.put("Pasaulio pabaiga", "rytoi");
	//balsai.put("Pralamento rinkimai", "Sernys");
		
		//tam kad is hashmap galetume istraukti visus duomenis naudosim mapset
		Set mapSet = (Set) balsai.entrySet();
		Iterator mapIterator = mapSet.iterator();
		// ViewListo adapteriams sukurti naudojama medziaga is array listo, taigi visus duomenis sukisim i array list
		ArrayList<String> list = new ArrayList<String>();
		
		while (mapIterator.hasNext()) {
             Map.Entry mapEntry = (Map.Entry) mapIterator.next();
             // getKey Method of HashMap access a key of map
             String keyValue = (String) mapEntry.getKey();
             //getValue method returns corresponding key's value
             String value = (String) mapEntry.getValue();
             
            // is Map set perduodame duomenis i array list 
             list.add( keyValue +" balsavote uz "+ value);
		}

		
		

		
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_me);
	
		
		   ListView history = (ListView) findViewById(R.id.lv_history);
		   TextView name = (TextView) findViewById(R.id.tv_name);
		   TextView yourHistory = (TextView) findViewById(R.id.tv_history);
		   Button userData = (Button)findViewById(R.id.button_edit_user_data);
		   
		   userData.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Input ived = new Input();
				
				ived.show(getFragmentManager(), "ss");
				
			}
		});
		   final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);
		   history.setAdapter(adapter);
		   
		  
		name.setText("Sveiki "+ VartotojoDuomenys.getEmail());
		yourHistory.setText(R.string.balsavote_uz);
	}
	// adapterio sukurimas 
	  private class StableArrayAdapter extends ArrayAdapter<String> {

		    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		    public StableArrayAdapter(Context context, int textViewResourceId,
		        List<String> objects) {
		      super(context, textViewResourceId, objects);
		      for (int i = 0; i < objects.size(); ++i) {
		        mIdMap.put(objects.get(i), i);
		      }
		    }

		    @Override
		    public long getItemId(int position) {
		      String item = getItem(position);
		      return mIdMap.get(item);
		    }

		    @Override
		    public boolean hasStableIds() {
		      return true;
		    }

		  } 
	  @SuppressLint("ValidFragment")
	public class Input extends DialogFragment
	  {

		  @Override
		public void onDestroyView() {
			  new PushToDb().execute();
			super.onDestroyView();
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
//			return super.onCreateDialog(savedInstanceState);
			  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			  
			  LayoutInflater inflater = getActivity().getLayoutInflater();
			  
			  
			  View maView = inflater.inflate(R.layout.dialod_user_data_input, null);
			  
			   builder.setView(maView);
			  
			   final EditText username = (EditText) maView.findViewById(R.id.username);
			   final EditText password = (EditText) maView.findViewById(R.id.password);
			   final EditText name = (EditText) maView.findViewById(R.id.name);
			   final EditText surename = (EditText) maView.findViewById(R.id.surename);
			   final EditText asmenskodas = (EditText) maView.findViewById(R.id.asmenskodas);
			   final EditText builetenio_nr = (EditText) maView.findViewById(R.id.builetenio_nr);
			   
			  builder.
			  setPositiveButton("Patvirtiniti", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if(!username.getText().toString().isEmpty()){
					VartotojoDuomenys.setEmail(username.getText().toString());
					}
					if(!password.getText().toString().isEmpty()){
					VartotojoDuomenys.setPassword(password.getText().toString());
					}
					if(!name.getText().toString().isEmpty()){
					VartotojoDuomenys.setName(name.getText().toString());
					}
					if(!surename.getText().toString().isEmpty()){
					VartotojoDuomenys.setSurename(surename.getText().toString());
					}
					if(!asmenskodas.getText().toString().isEmpty()){
					VartotojoDuomenys.setAsm_kod(asmenskodas.getText().toString());
					}
					if(!builetenio_nr.getText().toString().isEmpty()){
					VartotojoDuomenys.setBil_nr(builetenio_nr.getText().toString());
					}
					
					SQLiteCommandCenter.naujiVartotojoDuomenys();
					
					new PushToDb().execute();

				}
			}).setNegativeButton("Atsaukti", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Input.this.getDialog().cancel();
				}
			});
			  
			  return builder.create();
		}
	  }
	  public class PushToDb extends AsyncTask<String, String, String>
	  {
		  @Override
		protected void onPostExecute(String result) {
//			pDialog.dismiss();
		}
		private ProgressDialog pDialog;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
//				pDialog = new ProgressDialog(getApplicationContext());
//				pDialog.setMessage("Attempting login...");
//				pDialog.setIndeterminate(false);
//				pDialog.setCancelable(true);
//				pDialog.show();
			}
		@Override
		protected String doInBackground(String... params) {
//			if(Home.networkStateListener.isInternetOn())
//			{
//			try {
//				// Building Parameters
//				List<NameValuePair> params = new ArrayList<NameValuePair>();
//				params.add(new BasicNameValuePair("email", username));
//				params.add(new BasicNameValuePair("password", password));
//
//				Log.d("request!", "starting");
//				// getting product details by making HTTP request
//				JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
//						params);
//
//				// check your log for json response
//				Log.d("Login attempt", json.toString());
////				Toast.makeText(getApplicationContext(), json.getString("user_id"), Toast.LENGTH_LONG).show();
//				// json success tag
//				success = json.getInt(TAG_SUCCESS);
//				if (success == 1) {
//					Log.d("Login Successful!", json.toString());
//					// save user data
//					thisUser = username;
//					
//					VartotojoDuomenys.setEmail(username);
//					VartotojoDuomenys.setPassword(password);
//					SQLiteCommandCenter.loginDataToSql();
//					
//					// wtf ? --------------------------
//					SharedPreferences sp = PreferenceManager
//							.getDefaultSharedPreferences(Home.this);
//					Editor edit = sp.edit();
//					edit.putString("email", username);
//					edit.commit();
//					// --------------------------------
//					Intent i = new Intent(Home.this, MainTabs.class);
//					finish();
//					startActivity(i);
//					return json.getString(TAG_MESSAGE);
//				} else {
//					Log.d("Login Failure!", json.getString(TAG_MESSAGE));
//					return json.getString(TAG_MESSAGE);
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//			}
//			else {
//				success = 1;
//				Intent i = new Intent(Home.this, MainTabs.class);
//				finish();
//				startActivity(i);
//			}

			
			return null;
		}
		  
	  }
}
