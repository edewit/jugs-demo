package com.canoo.jugs.core.sprite;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.GroupLayer;
import playn.core.ResourceCallback;

import static playn.core.PlayN.log;

/**
 * @author edewit
 */
public class Rope extends AbstractSprite {

   private static final String JSON = "rope.json";
   private static final String IMAGE = "images/rope.png";

   public Rope(final GroupLayer layer, World world, final float x, final float y) {
      super(x, y, world);
      sprite = SpriteLoader.getSprite(IMAGE, JSON);

      sprite.addCallback(new ResourceCallback<Sprite>() {
         @Override
         public void done(Sprite resource) {
            sprite.setSprite(0);
            setPosition(x, y);

            sprite.layer().setOrigin(sprite.width() / 2f, sprite.height() / 2f);
            sprite.layer().setDepth(1);
            layer.add(sprite.layer());
         }

         @Override
         public void error(Throwable err) {
            log().error("Error loading sprite image", err);
         }
      });
   }

   @Override
   public void update(float delta) {
      super.update(delta);
      if (y - 20 - sprite.height() / 2 < 0) {
         body.setLinearVelocity(new Vec2(0, 0));
      }
   }

   public Body initPhysicsBody() {
      FixtureDef fixtureDef = new FixtureDef();
      BodyDef bodyDef = new BodyDef();
      bodyDef.type = BodyType.KINEMATIC;
      bodyDef.position = new Vec2(0, 0);
      Body body = world.createBody(bodyDef);

      PolygonShape shape = new PolygonShape();
      //TODO seems a bit hard coded ;)
      shape.setAsBox(0.1f, 9);
      fixtureDef.shape = shape;
      body.createFixture(fixtureDef);
      body.setLinearVelocity(new Vec2(0, -10));
      body.setTransform(new Vec2(x, y), 0);
      return body;
   }
}
