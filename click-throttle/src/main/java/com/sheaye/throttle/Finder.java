package com.sheaye.throttle;

import android.app.Activity;
import android.view.View;

/**
 * Created by yexinyan on 2017/5/24.
 */

public enum Finder {

    VIEW{
        @Override
        public View findViewById(Object source, int id) {
            return ((View) source).findViewById(id);
        }
    },

    ACTIVITY{
        @Override
        public View findViewById(Object source, int id) {
            return ((Activity) source).findViewById(id);
        }
    };

    public abstract View findViewById(Object source, int id);

}
