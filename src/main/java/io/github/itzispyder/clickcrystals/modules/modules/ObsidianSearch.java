package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.BlockUtils;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;

/**
 * ObsidianSearch module
 */
public class ObsidianSearch extends Module implements Listener {

    private static long cooldown;

    public ObsidianSearch() {
        super("ObsidianSearch", Categories.CRYSTALLING,"Searches your hotbar for an obsidian when you left click a non-obsidian block with a crystal.");
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
    private void onPacketSend(PacketSendEvent e) {
        if (e.getPacket() instanceof PlayerActionC2SPacket packet) {
            if (cooldown > System.currentTimeMillis()) return;
            cooldown = System.currentTimeMillis() + (50 * 4);
            if (packet.getAction() != PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) return;
            BlockPos pos = packet.getPos();
            if (BlockUtils.isCrystallabe(pos)) return;
            if (!HotbarUtils.has(Items.END_CRYSTAL)) return;

            if (HotbarUtils.nameContains("crystal") || HotbarUtils.nameContains("totem") || HotbarUtils.nameContains("sword") || HotbarUtils.nameContains("anchor") || HotbarUtils.isHolding(Items.GLOWSTONE)) {
                e.setCancelled(true);
                HotbarUtils.search(Items.OBSIDIAN);
                BlockUtils.interact(pos,packet.getDirection());
                HotbarUtils.search(Items.END_CRYSTAL);
            }
        }
    }
}
