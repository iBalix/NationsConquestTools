package main.java.fr.ng.ibalix.nationsconquesttools;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class NationsConquestToolsCommandExecutor implements CommandExecutor {

    private final NationsConquestTools p;

    public NationsConquestToolsCommandExecutor(NationsConquestTools p) {
        this.p = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		/*
		 * Commande upgrade
		 * Check si le joueur doit upgrade
		 */
        if (cmd.getName().equalsIgnoreCase("kitelite")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if(sender.hasPermission("nationsconquest.elite")) {
                    if(p.kitsCooldownConfig.contains(player.getName())) {
                        Date date = new Date();
                        long time = date.getTime();

                        long diff = time - p.kitsCooldownConfig.getLong(player.getName());

                        if(diff < 259200000) {
                            long delay = 259200000 - diff;
                            int minutes = (int) ((delay / (1000*60)) % 60);
                            int hours   = (int) ((delay / (1000*60*60)));
                            player.sendMessage(ChatColor.RED + "Vous devez encore patienter "+hours+" heure(s) et "+minutes+" minute(s) avant de pouvoir obtenir le kit !");
                            return true;
                        }
                    }

                    // Set time for cooldown
                    Date date = new Date();
                    long time = date.getTime();
                    //p.kitsCooldownConfig.set(player.getName(), time);

                    HashMap<String, Integer> metiers = getMetiers(player);

                    ArrayList<String> items = new ArrayList<String>();

                    Iterator it = metiers.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();

                        for(int i = 1; i <= (int)pair.getValue(); i++){
                            Collection itemsLevel = p.getConfig().getList("metiers."+pair.getValue().toString()+"."+i);

                            items.addAll(itemsLevel);
                        }

                        p.getLogger().info(items.toString());

                        //score.put(pair.getKey().toString(), Integer.parseInt(((HashMap)pair.getValue()).get("kills").toString()));

                    }

                } else {
                    player.sendMessage(ChatColor.RED + "Seuls les elites ont accès à cette commande...");
                    return true;
                }
            } else {
                p.getLogger().info(ChatColor.RED + "La console ne peut pas executer cette commande...");
                return true;
            }
        }
        return false;
    }

    private HashMap<String, Integer> getMetiers(Player player) {
        Set<String> metiers = p.getConfig().getConfigurationSection("metiers").getKeys(false);

        HashMap<String, Integer> res = new  HashMap<String, Integer>();

        for(String metier : metiers) {
            if(player.hasPermission("levelmetiers."+metier)) {
                int level = getLevel(player, metier);
                res.put(metier, level);
            }
        }
        return res;
    }

    private int getLevel(Player player, String metier) {
        Set<String> levels = p.getConfig().getConfigurationSection("metiers."+metier).getKeys(false);

        List list = new ArrayList(levels);
        Collections.sort(list, Collections.reverseOrder());
        Set<String> levelsInverse = new LinkedHashSet(list);

        for(String level : levelsInverse) {
            if(player.hasPermission("levelmetiers."+metier+"."+level)) {
                int levelInt = Integer.parseInt(level);
                return levelInt;
            }
        }
        return 1;
    }
}
