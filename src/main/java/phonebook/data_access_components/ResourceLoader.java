package phonebook.data_access_components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class ResourceLoader {

    private final String[] resourceNames={
            "logo",
            "add",
            "delete",
            "edit",
            "telegram",
            "vk",
            "ok",
            "facebook",
            "twitter",
            "instagram",
            "mail",
            "to_down",
            "to_up"
    };

    private final String resourcePrefix = "images/";
    private final String resourceSuffix = ".png";

    private final HashMap<String, Image> resourceMap = new HashMap<>();

    public void loadResource() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        Image image;
        for (String name: resourceNames){
            image = ImageIO.read(classLoader.getResourceAsStream(resourcePrefix + name + resourceSuffix));
            resourceMap.put(name, image);
        }
    }

    public Image getImageResource(String name){
        return resourceMap.get(name);
    }

    public ImageIcon getImageIconResource(String name){
        Image image = resourceMap.get(name);
        return name==null?null:new ImageIcon(image);
    }

}
