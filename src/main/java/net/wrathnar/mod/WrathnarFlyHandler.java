package net.wrathnar.mod;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
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

			if (WrathnarClientMod.flyKey.wasPressed() && holdingWrathnar && !isFlying) {
				isFlying = true;
				flyTicks = 200;
				player.getAbilities().allowFlying = true;
				player.getAbilities().flying = true;
				player.sendAbilitiesUpdate();
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
				Vec3d pos = player.getPos();
				// Oyuncuyu hasar yemez yap
				player.setInvulnerable(true);
				// Patlama komutu - etraftaki canlılara hasar verir
				client.player.networkHandler.sendChatCommand(
					"execute at @s run summon minecraft:tnt " +
					pos.x + " " + pos.y + " " + pos.z
				);
				// 1 saniye sonra invulnerability kaldır
				new Thread(() -> {
					try {
						Thread.sleep(1000);
						player.setInvulnerable(false);
					} catch (Exception e) {}
				}).start();
			}
		});
	}
}
