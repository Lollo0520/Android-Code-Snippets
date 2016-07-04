package com.compscieddy.eddie_utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by elee on 2/22/16.
 */
public class Etils {

  public static void applyColorFilter(Drawable drawable, int color) {
    applyColorFilter(drawable, color, false);
  }

  public static void applyColorFilter(Drawable drawable, int color, boolean mutate) {
    drawable.clearColorFilter();
    PorterDuff.Mode mode = (color == Color.TRANSPARENT ? PorterDuff.Mode.SRC_ATOP : PorterDuff.Mode.SRC_IN);
    if (mutate) {
      drawable.mutate().setColorFilter(color, mode);
    } else {
      drawable.setColorFilter(color, mode);
    }
  }

  public static void showToast(Context context, String message) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
  }

  public static void logAndToast(Context context, String message, String TAG) {
    Log.e(TAG, message);
    showToast(context, message);
  }

  public static void logAndToast(Context context, String message) {
    Log.e("TAG", message);
    showToast(context, message);
  }

  /** Time-Related */
  public static String militaryTimeToAMPM(int militaryTime) {
    boolean isAM = (militaryTime / 12) < 1;
    String amPM = isAM ? "am" : "pm";
    int adjustedTime = militaryTime % 12;
    if (adjustedTime == 0) {
      if (militaryTime / 12 == 1) {
        adjustedTime = 12;
      } else if (militaryTime / 12 == 1) {
        adjustedTime = 24;
      }
    }
    return String.valueOf(adjustedTime) + amPM;
  }

  public static int dpToPx(int dp) {
    return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
  }

  public static int pxToDp(int px) {
    return (int) (px / Resources.getSystem().getDisplayMetrics().density);
  }

  /** Drawing and Path Stuff */

  /* If you're using this in an onDraw it may be a better idea to re-use the
      PathMeasure object (less memory allocations which = less GC operations)
      @return coordinates that is `fraction` distance into the path.
      @param fraction - percentage of length as a 0-1 float */
  public static float[] getDistanceInPathCoordinates(Path path, float fraction) {
    return getDistanceInPathCoordinates(path, fraction, false);
  }
  /** TODO: create alternate utility method that actually makes you pass in your own PathMeasure object */
  public static float[] getDistanceInPathCoordinates(Path path, float distance, boolean isActualDistanceNotFraction) {
    PathMeasure pathMeasure = new PathMeasure(path, false);
    float[] pos = new float[2];
    if (isActualDistanceNotFraction) {
      pathMeasure.getPosTan(distance, pos, null);
    } else {
      pathMeasure.getPosTan(pathMeasure.getLength() * distance, pos, null);
    }
    return pos;
  }
  //public static float[] getDistanceInPathCoordinates(PathMeasure, Path, distance)

  /** Java by default has mod do -30 % 100 = -30 instead of 70. */
  public static float betterMod(float number, float modNumber) {
    return (number % modNumber + modNumber) % modNumber;
  }

  public static void setPaddingLeft(View view, int padding) {
    view.setPadding(padding, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
  }
  public static void setPaddingTop(View view, int padding) {
    view.setPadding(view.getPaddingLeft(), padding, view.getPaddingRight(), view.getPaddingBottom());
  }
  public static void setPaddingRight(View view, int padding) {
    view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), padding, view.getPaddingBottom());
  }
  public static void setPaddingBottom(View view, int padding) {
    view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), padding);
  }
  public static void setPaddingLeftRelative(View view, int padding) {
    view.setPaddingRelative(padding, 0, 0, 0);
  }
  public static void setPaddingTopRelative(View view, int padding) {
    view.setPaddingRelative(0, padding, 0, 0);
  }
  public static void setPaddingRightRelative(View view, int padding) {
    view.setPaddingRelative(0, 0, padding, 0);
  }
  public static void setPaddingBottomRelative(View view, int padding) {
    view.setPaddingRelative(0, 0, 0, padding);
  }

  /**
   * Return the color that is in-between 'startColor' and 'endColor' at 'progressPercentage'
   * @param progressPercentage - 0.0 is beginning, 1.0 is end. Ex: 0.5 returns the color exactly
   *                           in-between 'startColor' and 'endColor'
   */
  public static int getIntermediateColor(int startColor, int endColor, float progressPercentage) {
        /* There is a more efficient way to write this with bit-shifting I think, let me (@elee) know
           if this should be rewritten like that */
    int startRed = Color.red(startColor);
    int endRed = Color.red(endColor);
    int newRed = startRed + Math.round(progressPercentage * (endRed - startRed));

    int startGreen = Color.green(startColor);
    int endGreen = Color.green(endColor);
    int newGreen = startGreen + Math.round(progressPercentage * (endGreen - startGreen));

    int startBlue = Color.blue(startColor);
    int endBlue = Color.blue(endColor);
    int newBlue = startBlue + Math.round(progressPercentage * (endBlue - startBlue));

        /* Safety checks, just in case - could be a for loop but I didn't want to deal with storing
           these in an array and implementing a way to log the correct variable name
         */
    newRed = Math.max(0, Math.min(newRed, 255));
    newGreen = Math.max(0, Math.min(newGreen, 255));
    newBlue = Math.max(0, Math.min(newBlue, 255));

    return Color.rgb(newRed, newGreen, newBlue);
  }

  public static int setAlpha(int color, float alphaPercentage) {
    return Color.argb(
        Math.round(Color.alpha(color) * alphaPercentage),
        Color.red(color),
        Color.green(color),
        Color.blue(color));
  }

  /**
   * Has to modify and not create a new RectF so that this method can be used in the onDraw(),
   * because new objects shouldn't be instantiated in onDraw().
   */
  public static void constrainRect(RectF rect, float amount) {
    rect.left += amount / 2;
    rect.top += amount / 2;
    rect.bottom -= amount / 2;
    rect.right -= amount / 2;
  }

  /**
   * This will give you an extended distance from one side of a line path. Extends from the first
   * coordinate that the path was started with (ie. the path.moveTo(x, y) coordinate), then extends past that bound.
   * @param path - needs to be a line path; a line equation
   */

  public static float[] extendedDistanceInPathCoordinates(Path path, float fraction) {
    return extendedDistanceInPathCoordinates(path, fraction, true);
  }

  public static float[] extendedDistanceInPathCoordinates(Path path, float fraction, boolean isFractionNotDistance) {
    float[] insetCoord = Etils.getDistanceInPathCoordinates(path, fraction, !isFractionNotDistance);
    float[] endCoord = Etils.getDistanceInPathCoordinates(path, 0, !isFractionNotDistance); // though with 0, this boolean flag isn't necessary
    final int x = 0, y = 1;

    float diffX = endCoord[x] - insetCoord[x];
    float diffY = endCoord[y] - insetCoord[y];
    return new float[] { endCoord[x] + diffX, endCoord[y] + diffY };
  }

  public static void showKeyboard(Context context) {
    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
  }

  public static void hideKeyboard(Context context, View view) {
    // Check if no view has focus:
    if (view != null) {
      InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  /** Encoding email for Firebase */

  public static String encodeEmail(String userEmail) {
    return userEmail.replace(".", ",");
  }

  public static boolean isEmailValid(String email) {
    return (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
  }

  /** http://stackoverflow.com/a/28307436/4326052
   *  Useful for RemoteViews where custom fonts can't be set
   */
  public static Bitmap createTextBitmap(String text, Typeface typeface, int textColor, float textSizePixels) {
    TextPaint textPaint = new TextPaint();
    textPaint.setTypeface(typeface);
    textPaint.setTextSize(textSizePixels);
    textPaint.setAntiAlias(true);
    textPaint.setSubpixelText(true);
    textPaint.setColor(textColor);
    textPaint.setTextAlign(Paint.Align.LEFT);
    float offsetFactor = 1.3f; // letters like "y" get cut off if there isn't an offset like this
    int textHeight = (int) (textSizePixels * offsetFactor);
    Bitmap bitmap = Bitmap.createBitmap((int) textPaint.measureText(text), textHeight, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    canvas.drawText(text, 0, bitmap.getHeight() / offsetFactor, textPaint);
    return bitmap;
  }

  /**
   * http://stackoverflow.com/a/11755265/4326052
   */
  public static float getScreenWidth(Context context) {
    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
    return dpWidth;
  }
  public static float getScreenHeight(Context context) {
    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
    return dpHeight;
  }

  public static float mapValue(float value, float min1, float max1, float min2, float max2) {
    float firstSpan = max1 - min1;
    float secondSpan = max2 - min2;
    float scaledValue = (value - min1) / firstSpan;
    scaledValue = Math.min(scaledValue, 1.0f);
    return min2 + (scaledValue * secondSpan);
  }

  public static void setMarginTop(View view, int margin) {
    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
    layoutParams.topMargin = margin;
  }
  public static void setMarginLeft(View view, int margin) {
    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
    layoutParams.leftMargin = margin;
  }
  public static void setMarginRight(View view, int margin) {
    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
    layoutParams.rightMargin = margin;
  }
  public static void setMarginBottom(View view, int margin) {
    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
    layoutParams.bottomMargin = margin;
  }

  public static boolean isTextViewEllipsized(TextView textView) {
    Layout layout = textView.getLayout();
    if (layout != null) {
      int lines = layout.getLineCount();
      if (lines > 0 && layout.getEllipsisCount(lines - 1) > 0) {
        return true;
      }
    }
    return false;
  }

  /**
    http://www.mkyong.com/java/java-generate-random-integers-in-a-range/
  */
  public static int getRandomNumberInRange(int min, int max) {
    if (min >= max) {
      throw new IllegalArgumentException("max must be greater than min");
    }
    Random r = new Random();
    return r.nextInt((max - min) + 1) + min;
  }

}
