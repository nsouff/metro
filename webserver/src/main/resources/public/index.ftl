<!DOCTYPE html>
<html lang="fr" dir="ltr">
  <head>
    <meta charset="utf-8">
    <meta content="IE=edge" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <link rel="stylesheet" href="assets/css/main.css">
    <title>Metro</title>
  </head>
  <body>
    <h1>Welcome on Metro</h1>
    <h2>Itinerary search</h2>
    <form class="form" action="itinerary" method="post" class="form">

      <label for="city">Which city</label>
      <select name="city" id="city">
        ${cities}
      </select>
      <br>
      <label for="start">From</label>
      <input id="start" type="text" name="start" required>
      <br>
      <label for="end">To</label>
      <input id="end" type="text" name="end" required>
      <br>
      <input type="radio" name="type" value="shortest" required id="shortest"> <label for="shortest">Shortest</label><br>
      <input type="radio" name="type" value="leastConnexion" required id="leastConnexion"> <label for="leastConnexion">least connexion</label><br>
      <input type="submit">
    </form>
  </body>
</html>
