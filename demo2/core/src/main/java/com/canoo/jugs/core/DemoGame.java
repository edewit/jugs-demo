package com.canoo.jugs.core;

import com.canoo.jugs.core.sprite.Balloon;
import com.canoo.jugs.core.sprite.Player;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import playn.core.*;

import static playn.core.PlayN.*;

public class DemoGame implements Game {
   public static float physUnitPerScreenUnit = 1 / 26.666667f;
   private Player player;
   private World world;
   private Balloon balloon;

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

      pointer().setListener(new Pointer.Adapter() {
         @Override
         public void onPointerStart(Pointer.Event event) {
            if (event.x() > player.x()) {
               player.moveRight();
            } else {
               player.moveLeft();
            }
         }

         @Override
         public void onPointerEnd(Pointer.Event event) {
            player.stop();
         }
      });

      // size of world
      int width = 29;
      int height = 20;

      // create the physics world
      Vec2 gravity = new Vec2(0.0f, 10.0f);
      world = new World(gravity, true);
      world.setWarmStarting(true);
      world.setAutoClearForces(true);

      // create the ground
      Body ground = world.createBody(new BodyDef());
      PolygonShape groundShape = new PolygonShape();
      groundShape.setAsEdge(new Vec2(0, height), new Vec2(width, height));
      ground.createFixture(groundShape, 0.0f);

      // create the walls
      Body wallLeft = world.createBody(new BodyDef());
      PolygonShape wallLeftShape = new PolygonShape();
      wallLeftShape.setAsEdge(new Vec2(0, 0), new Vec2(1, height));
      wallLeft.createFixture(wallLeftShape, 0.0f);
      Body wallRight = world.createBody(new BodyDef());
      PolygonShape wallRightShape = new PolygonShape();
      wallRightShape.setAsEdge(new Vec2(width, 0), new Vec2(width, height));
      wallRight.createFixture(wallRightShape, 0.0f);

      balloon = new Balloon(layer, world, 100 * physUnitPerScreenUnit, 20 * physUnitPerScreenUnit);
   }

   @Override
   public void paint(float alpha) {
      balloon.paint(alpha);
   }

   @Override
   public void update(float delta) {
      player.update(delta);
      world.step(0.033f, 10, 10);
      balloon.update(delta);
   }

   @Override
   public int updateRate() {
      return 25;
   }
}
