package me.iblitzkriegi.vixio.effects.interaction;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.events.interaction.EvtButtonReceived;
import me.iblitzkriegi.vixio.events.interaction.EvtSelectReceived;
import me.iblitzkriegi.vixio.events.interaction.EvtSlashCMDReceived;
import me.iblitzkriegi.vixio.util.Util;
import me.iblitzkriegi.vixio.util.skript.AsyncEffect;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenuInteraction;
import org.bukkit.event.Event;

import static me.iblitzkriegi.vixio.Vixio.getInstance;

public class EffFollowUpInteraction extends AsyncEffect {
    static {
        getInstance().registerEffect(EffFollowUpInteraction.class, "say interaction message (1¦silently|2¦normally) (and say|with) %messages/strings%")
                .setName("Interaction Follow Up Response")
                .setDesc("Send a follow up response to an interaction.")
                .setExample(
                        "on slash command received:",
                        "\tsay interaction message normally with \"test\""
                );
    }

    private boolean isEphemeral;
    private Expression<Object> message;

    @Override
    protected void execute(Event e) {
        Message message = Util.messageFrom(this.message.getSingle(e));
        assert message != null;
        if(e.getEventName().equals("SlashCMDReceived")) {
            SlashCommandInteraction interaction = ((EvtSlashCMDReceived.SlashCMDReceived) e).getJDAEvent().getInteraction();
            interaction.getHook().sendMessage(message).setEphemeral(isEphemeral).queue();
        } else if(e.getEventName().equals("ButtonInteractionReceived")) {
            ButtonInteraction interaction = ((EvtButtonReceived.ButtonInteractionReceived) e).getJDAEvent().getInteraction();
            interaction.getHook().sendMessage(message).setEphemeral(isEphemeral).queue();
        } else if(e.getEventName().equals("SelectInteractionReceived")) {
            SelectMenuInteraction interaction = ((EvtSelectReceived.SelectInteractionReceived) e).getJDAEvent().getInteraction();
            interaction.getHook().sendMessage(message).setEphemeral(isEphemeral).queue();
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "say interaction message";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        message = (Expression<Object>) exprs[0];
        isEphemeral = parseResult.mark == 1;
        //noinspection deprecation
        if (!ScriptLoader.isCurrentEvent(EvtSlashCMDReceived.SlashCMDReceived.class) && !ScriptLoader.isCurrentEvent(EvtButtonReceived.ButtonInteractionReceived.class) && !ScriptLoader.isCurrentEvent(EvtSelectReceived.SelectInteractionReceived.class)) {
            Skript.error("Cannot use the option expression in a non-interaction command event!");
            return false;
        }
        return true;
    }
}
