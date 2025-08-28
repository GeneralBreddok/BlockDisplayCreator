package me.general_breddok.blockdisplaycreator.custom;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import me.general_breddok.blockdisplaycreator.BlockDisplayCreator;
import me.general_breddok.blockdisplaycreator.commandparser.CommandLine;
import me.general_breddok.blockdisplaycreator.commandparser.CommandRegistry;
import me.general_breddok.blockdisplaycreator.commandparser.SummonCommandLine;
import me.general_breddok.blockdisplaycreator.common.DeepCloneable;
import me.general_breddok.blockdisplaycreator.entity.CommandSummoner;
import me.general_breddok.blockdisplaycreator.entity.display.TranslationVectorAdjustable;
import me.general_breddok.blockdisplaycreator.permission.DefaultPermissions;
import me.general_breddok.blockdisplaycreator.rotation.DirectedVector;
import me.general_breddok.blockdisplaycreator.util.ChatUtil;
import me.general_breddok.blockdisplaycreator.util.LocationUtil;
import me.general_breddok.blockdisplaycreator.util.OperationUtil;
import me.general_breddok.blockdisplaycreator.world.TransformationBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.Predicate;

@Getter
@Setter
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutomaticCommandDisplaySummoner implements CommandSummoner<Display>, TranslationVectorAdjustable, DeepCloneable<AutomaticCommandDisplaySummoner> {
    List<CommandLine> commands;
    DirectedVector translation;
    boolean usePlaceholder;

    public AutomaticCommandDisplaySummoner(List<CommandLine> commands, DirectedVector translation) {
        this.commands = commands;
        this.translation = translation;
    }

    public AutomaticCommandDisplaySummoner(List<CommandLine> commands) {
        this.commands = commands;
    }

    @Override
    @SuppressWarnings("all")
    public List<Display> summon(@NotNull Location location, @NotNull Entity commandSender, @Nullable Predicate<Display> filter) {
        prepareDisplayCommands(location, this.commands);

        World world = location.getWorld();

        if (world == null) {
            return List.of();
        }

        boolean summonPermission = commandSender.hasPermission(DefaultPermissions.Vanilla.Command.SUMMON);
        boolean functionPermission = commandSender.hasPermission(DefaultPermissions.Vanilla.Command.FUNCTION);

        boolean cmdFeedback = world.getGameRuleValue(GameRule.SEND_COMMAND_FEEDBACK);
        boolean logAdminCommands = world.getGameRuleValue(GameRule.LOG_ADMIN_COMMANDS);

        giveCommandPermissions(commandSender);
        world.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, false);
        world.setGameRule(GameRule.LOG_ADMIN_COMMANDS, false);

        executeDisplayCommands(commands, commandSender);

        commandSender.addAttachment(BlockDisplayCreator.getInstance(), DefaultPermissions.Vanilla.Command.SUMMON, summonPermission);
        commandSender.addAttachment(BlockDisplayCreator.getInstance(), DefaultPermissions.Vanilla.Command.FUNCTION, functionPermission);
        world.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, cmdFeedback);
        world.setGameRule(GameRule.LOG_ADMIN_COMMANDS, logAdminCommands);


        return LocationUtil.getEntities(location, entity -> {
                    if (entity instanceof Display display) {
                        if (this.translation != null) {
                            display.setTransformation(new TransformationBuilder(display).addTranslation(this.translation.clone()).build());
                        }

                        if (filter != null) {
                            return filter.test(display);
                        }
                        return true;
                    }
                    return false;
                })
                .stream()
                .map(entity -> (Display) entity)
                .collect(OperationUtil.toArrayList());
    }

    private void prepareDisplayCommands(Location location, List<CommandLine> displayCommands) {
        CommandRegistry registry = new CommandRegistry();

        ListIterator<CommandLine> iterator = displayCommands.listIterator();
        while (iterator.hasNext()) {
            CommandLine line = iterator.next();

            SummonCommandLine summon = (line instanceof SummonCommandLine sc)
                    ? sc
                    : (registry.getCommand(line.toString()) instanceof SummonCommandLine rc ? rc : null);

            if (summon != null) {
                summon.setCoordinates(location);
                iterator.set(summon);
            }
        }
    }

    private void executeDisplayCommands(List<CommandLine> displayCommand, Entity commandSender) {
        displayCommand.forEach(command -> command.execute(commandSender));
    }

    private void giveCommandPermissions(Entity commandSender) {
        commandSender.addAttachment(BlockDisplayCreator.getInstance(), DefaultPermissions.Vanilla.Command.SUMMON, true);
        commandSender.addAttachment(BlockDisplayCreator.getInstance(), DefaultPermissions.Vanilla.Command.FUNCTION, true);
    }

    @Override
    public AutomaticCommandDisplaySummoner clone() {
        AutomaticCommandDisplaySummoner cloned = new AutomaticCommandDisplaySummoner(DeepCloneable.tryCloneList(this.commands));
        if (translation != null) {
            cloned.setTranslation(this.translation.clone());
        }
        cloned.setUsePlaceholder(this.usePlaceholder);
        return cloned;
    }
}
