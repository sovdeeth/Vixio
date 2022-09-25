package me.iblitzkriegi.vixio.effects.interaction;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.util.Util;
import me.iblitzkriegi.vixio.util.skript.AsyncEffect;
import me.iblitzkriegi.vixio.util.wrapper.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.bukkit.event.Event;
import org.json.JSONObject;

import java.util.Objects;

import static me.iblitzkriegi.vixio.Vixio.getInstance;

public class EffAddSlashCommand extends AsyncEffect {
    static {
        getInstance().registerEffect(EffAddSlashCommand.class, "create command %string% with description %string% [in %-guild%] with %bot/string%")
                .setName("Create Command")
                .setDesc("Create a new slash command.")
                .setExample("create command \"say\" with description \"Say a message\" in event-guild");
    }

    private Expression<String> cmdName;
    private Expression<String> cmdDesc;
    private Expression<Guild> cmdGuild;
    private Expression<Object> bot;

    @Override
    protected void execute(Event e) {
        DataObject cmdData = DataObject.fromJson(String.valueOf(new JSONObject().put("name", Objects.requireNonNull(this.cmdName.getSingle(e))).put("description", Objects.requireNonNull(this.cmdDesc.getSingle(e)))));
        CommandData cmd = CommandData.fromData(cmdData);
        Guild guild = this.cmdGuild == null ? null : this.cmdGuild.getSingle(e);
        if(guild != null) {
            guild.upsertCommand(cmd).queue();
        } else {
            Bot bot = Util.botFrom(this.bot.getSingle(e));
            if (bot == null) {
                return;
            }
            bot.getJDA().upsertCommand(cmd).queue();
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create command";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        cmdName = (Expression<String>) exprs[0];
        cmdDesc = (Expression<String>) exprs[1];
        cmdGuild = (Expression<Guild>) exprs[2];
        bot = (Expression<Object>) exprs[3];
        return true;
    }
}
