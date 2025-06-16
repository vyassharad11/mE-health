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

public class RoundedDrawable extends android.graphics.drawable.Drawable {

    public static final int DEFAULT_BORDER_COLOR = android.graphics.Color.BLACK;

    public enum Corner {
        TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT
    }

    private final android.graphics.RectF mBounds = new android.graphics.RectF();
    private final android.graphics.RectF mDrawableRect = new android.graphics.RectF();
    private final android.graphics.RectF mBitmapRect = new android.graphics.RectF();
    private final android.graphics.Bitmap mBitmap;
    private final android.graphics.Paint mBitmapPaint;
    private final int mBitmapWidth;
    private final int mBitmapHeight;
    private final android.graphics.RectF mBorderRect = new android.graphics.RectF();
    private final android.graphics.Paint mBorderPaint;
    private final android.graphics.Matrix mShaderMatrix = new android.graphics.Matrix();
    private final android.graphics.RectF mSquareCornersRect = new android.graphics.RectF();

    private android.graphics.Shader.TileMode mTileModeX = android.graphics.Shader.TileMode.CLAMP;
    private android.graphics.Shader.TileMode mTileModeY = android.graphics.Shader.TileMode.CLAMP;
    private boolean mRebuildShader = true;

    // [ topLeft, topRight, bottomLeft, bottomRight ]
    private float mCornerRadius = 0f;
    private final boolean[] mCornersRounded = new boolean[]{true, true, true,
            true};

    private boolean mOval = false;
    private float mBorderWidth = 0;
    private android.content.res.ColorStateList mBorderColor = android.content.res.ColorStateList
            .valueOf(DEFAULT_BORDER_COLOR);
    private android.widget.ImageView.ScaleType mScaleType = android.widget.ImageView.ScaleType.FIT_CENTER;

    private RoundedDrawable(android.graphics.Bitmap bitmap) {
        mBitmap = bitmap;
        mBitmapWidth = bitmap.getWidth();
        mBitmapHeight = bitmap.getHeight();
        mBitmapRect.set(0, 0, mBitmapWidth, mBitmapHeight);

        mBitmapPaint = new android.graphics.Paint();
        mBitmapPaint.setStyle(android.graphics.Paint.Style.FILL);
        mBitmapPaint.setAntiAlias(true);

        mBorderPaint = new android.graphics.Paint();
        mBorderPaint.setStyle(android.graphics.Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor.getColorForState(getState(), DEFAULT_BORDER_COLOR));
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }

    public static RoundedDrawable fromBitmap(android.graphics.Bitmap bitmap) {
        if (bitmap != null) {
            return new RoundedDrawable(bitmap);
        } else {
            return null;
        }
    }

    public static android.graphics.drawable.Drawable fromDrawable(android.graphics.drawable.Drawable drawable) {
        if (drawable != null) {
            if (drawable instanceof RoundedDrawable) {
                // just return if it's already a RoundedDrawable
                return drawable;
            } else if (drawable instanceof android.graphics.drawable.LayerDrawable) {
                android.graphics.drawable.LayerDrawable ld = (android.graphics.drawable.LayerDrawable) drawable;
                int num = ld.getNumberOfLayers();

                // loop through layers to and change to RoundedDrawables if
                // possible
                for (int i = 0; i < num; i++) {
                    android.graphics.drawable.Drawable d = ld.getDrawable(i);
                    ld.setDrawableByLayerId(ld.getId(i), fromDrawable(d));
                }
                return ld;
            }

            // try to get a bitmap from the drawable and
            android.graphics.Bitmap bm = drawableToBitmap(drawable);
            if (bm != null) {
                return new RoundedDrawable(bm);
            }
        }
        return drawable;
    }

    private static android.graphics.Bitmap drawableToBitmap(android.graphics.drawable.Drawable drawable) {
        if (drawable instanceof android.graphics.drawable.BitmapDrawable) {
            return ((android.graphics.drawable.BitmapDrawable) drawable).getBitmap();
        }

        android.graphics.Bitmap bitmap;
        int width = Math.max(drawable.getIntrinsicWidth(), 2);
        int height = Math.max(drawable.getIntrinsicHeight(), 2);
        try {
            bitmap = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888);
            android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
            bitmap = null;
        }

