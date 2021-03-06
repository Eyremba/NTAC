package net.newtownia.NTAC.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PlayerUtils
{
    private static final double GROUND_THRESHOLD = 0.001;

    public static boolean isPlayerOnGround(Player p)
    {
        return isPlayerOnGroundNTAC(p);
    }

    public static boolean isLocationOnGroundNTAC(Location loc)
    {
        List<Material> materials = getMaterialsAround(loc.clone().add(0, -GROUND_THRESHOLD, 0));
        for (Material m : materials)
            if (!MaterialUtils.isUnsolid(m) && m != Material.WATER && m != Material.STATIONARY_WATER &&
                    m != Material.LAVA && m != Material.STATIONARY_LAVA)
                return true;
        return false;
    }

    public static boolean isPlayerOnGroundNTAC(Player p)
    {
        return isLocationOnGroundNTAC(p.getLocation());
    }

    public static boolean isPlayerOnGroundNTACOld(Player p)
    {
        List<Material> materials = getMaterialsBelowOld(p);
        for (Material m : materials)
            if (m != Material.AIR && !MaterialUtils.isUnsolid(m) &&
                    m != Material.WATER && m != Material.STATIONARY_WATER &&
                    m != Material.LAVA && m != Material.STATIONARY_LAVA)
                return true;
        return false;
    }

    public static Location getPlayerStandOnBlockLocationstair(Location locationUnderPlayer) {
        Location b11 = locationUnderPlayer.clone().add(0.3, 0, -0.3);
        if (b11.getBlock().getType().name().contains("STAIR")) {
            return b11;
        }
        Location b12 = locationUnderPlayer.clone().add(-0.3, 0, -0.3);
        if (b12.getBlock().getType().name().contains("STAIR")) {
            return b12;
        }
        Location b21 = locationUnderPlayer.clone().add(0.3, 0, 0.3);
        if (b21.getBlock().getType().name().contains("STAIR")) {
            return b21;
        }
        Location b22 = locationUnderPlayer.clone().add(-0.3, 0, +0.3);
        if (b22.getBlock().getType().name().contains("STAIR")) {
            return b22;
        }
        return locationUnderPlayer;
    }

    public static boolean isOnStair(Player p) {
        Location loc = p.getLocation().subtract(0, GROUND_THRESHOLD, 0);
        if (getPlayerStandOnBlockLocationstair(loc).getBlock().getType().name().contains("STAIR")) {
            return true;
        }
        return false;
    }

    public static boolean isInWeb(Location loc)
    {
        return loc.getBlock().getType() == Material.WEB ||
                loc.getBlock().getRelative(BlockFace.UP).getType() == Material.WEB;
    }

    public static boolean isOnClimbable(Location loc)
    {
        return loc.getBlock().getType() == Material.LADDER || loc.getBlock().getType() == Material.VINE;
    }

    public static boolean isUnderBlock(Player p)
    {
        Block blockAbove = p.getEyeLocation().getBlock().getRelative(BlockFace.UP);
        return blockAbove != null && !MaterialUtils.isUnsolid(blockAbove);
    }

    public static boolean isOnIce(Player p, boolean strict)
    {
        if (isPlayerOnGround(p) || strict)
        {
            List<Material> materials = getMaterialsAround(p.getLocation().clone().add(0, -GROUND_THRESHOLD, 0));
            return materials.contains(Material.ICE) || materials.contains(Material.PACKED_ICE);
        }
        else
        {
            List<Material> m1 = getMaterialsAround(p.getLocation().clone().add(0, -1, 0));
            List<Material> m2 = getMaterialsAround(p.getLocation().clone().add(0, -2, 0));
            return m1.contains(Material.ICE) || m1.contains(Material.PACKED_ICE) ||
                    m2.contains(Material.ICE) || m2.contains(Material.PACKED_ICE);
        }
    }

    public static boolean isOnSteps(Player p)
    {
        List<Material> materials = getMaterialsAround(p.getLocation().clone().add(0, -GROUND_THRESHOLD, 0));
        for (Material m : materials)
            if (MaterialUtils.isStepable(m))
                return true;
        return false;
    }

    public static Location getPlayerStandOnBlockLocation(Location locationUnderPlayer, Material mat) {
        Location b11 = locationUnderPlayer.clone().add(0.3, 0, -0.3);
        if (b11.getBlock().getType() != mat) {
            return b11;
        }
        Location b12 = locationUnderPlayer.clone().add(-0.3, 0, -0.3);
        if (b12.getBlock().getType() != mat) {
            return b12;
        }
        Location b21 = locationUnderPlayer.clone().add(0.3, 0, 0.3);
        if (b21.getBlock().getType() != mat) {
            return b21;
        }
        Location b22 = locationUnderPlayer.clone().add(-0.3, 0, +0.3);
        if (b22.getBlock().getType() != mat) {
            return b22;
        }
        return locationUnderPlayer;
    }

    public static boolean isInWater(Player p) {
        Location loc = p.getLocation().subtract(0, 0.2, 0);
        return getPlayerStandOnBlockLocation(loc, Material.STATIONARY_WATER).getBlock().getType() == Material.STATIONARY_WATER
                || getPlayerStandOnBlockLocation(loc, Material.WATER).getBlock().getType() == Material.WATER;
    }

    public static boolean isInBlock(Player p, Material block) {
        Location loc = p.getLocation().add(0, 0, 0);
        return getPlayerStandOnBlockLocation(loc, block).getBlock().getType() == block;
    }

    public static boolean isOnWater(Player p) {
        Location loc = p.getLocation().subtract(0, 1, 0);
        return getPlayerStandOnBlockLocation(loc, Material.STATIONARY_WATER).getBlock().getType() == Material.STATIONARY_WATER;
    }

    public static boolean isOnBlock(Player p, Material mat) {
        Location loc = p.getLocation().subtract(0, 1, 0);
        return getPlayerStandOnBlockLocation(loc, mat).getBlock().getType() == mat;
    }

    public static List<Material> getMaterialsAround(Location loc)
    {
        List<Material> result = new ArrayList<>();
        result.add(loc.getBlock().getType());
        result.add(loc.clone().add(0.3, 0, -0.3).getBlock().getType());
        result.add(loc.clone().add(-0.3, 0, -0.3).getBlock().getType());
        result.add(loc.clone().add(0.3, 0, 0.3).getBlock().getType());
        result.add(loc.clone().add(-0.3, 0, 0.3).getBlock().getType());
        return result;
    }

    public static List<Material> getMaterialsBelowOld(Player p)
    {
        return getMaterialsBelowOld(p.getLocation());
    }

    public static List<Material> getMaterialsBelowOld(Location loc)
    {
        Block blockDown = loc.getBlock().getRelative(BlockFace.DOWN);

        ArrayList<Material> materials = new ArrayList<>();
        materials.add(blockDown.getType());
        materials.add(blockDown.getRelative(BlockFace.NORTH).getType());
        materials.add(blockDown.getRelative(BlockFace.NORTH_EAST).getType());
        materials.add(blockDown.getRelative(BlockFace.EAST).getType());
        materials.add(blockDown.getRelative(BlockFace.SOUTH_EAST).getType());
        materials.add(blockDown.getRelative(BlockFace.SOUTH).getType());
        materials.add(blockDown.getRelative(BlockFace.SOUTH_WEST).getType());
        materials.add(blockDown.getRelative(BlockFace.WEST).getType());
        materials.add(blockDown.getRelative(BlockFace.NORTH_WEST).getType());

        return materials;
    }

    public static boolean materialsBelowContains(Player p, Material m)
    {
        return getMaterialsBelowOld(p).contains(m);
    }

    public static boolean isGlidingWithElytra(Player p)
    {
        ItemStack chestplate = p.getInventory().getChestplate();
        return p.isGliding() && chestplate != null && chestplate.getType() == Material.ELYTRA;
    }

    public static boolean isOnEntity(Player p, EntityType type)
    {
        for (Entity e : p.getWorld().getNearbyEntities(p.getLocation(), 1, 1, 1))
            if (e.getType() == type && e.getLocation().getY() < p.getLocation().getY())
                return true;
        return false;
    }

    public static PotionEffect getPotionEffect(Player p, PotionEffectType type)
    {
        PotionEffect effect = null;
        for (PotionEffect tmp : p.getActivePotionEffects())
        {
            if (tmp.getType() == type)
            {
                effect = tmp;
                break;
            }
        }
        return effect;
    }

    public static int getPing(Player p)
    {
        //Get version number
        String bpName = Bukkit.getServer().getClass().getPackage().getName();
        String version = bpName.substring(bpName.lastIndexOf(".") + 1, bpName.length());

        try
        {
            //Get craft player
            Class<?> CPClass = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
            Object craftPlayer = CPClass.cast(p);

            //Get EntityPlayer
            Method getHandle = craftPlayer.getClass().getMethod("getHandle");
            Object EntityPlayer = getHandle.invoke(craftPlayer);

            //Get Ping-Field
            Field ping = EntityPlayer.getClass().getDeclaredField("ping");

            //Return value of the Ping-Field
            return ping.getInt(EntityPlayer);
        }
        catch (Exception e) { }

        return 100;
    }
}
