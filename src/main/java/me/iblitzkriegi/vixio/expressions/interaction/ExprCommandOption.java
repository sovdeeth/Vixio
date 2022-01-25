package me.iblitzkriegi.vixio.expressions.interaction;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.events.interaction.EvtSlashCMDReceived;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static me.iblitzkriegi.vixio.Vixio.getInstance;

public class ExprCommandOption extends SimpleExpression<Object> {

    public static Object commandCore;

    static {
        getInstance().registerExpression(ExprCommandOption.class, Object.class, ExpressionType.SIMPLE, "[the] [slash] [command] option [with id] %string%")
            .setName("Slash Command Option")
            .setDesc("Get the content of a Message. The content can be set and deleted.")
            .setExample("slash command option \"name\"");
    }

    private Expression<String> exprID;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!ScriptLoader.isCurrentEvent(EvtSlashCMDReceived.SlashCMDReceived.class)) {
            Skript.error("Cannot use the option expression in a non-slash command event!");
            return false;
        }
        this.exprID = (Expression<String>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected Object[] get(Event e) {
        String id = exprID.getSingle(e);
        if (id == null) return new Object[0];
        if (e instanceof EvtSlashCMDReceived.SlashCMDReceived) {
            OptionMapping option = ((EvtSlashCMDReceived.SlashCMDReceived) e).getJDAEvent().getOption(id);
            if (option == null) return new Object[0];
            switch (option.getType()) {
                case CHANNEL:
                    return new Channel[] {option.getAsMessageChannel()};
                case USER:
                    return new User[] {option.getAsUser()};
                case ROLE:
                    return new Role[] {option.getAsRole()};
                case BOOLEAN:
                    return new Boolean[] {option.getAsBoolean()};
                case INTEGER:
                    return new Long[] {option.getAsLong()};
                default:
                    return new String[] {option.getAsString()};
            }
        }
        getInstance().getLogger()
                .warning("Vixio can't get option of slash command from a non slash command event!");
        return new Object[0];
    }

    @Override
    public Class<?> getReturnType() { return Object.class; }

    @Override
    public boolean isSingle() { return true; }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "slash command option";
    }

}
