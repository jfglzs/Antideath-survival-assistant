package io.github.jfglzs.asa.mixin.feature.optimizations.fastModelRender;
//? if >= 26.2 {
//import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
//import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.VertexSorting;
//import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
//import io.github.jfglzs.asa.config.Configs;
//import net.minecraft.client.renderer.StagedVertexBuffer;
//import net.minecraft.client.renderer.rendertype.PreparedRenderType;
//import net.minecraft.client.renderer.rendertype.RenderType;
//import org.spongepowered.asm.mixin.*;
//
//import java.util.List;
//
//@Mixin(targets = "net.minecraft.client.renderer.feature.RenderTypeFeatureRenderer$Group")
//public class Group_Mixin {
//    @Shadow
//    @Final
//    private boolean canReorder;
//    @Shadow
//    @Final
//    private StagedVertexBuffer stagedBuffer;
//    @Mutable
//    @Shadow
//    @Final
//    private List<StagedVertexBuffer.Draw> draws;
//    @Mutable
//    @Shadow
//    @Final
//    private List<PreparedRenderType> drawRenderTypes;
//    @Unique public final Object2ObjectOpenHashMap<PreparedRenderType, StagedVertexBuffer.Draw> FMR$map = new Object2ObjectOpenHashMap<>();
//
//    @WrapMethod(
//            method = "getOrAddDraw"
//    )
//    private StagedVertexBuffer.Draw getOrAddDraw(RenderType renderType, Operation<StagedVertexBuffer.Draw> original) {
//        if (!Configs.Optimizations.FAST_MODEL_RENDER.getBooleanValue())
//            return original.call(renderType);
//        PreparedRenderType preparedRenderType = renderType.prepare();
//        if (this.canReorder && renderType.canConsolidateConsecutiveGeometry()) {
//            StagedVertexBuffer.Draw result = this.FMR$map.get(preparedRenderType);
//            if (result != null)
//                return result;
//        }
//        VertexSorting quadSorting = renderType.sortOnUpload() ? RenderSystem.getProjectionType().vertexSorting() : null;
//        StagedVertexBuffer.Draw draw = this.stagedBuffer.appendDraw(renderType.format(), renderType.primitiveTopology(), quadSorting);
//        this.FMR$map.putIfAbsent(preparedRenderType, draw);
//        this.drawRenderTypes.add(preparedRenderType);
//        this.draws.add(draw);
//        return draw;
//    }
//}
//?} else {
@org.spongepowered.asm.mixin.Mixin(io.github.jfglzs.asa.utils.DummyClass.class)
public class Group_Mixin {
}
//?}

