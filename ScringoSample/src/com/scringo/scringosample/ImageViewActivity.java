package com.scringo.scringosample;

import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

import com.scringo.Scringo;

public class ImageViewActivity extends Activity {
	private Scringo scringo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);
		String url = getIntent().getStringExtra("url");
		scringo = new Scringo(this);
	    scringo.init();
	    new DownloadImageTask((ImageView)findViewById(R.id.image)).execute(url);
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_view, menu);
		return true;
	}

	@Override
	protected void onStart() {
        super.onStart();
        scringo.onStart();
	}

	@Override
	protected void onStop() {
        super.onStop();
        scringo.onStop();
	}

	@Override
	public void onBackPressed() {
        if (!scringo.onBackPressed()) {
        	super.onBackPressed();
        }
	}
}
