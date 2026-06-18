package net.wrathnar.mod;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.wrathnar.mod.item.ModItems;

public class WrathnarFlyHandler {

	private static int flyTicks = 0;
	private static boolean isFlying = false;
	private static boolean wasFlying = false;

	public static void register() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;
			PlayerEntity player = client.player;

			boolean holdingWrathnar =
				player.getMainHandStack().getItem() == ModItems.WRATHNAR_SWORD ||
				player.getOffHandStack().getItem() == ModItems.WRATHNAR_SWORD;

			// Z tuşuna basınca direkt uç
			if (WrathnarClientMod.flyKey.wasPressed() && holdingWrathnar && !isFlying) {
				isFlying = true;
				flyTicks = 200;
				player.getAbilities().allowFlying = true;
				player.getAbilities().flying = true;
				player.getAbilities().flying = true;
				player.sendAbilitiesUpdate();
				// Yukarı fırlat
				player.setVelocity(player.getVelocity().x, 1.5, player.getVelocity().z);
			}

			if (isFlying) {
				flyTicks--;
				wasFlying = true;
				if (flyTicks <= 0) {
					isFlying = false;
					player.getAbilities().allowFlying = false;
					player.getAbilities().flying = false;
					player.sendAbilitiesUpdate();
				}
			}

			// Yere değince patlama
			if (wasFlying && !isFlying && player.isOnGround()) {
				wasFlying = false;
				if (!client.isIntegratedServerRunning()) return;
				// Sunucuya patlama komutu gönder
				client.player.networkHandler.sendChatCommand(
					"execute at " + 
					(int)player.getX() + " " + 
					(int)player.getY() + " " + 
					(int)player.getZ() + 
					" run execute as @e[distance=..5,type=!minecraft:player] run damage @s 0 minecraft:generic"
				);
				// Patlama efekti
				client.player.networkHandler.sendChatCommand(
					"particle minecraft:explosion_emitter " +
					player.getX() + " " + player.getY() + " " + player.getZ() +
					" 0 0 0 1 1 force"
				);
			}
		});
	}
}
