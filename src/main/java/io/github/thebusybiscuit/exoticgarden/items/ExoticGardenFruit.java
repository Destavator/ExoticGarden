package io.github.thebusybiscuit.exoticgarden.items;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.HandledBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.ItemUtils;

public class ExoticGardenFruit extends HandledBlock {

    private final boolean edible;

    public ExoticGardenFruit(Category category, SlimefunItemStack item, RecipeType recipeType, boolean edible, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        this.edible = edible;
    }

    @Override
    public void preRegister() {
        addItemHandler(onRightClick());
        super.preRegister();
    }

    public ItemUseHandler onRightClick() {
        return e -> {
            Optional<Block> block = e.getClickedBlock();

            if (block.isPresent()) {
                Material material = block.get().getType();

                // Cancel the Block placement if the Player sneaks or the Block is not interactable
                if (!material.isInteractable() || e.getPlayer().isSneaking()) {
                    e.cancel();
                }
                else {
                    return;
                }
            }

            if (edible && e.getPlayer().getFoodLevel() < 20) {
                restoreHunger(e.getPlayer());
                ItemUtils.consumeItem(e.getItem(), false);
            }
        };
    }

    protected int getFoodValue() {
        return 2;
    }

    private void restoreHunger(Player p) {
        int level = p.getFoodLevel() + getFoodValue();
        p.playSound(p.getEyeLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
        p.setFoodLevel(Math.min(level, 20));
        p.setSaturation(p.getSaturation() + getFoodValue());
    }

}
