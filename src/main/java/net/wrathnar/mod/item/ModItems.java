package net.wrathnar.mod.item;

import java.util.function.Function;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.wrathnar.mod.WrathnarMod;

public class ModItems {

	public static final ToolMaterial WRATHNAR_MATERIAL = new ToolMaterial(
			BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
			2500,
			9.0f,
			5.0f,
			20,
			ItemTags.NETHERITE_TOOL_MATERIALS
	);

	public static final Item WRATHNAR_SWORD = registerItem("wrathnar_sword",
			settings -> new SwordItem(WRATHNAR_MATERIAL, 4.0f, -2.4f, settings
					.rarity(Rarity.EPIC)
					.fireproof()
			)
	);

	private static Item registerItem(String name, Function<Item.Settings, Item> factory) {
		RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(WrathnarMod.MOD_ID, name));
		Item item = factory.apply(new Item.Settings().registryKey(key));
		return Registry.register(Registries.ITEM, key, item);
	}

	public static void registerModItems() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
			entries.add(WRATHNAR_SWORD);
		});
	}
}
