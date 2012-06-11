package com.canoo.jugs.core.sprite;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.GroupLayer;
import playn.core.ResourceCallback;

import static com.canoo.jugs.core.DemoGame.*;
import static playn.core.PlayN.log;

/**
 * @author edewit
 */
public class Balloon {

    private static final String JSON = "balloon.json";
    private static final String IMAGE = "images/balloon.png";

    private Sprite sprite;
    public float x;
    public float y;
    private World world;

    private float prevX, prevY, prevA;
    private Body body;


    public Balloon(final GroupLayer layer, World world, final float x, final float y) {
        this.world = world;
        sprite = SpriteLoader.getSprite(IMAGE, JSON);
        this.x = x;
        this.y = y;
        body = getBody();

        sprite.addCallback(new ResourceCallback<Sprite>() {
            @Override
            public void done(Sprite sprite) {
                sprite.setSprite(0);
                setPosition(x, y);
                sprite.layer().setOrigin(sprite.width() / 2f, sprite.height() / 2f);
                layer.add(sprite.layer());
            }

            @Override
            public void error(Throwable err) {
                log().error("Error loading sprite image", err);
            }
        });
    }

    private void setPosition(float x, float y) {
        sprite.layer().setTranslation(x / physUnitPerScreenUnit, y / physUnitPerScreenUnit);
    }

    public Body getBody() {
        FixtureDef fixtureDef = new FixtureDef();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0, 0);
        Body body = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.m_radius = 0.5f;
        fixtureDef.shape = circleShape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.35f;
        circleShape.m_p.set(0, 0);
        body.createFixture(fixtureDef);
        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x, y), 0);
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
