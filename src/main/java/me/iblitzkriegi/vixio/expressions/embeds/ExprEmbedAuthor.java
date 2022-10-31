package me.iblitzkriegi.vixio.expressions.embeds;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.Vixio;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.event.Event;

public class ExprEmbedAuthor extends SimplePropertyExpression<EmbedBuilder, MessageEmbed.AuthorInfo> {

    static {
        Vixio.getInstance().registerPropertyExpression(ExprEmbedAuthor.class, MessageEmbed.AuthorInfo.class,
                        "author", "embedbuilders")
                .setName("Author of Embed")
                .setDesc("Returns the author of an embed. Can be set any author.")
                .setExample("set author of {_embed} to an author named \"Hi Pika\" and icon \"https://i.imgur.com/TQgR2hW.jpg\" and url \"https://1a3dev.github.io/VixioSite/\"");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        super.init(exprs, matchedPattern, isDelayed, parseResult);
        setExpr((Expression<EmbedBuilder>) exprs[0]);
        return true;
    }

    @Override
    public MessageEmbed.AuthorInfo convert(EmbedBuilder embed) {
        return embed.isEmpty() ? null : embed.build().getAuthor();
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if ((mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.DELETE) && getExpr().isSingle()) {
            return new Class[]{
                    String.class,
                    MessageEmbed.AuthorInfo.class
            };
        }
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {

        EmbedBuilder embed = getExpr().getSingle(e);
        if (embed == null) return;

        switch (mode) {

            case RESET:
            case DELETE:
                embed.setAuthor(null, null);
                return;

            case SET:
                MessageEmbed.AuthorInfo author = delta[0] instanceof String ?
                        new EmbedBuilder().setAuthor((String) delta[0], null).build().getAuthor()
                        : (MessageEmbed.AuthorInfo) delta[0];
                embed.setAuthor(author.getName(), author.getUrl(), author.getIconUrl());

        }
    }

    @Override
    public Class<? extends MessageEmbed.AuthorInfo> getReturnType() {
        return MessageEmbed.AuthorInfo.class;
    }

    @Override
    protected String getPropertyName() {
        return "author of embed";
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "the author of " + getExpr().toString(e, debug);
    }

}
