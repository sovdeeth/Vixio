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
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenuInteraction;
import org.bukkit.event.Event;

import static me.iblitzkriegi.vixio.Vixio.getInstance;

public class EffDeferInteraction extends AsyncEffect {
    static {
        getInstance().registerEffect(EffDeferInteraction.class, "defer [the] interaction [event] (1¦silently|2¦normally)")
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
        if(e.getEventName().equals("SlashCMDReceived")) {
            SlashCommandInteraction interaction = ((EvtSlashCMDReceived.SlashCMDReceived) e).getJDAEvent().getInteraction();
            if (!interaction.isAcknowledged()) {
                interaction.deferReply(isEphemeral).queue();
            }
        } else if(e.getEventName().equals("ButtonInteractionReceived")) {
            ButtonInteraction interaction = ((EvtButtonReceived.ButtonInteractionReceived) e).getJDAEvent().getInteraction();
            if (!interaction.isAcknowledged()) {
                interaction.deferReply(isEphemeral).queue();
            }
        } else if(e.getEventName().equals("SelectInteractionReceived")) {
            SelectMenuInteraction interaction = ((EvtSelectReceived.SelectInteractionReceived) e).getJDAEvent().getInteraction();
            if (!interaction.isAcknowledged()) {
                interaction.deferReply(isEphemeral).queue();
            }
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "defer the interaction event";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        isEphemeral = parseResult.mark == 1;
        //noinspection deprecation
        if (!ScriptLoader.isCurrentEvent(EvtSlashCMDReceived.SlashCMDReceived.class) && !ScriptLoader.isCurrentEvent(EvtButtonReceived.ButtonInteractionReceived.class) && !ScriptLoader.isCurrentEvent(EvtSelectReceived.SelectInteractionReceived.class)) {
            Skript.error("Cannot use the option expression in a non-interaction event!");
            return false;
        }
        return true;
    }
}
