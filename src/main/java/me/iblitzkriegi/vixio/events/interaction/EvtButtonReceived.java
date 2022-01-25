package me.iblitzkriegi.vixio.events.interaction;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import me.iblitzkriegi.vixio.events.base.BaseEvent;
import me.iblitzkriegi.vixio.events.base.SimpleVixioEvent;
import me.iblitzkriegi.vixio.util.Util;
import me.iblitzkriegi.vixio.util.wrapper.Bot;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class EvtButtonReceived extends BaseEvent<ButtonClickEvent> {
    static {
        BaseEvent.register("button interaction received", EvtButtonReceived.class, ButtonInteractionReceived.class,
                "button interaction receive[d]")
                .setName("Slash Command Received")
                .setDesc("Fired when a button is clicked.")
                .setExample("on button interaction received:");

        EventValues.registerEventValue(ButtonInteractionReceived.class, User.class, new Getter<User, ButtonInteractionReceived>() {
            @Override
            public User get(ButtonInteractionReceived event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

        EventValues.registerEventValue(ButtonInteractionReceived.class, MessageChannel.class, new Getter<MessageChannel, ButtonInteractionReceived>() {
            @Override
            public MessageChannel get(ButtonInteractionReceived event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(ButtonInteractionReceived.class, Member.class, new Getter<Member, ButtonInteractionReceived>() {
            @Override
            public Member get(ButtonInteractionReceived event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(ButtonInteractionReceived.class, Bot.class, new Getter<Bot, ButtonInteractionReceived>() {
            @Override
            public Bot get(ButtonInteractionReceived event) {
                return Util.botFrom(event.getJDAEvent().getJDA());
            }
        }, 0);

        EventValues.registerEventValue(ButtonInteractionReceived.class, String.class, new Getter<String, ButtonInteractionReceived>() {
            @Override
            public String get(ButtonInteractionReceived event) {
                return event.getJDAEvent().getButton().getLabel();
            }
        }, 0);

        EventValues.registerEventValue(ButtonInteractionReceived.class, Guild.class, new Getter<Guild, ButtonInteractionReceived>() {
            @Override
            public Guild get(ButtonInteractionReceived event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(ButtonInteractionReceived.class, GuildChannel.class, new Getter<GuildChannel, ButtonInteractionReceived>() {
            @Override
            public GuildChannel get(ButtonInteractionReceived event) {
                return event.getJDAEvent().getTextChannel();
            }
        }, 0);

    }

    public class ButtonInteractionReceived extends SimpleVixioEvent<ButtonClickEvent> {
    }

}
