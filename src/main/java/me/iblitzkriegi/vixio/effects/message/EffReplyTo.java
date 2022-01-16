package me.iblitzkriegi.vixio.effects.message;

import ch.njol.skript.lang.*;
import ch.njol.skript.registrations.EventValues;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.Vixio;
import me.iblitzkriegi.vixio.util.UpdatingMessage;
import me.iblitzkriegi.vixio.util.Util;
import me.iblitzkriegi.vixio.util.skript.SkriptUtil;
import me.iblitzkriegi.vixio.util.wrapper.Bot;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import org.bukkit.event.Event;

public class EffReplyTo extends Effect {
    static {
        Vixio.getInstance().registerEffect(EffReplyTo.class, "reply to %messages% (1¦with|2¦without) mention and say %message/string% [and store (it|the message) in %-objects%]")
                .setName("Reply to Message")
                .setDesc("Reply to a message.")
                .setUserFacing("reply to %messages% (with|without) mention and say %message/string% [and store (it|the message) in %-objects%]")
                .setExample(
                        "discord command reply:",
                        "\ttrigger:",
                        "\t\treply to event-message with mention and say \"Replied\""
                );
    }

    private boolean shouldMention;
    private Expression<UpdatingMessage> messages;
    private Expression<String> content;
    private Variable<?> varExpr;
    private VariableString varName;

    @Override
    protected void execute(Event e) {
        Bot bot = EventValues.getEventValue(e, Bot.class, 0);
        Message content = Util.messageFrom(this.content.getSingle(e));
        if (bot == null || content == null) {
            return;
        }

        try {
            for (Message message : UpdatingMessage.convert(messages.getAll(e))) {
                if (message != null) {
                    if (varExpr == null) {
                        message.reply(content).mentionRepliedUser(shouldMention).queue();
                    } else {
                        try {
                            Message resultingMessage = message.reply(content).mentionRepliedUser(shouldMention).complete(true);
                            if (resultingMessage != null) {
                                Util.storeInVar(varName, varExpr, UpdatingMessage.from(resultingMessage), e);
                            }
                        } catch (RateLimitedException e1) {
                            Vixio.getErrorHandler().warn("Vixio tried to reply with and store a message but was rate limited");
                        }
                    }
                }
            }
        } catch (PermissionException x) {
            Vixio.getErrorHandler().needsPerm(bot, x.getPermission().getName(), "send message");
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "reply to " + messages.toString(e, debug) + " with " + content.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        messages = (Expression<UpdatingMessage>) exprs[0];
        shouldMention = parseResult.mark == 1;
        content = (Expression<String>) exprs[1];
        if (exprs[2] instanceof Variable) {
            varExpr = (Variable<?>) exprs[2];
            varName = SkriptUtil.getVariableName(varExpr);
        }
        return true;
    }
}