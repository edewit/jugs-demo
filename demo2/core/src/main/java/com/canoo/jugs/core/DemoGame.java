package com.canoo.jugs.core;

import com.canoo.jugs.core.sprite.Player;
import playn.core.*;

import static playn.core.PlayN.*;

public class DemoGame implements Game {
    private Player player;

    @Override
    public void init() {
        graphics().setSize(798, 595);
        GroupLayer layer = graphics().createGroupLayer();
        graphics().rootLayer().add(layer);

        Image bgImage = assets().getImage("images/bg.png");
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        layer.add(bgLayer);

        player = new Player(layer, 100, 523);

        keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyDown(Keyboard.Event event) {
                switch (event.key()) {
                    case LEFT:
                        player.moveLeft();
                        break;
                    case RIGHT:
                        player.moveRight();
                        break;
                }
            }

            @Override
            public void onKeyUp(Keyboard.Event event) {
                player.stop();
            }
        });
    }

    @Override
    public void paint(float alpha) {
    }

    @Override
    public void update(float delta) {
        player.update(delta);
    }

    @Override
    public int updateRate() {
        return 25;
    }
}
