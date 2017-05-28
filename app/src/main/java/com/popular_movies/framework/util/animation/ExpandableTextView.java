package com.popular_movies.framework.util.animation;

import android.animation.ObjectAnimator;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * Created by Gurpreet on 08-04-2017.
 */

public class ExpandableTextView {

    private static final String PROPERTY_MAX_LINES = "maxLines";
    private static final int COLLAPSE_MAX_LINES = 10;

    public void init(TextView textView) {
        int lineCount = textView.getLineCount();
        if(lineCount > textView.getMaxLines()) {
            lineCount = COLLAPSE_MAX_LINES;
        }
        ObjectAnimator animation = ObjectAnimator.ofInt(textView, PROPERTY_MAX_LINES, lineCount);
        animation.setDuration(200).start();
    }

    public void expandTextView(TextView tv){
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, PROPERTY_MAX_LINES, tv.getLineCount());
        animation.setDuration(200).start();
    }

    public void collapseTextView(TextView tv, int numLines){
        tv.setEllipsize(TextUtils.TruncateAt.END);
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, PROPERTY_MAX_LINES, numLines);
        animation.setDuration(200).start();
    }

    public void cycleTextViewExpansion(TextView tv){
       /* if(tv.getMaxLines() == COLLAPSE_MAX_LINES) {
            tv.setEllipsize(null);
            tv.setLines(tv.getLineCount());
        }
        else {
            tv.setEllipsize(TextUtils.TruncateAt.END);
            tv.setLines(COLLAPSE_MAX_LINES);
        }*/
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, PROPERTY_MAX_LINES,
                tv.getMaxLines() == COLLAPSE_MAX_LINES ? tv.getLineCount() : COLLAPSE_MAX_LINES);
        animation.setDuration(200).start();
    }
}
