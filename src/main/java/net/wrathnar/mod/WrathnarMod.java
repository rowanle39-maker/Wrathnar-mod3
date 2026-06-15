package net.wrathnar.mod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
import net.wrathnar.mod.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WrathnarMod implements ModInitializer {

	public static final String MOD_ID = "wrathnar";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();

		// Wrathnar's curse: hitting a living entity with the Wrathnar sword
		// sets it on fire for 5 seconds.
		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (!world.isClient()
					&& player.getStackInHand(hand).getItem() == ModItems.WRATHNAR_SWORD
					&& entity instanceof LivingEntity living) {
				living.setFireTicks(100);
			}
			return ActionResult.PASS;
		});

		LOGGER.info("[Wrathnar] The Wrathnar sword has entered the world!");
	}
}
