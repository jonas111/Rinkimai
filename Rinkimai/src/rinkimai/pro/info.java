package rinkimai.pro;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class info extends Activity {
	
	 public void onCreate(Bundle savedInstanceState) { 
		 
		 	LinearLayout panele = (LinearLayout) findViewById(R.id.panele3);

	        super.onCreate(savedInstanceState);    
	        setContentView(R.layout.info);
	        TextView tv = (TextView)findViewById(R.id.variantas);
	        TextView inf = (TextView)findViewById(R.id.info);
	        Intent iin= getIntent();
	        Bundle b = iin.getExtras();

	        if(b!=null)
	        {
	            tv.setText((String) b.get("pavadinimas"));
	            inf.setText((String) b.get("info"));
	        }
	        backBtn();
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
	        //    finish();

	    }


}
