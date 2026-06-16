package net.wrathnar.mod;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.wrathnar.mod.item.ModItems;

public class WrathnarFlyHandler {

	private static int flyTicks = 0;
	private static boolean isFlying = false;

	public static void register() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;
			PlayerEntity player = client.player;

			boolean holdingWrathnar = 
				player.getMainHandStack().getItem() == ModItems.WRATHNAR_SWORD ||
				player.getOffHandStack().getItem() == ModItems.WRATHNAR_SWORD;

			if (WrathnarClientMod.flyKey.wasPressed() && holdingWrathnar && !isFlying) {
				isFlying = true;
				flyTicks = 200;
				player.getAbilities().allowFlying = true;
				player.getAbilities().flying = true;
				player.sendAbilitiesUpdate();
			}

			if (isFlying) {
				flyTicks--;
				if (flyTicks <= 0) {
					isFlying = false;
					player.getAbilities().allowFlying = false;
					player.getAbilities().flying = false;
					player.sendAbilitiesUpdate();
					// Hasar yememek için geçici no fall damage
					player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 60, 4));
				}
			}
		});
	}
}
