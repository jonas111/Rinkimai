package rinkimai.pro;

import java.sql.SQLDataException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.JsonReader;
import android.widget.Toast;



public class SQLiteCommandCenter extends SQLiteOpenHelper
{
	private Context context;
	private JSONObject json ;
	private SQLiteDatabase db;
	private String[] urls = 
		{
			"http://rinkimai2014.coxslot.com/webservisas/balsavimai.php"
		};
	
	// konstruktorius nereikalingas, inicializacijua bus statinis metodas
	public void SQLiteCommandCenterInit(Context context) 
	{	
		this.context = context;
		
		db = createdbIfNotExists();
		createTables(db);
		


		checkBalsavimai();
		

//			 String[] val = {"id"};
//			 Cursor cur = db.query("balsavimai",null , (String)null, (String[])null, (String)null, (String)null, (String)null);
//			cur.moveToFirst();
//			 cur.moveToNext();
//			 cur.moveToNext();
//			 cur.moveToNext();
			// cur.moveToNext();
//			 Toast.makeText(context, String.valueOf(cur.), Toast.LENGTH_SHORT).show();
	}
	
	private void checkBalsavimai() {
		if(!isOutterSameSizeAsInner("balsavimai"))
		{
			try {
				balsavimaiJsonToSqlite();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	private void balsavimaiJsonToSqlite() throws JSONException
	{
			JSONArray isWebo = json.getJSONArray("posts");

			db.delete("balsavimai", null, null);

			for (int i = 0; i < isWebo.length(); i++) {
				JSONObject eilut = isWebo.getJSONObject(i);

                ContentValues eilutesReiksmes = new ContentValues(4);
                
                eilutesReiksmes.put("id", eilut.getInt("id"));
                eilutesReiksmes.put("pavadinimas",eilut.getString("pavadinimas"));
                eilutesReiksmes.put("ikeltas", eilut.getInt("ikeltas"));
                eilutesReiksmes.put("pabaiga", eilut.getInt("pabaiga"));
                db.insert("balsavimai", null, eilutesReiksmes);   
				}
	}
	
	//konstruktorius tam kad DatabaseHelperis grazintu duomenu baze, kitaip neiseina
	public SQLiteCommandCenter(Context context) {
		super(context, "Mmusu.db", null, 1);
		SQLiteCommandCenterInit(context);
	}
	
	// palygina lenteliu dydzius, 
	// String : lenteles pavadinimas, taippat php failo pavadinimas
	private boolean isOutterSameSizeAsInner(String tablename)
	{
		json = JSONParser.getJSONFromUrl("http://rinkimai2014.coxslot.com/webservisas/"+tablename+".php");
		if(json.length() == db.query(tablename, null, (String)null, (String[])null, (String)null, (String)null, (String)null).getCount()){
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
		SQLiteCommandCenter qc = new SQLiteCommandCenter(context);
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
