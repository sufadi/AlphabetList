package com.wu.su.alphabetlist.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wu.su.alphabetlist.R;

/**
 * List of first letters on the right
 */
public class SideBarView extends View {

    public SideBarView(Context context) {
        super(context);
    }

    public SideBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static String[] characters = {"#", "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};
    private static final int SELECT_FONT_COLOR = 0xFF3399ff;
    private static final int NORMAL_FONT_COLOR = 0xFF999999;
    private static final double SCALING_RATIO = 0.85;

    private int ifSelected = -1;
    private Paint paint = new Paint();
    private String currCharacter = "#";
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    // 字母列表点击事件监听
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    // 绘制字母列表
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = (int) (getHeight() * SCALING_RATIO);
        int width = getWidth();
        int singleHeight = height / characters.length; // Get the height of each letter

        for (int i = 0; i < characters.length; i++) {
            paint.setColor(NORMAL_FONT_COLOR);
            paint.setAntiAlias(true);
            paint.setTextSize(getResources().getDimension(R.dimen.side_bar_font_size));
            if (characters[i].equals(currCharacter)) {
                paint.setColor(SELECT_FONT_COLOR);
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(characters[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(characters[i], xPos, yPos, paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldSelected = ifSelected;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int selected = (int) (y / (getHeight()* SCALING_RATIO) * characters.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                ifSelected = -1;
                invalidate();
                break;
            default:
                if (oldSelected != selected) {
                    if (selected >= 0 && selected < characters.length) {
                        if (listener != null) {
                            // 将事件通知ListView显示对应首字母内容
                            listener.onTouchingLetterChanged(characters[selected]);
                        }
                        ifSelected = selected;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    public void setCurrCharacter(String character) {
        if (!currCharacter.equals(character)) {
            currCharacter = character;
            invalidate();
        }
    }
}