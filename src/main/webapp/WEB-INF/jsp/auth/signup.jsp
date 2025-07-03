<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.ProfilsAdherent" %>
<%@ page import="java.util.List" %>
<%
    List<ProfilsAdherent> profils = (List<ProfilsAdherent>) request.getAttribute("profils");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Inscription - Bibliothèque</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .form-group { margin-bottom: 15px; }
        .hidden { display: none; }
        form {
            max-width: 500px;
            margin: auto;
            padding: 2em;
            border: 1px solid #ccc;
            border-radius: 8px;
            background-color: #f9f9f9;
        }
        h2 { text-align: center; }
        button { display: block; margin: auto; }
    </style>
</head>
<body>

<form action="/register" method="post">
    <h2>Créer un compte</h2>


    <div class="form-group">
        <label for="profilType">Type d'inscription</label>
        <select id="profilType" name="profilType" required>
            <option value="Client" selected>Client</option>
            <option value="Bibliothecaire">Bibliothecaire</option>
        </select>
    </div>

    <div class="form-group">
        <label for="email">Adresse email</label>
        <input type="email" id="email" name="email" required/>
    </div>

    <div class="form-group">
        <label for="motDePasseHash">Mot de passe</label>
        <input type="password" id="motDePasseHash" name="motDePasseHash" required/>
    </div>


    <div class="form-group">
        <label for="nom">Nom</label>
        <input type="text" id="nom" name="nom" required/>
    </div>

    <div class="form-group">
        <label for="prenom">Prénom</label>
        <input type="text" id="prenom" name="prenom" required/>
    </div>


    <div class="form-group" id="dateNaissanceGroup">
        <label for="dateNaissance">Date de naissance</label>
        <input type="date" id="dateNaissance" name="dateNaissance"/>
    </div>

    <div class="form-group" id="idProfilGroup">
        <label for="idProfil">Profil (en base)</label>
        <select id="idProfil" name="idProfil">
            <option value="">-- Sélectionnez un profil --</option>
            <% if (profils != null) {
                for (ProfilsAdherent profil : profils) { %>
                    <option value="<%= profil.getId() %>"><%= profil.getNomProfil() %></option>
            <% } } %>
        </select>
    </div>

    <button type="submit">S'inscrire</button>

    <div class="form-footer" style="text-align: center; margin-top: 1em;">
        <p>Déjà inscrit ? <a href="/">Se connecter</a></p>
    </div>
</form>

<script>
    const profilTypeSelect = document.getElementById("profilType");
    const dateNaissanceGroup = document.getElementById("dateNaissanceGroup");
    const idProfilGroup = document.getElementById("idProfilGroup");

    function updateFormVisibility() {
        const selectedValue = profilTypeSelect.value.toLowerCase();

        if (selectedValue === "client") {
            dateNaissanceGroup.classList.remove("hidden");
            idProfilGroup.classList.remove("hidden");
        } else if (selectedValue === "bibliothecaire") {
            dateNaissanceGroup.classList.add("hidden");
            idProfilGroup.classList.add("hidden");
        }
    }

    profilTypeSelect.addEventListener("change", updateFormVisibility);

    updateFormVisibility();
</script>

</body>
</html>
