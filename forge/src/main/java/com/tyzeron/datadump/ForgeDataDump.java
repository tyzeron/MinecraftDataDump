package com.tyzeron.datadump;

import com.tyzeron.datadump.command.CommandHandler;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import static net.minecraft.commands.Commands.literal;


@Mod(DataDump.MOD_ID)
public class ForgeDataDump {

    public ForgeDataDump() {
        // Initialize platform-specific components
        PlatformHelper.setBlockDataProvider(new ForgeBlockDataProvider());
        PlatformHelper.setNbtWriter(new ForgeNbtWriter());
        PlatformHelper.setGameDirectory(FMLPaths.GAMEDIR.get());
        PlatformHelper.setConfigDirectory(FMLPaths.CONFIGDIR.get());

        // Initialize command handler
        CommandHandler.initialize(PlatformHelper.getConfigDirectory());

        // Initialize the mod
        DataDump.init();
    }

    @Mod.EventBusSubscriber(modid = DataDump.MOD_ID)
    public static class ForgeEvents {

        @SubscribeEvent
        public static void onRegisterCommands(RegisterCommandsEvent event) {
            event.getDispatcher().register(literal("datadump")
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
                            var result = CommandHandler.handleRun(profileName);
                            if (result.isSuccess()) {
                                context.getSource().sendSuccess(() -> 
                                    net.minecraft.network.chat.Component.literal(result.getMessage()), true);
                            } else {
                                context.getSource().sendFailure(
                                    net.minecraft.network.chat.Component.literal(result.getMessage()));
                            }
                            return result.isSuccess() ? Command.SINGLE_SUCCESS : 0;
                        })))
                .then(literal("list")
                    .executes(context -> {
                        var result = CommandHandler.handleList();
                        context.getSource().sendSuccess(() -> 
                            net.minecraft.network.chat.Component.literal(result.getMessage()), false);
                        return Command.SINGLE_SUCCESS;
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
                        return result.isSuccess() ? Command.SINGLE_SUCCESS : 0;
                    }))
                .then(literal("help")
                    .executes(context -> {
                        var result = CommandHandler.handleHelp();
                        context.getSource().sendSuccess(() -> 
                            net.minecraft.network.chat.Component.literal(result.getMessage()), false);
                        return Command.SINGLE_SUCCESS;
                    }))
            );
        }
    }

}
