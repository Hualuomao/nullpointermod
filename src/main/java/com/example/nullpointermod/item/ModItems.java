package com.example.nullpointermod.item;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.Item;
import com.example.nullpointermod.NullPointerMod;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NullPointerMod.MOD_ID);

    public static final RegistryObject<Item> JAVA_ITEM = ITEMS.register("java_item",
            () -> new JavaItem(new Item.Properties()));

    public static final RegistryObject<Item> NULL_POINTER_ITEM = ITEMS.register("java_null_pointer_exception",
            () -> new NullPointerItem(new Item.Properties()));
}
