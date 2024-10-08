package com.chimericdream.hopperxtreme.registry;

import com.chimericdream.hopperxtreme.HopperXtremeMod;
import com.chimericdream.hopperxtreme.ModInfo;
import com.chimericdream.hopperxtreme.block.Hoppers;
import com.chimericdream.hopperxtreme.item.ModItems;
import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class ModRegistries {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ModInfo.MOD_ID, (RegistryKey<Registry<Block>>) Registries.BLOCK.getKey());
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ModInfo.MOD_ID, (RegistryKey<Registry<Item>>) Registries.ITEM.getKey());
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ModInfo.MOD_ID, (RegistryKey<Registry<BlockEntityType<?>>>) Registries.BLOCK_ENTITY_TYPE.getKey());

    public static void init() {
        Hoppers.init();
        ModItems.init();

        HopperXtremeMod.LOGGER.debug("[hopperxtreme] registering blocks");
        BLOCKS.register();

        HopperXtremeMod.LOGGER.debug("[hopperxtreme] registering items");
        ITEMS.register();
    }

    public static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntity(final String name, final Supplier<T> supplier) {
        Registrar<BlockEntityType<?>> registrar = BLOCK_ENTITY_TYPES.getRegistrar();

        return registrar.register(Identifier.of(ModInfo.MOD_ID, name), supplier);
    }

    public static RegistrySupplier<Block> registerWithItem(String name, Supplier<Block> supplier) {
        return registerWithItem(name, supplier, new Item.Settings());
    }

    public static RegistrySupplier<Block> registerWithItem(String name, Supplier<Block> supplier, Item.Settings itemSettings) {
        RegistrySupplier<Block> block = registerBlock(Identifier.of(ModInfo.MOD_ID, name), supplier);

        registerItem(Identifier.of(ModInfo.MOD_ID, name), () -> new BlockItem(block.get(), itemSettings));

        return block;
    }

    public static <T extends Block> RegistrySupplier<T> registerBlock(Identifier path, Supplier<T> block) {
        Registrar<Block> registrar = BLOCKS.getRegistrar();

        if (Platform.isNeoForge()) {
            return BLOCKS.register(path.getPath(), block);
        }

        return registrar.register(path, block);
    }

    public static <T extends Item> RegistrySupplier<T> registerItem(Identifier path, Supplier<T> itemSupplier) {
        Registrar<Item> registrar = ITEMS.getRegistrar();

        if (Platform.isNeoForge()) {
            return ITEMS.register(path.getPath(), itemSupplier);
        }

        return registrar.register(path, itemSupplier);
    }
}
