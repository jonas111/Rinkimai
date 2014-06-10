package rinkimai.pro;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Tab_Stats extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_stats);
		
		final WebView myWebView = (WebView) findViewById(R.id.webview); //sukuriamas laukas, kuris atvaizduoja web puslapi.
		WebSettings webSettings = myWebView.getSettings(); //igalina nustatymus WebView klasei.
		webSettings.setJavaScriptEnabled(true); //ijungia JavaScript.
		myWebView.loadUrl("http://rinkimai2014.coxslot.com/webservisas/stats.php"); //atidaro stats.php
		
		myWebView.setWebViewClient(new WebViewClient(){ //myWebView implementacija.

	        @Override
	        public void onPageFinished(WebView view, String url) { //randa grafiko centro koordinates, kai baigia krauti puslapi.
	            // TODO Auto-generated method stub
	        	DisplayMetrics displaymetrics = new DisplayMetrics(); //struktura reikalinga rasti ekrano matmenims.
	    		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics); //randa ekrano matmenis.
	    		int height = displaymetrics.heightPixels; //aukstis
	    		int width = displaymetrics.widthPixels; //plotis
	            super.onPageFinished(view, url); //pranesa, kada puslapis buvo baigtas krauti.
	            myWebView.scrollTo((width/48*9), height); // nustumia grafika i norima pozicija.
	        }

	    }
	    );
		
		myWebView.setOnTouchListener(new View.OnTouchListener() { //iskvieciamas callback'as, kai palieciamas web puslapis.

		    public boolean onTouch(View v, MotionEvent event) { //listeneris pasileidzia dar pries palieciant ekrana.
		      return (event.getAction() == MotionEvent.ACTION_MOVE); //neleidzia pajudinti puslapio.
		    }
		});
	}
}