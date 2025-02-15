package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.BlockUtils;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.math.BlockPos;

public class AnchorSearch extends Module implements Listener {

    public AnchorSearch() {
        super("AnchorSearch", Categories.ANCHORING,"Searches your hotbar for anchors when you right click an end crystal on a non-crystallable block.");
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }

    @EventHandler
    private void onClickBlock(PacketSendEvent e) {
        if (e.getPacket() instanceof PlayerInteractBlockC2SPacket packet) {
            BlockPos pos = packet.getBlockHitResult().getBlockPos();
            if (BlockUtils.isCrystallabe(pos)) return;
            if (!HotbarUtils.nameContains("crystal")) return;
            if (!HotbarUtils.has(Items.RESPAWN_ANCHOR)) return;
            e.setCancelled(true);
            HotbarUtils.search(Items.RESPAWN_ANCHOR);
            BlockUtils.interact(packet.getBlockHitResult());
            HotbarUtils.search(Items.GLOWSTONE);
        }
    }
}
