package au.id.tedp.nertz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;

import android.util.Log;

class GameView extends View {
    public GameView(android.content.Context context) {
        super(context);
        cachedCanvasX = -1;
        cachedCanvasY = -1;
        cachedXSep = -1;
        cachedYSep = -1;
    }

    private int cachedCanvasX, cachedCanvasY;
    private int cachedXSep, cachedYSep;

    /**
     * Return a Rect for the destination card's location on the given Canvas,
     * x &amp; y position, and card dimensions.
     */
    private Rect cardPosition(Canvas canvas, int x, int y, int cardWidth, int cardHeight) {
        Rect bounds = canvas.getClipBounds();
        int xsep = -1, ysep = -1;

        if (cachedCanvasX == bounds.right && cachedCanvasY == bounds.bottom) {
            xsep = cachedXSep;
            ysep = cachedYSep;
        } else {
            ysep = (bounds.bottom - cardHeight * 2) / 3;
            xsep = (bounds.right - cardWidth * 6) / 7;
            cachedXSep = xsep;
            cachedYSep = ysep;
        }

        Rect position = new Rect();
        position.top = ysep + (ysep + cardHeight) * y;
        position.bottom = position.top + cardHeight;
        position.left = xsep + (xsep + cardWidth) * x;
        position.right = position.left + cardWidth;
        return position;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawARGB(255, 0, 128, 0);

        Resources res = getResources();

        BitmapDrawable blueBack = (BitmapDrawable) res.getDrawable(R.drawable.b1fv);
        Bitmap bmp = blueBack.getBitmap();

        int cardHeight, cardWidth;
        cardHeight = blueBack.getIntrinsicHeight();
        cardWidth = blueBack.getIntrinsicWidth();

        for (int x = 0; x < 6; ++x) {
            for (int y = 0; y < 2; ++y) {
                Rect dest = cardPosition(canvas, x, y, cardWidth, cardHeight);
                canvas.drawBitmap(bmp, null, dest, null);
            }
        }
    }
}
