<!DOCTYPE html>
<html lang="fr" dir="ltr">
  <head>
    <style type="text/css">
      td, th {
        border: 1px solid;
        text-align:center;
      }
      .suggestions{
        width: 200px;
      }
      .suggestions > div{
        padding: 5px;
        font-size: 15px;
        border-top: 1px solid #666;
        width: 200px;
      }
      .suggestions > div:hover{
        background-color: #1e4dd4;
      }
      #svp{
        text-decoration: underline;
      }
    </style>

    <meta charset="utf-8">
    <meta content="IE=edge" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <link rel="stylesheet" href="assets/css/main.css">
    <title>Metro</title>
  </head>
  <body>
    <h2>Itinerary search</h2>
    <p><em>To make auto-completion work, <span id="svp">please write the stations in lowercase letters.</span></em></p>
    <form class="form" action="/${city}/itinerary" method="post" class="form">
      <label for="start">From</label>
      <div class="container">
        <input id="start" type="text" name="start" autocomplete="off" required>
        <div class="suggestions" id="resultsStart"></div>
        <br>
        <label for="end">To</label>
        <input id="end" type="text" name="end" autocomplete="off" required>
        <div class="suggestions" id="resultsEnd"></div>
        <br>
      </div>
      <input type="radio" name="type" value="shortest" required id="shortest"> <label for="shortest">Shortest</label><br>
      <input type="radio" name="type" value="leastConnexion" required id="leastConnexion"> <label for="leastConnexion">Limited connexion with Bouarah's algorithm</label><br>
      <input type="radio" name="type" value="leastConnexionFloyd" required id="leastConnexionFloyd"> <label for="leastConnexionFloyd">Limited connexion with Floyd's algorithm (Warning : we can not add pertubations with this algorithm)</label><br>
      <input type="submit">
    </form>

    <h2>Trafics Perturbation</h2>
    <h3>Current Perturbation</h3>
    ${perturbation}
    <h3>Add perturbation</h3>

    <h4>Line shutdown</h4>
    <form class="form" action="/${city}/addPerturbation" method="post">
      <label for="line_shutdown_name">Name of the perturbation</label>
      <input required type="text" name="name" id="line_shutdown_name"><br>

      <label for="line_shutdown_line">Line you want to shutdown</label>
      <input required type="text" name="line" id="line_shutdown_line"><br>

      <input type="hidden" name="type" value="LINE_SHUTDOWN">
      <input type="submit">
    </form>

    <h4>Line slowdown</h4>
    <form class="form" action="/${city}/addPerturbation" method="post">
      <label for="line_slow_down_name">Name of the perturbation</label>
      <input required type="text" name="name" id="line_slow_down_name"><br>

      <label for="line_slow_down_line">Line you want to slow down</label>
      <input required type="text" name="line" id="line_slow_down_line"><br>

      <label for="line_slow_down_times">Times which multiply every traject in the line</label>
      <input required type="number" step="any" min="1" name="times" id="line_slow_down_times"><br>

      <input type="hidden" name="type" value="LINE_SLOW_DOWN">
      <input type="submit">
    </form>

    <h4>Station shutdown</h4>
    <form class="form" action="/${city}/addPerturbation" method="post">
      <label for="station_shutdown_name">Name of the perturbation</label>
      <input required type="text" name="name" id="station_shutdown_name"><br>

      <label for="station_shutdown_station_name">Station you want to shutdown</label>
      <input required type="text" name="station_name" id="station_shutdown_station_name"><br>
      <div class="suggestions" id="results_station_shutdown_station_name"></div>

      <input type="hidden" name="type" value="ENTIRE_STATION_SHUT_DOWN">
      <input type="submit">
    </form>

    <h4>One line of station shutdown</h4>
    <form class="form" action="/${city}/addPerturbation" method="post">
      <label for="part_station_shut_down_name">Name of the perturbation</label>
      <input required type="text" name="name" id="part_station_shut_down_name"><br>

      <label for="part_station_shut_down_station_name">Name of the station you want to shutdown</label>
      <input class="autocompletion" required type="text" name="station_name" id="part_station_shut_down_station_name"><br>
      <div class="suggestions" id="results_part_station_shut_down_station_name"></div>

      <label for="part_station_shut_down_station_line">Line you can't use in this station</label>
      <input required type="text" name="station_line" id="part_station_shut_down_station_line"><br>

      <input type="hidden" name="type" value="PART_STATION_SHUT_DOWN">
      <input type="submit">
    </form>

    <h4>Part of a line shutdown</h4>
    <form class="form" action="/${city}/addPerturbation" method="post">
      <label for="part_line_shut_down_name">Name of the perturbation</label>
      <input required type="text" name="name" id="part_line_shut_down_name"><br>

      <label for="part_line_shut_down_line">Line concerned</label>
      <input required type="text" name="line" id="part_line_shut_down_line"><br>

      <label for="part_line_shut_down_start_station">Start of the perturbation</label>
      <input class="autocompletion" required type="text" name="start_station" id="part_line_shut_down_start_station"><br>
      <div class="suggestions" id="results_part_line_shut_down_start_station"></div>

      <label for="part_line_shut_down_end_station" >End of the perturbation</label>
      <input class="autocompletion" required type="text" name="end_station" id="part_line_shut_down_end_station"><br>
      <div class="suggestions" id="results_part_line_shut_down_end_station"></div>

      <input type="hidden" name="type" value="PART_LINE_SHUT_DOWN">
      <input type="submit">
    </form>

    <h4>Part of a line slowdown</h4>
    <form class="form" action="/${city}/addPerturbation" method="post">
      <label for="part_line_slow_down_name">Name of the perturbation</label>
      <input required type="text" name="name" id="part_line_slow_down_name"><br>

      <label for="part_line_slow_down_line">Line concerned</label>
      <input required type="text" name="line" id="part_line_slow_down_line"><br>

      <label for="part_line_slow_down_start_station">Start of the perturbation</label>
      <input class="autocompletion" equired type="text" name="start_station" id="part_line_slow_down_start_station"><br>
      <div class="suggestions" id="results_part_line_slow_down_start_station"></div>

      <label for="part_line_slow_down_end_station">End of the perturbation</label>
      <input class="autocompletion" required type="text" name="end_station" id="part_line_slow_down_end_station"><br>
      <div class="suggestions" id="results_part_line_slow_down_end_station"></div>

      <label for="part_line_slow_down_times">Times which multiply every traject in the line</label>
      <input class="autocompletion" required type="number" step="any" name="times" id="part_line_slow_down_times"><br>

      <input type="hidden" name="type" value="PART_LINE_SLOW_DOWN">
      <input type="submit">
    </form>

    <h4>All trafics slowdown</h4>
    <form class="form" action="/${city}/addPerturbation" method="post">
      <label for="all_trafics_slow_down_name">Name of the perturbation</label>
      <input required type="text" name="name" id="all_trafics_slow_down_name"><br>

      <label for="all_trafics_slow_down_times">Times which multiply every traject</label>
      <input required type="number" step="any" min="1" name="times" id="all_trafics_slow_down_times"><br>

      <input type="hidden" name="type" value="ALL_TRAFICS_SLOW_DOWN">
      <input type="submit">
    </form>

    <script type="text/javascript">
    const listStation = ${arrayStation};

    function autocompletion(searchInputID, divResultsID){
      const results = document.getElementById(divResultsID);
      const searchInput = document.getElementById(searchInputID);
      searchInput.addEventListener('keyup', function(){
        const input = searchInput.value;
        results.innerHTML = '';
        if(input.length > 0){
          const suggestions = listStation.filter(function(station){
            return station.toLowerCase().startsWith(input);
          });
          suggestions.forEach(function(suggested){
            const div = document.createElement('div');
            div.innerHTML = suggested;
            div.addEventListener('click', function(){
              searchInput.value = div.innerHTML;
              results.innerHTML = '';
            });
            results.appendChild(div);
          });
        }
      });
    }
    autocompletion("start", "resultsStart");
    autocompletion("end", "resultsEnd");
    autocompletion("station_shutdown_station_name", "results_station_shutdown_station_name");
    autocompletion("part_station_shut_down_station_name", "results_part_station_shut_down_station_name");
    autocompletion("part_line_shut_down_start_station", "results_part_line_shut_down_start_station");
    autocompletion("part_line_shut_down_end_station", "results_part_line_shut_down_end_station");
    autocompletion("part_line_slow_down_start_station", "results_part_line_slow_down_start_station");
    autocompletion("part_line_slow_down_end_station", "results_part_line_slow_down_end_station");
    </script>
    <br>
    <h2>Statistics</h2>
    <p>To acceed to the statistics of the network, click <a href="/${city}/statistics">here</a> and please wait few seconds.</p>
  </body>
</html>
