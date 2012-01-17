package au.id.tedp.nertz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;

import android.util.Log;

class GameView extends View {
    public GameView(android.content.Context context, Player player) {
        super(context);
        cachedCanvasX = -1;
        cachedCanvasY = -1;
        cachedXSep = -1;
        cachedYSep = -1;
        this.player = player;
        this.res = getResources();
    }

    private int cachedCanvasX, cachedCanvasY;
    private int cachedXSep, cachedYSep;

    private Player player;

    private Resources res;

    private int cardHeight, cardWidth;

    /**
     * Return a Rect for the destination card's location on the given Canvas,
     * x &amp; y position, and card dimensions.
     */
    private Rect cardPosition(Canvas canvas, int x, int y) {
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

    protected void drawStream(Canvas canvas, Stream stream) {
        int areaWidth = streamArea.right - streamArea.left;
        int areaHeight = streamArea.bottom - streamArea.top;

        if (!stream.isFaceDownEmpty()) {
            BitmapDrawable cardBack = DeckGraphics.getCardBack(res);
            Rect dest = new Rect();
            dest.left = streamArea.left + (areaWidth - cardWidth * 2) / 3;
            dest.top = streamArea.top + (areaHeight - cardHeight) / 2;
            dest.right = dest.left + cardWidth;
            dest.bottom = dest.top + cardHeight;
            canvas.drawBitmap(cardBack.getBitmap(), null, dest, null);
        }

        if (!stream.isFaceUpEmpty()) {
            BitmapDrawable drawable = stream.topCardImage(res);
            Rect dest = new Rect();
            dest.left = streamArea.left + (areaWidth - cardWidth * 2) * 2 / 3 + cardWidth;
            dest.top = streamArea.top + (areaHeight - cardHeight) / 2;
            dest.right = dest.left + cardWidth;
            dest.bottom = dest.top + cardHeight;
            canvas.drawBitmap(drawable.getBitmap(), null, dest, null);
        }
    }

    protected void drawNertzPile(Canvas canvas) {
        NertzPile nertzPile = player.getNertzPile();
        if (nertzPile.isEmpty())
            return;

        BitmapDrawable drawable = nertzPile.topCardImage(res);
        int centerX = nertzPileArea.centerX();
        int centerY = nertzPileArea.centerY();
        Rect dest = new Rect(
                centerX - cardWidth / 2,
                centerY - cardHeight / 2,
                centerX + cardWidth / 2,
                centerY + cardHeight / 2);
        canvas.drawBitmap(drawable.getBitmap(), null, dest, null);
    }

    protected void drawRiverPile(Canvas canvas, TableauPile pile, int pilenum) {
        int top = riverArea.top + (riverArea.bottom - riverArea.top) / 15;
        int sep = ((riverArea.right - riverArea.left) - cardWidth * 4) / 5;
        Rect dest = new Rect();
        dest.left = riverArea.left + (1 + pilenum) * sep + pilenum * cardWidth;
        dest.top = top;
        dest.right = dest.left + cardWidth;
        dest.bottom = dest.top + cardHeight;
        BitmapDrawable drawable = pile.topCardImage(res);
        canvas.drawBitmap(drawable.getBitmap(), null, dest, null);
    }

    protected void drawRiver(Canvas canvas) {
        ArrayList<TableauPile> river = player.getRiver();

        int pilenum = 0;
        for (TableauPile pile : river) {
            if (!pile.isEmpty())
                drawRiverPile(canvas, pile, pilenum);
            ++pilenum;
        }
    }

    protected void drawLakePile(Canvas canvas, Pile pile, int pilenum) {
        BitmapDrawable bmp = pile.topCardImage(res);
        int top = lakeArea.centerY() - cardHeight / 2;
        int bottom = lakeArea.centerY() + cardHeight / 2;
        int sep = ((lakeArea.right - lakeArea.left) - cardWidth * 8) / 10;
        Rect dest = new Rect(0, top, 0, bottom);
        dest.left = lakeArea.left + cardWidth * pilenum + sep * pilenum;
        dest.left = dest.right + cardWidth;
        canvas.drawBitmap(bmp.getBitmap(), null, dest, null);
    }

    protected void drawLake(Canvas canvas) {
        Lake lake = player.getLake();

        int pilenum = 0;
        for (Pile pile : lake.getPiles()) {
            drawLakePile(canvas, pile, pilenum);
            ++pilenum;
        }
    }

    protected Rect nertzPileArea, riverArea, streamArea, lakeArea, oppArea;

    private void calculateAreas() {
        int height = getHeight(), width = getWidth();
        int hs = height / 2;
        // XXX: Might want to cache these.
        nertzPileArea = new Rect(0, hs, width / 6, height);
        riverArea = new Rect(nertzPileArea.right, hs, width / 4 * 3, height);
        streamArea = new Rect(riverArea.right, hs, width, height);
        lakeArea = new Rect(0, height / 6, width, hs);
        oppArea = new Rect(0, 0, width, lakeArea.top);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calculateAreas();

        canvas.drawColor(0xff669900);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xff99cc00);

        canvas.drawRect(nertzPileArea, paint);
        canvas.drawRect(riverArea, paint);
        canvas.drawRect(streamArea, paint);
        canvas.drawRect(lakeArea, paint);
        canvas.drawRect(oppArea, paint);

        cardWidth = getWidth() / 11;
        cardHeight = getHeight() / 4;

        drawNertzPile(canvas);
        drawRiver(canvas);
        drawStream(canvas, player.getStream());
        drawLake(canvas);
    }
}
