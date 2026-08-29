package com.example.nullpointermod.client;

import com.example.nullpointermod.NullPointerMod;
import com.example.nullpointermod.entity.NullPointerProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class NullPointerProjectileRenderer extends EntityRenderer<NullPointerProjectile> {
    private final ItemRenderer itemRenderer;
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(NullPointerMod.MOD_ID, "textures/entity/null_pointer.png");

    public NullPointerProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(NullPointerProjectile entity, float yaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        ItemStack stack = new ItemStack(NullPointerMod.NULL_POINTER_ITEM.get());
        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight,
                OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), 0);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(NullPointerProjectile entity) {
        return TEXTURE;
    }
}
