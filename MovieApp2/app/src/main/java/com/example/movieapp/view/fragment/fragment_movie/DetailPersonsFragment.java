package com.example.movieapp.view.fragment.fragment_movie;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.databinding.LayoutItemPersonBinding;
import com.example.movieapp.model.Person;
import com.example.movieapp.utils.CONSTANT;
import com.example.movieapp.utils.Utils;

public class DetailPersonsFragment extends Fragment implements View.OnClickListener{

    private LayoutItemPersonBinding layoutItemPersonBinding;

    private Person mPerson;

    private int positionMMovie = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setLanguage(requireContext());

        Bundle bundle = getArguments();
        if (bundle != null) {
            mPerson = bundle.getParcelable(CONSTANT.KEY_SEND_PERSON);

            positionMMovie = bundle.getInt(CONSTANT.KEY_CURRENT_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutItemPersonBinding = LayoutItemPersonBinding.inflate(inflater, container, false);
        return layoutItemPersonBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        initEvents();
    }

    private void init() {
        layoutItemPersonBinding.tvPersonName.setText(mPerson.getName());
        layoutItemPersonBinding.tvBirthdayPerson.setText(mPerson.getBirthDay());

        layoutItemPersonBinding.tvPersonInfo.setText(mPerson.getBiography());
        makeTextViewResizable(layoutItemPersonBinding.tvPersonInfo, 3, "View More", true);

        String urlImg = "https://image.tmdb.org/t/p/w500" + mPerson.getProfilePath();
        Glide.with(requireContext()).load(urlImg).placeholder(R.drawable.beginner3).dontAnimate().into(layoutItemPersonBinding.imgAvatarPerson);
    }

    private void initEvents() {
        layoutItemPersonBinding.btnDone.setOnClickListener(this);
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeOnGlobalLayoutListener(this);

                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }
                tv.setText(text);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(
                        addClickablePartTextViewResizable(new SpannableString(tv.getText().toString()), tv, lineEndIndex, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);
            }
        });
    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        makeTextViewResizable(tv, 3, "View More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        layoutItemPersonBinding = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_done){
            requireActivity().onBackPressed();
        }
    }
}
