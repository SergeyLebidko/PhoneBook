package phonebook.data_access_components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

import phonebook.AccountTypes;

public class ResourceLoader {

    private final String[] resourceNames = {
            "logo",
            "add",
            "delete",
            "edit",
            "to_down",
            "to_up",
            "clean"
    };

    private final String resourcePrefix = "images/";
    private final String resourceSuffix = ".png";

    private final HashMap<String, Image> resourceMap = new HashMap<>();

    public void loadResource() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        Image image;
        for (String name : resourceNames) {
            image = ImageIO.read(classLoader.getResourceAsStream(resourcePrefix + name + resourceSuffix));
            resourceMap.put(name, image);
        }
        for (AccountTypes accountType : AccountTypes.values()) {
            image = ImageIO.read(classLoader.getResourceAsStream(resourcePrefix + accountType.getType() + resourceSuffix));
            resourceMap.put(accountType.getType(), image);
        }
    }

    public Image getImageResource(String name) {
        return resourceMap.get(name);
    }

    public ImageIcon getImageIconResource(String name) {
        Image image = resourceMap.get(name);
        return name == null ? null : new ImageIcon(image);
    }

}
