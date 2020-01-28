package com.fleynaro.advancedkits;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.SignChangeEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.utils.TextFormat;

class EventListener implements Listener {

    private Main ak;

    public EventListener(Main ak) {
        this.ak = ak;
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        int id = event.getBlock().getId();
        Player player = event.getPlayer();
        if (id == Block.SIGN_POST || id == Block.WALL_SIGN) {
            BlockEntity tile = player.getLevel().getBlockEntity(event.getBlock().getLocation());
            if (tile instanceof BlockEntitySign) {
                String[] text = ((BlockEntitySign) tile).getText();
                if (text[0] != null && TextFormat.clean(text[0]).toLowerCase().equals(this.ak.getConfig().getString("sign-text").toLowerCase())) {
                    event.setCancelled();
                    if (text[1] == null || text[1].isEmpty()) {
                        event.getPlayer().sendMessage(this.ak.langManager.getTranslation("no-sign-on-kit"));
                        return;
                    }
                    Kit kit = this.ak.getKit(text[1]);
                    if (kit == null) {
                        player.sendMessage(this.ak.langManager.getTranslation("no-kit", new String[] { text[1] }));
                        return;
                    }

                    kit.handleRequest(player);
                }
            }
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (TextFormat.clean(event.getLine(0)).toLowerCase().equals(this.ak.getConfig().getString("sign-text").toLowerCase()) && !event.getPlayer().hasPermission("advancedkits.admin")) {
            event.getPlayer().sendMessage(this.ak.langManager.getTranslation("no-perm-sign"));
            event.setCancelled();
        }
    }
}