package fr.univparis.metro;
import java.io.IOException;
import java.io.File;
import io.javalin.plugin.rendering.template.TemplateUtil;
import io.javalin.*;
import java.util.HashMap;
import java.util.Set;


public class Webserver {
  public static void main(String[] args) throws IOException {
    Configuration.loadFrom(new File("src/main/resources/cities.json"));
    Trafics.initTrafics();
    Javalin app = launch();
    installIndex(app);
    installCity(app);
    installToCity(app);
    InstallItinerary(app);
    installAddPerturbation(app);
  }


  public static Javalin launch() {
    Javalin app = Javalin.create (config -> {
      // config.addStaticFiles ("public/");
      config.enableWebjars ();
    }).start (8080);
    return app;
  }

  public static void installIndex(Javalin app) {
    app.get("/", ctx -> {
      ctx.render("/public/index.ftl", TemplateUtil.model ( "cities", WebserverLib.toOption() ));
    });
  }

  public static void installToCity(Javalin app) {
    app.post("/toCity", ctx -> {
      ctx.redirect("/" + ctx.formParam("city"));
    });
  }

  public static void installCity(Javalin app) {
    app.get("/:city", ctx -> {
      String city = ctx.pathParam("city");
      Set<String> cities = Trafics.getCities();
      if (! cities.contains(city)) {
        app.error(404, c -> {});
        return;
      }
      ctx.render("/public/city.ftl", TemplateUtil.model(
        "city", city,
        "perturbation", WebserverLib.perturbationToHtml(Trafics.getPertubation(city))
      ));
    });
  }

  public static void InstallItinerary(Javalin app) {
    app.post("/:city/itinerary", ctx -> {
        WGraph<Station> g = Trafics.getGraph(ctx.pathParam("city"));
        Station start = new Station(ctx.formParam("start"), "Meta Station Start");
        Station end = new Station(ctx.formParam("end"), "Meta Station End");
        if (! g.getVertices().contains(start) || !g.getVertices().contains(end)){
          ctx.redirect("/");
        }
        else {
          if (ctx.formParam("type").equals("shortest")) {
            HashMap<Station, Station> prev = new HashMap<Station, Station>();
            HashMap<Station, Double> dist = new HashMap<Station, Double>();
            Dijkstra.shortestPath(g, start, prev, dist);
            ctx.render("/public/itinerary.ftl", TemplateUtil.model(
            "time", WebserverLib.time(dist.get(end)),
            "itinerary", WebserverLib.path(prev, end)
            ));
          }
        }
    });
  }

  public static void installAddPerturbation(Javalin app) {
    app.post("/:city/addPertubation", ctx -> {
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
      }

      Trafics.addPertubation(city, type, name, parameter);
      ctx.redirect("/" + city);
    });
  }


}
