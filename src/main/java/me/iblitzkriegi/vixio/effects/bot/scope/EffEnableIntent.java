package me.iblitzkriegi.vixio.effects.bot.scope;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.Vixio;
import me.iblitzkriegi.vixio.scopes.ScopeMakeBot;
import me.iblitzkriegi.vixio.util.scope.EffectSection;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.event.Event;

public class EffEnableIntent extends Effect {

    static {
        Vixio.getInstance().registerEffect(EffEnableIntent.class,
                "enable [the] %gatewayintent% [gateway] intent [for bot]")
                .setName("Enable Gateway Intent")
                .setDesc("This is used specifically to enable specific gateway intents in the create bot scope. This must be done before you login to your bot and you must restart for changes to take effect.")
                .setExample(
                        "on skript load:",
                        "\tcreate vixio bot:",
                        "\t\tenable the guild members intent",
                        "\t\tlogin to \"YAHITAMUH\" with the name \"Neeko\""
                );
    }

    private Expression<GatewayIntent> gatewayIntent;

    @Override
    protected void execute(Event e) {
        GatewayIntent gatewayIntent = this.gatewayIntent.getSingle(e);
        if (gatewayIntent == null) {
            return;
        }
        ScopeMakeBot.jdaBuilder.enableIntents(gatewayIntent);
        switch (gatewayIntent) {
            case GUILD_MEMBERS:
                ScopeMakeBot.jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
                break;
            case GUILD_PRESENCES:
                ScopeMakeBot.jdaBuilder.enableCache(CacheFlag.CLIENT_STATUS);
                ScopeMakeBot.jdaBuilder.enableCache(CacheFlag.ACTIVITY);
                break;
            case GUILD_VOICE_STATES:
                ScopeMakeBot.jdaBuilder.enableCache(CacheFlag.VOICE_STATE);
                break;
            case GUILD_EMOJIS_AND_STICKERS:
                ScopeMakeBot.jdaBuilder.enableCache(CacheFlag.EMOJI);
                break;
        }


    }

    @Override
    public String toString(Event e, boolean debug) {
        return "enable the " + gatewayIntent.toString() + " intent";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!EffectSection.isCurrentSection(ScopeMakeBot.class)) {
            Skript.warning("The enable intents effect may only be used within the create bot scope");
            return false;
        }
        gatewayIntent = (Expression<GatewayIntent>) exprs[0];
        return true;
    }
}
