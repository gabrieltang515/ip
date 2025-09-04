package atlas;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Atlas atlas;

    private Image userImage;
    private Image atlasImage;

    @FXML
    public void initialize() {

        try {
            userImage = new Image(getClass().getResourceAsStream("/images/DaUser.png"));
            atlasImage = new Image(getClass().getResourceAsStream("/images/DaDuke.png"));
        } catch (Exception e) {
            System.err.println("Failed to load images: " + e.getMessage());
            // You could set default images or handle this gracefully
        }

        dialogContainer.heightProperty().addListener((observable, oldValue, newValue) -> {
            scrollPane.setVvalue(1.0);
        });
    }

    /** Injects the Duke instance */
    public void setAtlas(Atlas a) {
        atlas = a;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = atlas.getResponse(input);
        String commandType = atlas.getCommandType();
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDukeDialog(response, atlasImage)
        );
        userInput.clear();
    }

}
