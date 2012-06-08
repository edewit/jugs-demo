package com.canoo.jugs.core.sprite;

import playn.core.GroupLayer;
import playn.core.ResourceCallback;

import static playn.core.PlayN.log;

/**
 * @author edewit
 */
public class Player {
    private static final String IMAGE = "images/pang2.png";
    private static final String JSON = "player.json";
    private int spriteIndex = 0;

    private Sprite sprite;
    private boolean hasLoaded;

    private float step;
    private float direction;
    private float facing = 1;
    private float x, y;

    public Player(final GroupLayer groupLayer, final float x, final float y) {
        sprite = SpriteLoader.getSprite(IMAGE, JSON);

        this.x = x;
        this.y = y;

        sprite.addCallback(new ResourceCallback<Sprite>() {
            @Override
            public void done(Sprite sprite) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width() / 2f, sprite.height());
                sprite.layer().setTranslation(x, y);
                groupLayer.add(sprite.layer());
                hasLoaded = true;
            }

            @Override
            public void error(Throwable err) {
                log().error("Error loading image!", err);
            }
        });
    }

    public void update(float delta) {
        if (hasLoaded) {
            if (direction != 0) {
                animate(delta);
                x += direction * delta / 3;
                sprite.layer().setTranslation(x, y);
            }
        }
    }

    private void animate(float delta) {
        step = (step + delta) % 150;
        if (step == 0) {
            step = 0;
            spriteIndex = (spriteIndex + 1) % sprite.numSprites();
            sprite.setSprite(spriteIndex);
        }
    }

    public void moveRight() {
        turnPlayer(1);
    }

    public void moveLeft() {
        turnPlayer(-1);
    }

    private void turnPlayer(int direction) {
        if (facing != direction) {
            sprite.layer().transform().scale(-1, 1);
        }
        facing = direction;
        this.direction = direction;
    }

    public void stop() {
        direction = 0;
    }
}
