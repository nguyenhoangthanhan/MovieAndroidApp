package com.example.movieapp.customview;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class ShowMoreTextView extends AppCompatTextView {

    private int showingLine = 1;

    private String showMore = "Show more";
    private String showLess = "Show less";
    private String threeDot = "…";

    private int showMoreTextColor = Color.RED;
    private int showLessTextColor = Color.RED;

    private String mainText = null;

    private boolean isAlreadySet = false;
    private boolean isCollapse = true;

    public ShowMoreTextView(@NonNull Context context) {
        super(context);

        getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (showingLine >= getLineCount()) return;
            showMoreButton();
        });
    }

    public ShowMoreTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (showingLine >= getLineCount()) return;
            showMoreButton();
        });
    }

    public ShowMoreTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (showingLine >= getLineCount()) return;
            showMoreButton();
        });
    }

    @Override
    protected void onFinishInflate() {
        mainText = getText().toString();
        super.onFinishInflate();
    }

    private void showMoreButton() {
        final String text = getText().toString();
        if (!isAlreadySet){
            mainText = text;
            isAlreadySet = true;
        }
        StringBuilder showingText = new StringBuilder();
        int start = 0;
        int end;
        for (int i = 0; i < showingLine; i++){
            end = getLayout().getLineEnd(i);
            showingText.append(text.substring(start, end));
            start = end;
        }
        int specialSpace = 0;
        String newText;

        do {
            newText = showingText.substring(0, showingText.length() - (specialSpace));
            newText += (threeDot + " " + showMore);
            setText(newText);
            specialSpace++;

        } while (getLineCount() > showingLine);
        isCollapse = true;
        setShowMoreColoringAndClickable();
    }

    private void setShowMoreColoringAndClickable() {
        SpannableString spannableString = new SpannableString(getText());
        spannableString.setSpan(
                new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        setMaxLines(2147483647);
                        setText(mainText);
                        isCollapse = false;
                        showLessButton();
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        ds.setUnderlineText(false);
                    }
                }
                , getText().length() - showMore.length(), getText().length(), 0
        );
        spannableString.setSpan(
                new ForegroundColorSpan(showMoreTextColor),
                getText().length() - showMore.length(),
                getText().length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        setMovementMethod(LinkMovementMethod.getInstance());
        setText(spannableString, BufferType.SPANNABLE);
    }

    private void showLessButton() {
        String text = getText() + " " + showLess;
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(

                new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        setMaxLines(showingLine);
                        showMoreButton();
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        ds.setUnderlineText(false);
                    }
                }, text.length() - showLess.length(), text.length(), 0
        );
        spannableString.setSpan(
                new ForegroundColorSpan(showLessTextColor),
                text.length() - (threeDot.length() + showLess.length()),
                text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        setMovementMethod(LinkMovementMethod.getInstance());
        setText(spannableString, BufferType.SPANNABLE);
    }

    // set maxlengt cho textview, chính là số dòng hiển thị tối đa khi ở chế độ show more
    public void setShowingLine(int lineNumber) {
        if (lineNumber == 0) return;
        showingLine = lineNumber;
        setMaxLines(showingLine);
    }

    // title của button show more
    public void addShowMoreText(String text) {
        showMore = text;
    }

    // title của button show less
    public void addShowLessText(String text) {
        showLess = text;
    }

    // color của button show more
    public void setShowMoreTextColor(int color) {
        showMoreTextColor = color;
    }

    // color của button show less
    public void setShowLessTextColor(int color) {
        showLessTextColor = color;
    }
}
