package com.example.movies.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * ImageView quadrada cujo lado é do tamanho da largura
 * Created by tuliomoura on 10/20/15.
 */
public class SquareImageView extends ImageView {
    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
