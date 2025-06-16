/*
 * Copyright (C) 2015 Vincent Mi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mE.Health.utility.roundview;


import com.mE.Health.R;

public class RoundedImageView extends androidx.appcompat.widget.AppCompatImageView {

    // Constants for tile mode attributes
    private static final int TILE_MODE_UNDEFINED = -2;
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_REPEAT = 1;
    private static final int TILE_MODE_MIRROR = 2;

    public static final float DEFAULT_RADIUS = 0f;
    public static final float DEFAULT_BORDER_WIDTH = 0f;
    public static final android.graphics.Shader.TileMode DEFAULT_TILE_MODE = android.graphics.Shader.TileMode.CLAMP;
    private static final ScaleType[] SCALE_TYPES = {ScaleType.MATRIX,
            ScaleType.FIT_XY, ScaleType.FIT_START, ScaleType.FIT_CENTER,
            ScaleType.FIT_END, ScaleType.CENTER, ScaleType.CENTER_CROP,
            ScaleType.CENTER_INSIDE};

    private final float[] mCornerRadii = new float[]{DEFAULT_RADIUS,
            DEFAULT_RADIUS, DEFAULT_RADIUS, DEFAULT_RADIUS};

    private android.graphics.drawable.Drawable mBackgroundDrawable;
    private android.content.res.ColorStateList mBorderColor = android.content.res.ColorStateList
            .valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
    private float mBorderWidth = DEFAULT_BORDER_WIDTH;
    private android.graphics.ColorFilter mColorFilter = null;
    private boolean mColorMod = false;
    private android.graphics.drawable.Drawable mDrawable;
    private boolean mHasColorFilter = false;
    private boolean mIsOval = false;
    private boolean mMutateBackground = false;
    private int mResource;
    private ScaleType mScaleType = ScaleType.FIT_CENTER;
    private android.graphics.Shader.TileMode mTileModeX = DEFAULT_TILE_MODE;
    private android.graphics.Shader.TileMode mTileModeY = DEFAULT_TILE_MODE;

    public RoundedImageView(android.content.Context context) {
        super(context);
    }

    public RoundedImageView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedImageView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView, defStyle, 0);

        int index = a.getInt(R.styleable.RoundedImageView_android_scaleType, -1);
        if (index >= 0) {
            setScaleType(SCALE_TYPES[index]);
        } else {
            // default scaletype to FIT_CENTER
            setScaleType(ScaleType.FIT_CENTER);
        }
        float cornerRadiusOverride = a.getDimensionPixelSize(R.styleable.RoundedImageView_rv_cornerRadius, -1);
        mCornerRadii[RoundedDrawable.Corner.TOP_LEFT.ordinal()] = a.getDimensionPixelSize(R.styleable.RoundedImageView_rv_cornerRadius_TL, -1);
        mCornerRadii[RoundedDrawable.Corner.TOP_RIGHT.ordinal()] = a.getDimensionPixelSize(R.styleable.RoundedImageView_rv_cornerRadius_TR, -1);
        mCornerRadii[RoundedDrawable.Corner.BOTTOM_RIGHT.ordinal()] = a.getDimensionPixelSize(R.styleable.RoundedImageView_rv_cornerRadius_BR, -1);
        mCornerRadii[RoundedDrawable.Corner.BOTTOM_LEFT.ordinal()] = a.getDimensionPixelSize(R.styleable.RoundedImageView_rv_cornerRadius_BL, -1);

        boolean any = false;
        for (int i = 0, len = mCornerRadii.length; i < len; i++) {
            if (mCornerRadii[i] < 0) {
                mCornerRadii[i] = 0f;
            } else {
                any = true;
            }
        }

        if (!any) {
            if (cornerRadiusOverride < 0) {
                cornerRadiusOverride = DEFAULT_RADIUS;
            }
            for (int i = 0, len = mCornerRadii.length; i < len; i++) {
                mCornerRadii[i] = cornerRadiusOverride;
            }
        }

        mBorderWidth = a.getDimensionPixelSize(R.styleable.RoundedImageView_rv_strokeWidth, -1);
        if (mBorderWidth < 0) {
            mBorderWidth = DEFAULT_BORDER_WIDTH;
        }

        mBorderColor = a.getColorStateList(R.styleable.RoundedImageView_rv_strokeColor);
        if (mBorderColor == null) {
            mBorderColor = android.content.res.ColorStateList
                    .valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
        }

        mMutateBackground = a.getBoolean(R.styleable.RoundedImageView_rv_mutateBackground, false);
        mIsOval = a.getBoolean(R.styleable.RoundedImageView_rv_isOval, false);

        final int tileMode = a.getInt(R.styleable.RoundedImageView_rv_tileMode, TILE_MODE_UNDEFINED);
        if (tileMode != TILE_MODE_UNDEFINED) {
            setTileModeX(parseTileMode(tileMode));
            setTileModeY(parseTileMode(tileMode));
        }

        final int tileModeX = a.getInt(R.styleable.RoundedImageView_rv_tileMode_x, TILE_MODE_UNDEFINED);
        if (tileModeX != TILE_MODE_UNDEFINED) {
            setTileModeX(parseTileMode(tileModeX));
        }

        final int tileModeY = a.getInt(R.styleable.RoundedImageView_rv_tileMode_y, TILE_MODE_UNDEFINED);
        if (tileModeY != TILE_MODE_UNDEFINED) {
            setTileModeY(parseTileMode(tileModeY));
        }

        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(true);

        a.recycle();
    }

    private static android.graphics.Shader.TileMode parseTileMode(int tileMode) {
        switch (tileMode) {
            case TILE_MODE_CLAMP:
                return android.graphics.Shader.TileMode.CLAMP;
            case TILE_MODE_REPEAT:
                return android.graphics.Shader.TileMode.REPEAT;
            case TILE_MODE_MIRROR:
                return android.graphics.Shader.TileMode.MIRROR;
            default:
                return null;
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    @Override
    public ScaleType getScaleType() {
        return mScaleType;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        assert scaleType != null;

        if (mScaleType != scaleType) {
            mScaleType = scaleType;

            switch (scaleType) {
                case CENTER:
                case CENTER_CROP:
                case CENTER_INSIDE:
                case FIT_CENTER:
                case FIT_START:
                case FIT_END:
                case FIT_XY:
                    super.setScaleType(ScaleType.FIT_XY);
                    break;
                default:
                    super.setScaleType(scaleType);
                    break;
            }

            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    @Override
    public void setImageDrawable(android.graphics.drawable.Drawable drawable) {
        mResource = 0;
        mDrawable = RoundedDrawable.fromDrawable(drawable);
        updateDrawableAttrs();
        super.setImageDrawable(mDrawable);
    }

    @Override
    public void setImageBitmap(android.graphics.Bitmap bm) {
        mResource = 0;
        mDrawable = RoundedDrawable.fromBitmap(bm);
        updateDrawableAttrs();
        super.setImageDrawable(mDrawable);
    }

    @Override
    public void setImageResource(@androidx.annotation.DrawableRes int resId) {
        if (mResource != resId) {
            mResource = resId;
            mDrawable = resolveResource();
            updateDrawableAttrs();
            super.setImageDrawable(mDrawable);
        }
    }

    @Override
    public void setImageURI(android.net.Uri uri) {
        super.setImageURI(uri);
        setImageDrawable(getDrawable());
    }

    private android.graphics.drawable.Drawable resolveResource() {
        android.content.res.Resources rsrc = getResources();
        if (rsrc == null) {
            return null;
        }

        android.graphics.drawable.Drawable d = null;

        if (mResource != 0) {
            try {
                d = rsrc.getDrawable(mResource);
            } catch (Exception e) {
                e.printStackTrace();
                // Don't try again.
                mResource = 0;
            }
        }
        return RoundedDrawable.fromDrawable(d);
    }

    // @Override
    // public void setBackground(Drawable background) {
    // // setBackgroundDrawable(background);
    // }

    private void updateDrawableAttrs() {
        updateAttrs(mDrawable);
    }

    private void updateBackgroundDrawableAttrs(boolean convert) {
        if (mMutateBackground) {
            if (convert) {
                mBackgroundDrawable = RoundedDrawable
                        .fromDrawable(mBackgroundDrawable);
            }
            updateAttrs(mBackgroundDrawable);
        }
    }

    @Override
    public void setColorFilter(android.graphics.ColorFilter cf) {
        if (mColorFilter != cf) {
            mColorFilter = cf;
            mHasColorFilter = true;
            mColorMod = true;
            applyColorMod();
            invalidate();
        }
    }

    private void applyColorMod() {
        // Only mutate and apply when modifications have occurred. This should
        // not reset the mColorMod flag, since these filters need to be
        // re-applied if the Drawable is changed.
        if (mDrawable != null && mColorMod) {
            mDrawable = mDrawable.mutate();
            if (mHasColorFilter) {
                mDrawable.setColorFilter(mColorFilter);
            }
            // TODO: support, eventually...
            // mDrawable.setXfermode(mXfermode);
            // mDrawable.setAlpha(mAlpha * mViewAlphaScale >> 8);
        }
    }

    private void updateAttrs(android.graphics.drawable.Drawable drawable) {
        if (drawable == null) {
            return;
        }

        if (drawable instanceof RoundedDrawable) {
            ((RoundedDrawable) drawable).setScaleType(mScaleType)
                    .setBorderWidth(mBorderWidth).setBorderColor(mBorderColor)
                    .setOval(mIsOval).setTileModeX(mTileModeX)
                    .setTileModeY(mTileModeY);

            if (mCornerRadii != null) {
                ((RoundedDrawable) drawable).setCornerRadius(mCornerRadii[0],
                        mCornerRadii[1], mCornerRadii[2], mCornerRadii[3]);
            }

            applyColorMod();
        } else if (drawable instanceof android.graphics.drawable.LayerDrawable) {
            // loop through layers to and set drawable attrs
            android.graphics.drawable.LayerDrawable ld = ((android.graphics.drawable.LayerDrawable) drawable);
            for (int i = 0, layers = ld.getNumberOfLayers(); i < layers; i++) {
                updateAttrs(ld.getDrawable(i));
            }
        }
    }

    // @Override
    // @Deprecated
    // public void setBackgroundDrawable(Drawable background) {
    // mBackgroundDrawable = background;
    // updateBackgroundDrawableAttrs(true);
    // super.setBackgroundDrawable(mBackgroundDrawable);
    // }

    /**
     * @return the corner radius.
     */
    public float getCornerRadius() {
        for (int i = 0; i < 4; i++) {
            if (mCornerRadii[i] > 0) {
                return mCornerRadii[i];
            }
        }
        return 0;
    }

    /**
     * Set all the corner radii from a dimension resource id.
     *
     * @param resId dimension resource id of radii.
     */
    public void setCornerRadiusDimen(@androidx.annotation.DimenRes int resId) {
        float radius = getResources().getDimension(resId);
        setCornerRadius(radius, radius, radius, radius);
    }

    /**
     * Set the corner radii of all corners in px.
     *
     * @param radius the radius.
     */
    public void setCornerRadius(float radius) {
        setCornerRadius(radius, radius, radius, radius);
    }

    /**
     * Set the corner radii of each corner individually. Currently only one
     * unique nonzero value is supported.
     *
     * @param topLeft     radius of the top left corner in px.
     * @param topRight    radius of the top right corner in px.
     * @param bottomRight radius of the bottom right corner in px.
     * @param bottomLeft  radius of the bottom left corner in px.
     */
    public void setCornerRadius(float topLeft, float topRight,
                                float bottomLeft, float bottomRight) {
        if (mCornerRadii[RoundedDrawable.Corner.TOP_LEFT.ordinal()] == topLeft
                && mCornerRadii[RoundedDrawable.Corner.TOP_RIGHT.ordinal()] == topRight
                && mCornerRadii[RoundedDrawable.Corner.BOTTOM_RIGHT.ordinal()] == bottomRight
                && mCornerRadii[RoundedDrawable.Corner.BOTTOM_LEFT.ordinal()] == bottomLeft) {
            return;
        }

        mCornerRadii[0] = topLeft;
        mCornerRadii[1] = topRight;
        mCornerRadii[2] = bottomLeft;
        mCornerRadii[3] = bottomRight;

        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(@androidx.annotation.DimenRes int resId) {
        setBorderWidth(getResources().getDimension(resId));
    }

    public void setBorderWidth(float width) {
        if (mBorderWidth == width) {
            return;
        }

        mBorderWidth = width;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public int getBorderColor() {
        return mBorderColor.getDefaultColor();
    }

    public void setBorderColor(@androidx.annotation.ColorRes int color) {
        setBorderColor(android.content.res.ColorStateList.valueOf(getResources().getColor(color)));
    }

    public android.content.res.ColorStateList getBorderColors() {
        return mBorderColor;
    }

    public void setBorderColor(android.content.res.ColorStateList colors) {
        if (mBorderColor.equals(colors)) {
            return;
        }

        mBorderColor = (colors != null) ? colors : android.content.res.ColorStateList
                .valueOf(RoundedDrawable.DEFAULT_BORDER_COLOR);
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        if (mBorderWidth > 0) {
            invalidate();
        }
    }

    public boolean isOval() {
        return mIsOval;
    }

    public void setOval(boolean oval) {
        mIsOval = oval;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public android.graphics.Shader.TileMode getTileModeX() {
        return mTileModeX;
    }

    public void setTileModeX(android.graphics.Shader.TileMode tileModeX) {
        if (this.mTileModeX == tileModeX) {
            return;
        }

        this.mTileModeX = tileModeX;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public android.graphics.Shader.TileMode getTileModeY() {
        return mTileModeY;
    }

    public void setTileModeY(android.graphics.Shader.TileMode tileModeY) {
        if (this.mTileModeY == tileModeY) {
            return;
        }

        this.mTileModeY = tileModeY;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public boolean mutatesBackground() {
        return mMutateBackground;
    }

    public void mutateBackground(boolean mutate) {
        if (mMutateBackground == mutate) {
            return;
        }

        mMutateBackground = mutate;
        updateBackgroundDrawableAttrs(true);
        invalidate();
    }
}
