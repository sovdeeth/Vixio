package me.iblitzkriegi.vixio.events.interaction;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import me.iblitzkriegi.vixio.events.base.BaseEvent;
import me.iblitzkriegi.vixio.events.base.SimpleVixioEvent;
import me.iblitzkriegi.vixio.util.Util;
import me.iblitzkriegi.vixio.util.wrapper.Bot;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class EvtSlashCMDReceived extends BaseEvent<SlashCommandInteractionEvent> {
    static {
        BaseEvent.register("slash command received", EvtSlashCMDReceived.class, SlashCMDReceived.class,
                "slash command [interaction] receive[d]")
                .setName("Slash Command Received")
                .setDesc("Fired when a slash command is executed.")
                .setExample("on slash command received:");

        EventValues.registerEventValue(SlashCMDReceived.class, User.class, new Getter<User, SlashCMDReceived>() {
            @Override
            public User get(SlashCMDReceived event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

        EventValues.registerEventValue(SlashCMDReceived.class, MessageChannel.class, new Getter<MessageChannel, SlashCMDReceived>() {
            @Override
            public MessageChannel get(SlashCMDReceived event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(SlashCMDReceived.class, Member.class, new Getter<Member, SlashCMDReceived>() {
            @Override
            public Member get(SlashCMDReceived event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(SlashCMDReceived.class, Bot.class, new Getter<Bot, SlashCMDReceived>() {
            @Override
            public Bot get(SlashCMDReceived event) {
                return Util.botFrom(event.getJDAEvent().getJDA());
            }
        }, 0);

        EventValues.registerEventValue(SlashCMDReceived.class, String.class, new Getter<String, SlashCMDReceived>() {
            @Override
            public String get(SlashCMDReceived event) {
                String name = event.getJDAEvent().getName();
                if(event.getJDAEvent().getSubcommandGroup() != null) name += " " + event.getJDAEvent().getSubcommandGroup();
                if(event.getJDAEvent().getSubcommandName() != null) name += " " + event.getJDAEvent().getSubcommandName();
                return name;
            }
        }, 0);

        EventValues.registerEventValue(SlashCMDReceived.class, Guild.class, new Getter<Guild, SlashCMDReceived>() {
            @Override
            public Guild get(SlashCMDReceived event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(SlashCMDReceived.class, GuildChannel.class, new Getter<GuildChannel, SlashCMDReceived>() {
            @Override
            public GuildChannel get(SlashCMDReceived event) {
                return event.getJDAEvent().getGuildChannel();
            }
        }, 0);

    }

    public class SlashCMDReceived extends SimpleVixioEvent<SlashCommandInteractionEvent> {
    }

}
