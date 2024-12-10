package de.presti.ree6.commands.impl.info;

import de.presti.ree6.bot.BotConfig;
import de.presti.ree6.commands.Category;
import de.presti.ree6.commands.CommandEvent;
import de.presti.ree6.commands.interfaces.Command;
import de.presti.ree6.commands.interfaces.ICommand;
import de.presti.ree6.main.Main;
import de.presti.ree6.sql.SQLSession;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

/**
 * A command to help the user navigate.
 */
@Command(name = "help", description = "command.description.help", category = Category.INFO)
public class Help implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {

        if (commandEvent.isSlashCommand()) {
            OptionMapping categoryOption = commandEvent.getOption("category");

            if (categoryOption != null) {
                sendHelpInformation(categoryOption.getAsString(), commandEvent);
            } else {
                sendHelpInformation(null, commandEvent);
            }
        } else {

            if (commandEvent.getArguments().length != 1) {
                sendHelpInformation(null, commandEvent);
            } else if (commandEvent.getArguments().length == 1) {
                sendHelpInformation(commandEvent.getArguments()[0], commandEvent);
            }
        }
    }

    /**
     * Sends the help information to the user.
     *
     * @param categoryString The category to show the help for.
     * @param commandEvent   The command event.
     */
    public void sendHelpInformation(String categoryString, CommandEvent commandEvent) {
    MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();

    EmbedBuilder em = new EmbedBuilder();

    em.setColor(BotConfig.getMainColor());
    em.setTitle("Help Center");
    em.setThumbnail(commandEvent.getGuild().getJDA().getSelfUser().getEffectiveAvatarUrl());
    em.setFooter(commandEvent.getGuild().getName() + " - " + BotConfig.getAdvertisement(), commandEvent.getGuild().getIconUrl());

    // Removed the addActionRow() entirely

    if (categoryString == null) {
        SQLSession.getSqlConnector().getSqlWorker().getSetting(commandEvent.getGuild().getIdLong(), "chatprefix").subscribe(setting -> {
            for (Category cat : Category.values()) {
                if (cat != Category.HIDDEN) {
                    if (!BotConfig.isModuleActive(cat.name().toLowerCase())) continue;

                    String formattedName = cat.name().toUpperCase().charAt(0) + cat.name().substring(1).toLowerCase();
                    em.addField("**" + formattedName + "**", setting.get().getStringValue() + "help " + cat.name().toLowerCase(), true);
                }
            }

            commandEvent.reply(messageCreateBuilder.setEmbeds(em.build()).build());
        });
    } else {
        if (isValid(categoryString)) {
            StringBuilder end = new StringBuilder();

            Category category = getCategoryFromString(categoryString);

            SQLSession.getSqlConnector().getSqlWorker().getSetting(commandEvent.getGuild().getIdLong(), "chatprefix").subscribe(setting -> {
                for (ICommand cmd : Main.getInstance().getCommandManager().getCommands().stream().filter(command -> command.getClass().getAnnotation(Command.class).category() == category).toList()) {
                    end.append("``")
                        .append(setting.get().getStringValue())
                        .append(cmd.getClass().getAnnotation(Command.class).name())
                        .append("``\n")
                        .append(commandEvent.getResource(cmd.getClass().getAnnotation(Command.class).description()))
                        .append("\n\n");
                }

                em.setDescription(end.toString());
                commandEvent.reply(messageCreateBuilder.setEmbeds(em.build()).build());
            });
        } else {
            sendHelpInformation(null, commandEvent);
        }
    }
}



    /**
     * @inheritDoc
     */
    @Override
    public String[] getAlias() {
        return new String[0];
    }

    /**
     * @inheritDoc
     */
    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl("help", "command.description.help")
                .addOptions(new OptionData(OptionType.STRING, "category", "Which Category you want to check out."));
    }

    /**
     * Check if a String is a valid category.
     *
     * @param arg The String to check.
     * @return True if the String is a valid category.
     */
    private boolean isValid(String arg) {
        if (arg == null) return false;

        for (Category cat : Category.values()) {
            if (cat.name().equalsIgnoreCase(arg) && cat != Category.HIDDEN) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get the Category from a String.
     *
     * @param arg The String to use.
     * @return The Category.
     */
    private Category getCategoryFromString(String arg) {
        if (arg == null) return null;
        for (Category cat : Category.values()) {
            if (cat.name().equalsIgnoreCase(arg)) {
                return cat;
            }
        }

        return null;
    }

}
