package framework.gui;

import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import framework.core.DataPlugin;
import framework.core.FrameworkCore;
import framework.core.Media;
import framework.core.Node;
import framework.core.PluginFinder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

/**
 * Main framework. The program must call this main function to start the
 * program. The code is derived from <a href=
 * "https://docs.oracle.com/javase/8/javafx/embedded-browser-tutorial/js-javafx.htm">here</a>
 * 
 * @author sdk1
 */
public class FrameworkGUI extends Application {
  private Scene scene;

  /**
   * Main function run by Gradle.
   * 
   * @param args
   *          argument to the main function.
   */
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    // create scene
    stage.setTitle("Data Explorer");
    scene = new Scene(new MainBrowser(stage), 1000, 600);
    stage.setScene(scene);
    stage.setResizable(false);

    stage.show();
  }
}

/**
 * Main browser that loads the html file.
 * 
 * @author sdk1
 */
class MainBrowser extends Region {
  final WebView browser = new WebView();
  final WebEngine webEngine = browser.getEngine();
  private List<DataPlugin> dataOptions;
  private List<DisplayPlugin> displayOptions;
  private FrameworkCore core;
  private DisplayPlugin displayPlugin;
  private boolean debug = false; // debugging flag that prints debug messages.

  public MainBrowser(final Stage stage) {
    getStyleClass().add("browser");

    webEngine.getLoadWorker().stateProperty()
        .addListener((ObservableValue<? extends State> ov, State oldState, State newState) -> {
          if (newState == Worker.State.SUCCEEDED) {
            JSObject jsObj = (JSObject) webEngine.executeScript("window");
            jsObj.setMember("java", new JavaApp());
          }
        });

    // opens the html file
    URL url = FrameworkGUI.class.getResource("/www/start.html");
    if (url == null) {
      // for eclipse run, change this to relative path
      url = FrameworkGUI.class.getResource("../../../resources/www/");

      if (url == null)
        throw new RuntimeException("Could not find html file.");
    }
    String htmlFile = url.toExternalForm();
    webEngine.load(htmlFile);

    getChildren().add(browser);
  }

  /**
   * This class is created when html file is loaded.
   */
  public class JavaApp {
    /**
     * Case on specific file instance and render the page accordingly.
     */
    public JavaApp() {
      String file = getFileName(webEngine.executeScript("window.location.href").toString());
      switch (file) {
      case "start.html":
        List<String> names = new ArrayList<String>();

        if (dataOptions == null) {
          dataOptions = PluginFinder.getDataPlugins();
        }
        for (DataPlugin data : dataOptions) {
          names.add(data.getDomainName());
        }
        callFunctionWithList("showDataOptions", names);

        names = new ArrayList<String>();

        if (displayOptions == null) {
          displayOptions = PluginFinder.getDisplayPlugins();
        }
        for (DisplayPlugin display : displayOptions) {
          names.add(display.getDisplayName());
        }
        callFunctionWithList("showDisplayOptions", names);
        break;
      case "index.html":
        updateColor();
        callFunction("setAppName", core.getAppName());
        callFunction("setSearchPrompt", core.getPromptString());
        break;
      case "graph.html":
        updateColor();
        callFunction("setnNodes", displayPlugin.getNeighborNum());
        callFunction("setStartPoint", core.getCurrentNode().getName());
        setCurrentNodeOnGraph(core.getCurrentNode());
        callFunction("restart");
        break;
      default:
        break;
      }
    }

    private void updateColor() {
      List<String> colors = new ArrayList<String>();
      colors.add(toHexString(displayPlugin.getLightColor()));
      colors.add(toHexString(displayPlugin.getMediumColor()));
      colors.add(toHexString(displayPlugin.getDarkColor()));
      colors.add(toHexString(displayPlugin.getTextColor()));

      callFunctionWithList("updateColor", colors);
    }

    private String toHexString(Color color) {
      if (color == null)
        return "";
      String hex = Integer.toHexString(color.getRGB() & 0xffffff);
      if (hex.length() < 6) {
        hex = "0" + hex;
      }
      hex = "#" + hex;
      return hex;
    }

    public String getFileName(String filepath) {
      String[] strings = filepath.split("/");
      return strings[strings.length - 1];
    }

