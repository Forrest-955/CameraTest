/*
 * Copyright(c) 2017 Haocent Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.itep.mt.common.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itep.mt.common.R;
import com.itep.mt.common.util.DateUtil;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;

/**
 * 显示时间控件
 */

public class TvClockWidget extends LinearLayout {

    private static final String TAG = TvClockWidget.class.getSimpleName();

    private Timer timer;
    private Handler handler = new Handler();
    private TextView timeView;
    private String lastTime;

    public TvClockWidget (Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        addView(inflate(context, R.layout.tv_clock_widget, null));

        timeView = (TextView) findViewById(R.id.time);

        start();
    }

    /**
     * Thread to update clock every second
     */
    public void start() {
        Log.i(TAG, "start: ");
        if (timer == null) {
            timer = new Timer();
        }
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
//                final String time = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(),
//                        DateUtils.FORMAT_UTC);
                final String time = DateUtil.getCurrentTimeStrByFormat();
                if (!time.equals(lastTime)) {
                    handler.post(new Runnable() {
                        public void run() {
                            Log.i(TAG, "run: ");
                            timeView.setText(time);
                        }
                    });
                    lastTime = time;
                }
            }
        }, 0, 1000); // every second
    }

    /**
     * Stop the update thread
     */
    public void stop() {
        Log.i(TAG, "stop: ");
        timer.cancel();
        timer = null;
    }

}
