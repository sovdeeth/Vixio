package me.iblitzkriegi.vixio.effects.interaction;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.events.interaction.EvtButtonReceived;
import me.iblitzkriegi.vixio.events.interaction.EvtSlashCMDReceived;
import me.iblitzkriegi.vixio.util.skript.AsyncEffect;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import org.bukkit.event.Event;

import static me.iblitzkriegi.vixio.Vixio.getInstance;

public class EffDeferInteraction extends AsyncEffect {
    static {
        getInstance().registerEffect(EffDeferInteraction.class, "(defer|acknowledge) [the] interaction [event] (1¦silently|2¦normally)")
                .setName("Defer Interaction")
                .setDesc("Defer an interaction.")
                .setExample(
                        "on slash command received:",
                        "\tdefer interaction"
                );
    }

    private boolean isEphemeral;

    @Override
    protected void execute(Event e) {
        Interaction interaction = null;
        if(e.getEventName().equals("SlashCMDReceived")) {
            interaction = ((EvtSlashCMDReceived.SlashCMDReceived) e).getJDAEvent().getInteraction();
        } else if(e.getEventName().equals("ButtonInteractionReceived")) {
            interaction = ((EvtButtonReceived.ButtonInteractionReceived) e).getJDAEvent().getInteraction();
        }
        if (interaction != null) {
            interaction.deferReply(isEphemeral).queue();
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "defer the interaction";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        isEphemeral = parseResult.mark == 1;
        if (!ScriptLoader.isCurrentEvent(EvtSlashCMDReceived.SlashCMDReceived.class) && !ScriptLoader.isCurrentEvent(EvtButtonReceived.ButtonInteractionReceived.class)) {
            Skript.error("Cannot use the option expression in a interaction event!");
            return false;
        }
        return true;
    }
}
