package me.iblitzkriegi.vixio.expressions.interaction;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.Vixio;
import me.iblitzkriegi.vixio.events.interaction.EvtSelectReceived;
import me.iblitzkriegi.vixio.events.member.EvtAddRole;
import me.iblitzkriegi.vixio.events.member.EvtRemoveRole;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.event.Event;

import java.util.List;

public class ExprSelectOptions extends SimpleExpression<String> {

    static {
        Vixio.getInstance().registerExpression(ExprSelectOptions.class, String.class, ExpressionType.SIMPLE,
                "[the] selection[s]")
                .setName("Selected options.")
                .setDesc("Get the selected options on the select interaction event.")
                .setExample("SOON");
    }

    @Override
    protected String[] get(Event e) {
        List<String> selections = ((EvtSelectReceived.SelectInteractionReceived) e).getJDAEvent().getValues();
        return selections.toArray(new String[selections.size()]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "the selections";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (ScriptLoader.isCurrentEvent(EvtSelectReceived.SelectInteractionReceived.class)) {
            return true;
        }
        Skript.error("This expression may only be used inside the select interaction received event.");
        return false;
    }
}
