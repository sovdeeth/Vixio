package me.iblitzkriegi.vixio.expressions.interaction;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.events.interaction.EvtButtonReceived;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static me.iblitzkriegi.vixio.Vixio.getInstance;

public class ExprButtonLabel extends SimpleExpression<String> {
    static {
        getInstance().registerExpression(ExprButtonLabel.class, String.class, ExpressionType.SIMPLE, "[the] button [interaction] label")
            .setName("Button Label")
            .setDesc("Get the label from a button interaction.")
            .setExample("button label");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!ScriptLoader.isCurrentEvent(EvtButtonReceived.ButtonInteractionReceived.class)) {
            Skript.error("Cannot use the option expression in a non-button event!");
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    protected String[] get(Event e) {
        if(e.getEventName().equals("ButtonInteractionReceived")) {
            ButtonInteractionEvent bEvent = ((EvtButtonReceived.ButtonInteractionReceived) e).getJDAEvent();

            return new String[]{bEvent.getButton().getLabel()};
        }
        getInstance().getLogger()
                .warning("Vixio can't get emote of button interaction from a non interaction event!");
        return null;
    }

    @Override
    public Class<? extends String> getReturnType() { return String.class; }

    @Override
    public boolean isSingle() { return true; }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "button label";
    }

}
