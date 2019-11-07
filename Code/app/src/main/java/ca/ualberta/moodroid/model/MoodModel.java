package ca.ualberta.moodroid.model;

/**
 * Simple implementation of the mood object
 */
public class MoodModel extends BaseModel {

    /**
     * Name of the mood
     */
    private String name;

    /**
     * UTF-8 string of the emoji
     */
    private String emoji;

    /**
     * Hex compatible color as a string (ie: #ababab)
     */
    private String color;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets emoji.
     *
     * @return the emoji
     */
    public String getEmoji() {
        return emoji;
    }

    /**
     * Sets emoji.
     *
     * @param emoji the emoji
     */
    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    /**
     * Gets color.
     *
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets color.
     *
     * @param color the color
     */
    public void setColor(String color) {
        this.color = color;
    }
}
