package me.iblitzkriegi.vixio.expressions.channel;

import ch.njol.skript.classes.Changer;
import me.iblitzkriegi.vixio.Vixio;
import me.iblitzkriegi.vixio.changers.ChangeableSimplePropertyExpression;
import me.iblitzkriegi.vixio.util.Util;
import me.iblitzkriegi.vixio.util.wrapper.Bot;
import net.dv8tion.jda.api.entities.StandardGuildMessageChannel;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.PermissionException;
import org.bukkit.event.Event;

public class ExprChannelTopic extends ChangeableSimplePropertyExpression<GuildChannel, String> {

    static {
        Vixio.getInstance().registerPropertyExpression(ExprChannelTopic.class, String.class,
                "topic", "channels")
                .setName("Topic of Text Channel")
                .setDesc("Get or set the topic of a text channel. You may also delete the topic to reset it.")
                .setUserFacing("[the] topic[s] of %textchannels%" ,"%textchannels%'[s] topic[s]")
                .setExample("set topic of event-channel to \"Hi Pika\" with event-bot");
    }

    @Override
    public String convert(GuildChannel channel) {
        return channel instanceof TextChannel ? ((TextChannel) channel).getTopic() : null;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "topic";
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean vixioChanger) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.DELETE)
            return new Class[]{String.class};
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode) {
        for (GuildChannel channel : getExpr().getAll(e)) {
            if (channel instanceof TextChannel) {
                channel = Util.bindChannel(bot, (TextChannel) channel);
                if (channel != null) {
                    try {
                        ((StandardGuildMessageChannel) channel).getManager().setTopic(mode == Changer.ChangeMode.DELETE ? null : (String) delta[0]).queue();
                    } catch (PermissionException ex) {
                        Vixio.getErrorHandler().needsPerm(bot, mode.name().toLowerCase() + " topic", ex.getPermission().getName());
                    }
                }
            }
        }
    }

}
