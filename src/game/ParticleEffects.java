package game;

import org.joml.Vector2f;
import org.joml.Vector3i;
import edu.usu.graphics.Rectangle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Combined particle manager with custom effects: death, win, and edge-highlight boxes.
 */
public class ParticleEffects {
    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();
    private final Vector2f screenCenter;

    /**
     * @param screenCenter center of the screen (for playerWin explosion)
     */
    public ParticleEffects(Vector2f screenCenter) {
        this.screenCenter = screenCenter;
    }

    /**
     * Update all active particles; remove ones whose lifetime expired.
     */
    public void update(double elapsedTime) {
        List<Particle> alive = new ArrayList<>();
        for (Particle p : particles) {
            if (p.update(elapsedTime)) {
                alive.add(p);
            }
        }
        particles.clear();
        particles.addAll(alive);
    }

    /**
     * @return current active particles for rendering
     */
    public Collection<Particle> getParticles() {
        return new ArrayList<>(particles);
    }

    /**
     * Mini explosion at player death position.
     */
    public void playerDeath(Vector3i pos) {
        Vector2f center = new Vector2f(pos.x, pos.y);
        float sizeMean  = 5f,   sizeStdDev = 2f;
        float speedMean = 120f, speedStdDev = 30f;
        float lifeMean  = 1.0f, lifeStdDev = 0.2f;
        int   count     = 50;
        spawnExplosion(center, sizeMean, sizeStdDev, speedMean, speedStdDev, lifeMean, lifeStdDev, count);
    }

    /**
     * Mini explosion at object death position.
     */
    public void objectDeath(Vector3i pos) {
        Vector2f center = new Vector2f(pos.x, pos.y);
        float sizeMean  = 3f,   sizeStdDev = 1f;
        float speedMean = 80f,  speedStdDev = 20f;
        float lifeMean  = 0.8f, lifeStdDev = 0.15f;
        int   count     = 30;
        spawnExplosion(center, sizeMean, sizeStdDev, speedMean, speedStdDev, lifeMean, lifeStdDev, count);
    }

    /**
     * Box-edge highlight around object when it becomes a win object.
     */
    public void objectIsWin(Vector3i pos) {
        float boxW      = 50f, boxH = 50f;
        float sizeMean  = 4f,  sizeStdDev = 1f;
        float speedMean = 60f, speedStdDev = 15f;
        float lifeMean  = 1.2f,lifeStdDev = 0.3f;
        int   count     = 80;
        spawnBoxEdges(pos, boxW, boxH, sizeMean, sizeStdDev, speedMean, speedStdDev, lifeMean, lifeStdDev, count);
    }

    /**
     * Box-edge highlight around object when it becomes "You".
     */
    public void objectIsYou(Vector3i pos) {
        float boxW      = 60f, boxH = 60f;
        float sizeMean  = 4f,  sizeStdDev = 1f;
        float speedMean = 70f, speedStdDev = 20f;
        float lifeMean  = 1.0f,lifeStdDev = 0.25f;
        int   count     = 80;
        spawnBoxEdges(pos, boxW, boxH, sizeMean, sizeStdDev, speedMean, speedStdDev, lifeMean, lifeStdDev, count);
    }

    /**
     * Full-screen celebration explosion when player wins.
     */
    public void playerWin() {
        float sizeMean  = 8f,  sizeStdDev = 3f;
        float speedMean = 100f,speedStdDev = 40f;
        float lifeMean  = 1.5f,lifeStdDev = 0.3f;
        int   count     = 100;
        spawnExplosion(screenCenter, sizeMean, sizeStdDev, speedMean, speedStdDev, lifeMean, lifeStdDev, count);
    }

    // ----- private helpers -----

    private void spawnExplosion(Vector2f center,
                                float sizeMean, float sizeStdDev,
                                float speedMean, float speedStdDev,
                                float lifeMean,  float lifeStdDev,
                                int count) {
        for (int i = 0; i < count; i++) {
            Vector2f dir = randomCircleVector();
            float size   = randomGaussian(sizeMean, sizeStdDev);
            float speed  = randomGaussian(speedMean, speedStdDev);
            double life  = randomGaussian(lifeMean, lifeStdDev);
            Particle p = new Particle(
                    new Vector2f(center.x, center.y),
                    dir,
                    speed,
                    new Vector2f(size, size),
                    life);
            particles.add(p);
        }
    }

    private void spawnBoxEdges(Vector3i pos,
                               float boxW, float boxH,
                               float sizeMean, float sizeStdDev,
                               float speedMean, float speedStdDev,
                               float lifeMean, float lifeStdDev,
                               int count) {
        float x0 = pos.x - boxW / 2f;
        float y0 = pos.y - boxH / 2f;
        for (int i = 0; i < count; i++) {
            float x, y;
            switch (random.nextInt(4)) {
                case 0: // top edge
                    x = x0 + random.nextFloat() * boxW;
                    y = y0;
                    break;
                case 1: // bottom edge
                    x = x0 + random.nextFloat() * boxW;
                    y = y0 + boxH;
                    break;
                case 2: // left edge
                    x = x0;
                    y = y0 + random.nextFloat() * boxH;
                    break;
                default: // right edge
                    x = x0 + boxW;
                    y = y0 + random.nextFloat() * boxH;
                    break;
            }
            Vector2f center = new Vector2f(x, y);
            Vector2f dir    = randomCircleVector();
            float size   = randomGaussian(sizeMean, sizeStdDev);
            float speed  = randomGaussian(speedMean, speedStdDev);
            double life  = randomGaussian(lifeMean, lifeStdDev);
            Particle p = new Particle(center, dir, speed, new Vector2f(size, size), life);
            particles.add(p);
        }
    }

    private Vector2f randomCircleVector() {
        double ang = random.nextDouble() * Math.PI * 2;
        return new Vector2f((float) Math.cos(ang), (float) Math.sin(ang));
    }

    private float randomGaussian(float mean, float stdDev) {
        return (float) (mean + random.nextGaussian() * stdDev);
    }

    // ----- inner Particle class -----
    private static class Particle {
        public static long nextName = 0;
        public long name;
        public Vector2f center;
        public Vector2f size;
        public Rectangle area;
        public float rotation;

        private Vector2f direction;
        private float speed;
        private double lifetime;
        private double alive = 0;

        public Particle(Vector2f center,
                        Vector2f direction,
                        float speed,
                        Vector2f size,
                        double lifetime) {
            this.name = nextName++;
            this.center = center;
            this.direction = direction;
            this.speed = speed;
            this.size = size;
            this.area = new Rectangle(center.x - size.x / 2,
                    center.y - size.y / 2,
                    size.x,
                    size.y);
            this.lifetime = lifetime;
            this.rotation = 0;
        }

        /**
         * @return true if still alive
         */
        public boolean update(double elapsedTime) {
            alive += elapsedTime;
            center.x += (float) (elapsedTime * speed * direction.x);
            center.y += (float) (elapsedTime * speed * direction.y);
            area.left += (float) (elapsedTime * speed * direction.x);
            area.top  += (float) (elapsedTime * speed * direction.y);
            rotation += (speed / 0.5f);
            return alive < lifetime;
        }
    }
}
