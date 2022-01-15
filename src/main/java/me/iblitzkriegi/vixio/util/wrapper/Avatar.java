package me.iblitzkriegi.vixio.util.wrapper;

import net.dv8tion.jda.api.entities.User;

public class Avatar {
    private final User user;
    private final String avatar;
    private final boolean isDefault;

    public Avatar(User user, String avatar, boolean isDefault) {
        this.user = user;
        this.avatar = avatar;
        this.isDefault = isDefault;
    }

    public User getUser() {
        return user;
    }

    public String getAvatar() {
        return avatar;
    }

    public boolean isDefault() {
        return isDefault;
    }
}
