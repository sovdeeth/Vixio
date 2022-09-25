package me.iblitzkriegi.vixio.expressions.channel;

import ch.njol.skript.classes.Changer;
import me.iblitzkriegi.vixio.Vixio;
import me.iblitzkriegi.vixio.changers.ChangeableSimplePropertyExpression;
import me.iblitzkriegi.vixio.util.wrapper.Bot;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.bukkit.event.Event;

public class ExprChannelType extends ChangeableSimplePropertyExpression<MessageChannel, String> {

    static {
        Vixio.getInstance().registerPropertyExpression(ExprChannelType.class, String.class,
                "type", "channels")
                .setName("Type of Channel")
                .setDesc("Get or set the type of a channel.")
                .setUserFacing("[the] type[s] of %channels%" ,"%channels%'[s] topic[s]")
                .setExample("set type of event-channel to \"TEXT\" with event-bot");
    }

    @Override
    public String convert(MessageChannel channel) {
        return channel.getType().toString();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "type";
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean vixioChanger) {
//        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.DELETE)
//            return new Class[]{String.class};
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode) {
//        for (GuildChannel channel : getExpr().getAll(e)) {
//            channel = Util.bindChannel(bot, channel);
//            if (channel != null) {
//                try {
//                    channel.getManager().setType(mode == Changer.ChangeMode.DELETE ? null : (String) delta[0]).queue();
//                } catch (PermissionException ex) {
//                    Vixio.getErrorHandler().needsPerm(bot, mode.name().toLowerCase() + " type", ex.getPermission().getName());
//                }
//            }
//        }
    }
}
