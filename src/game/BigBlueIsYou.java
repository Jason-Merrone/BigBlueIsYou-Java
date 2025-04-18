package game;

import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;

public class BigBlueIsYou {

    public static void main(String[] args) {

        try (Graphics2D graphics = new Graphics2D(1920, 1080, "Big Blue Is You")) {
            graphics.initialize(Color.BLACK);
            Game game = new Game(graphics);
            game.initialize();
            game.run();
            game.shutdown();
        }
    }
}
