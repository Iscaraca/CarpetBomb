package carpetbomb;

import java.lang.Thread;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Location;

import org.bukkit.scheduler.BukkitRunnable;

import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.EntityType;
import org.bukkit.Material;

import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class App extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getLogger().info("Carpet bomb command active!");
        getServer().getPluginManager().registerEvents(this, this);
    }

    /**
     * @param playerName String denoting the name of the player who triggered the event
     * @param cycles Number of cycles of 4 TNT
     * @param heightOfDrop Height at which TNT spawns, relative to the location of the player
     * @param tntFuseTime Number of ticks for the bomb to go off
     */
    public void carpetBomb(final String playerName, final int cycles, final int heightOfDrop, final int tntFuseTime) {

        new BukkitRunnable() { // BukkitRunnable for a delayed synchronous repeated execution
            private int counter = cycles; // Counter to disable the repeat

            World myWorld = Bukkit.getWorlds().get(0);
            Player Target = Bukkit.getPlayer(playerName);

            // getDirection returns a unit vector in the direction the player is looking at
            // We can split the vector into its X and Z components to increment the location of spawn for the TNTs
            double x = Target.getLocation().getDirection().getX();
            double z = Target.getLocation().getDirection().getZ();

            /*
             * Multiply by an initial 7 to all X and Z values because the first few times
             * I tried without it I blew myself up.
             * The 1s in certain areas are so that the TNT spawns 4 times per cycle
             * in a cross, for maximum destruction which was the whole goal of this
             * plugin so sue me.
             */
            Location locOne = Target.getLocation().add(1 + x * 7, heightOfDrop, z * 7);
            Location locTwo = Target.getLocation().add(x * 7, heightOfDrop, 1 + z * 7);
            Location locThree = Target.getLocation().add(x * 7, heightOfDrop, -1 + z * 7);
            Location locFour = Target.getLocation().add(-1 + x * 7, heightOfDrop, z * 7);

            @Override
            public void run() {
                double x = Target.getLocation().getDirection().getX();
                double z = Target.getLocation().getDirection().getZ();

                // Multipliers just in case your intervals are way too large the TNT
                // gets out of your render distance
                x *= 0.9;
                z *= 0.9;

                // Make the carpet bomb follow the line of sight of the player, to allow
                // for steering midair
                locOne.add(x, 0, z);
                locTwo.add(x, 0, z);
                locThree.add(x, 0, z);
                locFour.add(x, 0, z);

                // Fuse ticks are in Minecraft ticks, not milliseconds. I thought this at first
                // and waited a long time for the charges to go off on my first test :(
                TNTPrimed tntOne = (TNTPrimed) myWorld.spawnEntity(locOne, EntityType.PRIMED_TNT);
                tntOne.setFuseTicks(tntFuseTime);
                TNTPrimed tntTwo = (TNTPrimed) myWorld.spawnEntity(locTwo, EntityType.PRIMED_TNT);
                tntTwo.setFuseTicks(tntFuseTime);
                TNTPrimed tntThree = (TNTPrimed) myWorld.spawnEntity(locThree, EntityType.PRIMED_TNT);
                tntThree.setFuseTicks(tntFuseTime);
                TNTPrimed tntFour = (TNTPrimed) myWorld.spawnEntity(locFour, EntityType.PRIMED_TNT);
                tntFour.setFuseTicks(tntFuseTime);

                if (--counter == 0) {
                    cancel();
                }
            }
        }.runTaskTimer(this, 0L, 2L); // Second parameter is initial delay, third one is delay between cycles. Feel free to change these up.
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = (Player) event.getPlayer();
        try {
            if (player.getEquipment().getItemInMainHand().getItemMeta().getDisplayName().replaceAll("\\s+","").equalsIgnoreCase("Boomstick")) { // Change this name to whatever
                if (player.getEquipment().getItemInMainHand().getType() == Material.BLAZE_ROD // Change the item type that triggers the bombs
                    && player.isOp()) {
                    String playerName = player.getPlayerListName();
                    getLogger().info(playerName + " used the boomstick!");
                    this.carpetBomb(playerName, 100, 10, 50);
                }
            } else {
                return;
            }
        } catch (NullPointerException exception) {
            return;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("carpetbomb")) {
            if (args.length < 4) { // Not enough arguments
                sender.sendMessage("The command is /carpetbomb <player name> <number of tnts> <height of drop> <tnt fuse timer>");
            } else {
                Player Target = Bukkit.getPlayer(args[0]);
                if (Target.isOp()) {
                    String playerName = args[0];
                    int cycles = Integer.parseInt(args[1]);
                    int heightOfDrop = Integer.parseInt(args[2]);
                    int tntFuseTime = Integer.parseInt(args[3]);

                    this.carpetBomb(playerName, cycles, heightOfDrop, tntFuseTime);
                } else {
                    sender.sendMessage("You do not have the required privileges for this command.");
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDisable() {
        getLogger().info("See ya.");
    }
}
