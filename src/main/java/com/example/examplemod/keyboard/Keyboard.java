package com.example.examplemod.keyboard;

import com.example.examplemod.ExampleMod;
import com.mojang.blaze3d.platform.InputConstants;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class Keyboard {

    public static Map<String, ClientKey> keys = Object2ObjectMaps.synchronize(new Object2ObjectLinkedOpenHashMap<>());

    public static IKey registerKey(String name, int keyBinding) {
        return keys.computeIfAbsent(name, T -> new ClientKey(T, keyBinding));
    }

    public static class ClientKey implements IKey {

        String name;
        KeyMapping keyMapping;

        public ClientKey(String name, int key) {
            this.name = name;
            this.keyMapping = new KeyMapping(name, key, ExampleMod.MODID);
            Options options = Minecraft.getInstance().options;
            options.keyMappings = ArrayUtils.add(options.keyMappings, keyMapping);
        }

        public boolean isKeyPressed(KeyMapping binding) {
            IKeyConflictContext context = binding.getKeyConflictContext();
            binding.setKeyConflictContext(KeyConflictContext.UNIVERSAL);
            boolean result = isKeyDown(binding);
            binding.setKeyConflictContext(context);
            return result;
        }

        private boolean isKeyDown(KeyMapping binding) {
            if(binding.isUnbound()) return false;
            InputConstants.Key input = binding.getKey();
            long monitor = Minecraft.getInstance().getWindow().getWindow();
            return input.getType() == InputConstants.Type.MOUSE ? GLFW.glfwGetMouseButton(monitor, input.getValue()) == 1 : InputConstants.isKeyDown(monitor, input.getValue());
        }

        public boolean getState() {
            return isKeyPressed(keyMapping);
        }

        @Override
        public boolean test(Player player) {
            return getState();
        }

        @Override
        public Component getName() {
            return Component.literal(name);
        }

        @Override
        public Component getKeyName() {
            return keyMapping.getTranslatedKeyMessage();
        }
    }
 }
