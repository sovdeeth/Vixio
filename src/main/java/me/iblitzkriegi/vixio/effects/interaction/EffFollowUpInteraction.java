package me.iblitzkriegi.vixio.effects.interaction;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.events.interaction.EvtButtonReceived;
import me.iblitzkriegi.vixio.events.interaction.EvtSlashCMDReceived;
import me.iblitzkriegi.vixio.util.Util;
import me.iblitzkriegi.vixio.util.skript.AsyncEffect;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.Interaction;
import org.bukkit.event.Event;

import static me.iblitzkriegi.vixio.Vixio.getInstance;

public class EffFollowUpInteraction extends AsyncEffect {
    static {
        getInstance().registerEffect(EffFollowUpInteraction.class, "send interaction message (1¦silently|2¦normally) (and say|with) %string%")
                .setName("Interaction Follow Up Response")
                .setDesc("Send a follow up response to an interaction.")
                .setExample(
                        "on slash command received:",
                        "\tsend interaction message normally with \"test\""
                );
    }

    private boolean isEphemeral;
    private Expression<String> content;

    @Override
    protected void execute(Event e) {
        Interaction interaction = null;
        if(e.getEventName().equals("SlashCMDReceived")) {
            interaction = ((EvtSlashCMDReceived.SlashCMDReceived) e).getJDAEvent().getInteraction();
        } else if(e.getEventName().equals("ButtonInteractionReceived")) {
            interaction = ((EvtButtonReceived.ButtonInteractionReceived) e).getJDAEvent().getInteraction();
        }
        if (interaction != null) {
            Message content = Util.messageFrom(this.content.getSingle(e));
            assert content != null;
            interaction.getHook().sendMessage(content).setEphemeral(isEphemeral).queue();
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "respond to the interaction";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        content = (Expression<String>) exprs[0];
        isEphemeral = parseResult.mark == 1;
        //noinspection deprecation
        if (!ScriptLoader.isCurrentEvent(EvtSlashCMDReceived.SlashCMDReceived.class) && !ScriptLoader.isCurrentEvent(EvtButtonReceived.ButtonInteractionReceived.class)) {
            Skript.error("Cannot use the option expression in a non-slash command event!");
            return false;
        }
        return true;
    }
}
