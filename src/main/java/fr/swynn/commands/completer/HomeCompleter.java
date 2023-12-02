package fr.swynn.commands.completer;

import fr.swynn.HomeAndTPA;
import fr.swynn.core.data.HomeService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomeCompleter implements TabCompleter {

    // Home service
    private static final HomeService SERVICE = HomeAndTPA.getInstance().getHomeService();

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {

        final List<String> completions = new ArrayList<>();

        if (!(sender instanceof Player player)) {
            return completions;
        }


        if (args.length == 1) {
            final String[] playerHomes = SERVICE.getPlayerHomeNames(player.getUniqueId()).toArray(new String[0]);
            for (final String home : playerHomes) {
                if (home.startsWith(args[0])) {
                    completions.add(home);
                }
            }
        }

        return completions;
    }
}
