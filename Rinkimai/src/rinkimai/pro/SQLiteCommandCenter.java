package rinkimai.pro;


import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;



public class SQLiteCommandCenter extends SQLiteOpenHelper
{
	private static SQLiteDatabase db;
	
	public static boolean isInternetWorking(Context context)
	{
		ConnectivityManager connectivitymanager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
        NetworkInfo mobnetworkinfo = connectivitymanager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE );
		if(networkinfo != null && networkinfo.isConnected() || mobnetworkinfo != null && mobnetworkinfo.isConnected() ){
			return true;
		}
		return false;
	}
	
	public static HashMap<String,String> metodasArturui(Context context)
	{
		Cursor cur = db.query("balsai", null, (String)null, (String[])null, (String)null, (String)null, (String)null);
		int ilgis = cur.getCount();
		HashMap<String, String> result = new HashMap<String, String>();
		String[] pavadinimas = {"pavadinimas"};
		String[] variantas = {"variantas"};
		Cursor tmp;
		Cursor tmp2;
        for(int i = 0 ; i < ilgis ; i++)
        {
        	if(ilgis>0)
        	{
        	cur.moveToNext();
        	tmp = db.query("balsavimai", pavadinimas, "id = "+cur.getString(1), null, null, null, null);
        	Toast.makeText(context, cur.getColumnName(0) + cur.getString(0)+cur.getString(1)+cur.getString(2), Toast.LENGTH_LONG).show();
        	tmp.moveToNext();
//        	tmp2 = db.query("variantai", variantas, "id = "+cur.getString(2), null, null, null, null);
//        	tmp2.moveToNext();
        	result.put(tmp.getString(0), cur.getString(2));
        	}
        }
		
		return result;
	}
	
	
	public static void vidinisBalsavimas(String id, String varId)
	{
        ContentValues eilutesReiksmes = new ContentValues(3);
        
        eilutesReiksmes.put("vart_id","as");
        eilutesReiksmes.put("bals_id", id);
        eilutesReiksmes.put("ats_id",varId);
        db.delete("balsai", null, null);
        db.insert("balsai", null, eilutesReiksmes);   
		
        Cursor cur = db.query("balsai", null, (String)null, (String[])null, (String)null, (String)null, (String)null);
        cur.moveToNext();

//        Toast.makeText(context, cur.getString(0)+" "+cur.getString(1)+" "+cur.getString(2), Toast.LENGTH_LONG).show();
	}
	

	public static void variantaiJsonToSqlite(JSONObject json) throws JSONException
	{
		String lentele = "variantai";
			JSONArray isWebo = json.getJSONArray("posts");

			db.delete(lentele, null, null);

			for (int i = 0; i < isWebo.length(); i++) {
				JSONObject eilut = isWebo.getJSONObject(i);

                ContentValues eilutesReiksmes = new ContentValues(4);
                
                eilutesReiksmes.put("id", eilut.getInt("id"));
                eilutesReiksmes.put("var_id", eilut.getInt("var_id"));
                eilutesReiksmes.put("info",eilut.getString("info"));
                eilutesReiksmes.put("variantas", eilut.getString("variantas"));
                
                db.insert(lentele, null, eilutesReiksmes);   
				}
	}

	public static void balsavimaiJsonToSqlite(JSONObject json) throws JSONException
	{
		String lentele = "balsavimai";
			JSONArray isWebo = json.getJSONArray("posts");

			db.delete(lentele, null, null);

			for (int i = 0; i < isWebo.length(); i++) {
				JSONObject eilut = isWebo.getJSONObject(i);

                ContentValues eilutesReiksmes = new ContentValues(4);
                
                eilutesReiksmes.put("id", eilut.getInt("id"));
                eilutesReiksmes.put("pavadinimas",eilut.getString("pavadinimas"));
                eilutesReiksmes.put("ikeltas", eilut.getString("ikeltas"));
                eilutesReiksmes.put("pabaiga", eilut.getString("pabaiga"));
                db.insert(lentele, null, eilutesReiksmes);   
				}
	}
	
	public static SQLiteDatabase initiateDb(Context context)
	{
		db = new SQLiteCommandCenter(context).getReadableDatabase();
		createTables(db);
		return db;
	}
	
	//konstruktorius tam kad DatabaseHelperis grazintu duomenu baze, kitaip neiseina
	public SQLiteCommandCenter(Context context) {
		super(context, "Mmusu.db", null, 1);
//		this.context = context;
	}
	
	// kuria lenteles jei neegzistuoja
	private static void createTables(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS balsai "+
	"( vart_id text NOT NULL,"
	+ " bals_id int(11) NOT NULL,"+
				" ats_id int(11) NOT NULL);");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS balsavimai ("
				+ " id int(11),"
		+ " pavadinimas text NOT NULL,"+
				" ikeltas datetime NOT NULL,"
				+ " pabaiga datetime NOT NULL) ; ");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS variantai ("+
		" id int(11) NOT NULL, "
		+ " var_id int(11),"+
				" info text NOT NULL,  "
				+ "variantas text NOT NULL) ");
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
