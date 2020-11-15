package cn.lsmya.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 商品单价控件，带删除线
 * @author lsm
 */
public class PriceTextView extends AppCompatTextView {

    public PriceTextView(Context context) {
        this(context, null);
    }

    public PriceTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PriceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getPaint().setStrikeThruText(true);
        getPaint().setAntiAlias(true);
        super.onDraw(canvas);
    }
}
