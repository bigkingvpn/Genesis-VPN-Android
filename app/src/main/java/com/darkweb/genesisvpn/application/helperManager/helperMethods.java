package com.darkweb.genesisvpn.application.helperManager;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import com.darkweb.genesisvpn.application.constants.strings;
import com.darkweb.genesisvpn.application.homeManager.home_model;

public class helperMethods
{
    public static void shareApp() {
        ShareCompat.IntentBuilder.from(home_model.getInstance().getHomeInstance())
                .setType(strings.sh_type)
                .setChooserTitle(strings.sh_title)
                .setSubject(strings.sh_subject)
                .setText(strings.sh_desc + home_model.getInstance().getHomeInstance().getPackageName())
                .startChooser();
    }

    public static void sendEmail()
    {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(strings.co_type)); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, "");
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        if (intent.resolveActivity(home_model.getInstance().getHomeInstance().getPackageManager()) != null) {
            home_model.getInstance().getHomeInstance().startActivity(intent);
        }
    }

    public static void quit(AppCompatActivity activity) {
        activity.finish();
    }

    public static void openActivity( Class<?> cls){
        Intent myIntent = new Intent(home_model.getInstance().getHomeInstance(), cls);
        home_model.getInstance().getHomeInstance().startActivity(myIntent);
    }

}
