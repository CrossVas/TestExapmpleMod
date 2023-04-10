package com.example.examplemod.keyboard;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public interface IKey {
    boolean test(Player player);
    Component getName();
    Component getKeyName();

    static IKey empty() {
        return EmptyKeyBind.INSTANCE;
    }

    class EmptyKeyBind implements IKey {
        static final IKey INSTANCE = new EmptyKeyBind();

        @Override
        public boolean test(Player player) {
            return false;
        }

        @Override
        public Component getName() {
            return Component.literal("Unbound Keybinding");
        }

        @Override
        public Component getKeyName() {
            return Component.literal("Unknown");
        }
    }
}
