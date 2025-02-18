package shadows.placebo.patreon.wings;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

//Made with Blockbench 3.8.4
//Exported for Minecraft version 1.15 - 1.16
//Paste this class into your mod and generate all required imports

public class Wing extends EntityModel<AbstractClientPlayerEntity> implements IWingModel {

	public static final Wing INSTANCE = new Wing();

	private final ModelRenderer bb_main;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;

	public Wing() {
		textureWidth = 32;
		textureHeight = 32;

		bb_main = new ModelRenderer(this);
		bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(0.0F, -8.0F, 0.0F);
		bb_main.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.3491F, 0.0F);
		cube_r1.setTextureOffset(0, 0).addBox(1.0F, -8.0F, 0.0F, 16.0F, 16.0F, 0.0F, 0.001F, false);

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(0.0F, -8.0F, 0.0F);
		bb_main.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, -0.3491F, 0.0F);
		cube_r2.setTextureOffset(0, 0).addBox(1.0F, -8.0F, 0.0F, 16.0F, 16.0F, 0.0F, 0.001F, false);
	}

	@Override
	public void setRotationAngles(AbstractClientPlayerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	@Override
	public void render(MatrixStack stack, IRenderTypeBuffer buf, int packedLightIn, AbstractClientPlayerEntity player, float partialTicks, ResourceLocation texture, PlayerModel<AbstractClientPlayerEntity> model) {
		if (player.isInvisible()) return;
		stack.translate(0, 0, 0.065);
		if (player.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() instanceof ArmorItem) stack.translate(0, 0, 0.075);
		stack.rotate(Vector3f.YN.rotationDegrees(90));
		float rotationTime = player.ticksExisted % 40 + partialTicks;
		this.setRotationAngle(this.cube_r1, 0, 0.3491F * 1.5F + 0.3491F / 2 * (float) Math.sin(Math.PI * rotationTime / 20), 0);
		this.setRotationAngle(this.cube_r2, 0, -(0.3491F * 1.5F + 0.3491F / 2 * (float) Math.sin(Math.PI * rotationTime / 20)), 0);
		this.render(stack, buf.getBuffer(RenderType.getEntityTranslucent(texture)), packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}