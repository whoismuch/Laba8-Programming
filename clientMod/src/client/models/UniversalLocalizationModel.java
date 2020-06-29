package client.models;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.CharArrayReader;
import java.util.*;

public class UniversalLocalizationModel {

    private String normalResult;

    public void setNormalResult (String normalResult) {
        this.normalResult = normalResult;
    }

    private void addAllDescendents (Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable( )) {
            if (!nodes.contains(node)) {
                nodes.add(node);
                if (node instanceof Parent) {
                    if (!((Parent) node).getChildrenUnmodifiable( ).isEmpty( )) {
                        addAllDescendents((Parent) node, nodes);
                    }
                }
            }
        }

    }


    public void changeLanguage (Parent root, ResourceBundle bundle) {
        ArrayList<Node> nodes = new ArrayList<>( );
        addAllDescendents(root, nodes);
        nodes.add(root);
        int i = 0;
        for (Node node : nodes) {
            try {
                if ((node instanceof Labeled) & (node != null) & !(node instanceof Cell)) {
                    ((Labeled) node).setText(bundle.getString(node.getId( )));
                }

                if (node instanceof TableView) {
                    for (Object columns : ((TableView) node).getColumns( )) {
                        TableColumn<?, ?> column = (TableColumn) columns;
                        column.setText(bundle.getString(column.getId( )));
                    }
                }

                if (node instanceof TabPane) {
                    for (Tab tab : ((TabPane) node).getTabs( )) {
                        tab.setText(bundle.getString(tab.getId( )));

                    }
                }

                if (node instanceof TextArea) {
                    TextArea textArea = (TextArea) node;
                    textArea.setText(translateMeAText(bundle));
                }
            } catch (NullPointerException | MissingResourceException ex) {
            }
        }

    }

    public void updateLabels (Labeled labeled, String example, ResourceBundle bundle) {
        try {
            labeled.setText(bundle.getString(example));
        } catch (MissingResourceException | NullPointerException ex) {
        }
    }

    public String translateMeAText (ResourceBundle bundle) {
        CharArrayReader car = new CharArrayReader(normalResult.toCharArray( ));
        Scanner scanner = new Scanner(car);
        String translateResult = "";
        String finalResult = "";
        while (scanner.hasNextLine( )) {
            translateResult = scanner.nextLine( );
            Set<String> keys = bundle.keySet( );
            for (String key : keys) {
                if (translateResult.contains(key)) {
                    translateResult = translateResult.replaceAll(key, bundle.getString(key));
                }
            }

            finalResult = finalResult + translateResult + "\n";
        }

        return finalResult;
    }

}
