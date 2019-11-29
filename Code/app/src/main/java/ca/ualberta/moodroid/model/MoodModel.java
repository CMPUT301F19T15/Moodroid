package ca.ualberta.moodroid.model;

/**
 * Simple implementation of the mood object. These mood objects currently include our 6 moods:
 * <p>
 * Happy
 * Angry
 * Sad
 * Sick
 * Sacred
 * Annoyed
 * <p>
 * This class will define the attributes that are associated and carried throughout the entire
 * app for each mood
 */
public class MoodModel extends BaseModel {

    /**
     * Name of the mood, which is one of the 6 names above.
     */
    private String name;

    /**
     * UTF-8 string of the emoji. Each emoji is thematically associated with each mood
     */
    private String emoji;

    /**
     * Hex compatible colour as a string (ie: #ababab). Each color is also thematically associated
     * with each mood, such as the colour red relates with an Angry mood, or blue with a Sad mood
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
