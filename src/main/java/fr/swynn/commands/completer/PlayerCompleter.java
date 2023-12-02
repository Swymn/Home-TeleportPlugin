package fr.swynn.commands.completer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {

        List<String> completions = new ArrayList<>();

        if (!(sender instanceof Player)) return completions;

        if (args.length == 1) {
            for (final Player onlinePlayer : sender.getServer().getOnlinePlayers()) {
                if (onlinePlayer.getName().startsWith(args[0])) {
                    completions.add(onlinePlayer.getName());
                }
            }
        }

        return completions;
    }
}
