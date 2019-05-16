package com.abhishek.android.apps.whichway;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class Extras extends Activity {

    void share_app(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=" + context.getPackageName());
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this cool App");
        context.startActivity(Intent.createChooser(intent, "Share"));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void rate(Uri uri, Context context) {
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

}
