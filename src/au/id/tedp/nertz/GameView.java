package au.id.tedp.nertz;

import android.graphics.Canvas;
import android.view.View;

class GameView extends View {
    public GameView(android.content.Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawARGB(255, 255, 0, 255);
    }
}
