package name.justhammersautopickup.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pro.mikey.justhammers.HammerItem;

@Mixin(HammerItem.class)
public class HammerItemMixin {

    @Redirect(
        method = "findAndBreakNearBlocks",
        at = @At(
            value = "NEW",
            target = "net/minecraft/entity/ItemEntity"
        )
    )
    private ItemEntity redirectItemEntity(
            net.minecraft.world.World world,
            double x, double y, double z,
            ItemStack stack,
            net.minecraft.world.World level,
            net.minecraft.util.math.BlockPos pos,
            net.minecraft.block.BlockState state,
            net.minecraft.item.ItemStack hammer,
            net.minecraft.entity.LivingEntity livingEntity
    ) {
        if (world.isClient()) {
            return new ItemEntity(world, x, y, z, stack);
        }

        if (!(livingEntity instanceof ServerPlayerEntity player)) {
            return new ItemEntity(world, x, y, z, stack);
        }

        boolean added = player.getInventory().insertStack(stack);

        if (added) {
            return null;
        }

        return new ItemEntity(world, x, y, z, stack);
    }
}