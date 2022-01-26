package me.iblitzkriegi.vixio.events.interaction;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import me.iblitzkriegi.vixio.events.base.BaseEvent;
import me.iblitzkriegi.vixio.events.base.SimpleVixioEvent;
import me.iblitzkriegi.vixio.util.Util;
import me.iblitzkriegi.vixio.util.wrapper.Bot;
import me.iblitzkriegi.vixio.util.wrapper.Emote;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

public class EvtSelectReceived extends BaseEvent<SelectionMenuEvent> {
    static {
        BaseEvent.register("select interaction received", EvtSelectReceived.class, SelectInteractionReceived.class,
                "select [menu] interaction receive[d]")
                .setName("Select Menu Interaction Received")
                .setDesc("Fired when a selection is made.")
                .setExample("on select interaction received:");

        EventValues.registerEventValue(SelectInteractionReceived.class, User.class, new Getter<User, SelectInteractionReceived>() {
            @Override
            public User get(SelectInteractionReceived event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

        EventValues.registerEventValue(SelectInteractionReceived.class, MessageChannel.class, new Getter<MessageChannel, SelectInteractionReceived>() {
            @Override
            public MessageChannel get(SelectInteractionReceived event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(SelectInteractionReceived.class, Member.class, new Getter<Member, SelectInteractionReceived>() {
            @Override
            public Member get(SelectInteractionReceived event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(SelectInteractionReceived.class, Bot.class, new Getter<Bot, SelectInteractionReceived>() {
            @Override
            public Bot get(SelectInteractionReceived event) {
                return Util.botFrom(event.getJDAEvent().getJDA());
            }
        }, 0);

        EventValues.registerEventValue(SelectInteractionReceived.class, String.class, new Getter<String, SelectInteractionReceived>() {
            @Override
            public String get(SelectInteractionReceived event) {
                return event.getJDAEvent().getSelectionMenu().getId();
            }
        }, 0);

        EventValues.registerEventValue(SelectInteractionReceived.class, Guild.class, new Getter<Guild, SelectInteractionReceived>() {
            @Override
            public Guild get(SelectInteractionReceived event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(SelectInteractionReceived.class, GuildChannel.class, new Getter<GuildChannel, SelectInteractionReceived>() {
            @Override
            public GuildChannel get(SelectInteractionReceived event) {
                return event.getJDAEvent().getTextChannel();
            }
        }, 0);

    }

    public class SelectInteractionReceived extends SimpleVixioEvent<SelectionMenuEvent> {
    }

}
