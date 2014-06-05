package rinkimai.pro;

import java.sql.SQLDataException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.JsonReader;
import android.widget.Toast;



public class SQLiteCommandCenter extends SQLiteOpenHelper
{
	private Context context;
	private JSONObject json ;
	// konstruktorius nereikalingas, inicializacijua bus statinis metodas
	public SQLiteCommandCenter(Context context) 
	{
		super(null, null, null, 1);
		this.context = context;
		
		SQLiteDatabase db = createdbIfNotExists();
		createTables(db);

		
		if(!isOutterSameSizeAsInner(null, db, null))
		{
			//Toast.makeText(context, json.toString(), Toast.LENGTH_LONG).show();
			try {
				jsonToSqlite();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void jsonToSqlite() throws JSONException
	{
			JSONArray isWebo = json.getJSONArray("posts");

			JSONObject eilut = isWebo.getJSONObject(1);
			
			Toast.makeText(context, eilut.toString(), Toast.LENGTH_SHORT).show();
			
//			JSONArray arr = json.getJSONArray("posts");
//			
//			for (int i = 0; i < arr.length(); i++) {
//				JSONObject eilute = arr.getJSONObject(i);
//
//                //gets the content of each tag
//               
                eilut.getString("pavadinimas");
                eilut.getString("id");
                eilut.getString("ikeltas");
//                
//                
//					Toast.makeText(context, eilute.getString("vart_id"), Toast.LENGTH_SHORT).show();
//					
//				}
			Toast.makeText(context, eilut.getString("id"), Toast.LENGTH_SHORT).show();
			//}
		
	}
	
	//konstruktorius tam kad DatabaseHelperis grazintu duomenu baze, kitaip neiseina
	public SQLiteCommandCenter(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	// palygina lenteliu dydzius
	private boolean isOutterSameSizeAsInner(String url, SQLiteDatabase db,String tableName)
	{
		json = JSONParser.getJSONFromUrl("http://rinkimai2014.coxslot.com/webservisas/balsavimai.php");
		
		String[] val = {"id"};
		
		if(json.length() == db.query("balsavimai", val, (String)null, (String[])null, (String)null, (String)null, (String)null).getCount())
		{
			return true;
		}
		return false;
	}
	
	// kuria lenteles jei neegzistuoja
	private void createTables(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS balsai "+
	"( vart_id int(11) NOT NULL, bals_id int(11) NOT NULL,"+
				" ats_id int(11) NOT NULL);");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS balsavimai "+
		"( id INTEGER PRIMARY KEY AUTOINCREMENT, pavadinimas text NOT NULL,"+
				" ikeltas datetime NOT NULL, pabaiga datetime NOT NULL) ; ");
		
	}
	
	//kurai db jei neegzistuoja
	private SQLiteDatabase createdbIfNotExists()
	{
		SQLiteCommandCenter qc = new SQLiteCommandCenter(context,"Mmusu.db",null,1);
		SQLiteDatabase db = qc.getReadableDatabase();
		
		return db;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	

}
