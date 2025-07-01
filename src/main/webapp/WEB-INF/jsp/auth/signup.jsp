<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscription - Bibliothèque</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <%@ page import="com.bibliotheque.entities.ProfilsAdherent" %>
    <%@ page import="java.util.List" %>
    <% List<ProfilsAdherent> profils = (List<ProfilsAdherent>) request.getAttribute("profils"); %>

    <form action="/register" method="post" style="max-width: 400px; margin: auto;">
        <h2>Créer un compte</h2>
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" required>
        </div>
        <div class="form-group">
            <label for="motDePasseHash">Mot de passe</label>
            <input type="password" id="motDePasseHash" name="motDePasseHash" required>
        </div>
        <div class="form-group">
            <label for="nom">Nom</label>
            <input type="text" id="nom" name="nom" required>
        </div>
        <div class="form-group">
            <label for="prenom">Prénom</label>
            <input type="text" id="prenom" name="prenom" required>
        </div>
        <div class="form-group">
            <label for="dateNaissance">Date de naissance</label>
            <input type="date" id="dateNaissance" name="dateNaissance" required>
        </div>
        <div class="form-group">
            <label for="idProfil">Profil</label>
            <select id="idProfil" name="idProfil" required>
                <option value="">-- Sélectionnez un profil --</option>
                <% if (profils != null) {
                    for (ProfilsAdherent profil : profils) { %>
                        <option value="<%= profil.getId() %>"><%= profil.getNomProfil() %></option>
                    <% }
                } %>
            </select>
        </div>
        <button type="submit">S'inscrire</button>
        <div class="form-footer">
            <p>Déjà inscrit? <a href="/">Se connecter</a></p>
        </div>
    </form>
</body>
</html>
