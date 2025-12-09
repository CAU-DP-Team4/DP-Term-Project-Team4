package game.mode;

import game.GameMode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public final class ModeStrategyFactory {
    public static ModeStrategy getStrategy(GameMode mode) {
        switch (mode) {
            case CLASSIC:
                return new ClassicModeStrategy();
            case LIVES:
                return new LivesModeStrategy(3);
            default:
                return new ClassicModeStrategy();
        }
    }
}