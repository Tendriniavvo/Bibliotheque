<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Espace Adhérent</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background: #f8f8f8; }
        .sidebar {
            width: 220px;
            background: #2c3e50;
            color: #fff;
            height: 100vh;
            position: fixed;
            top: 0; left: 0;
            display: flex;
            flex-direction: column;
            padding-top: 30px;
        }
        .sidebar a {
            color: #fff;
            text-decoration: none;
            padding: 15px 30px;
            display: block;
            transition: background 0.2s;
        }
        .sidebar a:hover, .sidebar a.active {
            background: #34495e;
        }
        .main-content {
            margin-left: 220px;
            padding: 30px;
        }
        .logout {
            margin-top: auto;
            background: #c0392b;
            text-align: center;
        }
        .logout a { color: #fff; }
    </style>
</head>
<body>
    <div class="sidebar">
        <a href="/adherent/dashboard">Tableau de bord</a>
        <a href="/adherent/emprunts">Mes emprunts</a>
        <a href="/adherent/reservations">Mes réservations</a>
        <a href="/adherent/profil">Mon profil</a>
        <div class="logout"><a href="/logout">Déconnexion</a></div>
    </div>
    <div class="main-content">
        <jsp:include page="${contentPage}" />
    </div>
</body>
</html>