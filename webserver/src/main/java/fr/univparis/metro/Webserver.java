package fr.univparis.metro;
import java.io.File;
import io.javalin.plugin.rendering.template.TemplateUtil;
import io.javalin.*;
import java.util.HashMap;


public class Webserver {
  public static void main(String[] args) {
    Configuration.loadFrom(new File("src/main/resources/cities.json"));
    Javalin app = launch();
    installIndex(app);
    InstallItinerary(app);
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
      ctx.render("/public/index.ftl", TemplateUtil.model ( "cities", WebserverLib.toOption() ) );
    });
  }

  public static void InstallItinerary(Javalin app) {
    app.post("/itinerary", ctx -> {
        WGraph<Station> g = Parser.loadFrom(new File(Configuration.getFileName(ctx.formParam("city"))));
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


}
