package com.scringo.scringosample;

import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.scringo.Scringo;
import com.scringo.Scringo.ScringoIcon;
import com.scringo.ScringoActivationButton;
import com.scringo.ScringoCommentButton;
import com.scringo.ScringoCommentButton.ScringoCommentObjectType;
import com.scringo.ScringoEventHandler;
import com.scringo.ScringoLeftRibbonButton;
import com.scringo.ScringoLikeButton;
import com.scringo.ScringoLikeButton.ScringoLikeObjectType;
import com.scringo.utils.ScringoLogger;
import com.scringo.utils.ScringoLogger.ScringoLogLevel;
//import com.scringo.ScringoRightRibbonButton;

public class MainActivity extends Activity {
	private static final String IMAGE_URL = "http://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Loboc_river.png/220px-Loboc_river.png";
	private static final String APP_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzO9n+7OGseMNGPBb+NVcRKXaVBWRJ2lUsQ0LpNqXqclKmw5SkvP6QpXfBPkIZZhWj3G2yEnnxZn6nI8L3vY9zspqGlxAM2Xqfc8ZmUOny+nNBwpyrWWCBbZVKNC8P7imcFKF8gbRUIHgViv/sEJ7OwaFKUig3Xg3Al04KxL33TvWFL9mGeIaUTS0yNgqW5Hcl0MYZHo2VfhToeKioDvKweI0KwN+LeEBTz3aW1F4MV2xprF3sSydiItcCqLumyWjKcyrc+ialteJFlUasWNyRRjOGlmqWbaxQXrTtCNWNaBJSe7tLexWcHqFFKrSEMQ2p11QXN3qZS7qpPqClW3QwwIDAQAB";
	private static final String RIVER_URL = "http://ichef.bbci.co.uk/naturelibrary/images/ic/credit/640x395/r/ri/river/river_1.jpg";

	private Scringo scringo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		scringo = new Scringo(this);
		new DownloadImageTask((ImageView) findViewById(R.id.riverImage)).execute(RIVER_URL);

		Scringo.setLogLevel(ScringoLogLevel.SCRINGO_LOG_LEVEL_DEBUG);
		Scringo.setDebugMode(true);
		scringo.setIcon(ScringoIcon.PERSON);
		Scringo.setGoogleAppPublicKey(APP_PUBLIC_KEY);

		boolean withSidebar = true;
		scringo.init(new ScringoEventHandler() {
			@Override
			public void onInitCompleted() {
				ScringoLogger.d("Init completion called");
			}
		});
		if (withSidebar) {
			// if you want a sidebar, call this
			scringo.addSidebar();
		} else {
			// Preload the data you want (optional)
			Scringo.preloadInbox();
			Scringo.preloadChatRooms();
		}
			
		((ScringoActivationButton) findViewById(R.id.activationButton)).setScringo(scringo);
		((ScringoLeftRibbonButton) findViewById(R.id.activationRibbonLeft)).setScringo(scringo);
//		((ScringoRightRibbonButton) findViewById(R.id.activationRibbonRight)).setScringo(scringo);

		((ScringoLikeButton) findViewById(R.id.riverLikeButton)).setLikeObject(scringo, "http://ichef.bbci.co.uk/naturelibrary/images/ic/credit/640x395/r/ri/river/river_1.jpg", ScringoLikeObjectType.IMAGE, "A River");
		((ScringoCommentButton) findViewById(R.id.riverCommentButton)).setCommentObject(scringo, "http://ichef.bbci.co.uk/naturelibrary/images/ic/credit/640x395/r/ri/river/river_1.jpg", ScringoCommentObjectType.IMAGE, "A River");

		findViewById(R.id.riverFeedButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Scringo.sendFeed("Yet another very nice river", IMAGE_URL, 
						"Menya River", "In Papua New Guinea", "View", IMAGE_URL);
			}
		});

		findViewById(R.id.openChatRoomButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Scringo.openChatRooms(MainActivity.this);
			}
		});

		findViewById(R.id.openInboxButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Scringo.openInbox(MainActivity.this);
			}
		});

		scringo.setEventHandler(new ScringoEventHandler() {
			@Override
			public void onPostItemClicked(String actionId) {
				if (actionId != null && actionId.equals(IMAGE_URL)) {
					Intent intent = new Intent(MainActivity.this, ImageViewActivity.class);
					intent.putExtra("url", actionId);
					startActivity(intent);
				}
			}

			@Override
			public void onMenuItemClicked(String menuId) {
				if (menuId != null && menuId.equals("myMenuId")) {
					Toast.makeText(MainActivity.this, "The App Optional Custom button " + menuId + " Clicked", Toast.LENGTH_LONG).show();
				}
			}
		});
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
}