        return bitmap;
    }

    public android.graphics.Bitmap getSourceBitmap() {
        return mBitmap;
    }

    @Override
    public boolean isStateful() {
        return mBorderColor.isStateful();
    }

    @Override
    protected boolean onStateChange(int[] state) {
        int newColor = mBorderColor.getColorForState(state, 0);
        if (mBorderPaint.getColor() != newColor) {
            mBorderPaint.setColor(newColor);
            return true;
        } else {
            return super.onStateChange(state);
        }
    }

    private void updateShaderMatrix() {
        float scale;
        float dx;
        float dy;

        switch (mScaleType) {
            case CENTER:
                mBorderRect.set(mBounds);
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);

                mShaderMatrix.reset();
                mShaderMatrix
                        .setTranslate(
                                (int) ((mBorderRect.width() - mBitmapWidth) * 0.5f + 0.5f),
                                (int) ((mBorderRect.height() - mBitmapHeight) * 0.5f + 0.5f));
                break;

            case CENTER_CROP:
                mBorderRect.set(mBounds);
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);

                mShaderMatrix.reset();

                dx = 0;
                dy = 0;

                if (mBitmapWidth * mBorderRect.height() > mBorderRect.width()
                        * mBitmapHeight) {
                    scale = mBorderRect.height() / (float) mBitmapHeight;
                    dx = (mBorderRect.width() - mBitmapWidth * scale) * 0.5f;
                } else {
                    scale = mBorderRect.width() / (float) mBitmapWidth;
                    dy = (mBorderRect.height() - mBitmapHeight * scale) * 0.5f;
                }

                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth,
                        (int) (dy + 0.5f) + mBorderWidth);
                break;

            case CENTER_INSIDE:
                mShaderMatrix.reset();

                if (mBitmapWidth <= mBounds.width()
                        && mBitmapHeight <= mBounds.height()) {
                    scale = 1.0f;
                } else {
                    scale = Math.min(mBounds.width() / (float) mBitmapWidth,
                            mBounds.height() / (float) mBitmapHeight);
                }

                dx = (int) ((mBounds.width() - mBitmapWidth * scale) * 0.5f + 0.5f);
                dy = (int) ((mBounds.height() - mBitmapHeight * scale) * 0.5f + 0.5f);

                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate(dx, dy);

                mBorderRect.set(mBitmapRect);
                mShaderMatrix.mapRect(mBorderRect);
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect,
                        android.graphics.Matrix.ScaleToFit.FILL);
                break;

            default:
            case FIT_CENTER:
                mBorderRect.set(mBitmapRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds,
                        android.graphics.Matrix.ScaleToFit.CENTER);
                mShaderMatrix.mapRect(mBorderRect);
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect,
                        android.graphics.Matrix.ScaleToFit.FILL);
                break;

            case FIT_END:
                mBorderRect.set(mBitmapRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds,
                        android.graphics.Matrix.ScaleToFit.END);
                mShaderMatrix.mapRect(mBorderRect);
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect,
                        android.graphics.Matrix.ScaleToFit.FILL);
                break;

            case FIT_START:
                mBorderRect.set(mBitmapRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds,
                        android.graphics.Matrix.ScaleToFit.START);
                mShaderMatrix.mapRect(mBorderRect);
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect,
                        android.graphics.Matrix.ScaleToFit.FILL);
                break;

            case FIT_XY:
                mBorderRect.set(mBounds);
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                mShaderMatrix.reset();
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect,
                        android.graphics.Matrix.ScaleToFit.FILL);
                break;
        }

        mDrawableRect.set(mBorderRect);
    }

    @Override
    protected void onBoundsChange(@androidx.annotation.NonNull android.graphics.Rect bounds) {
        super.onBoundsChange(bounds);

        mBounds.set(bounds);

        updateShaderMatrix();
    }

    @Override
    public void draw(@androidx.annotation.NonNull android.graphics.Canvas canvas) {
        if (mRebuildShader) {
            android.graphics.BitmapShader bitmapShader = new android.graphics.BitmapShader(mBitmap, mTileModeX,
                    mTileModeY);
            if (mTileModeX == android.graphics.Shader.TileMode.CLAMP
                    && mTileModeY == android.graphics.Shader.TileMode.CLAMP) {
                bitmapShader.setLocalMatrix(mShaderMatrix);
            }
            mBitmapPaint.setShader(bitmapShader);
            mRebuildShader = false;
        }

        if (mOval) {
            if (mBorderWidth > 0) {
                canvas.drawOval(mDrawableRect, mBitmapPaint);
                canvas.drawOval(mBorderRect, mBorderPaint);
            } else {
                canvas.drawOval(mDrawableRect, mBitmapPaint);
            }
        } else {
            if (any(mCornersRounded)) {
                float radius = mCornerRadius;
                if (mBorderWidth > 0) {
                    canvas.drawRoundRect(mDrawableRect, radius, radius,
                            mBitmapPaint);
                    canvas.drawRoundRect(mBorderRect, radius, radius,
                            mBorderPaint);
                    redrawBitmapForSquareCorners(canvas);
                    redrawBorderForSquareCorners(canvas);
                } else {
                    canvas.drawRoundRect(mDrawableRect, radius, radius,
                            mBitmapPaint);
                    redrawBitmapForSquareCorners(canvas);
                }
            } else {
                canvas.drawRect(mDrawableRect, mBitmapPaint);
                canvas.drawRect(mBorderRect, mBorderPaint);
            }
        }
    }

    private void redrawBitmapForSquareCorners(android.graphics.Canvas canvas) {
        if (all(mCornersRounded)) {
            // no square corners
            return;
        }

        if (mCornerRadius == 0) {
            return; // no round corners
        }

        float left = mDrawableRect.left;
        float top = mDrawableRect.top;
        float right = left + mDrawableRect.width();
        float bottom = top + mDrawableRect.height();
        float radius = mCornerRadius;

        if (!mCornersRounded[Corner.TOP_LEFT.ordinal()]) {
            mSquareCornersRect.set(left, top, left + radius, top + radius);
            canvas.drawRect(mSquareCornersRect, mBitmapPaint);
        }

        if (!mCornersRounded[Corner.TOP_RIGHT.ordinal()]) {
            mSquareCornersRect.set(right - radius, top, right, radius);
            canvas.drawRect(mSquareCornersRect, mBitmapPaint);
        }

        if (!mCornersRounded[Corner.BOTTOM_RIGHT.ordinal()]) {
            mSquareCornersRect.set(right - radius, bottom - radius, right,
                    bottom);
            canvas.drawRect(mSquareCornersRect, mBitmapPaint);
        }

        if (!mCornersRounded[Corner.BOTTOM_LEFT.ordinal()]) {
            mSquareCornersRect
                    .set(left, bottom - radius, left + radius, bottom);
            canvas.drawRect(mSquareCornersRect, mBitmapPaint);
        }
    }

    private void redrawBorderForSquareCorners(android.graphics.Canvas canvas) {
        if (all(mCornersRounded)) {
            // no square corners
            return;
        }

        if (mCornerRadius == 0) {
            return; // no round corners
        }

        float left = mDrawableRect.left;
        float top = mDrawableRect.top;
        float right = left + mDrawableRect.width();
        float bottom = top + mDrawableRect.height();
        float radius = mCornerRadius;
        float offset = mBorderWidth / 2;

        if (!mCornersRounded[Corner.TOP_LEFT.ordinal()]) {
            canvas.drawLine(left - offset, top, left + radius, top,
                    mBorderPaint);
            canvas.drawLine(left, top - offset, left, top + radius,
                    mBorderPaint);
        }

        if (!mCornersRounded[Corner.TOP_RIGHT.ordinal()]) {
            canvas.drawLine(right - radius - offset, top, right, top,
                    mBorderPaint);
            canvas.drawLine(right, top - offset, right, top + radius,
                    mBorderPaint);
        }

        if (!mCornersRounded[Corner.BOTTOM_RIGHT.ordinal()]) {
            canvas.drawLine(right - radius - offset, bottom, right + offset,
                    bottom, mBorderPaint);
            canvas.drawLine(right, bottom - radius, right, bottom, mBorderPaint);
        }

        if (!mCornersRounded[Corner.BOTTOM_LEFT.ordinal()]) {
            canvas.drawLine(left - offset, bottom, left + radius, bottom,
                    mBorderPaint);
            canvas.drawLine(left, bottom - radius, left, bottom, mBorderPaint);
        }
    }

    @Override
    public int getOpacity() {
        return android.graphics.PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getAlpha() {
        return mBitmapPaint.getAlpha();
    }

    @Override
    public void setAlpha(int alpha) {
        mBitmapPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public android.graphics.ColorFilter getColorFilter() {
        return mBitmapPaint.getColorFilter();
    }

    @Override
    public void setColorFilter(android.graphics.ColorFilter cf) {
        mBitmapPaint.setColorFilter(cf);
        invalidateSelf();
    }

    @Override
    public void setDither(boolean dither) {
        mBitmapPaint.setDither(dither);
        invalidateSelf();
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        mBitmapPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmapWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmapHeight;
    }

    /**
     * @return the corner radius.
     */
    public float getCornerRadius() {
        return mCornerRadius;
    }

    /**
     * @param corner the specific corner to get radius of.
     * @return the corner radius of the specified {@link Corner}.
     */
    public float getCornerRadius(Corner corner) {
        return mCornersRounded[corner.ordinal()] ? mCornerRadius : 0f;
    }

    /**
     * Sets all corners to the specified radius.
     *
     * @param radius the radius.
     * @return the {@link RoundedDrawable} for chaining.
     */
    public RoundedDrawable setCornerRadius(float radius) {
        setCornerRadius(radius, radius, radius, radius);
        return this;
    }

    /**
     * Sets the corner radius of one specific corner.
     *
     * @param corner the corner.
     * @param radius the radius.
     * @return the {@link RoundedDrawable} for chaining.
     */
    public RoundedDrawable setCornerRadius(Corner corner, float radius) {
        if (radius != 0 && mCornerRadius != 0 && mCornerRadius != radius) {
            throw new IllegalArgumentException(
                    "Multiple nonzero corner radii not yet supported.");
        }

        if (radius == 0) {
            if (only(corner.ordinal(), mCornersRounded)) {
                mCornerRadius = 0;
            }
            mCornersRounded[corner.ordinal()] = false;
        } else {
            if (mCornerRadius == 0) {
                mCornerRadius = radius;
            }
            mCornersRounded[corner.ordinal()] = true;
        }

        return this;
    }

    /**
     * Sets the corner radii of all the corners.
     *
     * @param topLeft     top left corner radius.
     * @param topRight    top right corner radius
     * @param bottomRight bototm right corner radius.
     * @param bottomLeft  bottom left corner radius.
     * @return the {@link RoundedDrawable} for chaining.
     */
    public RoundedDrawable setCornerRadius(float topLeft, float topRight,
                                           float bottomRight, float bottomLeft) {
        java.util.Set<Float> radiusSet = new java.util.HashSet<>(4);
        radiusSet.add(topLeft);
        radiusSet.add(topRight);
        radiusSet.add(bottomRight);
        radiusSet.add(bottomLeft);

        radiusSet.remove(0f);

        if (radiusSet.size() > 1) {
            throw new IllegalArgumentException(
                    "Multiple nonzero corner radii not yet supported.");
        }

        if (!radiusSet.isEmpty()) {
            float radius = radiusSet.iterator().next();
            if (Float.isInfinite(radius) || Float.isNaN(radius) || radius < 0) {
                throw new IllegalArgumentException("Invalid radius value: "
                        + radius);
            }
            mCornerRadius = radius;
        } else {
            mCornerRadius = 0f;
        }

        mCornersRounded[Corner.TOP_LEFT.ordinal()] = topLeft > 0;
        mCornersRounded[Corner.TOP_RIGHT.ordinal()] = topRight > 0;
        mCornersRounded[Corner.BOTTOM_RIGHT.ordinal()] = bottomRight > 0;
        mCornersRounded[Corner.BOTTOM_LEFT.ordinal()] = bottomLeft > 0;
        return this;
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    public RoundedDrawable setBorderWidth(float width) {
        mBorderWidth = width;
        mBorderPaint.setStrokeWidth(mBorderWidth);
        return this;
    }

    public int getBorderColor() {
        return mBorderColor.getDefaultColor();
    }

    public RoundedDrawable setBorderColor(int color) {
        return setBorderColor(android.content.res.ColorStateList.valueOf(color));
    }

    public android.content.res.ColorStateList getBorderColors() {
        return mBorderColor;
    }

    public RoundedDrawable setBorderColor(android.content.res.ColorStateList colors) {
        mBorderColor = colors != null ? colors : android.content.res.ColorStateList.valueOf(0);
        mBorderPaint.setColor(mBorderColor.getColorForState(getState(),
                DEFAULT_BORDER_COLOR));
        return this;
    }

    public boolean isOval() {
        return mOval;
    }

    public RoundedDrawable setOval(boolean oval) {
        mOval = oval;
        return this;
    }

    public android.widget.ImageView.ScaleType getScaleType() {
        return mScaleType;
    }

    public RoundedDrawable setScaleType(android.widget.ImageView.ScaleType scaleType) {
        if (scaleType == null) {
            scaleType = android.widget.ImageView.ScaleType.FIT_CENTER;
        }
        if (mScaleType != scaleType) {
            mScaleType = scaleType;
            updateShaderMatrix();
        }
        return this;
    }

    public android.graphics.Shader.TileMode getTileModeX() {
        return mTileModeX;
    }

    public RoundedDrawable setTileModeX(android.graphics.Shader.TileMode tileModeX) {
        if (mTileModeX != tileModeX) {
            mTileModeX = tileModeX;
            mRebuildShader = true;
            invalidateSelf();
        }
        return this;
    }

    public android.graphics.Shader.TileMode getTileModeY() {
        return mTileModeY;
    }

    public RoundedDrawable setTileModeY(android.graphics.Shader.TileMode tileModeY) {
        if (mTileModeY != tileModeY) {
            mTileModeY = tileModeY;
            mRebuildShader = true;
            invalidateSelf();
        }
        return this;
    }

    private static boolean only(int index, boolean[] booleans) {
        for (int i = 0, len = booleans.length; i < len; i++) {
            if (booleans[i] != (i == index)) {
                return false;
            }
        }
        return true;
    }

    private static boolean any(boolean[] booleans) {
        for (boolean b : booleans) {
            if (b) {
                return true;
            }
        }
        return false;
    }

    private static boolean all(boolean[] booleans) {
        for (boolean b : booleans) {
            if (b) {
                return false;
            }
        }
        return true;
    }

    public android.graphics.Bitmap toBitmap() {
        return drawableToBitmap(this);
    }
}
