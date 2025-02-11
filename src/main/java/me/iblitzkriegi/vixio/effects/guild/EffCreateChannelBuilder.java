package me.iblitzkriegi.vixio.effects.guild;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.lang.VariableString;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.Vixio;
import me.iblitzkriegi.vixio.util.Util;
import me.iblitzkriegi.vixio.util.skript.AsyncEffect;
import me.iblitzkriegi.vixio.util.skript.SkriptUtil;
import me.iblitzkriegi.vixio.util.wrapper.Bot;
import me.iblitzkriegi.vixio.util.wrapper.ChannelBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import org.bukkit.event.Event;

public class EffCreateChannelBuilder extends AsyncEffect {
    static {
        Vixio.getInstance().registerEffect(EffCreateChannelBuilder.class,
                "create %channelbuilder% in %guild% [(with|as) %bot/string%] [and store (it|the channel) in %-objects%]")
                .setName("Create Channel Builder")
                .setDesc("Create a channel created with the create channel scope")
                .setExample(
                        "discord command ##create:",
                        "\ttrigger:",
                        "\t\tcreate voice channel:",
                        "\t\t\tset the name of the channel to \"{@bot}\"",
                        "\t\t\tset the bitrate of the channel to 69",
                        "\t\t\tcreate the channel in event-guild"
                );
    }

    private Expression<ChannelBuilder> channelBuilder;
    private Expression<Guild> guild;
    private Expression<Object> bot;
    private Variable<?> varExpr;
    private VariableString varName;

    @Override
    protected void execute(Event e) {
        ChannelBuilder channelBuilder = this.channelBuilder.getSingle(e);
        Bot bot = Util.botFrom(this.bot.getSingle(e));
        Guild guild = Util.bindGuild(bot, this.guild.getSingle(e));
        if (bot == null || guild == null || channelBuilder == null) {
            return;
        }

        if (varExpr == null) {
            try {
                if (channelBuilder.getType() == ChannelType.TEXT) {
                    guild.createTextChannel(channelBuilder.getName())
                            .setParent(channelBuilder.getParent())
                            .setNSFW(channelBuilder.isNSFW())
                            .setTopic(channelBuilder.getTopic()).queue();
                    return;
                }
                guild.createVoiceChannel(channelBuilder.getName())
                        .setParent(channelBuilder.getParent())
                        .setBitrate(channelBuilder.getBitRate() * 1000)
                        .setUserlimit(channelBuilder.getUserLimit()).queue();
            } catch (PermissionException x) {
                Vixio.getErrorHandler().needsPerm(bot, "create channel", x.getPermission().getName());
                return;
            } catch (IllegalArgumentException x) {
                Vixio.getErrorHandler().warn("Vixio attempted to create a channel with a name that was less than 2 characters or more than 100 which is not supported.");
                return;
            }
            return;
        }
        GuildChannel channel;
        try {
            if (channelBuilder.getType() == ChannelType.TEXT) {
                channel = guild.createTextChannel(channelBuilder.getName())
                        .setParent(channelBuilder.getParent())
                        .setNSFW(channelBuilder.isNSFW())
                        .setTopic(channelBuilder.getTopic()).complete(true);
            } else {
                channel = guild.createVoiceChannel(channelBuilder.getName())
                        .setParent(channelBuilder.getParent())
                        .setBitrate(channelBuilder.getBitRate() * 1000)
                        .setUserlimit(channelBuilder.getUserLimit()).complete(true);
            }
        } catch (RateLimitedException x) {
            Vixio.getErrorHandler().warn("Vixio tried to create and store a channel but was rate limited");
            return;
        } catch (PermissionException x) {
            Vixio.getErrorHandler().needsPerm(bot, "create channel", x.getPermission().getName());
            return;
        } catch (IllegalArgumentException x) {
            Vixio.getErrorHandler().warn("Vixio attempted to create a channel with a name that was less than 2 characters or more than 100 which is not supported.");
            return;
        }

        Util.storeInVar(varName, varExpr, channel, e);

    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create " + channelBuilder.toString(e, debug) + " in " + guild.toString(e, debug) + " as " + bot.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        channelBuilder = (Expression<ChannelBuilder>) exprs[0];
        guild = (Expression<Guild>) exprs[1];
        bot = (Expression<Object>) exprs[2];

        if (exprs[3] instanceof Variable) {
            varExpr = (Variable<?>) exprs[3];
            varName = SkriptUtil.getVariableName(varExpr);
        }
        return true;
    }
}
