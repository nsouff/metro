<!DOCTYPE html>
<html lang="fr" dir="ltr">
  <head>
    <style>
        h1{text-align: center;text-decoration: underline;}
        .form{
          text-align: center;
          border: 2px black double;
        }
    </style>
    <meta charset="utf-8">
    <meta content="IE=edge" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <title>Metro</title>
  </head>
  <body background="fond.jpg">
    <h1>Welcome on Metro</h1>
    <form class="form" action="toCity" method="post" class="form">
      <label for="city">Which city</label>
      <select name="city" id="city">
        ${cities}
      </select>
      <input type="submit">
    </form>
  </body>
</html>
