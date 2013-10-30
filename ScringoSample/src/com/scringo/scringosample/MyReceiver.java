package com.scringo.scringosample;

import com.scringo.Scringo;
import com.scringo.Scringo.ScringoFeature;
import com.scringo.utils.ScringoLogger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("com.scringo.BadgeBroadcast")) {
			ScringoFeature feature = (ScringoFeature) intent.getExtras().getSerializable("feature");
			ScringoLogger.e("Got Badge receiver: " + feature + ", " + Scringo.getBadgeValue(feature));
		} else if (intent.getAction().equals("com.scringo.LoginBroadcast")) {
			boolean isLogin = intent.getExtras().getBoolean("isLogin");
			String accountId = intent.getExtras().getString("accountId");
			ScringoLogger.e("Got Login receiver: " + isLogin + ", " + accountId);		
		}
	}
}
