package au.id.tedp.nertz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Iterator;

import android.util.Log;

class GameView extends View implements View.OnTouchListener {
    public GameView(android.content.Context context, HumanPlayer player, Game game) {
        super(context);
        cachedCanvasX = -1;
        cachedCanvasY = -1;
        cachedXSep = -1;
        cachedYSep = -1;
        this.player = player;
        this.game = game;
        this.res = getResources();
        setOnTouchListener(this);
        state = TouchState.NONE;
        liveCards = new ArrayList<Card>(12);
    }

    private int cachedCanvasX, cachedCanvasY;
    private int cachedXSep, cachedYSep;

    private HumanPlayer player;
    private Game game;

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

        Rect dest = new Rect(0, streamArea.centerY() - cardHeight / 2, 0,
                streamArea.centerY() + cardHeight / 2);

        if (!stream.isFaceDownEmpty()) {
            BitmapDrawable cardBack = DeckGraphics.getCardBack(res);
            dest.left = streamArea.left + (areaWidth - cardWidth * 2) * 2 / 3 + cardWidth;
            dest.right = dest.left + cardWidth;
            canvas.drawBitmap(cardBack.getBitmap(), null, dest, null);
        }

        if (!stream.isFaceUpEmpty()) {
            BitmapDrawable drawable = stream.topCardImage(res);
            dest.left = streamArea.left + (areaWidth - cardWidth * 2) / 3;
            dest.right = dest.left + cardWidth;
            canvas.drawBitmap(drawable.getBitmap(), null, dest, null);
        }
    }

    protected void drawNertzPileCount(NertzPile pile, Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
        paint.setTextSize(28.0f);

        canvas.drawText(String.valueOf(pile.size()),
                nertzPileArea.centerX(),
                nertzPileArea.centerY() + cardHeight / 2 +
                (nertzPileArea.bottom - nertzPileArea.top - cardHeight) / 4,
                paint);
    }

    protected void drawNertzPile(Canvas canvas) {
        NertzPile nertzPile = player.getNertzPile();

        drawNertzPileCount(nertzPile, canvas);

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

    private int getRiverPileLeft(int pilenum) {
        int hsep = ((riverArea.right - riverArea.left) - cardWidth * 4) / 5;
        return riverArea.left + (1 + pilenum) * hsep + pilenum * cardWidth;
    }

    private int getRiverPileLeft(TableauPile pile) {
        int pilenum = 0;
        boolean foundPile = false;

        for (TableauPile rp: player.getRiver().getPiles()) {
            if (rp == pile) {
                foundPile = true;
                break;
            }
            ++pilenum;
        }

        if (!foundPile) {
            // FIXME: Throw exception
            return 0;
        }

        return getRiverPileLeft(pilenum);
    }

    protected void drawRiverPile(Canvas canvas, TableauPile pile, int pilenum) {
        int top_gap = (riverArea.bottom - riverArea.top) / 15;
        int top = riverArea.top + top_gap;
        int voffset = cardHeight / 4;
        Stack<Card> faceup = pile.getFaceUpCards();
        // Figure out the max vsep allowed by the number of cards
        int max_voffset = (riverArea.bottom - riverArea.top - top_gap * 2) / faceup.size();
        if (voffset > max_voffset)
            voffset = max_voffset;
        Rect dest = new Rect();
        dest.left = getRiverPileLeft(pilenum);
        dest.top = top;
        dest.right = dest.left + cardWidth;
        dest.bottom = dest.top + cardHeight;
        for (Card card: faceup) {
            BitmapDrawable bd = DeckGraphics.getBitmapDrawable(res, card);
            canvas.drawBitmap(bd.getBitmap(), null, dest, null);
            dest.top += voffset;
            dest.bottom += voffset;
        }
    }

    protected void drawRiver(Canvas canvas, TableauPile skipPile) {
        River river = player.getRiver();

        int pilenum = 0;
        for (TableauPile pile : river.getPiles()) {
            if (!pile.isEmpty() && pile != skipPile)
                drawRiverPile(canvas, pile, pilenum);
            ++pilenum;
        }
    }

    public final int LAKE_PILES = 8;

    protected void drawLakePile(Canvas canvas, Pile pile, int pilenum) {
        Card topCard = pile.peek();
        BitmapDrawable bmp;
        int pile_spots = Math.max(LAKE_PILES, player.getLake().size());
        if (topCard.getFace() == Card.Face.KING)
            bmp = DeckGraphics.getCardBack(res);
        else
            bmp = DeckGraphics.getBitmapDrawable(res, topCard);

        Rect dest = new Rect();

        if (getWidth() < getHeight()) {
            // Portrait layout
            int columns = Math.round((float)pile_spots / 2.0f);
            int column = pilenum % columns;
            int row = pilenum / columns;
            int vsep = (lakeArea.bottom - lakeArea.top - cardHeight * 2) / 3;
            int hsep = ((lakeArea.right - lakeArea.left) - cardWidth * columns)
                    / (columns + 1);

            // Don't draw the leftmost card off the left of the screen
            if (row == 0 && hsep < 0)
                hsep = 0;

            dest.left = lakeArea.left + hsep / 2 + hsep * column + column * cardWidth;
            dest.top = lakeArea.top + vsep * (row + 1) + row * cardHeight;
        } else {
            // Landscape layout
            int sep = ((lakeArea.right - lakeArea.left) - cardWidth * pile_spots) /
                (pile_spots + 1);
            if (pilenum == 0 && sep < 0)
                sep = 0;
            dest.left = lakeArea.left + cardWidth * pilenum + sep * (pilenum + 1);
            dest.top = lakeArea.centerY() - cardHeight / 2;
        }

        dest.right = dest.left + cardWidth;
        dest.bottom = dest.top + cardHeight;
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
        // XXX: Might want to cache these.

        if (width < height) {
            // Portrait
            nertzPileArea = new Rect(0, height / 4 * 3, width / 2, height);
            streamArea = new Rect(width / 2, height / 4 * 3, width, height);
            riverArea = new Rect(0, height / 2, width, height / 4 * 3);
            lakeArea = new Rect(0, height / 6, width, height / 2);
            oppArea = new Rect(0, 0, width, lakeArea.top);
        } else {
            // Landscape
            int hs = height / 2;
            nertzPileArea = new Rect(0, hs, width / 6, height);
            riverArea = new Rect(nertzPileArea.right, hs, width / 4 * 3, height);
            streamArea = new Rect(riverArea.right, hs, width, height);
            lakeArea = new Rect(0, height / 6, width, hs);
            oppArea = new Rect(0, 0, width, lakeArea.top);
        }
    }

    private void drawLiveCards(Canvas canvas) {
        if (liveCards.isEmpty())
            return;

        int offset = cardHeight / 4;

        for (int i = 0; i < liveCards.size(); ++i) {
            Card card = liveCards.get(i);
            BitmapDrawable bd = DeckGraphics.getBitmapDrawable(res, card);
            RectF dest = new RectF(liveCardX - cardWidth / 2,
                    liveCardY - cardHeight / 8 * 7 + (offset * i),
                    liveCardX + cardWidth / 2,
                    liveCardY + cardHeight / 8 + (offset * i));
            canvas.drawBitmap(bd.getBitmap(), null, dest, null);
        }
    }

    private void drawExpandedPile(Canvas canvas, TableauPile pile) {
        int ypad, vsep;
        int numcards = pile.getFaceUpCards().size();

        if (numcards * cardHeight < getHeight()) {
            // Not enough cards to fill the vertical space without gaps.
            // Put them edge-to-edge along the center of the screen.
            ypad = (getHeight() - numcards * cardHeight) / 2;
            vsep = cardHeight;
        } else {
            // Overlap cards.
            ypad = cardHeight / 4;
            vsep = (getHeight() - ypad * 2) / numcards;
        }

        Rect dest = new Rect(getRiverPileLeft(pile), 0, 0, 0);
        dest.right = dest.left + cardWidth;

        int cardnum = 0;
        for (Card card: pile.getFaceUpCards()) {
            dest.top = ypad + vsep * cardnum;
            dest.bottom = dest.top + cardHeight;
            BitmapDrawable bd = DeckGraphics.getBitmapDrawable(res, card);
            canvas.drawBitmap(bd.getBitmap(), null, dest, null);
            ++cardnum;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (staticTableBitmap == null || expandedPile != null) {
            staticTableBitmap = Bitmap.createBitmap(getWidth(),
                    getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(staticTableBitmap);
            calculateAreas();

            c.drawColor(0xff669900);

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(0xff99cc00);

            c.drawRect(nertzPileArea, paint);
            c.drawRect(riverArea, paint);
            c.drawRect(streamArea, paint);
            c.drawRect(lakeArea, paint);
            c.drawRect(oppArea, paint);

            /*
            cardWidth = getWidth() / 11;
            cardHeight = getHeight() / 4;
            */

            Bitmap cardBmp = DeckGraphics.getCardBack(res).getBitmap();
            cardWidth = cardBmp.getWidth();
            cardHeight = cardBmp.getHeight();

            drawNertzPile(c);
            drawRiver(c, expandedPile);
            drawStream(c, player.getStream());
            drawLake(c);
        }
        Rect fullDest = new Rect(0, 0, getWidth(), getHeight());
        canvas.drawBitmap(staticTableBitmap, null, fullDest, null);
        if (expandedPile != null) {
            canvas.drawARGB(128, 0, 0, 0);
            drawExpandedPile(canvas, expandedPile);
        } else {
            drawLiveCards(canvas);
        }
    }

    protected enum TouchState {
        NONE,
        DRAG,
        DROP
    }

    protected enum Area {
        VOID,
        LAKE,
        RIVER,
        STREAM,
        NERTZ_PILE
    }

    private TouchState state;

    private Area detectArea(MotionEvent ev) {
        int x, y;

        x = (int) ev.getX();
        y = (int) ev.getY();

        if (streamArea.contains(x, y)) {
            return Area.STREAM;
        } else if (nertzPileArea.contains(x, y)) {
            return Area.NERTZ_PILE;
        } else if (riverArea.contains(x, y)) {
            return Area.RIVER;
        } else if (lakeArea.contains(x, y)) {
            return Area.LAKE;
        }
        return Area.VOID;
    }

    // FIXME: liveCards needs to be its own class.
    private ArrayList<Card> liveCards;
    private boolean liveCardsFromExpandedPile;
    private Pile fromPile;
    private float liveCardX, liveCardY;
    //! The touch event began on the stream pile
    private boolean streamPileTouched;
    private Bitmap staticTableBitmap;
    private TableauPile expandedPile;

    private TableauPile getRiverPile(float x, float y) {
        River river = player.getRiver();
        float cardAreaWidth = riverArea.right - riverArea.left;
        int pileNum = (int) ((x - (float) riverArea.left) / (cardAreaWidth) * (float) river.size());
        Log.d("Nertz", "getRiverPile returning pile " + pileNum);
        return river.get(pileNum);
    }

    private void returnLiveCards() {
        if (liveCards.isEmpty() || fromPile == null)
            return;

        liveCardsFromExpandedPile = false;

        try {
            for (Card card: liveCards)
                fromPile.push(card);
            liveCards.clear();
        } catch (CardSequenceException e) {
            Log.e("Nertz", "Cannot put card " + liveCards.toString() + " back on original pile!");
        }
    }

    private void handleStreamTouch(float x, float y) {
        Stream stream = player.getStream();

        if (x < (float)streamArea.centerX()) {
            // Waste pile
            if (!stream.isFaceUpEmpty())
                fromPile = stream;
        } else {
            // Stream pile (face down)
            streamPileTouched = true;
        }
    }

    private void handleStreamUp(float x, float y) {
        if (streamPileTouched &&
                x > (float)streamArea.centerX())
        {
            // Stream pile (face down)
            Stream stream = player.getStream();
            try {
                if (!stream.isFaceDownEmpty()) {
                    stream.flipThree();
                    game.onPlayerMove();
                } else {
                    stream.restartPile();
                }
            }
            catch (EmptyPileException e) {
                Log.e("Nertz", "Tried to handle stream touch, but stream was empty");
            }
        }
    }

    private Pile getLakePile(float x, float y) {
        Lake lake = player.getLake();
        int width = lakeArea.right - lakeArea.left;
        x -= (float) lakeArea.left;
        int pileNum = (int) (x / (float) width * (float) LAKE_PILES);
        return lake.getPiles().get(pileNum);
    }

    // XXX: This duplicates code from another implementation of getRiverPile().
    private TableauPile getRiverPile(float x, float y, Card card) {
        River river = player.getRiver();
        int num = (int) ((x - ((float) riverArea.left)) / (float) (riverArea.right - riverArea.left) * river.size());
        TableauPile target = river.get(num);
        if (target.isValidMove(card))
            return target;
        return null;
    }

    // XXX: Provide getRiverPile(Card c) to allow flicking to the river?

    private void handleExpandedTouch(TableauPile pile, float x, float y) {
        int riverPileLeft = getRiverPileLeft(pile);
        if (x < (float)riverPileLeft || x > (float)(riverPileLeft + cardWidth)) {
            returnLiveCards();
            return; // Outside the expanded pile area.
        }

        fromPile = pile;

        Stack<Card> cards = pile.getFaceUpCards();
        int cardnum = (int) ((y / (float) getHeight()) * cards.size());

        Log.d("Nertz", "Expanded Touch: Taking card " + cardnum);

        int end = cards.size();
        for (int i = cardnum; i < end; ++i)
            liveCards.add(cards.remove(cardnum));

        liveCardsFromExpandedPile = true;

        // Draw the pile with the live cards removed
        staticTableBitmap = null;
    }

    // Not sure if this should be part of some other class
    public boolean onTouch(View v, MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (expandedPile != null) {
                    handleExpandedTouch(expandedPile, ev.getX(), ev.getY());
                    expandedPile = null;
                    break;
                }

                staticTableBitmap = null;
                fromPile = null;
                liveCards.clear();
                streamPileTouched = false;

                liveCardX = ev.getX();
                liveCardY = ev.getY();

                switch (detectArea(ev)) {
                    case NERTZ_PILE:
                        fromPile = player.getNertzPile();
                        break;

                    case RIVER:
                        fromPile = getRiverPile(ev.getX(), ev.getY());
                        break;

                    case STREAM:
                        handleStreamTouch(ev.getX(), ev.getY());
                        break;
                }

                // Only pop a card if the pile was selected but nothing
                // has been popped from it yet.
                if (fromPile != null) {
                    junit.framework.Assert.assertTrue(liveCards.isEmpty());
                    try {
                        if (!fromPile.isEmpty())
                            liveCards.add(fromPile.pop());
                    } catch (EmptyPileException e) {
                        Log.d("Nertz", "Tried to pop from empty pile!");
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                staticTableBitmap = null;
                TargetPile toPile = null;
                switch (detectArea(ev)) {
                    case STREAM:
                        handleStreamUp(ev.getX(), ev.getY());
                        break;
                    case LAKE:
                        if (liveCards.size() == 1)
                            toPile = player.getLake().findTargetPile(liveCards.get(0));
                        break;
                    case RIVER:
                        if (!liveCards.isEmpty()) {
                            TableauPile tp = getRiverPile(ev.getX(), ev.getY(), liveCards.get(0));
                            if (tp == fromPile) {
                                // Don't redraw the expansion if cards went nowhere
                                if (!liveCardsFromExpandedPile)
                                    expandedPile = tp;
                                // Return cards, don't count this as a move
                                returnLiveCards();
                                toPile = null;
                                fromPile = null;
                            } else {
                                toPile = tp;
                            }
                        }
                        break;
                }

                if (toPile != null) {
                    try {
                        LiveMove livemove = new LiveMove(fromPile, toPile, liveCards);
                        livemove.execute();

                        try {
                            // Nertz Pile needs the top card flipped
                            // after successfully moving the live card
                            // from it.
                            if (fromPile == player.getNertzPile()
                                    && fromPile.isFaceUpEmpty() == true
                                    && fromPile.isFaceDownEmpty() == false)
                                fromPile.flipTopCard();
                        }
                        catch (EmptyPileException e) {
                            Log.e("Nertz", "Tried to flip top nertz pile card but pile was empty");
                        }
                        liveCards.clear();
                        fromPile = null;
                        game.onPlayerMove();
                    }
                    catch (CardSequenceException e) {
                        Log.e("Nertz", "Failed to push live card onto destination pile");
                        returnLiveCards();
                    }
                } else {
                    returnLiveCards();
                }
                // Ensure that if the touch event began on the stream
                // pile that it is no longer considered the start of the
                // touch.
                // Probably better to just track the down & up locations
                // then figure out what to do.
                streamPileTouched = false;
                liveCardsFromExpandedPile = false;
                break;

            case MotionEvent.ACTION_MOVE:
                liveCardX = ev.getX();
                liveCardY = ev.getY();
                break;

            case MotionEvent.ACTION_CANCEL:
                staticTableBitmap = null;
                returnLiveCards();
                streamPileTouched = false;
                break;
        }

        invalidate();

        return true;
    }
}
