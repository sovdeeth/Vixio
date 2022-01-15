package me.iblitzkriegi.vixio.expressions.embeds;

import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.Vixio;
import me.iblitzkriegi.vixio.util.UpdatingMessage;
import me.iblitzkriegi.vixio.util.skript.EasyMultiple;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;

public class ExprEmbeds extends PropertyExpression<UpdatingMessage, EmbedBuilder> implements EasyMultiple<UpdatingMessage, EmbedBuilder> {

    static {
        Vixio.getInstance().registerPropertyExpression(ExprEmbeds.class, EmbedBuilder.class,
                "embed", "messages")
                .setName("Embed of Message")
                .setDesc("Get the Embed of a Message")
                .setExample("broadcast \"%embed of event-message%\"");

    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setExpr((Expression<UpdatingMessage>) exprs[0]);
        return true;
    }

    @Override
    protected EmbedBuilder[] get(Event e, UpdatingMessage[] messages) {
        return convert(getReturnType(), getExpr().getAll(e), message -> message.getMessage().getEmbeds().stream()
                .map(EmbedBuilder::new)
                .toArray(size -> new EmbedBuilder[size])
        );
    }

    @Override
    public Class<? extends EmbedBuilder> getReturnType() {
        return EmbedBuilder.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "the embed of " + getExpr().toString(e, debug);
    }

}
