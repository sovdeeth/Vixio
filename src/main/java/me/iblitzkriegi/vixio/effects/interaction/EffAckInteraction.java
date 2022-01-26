package me.iblitzkriegi.vixio.effects.interaction;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.events.interaction.EvtButtonReceived;
import me.iblitzkriegi.vixio.events.interaction.EvtSelectReceived;
import me.iblitzkriegi.vixio.events.interaction.EvtSlashCMDReceived;
import me.iblitzkriegi.vixio.util.skript.AsyncEffect;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.ComponentInteraction;
import net.dv8tion.jda.api.interactions.components.ComponentLayout;
import net.dv8tion.jda.api.requests.restaction.interactions.InteractionCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.UpdateInteractionAction;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

import static me.iblitzkriegi.vixio.Vixio.getInstance;

public class EffAckInteraction extends AsyncEffect {
    static {
        getInstance().registerEffect(EffAckInteraction.class, "ack[nowledge] [the] interaction [event]")
                .setName("Acknowledge Interaction")
                .setDesc("Acknowledge an interaction.")
                .setExample(
                        "on slash command received:",
                        "\tacknowledge interaction"
                );
    }

    @Override
    protected void execute(Event e) {
        Interaction interaction = null;
        if(e.getEventName().equals("ButtonInteractionReceived")) {
            interaction = ((EvtButtonReceived.ButtonInteractionReceived) e).getJDAEvent().getInteraction();
        } else if(e.getEventName().equals("SelectInteractionReceived")) {
            interaction = ((EvtSelectReceived.SelectInteractionReceived) e).getJDAEvent().getInteraction();
        }
        if (interaction != null) {
            ((ComponentInteraction) interaction).deferEdit().queue();
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "acknowledge the interaction event";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        //noinspection deprecation
        if (!ScriptLoader.isCurrentEvent(EvtButtonReceived.ButtonInteractionReceived.class) && !ScriptLoader.isCurrentEvent(EvtSelectReceived.SelectInteractionReceived.class)) {
            Skript.error("Cannot use the option expression in a non-interaction event!");
            return false;
        }
        return true;
    }
}
