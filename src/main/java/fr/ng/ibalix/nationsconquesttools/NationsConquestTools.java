package main.java.fr.ng.ibalix.nationsconquesttools;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import java.io.File;
import java.io.IOException;

public class NationsConquestTools extends JavaPlugin {

    public Permission permission;
    public Economy econ = null;
    public FileConfiguration kitsCooldownConfig = null;
    public File kitsCooldownFile = null;

    @Override
    public void onEnable() {
        getLogger().info("Plugin NationsConquestTools activé");

        if(!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        setupPermissions();
        setupEconomy();

        // Génération config
        this.saveDefaultConfig();

        // Load data config
        this.kitsCooldownFile = new File(this.getDataFolder(), "kitsCooldown.yml");
        if (!kitsCooldownFile.exists()) {
            try {
                kitsCooldownFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        this.kitsCooldownConfig = YamlConfiguration.loadConfiguration(kitsCooldownFile);

        // Initialisation des Commandes
        this.getCommand("kitelite").setExecutor(new NationsConquestToolsCommandExecutor(this));
    }

    // Activation du plugin
    @Override
    public void onDisable() {
        getLogger().info("Plugin NationsConquestTools désactivé");
    }

    private void setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permission = rsp.getProvider();
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();
    }
}
