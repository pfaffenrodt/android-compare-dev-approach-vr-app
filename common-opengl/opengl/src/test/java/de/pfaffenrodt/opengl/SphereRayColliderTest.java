package de.pfaffenrodt.opengl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

/**
 * Created by Dimitri on 30.08.16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class SphereRayColliderTest {

    public static final float RADIUS = 0.5f;
    private Transform mTransform;
    private SphereRayCollider mSphereRayCollider;

    @Before
    public void setUp() throws Exception {
        mTransform = new Transform();
        mTransform.setPosition(0,0,1f);
        mSphereRayCollider = new SphereRayCollider(mTransform, RADIUS);

    }
    @Test
    public void testIntersect() throws Exception {
        Vector rayOrigin = new Vector(0, 0, 0);
        Vector toPoint = new Vector(0,0,0);
        Ray ray = new Ray(rayOrigin, toPoint);
        boolean intersect = mSphereRayCollider.intersects(ray);
        assertFalse(intersect);

        toPoint = new Vector(0,0,1);
        ray = new Ray(rayOrigin, toPoint);
        intersect = mSphereRayCollider.intersects(ray);
        assertTrue(intersect);
    }

    @Test
    public void testSphereIntersect() throws Exception {
        Vector rayOrigin = new Vector(0, 0, 0);
        Vector toPoint = new Vector(0,0,0);
        Ray ray = new Ray(rayOrigin, toPoint);
        Vector sphereOrigin = new Vector(0,0,1);
        boolean intersect = mSphereRayCollider.sphereIntersect(ray, sphereOrigin, RADIUS);
        assertFalse(intersect);

        toPoint = new Vector(0,0,1);
        ray = new Ray(rayOrigin, toPoint);
        intersect = mSphereRayCollider.sphereIntersect(ray, sphereOrigin, RADIUS);
        assertTrue(intersect);


        toPoint = new Vector(1,0,1);
        ray = new Ray(rayOrigin, toPoint);
        intersect = mSphereRayCollider.sphereIntersect(ray, sphereOrigin, RADIUS);
        assertFalse(intersect);

        toPoint = new Vector(RADIUS,0,1);
        ray = new Ray(rayOrigin, toPoint);
        intersect = mSphereRayCollider.sphereIntersect(ray, sphereOrigin, RADIUS);
        assertTrue(intersect);

        toPoint = new Vector(-RADIUS ,0,1);
        ray = new Ray(rayOrigin, toPoint);
        intersect = mSphereRayCollider.sphereIntersect(ray, sphereOrigin, RADIUS);
        assertTrue(intersect);

        toPoint = new Vector(-1,0,1);
        ray = new Ray(rayOrigin, toPoint);
        intersect = mSphereRayCollider.sphereIntersect(ray, sphereOrigin, RADIUS);
        assertFalse(intersect);
    }


}
