package me.iblitzkriegi.vixio.expressions.message;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import me.iblitzkriegi.vixio.Vixio;
import me.iblitzkriegi.vixio.util.UpdatingMessage;
import net.dv8tion.jda.api.entities.Message;

import java.util.Objects;

public class ExprReferencedMessage extends SimplePropertyExpression<UpdatingMessage, UpdatingMessage> {
    static {
        Vixio.getInstance().registerPropertyExpression(ExprReferencedMessage.class, UpdatingMessage.class, "message reference", "message")
                .setName("Message Reference of Message")
                .setDesc("Get the message that was replied to.")
                .setExample(
                    "discord command $rmsg <text>:",
                    "\ttrigger:",
                    "\t\tset {_msg} to message reference of event-message",
                    "\t\treply with \"%content of {_msg}%\""
                );
    }

    @Override
    protected String getPropertyName() {
        return "message reference";
    }

    @Override
    public UpdatingMessage convert(UpdatingMessage message) {
        Message tmpMessage = message.getMessage().getReferencedMessage();
        if(tmpMessage != null) return UpdatingMessage.from(tmpMessage);
        else return null;
    }

    @Override
    public Class<? extends UpdatingMessage> getReturnType() {
        return UpdatingMessage.class;
    }
}