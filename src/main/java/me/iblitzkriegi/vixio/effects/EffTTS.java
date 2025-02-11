package me.iblitzkriegi.vixio.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.Vixio;
import me.iblitzkriegi.vixio.util.skript.EasyMultiple;
import net.dv8tion.jda.api.MessageBuilder;
import org.bukkit.event.Event;


/**
 * A more natural english alternative to {@link me.iblitzkriegi.vixio.expressions.message.builder.ExprMessageBuilderTts}
 */
public class EffTTS extends Effect implements EasyMultiple<MessageBuilder, Void> {

    static {
        Vixio.getInstance().registerEffect(EffTTS.class,
                "(1¦enable|2¦disable) t[ext ]t[o ]s[peech] for %messagebuilders%")
                .setName("Change TTS")
                .setDesc("Lets you enable or disable text to speech for message builders")
                .setUserFacing("(enable|disable) t[ext ]t[o ]s[peech] for %messagebuilders%")
                .setExample("enable tts for {_message builder}");
    }

    private boolean tts;
    private Expression<MessageBuilder> builders;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        builders = (Expression<MessageBuilder>) exprs[0];
        tts = parseResult.mark == 1;
        return true;
    }

    @Override
    protected void execute(Event e) {
        change(builders.getAll(e), builder -> builder.setTTS(tts));
    }

    @Override
    public String toString(Event e, boolean debug) {
        return (tts ? "enable" : "disable") + "tts for " + builders.toString(e, debug);
    }

}
