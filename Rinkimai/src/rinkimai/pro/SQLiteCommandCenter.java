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
import android.widget.Toast;



public class SQLiteCommandCenter extends SQLiteOpenHelper
{
	private Context context;
	private JSONObject json ;
	private SQLiteDatabase db;
	private int userId;  //      kazkaip patraukt userio id
	
	//duomenu baziu apdorojimo metodas
	public void SQLiteCommandCenterInit() 
	{		
		db = createdbIfNotExists();
		createTables(db);

		sinchronizuojaLenteles();
	}
	
	public void vidinisBalsavimas(String id, String varId)
	{
        ContentValues eilutesReiksmes = new ContentValues(3);
        
        eilutesReiksmes.put("vart_id","as");
        eilutesReiksmes.put("bals_id", id);
        eilutesReiksmes.put("ats_id",varId);
        db.delete("balsai", null, null);
        db.insert("balsai", null, eilutesReiksmes);   
		
        Cursor cur = db.query("balsai", null, (String)null, (String[])null, (String)null, (String)null, (String)null);
        cur.moveToNext();

        Toast.makeText(context, cur.getString(0)+" "+cur.getString(1)+" "+cur.getString(2), Toast.LENGTH_LONG).show();
	}
	
	private void sinchronizuojaLenteles() {
		try{
			checkBalsavimai();
		}
		catch(Exception e){
			// KOKY NO0RS HANDLERI, PER NAUJA PALEISTU TASKA JAI EXEPTIONAS, ARBA LAUKTU SALYGU
		}
		try{
			checkVariantai();
		}
		catch(Exception e){
		}
	}
	
	private void checkBalasai() {
		
		if(!isOutterSameSizeAsInner("balsai"))
		{
			try {
				balsaiJsonToSqlite();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	private void balsaiJsonToSqlite() throws JSONException
	{
		String lentele = "balsai";
			JSONArray isWebo = json.getJSONArray("posts");

			db.delete(lentele, null, null);

			for (int i = 0; i < isWebo.length(); i++) {
				JSONObject eilut = isWebo.getJSONObject(i);

                ContentValues eilutesReiksmes = new ContentValues(3);
                
                eilutesReiksmes.put("vart_id", eilut.getString("vart_id"));
                eilutesReiksmes.put("bals_id",eilut.getInt("bals_id"));
                eilutesReiksmes.put("ats_id", eilut.getInt("ats_id"));
                
                db.insert(lentele, null, eilutesReiksmes);   
				}
	}
	private void checkVariantai() {
		
		if(!isOutterSameSizeAsInner("variantai"))
		{
			try {
				variantaiJsonToSqlite();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	private void variantaiJsonToSqlite() throws JSONException
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
	private void checkBalsavimai() {
		if(!isOutterSameSizeAsInner("balsavimai"))
		{
			try {
				balsavimaiJsonToSqlite();
			} catch (Exception e) {
				e.printStackTrace();
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
		this.context = context;
	}
	
	// palygina lenteliu dydzius, 
	// String : lenteles pavadinimas, taippat php failo pavadinimas
	private boolean isOutterSameSizeAsInner(String tablename)
	{
		try{
			json = JSONParser.getJSONFromUrl("http://rinkimai2014.coxslot.com/webservisas/"+tablename+".php");
		}
		catch(Exception e){
			
		}
		if(json.length() == db.query(tablename, null, (String)null, (String[])null, (String)null, (String)null, (String)null).getCount()){
			return true;
		}
		return false;
	}
	
	// kuria lenteles jei neegzistuoja
	private void createTables(SQLiteDatabase db) {
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
	
	//kurai db jei neegzistuoja
	private SQLiteDatabase createdbIfNotExists()
	{
		SQLiteCommandCenter qc = this;
		// metodas arba kuria arba atidaro db kuria nurodome commandcenter superio konstruktoriuje
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
	public HashMap<String,String> metodasArturui()
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
        	if(ilgis<0)
        	{
        	cur.moveToNext();
        	tmp = db.query("balsavimai", pavadinimas, "id = "+cur.getString(1), null, null, null, null);
        	tmp.moveToNext();
        	tmp2 = db.query("variantai", variantas, "id = "+cur.getString(2), null, null, null, null);
        	tmp2.moveToNext();
        	result.put(tmp.getString(1), tmp2.getString(0));
        	}
        }
		
		return result;
	}
	

}