    /**
     * Called when start button is clicked in start.html
     */
    public void startClicked(String selectedData, String selectDisplay) {
      for (DataPlugin pg : dataOptions) {
        if (pg.getDomainName().equals(selectedData)) {
          core = new FrameworkCore(pg);
        }
      }

      for (DisplayPlugin pg1 : displayOptions) {
        if (pg1.getDisplayName().equals(selectDisplay)) {
          displayPlugin = pg1;
        }
      }

      if (core == null || displayPlugin == null) {
        throw new RuntimeException();
      }

      callFunction("moveToMain");
    }

    /**
     * Called when explore button is clicked in index.html
     * 
     * @param name
     *          name of the node.
     */
    public void exploreClicked(String name) {
      // set new current node
      try {
        core.setCurrentNode(name);
      } catch (IllegalArgumentException e) {
        throw new RuntimeException();
      } catch (NoSuchElementException e) {
        callFunction("displayErrorMessage");
        return;
      }

      // add title
      callFunction("moveToGraph");
    }

    public void neighborClicked(String name) {
      printMessage("neighborClicked ".concat(name));
      if (!name.equalsIgnoreCase("undefined")) {
        Node node = core.setCurrentNode(name);
        setCurrentNodeOnGraph(node);
      }
    }

    private void setCurrentNodeOnGraph(Node node) {
      // within node
      callFunction("setTitle", node.getTitle());
      String html = node.getMainMedia().getHTML();
      callFunction("setMainMedia", displayPlugin.renderHTML(html));
      String emptyHTML = "<p></p>";
      List<String> submediaHTMLs = new ArrayList<String>();
      int row = displayPlugin.getSubMediaRow();
      int col = displayPlugin.getSubMediaCol();
      submediaHTMLs.add(Integer.toString(row));
      submediaHTMLs.add(Integer.toString(col));

      try {
        Iterator<Media> submedias = node.getSubMediaIterator();

        for (int i = 0; i < (row * col); i++) {
          if (submedias.hasNext()) {
            submediaHTMLs.add(displayPlugin.renderHTML(submedias.next().getHTML()));
          } else {
            submediaHTMLs.add(emptyHTML);
          }
        }
      } catch (NoSuchElementException e) {
        // no submedia iterator
        for (int i = 0; i < (row * col); i++) {
          submediaHTMLs.add(emptyHTML);
        }
      }

      callFunctionWithList("setSubMedia", submediaHTMLs);

      // neighbors
      List<Node> neighbors = node.getNeighbors(displayPlugin.getNeighborNum());
      List<String> neighNames = new ArrayList<String>();
      for (Node neigh : neighbors)
        neighNames.add(neigh.getName());

      callFunctionWithList("setNeighborNodes", neighNames);
      callFunction("setStartPoint", node.getName());
    }

    public void printMessage(Object msg) {
      if (debug)
        System.out.println(msg);
    }

    public Object callFunction(String functionName) {
      try {
        printMessage("Calling ".concat(functionName.concat("()")));
        Object res = webEngine.executeScript(functionName.concat("()"));
        printMessage(res);
        return true;
      } catch (Exception e) {
        printMessage("ERROR!! in executing function");
        return null;
      }
    }

    public Object callFunction(String functionName, Object arg) {
      try {
        StringBuilder str = new StringBuilder();

        str.append(functionName);
        str.append("(\"");
        str.append(arg.toString().replaceAll("\"", "\\\\\"").replaceAll("\n", ""));
        str.append("\")");
        printMessage("Calling ".concat(str.toString()));

        Object res = webEngine.executeScript(str.toString());
        printMessage(res);
        return res;
      } catch (Exception e) {
        printMessage("ERROR!! in executing function");
        return null;
      }
    }

    public Object callFunctionWithList(String functionName, List<String> list) {
      try {
        StringBuilder str = new StringBuilder();
        str.append(functionName);
        str.append("([");

        for (int i = 0; i < list.size(); i++) {
          str.append("\"");
          str.append(list.get(i).replaceAll("\"", "\\\\\"").replaceAll("\n", ""));

          str.append("\"");
          if (i < list.size() - 1) {
            str.append(",");
          }
        }

        str.append("])");
        printMessage("Calling ".concat(str.toString()));

        Object res = webEngine.executeScript(str.toString());
        printMessage(res);
        return res;
      } catch (Exception e) {
        printMessage("ERROR!! in executing function");
        return null;
      }
    }

    public void exit() {
      printMessage("exiting");
      Platform.exit();
    }
  }

  @Override
  protected void layoutChildren() {
    layoutInArea(browser, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.CENTER);
  }
}