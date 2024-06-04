package de.rasmusantons.playertracker;

import de.rasmusantons.playertracker.network.PlayerTrackerNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;

public class PlayerTracker implements ModInitializer {
    public static final GameRules.Key<GameRules.BooleanValue> GIVE_PLAYER_TRACKER =
            GameRuleRegistry.register("givePlayerTracker",
                    GameRules.Category.PLAYER,
                    GameRuleFactory.createBooleanRule(false)
            );

    @Override
    public void onInitialize() {
        PlayerTrackerNetworking.init();
    }
}
