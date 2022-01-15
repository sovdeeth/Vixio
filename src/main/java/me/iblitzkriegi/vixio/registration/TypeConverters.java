package me.iblitzkriegi.vixio.registration;

import ch.njol.skript.registrations.Converters;
import me.iblitzkriegi.vixio.util.UpdatingMessage;
import me.iblitzkriegi.vixio.util.wrapper.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class TypeConverters {
    public static void register() {
        /*
        Generally a bad idea for the same reasons skript doesn't have a player -> name converter
        Converters.registerConverter(ISnowflake.class, String.class, (Converter<ISnowflake, String>) u -> u.getId());
        Converters.registerConverter(Emoji.class, String.class, (Converter<Emoji, String>) u -> u.isEmote() ? u.getEmote().getAsMention() : u.getName());
        Converters.registerConverter(Category.class, String.class, (Converter<Category, String>) u -> u.getName());
        Converters.registerConverter(Avatar.class, String.class, (Converter<Avatar, String>) u -> u.getAvatar());
        Converters.registerConverter(Bot.class, String.class, (Converter<Bot, String>) u -> u.getSelfUser().getId());
        Converters.registerConverter(DiscordCommand.class, String.class, (Converter<DiscordCommand, String>) c -> c.getName());
        Converters.registerConverter(Member.class, String.class, (Converter<Member, String>) u -> u.getUser().getId());
        */
        Converters.registerConverter(ch.njol.skript.util.Color.class, java.awt.Color.class, color -> {
            org.bukkit.Color bukkitColor = color.asBukkitColor();
            return new java.awt.Color(bukkitColor.getRed(), bukkitColor.getGreen(), bukkitColor.getBlue());
        });
        Converters.registerConverter(EmbedBuilder.class, MessageEmbed.class,
                e -> e.isEmpty() ? null : e.build()
        );
		Converters.registerConverter(MessageBuilder.class, UpdatingMessage.class,
                builder -> {
                    UpdatingMessage m = builder.isEmpty() ? null : UpdatingMessage.from(builder.build());
                    return m;
                });
        Converters.registerConverter(Member.class, User.class,
                Member::getUser);
		Converters.registerConverter(EmbedBuilder.class, UpdatingMessage.class,
                b -> b.isEmpty() ? null : UpdatingMessage.from(new MessageBuilder().setEmbeds(b.build()).build()));
        Converters.registerConverter(Bot.class, User.class,
                Bot::getSelfUser);
    }
}
