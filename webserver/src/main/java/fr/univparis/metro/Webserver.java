package fr.univparis.metro;
import io.javalin.*;
import io.javalin.plugin.rendering.template.TemplateUtil;
import java.io.IOException;
import java.util.List;
import java.util.Set;
/**
 * Main class for the Webserver where all the webpages are installed
 */
public class Webserver {
  public static void main(String[] args) throws IOException {
    Configuration.loadFrom(Webserver.class.getResourceAsStream("/cities.json"));
    Trafics.initTrafics();
    StatisticsVue.initStatistics();
    Javalin app = launch();
    installIndex(app);
    installCity(app);
    installToCity(app);
    installItinerary(app);
    installAddPerturbation(app);
    installRemovePerturbation(app);
    installStatistics(app);
  }

 /**
  * Launch the webserver
  * @return the javalin app that has be created
  */
  public static Javalin launch() {
    Javalin app = Javalin.create (config -> {
      // config.addStaticFiles ("public/");
      config.enableWebjars ();
    }).start (8080);
    return app;
  }

  private static void installIndex(Javalin app) {
    app.get("/", ctx -> {
      ctx.render("/public/index.ftl", TemplateUtil.model ( "cities", WebserverLib.toOption() ));
    });
  }

  private static void installToCity(Javalin app) {
    app.post("/toCity", ctx -> {
      ctx.redirect("/" + ctx.formParam("city"));
    });
  }


  private static void installItinerary(Javalin app) {
    app.post("/:city/itinerary", ctx -> {
      WGraph<Station> g = Trafics.getGraph(ctx.pathParam("city")).clone();
      Station start = new Station(ctx.formParam("start"), "Meta Station Start");
      Station end = new Station(ctx.formParam("end"), "Meta Station End");
      String body = "";
      if (! g.getVertices().contains(start)){
        body = start.getName() + " doesn't exist";
      }
      else if (! g.getVertices().contains(end)) {
        body = end.getName() + " doesn't exist";
      }
      else if (ctx.formParam("type").equals("shortest")) {
        body = PathVue.multiplePath(g, start, end);
      }
      else if(ctx.formParam("type").equals("leastConnexion")){
        body = PathVue.limitedConnectionPath(g, start, end);
      }
      else if(ctx.formParam("type").equals("leastConnexionFloyd")){
        body = PathVue.limitedConnexionPathWithFloyd(g, start, end);
      }
      ctx.render("/public/itinerary.ftl", TemplateUtil.model("body", body));
    });
  }


  private static void installCity(Javalin app) {
    app.get("/:city", ctx -> {
      String city = ctx.pathParam("city");
      Set<String> cities = Trafics.getCities();
      if (! cities.contains(city)) {
        app.error(404, c -> {});
        return;
      }
      ctx.render("/public/city.ftl", TemplateUtil.model(
      "city", city,
      "perturbation", WebserverLib.perturbationToHtml(city),
      "arrayStation", ListStation.getListStation(city)
      ));
    });
  }


  private static void installAddPerturbation(Javalin app) {
    app.post("/:city/addPerturbation", ctx -> {
      String city = ctx.pathParam("city");
      String name = ctx.formParam("name");
      Trafics.Perturbation type = Trafics.Perturbation.valueOf(ctx.formParam("type"));
      Object parameter = null;
      switch (type) {
        case LINE_SHUTDOWN:
        parameter = ctx.formParam("line");
        break;
        case LINE_SLOW_DOWN:
        parameter = new Pair<String, Double>(ctx.formParam("line"), Double.valueOf(ctx.formParam("times")));
        break;
        case ENTIRE_STATION_SHUT_DOWN:
        parameter = ctx.formParam("station_name");
        break;
        case PART_STATION_SHUT_DOWN:
        parameter = new Station(ctx.formParam("station_name"), ctx.formParam("station_line"));
        break;
        case PART_LINE_SHUT_DOWN:
        String line1 = ctx.formParam("line");
        parameter = new Pair<Station, Station>(new Station(ctx.formParam("start_station"), line1), new Station(ctx.formParam("end_station"), line1));
        break;
        case PART_LINE_SLOW_DOWN:
        parameter = new Object[3];
        String line2 = ctx.formParam("line");
        ((Object[]) parameter)[0] = new Station(ctx.formParam("start_station"), line2);
        ((Object[]) parameter)[1] = new Station(ctx.formParam("end_station"), line2);
        ((Object[]) parameter)[2] = Double.valueOf(ctx.formParam("times"));
        break;
        case ALL_TRAFICS_SLOW_DOWN:
        parameter = Double.valueOf(ctx.formParam("times"));
        break;
      }

      Trafics.addPerturbation(city, type, name, parameter);
      ctx.redirect("/" + city);
    });
  }

  private static void installRemovePerturbation(Javalin app) {
    app.post("/:city/removePerturbation", ctx -> {
      String city = ctx.pathParam("city");
      List<String> l = ctx.formParams("removePerturbation");
      for (String s : l) {
        Trafics.revertPerturbation(city, s);
      }
      ctx.redirect("/" + city);
    });
  }

  private static void installStatistics(Javalin app){
    app.get("/:city/statistics", ctx -> {
      String city = ctx.pathParam("city");
      ctx.render("/public/statistics.ftl", TemplateUtil.model(
      "stat1", StatisticsVue.getStringStatistics(city)
      ));
    });
  }


}
