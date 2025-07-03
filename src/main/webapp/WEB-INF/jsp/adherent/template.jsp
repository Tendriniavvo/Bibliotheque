<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Adherent" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Optional" %>
<%
    Adherent profil = (Adherent) request.getAttribute("profil");
    String contentPage = (String) request.getAttribute("contentPage");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Espace Client - Bibliothèque</title>
    <link rel="stylesheet" href="css/adherent.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <div class="client-container">
        <nav class="sidebar">
            <div class="user-profile">
                <img src="images/default-avatar.png" alt="Photo de profil" class="profile-img">
                <h3>Bienvenue, ${profil.nom}</h3>
            </div>
            <ul class="nav-links">
                <li class="active">
                    <a href="/adherent/dashboard"><i class="fas fa-home"></i>Accueil</a>
                </li>
                <li>
                    <a href="/adherent/catalogue"><i class="fas fa-book"></i>Catalogue</a>
                </li>
                <li>
                    <a href="/adherent/emprunts"><i class="fas fa-list"></i>Mes Emprunts</a>
                </li>
                <li>
                    <a href="/adherent/reservations"><i class="fas fa-bookmark"></i>Mes Réservations</a>
                </li>
                <li>
                    <a href="/adherent/profile"><i class="fas fa-user"></i>Mon Profil</a>
                </li>
                <li class="logout">
                    <a href="/logout"><i class="fas fa-sign-out-alt"></i>Déconnexion</a>
                </li>
            </ul>
        </nav>

        <main class="content">
            
            <jsp:include page="<%= contentPage %>" />

        </main>
    </div>
</body>
</html>
