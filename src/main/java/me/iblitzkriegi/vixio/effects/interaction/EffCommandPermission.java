package me.iblitzkriegi.vixio.effects.interaction;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.Vixio;
import me.iblitzkriegi.vixio.util.Util;
import me.iblitzkriegi.vixio.util.wrapper.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.privileges.IntegrationPrivilege;
import org.bukkit.event.Event;

import java.util.List;
import java.util.Optional;

public class EffCommandPermission extends Effect {
    static {
        Vixio.getInstance().registerEffect(EffCommandPermission.class,
                "(allow|1¦deny|2¦reset) %roles/users/members% access to %string% in %guild% [with %bot/string%]")
                .setName("Command Permission in Guild")
                .setDesc("Allow, or deny a role or a member permissions to a command")
                .setExample(
                        "discord command grant <text> <string>:",
                        "\ttrigger:",
                        "\t\tif id of event-guild is not \"219967335266648065\":",
                        "\t\t\tstop",
                        "\t\tset {_role} to role with id arg-1",
                        "\t\tallow {_role} access to arg-2 in event-guild",
                        "\t\tdeny {_role} access to arg-2 in event-guild"
                );
    }

    private int allow;
    private Expression<Object> inputs;
    private Expression<String> cmdName;
    private Expression<Guild> guild;
    private Expression<Object> bot;

    @Override
    protected void execute(Event e) {
        Bot bot = Util.botFrom(this.bot.getSingle(e));
        if (bot == null) {
            return;
        }

        String cmdName = this.cmdName.getSingle(e);
        Command cmd = null;
        List<Command> cmdList;
        // Guild Commands
        Guild guild = this.guild.getSingle(e);
        if (guild == null) {
            return;
        }
        cmdList = guild.retrieveCommands().complete();
        Optional<Command> cmdOpt = cmdList.stream().filter(c -> c.getName().equals(cmdName)).findFirst();
        // Global Commands
        if(!cmdOpt.isPresent()) {
            cmdList = bot.getJDA().retrieveCommands().complete();
            cmdOpt = cmdList.stream().filter(c -> c.getName().equals(cmdName)).findFirst();
        }
        if(cmdOpt.isPresent()) {
            cmd = cmdOpt.get();
        }
        if(cmd == null) {
            Vixio.getErrorHandler().log("Tried changing access for unknown command: " + cmdName);
            return;
        }

        List<IntegrationPrivilege> oldPerms = cmd.retrievePrivileges(guild).complete();

        Object[] inputs = this.inputs.getAll(e);
        for (Object input : inputs) {
            if (input instanceof Role) {
                Role role = (Role) input;
                if (allow == 0) {
//                    oldPerms.add(IntegrationPrivilege.enableRole(role.getId()));
                } else if (allow == 1) {
//                    oldPerms.add(IntegrationPrivilege.disableRole(role.getId()));
                } else {
                    Optional<IntegrationPrivilege> optOldPerm = oldPerms.stream().filter(p -> p.getId().equals(role.getId())).findFirst();
                    optOldPerm.ifPresent(oldPerms::remove);
                }
            } else if (input instanceof User) {
                User user = (User) input;
                if (allow == 0) {
//                    oldPerms.add(IntegrationPrivilege.enableUser(user.getId()));
                } else if (allow == 1) {
//                    oldPerms.add(IntegrationPrivilege.disableUser(user.getId()));
                } else {
                    Optional<IntegrationPrivilege> optOldPerm = oldPerms.stream().filter(p -> p.getId().equals(user.getId())).findFirst();
                    optOldPerm.ifPresent(oldPerms::remove);
                }
            } else if (input instanceof Member) {
                Member member = (Member) input;
                if (allow == 0) {
//                    oldPerms.add(IntegrationPrivilege.enableUser(member.getId()));
                } else if (allow == 1) {
//                    oldPerms.add(IntegrationPrivilege.disableUser(member.getId()));
                } else {
                    Optional<IntegrationPrivilege> optOldPerm = oldPerms.stream().filter(p -> p.getId().equals(member.getId())).findFirst();
                    optOldPerm.ifPresent(oldPerms::remove);
                }
            }
        }
        try {
//            cmd.updatePrivileges(guild, oldPerms).queue();
        } catch (PermissionException x) {
            Vixio.getErrorHandler().needsPerm(bot, "manage permissions", x.getPermission().getName());
        }
    }


    @Override
    public String toString(Event e, boolean debug) {
        return (allow == 0 ? "allow " : (allow == 1 ? "deny " : "reset ")) + inputs.toString(e, debug) + " access to command "+ cmdName.toString(e, debug) + " with " + bot.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        allow = parseResult.mark;
        inputs = (Expression<Object>) exprs[0];
        cmdName = (Expression<String>) exprs[1];
        guild = (Expression<Guild>) exprs[2];
        bot = (Expression<Object>) exprs[3];
        return true;
    }
}
