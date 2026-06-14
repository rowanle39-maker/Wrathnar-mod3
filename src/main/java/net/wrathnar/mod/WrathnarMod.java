package net.wrathnar.mod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.ActionResult;
import net.wrathnar.mod.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WrathnarMod implements ModInitializer {

	public static final String MOD_ID = "wrathnar";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Register the Wrathnar sword and any other mod items.
		ModItems.registerModItems();

		// Wrathnar's curse: hitting a living entity with the Wrathnar sword
		// afflicts it with a short burst of the Wither effect.
		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (!world.isClient()
					&& player.getStackInHand(hand).getItem() == ModItems.WRATHNAR_SWORD
					&& entity instanceof LivingEntity living) {
				living.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 0));
			}
			return ActionResult.PASS;
		});

		LOGGER.info("[Wrathnar] The Wrathnar sword has entered the world!");
	}
}
