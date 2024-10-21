package ab.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class ModelFountainMana<PoseStack, VertexConsumer> extends ModelBase {

    private final ModelRenderer bone;
    private final ModelRenderer bb_main;

    public ModelFountainMana() {
        textureWidth = 16;
        textureHeight = 16;

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 0, 0, -1.0F, -11.0F, 2.0F, 2, 1, 1, 0.0F));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -1.0F, -11.0F, -3.0F, 2, 1, 1, 0.0F));
        bone.cubeList.add(new ModelBox(bone, 0, 0, 2.0F, -11.0F, -1.0F, 1, 1, 2, 0.0F));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -3.0F, -11.0F, -1.0F, 1, 1, 2, 0.0F));
        bone.cubeList.add(new ModelBox(bone, 0, 0, 1.0F, -12.0F, 1.0F, 1, 2, 2, 0.0F));
        bone.cubeList.add(new ModelBox(bone, 0, 0, 2.0F, -12.0F, 1.0F, 1, 2, 1, 0.0F));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -2.0F, -12.0F, 1.0F, 1, 2, 2, 0.0F));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -2.0F, -12.0F, -3.0F, 1, 2, 2, 0.0F));
        bone.cubeList.add(new ModelBox(bone, 0, 0, 1.0F, -12.0F, -3.0F, 1, 2, 2, 0.0F));
        bone.cubeList.add(new ModelBox(bone, 0, 0, 2.0F, -12.0F, -2.0F, 1, 2, 1, 0.0F));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -3.0F, -12.0F, 1.0F, 1, 2, 1, 0.0F));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -3.0F, -12.0F, -2.0F, 1, 2, 1, 0.0F));
        bone.cubeList.add(new ModelBox(bone, 0, 0, 2.0F, -11.0F, -2.0F, 4, 3, 4, 0.0F));
        // PartPose.offset(0.0F, 24.0F, 0.0F);

        bb_main = new ModelRenderer(this);
        bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 3, -7.0F, -1.0F, -6.0F, 14, 1, 12, 0.0F));
        bb_main.cubeList.add(new ModelBox(bb_main, -10, -11, -8.0F, -5.0F, -6.0F, 1, 5, 12, 0.0F));
        bb_main.cubeList.add(new ModelBox(bb_main, -11, -11, 7.0F, -5.0F, -6.0F, 1, 5, 12, 0.0F));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -6.0F, -5.0F, -8.0F, 12, 5, 1, 0.0F));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -6.0F, -5.0F, 7.0F, 12, 5, 1, 0.0F));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, 6.0F, -5.0F, -7.0F, 1, 5, 1, 0.0F));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, 6.0F, -5.0F, 6.0F, 1, 5, 1, 0.0F));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -7.0F, -5.0F, 6.0F, 1, 5, 1, 0.0F));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -7.0F, -5.0F, -7.0F, 1, 5, 1, 0.0F));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -6.0F, -1.0F, 6.0F, 12, 1, 1, 0.0F));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -6.0F, -1.0F, -7.0F, 12, 1, 1, 0.0F));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -2.0F, -8.0F, -2.0F, 4, 7, 4, 0.0F));

    }

    public void renderTop() {
        bone.render(0.0625f);
    }

    public void renderBottom() {
        bb_main.render(0.0625f);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
            float red, float green, float blue, float alpha) {
        bone.render(packedLight);
        bb_main.render(packedLight);
    }
}
