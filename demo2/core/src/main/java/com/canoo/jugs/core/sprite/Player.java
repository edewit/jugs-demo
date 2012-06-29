package com.canoo.jugs.core.sprite;

import com.canoo.jugs.core.DemoGame;
import org.jbox2d.dynamics.World;
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

   private World world;
   private Rope rope;
   private GroupLayer groupLayer;

   public Player(final GroupLayer groupLayer, World world, final float x, final float y) {
      sprite = SpriteLoader.getSprite(IMAGE, JSON);

      this.world = world;
      this.groupLayer = groupLayer;
      this.x = x;
      this.y = y;

      sprite.addCallback(new ResourceCallback<Sprite>() {
         @Override
         public void done(Sprite sprite) {
            sprite.setSprite(spriteIndex);
            sprite.layer().setOrigin(sprite.width() / 2f, sprite.height());
            sprite.layer().setTranslation(x, y);
            sprite.layer().setDepth(99);
            groupLayer.add(sprite.layer());
            hasLoaded = true;
         }

         @Override
         public void error(Throwable err) {
            log().error("Error loading image!", err);
         }
      });
   }

   public void paint(float alpha) {
      if (rope != null) {
         rope.paint(alpha);
      }
   }

   public void update(float delta) {
      if (hasLoaded) {
         if (direction != 0) {
            animate(delta);
            x += direction * delta / 3;
            sprite.layer().setTranslation(x, y);
         }
      }

      if (rope != null) {
         rope.update(delta);
      }
   }

   private void animate(float delta) {
      step = (step + delta) % 150;
      if (step == 0) {
         step = 0;
         spriteIndex = (spriteIndex + 1) % (sprite.numSprites() - 1);
         sprite.setSprite(spriteIndex);
      }
   }

   public void shoot() {
      rope = new Rope(groupLayer, world, x * DemoGame.physUnitPerScreenUnit, y * DemoGame.physUnitPerScreenUnit);
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
      sprite.setSprite(5);
      direction = 0;
   }

   public float y() {
      return y;
   }

   public float x() {
      return x;
   }

   public Rope getRope() {
      return rope;
   }
}
