package me.iblitzkriegi.vixio.expressions.member;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.Vixio;
import me.iblitzkriegi.vixio.changers.ChangeableSimplePropertyExpression;
import me.iblitzkriegi.vixio.util.wrapper.Bot;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;

public class ExprDeafened extends ChangeableSimplePropertyExpression<Member, Boolean> {

    static {
        Vixio.getInstance().registerPropertyExpression(ExprDeafened.class, Boolean.class,
                "[<guild>] deafen[ed] state", "members")
                .setName("Guild Deafened State of Member")
                .setDesc("Get the deafened state of a member in a guild. If they are deafened by someone then this returns true. This will not be updated unless a user is in a voice channel when they are deafened.")
                .setExample(
                        "on guild message receive:",
                        "\treply with \"%deafen state of event-user in event-guild%\""
                );
    }

    private boolean guild;

    @Override
    public Boolean convert(Member member) {
        return guild ? member.getVoiceState().isGuildDeafened() : member.getVoiceState().isDeafened();
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public String getPropertyName() {
        return (guild ? "guild " : "") + "deafened state";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        super.init(exprs, matchedPattern, isDelayed, parseResult);
        guild = parseResult.regexes.size() == 1;
        return true;
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode, boolean vixioChanger) {
        if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.SET) {
            return new Class[]{Boolean.class};
        }
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Bot bot, Changer.ChangeMode mode) {
        for (Member member : getExpr().getAll(e)) {
            boolean state = mode != Changer.ChangeMode.RESET && (Boolean) delta[0];
            member.getGuild().deafen(member, state).queue();
        }
    }
}
