<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Adherent" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Optional" %>
<%
    String contentPage = (String) request.getAttribute("contentPage");
    String userName = (String) session.getAttribute("userName");
    if (userName == null) {
        userName = "Utilisateur";
    }
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tableau de Bord - Bibliothécaire</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background-color: #f4f6f9;
            margin: 0;
            padding: 0;
            color: #333;
        }
        .container {
            display: flex;
            min-height: 100vh;
        }
        .sidebar {
            width: 260px;
            background-color: #1a2533;
            color: #fff;
            padding: 20px 15px;
            box-shadow: 3px 0 10px rgba(0, 0, 0, 0.2);
            position: sticky;
            top: 0;
            height: 100vh;
            overflow-y: auto;
        }
        .sidebar h2 {
            font-size: 20px;
            margin: 20px 0 10px;
            padding-left: 10px;
            color: #ecf0f1;
            text-transform: uppercase;
            letter-spacing: 1px;
            border-bottom: 1px solid #34495e;
            padding-bottom: 5px;
        }
        .sidebar a {
            display: flex;
            align-items: center;
            color: #ecf0f1;
            text-decoration: none;
            padding: 12px 15px;
            margin: 5px 0;
            border-radius: 6px;
            transition: all 0.3s ease;
            font-size: 16px;
        }
        .sidebar a i {
            margin-right: 10px;
            width: 20px;
            text-align: center;
        }
        .sidebar a:hover {
            background-color: #3498db;
            transform: translateX(5px);
        }
        .sidebar a.active {
            background-color: #2980b9;
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
            background-color: #2c3e50;
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
        .add-button {
            display: inline-block;
            padding: 10px 20px;
            background-color: #3498db;
            color: #fff;
            text-decoration: none;
            border-radius: 5px;
            margin-bottom: 20px;
            transition: background-color 0.3s;
        }
        .add-button:hover {
            background-color: #2980b9;
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
            background-color: #3498db;
            color: #fff;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        .action-buttons {
            display: flex;
            gap: 10px;
        }
        .action-buttons button {
            padding: 8px 12px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        .edit-button {
            background-color: #f39c12;
            color: #fff;
        }
        .edit-button:hover {
            background-color: #e67e22;
        }
        .delete-button {
            background-color: #e74c3c;
            color: #fff;
        }
        .delete-button:hover {
            background-color: #c0392b;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input[type="text"],
        input[type="number"],
        input[type="date"],
        input[type="datetime-local"],
        select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-sizing: border-box;
            margin-bottom: 10px;
        }
        button {
            display: inline-block;
            padding: 10px 20px;
            background-color: #3498db;
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        button:hover {
            background-color: #2980b9;
        }
        .back-button {
            background-color: #7f8c8d;
            margin-left: 10px;
        }
        .back-button:hover {
            background-color: #6c7a89;
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
            .action-buttons {
                flex-direction: column;
                gap: 5px;
            }
            .message {
            padding: 10px;
            margin-bottom: 20px;
            border-radius: 5px;
            font-size: 14px;
            text-align: center;
            }
            .message.success {
                background-color: #d4edda;
                color: #155724;
                border: 1px solid #c3e6cb;
            }
            .message.error {
                background-color: #f8d7da;
                color: #721c24;
                border: 1px solid #f5c6cb;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="sidebar">
            <h2><i class="fas fa-book"></i> Bibliothèque</h2>
            <a href="/abonnement/liste" class="<%= contentPage.contains("abonnement") ? "active" : "" %>"><i class="fas fa-user-check"></i> Abonnement</a>
            <a href="/penalite/liste" class="<%= contentPage.contains("penalite") ? "active" : "" %>"><i class="fas fa-exclamation-circle"></i> Pénalité</a>

            <h2><i class="fas fa-books"></i> Livre</h2>
            <a href="/auteur/liste" class="<%= contentPage.contains("auteur") ? "active" : "" %>"><i class="fas fa-user"></i> Auteur</a>
            <a href="/editeur/liste" class="<%= contentPage.contains("editeur") ? "active" : "" %>"><i class="fas fa-print"></i> Éditeur</a>
            <a href="/livre/liste" class="<%= contentPage.contains("livre") ? "active" : "" %>"><i class="fas fa-book-open"></i> Livre</a>
            <a href="/exemplaire/liste" class="<%= contentPage.contains("exemplaire") ? "active" : "" %>"><i class="fas fa-copy"></i> Exemplaire</a>

            <h2><i class="fas fa-handshake"></i> Prêt</h2>
            <a href="/emprunt/liste" class="<%= contentPage.contains("emprunt") ? "active" : "" %>"><i class="fas fa-book-reader"></i> Emprunt</a>
            <a href="/prolongement/liste" class="<%= contentPage.contains("prolongement") ? "active" : "" %>"><i class="fas fa-clock"></i> Prolongement</a>
            <a href="/reservation/liste" class="<%= contentPage.contains("reservation") ? "active" : "" %>"><i class="fas fa-calendar-check"></i> Réservation</a>
        </div>
        <div class="content">
            <div class="navbar">
                <div class="user-info">
                    <i class="fas fa-user"></i>
                    Bienvenue, <%= userName %>
                </div>
                <a href="/logout" class="logout">Déconnexion</a>
            </div>
            <jsp:include page="<%= contentPage %>" />
        </div>
    </div>
</body>
</html>