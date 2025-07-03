<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Système de Gestion des Employés</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <form action="login" method="post" style="max-width: 400px; margin: auto;">
        <div style="margin-bottom: 15px;">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required style="width: 100%; padding: 8px;">
        </div>
        <div style="margin-bottom: 15px;">
            <label for="password">Mot de passe:</label>
            <input type="password" id="password" name="motDePasseHash" required style="width: 100%; padding: 8px;">
        </div>
        <button type="submit" style="width: 100%; padding: 10px; background-color: #4CAF50; color: white; border: none;">Se connecter</button>
        <a href="signup">creer un compte</a>

    </form>
</body>
</html>