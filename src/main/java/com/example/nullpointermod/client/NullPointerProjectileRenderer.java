package com.example.nullpointermod.client;

import com.example.nullpointermod.NullPointerMod;
import com.example.nullpointermod.entity.NullPointerProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;

public class NullPointerProjectileRenderer extends ThrownItemRenderer<NullPointerProjectile> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(NullPointerMod.MOD_ID, "textures/entity/null_pointer.png");

    public NullPointerProjectileRenderer(EntityRendererProvider.Context context) {
        super(context, NullPointerMod.NULL_POINTER_ITEM.get());
    }

    @Override
    public ResourceLocation getTextureLocation(NullPointerProjectile entity) {
        return TEXTURE;
    }
}
