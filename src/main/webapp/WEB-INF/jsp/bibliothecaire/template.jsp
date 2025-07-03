<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Bibliothecaire" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Optional" %>
<%
    Bibliothecaire profil = (Bibliothecaire) request.getAttribute("bibliothecaire");
    String contentPage = (String) request.getAttribute("contentPage");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tableau de bord - Administration</title>
    <link rel="stylesheet" href="css/admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <div class="admin-container">
        <nav class="sidebar">
            <div class="sidebar-header">
                <h3><%= profil.getNom() %></h3>
            </div>
            <ul class="nav-links">
                <li class="active">
                    <a href="/admin/dashboard"><i class="fas fa-home"></i> Tableau de bord</a>
                </li>
                <li>
                    <a href="/admin/users"><i class="fas fa-users"></i> Gestion Utilisateurs</a>
                </li>
                <li>
                    <a href="/admin/books"><i class="fas fa-book"></i> Gestion Livres</a>
                </li>
                <li>
                    <a href="/admin/loans"><i class="fas fa-clipboard-list"></i> Emprunts</a>
                </li>
                <li>
                    <a href="/admin/categories"><i class="fas fa-tags"></i> Catégories</a>
                </li>
                <li class="logout">
                    <a href="/logout"><i class="fas fa-sign-out-alt"></i> Déconnexion</a>
                </li>
            </ul>
        </nav>

        <main class="content">
            <jsp:include page="<%= contentPage %>" />
        </main>
    </div>
</body>
</html>
