package me.iblitzkriegi.vixio.effects.interaction;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.events.EvtSlashCMDReceived;
import me.iblitzkriegi.vixio.util.Util;
import me.iblitzkriegi.vixio.util.skript.AsyncEffect;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.Interaction;
import org.bukkit.event.Event;

import static me.iblitzkriegi.vixio.Vixio.getInstance;

public class EffRespondInteraction extends AsyncEffect {
    static {
        getInstance().registerEffect(EffRespondInteraction.class, "respond to [the] interaction [event] (1¦silently|2¦normally) (and say|with) %string%")
                .setName("Respond to Interaction")
                .setDesc("Respond to an interaction.")
                .setExample(
                        "on slash command received:",
                        "\trespond to the interaction normally with \"test\""
                );
    }

    private boolean isEphemeral;
    private Expression<String> content;

    @Override
    protected void execute(Event e) {
        Interaction interaction = ((EvtSlashCMDReceived.SlashCMDReceived) e).getJDAEvent().getInteraction();
        if (interaction != null) {
            Message content = Util.messageFrom(this.content.getSingle(e));
            if(interaction.isAcknowledged()) {
                interaction.getHook().editOriginal(content).queue();
            } else {
                interaction.reply(content).setEphemeral(isEphemeral).queue();
            }
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
        if (!ScriptLoader.isCurrentEvent(EvtSlashCMDReceived.SlashCMDReceived.class)) {
            Skript.error("Cannot use the option expression in a non-slash command event!");
            return false;
        }
        return true;
    }
}
