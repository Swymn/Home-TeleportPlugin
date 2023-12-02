package fr.swynn.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private static final int SPAWN_X = -41;
    private static final int SPAWN_Y = 160;
    private static final int SPAWN_Z = 97;

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

        if (!(sender instanceof Player player)) {
            return false;
        }

        final var world = Bukkit.getWorld("world");
        final var location = new Location(world, SPAWN_X, SPAWN_Y, SPAWN_Z);

        player.teleport(location);
        return true;
    }
}
