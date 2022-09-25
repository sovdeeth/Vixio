package me.iblitzkriegi.vixio.expressions.channel;

import ch.njol.skript.classes.Changer;
import me.iblitzkriegi.vixio.Vixio;
import me.iblitzkriegi.vixio.changers.ChangeableSimplePropertyExpression;
import me.iblitzkriegi.vixio.util.Util;
import me.iblitzkriegi.vixio.util.wrapper.Bot;
import net.dv8tion.jda.api.entities.StandardGuildMessageChannel;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.exceptions.PermissionException;
import org.bukkit.event.Event;

public class ExprChannelParent extends ChangeableSimplePropertyExpression<GuildChannel, Category> {

    static {
        Vixio.getInstance().registerPropertyExpression(ExprChannelParent.class, Category.class,
                "(category|parent)", "channels")
                .setName("Category of Channel")
                .setDesc("Get or set the category of a channel.")
                .setExample(
                        "discord command $category <string> <string>:",
                        "\ttrigger:",
                        "\t\tset {_category} to category named arg-2",
                        "\t\tset {_channel} to channel with id arg-1",
                        "\t\tset parent of {_channel} to {_category} with event-bot",
                        "\t\treply with \"Successfully moved %{_channel}% to %{_category}%\""
                );
    }

    @Override
    public Category convert(GuildChannel channel) {
        return ((StandardGuildMessageChannel) channel).getParentCategory();
    }

    @Override
    public Class<? extends Category> getReturnType() {
        return Category.class;
    }

    @Override
    protected String getPropertyName() {
        return "category";
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean vixioChanger) {
        if (mode == Changer.ChangeMode.SET) {
            return new Class[]{Category.class};
        }
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode) {
        for (GuildChannel channel : getExpr().getAll(e)) {
            channel = Util.bindChannel(bot, channel);
            if (channel != null) {
                try {
                    ((StandardGuildMessageChannel) channel).getManager().setParent((Category) delta[0]).queue();
                } catch (PermissionException ex) {
                    Vixio.getErrorHandler().needsPerm(bot, "set category", ex.getPermission().getName());
                }
            }
        }
    }

    @Override
    public boolean shouldError() {
        return false;
    }

}
