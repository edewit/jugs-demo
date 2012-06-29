package com.canoo.jugs.core.sprite;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import static com.canoo.jugs.core.DemoGame.physUnitPerScreenUnit;

/**
 * @author edewit
 */
public abstract class AbstractSprite {
   protected Sprite sprite;
   protected float x;
   protected float y;

   protected World world;
   protected Body body;


   private float prevX;
   private float prevY;
   private float prevA;

   public AbstractSprite(final float x, final float y, World world) {
      this.x = x;
      this.y = y;
      this.world = world;
      body = initPhysicsBody();
   }

   protected void setPosition(float x, float y) {
      this.x = x / physUnitPerScreenUnit;
      this.y = y / physUnitPerScreenUnit;
      sprite.layer().setTranslation(this.x, this.y);
   }

   public abstract Body initPhysicsBody();

   public Body getBody() {
      return body;
   }

   public void paint(float alpha) {
      // interpolate based on previous state
      float x = (body.getPosition().x * alpha) + (prevX * (1f - alpha));
      float y = (body.getPosition().y * alpha) + (prevY * (1f - alpha));
      float a = (body.getAngle() * alpha) + (prevA * (1f - alpha));
      setPosition(x, y);
      sprite.layer().setRotation(a);
   }

   public void update(float delta) {
      // store state for interpolation in paint()
      prevX = body.getPosition().x;
      prevY = body.getPosition().y;
      prevA = body.getAngle();
   }
}
