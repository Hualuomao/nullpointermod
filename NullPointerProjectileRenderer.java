package com.example.nullpointermod;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class NullPointerProjectileRenderer extends EntityRenderer<NullPointerProjectile> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(NullPointerMod.MODID, "textures/entity/null_pointer_projectile.png");

    public NullPointerProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(NullPointerProjectile entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        // 使贴图始终面向相机
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        // 修正旋转，使纹理正面朝向玩家
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

        PoseStack.Pose pose = poseStack.last();
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(TEXTURE));

        float size = 0.5f;
        float half = size / 2.0f;
        // 添加 overlay 坐标（无覆盖）
        int overlay = OverlayTexture.NO_OVERLAY;

        // 逆时针顺序：左下 -> 右下 -> 右上 -> 左上
        vertexConsumer.vertex(pose.pose(), -half, -half, 0.0f)
                .color(255, 255, 255, 255)
                .uv(0.0f, 1.0f)
                .overlayCoords(overlay)
                .uv2(packedLight)
                .normal(0.0f, 0.0f, 1.0f)
                .endVertex();

        vertexConsumer.vertex(pose.pose(), half, -half, 0.0f)
                .color(255, 255, 255, 255)
                .uv(1.0f, 1.0f)
                .overlayCoords(overlay)
                .uv2(packedLight)
                .normal(0.0f, 0.0f, 1.0f)
                .endVertex();

        vertexConsumer.vertex(pose.pose(), half, half, 0.0f)
                .color(255, 255, 255, 255)
                .uv(1.0f, 0.0f)
                .overlayCoords(overlay)
                .uv2(packedLight)
                .normal(0.0f, 0.0f, 1.0f)
                .endVertex();

        vertexConsumer.vertex(pose.pose(), -half, half, 0.0f)
                .color(255, 255, 255, 255)
                .uv(0.0f, 0.0f)
                .overlayCoords(overlay)
                .uv2(packedLight)
                .normal(0.0f, 0.0f, 1.0f)
                .endVertex();

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(NullPointerProjectile entity) {
        return TEXTURE;
    }
}