<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Livre" %>
<%@ page import="java.util.List" %>
<%
    String userName = (String) session.getAttribute("userName");
    if (userName == null) {
        userName = "Client";
    }
    List<Livre> livres = (List<Livre>) request.getAttribute("livres");
    if (livres == null) {
        livres = new java.util.ArrayList<>();
    }
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Portail Client - Bibliothèque</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background-color: #e9ecef;
            margin: 0;
            padding: 0;
            color: #333;
        }
        .container {
            display: flex;
            min-height: 100vh;
        }
        .sidebar {
            width: 220px;
            background-color: #34495e;
            color: #fff;
            padding: 20px 15px;
            box-shadow: 3px 0 10px rgba(0, 0, 0, 0.15);
            position: sticky;
            top: 0;
            height: 100vh;
            overflow-y: auto;
        }
        .sidebar h2 {
            font-size: 18px;
            margin: 20px 0 10px;
            padding-left: 10px;
            color: #ecf0f1;
            text-transform: uppercase;
            letter-spacing: 1px;
            border-bottom: 1px solid #4a6278;
            padding-bottom: 5px;
        }
        .sidebar a {
            display: flex;
            align-items: center;
            color: #ecf0f1;
            text-decoration: none;
            padding: 10px 15px;
            margin: 5px 0;
            border-radius: 6px;
            transition: all 0.3s ease;
            font-size: 15px;
        }
        .sidebar a i {
            margin-right: 10px;
            width: 20px;
            text-align: center;
        }
        .sidebar a:hover {
            background-color: #1abc9c;
            transform: translateX(5px);
        }
        .sidebar a.active {
            background-color: #16a085;
            font-weight: bold;
        }
        .content {
            flex: 1;
            padding: 20px;
            background: #fff;
            border-radius: 8px;
            margin: 20px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        .navbar {
            background-color: #34495e;
            color: #fff;
            padding: 15px 20px;
            border-radius: 6px 6px 0 0;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        .navbar .user-info {
            font-size: 16px;
            display: flex;
            align-items: center;
        }
        .navbar .user-info i {
            margin-right: 8px;
        }
        .navbar .logout {
            color: #ecf0f1;
            text-decoration: none;
            padding: 8px 15px;
            border-radius: 5px;
            transition: background-color 0.3s;
        }
        .navbar .logout:hover {
            background-color: #e74c3c;
        }
        h1 {
            color: #2c3e50;
            text-align: center;
            margin-bottom: 20px;
        }
        .search-bar {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
        }
        .search-bar input[type="text"] {
            flex: 1;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
        }
        .search-bar button {
            padding: 10px 20px;
            background-color: #1abc9c;
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        .search-bar button:hover {
            background-color: #16a085;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #1abc9c;
            color: #fff;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        .action-buttons button {
            padding: 8px 12px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            background-color: #1abc9c;
            color: #fff;
            transition: background-color 0.3s;
        }
        .action-buttons button:hover {
            background-color: #16a085;
        }
        @media (max-width: 768px) {
            .container {
                flex-direction: column;
            }
            .sidebar {
                width: 100%;
                height: auto;
                position: static;
                padding: 15px;
            }
            .content {
                margin: 10px;
            }
            .navbar {
                flex-direction: column;
                gap: 10px;
            }
            th, td {
                font-size: 14px;
                padding: 8px;
            }
            .search-bar {
                flex-direction: column;
            }
            .search-bar input[type="text"] {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="sidebar">
            <h2><i class="fas fa-book"></i> Bibliothèque</h2>
            <a href="/client/livres" class="<%= contentPage.contains("livres") ? "active" : "" %>"><i class="fas fa-book-open"></i> Livres Disponibles</a>
            <a href="/client/emprunts" class="<%= contentPage.contains("emprunts") ? "active" : "" %>"><i class="fas fa-book-reader"></i> Mes Emprunts</a>
            <a href="/client/reservations" class="<%= contentPage.contains("reservations") ? "active" : "" %>"><i class="fas fa-calendar-check"></i> Mes Réservations</a>
            <a href="/client/profil" class="<%= contentPage.contains("profil") ? "active" : "" %>"><i class="fas fa-user"></i> Mon Profil</a>
        </div>
        <div class="content">
            <div class="navbar">
                <div class="user-info">
                    <i class="fas fa-user"></i>
                    Bienvenue, <%= userName %>
                </div>
                <a href="/logout" class="logout">Déconnexion</a>
            </div>
            <h1>Livres Disponibles</h1>
            <div class="search-bar">
                <input type="text" placeholder="Rechercher un livre par titre ou auteur..." name="search">
                <button type="submit"><i class="fas fa-search"></i> Rechercher</button>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>Titre</th>
                        <th>Auteur</th>
                        <th>Année</th>
                        <th>ISBN</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Livre livre : livres) { %>
                    <tr>
                        <td><%= livre.getTitre() %></td>
                        <td><%= livre.getAuteur() %></td>
                        <td><%= livre.getAnneePublication() %></td>
                        <td><%= livre.getIsbn() %></td>
                        <td class="action-buttons">
                            <button onclick="location.href='/client/reserver?id=<%= livre.getId() %>'">Réserver</button>
                        </td>
                    </tr>
                    <% } %>
                    <% if (livres.isEmpty()) { %>
                    <tr>
                        <td colspan="5">Aucun livre disponible pour le moment.</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>