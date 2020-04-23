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
    </style>

    <meta charset="utf-8">
    <meta content="IE=edge" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <link rel="stylesheet" href="assets/css/main.css">
    <title>Metro</title>
  </head>
  <body>
    <h2>Itinerary search</h2>
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
      <input type="radio" name="type" value="leastConnexion" required id="leastConnexion"> <label for="leastConnexion">Limited connexion</label><br>
      <input type="submit">
    </form>

    <script type="text/javascript">
      const listStation = ${arrayStation};
      const resultsStart = document.getElementById("resultsStart");
      const resultsEnd = document.getElementById("resultsEnd");
      const searchInputStart = document.getElementById("start");
      const searchInputEnd = document.getElementById("end");
      searchInputStart.addEventListener('keyup', function(){
        const inputStart = searchInputStart.value;
        resultsStart.innerHTML = '';
        if(inputStart.length > 0){
          const suggestions = listStation.filter(function(station){
            return station.toLowerCase().startsWith(inputStart);
          });
          suggestions.forEach(function(suggested){
            const div = document.createElement('div');
            div.innerHTML = suggested;
            div.addEventListener('click', function(){
              searchInputStart.value = div.innerHTML;
              resultsStart.innerHTML = '';
            });
            resultsStart.appendChild(div);
          });
        }
      });
      searchInputEnd.addEventListener('keyup', function(){
        const inputEnd = searchInputEnd.value;
        resultsEnd.innerHTML = ''; 
        if(inputEnd.length > 0){
          const suggestions = listStation.filter(function(station){
            return station.toLowerCase().startsWith(inputEnd);
          });
          suggestions.forEach(function(suggested){
            const div = document.createElement('div');
            div.innerHTML = suggested;
            div.addEventListener('click', function(){
              searchInputEnd.value = div.innerHTML;
              resultsEnd.innerHTML = '';
            });
            resultsEnd.appendChild(div);
          });
        }
      });
    </script>

    <h2>Trafics Perturbation</h2>
    <h3>Current Perturbation</h3>
    ${perturbation}
    <h3>Add perturbation</h3>
    <form class="form" action="/${city}/addPerturbation" method="post">

      <table>
        <tr>
          <th>Type</th>
          <th><input  required type="radio" name="type" value="LINE_SHUTDOWN", id="line_shutdown"> <label for="line_shutdown">Line shutdown</label></th>
          <th><input type="radio" name="type" value="LINE_SLOW_DOWN" id="line_slow_down"> <label for="line_slow_down">Line slow down</label></th>
          <th><input type="radio" name="type" value="ENTIRE_STATION_SHUT_DOWN" id="entire_station_shut_down"><label for="entire_station_shut_down">Whole station shut down</label></th>
          <th><input type="radio" name="type" value="PART_STATION_SHUT_DOWN" id="part_station_shut_down"><label for="part_station_shut_down">One line of the a station shutdown</label></th>
        </tr>
        <tr>
          <td>Line concerned</td>
          <td colspan="2"><input type="text" name="line"></td>
          <td></td>
          <td></td>
        </tr>
        <tr>
          <td>How much time to slow down the line</td>
          <td></td>
          <td><input type="number" min="1" step="any"name="times"></td>
          <td></td>
          <td></td>
        </tr>
        <tr>
          <td>Name of station concerned</td>
          <td></td>
          <td></td>
          <td colspan="2"><input type="text" name="station_name"></td>
        </tr>
        <tr>
          <td>Line of the station concerned</td>
          <td></td>
          <td></td>
          <td></td>
          <td><input type="text" name="station_line" value=""></td>
        </tr>
        <tr>
          <td>Name of the perturbation</td>
          <td colspan="4"><input required type="text" name="name"></td>
        </tr>
      </table>
      <input type="submit">
    </form>
    <br>
    <h2>Statistics</h2>
    <p>To acceed to the statistics of the network, click <a href="/${city}/statistics">here</a> and please wait few seconds.</p>
  </body>
</html>
