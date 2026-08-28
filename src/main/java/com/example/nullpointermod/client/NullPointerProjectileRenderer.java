package com.example.nullpointermod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import com.example.nullpointermod.entity.NullPointerProjectile;
import com.example.nullpointermod.NullPointerMod;

/**
 * Renderer for NullPointerProjectile
 * Renders as item texture
 */
public class NullPointerProjectileRenderer extends EntityRenderer<NullPointerProjectile> {

    public NullPointerProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
    }

    @Override
    public ResourceLocation getTextureLocation(NullPointerProjectile entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }

    @Override
    public void render(NullPointerProjectile entity, float entityYaw, float partialTick, PoseStack poseStack,
                      net.minecraft.client.renderer.MultiBufferSource bufferSource, int packedLight) {
        
        poseStack.pushPose();
        
        // Translate to entity position
        poseStack.translate(0.0D, 0.0D, 0.0D);
        
        // Rotate based on velocity
        if (entity.getDeltaMovement().length() > 0.0D) {
            double deltaX = entity.getDeltaMovement().x;
            double deltaY = entity.getDeltaMovement().y;
            double deltaZ = entity.getDeltaMovement().z;
            
            float yaw = (float) Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0F;
            float pitch = (float) Math.toDegrees(Math.atan2(deltaY, Math.sqrt(deltaX * deltaX + deltaZ * deltaZ)));
            
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(yaw));
            poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));
        }
        
        // Scale and render
        poseStack.scale(0.5F, 0.5F, 0.5F);
        
        // Get item texture
        TextureAtlasSprite sprite = this.itemRenderer.getItemModelShaper()
            .getParticleIcon(com.example.nullpointermod.item.ModItems.NULL_POINTER_ITEM.get().getDefaultInstance());
        
        // Render the item as a sprite
        int light = net.minecraft.client.renderer.LightTexture.pack(15, 15);
        net.minecraft.client.renderer.texture.TextureAtlasSprite finalSprite = sprite;
        var vertexConsumer = bufferSource.getBuffer(net.minecraft.client.renderer.RenderType.entityCutout(this.getTextureLocation(entity)));
        
        poseStack.pushPose();
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(180.0F));
        
        // Draw quad (simplified, actual implementation may vary)
        // This is a basic placeholder
        
        poseStack.popPose();
        poseStack.popPose();
        
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
