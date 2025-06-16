package com.mE.Health.utility.roundview;

public class RoundRelativeLayout extends android.widget.RelativeLayout {
    private RoundViewDelegate delegate;

    public RoundRelativeLayout(android.content.Context context) {
        this(context, null);
    }

    public RoundRelativeLayout(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        delegate = new RoundViewDelegate(this, context, attrs);
    }

    /**
     * use delegate to set attr
     */
    public RoundViewDelegate getDelegate() {
        return delegate;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (delegate.isWidthHeightEqual() && getWidth() > 0 && getHeight() > 0) {
            int max = Math.max(getWidth(), getHeight());
            int measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
            super.onMeasure(measureSpec, measureSpec);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (delegate.isRadiusHalfHeight()) {
            delegate.setCornerRadius(getHeight() / 2);
        } else if (delegate.isRadiusHalfHeightLeft()) {
            delegate.setCornerRadiusLeft(getHeight() / 2);
        } else if (delegate.isRadiusHalfHeightRight()) {
            delegate.setCornerRadiusRight(getHeight() / 2);
        } else {
            delegate.setBgSelector();
        }
    }
}
