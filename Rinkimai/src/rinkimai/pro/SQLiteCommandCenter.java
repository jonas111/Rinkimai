package rinkimai.pro;

import java.sql.SQLDataException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.JsonReader;
import android.widget.Toast;



public class SQLiteCommandCenter extends SQLiteOpenHelper
{
	private Context context;
	private JSONObject json ;
	private SQLiteDatabase db;

	//duomenu baziu apdorojimo metodas
	public void SQLiteCommandCenterInit() 
	{		
		db = createdbIfNotExists();
		createTables(db);

		NetworkStatusListener nl = new NetworkStatusListener();


		checkBalsavimai();
		checkBalasai();
		checkVariantai();
	}

	private void checkBalasai() {

		if(!isOutterSameSizeAsInner("balsai"))
		{
			try {
				balsaiJsonToSqlite();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
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
				Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
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

                ContentValues eilutesReiksmes = new ContentValues(3);
                
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
		this.context = context;
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

	public class NetworkStatusListener extends BroadcastReceiver
	{
	  @Override
	  public void onReceive( Context context, Intent intent )
	  {
	    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
	    NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
	    NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(     ConnectivityManager.TYPE_MOBILE );
	    if ( activeNetInfo != null )
	    {
	      Toast.makeText( context, "Active Network Type : " + activeNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
	    }
	    if( mobNetInfo != null )
	    {
	      Toast.makeText( context, "Mobile Network Type : " + mobNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
	    }
	  }
	}


}