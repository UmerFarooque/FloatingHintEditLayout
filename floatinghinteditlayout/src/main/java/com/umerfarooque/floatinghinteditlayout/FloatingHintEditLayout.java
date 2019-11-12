package com.umerfarooque.floatinghinteditlayout;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class FloatingHintEditLayout extends ConstraintLayout {

    private final String TEXT_COLOR_PROPERTY = "textColor";
    private final int INACTIVE_HINT_TEXT_SIZE = 16;
    private final int TRANSITION_DURATION_IN_MILLIS = 100;
    private final int inactiveStrokeWidth = convertDpToPx(1);
    private final int activeStrokeWidth = convertDpToPx(2);
    private final int errorStrokeWidth = convertDpToPx(1);

    private Context mContext;
    private AttributeSet mAttributeSet;
    private boolean isError = false;
    private GradientDrawable gradientDrawable;
    private TextView mHintTv;
    private EditText mEditText;
    private TextView mErrorTv;

    private int errorStrokeColor;
    private int editTextPadding;
    private int inactiveStrokeColor;
    private int activeStrokeColor;
    private int errorTextColor;
    private int inactiveHintColor;
    private int activeHintColor;
    private int errorHintColor;
    private float cornerRadius;

    private CharSequence hint;
    private CharSequence errorText;

    public FloatingHintEditLayout(@NonNull Context context) {
        super(context);
    }

    public FloatingHintEditLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAttributeSet = attrs;
        createAttributeFields();
        createHintTv();
        createErrorTv();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child instanceof EditText) {
            if (mEditText != null) {
                throw new IllegalArgumentException("Can only have on EditText");
            }
            setEditText((EditText) child);
            createHintTvConstraints();
            createErrorTvConstraints();
        }
    }

    private void createAttributeFields() {
        TypedArray a = mContext.obtainStyledAttributes(mAttributeSet, R.styleable.FloatingHintEditLayout);
        hint = a.getText(R.styleable.FloatingHintEditLayout_fhelHintText);
        errorText = a.getText(R.styleable.FloatingHintEditLayout_fhelErrorText);
        errorTextColor = a.getColor(R.styleable.FloatingHintEditLayout_fhelErrorTextColor, Color.RED);

        inactiveStrokeColor = a.getColor(R.styleable.FloatingHintEditLayout_fhelStrokeInactiveColor, Color.GRAY);
        activeStrokeColor = a.getColor(R.styleable.FloatingHintEditLayout_fhelStrokeActiveColor, Color.WHITE);
        errorStrokeColor = a.getColor(R.styleable.FloatingHintEditLayout_fhelStrokeErrorColor, Color.RED);

        cornerRadius = a.getDimension(R.styleable.FloatingHintEditLayout_fhelCornerRadius, 0);

        activeHintColor = a.getColor(R.styleable.FloatingHintEditLayout_fhelHintActiveColor, Color.WHITE);
        inactiveHintColor = a.getColor(R.styleable.FloatingHintEditLayout_fhelHintInactiveColor, Color.GRAY);
        errorHintColor = a.getColor(R.styleable.FloatingHintEditLayout_fhelHintErrorColor, Color.RED);
        a.recycle();
    }

    private void createHintTv() {
        mHintTv = new TextView(mContext);
        mHintTv.setId(View.generateViewId());
        mHintTv.setTranslationZ(2);
        mHintTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, INACTIVE_HINT_TEXT_SIZE);
        mHintTv.setTextColor(inactiveHintColor);
        mHintTv.setText(hint);
        addView(mHintTv);
    }

    private void createErrorTv() {
        int ERROR_TEXT_SIZE = 12;
        int DEFAULT_TEXT_VIEW_HEIGHT = 16;

        mErrorTv = new TextView(mContext);
        mErrorTv.setId(View.generateViewId());
        mErrorTv.setVisibility(View.INVISIBLE);
        mErrorTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, ERROR_TEXT_SIZE);
        mErrorTv.setTextColor(errorTextColor);
        mErrorTv.setText(errorText);
        addView(mErrorTv, ViewGroup.LayoutParams.WRAP_CONTENT, convertDpToPx(DEFAULT_TEXT_VIEW_HEIGHT));
    }

    private void setEditText(EditText editText) {
        mEditText = editText;
        createGradientDrawable();
        mEditText.setBackground(gradientDrawable);

        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    isError = false;
                    ((GradientDrawable) mEditText.getBackground()).setStroke(activeStrokeWidth, activeStrokeColor);
                    hideErrorTv();
                    animateUp();
                } else {
                    if (mEditText.getText().toString().isEmpty() || mEditText.getText() == null) {
                        animateBack();
                    }
                    if (isError) {
                        ((GradientDrawable) mEditText.getBackground()).setStroke(errorStrokeWidth, errorStrokeColor);
                    } else {
                        ((GradientDrawable) mEditText.getBackground()).setStroke(inactiveStrokeWidth, inactiveStrokeColor);
                    }
                }
            }
        });
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        constraintSet.connect(mEditText.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(mEditText.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        constraintSet.connect(mEditText.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(mEditText.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.applyTo(this);
        editTextPadding = editText.getPaddingStart();
    }

    private void createHintTvConstraints() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        constraintSet.connect(mHintTv.getId(), ConstraintSet.TOP, mEditText.getId(), ConstraintSet.TOP);
        constraintSet.connect(mHintTv.getId(), ConstraintSet.BOTTOM, mEditText.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(mHintTv.getId(), ConstraintSet.START, mEditText.getId(), ConstraintSet.START, editTextPadding);
        constraintSet.applyTo(this);
    }

    private void createErrorTvConstraints() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        constraintSet.connect(mErrorTv.getId(), ConstraintSet.TOP, mEditText.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(mErrorTv.getId(), ConstraintSet.START, mEditText.getId(), ConstraintSet.START, editTextPadding);
        constraintSet.applyTo(this);
    }

    private void animateUp() {
        int ACTIVE_HINT_TEXT_SIZE = 12;

        ChangeBounds transition = new ChangeBounds();
        transition.setDuration(TRANSITION_DURATION_IN_MILLIS);

        mHintTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, ACTIVE_HINT_TEXT_SIZE);

        TransitionManager.beginDelayedTransition(this, transition);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);

        constraintSet.clear(mHintTv.getId(), ConstraintSet.TOP);
        constraintSet.connect(mHintTv.getId(), ConstraintSet.BOTTOM, mEditText.getId(), ConstraintSet.TOP);
        constraintSet.applyTo(this);

        ObjectAnimator.ofObject(mHintTv, TEXT_COLOR_PROPERTY, new ArgbEvaluator(), inactiveHintColor, activeHintColor).setDuration(TRANSITION_DURATION_IN_MILLIS).start();
    }

    private void animateBack() {
        ChangeBounds transition = new ChangeBounds();
        transition.setDuration(TRANSITION_DURATION_IN_MILLIS);

        TransitionManager.beginDelayedTransition(this, transition);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);

        constraintSet.connect(mHintTv.getId(), ConstraintSet.BOTTOM, mEditText.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(mHintTv.getId(), ConstraintSet.TOP, mEditText.getId(), ConstraintSet.TOP);
        constraintSet.applyTo(this);

        mHintTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, INACTIVE_HINT_TEXT_SIZE);

        ObjectAnimator.ofObject(mHintTv, TEXT_COLOR_PROPERTY, new ArgbEvaluator(), activeHintColor, inactiveHintColor).setDuration(TRANSITION_DURATION_IN_MILLIS).start();
    }

    public void showError() {
        isError = true;
        mErrorTv.setVisibility(View.VISIBLE);
        if (!(mEditText.getText().toString().isEmpty() || mEditText.getText() == null)) {
            mHintTv.setTextColor(errorHintColor);
        }
    }

    public void setErrorText(String text) {
        this.errorText = text;
        this.mErrorTv.setText(text);
    }

    private void createGradientDrawable() {
        gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(cornerRadius);
        gradientDrawable.setColor(Color.WHITE);
        gradientDrawable.setStroke(inactiveStrokeWidth, inactiveStrokeColor);
    }

    public void hideErrorTv() {
        mErrorTv.setVisibility(View.INVISIBLE);
    }

    private int convertDpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
