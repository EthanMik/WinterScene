package scene;

import java.awt.Color;

public enum ParticleType {
    RAIN {
        @Override
        public Shape createParticle(int x, int y) {
            return new Rain(x, y, Color.BLUE, 0, 5, 0, 15, 0, 0);
        }
    },
    SNOWFLAKE {
        @Override
        public Shape createParticle(int x, int y) {
            return new Snowflake(x, y, new Color(230, 255, 255, 80), 1, 20, 0.1, 2, 1, 0);
        }
    },
    SNOWFLAKE3D {
        @Override
        public Shape createParticle(int x, int y) {
            return new Snowflake3D(x, y, new Color(230, 255, 255, 80), 10, 20, 1, 1.5, 0, 0);
        }
    };

    public abstract Shape createParticle(int x, int y);
}
