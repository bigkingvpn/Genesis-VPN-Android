package com.darkweb.genesisvpn.application.helperManager;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

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

    public static int screenWidth()
    {
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;

        if(width>height)
        {
            return height;
        }
        {
            return width;
        }

    }

    public static void screenToFont(TextView textView, int desiredWidth)
    {
        Paint paint = new Paint();
        Rect bounds = new Rect();

        paint.setTypeface(textView.getTypeface());
        float textSize = textView.getTextSize();
        paint.setTextSize(textSize);
        String text = textView.getText().toString();
        paint.getTextBounds(text, 0, text.length(), bounds);

        while (bounds.width() > desiredWidth)
        {
            textSize--;
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), bounds);
        }

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize+30);
    }

    public static RotateAnimation getRotationAnimation(){
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        rotate.setDuration(2000);
        rotate.setRepeatCount(Animation.INFINITE);
        return rotate;
    }

    public static Spannable getScreenText(float size,String text)
    {
        Spannable span = new SpannableString(text);
        span.setSpan(new RelativeSizeSpan(size), 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }


}
