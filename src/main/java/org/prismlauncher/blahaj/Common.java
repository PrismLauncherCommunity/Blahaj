package org.prismlauncher.blahaj;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import org.intellij.lang.annotations.Identifier;

import java.util.List;
import java.util.function.Consumer;

public class Common implements ModInitializer {
	public static String MODID = "blahaj";

	public void onInitialize() {
		Item grayShark = new CuddlyItem(new Item.Properties().stacksTo(1), "item.blahaj.gray_shark.tooltip");
		Registry.register(BuiltInRegistries.ITEM, asResource("gray_shark"), grayShark);

		Item blueShark = new CuddlyItem(new Item.Properties().stacksTo(1), "item.blahaj.blue_shark.tooltip");
		Registry.register(BuiltInRegistries.ITEM, asResource("blue_shark"), blueShark);

		Item blueWhale = new ItemContainerCuddlyItem(new Item.Properties().stacksTo(1), "item.blahaj.blue_whale.tooltip");
		Registry.register(BuiltInRegistries.ITEM, asResource("blue_whale"), blueWhale);

		Item breadPillow = new CuddlyItem(new Item.Properties().stacksTo(1), null);
		Registry.register(BuiltInRegistries.ITEM, asResource("bread"), breadPillow);

		// Register items to item group
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((content) -> {
			content.accept(blueShark);
			content.accept(grayShark);
			content.accept(blueWhale);
			content.accept(breadPillow);
		});

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if(!source.isBuiltin()) return;
			if(id.equals(BuiltInLootTables.STRONGHOLD_CROSSING)
				|| id.equals(BuiltInLootTables.STRONGHOLD_CORRIDOR)) {
				LootPool pb = LootPool.lootPool()
					.with(LootItem.lootTableItem(grayShark)
						.setWeight(5).build())
					.with(LootItem.lootTableItem(Items.AIR)
						.setWeight(100).build()).build();
				tableBuilder.pool(pb);
			}
			else if(id.equals(BuiltInLootTables.VILLAGE_PLAINS_HOUSE)) {
				LootPool pb = LootPool.lootPool()
					.with(LootItem.lootTableItem(grayShark).build())
					.with(LootItem.lootTableItem(Items.AIR)
						.setWeight(43).build()).build();
				tableBuilder.pool(pb);
			}
			else if(id.equals(BuiltInLootTables.VILLAGE_TAIGA_HOUSE)
				|| id.equals(BuiltInLootTables.VILLAGE_SNOWY_HOUSE)) {
				LootPool pb = LootPool.lootPool()
					.with(LootItem.lootTableItem(grayShark)
						.setWeight(5).build())
					.with(LootItem.lootTableItem(Items.AIR)
						.setWeight(54).build()).build();
				tableBuilder.pool(pb);
			}
		});

		Consumer<List<VillagerTrades.ItemListing>> lambda = factories -> {
			factories.add((entity, random) -> new MerchantOffer(
				new ItemStack(Items.EMERALD, 15), new ItemStack(grayShark),
				2, 30, 0.1f));
		};

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.SHEPHERD, 5, lambda);
	}

	public static ResourceLocation asResource(String name) {
		return new ResourceLocation(MODID, name);
	}
}
