package net.aether.module;

public enum Category
{
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    EXPLOIT("Exploit"),
    PLAYER("Player"),
    WORLD("World"),
    MISC("Misc");

    private final String displayName;

    Category(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }
}
