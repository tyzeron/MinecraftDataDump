package com.tyzeron.datadump;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.tyzeron.datadump.command.CommandHandler;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;

import static net.minecraft.commands.Commands.literal;


public class FabricDataDump implements ModInitializer {

    @Override
    public void onInitialize() {
        // Initialize platform-specific components
        PlatformHelper.setBlockDataProvider(new FabricBlockDataProvider());
        PlatformHelper.setRegistryDataProvider(new FabricRegistryDataProvider());
        PlatformHelper.setNbtWriter(new FabricNbtWriter());
        PlatformHelper.setGameDirectory(FabricLoader.getInstance().getGameDir());
        PlatformHelper.setConfigDirectory(FabricLoader.getInstance().getConfigDir());

        // Initialize command handler
        CommandHandler.initialize(PlatformHelper.getConfigDirectory());

        // Initialize the mod
        DataDump.init();

        // Register commands
        registerCommands();
    }

    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("datadump")
                .then(literal("run")
                    .requires(source -> source.hasPermission(2))
                    .then(net.minecraft.commands.Commands.argument("profile", StringArgumentType.string())
                        .suggests((context, builder) -> {
                            // Suggest available profiles
                            var profiles = CommandHandler.getConfigManager().listProfiles();
                            for (String profile : profiles) {
                                builder.suggest(profile);
                            }
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            String profileName = StringArgumentType.getString(context, "profile");
                            var registryProvider = (FabricRegistryDataProvider) PlatformHelper.getRegistryDataProvider();
                            registryProvider.setServer(context.getSource().getServer());
                            var result = CommandHandler.handleRun(profileName);
                            if (result.isSuccess()) {
                                context.getSource().sendSuccess(() -> 
                                    net.minecraft.network.chat.Component.literal(result.getMessage()), true);
                            } else {
                                context.getSource().sendFailure(
                                    net.minecraft.network.chat.Component.literal(result.getMessage()));
                            }
                            return result.isSuccess() ? 1 : 0;
                        })))
                .then(literal("list")
                    .executes(context -> {
                        var result = CommandHandler.handleList();
                        context.getSource().sendSuccess(() -> 
                            net.minecraft.network.chat.Component.literal(result.getMessage()), false);
                        return 1;
                    }))
                .then(literal("reset")
                    .requires(source -> source.hasPermission(2))
                    .executes(context -> {
                        var result = CommandHandler.handleReset();
                        if (result.isSuccess()) {
                            context.getSource().sendSuccess(() -> 
                                net.minecraft.network.chat.Component.literal(result.getMessage()), true);
                        } else {
                            context.getSource().sendFailure(
                                net.minecraft.network.chat.Component.literal(result.getMessage()));
                        }
                        return result.isSuccess() ? 1 : 0;
                    }))
                .then(literal("help")
                    .executes(context -> {
                        var result = CommandHandler.handleHelp();
                        context.getSource().sendSuccess(() -> 
                            net.minecraft.network.chat.Component.literal(result.getMessage()), false);
                        return 1;
                    }))
            );
        });
    }

}
