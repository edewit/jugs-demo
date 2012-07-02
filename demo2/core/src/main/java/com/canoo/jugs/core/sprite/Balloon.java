package com.canoo.jugs.core.sprite;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.GroupLayer;
import playn.core.ResourceCallback;

import static playn.core.PlayN.log;
import static playn.core.PlayN.random;

/**
 * @author edewit
 */
public class Balloon extends AbstractSprite {

   private static final String JSON = "balloon.json";
   private static final String IMAGE = "images/balloon.png";

   private int spriteIndex = 1;
   private boolean animateDeath;
   private int time;

   public Balloon(final GroupLayer layer, World world, final float x, final float y) {
      super(x, y, world);
      sprite = SpriteLoader.getSprite(IMAGE, JSON);

      sprite.addCallback(new ResourceCallback<Sprite>() {
         @Override
         public void done(Sprite sprite) {
            sprite.setSprite(0);
            setPosition(x, y);
            sprite.layer().setOrigin(sprite.width() / 2f, sprite.height() / 2f);
            sprite.layer().setDepth(3);
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
      time += delta;
      if (spriteIndex >= sprite.numSprites()) {
         sprite.layer().destroy();
      } else if (animateDeath && time % (delta * 5) == 0) {
         sprite.setSprite(spriteIndex++);
      }
   }

   public Body initPhysicsBody() {
      FixtureDef fixtureDef = new FixtureDef();
      BodyDef bodyDef = new BodyDef();
      bodyDef.type = BodyType.DYNAMIC;
      bodyDef.position = new Vec2(0, 0);
      Body body = world.createBody(bodyDef);

      CircleShape circleShape = new CircleShape();
      circleShape.m_radius = 1.5f;
      fixtureDef.shape = circleShape;
      fixtureDef.density = 0.4f;
      fixtureDef.friction = 0.1f;
      fixtureDef.restitution = 0.95f;
      circleShape.m_p.set(0, 0);
      body.createFixture(fixtureDef);
      //body.setLinearDamping(0.2f);
      body.setLinearVelocity(new Vec2(random() * 20 + 1, 1));
      body.setTransform(new Vec2(x, y), 0);
      return body;
   }

   public void pang() {
      world.destroyBody(body);
      animateDeath = true;
   }
}
