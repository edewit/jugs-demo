package com.canoo.jugs.core;

import com.canoo.jugs.core.sprite.Balloon;
import com.canoo.jugs.core.sprite.Player;
import com.canoo.jugs.core.sprite.Rope;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;

import java.util.Stack;

import static playn.core.PlayN.*;

public class DemoGame implements Game {
   public static float physUnitPerScreenUnit = 1 / 26.666667f;
   private Player player;
   private World world;
   private Balloon balloon;
   private Stack<Contact> contacts = new Stack<Contact>();

   private static boolean showDebugDraw = false;
   private DebugDrawBox2D debugDraw;

   @Override
   public void init() {
      graphics().setSize(798, 595);
      GroupLayer layer = graphics().createGroupLayer();
      graphics().rootLayer().add(layer);

      Image bgImage = assets().getImage("images/bg.png");
      ImageLayer bgLayer = graphics().createImageLayer(bgImage);
      layer.add(bgLayer);

      Image bgImageOverlay = assets().getImage("images/bg-overlay.png");
      ImageLayer bgLayerOverlay = graphics().createImageLayer(bgImageOverlay);
      bgLayerOverlay.setTranslation(0, 595 - 84);
      bgLayerOverlay.setDepth(2);
      layer.add(bgLayerOverlay);

      // size of world
      int width = 29;
      int height = 20;

      // create the physics world
      Vec2 gravity = new Vec2(0.0f, 10.0f);
      world = new World(gravity, true);


      if (showDebugDraw) {
         CanvasImage image = graphics().createImage(798, 595);
         graphics().rootLayer().add(graphics().createImageLayer(image));
         debugDraw = new DebugDrawBox2D();
         debugDraw.setCanvas(image);
         debugDraw.setFlipY(false);
         debugDraw.setStrokeAlpha(150);
         debugDraw.setFillAlpha(75);
         debugDraw.setStrokeWidth(2.0f);
         debugDraw.setFlags(DebugDraw.e_shapeBit | DebugDraw.e_jointBit | DebugDraw.e_aabbBit);
         debugDraw.setCamera(0, 0, 1f / physUnitPerScreenUnit);
         world.setDebugDraw(debugDraw);
      }

      world.setWarmStarting(true);
      world.setAutoClearForces(true);

      player = new Player(layer, world, 100, 523);

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
               case SPACE:
                  player.shoot();
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

      // create the ground
      Body ground = world.createBody(new BodyDef());
      PolygonShape groundShape = new PolygonShape();
      groundShape.setAsEdge(new Vec2(0, height), new Vec2(width, height));
      ground.createFixture(groundShape, 0.0f);

      world.setContactListener(new ContactListener() {
         @Override
         public void beginContact(Contact contact) {
            contacts.push(contact);
         }

         @Override
         public void endContact(Contact contact) {

         }

         @Override
         public void preSolve(Contact contact, Manifold oldManifold) {

         }

         @Override
         public void postSolve(Contact contact, ContactImpulse impulse) {

         }
      });

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
      player.paint(alpha);

      if (showDebugDraw) {
         debugDraw.getCanvas().clear();
         world.drawDebugData();
      }
   }

   @Override
   public void update(float delta) {
      processContacts();
      player.update(delta);
      world.step(0.033f, 10, 10);
      balloon.update(delta);
   }

   @Override
   public int updateRate() {
      return 25;
   }

   public void processContacts() {
      while (!contacts.isEmpty()) {
         Contact contact = contacts.pop();

         final Body body1 = contact.getFixtureA().getBody();
         final Body body2 = contact.getFixtureB().getBody();

         final Rope rope = player.getRope();
         if (rope != null) {
            if ((body1.equals(balloon.getBody()) || body2.equals(balloon.getBody()))
                    && (body1.equals(rope.getBody()) || body2.equals(rope.getBody()))) {
               balloon.pang();
            }
         }
      }
   }

}
