package com.darkweb.genesisvpn.application.homeManager;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;

class home_animations {

    static ObjectAnimator circleGrow(Object target)
    {
        return ObjectAnimator.ofPropertyValuesHolder(
                target,
                PropertyValuesHolder.ofFloat("scaleX", 1.3f),
                PropertyValuesHolder.ofFloat("scaleY", 1.3f),
                PropertyValuesHolder.ofFloat("alpha", 0f)
        );
    }

    static ObjectAnimator rorateView(Object target)
    {
        ObjectAnimator imageViewObjectAnimator = ObjectAnimator.ofFloat(target ,"rotation", 0f, 360f);
        imageViewObjectAnimator.setDuration(1000); // miliseconds
        imageViewObjectAnimator.start();

        return imageViewObjectAnimator;
    }

}
