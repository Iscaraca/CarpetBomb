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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Carpet bomb command active!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
	if (cmd.getName().equalsIgnoreCase("carpetbomb")) { 
        if (args.length < 2) { // Not enough arguments
            sender.sendMessage("The command is /carpetbomb <player-name> <number-of-tnts>");
        } else {
            new BukkitRunnable() { // BukkitRunnable for a delayed synchronous repeated execution
                private int counter = Integer.parseInt(args[1]); // Counter to disable the repeat

                World myWorld = Bukkit.getWorlds().get(0);
                Player Target = Bukkit.getPlayer(args[0]);  

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
                Location locOne = Target.getLocation().add(1 + x * 7, 20,  z * 7); 
                Location locTwo = Target.getLocation().add(x * 7, 20, 1 + z * 7);
                Location locThree = Target.getLocation().add(x * 7, 20, -1 + z * 7);
                Location locFour = Target.getLocation().add(-1  + x * 7, 20,  z * 7);

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
                    tntOne.setFuseTicks(100);
                    TNTPrimed tntTwo = (TNTPrimed) myWorld.spawnEntity(locTwo, EntityType.PRIMED_TNT);
                    tntTwo.setFuseTicks(100);
                    TNTPrimed tntThree = (TNTPrimed) myWorld.spawnEntity(locThree, EntityType.PRIMED_TNT);
                    tntThree.setFuseTicks(100);
                    TNTPrimed tntFour = (TNTPrimed) myWorld.spawnEntity(locFour, EntityType.PRIMED_TNT);
                    tntFour.setFuseTicks(100);

                    if (--counter == 0) {
                        cancel();
                    }
                }
            }.runTaskTimer(this, 0L, 2L); // Second parameter is initial delay, third one is delay between cycles. Feel free to change these up.
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
