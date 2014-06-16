package rinkimai.pro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Tab_Me extends Activity {
	// cia saugosim balsavimo pavadinima ir varianta uz kuri balsuota
	
    HashMap<String, String> balsai = new HashMap<String, String>();
    // fake user name
    private String user = "Arturas luksas";
  
  SQLiteCommandCenter db = new SQLiteCommandCenter(getBaseContext());

      
	
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
		   
		   final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);
		   history.setAdapter(adapter);
		   
		  
		name.setText("Sveiki "+ user);
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
}
