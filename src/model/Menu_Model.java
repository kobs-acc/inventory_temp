package model;

import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Menu_Model {

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuType getType() {
        return type;
    }

    public void setType(MenuType type) {
        this.type = type;
    }

    public Menu_Model(String icon, String name, MenuType type) {
        this.icon = icon;
        this.name = name;
        this.type = type;
    }

    public Menu_Model() {
    }

    private String icon;
    private String name;
    private MenuType type;

    public Icon toIcon() {
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/" + icon + ".png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage); // Return the scaled icon
    }
    


    public static enum MenuType {
        TITLE, MENU, EMPTY
    }
}